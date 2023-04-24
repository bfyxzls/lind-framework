package com.lind.common.collection;

import org.junit.Test;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * TreeSet是一个有序的集合，它的作用是提供有序的Set集合
 * TreeSet是对TreeMap的简单包装，对TreeSet的函数调用都会转换成合适的TreeMap方法，因此TreeSet的实现非常简单.
 *
 * @author lind
 * @date 2023/2/8 11:24
 * @since 1.0.0
 */
public class TreeSetTest {

	@Test
	public void asc() {
		TreeSet set = new TreeSet();
		set.add("aaa");
		set.add("aaa");
		set.add("bbb");
		set.add("eee");
		set.add("ddd");
		set.add("ccc");

		// 顺序遍历TreeSet
		System.out.print("\n ---- Ascend Iterator ----\n");
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			System.out.printf("asc : %s\n", iter.next());
		} // 逆序遍历TreeSet
		System.out.printf("\n ---- Descend Iterator ----\n");
		for (Iterator iter = set.descendingIterator(); iter.hasNext();)
			System.out.printf("desc : %s\n", (String) iter.next());

	}

}
