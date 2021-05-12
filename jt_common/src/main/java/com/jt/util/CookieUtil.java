package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void addCookie(String cookieName, String cookieValue, String domain, Integer maxAge, HttpServletResponse response){
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 根据Cookie的名称 获取Cookie对象
     */
    public static Cookie getCookie(String cookieName,HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies !=null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(String cookieName,HttpServletRequest request){
        Cookie cookie = getCookie(cookieName,request);

        return cookie ==null ? null :cookie.getValue();
    }

    public static void deleteCookie(HttpServletResponse response,String cookieName,String domain){
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
