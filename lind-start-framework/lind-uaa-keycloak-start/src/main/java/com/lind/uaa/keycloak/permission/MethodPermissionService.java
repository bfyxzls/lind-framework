package com.lind.uaa.keycloak.permission;

import cn.hutool.core.util.ArrayUtil;
import com.lind.uaa.keycloak.utils.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 接口权限判断工具
 *
 * @author lind
 * @date 2022/9/19 17:04
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class MethodPermissionService {

	private final KeycloakSpringBootProperties keycloakSpringBootProperties;

	/**
	 * 判断接口是否有任意xxx，xxx权限
	 * @param permissions 权限
	 * @return {boolean}
	 */
	public boolean hasPermission(String... permissions) {
		if (ArrayUtil.isEmpty(permissions)) {
			return false;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).filter(StringUtils::hasText)
				.anyMatch(x -> PatternMatchUtils.simpleMatch(permissions,
						RoleUtil.removeRolePrefix(x, keycloakSpringBootProperties.getResource())));
	}

}
