package com.lind.common.locale;

import cn.hutool.extra.spring.SpringUtil;
import com.lind.common.util.SpringContextUtils;
import lombok.experimental.UtilityClass;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * 国际化消息配置 LocalMessageConfig中配置了resources/i18n/messages_zh_CN.properties.
 *
 * @UtilityClass所有的方法和属性都会被加上static关键字，并且该类会创建一个私有的空参构造器
 */
@UtilityClass
public class LocaleMessageUtils {

	/**
	 * 通过code 获取中文错误信息
	 * @param code
	 * @return
	 */
	public String getMessage(String code) {
		MessageSource messageSource = SpringContextUtils.getBean("messageSource");
		return messageSource.getMessage(code, null, Locale.CHINA);
	}

	/**
	 * 通过code 和参数获取中文错误信息
	 * @param code
	 * @return
	 */
	public String getMessage(String code, Object... objects) {
		MessageSource messageSource = SpringUtil.getBean("messageSource");
		return messageSource.getMessage(code, objects, Locale.CHINA);
	}

}
