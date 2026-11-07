
package storage;

import java.util.ArrayList;
import java.util.List;

public class BTree {

    private static final int T = 3;

    private class Node {
        int n;
        boolean leaf = true;
        int[] keys = new int[2 * T - 1];
        Node[] children = new Node[2 * T];
    }

    private Node root = new Node();

    public void insert(int key) {
        Node r = root;

        if (r.n == 2 * T - 1) {
            Node newRoot = new Node();
            newRoot.leaf = false;
            newRoot.children[0] = r;
            splitChild(newRoot, 0);
            root = newRoot;
        }

        insertNonFull(root, key);
    }

    private void insertNonFull(Node node, int key) {
        int i = node.n - 1;

        if (node.leaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }

            if (i >= 0 && key == node.keys[i]) {
                return;
            }

            node.keys[i + 1] = key;
            node.n++;
        } else {
            while (i >= 0 && key < node.keys[i]) {
                i--;
            }

            i++;

            if (key == node.keys[i - 1 < 0 ? 0 : i - 1] && i > 0) {
                return;
            }

            if (node.children[i].n == 2 * T - 1) {
                splitChild(node, i);

                if (key > node.keys[i]) {
                    i++;
                } else if (key == node.keys[i]) {
                    return;
                }
            }

            insertNonFull(node.children[i], key);
        }
    }

    private void splitChild(Node parent, int index) {
        Node full = parent.children[index];
        Node sibling = new Node();

        sibling.leaf = full.leaf;
        sibling.n = T - 1;

        for (int j = 0; j < T - 1; j++) {
            sibling.keys[j] = full.keys[j + T];
        }

        if (!full.leaf) {
            for (int j = 0; j < T; j++) {
                sibling.children[j] = full.children[j + T];
            }
        }

        full.n = T - 1;

        for (int j = parent.n; j >= index + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }

        parent.children[index + 1] = sibling;

        for (int j = parent.n - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }

        parent.keys[index] = full.keys[T - 1];
        parent.n++;
    }

    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node node, int key) {
        int i = 0;

        while (i < node.n && key > node.keys[i]) {
            i++;
        }

        if (i < node.n && key == node.keys[i]) {
            return true;
        }

        if (node.leaf) {
            return false;
        }

        return search(node.children[i], key);
    }

    public void display() {
        display(root);
    }

    private void display(Node node) {
        int i;

        for (i = 0; i < node.n; i++) {
            if (!node.leaf) {
                display(node.children[i]);
            }

            System.out.print(node.keys[i] + " ");
        }

        if (!node.leaf) {
            display(node.children[i]);
        }
    }
}
