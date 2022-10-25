package com.jagex.runescape.collection;

public final class DoubleEndedQueue {

    private final Linkable tail;
    private Linkable current;

    public DoubleEndedQueue() {
        this.tail = new Linkable();
        this.tail.next = this.tail;
        this.tail.previous = this.tail;
    }

    public Linkable peekFront() {
        final Linkable node = this.tail.next;

        if (node == this.tail) {
            this.current = null;
            return null;
        }

        this.current = node.next;
        return node;
    }

    public Linkable peekBack() {
        final Linkable node = this.tail.previous;
        if (node == this.tail) {
            this.current = null;
            return null;
        }

        this.current = node.previous;
        return node;
    }

    public Linkable getPrevious() {
        final Linkable node = this.current;

        if (node == this.tail) {
            this.current = null;
            return null;
        }

        this.current = node.previous;
        return node;
    }

    public void pushBack(final Linkable item) {
        if (item.previous != null) {
            item.unlink();
        }

        item.previous = this.tail.previous;
        item.next = this.tail;
        item.previous.next = item;
        item.next.previous = item;
    }

    public void pushFront(final Linkable item) {
        if (item.previous != null) {
            item.unlink();
        }

        item.previous = this.tail;
        item.next = this.tail.next;
        item.previous.next = item;
        item.next.previous = item;
    }

    public Linkable popFront() {
        final Linkable next = this.tail.next;

        if (next == this.tail) {
            return null;
        }

        next.unlink();
        return next;
    }

    public void clear() {
        if (this.tail.next == this.tail) {
            return;
        }

        while (true) {
            final Linkable next = this.tail.next;

            if (next == this.tail) {
                return;
            }

            next.unlink();
        }
    }

    public Linkable getNext() {
        final Linkable node = this.current;

        if (node == this.tail) {
            this.current = null;
            return null;
        }

        this.current = node.next;
        return node;
    }
}
