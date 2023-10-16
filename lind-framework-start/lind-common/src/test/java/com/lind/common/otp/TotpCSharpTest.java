package com.lind.common.otp;

import org.junit.Test;

import static com.lind.common.otp.HotpCSharpUtil.generateTOTP;

/**
 * @author lind
 * @date 2023/10/16 9:40
 * @since 1.0.0
 */
public class TotpCSharpTest {

	@Test
	public void test_30s_8bit() throws Exception {
		System.out.println(generateTOTP("pkulaw", 30, 8));
	}

}
