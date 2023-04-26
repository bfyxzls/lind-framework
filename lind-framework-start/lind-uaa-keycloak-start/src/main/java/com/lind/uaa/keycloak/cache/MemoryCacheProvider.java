package com.lind.uaa.keycloak.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lind
 * @date 2023/3/10 14:20
 * @since 1.0.0
 */
@ConditionalOnMissingBean(CacheProvider.class)
@Component
public class MemoryCacheProvider implements CacheProvider {

	static ConcurrentHashMap<String, String> dic = new ConcurrentHashMap<>();

	@Override
	public String get(String key) {
		return dic.get(key);
	}

	@Override
	public void delete(String key) {
		dic.remove(key);
	}

	@Override
	public void add(String key, String value) {
		dic.replace(key, value);
	}

	@Override
	public Boolean hasKey(String key) {
		return dic.containsKey(key);
	}

}
