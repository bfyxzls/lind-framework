package com.lind.uaa.keycloak.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("keycloak.uaa")
public class UaaProperties {

	/**
	 * 业务平台重定向回调地址，为空表示直接在页面上输出token.
	 */
	private String redirectUri;

	/**
	 * URL白名单.
	 */
	private String[] permitAll;

	/**
	 * 除了permitAll里的地址，其它地址需要有allowRoles包含的角色，才能访问.
	 */
	private String[] allowRoles;

}
