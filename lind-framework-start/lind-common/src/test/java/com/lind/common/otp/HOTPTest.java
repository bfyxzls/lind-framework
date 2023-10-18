package com.lind.common.otp;

import com.lind.common.encrypt.DESCbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
		// Instant.now()是UTC时间
		ZoneId localZoneId = ZoneId.systemDefault();
		ZonedDateTime localTime = Instant.now().atZone(ZoneId.systemDefault()); // 获取本地时间
		Instant instant = localTime.toInstant();
		// String dynamicPassKey = String.format("%08d",
		// timeBasedOneTimePasswordGenerator.generateOneTimePassword(password, instant));
		// log.info("dynamicPassKey:{}", dynamicPassKey);
		String dynamicPassKey = "89919373";
		String result = DESCbcUtils.encrypt(dynamicPassKey, plaintext);
		log.info("encryptDES加密结果={}", result);

		for (int i = 0; i < 40; i++) {
			// 动态同密钥
			dynamicPassKey = String.format("%08d",
					timeBasedOneTimePasswordGenerator.generateOneTimePassword(password, Instant.now()));
			log.info("dynamicPassKey:{},{} decryptDES result={}", dynamicPassKey, i,
					DESCbcUtils.decrypt(dynamicPassKey, result));
			TimeUnit.SECONDS.sleep(1);
		}
	}

	@Test
	public void bitMove() {
		long counter = 327689;
		byte[] b = new byte[8];

		b[0] = (byte) ((counter & 0xff00000000000000L) >>> 56);// 最高位，无符号右移。
		b[1] = (byte) ((counter & 0x00ff000000000000L) >>> 48);
		b[2] = (byte) ((counter & 0x0000ff0000000000L) >>> 40);
		b[3] = (byte) ((counter & 0x000000ff00000000L) >>> 32);
		b[4] = (byte) ((counter & 0x00000000ff000000L) >>> 24);
		b[5] = (byte) ((counter & 0x0000000000ff0000L) >>> 16);
		b[6] = (byte) ((counter & 0x000000000000ff00L) >>> 8);
		b[7] = (byte) (counter & 0x00000000000000ffL);// 最低位,byte[-128,127]
		System.out.println(b);
	}

}
