package com.jagex.runescape.collection;

public final class CacheableQueue {

    private final Cacheable head;
    private Cacheable current;

    public CacheableQueue() {
        this.head = new Cacheable();
        this.head.nextCacheable = this.head;
        this.head.previousCacheable = this.head;
    }

    public int getSize() {
        int count = 0;

        for (Cacheable next = this.head.nextCacheable; next != this.head; next = next.nextCacheable) {
            count++;
        }

        return count;
    }

    public void push(final Cacheable item) {
        if (item.previousCacheable != null) {
            item.unlinkCacheable();
        }

        item.previousCacheable = this.head.previousCacheable;
        item.nextCacheable = this.head;
        item.previousCacheable.nextCacheable = item;
        item.nextCacheable.previousCacheable = item;
    }

    public Cacheable pop() {
        final Cacheable next = this.head.nextCacheable;

        if (next == this.head) {
            return null;
        }

        next.unlinkCacheable();
        return next;
    }

    public Cacheable peek() {
        final Cacheable next = this.head.nextCacheable;

        if (next == this.head) {
            this.current = null;
            return null;
        }

        this.current = next.nextCacheable;
        return next;
    }

    public Cacheable getNext() {
        final Cacheable current = this.current;

        if (current == this.head) {
            this.current = null;
            return null;
        }

        this.current = current.nextCacheable;
        return current;
    }
}
