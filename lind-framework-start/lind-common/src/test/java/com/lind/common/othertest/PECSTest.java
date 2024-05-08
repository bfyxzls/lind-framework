package com.lind.common.othertest;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全称是Producer Extends Consumer Super 使用extends确定上界的只能是生产者，只能往外生产东西，取出的就是上界类型。不能往里塞东西。
 * 使用Super确定下界的只能做消费者，只能往里塞东西。取出的因为无法确定类型只能转成Object类型看代码更好理解。
 */
public class PECSTest {

	@Test
	public void pecsExtends() {
		List<Integer> arr = Arrays.asList(1, 2, 3);
		List<? extends Number> numbers = new ArrayList(arr);
		numbers.forEach(System.out::println);
	}

	@Test
	public void pecsSuper() {
		List<Integer> arr = Arrays.asList(1, 2, 3);
		List<? super Integer> numbers = new ArrayList(arr);
		numbers.add(4);

		numbers.forEach(o -> {
			Object a = o;
		});
	}

}
