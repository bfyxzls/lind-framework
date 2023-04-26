package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_dictionary")
public class RbacDictionary {

	@ApiModelProperty(value = "id")
	private Integer id;

	@ApiModelProperty(value = "code")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "类型")
	private String type;

	@ApiModelProperty(value = "父id")
	private Integer pId;

}
