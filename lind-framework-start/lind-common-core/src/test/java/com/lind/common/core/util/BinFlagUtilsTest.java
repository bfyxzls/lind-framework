package com.lind.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BinFlagUtilsTest {

	static Map<Integer, String> lawyerFlags = new HashMap<>();

	static {
		lawyerFlags.put(1, "律师职业证号");
		lawyerFlags.put(2, "联系电话");
		lawyerFlags.put(4, "联系邮箱");
		lawyerFlags.put(8, "专业领域");
		lawyerFlags.put(16, "服务地区");
		lawyerFlags.put(32, "律所网站");
	}

	@Test
	public void splitBinPowerTest() {
		for (Integer integer : BinFlagUtils.splitBinPower(15)) {
			log.info("o={}", integer);
		}
	}

	@Test
	public void isTowPowerTest() {
		Assert.assertTrue(BinFlagUtils.isTowPower(4));
		Assert.assertFalse(BinFlagUtils.isTowPower(5));
	}

	@Test
	public void hasValueTest() {
		Assert.assertTrue(BinFlagUtils.hasValue(15, 8));
	}

	@Test
	public void addValueListTest() {
		Assert.assertEquals(7, (int) BinFlagUtils.increaseValueList(Arrays.asList(1, 2, 4, 4, 4)));
	}

	@Test
	public void binFlagRanges() {
		int valDb = 11;
		int valSel = 7;
		for (int i = 1; i <= 32; i *= 2) {
			if ((valSel & i) == i) {
				System.out.println(lawyerFlags.get(i));
			}
			if ((valDb & i) == i && (valSel & i) < 1) {
				System.out.println("删除了:" + lawyerFlags.get(i));
			}
		}
	}

	/**
	 * 判断组定的数是否为2的指数幂
	 */
	@Test
	public void ifBinFlag() {
		for (int i = 1; i < 33; i++) {
			if ((i & (i - 1)) == 0) {
				System.out.println(i);
			}
		}
		int a = 6 & 5;// 110 & 101 = 100 (4)
		int b = 8 & 7;// 1000 & 0111=0 所有2的n次方-1都是最高位1，其它位0的，类似f,ff,fff,ffff
	}

}
