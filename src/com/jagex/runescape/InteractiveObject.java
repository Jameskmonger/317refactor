package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
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

/* 
 * This file was renamed as part of the 317refactor project.
 */

public final class InteractiveObject {

	int z;

	int worldZ;
	int worldX;
	int worldY;
	public Animable renderable;
	public int rotation;
	int tileLeft;
	int tileRight;
	int tileTop;
	int tileBottom;
	int anInt527;
	int anInt528;
	public int uid;
	byte objConf;

	public InteractiveObject() {
	}
}
