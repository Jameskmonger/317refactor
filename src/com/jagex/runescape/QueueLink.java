package com.jagex.runescape;

import com.jagex.runescape.collection.Linkable;

public class QueueLink extends Linkable {

	public QueueLink prevNodeSub;

	QueueLink nextNodeSub;

	public static int anInt1305;

	public QueueLink() {
	}

	public final void unlist() {
		if (nextNodeSub == null) {
		} else {
			nextNodeSub.prevNodeSub = prevNodeSub;
			prevNodeSub.nextNodeSub = nextNodeSub;
			prevNodeSub = null;
			nextNodeSub = null;
		}
	}
}
