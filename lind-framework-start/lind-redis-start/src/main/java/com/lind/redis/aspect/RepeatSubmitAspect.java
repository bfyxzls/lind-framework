package com.lind.redis.aspect;

import com.lind.redis.annotation.RepeatSubmit;
import com.lind.redis.lock.UserIdAuditorAware;
import com.lind.redis.lock.exception.RepeatSubmitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class RepeatSubmitAspect implements ApplicationContextAware {

	/**
	 * 拦截器执行顺序.
	 *
	 * @around before
	 * @before
	 * @around after
	 * @after
	 */
	private final RedisTemplate<String, String> redisTemplate;

	ApplicationContext applicationContext;

	@Around("@annotation(repeatSubmit)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, RepeatSubmit repeatSubmit) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		notNull(attributes, "attributes is null");
		HttpServletRequest request = attributes.getRequest();
		UserIdAuditorAware userIdAuditorAware = applicationContext.getBean(UserIdAuditorAware.class);
		String currUser = "system";
		if (userIdAuditorAware != null) {
			currUser = userIdAuditorAware.getCurrentAuditor().orElse("system");
		}
		String key = repeatSubmit.redisKey() + ":" + currUser + ":"
				+ DigestUtils.md5DigestAsHex(request.getServletPath().getBytes("UTF-8"));
		// 如果缓存中有这个url视为重复提交
		Object hasSubmit = redisTemplate.opsForValue().get(key);
		if (Objects.isNull(hasSubmit)) {
			// redis单行命令，保证原子性
			redisTemplate.opsForValue().set(key, request.getServletPath(), repeatSubmit.expireTime(), TimeUnit.SECONDS);
			Object o = proceedingJoinPoint.proceed();
			return o;
		}
		else {
			String message = String.format("重复提交url:%s", request.getServletPath());
			log.warn(message);
			throw new RepeatSubmitException(message);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
