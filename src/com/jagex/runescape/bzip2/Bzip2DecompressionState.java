package com.jagex.runescape.bzip2;

/*
 * From Major at Rune-Server
 * https://www.rune-server.ee/runescape-development/rs2-client/snippets/430099-bzip2-classes-refactor.html
 */

/**
 * A Java implementation of the DState struct (structure holding all the
 * decompression-side stuff).
 *
 * @see "http://svn.apache.org/repos/asf/labs/axmake/trunk/src/libuc++/srclib/bzip2/bzlib_private.h"
 */
class BZip2DecompressionState {

    // Class32

    /* Constants */
    final int bzMaxAlphaSize = 258;
    final int bzMaxCodeLen = 23;
    final int bzRunB = 1;
    final int bzNGroups = 6;
    final int bzGSize = 50;
    final int bzNIters = 4;
    final int bzMaxSelectors = 18002; // (2 + (900000 / BZ_G_SIZE))

    /*-- Constants for the fast MTF decoder. --*/
    final int mtfaSize = 4096;
    final int mtflSize = 16;

    /* for undoing the Burrows-Wheeler transform (FAST) */
    public static int[] tt;

    /* map of bytes used in block */
    int nInUse;
    final boolean[] inUse;
    final boolean[] inUse16;
    final byte[] seqToUnseq;

    byte[] stream;
    byte[] buf; // out

    /* for doing the final run-length decoding */
    int stateOutLen;
    boolean blockRandomised;
    byte stateOutCh;

    /* the buffer for bit stream reading */
    int bsBuff;
    int bsLive;

    /* misc administratium */
    int blockSize100k;
    int currBlockNumber;

    /* for undoing the Burrows-Wheeler transform */
    int origPtr;
    int tPos;
    int k0;
    int nBlockUsed;
    final int[] unzftab;
    final int[] cftab;

    /* for decoding the MTF values */
    final byte[] mtfa;
    final int[] mtfbase;
    final byte[] selector;
    final byte[] selectorMtf;
    final byte[][] len;

    final int[] minLens;
    final int[][] limit;
    final int[][] base;
    final int[][] perm;

    int nBlock;
    int nextIn;
    int availableIn;
    int totalInLo32;
    int totalInHi32;
    int nextOut;
    int availOut;
    int totalOutLo32;
    int totalOutHigh32;

    BZip2DecompressionState() {
        this.unzftab = new int[256];
        this.cftab = new int[257];
        this.inUse = new boolean[256];
        this.inUse16 = new boolean[16];
        this.seqToUnseq = new byte[256];
        this.mtfa = new byte[4096];
        this.mtfbase = new int[16];
        this.selector = new byte[18002];
        this.selectorMtf = new byte[18002];
        this.len = new byte[6][258];
        this.limit = new int[6][258];
        this.base = new int[6][258];
        this.perm = new int[6][258];
        this.minLens = new int[6];
    }

}