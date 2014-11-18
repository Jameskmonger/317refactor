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

public final class VarBit {

	public static void load(Archive archive) {
		Stream stream = new Stream(archive.getFile("varbit.dat"));
		int cacheSize = stream.getUnsignedLEShort();
		if (cache == null)
			cache = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new VarBit();
			cache[j].loadDefinition(stream);
			if (cache[j].aBoolean651)
				Varp.cache[cache[j].configId].aBoolean713 = true;
		}

		if (stream.currentOffset != stream.buffer.length)
			System.out.println("varbit load mismatch");
	}

	public static VarBit cache[];

	public int configId;

	public int leastSignificantBit;
	public int mostSignificantBit;
	private boolean aBoolean651;
	private VarBit() {
		aBoolean651 = false;
	}
	private void loadDefinition(Stream stream) {
		do {
			int j = stream.getUnsignedByte();
			if (j == 0)
				return;
			if (j == 1) {
				configId = stream.getUnsignedLEShort();
				leastSignificantBit = stream.getUnsignedByte();
				mostSignificantBit = stream.getUnsignedByte();
			} else if (j == 10)
				stream.getString();
			else if (j == 2)
				aBoolean651 = true;
			else if (j == 3)
				stream.getInt();
			else if (j == 4)
				stream.getInt();
			else
				System.out.println("Error unrecognised config code: " + j);
		} while (true);
	}
}
