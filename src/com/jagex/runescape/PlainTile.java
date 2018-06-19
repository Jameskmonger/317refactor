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

final class PlainTile {

	final int colourA;

	final int colourB;
	final int colourD;
	final int colourC;
	final int texture;
	boolean flat;
	final int colourRGB;

	public PlainTile(int colourA, int colourB, int colourC, int colourD, int colourRGB, int texture, boolean flat) {
		this.flat = true;
		this.colourA = colourA;
		this.colourB = colourB;
		this.colourD = colourD;
		this.colourC = colourC;
		this.texture = texture;
		this.colourRGB = colourRGB;
		this.flat = flat;
	}
}
