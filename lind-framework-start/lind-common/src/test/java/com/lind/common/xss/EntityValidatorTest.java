package com.lind.common.xss;

import com.lind.common.core.validate.BeanValidatorUtils;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2024/5/6 9:30
 * @since 1.0.0
 */
public class EntityValidatorTest {

	@Test
	public void testEntityValidator() {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail("<script>okk@sina.com");
		BeanValidatorUtils.validateWithException(userDTO);

	}

}
