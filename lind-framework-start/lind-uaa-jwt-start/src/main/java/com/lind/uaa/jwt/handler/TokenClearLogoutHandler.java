package com.lind.uaa.jwt.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.common.util.IpInfoUtil;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.config.JwtAuthenticationToken;
import com.lind.uaa.jwt.event.LogoutSuccessEvent;
import com.lind.uaa.jwt.service.JwtUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户主动退出，清token事件.
 */
@Slf4j
public class TokenClearLogoutHandler implements LogoutHandler {

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	RedisService redisService;

	@Autowired
	IpInfoUtil ipInfoUtil;

	private JwtUserService jwtUserService;

	public TokenClearLogoutHandler(JwtUserService jwtUserService) {
		this.jwtUserService = jwtUserService;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		clearToken(authentication, request);
	}

	protected void clearToken(Authentication authentication, HttpServletRequest request) {
		log.info("logout tokenClearLogoutHandler");

		if (authentication == null)
			return;
		UserDetails user = (UserDetails) authentication.getPrincipal();
		if (user != null && user.getUsername() != null) {
			// 清除jwt token的策略.
			DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
			redisService.del(Constants.ONLINE_USER + jwt.getToken());

			applicationEventPublisher.publishEvent(new LogoutSuccessEvent(user, ipInfoUtil.getIpAddr(request)));
		}
	}

}