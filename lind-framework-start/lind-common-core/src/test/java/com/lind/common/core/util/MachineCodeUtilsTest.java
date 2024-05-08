package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/7/13 14:57
 * @since 1.0.0
 */
public class MachineCodeUtilsTest {

	@Test
	public void getMachineCode() {
		System.out.println(MachineCodeUtils.getCPUSerial());
	}

}
