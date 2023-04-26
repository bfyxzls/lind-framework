package com.lind.uaa.keycloak.cache;

/**
 * @author lind
 * @date 2023/3/10 14:15
 * @since 1.0.0
 */
public interface CacheProvider {

	String get(String key);

	void delete(String key);

	void add(String key, String value);

	Boolean hasKey(String key);

}
