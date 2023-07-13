package com.lind.common.core.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UrlUtils {

	private static PathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 从url中提取参数，并进行url编码.
	 * @param needValid
	 * @return
	 */
	public static String getUrlEncodeParams(String needValid) {
		if (needValid.indexOf("?") > 1) {
			String param = needValid.substring(needValid.indexOf("?"));
			String[] paramList = param.split("&");
			List<String> paramEncode = new ArrayList<>();
			for (String item : paramList) {
				String[] valAttr = item.split("=");
				if (valAttr.length > 1) {
					try {
						paramEncode.add(valAttr[0] + "=" + URLEncoder.encode(valAttr[1], "UTF-8"));
					}
					catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			return needValid.substring(0, needValid.indexOf("?")) + String.join("&", paramEncode);
		}
		return needValid;
	}

	public static boolean match(String patternPath, String requestPath) {
		if (StringUtils.isEmpty(patternPath) || StringUtils.isEmpty(requestPath)) {
			return false;
		}
		return pathMatcher.match(patternPath, requestPath);
	}

	/**
	 * 格式化url.
	 * @param url
	 * @return
	 */
	public static URI formatUrl(String url) {
		return URI.create(removeUrlSpaceParams(url));
	}

	/**
	 * 去掉非法字符
	 * @param needValid
	 * @return
	 */
	public static String removeUrlSpaceParams(String needValid) {
		needValid = needValid.replaceAll(" ", "%20");
		needValid = needValid.replaceAll("<", "%3C");
		needValid = needValid.replaceAll(">", "%3E");
		needValid = needValid.replaceAll("\\{", "%7B");
		needValid = needValid.replaceAll("}", "%7D");
		needValid = needValid.replaceAll("\"", "%20");
		return needValid;
	}

}
