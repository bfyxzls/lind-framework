package com.lind.common.collection;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 多线程情况下，使用ConcurrentSkipListMap性能更好，它没有非线程安全版本，因为单线程下直接用TreeMap即可
 */
public class ConcurrentSkipListTest {

	@SneakyThrows
	@Test
	public void add() {
		ConcurrentSkipListMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
		String a1 = concurrentSkipListMap.put("zzl", "zhangzhanling");
		String a2 = concurrentSkipListMap.put("zzl1", "zhan1");
		String a3 = concurrentSkipListMap.put("zzl2", "zhan2");
		String a4 = concurrentSkipListMap.put("china", "中国");
		System.out.printf(concurrentSkipListMap.toString());

	}

	@Test
	public void replaceSameKey() {
		// Initializing the map
		ConcurrentSkipListMap<Integer, Integer> cslm = new ConcurrentSkipListMap<Integer, Integer>();

		// Adding elements to this map
		for (int i = 1; i <= 9; i++)
			cslm.put(i, i);

		cslm.put(1, 100);

		// put() operation on the map
		System.out.println("After put(): " + cslm);
	}

	@Test
	public void iterator() {
		// create an instance of ConcurrentSkipListMap
		ConcurrentSkipListMap<Integer, Integer> cslm = new ConcurrentSkipListMap<>();

		// Add mappings using put method
		for (int i = 0; i < 6; i++) {
			cslm.put(i, i);
		}

		// Create an Iterator over the
		// ConcurrentSkipListMap
		Iterator<ConcurrentSkipListMap.Entry<Integer, Integer>> itr = cslm.entrySet().iterator();

		// The hasNext() method is used to check if there is
		// a next element The next() method is used to
		// retrieve the next element
		while (itr.hasNext()) {
			ConcurrentSkipListMap.Entry<Integer, Integer> entry = itr.next();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

}
