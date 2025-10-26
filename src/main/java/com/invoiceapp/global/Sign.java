package com.invoiceapp.global;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/** Πρόσημο εμφάνισης για εκτυπώσεις/λίστες. */
public enum Sign {
    NEGATIVE(-1, "Αρνητικό"),
    NEUTRAL(0, "Αδιάφορο"),
    POSITIVE(1, "Θετικό");
    private final int code;
    private final String labelEl;

    Sign(int code, String labelEl) { this.code = code; this.labelEl = labelEl; }
    public int getCode() { return code; }
    public String getLabelEl() { return labelEl; }

    private static final Map<Integer, Sign> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(Sign::getCode, e -> e));

    public static Sign fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown Sign code: " + code);
        return e;
    }
}
