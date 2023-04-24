package com.lind.uaa.config;

import com.lind.uaa.impl.AbstractRedisClientDetailsService;
import com.lind.uaa.impl.RandomAuthenticationKeyGenerator;
import com.lind.uaa.impl.RedisAuthorizationCodeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证管理配置,
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	public UserDetailsService userDetailsService;

	/**
	 * 认证管理器
	 */
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

	@Autowired
	private AbstractRedisClientDetailsService abstractRedisClientDetailsService;

	/**
	 * 令牌存储
	 */
	@Bean
	public TokenStore tokenStore() {
		RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
		// 解决同一username每次登陆access_token都相同的问题
		redisTokenStore.setAuthenticationKeyGenerator(new RandomAuthenticationKeyGenerator());

		return redisTokenStore;
	}

	/**
	 * 将当前用户信息追加到登陆后返回的json数据里<br>
	 * 通过参数access_token.add-userinfo控制<br>
	 * 2018.07.13
	 * @param accessToken accessToken
	 * @param authentication authentication
	 */
	private void addLoginUserInfo(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if (accessToken instanceof DefaultOAuth2AccessToken) {
			DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;

			Authentication userAuthentication = authentication.getUserAuthentication();
			Object principal = userAuthentication.getPrincipal();
			Map<String, Object> map = new HashMap<>(defaultOAuth2AccessToken.getAdditionalInformation()); // 旧的附加参数
			map.put("loginUser", principal); // 追加当前登陆用户
			defaultOAuth2AccessToken.setAdditionalInformation(map);
		}
	}

	/**
	 * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
	 * @param endpoints endpoints
	 * @throws Exception Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(this.authenticationManager);
		endpoints.tokenStore(tokenStore());
		// 授权码模式下，code存储
		endpoints.authorizationCodeServices(redisAuthorizationCodeServices);
		// 设置userDetailsService刷新token时候会用到
		endpoints.userDetailsService(userDetailsService);

		endpoints.tokenEnhancer((accessToken, authentication) -> {
			addLoginUserInfo(accessToken, authentication);
			return accessToken;
		});

	}

	/**
	 * AuthorizationServerSecurityConfigurer：用来配置令牌端点(Token Endpoint)的安全约束.
	 * @param security security
	 * @throws Exception Exception
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients(); // 允许表单形式的认证
	}

	/**
	 * 我们将client信息存储到oauth_client_details表里<br>
	 * 并将数据缓存到redis
	 * @param clients clients
	 * @throws Exception Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(abstractRedisClientDetailsService);
	}

}
