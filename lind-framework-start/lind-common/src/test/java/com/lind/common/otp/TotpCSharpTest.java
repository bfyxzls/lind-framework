package com.lind.common.otp;

import com.lind.common.core.util.ByteUtils;
import com.lind.common.core.util.DateUtils;
import com.lind.common.encrypt.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;

import static com.lind.common.otp.HotpCSharpUtil.generateTOTP;

/**
 * @author lind
 * @date 2023/10/16 9:40
 * @since 1.0.0
 */
@Slf4j
public class TotpCSharpTest {

	@Test
	public void test_30s_8bit() throws Exception {
		String totp = generateTOTP("ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 300, 8);
		System.out.println(totp);
		String sign = HashUtils.md5("0f2070f9-0a33-49aa-a2d2-50a6d090eb30" + totp).toUpperCase();
		System.out.println(sign);
		// http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/auth?client_id=account&redirect_uri=http%3A%2F%2F192.168.4.26%3A8080%2Fauth%2Frealms%2Ffabao%2Faccount%2Flogin-redirect&scope=openid&response_type=code&userId=347c9e9e-076c-45e3-be74-c482fffcc6e5&sign=333CCCE6F5430E5703B61FB86FB340B2
	}

	@Test
	public void simpleTimeWindow() {
		// 简单的时间窗口计算
		IntStream.range(1, 100).forEach(o -> {
			log.info("{}:{}", o, o / 30);
		});
	}

	@Test
	public void timeWindow() {
		int timeStep = 30;

		IntStream.range(1, 100).forEach(o -> {
			long counter = (System.currentTimeMillis() / 1000) / timeStep; // 生成时间窗口
			log.info("{}:{}", DateUtils.dateTimeNow(), counter);
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});

	}

	@Test
	public void longToBytes() {
		long counter = 1000;
		byte[] counterBytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			counterBytes[7 - i] = (byte) (counter >> (8 * i));
		}
		log.info("{}", counterBytes);
		log.info("{}", ByteUtils.toBytes(counter));
	}

}
