package com.lind.common.proxy.anno;

import com.lind.common.proxy.handler.DefaultMessageProviderHandler;
import com.lind.common.proxy.handler.DefaultSuccessSendHandler;
import com.lind.common.proxy.handler.MessageProviderHandler;
import com.lind.common.proxy.handler.SuccessSendHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Inherited
public @interface MessageSend {

	/**
	 * 消息成功后的事件
	 * @return
	 */
	Class<? extends SuccessSendHandler> successSendHandler() default DefaultSuccessSendHandler.class;

	/**
	 * 消息发送者.
	 * @return
	 */
	Class<? extends MessageProviderHandler> messageProviderHandler() default DefaultMessageProviderHandler.class;

}
