package com.lind.common.othertest;

import com.lind.common.registrar.HelloService;
import com.lind.common.registrar.PeoImportBeanDefinitionRegistrar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan(basePackages = { "com.lind.common.registrar" })
@SpringBootTest
@Import(PeoImportBeanDefinitionRegistrar.class)
public class ImportBeanDefinitionRegistrarTest {

	@Autowired
	HelloService helloService;

	@Test
	public void test() {
		helloService.print();
	}

}
