package com.jagex.runescape.collection;

class LinkableHashMap {

    private final int size;
    private final Linkable[] entries;

    public LinkableHashMap(final int size) {
        this.size = size;
        this.entries = new Linkable[size];

        for (int i = 0; i < size; i++) {
            final Linkable entry = new Linkable();
            entry.next = entry;
            entry.previous = entry;
            this.entries[i] = entry;
        }
    }

    public Linkable get(final long key) {
        final Linkable start = this.entries[(int) (key & this.size - 1)];

        for (Linkable next = start.next; next != start; next = next.next) {
            if (next.id == key) {
                return next;
            }
        }

        return null;
    }

    public void put(final long key, final Linkable item) {
        if (item.previous != null) {
            item.unlink();
        }

        final Linkable current = this.entries[(int) (key & this.size - 1)];
        item.previous = current.previous;
        item.next = current;
        item.previous.next = item;
        item.next.previous = item;
        item.id = key;
    }
}
