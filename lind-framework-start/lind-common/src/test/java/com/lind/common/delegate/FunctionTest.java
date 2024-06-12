package com.lind.common.delegate;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

/**
 * @author lind
 * @date 2024/6/11 9:46
 * @since 1.0.0
 */
public class FunctionTest {

	@Test
	public void test1() {
		Function<Integer, String> converter = (num) -> "Number: " + num;
		String result = converter.apply(100);
		System.out.println(result);
	}

}
