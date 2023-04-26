package com.lind.common.proxy.register;

import com.lind.common.proxy.anno.EnableMessage;
import com.lind.common.proxy.anno.MessageProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 1.对MessageProvider注解的接口进行注册.
 */
@Slf4j
@ConditionalOnClass(EnableMessage.class)
public class MessageProviderBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private MetadataReaderFactory metadataReaderFactory;

	private ResourcePatternResolver resourcePatternResolver;

	/**
	 * 根据包路径获取包及子包下的标识有@MessageProvider注解的类型，它将被动态代理
	 * @param basePackage basePackage
	 * @return Set<Class < ?>> Set<Class<?>>
	 */
	private Set<Class<?>> scannerPackages(String basePackage) {
		Set<Class<?>> set = new LinkedHashSet<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ StringUtils.replace(basePackage, ".", "/") + "/**/*.class";
		try {
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz;
					try {
						clazz = Class.forName(className);
						if (clazz.isAnnotationPresent(MessageProvider.class)) {
							set.add(clazz);
						}
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}

	/**
	 * 注册需要的BeanDefinition.
	 * @param importingClassMetadata
	 * @param registry
	 * @param importBeanNameGenerator
	 */
	@SneakyThrows
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
			BeanNameGenerator importBeanNameGenerator) {
		// 获取MapperScan注解属性信息
		AnnotationAttributes annotationAttributes = AnnotationAttributes
				.fromMap(importingClassMetadata.getAnnotationAttributes(EnableMessage.class.getName()));
		// 获取注解的属性值,拿到定义的扫描路径
		String[] basePackages = annotationAttributes.getStringArray("basePackages");
		if (basePackages == null || basePackages.length == 0) {
			basePackages = new String[] { Class.forName(importingClassMetadata.getClassName()).getPackage().getName() };
		}

		// 使用自定义扫描器扫描
		Set<Class<?>> classes = new HashSet<>();
		for (String basePackage : basePackages) {
			classes.addAll(scannerPackages(basePackage));
		}
		for (Class beanClazz : classes) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

			MutablePropertyValues propertyValues = definition.getPropertyValues();
			propertyValues.add("interfaceType", beanClazz);
			definition.setBeanClass(MessageProviderProxyFactoryBean.class);
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
