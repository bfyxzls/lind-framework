package com.lind.common.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author lind
 * @date 2023/6/21 15:35
 * @since 1.0.0
 */
public class CollectionUtilTest {

	@Test
	public void join() {
		Collection<String> list = Arrays.asList("1", "a", "2", "b");
		System.out.println(CollectionUtil.join(list));
	}

}
