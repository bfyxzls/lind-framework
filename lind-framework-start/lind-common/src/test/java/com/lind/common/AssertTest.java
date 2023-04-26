package com.lind.common;

import cn.hutool.core.lang.Assert;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/4/24 15:22
 * @since 1.0.0
 */
public class AssertTest {

	@Test
	public void notNull() {
		String str = null;
		Assert.notNull(str, "不能为空");
	}

	@Test
	public void notEmpty() {
		String str = "";
		Assert.notEmpty(str, "不能为空");
	}

	@Test
	public void notBlank() {
		String str = " ";
		Assert.notBlank(str, "不能为空");
	}

	@Test
	public void notBlank2() {
		String str = null;
		Assert.notBlank(str, "不能为空");
	}

	@Test
	public void notArrayNull() {
		String[] str = null;
		Assert.notEmpty(str, "不能为空");
	}

	@Test
	public void notArrayEmpty() {
		String[] str = new String[0];
		Assert.notEmpty(str, "不能为空");

	}

}
