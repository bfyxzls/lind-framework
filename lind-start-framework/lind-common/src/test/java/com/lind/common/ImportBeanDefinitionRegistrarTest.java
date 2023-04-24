package com.lind.common;

import com.lind.common.registrar.HelloService;
import com.lind.common.registrar.PeoImportBeanDefinitionRegistrar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@ComponentScan(basePackages = { "com.lind.common.registrar" })
@RunWith(SpringRunner.class)
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
