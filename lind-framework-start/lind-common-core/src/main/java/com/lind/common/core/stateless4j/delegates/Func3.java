package com.lind.common.core.stateless4j.delegates;

/**
 * Represents a function that accepts an input and produces a result
 *
 * @param <T1> Input argument type
 * @param <T2> Input argument type
 * @param <R> Result type
 */
@FunctionalInterface
public interface Func3<T1, T2, R> {

	/**
	 * Applies this function to the given input
	 * @param arg1 Input argument
	 * @param arg2 Input argument
	 * @return Result
	 */
	R call(T1 arg1, T2 arg2);

}
