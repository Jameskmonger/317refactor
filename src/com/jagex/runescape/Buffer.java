package com.jagex.runescape;

import com.jagex.runescape.sign.signlink;

import java.math.BigInteger;

public final class Buffer extends QueueLink {

    public static Buffer create() {
        synchronized (BUFFER_CACHE) {
            Buffer stream = null;

            if (cacheCount > 0) {
                cacheCount--;
                stream = (Buffer) BUFFER_CACHE.popHead();
            }

            if (stream != null) {
                stream.position = 0;
                return stream;
            }
        }
        Buffer stream_1 = new Buffer();
        stream_1.position = 0;
        stream_1.buffer = new byte[5000];
        return stream_1;
    }

    public byte buffer[];

    public int position;

    public int bitPosition;

    private static final int[] BIT_MASKS = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383,
            32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff,
            0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

    public ISAACRandomGenerator encryptor;

    private static int cacheCount;

    private static final NodeList BUFFER_CACHE = new NodeList();

    private Buffer() {
    }

    public Buffer(byte buf[]) {
        buffer = buf;
        position = 0;
    }

    public void finishBitAccess() {
        position = (bitPosition + 7) / 8;
    }

    public void generateKeys() {
        int tmpPos = position;
        position = 0;
        byte buf[] = new byte[tmpPos];
        readBytes(tmpPos, 0, buf);
        BigInteger val1 = new BigInteger(buf);
        BigInteger val2 = val1/* .modPow(val1, val2) */;
        byte finalBuf[] = val2.toByteArray();
        position = 0;
        put(finalBuf.length);
        putBytes(finalBuf, finalBuf.length, 0);
    }

    public byte get() {
        return buffer[position++];
    }

    public int get3Bytes() {
        position += 3;
        return ((buffer[position - 3] & 0xff) << 16) + ((buffer[position - 2] & 0xff) << 8)
                + (buffer[position - 1] & 0xff);
    }

    public byte getByteC() {
        return (byte) (-buffer[position++]);
    }

    public void getBytes(int startPos, int endPos, byte buf[]) {
        for (int k = (endPos + startPos) - 1; k >= endPos; k--)
            buf[k] = buffer[position++];
    }

    public byte getByteS() {
        return (byte) (128 - buffer[position++]);
    }

