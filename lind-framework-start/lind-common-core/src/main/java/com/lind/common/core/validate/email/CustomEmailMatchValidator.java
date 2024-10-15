package com.lind.common.core.validate.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lind
 * @date 2023/7/20 16:36
 * @since 1.0.0
 */
public class CustomEmailMatchValidator implements ConstraintValidator<CustomEmailMatch, String> {

	@Override
	public void initialize(CustomEmailMatch constraintAnnotation) {
		// 可以在此方法中获取约束注解中的信息并进行初始化设置
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// 在此方法中实现自定义的email验证逻辑
		// 返回true表示验证通过，返回false表示验证失败
		// 可以根据需要在此方法中编写逻辑来验证value是否为合法的email地址
		// 例如，使用正则表达式或其他逻辑进行验证
		return value != null && value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	}

}
