package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色表.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_user_role")
public class UserRole {

	private String id;

	private String userId;

	private String roleId;

	private transient String roleName;

}
