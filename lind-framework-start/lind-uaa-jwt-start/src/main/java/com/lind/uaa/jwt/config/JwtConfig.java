package com.lind.uaa.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {

	/**
	 * jwt算法密钥.
	 */
	private String secret;

	/**
	 * jwt超时时间(分).
	 */
	private Long expiresAt;

	/**
	 * 自动刷新token超时时间(分).
	 */
	private Long refreshTokenExpiresAt;

	/**
	 * URL白名单.
	 */
	private String[] permitAll;

	/**
	 * 每分钟容许登录失败的次数.
	 */
	private Integer failLimit;

	/**
	 * 多长时间内出现了failLimit就会锁定failLockTime的时间，不让它登录(分).
	 */
	private Long failLimitTime;

	/**
	 * 登录锁定的时间(分).
	 */
	private Long failLockTime;

	/**
	 * init.
	 */
	public JwtConfig() {
		this.secret = "abc123";
		this.expiresAt = 60L;
		this.refreshTokenExpiresAt = 50L;
		this.failLimit = 5;
		this.failLimitTime = 30L;
		this.permitAll = new String[] {};
	}

}
