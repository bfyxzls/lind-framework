package com.lind.common.core.util;

import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2022/11/17 14:15
 * @since 1.0.0
 */
public class MaskUtilsTest {

	org.slf4j.Logger logger = LoggerFactory.getLogger(MaskUtilsTest.class);

	@Test
	public void inc() {
		MaskUtils maskUtils = new MaskUtils(32L);
		maskUtils.toRangeInc(2L);
		logger.info("range={}", maskUtils.toRangeInc(2L));
		logger.info("inc={}", maskUtils.toInc(maskUtils.toRangeInc(2L)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void incExpected() {
		MaskUtils maskUtils = new MaskUtils(32L);
		maskUtils.toRangeInc(32L);
	}

}
