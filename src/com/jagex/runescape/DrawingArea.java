package com.jagex.runescape;

import com.jagex.runescape.collection.Cacheable;

public class DrawingArea extends Cacheable {

    public static void clear() {
        final int i = width * height;
        for (int j = 0; j < i; j++) {
            pixels[j] = 0;
        }

    }

    public static void defaultDrawingAreaSize() {
        topX = 0;
        topY = 0;
        bottomX = width;
        bottomY = height;
        centerX = bottomX - 1;
        viewportCentreX = bottomX / 2;
    }

    public static void drawFilledRectangle(int x, int y, int width, int height, final int colour) {
        if (x < topX) {
            width -= topX - x;
            x = topX;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (x + width > bottomX) {
            width = bottomX - x;
        }
        if (y + height > bottomY) {
            height = bottomY - y;
        }
        final int increment = DrawingArea.width - width;
        int pointer = x + y * DrawingArea.width;
        for (int row = -height; row < 0; row++) {
            for (int column = -width; column < 0; column++) {
                pixels[pointer++] = colour;
            }

            pointer += increment;
        }
    }

    public static void drawFilledCircle(final int x, final int y, final int radius, final int colour) {
        for (int _y = -radius; _y <= radius; _y++) {
            for (int _x = -radius; _x <= radius; _x++) {
                if (_x * _x + _y * _y <= radius * radius) {
                    drawHorizontalLine(_x + x, _y + y, 1, colour);
                }
            }
        }
    }

    public static void drawFilledCircleAlpha(final int x, final int y, final int radius, final int colour, final int alpha) {
        for (int _y = -radius; _y <= radius; _y++) {
            for (int _x = -radius; _x <= radius; _x++) {
                if (_x * _x + _y * _y <= radius * radius) {
                    drawHorizontalLineAlpha(_x + x, _y + y, 1, colour, alpha);
                }
            }
        }
    }

    public static void drawFilledRectangleAlpha(final int colour, int y, int width, int height, final int alpha, int x) {
        if (x < topX) {
            width -= topX - x;
            x = topX;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (x + width > bottomX) {
            width = bottomX - x;
        }
        if (y + height > bottomY) {
            height = bottomY - y;
        }
        final int l1 = 256 - alpha;
        final int i2 = (colour >> 16 & 0xff) * alpha;
        final int j2 = (colour >> 8 & 0xff) * alpha;
        final int k2 = (colour & 0xff) * alpha;
        final int k3 = DrawingArea.width - width;
        int l3 = x + y * DrawingArea.width;
        for (int i4 = 0; i4 < height; i4++) {
            for (int j4 = -width; j4 < 0; j4++) {
                final int l2 = (pixels[l3] >> 16 & 0xff) * l1;
                final int i3 = (pixels[l3] >> 8 & 0xff) * l1;
                final int j3 = (pixels[l3] & 0xff) * l1;
                final int packedRGB = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
                pixels[l3++] = packedRGB;
            }

            l3 += k3;
        }
    }

    public static void drawHorizontalLine(final int x, int y, int width, final int colour) {
        if (x < topY || x >= bottomY) {
            return;
        }
        if (y < topX) {
            width -= topX - y;
            y = topX;
        }
        if (y + width > bottomX) {
            width = bottomX - y;
        }
        final int pointer = y + x * DrawingArea.width;
        for (int column = 0; column < width; column++) {
            pixels[pointer + column] = colour;
        }
    }

    private static void drawHorizontalLineAlpha(final int x, int y, int width, final int colour, final int alpha) {
        if (x < topY || x >= bottomY) {
            return;
        }
        if (y < topX) {
            width -= topX - y;
            y = topX;
        }
        if (y + width > bottomX) {
            width = bottomX - y;
        }
        final int opacity = 256 - alpha;
        final int r = (colour >> 16 & 0xff) * alpha;
        final int g = (colour >> 8 & 0xff) * alpha;
        final int b = (colour & 0xff) * alpha;
        final int pointer = y + x * DrawingArea.width;
        for (int column = 0; column < width; column++) {
            final int rAlpha = (pixels[pointer + column] >> 16 & 0xff) * opacity;
            final int gAlpha = (pixels[pointer + column] >> 8 & 0xff) * opacity;
            final int bAlpha = (pixels[pointer + column] & 0xff) * opacity;
            final int packedRGB = ((r + rAlpha >> 8) << 16) + ((g + gAlpha >> 8) << 8) + (b + bAlpha >> 8);
            pixels[pointer + column] = packedRGB;
        }
    }

    public static void drawUnfilledRectangle(final int i, final int j, final int k, final int l, final int i1) {
        drawHorizontalLine(i1, i, j, l);
        drawHorizontalLine((i1 + k) - 1, i, j, l);
        drawVerticalLine(i, i1, k, l);
        drawVerticalLine((i + j) - 1, i1, k, l);
    }

    public static void drawUnfilledRectangleAlpha(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        method340(l, i1, i, k, j1);
        method340(l, i1, (i + j) - 1, k, j1);
        if (j >= 3) {
            method342(l, j1, k, i + 1, j - 2);
            method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
        }
    }

    public static void drawVerticalLine(final int x, int y, int height, final int colour) {
        if (x < topX || x >= bottomX) {
            return;
        }
        if (y < topY) {
            height -= topY - y;
            y = topY;
        }
        if (y + height > bottomY) {
            height = bottomY - y;
        }
        final int pointer = x + y * DrawingArea.width;
        for (int row = 0; row < height; row++) {
            pixels[pointer + row * DrawingArea.width] = colour;
        }

    }

    public static void initDrawingArea(final int height, final int width, final int[] pixels) {
        DrawingArea.pixels = pixels;
        DrawingArea.width = width;
        DrawingArea.height = height;
        setDrawingArea(height, 0, width, 0);
    }

    private static void method340(final int i, int j, final int k, final int l, int i1) {
        if (k < topY || k >= bottomY) {
            return;
        }
        if (i1 < topX) {
            j -= topX - i1;
            i1 = topX;
        }
        if (i1 + j > bottomX) {
            j = bottomX - i1;
        }
        final int j1 = 256 - l;
        final int k1 = (i >> 16 & 0xff) * l;
        final int l1 = (i >> 8 & 0xff) * l;
        final int i2 = (i & 0xff) * l;
        int i3 = i1 + k * width;
        for (int j3 = 0; j3 < j; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3++] = k3;
        }

    }

    private static void method342(final int i, final int j, final int k, int l, int i1) {
        if (j < topX || j >= bottomX) {
            return;
        }
        if (l < topY) {
            i1 -= topY - l;
            l = topY;
        }
        if (l + i1 > bottomY) {
            i1 = bottomY - l;
        }
        final int j1 = 256 - k;
        final int k1 = (i >> 16 & 0xff) * k;
        final int l1 = (i >> 8 & 0xff) * k;
        final int i2 = (i & 0xff) * k;
        int i3 = j + l * width;
        for (int j3 = 0; j3 < i1; j3++) {
            final int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            final int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            final int l2 = (pixels[i3] & 0xff) * j1;
            final int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3] = k3;
            i3 += width;
        }

    }

    public static void setDrawingArea(int h, int j, int w, int l) {
        if (j < 0) {
            j = 0;
        }
        if (l < 0) {
            l = 0;
        }
        if (w > width) {
            w = width;
        }
        if (h > height) {
            h = height;
        }
        topX = j;
        topY = l;
        bottomX = w;
        bottomY = h;
        centerX = bottomX - 1;
        viewportCentreX = bottomX / 2;
        viewportCentreY = bottomY / 2;
    }

    public static int[] pixels;

    public static int width;
    public static int height;
    public static int topY;
    public static int bottomY;
    public static int topX;
    public static int bottomX;
    public static int centerX;
    public static int viewportCentreX;
    public static int viewportCentreY;

}
