package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

public final class OnDemandData extends NodeSub {

	int dataType;

	byte buffer[];
	int id;
	boolean incomplete;
	int loopCycle;
	public OnDemandData() {
		incomplete = true;
	}
}
