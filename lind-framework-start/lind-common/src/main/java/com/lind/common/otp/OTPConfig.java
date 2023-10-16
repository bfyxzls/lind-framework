package com.lind.common.otp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Component
public class OTPConfig {

	/**
	 * TOTP的时间有效期（秒）.
	 */
	@Value("${totpSeconds:30}")
	private long totpSeconds;

	@Bean
	HmacOneTimePasswordGenerator hmacOneTimePasswordGenerator() throws NoSuchAlgorithmException {
		return new HmacOneTimePasswordGenerator();
	}

	@Bean
	TimeBasedOneTimePasswordGenerator timeBasedOneTimePasswordGenerator() throws NoSuchAlgorithmException {
		return new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(totpSeconds));
	}

}
