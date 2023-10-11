package com.lind.common.othertest;

import org.junit.Test;

import java.io.Serializable;
import java.util.Objects;

public class OverrideClassTest {

	@Test
	public void equalsTest() {
		Person person1 = new Person("zzl");
		Person person2 = new Person("zzl");
		System.out.println("p1 equals p2 : " + person1.equals(person2));
	}

	static class Person implements Serializable {

		private static final long serialVersionUID = 1L;

		private final String name;

		public Person(String name) {
			this.name = name;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			Person person = (Person) o;
			return Objects.equals(name, person.name);
		}

		public int hashCode() {
			return Objects.hash(name);
		}

	}

}
