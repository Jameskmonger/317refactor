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

public final class Skins
{

    public Skins(Stream buffer)
    {
        int count = buffer.getUnsignedByte();
        opcodes = new int[count];
        skinList = new int[count][];
        for(int opcode = 0; opcode < count; opcode++)
            opcodes[opcode] = buffer.getUnsignedByte();

        for(int skin = 0; skin < count; skin++)
        {
            int subSkinAmount = buffer.getUnsignedByte();
            skinList[skin] = new int[subSkinAmount];
            for(int subSkin = 0; subSkin < subSkinAmount; subSkin++)
                skinList[skin][subSkin] = buffer.getUnsignedByte();

        }
    }

    public final int[] opcodes;
    public final int[][] skinList;
}
