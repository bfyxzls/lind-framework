package com.lind.common;

import org.junit.Test;

/**
 * 取模.
 */
public class ModTest {

	@Test
	public void mod() {
		System.out.println("8 % 16=" + 8 % 16);
		// 2的N次方数：二进制与运算等同于模运算
		System.out.println(8 & (16 - 1));

		System.out.println("7 % 5=" + 7 % 5);
		System.out.println(7 & (5 - 1));

		System.out.println("47 % 32=" + 47 % 32);
		System.out.println(47 & (32 - 1));

		System.out.println("47 % 16=" + 47 % 16);
		System.out.println(47 & (16 - 1));

		System.out.println("7 % 4=" + 7 % 4);
		System.out.println(7 & (4 - 1));
	}

	@Test
	public void bucket() {
		System.out.println("0~5之间");
		System.out.println("8 % 5=" + 8 % 5);
		System.out.println("9 % 5=" + 9 % 5);
		System.out.println("10 % 5=" + 10 % 5);
	}

}
