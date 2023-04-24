package com.lind.uaa.keycloak.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 重写token与认证的适配. 20210716添加token在线校验功能
 */
@RequiredArgsConstructor
public class MyKeycloakAuthenticationProvider implements AuthenticationProvider {

	private final static Logger logger = LoggerFactory.getLogger(MyKeycloakAuthenticationProvider.class);

	private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

	public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
		this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
	}

	@SneakyThrows
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		// Extract roles from the `realm_access.roles`
		Set<String> roles = token.getAccount().getRoles();
		if (roles != null) {
			grantedAuthorities.addAll(roles.stream().map(role -> new KeycloakRole(role)).collect(Collectors.toList()));
		}

		// Extract roles from the `realm_access.scopes`
		if (token.getPrincipal() instanceof KeycloakPrincipal) {
			KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) token.getPrincipal();
			String scope = keycloakPrincipal.getKeycloakSecurityContext().getToken().getScope();
			Set<String> scopes = new HashSet<>();
			for (String item : StringUtils.split(scope, " ")) {
				scopes.add(item);
			}
			grantedAuthorities.addAll(scopes.stream().map(role -> new KeycloakRole(role)).collect(Collectors.toList()));
		}

		// Extract roles from the `resource_access.<client-id>.roles`
		Map<String, AccessToken.Access> resourceAccess = token.getAccount().getKeycloakSecurityContext().getToken()
				.getResourceAccess();
		if (resourceAccess != null) {
			grantedAuthorities.addAll(resourceAccess.entrySet().stream()
					.flatMap(access -> access.getValue().getRoles().stream().map(role -> access.getKey() + "/" + role))
					.map(role -> new KeycloakRole(role)).collect(Collectors.toList()));
		}

		return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(),
				mapAuthorities(grantedAuthorities));
	}

	private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		return grantedAuthoritiesMapper != null ? grantedAuthoritiesMapper.mapAuthorities(authorities) : authorities;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
	}

}
