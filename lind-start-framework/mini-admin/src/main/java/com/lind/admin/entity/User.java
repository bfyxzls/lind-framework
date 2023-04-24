package com.lind.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
@Data
@TableName("user_info")
public class User {

	private Integer id;

	private String username; // 账号

	private String password; // 密码

	private int role; // 角色：0-普通用户、1-管理员

	private String permission; // 权限：操作权限列表，每个操作可能是一个字符串，如：read,del,update，多个逗号分割

	private String email;

	private String phone;

	private String realName;

	// plugin
	public boolean validPermission(String... permissions) {
		if (this.role == 1) {
			return true;
		}
		else {

			if (permissions == null || permissions.length == 0 || permissions[0].equals("")) {
				return true;
			}
			if (StringUtils.hasText(this.permission)) {
				String[] havePermissions = this.permission.split(",");

				for (String param : permissions) {
					if (Arrays.stream(havePermissions).anyMatch(o -> o.equals(param))) {
						return true;
					}
				}
			}
			return false;
		}

	}

}
