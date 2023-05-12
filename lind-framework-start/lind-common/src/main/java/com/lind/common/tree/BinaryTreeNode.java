package com.lind.common.tree;

/**
 * 二叉树节点.
 *
 * @author lind
 * @date 2023/4/20 9:37
 * @since 1.0.0
 */
public class BinaryTreeNode {

	private int val;

	private BinaryTreeNode left;

	private BinaryTreeNode right;

	public BinaryTreeNode(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public BinaryTreeNode getLeft() {
		return left;
	}

	public void setLeft(BinaryTreeNode left) {
		this.left = left;
	}

	public BinaryTreeNode getRight() {
		return right;
	}

	public void setRight(BinaryTreeNode right) {
		this.right = right;
	}

	// 前序遍历
	public void preOrder(BinaryTreeNode root) {
		if (root != null) {
			System.out.println(root.val);
			preOrder(root.left);
			preOrder(root.right);
		}
	}

	// 中序遍历
	public void inOrder(BinaryTreeNode root) {
		if (root != null) {
			inOrder(root.left);
			System.out.println(root.val);
			inOrder(root.right);
		}
	}

	// 后序遍历
	public void postOrder(BinaryTreeNode root) {
		if (root != null) {
			postOrder(root.left);
			postOrder(root.right);
			System.out.println(root.val);
		}
	}

}
