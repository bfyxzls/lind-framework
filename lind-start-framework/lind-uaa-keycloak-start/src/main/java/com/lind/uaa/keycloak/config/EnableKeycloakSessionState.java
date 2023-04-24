package com.lind.uaa.keycloak.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启匿名访问页面的登录检查，如果用户在其它端已经登录，那匿名页面将同步登录状态。
 * 需要将`KeycloakSessionStateInterceptor`添加到WebMvcConfigurer.addInterceptors
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import(KeycloakSessionStateInterceptor.class)
public @interface EnableKeycloakSessionState {

}
