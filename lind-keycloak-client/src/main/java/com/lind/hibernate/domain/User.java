package com.lind.hibernate.domain;

import com.lind.common.core.validate.dic.DictionaryMatch;
import com.lind.common.core.validate.email.CustomEmailMatch;
import com.lind.common.core.validate.flag.FlagMatch;
import com.lind.common.core.validate.phone.CustomPhoneMatch;
import lombok.Data;

import java.util.List;

/**
 * @author lind
 * @date 2023/7/20 16:37
 * @since 1.0.0
 */
@Data
public class User {

	@CustomEmailMatch
	private String email;

	@DictionaryMatch(value = { "0", "1" }, message = "性别不正确")
	private Integer sex;

	@FlagMatch(message = "权限不正确")
	private List<Integer> permission;

	@CustomPhoneMatch
	private String phone;

}
