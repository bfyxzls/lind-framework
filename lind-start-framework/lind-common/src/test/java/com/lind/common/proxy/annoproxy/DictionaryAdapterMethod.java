package com.lind.common.proxy.annoproxy;

import java.lang.annotation.*;

/**
 * 字典适配器注释.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictionaryAdapterMethod {

}
