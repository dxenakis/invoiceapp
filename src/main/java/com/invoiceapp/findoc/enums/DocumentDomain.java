package com.invoiceapp.findoc.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DocumentDomain {
    PURCHASES(1251, "Αγορές"),   // Αγορές
    SALES(1351, "Πωλήσεις"),       // Πωλήσεις
   // ITEDOC(1351, "Αποθήκη"),       // Πωλήσεις
    COLLECTIONS(1381, "Εισπράξεις"),    // Εισπράξεις
    PAYMENTS(1281, "Πληρωμές") ;   // Πληρωμές
    private final int code;
    private final String labelEl;

    DocumentDomain(int code, String labelEl) {
        this.code = code;
        this.labelEl = labelEl;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    private static final Map<Integer, DocumentDomain> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(DocumentDomain::getCode, e -> e));

    public static DocumentDomain fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown Sign code: " + code);
        return e;
    }
}

