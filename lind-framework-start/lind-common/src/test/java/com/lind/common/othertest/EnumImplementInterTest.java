package com.lind.common.othertest;

import org.junit.Test;

/**
 * 使用枚举使用的多态.
 */
public class EnumImplementInterTest {

	@Test
	public void test() {
		Strategy.CHINA.print();
		Strategy.US.print();
	}

	enum Strategy implements Hello {

		CHINA() {
			@Override
			public void print() {
				System.out.println("中文");
			}
		},
		US() {
			@Override
			public void print() {
				System.out.println("eng for us");
			}
		}

	}

	interface Hello {

		void print();

	}

}
