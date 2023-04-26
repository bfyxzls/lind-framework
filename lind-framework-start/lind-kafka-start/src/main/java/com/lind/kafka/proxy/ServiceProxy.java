package com.lind.kafka.proxy;

import com.lind.kafka.anno.MqSend;
import com.lind.kafka.entity.MessageEntity;
import com.lind.kafka.handler.FailureHandler;
import com.lind.kafka.handler.SuccessHandler;
import com.lind.kafka.producer.MessageSender;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
 *
 * @author lind
 */
@Slf4j
@Data
public class ServiceProxy<T> implements InvocationHandler {

	private MessageSender messageSender;

	private BeanFactory applicationContext;

	public ServiceProxy(BeanFactory applicationContext) {
		this.applicationContext = applicationContext;
		this.messageSender = applicationContext.getBean(MessageSender.class);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (args.length != 1) {
			throw new IllegalArgumentException("发送的消息只能是MessageEntity的子类且只能有一个参数");
		}

		Object arg = args[0];
		if (!MessageEntity.class.isAssignableFrom(arg.getClass())) {
			throw new IllegalArgumentException("参数必须是MessageEntity的子类");
		}
		// 从MessageSend注解中拿到topic和handler的信息
		if (method.isAnnotationPresent(MqSend.class)) {
			MqSend annotation = method.getAnnotation(MqSend.class);
			String topic = annotation.topic();

			Assert.hasText(topic, "发送主题不能为空，支持springEL表达式，请重新设置");
			// 解析springEL表达式
			topic = evaluateExpression(topic);
			Class<? extends SuccessHandler> sccessHandlerType = annotation.successHandler();
			SuccessHandler successHandler = applicationContext.getBean(sccessHandlerType);
			Class<? extends FailureHandler> failureHandlerType = annotation.failureHandler();
			FailureHandler failureHandler = applicationContext.getBean(failureHandlerType);
			messageSender.send(topic, (MessageEntity) arg, successHandler, failureHandler);
		}
		return null;
	}

	/**
	 * 解析springEL表达式
	 * @param myString
	 * @return
	 */
	public String evaluateExpression(String myString) {
		StringValueResolver str = new EmbeddedValueResolver((ConfigurableBeanFactory) applicationContext);
		return str.resolveStringValue(myString);
	}

}
