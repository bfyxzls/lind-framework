package com.lind.kafka.proxy;

import com.lind.kafka.anno.EnableMqKafka;
import com.lind.kafka.anno.MqProducer;
import lombok.SneakyThrows;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 装载目录下的bean，为MessageProvider注册动态代理. 注册FactoryBean.
 **/
@ConditionalOnClass(EnableMqKafka.class)
public class MqProducerBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private MetadataReaderFactory metadataReaderFactory;

	private ResourcePatternResolver resourcePatternResolver;

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

	/**
	 * 用"/"替换包路径中"."
	 * @param path
	 * @return
	 */
	private String replaceDotByDelimiter(String path) {
		return StringUtils.replace(path, ".", "/");
	}

	/**
	 * 根据包路径获取包及子包下的所有类
	 * @param basePackage basePackage
	 * @return Set<Class < ?>> Set<Class<?>>
	 */
	private Set<Class<?>> scannerPackages(String basePackage) {
		Set<Class<?>> set = new LinkedHashSet<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + replaceDotByDelimiter(basePackage)
				+ '/' + DEFAULT_RESOURCE_PATTERN;
		try {
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz;
					try {
						clazz = Class.forName(className);
						if (clazz.isAnnotationPresent(MqProducer.class)) {
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

	@SneakyThrows
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
			BeanDefinitionRegistry beanDefinitionRegistry) {
		// 获取所有注解的属性和值
		AnnotationAttributes annotationAttributes = AnnotationAttributes
				.fromMap(annotationMetadata.getAnnotationAttributes(EnableMqKafka.class.getName()));
		// 获取到basePackages的值
		String[] basePackages = annotationAttributes.getStringArray("basePackages");
		// 如果没有设置basePackages 扫描路径,就扫描对应包下面的值
		if (basePackages.length == 0) {
			if (!(annotationMetadata instanceof StandardAnnotationMetadata)) {
				Class<?> aClass = null;
				aClass = ClassUtils.forName(annotationMetadata.getClassName(), null);
				annotationMetadata = new StandardAnnotationMetadata(aClass, true);
			}
			basePackages = new String[] {
					((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName() };
		}

		Set<Class<?>> classes = new HashSet<>();
		// 装截MessageProvider注解的对象
		for (String basePackage : basePackages) {
			classes.addAll(scannerPackages(basePackage));
		}
		for (Class beanClazz : classes) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

			MutablePropertyValues propertyValues = definition.getPropertyValues();
			propertyValues.add("interfaceType", beanClazz);
			definition.setBeanClass(MqProducerFactoryBean.class);
			definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
			beanDefinitionRegistry.registerBeanDefinition(beanClazz.getSimpleName(), definition);

		}

	}

}
