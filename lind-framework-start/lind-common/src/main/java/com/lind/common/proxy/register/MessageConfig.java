package com.lind.common.proxy.register;

import com.lind.common.proxy.handler.DefaultMessageProviderHandler;
import com.lind.common.proxy.handler.DefaultSuccessSendHandler;
import com.lind.common.proxy.handler.MessageProviderHandler;
import com.lind.common.proxy.handler.SuccessSendHandler;
import com.lind.common.proxy.service.DefaultMessageService;
import com.lind.common.proxy.service.MessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

	@Bean
	@ConditionalOnMissingBean
	public MessageService messageService() {
		return new DefaultMessageService();
	}

	@Bean
	@ConditionalOnMissingBean
	public SuccessSendHandler successSendHandler() {
		return new DefaultSuccessSendHandler();
	}

	@Bean
	public MessageProviderHandler consoleMessageProviderHandler() {
		return new DefaultMessageProviderHandler();
	}

}
