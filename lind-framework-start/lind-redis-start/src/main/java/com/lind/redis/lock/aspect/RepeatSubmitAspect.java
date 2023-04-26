package com.lind.redis.lock.aspect;

import com.lind.redis.lock.annotation.RepeatSubmit;
import com.lind.redis.lock.exception.RepeatSubmitException;
import com.lind.redis.lock.template.UserIdAuditorAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.Assert.notNull;

/**
 * URL重复提交拦截器.
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class RepeatSubmitAspect {

	/**
	 * 拦截器执行顺序.
	 *
	 * @around before
	 * @before
	 * @around after
	 * @after
	 */
	private final RedisTemplate<String, String> redisTemplate;

	private final UserIdAuditorAware userIdAuditorAware;

	@Around("@annotation(repeatSubmit)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, RepeatSubmit repeatSubmit) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		notNull(attributes, "attributes is null");
		HttpServletRequest request = attributes.getRequest();
		String key = repeatSubmit.redisKey() + ":" + userIdAuditorAware.getCurrentAuditor().orElse("system") + ":"
				+ DigestUtils.md5DigestAsHex(request.getServletPath().getBytes("UTF-8"));
		// 如果缓存中有这个url视为重复提交
		Object hasSubmit = redisTemplate.opsForValue().get(key);
		if (Objects.isNull(hasSubmit)) {
			redisTemplate.opsForValue().set(key, request.getServletPath());
			redisTemplate.expire(key, repeatSubmit.expireTime(), TimeUnit.SECONDS);
			Object o = proceedingJoinPoint.proceed();
			return o;
		}
		else {
			String message = String.format("重复提交url:%s", request.getServletPath());
			log.warn(message);
			throw new RepeatSubmitException(message);
		}
	}

}
