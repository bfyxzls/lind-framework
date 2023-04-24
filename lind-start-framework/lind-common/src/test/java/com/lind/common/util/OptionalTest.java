package com.lind.common.util;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.junit.Test;

import java.util.Optional;

public class OptionalTest {

	@Test
	public void nullable() {
		@Data
		class A {

			private String name;

		}
		A a = null;
		a = Optional.ofNullable(a).orElse(new A());
		System.out.println("result:" + a);
		Assert.notNull(a.getName(), "a.name is not null");

	}

}
