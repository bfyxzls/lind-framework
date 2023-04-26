package com.lind.common.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lind
 * @date 2022/8/17 16:05
 * @since 1.0.0
 */
public class MapAndFlatMapTest {

	@Test
	public void map() {
		List<List<Integer>> lists = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		list.add(4444);
		list.add(33333);
		list.add(444444);
		lists.add(list);
		lists.stream().map(Collection::stream).forEach(System.out::println);
	}

	/**
	 * List<List<String>>这种嵌套的会用它
	 */
	@Test
	public void flatMap() {
		List<List<Integer>> lists = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		list.add(4444);
		list.add(33333);
		list.add(444444);
		lists.add(list);
		lists.stream().flatMap(Collection::stream).forEach(System.out::println);
	}

}
