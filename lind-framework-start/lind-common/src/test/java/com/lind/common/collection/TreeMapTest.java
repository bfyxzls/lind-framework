package com.lind.common.collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.common.core.util.ObjectByteUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author lind
 * @date 2023/6/8 11:44
 * @since 1.0.0
 */
@Slf4j
public class TreeMapTest {

	/**
	 * hashMap不能保持顺序
	 */
	@Test
	public void mapByteNotSort() {
		Map<Integer, String> hashMap = new HashMap<>();
		hashMap.put(3, "three");
		hashMap.put(8, "eight");
		hashMap.put(1, "one");
		hashMap.put(7, "seven");
		// Serialize
		byte[] buffer = ObjectByteUtils.toByteArray(hashMap);
		HashMap<String, Integer> deserializedMap = (HashMap<String, Integer>) ObjectByteUtils.toObject(buffer);
		log.info("map={}", deserializedMap);

	}

	@Test
	public void mapJsonNotSort() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<Integer, String> hashMap = new HashMap<>();
		hashMap.put(3, "three");
		hashMap.put(8, "eight");
		hashMap.put(1, "one");
		hashMap.put(7, "seven");

		// Serialize
		String buffer = objectMapper.writeValueAsString(hashMap);
		log.info("source map={}", buffer);
		HashMap<Integer, String> deserializedMap = objectMapper.readValue(buffer,
				new TypeReference<HashMap<Integer, String>>() {
				});
		log.info("map={}", deserializedMap);

	}

	@Test
	public void testSort() throws JsonProcessingException {
		TreeMap<Person, String> treeMap = new TreeMap();
		treeMap.put(new Person("zzl", 40), "zzl");
		treeMap.put(new Person("zhz", 14), "zhz");
		treeMap.forEach((i, o) -> {
			System.out.println(i);
		});
		System.out.println(new ObjectMapper().writeValueAsString(treeMap));

	}

	@Test
	public void linkHashMapSort() throws JsonProcessingException {
		// Use LinkedHashMap to maintain insertion order
		LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();
		linkedHashMap.put(3, "three");
		linkedHashMap.put(8, "eight");
		linkedHashMap.put(1, "one");
		linkedHashMap.put(7, "seven");

		// Serialize LinkedHashMap to JSON
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(linkedHashMap);
		System.out.println("Serialized JSON: " + json);

		// Deserialize JSON to LinkedHashMap
		LinkedHashMap<String, Integer> deserializedMap = objectMapper.readValue(json, LinkedHashMap.class);
		System.out.println("Deserialized LinkedHashMap: " + deserializedMap);
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
