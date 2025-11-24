package com.invoiceapp.mtrunit.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MyDataMtrUnitCode {

    // Κωδικός - Περιγραφή
    PIECES(1, "Τεμάχια"),
    KILOGRAMS(2, "Κιλά"),
    LITERS(3, "Λίτρα"),
    METERS(4, "Μέτρα"),
    SQUARE_METERS(5, "Τετραγωνικά Μέτρα"),
    CUBIC_METERS(6, "Κυβικά Μέτρα"),
    PIECES_OTHER(7, "Τεμάχια_Λοιπές Περιπτώσεις");

    private final int code;
    private final String labelEl;

    MyDataMtrUnitCode(int code, String labelEl) {
        this.code = code;
        this.labelEl = labelEl;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    private static final Map<Integer, MyDataMtrUnitCode> BY_CODE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(MyDataMtrUnitCode::getCode, e -> e));

    public static MyDataMtrUnitCode fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) {
            throw new IllegalArgumentException("Unknown MyDataMtrUnitCode: " + code);
        }
        return e;
    }
}
