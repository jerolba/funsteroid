package com.otogami.web.controller;

import javax.servlet.http.Cookie;

public class CookieHelper {

	public static Cookie getCookie(Cookie[] cookies,String name){
		if (cookies!=null){
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)){
					return cookie;
				}
			}
		}
		return null;
	}
	
	public static String getCookieValue(Cookie[] cookies,String name){
		Cookie cookie=getCookie(cookies,name);
		if (cookie!=null){
			return cookie.getValue();
		}
		return null;
	}


}
