package com.jagex.runescape;

public class Link {

	public long id;
	public Link previous;
	public Link next;

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
