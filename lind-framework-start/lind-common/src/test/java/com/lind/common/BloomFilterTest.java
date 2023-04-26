package com.lind.common;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.lind.common.minibase.Bytes;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.lind.common.minibase.DiskFile.BLOOM_FILTER_BITS_PER_KEY;
import static com.lind.common.minibase.DiskFile.BLOOM_FILTER_HASH_COUNT;

/**
 * 布隆过滤器适合大数据判重的场景，如网络爬虫中判断一个URL是否已经爬取过，判断一个用户是否在黑名单中，判断一个邮件是否是垃圾邮件，等等。
 * 优点：占用空间小，效率高，简而言之，就是以正确率换空间和时间。
 * 缺点：有一定的误判率，一个URL经过布隆过滤器判断没爬取过，那么一定没爬取过，一个URL经过布隆过滤器判断爬取过，可能并没有爬取过，这种情况会有误判。
 * 布隆过滤器本身是基于位图的，是对位图的一种改进，位图在java中的实现就是BitSet。
 */
public class BloomFilterTest {

	@Test
	public void main() {
		int size = 1_000;
		BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);
		for (int i = 0; i < size; i++) {
			bloomFilter.put(i);
		}

		// 一定不存在
		for (int i = 0; i < size; i++) {
			if (!bloomFilter.mightContain(i)) {
				System.out.println("有坏人逃脱了");
			}
		}
		// 可能存在
		List<Integer> list = new ArrayList<>(1000);
		for (int i = size + 1000; i < size + 2000; i++) {
			if (bloomFilter.mightContain(i)) {
				list.add(i);
			}
		}
		System.out.println("有误伤的数量：" + list.size());
	}

	@Test
	public void miniBaseBloom() {
		com.lind.common.minibase.BloomFilter bloomFilter = new com.lind.common.minibase.BloomFilter(
				BLOOM_FILTER_HASH_COUNT, BLOOM_FILTER_BITS_PER_KEY);
		byte[][] bytes = new byte[26][];

		for (int i = 0; i < 26; i++) {
			bytes[i] = Bytes.toBytes(i + 10);
		}
		bloomFilter.generate(bytes);

		Assert.assertTrue(bloomFilter.contains(Bytes.toBytes(15)));
		Assert.assertFalse(bloomFilter.contains(Bytes.toBytes(45)));
	}

	@Test
	public void testStr() {
		BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 10);
		// A~Z 65~90
		for (int i = 65; i < 75; i++) {
			bloomFilter.put(new String(new char[] { (char) i }));
		}
		System.out.println("is contaions B:" + bloomFilter.mightContain("B"));
	}

}
