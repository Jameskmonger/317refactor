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

final class BZ2Context
{

    BZ2Context()
    {
        anIntArray583 = new int[256];
        anIntArray585 = new int[257];
        inUse = new boolean[256];
        inUse16 = new boolean[16];
        aByteArray591 = new byte[256];
        aByteArray592 = new byte[4096];
        anIntArray593 = new int[16];
        aByteArray594 = new byte[18002];
        selectorMtf = new byte[18002];
        aByteArrayArray596 = new byte[6][258];
        anIntArrayArray597 = new int[6][258];
        anIntArrayArray598 = new int[6][258];
        anIntArrayArray599 = new int[6][258];
        anIntArray600 = new int[6];
    }

  byte aByteArray563[];
    int anInt564;
    int anInt565;
    int anInt566;
    int anInt567;
    byte aByteArray568[];
    int anInt569;
    int anInt570;
    int anInt571;
    int anInt572;
    byte aByte573;
    int anInt574;
    boolean randomised;
    int anInt576;
    int anInt577;
    int blockSize_100k;
    int anInt579;
    int originalPointer;
    int anInt581;
    int anInt582;
    final int[] anIntArray583;
    int anInt584;
    final int[] anIntArray585;
    public static int anIntArray587[];
    int nInUse;
    final boolean[] inUse;
    final boolean[] inUse16;
    final byte[] aByteArray591;
    final byte[] aByteArray592;
    final int[] anIntArray593;
    final byte[] aByteArray594;
    final byte[] selectorMtf;
    final byte[][] aByteArrayArray596;
    final int[][] anIntArrayArray597;
    final int[][] anIntArrayArray598;
    final int[][] anIntArrayArray599;
    final int[] anIntArray600;
    int anInt601;
}
