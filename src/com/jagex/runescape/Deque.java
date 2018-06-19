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
