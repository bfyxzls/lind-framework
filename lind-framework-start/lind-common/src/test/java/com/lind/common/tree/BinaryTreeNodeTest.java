package com.lind.common.tree;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/4/20 9:39
 * @since 1.0.0
 */
public class BinaryTreeNodeTest {

	@Test
	public void iterator() {
		BinaryTreeNode root = new BinaryTreeNode(1);
		BinaryTreeNode left = new BinaryTreeNode(2);
		BinaryTreeNode right = new BinaryTreeNode(3);
		root.setLeft(left);
		root.setRight(right);
		System.out.println("前序遍历");// 前序遍历(DLR)首先访问根结点然后遍历左子树，最后遍历右子树
		root.preOrder(root);// 1,2,3
		System.out.println("中序遍历");// 中序遍历(LDR)是二叉树遍历的一种，也叫做中根遍历、中序周游。在二叉树中，中序遍历首先遍历左子树，然后访问根结点，最后遍历右子树。
		root.inOrder(root);// 2,1,3
		System.out.println("后序遍历");// 后序遍历(LRD)有递归算法和非递归算法两种。在二叉树中，先左后右再根，即首先遍历左子树，然后遍历右子树，最后访问根结点。
		root.postOrder(root);// 2,3,1
	}

}
