package com.lind.mybatis.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 查询生成器.
 */
@SuppressWarnings("unchecked")
public class QueryFactory {

	/**
	 * 实体字段封装条件.
	 * @param obj
	 * @return
	 */
	public static QueryWrapper entityToWrapper(Object obj) {
		Class<?> aClass = obj.getClass();
		Field[] fields = obj.getClass().getDeclaredFields();
		QueryWrapper wrapper = new QueryWrapper();
		// Traversal properties
		for (Field field : fields) {
			Method method = null;
			try {
				String fieldName = field.getName();
				// skip serialVersionUID
				if (fieldName.equals("serialVersionUID")) {
					continue;
				}
				// Gets an annotation on a property
				TableField fieldAnnotation = field.getAnnotation(TableField.class);
				// Get the list
				String value = fieldAnnotation == null ? null : fieldAnnotation.value();
				// get Method
				if (value != null) {
					method = aClass.getDeclaredMethod("get" + captureName(fieldName), null);
					Object returnValue = method.invoke(obj);
					if (returnValue != null) {
						if (returnValue instanceof String) {
							String str = (String) returnValue;
							wrapper.eq(StringUtils.isNotBlank(str), value, returnValue);
						}
						else {
							wrapper.eq(returnValue != null, value, returnValue);
						}
					}
				}

			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return wrapper;
	}

	/**
	 * Capitalize the first letter of a string
	 * @param str
	 * @return
	 */
	private static String captureName(String str) {
		// Carry out alphabetic ascii Coding forward , It is more efficient than the
		// operation of intercepting string for conversion
		char[] cs = str.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

}
