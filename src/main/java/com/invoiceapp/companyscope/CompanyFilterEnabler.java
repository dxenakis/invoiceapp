package com.invoiceapp.companyscope;

import com.invoiceapp.securityconfig.SecurityUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Ενεργοποιεί το Hibernate filter "companyFilter" με το company_id από το JWT.
 * Αφορά μόνο τα entities που κάνουν extends CompanyScopedEntity.
 * Προσοχή: με open-in-view=false, προτίμησε το Aspect παρακάτω.
 */
@Component
public class CompanyFilterEnabler extends OncePerRequestFilter {

    private final EntityManagerFactory emf;

    public CompanyFilterEnabler(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        Long cid = SecurityUtils.getActiveCompanyId();
        if (cid != null) {
            SessionFactory sf = emf.unwrap(SessionFactory.class);
            Session session = sf.getCurrentSession(); // απαιτεί open-in-view ή @Transactional
            session.enableFilter("companyFilter").setParameter("cid", cid);
        }

        chain.doFilter(req, res);
    }
}
