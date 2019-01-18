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

	public Cache(int length) {
        this.empty = new Cacheable();
        this.retrievedItems = new CacheableQueue();
        this.hashmap = new LinkableHashMap(1024);

        this.size = length;
        this.available = length;
	}

	public Cacheable get(long key) {
		Cacheable item = (Cacheable) this.hashmap.get(key);

		if (item != null) {
            this.retrievedItems.push(item);
		}

		return item;
	}

	public void put(Cacheable item, long key) {
		if (this.available == 0) {
			Cacheable oldest = this.retrievedItems.pop();
			oldest.unlink();
			oldest.unlinkCacheable();

			if (oldest == this.empty) {
				Cacheable secondOldest = this.retrievedItems.pop();
				secondOldest.unlink();
				secondOldest.unlinkCacheable();
			}
		} else {
            this.available--;
		}

        this.hashmap.put(key, item);
        this.retrievedItems.push(item);
		return;
	}

	public void clear() {
		while (true) {
			Cacheable oldest = this.retrievedItems.pop();

			if (oldest == null) {
                this.available = this.size;
				return;
			}

			oldest.unlink();
			oldest.unlinkCacheable();
		}
	}
}