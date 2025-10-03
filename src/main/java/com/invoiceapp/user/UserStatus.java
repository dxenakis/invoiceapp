package com.invoiceapp.user;

public enum UserStatus {
    ACTIVE,            // μπορεί να κάνει login & να χρησιμοποιεί το σύστημα
    PENDING_APPROVAL,  // περιμένει έγκριση (π.χ. αν δεν έγινε gov verification)
    SUSPENDED,         // προσωρινά απενεργοποιημένος από admin
    DELETED            // λογαριασμός που έχει διαγραφεί/απενεργοποιηθεί οριστικά
}