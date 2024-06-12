package com.lind.common.delegate;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

/**
 * @author lind
 * @date 2024/6/11 9:46
 * @since 1.0.0
 */
public class SupplierTest {

	@Test
	public void test1() {
		Supplier<Double> supplier = () -> Math.random();
		double randomValue = supplier.get();
		System.out.println(randomValue);
	}

}
