package com.lind.common.tree;

import org.junit.jupiter.api.Test;

/**
 * 红黑树是一种自平衡的二叉查找树，它保证从根节点到叶子节点的最长路径不超过最短路径的两倍。
 *
 * @author lind
 * @date 2023/5/15 9:22
 * @since 1.0.0
 */
public class RedBlackTreeTest {

	@Test
	public void insert() {
		RedBlackTree<Integer> tree = new RedBlackTree<>();
		tree.insert(10);
		tree.insert(20);
	}

}
