package com.jagex.runescape.bzip2;

/*
 * From Major at Rune-Server
 * https://www.rune-server.ee/runescape-development/rs2-client/snippets/430099-bzip2-classes-refactor.html
 */

/*
 * http://svn.apache.org/repos/asf/labs/axmake/trunk/src/libuc++/srclib/bzip2/
 */
public final class Bzip2Decompressor {

    private static final BZip2DecompressionState state = new BZip2DecompressionState();

    /*
     * http://svn.apache.org/repos/asf/labs/axmake/trunk/src/libuc++/srclib/bzip2
     * /huffman.c
     */
    private static void createDecodeTables(final int[] limit, final int[] base, final int[] perm,
                                           final byte[] len, final int minLen, final int maxLen, final int alphaSize) {
        int pp = 0;
        for (int i = minLen; i <= maxLen; i++) {
            for (int j = 0; j < alphaSize; j++) {
                if (len[j] == i) {
                    perm[pp] = j;
                    pp++;
                }
            }

        }

        for (int i = 0; i < 23; i++) {
            base[i] = 0;
        }

        for (int i = 0; i < alphaSize; i++) {
            base[len[i] + 1]++;
        }

        for (int i = 1; i < 23; i++) {
            base[i] += base[i - 1];
        }

        for (int i = 0; i < 23; i++) {
            limit[i] = 0;
        }

        int vec = 0;
        for (int i = minLen; i <= maxLen; i++) {
            vec += base[i + 1] - base[i];
            limit[i] = vec - 1;
            vec <<= 1;
        }

        for (int i = minLen + 1; i <= maxLen; i++) {
            base[i] = (limit[i - 1] + 1 << 1) - base[i];
        }
    }

    public static int decompress(final byte[] output, int retVal, final byte[] bzStream,
                                 final int maxLen, final int minLen) {
        synchronized (state) {
            state.stream = bzStream; // input
            state.nextIn = minLen;
            state.buf = output;
            state.nextOut = 0;
            state.availableIn = maxLen;
            state.availOut = retVal;
            state.bsLive = 0;
            state.bsBuff = 0;
            state.totalInLo32 = 0;
            state.totalInHi32 = 0;
            state.totalOutLo32 = 0;
            state.totalOutHigh32 = 0;
            state.currBlockNumber = 0;
            decompress(state);
            retVal -= state.availOut;
            return retVal;
        }
    }

