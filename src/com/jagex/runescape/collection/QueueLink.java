package com.jagex.runescape.collection;

public class QueueLink extends Linkable {

	public QueueLink prevNodeSub;
	public QueueLink nextNodeSub;

	public final void unlist() {
		if (nextNodeSub == null) {
			return;
		}
		
		nextNodeSub.prevNodeSub = prevNodeSub;
		prevNodeSub.nextNodeSub = nextNodeSub;
		prevNodeSub = null;
		nextNodeSub = null;
	}
}
