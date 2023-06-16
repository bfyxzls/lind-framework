package com.lind.uaa.keycloak.config;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * 当前用户和token上下文.
 */
public class SecurityUser {

	private static ThreadLocal<String> currentUserName = new ThreadLocal<>();

	private static ThreadLocal<String> currentUserId = new ThreadLocal<>();

	/**
	 * 获取当前用户名
	 * @return
	 */
	public static String getCurrentUserName() {
		String userName = currentUserName.get();
		if (StringUtils.isEmpty(userName)) {
			Object principal = getCurrentPrincipal();
			requireNonNull(principal, "principal is null");
			if (principal instanceof KeycloakPrincipal) {
				userName = ((KeycloakPrincipal) principal).getName();
				setCurrentUserName(userName);
			}

		}
		return userName;
	}

	private static void setCurrentUserName(String userName) {
		currentUserName.set(userName);
	}

	public static void clearCurrentUserName() {
		currentUserName.remove();
	}

	public static void clearCurrentUserId() {
		currentUserId.remove();
	}

	/**
	 * 获取当前用户Id
	 * @return
	 */
	public static String getCurrentUserId() {
		String userId = currentUserId.get();
		if (StringUtils.isEmpty(userId)) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			requireNonNull(authentication, "authentication is null, http status 401");
			if (authentication.getDetails() instanceof SimpleKeycloakAccount) {
				userId = ((SimpleKeycloakAccount) authentication.getDetails()).getKeycloakSecurityContext().getToken()
						.getSubject();
				setCurrentUserId(userId);
			}
		}
		return userId;
	}

	private static void setCurrentUserId(String userId) {
		currentUserId.set(userId);
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
