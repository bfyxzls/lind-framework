package com.lind.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("菜单DTO")
public class PermissionDTO {

	@ApiModelProperty("主键")
	private String id;

	/**
	 * 菜单-按钮-名称.
	 */
	@ApiModelProperty("名称")
	private String title;

	/**
	 * 资源相对路径.
	 */
	@ApiModelProperty("相对路径")
	private String path;

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
	 * 单行按钮组.
	 */
	@ApiModelProperty("单行按钮组")
	private Integer rowButton;

	/**
	 * 批量操作按钮组.
	 */
	@ApiModelProperty("批量操作按钮组")
	private Integer bulkButton;

}
