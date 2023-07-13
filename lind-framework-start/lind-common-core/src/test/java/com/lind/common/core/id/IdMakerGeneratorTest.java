package com.lind.common.core.id;

import org.junit.Test;

/**
 * @author lind
 * @date 2023/7/13 14:50
 * @since 1.0.0
 */
public class IdMakerGeneratorTest {

	@Test
	public void getId() {
		System.out.println(IdMakerGenerator.generateId(127));
	}

}
