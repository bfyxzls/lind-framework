package com.lind.hibernate.dto;

import com.lind.hibernate.validator.PrefixRoleMatch;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lind
 * @date 2024/10/14 10:53
 * @since 1.0.0
 */
@Data
public class UpdateUserModifyPasswordDTO implements UpdateUser {

	@NotNull
	private String userName;

	@PrefixRoleMatch(groups = PrefixRoleMatch.Ignore.class)
	private String role;

	@PrefixRoleMatch
	private String roleEng;

	@NotNull
	private String password;

	private String newPassword;

	private String confirmPassword;

}
