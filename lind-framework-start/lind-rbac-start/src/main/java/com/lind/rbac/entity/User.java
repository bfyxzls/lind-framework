package com.lind.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lind.mybatis.base.BaseEntity;
import com.lind.mybatis.type.handler.RsaTypeHandler;
import com.lind.uaa.jwt.entity.ResourceRole;
import com.lind.uaa.jwt.entity.ResourceUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户表 因为ResourceUser有自己的序列化，所有User也需要加上@JsonDeserialize(as = User.class)
 * 参考：https://stackoverflow.com/questions/25387978/how-to-add-custom-deserializer-to-interface-using-jackson
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(as = User.class)
@ApiModel("用户表")
@TableName(value = "sys_user", autoResultMap = true)
public class User extends BaseEntity implements ResourceUser {

	/**
	 * 接口中的几个字段
	 */
	@ApiModelProperty("账号")
	private String username;

	@ApiModelProperty("密码")
	private String password;

	@ApiModelProperty("邮件")
	private String email;

	/*
	 * 自定义的几个字段phone,realName
	 */
	@ApiModelProperty("手机")
	private String phone;

	@ApiModelProperty("真实姓名")
	@TableField(typeHandler = RsaTypeHandler.class)
	private String realName;

	@ApiModelProperty("状态")
	private Integer status;

	@ApiModelProperty("组织")
	@TableField(typeHandler = RsaTypeHandler.class)
	private String organization;

	@ApiModelProperty("职务")
	@TableField(typeHandler = RsaTypeHandler.class)
	private String job;

	@ApiModelProperty("性别")
	private Integer gender;

	/**
	 * 用户的角色列表
	 */
	private transient List<? extends ResourceRole> resourceRoles;

}
