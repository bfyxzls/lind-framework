package com.lind.common.core.constraintvalidator;

import com.lind.common.core.validate.dic.DictionaryMatches;
import com.lind.common.core.validate.email.CustomEmailMatches;
import com.lind.common.core.validate.flag.FlagMatches;
import com.lind.common.core.validate.phone.CustomPhoneMatches;
import lombok.Data;

import java.util.List;

/**
 * @author lind
 * @date 2023/7/20 16:37
 * @since 1.0.0
 */
@Data
public class User {

	@CustomEmailMatches
	private String email;

	@DictionaryMatches(value = { "0", "1" }, message = "性别不正确")
	private Integer sex;

	@FlagMatches(message = "权限不正确")
	private List<Integer> permission;

	@CustomPhoneMatches
	private String phone;

}
