package com.jagex.runescape.collection;

import com.jagex.runescape.collection.Linkable;

final class LinkedHashMap {

	private final int length;
	private final Linkable[] entries;

	public LinkedHashMap(int length) {
		this.length = length;
		this.entries = new Linkable[length];
		
		for (int i = 0; i < length; i++) {
			Linkable entry = new Linkable();
			entry.next = entry;
			entry.previous = entry;
			entries[i] = entry;
		}
	}

	public Linkable get(long key) {
		Linkable start = entries[(int) (key & length - 1)];

		for (Linkable next = start.next; next != start; next = next.next) {
			if (next.id == key) {
				return next;
			}
		}

		return null;
	}

	public void put(long key, Linkable item) {
		if (item.previous != null) {
			item.unlink();
		}
		
		Linkable current = entries[(int) (key & length - 1)];
		item.previous = current.previous;
		item.next = current;
		item.previous.next = item;
		item.next.previous = item;
		item.id = key;
	}
}
