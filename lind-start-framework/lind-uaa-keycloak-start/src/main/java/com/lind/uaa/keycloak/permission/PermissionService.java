package com.lind.uaa.keycloak.permission;

import java.util.List;

/**
 * 获取数据仓库里的权限.
 */
public interface PermissionService {

	/**
	 * 获取资源与权限对应关系列表.
	 * @return
	 */
	List<? extends ResourcePermission> getAll();

	/**
	 * 按着角色Key获取资源权限,与用户没有一毛钱关系，这主要为了客户端对客户端的授权考虑的.
	 * @return
	 */
	List<? extends ResourcePermission> getByRoleId(String roleKey);

}
