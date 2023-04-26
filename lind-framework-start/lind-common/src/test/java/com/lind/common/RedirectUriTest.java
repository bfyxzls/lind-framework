package com.lind.common;

import cn.hutool.core.lang.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedirectUriTest {

	private static boolean matchesRedirects(Set<String> validRedirects, String redirect) {
		for (String validRedirect : validRedirects) {
			if (validRedirect.endsWith("*") && !validRedirect.contains("?")) {
				// strip off the query component - we don't check them when wildcards are
				// effective
				String r = redirect.contains("?") ? redirect.substring(0, redirect.indexOf("?")) : redirect;
				// strip off *
				int length = validRedirect.length() - 1;
				validRedirect = validRedirect.substring(0, length);
				if (r.startsWith(validRedirect))
					return true;
				// strip off trailing '/'
				if (length - 1 > 0 && validRedirect.charAt(length - 1) == '/')
					length--;
				validRedirect = validRedirect.substring(0, length);
				if (validRedirect.equals(r))
					return true;
			}
			else if (validRedirect.equals(redirect))
				return true;
		}
		return false;
	}

	@Test
	public void valid() {
		String needValid = "https://www.pkulaw.com/chl/d9bd70327e25ee4bbdfb.html?keyword=中国";
		Set<String> uris = new HashSet<>();
		uris.add("*");
		Assert.isTrue(matchesRedirects(uris, needValid));
	}

	@Test(expected = IllegalArgumentException.class)
	public void errorUri() {
		String needValid = "https://www.pkulaw.com/chl/d9bd70327e25ee4bbdfb.html?keyword=中 国";
		URI uri = URI.create(needValid);
		String redirectUri = uri.normalize().toString();
		System.out.println(redirectUri);
	}

	@Test(expected = IllegalArgumentException.class)
	public void errorUriMenuEncode() throws UnsupportedEncodingException {
		String needValid = "https://www.pkulaw.com/chl/d9bd70327e25ee4bbdfb.html?keyword=中 国&des=人 民";
		if (needValid.indexOf("?") > 1) {
			String url = needValid.substring(0, needValid.indexOf("?"));
			String param = needValid.substring(needValid.indexOf("?"));
			String[] paramList = param.split("&");
			List<String> paramEncode = new ArrayList<>();
			for (String item : paramList) {
				String[] valAttr = item.split("=");
				if (valAttr.length > 1) {
					paramEncode.add(valAttr[0] + "=" + URLEncoder.encode(valAttr[1], "UTF-8"));
				}
			}
			needValid = url + String.join("&", paramEncode);
		}
		URI uri = URI.create(needValid);
		String redirectUri = uri.normalize().toString();
		System.out.println(redirectUri);
	}

	@Test()
	public void errorUriEncode() {
		String needValid = "https://www.pkulaw.com/chl/d9bd70327e25ee4bbdfb.html?keyword=中%20国";
		URI uri = URI.create(needValid);
		String redirectUri = uri.normalize().toString();
		System.out.println(redirectUri);
	}

}
