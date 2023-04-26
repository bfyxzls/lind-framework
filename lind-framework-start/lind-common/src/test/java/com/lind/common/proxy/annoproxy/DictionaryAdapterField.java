package com.lind.common.proxy.annoproxy;

import java.lang.annotation.*;

/**
 * 字典适配器字段标识.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictionaryAdapterField {

}
