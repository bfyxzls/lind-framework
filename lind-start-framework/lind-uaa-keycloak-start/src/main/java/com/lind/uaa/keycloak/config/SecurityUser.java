package com.lind.uaa.keycloak.config;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 当前用户和token上下文.
 */
public class SecurityUser {

	/**
	 * 获取当前用户名
	 * @return
	 */
	public static String getCurrentUserName() {
		Object principal = getCurrentPrincipal();
		if (principal instanceof KeycloakPrincipal) {
			return ((KeycloakPrincipal) principal).getName();
		}
		return (String) principal;
	}

	/**
	 * 获取当前用户Id
	 * @return
	 */
	public static String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((SimpleKeycloakAccount) ((KeycloakAuthenticationToken) authentication).getDetails())
				.getKeycloakSecurityContext().getToken().getSubject();
	}

	/**
	 * Principal.
	 * @return
	 */
	public static Object getCurrentPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.isNull(authentication)) {
			throw new IllegalArgumentException("401");
		}
		return authentication.getPrincipal();
	}

	/**
	 * 获取当前token，包含了用户信息.
	 * @return
	 */
	public static AccessToken getCurrentToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ((SimpleKeycloakAccount) ((KeycloakAuthenticationToken) authentication).getDetails())
				.getKeycloakSecurityContext().getToken();
	}

	/**
	 * 返回当前token里包含的scope.
	 * @return
	 */
	public static String[] getScope() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String scopeString = ((SimpleKeycloakAccount) ((KeycloakAuthenticationToken) authentication).getDetails())
				.getKeycloakSecurityContext().getToken().getScope();
		return scopeString.split(" ");
	}

}
