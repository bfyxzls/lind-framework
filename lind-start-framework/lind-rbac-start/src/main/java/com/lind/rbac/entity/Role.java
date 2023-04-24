package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lind.mybatis.base.BaseEntity;
import com.lind.uaa.jwt.entity.ResourceRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色表.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonDeserialize(as = Role.class)
@ApiModel("角色表")
@TableName("sys_role")
public class Role extends BaseEntity implements ResourceRole {

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("备注")
	private String remark;

}
