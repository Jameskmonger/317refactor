package com.jagex.runescape.collection;

public class Cache {

	private final QueueLink empty;
	private final int size;
	private int available;
	private final LinkedHashMap hashmap;
	private final Deque deque;

	public Cache(int length) {
		empty = new QueueLink();
		deque = new Deque();
		size = length;
		available = length;
		hashmap = new LinkedHashMap(1024);
	}

	public QueueLink get(long key) {
		QueueLink item = (QueueLink) hashmap.get(key);

		if (item != null) {
			deque.push(item);
		}

		return item;
	}

	public void put(QueueLink item, long key) {
		if (available == 0) {
			QueueLink head = deque.pull();
			head.unlink();
			head.unlist();

			if (head == empty) {
				QueueLink secondHead = deque.pull();
				secondHead.unlink();
				secondHead.unlist();
			}
		} else {
			available--;
		}

		hashmap.put(key, item);
		deque.push(item);
		return;
	}

	public void clear() {
		while (true) {
			QueueLink head = deque.pull();

			if (head == null) {
				available = size;
				return;
			}

			head.unlink();
			head.unlist();
		}
	}
}