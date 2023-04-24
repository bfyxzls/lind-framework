package com.lind.common.enums;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 枚举常用工具类。<br/>
 * 使用该枚举工具类需要指定的枚举实现{@link ValueEnum} OR {@link NameValueEnum}接口
 */
public final class EnumUtils {

	private final static String RESOURCE_PATTERN = "/**/*.class";

	/**
	 * 判断枚举值是否存在于指定枚举数组中
	 * @param enums 枚举数组
	 * @param value 枚举值
	 * @return 是否存在
	 */
	public static <T> boolean isExist(ValueEnum[] enums, T value) {
		if (value == null) {
			return false;
		}
		for (ValueEnum e : enums) {
			if (value.equals(e.getValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断枚举值是否存与指定枚举类中（尽量显示调用Enum.values()方法获取所有枚举类，此方法内部反射调用values方法）
	 * @param enumClass 枚举类
	 * @param value 枚举值
	 * @param <E> 枚举类型
	 * @param <V> 值类型
	 * @return true：存在
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<? extends ValueEnum>, V> boolean isExist(Class<E> enumClass, V value) {
		for (Enum<? extends ValueEnum> e : enumClass.getEnumConstants()) {
			if (((ValueEnum) e).getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据枚举值获取其对应的名字
	 * @param enums 枚举列表
	 * @param value 枚举值
	 * @return 枚举名称
	 */
	public static <T> String getNameByValue(NameValueEnum[] enums, T value) {
		if (value == null) {
			return null;
		}
		for (NameValueEnum e : enums) {
			if (value.equals(e.getValue())) {
				return e.getName();
			}
		}
		return null;
	}

	/**
	 * 根据枚举名称获取对应的枚举值
	 * @param enums 枚举列表
	 * @param name 枚举名
	 * @return 枚举值
	 */
	public static Integer getValueByName(NameValueEnum[] enums, String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		for (NameValueEnum e : enums) {
			if (name.equals(e.getName())) {
				return e.getValue();
			}
		}
		return null;
	}

	/**
	 * 根据枚举值获取对应的枚举对象
	 * @param enums 枚举列表
	 * @return 枚举对象
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<? extends ValueEnum>> E getEnumByValue(E[] enums, Integer value) {
		for (E e : enums) {
			if (((ValueEnum) e).getValue().equals(value)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据枚举值获取对应的枚举对象
	 * @param enumClass 枚举class
	 * @return 枚举对象
	 */
	public static <E extends Enum<? extends ValueEnum>> E getEnumByValue(Class<E> enumClass, Integer value) {
		return getEnumByValue(enumClass.getEnumConstants(), value);
	}

	public static <E extends Enum<? extends NameValueEnum>> E getEnumByName(E[] enums, String name) {
		for (E e : enums) {
			if (((NameValueEnum) e).getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public static <E extends Enum<? extends NameValueEnum>> E getEnumByName(Class<E> enumClass, String name) {
		return getEnumByName(enumClass.getEnumConstants(), name);
	}

	/**
	 * 遍历枚举类型的键值列表.
	 * @param enumClass
	 * @param <E>
	 * @return
	 */
	public static <E extends Enum<? extends NameValueEnum>> List<NameValueEnum> getEnumNameValues(Class<E> enumClass) {
		List<NameValueEnum> list = new ArrayList<>();
		for (Enum<? extends NameValueEnum> e : enumClass.getEnumConstants()) {
			list.add(new NameValueEnum() {
				@Override
				public String getName() {
					return ((NameValueEnum) e).getName();
				}

				@Override
				public Integer getValue() {
					return ((NameValueEnum) e).getValue();
				}
			});
		}
		return list;
	}

	/**
	 * 装载enum列表.
	 * @param E 接口类型
	 * @param basePackage 扫描包下的枚举
	 * @param <E>
	 * @return
	 */
	public static <E> Set<Class> loadEnumList(Class<E> E, String basePackage) {
		Set<Class> handlerMap = new HashSet<>();

		// spring工具类，可以获取指定路径下的全部类
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		try {
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + RESOURCE_PATTERN;
			Resource[] resources = resourcePatternResolver.getResources(pattern);
			// MetadataReader 的工厂类
			MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {
				// 用于读取类信息
				MetadataReader reader = readerfactory.getMetadataReader(resource);
				// 扫描到的class
				String classname = reader.getClassMetadata().getClassName();
				Class<?> clazz = Class.forName(classname);
				// 判断是否有指定主解
				if (Arrays.stream(clazz.getInterfaces()).filter(o -> o == E).count() > 0) {
					// 将注解中的类型值作为key，对应的类作为 value
					handlerMap.add(clazz);
				}
			}
		}
		catch (IOException | ClassNotFoundException e) {
		}
		return handlerMap;
	}

}
