package com.lind.uaa.keycloak.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lind
 * @date 2022/9/16 10:01
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultPermissionServiceImpl implements PermissionService {

	@Override
	public List<? extends ResourcePermission> getAll() {
		return null;
	}

	@Override
	public List<? extends ResourcePermission> getByRoleId(String roleKey) {
		return null;
	}

}
