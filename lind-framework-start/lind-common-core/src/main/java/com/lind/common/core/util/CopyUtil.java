package com.lind.common.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 复制字段.
 */
public class CopyUtil {

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	/**
	 * 复制实体.
	 * @param src
	 * @param target
	 */
	public static void copyProperties(Object src, Object target) {
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	/**
	 * 复制实体.
	 * @param src
	 * @param target
	 */
	public static void copyProperties(Object src, Object target, String... ignoreProperties) {
		BeanUtils.copyProperties(src, target, ignoreProperties);
	}

	/**
	 * 类型转换.
	 * @param src
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T copyProperties(Object src, Class<T> clazz) {
		T t = null;
		try {
			t = clazz.newInstance();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		BeanUtils.copyProperties(src, t, getNullPropertyNames(src));
		return t;
	}

	/**
	 * 复制集合.
	 * @param src
	 */
	public static <S, T> List<T> copyListProperties(List<S> src, Class<T> classT) {
		List<T> list = new ArrayList<>();

		for (S obj : src) {
			T t = null;
			BeanUtils.copyProperties(obj, t, classT);
			list.add(t);
		}
		return list;
	}

}
