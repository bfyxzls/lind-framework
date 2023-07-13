/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.common.core.util;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * cookies工具类，通过与客户端浏览器交互，cookie操作才有意义，同时支持新老版浏览器samesite属性.
 */
public class CookieUtils {

	public static final String LEGACY_COOKIE = "_LEGACY";

	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	/**
	 * Set a response cookie. This solely exists because JAX-RS 1.1 does not support
	 * setting HttpOnly cookies
	 * @param name
	 * @param value
	 * @param path
	 * @param domain
	 * @param maxAge
	 * @param secure
	 * @param httpOnly
	 * @param sameSite
	 */
	public static void addCookie(String name, String value, String path, String domain, int maxAge, boolean secure,
			boolean httpOnly, SameSiteCookies sameSite) {

		// when expiring a cookie we shouldn't set the sameSite attribute; if we set e.g.
		// SameSite=None when expiring a cookie, the new cookie (with maxAge == 0)
		// might be rejected by the browser in some cases resulting in leaving the
		// original cookie untouched; that can even prevent user from accessing their
		// application
		if (maxAge == 0) {
			sameSite = null;
		}

		boolean secure_sameSite = sameSite == SameSiteCookies.NONE || secure; // when
																				// SameSite=None,
																				// Secure
																				// attribute
																				// must be
																				// set
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		HttpServletResponse response = servletRequestAttributes.getResponse();

		ResponseCookie cookie = ResponseCookie.from(name, value).httpOnly(httpOnly).domain(domain).path(path)
				.maxAge(Duration.ofSeconds(maxAge)).build();
		if (sameSite != null) {
			cookie = ResponseCookie.from(name, value).httpOnly(httpOnly).secure(secure_sameSite).domain(domain)
					.path(path).maxAge(Duration.ofSeconds(maxAge)).sameSite(sameSite.name()).build();
		}

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		// 老版浏览器
		if (sameSite == SameSiteCookies.NONE) {
			addCookie(name + LEGACY_COOKIE, value, path, domain, maxAge, secure, httpOnly, null);
		}
	}

	/**
	 * Set a response cookie avoiding SameSite parameter
	 * @param name
	 * @param value
	 * @param path
	 * @param domain
	 * @param maxAge
	 * @param secure
	 * @param httpOnly
	 */
	public static void addCookie(String name, String value, String path, String domain, int maxAge, boolean secure,
			boolean httpOnly) {
		addCookie(name, value, path, domain, maxAge, secure, httpOnly, null);
	}

	public static void addCookie(String name, String value) {
		addCookie(name, value, null, null, -1, false, true, null);
	}

	public static void addCookie(String name, String value, boolean secure, SameSiteCookies sameSiteCookies) {
		addCookie(name, value, null, null, -1, secure, true, sameSiteCookies);
	}

	public static Cookie getCookie(String name) {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();

		HttpServletRequest request = servletRequestAttributes.getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 过期cookie，相当于删除.
	 * @param cookieName
	 * @param path
	 * @param domain
	 * @param httpOnly
	 * @param sameSite
	 */
	public static void expireCookie(String cookieName, String path, String domain, boolean secureOnly, boolean httpOnly,
			SameSiteCookies sameSite) {
		addCookie(cookieName, "", path, domain, 0, secureOnly, httpOnly, sameSite);
	}

}
