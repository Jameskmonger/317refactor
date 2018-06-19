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
		array = new Array();
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