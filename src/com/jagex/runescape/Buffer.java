package com.jagex.runescape;

import com.jagex.runescape.collection.Cacheable;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.isaac.ISAACRandomGenerator;
import com.jagex.runescape.sign.signlink;

import java.math.BigInteger;

public final class Buffer extends Cacheable {

    public static Buffer create() {
        synchronized (BUFFER_CACHE) {
            Buffer stream = null;

            if (cacheCount > 0) {
                cacheCount--;
                stream = (Buffer) BUFFER_CACHE.popFront();
            }

            if (stream != null) {
                stream.position = 0;
                return stream;
            }
        }
        final Buffer stream_1 = new Buffer();
        stream_1.position = 0;
        stream_1.buffer = new byte[5000];
        return stream_1;
    }

    public byte[] buffer;

    public int position;

    public int bitPosition;

    private static final int[] BIT_MASKS = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383,
        32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff,
        0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1};

    public ISAACRandomGenerator encryptor;

    private static int cacheCount;

    private static final DoubleEndedQueue BUFFER_CACHE = new DoubleEndedQueue();

    private Buffer() {
    }

    public Buffer(final byte[] buf) {
        this.buffer = buf;
        this.position = 0;
    }

    public void finishBitAccess() {
        this.position = (this.bitPosition + 7) / 8;
    }

    public void generateKeys() {
        final int tmpPos = this.position;
        this.position = 0;
        final byte[] buf = new byte[tmpPos];
        this.readBytes(tmpPos, 0, buf);
        final BigInteger val1 = new BigInteger(buf);
        final BigInteger val2 = val1/* .modPow(val1, val2) */;
        final byte[] finalBuf = val2.toByteArray();
        this.position = 0;
        this.put(finalBuf.length);
        this.putBytes(finalBuf, finalBuf.length, 0);
    }

    public byte get() {
        return this.buffer[this.position++];
    }

    public int get3Bytes() {
        this.position += 3;
        return ((this.buffer[this.position - 3] & 0xff) << 16) + ((this.buffer[this.position - 2] & 0xff) << 8)
            + (this.buffer[this.position - 1] & 0xff);
    }

    public byte getByteC() {
        return (byte) (-this.buffer[this.position++]);
    }

    public void getBytes(final int startPos, final int endPos, final byte[] buf) {
        for (int k = (endPos + startPos) - 1; k >= endPos; k--) {
            buf[k] = this.buffer[this.position++];
        }
    }

    public byte getByteS() {
        return (byte) (128 - this.buffer[this.position++]);
    }

    public int getSignedLEShort() {
        this.position += 2;
        int j = ((this.buffer[this.position - 1] & 0xff) << 8) + (this.buffer[this.position - 2] & 0xff);
        if (j > 32767) {
            j -= 0x10000;
        }
        return j;
    }

    public int getSignedLEShortA() {
        this.position += 2;
        int j = ((this.buffer[this.position - 1] & 0xff) << 8) + (this.buffer[this.position - 2] - 128 & 0xff);
        if (j > 32767) {
            j -= 0x10000;
        }
        return j;
    }

    public int getIntBE() {
        this.position += 4;
        return ((this.buffer[this.position - 4] & 0xff) << 24) + ((this.buffer[this.position - 3] & 0xff) << 16)
            + ((this.buffer[this.position - 2] & 0xff) << 8) + (this.buffer[this.position - 1] & 0xff);
    }

    public int getMEBInt() { // Middle endian big int: C3 D4 A1 B2 (A1 smallest D4 biggest byte)
        this.position += 4;
        return ((this.buffer[this.position - 3] & 0xff) << 24) + ((this.buffer[this.position - 4] & 0xff) << 16)
            + ((this.buffer[this.position - 1] & 0xff) << 8) + (this.buffer[this.position - 2] & 0xff);
    }

    public int getMESInt() { // Middle endian small int: B2 A1 D4 C3 (A1 smallest D4 biggest byte)
        this.position += 4;
        return ((this.buffer[this.position - 2] & 0xff) << 24) + ((this.buffer[this.position - 1] & 0xff) << 16)
            + ((this.buffer[this.position - 4] & 0xff) << 8) + (this.buffer[this.position - 3] & 0xff);
    }

    public long getLongBE() {
        final long ms = this.getIntBE() & 0xffffffffL;
        final long ls = this.getIntBE() & 0xffffffffL;
        return (ms << 32) + ls;
    }

    public int getShortBE() {
        this.position += 2;
        int i = ((this.buffer[this.position - 2] & 0xff) << 8) + (this.buffer[this.position - 1] & 0xff);
        if (i > 32767) {
            i -= 0x10000;
        }
        return i;
    }

    public int getSmartA() {
        final int i = this.buffer[this.position] & 0xff;
        if (i < 128) {
            return this.getUnsignedByte() - 64;
        } else {
            return this.getUnsignedBEShort() - 49152;
        }
    }

    public int getSmartB() {
        final int i = this.buffer[this.position] & 0xff;
        if (i < 128) {
            return this.getUnsignedByte();
        } else {
            return this.getUnsignedBEShort() - 32768;
        }
    }

    public String getString() {
        final int i = this.position;
        while (this.buffer[this.position++] != 10) {
        }
        return new String(this.buffer, i, this.position - i - 1);
    }

    public int getUnsignedByte() {
        return this.buffer[this.position++] & 0xff;
    }

    public int getUnsignedByteA() {
        return this.buffer[this.position++] - 128 & 0xff;
    }

    public int getUnsignedByteC() {
        return -this.buffer[this.position++] & 0xff;
    }

    public int getUnsignedByteS() {
        return 128 - this.buffer[this.position++] & 0xff;
    }

    public int getUnsignedBEShort() {
        this.position += 2;
        return ((this.buffer[this.position - 2] & 0xff) << 8) + (this.buffer[this.position - 1] & 0xff);
    }

    public int getUnsignedBEShortA() {
        this.position += 2;
        return ((this.buffer[this.position - 2] & 0xff) << 8) + (this.buffer[this.position - 1] - 128 & 0xff);
    }

    public int getUnsignedLEShort() {
        this.position += 2;
        return ((this.buffer[this.position - 1] & 0xff) << 8) + (this.buffer[this.position - 2] & 0xff);
    }

    public int getUnsignedLEShortA() {
        this.position += 2;
        return ((this.buffer[this.position - 1] & 0xff) << 8) + (this.buffer[this.position - 2] - 128 & 0xff);
    }

    public void initBitAccess() {
        this.bitPosition = this.position * 8;
    }

    public void put(final int i) {
        this.buffer[this.position++] = (byte) i;
    }

    public void put24BitInt(final int i) {
        this.buffer[this.position++] = (byte) (i >> 16);
        this.buffer[this.position++] = (byte) (i >> 8);
        this.buffer[this.position++] = (byte) i;
    }

    public void putByteC(final int i) {
        this.buffer[this.position++] = (byte) (-i);
    }

    public void putBytes(final byte[] buf, final int length, final int startPosition) {
        for (int k = startPosition; k < startPosition + length; k++) {
            this.buffer[this.position++] = buf[k];
        }

    }

    public void putByteS(final int j) {
        this.buffer[this.position++] = (byte) (128 - j);
    }

    public void putBytesA(final int i, final byte[] buf, final int j) {
        for (int k = (i + j) - 1; k >= i; k--) {
            this.buffer[this.position++] = (byte) (buf[k] + 128);
        }

    }

    public void putIntBE(final int i) {
        this.buffer[this.position++] = (byte) (i >> 24);
        this.buffer[this.position++] = (byte) (i >> 16);
        this.buffer[this.position++] = (byte) (i >> 8);
        this.buffer[this.position++] = (byte) i;
    }

    public void putLEInt(final int j) {
        this.buffer[this.position++] = (byte) j;
        this.buffer[this.position++] = (byte) (j >> 8);
        this.buffer[this.position++] = (byte) (j >> 16);
        this.buffer[this.position++] = (byte) (j >> 24);
    }

    public void putLEShort(final int i) {
        this.buffer[this.position++] = (byte) i;
        this.buffer[this.position++] = (byte) (i >> 8);
    }

    public void putLEShortA(final int j) {
        this.buffer[this.position++] = (byte) (j + 128);
        this.buffer[this.position++] = (byte) (j >> 8);
    }

    public void putLong(final long l) {
        try {
            this.buffer[this.position++] = (byte) (int) (l >> 56);
            this.buffer[this.position++] = (byte) (int) (l >> 48);
            this.buffer[this.position++] = (byte) (int) (l >> 40);
            this.buffer[this.position++] = (byte) (int) (l >> 32);
            this.buffer[this.position++] = (byte) (int) (l >> 24);
            this.buffer[this.position++] = (byte) (int) (l >> 16);
            this.buffer[this.position++] = (byte) (int) (l >> 8);
            this.buffer[this.position++] = (byte) (int) l;
        } catch (final RuntimeException ex) {
            signlink.reporterror("14395, " + 5 + ", " + l + ", " + ex);
            throw new RuntimeException();
        }
    }

    public void putOpcode(final int i) {
        this.buffer[this.position++] = (byte) (i + this.encryptor.value());
    }

    public void putShort(final int i) {
        this.buffer[this.position++] = (byte) (i >> 8);
        this.buffer[this.position++] = (byte) i;
    }

    public void putShortA(final int j) {
        this.buffer[this.position++] = (byte) (j >> 8);
        this.buffer[this.position++] = (byte) (j + 128);
    }

    public void putSizeByte(final int i) {
        this.buffer[this.position - i - 1] = (byte) i;
    }

    public void putString(final String s) {
        // s.getBytes(0, s.length(), buffer, currentOffset); //deprecated
        System.arraycopy(s.getBytes(), 0, this.buffer, this.position, s.length());
        this.position += s.length();
        this.buffer[this.position++] = 10;
    }

    public int readBits(int i) {
        int k = this.bitPosition >> 3;
        int l = 8 - (this.bitPosition & 7);
        int val = 0;
        this.bitPosition += i;
        for (; i > l; l = 8) {
            val += (this.buffer[k++] & BIT_MASKS[l]) << i - l;
            i -= l;
        }
        if (i == l) {
            val += this.buffer[k] & BIT_MASKS[l];
        } else {
            val += this.buffer[k] >> l - i & BIT_MASKS[i];
        }
        return val;
    }

    public byte[] readBytes() {
        final int tmpPos = this.position;
        while (this.buffer[this.position++] != 10) {
        }
        final byte[] buf = new byte[this.position - tmpPos - 1];
        System.arraycopy(this.buffer, tmpPos, buf, 0, this.position - 1 - tmpPos);
        return buf;
    }

    public void readBytes(final int length, final int startPosition, final byte[] dest) {
        for (int i = startPosition; i < startPosition + length; i++) {
            dest[i] = this.buffer[this.position++];
        }
    }

    // removed useless static initializer
}
