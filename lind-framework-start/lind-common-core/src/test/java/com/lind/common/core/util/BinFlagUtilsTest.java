package com.lind.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class BinFlagUtilsTest {

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
		Assert.assertTrue(BinFlagUtils.addValueList(Arrays.asList(1, 2, 4, 4, 4)) == 7);
	}

}
