package com.invoiceapp.item;


import com.invoiceapp.companyscope.RequireTenant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@Service
@Transactional
@RequireTenant // Εξασφαλίζει ότι υπάρχει active tenant (companyId) σε ΟΛΕΣ τις μεθόδους του service
public class ItemServiceImpl implements ItemService {


    private final ItemRepository repo;


    public ItemServiceImpl(ItemRepository repo) {
        this.repo = repo;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Item> listAll() {
// Το Hibernate εφαρμόζει αυτόματα το tenant filter (μέσω @TenantId στο Item)
        return repo.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Item getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }


    @Override
    public Item create(Item incoming) {
// Μην πειράζεις companyId εδώ· με @TenantId θα συμπληρωθεί από το Session.
// Αν το entity σου ΔΕΝ έχει @TenantId, πρόσθεσέ το εκεί (π.χ. private Long companyId με @TenantId).
        return repo.save(incoming);
    }


    @Override
    public Item update(Long id, Item patch) {
        Item existing = getById(id); // already tenant-filtered


        return existing;
    }

    @Override
    public void delete(Long id){
        repo.deleteById(id);
    };
}