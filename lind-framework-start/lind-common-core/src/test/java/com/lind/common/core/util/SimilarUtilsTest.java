package com.lind.common.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;

import static com.lind.common.core.util.SimilarUtils.calculateSimilarity;
import static com.lind.common.core.util.SimilarUtils.degree;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimilarUtilsTest {

	@Test
	public void tagA() {
		// 结果[5,39]，从第5个字符开始，共有39个字符返回
		LinkedList<String> result = SimilarUtils.getATagIndex(
				"1234<a href=ddddddsddd class=.fjLink>dd</a>ddsafdasfadsdfsa1234<a href=ccc class=.fjLink>dad</a>");

		for (Iterator iter = result.iterator(); iter.hasNext();) {
			iter.next();
		}
		StringBuffer message = new StringBuffer();
		message.append(LocalDateTime.now()).append("\t").append("hello world").append("\n\r");
		System.out.println(message.toString());

		System.out.println(result);
	}

	@Test
	public void demo1() {
		assertEquals(Double.parseDouble("1.0"), degree("hello", "hello"), 0);
		System.out.println(degree("hello", "hellos")); // 0.8333333333333334
		System.out.println(degree("hello", "hello1")); // 0.8333333333333334
		System.out.println(degree("hello", "hello world")); // 0.5
	}

	@Test
	public void demo2() {
		System.out.println(calculateSimilarity("hello", "hellos")); // 83
		System.out.println(calculateSimilarity("hello", "hello1")); // 83
		System.out.println(calculateSimilarity("hello", "hello world")); // 45
	}

}
