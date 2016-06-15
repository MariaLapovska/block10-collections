package com.epam.collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Custom linked list implementation
 */
public class CustomLinkedList<T> implements Iterable<T>, Queue<T> {
    private Entry<T> first;
    private Entry<T> last;
    private int size = 0;
    private int modCount = 0;

    private static final class Entry<T> {
        private T data;
        private Entry next;
        private Entry previous;

        public Entry() {}

        public Entry(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            if (data != null) {
                return data.toString();
            }
            return null;
        }
    }

    public CustomLinkedList() {}

    public CustomLinkedList(Collection<? extends T> c) {
        addAll(c);
    }

    public T getFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return (T) first.data;
    }

    public T getLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        return (T) last.data;
    }

    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        modCount++;
        size--;

        T r = first.data;

        if (first.next != null) {
            first.next.previous = null;
        } else {
            last = null;
        }

        first = first.next;

        return r;
    }

    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        modCount++;
        size--;

        T r = last.data;

        if (last.previous != null) {
            last.previous.next = null;
        } else {
            first = null;
        }

        last = last.previous;

        return r;
    }

    @Override
    public boolean remove(Object o) {
        Entry<T> e = last;

        while (e != null) {
            if (e.data.equals(o)) {
                removeEntry(e);
                return true;
            }
            e = e.next;
        }

        return false;
    }

    @Override
    public T remove() {
        return removeLast();
    }

    public void addFirst(T o) {
        Entry<T> e = new Entry<>(o);

        modCount++;
        if (size == 0) {
            first = last = e;
        } else {
            e.next = first;
            first.previous = e;
            first = e;
        }
        size++;
    }

    public void addLast(T o) {
        Entry<T> e = new Entry<>(o);

        modCount++;
        if (size == 0) {
            first = last = e;
        } else {
            e.previous = last;
            last.next = e;
            last = e;
        }
        size++;
    }

    @Override
    public boolean add(T o) {
        addLast(o);

        return true;
    }

    public void add(int index, T o) {
        checkBoundInclusive(index);

        if (index < size) {
            Entry<T> e = new Entry<>(o);
            Entry<T> after = getEntry(index);

            modCount++;
            e.next = after;
            e.previous = after.previous;

            if (after.previous == null) {
                first = e;
            } else {
                after.previous.next = e;
            }

            after.previous = e;
            size++;
        } else  {
            addLast(o);
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        checkBoundInclusive(index);
        int cSize = c.size();

        if (cSize == 0) {
            return false;
        }

        Iterator<? extends T> iterator = c.iterator();
        Entry<T> before = null;
        Entry<T> after = null;

        if (index != size) {
            after = getEntry(index);
            before = after.previous;
        } else {
            before = last;
        }

        Entry<T> e = new Entry<T>(iterator.next());
        e.previous = before;
        Entry<T> prev = e;
        Entry<T> firstNew = e;

        for (int pos = 1; pos < cSize; pos++) {
            e = new Entry<T>(iterator.next());
            e.previous = prev;
            prev.next = e;
            prev = e;
        }

        modCount++;
        size+= cSize;
        prev.next = after;

        if (after != null) {
            after.previous = e;
        } else {
            last = e;
        }

        if (before != null) {
            before.next = firstNew;
        } else {
            first = firstNew;
        }

        return true;
    }

    public int indexOf(Object o) {
        int index = 0;
        Entry<T> e = first;

        while (e != null) {
            if (e.data.equals(o)) {
                return index;
            }

            index++;
            e = e.next;
        }

        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = size - 1;
        Entry<T> e = last;

        while (e != null) {
            if (e.data.equals(o)) {
                return index;
            }

            index--;
            e = e.previous;
        }

        return -1;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Entry<T> e = first;

        for (int i = 0; i < size; i++) {
            array[i] = e.data;
            e = e.next;
        }

        return array;
    }

    @Override
    public <S> S[] toArray(S[] a) {
        if (a.length < size) {
            a = (S[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else if (a.length > size) {
            a[size] = null;
        }

        Entry<T> e = first;

        for (int i = 0; i < size; i++) {
            a[i] = (S) e.data;
            e = e.next;
        }

        return a;
    }

    public T get(int index) {
        checkBoundExclusive(index);

        return (T) getEntry(index).data;
    }

    public T set(int index, T o) {
        checkBoundExclusive(index);
        Entry<T> e = getEntry(index);
        T old = e.data;
        e.data = o;

        return old;
    }

    @Override
    public boolean contains(Object o) {
        Entry<T> e = first;

        while (e != null) {
            if (e.data.equals(o)) {
                return true;
            }
            e = e.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T poll() {
        if (size == 0) {
            return null;
        }

        return removeFirst();
    }

    @Override
    public boolean offer(T value) {
        return add(value);
    }

    @Override
    public T peek() {
        if (size == 0) {
            return null;
        }

        return getFirst();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T element() {
        return getFirst();
    }

    @Override
    public void clear() {
        if (size > 0) {
            modCount++;
            first = null;
            last = null;
            size = 0;
        }
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
    public boolean removeAll(Collection c) {
        Entry<T> e = first;

        while (e != null) {
            if (c.contains(e.data)) {
                break;
            }
            e = e.next;
        }
        if (e == null) {
            return false;
        }

        modCount++;
        Entry<T> temp;

        while (e != null) {
            if (c.contains(e.data)) {
                temp = e.next;
                removeEntry(e);
                e = temp;
                continue;
            }
            e = e.next;
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection c) {
        Entry<T> e = first;

        while (e != null) {
            if (!c.contains(e.data)) {
                break;
            }
            e = e.next;
        }
        if (e == null) {
            return false;
        }

        modCount++;
        Entry<T> temp;

        while (e != null) {
            if (!c.contains(e.data)) {
                temp = e.next;
                removeEntry(e);
                e = temp;
                continue;
            }
            e = e.next;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListIterator iterator = iterator();

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
    public ListIterator<T> iterator() {
        return listIterator();
    }

    private ListIterator<T> listIterator() {

        return new ListIterator<T>() {
            private int modCount = CustomLinkedList.this.modCount;
            private Entry<T> next = (Entry<T>) getEntry(0);
            private Entry<T> previous = next.previous;
            private Entry<T> lastReturned;
            private int position = 0;

            private void checkMod() {
                if (modCount != CustomLinkedList.this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public boolean hasNext() {
                return (next != null);
            }

            @Override
            public T next() {
                checkMod();
                if (next == null) {
                    throw new NoSuchElementException();
                }
                position++;

                lastReturned = previous = next;
                next = lastReturned.next;

                return lastReturned.data;
            }

            @Override
            public boolean hasPrevious() {
                return (previous != null);
            }

            @Override
            public T previous() {
                checkMod();
                if (previous == null) {
                    throw new NoSuchElementException();
                }
                position--;

                lastReturned = next = previous;
                previous = lastReturned.previous;

                return lastReturned.data;
            }

            @Override
            public int nextIndex() {
                return position;
            }

            @Override
            public int previousIndex() {
                return position - 1;
            }

            @Override
            public void remove() {
                checkMod();

                if (lastReturned == null) {
                    throw new IllegalStateException();
                }

                // Adjust the position to before the removed element, if the element
                // being removed is behind the cursor.
                if (lastReturned == previous) {
                    position--;
                }

                next = lastReturned.next;
                previous = lastReturned.previous;

                removeEntry(lastReturned);
                modCount++;
                lastReturned = null;
            }

            @Override
            public void set(T o) {
                checkMod();
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }

                lastReturned.data = o;
            }

            @Override
            public void add(T o) {
                checkMod();
                modCount++;
                CustomLinkedList.this.modCount++;
                size++;
                position++;

                Entry<T> e = new Entry<T>(o);
                e.previous = previous;
                e.next = next;

                if (previous != null) {
                    previous.next = e;
                } else {
                    first = e;
                }

                if (next != null) {
                    next.previous = e;
                } else {
                    last = e;
                }

                previous = e;
                lastReturned = null;
            }
        };
    }

    //Auxiliary methods

    private Entry getEntry(int index) {
        Entry<T> e;

        if (index < size() / 2) {
            e = first;
            while (index-- > 0) { //iterate from start
                e = e.next;
            }
        } else { //index >= size() / 2
            e = last;
            while (++index < size) { //iterate from end
                e = e.previous;
            }
        }

        return e;
    }

    private void removeEntry(Entry<T> e) {
        modCount++;
        size--;

        if (size == 0) {
            first = last = null;
        } else {
            if (e == first) {
                first = e.next;
                e.next.previous = null;
            } else if (e == last) {
                last = e.previous;
                e.previous.next = null;
            } else {
                e.next.previous = e.previous;
                e.previous.next = e.next;
            }
        }
    }

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