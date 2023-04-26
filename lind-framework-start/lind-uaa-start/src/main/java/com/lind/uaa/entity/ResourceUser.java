package com.lind.uaa.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户.
 */
public interface ResourceUser extends Serializable {

	String getId();

	/**
	 * 用户名.
	 */
	String getUsername();

	/**
	 * 密码.
	 * @return
	 */
	String getPassword();

	/**
	 * 用户拥有角色.
	 */
	List<ResourceRole> getResourceRoles();

	/**
	 * 用户拥有的权限.
	 */
	List<ResourcePermission> getResourcePermissions();

}
