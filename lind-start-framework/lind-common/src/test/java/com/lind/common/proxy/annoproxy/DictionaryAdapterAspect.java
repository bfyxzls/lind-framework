package com.lind.common.proxy.annoproxy;

import cn.hutool.core.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Aspect
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class DictionaryAdapterAspect {

	/**
	 * 拦截注释的方法.
	 * @param joinPoint
	 * @param dictionaryAdapterMethod
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(dictionaryAdapterMethod)")
	public Object around(ProceedingJoinPoint joinPoint, DictionaryAdapterMethod dictionaryAdapterMethod)
			throws Throwable {
		// 遍历参数列表
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			Field[] fields = ReflectUtil.getFields(arg.getClass());
			for (Field field : fields) {
				log.info("dictionaryAdapterMethod.field:{}", field.getName());
				if (field.isAnnotationPresent(DictionaryAdapterField.class)) {
					changObjectValue(arg, field.getName(), "hello");
				}
			}
		}
		return joinPoint.proceed(args);
	}

	/**
	 * 修改属性值.
	 * @param obj
	 * @param filedName
	 * @param afterValue
	 * @return
	 * @throws Exception
	 */
	private Object changObjectValue(Object obj, String filedName, Object afterValue) throws Exception {
		Class<?> resultClz = obj.getClass();
		Field[] fieldInfo = resultClz.getDeclaredFields();
		for (Field field : fieldInfo) {
			if (filedName.equals(field.getName())) {
				field.setAccessible(true);
				field.set(obj, afterValue);
				break;
			}
		}
		return obj;
	}

}
