package com.lind.common.environment_post_processor;

import com.lind.common.core.util.SpringContextUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/10/9 8:37
 * @since 1.0.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ETest {

	@Test
	public void test() {
		System.out.println("ETest");
	}

	@Test
	public void mpwTest() {
		// AESNetUtils.encrypt("中国人", "lind123456123456");//vFLIjoQXy4Pzo1/hOm8hWw==
		System.out.println(SpringContextUtils.getEnvironment().getProperty("author.name"));
		System.out.println(SpringContextUtils.getEnvironment().getProperty("author.nationality"));
	}

}
