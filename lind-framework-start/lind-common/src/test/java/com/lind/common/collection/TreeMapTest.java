package com.lind.common.collection;

import org.junit.Test;

import java.util.TreeMap;

/**
 * @author lind
 * @date 2023/6/8 11:44
 * @since 1.0.0
 */
public class TreeMapTest {

	@Test
	public void testSort() {
		TreeMap<Person, String> treeMap = new TreeMap();
		treeMap.put(new Person("zzl", 40), "zzl");
		treeMap.put(new Person("zhz", 14), "zhz");
		treeMap.forEach((i, o) -> {
			System.out.println(i);
		});

	}

	class Person implements Comparable<Person> {

		private String name;

		private int age;

		public Person(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}

		@Override
		public int compareTo(Person o) {
			return this.age - o.age; // 排序方式
		}

	}

}
