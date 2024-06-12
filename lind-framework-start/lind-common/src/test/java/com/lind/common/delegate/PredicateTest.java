package com.lind.common.delegate;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

/**
 * @author lind
 * @date 2024/6/11 9:47
 * @since 1.0.0
 */
public class PredicateTest {

	@Test
	public void test1() {
		Predicate<Integer> isPositive = (num) -> num > 0;
		boolean result = isPositive.test(5);
		System.out.println(result);
	}

}
