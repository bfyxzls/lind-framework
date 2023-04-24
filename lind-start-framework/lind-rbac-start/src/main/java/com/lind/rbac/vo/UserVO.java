package com.lind.rbac.vo;

import com.lind.rbac.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("用户视图")
public class UserVO {

	private String id;

	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("电子邮件")
	private String email;

	@ApiModelProperty("手机")
	private String phone;

	@ApiModelProperty("是否管理员")
	private Integer isAdmin;

	@ApiModelProperty("真实姓名")
	private String realName;

	@ApiModelProperty("用户的角色列表")
	private List<Role> roleList;

	@ApiModelProperty("建立时间")
	private LocalDateTime createTime;

	@ApiModelProperty("更新时间")
	private LocalDateTime updateTime;

	@ApiModelProperty("建立人")
	private String createBy;

	@ApiModelProperty("更新人")
	private String updateBy;

}
