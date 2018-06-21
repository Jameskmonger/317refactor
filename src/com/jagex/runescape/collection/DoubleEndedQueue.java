package com.jagex.runescape.collection;

public final class DoubleEndedQueue {

	private final Linkable tail;
	private Linkable current;

	public DoubleEndedQueue() {
		tail = new Linkable();
		tail.next = tail;
		tail.previous = tail;
	}

	public Linkable peekFront() {
		Linkable node = tail.next;

		if (node == tail) {
			current = null;
			return null;
		}
		
		current = node.next;
		return node;
	}

	public Linkable peekBack() {
		Linkable node = tail.previous;
		if (node == tail) {
			current = null;
			return null;
		}
		
		current = node.previous;
		return node;
	}

	public Linkable getPrevious() {
		Linkable node = current;

		if (node == tail) {
			current = null;
			return null;
		}

		current = node.previous;
		return node;
	}

	public void pushBack(Linkable item) {
		if (item.previous != null) {
			item.unlink();
		}

		item.previous = tail.previous;
		item.next = tail;
		item.previous.next = item;
		item.next.previous = item;
	}

	public void pushFront(Linkable item) {
		if (item.previous != null) {
			item.unlink();
		}

		item.previous = tail;
		item.next = tail.next;
		item.previous.next = item;
		item.next.previous = item;
	}

	public Linkable popFront() {
		Linkable next = tail.next;

		if (next == tail) {
			return null;
		}
		
		next.unlink();
		return next;
	}

	public void clear() {
		if (tail.next == tail) {
			return;
		}

		while (true) {
			Linkable next = tail.next;

			if (next == tail) {
				return;
			}

			next.unlink();
		}
	}

	public Linkable getNext() {
		Linkable node = current;

		if (node == tail) {
			current = null;
			return null;
		}

		current = node.next;
		return node;
	}
}
