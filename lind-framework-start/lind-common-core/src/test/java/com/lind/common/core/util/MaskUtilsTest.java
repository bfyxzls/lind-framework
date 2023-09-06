package com.lind.common.core.util;

import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2022/11/17 14:15
 * @since 1.0.0
 */
public class MaskUtilsTest {

	org.slf4j.Logger logger = LoggerFactory.getLogger(MaskUtilsTest.class);

	@Test
	public void inc() {
		MaskUtils maskUtils = new MaskUtils(16L); // init:16,max:31,range:15
		maskUtils.toRangeInc(2L);
		logger.info("range={}", maskUtils.toRangeInc(2L));
		logger.info("inc={}", maskUtils.toInc(maskUtils.toRangeInc(2L)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void incExpected() {
		MaskUtils maskUtils = new MaskUtils(32L);
		maskUtils.toRangeInc(32L);
	}

	@Test
	public void containPremission() {
		int READ_PERMISSION = 1; // 读权限
		int WRITE_PERMISSION = 2; // 写权限
		int EXECUTE_PERMISSION = 4; // 执行权限
		int MAX_GRANT = (int) Math.pow(2, 30); // 最大授权数,注意int最大值为2的31次方-1,就像long最大值一样，也是2的63次方-1【表示2和31异或，结果为29，不可不是指数运算】

		// 模拟用户权限
		int userPermission = WRITE_PERMISSION | READ_PERMISSION | MAX_GRANT;
		System.out.println("READ_PERMISSION:" + ((userPermission & READ_PERMISSION) != 0));
		System.out.println("WRITE_PERMISSION:" + ((userPermission & WRITE_PERMISSION) != 0));
		System.out.println("EXECUTE_PERMISSION:" + ((userPermission & EXECUTE_PERMISSION) != 0));
		System.out.println("MAX_GRANT:" + ((userPermission & MAX_GRANT) != 0));

	}

	@Test
	public void sum() {
		int sum = 0;
		for (int i = 1; i < 32; i++) {
			sum += Math.pow(2, i);
		}
		System.out.println("sum=" + sum); // 2147483647 2^32-1 即：2^1 + 2^2.......2^(n-1) =
											// 2^n - 1
	}

	// 只保留低4位的值.
	@Test
	public void clearHigh() {
		int value = 0b11011010; // 二进制表示的整数
		int mask = 0b00001111; // 用于提取低4位的掩码
		int result = value & mask; // 结果将是低4位的值
		System.out.println("result=" + result);
	}

	// 提取颜色.
	@Test
	public void extractColor() {
		int pixelColor = 0xFFAABBCC; // 一个32位的颜色值
		int redMask = 0xFF0000; // 红色通道的掩码
		int redChannel = (pixelColor & redMask) >> 16; // 提取红色通道值
		System.out.println("redChannel=" + redChannel);

		int greenChannel = (pixelColor & 0x00ff00) >> 8; // 提取绿色通道值,每个16进制数是4位，2个16进制是8位，所以右移8位
		System.out.println("greenChannel=" + greenChannel);

		int blueChannel = (pixelColor & 0x0000ff); // 提取蓝色通道值
		System.out.println("blueChannel=" + blueChannel);

	}
}
