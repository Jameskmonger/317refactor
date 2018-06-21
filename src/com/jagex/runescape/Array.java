package com.jagex.runescape;

import com.jagex.runescape.sign.signlink;

final class Array {

	private final int length;
	private final Link[] array;

	public Array() {
		int size = 1024;// was parameter
		length = size;
		array = new Link[size];
		for (int n = 0; n < size; n++) {
			Link l = array[n] = new Link();
			l.previous = l;
			l.next = l;
		}
	}

	public Link get(long id) {
		Link l = array[(int) (id & length - 1)];
		for (Link l1 = l.previous; l1 != l; l1 = l1.previous)
			if (l1.id == id)
				return l1;
		return null;
	}

	public void put(Link l, long id) {
		try {
			if (l.next != null)
				l.unlink();
			Link l1 = array[(int) (id & length - 1)];
			l.next = l1.next;
			l.previous = l1;
			l.next.previous = l;
			l.previous.next = l;
			l.id = id;
			return;
		} catch (RuntimeException e) {
			signlink.reporterror("91499, " + l + ", " + id + ", " + (byte) 7 + ", " + e.toString());
		}
		throw new RuntimeException();
	}
}
