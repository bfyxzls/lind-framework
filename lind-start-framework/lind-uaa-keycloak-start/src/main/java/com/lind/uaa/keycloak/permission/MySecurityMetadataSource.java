package com.lind.uaa.keycloak.permission;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限资源管理器 为权限决断器提供支持
 *
 * @author Exrickx
 */
@Slf4j
@ConditionalOnClass(PermissionService.class)
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private PermissionService permissionDao;

	private Map<String, Collection<ConfigAttribute>> map = null;

	/**
	 * 加载权限表中所有操作请求权限
	 */
	public void loadResourceDefine() {

		map = new HashMap<>(16);
		Collection<ConfigAttribute> configAttributes;
		ConfigAttribute cfg;
		// 获取启用的权限操作请求
		List<? extends ResourcePermission> permissions = permissionDao.getAll();
		for (ResourcePermission permission : permissions) {
			configAttributes = new ArrayList<>();
			if (StringUtils.isNotBlank(permission.getPath()) && StringUtils.isNotBlank(permission.getTitle())) {
				cfg = new SecurityConfig(permission.getTitle());
				configAttributes.add(cfg);
			}

			if (!CollectionUtils.isEmpty(configAttributes)) {
				map.put(permission.getPath(), configAttributes);
			}

		}
	}

	/**
	 * 判定用户请求的url是否在权限表中 如果在权限表中，则返回给decide方法，用来判定用户是否有此权限 如果不在权限表中则放行
	 * @param o
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

		if (map == null) {
			loadResourceDefine();
		}
		// Object中包含用户请求request
		String url = ((FilterInvocation) o).getRequestUrl();
		PathMatcher pathMatcher = new AntPathMatcher();
		for (Map.Entry<String, Collection<ConfigAttribute>> item : map.entrySet()) {
			String resURL = item.getKey();
			if (StringUtils.isNotBlank(resURL) && pathMatcher.match(resURL, url)) {
				return item.getValue();
			}
		}

		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}

}
