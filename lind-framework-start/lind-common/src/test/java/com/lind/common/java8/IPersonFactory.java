package com.lind.common.java8;

/**
 * @author lind
 * @date 2023/6/20 15:18
 * @since 1.0.0
 */
@FunctionalInterface
public interface IPersonFactory<P extends StreamTest.Person> {

	P create(String firstName, String lastName);

	default void print() {
		System.out.println("print hello.");
	}

}
