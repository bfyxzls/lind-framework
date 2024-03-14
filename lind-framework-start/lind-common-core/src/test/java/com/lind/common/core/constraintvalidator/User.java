package com.lind.common.core.constraintvalidator;

import com.lind.common.core.validate.dic.ValidDic;
import com.lind.common.core.validate.email.ValidEmail;
import com.lind.common.core.validate.flag.ValidFlag;
import lombok.Data;

import java.util.List;

/**
 * @author lind
 * @date 2023/7/20 16:37
 * @since 1.0.0
 */
@Data
public class User {

	@ValidEmail
	private String email;

	@ValidDic(value = { "0", "1" }, message = "性别不正确")
	private Integer sex;

	@ValidFlag(message = "权限不正确")
	private List<Integer> permission;

}
