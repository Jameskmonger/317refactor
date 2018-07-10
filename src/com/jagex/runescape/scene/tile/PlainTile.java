package com.jagex.runescape.scene.tile;

public final class PlainTile {

	public final int colourA;

	public final int colourB;
	public final int colourD;
	public final int colourC;
	public final int texture;
	public boolean flat;
	public final int colourRGB;

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
