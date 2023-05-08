package com.lind.common.tree;

import java.util.HashMap;

/**
 * 字典树，前缀树.
 */
public class TrieNode {

	Character value;

	boolean tail = false;

	HashMap<Character, TrieNode> son = new HashMap<>();

	TrieNode(Character c) {
		value = c;
	}

}
