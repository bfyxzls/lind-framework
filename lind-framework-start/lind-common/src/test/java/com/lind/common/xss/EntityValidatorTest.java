package com.lind.common.xss;

import com.lind.common.core.util.BeanValidatorUtils;
import com.lind.common.core.util.VerifyCodeUtils;
import org.junit.Test;

/**
 * @author lind
 * @date 2024/5/6 9:30
 * @since 1.0.0
 */
public class EntityValidatorTest {
    @Test
    public void testEntityValidator() {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("<script>okk@sina.com");
        BeanValidatorUtils.validateWithException(userDTO);

    }
}
