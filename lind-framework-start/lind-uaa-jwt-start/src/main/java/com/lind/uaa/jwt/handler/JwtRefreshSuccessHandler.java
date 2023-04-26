package com.lind.uaa.jwt.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.config.JwtAuthenticationToken;
import com.lind.uaa.jwt.config.JwtConfig;
import com.lind.uaa.jwt.entity.ResourceUser;
import com.lind.uaa.jwt.service.JwtUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 自动刷新token，通过token访问资源时，当达到某个时间，会在Response响应头中输出Authorization,前端需要根据这个值来刷新本地存储的token.
 * IssuedAt是生成token的当前时间，然后与RefreshToken的时间做对比，达到了，需要从新获取新token 注意RefreshToken
 * expire的值需要小于token expire的值
 */
public class JwtRefreshSuccessHandler implements AuthenticationSuccessHandler {

	private static Logger logger = LoggerFactory.getLogger(JwtRefreshSuccessHandler.class);

	@Autowired
	JwtConfig jwtConfig;

	@Autowired
	RedisService redisService;

	private JwtUserService jwtUserService;

	public JwtRefreshSuccessHandler(JwtUserService jwtUserService) {
		this.jwtUserService = jwtUserService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
		boolean shouldRefresh = shouldTokenRefresh(jwt.getIssuedAt());
		if (shouldRefresh) {
			// 刷新后的新token在线状态
			logger.info("JwtRefreshSuccessHandler");
			ResourceUser userDetails = (ResourceUser) authentication.getPrincipal();
			String newToken = jwtUserService.generateJwtJoinUser(userDetails);
			redisService.expire(Constants.ONLINE_USER + newToken, jwtConfig.getExpiresAt() * 60);
			response.setHeader("Authorization", newToken);
		}
	}

	protected boolean shouldTokenRefresh(Date issueAt) {
		long tokenRefreshInterval = jwtConfig.getRefreshTokenExpiresAt();
		LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
		return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
	}

}
