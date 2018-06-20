package com.jagex.runescape.bzip2;

/*
 * From Major at Rune-Server
 * https://www.rune-server.ee/runescape-development/rs2-client/snippets/430099-bzip2-classes-refactor.html
 */

/**
 * A Java implementation of the DState struct (structure holding all the
 * decompression-side stuff).
 * 
 * @see http 
 *      ://svn.apache.org/repos/asf/labs/axmake/trunk/src/libuc++/srclib/bzip2
 *      /bzlib_private.h
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
	boolean[] inUse;
	boolean[] inUse16;
	byte[] seqToUnseq;

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
	int[] unzftab;
	int[] cftab;

	/* for decoding the MTF values */
	byte[] mtfa;
	int[] mtfbase;
	byte[] selector;
	byte[] selectorMtf;
	byte[][] len;

	int[] minLens;
	int[][] limit;
	int[][] base;
	int[][] perm;

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
		unzftab = new int[256];
		cftab = new int[257];
		inUse = new boolean[256];
		inUse16 = new boolean[16];
		seqToUnseq = new byte[256];
		mtfa = new byte[4096];
		mtfbase = new int[16];
		selector = new byte[18002];
		selectorMtf = new byte[18002];
		len = new byte[6][258];
		limit = new int[6][258];
		base = new int[6][258];
		perm = new int[6][258];
		minLens = new int[6];
	}

}