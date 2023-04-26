package com.lind.common.util;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

import static com.lind.common.util.BinFlagUtils.addValueList;
import static com.lind.common.util.BinFlagUtils.hasValue;
import static com.lind.common.util.BinFlagUtils.isTowPower;
import static com.lind.common.util.BinFlagUtils.splitBinPower;

@Slf4j
public class BinFlagUtilsTest {

	@Test
	public void splitBinPowerTest() {
		for (Integer integer : splitBinPower(15)) {
			log.info("o={}", integer);
		}
	}

	@Test
	public void isTowPowerTest() {
		Assert.isTrue(isTowPower(4));
		Assert.isFalse(isTowPower(5));
	}

	@Test
	public void hasValueTest() {
		Assert.isTrue(hasValue(15, 8));
	}

	@Test
	public void addValueListTest() {
		Assert.isTrue(addValueList(Arrays.asList(1, 2, 4, 4, 4)) == 7);
	}

}
