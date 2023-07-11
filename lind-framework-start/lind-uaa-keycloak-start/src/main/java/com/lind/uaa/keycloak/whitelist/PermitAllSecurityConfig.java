package com.lind.uaa.keycloak.whitelist;

import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * 白名单配置.
 */
public class PermitAllSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private PermitAuthenticationFilter permitAuthenticationFilter;

	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(permitAuthenticationFilter, KeycloakAuthenticationProcessingFilter.class);
	}

}
