package com.lind.common.bean.definition;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

@Import({ RunFactoryBeanDefinitionRegistry.class })
public class Demo {

	@Test
	public void run() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Demo.class);
		Bird bird = applicationContext.getBean(Bird.class);
		bird.run();
	}

	interface Bird {

		void run();

	}

}
