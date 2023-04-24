package com.lind.uaa.keycloak.permission;

/**
 * 资源权限,实体数据表需要实现这个接口. 对接口api进行权限设置,auth,scope等,如果没有设置auth或者scope,表示不受限制.
 */
public interface ResourcePermission {

	/**
	 * 授权名称-菜单-按钮名称等.
	 */
	String getTitle();

	/**
	 * 页面路径/资源链接url.
	 */
	String getPath();

}
