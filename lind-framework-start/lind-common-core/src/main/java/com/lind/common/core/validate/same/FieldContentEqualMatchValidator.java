package com.lind.common.core.validate.same;

/**
 * @author lind
 * @date 2024/10/14 11:30
 * @since 1.0.0
 */

import cn.hutool.core.bean.BeanUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldContentEqualMatchValidator implements ConstraintValidator<FieldContentEqualMatch, Object> {

	private String sourceField;

	private String destinationField;

	@Override
	public void initialize(FieldContentEqualMatch constraintAnnotation) {
		this.sourceField = constraintAnnotation.sourceField();
		this.destinationField = constraintAnnotation.destinationField();
	}

	@Override
	public boolean isValid(Object o, final ConstraintValidatorContext context) {
		final Object sourceFieldVal = BeanUtil.getProperty(o, this.sourceField);
		final Object destinationFieldVal = BeanUtil.getProperty(o, this.destinationField);
		return sourceFieldVal == null && destinationFieldVal == null
				|| sourceFieldVal != null && sourceFieldVal.equals(destinationFieldVal);
	}

}
