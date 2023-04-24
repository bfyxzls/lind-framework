package com.lind.uaa.jwt.permission;

import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.entity.ResourcePermission;
import com.lind.uaa.jwt.entity.ResourceUser;
import com.lind.uaa.jwt.entity.RoleGrantedAuthority;
import com.lind.uaa.jwt.service.ResourcePermissionService;
import com.lind.uaa.jwt.utils.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限管理决断器 判断用户拥有的权限或角色是否有资源访问权限
 * configAttributes表示当前资源需要的权限,authentication.getAuthorities()表示当前用户所拥有的权限
 */
@Slf4j
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

	@Autowired
	RedisService redisService;

	@Autowired
	ResourcePermissionService resourcePermissionService;

	@SneakyThrows
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		log.debug("path permission:{}", configAttributes);
		log.debug("current user role:");
		Optional.ofNullable(authentication.getAuthorities()).ifPresent(o -> {
			o.forEach(i -> log.info("{}", i));
		});
		if (configAttributes == null) {
			return;
		}
		ResourceUser userDetails = (ResourceUser) SecurityUtils.getUser();
		if (userDetails.getAuthorities().stream().filter(o -> o.equals(new SimpleGrantedAuthority("管理员"))).findAny()
				.isPresent()) {
			return;
		}

		// 遍历当前path所需的权限进行断言
		for (ConfigAttribute configAttribute : configAttributes) {
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				// 匹配用户拥有的ga 和 系统中的needPerm
				if (ga instanceof RoleGrantedAuthority) {
					String needPerm = configAttribute.getAttribute();
					RoleGrantedAuthority userAuth = (RoleGrantedAuthority) ga;
					if (needPerm.trim().equals(userAuth.getName())) {
						return;
					}
					else {
						// 通过角色取它的权限列表
						List<? extends ResourcePermission> permissionList = resourcePermissionService
								.getAllByRoleId(userAuth.getId());
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
