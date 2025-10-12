package com.invoiceapp.companyscope;

import com.invoiceapp.exception.MissingTenantException;

public final class TenantContext {
    private static final ThreadLocal<Long> CURRENT = new ThreadLocal<>();

    private TenantContext() {}

    public static void set(Long companyId) { CURRENT.set(companyId); }
    public static Long get() { return CURRENT.get(); }
    public static void clear() { CURRENT.remove(); }

    public static Long require() {
        Long id = get();
        if (id == null) throw new MissingTenantException("No active company/tenant");
        return id;
    }
}
