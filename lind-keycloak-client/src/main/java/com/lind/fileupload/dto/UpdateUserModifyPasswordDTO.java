package com.lind.fileupload.dto;

import com.lind.common.core.validate.same.SameContentMatches;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author lind
 * @date 2024/10/14 10:53
 * @since 1.0.0
 */
@Data
@SameContentMatches(sourceField = "confirmPassword", destinationField = "newPassword")
public class UpdateUserModifyPasswordDTO implements UpdateUser {

	@NotNull
	private String userName;

	@NotNull
	private String password;

	private String newPassword;

	private String confirmPassword;

}
