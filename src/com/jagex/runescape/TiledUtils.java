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

final class TiledUtils {

	public static int getRotatedLandscapeChunkX(int rotation, int objectSizeY, int x, int y, int objectSizeX) {
		rotation &= 3;
		if (rotation == 0)
			return x;
		if (rotation == 1)
			return y;
		if (rotation == 2)
			return 7 - x - (objectSizeX - 1);
		else
			return 7 - y - (objectSizeY - 1);
	}

	public static int getRotatedLandscapeChunkY(int y, int objectSizeY, int rotation, int objectSizeX, int x) {
		rotation &= 3;
		if (rotation == 0)
			return y;
		if (rotation == 1)
			return 7 - x - (objectSizeX - 1);
		if (rotation == 2)
			return 7 - y - (objectSizeY - 1);
		else
			return x;
	}

	public static int getRotatedMapChunkX(int rotation, int y, int x) {
		rotation &= 3;
		if (rotation == 0)
			return x;
		if (rotation == 1)
			return y;
		if (rotation == 2)
			return 7 - x;
		else
			return 7 - y;
	}

	public static int getRotatedMapChunkY(int y, int rotation, int x) {
		rotation &= 3;
		if (rotation == 0)
			return y;
		if (rotation == 1)
			return 7 - x;
		if (rotation == 2)
			return 7 - y;
		else
			return x;
	}

}
