package com.lind.redis.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class KeyExpiredEventMessageListener implements MessageListener {

	private final RedisTemplate redisTemplate;

	public KeyExpiredEventMessageListener(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();

		String[] keys = expiredKey.split("\\:");
		if (expiredKey.length() == 2) {
			// 处理键过期事件逻辑
			log.debug("Key expired:{}", expiredKey);
			redisTemplate.opsForHash().delete(keys[0], keys[1]);
		}
	}

}
