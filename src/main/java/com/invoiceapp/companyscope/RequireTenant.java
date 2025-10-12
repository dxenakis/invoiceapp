package com.invoiceapp.companyscope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireTenant {
    // Optional: τι status να επιστρέφουμε αν λείπει tenant
    int httpStatus() default 401; // 401 Unauthorized by default (ή 400/422 αν θες)
}
