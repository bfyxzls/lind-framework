package com.lind.common.tree;

/**
 * 一颗红黑树.
 * @author lind
 * @date 2023/5/15 9:19
 * @since 1.0.0
 */
public class RedBlackTree<T extends Comparable<T>> {

    /**
     * 其中，插入操作使用insert()方法实现，该方法首先在树中插入新节点，并使用insertFixUp()方法恢复红黑树的平衡。
     *
     * 在insertFixUp()方法中，我们使用了左旋和右旋操作，以恢复树的平衡。当插入的节点的父节点和叔父节点都是红色时，需要进行颜色变更和旋转操作，直至树重新平衡。
     *
     * 至于其他操作，例如删除节点、查找节点等，可以根据需要自行实现。
     *
     */
    private Node<T> root;

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private static class Node<T> {
        T value;
        Node<T> left, right, parent;
        boolean color;

        Node(T value) {
            this.value = value;
            this.color = RED;
        }
    }

    public void insert(T value) {
        Node<T> node = new Node<>(value);
        if (root == null) {
            root = node;
        } else {
            Node<T> parent = null;
            Node<T> current = root;
            while (current != null) {
                parent = current;
                if (value.compareTo(current.value) < 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
            node.parent = parent;
            if (value.compareTo(parent.value) < 0) {
                parent.left = node;
            } else {
                parent.right = node;
            }
            insertFixUp(node);
        }
    }

    private void insertFixUp(Node<T> node) {
        while (node.parent != null && node.parent.color == RED) {
            if (node.parent == node.parent.parent.left) {
                Node<T> uncle = node.parent.parent.right;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        leftRotate(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rightRotate(node.parent.parent);
                }
            } else {
                Node<T> uncle = node.parent.parent.left;
                if (uncle != null && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rightRotate(node);
                    }
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    leftRotate(node.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void leftRotate(Node<T> node) {
        Node<T> right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        right.parent = node.parent;
        if (node.parent == null) {
            root = right;
        } else if (node == node.parent.left) {
            node.parent.left = right;
        } else {
            node.parent.right = right;
        }
        right.left = node;
        node.parent = right;
    }

    private void rightRotate(Node<T> node) {
        Node<T> left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        left.parent = node.parent;
        if (node.parent == null) {
            root = left;
        } else if (node == node.parent.right) {
            node.parent.right = left;
        } else {
            node.parent.left = left;
        }
        left.right = node;
        node.parent = left;
    }

    public Node<T> search(T value) {
        Node<T> node = root;
        while (node != null && value.compareTo(node.value) != 0) {
            if (value.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }
}
