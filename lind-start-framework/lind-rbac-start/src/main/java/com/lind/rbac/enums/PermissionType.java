package com.lind.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lind.common.enums.NameValueEnum;

/**
 * 权限类型.
 */
public enum PermissionType implements NameValueEnum {

	MENU("菜单", 0), BUTTON("按钮", 1), TREE("目录", 2);

	@JsonValue
	private String name;

	@EnumValue
	private Integer value;

	PermissionType(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
