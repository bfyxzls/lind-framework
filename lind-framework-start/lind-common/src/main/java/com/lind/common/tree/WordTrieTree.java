package com.lind.common.tree;

/**
 * 英文单词的前缀树.
 *
 * @author lind
 * @date 2023/5/6 15:56
 * @since 1.0.0
 */
public class WordTrieTree {

	TrieNode root;

	public WordTrieTree(TrieNode root) {
		this.root = root;
	}

	public static String[] transContext(String context) {
		String c = context.toLowerCase();
		c = c.replaceAll("[,?!.]", " ");
		c = c.substring(0, context.length() - 1);
		return c.split(" ");
	}

	public void insertElement(TrieNode root, String word) {
		if (word == null || word.isEmpty())
			return;
		char[] elements = word.toCharArray();
		int index = elements[0] - 'a';
		if (root.getNode()[index] == null) {
			root.getNode()[index] = new TrieNode();
			root.getNode()[index].setElement(elements[0]);
		}
		if (word.length() == 0)
			return;
		insertElement(root.getNode()[index], word.substring(1));
	}

	public boolean searchWord(TrieNode root, String word) {
		if (word == null || word.isEmpty())
			return false;
		TrieNode p = root;
		int index = 0;
		while (p != null && index < word.length()) {
			int k = word.charAt(index) - 'a';
			if (root.getNode()[k] != null) {
				if (index == word.length() - 1) {
					root.getNode()[k].setFreq(root.getNode()[k].getFreq() + 1);
					return true;
				}
				else {
					root = root.getNode()[k];
					index++;
				}
			}
			else {
				return false;
			}
		}
		return false;
	}

	static class TrieNode {

		private TrieNode[] node;

		private int freq;

		private char element;

		public TrieNode() {
			node = new TrieNode[26];
			freq = 0;
		}

		public TrieNode[] getNode() {
			return node;
		}

		public void setNode(TrieNode[] node) {
			this.node = node;
		}

		public int getFreq() {
			return freq;
		}

		public void setFreq(int freq) {
			this.freq = freq;
		}

		public char getElement() {
			return element;
		}

		public void setElement(char element) {
			this.element = element;
		}

	}

}
