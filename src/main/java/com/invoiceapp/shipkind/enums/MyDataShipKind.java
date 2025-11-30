package com.invoiceapp.shipkind.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MyDataShipKind {

    // Κωδικός - Περιγραφή (σύμφωνα με αυτά που έγραψες)
    SALE(1, "Πώληση"),
    SALE_FOR_THIRD_PARTY(2, "Πώληση για Λογαριασμό Τρίτων"),
    SAMPLING(3, "Δειγματισμός"),
    EXHIBITION(4, "Έκθεση"),
    RETURN(5, "Επιστροφή"),
    CUSTODY(6, "Φύλαξη"),
    PROCESSING_ASSEMBLY(7, "Επεξεργασία Συναρμολόγηση"),
    BETWEEN_ENTITY_FACILITIES(8, "Μεταξύ Εγκαταστάσεων Οντότητας"),
    PURCHASE(9, "Αγορά"),
    SHIP_AIRCRAFT_SUPPLY(10, "Εφοδιασμός πλοίων και αεροσκαφών"),
    FREE_DISPOSAL(11, "Δωρεάν διάθεση"),
    GUARANTEE(12, "Εγγύηση"),
    LENDING(13, "Χρησιδανεισμός"),
    STORAGE_TO_THIRD_PARTY(14, "Αποθήκευση σε Τρίτους"),
    RETURN_FROM_CUSTODY(15, "Επιστροφή από Φύλαξη");

    private final int code;
    private final String labelEl;

    MyDataShipKind(int code, String labelEl) {
        this.code = code;
        this.labelEl = labelEl;
    }

    public int getCode() {
        return code;
    }

    public String getLabelEl() {
        return labelEl;
    }

    // map για γρήγορο lookup by code
    private static final Map<Integer, MyDataShipKind> BY_CODE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(MyDataShipKind::getCode, e -> e));

    public static MyDataShipKind fromCode(Integer code) {
        var e = BY_CODE.get(code);
        if (e == null) {
            throw new IllegalArgumentException("Unknown MyDataShipKind code: " + code);
        }
        return e;
    }
}
