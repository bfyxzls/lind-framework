package com.lind.logger;

import com.lind.common.core.util.MapUtils;

import java.util.Map;

public class ThreadTest {

	private static final InheritableThreadLocal<Map<String, Object>> VAR_MAP = new InheritableThreadLocal<>();

	private static final ThreadLocal<String> VAR_MAP2 = new ThreadLocal<>();

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

	public static void main(String[] args) {
		putVar("hello", "你好");
		VAR_MAP2.set("good");
		Thread thread = new Thread(() -> {
			System.out.println(getVar("hello")); // 你好
			System.out.println(VAR_MAP2.get()); // null
		});
		thread.start();
	}

	static class newThread implements Runnable {

		@Override
		public void run() {
			putVar("hello", "你好");
			VAR_MAP2.set("good");
			new Runnable() {
				@Override
				public void run() {
					System.out.println(getVar("hello")); // 你好
					System.out.println(VAR_MAP2.get()); // good

				}
			}.run();
		}

	}

}
