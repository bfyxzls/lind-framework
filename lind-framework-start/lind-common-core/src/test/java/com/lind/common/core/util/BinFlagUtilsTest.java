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

	/**
	 * 当进行取模运算时，如果被模数是2的N次方，可以使用位运算来代替取模运算
	 * 这是因为对于2的N次方的数，其二进制表示中只有一个1，其他位都是0，这样在进行取模运算时，可以通过位运算（与操作）来实现取模，而不需要使用乘法和除法等运算，从而提高了运算效率
	 * x & (m - 1) [m = 2^n]
	 */
	@Test
	public void mod() {
		int x = 10;
		int m = 8;
		int mod = x & (m - 1);
		log.info("mod={}", mod);
	}

	/**
	 * 在int64范围内，最大的2的幂次方的范围
	 */
	@Test
	public void maxFlags() {
		long sum = 0; // max:9223372036854775807,最大可用范围到2^62，可以正常存储62个位数
		for (int i = 0; i < 64; i++) {
			double val = Math.pow(2, i);
			sum += val;
			log.info("i={},val={},sum={}", i, (long) val, sum);
		}
	}

	@Test
	public void bitMove() {
		Long result = 4096L << 1;
		Assert.assertEquals(8192, result.longValue());
	}

}
