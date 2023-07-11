package com.lind.uaa.keycloak.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.lind.uaa.keycloak.permission.DefaultPermissionServiceImpl;
import com.lind.uaa.keycloak.permission.MethodPermissionService;
import com.lind.uaa.keycloak.permission.PermissionService;
import com.lind.uaa.keycloak.permission.PermissionServiceManager;
import com.lind.uaa.keycloak.scope.ScopeSetInterceptor;
import com.lind.uaa.keycloak.whitelist.PermitAllSecurityConfig;
import com.lind.uaa.keycloak.whitelist.PermitAllUrl;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@KeycloakConfiguration
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired(required = false)
	ScopeSetInterceptor scopeSetInterceptor;

	@Autowired(required = false)
	KeycloakSessionStateInterceptor keycloakSessionStateInterceptor;

	@Autowired
	UaaProperties uaaProperties;

	@Autowired
	PermitAllSecurityConfig permitAllSecurityConfig;

	@Autowired
	private KeycloakSpringBootProperties keycloakSpringBootProperties;

	/**
	 * Registers the MyKeycloakAuthenticationProvider with the authentication manager.
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		MyKeycloakAuthenticationProvider authenticationProvider = new MyKeycloakAuthenticationProvider();
		authenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
		auth.authenticationProvider(authenticationProvider);
	}

	@Bean
	public KeycloakConfigResolver keycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

	/**
	 * 默认的PermissionService实现类.
	 */
	@Bean
	@ConditionalOnMissingBean
	public PermissionService permissionService() {
		return new DefaultPermissionServiceImpl();
	}

	/**
	 * 默认的PermissionService实现类，从redis里取出权限数据.
	 * @param redisTemplate
	 * @param permissionService
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PermissionServiceManager permissionServiceManager(RedisTemplate redisTemplate,
			PermissionService permissionService) {
		return new PermissionServiceManager(redisTemplate, permissionService, keycloakSpringBootProperties);
	}

	/**
	 * Defines the session authentication strategy.
	 */
	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] urls = uaaProperties.getPermitAll();
		super.configure(http);
		http.apply(permitAllSecurityConfig).and().authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl(urls))
				.permitAll()// 白名单也会进行token校验
				.anyRequest().authenticated().and().httpBasic().and().csrf().disable().cors();

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (scopeSetInterceptor != null) {
			registry.addInterceptor(scopeSetInterceptor).addPathPatterns("/**");
		}

		// 这些接口需要同步登录状态，而不需要在未登录时去登录页
		String[] urls = uaaProperties.getPermitAll();

		if (keycloakSessionStateInterceptor != null && urls != null && urls.length > 0) {
			// TODO:这块需要注册，如果是post方法，是不需要同步状态的，否则post与重定向GET会冲突
			registry.addInterceptor(keycloakSessionStateInterceptor).addPathPatterns(urls);
		}
	}

	/**
	 * 存储纯json字符串
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 日期序列化处理
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		om.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule())
				.registerModule(new ParameterNamesModule());
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	/**
	 * 鉴权具体的实现逻辑
	 * @return @PreAuthorize("@pms.hasPermission('权限名称')")
	 */
	@Bean("pms")
	public MethodPermissionService methodPermissionService() {
		return new MethodPermissionService(keycloakSpringBootProperties);
	}

}
