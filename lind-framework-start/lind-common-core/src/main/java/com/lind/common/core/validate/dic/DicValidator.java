package com.lind.common.core.validate.dic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字典值约束校验器.
 */
public class DicValidator implements ConstraintValidator<ValidDic, Integer> {

	private String[] values;

	@Override
	public void initialize(ValidDic validDic) {
		this.values = validDic.value();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
		boolean isValid = false;
		if (value == null) {
			// 当状态为空时使用默认值
			return true;
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(String.valueOf(value))) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}

}
