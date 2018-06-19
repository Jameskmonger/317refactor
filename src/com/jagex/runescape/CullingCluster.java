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

final class CullingCluster {

	int tileStartX;

	int tileEndX;
	int tileStartY;
	int tileEndY;
	int searchMask;
	int worldStartX;
	int worldEndX;
	int worldStartY;
	int worldEndY;
	int worldEndZ;
	int worldStartZ;
	int tileDistanceEnum;
	int worldDistanceFromCameraStartX;
	int worldDistanceFromCameraEndX;
	int worldDistanceFromCameraStartY;
	int worldDistanceFromCameraEndY;
	int worldDistanceFromCameraStartZ;
	int worldDistanceFromCameraEndZ;

	CullingCluster() {
	}
}
