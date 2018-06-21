package com.jagex.runescape.collection;

// This is FIFO

public final class Deque {

	private final Cacheable head;
	private Cacheable current;

	public Deque() {
		head = new Cacheable();
		head.previousCacheable = head;
		head.nextCacheable = head;
	}

	public int getSize() {
		int count = 0;

		for (Cacheable next = head.previousCacheable; next != head; next = next.previousCacheable)
			count++;

		return count;
	}

	public void push(Cacheable item) {
		if (item.nextCacheable != null) {
			item.unlinkCacheable();
		}

		item.nextCacheable = head.nextCacheable;
		item.previousCacheable = head;
		item.nextCacheable.previousCacheable = item;
		item.previousCacheable.nextCacheable = item;
	}

	public Cacheable pull() {
		Cacheable l = head.previousCacheable;

		if (l == head) {
			return null;
		}
		
		l.unlinkCacheable();		
		return l;
	}

	public Cacheable reverseGetFirst() {
		Cacheable nodeSub = head.previousCacheable;

		if (nodeSub == head) {
			current = null;
			return null;
		}
		
		current = nodeSub.previousCacheable;
		return nodeSub;
	}

	public Cacheable reverseGetNext() {
		Cacheable nodeSub = current;
		
		if (nodeSub == head) {
			current = null;
			return null;
		}

		current = nodeSub.previousCacheable;
		return nodeSub;
	}
}
