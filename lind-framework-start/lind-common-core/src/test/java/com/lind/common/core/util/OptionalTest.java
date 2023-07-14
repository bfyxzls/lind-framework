package com.lind.common.core.util;

import lombok.Data;
import org.junit.Test;
import org.springframework.util.Assert;

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
		Assert.isNull(a.getName(), "a.name is not null");

	}

}
