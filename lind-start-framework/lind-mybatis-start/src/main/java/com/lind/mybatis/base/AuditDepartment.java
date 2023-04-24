package com.lind.mybatis.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public abstract class AuditDepartment extends BaseEntity {

	/**
	 * 建立部门.
	 */
	@TableField("create_department_id")
	private String createDepartmentId;

}
