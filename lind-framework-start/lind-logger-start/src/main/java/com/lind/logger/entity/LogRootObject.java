package com.lind.logger.entity;

import com.lind.common.util.MapUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Map;

@Getter
@AllArgsConstructor
public class LogRootObject {

	private static final InheritableThreadLocal<Map<String, Object>> VAR_MAP = new InheritableThreadLocal<>();

	/**
	 * Target method
	 */
	private final Method method;

	/**
	 * Method parameters
	 */
	private final Object[] args;

	/**
	 * Type information for the target class
	 */
	private final Class<?> targetClass;

	/**
	 * 设置日志上下文参数
	 * @param key 参数key
	 * @param obj 参数值
	 */
	public static void putVar(String key, Object obj) {
		if (VAR_MAP.get() == null) {
			VAR_MAP.set(MapUtils.<String, Object>hashMapBuilder(8).put(key, obj).build());
		}
		else {
			VAR_MAP.get().put(key, obj);
		}
	}

	/**
	 * 获取日志上下文参数
	 * @param key 参数key
	 * @return 参数值
	 */
	public static Object getVar(String key) {
		if (VAR_MAP.get() == null) {
			return null;
		}
		return VAR_MAP.get().get(key);
	}

	/**
	 * 清空线程上下文值
	 */
	public static void clear() {
		VAR_MAP.remove();
	}

}
