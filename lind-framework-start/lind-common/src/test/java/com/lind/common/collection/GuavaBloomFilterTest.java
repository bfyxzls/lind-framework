package com.lind.common.collection;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

/**
 * @author lind
 * @date 2023/12/4 14:17
 * @since 1.0.0
 */
public class GuavaBloomFilterTest {

	@Test
	public void test() {
		int expectedInsertions = 1000000;// 期望插入的数量
		double fpp = 0.01; // 期望的误判率
		BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
				expectedInsertions, fpp);
		bloomFilter.put("apple");
		bloomFilter.put("banana");
		bloomFilter.put("orange");
		if (bloomFilter.mightContain("apple")) {
			System.out.println("The element 'apple' might exist in the BloomFilter.");
		}
		else {
			System.out.println("The element 'apple' definitely does not exist in the BloomFilter.");
		}
	}

}
