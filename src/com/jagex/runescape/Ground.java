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

public final class Ground extends Link {

	int z;

	final int x;
	final int y;
	final int anInt1310;
	public PlainTile plainTile;
	public ShapedTile shapedTile;
	public WallObject wallObject;
	public WallDecoration wallDecoration;
	public GroundDecoration groundDecoration;
	public GroundItemTile groundItemTile;
	int entityCount;
	public final InteractiveObject[] interactiveObjects;
	final int[] interactiveObjectsSize;
	int interactiveObjectsSizeOR;
	int logicHeight;
	boolean aBoolean1322;
	boolean aBoolean1323;
	boolean aBoolean1324;
	int anInt1325;
	int anInt1326;
	int anInt1327;
	int anInt1328;
	public Ground tileBelow;

	public Ground(int i, int j, int k) {
		interactiveObjects = new InteractiveObject[5];
		interactiveObjectsSize = new int[5];
		anInt1310 = z = i;
		x = j;
		y = k;
	}
}
