package com.lind.common.util;

import org.junit.Assert;
import org.junit.Test;

public class PhoneFormatCheckUtilsTest {

	@Test
	public void validPhone() {
		Assert.assertTrue(PhoneFormatCheckUtils.isPhoneLegal("18600546316"));
	}

}
