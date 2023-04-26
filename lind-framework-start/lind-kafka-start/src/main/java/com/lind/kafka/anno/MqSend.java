package com.lind.kafka.anno;

import com.lind.kafka.handler.DefaultFailureHandler;
import com.lind.kafka.handler.DefaultSuccessHandler;
import com.lind.kafka.handler.FailureHandler;
import com.lind.kafka.handler.SuccessHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface MqSend {

	/**
	 * 发送的主题，支持springEL表达式
	 * @return
	 */
	String topic();

	/**
	 * 主题的key，相同的key会发到相同的partition
	 * @return
	 */
	String key() default "";

	/**
	 * 消息发送成功处理函数
	 * @return
	 */
	Class<? extends SuccessHandler> successHandler() default DefaultSuccessHandler.class;

	/**
	 * 消息发送失败处理函数
	 * @return
	 */
	Class<? extends FailureHandler> failureHandler() default DefaultFailureHandler.class;

}
