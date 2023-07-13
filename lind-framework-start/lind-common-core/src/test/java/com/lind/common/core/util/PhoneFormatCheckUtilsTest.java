package com.lind.common.core.util;

import org.junit.Assert;
import org.junit.Test;

public class PhoneFormatCheckUtilsTest {

	@Test
	public void validPhone() {
		Assert.assertTrue(PhoneFormatCheckUtils.isPhoneLegal("18600546316"));
	}

}
