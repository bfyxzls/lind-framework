package com.lind.common.proxy.register;

import com.lind.common.proxy.anno.MessageSend;
import com.lind.common.proxy.handler.MessageProviderHandler;
import com.lind.common.proxy.handler.SuccessSendHandler;
import com.lind.common.proxy.service.MessageService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 3.代理一个注解为MessageProvide的接口.
 */
@Slf4j
@Data
public class MessageProviderProxy implements InvocationHandler {

	private BeanFactory applicationContext;

	private MessageService messageService;

	public MessageProviderProxy(BeanFactory applicationContext) {
		this.applicationContext = applicationContext;
		messageService = applicationContext.getBean(MessageService.class);
	}

	/**
	 * 首字母转小写
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (args.length != 1) {
			throw new IllegalArgumentException("方法只能有一个参数");
		}
		Object arg = args[0];
		if (method.isAnnotationPresent(MessageSend.class)) {
			MessageSend annotation = method.getAnnotation(MessageSend.class);

			// 获取SuccessSendHandler的实例
			Class<? extends SuccessSendHandler> sccessHandlerType = annotation.successSendHandler();
			SuccessSendHandler successHandler = applicationContext.getBean(sccessHandlerType);

			// 获取MessageProviderHandler的实例
			Class<? extends MessageProviderHandler> messageProviderHandlerType = annotation.messageProviderHandler();
			// bean的名称默认使用小驼峰的简单类名
			String beanName = messageProviderHandlerType.getSimpleName();
			MessageProviderHandler messageProviderHandler = applicationContext.getBean(toLowerCaseFirstOne(beanName),
					MessageProviderHandler.class);

			messageService.send(arg, messageProviderHandler, successHandler);
		}
		return null;
	}

}
