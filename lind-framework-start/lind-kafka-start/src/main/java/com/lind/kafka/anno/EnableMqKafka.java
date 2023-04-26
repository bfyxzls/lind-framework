package com.lind.kafka.anno;

import com.lind.kafka.config.KafkaProviderConfig;
import com.lind.kafka.proxy.MqProducerBeanDefinitionRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({ MqProducerBeanDefinitionRegistry.class, KafkaProviderConfig.class })
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface EnableMqKafka {

	/**
	 * 扫描的基础包，默认为启动类所在包.
	 * @return
	 */
	@AliasFor("basePackages")
	String[] value() default {};

	/**
	 * 扫描的基础包，默认为启动类所在包
	 * @return
	 */
	@AliasFor("value")
	String[] basePackages() default {};

}
