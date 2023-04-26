package com.lind.common.proxy.annoproxy;

import org.springframework.context.annotation.Import;

/**
 * 开启字典适配拦截器.
 */
@Import(DictionaryAdapterAspect.class)
public @interface EnableDictionaryAdapter {

}
