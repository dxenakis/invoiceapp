package com.invoiceapp.payment;

import com.invoiceapp.companyscope.RequireTenant;
import com.invoiceapp.payment.enums.MyDataPaymentMethod;
import com.invoiceapp.payment.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@RequireTenant
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repo;

    public PaymentMethodServiceImpl(PaymentMethodRepository repo) {
        this.repo = repo;
    }

    @Override
    public PaymentMethodResponse create(PaymentMethodCreateRequest req) {
        if (req.code() == null || req.code().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
        if (req.description() == null || req.description().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description is required");
        if (req.mydataMethodCode() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mydataMethodCode is required");

        repo.findByCode(req.code().trim()).ifPresent(x -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code already exists: " + req.code());
        });

        var e = new PaymentMethod();
        e.setCode(req.code().trim());
        e.setDescription(req.description().trim());
        e.setMydataMethod(MyDataPaymentMethod.fromCode(req.mydataMethodCode()));
        e.setActive(req.active() == null ? true : req.active());

        return toDto(repo.save(e));
    }

    @Override
    public PaymentMethodResponse update(Long id, PaymentMethodUpdateRequest req) {
        var e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentMethod not found: " + id));

        if (req.description() != null) e.setDescription(req.description().trim());
        if (req.mydataMethodCode() != null) e.setMydataMethod(MyDataPaymentMethod.fromCode(req.mydataMethodCode()));
        if (req.active() != null) e.setActive(req.active());


        return toDto(e);
    }

    @Override
    public void delete(Long id) {
        var e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentMethod not found: " + id));
        repo.delete(e);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodResponse get(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentMethod not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodResponse> list(Pageable pageable, Boolean activeOnly) {
        return (Boolean.TRUE.equals(activeOnly) ? repo.findAllByActiveTrue(pageable) : repo.findAll(pageable))
                .map(this::toDto);
    }

    @Override
    public void seedDefaultsIfEmpty() {
        if (repo.count() > 0) return;

        List<PaymentMethod> defaults = List.of(
                build("CASH", "Μετρητά", MyDataPaymentMethod.CASH, 1),
                build("POS", "POS / e-POS", MyDataPaymentMethod.POS_EPOS, 2),
                build("CREDIT", "Επί πιστώσει", MyDataPaymentMethod.ON_CREDIT, 3),
                build("WEB-BANK", "Web Banking", MyDataPaymentMethod.WEB_BANKING, 4),
                build("CHEQUE", "Επιταγή", MyDataPaymentMethod.CHEQUE, 5),
                build("IRIS", "IRIS", MyDataPaymentMethod.IRIS_INSTANT_PAYMENTS, 6),
                build("BANK-DOM", "Επαγ. Λογ. Ημεδαπής", MyDataPaymentMethod.PRO_BUSINESS_ACCOUNT_DOMESTIC, 7),
                build("BANK-FOR", "Επαγ. Λογ. Αλλοδαπής", MyDataPaymentMethod.PRO_BUSINESS_ACCOUNT_FOREIGN, 8)
                );
        defaults.forEach(repo::save);
    }

    private PaymentMethod build(String code, String desc, MyDataPaymentMethod m, int order) {
        var e = new PaymentMethod();
        e.setCode(code);
        e.setDescription(desc);
        e.setMydataMethod(m);
        e.setActive(true);
        return e;
    }

    private PaymentMethodResponse toDto(PaymentMethod e) {
        return new PaymentMethodResponse(
                e.getId(),
                e.getCompanyId(),
                e.getCode(),
                e.getDescription(),
                e.getMydataMethod() != null ? e.getMydataMethod().getCode() : null,
                e.isActive()

        );
    }
}
