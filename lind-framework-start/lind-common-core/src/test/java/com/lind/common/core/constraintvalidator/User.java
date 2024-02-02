package com.lind.common.core.constraintvalidator;

import com.lind.common.core.validate.email.ValidEmail;
import com.lind.common.core.validate.flag.ValidFlag;
import lombok.Data;

/**
 * @author lind
 * @date 2023/7/20 16:37
 * @since 1.0.0
 */
@Data
public class User {

	@ValidEmail
	private String email;
	@ValidFlag(value = {"0","1"}, message = "性别不正确")
	private Integer sex;
}
