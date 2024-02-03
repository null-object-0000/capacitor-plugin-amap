package site.snewbie.plugins.amap;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MutableList<E> extends ArrayList<E> {
    public MutableList() {
        super();
    }

    public E first() {
        if (super.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        } else {
            return super.get(0);
        }
    }

    public E removeFirst() {
        if (super.isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        } else {
            return super.remove(0);
        }
    }
}