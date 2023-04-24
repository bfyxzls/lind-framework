/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.lind.uaa.jwt.permission;

import com.lind.uaa.jwt.anno.RequiresPermissions;
import com.lind.uaa.jwt.entity.ResourcePermission;
import com.lind.uaa.jwt.entity.ResourceUser;
import com.lind.uaa.jwt.entity.RoleGrantedAuthority;
import com.lind.uaa.jwt.service.ResourcePermissionService;
import com.lind.uaa.jwt.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限拦截器
 *
 * @author lind
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

	@Autowired
	ResourcePermissionService resourcePermissionService;

	@Pointcut("@annotation(com.lind.uaa.jwt.anno.RequiresPermissions)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = point.getTarget().getClass().getDeclaredMethod(signature.getName(),
				signature.getParameterTypes());
		if (method.isAnnotationPresent(RequiresPermissions.class)) {
			RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
			ResourceUser userDetails = (ResourceUser) SecurityUtils.getUser();
			if (userDetails.getAuthorities().stream().filter(o -> o.equals(new SimpleGrantedAuthority("管理员"))).findAny()
					.isPresent())
				return point.proceed();
			Boolean flag = requiresPermissions.logical().equals(Logical.AND) ? true : false;

			for (String needPerm : requiresPermissions.value()) {
				for (GrantedAuthority authority : userDetails.getAuthorities()) {
					log.info(authority.getAuthority());
					RoleGrantedAuthority userAuth = (RoleGrantedAuthority) authority;

					// 通过角色取它的权限列表
					List<? extends ResourcePermission> permissionList = resourcePermissionService
							.getAllByRoleId(userAuth.getId());
					if (!CollectionUtils.isEmpty(permissionList)) {
						List<String> authPermissions = permissionList.stream()
								.map(permission -> permission.getPermissions()).collect(Collectors.toList());
						if (requiresPermissions.logical() == Logical.OR) {
							flag = flag || authPermissions.contains(needPerm);// 默认值为假;全假才假
						}
						else {
							flag = flag && authPermissions.contains(needPerm);// 默认值为真;全真才真
						}
					}

				}
			}
			// 触发AccessDeniedHandler的实现类
			if (flag) {
				return point.proceed();
			}
			throw new AccessDeniedException("access denied");
		}
		return point.proceed();
	}

}
