package com.lind.common.tree;

import java.util.HashMap;

public class DictionarySearch {

	// 2.预初始化根节点
	private TrieNode root = new TrieNode(null);

	// 3.插入关键词
	public void insertKeyword(String keyword) {
		TrieNode cur = root;
		char c;
		int length = keyword.length();
		for (int i = 0; i < length; i++) {
			c = keyword.charAt(i);
			if (!cur.son.containsKey(c)) {
				cur.son.put(c, new TrieNode(c));
			}
			cur = cur.son.get(c);
		}
		cur.tail = true;
	}

	/**
	 * 统计keyword在document中出现的频率
	 * @param document
	 * @return
	 */
	public HashMap<String, Integer> search(String document) {
		HashMap<String, Integer> result = new HashMap<>();
		if (document == null || document.equals("") || root.son.size() == 0)
			return result;
		TrieNode cur = root;
		int start = 0, end = 0, len = document.length();
		while (start < len) {
			if (end < len && cur.son.containsKey(document.charAt(end))) {
				cur = cur.son.get(document.charAt(end));
				end++;
				if (cur.tail) {
					result.merge(document.substring(start, end), 1, Integer::sum);
				}
			}
			else {
				end = ++start;
				cur = root;
			}
		}
		return result;
	}

}
