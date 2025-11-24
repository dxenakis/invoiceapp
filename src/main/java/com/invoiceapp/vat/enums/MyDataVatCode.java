package com.invoiceapp.vat.enums;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MyDataVatCode {

    // ------------------------------
    // ΚΩΔΙΚΟΙ ΟΠΩΣ ΤΟΥΣ ΔΙΝΕΙ ΤΟ myDATA
    // ------------------------------

    VAT_24(1, "ΦΠΑ συντελεστής 24%", 24),
    VAT_13(2, "ΦΠΑ συντελεστής 13%", 13),
    VAT_6(3, "ΦΠΑ συντελεστής 6%", 6),
    VAT_17(4, "ΦΠΑ συντελεστής 17%", 17),
    VAT_9(5, "ΦΠΑ συντελεστής 9%", 9),
    VAT_4(6, "ΦΠΑ συντελεστής 4%", 4),

    WITHOUT_VAT(7, "Άνευ Φ.Π.Α.", 0),

    WITHOUT_VAT_EXPENSES(8,
            "Εγγραφές χωρίς ΦΠΑ (π.χ. Μισθοδοσία, Αποσβέσεις)",
            null
    ),

    VAT_3_NEW(9,
            "ΦΠΑ συντελεστής 3% (αρ. 31 ν.5057/2023)",
            3
    ),

    VAT_4_NEW(10,
            "ΦΠΑ συντελεστής 4% (αρ. 31 ν.5057/2023)",
            4
    );

    private final int code;
    private final String labelEl;
    private final Integer percent; // μπορεί να είναι null όταν δεν έχει % (π.χ. κωδ. 8)

    MyDataVatCode(int code, String labelEl, Integer percent) {
        this.code = code;
        this.labelEl = labelEl;
        this.percent = percent;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    public Integer getPercent() {
        return percent;
    }

    // Για γρήγορη αναζήτηση
    private static final Map<Integer, MyDataVatCode> BY_CODE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(MyDataVatCode::getCode, e -> e));

    public static MyDataVatCode fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null)
            throw new IllegalArgumentException("Unknown MyDataVatCode: " + code);
        return e;
    }
}
