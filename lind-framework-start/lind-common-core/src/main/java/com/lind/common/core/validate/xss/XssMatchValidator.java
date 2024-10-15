package com.lind.common.core.validate.xss;

import com.lind.common.core.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义xss校验注解实现
 *
 * @author ruoyi
 */
public class XssMatchValidator implements ConstraintValidator<XssMatch, String> {

	private static final String HTML_PATTERN = "<(\\S*?)[^>]*>.*?|<.*? />";

	public static boolean containsHtml(String value) {
		Pattern pattern = Pattern.compile(HTML_PATTERN);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		return !containsHtml(value);
	}

}
