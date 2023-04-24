package com.lind.common;

import com.lind.common.encrypt.DESCbcUtils;
import com.lind.common.otp.HmacOneTimePasswordGenerator;
import com.lind.common.otp.TimeBasedOneTimePasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class HOTPTest {

	final static String password = "pkulaw";

	TimeBasedOneTimePasswordGenerator timeBasedOneTimePasswordGenerator;

	public HOTPTest() throws NoSuchAlgorithmException {
		timeBasedOneTimePasswordGenerator = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));
	}

	static Stream<Arguments> hotpTestVectorSource() {
		return Stream.of(arguments(0, 755224), arguments(1, 287082), arguments(2, 359152), arguments(3, 969429),
				arguments(4, 338314), arguments(5, 254676), arguments(6, 287922), arguments(7, 162583),
				arguments(8, 399871), arguments(9, 520489));
	}

	/**
	 * now time.
	 */
	@Test
	public void instantTest() {
		Instant now = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));// beijing时间
		System.out.printf("now:%s", now);
	}

	/**
	 * 生成动态密钥.
	 * @throws Exception
	 */
	@Test
	public void totpGenerator() throws Exception {
		System.out.format("format用0补全结果: %06d", 9696);
		final Instant now = Instant.now();
		String passKey = String.valueOf(timeBasedOneTimePasswordGenerator.generateOneTimePassword("pkulaw", now));
		System.out.printf("Current password: %s\n", passKey);
	}

	@Test
	public void hotp() throws Exception {
		HmacOneTimePasswordGenerator hmacOneTimePasswordGenerator = new HmacOneTimePasswordGenerator();
		for (int i = 0; i < 10; i++)
			System.out.format("hmac:%06d\n", hmacOneTimePasswordGenerator.generateOneTimePassword("pkulaw", i));
	}

	/**
	 * 密钥的是一个随时间动态变化的值，在一定时间窗格时，生成的密钥是同一个，这就是TOTP算法.
	 * @throws Exception
	 */
	@Test
	public void totpDesGenerator() throws Exception {

		String plaintext = "hello";

		// 动态同密钥
		String dynamicPassKey = String.format("%08d",
				timeBasedOneTimePasswordGenerator.generateOneTimePassword(password, Instant.now()));
		log.info("dynamicPassKey:{}", dynamicPassKey);
		String result = DESCbcUtils.encrypt(dynamicPassKey, plaintext);

		log.info("encryptDES result={}", result);
		for (int i = 0; i < 30; i++) {
			// 动态同密钥
			dynamicPassKey = String.format("%08d",
					timeBasedOneTimePasswordGenerator.generateOneTimePassword(password, Instant.now()));
			log.info("dynamicPassKey:{}", dynamicPassKey);

			log.info("{} decryptDES result={}", i, DESCbcUtils.decrypt(dynamicPassKey, result));
			TimeUnit.SECONDS.sleep(1);
		}
	}

}
