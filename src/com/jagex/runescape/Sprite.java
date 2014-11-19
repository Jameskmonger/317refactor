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

import java.awt.*;
import java.awt.image.PixelGrabber;

public final class Sprite extends DrawingArea {

	public int pixels[];

	public int width;

	public int height;

	private int offsetX;

	private int offsetY;

	public int maxWidth;

	public int maxHeight;

	public Sprite(Archive streamLoader, String target, int archiveId) {
		Buffer dataStream = new Buffer(streamLoader.decompressFile(target + ".dat"));
		Buffer indexStream = new Buffer(streamLoader.decompressFile("index.dat"));
		indexStream.position = dataStream.getUnsignedLEShort();
		maxWidth = indexStream.getUnsignedLEShort();
		maxHeight = indexStream.getUnsignedLEShort();
		int length = indexStream.getUnsignedByte();
		int pixels[] = new int[length];
		for (int p = 0; p < length - 1; p++) {
			pixels[p + 1] = indexStream.get24BitInt();
			if (pixels[p + 1] == 0)
				pixels[p + 1] = 1;
		}

		for (int i = 0; i < archiveId; i++) {
			indexStream.position += 2;
			dataStream.position += indexStream.getUnsignedLEShort()
					* indexStream.getUnsignedLEShort();
			indexStream.position++;
		}

		offsetX = indexStream.getUnsignedByte();
		offsetY = indexStream.getUnsignedByte();
		width = indexStream.getUnsignedLEShort();
		height = indexStream.getUnsignedLEShort();
		int type = indexStream.getUnsignedByte();
		int pixelCount = width * height;
		this.pixels = new int[pixelCount];
		if (type == 0) {
			for (int p = 0; p < pixelCount; p++)
				this.pixels[p] = pixels[dataStream.getUnsignedByte()];

			return;
		}
		if (type == 1) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++)
					this.pixels[x + y * width] = pixels[dataStream
							.getUnsignedByte()];

			}

		}
	}

	public Sprite(byte abyte0[], Component component) {
		try {
			// Image image =
			// Toolkit.getDefaultToolkit().getImage(signlink.findcachedir()+"mopar.jpg");
			Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
			MediaTracker mediatracker = new MediaTracker(component);
			mediatracker.addImage(image, 0);
			mediatracker.waitForAll();
			width = image.getWidth(component);
			height = image.getHeight(component);
			maxWidth = width;
			maxHeight = height;
			offsetX = 0;
			offsetY = 0;
			pixels = new int[width * height];
			PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width,
					height, pixels, 0, width);
			pixelgrabber.grabPixels();
		} catch (Exception _ex) {
			System.out.println("Error converting jpg");
		}
	}

	public Sprite(int i, int j) {
		pixels = new int[i * j];
		width = maxWidth = i;
		height = maxHeight = j;
		offsetX = offsetY = 0;
	}

	public void adjustRGB(int adjustmentR, int adjustmentG, int adjustmentB) {
		for (int pixel = 0; pixel < pixels.length; pixel++) {
			int originalColour = pixels[pixel];
			if (originalColour != 0) {
				int red = originalColour >> 16 & 0xff;
				red += adjustmentR;
				if (red < 1)
					red = 1;
				else if (red > 255)
					red = 255;
				int green = originalColour >> 8 & 0xff;
				green += adjustmentG;
				if (green < 1)
					green = 1;
				else if (green > 255)
					green = 255;
				int blue = originalColour & 0xff;
				blue += adjustmentB;
				if (blue < 1)
					blue = 1;
				else if (blue > 255)
					blue = 255;
				pixels[pixel] = (red << 16) + (green << 8) + blue;
			}
		}

	}

	private void blockCopy(int destinationPointer, int copyLength, int k,
			int sourceBlockLength, int sourcePointer,
			int destinationBlockLength, int source[], int destination[]) {
		int blockCount = -(copyLength >> 2);
		copyLength = -(copyLength & 3);
		for (int i2 = -k; i2 < 0; i2++) {
			for (int ptr = blockCount; ptr < 0; ptr++) {
				destination[destinationPointer++] = source[sourcePointer++];
				destination[destinationPointer++] = source[sourcePointer++];
				destination[destinationPointer++] = source[sourcePointer++];
				destination[destinationPointer++] = source[sourcePointer++];
			}

			for (int ptr = copyLength; ptr < 0; ptr++)
				destination[destinationPointer++] = source[sourcePointer++];

			destinationPointer += destinationBlockLength;
			sourcePointer += sourceBlockLength;
		}
	}

	private void blockCopyAlpha(int sourcePointer, int blockCount,
			int destination[], int source[], int sourceBlockLength, int i1,
			int destinationBlockLength, int alpha, int destinationPointer) {
		int sourceValue;// was parameter
		int destinationAlpha = 256 - alpha;
		for (int k2 = -i1; k2 < 0; k2++) {
			for (int ptr = -blockCount; ptr < 0; ptr++) {
				sourceValue = source[sourcePointer++];
				if (sourceValue != 0) {
					int destinationValue = destination[destinationPointer];
					destination[destinationPointer++] = ((sourceValue & 0xff00ff)
							* alpha
							+ (destinationValue & 0xff00ff)
							* destinationAlpha & 0xff00ff00)
							+ ((sourceValue & 0xff00) * alpha
									+ (destinationValue & 0xff00)
									* destinationAlpha & 0xff0000) >> 8;
				} else {
					destinationPointer++;
				}
			}

			destinationPointer += destinationBlockLength;
			sourcePointer += sourceBlockLength;
		}
	}

	private void blockCopyTransparent(int destination[], int source[],
			int sourcePointer, int destinationPointer, int copyLength, int i1,
			int destinationBlockLength, int sourceBlockLength) {
		int value;// was parameter
		int blockCount = -(copyLength >> 2);
		copyLength = -(copyLength & 3);
		for (int i2 = -i1; i2 < 0; i2++) {
			for (int ptr = blockCount; ptr < 0; ptr++) {
				value = source[sourcePointer++];
				if (value != 0)
					destination[destinationPointer++] = value;
				else
					destinationPointer++;
				value = source[sourcePointer++];
				if (value != 0)
					destination[destinationPointer++] = value;
				else
					destinationPointer++;
				value = source[sourcePointer++];
				if (value != 0)
					destination[destinationPointer++] = value;
				else
					destinationPointer++;
				value = source[sourcePointer++];
				if (value != 0)
					destination[destinationPointer++] = value;
				else
					destinationPointer++;
			}

			for (int ptr = copyLength; ptr < 0; ptr++) {
				value = source[sourcePointer++];
				if (value != 0)
					destination[destinationPointer++] = value;
				else
					destinationPointer++;
			}

			destinationPointer += destinationBlockLength;
			sourcePointer += sourceBlockLength;
		}

	}

	public void drawImage(int x, int y) {
		x += offsetX;
		y += offsetY;
		int destinationOffset = x + y * DrawingArea.width;
		int sourceOffset = 0;
		int rowCount = height;
		int columnCount = width;
		int lineDestinationOffset = DrawingArea.width - columnCount;
		int lineSourceOffset = 0;
		if (y < DrawingArea.topY) {
			int clipHeight = DrawingArea.topY - y;
			rowCount -= clipHeight;
			y = DrawingArea.topY;
			sourceOffset += clipHeight * columnCount;
			destinationOffset += clipHeight * DrawingArea.width;
		}
		if (y + rowCount > DrawingArea.bottomY)
			rowCount -= (y + rowCount) - DrawingArea.bottomY;
		if (x < DrawingArea.topX) {
			int clipWidth = DrawingArea.topX - x;
			columnCount -= clipWidth;
			x = DrawingArea.topX;
			sourceOffset += clipWidth;
			destinationOffset += clipWidth;
			lineSourceOffset += clipWidth;
			lineDestinationOffset += clipWidth;
		}
		if (x + columnCount > DrawingArea.bottomX) {
			int clipWidth = (x + columnCount) - DrawingArea.bottomX;
			columnCount -= clipWidth;
			lineSourceOffset += clipWidth;
			lineDestinationOffset += clipWidth;
		}
		if (!(columnCount <= 0 || rowCount <= 0)) {
			blockCopyTransparent(DrawingArea.pixels, pixels, sourceOffset,
					destinationOffset, columnCount, rowCount,
					lineDestinationOffset, lineSourceOffset);
		}
	}

	public void drawImageAlpha(int i, int j) {
		int k = 128;// was parameter
		i += offsetX;
		j += offsetY;
		int i1 = i + j * DrawingArea.width;
		int j1 = 0;
		int k1 = height;
		int l1 = width;
		int i2 = DrawingArea.width - l1;
		int j2 = 0;
		if (j < DrawingArea.topY) {
			int k2 = DrawingArea.topY - j;
			k1 -= k2;
			j = DrawingArea.topY;
			j1 += k2 * l1;
			i1 += k2 * DrawingArea.width;
		}
		if (j + k1 > DrawingArea.bottomY)
			k1 -= (j + k1) - DrawingArea.bottomY;
		if (i < DrawingArea.topX) {
			int l2 = DrawingArea.topX - i;
			l1 -= l2;
			i = DrawingArea.topX;
			j1 += l2;
			i1 += l2;
			j2 += l2;
			i2 += l2;
		}
		if (i + l1 > DrawingArea.bottomX) {
			int i3 = (i + l1) - DrawingArea.bottomX;
			l1 -= i3;
			j2 += i3;
			i2 += i3;
		}
		if (!(l1 <= 0 || k1 <= 0)) {
			blockCopyAlpha(j1, l1, DrawingArea.pixels, pixels, j2, k1, i2, k,
					i1);
		}
	}

	public void drawInverse(int i, int j) {
		i += offsetX;
		j += offsetY;
		int l = i + j * DrawingArea.width;
		int i1 = 0;
		int j1 = height;
		int k1 = width;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if (j < DrawingArea.topY) {
			int j2 = DrawingArea.topY - j;
			j1 -= j2;
			j = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if (j + j1 > DrawingArea.bottomY)
			j1 -= (j + j1) - DrawingArea.bottomY;
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
		if (k1 <= 0 || j1 <= 0) {
		} else {
			blockCopy(l, k1, j1, i2, i1, l1, pixels, DrawingArea.pixels);
		}
	}
	public void initDrawingArea() {
		DrawingArea.initDrawingArea(height, width, pixels);
	}
	public void method354(Background background, int x, int y) {
		y += offsetX;
		x += offsetY;
		int k = y + x * DrawingArea.width;
		int l = 0;
		int i1 = height;
		int j1 = width;
		int k1 = DrawingArea.width - j1;
		int l1 = 0;
		if (x < DrawingArea.topY) {
			int i2 = DrawingArea.topY - x;
			i1 -= i2;
			x = DrawingArea.topY;
			l += i2 * j1;
			k += i2 * DrawingArea.width;
		}
		if (x + i1 > DrawingArea.bottomY)
			i1 -= (x + i1) - DrawingArea.bottomY;
		if (y < DrawingArea.topX) {
			int j2 = DrawingArea.topX - y;
			j1 -= j2;
			y = DrawingArea.topX;
			l += j2;
			k += j2;
			l1 += j2;
			k1 += j2;
		}
		if (y + j1 > DrawingArea.bottomX) {
			int k2 = (y + j1) - DrawingArea.bottomX;
			j1 -= k2;
			l1 += k2;
			k1 += k2;
		}
		if (!(j1 <= 0 || i1 <= 0)) {
			method355(pixels, j1, background.imagePixels, i1,
					DrawingArea.pixels, 0, k1, k, l1, l);
		}
	}
	private void method355(int ai[], int i, byte abyte0[], int j, int ai1[],
			int k, int l, int i1, int j1, int k1) {
		int l1 = -(i >> 2);
		i = -(i & 3);
		for (int j2 = -j; j2 < 0; j2++) {
			for (int k2 = l1; k2 < 0; k2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			for (int l2 = i; l2 < 0; l2++) {
				k = ai[k1++];
				if (k != 0 && abyte0[i1] == 0)
					ai1[i1++] = k;
				else
					i1++;
			}

			i1 += l;
			k1 += j1;
		}

	}
	public void rotate(int x, int y, double rotation) {
		// all of the following were parameters
		int centreY = 15;
		int width = 20;
		int centreX = 15;
		int hingeSize = 256;
		int height = 20;
		// all of the previous were parameters
		try {
			int i2 = -width / 2;
			int j2 = -height / 2;
			int k2 = (int) (Math.sin(rotation) * 65536D);
			int l2 = (int) (Math.cos(rotation) * 65536D);
			k2 = k2 * hingeSize >> 8;
			l2 = l2 * hingeSize >> 8;
			int i3 = (centreX << 16) + (j2 * k2 + i2 * l2);
			int j3 = (centreY << 16) + (j2 * l2 - i2 * k2);
			int k3 = x + y * DrawingArea.width;
			for (y = 0; y < height; y++) {
				int l3 = k3;
				int i4 = i3;
				int j4 = j3;
				for (x = -width; x < 0; x++) {
					int k4 = pixels[(i4 >> 16) + (j4 >> 16) * this.width];
					if (k4 != 0)
						DrawingArea.pixels[l3++] = k4;
					else
						l3++;
					i4 += l2;
					j4 -= k2;
				}

				i3 += k2;
				j3 += l2;
				k3 += DrawingArea.width;
			}

		} catch (Exception _ex) {
		}
	}
	public void rotate(int height, int rotation, int widthMap[], int hingeSize,
			int ai1[], int centreY, int y, int x, int width, int centreX) {
		try {
			int negativeCentreX = -width / 2;
			int negativeCentreY = -height / 2;
			int offsetY = (int) (Math
					.sin(rotation / 326.11000000000001D) * 65536D);
			int offsetX = (int) (Math
					.cos(rotation / 326.11000000000001D) * 65536D);
			offsetY = offsetY * hingeSize >> 8;
			offsetX = offsetX * hingeSize >> 8;
			int j3 = (centreX << 16)
					+ (negativeCentreY * offsetY + negativeCentreX * offsetX);
			int k3 = (centreY << 16)
					+ (negativeCentreY * offsetX - negativeCentreX * offsetY);
			int l3 = x + y * DrawingArea.width;
			for (y = 0; y < height; y++) {
				int i4 = ai1[y];
				int j4 = l3 + i4;
				int k4 = j3 + offsetX * i4;
				int l4 = k3 - offsetY * i4;
				for (x = -widthMap[y]; x < 0; x++) {
					DrawingArea.pixels[j4++] = pixels[(k4 >> 16) + (l4 >> 16)
							* this.width];
					k4 += offsetX;
					l4 -= offsetY;
				}

				j3 += offsetY;
				k3 += offsetX;
				l3 += DrawingArea.width;
			}

		} catch (Exception _ex) {
		}
	}
	public void trim() {
		int targetPixels[] = new int[maxWidth * maxHeight];
		for (int _y = 0; _y < height; _y++) {
			System.arraycopy(pixels, _y * width, targetPixels, _y + offsetY
					* maxWidth + offsetX, width);
		}

		pixels = targetPixels;
		width = maxWidth;
		height = maxHeight;
		offsetX = 0;
		offsetY = 0;
	}
}
