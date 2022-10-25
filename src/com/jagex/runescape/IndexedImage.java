package com.jagex.runescape;

public final class IndexedImage extends DrawingArea {

    public byte[] pixels;

    public final int[] palette;

    public int width;

    public int height;

    public int drawOffsetX;

    public int drawOffsetY;

    public int resizeWidth;

    private int resizeHeight;

    public IndexedImage(final Archive archive, final String name, final int id) {
        final Buffer imageBuffer = new Buffer(archive.decompressFile(name + ".dat"));
        final Buffer metadataBuffer = new Buffer(archive.decompressFile("index.dat"));

        metadataBuffer.position = imageBuffer.getUnsignedLEShort();
        this.resizeWidth = metadataBuffer.getUnsignedLEShort();
        this.resizeHeight = metadataBuffer.getUnsignedLEShort();

        final int colourCount = metadataBuffer.getUnsignedByte();
        this.palette = new int[colourCount];
        for (int c = 0; c < colourCount - 1; c++) {
            this.palette[c + 1] = metadataBuffer.get3Bytes();
        }

        for (int i = 0; i < id; i++) {
            metadataBuffer.position += 2;
            imageBuffer.position += metadataBuffer.getUnsignedLEShort() * metadataBuffer.getUnsignedLEShort();
            metadataBuffer.position++;
        }

        this.drawOffsetX = metadataBuffer.getUnsignedByte();
        this.drawOffsetY = metadataBuffer.getUnsignedByte();
        this.width = metadataBuffer.getUnsignedLEShort();
        this.height = metadataBuffer.getUnsignedLEShort();
        final int type = metadataBuffer.getUnsignedByte();
        final int pixelCount = this.width * this.height;
        this.pixels = new byte[pixelCount];

        if (type == 0) {
            for (int i = 0; i < pixelCount; i++) {
                this.pixels[i] = imageBuffer.get();
            }
            return;
        }

        if (type == 1) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixels[x + y * this.width] = imageBuffer.get();
                }
            }
        }
    }

    public void draw(int x, int y) {
        x += this.drawOffsetX;
        y += this.drawOffsetY;
        int l = x + y * DrawingArea.width;
        int i1 = 0;
        int height = this.height;
        int width = this.width;
        int l1 = DrawingArea.width - width;
        int i2 = 0;
        if (y < DrawingArea.topY) {
            final int j2 = DrawingArea.topY - y;
            height -= j2;
            y = DrawingArea.topY;
            i1 += j2 * width;
            l += j2 * DrawingArea.width;
        }
        if (y + height > DrawingArea.bottomY) {
            height -= (y + height) - DrawingArea.bottomY;
        }
        if (x < DrawingArea.topX) {
            final int k2 = DrawingArea.topX - x;
            width -= k2;
            x = DrawingArea.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if (x + width > DrawingArea.bottomX) {
            final int l2 = (x + width) - DrawingArea.bottomX;
            width -= l2;
            i2 += l2;
            l1 += l2;
        }
        if (!(width <= 0 || height <= 0)) {
            this.draw(height, DrawingArea.pixels, this.pixels, l1, l, width, i1, this.palette, i2);
        }
    }

    public void resizeToHalf() {
        this.resizeWidth /= 2;
        this.resizeHeight /= 2;

        final byte[] newPixels = new byte[this.resizeWidth * this.resizeHeight];
        int i = 0;
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                newPixels[(y + this.drawOffsetX >> 1) + (x + this.drawOffsetY >> 1) * this.resizeWidth] = pixels[i++];
            }
        }

        this.pixels = newPixels;
        this.width = this.resizeWidth;
        this.height = this.resizeHeight;
        this.drawOffsetX = 0;
        this.drawOffsetY = 0;
    }

    public void resize() {
        if (this.width == this.resizeWidth && this.height == this.resizeHeight) {
            return;
        }

        final byte[] newPixels = new byte[this.resizeWidth * this.resizeHeight];
        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                newPixels[x + this.drawOffsetX + (y + this.drawOffsetY) * this.resizeWidth] = pixels[i++];
            }
        }

        this.pixels = newPixels;
        this.width = this.resizeWidth;
        this.height = this.resizeHeight;
        this.drawOffsetX = 0;
        this.drawOffsetY = 0;
    }

    public void flipHorizontally() {
        final byte[] newPixels = new byte[this.width * this.height];
        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = this.width - 1; x >= 0; x--) {
                newPixels[i++] = pixels[x + y * this.width];
            }
        }

        this.pixels = newPixels;
        this.drawOffsetX = this.resizeWidth - this.width - this.drawOffsetX;
    }

    public void flipVertically() {
        final byte[] newPixels = new byte[this.width * this.height];
        int i = 0;
        for (int y = this.height - 1; y >= 0; y--) {
            for (int x = 0; x < this.width; x++) {
                newPixels[i++] = pixels[x + y * this.width];
            }
        }

        this.pixels = newPixels;
        this.drawOffsetY = this.resizeHeight - this.height - this.drawOffsetY;
    }

    private void draw(final int i, final int[] pixels, final byte[] image, final int j, int k, int l, int i1, final int[] palette, final int j1) {
        final int k1 = -(l >> 2);
        l = -(l & 3);
        for (int l1 = -i; l1 < 0; l1++) {
            for (int i2 = k1; i2 < 0; i2++) {
                byte pixel = image[i1++];
                if (pixel != 0) {
                    pixels[k++] = palette[pixel & 0xff];
                } else {
                    k++;
                }
                pixel = image[i1++];
                if (pixel != 0) {
                    pixels[k++] = palette[pixel & 0xff];
                } else {
                    k++;
                }
                pixel = image[i1++];
                if (pixel != 0) {
                    pixels[k++] = palette[pixel & 0xff];
                } else {
                    k++;
                }
                pixel = image[i1++];
                if (pixel != 0) {
                    pixels[k++] = palette[pixel & 0xff];
                } else {
                    k++;
                }
            }

            for (int j2 = l; j2 < 0; j2++) {
                final byte pixel = image[i1++];
                if (pixel != 0) {
                    pixels[k++] = palette[pixel & 0xff];
                } else {
                    k++;
                }
            }

            k += j;
            i1 += j1;
        }

    }

    public void mixPalette(final int red, final int green, final int blue) {
        for (int i = 0; i < this.palette.length; i++) {
            int r = this.palette[i] >> 16 & 0xff;
            r += red;
            if (r < 0) {
                r = 0;
            } else if (r > 255) {
                r = 255;
            }
            int g = this.palette[i] >> 8 & 0xff;
            g += green;
            if (g < 0) {
                g = 0;
            } else if (g > 255) {
                g = 255;
            }
            int b = this.palette[i] & 0xff;
            b += blue;
            if (b < 0) {
                b = 0;
            } else if (b > 255) {
                b = 255;
            }
            this.palette[i] = (r << 16) + (g << 8) + b;
        }
    }
}
