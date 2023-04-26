package com.lind.uaa.jwt.config;

import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.filter.OptionsRequestFilter;
import com.lind.uaa.jwt.handler.CustomAccessDeineHandler;
import com.lind.uaa.jwt.handler.CustomAuthenticationEntryPoint;
import com.lind.uaa.jwt.handler.JsonLoginSuccessHandler;
import com.lind.uaa.jwt.handler.JwtRefreshSuccessHandler;
import com.lind.uaa.jwt.handler.TokenClearLogoutHandler;
import com.lind.uaa.jwt.service.JwtAuthenticationProvider;
import com.lind.uaa.jwt.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtUserService jwtUserService;

	@Autowired
	JwtConfig jwtConfig;

	@Autowired
	RedisService redisService;

	protected void configure(HttpSecurity http) throws Exception {
		String[] urls = PermitAllUrl.permitAllUrl(jwtConfig.getPermitAll());
		http.authorizeRequests().antMatchers(urls).permitAll().and().csrf().disable().formLogin().disable()
				.sessionManagement().disable().cors().and().headers()
				.addHeaderWriter(new StaticHeadersWriter(Arrays.asList(new Header("Access-control-Allow-Origin", "*"),
						new Header("Access-Control-Expose-Headers", "Authorization"))))
				.and().addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)
				.apply(new JsonLoginConfigurer<>(redisService, jwtConfig))
				.loginSuccessHandler(jsonLoginSuccessHandler()).and()
				.apply(new JwtLoginConfigurer<>(redisService, jwtConfig))
				.tokenValidSuccessHandler(jwtRefreshSuccessHandler()).permissiveRequestUrls("/logout").and().logout()
				.logoutUrl("/logout") // 默认就是"/logout"
				.addLogoutHandler(tokenClearLogoutHandler())
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).and().sessionManagement()
				.disable();

		// 401和403自定义
		http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomAccessDeineHandler());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
	}

	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean("jwtAuthenticationProvider")
	protected AuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(jwtUserService);
	}

	@Bean("daoAuthenticationProvider")
	protected AuthenticationProvider daoAuthenticationProvider() throws Exception {
		// 这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService());
		daoProvider.setPasswordEncoder(passwordEncoder());
		return daoProvider;
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	protected JsonLoginSuccessHandler jsonLoginSuccessHandler() {
		return new JsonLoginSuccessHandler(jwtUserService);
	}

	@Bean
	protected JwtRefreshSuccessHandler jwtRefreshSuccessHandler() {
		return new JwtRefreshSuccessHandler(jwtUserService);
	}

	@Bean
	protected TokenClearLogoutHandler tokenClearLogoutHandler() {
		return new TokenClearLogoutHandler(jwtUserService);
	}

	/**
	 * 跨域支持,SpringBoot默认跨域方法只支持HEAD,GET,POST.
	 * @return
	 */
	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "HEAD", "PUT", "DELETE", "OPTION"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.addExposedHeader("Authorization");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
