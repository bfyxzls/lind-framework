package com.lind.common.core.util;

import lombok.Data;
import org.junit.jupiter.api.Test;
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

	@Test
	public void mapOr() {
		Optional<String> optionalString = Optional.of("AAE");
		String result = optionalString.filter(s -> s.startsWith("A")).map(String::toLowerCase).orElse("default");
		System.out.println("result1:" + result);// aae
		result = optionalString.filter(s -> s.startsWith("B")).map(String::toLowerCase).orElse("default");
		System.out.println("result2:" + result);// default
		result = optionalString.filter(s -> s.startsWith("B")).map(String::toLowerCase)
				.orElse(optionalString.orElse(""));
		System.out.println("result3:" + result);// AAE

	}

}
