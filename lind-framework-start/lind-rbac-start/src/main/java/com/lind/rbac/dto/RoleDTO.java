package com.lind.rbac.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.lind.rbac.valid.PrefixAdmin;
import com.lind.rbac.valid.RoleProvider;
import com.lind.uaa.jwt.entity.ResourcePermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;

@Data
@GroupSequenceProvider(RoleProvider.class)
@ApiModel("角色DTO")
public class RoleDTO {

	@ApiModelProperty("主键")
	@TableField("id")
	private String id;

	@ApiModelProperty("名称")
	@TableField("name")
	@PrefixAdmin(groups = CheckManagerGroup.class)
	private String name;

	private boolean manager;

	@ApiModelProperty("菜单ID列表")
	private List<String> permissionIdList;

	@ApiModelProperty("菜单列表")
	private List<? extends ResourcePermission> permissionList;

	public interface CheckManagerGroup {

	}

}
