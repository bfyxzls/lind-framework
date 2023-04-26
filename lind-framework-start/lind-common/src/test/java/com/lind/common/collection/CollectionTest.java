package com.lind.common.collection;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合操作
 */
public class CollectionTest {

	public static final String USER = "user";

	List<Student> students = new ArrayList<Student>();

	public static void peekFun(Map<String, String> map) {
		map.put("NAME", map.get("name").toUpperCase());
	}

	@Test
	public void test1() {
		List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		list1.add(3);

		List<Integer> list2 = new ArrayList<>();
		list2.add(3);
		list2.add(4);
		list2.add(5);

		System.out.println("====求交集===");
		List<Integer> list = list1.stream().filter(t -> list2.contains(t)).collect(Collectors.toList());
		list.stream().forEach(System.out::println);

		System.out.println("====求差集===");
		list = list1.stream().filter(t -> !list2.contains(t)).collect(Collectors.toList());
		list.stream().forEach(System.out::println);

		System.out.println("====求并集===");
		list.addAll(list1);
		list.addAll(list2);
		list = list.stream().distinct().collect(Collectors.toList());
		list.stream().forEach(System.out::println);
	}

	@Test
	public void itl() {
		Iterator<Student> iterator = students.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	@Test
	public void filterField() {
		// mysql
		List<Column> columnVoList = new ArrayList<>();
		columnVoList.add(new Column("a1"));
		columnVoList.add(new Column("a2"));
		columnVoList.add(new Column("a3"));

		// http
		Map<String, Object> record = new HashMap<>();
		record.put("a1", "zhangsan");
		record.put("a2", "lisi");
		record.put("a4", "wangwu");
		columnVoList.stream()
				// 取交集
				.filter(columnVo -> {
					String columnName = columnVo.getName();
					return record.containsKey(columnName);
				}).forEach(o -> {
					System.out.println(o.name);
				});

	}

	@Before
	public void init() {
		students.add(new Student(10, "zzl", 90));
		students.add(new Student(30, "lr", 98));
		students.add(new Student(9, "zhz", 90));
	}

	/**
	 * 分组集合
	 */
	@Test
	public void ListToMap() {
		ConcurrentHashMap<String, List<String>> concurrentHashMap;
		List<UserAccountSet> userAccountSets = Arrays.asList(new UserAccountSet("zzl", "a1"),
				new UserAccountSet("lisi", "a2"), new UserAccountSet("zhz", "a1"));
		concurrentHashMap = userAccountSets.stream()
				.map(i -> ImmutableMap.of(i.getGroupAccountName(),
						userAccountSets.stream().filter(o -> o.getGroupAccountName().equals(i.getGroupAccountName()))
								.map(p -> p.getLoginAccount()).collect(Collectors.toList())))
				.collect(ConcurrentHashMap::new, Map::putAll, Map::putAll);
		System.out.println(concurrentHashMap);
	}

	@Test
	public void anyMath() {
		List<UserAccountSet> userAccountSets = Arrays.asList(new UserAccountSet("zzl", "a1"),
				new UserAccountSet("lisi", "a2"), new UserAccountSet("zhz", "a1"));
		Assert.isTrue(userAccountSets.stream().map(o -> o.loginAccount).filter(StrUtil::isNotBlank)
				.anyMatch(o -> o.equals("zzl")));
	}

	/**
	 * 至少有一个元素包含了admin
	 */
	@Test
	public void anyMath2() {
		List<String> role = Arrays.asList("admin", "editor", "");
		Assert.isTrue(role.stream().filter(StrUtil::isNotBlank).anyMatch(o -> o.equals("admin")));
	}

	/**
	 * 所有元素都不包含admin
	 */
	@Test
	public void notAnyMath() {
		List<String> role = Arrays.asList("admin", "editor", "");
		Assert.isTrue(role.stream().filter(StrUtil::isNotBlank).noneMatch(o -> o.equals("write")));
	}

	@Test
	public void computeIfAbsent() {
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			int key = i % 5;
			List<Integer> list = map.computeIfAbsent(key, k -> new ArrayList<>());
			list.add(i);
		}
		System.out.println(map);
	}

	@Test
	public void testOld() {
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			int key = i % 5;
			List<Integer> list = map.get(key);
			if (list == null) {
				list = new ArrayList<>();
				map.put(key, list);
			}
			list.add(i);
		}
		System.out.println(map);
	}

	/**
	 * putIfAbsent 如果key存在则不存进行put
	 */
	@Test
	public void putIfAbsent() {
		ConcurrentHashMap<String, ConcurrentHashMap<Locale, String>> messages = new ConcurrentHashMap<>();
		messages.putIfAbsent(USER, new ConcurrentHashMap<Locale, String>());
		messages.get(USER).putIfAbsent(Locale.CHINA, "中文");
		messages.get(USER).putIfAbsent(Locale.ENGLISH, "英文");
		System.out.println(messages.get(USER));
	}

	/**
	 * ListIterator迭代器.
	 */
	@Test
	public void listIterator() {
		List<String> list = Arrays.asList("1", "2", "3");
		ListIterator<String> itr = list.listIterator(list.size());
		while (itr.hasPrevious()) {
			System.out.println(itr.previous());
		}
	}

	@Test
	public void peekAndMap1() {
		Stream.of("one", "two", "three", "four").peek(u -> u.toUpperCase()).forEach(System.out::println);
	}

	@Test
	public void peekAndMap2() {
		List<Map<String, String>> list = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("name", "zzl");
		list.add(map);
		list.stream().peek(CollectionTest::peekFun).forEach(System.out::println);
	}

	@Test
	public void contains() {
		List<Integer> dic = Arrays.asList(1, 3);
		List<Integer> allData = Arrays.asList(1, 3, 5, 7);
		allData.stream().filter(o -> dic.contains(o)).collect(Collectors.toList()).forEach(o -> {
			System.out.println(o);
		});
	}

	public static List<String> subtract(List<String> list1, List<String> list2) {
		ArrayList<String> result = new ArrayList();
		HashBag<String> bag = new HashBag(list2);
		Iterator i$ = list1.iterator();

		while (i$.hasNext()) {
			String e = (String) i$.next();
			if (!bag.remove(e, 1)) {
				result.add(e);
			}
		}

		return result;
	}

	@Test
	public void sub() {
		List<String> olds = new ArrayList<>();
		olds.add("a");
		olds.add("b");
		List<String> news = new ArrayList<>();
		news.add("a");
		news.add("c");
		news.add("d");

		System.out.println(subtract(olds, news));
		System.out.println(CollectionUtils.subtract(olds, news));
		olds.removeAll(news);
		System.out.println(olds);

	}

	@Test
	public void iterator() {
		Collection<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		Iterator var5 = list.iterator();

		while (var5.hasNext()) {
			System.out.println(var5.next());
		}
	}

	/**
	 * 只读集合
	 */
	@Test
	public void unmodifiableSet() {
		Set<String> read = Collections.unmodifiableSet(new HashSet<>(5));
		read.add("a");
	}

	@Data
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	class UserAccountSet {

		private String loginAccount;

		private String groupAccountName;

	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	class Column {

		private String name;

	}

}
