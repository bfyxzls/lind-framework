package com.lind.common.otp;

import com.lind.common.encrypt.HashUtils;
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
		String totp = generateTOTP("bdyh_2021_cas", 30, 8);
		String userId = "347c9e9e-076c-45e3-be74-c482fffcc6e5";
		String sign = HashUtils.md5(userId + totp).toUpperCase();
		System.out.println(sign);
		// http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/auth?response_type=code&client_id=democlient&redirect_uri=http://www.baidu.com&scope=openid&userId=347c9e9e-076c-45e3-be74-c482fffcc6e5&sign=d21a1c3d766df6f0dad4b21223998a1d
	}

}
