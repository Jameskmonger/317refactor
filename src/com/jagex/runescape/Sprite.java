package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.awt.*;
import java.awt.image.PixelGrabber;

public final class Sprite extends DrawingArea {

    public Sprite(int i, int j)
    {
        pixels = new int[i * j];
        width = maxWidth = i;
        height = maxHeight = j;
        offsetX = offsetY = 0;
    }

    public Sprite(byte abyte0[], Component component)
    {
        try
        {
//            Image image = Toolkit.getDefaultToolkit().getImage(signlink.findcachedir()+"mopar.jpg");
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
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pixelgrabber.grabPixels();
        }
        catch(Exception _ex)
        {
            System.out.println("Error converting jpg");
        }
    }

    public Sprite(Archive streamLoader, String target, int archiveId)
    {
        Stream dataStream = new Stream(streamLoader.getFile(target + ".dat"));
        Stream indexStream = new Stream(streamLoader.getFile("index.dat"));
        indexStream.currentOffset = dataStream.getUnsignedLEShort();
        maxWidth = indexStream.getUnsignedLEShort();
        maxHeight = indexStream.getUnsignedLEShort();
        int length = indexStream.getUnsignedByte();
        int pixels[] = new int[length];
        for(int p = 0; p < length - 1; p++)
        {
            pixels[p + 1] = indexStream.get24BitInt();
            if(pixels[p + 1] == 0)
                pixels[p + 1] = 1;
        }

        for(int i = 0; i < archiveId; i++)
        {
            indexStream.currentOffset += 2;
            dataStream.currentOffset += indexStream.getUnsignedLEShort() * indexStream.getUnsignedLEShort();
            indexStream.currentOffset++;
        }

        offsetX = indexStream.getUnsignedByte();
        offsetY = indexStream.getUnsignedByte();
        width = indexStream.getUnsignedLEShort();
        height = indexStream.getUnsignedLEShort();
        int type = indexStream.getUnsignedByte();
        int pixelCount = width * height;
        this.pixels = new int[pixelCount];
        if(type == 0)
        {
            for(int p = 0; p < pixelCount; p++)
                this.pixels[p] = pixels[dataStream.getUnsignedByte()];

            return;
        }
        if(type == 1)
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                    this.pixels[x + y * width] = pixels[dataStream.getUnsignedByte()];

            }

        }
    }

    public void initDrawingArea()
    {
        DrawingArea.initDrawingArea(height, width, pixels);
    }

    public void adjustRGB(int adjustmentR, int adjustmentG, int adjustmentB)
    {
        for(int pixel = 0; pixel < pixels.length; pixel++)
        {
            int originalColour = pixels[pixel];
            if(originalColour != 0)
            {
                int red = originalColour >> 16 & 0xff;
                red += adjustmentR;
                if(red < 1)
                    red = 1;
                else
                if(red > 255)
                    red = 255;
                int green = originalColour >> 8 & 0xff;
                green += adjustmentG;
                if(green < 1)
                    green = 1;
                else
                if(green > 255)
                    green = 255;
                int blue = originalColour & 0xff;
                blue += adjustmentB;
                if(blue < 1)
                    blue = 1;
                else
                if(blue > 255)
                    blue = 255;
                pixels[pixel] = (red << 16) + (green << 8) + blue;
            }
        }

    }

    public void trim()
    {
        int ai[] = new int[maxWidth * maxHeight];
        for(int j = 0; j < height; j++)
        {
            System.arraycopy(pixels, j * width, ai, j + offsetY * maxWidth + offsetX, width);
        }

        pixels = ai;
        width = maxWidth;
        height = maxHeight;
        offsetX = 0;
        offsetY = 0;
    }

    public void drawInverse(int i, int j)
    {
        i += offsetX;
        j += offsetY;
        int l = i + j * DrawingArea.width;
        int i1 = 0;
        int j1 = height;
        int k1 = width;
        int l1 = DrawingArea.width - k1;
        int i2 = 0;
        if(j < DrawingArea.topY)
        {
            int j2 = DrawingArea.topY - j;
            j1 -= j2;
            j = DrawingArea.topY;
            i1 += j2 * k1;
            l += j2 * DrawingArea.width;
        }
        if(j + j1 > DrawingArea.bottomY)
            j1 -= (j + j1) - DrawingArea.bottomY;
        if(i < DrawingArea.topX)
        {
            int k2 = DrawingArea.topX - i;
            k1 -= k2;
            i = DrawingArea.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if(i + k1 > DrawingArea.bottomX)
        {
            int l2 = (i + k1) - DrawingArea.bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if(k1 <= 0 || j1 <= 0)
        {
        } else
        {
            method347(l, k1, j1, i2, i1, l1, pixels, DrawingArea.pixels);
        }
    }

    private void method347(int i, int j, int k, int l, int i1, int k1,
                           int ai[], int ai1[])
    {
        int l1 = -(j >> 2);
        j = -(j & 3);
        for(int i2 = -k; i2 < 0; i2++)
        {
            for(int j2 = l1; j2 < 0; j2++)
            {
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
                ai1[i++] = ai[i1++];
            }

            for(int k2 = j; k2 < 0; k2++)
                ai1[i++] = ai[i1++];

            i += k1;
            i1 += l;
        }
    }

    public void drawAlpha(int i, int j)
    {
        int k = 128;//was parameter
        i += offsetX;
        j += offsetY;
        int i1 = i + j * DrawingArea.width;
        int j1 = 0;
        int k1 = height;
        int l1 = width;
        int i2 = DrawingArea.width - l1;
        int j2 = 0;
        if(j < DrawingArea.topY)
        {
            int k2 = DrawingArea.topY - j;
            k1 -= k2;
            j = DrawingArea.topY;
            j1 += k2 * l1;
            i1 += k2 * DrawingArea.width;
        }
        if(j + k1 > DrawingArea.bottomY)
            k1 -= (j + k1) - DrawingArea.bottomY;
        if(i < DrawingArea.topX)
        {
            int l2 = DrawingArea.topX - i;
            l1 -= l2;
            i = DrawingArea.topX;
            j1 += l2;
            i1 += l2;
            j2 += l2;
            i2 += l2;
        }
        if(i + l1 > DrawingArea.bottomX)
        {
            int i3 = (i + l1) - DrawingArea.bottomX;
            l1 -= i3;
            j2 += i3;
            i2 += i3;
        }
        if(!(l1 <= 0 || k1 <= 0))
        {
            method351(j1, l1, DrawingArea.pixels, pixels, j2, k1, i2, k, i1);
        }
    }

    public void drawSprite(int i, int k)
    {
        i += offsetX;
        k += offsetY;
        int l = i + k * DrawingArea.width;
        int i1 = 0;
        int j1 = height;
        int k1 = width;
        int l1 = DrawingArea.width - k1;
        int i2 = 0;
        if(k < DrawingArea.topY)
        {
            int j2 = DrawingArea.topY - k;
            j1 -= j2;
            k = DrawingArea.topY;
            i1 += j2 * k1;
            l += j2 * DrawingArea.width;
        }
        if(k + j1 > DrawingArea.bottomY)
            j1 -= (k + j1) - DrawingArea.bottomY;
        if(i < DrawingArea.topX)
        {
            int k2 = DrawingArea.topX - i;
            k1 -= k2;
            i = DrawingArea.topX;
            i1 += k2;
            l += k2;
            i2 += k2;
            l1 += k2;
        }
        if(i + k1 > DrawingArea.bottomX)
        {
            int l2 = (i + k1) - DrawingArea.bottomX;
            k1 -= l2;
            i2 += l2;
            l1 += l2;
        }
        if(!(k1 <= 0 || j1 <= 0))
        {
            method349(DrawingArea.pixels, pixels, i1, l, k1, j1, l1, i2);
        }
    }

    private void method349(int ai[], int ai1[], int j, int k, int l, int i1,
                           int j1, int k1)
    {
        int i;//was parameter
        int l1 = -(l >> 2);
        l = -(l & 3);
        for(int i2 = -i1; i2 < 0; i2++)
        {
            for(int j2 = l1; j2 < 0; j2++)
            {
                i = ai1[j++];
                if(i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if(i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if(i != 0)
                    ai[k++] = i;
                else
                    k++;
                i = ai1[j++];
                if(i != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            for(int k2 = l; k2 < 0; k2++)
            {
                i = ai1[j++];
                if(i != 0)
                    ai[k++] = i;
                else
                    k++;
            }

            k += j1;
            j += k1;
        }

    }

    private void method351(int i, int j, int ai[], int ai1[], int l, int i1,
                           int j1, int k1, int l1)
    {
        int k;//was parameter
        int j2 = 256 - k1;
        for(int k2 = -i1; k2 < 0; k2++)
        {
            for(int l2 = -j; l2 < 0; l2++)
            {
                k = ai1[i++];
                if(k != 0)
                {
                    int i3 = ai[l1];
                    ai[l1++] = ((k & 0xff00ff) * k1 + (i3 & 0xff00ff) * j2 & 0xff00ff00) + ((k & 0xff00) * k1 + (i3 & 0xff00) * j2 & 0xff0000) >> 8;
                } else
                {
                    l1++;
                }
            }

            l1 += j1;
            i += l;
        }
    }

    public void rotate(int i, int j, int ai[], int k, int ai1[], int i1,
                          int j1, int k1, int l1, int i2)
    {
        try
        {
            int j2 = -l1 / 2;
            int k2 = -i / 2;
            int l2 = (int)(Math.sin((double)j / 326.11000000000001D) * 65536D);
            int i3 = (int)(Math.cos((double)j / 326.11000000000001D) * 65536D);
            l2 = l2 * k >> 8;
            i3 = i3 * k >> 8;
            int j3 = (i2 << 16) + (k2 * l2 + j2 * i3);
            int k3 = (i1 << 16) + (k2 * i3 - j2 * l2);
            int l3 = k1 + j1 * DrawingArea.width;
            for(j1 = 0; j1 < i; j1++)
            {
                int i4 = ai1[j1];
                int j4 = l3 + i4;
                int k4 = j3 + i3 * i4;
                int l4 = k3 - l2 * i4;
                for(k1 = -ai[j1]; k1 < 0; k1++)
                {
                    DrawingArea.pixels[j4++] = pixels[(k4 >> 16) + (l4 >> 16) * width];
                    k4 += i3;
                    l4 -= l2;
                }

                j3 += l2;
                k3 += i3;
                l3 += DrawingArea.width;
            }

        }
        catch(Exception _ex)
        {
        }
    }

    public void rotate(int i,
                          double d, int l1)
    {
        //all of the following were parameters
        int j = 15;
        int k = 20;
        int l = 15;
        int j1 = 256;
        int k1 = 20;
        //all of the previous were parameters
        try
        {
            int i2 = -k / 2;
            int j2 = -k1 / 2;
            int k2 = (int)(Math.sin(d) * 65536D);
            int l2 = (int)(Math.cos(d) * 65536D);
            k2 = k2 * j1 >> 8;
            l2 = l2 * j1 >> 8;
            int i3 = (l << 16) + (j2 * k2 + i2 * l2);
            int j3 = (j << 16) + (j2 * l2 - i2 * k2);
            int k3 = l1 + i * DrawingArea.width;
            for(i = 0; i < k1; i++)
            {
                int l3 = k3;
                int i4 = i3;
                int j4 = j3;
                for(l1 = -k; l1 < 0; l1++)
                {
                    int k4 = pixels[(i4 >> 16) + (j4 >> 16) * width];
                    if(k4 != 0)
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

        }
        catch(Exception _ex)
        {
        }
    }

    public void method354(Background background, int i, int j)
    {
        j += offsetX;
        i += offsetY;
        int k = j + i * DrawingArea.width;
        int l = 0;
        int i1 = height;
        int j1 = width;
        int k1 = DrawingArea.width - j1;
        int l1 = 0;
        if(i < DrawingArea.topY)
        {
            int i2 = DrawingArea.topY - i;
            i1 -= i2;
            i = DrawingArea.topY;
            l += i2 * j1;
            k += i2 * DrawingArea.width;
        }
        if(i + i1 > DrawingArea.bottomY)
            i1 -= (i + i1) - DrawingArea.bottomY;
        if(j < DrawingArea.topX)
        {
            int j2 = DrawingArea.topX - j;
            j1 -= j2;
            j = DrawingArea.topX;
            l += j2;
            k += j2;
            l1 += j2;
            k1 += j2;
        }
        if(j + j1 > DrawingArea.bottomX)
        {
            int k2 = (j + j1) - DrawingArea.bottomX;
            j1 -= k2;
            l1 += k2;
            k1 += k2;
        }
        if(!(j1 <= 0 || i1 <= 0))
        {
            method355(pixels, j1, background.imagePixels, i1, DrawingArea.pixels, 0, k1, k, l1, l);
        }
    }

    private void method355(int ai[], int i, byte abyte0[], int j, int ai1[], int k,
                           int l, int i1, int j1, int k1)
    {
        int l1 = -(i >> 2);
        i = -(i & 3);
        for(int j2 = -j; j2 < 0; j2++)
        {
            for(int k2 = l1; k2 < 0; k2++)
            {
                k = ai[k1++];
                if(k != 0 && abyte0[i1] == 0)
                    ai1[i1++] = k;
                else
                    i1++;
                k = ai[k1++];
                if(k != 0 && abyte0[i1] == 0)
                    ai1[i1++] = k;
                else
                    i1++;
                k = ai[k1++];
                if(k != 0 && abyte0[i1] == 0)
                    ai1[i1++] = k;
                else
                    i1++;
                k = ai[k1++];
                if(k != 0 && abyte0[i1] == 0)
                    ai1[i1++] = k;
                else
                    i1++;
            }

            for(int l2 = i; l2 < 0; l2++)
            {
                k = ai[k1++];
                if(k != 0 && abyte0[i1] == 0)
                    ai1[i1++] = k;
                else
                    i1++;
            }

            i1 += l;
            k1 += j1;
        }

    }

    public int pixels[];
    public int width;
    public int height;
    private int offsetX;
    private int offsetY;
    public int maxWidth;
    public int maxHeight;
}
