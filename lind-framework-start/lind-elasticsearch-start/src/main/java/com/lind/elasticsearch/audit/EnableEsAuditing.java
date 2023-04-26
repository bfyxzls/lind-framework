package com.lind.elasticsearch.audit;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启ES审计功能. 主要对建立人、建立时间、更新人、更新时间赋值.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ AuditAspect.class })
public @interface EnableEsAuditing {

}
