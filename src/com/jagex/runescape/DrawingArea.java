package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
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

public class DrawingArea extends NodeSub {

    public static void initDrawingArea(int height, int width, int pixels[])
    {
        DrawingArea.pixels = pixels;
        DrawingArea.width = width;
        DrawingArea.height = height;
        setDrawingArea(height, 0, width, 0);
    }

    public static void defaultDrawingAreaSize()
    {
            topX = 0;
            topY = 0;
            bottomX = width;
            bottomY = height;
            centerX = bottomX - 1;
            viewportCentreX = bottomX / 2;
    }

    public static void setDrawingArea(int h, int j, int w, int l)
    {
        if(j < 0)
            j = 0;
        if(l < 0)
            l = 0;
        if(w > width)
            w = width;
        if(h > height)
            h = height;
        topX = j;
        topY = l;
        bottomX = w;
        bottomY = h;
        centerX = bottomX - 1;
        viewportCentreX = bottomX / 2;
        viewportCentreY = bottomY / 2;
    }

    public static void clear()
    {
        int i = width * height;
        for(int j = 0; j < i; j++)
            pixels[j] = 0;

    }

    public static void drawFilledRectangleAlpha(int i, int j, int k, int l, int i1, int k1)
    {
        if(k1 < topX)
        {
            k -= topX - k1;
            k1 = topX;
        }
        if(j < topY)
        {
            l -= topY - j;
            j = topY;
        }
        if(k1 + k > bottomX)
            k = bottomX - k1;
        if(j + l > bottomY)
            l = bottomY - j;
        int l1 = 256 - i1;
        int i2 = (i >> 16 & 0xff) * i1;
        int j2 = (i >> 8 & 0xff) * i1;
        int k2 = (i & 0xff) * i1;
        int k3 = width - k;
        int l3 = k1 + j * width;
        for(int i4 = 0; i4 < l; i4++)
        {
            for(int j4 = -k; j4 < 0; j4++)
            {
                int l2 = (pixels[l3] >> 16 & 0xff) * l1;
                int i3 = (pixels[l3] >> 8 & 0xff) * l1;
                int j3 = (pixels[l3] & 0xff) * l1;
                int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8) + (k2 + j3 >> 8);
                pixels[l3++] = k4;
            }

            l3 += k3;
        }
    }

    public static void drawFilledRectangle(int x, int y, int width, int height, int colour)
    {
        if(x < topX)
        {
            width -= topX - x;
            x = topX;
        }
        if(y < topY)
        {
            height -= topY - y;
            y = topY;
        }
        if(x + width > bottomX)
            width = bottomX - x;
        if(y + height > bottomY)
            height = bottomY - y;
        int increment = DrawingArea.width - width;
        int pointer = x + y * DrawingArea.width;
        for(int row = -height; row < 0; row++)
        {
            for(int column = -width; column < 0; column++)
                pixels[pointer++] = colour;

            pointer += increment;
        }

    }

    public static void drawUnfilledRectangle(int i, int j, int k, int l, int i1)
    {
        drawHorizontalLine(i1, i, j, l);
        drawHorizontalLine((i1 + k) - 1, i, j, l);
        drawVerticalLine(i, i1, k, l);
        drawVerticalLine((i + j) - 1, i1, k, l);
    }

    public static void drawUnfilledRectangleAlpha(int i, int j, int k, int l, int i1, int j1)
    {
        method340(l, i1, i, k, j1);
        method340(l, i1, (i + j) - 1, k, j1);
        if(j >= 3)
        {
            method342(l, j1, k, i + 1, j - 2);
            method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
        }
    }

    public static void drawHorizontalLine(int x, int y, int width, int colour)
    {
        if(x < topY || x >= bottomY)
            return;
        if(y < topX)
        {
            width -= topX - y;
            y = topX;
        }
        if(y + width > bottomX)
            width = bottomX - y;
        int pointer = y + x * DrawingArea.width;
        for(int column = 0; column < width; column++)
            pixels[pointer + column] = colour;

    }

    private static void method340(int i, int j, int k, int l, int i1)
    {
        if(k < topY || k >= bottomY)
            return;
        if(i1 < topX)
        {
            j -= topX - i1;
            i1 = topX;
        }
        if(i1 + j > bottomX)
            j = bottomX - i1;
        int j1 = 256 - l;
        int k1 = (i >> 16 & 0xff) * l;
        int l1 = (i >> 8 & 0xff) * l;
        int i2 = (i & 0xff) * l;
        int i3 = i1 + k * width;
        for(int j3 = 0; j3 < j; j3++)
        {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3++] = k3;
        }

    }

    public static void drawVerticalLine(int x, int y, int height, int colour)
    {
        if(x < topX || x >= bottomX)
            return;
        if(y < topY)
        {
            height -= topY - y;
            y = topY;
        }
        if(y + height > bottomY)
            height = bottomY - y;
        int pointer = x + y * DrawingArea.width;
        for(int row = 0; row < height; row++)
            pixels[pointer + row * DrawingArea.width] = colour;

    }

    private static void method342(int i, int j, int k, int l, int i1)
    {
        if(j < topX || j >= bottomX)
            return;
        if(l < topY)
        {
            i1 -= topY - l;
            l = topY;
        }
        if(l + i1 > bottomY)
            i1 = bottomY - l;
        int j1 = 256 - k;
        int k1 = (i >> 16 & 0xff) * k;
        int l1 = (i >> 8 & 0xff) * k;
        int i2 = (i & 0xff) * k;
        int i3 = j + l * width;
        for(int j3 = 0; j3 < i1; j3++)
        {
            int j2 = (pixels[i3] >> 16 & 0xff) * j1;
            int k2 = (pixels[i3] >> 8 & 0xff) * j1;
            int l2 = (pixels[i3] & 0xff) * j1;
            int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
            pixels[i3] = k3;
            i3 += width;
        }

    }

    DrawingArea()
    {
    }

    public static int pixels[];
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
