package com.lind.uaa.keycloak.permission;

import com.lind.uaa.keycloak.utils.RoleUtil;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限管理决断器 判断用户拥有的权限或角色是否有资源访问权限
 *
 * @author Exrickx
 */
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {

	@Autowired
	PermissionServiceManager permissionServiceManager;

	/**
	 * 当前资源服务器地址.
	 */
	@Autowired
	private KeycloakSpringBootProperties keycloakSpringBootProperties;

	@Override
	public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		log.info("path permission:{}", configAttributes);
		log.info("current user auth:{}", authentication.getAuthorities());
		if (configAttributes == null) {
			return;
		}
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while (iterator.hasNext()) {
			ConfigAttribute c = iterator.next();
			String needPerm = c.getAttribute();
			needPerm = RoleUtil.removeRolePrefix(needPerm, keycloakSpringBootProperties.getResource());// 比较时都去掉前缀
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				// 获取当前用户token里的权限字段
				String userAuth = RoleUtil.removeRolePrefix(ga.getAuthority(),
						keycloakSpringBootProperties.getResource());
				if (needPerm.equals(userAuth)) {
					return;
				}
				else {
					// 通过角色取它的权限列表
					List<? extends ResourcePermission> permissionList = permissionServiceManager
							.getPermissionByRoleFromCache(userAuth);
					if (!CollectionUtils.isEmpty(permissionList)) {
						List<String> authTitles = permissionList.stream().map(permission -> permission.getTitle())
								.collect(Collectors.toList());
						if (authTitles.contains(needPerm)) {
							return;
						}
					}
				}
			}
		}

		throw new AccessDeniedException("抱歉，您没有访问权限");
	}

	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}

}
