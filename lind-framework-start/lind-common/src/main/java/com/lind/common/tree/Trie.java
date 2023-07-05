package com.lind.common.tree;

/**
 * @author lind
 * @date 2023/7/5 11:07
 * @since 1.0.0
 */
public class Trie {

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	// 测试代码
	public static void main(String[] args) {
		Trie trie = new Trie();

		// 插入测试
		trie.insert("apple");
		trie.insert("banana");
		trie.insert("orange");

		// 搜索测试
		System.out.println(trie.search("apple")); // 输出: true
		System.out.println(trie.search("banana")); // 输出: true
		System.out.println(trie.search("orange")); // 输出: true
		System.out.println(trie.search("pear")); // 输出: false

		// 前缀搜索测试
		System.out.println(trie.startsWith("app")); // 输出: true
		System.out.println(trie.startsWith("ban")); // 输出: true
		System.out.println(trie.startsWith("ora")); // 输出: true
		System.out.println(trie.startsWith("pea")); // 输出: false
	}

	public void insert(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			TrieNode node = current.getChild(ch);
			if (node == null) {
				node = new TrieNode();
				current.setChild(ch, node);
			}
			current = node;
		}
		current.setEndOfWord(true);
	}

	public boolean search(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			TrieNode node = current.getChild(ch);
			if (node == null) {
				return false;
			}
			current = node;
		}
		return current.isEndOfWord();
	}

	public boolean startsWith(String prefix) {
		TrieNode current = root;
		for (int i = 0; i < prefix.length(); i++) {
			char ch = prefix.charAt(i);
			TrieNode node = current.getChild(ch);
			if (node == null) {
				return false;
			}
			current = node;
		}
		return true;
	}

	// 前缀树节点类
	class TrieNode {

		private TrieNode[] children;

		private boolean isEndOfWord;

		public TrieNode() {
			children = new TrieNode[26]; // 假设只包含小写字母
			isEndOfWord = false;
		}

		public TrieNode getChild(char ch) {
			return children[ch - 'a'];
		}

		public void setChild(char ch, TrieNode node) {
			children[ch - 'a'] = node;
		}

		public boolean isEndOfWord() {
			return isEndOfWord;
		}

		public void setEndOfWord(boolean endOfWord) {
			isEndOfWord = endOfWord;
		}

	}

}
