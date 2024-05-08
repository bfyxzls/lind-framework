package com.lind.common.collection;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 栈的使用.
 *
 * @author lind
 * @date 2023/7/25 17:04
 * @since 1.0.0
 */
public class DequeTest {

	@Test
	public void deque() {
		boolean result = Person.builder().id("1").name("zzl1").build()
				.equals(Person.builder().id("1").name("zzl2").build());
		Deque<Person> personDeque = new LinkedList<>();
		personDeque.push(Person.builder().id("1").name("zzl1").build());
		personDeque.push(Person.builder().id("2").name("zzl2").build());
		personDeque.push(Person.builder().id("1").name("zzl3").build());
		if (personDeque.contains(Person.builder().id("1").build())) {
			personDeque.pop();
		}
		System.out.println("result=" + result + "queue=" + personDeque);
	}

	@Test
	public void list_contains() {
		List<Person> personList = new ArrayList<>();
		personList.add(Person.builder().id("1").name("zzl1").build());
		personList.add(Person.builder().id("2").name("zzl2").build());
		personList.add(Person.builder().id("1").name("zzl3").build());
		// result=true
		System.out.println("result=" + personList.contains(personList.contains(Person.builder().id("1").build())));
	}

	@Builder
	@Data
	static class Person implements Serializable {

		private String name;

		private String id;

		// 重写equals时，字段要与hashCode里的字段保持一致
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Person person = (Person) o;
			return Objects.equals(id, person.id);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

	}

}
