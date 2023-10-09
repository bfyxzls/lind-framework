package com.lind.common.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author lind
 * @date 2022/7/4 13:08
 * @description
 */
@Configuration
public class LocalMessageConfig {

	/**
	 * 与客户端浏览器中的语言环境相关的MessageSource. 系统国际化文件配置.
	 * 文件位于resources/i18n/message_{locale}.properties
	 * 例如：message_zh_CN.properties,message_en_US.properties
	 * @return MessageSource
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");// 加载resources/messages_zh_CN.properties文件，根据你的区域语言来加载对应的
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
