package com.jagex.runescape;

import com.jagex.runescape.collection.Linkable;

final class NodeList {

	private final Linkable head;

	private Linkable current;

	public NodeList() {
		head = new Linkable();
		head.previous = head;
		head.next = head;
	}

	public Linkable peekLast() {
		Linkable node = head.previous;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}

	public Linkable getFirst() {
		Linkable node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Linkable getNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.next;
		return node;
	}

	public void insertHead(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head.next;
		node.previous = head;
		node.next.previous = node;
		node.previous.next = node;
	}

	public void insertTail(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head;
		node.previous = head.previous;
		node.next.previous = node;
		node.previous.next = node;
	}

	public Linkable popHead() {
		Linkable node = head.previous;
		if (node == head) {
			return null;
		} else {
			node.unlink();
			return node;
		}
	}

	public void removeAll() {
		if (head.previous == head)
			return;
		do {
			Linkable node = head.previous;
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
			current = node.previous;
			return node;
		}
	}
}
