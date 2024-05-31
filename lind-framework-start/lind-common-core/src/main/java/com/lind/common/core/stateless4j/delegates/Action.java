package com.lind.common.core.stateless4j.delegates;

/**
 * Represents an operation that accepts no input arguments and returns no result.
 */
@FunctionalInterface
public interface Action {

	/**
	 * Performs this operation
	 */
	void doIt();

}
