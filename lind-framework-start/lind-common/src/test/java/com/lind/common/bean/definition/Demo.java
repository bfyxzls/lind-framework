package com.lind.common.bean.definition;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

@Import({ RunFactoryBeanDefinitionRegistry.class, Car.class })
public class Demo {

	static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Demo.class);

	@Test
	public void run() {
		Bird bird = applicationContext.getBean(Bird.class);
		bird.run();
	}

	@Test
	public void car() {
		PrintInfo car = applicationContext.getBean(PrintInfo.class);
		car.print();
	}

	interface Bird {

		void run();

	}

}
