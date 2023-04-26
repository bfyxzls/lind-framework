package com.lind.common.jackson.convert;

import com.lind.common.enums.serializer.NameValueEnumSerializer;
import com.lind.common.jackson.JacksonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启json格式化.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ NameValueEnumSerializer.class, JacksonDateSerializerModifier.class, JacksonNullValueSerializerModifier.class,
		JacksonConfiguration.class })
public @interface EnableJacksonFormatting {

}
