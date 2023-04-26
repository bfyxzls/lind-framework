package com.lind.admin.entity;

import lombok.Data;

/**
 * @author lind
 * @date 2022/11/18 13:25
 * @since 1.0.0
 */
@Data
public class Permission {

	private Integer id;

	private String name;

	private Integer parentId;

	private Integer type;

	private String path;

	private String permissionCode;

}
