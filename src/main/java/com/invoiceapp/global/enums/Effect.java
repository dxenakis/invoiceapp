package com.invoiceapp.global.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Effect {
    INCREASE(1,"Αυξάνει"),    // αυξάνει
    DECREASE(-1,"Μειώνει"),    // μειώνει
    NEUTRAL(0,"Αδιάφορο");   // αδιάφορο
    private final int code;
    private final String labelEl;

    Effect(int code, String labelEl) { this.code = code; this.labelEl = labelEl; }
    public int getCode() { return code; }
    public String getLabelEl() { return labelEl; }

    private static final Map<Integer, Effect> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(Effect::getCode, e -> e));

    public static Effect fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown Sign code: " + code);
        return e;
    }

}
