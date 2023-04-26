package com.lind.uaa.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 操作oauth_client_details数据到redis，操作怎么把oauth_client_details数据读出来由子类实现.
 */
@Slf4j
public abstract class AbstractRedisClientDetailsService extends JdbcClientDetailsService {

	/**
	 * 缓存client的redis key，这里是hash结构存储
	 */
	protected static final String CACHE_CLIENT_KEY = "client_details";

	/**
	 * redis对象,子类也使用这个对象.
	 */
	protected RedisTemplate<String, String> stringRedisTemplate;

	@Autowired
	ObjectMapper objectMapper;

	public AbstractRedisClientDetailsService(DataSource dataSource, StringRedisTemplate stringRedisTemplate) {
		super(dataSource);
		this.stringRedisTemplate = stringRedisTemplate;
		loadAllClientToCache();
	}

	@SneakyThrows
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
		ClientDetails clientDetails = null;

		// 先从redis获取
		String value = (String) stringRedisTemplate.boundHashOps(CACHE_CLIENT_KEY).get(clientId);
		if (StringUtils.isBlank(value)) {
			clientDetails = cacheAndGetClient(clientId);
		}
		else {
			clientDetails = objectMapper.readValue(value, BaseClientDetails.class);
		}

		return clientDetails;
	}

	/**
	 * 缓存client并返回client
	 * @param clientId
	 */
	@SneakyThrows
	private ClientDetails cacheAndGetClient(String clientId) {
		// 从数据库读取
		ClientDetails clientDetails = super.loadClientByClientId(clientId);
		if (clientDetails != null) {// 写入redis缓存
			stringRedisTemplate.boundHashOps(CACHE_CLIENT_KEY).put(clientId,
					objectMapper.writeValueAsString(clientDetails));
			log.info("缓存clientId:{},{}", clientId, clientDetails);
		}

		return clientDetails;
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		super.updateClientDetails(clientDetails);
		cacheAndGetClient(clientDetails.getClientId());
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		super.updateClientSecret(clientId, secret);
		cacheAndGetClient(clientId);
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		super.removeClientDetails(clientId);
		removeRedisCache(clientId);
	}

	/**
	 * 删除redis缓存
	 * @param clientId
	 */
	private void removeRedisCache(String clientId) {
		stringRedisTemplate.boundHashOps(CACHE_CLIENT_KEY).delete(clientId);
	}

	/**
	 * 将oauth_client_details全表刷入redis
	 */
	protected abstract void loadAllClientToCache();

}
