package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneFormatCheckUtilsTest {

	@Test
	public void validPhone() {
		assertTrue(PhoneFormatCheckUtils.isPhoneLegal("18600546316"));
	}

}
