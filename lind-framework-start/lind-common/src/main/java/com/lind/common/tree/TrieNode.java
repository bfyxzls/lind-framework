package com.lind.common.tree;

import java.util.HashMap;

/**
 * 字典树，前缀树.
 */
public class TrieNode {

	/**
	 * 当前字符.
	 */
	Character value;

	/**
	 * 关键字是否结束.
	 */
	boolean tail = false;

	/**
	 * 子节点. 使用hashMap为了更快的找到某个元素.
	 */
	HashMap<Character, TrieNode> son = new HashMap<>();

	TrieNode(Character c) {
		value = c;
	}

}
