package com.lind.common.minibase;

import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestBloomFilter {

	@Test
	public void testBloomFilter() throws IOException {
		String[] keys = { "hello world", "hi", "bloom", "filter", "key", "value", "1", "value" };
		BloomFilter bf = new BloomFilter(3, 10);
		byte[][] keyBytes = new byte[keys.length][];
		for (int i = 0; i < keys.length; i++) {
			keyBytes[i] = keys[i].getBytes();
		}
		bf.generate(keyBytes);
		Assert.assertTrue(bf.contains(Bytes.toBytes("hi")));
		Assert.assertFalse(bf.contains(Bytes.toBytes("h")));
		Assert.assertFalse(bf.contains(Bytes.toBytes("he")));
		Assert.assertTrue(bf.contains(Bytes.toBytes("hello world")));
		Assert.assertTrue(bf.contains(Bytes.toBytes("bloom")));
		Assert.assertTrue(bf.contains(Bytes.toBytes("key")));
	}

	@Test
	public void testGuavaBloomFilter() throws IOException {
		int expectedInsertions = 1000000;// 预期的插入元素数量
		double fpp = 0.01; // 期望的误判率
		com.google.common.hash.BloomFilter<String> bloomFilter = com.google.common.hash.BloomFilter
				.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp);
		bloomFilter.put("apple");
		bloomFilter.put("pear");
		bloomFilter.put("yellow");

		System.out.println("apple:" + bloomFilter.mightContain("apple"));
		System.out.println("pear:" + bloomFilter.mightContain("pear"));
		System.out.println("red:" + bloomFilter.mightContain("red"));
	}

}
