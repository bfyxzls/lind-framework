package com.lind.common.delegate;

import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

/**
 * @author lind
 * @date 2024/6/11 9:47
 * @since 1.0.0
 */
public class BiConsumerTest {

	@Test
	public void test1() {
		BiConsumer<String, Integer> biConsumer = (str, num) -> System.out.println(str + ": " + num);
		biConsumer.accept("Value", 10);
	}

}
