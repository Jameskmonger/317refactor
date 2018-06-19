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

final class ModelHeader {

	public byte modelData[];

	public int vertexCount;
	public int triangleCount;
	public int texturedTriangleCount;
	public int vertexDirectionOffset;
	public int dataOffsetX;
	public int dataOffsetY;
	public int dataOffsetZ;
	public int vertexSkinOffset;
	public int triangleDataOffset;
	public int triangleTypeOffset;
	public int colourDataOffset;
	public int texturePointerOffset;
	public int trianglePriorityOffset;
	public int triangleAlphaOffset;
	public int triangleSkinOffset;
	public int texturedTriangleOffset;

	public ModelHeader() {
	}
}
