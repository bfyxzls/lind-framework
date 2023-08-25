package com.lind.rbac.dto;

import com.lind.common.core.util.RegexUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel("更新密码DTO")
public class UserPasswordDTO {

	@ApiModelProperty("老密码")
	@Pattern(regexp = RegexUtils.password3, message = "老密码格式不合法")
	private String password;

	@ApiModelProperty("新密码")
	@Pattern(regexp = RegexUtils.password3, message = "新密码格式不合法")
	private String newPassword;

	@ApiModelProperty("确认密码")
	@Pattern(regexp = RegexUtils.password3, message = "确认密码格式不合法")
	private String confirmPassword;

}
