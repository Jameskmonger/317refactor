package com.jagex.runescape;

public final class Background extends DrawingArea {

	public byte pixels[];

	public final int[] palette;

	public int width;

	public int height;

	public int drawOffsetX;

	public int drawOffsetY;

	public int resizeWidth;

	private int resizeHeight;

	public Background(Archive archive, String name, int id) {
		Buffer imageBuffer = new Buffer(archive.decompressFile(name + ".dat"));
		Buffer metadataBuffer = new Buffer(archive.decompressFile("index.dat"));

		metadataBuffer.position = imageBuffer.getUnsignedLEShort();
		resizeWidth = metadataBuffer.getUnsignedLEShort();
		resizeHeight = metadataBuffer.getUnsignedLEShort();
		
		int colourCount = metadataBuffer.getUnsignedByte();
		palette = new int[colourCount];
		for (int c = 0; c < colourCount - 1; c++)
			palette[c + 1] = metadataBuffer.get3Bytes();

		for (int i = 0; i < id; i++) {
			metadataBuffer.position += 2;
			imageBuffer.position += metadataBuffer.getUnsignedLEShort() * metadataBuffer.getUnsignedLEShort();
			metadataBuffer.position++;
		}

		drawOffsetX = metadataBuffer.getUnsignedByte();
		drawOffsetY = metadataBuffer.getUnsignedByte();
		width = metadataBuffer.getUnsignedLEShort();
		height = metadataBuffer.getUnsignedLEShort();
		int type = metadataBuffer.getUnsignedByte();
		int pixelCount = width * height;
		pixels = new byte[pixelCount];

		if (type == 0) {
			for (int i = 0; i < pixelCount; i++) {
				pixels[i] = imageBuffer.get();
			}
			return;
		}

		if (type == 1) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					pixels[x + y * width] = imageBuffer.get();
				}
			}
		}
	}

	public void draw(int x, int y) {
		x += drawOffsetX;
		y += drawOffsetY;
		int l = x + y * DrawingArea.width;
		int i1 = 0;
		int height = this.height;
		int width = this.width;
		int l1 = DrawingArea.width - width;
		int i2 = 0;
		if (y < DrawingArea.topY) {
			int j2 = DrawingArea.topY - y;
			height -= j2;
			y = DrawingArea.topY;
			i1 += j2 * width;
			l += j2 * DrawingArea.width;
		}
		if (y + height > DrawingArea.bottomY)
			height -= (y + height) - DrawingArea.bottomY;
		if (x < DrawingArea.topX) {
			int k2 = DrawingArea.topX - x;
			width -= k2;
			x = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (x + width > DrawingArea.bottomX) {
			int l2 = (x + width) - DrawingArea.bottomX;
			width -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(width <= 0 || height <= 0)) {
			draw(height, DrawingArea.pixels, pixels, l1, l, width, i1, palette, i2);
		}
	}

	public void resizeToHalf() {
		resizeWidth /= 2;
		resizeHeight /= 2;

		byte pixels[] = new byte[resizeWidth * resizeHeight];
		int i = 0;
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				pixels[(y + drawOffsetX >> 1) + (x + drawOffsetY >> 1) * resizeWidth] = pixels[i++];
			}
		}

		this.pixels = pixels;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void resize() {
		if (width == resizeWidth && height == resizeHeight)
			return;

		byte pixels[] = new byte[resizeWidth * resizeHeight];
		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + drawOffsetX + (y + drawOffsetY) * resizeWidth] = pixels[i++];
			}
		}

		this.pixels = pixels;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void flipHorizontally() {
		byte pixels[] = new byte[width * height];
		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = width - 1; x >= 0; x--) {
				pixels[i++] = pixels[x + y * width];
			}
		}

		this.pixels = pixels;
		drawOffsetX = resizeWidth - width - drawOffsetX;
	}

	public void flipVertically() {
		byte pixels[] = new byte[width * height];
		int i = 0;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				pixels[i++] = pixels[x + y * width];
			}
		}

		this.pixels = pixels;
		drawOffsetY = resizeHeight - height - drawOffsetY;
	}

	private void draw(int i, int pixels[], byte image[], int j, int k, int l, int i1, int palette[], int j1) {
		int k1 = -(l >> 2);
		l = -(l & 3);
		for (int l1 = -i; l1 < 0; l1++) {
			for (int i2 = k1; i2 < 0; i2++) {
				byte pixel = image[i1++];
				if (pixel != 0)
					pixels[k++] = palette[pixel & 0xff];
				else
					k++;
				pixel = image[i1++];
				if (pixel != 0)
					pixels[k++] = palette[pixel & 0xff];
				else
					k++;
				pixel = image[i1++];
				if (pixel != 0)
					pixels[k++] = palette[pixel & 0xff];
				else
					k++;
				pixel = image[i1++];
				if (pixel != 0)
					pixels[k++] = palette[pixel & 0xff];
				else
					k++;
			}

			for (int j2 = l; j2 < 0; j2++) {
				byte pixel = image[i1++];
				if (pixel != 0)
					pixels[k++] = palette[pixel & 0xff];
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
