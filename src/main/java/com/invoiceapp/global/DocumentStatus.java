package com.invoiceapp.global;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DocumentStatus {
    DRAFT(1,"Draft"),
    POSTED(2,"Καταχωρημένο"),
    CANCELLED(3,"Ακυρωμένο");
    private final int code;
    private final String labelEl;

    DocumentStatus(int code, String labelEl) {
        this.code = code;
        this.labelEl = labelEl;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    private static final Map<Integer, DocumentStatus> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(DocumentStatus::getCode, e -> e));

    public static DocumentStatus fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown Status code: " + code);
        return e;
    }
    }


