package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lind.mybatis.base.BaseEntity;
import com.lind.uaa.jwt.entity.ResourcePermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 权限菜单表.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@JsonDeserialize(as = Permission.class)
@ApiModel("权限菜单表")
@TableName("sys_permission")
public class Permission extends BaseEntity implements ResourcePermission {

	/**
	 * 菜单-按钮-名称.
	 */
	@ApiModelProperty("名称")
	private String title;

	/**
	 * 资源相对路径.
	 */
	@ApiModelProperty("路径")
	private String url;

	/**
	 * 类型：0菜单,1按钮.
	 */
	@ApiModelProperty("类型：0菜单,1按钮")
	private Integer type;

	/**
	 * 上级Id
	 */
	@ApiModelProperty("上级Id")
	private String parentId;

	/**
	 * 图标.
	 */
	@ApiModelProperty("图标")
	private String icon;

	/**
	 * 排序,支持小数.
	 */
	@ApiModelProperty("排序,支持小数")
	private double sortNumber;

	/**
	 * 授权(多个用逗号分隔，如：sys:user:list,sys:user:save)
	 */
	@ApiModelProperty("授权标志")
	private String permissions;

	/**
	 * 子菜单列表.
	 */
	private transient List<? extends ResourcePermission> sons;

}
