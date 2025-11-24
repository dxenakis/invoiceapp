// src/main/java/com/invoiceapp/global/MyDataPaymentMethod.java
package com.invoiceapp.payment.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MyDataPaymentMethod {

    CASH(1, "Μετρητά"),
    POS_EPOS(2, "POS / e-POS"),
    ON_CREDIT(3, "Επί πιστώσει"),
    WEB_BANKING(4, "Web Banking"),
    CHEQUE(5, "Επιταγή"),
    IRIS_INSTANT_PAYMENTS(6, "Άμεσες Πληρωμές IRIS"),
    PRO_BUSINESS_ACCOUNT_DOMESTIC(7, "Επαγ. Λογαριασμός Πληρωμών Ημεδαπής"),
    PRO_BUSINESS_ACCOUNT_FOREIGN(8, "Επαγ. Λογαριασμός Πληρωμών Αλλοδαπής");
    private final int code;
    private final String labelEl;


    MyDataPaymentMethod(int code, String labelEl) { this.code = code; this.labelEl = labelEl; }
    public int getCode() { return code; }
    public String getLabelEl() { return labelEl; }

    private static final Map<Integer, MyDataPaymentMethod> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toMap(MyDataPaymentMethod::getCode, e -> e));

    public static MyDataPaymentMethod fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown MyDataPaymentMethod code: " + code);
        return e;
    }
}
