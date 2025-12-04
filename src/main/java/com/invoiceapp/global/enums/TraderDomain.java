package com.invoiceapp.global.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TraderDomain {

    CUSTOMER(13, "Πελάτης"),   // Πελάτης
    SUPPLIER(12, "Προμηθευτής");     // Προμηθευτής
    private final int code;
    private final String labelEl;

    TraderDomain(int code, String labelEl) {
        this.code = code;
        this.labelEl = labelEl;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    private static final Map<Integer, TraderDomain> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(TraderDomain::getCode, e -> e));

    public static TraderDomain fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown Sign code: " + code);
        return e;
    }


}




