package com.lind.common.core.validate.flag;

import com.lind.common.core.util.BinFlagUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * 二进制标识约束校验器，保证值为2的N次幂.
 *
 * @author lind
 * @date 2024/2/20 9:15
 * @since 1.0.0
 */
public class FlagMatchesValidator implements ConstraintValidator<FlagMatches, List<? extends Number>> {

	@Override
	public boolean isValid(List<? extends Number> values, ConstraintValidatorContext context) {
		if (values == null) {
			return true;
		}
		Boolean flag = true;
		for (Number o : values) {
			if (!BinFlagUtils.isTowPower(o.longValue())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

}
