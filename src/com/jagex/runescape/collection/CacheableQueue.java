package com.jagex.runescape.collection;

public final class CacheableQueue {

	private final Cacheable head;
	private Cacheable current;

	public CacheableQueue() {
		head = new Cacheable();
		head.nextCacheable = head;
		head.previousCacheable = head;
	}

	public int getSize() {
		int count = 0;

		for (Cacheable next = head.nextCacheable; next != head; next = next.nextCacheable) {
            count++;
        }

		return count;
	}

	public void push(Cacheable item) {
		if (item.previousCacheable != null) {
			item.unlinkCacheable();
		}

		item.previousCacheable = head.previousCacheable;
		item.nextCacheable = head;
		item.previousCacheable.nextCacheable = item;
		item.nextCacheable.previousCacheable = item;
	}

	public Cacheable pop() {
		Cacheable next = head.nextCacheable;

		if (next == head) {
			return null;
		}
		
		next.unlinkCacheable();		
		return next;
	}

	public Cacheable peek() {
		Cacheable next = head.nextCacheable;

		if (next == head) {
			current = null;
			return null;
		}
		
		current = next.nextCacheable;
		return next;
	}

	public Cacheable getNext() {
		Cacheable current = this.current;
		
		if (current == head) {
			this.current = null;
			return null;
		}

		this.current = current.nextCacheable;
		return current;
	}
}
