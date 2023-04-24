package com.lind.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel("用户DTO")
public class UserDTO {

	@ApiModelProperty("主键")
	private String id;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("密码")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$", message = "密码格式不合法")
	private String password;

	@ApiModelProperty("电子邮件")
	@Email(message = "email格式不合法")
	private String email;

	@ApiModelProperty("是否管理员")
	private Integer isAdmin;

	@ApiModelProperty("手机")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机格式不合法")
	private String phone;

	@ApiModelProperty("真实姓名")
	private String realName;

	@ApiModelProperty("用户的角色Id列表")
	private List<String> roleIdList;

}
