package com.jagex.runescape.collection;

public class Cache {

	private final Cacheable empty;
	private final int size;
	private int available;
	private final LinkedHashMap hashmap;
	private final Deque deque;

	public Cache(int length) {
		empty = new Cacheable();
		deque = new Deque();
		size = length;
		available = length;
		hashmap = new LinkedHashMap(1024);
	}

	public Cacheable get(long key) {
		Cacheable item = (Cacheable) hashmap.get(key);

		if (item != null) {
			deque.push(item);
		}

		return item;
	}

	public void put(Cacheable item, long key) {
		if (available == 0) {
			Cacheable head = deque.pull();
			head.unlink();
			head.unlinkCacheable();

			if (head == empty) {
				Cacheable secondHead = deque.pull();
				secondHead.unlink();
				secondHead.unlinkCacheable();
			}
		} else {
			available--;
		}

		hashmap.put(key, item);
		deque.push(item);
		return;
	}

	public void clear() {
		while (true) {
			Cacheable head = deque.pull();

			if (head == null) {
				available = size;
				return;
			}

			head.unlink();
			head.unlinkCacheable();
		}
	}
}