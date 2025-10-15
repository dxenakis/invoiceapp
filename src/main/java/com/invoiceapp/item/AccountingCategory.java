package com.invoiceapp.item;

public enum AccountingCategory {
    EMPOREVMATA("Εμπορεύματα"),
    PROIONTA("Προϊόντα"),
    YPHRESIES("Υπηρεσίες"),
    PROTES_YLES("Πρώτες Ύλες"),
    VOITHITIKA_YLIKA("Βοηθητικά Υλικά"),
    YLIKA_SYSKEVASIAS("Υλικά Συσκευασίας"),
    PAGIA("Πάγια");

    private final String Name;

    AccountingCategory(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }
}