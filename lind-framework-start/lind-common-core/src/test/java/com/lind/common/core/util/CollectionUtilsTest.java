package com.lind.common.core.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lind
 * @date 2023/6/21 15:35
 * @since 1.0.0
 */
public class CollectionUtilsTest {

	@Test
	public void join() {
		Collection<String> list = Arrays.asList("1", "a", "2", "b");
		System.out.println(CollectionUtils.join(list));
	}

	@Test
	public void mapReduce() {
		List<Integer> numbers = Arrays.asList(1, 2, 3);

		// 使用Stream进行Map操作，将每个元素乘以2
		int sum = numbers.stream().mapToInt(n -> n * 2) // Map操作，将每个元素乘以2
				.sum(); // Reduce操作，求和

		System.out.println("Sum of doubled numbers: " + sum);
	}

	@Test
	public void mapReduce2() throws IOException {
		File file = ResourceUtils.getFile("classpath:word.txt");
		InputStream inputStream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);
		Stream<String> lines = br.lines();// 将文件内容转换为一个Stream，每行作为一个元素。
		Map<String, Long> wordCountMap = lines.flatMap(line -> Arrays.stream(line.split("\\s+"))) // 将每一行拆分为单词
				.map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase()) // 去除标点并转为小写
				.filter(word -> !word.isEmpty()) // 过滤掉空单词
				.collect(Collectors.groupingBy(word -> word, // 按单词分组
						Collectors.counting() // 统计每个单词的数量
				));

		// 打印单词及其出现次数
		wordCountMap.forEach((word, count) -> System.out.println(word + ": " + count));

	}

}
