package com.lind.common.enums;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/5/11 10:02
 * @since 1.0.0
 */
public class KcErrorCodeTest {

	@Test
	public void getMessage() {
		Assert.assertEquals("手机号不存在",KcErrorCode.getMessage(KcErrorCode.PHONE_NOT_FOUND));
		Assert.assertEquals("U007",KcErrorCode.PHONE_NOT_FOUND.getCode());
	}

}
