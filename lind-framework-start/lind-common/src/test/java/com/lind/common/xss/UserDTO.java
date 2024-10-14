package com.lind.common.xss;

import com.lind.common.core.validate.xss.XssMatches;
import lombok.Data;

/**
 * @author lind
 * @date 2024/5/6 9:31
 * @since 1.0.0
 */
@Data
public class UserDTO {

	@XssMatches
	private String email;

}
