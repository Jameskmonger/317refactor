package com.jagex.runescape.collection;

// This is FIFO

public final class Deque {

	private final Cacheable head;
	private Cacheable current;

	public Deque() {
		head = new Cacheable();
		head.prevNodeSub = head;
		head.nextNodeSub = head;
	}

	public int getSize() {
		int count = 0;

		for (Cacheable next = head.prevNodeSub; next != head; next = next.prevNodeSub)
			count++;

		return count;
	}

	public void push(Cacheable item) {
		if (item.nextNodeSub != null) {
			item.unlist();
		}

		item.nextNodeSub = head.nextNodeSub;
		item.prevNodeSub = head;
		item.nextNodeSub.prevNodeSub = item;
		item.prevNodeSub.nextNodeSub = item;
	}

	public Cacheable pull() {
		Cacheable l = head.prevNodeSub;

		if (l == head) {
			return null;
		}
		
		l.unlist();		
		return l;
	}

	public Cacheable reverseGetFirst() {
		Cacheable nodeSub = head.prevNodeSub;

		if (nodeSub == head) {
			current = null;
			return null;
		}
		
		current = nodeSub.prevNodeSub;
		return nodeSub;
	}

	public Cacheable reverseGetNext() {
		Cacheable nodeSub = current;
		
		if (nodeSub == head) {
			current = null;
			return null;
		}

		current = nodeSub.prevNodeSub;
		return nodeSub;
	}
}
