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

/**
 * 
 * Represents a single archive within a cache.
 * 
 */
final class Archive {

	private final byte[] outputData;

	private final int fileCount;

	private final int[] hashes;
	private final int[] decompressedSizes;
	private final int[] compressedSizes;
	private final int[] initialOffsets;
	private final boolean decompressed;

	public Archive(byte data[]) {
		Buffer buffer = new Buffer(data);
		int compressedLength = buffer.get3Bytes();
		int decompressedLength = buffer.get3Bytes();

		if (decompressedLength != compressedLength) {
			byte output[] = new byte[compressedLength];
			BZ2Decompressor.decompress(output, compressedLength, data, decompressedLength, 6);
			outputData = output;
			buffer = new Buffer(outputData);
			this.decompressed = true;
		} else {
			outputData = data;
			this.decompressed = false;
		}

		fileCount = buffer.getUnsignedLEShort();
		hashes = new int[fileCount];
		decompressedSizes = new int[fileCount];
		compressedSizes = new int[fileCount];
		initialOffsets = new int[fileCount];
		int offset = buffer.position + fileCount * 10;

		for (int index = 0; index < fileCount; index++) {
			hashes[index] = buffer.getInt();
			decompressedSizes[index] = buffer.get3Bytes();
			compressedSizes[index] = buffer.get3Bytes();
			initialOffsets[index] = offset;
			offset += compressedSizes[index];
		}
	}

	public byte[] decompressFile(String name) {
		int hash = 0;
		name = name.toUpperCase();
		for (int c = 0; c < name.length(); c++)
			hash = (hash * 61 + name.charAt(c)) - 32;

		for (int file = 0; file < fileCount; file++)
			if (hashes[file] == hash) {
				byte[] output = new byte[decompressedSizes[file]];
				if (!decompressed) {
					BZ2Decompressor.decompress(output, decompressedSizes[file], outputData, compressedSizes[file],
							initialOffsets[file]);
				} else {
					System.arraycopy(outputData, initialOffsets[file], output, 0, decompressedSizes[file]);
				}
				return output;
			}
		return null;
	}
}