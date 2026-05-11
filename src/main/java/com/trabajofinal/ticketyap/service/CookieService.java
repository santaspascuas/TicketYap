package com.trabajofinal.ticketyap.service;


import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    @Value("${app.cookie.secure:false}")
    private boolean secureCookie;

    public void addHttpOnlyCookie(String name, String value, int maxAge,HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from(name, value)
        .maxAge(Duration.ofSeconds(maxAge))
            .httpOnly(true)
            .secure(secureCookie)
            .sameSite("Lax")
            .path("/")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(String name, HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from(name, null)
        .maxAge(0)
        .httpOnly(true)
        .secure(true)
        .sameSite("None")
        .path("/")
        .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }



}
