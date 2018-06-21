package com.jagex.runescape.collection;

// This is FIFO

public final class Deque {

	private final QueueLink head;
	private QueueLink current;

	public Deque() {
		head = new QueueLink();
		head.prevNodeSub = head;
		head.nextNodeSub = head;
	}

	public int getSize() {
		int count = 0;

		for (QueueLink next = head.prevNodeSub; next != head; next = next.prevNodeSub)
			count++;

		return count;
	}

	public void push(QueueLink item) {
		if (item.nextNodeSub != null) {
			item.unlist();
		}

		item.nextNodeSub = head.nextNodeSub;
		item.prevNodeSub = head;
		item.nextNodeSub.prevNodeSub = item;
		item.prevNodeSub.nextNodeSub = item;
	}

	public QueueLink pull() {
		QueueLink l = head.prevNodeSub;

		if (l == head) {
			return null;
		}
		
		l.unlist();		
		return l;
	}

	public QueueLink reverseGetFirst() {
		QueueLink nodeSub = head.prevNodeSub;

		if (nodeSub == head) {
			current = null;
			return null;
		}
		
		current = nodeSub.prevNodeSub;
		return nodeSub;
	}

	public QueueLink reverseGetNext() {
		QueueLink nodeSub = current;
		
		if (nodeSub == head) {
			current = null;
			return null;
		}

		current = nodeSub.prevNodeSub;
		return nodeSub;
	}
}
