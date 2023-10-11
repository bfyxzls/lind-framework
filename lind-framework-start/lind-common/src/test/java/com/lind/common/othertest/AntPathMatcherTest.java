package com.lind.common.othertest;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {

	@Test
	public void permit() {
		AntPathMatcher matcher = new AntPathMatcher();
		// 普通配置
		boolean match1 = matcher.match("/admin/test", "/admin/test");
		System.out.println(match1);
		// 通配符
		boolean match2 = matcher.match("/admin/test/**", "/admin/test/aaa/bbb");
		System.out.println(match2);
		// 通配符
		boolean match21 = matcher.match("/admin/test/*", "/admin/test/aaa?a=1");
		System.out.println(match21);
		// path variable
		boolean match3 = matcher.match("/admin/test/{id}", "/admin/test/aaa");
		System.out.println(match3);
	}

}
