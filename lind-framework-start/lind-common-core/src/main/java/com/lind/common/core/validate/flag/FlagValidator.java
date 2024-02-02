package com.lind.common.core.validate.flag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 状态约束校验器
 * Created by macro on 2018/4/26.
 */
public class FlagValidator implements ConstraintValidator<ValidFlag,Integer> {
    private String[] values;
    @Override
    public void initialize(ValidFlag validFlag) {
        this.values = validFlag.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        if(value==null){
            //当状态为空时使用默认值
            return true;
        }
        for(int i=0;i<values.length;i++){
            if(values[i].equals(String.valueOf(value))){
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
