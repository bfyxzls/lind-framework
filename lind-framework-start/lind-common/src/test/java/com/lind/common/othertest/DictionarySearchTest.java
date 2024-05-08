package com.lind.common.othertest;

import com.lind.common.tree.DictionarySearch;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class DictionarySearchTest {

	@Test
	public void rootComplete() {
		DictionarySearch ds = new DictionarySearch();
		// 先定义关键词
		ds.insertKeyword("美国");
		ds.insertKeyword("美海军");
		ds.insertKeyword("美国海军");
		ds.insertKeyword("战斗机");
		HashMap<String, Integer> result = ds.search("报道称，美国海军正面临严重的战机荒。美国总统表现出色");
		System.out.println(result);
	}

}
