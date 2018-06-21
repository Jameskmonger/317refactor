package com.jagex.runescape.collection;

public class Cacheable extends Linkable {

	public Cacheable prevNodeSub;
	public Cacheable nextNodeSub;

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
