package com.jagex.runescape.collection;

public class Linkable {

	public long id;
	public Linkable previous;
	public Linkable next;

	public final void unlink() {
		if (next == null) {
		} else {
			next.previous = previous;
			previous.next = next;
			previous = null;
			next = null;
		}
	}
}
