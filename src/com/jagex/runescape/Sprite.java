package com.jagex.runescape;

import java.awt.*;
import java.awt.image.PixelGrabber;

public final class Sprite extends DrawingArea {

    public int[] pixels;

    public int width;

    public int height;

    private int offsetX;

    private int offsetY;

    public int maxWidth;

    public int maxHeight;

    public Sprite(final Archive streamLoader, final String target, final int archiveId) {
        final Buffer dataStream = new Buffer(streamLoader.decompressFile(target + ".dat"));
        final Buffer indexStream = new Buffer(streamLoader.decompressFile("index.dat"));
        indexStream.position = dataStream.getUnsignedLEShort();
        this.maxWidth = indexStream.getUnsignedLEShort();
        this.maxHeight = indexStream.getUnsignedLEShort();
        final int length = indexStream.getUnsignedByte();
        final int[] pixels = new int[length];
        for (int p = 0; p < length - 1; p++) {
            pixels[p + 1] = indexStream.get3Bytes();
            if (pixels[p + 1] == 0) {
                pixels[p + 1] = 1;
            }
        }

        for (int i = 0; i < archiveId; i++) {
            indexStream.position += 2;
            dataStream.position += indexStream.getUnsignedLEShort() * indexStream.getUnsignedLEShort();
            indexStream.position++;
        }

        this.offsetX = indexStream.getUnsignedByte();
        this.offsetY = indexStream.getUnsignedByte();
        this.width = indexStream.getUnsignedLEShort();
        this.height = indexStream.getUnsignedLEShort();
        final int type = indexStream.getUnsignedByte();
        final int pixelCount = this.width * this.height;
        this.pixels = new int[pixelCount];
        if (type == 0) {
            for (int p = 0; p < pixelCount; p++) {
                this.pixels[p] = pixels[dataStream.getUnsignedByte()];
            }

            return;
        }
        if (type == 1) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixels[x + y * this.width] = pixels[dataStream.getUnsignedByte()];
                }

            }

        }
    }

    public Sprite(final byte[] abyte0, final Component component) {
        try {
            // Image image =
            // Toolkit.getDefaultToolkit().getImage(signlink.findcachedir()+"mopar.jpg");
            final Image image = Toolkit.getDefaultToolkit().createImage(abyte0);
            final MediaTracker mediatracker = new MediaTracker(component);
            mediatracker.addImage(image, 0);
            mediatracker.waitForAll();
            this.width = image.getWidth(component);
            this.height = image.getHeight(component);
            this.maxWidth = this.width;
            this.maxHeight = this.height;
            this.offsetX = 0;
            this.offsetY = 0;
            this.pixels = new int[this.width * this.height];
            final PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, this.width, this.height, this.pixels, 0, this.width);
            pixelgrabber.grabPixels();
        } catch (final Exception _ex) {
            System.out.println("Error converting jpg");
        }
    }

    public Sprite(final int i, final int j) {
        this.pixels = new int[i * j];
        this.width = this.maxWidth = i;
        this.height = this.maxHeight = j;
        this.offsetX = this.offsetY = 0;
    }

    public void adjustRGB(final int adjustmentR, final int adjustmentG, final int adjustmentB) {
        for (int pixel = 0; pixel < this.pixels.length; pixel++) {
            final int originalColour = this.pixels[pixel];
            if (originalColour != 0) {
                int red = originalColour >> 16 & 0xff;
                red += adjustmentR;
                if (red < 1) {
                    red = 1;
                } else if (red > 255) {
                    red = 255;
                }
                int green = originalColour >> 8 & 0xff;
                green += adjustmentG;
                if (green < 1) {
                    green = 1;
                } else if (green > 255) {
                    green = 255;
                }
                int blue = originalColour & 0xff;
                blue += adjustmentB;
                if (blue < 1) {
                    blue = 1;
                } else if (blue > 255) {
                    blue = 255;
                }
                this.pixels[pixel] = (red << 16) + (green << 8) + blue;
            }
        }

    }

    private void blockCopy(int destinationPointer, int copyLength, final int k, final int sourceBlockLength, int sourcePointer,
                           final int destinationBlockLength, final int[] source, final int[] destination) {
        final int blockCount = -(copyLength >> 2);
        copyLength = -(copyLength & 3);
        for (int i2 = -k; i2 < 0; i2++) {
            for (int ptr = blockCount; ptr < 0; ptr++) {
                destination[destinationPointer++] = source[sourcePointer++];
                destination[destinationPointer++] = source[sourcePointer++];
                destination[destinationPointer++] = source[sourcePointer++];
                destination[destinationPointer++] = source[sourcePointer++];
            }

            for (int ptr = copyLength; ptr < 0; ptr++) {
                destination[destinationPointer++] = source[sourcePointer++];
            }

            destinationPointer += destinationBlockLength;
            sourcePointer += sourceBlockLength;
        }
    }

    private void blockCopyAlpha(int sourcePointer, final int blockCount, final int[] destination, final int[] source,
                                final int sourceBlockLength, final int i1, final int destinationBlockLength, final int alpha, int destinationPointer) {
        int sourceValue;// was parameter
        final int destinationAlpha = 256 - alpha;
        for (int k2 = -i1; k2 < 0; k2++) {
            for (int ptr = -blockCount; ptr < 0; ptr++) {
                sourceValue = source[sourcePointer++];
                if (sourceValue != 0) {
                    final int destinationValue = destination[destinationPointer];
                    destination[destinationPointer++] = ((sourceValue & 0xff00ff) * alpha
                        + (destinationValue & 0xff00ff) * destinationAlpha & 0xff00ff00)
                        + ((sourceValue & 0xff00) * alpha + (destinationValue & 0xff00) * destinationAlpha
                        & 0xff0000) >> 8;
                } else {
                    destinationPointer++;
                }
            }

            destinationPointer += destinationBlockLength;
            sourcePointer += sourceBlockLength;
        }
    }

    private void blockCopyTransparent(final int[] destination, final int[] source, int sourcePointer, int destinationPointer,
                                      int copyLength, final int i1, final int destinationBlockLength, final int sourceBlockLength) {
        int value;// was parameter
        final int blockCount = -(copyLength >> 2);
        copyLength = -(copyLength & 3);
        for (int i2 = -i1; i2 < 0; i2++) {
            for (int ptr = blockCount; ptr < 0; ptr++) {
                value = source[sourcePointer++];
                if (value != 0) {
                    destination[destinationPointer++] = value;
                } else {
                    destinationPointer++;
                }
                value = source[sourcePointer++];
                if (value != 0) {
                    destination[destinationPointer++] = value;
                } else {
                    destinationPointer++;
                }
                value = source[sourcePointer++];
                if (value != 0) {
                    destination[destinationPointer++] = value;
                } else {
                    destinationPointer++;
                }
                value = source[sourcePointer++];
                if (value != 0) {
                    destination[destinationPointer++] = value;
                } else {
                    destinationPointer++;
                }
            }

            for (int ptr = copyLength; ptr < 0; ptr++) {
                value = source[sourcePointer++];
                if (value != 0) {
                    destination[destinationPointer++] = value;
                } else {
                    destinationPointer++;
                }
            }

            destinationPointer += destinationBlockLength;
            sourcePointer += sourceBlockLength;
        }

    }

    public void drawImage(int x, int y) {
        x += this.offsetX;
        y += this.offsetY;
        int destinationOffset = x + y * DrawingArea.width;
        int sourceOffset = 0;
        int rowCount = this.height;
        int columnCount = this.width;
        int lineDestinationOffset = DrawingArea.width - columnCount;
        int lineSourceOffset = 0;
        if (y < DrawingArea.topY) {
            final int clipHeight = DrawingArea.topY - y;
            rowCount -= clipHeight;
            y = DrawingArea.topY;
            sourceOffset += clipHeight * columnCount;
            destinationOffset += clipHeight * DrawingArea.width;
        }
        if (y + rowCount > DrawingArea.bottomY) {
            rowCount -= (y + rowCount) - DrawingArea.bottomY;
        }
        if (x < DrawingArea.topX) {
            final int clipWidth = DrawingArea.topX - x;
            columnCount -= clipWidth;
            x = DrawingArea.topX;
            sourceOffset += clipWidth;
            destinationOffset += clipWidth;
            lineSourceOffset += clipWidth;
            lineDestinationOffset += clipWidth;
        }
        if (x + columnCount > DrawingArea.bottomX) {
            final int clipWidth = (x + columnCount) - DrawingArea.bottomX;
            columnCount -= clipWidth;
            lineSourceOffset += clipWidth;
            lineDestinationOffset += clipWidth;
        }
        if (!(columnCount <= 0 || rowCount <= 0)) {
            this.blockCopyTransparent(DrawingArea.pixels, this.pixels, sourceOffset, destinationOffset, columnCount, rowCount,
                lineDestinationOffset, lineSourceOffset);
        }
    }

    public void drawImageAlpha(int i, int j) {
        final int k = 128;// was parameter
        i += this.offsetX;
        j += this.offsetY;
        int i1 = i + j * DrawingArea.width;
        int j1 = 0;
        int k1 = this.height;
        int l1 = this.width;
        int i2 = DrawingArea.width - l1;
        int j2 = 0;
        if (j < DrawingArea.topY) {
            final int k2 = DrawingArea.topY - j;
            k1 -= k2;
            j = DrawingArea.topY;
            j1 += k2 * l1;
            i1 += k2 * DrawingArea.width;
        }
        if (j + k1 > DrawingArea.bottomY) {
            k1 -= (j + k1) - DrawingArea.bottomY;
        }
        if (i < DrawingArea.topX) {
            final int l2 = DrawingArea.topX - i;
            l1 -= l2;
            i = DrawingArea.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if (i + l1 > DrawingArea.bottomX) {
            final int i3 = (i + l1) - DrawingArea.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if (!(l1 <= 0 || k1 <= 0)) {
            this.blockCopyAlpha(j1, l1, DrawingArea.pixels, this.pixels, j2, k1, i2, k, i1);
        }
    }

    public void drawInverse(int i, int j) {
        i += this.offsetX;
        j += this.offsetY;
        int l = i + j * DrawingArea.width;
        int i1 = 0;
        int j1 = this.height;
        int k1 = this.width;
        int l1 = DrawingArea.width - k1;
        int i2 = 0;
        if (j < DrawingArea.topY) {
            final int j2 = DrawingArea.topY - j;
            j1 -= j2;
            j = DrawingArea.topY;
            i1 += j2 * k1;
            l += j2 * DrawingArea.width;
        }
        if (j + j1 > DrawingArea.bottomY) {
            j1 -= (j + j1) - DrawingArea.bottomY;
        }
        if (i < DrawingArea.topX) {
            final int k2 = DrawingArea.topX - i;
            k1 -= k2;
            i = DrawingArea.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (i + k1 > DrawingArea.bottomX) {
            final int l2 = (i + k1) - DrawingArea.bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (k1 <= 0 || j1 <= 0) {
        } else {
            this.blockCopy(l, k1, j1, i2, i1, l1, this.pixels, DrawingArea.pixels);
        }
    }

    public void initDrawingArea() {
        DrawingArea.initDrawingArea(this.height, this.width, this.pixels);
    }

    public void method354(final IndexedImage background, int x, int y) {
        y += this.offsetX;
        x += this.offsetY;
        int k = y + x * DrawingArea.width;
        int l = 0;
        int i1 = this.height;
        int j1 = this.width;
        int k1 = DrawingArea.width - j1;
        int l1 = 0;
        if (x < DrawingArea.topY) {
            final int i2 = DrawingArea.topY - x;
            i1 -= i2;
            x = DrawingArea.topY;
            l += i2 * j1;
            k += i2 * DrawingArea.width;
        }
        if (x + i1 > DrawingArea.bottomY) {
            i1 -= (x + i1) - DrawingArea.bottomY;
        }
        if (y < DrawingArea.topX) {
            final int j2 = DrawingArea.topX - y;
            j1 -= j2;
            y = DrawingArea.topX;
            l += j2;
            k += j2;
            l1 += j2;
            k1 += j2;
        }
        if (y + j1 > DrawingArea.bottomX) {
            final int k2 = (y + j1) - DrawingArea.bottomX;
            j1 -= k2;
            l1 += k2;
            k1 += k2;
        }
        if (!(j1 <= 0 || i1 <= 0)) {
            this.method355(this.pixels, j1, background.pixels, i1, DrawingArea.pixels, 0, k1, k, l1, l);
        }
    }

    private void method355(final int[] ai, int i, final byte[] abyte0, final int j, final int[] ai1, int k, final int l, int i1, final int j1, int k1) {
        final int l1 = -(i >> 2);
        i = -(i & 3);
        for (int j2 = -j; j2 < 0; j2++) {
            for (int k2 = l1; k2 < 0; k2++) {
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
            }

            for (int l2 = i; l2 < 0; l2++) {
                k = ai[k1++];
                if (k != 0 && abyte0[i1] == 0) {
                    ai1[i1++] = k;
                } else {
                    i1++;
                }
            }

            i1 += l;
            k1 += j1;
        }

    }

    public void rotate(int x, int y, final double rotation) {
        // all of the following were parameters
        final int centreY = 15;
        final int width = 20;
        final int centreX = 15;
        final int hingeSize = 256;
        final int height = 20;
        // all of the previous were parameters
        try {
            final int i2 = -width / 2;
            final int j2 = -height / 2;
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
                    final int k4 = this.pixels[(i4 >> 16) + (j4 >> 16) * this.width];
                    if (k4 != 0) {
                        DrawingArea.pixels[l3++] = k4;
                    } else {
                        l3++;
                    }
                    i4 += l2;
                    j4 -= k2;
                }

                i3 += k2;
                j3 += l2;
                k3 += DrawingArea.width;
            }

        } catch (final Exception _ex) {
        }
    }

    public void rotate(final int height, final int rotation, final int[] widthMap, final int hingeSize, final int[] ai1, final int centreY, int y, int x,
                       final int width, final int centreX) {
        try {
            final int negativeCentreX = -width / 2;
            final int negativeCentreY = -height / 2;
            int offsetY = (int) (Math.sin(rotation / 326.11000000000001D) * 65536D);
            int offsetX = (int) (Math.cos(rotation / 326.11000000000001D) * 65536D);
            offsetY = offsetY * hingeSize >> 8;
            offsetX = offsetX * hingeSize >> 8;
            int j3 = (centreX << 16) + (negativeCentreY * offsetY + negativeCentreX * offsetX);
            int k3 = (centreY << 16) + (negativeCentreY * offsetX - negativeCentreX * offsetY);
            int l3 = x + y * DrawingArea.width;
            for (y = 0; y < height; y++) {
                final int i4 = ai1[y];
                int j4 = l3 + i4;
                int k4 = j3 + offsetX * i4;
                int l4 = k3 - offsetY * i4;
                for (x = -widthMap[y]; x < 0; x++) {
                    DrawingArea.pixels[j4++] = this.pixels[(k4 >> 16) + (l4 >> 16) * this.width];
                    k4 += offsetX;
                    l4 -= offsetY;
                }

                j3 += offsetY;
                k3 += offsetX;
                l3 += DrawingArea.width;
            }

        } catch (final Exception _ex) {
        }
    }

    public void trim() {
        final int[] targetPixels = new int[this.maxWidth * this.maxHeight];
        for (int _y = 0; _y < this.height; _y++) {
            System.arraycopy(this.pixels, _y * this.width, targetPixels, _y + this.offsetY * this.maxWidth + this.offsetX, this.width);
        }

        this.pixels = targetPixels;
        this.width = this.maxWidth;
        this.height = this.maxHeight;
        this.offsetX = 0;
        this.offsetY = 0;
    }
}
