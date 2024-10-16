package com.lind.fileupload.dto;

import com.lind.fileupload.validator.PrefixRoleMatch;
import com.lind.fileupload.validator.UserGroupSequenceProvider;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;

import javax.validation.constraints.NotNull;

/**
 * @author lind
 * @date 2024/10/14 10:53
 * @since 1.0.0
 */
@Data
@GroupSequenceProvider(UserGroupSequenceProvider.class)
// @FieldContentEqualMatch(sourceField = "newPassword", destinationField =
// "confirmPassword")
public class UpdateUserModifyPasswordDTO implements UpdateUser {

	@NotNull
	private String userName;

	@PrefixRoleMatch(groups = First.class)
	private String role;

	@NotNull
	private String confirmUserName;

	@NotNull
	private String password;

	private String newPassword;

	private String confirmPassword;

	public interface First {

	}

	public interface Second {

	}

}
