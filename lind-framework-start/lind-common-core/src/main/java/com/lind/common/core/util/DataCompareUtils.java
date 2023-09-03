package com.lind.common.core.util;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 比对两个实体有哪些字段不同.
 *
 * @author lind
 * @date 2023/9/1 11:47
 * @since 1.0.0
 */
public class DataCompareUtils {

	public static List<Field> compareObjects(Object obj1, Object obj2) throws IllegalAccessException {
		List<Field> changedFields = new ArrayList<>();

		Class<?> clazz = obj1.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			Object value1 = field.get(obj1);
			Object value2 = field.get(obj2);

			if (!Objects.equals(value1, value2)) {
				changedFields.add(field);
			}
		}

		return changedFields;
	}

	public static String getModifyInfo(Object obj1, Object obj2) {
		List<Field> fieldList = null;
		try {
			fieldList = compareObjects(obj1, obj2);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		if (!fieldList.isEmpty()) {
			List<String> list = new ArrayList<>();
			list.add("修改了");
			for (Field fieldName : fieldList) {
				if (fieldName.isAnnotationPresent(ApiModelProperty.class))
					list.add(fieldName.getAnnotation(ApiModelProperty.class).value());
				else
					list.add(fieldName.getName());
			}
			return String.join(",", list);
		}
		return "未到修改";
	}

	public static void main(String[] args) throws IllegalAccessException {
		Person person1 = new Person();
		person1.setFirstName("John");
		person1.setLastName("zhang1");

		Person person2 = new Person();
		person2.setFirstName("lind");
		person2.setLastName("zhang");

		String msg = getModifyInfo(person1, person2);
		System.out.println(msg);
	}

	static class Person {

		@ApiModelProperty("名字")
		String firstName;

		@ApiModelProperty("姓氏")
		String lastName;

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

	}

}
