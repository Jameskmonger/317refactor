package com.jagex.runescape;

import com.jagex.runescape.sign.signlink;

public final class LinkedList {

	private final QueueLink queueLink;
	private final int size;
	private int available;
	private final Array array;
	private final Deque deque;

	public LinkedList(int length) {
		queueLink = new QueueLink();
		deque = new Deque();
		size = length;
		available = length;
		array = new Array(1024);
	}

	public QueueLink get(long id) {
		QueueLink l = (QueueLink) array.get(id);
		if (l != null) {
			deque.push(l);
		}
		return l;
	}

	public void put(QueueLink l, long id) {
		try {
			if (available == 0) {
				QueueLink l1 = deque.pull();
				l1.unlink();
				l1.unlist();
				if (l1 == queueLink) {
					QueueLink l2 = deque.pull();
					l2.unlink();
					l2.unlist();
				}
			} else {
				available--;
			}
			array.put(l, id);
			deque.push(l);
			return;
		} catch (RuntimeException e) {
			signlink.reporterror("47547, " + l + ", " + id + ", " + (byte) 2 + ", " + e.toString());
		}
		throw new RuntimeException();
	}

	public void unlinkAll() {
		do {
			QueueLink l = deque.pull();
			if (l != null) {
				l.unlink();
				l.unlist();
			} else {
				available = size;
				return;
			}
		} while (true);
	}
}