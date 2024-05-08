package com.lind.common.otp;

import com.lind.common.core.util.ByteUtils;
import com.lind.common.core.util.DateUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.lind.common.encrypt.HashUtils.md5;
import static com.lind.common.otp.HotpCSharpUtil.generateTOTP;

/**
 * @author lind
 * @date 2023/10/16 9:40
 * @since 1.0.0
 */
@Slf4j
public class TotpCSharpTest {

	@Test
	public void test_30s_8bit2() throws Exception {
		for (int i = 0; i < 30; i++) {
			long count = System.currentTimeMillis();
			generateTOTP(count, "ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 10, 8);
			Thread.sleep(1000);
		}

	}

	@SneakyThrows
	@Test
	public void test_auto_login() {
		String userId = "oPBM51GIn6yc8MWZoCCz6_zr09lc";
		System.out.println(md5(userId + generateTOTP("ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 300, 8)).toUpperCase());
	}

	@Test
	public void test_30s_8bit() throws Exception {
		for (int i = -5; i <= 5; i++) {
			log.info(i + "->" + generateTOTP(1700117206901L + 60000 * i, "ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 300, 8));
		}

		// http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/auth?client_id=account&redirect_uri=http%3A%2F%2F192.168.4.26%3A8080%2Fauth%2Frealms%2Ffabao%2Faccount%2Flogin-redirect&scope=openid&response_type=code&userId=347c9e9e-076c-45e3-be74-c482fffcc6e5&sign=333CCCE6F5430E5703B61FB86FB340B2
	}

	/*
	 * timespan:1700133824529,counter:170013382,otp:89523413
	 * timespan:1700133826014,counter:170013382,otp:89523413
	 * timespan:1700133827020,counter:170013382,otp:89523413
	 * timespan:1700133828026,counter:170013382,otp:89523413
	 * timespan:1700133829031,counter:170013382,otp:89523413
	 * timespan:1700133830037,counter:170013383,otp:32758861
	 * timespan:1700133831043,counter:170013383,otp:32758861
	 * timespan:1700133832049,counter:170013383,otp:32758861
	 * timespan:1700133833056,counter:170013383,otp:32758861
	 * timespan:1700133834060,counter:170013383,otp:32758861
	 * timespan:1700133835066,counter:170013383,otp:32758861
	 * timespan:1700133836071,counter:170013383,otp:32758861
	 * timespan:1700133837077,counter:170013383,otp:32758861
	 * timespan:1700133838083,counter:170013383,otp:32758861
	 * timespan:1700133839089,counter:170013383,otp:32758861
	 * timespan:1700133840096,counter:170013384,otp:29394802
	 * timespan:1700133841101,counter:170013384,otp:29394802
	 * timespan:1700133842106,counter:170013384,otp:29394802
	 * timespan:1700133843111,counter:170013384,otp:29394802
	 * timespan:1700133844117,counter:170013384,otp:29394802
	 * timespan:1700133845123,counter:170013384,otp:29394802
	 * timespan:1700133846128,counter:170013384,otp:29394802
	 * timespan:1700133847133,counter:170013384,otp:29394802
	 * timespan:1700133848139,counter:170013384,otp:29394802
	 * timespan:1700133849145,counter:170013384,otp:29394802
	 * timespan:1700133850152,counter:170013385,otp:10514597
	 * timespan:1700133851157,counter:170013385,otp:10514597
	 * timespan:1700133852162,counter:170013385,otp:10514597
	 * timespan:1700133853167,counter:170013385,otp:10514597
	 * timespan:1700133854173,counter:170013385,otp:10514597
	 */
	@SneakyThrows
	@Test
	public void timestampTest() {
		long times = 1700133831043L;
		long prev = times - 10 * 1000;
		long next = times + 10 * 1000;
		generateTOTP(prev, "ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 10, 8);
		generateTOTP(times, "ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 10, 8);
		generateTOTP(next, "ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 10, 8);

	}

	@Test
	public void simpleTimeWindow() {
		// 简单的时间窗口计算
		IntStream.range(100, 1000).forEach(o -> {
			log.info("{}:{}", o, o / 30);
		});
	}

	@Test
	public void timeWindow() {
		int timeStep = 30;
		long counter1 = (System.currentTimeMillis() / 1000) / timeStep;
		System.out.println("counter=" + counter1);
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

	@SneakyThrows
	@Test
	public void carsiTotp() {

		String totp = generateTOTP("ABCDEFHGIJKLMNOPQRST234UVWXYZ567", 300, 8);
		String affiliation = "staff@shufe.edu.cn";
		String eptid = "tEgCVjfCJv8OQJx/PB+GLng1X7Y=";
		String sign = md5(affiliation + eptid + totp).toUpperCase();
		String url = String.format(
				"https://login.pkulaw.com/register/CarsiRegister?sign=%s&affiliation=staff@shufe.edu.cn&eptid=tEgCVjfCJv8OQJx/PB+GLng1X7Y=",
				sign);
		log.info("{}", url);
	}

}
