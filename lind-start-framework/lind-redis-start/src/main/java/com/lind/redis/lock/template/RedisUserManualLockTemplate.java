package com.lind.redis.lock.template;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.lind.redis.lock.config.RedisLockProperty;
import com.lind.redis.lock.exception.RedisUserManualLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.Assert.notNull;

/**
 * 基于redis实现的当前用户手动锁. 功能：根据当前用户去锁定某个资源,之后手动释放,否则超时释放.
 */
@Slf4j
@RequiredArgsConstructor
public class RedisUserManualLockTemplate {

	private final RedisTemplate<String, String> redisTemplate;

	private final RedisLockProperty redisLockProperty;

	private final RedisLockTemplate redisLockTemplate;

	@Autowired(required = false)
	UserIdAuditorAware auditorAware;

	/**
	 * 执行手动锁.
	 */
	public Object execute(String sourceId, Integer timeout, TimeUnit unit) {
		notNull(sourceId, "sourceId不能为空");
		String key = getKey(sourceId);
		String user = auditorAware.getCurrentAuditor().orElse(null);
		notNull(user, "AuditorAware实例返回的值为空");
		return redisLockTemplate.execute(sourceId, timeout, unit, new Callback() {
			@Override
			public Object onGetLock() {
				onGetLockValidate(key, user);
				redisTemplate.opsForValue().set(key, user, timeout, unit);
				log.info("获取锁成功，需要检查锁定列表里是否是自己的资源.");
				return true; // 成功获取
			}

			@Override
			public Object onTimeout() {
				log.info("没拿到锁，需要判断自己是否已经拿到了资源.");
				return onTimeoutValidate(key, user);
			}
		});
	}

	/**
	 * 当获取锁成功之后,判断是否为自己的资源,如果不是自己的则失败.
	 */
	void onGetLockValidate(String key, String user) {
		Boolean isExist = redisTemplate.hasKey(key);
		if (isExist != null && isExist) {
			String currentValue = redisTemplate.opsForValue().get(key);
			if (StrUtil.isBlank(currentValue) || !currentValue.equals(user)) {
				Long expire = redisTemplate.getExpire(key);
				notNull(expire, "key is null");
				throw new RedisUserManualLockException(String.format("记录正被用户%s锁定,将在%s秒后释放", currentValue, expire));
			}
		}
	}

	/**
	 * 当获取锁失败之后,判断这个资源是否在自己的锁里,如果是则成功.
	 */
	Object onTimeoutValidate(String key, String user) {
		String currentValue = "";
		Boolean isExist = redisTemplate.hasKey(key);
		if (isExist != null && isExist) {
			currentValue = redisTemplate.opsForValue().get(key);
			if (StrUtil.isNotBlank(currentValue) && currentValue.equals(user)) {
				return true; // 成功获取
			}
		}
		Long expire = redisTemplate.getExpire(key);
		notNull(expire, "key is null");
		throw new RedisUserManualLockException(String.format("记录正被用户%s锁定,将在%s秒后释放", currentValue, expire));
	}

	/**
	 * 得到key.
	 */
	String getKey(@NotNull String lockId) {
		String key = redisLockProperty.getManualLockKey() + ":" + lockId;
		return key;
	}

	/**
	 * 手动取消锁.
	 */
	public void unLock(String lockId) {
		redisTemplate.expire(getKey(lockId), 0, TimeUnit.SECONDS);
	}

	/**
	 * 得是被锁定的资源列表.
	 */
	public Set<Map<String, String>> getSourceList() {
		String manualLockKey = redisLockProperty.getManualLockKey();
		Set<String> keys = redisTemplate.keys(manualLockKey + "*");
		notNull(keys, "keys is null");

		Set<Map<String, String>> maps = new HashSet<>();
		for (String key : keys) {
			String val = redisTemplate.opsForValue().get(key);
			notNull(val, "val is null");
			maps.add(MapUtil.of(new Pair[] { Pair.of("sourceId", key), Pair.of("userId", val) }));

		}
		return maps;
	}

	/**
	 * 查看锁定资源的用户.
	 */
	public String getUserBySourceId(String sourceId) {
		return redisTemplate.opsForValue().get(getKey(sourceId));
	}

}
