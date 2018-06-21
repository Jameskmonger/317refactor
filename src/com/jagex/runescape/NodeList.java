package com.jagex.runescape;

import com.jagex.runescape.collection.Linkable;

final class NodeList {

	private final Linkable head;

	private Linkable current;

	public NodeList() {
		head = new Linkable();
		head.next = head;
		head.previous = head;
	}

	public Linkable peekLast() {
		Linkable node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Linkable getFirst() {
		Linkable node = head.previous;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}

	public Linkable getNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.previous;
		return node;
	}

	public void insertHead(Linkable node) {
		if (node.previous != null)
			node.unlink();
		node.previous = head.previous;
		node.next = head;
		node.previous.next = node;
		node.next.previous = node;
	}

	public void insertTail(Linkable node) {
		if (node.previous != null)
			node.unlink();
		node.previous = head;
		node.next = head.next;
		node.previous.next = node;
		node.next.previous = node;
	}

	public Linkable popHead() {
		Linkable node = head.next;
		if (node == head) {
			return null;
		} else {
			node.unlink();
			return node;
		}
	}

	public void removeAll() {
		if (head.next == head)
			return;
		do {
			Linkable node = head.next;
			if (node == head)
				return;
			node.unlink();
		} while (true);
	}

	public Linkable reverseGetNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}
}
