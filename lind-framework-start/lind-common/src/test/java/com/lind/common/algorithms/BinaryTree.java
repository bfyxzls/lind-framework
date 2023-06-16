package com.lind.common.algorithms;

/**
 * @author lind
 * @date 2023/5/30 15:20
 * @since 1.0.0
 */
public class BinaryTree {

	private Object key;

	private BinaryTree leftChild;

	private BinaryTree rightChild;

	public BinaryTree(Object rootObj) {
		this.key = rootObj;
		this.leftChild = null;
		this.rightChild = null;
	}

	public static void main(String[] args) {
		/*
		 * 以下为测试数据, 去掉 // 即可
		 */
		BinaryTree r = new BinaryTree('a');
		System.out.println(r.getRootVal());
		System.out.println(r.getLeftChild());
		r.insertLeft('b');
		System.out.println(r.getLeftChild());
		System.out.println(r.getLeftChild().getRootVal());
		r.insertRight('c');
		System.out.println(r.getRightChild());
		System.out.println(r.getRightChild().getRootVal());
		r.getRightChild().setRootVal("hello");
		System.out.println(r.getRightChild().getRootVal());
	}

	public void insertLeft(Object newNode) {
		if (this.leftChild == null) {
			this.leftChild = new BinaryTree(newNode);
		}
		else {
			BinaryTree t = new BinaryTree(newNode);
			t.leftChild = this.leftChild;
			this.leftChild = t;
		}
	}

	public void insertRight(Object newNode) {
		if (this.rightChild == null) {
			this.rightChild = new BinaryTree(newNode);
		}
		else {
			BinaryTree t = new BinaryTree(newNode);
			t.rightChild = this.rightChild;
			this.rightChild = t;
		}
	}

	public BinaryTree getRightChild() {
		return this.rightChild;
	}

	public BinaryTree getLeftChild() {
		return this.leftChild;
	}

	public Object getRootVal() {
		return this.key;
	}

	public void setRootVal(Object obj) {
		this.key = obj;
	}

	// 树的前序遍历
	// 树的后序遍历以及中序遍历见ParseTree.py
	public void preorder() {
		System.out.println(this.key);
		if (this.leftChild != null) {
			this.leftChild.preorder();
		}
		if (this.rightChild != null) {
			this.rightChild.preorder();
		}
	}

}
