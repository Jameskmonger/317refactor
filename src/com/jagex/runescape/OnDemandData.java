package com.jagex.runescape;

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
