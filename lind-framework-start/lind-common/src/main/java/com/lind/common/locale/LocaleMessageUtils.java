package com.lind.common.locale;

import com.lind.common.core.util.SpringContextUtils;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化消息配置 LocalMessageConfig中配置了resources/i18n/messages_zh_CN.properties. 通过
 * LocaleContextHolder.getLocale()获取客户端浏览器的语言环境，就是请求头中的Accept-Language的值，再根据它进行国际化消息的获取。
 *
 * @UtilityClass所有的方法和属性都会被加上static关键字，并且该类会创建一个私有的空参构造器
 */
@UtilityClass
public class LocaleMessageUtils {

	/**
	 * 通过code 获取错误信息
	 * @param code
	 * @return
	 */
	public String getMessage(String code) {
		return getMessage(code, null);
	}

	/**
	 * 通过code 和参数获取错误信息
	 * @param code
	 * @return
	 */
	public String getMessage(String code, Object... objects) {
		MessageSource messageSource = SpringContextUtils.getBean("messageSource");
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(code, null, locale);
	}

}
