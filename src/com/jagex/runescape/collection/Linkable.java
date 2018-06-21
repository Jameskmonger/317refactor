package com.jagex.runescape.collection;

public class Linkable {

	public long id;
	public Linkable next;
	public Linkable previous;

	public final void unlink() {
		if (previous == null) {
			return;
		}

		previous.next = next;
		next.previous = previous;
		next = null;
		previous = null;
	}
}
