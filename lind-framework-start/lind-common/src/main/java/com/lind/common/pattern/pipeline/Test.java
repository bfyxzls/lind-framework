package com.lind.common.pattern.pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author lind
 * @date 2023/6/30 9:35
 * @since 1.0.0
 */
@SpringBootApplication
public class Test {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Test.class, args);
		HandlerFactory handlerFactory = applicationContext.getBean(HandlerFactory.class);
		AbstractHandler<Request> abstractHandler = handlerFactory.createHandler();
		abstractHandler.handleRequest(new Request("1", "user1"));
	}

}
