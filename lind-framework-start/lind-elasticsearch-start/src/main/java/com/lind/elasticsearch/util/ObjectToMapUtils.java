package com.lind.elasticsearch.util;

import com.alibaba.excel.support.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lind
 * @date 2024/5/7 15:23
 * @since 1.0.0
 */
public class ObjectToMapUtils {

	/**
	 * 将对象转换成Map
	 * @param bean
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = new HashMap<>();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				if (beanMap.get(key) != null)
					map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

}
