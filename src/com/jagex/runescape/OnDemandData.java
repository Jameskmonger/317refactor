package com.jagex.runescape;

import com.jagex.runescape.collection.QueueLink;

public final class OnDemandData extends QueueLink {

	int dataType;

	byte buffer[];
	int id;
	boolean incomplete;
	int loopCycle;

	public OnDemandData() {
		incomplete = true;
	}
}
