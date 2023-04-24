package com.lind.common.proxy.anno;

import com.lind.common.proxy.register.MessageProviderBeanDefinitionRegistry;
import com.lind.common.proxy.register.MessageConfig;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Import(value = { MessageProviderBeanDefinitionRegistry.class, MessageConfig.class })
public @interface EnableMessage {

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
