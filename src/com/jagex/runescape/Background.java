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

public final class Background extends DrawingArea {

	public byte imagePixels[];

	public final int[] palette;

	public int imageWidth;

	public int imageHeight;

	public int drawOffsetX;

	public int drawOffsetY;

	public int libWidth;

	private int anInt1457;

	public Background(Archive streamLoader, String s, int i) {
		Buffer stream = new Buffer(streamLoader.decompressFile(s + ".dat"));
		Buffer stream_1 = new Buffer(streamLoader.decompressFile("index.dat"));
		stream_1.currentOffset = stream.getUnsignedLEShort();
		libWidth = stream_1.getUnsignedLEShort();
		anInt1457 = stream_1.getUnsignedLEShort();
		int j = stream_1.getUnsignedByte();
		palette = new int[j];
		for (int k = 0; k < j - 1; k++)
			palette[k + 1] = stream_1.get24BitInt();

		for (int l = 0; l < i; l++) {
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.getUnsignedLEShort()
					* stream_1.getUnsignedLEShort();
			stream_1.currentOffset++;
		}

		drawOffsetX = stream_1.getUnsignedByte();
		drawOffsetY = stream_1.getUnsignedByte();
		imageWidth = stream_1.getUnsignedLEShort();
		imageHeight = stream_1.getUnsignedLEShort();
		int i1 = stream_1.getUnsignedByte();
		int j1 = imageWidth * imageHeight;
		imagePixels = new byte[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				imagePixels[k1] = stream.get();

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < imageWidth; l1++) {
				for (int i2 = 0; i2 < imageHeight; i2++)
					imagePixels[l1 + i2 * imageWidth] = stream.get();

			}

		}
	}
	public void drawImage(int i, int k) {
		i += drawOffsetX;
		k += drawOffsetY;
		int l = i + k * DrawingArea.width;
		int i1 = 0;
		int j1 = imageHeight;
		int k1 = imageWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (k < DrawingArea.topY) {
			int j2 = DrawingArea.topY - k;
			j1 -= j2;
			k = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (k + j1 > DrawingArea.bottomY)
			j1 -= (k + j1) - DrawingArea.bottomY;
		if (i < DrawingArea.topX) {
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > DrawingArea.bottomX) {
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method362(j1, DrawingArea.pixels, imagePixels, l1, l, k1, i1,
					palette, i2);
		}
	}
	public void method356() {
		libWidth /= 2;
		anInt1457 /= 2;
		byte abyte0[] = new byte[libWidth * anInt1457];
		int i = 0;
		for (int j = 0; j < imageHeight; j++) {
			for (int k = 0; k < imageWidth; k++)
				abyte0[(k + drawOffsetX >> 1) + (j + drawOffsetY >> 1)
						* libWidth] = imagePixels[i++];

		}

		imagePixels = abyte0;
		imageWidth = libWidth;
		imageHeight = anInt1457;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}
	public void method357() {
		if (imageWidth == libWidth && imageHeight == anInt1457)
			return;
		byte abyte0[] = new byte[libWidth * anInt1457];
		int i = 0;
		for (int j = 0; j < imageHeight; j++) {
			for (int k = 0; k < imageWidth; k++)
				abyte0[k + drawOffsetX + (j + drawOffsetY) * libWidth] = imagePixels[i++];

		}

		imagePixels = abyte0;
		imageWidth = libWidth;
		imageHeight = anInt1457;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}
	public void method358() {
		byte abyte0[] = new byte[imageWidth * imageHeight];
		int j = 0;
		for (int k = 0; k < imageHeight; k++) {
			for (int l = imageWidth - 1; l >= 0; l--)
				abyte0[j++] = imagePixels[l + k * imageWidth];

		}

		imagePixels = abyte0;
		drawOffsetX = libWidth - imageWidth - drawOffsetX;
	}
	public void method359() {
		byte abyte0[] = new byte[imageWidth * imageHeight];
		int i = 0;
		for (int j = imageHeight - 1; j >= 0; j--) {
			for (int k = 0; k < imageWidth; k++)
				abyte0[i++] = imagePixels[k + j * imageWidth];

		}

		imagePixels = abyte0;
		drawOffsetY = anInt1457 - imageHeight - drawOffsetY;
	}
	private void method362(int i, int ai[], byte abyte0[], int j, int k, int l,
			int i1, int ai1[], int j1) {
		int k1 = -(l >> 2);
		l = -(l & 3);
		for (int l1 = -i; l1 < 0; l1++) {
			for (int i2 = k1; i2 < 0; i2++) {
				byte byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
			}

			for (int j2 = l; j2 < 0; j2++) {
				byte byte2 = abyte0[i1++];
				if (byte2 != 0)
					ai[k++] = ai1[byte2 & 0xff];
				else
					k++;
			}

			k += j;
			i1 += j1;
		}

	}
	public void mixPalette(int red, int green, int blue) {
		for (int i = 0; i < palette.length; i++) {
			int r = palette[i] >> 16 & 0xff;
			r += red;
			if (r < 0)
				r = 0;
			else if (r > 255)
				r = 255;
			int g = palette[i] >> 8 & 0xff;
			g += green;
			if (g < 0)
				g = 0;
			else if (g > 255)
				g = 255;
			int b = palette[i] & 0xff;
			b += blue;
			if (b < 0)
				b = 0;
			else if (b > 255)
				b = 255;
			palette[i] = (r << 16) + (g << 8) + b;
		}
	}
}
