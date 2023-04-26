package com.lind.common.java8;

import cn.hutool.core.lang.Assert;
import com.lind.common.collection.Student;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * java8Optional学习
 */
public class OptionalTest {

	/**
	 * 为可能为null的对象添加默认值.
	 */
	@Test
	public void nullable() {
		Student student = null;
		Student a = Optional.ofNullable(student).orElse(new Student());
		Assert.isTrue(a.getAge() == 0);

		Student student2 = Student.builder().age(1).build();
		Student b = Optional.ofNullable(student2).orElse(new Student());
		Assert.isTrue(b.getAge() == 1);
	}

	@Test
	public void ifPresent() {
		Student student = null;
		// 如果对象不为null，就执行ifPresent里的方法
		System.out.println("studnet!=null," + Optional.ofNullable(student).isPresent());
		Optional.ofNullable(student).ifPresent(u -> {
			System.out.println("The null object not ifPresent");
			student.setName(u.getName().trim());
		});

		Student student1 = Student.builder().name("    hello   ").build();
		Optional.ofNullable(student1).ifPresent(u -> {
			System.out.println("The student1 name is:" + u.getName());
			student1.setName(u.getName().trim());
		});
		System.out.println("The student1 name is:" + student1.getName());
		String a = "hello";
		Optional.ofNullable(a).ifPresent(u -> System.out.println("The hello name is : " + u));

	}

	@Test(expected = IllegalArgumentException.class)
	public void orElseThrow() {
		Student student = null;
		Optional.ofNullable(student).orElseThrow(IllegalArgumentException::new);
	}

	@Test
	public void orElseThrowMessage() {
		Student student = null;
		Optional.ofNullable(student).orElseThrow(() -> new IllegalArgumentException("student is not null!"));
	}

	@Test
	public void findAny() {
		List<String> array = new ArrayList<>();
		array.stream().filter(o -> o.equals("zzl")).findAny().ifPresent(m -> {
			throw new IllegalArgumentException("已存在zzl");
		});
		array.add("zzl");
		array.stream().filter(o -> o.equals("zzl")).findAny().ifPresent(m -> {
			throw new IllegalArgumentException("已存在zzl");
		});
	}

}
