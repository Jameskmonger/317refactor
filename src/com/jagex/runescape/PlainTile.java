package com.jagex.runescape;

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
