package com.lingchen.livora.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtils {

    @Value("${app.useSecureCookie}")
    private boolean useSecureCookie;

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAge) {
        if (response == null) {
            throw new IllegalArgumentException("HttpServletResponse cannot be null");
        }
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (maxAge / 1000));
        refreshTokenCookie.setSecure(useSecureCookie);
        String sameSite = useSecureCookie ? "None" : "Lax";
        this.setResponseHeader(response, refreshTokenCookie, sameSite);
    }

    private void setResponseHeader(HttpServletResponse response, Cookie refreshTokenCookie, String sameSite) {
        String cookieHeader = refreshTokenCookie.getName() + "=" +
                refreshTokenCookie.getValue() +
                "; HttpOnly; Path=" + refreshTokenCookie.getPath() +
                "; Max-Age=" + refreshTokenCookie.getMaxAge() +
                (useSecureCookie ? "; Secure" : "") +
                "; SameSite=" + sameSite;
        response.setHeader("Set-Cookie", cookieHeader);
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void logCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("Cookies: " + (cookies != null ? Arrays.toString(cookies) : "null"));
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Cookie name: " + cookie.getName() + ", value: " + cookie.getValue());
            }
        }
    }
}
