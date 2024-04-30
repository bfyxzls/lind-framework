package com.lind.uaa.simple.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 权限信息
 *
 * @author ruoyi
 */
public class PermissionContextHolder {

	private static final String PERMISSION_CONTEXT_ATTRIBUTES = "PERMISSION_CONTEXT";

	public static String getContext() {
		return toStr(RequestContextHolder.currentRequestAttributes().getAttribute(PERMISSION_CONTEXT_ATTRIBUTES,
				RequestAttributes.SCOPE_REQUEST));
	}

	public static void setContext(String permission) {
		RequestContextHolder.currentRequestAttributes().setAttribute(PERMISSION_CONTEXT_ATTRIBUTES, permission,
				RequestAttributes.SCOPE_REQUEST);
	}

	public static String toStr(Object value) {
		return toStr(value, null);
	}

	public static String toStr(Object value, String defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}

}
