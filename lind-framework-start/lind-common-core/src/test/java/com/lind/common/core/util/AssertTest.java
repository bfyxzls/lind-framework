package com.lind.common.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 断言测试.
 *
 * @author lind
 * @date 2024/11/11 15:49
 * @since 1.0.0
 */
public class AssertTest {

	@Test
	public void isTrue() {
		Assert.isTrue(1 == 2, "1不等于2");
	}

	@Test
	public void isNull() {
		Integer a = null;
		Assert.isNull(a, "1为空");
	}

	@Test
	public void requireNonNull() {
		Integer a = 0;
		// 非空检查
		Objects.requireNonNull(a);
	}

}
