package com.lind.uaa.jwt.config;

import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.filter.MyUsernamePasswordAuthenticationFilter;
import com.lind.uaa.jwt.handler.HttpStatusLoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

@RequiredArgsConstructor
public class JsonLoginConfigurer<T extends JsonLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
		extends AbstractHttpConfigurer<T, B> {

	private final RedisService redisService;

	private final JwtConfig jwtConfig;

	private MyUsernamePasswordAuthenticationFilter authFilter = new MyUsernamePasswordAuthenticationFilter();

	@Override
	public void configure(B http) throws Exception {
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		authFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler(redisService, jwtConfig));
		authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

		MyUsernamePasswordAuthenticationFilter filter = postProcess(authFilter);
		http.addFilterAfter(filter, LogoutFilter.class);
	}

	public JsonLoginConfigurer<T, B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler) {
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		return this;
	}

}