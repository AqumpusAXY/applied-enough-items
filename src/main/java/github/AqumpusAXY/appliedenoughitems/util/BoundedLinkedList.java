package github.AqumpusAXY.appliedenoughitems.util;

import java.util.LinkedList;

public class BoundedLinkedList<T> {
    private final LinkedList<T> list;
    private final int maxSize;

    public BoundedLinkedList(int maxSize) {
        this.maxSize = maxSize;
        this.list = new LinkedList<>();
    }

    public void addFirst(T element) {
        list.remove(element);
        list.addFirst(element);
        if (list.size() > maxSize) {
            list.removeLast();
        }
    }

    public void addLast(T element) {
        list.remove(element);
        list.addLast(element);
        if (list.size() > maxSize) {
            list.removeFirst();
        }
    }

    public LinkedList<T> getList() {
        return list;
    }

    public int getMaxSize() {
        return maxSize;
    }
}

