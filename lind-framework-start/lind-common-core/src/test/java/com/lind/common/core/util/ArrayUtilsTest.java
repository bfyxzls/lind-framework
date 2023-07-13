package com.lind.common.core.util;

import org.junit.Test;

import static com.lind.common.core.util.ArrayUtils.contains;
import static com.lind.common.core.util.ArrayUtils.isEmpty;

/**
 * @author lind
 * @date 2023/7/13 11:05
 * @since 1.0.0
 */
public class ArrayUtilsTest {

	@Test
	public void main() {
		System.out.println(isEmpty(new String[] { "a", "b", "c" }));
		System.out.println(contains(StringUtils.split("1,2,3", ","), String.valueOf(1)));
		System.out.println(contains(StringUtils.split("1,2,3", ","), String.valueOf(11)));
	}

}
