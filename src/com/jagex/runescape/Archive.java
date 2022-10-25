package com.jagex.runescape;

import com.jagex.runescape.bzip2.Bzip2Decompressor;

/**
 * Represents a single archive within a cache.
 */
public final class Archive {

    private final byte[] outputData;

    private final int fileCount;

    private final int[] hashes;
    private final int[] decompressedSizes;
    private final int[] compressedSizes;
    private final int[] initialOffsets;
    private final boolean decompressed;

    public Archive(final byte[] data) {
        Buffer buffer = new Buffer(data);
        final int compressedLength = buffer.get3Bytes();
        final int decompressedLength = buffer.get3Bytes();

        if (decompressedLength != compressedLength) {
            final byte[] output = new byte[compressedLength];
            Bzip2Decompressor.decompress(output, compressedLength, data, decompressedLength, 6);
            this.outputData = output;
            buffer = new Buffer(this.outputData);
            this.decompressed = true;
        } else {
            this.outputData = data;
            this.decompressed = false;
        }

        this.fileCount = buffer.getUnsignedLEShort();
        this.hashes = new int[this.fileCount];
        this.decompressedSizes = new int[this.fileCount];
        this.compressedSizes = new int[this.fileCount];
        this.initialOffsets = new int[this.fileCount];
        int offset = buffer.position + this.fileCount * 10;

        for (int index = 0; index < this.fileCount; index++) {
            this.hashes[index] = buffer.getInt();
            this.decompressedSizes[index] = buffer.get3Bytes();
            this.compressedSizes[index] = buffer.get3Bytes();
            this.initialOffsets[index] = offset;
            offset += this.compressedSizes[index];
        }
    }

    public byte[] decompressFile(String name) {
        int hash = 0;
        name = name.toUpperCase();
        for (int c = 0; c < name.length(); c++) {
            hash = (hash * 61 + name.charAt(c)) - 32;
        }

        for (int file = 0; file < this.fileCount; file++) {
            if (this.hashes[file] == hash) {
                final byte[] output = new byte[this.decompressedSizes[file]];
                if (!this.decompressed) {
                    Bzip2Decompressor.decompress(output, this.decompressedSizes[file], this.outputData, this.compressedSizes[file],
                        this.initialOffsets[file]);
                } else {
                    System.arraycopy(this.outputData, this.initialOffsets[file], output, 0, this.decompressedSizes[file]);
                }
                return output;
            }
        }
        return null;
    }
}