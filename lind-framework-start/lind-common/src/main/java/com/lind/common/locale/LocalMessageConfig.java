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
	 * 系统国际化文件配置. 文件位于resources/i18n/message_{locale}.properties
	 * 例如：message_zh_CN.properties,message_en_US.properties
	 * @return MessageSource
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/message");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

}
