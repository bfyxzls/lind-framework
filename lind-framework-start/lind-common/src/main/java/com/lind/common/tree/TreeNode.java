package com.lind.common.tree;

/**
 * 二叉树节点.
 *
 * @author lind
 * @date 2023/4/20 9:37
 * @since 1.0.0
 */
public class TreeNode {

	private int val;

	private TreeNode left;

	private TreeNode right;

	public TreeNode(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public TreeNode getLeft() {
		return left;
	}

	public void setLeft(TreeNode left) {
		this.left = left;
	}

	public TreeNode getRight() {
		return right;
	}

	public void setRight(TreeNode right) {
		this.right = right;
	}

	// 前序遍历
	public void preOrder(TreeNode root) {
		if (root != null) {
			System.out.println(root.val);
			preOrder(root.left);
			preOrder(root.right);
		}
	}

	// 中序遍历
	public void inOrder(TreeNode root) {
		if (root != null) {
			inOrder(root.left);
			System.out.println(root.val);
			inOrder(root.right);
		}
	}

	// 后序遍历
	public void postOrder(TreeNode root) {
		if (root != null) {
			postOrder(root.left);
			postOrder(root.right);
			System.out.println(root.val);
		}
	}

}
