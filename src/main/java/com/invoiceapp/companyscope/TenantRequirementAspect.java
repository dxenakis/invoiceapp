package com.invoiceapp.companyscope;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(0) // εκτέλεσέ το πριν από @Transactional για να κόβει άμεσα
public class TenantRequirementAspect {

    @Before("@within(com.invoiceapp.companyscope.RequireTenant) || "
            + "@annotation(com.invoiceapp.companyscope.RequireTenant)")
    public void ensureTenant(JoinPoint jp) {
        // 1) Αν υπάρχει method-level annotation, χρησιμοποίησέ το (ώστε να τιμήσεις httpStatus αν αλλάξει)
        RequireTenant ann = findRequireTenantAnnotation(jp);
        // 2) Θα πετάξει MissingTenantException αν λείπει
        TenantContext.require();
        // (προαιρετικά μπορείς να καταγράψεις tenantId εδώ)
    }

    private RequireTenant findRequireTenantAnnotation(JoinPoint jp) {
        // Προσπάθησε να βρεις method-level annotation, αλλιώς class-level
        try {
            Method method = jp.getTarget().getClass()
                    .getMethod(jp.getSignature().getName(),
                            ((org.aspectj.lang.reflect.MethodSignature) jp.getSignature()).getParameterTypes());
            RequireTenant m = AnnotationUtils.findAnnotation(method, RequireTenant.class);
            if (m != null) return m;
        } catch (NoSuchMethodException ignored) {}
        return AnnotationUtils.findAnnotation(jp.getTarget().getClass(), RequireTenant.class);
    }
}
