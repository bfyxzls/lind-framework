package com.lind.common.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 比较器，实现复杂的排序功能. 对集合的排序，应该先对集合的泛类实现这个接口.
 */
public class ComparableTest {

	@Test
	public void testArray() {
		/**
		 * Student{age=30, name='lr', score=98.0} Student{age=8, name='lmw', score=90.0}
		 * Student{age=9, name='zhz', score=90.0} Student{age=10, name='zzl', score=90.0}
		 */
		Student[] students = new Student[] { new Student(10, "zzl", 90), new Student(30, "lr", 98),
				new Student(8, "lmw", 90), new Student(9, "zhz", 90) };

		Arrays.sort(students);
		for (Student student : students) {
			System.out.println(student);
		}
	}

	@Test
	public void testList() {
		List<Student> students = new ArrayList<Student>();
		students.add(new Student(10, "zzl", 90));
		students.add(new Student(30, "lr", 98));
		students.add(new Student(9, "zhz", 90));
		Collections.sort(students);
		for (Student student : students) {
			System.out.println(student);
		}
	}

}
