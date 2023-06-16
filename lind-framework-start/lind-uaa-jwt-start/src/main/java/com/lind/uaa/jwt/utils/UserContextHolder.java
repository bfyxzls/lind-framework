package com.lind.uaa.jwt.utils;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.lind.uaa.jwt.config.Constants;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户id、用户名称、Token等信息 注意： 必须在网关通过请求头的方法传入，同时在HeaderInterceptor拦截器设置值。 否则这里无法获取
 *
 * 每次请求时，会从header头中拿到token并存储到UserContextHolder中，当次请求有效.
 *
 */
public class UserContextHolder {

	// TransmittableThreadLocal 是 Alibaba 开发的 ThreadLocal 扩展库，可以跨线程传递数据。类似于
	// ThreadLocal，每个线程都有自己独立的 TransmittableThreadLocal 值，可以通过 get() 和 set()
	// 方法访问和修改这些值。不同的是，TransmittableThreadLocal 可以在线程池（如
	// ForkJoinPool、ThreadPoolExecutor）中复用线程时，将当前的值自动传递给新线程，从而避免值的丢失或遗漏。
	private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

	public static void set(String key, Object value) {
		Map<String, Object> map = getLocalMap();
		map.put(key, value == null ? StringUtils.EMPTY : value);
	}

	public static String get(String key) {
		Map<String, Object> map = getLocalMap();
		return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
	}

	public static <T> T get(String key, Class<T> clazz) {
		Map<String, Object> map = getLocalMap();
		return (T) (map.getOrDefault(key, null));
	}

	public static Map<String, Object> getLocalMap() {
		Map<String, Object> map = THREAD_LOCAL.get();
		if (map == null) {
			map = new ConcurrentHashMap<String, Object>();
			THREAD_LOCAL.set(map);
		}
		return map;
	}

	public static void setLocalMap(Map<String, Object> threadLocalMap) {
		THREAD_LOCAL.set(threadLocalMap);
	}

	public static Long getUserId() {
		return Convert.toLong(get(Constants.DETAILS_USER_ID), 0L);
	}

	public static void setUserId(String account) {
		set(Constants.DETAILS_USER_ID, account);
	}

	public static String getUserName() {
		return get(Constants.DETAILS_USERNAME);
	}

	public static void setUserName(String username) {
		set(Constants.DETAILS_USERNAME, username);
	}

	@SneakyThrows
	public static UserDetails getUser() {
		return get(Constants.DETAILS_USER, UserDetails.class);
	}

	public static void setUser(UserDetails user) {
		set(Constants.DETAILS_USER, user);
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}

}
