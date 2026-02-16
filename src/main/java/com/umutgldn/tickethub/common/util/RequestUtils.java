package com.umutgldn.tickethub.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//utility class final olmalı . private cons yapıyorum ki gereksiz newlenmesin
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtils {

    public static String extractDeviceInfo(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
    public static String extractIpAddress(HttpServletRequest request) {
        String forwarded= request.getHeader("X-Forwarded-For");
        if(forwarded!=null && !forwarded.isBlank()){
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
