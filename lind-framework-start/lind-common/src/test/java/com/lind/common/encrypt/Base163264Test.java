package com.lind.common.encrypt;

import com.lind.common.core.util.ObjectByteUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Base163264Test {

	@Test
	public void base64() {
		String toEncoded = HashUtils.encryptBASE64("12345678_f_20200612_D_100_V6".getBytes());
		System.out.println(toEncoded);
		System.out.println(new String(HashUtils.decryptBASE64(toEncoded)));
	}

	@Test
	public void base16() {
		String toEncoded = HashUtils.encryptBASE16("12345678_f_20200612_D_100_V6".getBytes());
		System.out.println(toEncoded);
		System.out.println(new String(HashUtils.decryptBASE16(toEncoded)));

	}

	@Test
	public void decodeBase64Date() {
		byte[] toEncoded1 = HashUtils.decryptBASE64("rO0ABXNyAA5qYXZhLnV0aWwuRGF0ZWhqgQFLWXQZAwAAeHB3CAAAAXNs9AwAeA==");
		byte[] toEncoded2 = HashUtils.decryptBASE64("rO0ABXNyAA5qYXZhLnV0aWwuRGF0ZWhqgQFLWXQZAwAAeHB3CAAAAXNYWpwAeA==");
		log.info("{}", ObjectByteUtils.toObject(toEncoded1));
		log.info("{}", ObjectByteUtils.toObject(toEncoded2));

	}

	@Test
	public void decodeBase64Str() {
		byte[] caseClassName1 = HashUtils.decryptBASE64(
				"rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAADdwQAAAADdAADMDAxdAAGMDAxMDAxdAAJMDAxMDAxMDAxeA==");
		byte[] caseClassName2 = HashUtils.decryptBASE64(
				"rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAACdwQAAAACdAADMDAzdAAGMDAzMDAxeA==");
		log.info("{}", ObjectByteUtils.toObject(caseClassName1));
		log.info("{}", ObjectByteUtils.toObject(caseClassName2));
	}

}
