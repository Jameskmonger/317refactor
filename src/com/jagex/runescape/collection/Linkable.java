package com.jagex.runescape.collection;

public class Linkable {

    public long id;
    public Linkable next;
    public Linkable previous;

    public final void unlink() {
        if (this.previous == null) {
            return;
        }

        this.previous.next = this.next;
        this.next.previous = this.previous;
        this.next = null;
        this.previous = null;
    }
}
