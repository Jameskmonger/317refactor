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

import java.math.BigInteger;
import com.jagex.runescape.sign.signlink;

public final class Buffer extends QueueLink {

	public static Buffer create() {
		synchronized (nodeList) {
			Buffer stream = null;
			if (anInt1412 > 0) {
				anInt1412--;
				stream = (Buffer) nodeList.popHead();
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

	private static final int[] anIntArray1409 = { 0, 1, 3, 7, 15, 31, 63, 127,
			255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
			0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
			0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
			0x7fffffff, -1 };

	public ISAACRandomGenerator encryption;

	private static int anInt1412;

	private static final NodeList nodeList = new NodeList();

	private Buffer() {
	}

	public Buffer(byte abyte0[]) {
		buffer = abyte0;
		position = 0;
	}

	public void finishBitAccess() {
		position = (bitPosition + 7) / 8;
	}

	public void generateKeys() {
		int i = position;
		position = 0;
		byte abyte0[] = new byte[i];
		readBytes(i, 0, abyte0);
		BigInteger biginteger2 = new BigInteger(abyte0);
		BigInteger biginteger3 = biginteger2/* .modPow(biginteger, biginteger1) */;
		byte abyte1[] = biginteger3.toByteArray();
		position = 0;
		put(abyte1.length);
		putBytes(abyte1, abyte1.length, 0);
	}

	public byte get() {
		return buffer[position++];
	}

	public int get24BitInt() {
		position += 3;
		return ((buffer[position - 3] & 0xff) << 16)
				+ ((buffer[position - 2] & 0xff) << 8)
				+ (buffer[position - 1] & 0xff);
	}

	public byte getByteC() {
		return (byte) (-buffer[position++]);
	}

	public void getBytes(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = buffer[position++];

	}

	public byte getByteS() {
		return (byte) (128 - buffer[position++]);
	}

	public int getForceLEShort() {
		position += 2;
		int j = ((buffer[position - 1] & 0xff) << 8)
				+ (buffer[position - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int getForceLEShortA() {
		position += 2;
		int j = ((buffer[position - 1] & 0xff) << 8)
				+ (buffer[position - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int getInt() {
		position += 4;
		return ((buffer[position - 4] & 0xff) << 24)
				+ ((buffer[position - 3] & 0xff) << 16)
				+ ((buffer[position - 2] & 0xff) << 8)
				+ (buffer[position - 1] & 0xff);
	}

	public int getInt1() {
		position += 4;
		return ((buffer[position - 3] & 0xff) << 24)
				+ ((buffer[position - 4] & 0xff) << 16)
				+ ((buffer[position - 1] & 0xff) << 8)
				+ (buffer[position - 2] & 0xff);
	}

	public int getInt2() {
		position += 4;
		return ((buffer[position - 2] & 0xff) << 24)
				+ ((buffer[position - 1] & 0xff) << 16)
				+ ((buffer[position - 4] & 0xff) << 8)
				+ (buffer[position - 3] & 0xff);
	}

	public long getLong() {
		long l = getInt() & 0xffffffffL;
		long l1 = getInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public int getShort() {
		position += 2;
		int i = ((buffer[position - 2] & 0xff) << 8)
				+ (buffer[position - 1] & 0xff);
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
		return ((buffer[position - 2] & 0xff) << 8)
				+ (buffer[position - 1] & 0xff);
	}

	public int getUnsignedLEShortA() {
		position += 2;
		return ((buffer[position - 2] & 0xff) << 8)
				+ (buffer[position - 1] - 128 & 0xff);
	}

	public int getUnsignedShort() {
		position += 2;
		return ((buffer[position - 1] & 0xff) << 8)
				+ (buffer[position - 2] & 0xff);
	}

	public int getUnsignedShortA() {
		position += 2;
		return ((buffer[position - 1] & 0xff) << 8)
				+ (buffer[position - 2] - 128 & 0xff);
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

	public void putBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++)
			buffer[position++] = abyte0[k];

	}

	public void putByteS(int j) {
		buffer[position++] = (byte) (128 - j);
	}

	public void putBytesA(int i, byte abyte0[], int j) {
		for (int k = (i + j) - 1; k >= i; k--)
			buffer[position++] = (byte) (abyte0[k] + 128);

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
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("14395, " + 5 + ", " + l + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	public void putOpcode(int i) {
		buffer[position++] = (byte) (i + encryption.value());
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
		int i1 = 0;
		bitPosition += i;
		for (; i > l; l = 8) {
			i1 += (buffer[k++] & anIntArray1409[l]) << i - l;
			i -= l;
		}
		if (i == l)
			i1 += buffer[k] & anIntArray1409[l];
		else
			i1 += buffer[k] >> l - i & anIntArray1409[i];
		return i1;
	}
	public byte[] readBytes() {
		int i = position;
		while (buffer[position++] != 10)
			;
		byte abyte0[] = new byte[position - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, position - 1 - i);
		return abyte0;
	}
	public void readBytes(int i, int j, byte abyte0[]) {
		for (int l = j; l < j + i; l++)
			abyte0[l] = buffer[position++];
	}

	// removed useless static initializer
}
