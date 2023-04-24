package com.lind.kafka.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 消息提供者注解，name作为spring容器的名称，默认为被注解的class名
 *
 * @author BD-PC220
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface MqProducer {

	@AliasFor("value")
	String name() default "";

	@AliasFor("name")
	String value() default "";

}
