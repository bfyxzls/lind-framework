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

	// 前序遍历 前序遍历(DLR)首先访问根结点然后遍历左子树，最后遍历右子树
	public void preOrder(BinaryTreeNode root) {
		if (root != null) {
			System.out.println(root.val);
			preOrder(root.left);
			preOrder(root.right);
		}
	}

	// 中序遍历 中序遍历(LDR)是二叉树遍历的一种，也叫做中根遍历、中序周游。在二叉树中，中序遍历首先遍历左子树，然后访问根结点，最后遍历右子树。
	public void inOrder(BinaryTreeNode root) {
		if (root != null) {
			inOrder(root.left);
			System.out.println(root.val);
			inOrder(root.right);
		}
	}

	// 后序遍历 后序遍历(LRD)有递归算法和非递归算法两种。在二叉树中，先左后右再根，即首先遍历左子树，然后遍历右子树，最后访问根结点。
	public void postOrder(BinaryTreeNode root) {
		if (root != null) {
			postOrder(root.left);
			postOrder(root.right);
			System.out.println(root.val);
		}
	}

}
