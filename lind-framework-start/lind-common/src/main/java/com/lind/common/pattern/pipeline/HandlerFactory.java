package com.lind.common.pattern.pipeline;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lind
 * @date 2023/6/30 14:51
 * @since 1.0.0
 */
@Component
public class HandlerFactory implements ApplicationContextAware {

	static ApplicationContext applicationContext;

	public <T extends Request> AbstractHandler<T> createHandler() {
		Map<String, AbstractHandler> beans = applicationContext.getBeansOfType(AbstractHandler.class);
		AbstractHandler<T> handler = null;
		AbstractHandler<T> previousHandler = null;
		for (AbstractHandler<T> currentHandler : beans.values().stream().sorted().collect(Collectors.toList())) {
			// 处理获取到的泛型类型的Bean
			if (previousHandler != null) {
				previousHandler.setNextHandler(currentHandler);
			}
			else {
				handler = currentHandler;
			}
			previousHandler = currentHandler;

		}

		return handler;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
