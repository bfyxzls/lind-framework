package com.lind.common.java8;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author lind
 * @date 2023/6/20 15:08
 * @since 1.0.0
 */
@Slf4j
public class ComparatorTest {

	@Test
	public void lambdaSort() {
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		// 匿名类
		Collections.sort(names, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				return b.compareTo(a);
			}
		});
		System.out.println(names);

		// Lambda
		Collections.sort(names, (String a, String b) -> {
			return b.compareTo(a);
		});
		System.out.println(names);

		// Lambda
		Collections.sort(names, (String a, String b) -> b.compareTo(a));
		System.out.println(names);

		// Lambda reverse 就是说接口中不是只可有default默认实现，还可以有静态方法
		Collections.sort(names, Comparator.reverseOrder());
		System.out.println(names);

	}

	// 函数式接口
	@Test
	public void functionalInterface() {
		IPersonFactory<Person> personFactory = Person::new; // ::需要接口是函数式接口
															// [参照物]：(firstName, lastName)
															// -> new Person(firstName,
															// lastName);
		Person person = personFactory.create("Peter", "Parker");
		log.info("person={}", person);
	}

	@Test
	public void testPredicate() {
		Predicate<String> predicate = (s) -> s.length() > 0;

		boolean foo0 = predicate.test("foo");           // true
		boolean foo1 = predicate.negate().test("foo");  // negate否定相当于!true
		Assert.isTrue(foo0, "Expression must be true");
		assert !foo1;
		Predicate<Boolean> nonNull = Objects::nonNull;
		Predicate<Boolean> isNull = Objects::isNull;

		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> isNotEmpty = isEmpty.negate();
	}

	@Test
	public void testFunction() {
		Function<String, Integer> toInteger = Integer::valueOf;                                         //转Integer
		Function<String, String> backToString = toInteger.andThen(String::valueOf);                     //转String
		Function<String, String> afterToStartsWith = backToString.andThen(new Something()::startsWith); //截取第一位
		String apply = afterToStartsWith.apply("123");// "123"
		System.out.println(apply);
	}
	class Something {
		String startsWith(String s) {
			return String.valueOf(s.charAt(0));
		}
	}
	class Person extends StreamTest.Person {

		String firstName;
		String lastName;

		Person() {
		}

		Person(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

	}

}
