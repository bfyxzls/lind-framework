package com.lind.uaa.jwt.handler;

import com.alibaba.fastjson.JSONObject;
import com.lind.common.rest.CommonResult;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.lind.uaa.jwt.config.Constants.LOGIN_FAIL_LIMIT;
import static com.lind.uaa.jwt.config.Constants.LOGIN_FAIL_LOCK_LIMIT;

@RequiredArgsConstructor
public class HttpStatusLoginFailureHandler implements AuthenticationFailureHandler {

	private final RedisService redisService;

	private final JwtConfig jwtConfig;

	public static final String BAD_CREDENTIALS = "bad credentials";

	public static final String USER_NOT_FOUND = "userdetailsservice returned null, which is an interface contract violation";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpStatus.OK.value());
		response.setContentType("application/json;charset=utf-8");
		if (!exception.getMessage().toLowerCase().equals(BAD_CREDENTIALS)
				&& !exception.getMessage().toLowerCase().equals(USER_NOT_FOUND)) {
			// token超时
			response.getWriter().print(JSONObject.toJSONString(CommonResult.unauthorizedFailure("登录超时!")));
		}
		else {// 用户密码错误
			Object username = request.getAttribute("username");
			String key = LOGIN_FAIL_LIMIT + username;
			String lockKey = LOGIN_FAIL_LOCK_LIMIT + username;
			if (!redisService.hasKey(key)) {
				redisService.set(key, 0, jwtConfig.getFailLimitTime() * 60);
			}
			if (Integer.valueOf(redisService.get(key).toString()) >= jwtConfig.getFailLimit()) {
				redisService.set(lockKey, 0, jwtConfig.getFailLockTime() * 60);
			}
			redisService.incr(key, 1L);
			response.getWriter().print(JSONObject.toJSONString(CommonResult.unauthorizedFailure("用户名或密码错误!")));
		}
	}

}
