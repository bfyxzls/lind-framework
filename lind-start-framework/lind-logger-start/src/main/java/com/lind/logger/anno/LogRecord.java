package com.lind.logger.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {

	/**
	 * 消息，支持spEL表达式.
	 * @return
	 */
	String detail() default "";

	/**
	 * 数据ID.
	 * @return
	 */
	String dataId() default "";

	/**
	 * 数据标题
	 * @return
	 */
	String dataTitle() default "";

	/**
	 * 操作类型
	 * @return
	 */
	int operateType();

	/**
	 * 模块类型
	 * @return
	 */
	int moduleType();

}
