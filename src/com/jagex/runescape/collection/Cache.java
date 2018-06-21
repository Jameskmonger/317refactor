package com.jagex.runescape.collection;

/*
 * A least-recently-used cache
 */
public class Cache {

	private final Cacheable empty;
	private final int size;
	private int available;
	private final LinkedHashMap hashmap;
	private final DoubleEndedQueue retrievedItems;

	public Cache(int length) {
		empty = new Cacheable();
		retrievedItems = new DoubleEndedQueue();
		hashmap = new LinkedHashMap(1024);

		size = length;
		available = length;		
	}

	public Cacheable get(long key) {
		Cacheable item = (Cacheable) hashmap.get(key);

		if (item != null) {
			retrievedItems.push(item);
		}

		return item;
	}

	public void put(Cacheable item, long key) {
		if (available == 0) {
			Cacheable oldest = retrievedItems.pull();
			oldest.unlink();
			oldest.unlinkCacheable();

			if (oldest == empty) {
				Cacheable secondOldest = retrievedItems.pull();
				secondOldest.unlink();
				secondOldest.unlinkCacheable();
			}
		} else {
			available--;
		}

		hashmap.put(key, item);
		retrievedItems.push(item);
		return;
	}

	public void clear() {
		while (true) {
			Cacheable oldest = retrievedItems.pull();

			if (oldest == null) {
				available = size;
				return;
			}

			oldest.unlink();
			oldest.unlinkCacheable();
		}
	}
}