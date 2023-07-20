package com.lind.common.core.constraintvalidator;

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

}
