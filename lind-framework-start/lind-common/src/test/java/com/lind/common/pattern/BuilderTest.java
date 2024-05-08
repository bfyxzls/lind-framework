package com.lind.common.pattern;

import com.lind.common.pattern.builder.Builder;
import com.lind.common.pattern.builder.ConcreteBuilder;
import com.lind.common.pattern.builder.Director;
import com.lind.common.pattern.builder.Product;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2024/4/23 14:37
 * @since 1.0.0
 */
public class BuilderTest {

	@Test
	public void createProductTest() {
		Builder builder = new ConcreteBuilder();
		Director director = new Director(builder);

		director.construct();
		Product product = builder.getResult();

		System.out.println(product);
	}

}
