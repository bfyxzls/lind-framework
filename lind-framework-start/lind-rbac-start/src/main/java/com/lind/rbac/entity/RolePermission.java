package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * 角色权限表.
 */
@Data
@ToString
@TableName("sys_role_permission")
public class RolePermission {

	private String id;

	private String roleId;

	private String permissionId;

	private Boolean selected = true;

}
