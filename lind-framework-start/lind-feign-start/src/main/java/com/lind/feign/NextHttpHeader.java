package com.lind.feign;

import com.lind.common.core.util.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author lind
 * @date 2023/2/23 14:35
 * @since 1.0.0
 */
public class NextHttpHeader {

	static final InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal<>();

	public static void set(String key, String val) {
		if (inheritableThreadLocal.get() == null) {
			inheritableThreadLocal.set(MapUtils.<String, String>hashMapBuilder(8).put(key, val).build());
		}
		else {
			inheritableThreadLocal.get().put(key, val);
		}
	}

	public static String get(String key) {
		if (inheritableThreadLocal.get() == null) {
			return null;
		}
		return inheritableThreadLocal.get().get(key);
	}

	public static Set<String> get() {
		if (inheritableThreadLocal.get() == null) {
			return null;
		}
		return inheritableThreadLocal.get().keySet();
	}

	public void clear() {
		Map<String, String> map = inheritableThreadLocal.get();
		if (map != null) {
			map.clear();
			inheritableThreadLocal.remove();
		}
	}

	public static Map<String, String> getCopyOfContextMap() {

		Map<String, String> oldMap = inheritableThreadLocal.get();
		if (oldMap != null) {
			return new HashMap<String, String>(oldMap);
		}
		else {
			return null;
		}
	}

	public static void setContextMap(Map<String, String> contextMap) {

		inheritableThreadLocal.set(new HashMap<String, String>(contextMap));
	}

}
