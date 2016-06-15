package com.epam.collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Custom array list implementation
 */
public class CustomArrayList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] data;
    private int size = 0;
    private int modCount = 0;

    public CustomArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }

        data = (T[]) new Object[initialCapacity];
    }

    public CustomArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public CustomArrayList(Collection<? extends T> c) {
        this((int) (c.size() * 1.1f));
        addAll(c);
    }

    public void ensureCapacity(int minCapacity) {
        int current = data.length;

        if (minCapacity > current) {
            T[] newData = (T[]) new Object[Math.max(current * 2, minCapacity)];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
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
        return indexOf(o) != -1;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] toArray() {
        T[] array = (T[]) new Object[size];
        System.arraycopy(data, 0, array, 0, size);

        return array;
    }

    @Override
    public <S> S[] toArray(S[] a) {
        if (a.length < size) {
            a = (S[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else if (a.length > size) {
            a[size] = null;
        }
        System.arraycopy(data, 0, a, 0, size);

        return a;
    }

    @Override
    public T get(int index) {
        checkBoundExclusive(index);
        return (T) data[index];
    }

    @Override
    public T set(int index, T o) {
        checkBoundExclusive(index);
        T result = (T) data[index];
        data[index] = o;

        return result;
    }

    @Override
    public boolean add(T o) {
        modCount++;
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        data[size++] = o;

        return true;
    }

    @Override
    public void add(int index, T o) {
        checkBoundInclusive(index);
        modCount++;
        if (size == data.length) {
            ensureCapacity(size + 1);
        }
        if (index != size) {
            System.arraycopy(data, index, data, index + 1, size - index);
        }
        data[index] = o;
        size++;
    }

    @Override
    public T remove(int index) {
        checkBoundExclusive(index);
        T t = (T) data[index];
        modCount++;

        if (index != --size) {
            System.arraycopy(data, index + 1, data, index, size - index);
        }
        data[size] = null;

        return t;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(o)) {
                System.arraycopy(data, i + 1, data, i, size - i - 1);
                data[--size] = null;
                modCount++;
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        if (size > 0) {
            modCount++;
            Arrays.fill(data, 0, size, null);
            size = 0;
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        checkBoundInclusive(index);
        Iterator<? extends T> iterator = c.iterator();
        int cSize = c.size();
        int end = index + cSize;

        modCount++;
        if (cSize + size > data.length) {
            ensureCapacity(size + cSize);
        }
        if (size > 0 && index != size) {
            System.arraycopy(data, index, data, end, size - index);
        }

        size += cSize;

        for ( ; index < end; index++) {
            data[index] = iterator.next();
        }

        return cSize > 0;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("Lower index: " + fromIndex +
                                               " bigger than upper index: " + toIndex);
        }

        checkBoundExclusive(fromIndex);
        checkBoundInclusive(toIndex);

        List<T> newList = new CustomArrayList<>();

        if (fromIndex == toIndex) {
            return newList;
        }

        for (int i = fromIndex; i < toIndex; i++) {
            newList.add((T) data[i]);
        }

        return newList;
    }

    @Override
    public boolean removeAll(Collection c) {
        int i, j;

        for (i = 0; i < size; i++) {
            if (c.contains(data[i])) {
                break;
            }
        }
        if (i == size) {
            return false;
        }

        modCount++;

        for (j = i++; i < size; i++) {
            if (!c.contains(data[i])) {
                data[j++] = data[i];
            }
        }

        size -= i - j;

        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        int i, j;

        for (i = 0; i < size; i++) {
            if (!c.contains(data[i])) {
                break;
            }
        }
        if (i == size) {
            return false;
        }

        modCount++;

        for (j = i++; i < size; i++) {
            if (c.contains(data[i])) {
                data[j++] = data[i];
            }
        }

        size -= i - j;

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
        int i;

        sb.append("[");
        for (i = 0; i < size - 1; i++) {
            sb.append(data[i]);
            sb.append(", ");
        }
        sb.append(data[i]);
        sb.append("]");

        return sb.toString();
    }

    //Iterator

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        checkBoundInclusive(index);

        return new ListIterator<T>() {
            int currentIndex = index;
            int modCount = CustomArrayList.this.modCount;

            @Override
            public boolean hasNext() {
                checkMod();
                return currentIndex < size;
            }

            @Override
            public T next() {
                checkMod();
                return (T) data[currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                checkMod();
                return currentIndex > 0;
            }

            @Override
            public T previous() {
                checkMod();
                return (T) data[--currentIndex];
            }

            @Override
            public int nextIndex() {
                checkMod();
                return currentIndex + 1;
            }

            @Override
            public int previousIndex() {
                checkMod();
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                checkMod();
                CustomArrayList.this.remove(currentIndex--);
                modCount++;
            }

            @Override
            public void set(T t) {
                checkMod();
                CustomArrayList.this.set(currentIndex, t);
                modCount++;
            }

            @Override
            public void add(T t) {
                checkMod();
                CustomArrayList.this.add(currentIndex, t);
                modCount++;
            }

            private void checkMod() {
                if (modCount != CustomArrayList.this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    //Auxiliary methods

    private void checkBoundInclusive(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkBoundExclusive(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}