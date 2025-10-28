package com.invoiceapp.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public final class CookieUtils {
    private CookieUtils() {}

    public static void setRefreshCookie(HttpServletResponse res, String value) {
        ResponseCookie cookie = ResponseCookie.from("refresh", value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")     // "None" αν UI/API είναι cross-site
                .path("/auth")
                .maxAge(60L * 60L * 24L * 30L) // 30d
                .build();
        res.addHeader("Set-Cookie", cookie.toString());
    }

    public static void clearRefreshCookie(HttpServletResponse res) {
        ResponseCookie cookie = ResponseCookie.from("refresh", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(0)
                .build();
        res.addHeader("Set-Cookie", cookie.toString());
    }
}
