package com.lind.common.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;

public class SimilarUtilsTest {

	@Test
	public void tagA() {
		// 结果[5,39]，从第5个字符开始，共有39个字符返回
		LinkedList<String> result = SimilarUtils.tagA(
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
	public void degree() {
		Assert.assertEquals(Double.parseDouble("1.0"), SimilarUtils.degree("hello", "hello"), 0);
		System.out.println(SimilarUtils.degree("hello", "hellos"));
		System.out.println(SimilarUtils.degree("hello", "hello1"));
		System.out.println(SimilarUtils.degree("hello", "hello world"));
	}

}
