package com.lind.common.collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 集合可排序
 */
public class SortedMapTest {

	Logger logger = LoggerFactory.getLogger(SortedMapTest.class);

	@Test
	public void sqrt() {

		List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
		// 获取对应的平方数
		List<Integer> squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
		logger.info("{}", squaresList);

	}

	@Test
	public void sortAndIterator() {
		HashMap<String, String> map = new HashMap<>();
		map.put("3", "33");
		map.put("1", "11");
		map.put("2", "22");
		map.put("a", "aa");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("排序之前:" + entry.getKey() + " 值" + entry.getValue());
		}
		System.out.println("======================================================");
		SortedMap<String, String> sort = new TreeMap<>(map);
		Set<Map.Entry<String, String>> entry1 = sort.entrySet();
		Iterator<Map.Entry<String, String>> it = entry1.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			System.out.println("排序之后:" + entry.getKey() + " 值" + entry.getValue());
		}
		/**
		 * 输出结果: 排序之前:1 值11 排序之前:a 值aa 排序之前:2 值22 排序之前:3 值33
		 * ====================================================== 排序之后:1 值11 排序之后:2 值22
		 * 排序之后:3 值33 排序之后:a 值aa
		 */
	}

	@Test
	public void get() {
		SortedMap<String, String> map = null;
		map = new TreeMap<String, String>(); // 通过子类实例化接口对象
		map.put("D", "DDDDD");
		map.put("A", "AAAAA");
		map.put("C", "CCCCC");
		map.put("B", "BBBBB");
		System.out.println("第一个元素的key:" + map.firstKey());
		System.out.println("key对于的值为:" + map.get(map.firstKey()));
		System.out.println("最后一个元素的key:" + map.lastKey());
		System.out.println("key对于的值为:" + map.get(map.lastKey()));
		System.out.println("返回小于指定范围的集合（键值小于“C”）");
		for (Map.Entry<String, String> me : map.headMap("C").entrySet()) {
			System.out.println("\t|- " + me.getKey() + "-->" + me.getValue());
		}
		System.out.println("返回大于指定范围的集合（键值大于等于“C”）");
		for (Map.Entry<String, String> me : map.tailMap("C").entrySet()) {
			System.out.println("\t|- " + me.getKey() + "-->" + me.getValue());
		}
		System.out.println("返回部分集合（键值“B”和“D”之间,包括B不包括D）");
		for (Map.Entry<String, String> me : map.subMap("B", "D").entrySet()) {
			System.out.println("\t|- " + me.getKey() + "-->" + me.getValue());
		}
		/**
		 * 第一个元素的key:A key对于的值为:AAAAA 最后一个元素的key:D key对于的值为:DDDDD 返回小于指定范围的集合（键值小于“C”） |-
		 * A-->AAAAA |- B-->BBBBB 返回大于指定范围的集合（键值大于等于“C”） |- C-->CCCCC |- D-->DDDDD
		 * 返回部分集合（键值“B”和“D”之间,包括B不包括D） |- B-->BBBBB |- C-->CCCCC
		 *
		 */
	}

	@Test
	public void tailMap() {
		SortedMap<String, String> list = new TreeMap<>(); // 基于红黑树的实现，在单线程性能不错.
		list.put("a", "1");
		list.put("b", "2");
		list.put("c", "3");
		list.put("d", "4");
		SortedMap<String, String> tail = list.tailMap("c");// 返回大于等于c的
		Iterator<String> iterator = tail.values().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	@Test
	public void headMap() {
		SortedMap<String, String> list = new TreeMap<>(); // 基于红黑树的实现，在单线程性能不错.
		list.put("a", "1");
		list.put("b", "2");
		list.put("c", "3");
		list.put("d", "4");
		SortedMap<String, String> tail = list.headMap("c");// 返回小于c的
		Iterator<String> iterator = tail.values().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	/**
	 * 对象里按字段排序.
	 */
	@Test
	public void objSort() {
		List<Student> studentList = new ArrayList<>();
		studentList.add(new Student(20, "张三", 1.0));
		studentList.add(new Student(31, "李四", 2.0));
		studentList.add(new Student(28, "王五", 3.0));
		studentList.add(new Student(23, "赵六", 4.0));
		studentList.stream().sorted(Comparator.comparing(Student::getAge)).forEach(System.out::println);
		logger.info("----reversed---");
		studentList.stream().sorted(Comparator.comparing(Student::getAge).reversed()).forEach(System.out::println);
	}

}
