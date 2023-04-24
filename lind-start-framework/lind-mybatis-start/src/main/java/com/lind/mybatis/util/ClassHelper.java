package com.lind.mybatis.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/**
 * 类辅助方法.
 */
public class ClassHelper {

	/**
	 * 获取type所有字段,包含基类的.
	 * @param type type
	 * @return
	 */
	public static Field[] getAllFields(Class type) {
		Field[] fields = type.getDeclaredFields();
		Class superClass = type.getSuperclass();
		while (superClass != null) {
			Field[] superFileds = superClass.getDeclaredFields();
			fields = ArrayUtils.addAll(fields, superFileds);
			superClass = superClass.getSuperclass();
		}
		return fields;
	}

}
