/* Copyright (c) 2016 Jon Chambers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. */

package com.lind.common.otp;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;

/**
 * <p>
 * Generates time-based one-time passwords (TOTP) as specified in
 * <a href="https://tools.ietf.org/html/rfc6238">RFC&nbsp;6238</a>.
 * </p>
 *
 * <p>
 * {@code TimeBasedOneTimePasswordGenerator} instances are thread-safe and may be shared
 * between threads.
 * </p>
 *
 * @author <a href="https://github.com/jchambers">Jon Chambers</a>
 */
public class TimeBasedOneTimePasswordGenerator extends HmacOneTimePasswordGenerator {

	/**
	 * A string identifier for the HMAC-SHA1 algorithm (required by HOTP and allowed by
	 * TOTP). HMAC-SHA1 is the default algorithm for TOTP.
	 */
	public static final String TOTP_ALGORITHM_HMAC_SHA1 = "HmacSHA1";

	/**
	 * A string identifier for the HMAC-SHA256 algorithm (allowed by TOTP).
	 */
	public static final String TOTP_ALGORITHM_HMAC_SHA256 = "HmacSHA256";

	/**
	 * A string identifier for the HMAC-SHA512 algorithm (allowed by TOTP).
	 */
	public static final String TOTP_ALGORITHM_HMAC_SHA512 = "HmacSHA512";

	private final Duration timeStep;

	/**
	 * Constructs a new time-based one-time password generator with the given time-step
	 * and a default password length
	 * ({@value com.eatthepath.otp.HmacOneTimePasswordGenerator#DEFAULT_PASSWORD_LENGTH}
	 * decimal digits) and HMAC algorithm
	 * ({@value com.eatthepath.otp.HmacOneTimePasswordGenerator#HOTP_HMAC_ALGORITHM}).
	 */
	public TimeBasedOneTimePasswordGenerator(final Duration timeStep) throws NoSuchAlgorithmException {
		this(timeStep, HmacOneTimePasswordGenerator.DEFAULT_PASSWORD_LENGTH);
	}

	/**
	 * Constructs a new time-based one-time password generator with the given time-step
	 * and password length and a default HMAC algorithm
	 * ({@value com.eatthepath.otp.HmacOneTimePasswordGenerator#HOTP_HMAC_ALGORITHM}).
	 */
	public TimeBasedOneTimePasswordGenerator(final Duration timeStep, final int passwordLength)
			throws NoSuchAlgorithmException {
		this(timeStep, passwordLength, TOTP_ALGORITHM_HMAC_SHA1);
	}

	/**
	 * Constructs a new time-based one-time password generator with the given time-step,
	 * password length, and HMAC algorithm.
	 */
	public TimeBasedOneTimePasswordGenerator(final Duration timeStep, final int passwordLength, final String algorithm)
			throws NoSuchAlgorithmException {
		super(passwordLength, algorithm);

		this.timeStep = timeStep;
	}

	/**
	 * Generates a one-time password using the given key and timestamp.
	 */
	public int generateOneTimePassword(String password, final Instant timestamp) throws Exception {
		return this.generateOneTimePassword(password, timestamp.toEpochMilli() / this.timeStep.toMillis());
	}

	/**
	 * Returns the time step used by this generator.
	 * @return the time step used by this generator
	 */
	public Duration getTimeStep() {
		return this.timeStep;
	}

}