    private static void decompress(final BZip2DecompressionState block) {
        int gMinLen = 0;
        int[] gLimit = null;
        int[] gBase = null;
        int[] gPerm = null;
        block.blockSize100k = 1;
        if (BZip2DecompressionState.tt == null) {
            BZip2DecompressionState.tt = new int[block.blockSize100k * 0x186a0]; // 100000
        }
        boolean flag19 = true;
        while (flag19) {
            byte uc = getUChar(block);
            if (uc == 23) {
                return;
            }
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getUChar(block);
            block.currBlockNumber++;
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getUChar(block);
            uc = getBit(block);
            block.blockRandomised = uc != 0;
            if (block.blockRandomised) {
                System.out.println("PANIC! RANDOMISED BLOCK!");
            }
            block.origPtr = 0;
            uc = getUChar(block);
            block.origPtr = block.origPtr << 8 | uc & 0xff;
            uc = getUChar(block);
            block.origPtr = block.origPtr << 8 | uc & 0xff;
            uc = getUChar(block);
            block.origPtr = block.origPtr << 8 | uc & 0xff;

            /*--- Receive the mapping table ---*/

            for (int i = 0; i < 16; i++) {
                final byte bit = getBit(block);
                block.inUse16[i] = bit == 1;
            }

            for (int i = 0; i < 256; i++) {
                block.inUse[i] = false;
            }

            for (int i = 0; i < 16; i++) {
                if (block.inUse16[i]) {
                    for (int j = 0; j < 16; j++) {
                        final byte bit = getBit(block);
                        if (bit == 1) {
                            block.inUse[i * 16 + j] = true;
                        }
                    }

                }
            }

            makeMaps(block);
            final int alphaSize = block.nInUse + 2;
            /*
             * number of different Huffman tables in use
             */
            final int nGroups = getBits(3, block);
            /*
             * number of times that the Huffman tables are swapped (each 50
             * bytes)
             */
            final int nSelectors = getBits(15, block);

            /*--- Now the selectors ---*/
            for (int i = 0; i < nSelectors; i++) {
                int count = 0;
                do {
                    final byte terminator = getBit(block);
                    if (terminator == 0) {
                        break;
                    }
                    count++;
                } while (true);
                /*
                 * zero-terminated (see above) bit runs (0..62) of MTF'ed
                 * Huffman table (*selectors_used)
                 */
                block.selectorMtf[i] = (byte) count;
            }

            /*--- Undo the MTF values for the selectors. ---*/

            final byte[] pos = new byte[6];
            for (byte v = 0; v < nGroups; v++) {
                pos[v] = v;
            }

            for (int i = 0; i < nSelectors; i++) {
                byte v = block.selectorMtf[i];
                final byte tmp = pos[v];
                for (; v > 0; v--) {
                    pos[v] = pos[v - 1];
                }

                pos[0] = tmp;
                block.selector[i] = tmp;
            }

            /*--- Now the coding tables ---*/

            for (int t = 0; t < nGroups; t++) {
                int curr = getBits(5, block);
                for (int i = 0; i < alphaSize; i++) {
                    do {
                        byte bit = getBit(block);
                        if (bit == 0) {
                            break;
                        }
                        bit = getBit(block);
                        if (bit == 0) {
                            curr++;
                        } else {
                            curr--;
                        }
                    } while (true);
                    block.len[t][i] = (byte) curr;
                }
            }

            /*--- Create the Huffman decoding tables ---*/

            for (int t = 0; t < nGroups; t++) {
                byte minLen = 32;
                int maxLen = 0;
                for (int i = 0; i < alphaSize; i++) {
                    if (block.len[t][i] > maxLen) {
                        maxLen = block.len[t][i];
                    }
                    if (block.len[t][i] < minLen) {
                        minLen = block.len[t][i];
                    }
                }

                createDecodeTables(block.limit[t], block.base[t],
                    block.perm[t], block.len[t], minLen, maxLen, alphaSize);
                block.minLens[t] = minLen;
            }

            /*--- Now the MTF values ---*/

            final int eob = block.nInUse + 1; // End of block?
            int groupNo = -1;
            int groupPos = 0;
            for (int i = 0; i <= 255; i++) {
                block.unzftab[i] = 0;
            }
            /*-- MTF init --*/

            int kk = 4095;
            for (int ii = 15; ii >= 0; ii--) {
                for (int jj = 15; jj >= 0; jj--) {
                    block.mtfa[kk] = (byte) (ii * 16 + jj);
                    kk--;
                }

                block.mtfbase[ii] = kk + 1;
            }

            /*-- end MTF init --*/

            int nblock = 0;

            // start get MTF val
            if (groupPos == 0) {
                groupNo++;
                groupPos = 50;
                final byte gSel = block.selector[groupNo];
                gMinLen = block.minLens[gSel];
                gLimit = block.limit[gSel];
                gPerm = block.perm[gSel];
                gBase = block.base[gSel];
            }
            groupPos--;
            int zn = gMinLen;
            int zvec;
            byte zj;
            for (zvec = getBits(zn, block); zvec > gLimit[zn]; zvec = zvec << 1
                | zj) {
                zn++;
                zj = getBit(block);
            }
            for (int nextSym = gPerm[zvec - gBase[zn]]; nextSym != eob; ) {

                // end get mtf val

                if (nextSym == 0 || nextSym == 1) {
                    int es = -1;
                    int n = 1;
                    do {
                        if (nextSym == 0) {
                            es += n;
                        } else if (nextSym == 1) {
                            es += 2 * n;
                        }
                        n *= 2;
                        if (groupPos == 0) {
                            groupNo++;
                            groupPos = 50;
                            final byte gSel = block.selector[groupNo];
                            gMinLen = block.minLens[gSel];
                            gLimit = block.limit[gSel];
                            gPerm = block.perm[gSel];
                            gBase = block.base[gSel];
                        }
                        groupPos--;
                        int zn_ = gMinLen;
                        int zvec_;
                        byte byte10;
                        for (zvec_ = getBits(zn_, block); zvec_ > gLimit[zn_]; zvec_ = zvec_ << 1
                            | byte10) {
                            zn_++;
                            byte10 = getBit(block);
                        }

                        nextSym = gPerm[zvec_ - gBase[zn_]];
                    } while (nextSym == 0 || nextSym == 1);
                    es++;
                    final byte uc_ = block.seqToUnseq[block.mtfa[block.mtfbase[0]] & 0xff];
                    block.unzftab[uc_ & 0xff] += es;
                    for (; es > 0; es--) {
                        BZip2DecompressionState.tt[nblock] = uc_ & 0xff;
                        nblock++;
                    }

                } else {
                    int nn = nextSym - 1;
                    final byte uc_;
                    /* avoid general-case expense */
                    if (nn < 16) {
                        final int pp = block.mtfbase[0];
                        uc_ = block.mtfa[pp + nn];
                        for (; nn > 3; nn -= 4) {
                            final int z = pp + nn;
                            block.mtfa[z] = block.mtfa[z - 1];
                            block.mtfa[z - 1] = block.mtfa[z - 2];
                            block.mtfa[z - 2] = block.mtfa[z - 3];
                            block.mtfa[z - 3] = block.mtfa[z - 4];
                        }

                        for (; nn > 0; nn--) {
                            block.mtfa[pp + nn] = block.mtfa[pp + nn - 1];
                        }

                        block.mtfa[pp] = uc_;
                    } else {
                        /* general case */
                        int lno = nn / 16; // 16 is the MTFL size
                        final int off = nn % 16;
                        int pp = block.mtfbase[lno] + off;
                        uc_ = block.mtfa[pp];
                        for (; pp > block.mtfbase[lno]; pp--) {
                            block.mtfa[pp] = block.mtfa[pp - 1];
                        }

                        block.mtfbase[lno]++;
                        for (; lno > 0; lno--) {
                            block.mtfbase[lno]--;
                            block.mtfa[block.mtfbase[lno]] = block.mtfa[block.mtfbase[lno - 1] + 16 - 1];
                        }

                        block.mtfbase[0]--;
                        block.mtfa[block.mtfbase[0]] = uc_;
                        if (block.mtfbase[0] == 0) {
                            int kk_ = 4095;
                            for (int ii = 15; ii >= 0; ii--) {
                                for (int jj = 15; jj >= 0; jj--) {
                                    block.mtfa[kk_] = block.mtfa[block.mtfbase[ii]
                                        + jj];
                                    kk_--;
                                }

                                block.mtfbase[ii] = kk_ + 1;
                            }

                        }
                    }

                    block.unzftab[block.seqToUnseq[uc_ & 0xff] & 0xff]++;
                    BZip2DecompressionState.tt[nblock] = block.seqToUnseq[uc_ & 0xff] & 0xff;
                    nblock++;
                    if (groupPos == 0) {
                        groupNo++;
                        groupPos = 50;
                        final byte byte14 = block.selector[groupNo];
                        gMinLen = block.minLens[byte14];
                        gLimit = block.limit[byte14];
                        gPerm = block.perm[byte14];
                        gBase = block.base[byte14];
                    }
                    groupPos--;
                    int zn_ = gMinLen;
                    int zvec_;
                    byte byte11;
                    for (zvec_ = getBits(zn_, block); zvec_ > gLimit[zn_]; zvec_ = zvec_ << 1
                        | byte11) {
                        zn_++;
                        byte11 = getBit(block);
                    }

                    nextSym = gPerm[zvec_ - gBase[zn_]];
                }
            }

            /*
             * Now we know what nblock is, we can do a better sanity check on
             * s->origPtr.
             */
            block.stateOutLen = 0;
            block.stateOutCh = 0;
            /*-- Set up cftab to facilitate generation of T^(-1) --*/
            block.cftab[0] = 0;
            System.arraycopy(block.unzftab, 0, block.cftab, 1, 256);

            for (int i = 1; i <= 256; i++) {
                block.cftab[i] += block.cftab[i - 1];
            }

            /*-- compute the T^(-1) vector --*/
            for (int i = 0; i < nblock; i++) {
                final byte uc_ = (byte) (BZip2DecompressionState.tt[i] & 0xff);
                BZip2DecompressionState.tt[block.cftab[uc_ & 0xff]] |= i << 8;
                block.cftab[uc_ & 0xff]++;
            }

            block.tPos = BZip2DecompressionState.tt[block.origPtr] >> 8;
            block.nBlockUsed = 0;
            block.tPos = BZip2DecompressionState.tt[block.tPos];
            block.k0 = (byte) (block.tPos & 0xff);
            block.tPos >>= 8;
            block.nBlockUsed++;
            block.nBlock = nblock;
            method226(block);
            flag19 = block.nBlockUsed == block.nBlock + 1 && block.stateOutLen == 0;
        }
    }

