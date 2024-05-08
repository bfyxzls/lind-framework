package com.lind.common.core.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * @author lind
 * @date 2022/11/9 16:13
 * @since 1.0.0
 */
public class ObjectUtilsTest {

	private static Logger logger = LoggerFactory.getLogger(ObjectUtilsTest.class);

	@Test
	public void getIdentityHexString() {

		String futureId = ObjectUtils.getIdentityHexString(this);
		logger.info("类对象的16进制hashcode:{}", futureId); // 16进制字段串 6aaa5eb0
		logger.info("类对象的10进制hashcode:{}", this.hashCode()); // 10进制数 1789550256
	}

}
