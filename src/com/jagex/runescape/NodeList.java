package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

/* 
 * This file was renamed as part of the 317refactor project.
 */

final class NodeList {

	private final Link head;

	private Link current;

	public NodeList() {
		head = new Link();
		head.previous = head;
		head.next = head;
	}

	public Link peekLast() {
		Link node = head.previous;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}

	public Link getFirst() {
		Link node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Link getNext() {
		Link node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.next;
		return node;
	}

	public void insertHead(Link node) {
		if (node.next != null)
			node.unlink();
		node.next = head.next;
		node.previous = head;
		node.next.previous = node;
		node.previous.next = node;
	}

	public void insertTail(Link node) {
		if (node.next != null)
			node.unlink();
		node.next = head;
		node.previous = head.previous;
		node.next.previous = node;
		node.previous.next = node;
	}

	public Link popHead() {
		Link node = head.previous;
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
			Link node = head.previous;
			if (node == head)
				return;
			node.unlink();
		} while (true);
	}
	public Link reverseGetNext() {
		Link node = current;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.previous;
			return node;
		}
	}
}
