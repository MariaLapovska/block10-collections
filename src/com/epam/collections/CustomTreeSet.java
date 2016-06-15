package com.epam.collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Custom tree set implementation
 */
public class CustomTreeSet<T extends Comparable<T>> implements Set<T> {
    private Node<T> root = null;
    private int size = 0;
    private int modCount = 0;

    private final static class Node<T> {
        Node<T> left;
        Node<T> right;
        Node<T> parent;
        T value;

        public Node(T value) {
            left = null;
            right = null;
            parent = null;
            this.value = value;
        }

        @Override
        public String toString() {
            if (value != null) {
                return value.toString();
            }
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        Node<T> current = root;
        T k = (T) o;
        int cmp;

        while (current != null) {
            cmp = k.compareTo(current.value);

            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                current = current.left;
            } else { //(cmp > 0)
                current = current.right;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        if (size > 0) {
            root = null;
            size = 0;
            modCount++;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        fillArray(root, a, 0);

        return a;
    }

    @Override
    public <S> S[] toArray(S[] a) {
        if (a.length < size) {
            a = (S[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else if (a.length > size) {
            a[size] = null;
        }
        System.arraycopy(toArray(), 0, a, 0, size);

        return a;
    }

    @Override
    public boolean add(T o) {
        if (size == 0) {
            root = new Node<>(o);
        } else {
            Node<T> current = root;
            Node<T> parent = null;
            int cmp = 0;

            while (current != null) {
                parent = current;
                cmp = o.compareTo(current.value);

                if (cmp > 0) {
                    current = current.right;
                } else if (cmp < 0) {
                    current = current.left;
                } else { // Key already in tree.
                    return false;
                }
            }

            Node<T> newNode = new Node<>(o);
            newNode.parent = parent;

            if (cmp > 0) {
                parent.right = newNode;
            } else { // cmp < 0
                parent.left = newNode;
            }
        }

        modCount++;
        size++;

        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> node = getNode((T) o);

        if (node == null) {
            return false;
        }
        removeNode(node);

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.size() == 0) {
            return false;
        }

        Iterator<? extends T> iterator = c.iterator();
        boolean flag = false;

        while (iterator.hasNext()) {
            if (add(iterator.next())) {
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public boolean removeAll(Collection c) {
        if (size == 0) {
            return false;
        }

        Iterator<T> iterator = iterator();
        Node<T> temp = null;

        while (iterator.hasNext()) {
            temp = getNode(iterator.next());

            if (c.contains(temp.value)) {
                break;
            }
        }
        if (!iterator.hasNext()) {
            return false;
        }

        modCount++;

        while (iterator.hasNext()) {
            if (c.contains(temp.value)) {
                removeNode(temp);
            }

            temp = getNode(iterator.next());
        }

        if (c.contains(temp.value)) {
            removeNode(temp);
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        if (size == 0) {
            return false;
        }

        Iterator<T> iterator = iterator();
        Node<T> temp = null;

        while (iterator.hasNext()) {
            temp = getNode(iterator.next());

            if (!c.contains(temp.value)) {
                break;
            }
        }
        if (!iterator.hasNext()) {
            return false;
        }

        modCount++;

        while (iterator.hasNext()) {
            if (!c.contains(temp.value)) {
                removeNode(temp);
            }

            temp = getNode(iterator.next());
        }

        if (!c.contains(temp.value)) {
            removeNode(temp);
        }

        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        if (size == 0) {
            return false;
        }

        Iterator iterator = c.iterator();

        while (iterator.hasNext()) {
            if (!contains(iterator.next())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = iterator();

        sb.append("[");
        while (iterator.hasNext()) {
            sb.append(iterator.next());

            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    //Iterator

    @Override
    public Iterator<T> iterator() {
        return treeIterator();
    }

    private Iterator<T> treeIterator() {

        return new Iterator<T>() {
            Node<T> next = getMin();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Node<T> r = next;

                if (next.right != null) {
                    next = next.right;
                    while (next.left != null)
                        next = next.left;
                    return r.value;
                } else {
                    while (true) {
                        if (next.parent == null) {
                            next = null;
                            return r.value;
                        }
                        if (next.parent.left == next) {
                            next = next.parent;
                            return r.value;
                        }

                        next = next.parent;
                    }
                }
            }

            private Node<T> getMin() {
                if (size == 0) {
                    return null;
                }

                Node<T> current = root;

                while (current.left != null) {
                    current = current.left;
                }

                return current;
            }
        };
    }

    // Auxiliary methods

    private int fillArray(Node root, Object[] a, int pos) {
        if (root.left != null) {
            pos = fillArray(root.left, a, pos);
        }

        a[pos++] = root.value;

        if (root.right != null) {
            pos = fillArray(root.right, a, pos);
        }

        return pos;
    }

    private Node<T> getNode(T k) {
        Node<T> current = root;
        int cmp;

        while (current != null) {
            cmp = k.compareTo(current.value);

            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else { //(cmp > 0)
                current = current.right;
            }
        }

        return null;
    }

    private void removeNode(Node<T> node) {
        Node<T> splice;
        Node<T> child;

        modCount++;
        size--;

        // Find splice, the node at the position to actually remove from the tree.
        if (node.left == null) {
            // Node to be deleted has 0 or 1 children.
            splice = node;
            child = node.right;
        } else if (node.right == null) {
            // Node to be deleted has 1 child.
            splice = node;
            child = node.left;
        } else {
            // Node has 2 children. Splice is node's predecessor, and we swap
            // its contents into node.
            splice = node.left;
            while (splice.right != null) {
                splice = splice.right;
            }
            child = splice.left;
            node.value = splice.value;
        }

        // Unlink splice from the tree.
        Node<T> parent = splice.parent;
        if (child != null) {
            child.parent = parent;
        }
        if (parent == null) {
            // Special case for 0 or 1 node remaining.
            root = child;
            return;
        }

        if (splice == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }
}
