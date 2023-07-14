package com.lind.common.bean.factory.core;

import com.lind.common.core.util.ClassUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.HashSet;
import java.util.Set;

@Data
public class SendProxyBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private MetadataReaderFactory metadataReaderFactory;

	private ResourcePatternResolver resourcePatternResolver;

	@SneakyThrows
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
			BeanNameGenerator importBeanNameGenerator) {
		String[] basePackages = new String[] {
				Class.forName(importingClassMetadata.getClassName()).getPackage().getName() };

		// 使用自定义扫描器扫描
		Set<Class<?>> classes = new HashSet<>();
		for (String basePackage : basePackages) {
			classes.addAll(ClassUtils.scannerPackages(basePackage, Send.class, metadataReaderFactory,
					resourcePatternResolver));
		}
		for (Class beanClazz : classes) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

			MutablePropertyValues propertyValues = definition.getPropertyValues();
			propertyValues.add("interfaceType", beanClazz);
			definition.setBeanClass(SendFactoryBean.class);
			definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
			registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);

		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

}
