package com.lind.common.tree;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/5/6 16:02
 * @since 1.0.0
 */
public class WordTrieTreeTest {

	@Test
	public void find() {
		WordTrieTree.TrieNode root = new WordTrieTree.TrieNode();
		WordTrieTree tree = new WordTrieTree(root);
		String context = "中国人民";
		String target = "人民";
		for (String word : WordTrieTree.transContext(context)) {
			tree.insertElement(root, word);
		}
		System.out.println(tree.searchWord(root, target));
	}

}
