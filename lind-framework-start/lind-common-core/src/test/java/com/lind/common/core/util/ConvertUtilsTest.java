package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/7/13 13:40
 * @since 1.0.0
 */
public class ConvertUtilsTest {

	@Test
	public void parseString16ToLong() {
		System.out.println(ConvertUtils.parseString16ToLong("FF"));
	}

	@Test
	public void parseIntToByte() {
		int i = 128;
		byte b = (byte) i; // 显式类型转换 -128
		System.out.println(b);
		byte c = (byte) 129; // 显式类型转换 -127
		System.out.println(c);
		byte d = (byte) 255; // 显式类型转换 -1
		System.out.println(d);
		byte f = (byte) (255 + 128); // 显式类型转换 127
		System.out.println(f);
	}

}
