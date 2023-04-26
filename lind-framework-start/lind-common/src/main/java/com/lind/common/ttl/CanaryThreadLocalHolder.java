package com.lind.common.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 外层为threadLocal需要使用CanaryThreadLocalHolder这个对象. 写入CanaryThreadLocalHolder.put()
 * 读取CanaryThreadLocalHolder.getHeaders()
 */
public class CanaryThreadLocalHolder {

	private static final TransmittableThreadLocal<Map<String, List<String>>> HEADERS = TransmittableThreadLocal
			.withInitial(() -> new ConcurrentHashMap<>(8));

	public static Map<String, List<String>> getHeaders() {
		return HEADERS.get();
	}

	public static Map<String, List<String>> getHeadersAndRemove() {
		final Map<String, List<String>> headers = HEADERS.get();
		remove();
		return headers;
	}

	public static void put(String key, List<String> values) {
		HEADERS.get().put(key, values);
	}

	public static void remove() {
		HEADERS.remove();
	}

}
