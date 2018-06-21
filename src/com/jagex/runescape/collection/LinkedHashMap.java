package com.jagex.runescape.collection;

public class LinkedHashMap {

	private final int size;
	private final Linkable[] entries;

	public LinkedHashMap(int size) {
		this.size = size;
		this.entries = new Linkable[size];
		
		for (int i = 0; i < size; i++) {
			Linkable entry = new Linkable();
			entry.next = entry;
			entry.previous = entry;
			entries[i] = entry;
		}
	}

	public Linkable get(long key) {
		Linkable start = entries[(int) (key & size - 1)];

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
		
		Linkable current = entries[(int) (key & size - 1)];
		item.previous = current.previous;
		item.next = current;
		item.previous.next = item;
		item.next.previous = item;
		item.id = key;
	}
}
