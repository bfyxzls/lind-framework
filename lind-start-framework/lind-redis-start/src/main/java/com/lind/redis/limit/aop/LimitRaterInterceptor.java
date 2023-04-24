package com.lind.redis.limit.aop;

import cn.hutool.core.util.StrUtil;
import com.lind.redis.limit.annotation.RateLimiter;
import com.lind.redis.limit.config.LimitProperties;
import com.lind.redis.limit.execption.RedisLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * redis令牌桶限流. 通过WebMvcConfigurer来注册ReqInterceptor.
 */
@Slf4j
public class LimitRaterInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 限流标识.
	 */
	static String LIMIT_ALL = "LIMIT_ALL";

	@Autowired
	private LimitProperties limitProperties;

	@Autowired
	private RedisRaterLimiter redisRaterLimiter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 全局限流
		if (limitProperties != null && limitProperties.getEnabled()) {
			String token2 = redisRaterLimiter.acquireToken(LIMIT_ALL, limitProperties.getLimit(),
					limitProperties.getTimeout());
			if (StrUtil.isBlank(token2)) {
				throw new RedisLimitException("当前访问总人数太多啦，请稍后再试");
			}
		}

		// 方法限流
		try {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
			if (rateLimiter != null) {
				int limit = rateLimiter.limit();
				int timeout = rateLimiter.timeout();
				String token3 = redisRaterLimiter.acquireToken(method.getName(), limit, timeout);
				if (StrUtil.isBlank(token3)) {
					throw new RedisLimitException("当前访问人数太多啦，请稍后再试");
				}
			}
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return true;
	}

}