    public int getSignedLEShort() {
        position += 2;
        int j = ((buffer[position - 1] & 0xff) << 8) + (buffer[position - 2] & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int getSignedLEShortA() {
        position += 2;
        int j = ((buffer[position - 1] & 0xff) << 8) + (buffer[position - 2] - 128 & 0xff);
        if (j > 32767)
            j -= 0x10000;
        return j;
    }

    public int getInt() {
        position += 4;
        return ((buffer[position - 4] & 0xff) << 24) + ((buffer[position - 3] & 0xff) << 16)
                + ((buffer[position - 2] & 0xff) << 8) + (buffer[position - 1] & 0xff);
    }

    public int getMEBInt() { // Middle endian big int: C3 D4 A1 B2 (A1 smallest D4 biggest byte)
        position += 4;
        return ((buffer[position - 3] & 0xff) << 24) + ((buffer[position - 4] & 0xff) << 16)
                + ((buffer[position - 1] & 0xff) << 8) + (buffer[position - 2] & 0xff);
    }

    public int getMESInt() { // Middle endian small int: B2 A1 D4 C3 (A1 smallest D4 biggest byte)
        position += 4;
        return ((buffer[position - 2] & 0xff) << 24) + ((buffer[position - 1] & 0xff) << 16)
                + ((buffer[position - 4] & 0xff) << 8) + (buffer[position - 3] & 0xff);
    }

    public long getLong() {
        long ms = getInt() & 0xffffffffL;
        long ls = getInt() & 0xffffffffL;
        return (ms << 32) + ls;
    }

    public int getShort() {
        position += 2;
        int i = ((buffer[position - 2] & 0xff) << 8) + (buffer[position - 1] & 0xff);
        if (i > 32767)
            i -= 0x10000;
        return i;
    }

    public int getSmartA() {
        int i = buffer[position] & 0xff;
        if (i < 128)
            return getUnsignedByte() - 64;
        else
            return getUnsignedLEShort() - 49152;
    }

    public int getSmartB() {
        int i = buffer[position] & 0xff;
        if (i < 128)
            return getUnsignedByte();
        else
            return getUnsignedLEShort() - 32768;
    }

    public String getString() {
        int i = position;
        while (buffer[position++] != 10)
            ;
        return new String(buffer, i, position - i - 1);
    }

    public int getUnsignedByte() {
        return buffer[position++] & 0xff;
    }

    public int getUnsignedByteA() {
        return buffer[position++] - 128 & 0xff;
    }

    public int getUnsignedByteC() {
        return -buffer[position++] & 0xff;
    }

    public int getUnsignedByteS() {
        return 128 - buffer[position++] & 0xff;
    }

    public int getUnsignedLEShort() {
        position += 2;
        return ((buffer[position - 2] & 0xff) << 8) + (buffer[position - 1] & 0xff);
    }

    public int getUnsignedLEShortA() {
        position += 2;
        return ((buffer[position - 2] & 0xff) << 8) + (buffer[position - 1] - 128 & 0xff);
    }

    public int getUnsignedShort() {
        position += 2;
        return ((buffer[position - 1] & 0xff) << 8) + (buffer[position - 2] & 0xff);
    }

    public int getUnsignedShortA() {
        position += 2;
        return ((buffer[position - 1] & 0xff) << 8) + (buffer[position - 2] - 128 & 0xff);
    }

    public void initBitAccess() {
        bitPosition = position * 8;
    }

    public void put(int i) {
        buffer[position++] = (byte) i;
    }

    public void put24BitInt(int i) {
        buffer[position++] = (byte) (i >> 16);
        buffer[position++] = (byte) (i >> 8);
        buffer[position++] = (byte) i;
    }

    public void putByteC(int i) {
        buffer[position++] = (byte) (-i);
    }

    public void putBytes(byte buf[], int length, int startPosition) {
        for (int k = startPosition; k < startPosition + length; k++)
            buffer[position++] = buf[k];

    }

    public void putByteS(int j) {
        buffer[position++] = (byte) (128 - j);
    }

    public void putBytesA(int i, byte buf[], int j) {
        for (int k = (i + j) - 1; k >= i; k--)
            buffer[position++] = (byte) (buf[k] + 128);

    }

    public void putInt(int i) {
        buffer[position++] = (byte) (i >> 24);
        buffer[position++] = (byte) (i >> 16);
        buffer[position++] = (byte) (i >> 8);
        buffer[position++] = (byte) i;
    }

    public void putLEInt(int j) {
        buffer[position++] = (byte) j;
        buffer[position++] = (byte) (j >> 8);
        buffer[position++] = (byte) (j >> 16);
        buffer[position++] = (byte) (j >> 24);
    }

    public void putLEShort(int i) {
        buffer[position++] = (byte) i;
        buffer[position++] = (byte) (i >> 8);
    }

    public void putLEShortA(int j) {
        buffer[position++] = (byte) (j + 128);
        buffer[position++] = (byte) (j >> 8);
    }

    public void putLong(long l) {
        try {
            buffer[position++] = (byte) (int) (l >> 56);
            buffer[position++] = (byte) (int) (l >> 48);
            buffer[position++] = (byte) (int) (l >> 40);
            buffer[position++] = (byte) (int) (l >> 32);
            buffer[position++] = (byte) (int) (l >> 24);
            buffer[position++] = (byte) (int) (l >> 16);
            buffer[position++] = (byte) (int) (l >> 8);
            buffer[position++] = (byte) (int) l;
        } catch (RuntimeException ex) {
            signlink.reporterror("14395, " + 5 + ", " + l + ", " + ex.toString());
            throw new RuntimeException();
        }
    }

    public void putOpcode(int i) {
        buffer[position++] = (byte) (i + encryptor.value());
    }

    public void putShort(int i) {
        buffer[position++] = (byte) (i >> 8);
        buffer[position++] = (byte) i;
    }

    public void putShortA(int j) {
        buffer[position++] = (byte) (j >> 8);
        buffer[position++] = (byte) (j + 128);
    }

    public void putSizeByte(int i) {
        buffer[position - i - 1] = (byte) i;
    }

    public void putString(String s) {
        // s.getBytes(0, s.length(), buffer, currentOffset); //deprecated
        System.arraycopy(s.getBytes(), 0, buffer, position, s.length());
        position += s.length();
        buffer[position++] = 10;
    }

    public int readBits(int i) {
        int k = bitPosition >> 3;
        int l = 8 - (bitPosition & 7);
        int val = 0;
        bitPosition += i;
        for (; i > l; l = 8) {
            val += (buffer[k++] & BIT_MASKS[l]) << i - l;
            i -= l;
        }
        if (i == l)
            val += buffer[k] & BIT_MASKS[l];
        else
            val += buffer[k] >> l - i & BIT_MASKS[i];
        return val;
    }

    public byte[] readBytes() {
        int tmpPos = position;
        while (buffer[position++] != 10)
            ;
        byte buf[] = new byte[position - tmpPos - 1];
        System.arraycopy(buffer, tmpPos, buf, tmpPos - tmpPos, position - 1 - tmpPos);
        return buf;
    }

    public void readBytes(int length, int startPosition, byte dest[]) {
        for (int i = startPosition; i < startPosition + length; i++)
            dest[i] = buffer[position++];
    }

    // removed useless static initializer
}
