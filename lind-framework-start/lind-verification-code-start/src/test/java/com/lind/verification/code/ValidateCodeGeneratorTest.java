package com.lind.verification.code;

import com.lind.verification.code.common.ValidateCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/11/30 14:15
 * @since 1.0.0
 */
@SpringBootTest()
@RunWith(SpringRunner.class) // 让测试在Spring容器环境下执行
@Slf4j
public class ValidateCodeGeneratorTest {

	@Autowired
	@Qualifier("smsCodeGenerator")
	ValidateCodeGenerator validateCodeGenerator;

	@Test
	public void smsCode() {
		validateCodeGenerator.generate(null);
	}

}
