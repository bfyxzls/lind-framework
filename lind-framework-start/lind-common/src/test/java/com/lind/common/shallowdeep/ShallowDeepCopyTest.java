package com.lind.common.shallowdeep;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * 浅拷贝和深拷贝.
 *
 * @author lind
 * @date 2023/9/11 14:03
 * @since 1.0.0
 */
public class ShallowDeepCopyTest {

	@SneakyThrows
	@Test
	public void shallow() {
		// 浅拷贝示例,对于内部对象来说，它传递的是引用
		Address originalAddress = new Address("New York");
		Person originalPerson = new Person("Alice", originalAddress);
		Person shallowCopyPerson = (Person) originalPerson.clone();

		shallowCopyPerson.setName("Bob");
		shallowCopyPerson.getAddress().setCity("Los Angeles");

		System.out.println(originalPerson.getAddress().getCity()); // 输出 "Los
																	// Angeles"，因为两个对象共享引用
		System.out.println(shallowCopyPerson.getAddress().getCity()); // 输出 "Los Angeles"
	}

	@SneakyThrows
	@Test
	public void deep() {

		// 深拷贝示例，内部对象也会被拷贝
		Address originalAddress = new Address("New York");
		Person originalPerson = new Person("Alice", originalAddress);

		Person deepCopyPerson = originalPerson.deepCopy();
		deepCopyPerson.getAddress().setCity("Los Angeles");

		System.out.println(originalPerson.getAddress().getCity()); // 输出 "New
																	// York"，深拷贝不影响原始对象
		System.out.println(deepCopyPerson.getAddress().getCity());
	}

}
