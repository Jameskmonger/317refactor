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

public final class Skins {

	public final int[] opcodes;

	public final int[][] skinList;

	public Skins(Buffer buffer) {
		int count = buffer.getUnsignedByte();
		opcodes = new int[count];
		skinList = new int[count][];
		for (int opcode = 0; opcode < count; opcode++)
			opcodes[opcode] = buffer.getUnsignedByte();

		for (int skin = 0; skin < count; skin++) {
			int subSkinAmount = buffer.getUnsignedByte();
			skinList[skin] = new int[subSkinAmount];
			for (int subSkin = 0; subSkin < subSkinAmount; subSkin++)
				skinList[skin][subSkin] = buffer.getUnsignedByte();

		}
	}
}
