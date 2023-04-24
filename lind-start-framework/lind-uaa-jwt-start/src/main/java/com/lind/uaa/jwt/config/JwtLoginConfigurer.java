package com.lind.uaa.jwt.config;

import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.filter.JwtAuthenticationFilter;
import com.lind.uaa.jwt.handler.HttpStatusLoginFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 登陆配置器.
 *
 * @param <T>
 * @param <B>
 */
@RequiredArgsConstructor
public class JwtLoginConfigurer<T extends JwtLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>>
		extends AbstractHttpConfigurer<T, B> {

	private final RedisService redisService;

	private final JwtConfig jwtConfig;

	private JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter();

	@Override
	public void configure(B http) throws Exception {
		// 设置Filter使用的AuthenticationManager,这里取公共的即可
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		// 设置失败的Handler
		authFilter.setAuthenticationFailureHandler(new HttpStatusLoginFailureHandler(redisService, jwtConfig));
		// 不将认证后的context放入session
		JwtAuthenticationFilter filter = postProcess(authFilter);
		// 指定Filter的位置
		http.addFilterBefore(filter, LogoutFilter.class);
	}

	public JwtLoginConfigurer<T, B> permissiveRequestUrls(String... urls) {
		authFilter.setPermissiveUrl(urls);
		return this;
	}

	public JwtLoginConfigurer<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler) {
		authFilter.setAuthenticationSuccessHandler(successHandler);
		return this;
	}

}