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

final class Archive {

    public Archive(byte dataBuffer[])
    {
        Stream stream = new Stream(dataBuffer);
        int uncompressed = stream.get24BitInt();
        int compressed = stream.get24BitInt();
        if(compressed != uncompressed)
        {
            byte data[] = new byte[uncompressed];
            BZ2Decompressor.decompress(data, uncompressed, dataBuffer, compressed, 6);
            archiveDataBuffer = data;
            stream = new Stream(archiveDataBuffer);
            this.compressed = true;
        } else
        {
            archiveDataBuffer = dataBuffer;
            this.compressed = false;
        }
        dataSize = stream.getUnsignedLEShort();
        nameHashes = new int[dataSize];
        uncompressedSizes = new int[dataSize];
        compressedSizes = new int[dataSize];
        startOffsets = new int[dataSize];
        int offset = stream.currentOffset + dataSize * 10;
        for(int index = 0; index < dataSize; index++)
        {
            nameHashes[index] = stream.getInt();
            uncompressedSizes[index] = stream.get24BitInt();
            compressedSizes[index] = stream.get24BitInt();
            startOffsets[index] = offset;
            offset += compressedSizes[index];
        }
    }

    public byte[] getFile(String name)
    {
        byte dataBuffer[] = null; //was a parameter
        int hash = 0;
        name = name.toUpperCase();
        for(int c = 0; c < name.length(); c++)
            hash = (hash * 61 + name.charAt(c)) - 32;

        for(int i = 0; i < dataSize; i++)
            if(nameHashes[i] == hash)
            {
                if(dataBuffer == null)
                    dataBuffer = new byte[uncompressedSizes[i]];
                if(!compressed)
                {
                    BZ2Decompressor.decompress(dataBuffer, uncompressedSizes[i], archiveDataBuffer, compressedSizes[i], startOffsets[i]);
                } else
                {
                    System.arraycopy(archiveDataBuffer, startOffsets[i], dataBuffer, 0, uncompressedSizes[i]);

                }
                return dataBuffer;
            }

        return null;
    }

    private final byte[] archiveDataBuffer;
    private final int dataSize;
    private final int[] nameHashes;
    private final int[] uncompressedSizes;
    private final int[] compressedSizes;
    private final int[] startOffsets;
    private final boolean compressed;
}
