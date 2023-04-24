package com.lind.elasticsearch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * 与HBASE配合使用的实体，动态数据实体,列存储,动态扩展字段.
 */
@ToString(includeFieldNames = true)
public class DataRecord implements Map<String, Object>, Serializable {

	private static final long serialVersionUID = 2546462404989740543L;

	/**
	 * 存储对象的容器,key为字段名,value为字段值.
	 */
	@Getter
	private final LinkedHashMap<String, Object> document;

	/**
	 * 数据id，只能是string类型.
	 */
	@Getter
	@Setter
	private String id;

	public DataRecord(final Map<String, Object> document) {
		this.document = new LinkedHashMap<>(document);
	}

	public DataRecord() {
		document = new LinkedHashMap<String, Object>();
	}

	public DataRecord(String rowKey) {
		this();
		this.id = rowKey;
	}

	/**
	 * 增加 kv数据
	 * @param key key
	 * @param value value
	 * @return
	 */
	public DataRecord append(final String key, final Object value) {
		document.put(key, value);
		return this;
	}

	/**
	 * 获取指定类型参数
	 * @param key key
	 * @param clazz clazz
	 * @param <T> 类型
	 * @return
	 */
	public <T> T get(final String key, final Class<T> clazz) {
		Assert.notNull(clazz, "强转类型不能为空");
		return clazz.cast(document.get(key));
	}

	/**
	 * 获取指定类型的list列表
	 * @param key 键
	 * @param clazz 指定的数据类型
	 * @param <T> 类型
	 * @return
	 */
	public <T> List<T> getList(final String key, final Class<T> clazz) {
		List value = get(key, List.class);
		if (value == null) {
			return Collections.emptyList();
		}

		for (Object item : value) {
			if (!clazz.isAssignableFrom(item.getClass())) {
				throw new ClassCastException(format("List element cannot be cast to %s", clazz.getName()));
			}
		}
		return (List<T>) value;
	}

	@Override
	public int size() {
		return document.size();
	}

	@Override
	public boolean isEmpty() {
		return document.isEmpty();
	}

	@Override
	public boolean containsValue(final Object value) {
		return document.containsValue(value);
	}

	@Override
	public boolean containsKey(final Object key) {
		return document.containsKey(key);
	}

	@Override
	public Object get(final Object key) {
		return document.get(key);
	}

	/**
	 * 获取指定key的值，如果获取不到返回设定的默认值
	 * @param key 键
	 * @param defaultValue 默认值
	 * @param <T> 类型
	 * @return
	 */
	public <T> T get(final String key, final T defaultValue) {
		Assert.notNull(defaultValue, "默认值不能为空");
		Object value = document.get(key);
		return value == null ? defaultValue : (T) value;
	}

	@Override
	public Object put(final String key, final Object value) {
		return document.put(key, value);
	}

	@Override
	public Object remove(final Object key) {
		return document.remove(key);
	}

	@Override
	public void putAll(final Map<? extends String, ?> map) {
		document.putAll(map);
	}

	@Override
	public void clear() {
		document.clear();
	}

	@Override
	public Set<String> keySet() {
		return document.keySet();
	}

	@Override
	public Collection<Object> values() {
		return document.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return document.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		DataRecord document = (DataRecord) o;

		return this.document.equals(document.document);
	}

	@Override
	public int hashCode() {
		return document.hashCode();
	}

}
