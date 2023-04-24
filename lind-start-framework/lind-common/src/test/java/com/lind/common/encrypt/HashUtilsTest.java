package com.lind.common.encrypt;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtilsTest {

	final static String salt = "B1D2Y3H4";

	Logger logger = LoggerFactory.getLogger(HashUtilsTest.class);

	@Test
	public void md5() {
		logger.info(HashUtils.md5("123456"));
	}

	@Test
	public void salt() {
		logger.info(HashUtils.md5("123456"));
		logger.info(new String(salt.getBytes()));
		logger.info(HashUtils.md5("123456", salt.getBytes()));
	}

}
