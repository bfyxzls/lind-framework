package com.lind.common.core.util;

import org.junit.Test;

/**
 * @author lind
 * @date 2023/7/13 10:24
 * @since 1.0.0
 */
public class BinHexSwitchUtilsTest {

	@Test
	public void hexStringToBinRightZero() {
		System.out.println(BinHexSwitchUtils.hexStringToBinRightZero("1", 8));
	}

	@Test
	public void hexStringToBinLeftZero() {
		System.out.println(BinHexSwitchUtils.hexStringToBinLeftZero("1", 8));
	}

	@Test
	public void hexStringToBin() {
		System.out.println(BinHexSwitchUtils.hexStringToBin("ffff"));
	}

	@Test
	public void binStringToHex() {
		System.out.println(BinHexSwitchUtils.binStringToHex("1111111111111111"));
	}

}
