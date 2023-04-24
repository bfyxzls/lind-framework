package com.lind.common.pattern.factorymethod;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userinfo {

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("年纪")
	private Integer age;

	private String email;

}
