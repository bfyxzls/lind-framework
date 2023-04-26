package com.lind.common.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lind
 * @date 2023/1/30 11:48
 * @since 1.0.0
 */
public class TtlMdc {

	private static final int WRITE_OPERATION = 1;

	private static final int READ_OPERATION = 2;

	private static TtlMdc ttlMdc;

	static {
		ttlMdc = new TtlMdc();
	}

	final ThreadLocal<Map<String, String>> copyOnInheritThreadLocal = new TransmittableThreadLocal<>();

	final ThreadLocal<Integer> lastOperation = new ThreadLocal<>();

	public static TtlMdc getInstance() {
		return ttlMdc;
	}

	private boolean wasLastOpReadOrNull(Integer lastOp) {
		return lastOp == null || lastOp == READ_OPERATION;
	}

	private Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
		Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
		if (oldMap != null) {
			// we don't want the parent thread modifying oldMap while we are
			// iterating over it
			synchronized (oldMap) {
				newMap.putAll(oldMap);
			}
		}

		copyOnInheritThreadLocal.set(newMap);
		return newMap;
	}

	public void put(String key, String val) {
		if (key == null) {
			throw new IllegalArgumentException("key cannot be null");
		}

		Map<String, String> oldMap = copyOnInheritThreadLocal.get();
		Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

		if (wasLastOpReadOrNull(lastOp) || oldMap == null) {
			Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
			newMap.put(key, val);
		}
		else {
			oldMap.put(key, val);
		}
	}

	private Integer getAndSetLastOperation(int op) {
		Integer lastOp = lastOperation.get();
		lastOperation.set(op);
		return lastOp;
	}

	/**
	 * Get the current thread's MDC as a map. This method is intended to be used
	 * internally.
	 */
	private Map<String, String> getPropertyMap() {
		lastOperation.set(READ_OPERATION);
		return copyOnInheritThreadLocal.get();
	}

	public String get(String key) {
		Map<String, String> map = getPropertyMap();
		if ((map != null) && (key != null)) {
			return map.get(key);
		}
		else {
			return null;
		}
	}

	public void remove(String key) {
		if (key == null) {
			return;
		}
		Map<String, String> oldMap = copyOnInheritThreadLocal.get();
		if (oldMap == null) {
			return;
		}

		Integer lastOp = getAndSetLastOperation(WRITE_OPERATION);

		if (wasLastOpReadOrNull(lastOp)) {
			Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
			newMap.remove(key);
		}
		else {
			oldMap.remove(key);
		}

	}

}