    private static byte getBit(final BZip2DecompressionState block) {
        return (byte) getBits(1, block);
    }

    private static int getBits(final int numBits, final BZip2DecompressionState block) {
        final int bits;
        do {
            if (block.bsLive >= numBits) {
                final int v = block.bsBuff >> block.bsLive - numBits & (1 << numBits)
                    - 1;
                block.bsLive -= numBits;
                bits = v;
                break;
            }
            block.bsBuff = block.bsBuff << 8 | block.stream[block.nextIn]
                & 0xff;
            block.bsLive += 8;
            block.nextIn++;
            block.availableIn--;
            block.totalInLo32++;
            if (block.totalInLo32 == 0) {
                block.totalInHi32++;
            }
        } while (true);
        return bits;
    }

    private static byte getUChar(final BZip2DecompressionState block) {
        return (byte) getBits(8, block);
    }

    private static void makeMaps(final BZip2DecompressionState block) {
        block.nInUse = 0;
        for (int i = 0; i < 256; i++) {
            if (block.inUse[i]) {
                block.seqToUnseq[block.nInUse] = (byte) i;
                block.nInUse++;
            }
        }

    }

    private static void method226(final BZip2DecompressionState block) {
        // unRLE_obuf_to_output_FAST
        byte stateOutCh = block.stateOutCh;
        int stateOutLen = block.stateOutLen;
        int nBlockUsed = block.nBlockUsed;
        int k0 = block.k0;
        final int[] tt = BZip2DecompressionState.tt;
        int tPos = block.tPos;
        final byte[] buf = block.buf;
        int csNextOut = block.nextOut;
        int csAvailOut = block.availOut;

        final int availOutInit = csAvailOut;
        final int savedNBlockPP = block.nBlock + 1;

        outer:
        do {

            /* try to finish existing run */
            if (stateOutLen > 0) {
                do {
                    if (csAvailOut == 0) {
                        break outer;
                    }
                    if (stateOutLen == 1) {
                        break;
                    }
                    buf[csNextOut] = stateOutCh;
                    /*
                     * In the actual implementation it updates the BZ CRC here,
                     * but JaGex doesn't do this.
                     */
                    stateOutLen--;
                    csNextOut++;
                    csAvailOut--;
                } while (true);
                if (csAvailOut == 0) {
                    stateOutLen = 1;
                    break;
                }
                buf[csNextOut] = stateOutCh;
                csNextOut++;
                csAvailOut--;
            }
            boolean flag = true;
            while (flag) {
                flag = false;
                if (nBlockUsed == savedNBlockPP) {
                    stateOutLen = 0;
                    break outer;
                }
                stateOutCh = (byte) k0;

                // BZ_GET_FAST_C

                tPos = tt[tPos];
                final byte k1 = (byte) (tPos & 0xff);
                tPos >>= 8;

                nBlockUsed++;
                if (k1 != k0) {
                    k0 = k1;
                    if (csAvailOut == 0) {
                        stateOutLen = 1;
                    } else {
                        buf[csNextOut] = stateOutCh;
                        csNextOut++;
                        csAvailOut--;
                        flag = true;
                        continue;
                    }
                    break outer;
                }
                if (nBlockUsed != savedNBlockPP) {
                    continue;
                }
                if (csAvailOut == 0) {
                    stateOutLen = 1;
                    break outer;
                }
                buf[csNextOut] = stateOutCh;
                csNextOut++;
                csAvailOut--;
                flag = true;
            }
            stateOutLen = 2;

            // BZ_GET_FAST_C

            tPos = tt[tPos];
            final byte k1 = (byte) (tPos & 0xff);
            tPos >>= 8;

            // end BZ_GET_FAST_C

            if (++nBlockUsed != savedNBlockPP) {
                if (k1 != k0) {
                    k0 = k1;
                } else {
                    stateOutLen = 3;

                    tPos = tt[tPos];
                    final byte k1_ = (byte) (tPos & 0xff);
                    tPos >>= 8;

                    if (++nBlockUsed != savedNBlockPP) {
                        if (k1_ != k0) {
                            k0 = k1_;
                        } else {

                            tPos = tt[tPos];
                            final byte k1__ = (byte) (tPos & 0xff);
                            tPos >>= 8;

                            nBlockUsed++;
                            stateOutLen = (k1__ & 0xff) + 4;

                            tPos = tt[tPos];
                            k0 = (byte) (tPos & 0xff);
                            tPos >>= 8;

                            nBlockUsed++;
                        }
                    }
                }
            }
        } while (true);
        final int oldTotalOutLo32 = block.totalOutLo32;
        block.totalOutLo32 += availOutInit - csAvailOut;
        if (block.totalOutLo32 < oldTotalOutLo32) {
            block.totalOutHigh32++;
        }
        block.stateOutCh = stateOutCh;
        block.stateOutLen = stateOutLen;
        block.nBlockUsed = nBlockUsed;
        block.k0 = k0;
        BZip2DecompressionState.tt = tt;
        block.tPos = tPos;
        block.buf = buf;
        block.nextOut = csNextOut;
        block.availOut = csAvailOut;
    }

}