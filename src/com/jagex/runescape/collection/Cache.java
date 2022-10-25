package com.jagex.runescape.collection;

/*
 * A least-recently-used cache
 */
public class Cache {

    private final Cacheable empty;
    private final int size;
    private int available;
    private final LinkableHashMap hashmap;
    private final CacheableQueue retrievedItems;

    public Cache(final int length) {
        this.empty = new Cacheable();
        this.retrievedItems = new CacheableQueue();
        this.hashmap = new LinkableHashMap(1024);

        this.size = length;
        this.available = length;
    }

    public Cacheable get(final long key) {
        final Cacheable item = (Cacheable) this.hashmap.get(key);

        if (item != null) {
            this.retrievedItems.push(item);
        }

        return item;
    }

    public void put(final Cacheable item, final long key) {
        if (this.available == 0) {
            final Cacheable oldest = this.retrievedItems.pop();
            oldest.unlink();
            oldest.unlinkCacheable();

            if (oldest == this.empty) {
                final Cacheable secondOldest = this.retrievedItems.pop();
                secondOldest.unlink();
                secondOldest.unlinkCacheable();
            }
        } else {
            this.available--;
        }

        this.hashmap.put(key, item);
        this.retrievedItems.push(item);
    }

    public void clear() {
        while (true) {
            final Cacheable oldest = this.retrievedItems.pop();

            if (oldest == null) {
                this.available = this.size;
                return;
            }

            oldest.unlink();
            oldest.unlinkCacheable();
        }
    }
}