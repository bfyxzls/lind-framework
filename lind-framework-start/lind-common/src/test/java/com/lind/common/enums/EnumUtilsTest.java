package com.lind.common.enums;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

enum Calculate {

	SQUARE(1, Cal::square), CUBE(2, Cal::cube);

	private final Integer code;

	private final Function<Integer, Integer> function;

	Calculate(Integer code, Function<Integer, Integer> function) {
		this.code = code;
		this.function = function;
	}

	public static Calculate get(Integer code) {
		for (Calculate e : values()) {
			if (code.equals(e.getCode())) {
				return e;
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public Integer cal(Integer val) {
		return this.function.apply(val);
	}

}

public class EnumUtilsTest {

	@Test
	public void isExist() {
		Assert.assertTrue(EnumUtils.isExist(Test1.values(), "01"));
		Assert.assertFalse(EnumUtils.isExist(Test1.values(), "03"));
	}

	@Test
	public void enumFun() {
		Calculate cube = Calculate.get(2);
		Assert.assertEquals(1000, (int) cube.cal(10));
	}

}

class Cal {

	public static Integer square(Integer val) {
		return val * val;
	}

	public static Integer cube(Integer val) {
		return val * val * val;
	}

}
