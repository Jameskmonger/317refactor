package com.jagex.runescape;

final class Deque {

	private final QueueLink head;
	private QueueLink current;

	public Deque() {
		head = new QueueLink();
		head.prevNodeSub = head;
		head.nextNodeSub = head;
	}

	public int getNodeCount() {
		int count = 0;
		for (QueueLink l = head.prevNodeSub; l != head; l = l.prevNodeSub)
			count++;

		return count;
	}

	public void push(QueueLink l) {
		if (l.nextNodeSub != null)
			l.unlist();
		l.nextNodeSub = head.nextNodeSub;
		l.prevNodeSub = head;
		l.nextNodeSub.prevNodeSub = l;
		l.prevNodeSub.nextNodeSub = l;
	}

	public QueueLink pull() {
		QueueLink l = head.prevNodeSub;
		if (l == head) {
			return null;
		} else {
			l.unlist();
			return l;
		}
	}

	public QueueLink reverseGetFirst() {
		QueueLink nodeSub = head.prevNodeSub;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.prevNodeSub;
			return nodeSub;
		}
	}

	public QueueLink reverseGetNext() {
		QueueLink nodeSub = current;
		if (nodeSub == head) {
			current = null;
			return null;
		} else {
			current = nodeSub.prevNodeSub;
			return nodeSub;
		}
	}
}
