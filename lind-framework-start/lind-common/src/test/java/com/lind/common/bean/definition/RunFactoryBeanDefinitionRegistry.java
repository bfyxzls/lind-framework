package com.lind.common.bean.definition;

import lombok.SneakyThrows;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 构建一个FactoryBean对像.
 */
public class RunFactoryBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar {

	@SneakyThrows
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
			BeanDefinitionRegistry beanDefinitionRegistry) {
		Class beanClazz = Demo.Bird.class;// 这块需要确定可以被代码的接口或者类
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
		GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

		MutablePropertyValues propertyValues = definition.getPropertyValues();
		propertyValues.add("interfaceType", beanClazz);
		definition.setBeanClass(RunFactoryBean.class);
		definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
		beanDefinitionRegistry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
	}

}
