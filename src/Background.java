// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Background extends DrawingArea {

    public Background(StreamLoader streamLoader, String s, int i)
    {
        Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
        Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
        stream_1.currentOffset = stream.getUnsignedLEShort();
        libWidth = stream_1.getUnsignedLEShort();
        anInt1457 = stream_1.getUnsignedLEShort();
        int j = stream_1.getUnsignedByte();
        palette = new int[j];
        for(int k = 0; k < j - 1; k++)
            palette[k + 1] = stream_1.read3Bytes();

        for(int l = 0; l < i; l++)
        {
            stream_1.currentOffset += 2;
            stream.currentOffset += stream_1.getUnsignedLEShort() * stream_1.getUnsignedLEShort();
            stream_1.currentOffset++;
        }

        anInt1454 = stream_1.getUnsignedByte();
        anInt1455 = stream_1.getUnsignedByte();
        imageWidth = stream_1.getUnsignedLEShort();
        anInt1453 = stream_1.getUnsignedLEShort();
        int i1 = stream_1.getUnsignedByte();
        int j1 = imageWidth * anInt1453;
        imagePixels = new byte[j1];
        if(i1 == 0)
        {
            for(int k1 = 0; k1 < j1; k1++)
                imagePixels[k1] = stream.get();

            return;
        }
        if(i1 == 1)
        {
            for(int l1 = 0; l1 < imageWidth; l1++)
            {
                for(int i2 = 0; i2 < anInt1453; i2++)
                    imagePixels[l1 + i2 * imageWidth] = stream.get();

            }

        }
    }

    public void method356()
    {
        libWidth /= 2;
        anInt1457 /= 2;
        byte abyte0[] = new byte[libWidth * anInt1457];
        int i = 0;
        for(int j = 0; j < anInt1453; j++)
        {
            for(int k = 0; k < imageWidth; k++)
                abyte0[(k + anInt1454 >> 1) + (j + anInt1455 >> 1) * libWidth] = imagePixels[i++];

        }

        imagePixels = abyte0;
        imageWidth = libWidth;
        anInt1453 = anInt1457;
        anInt1454 = 0;
            anInt1455 = 0;
    }

    public void method357()
    {
        if(imageWidth == libWidth && anInt1453 == anInt1457)
            return;
        byte abyte0[] = new byte[libWidth * anInt1457];
        int i = 0;
        for(int j = 0; j < anInt1453; j++)
        {
            for(int k = 0; k < imageWidth; k++)
                abyte0[k + anInt1454 + (j + anInt1455) * libWidth] = imagePixels[i++];

        }

        imagePixels = abyte0;
        imageWidth = libWidth;
        anInt1453 = anInt1457;
        anInt1454 = 0;
        anInt1455 = 0;
    }

    public void method358()
    {
        byte abyte0[] = new byte[imageWidth * anInt1453];
        int j = 0;
        for(int k = 0; k < anInt1453; k++)
        {
            for(int l = imageWidth - 1; l >= 0; l--)
                abyte0[j++] = imagePixels[l + k * imageWidth];

        }

        imagePixels = abyte0;
        anInt1454 = libWidth - imageWidth - anInt1454;
    }

    public void method359()
    {
        byte abyte0[] = new byte[imageWidth * anInt1453];
        int i = 0;
        for(int j = anInt1453 - 1; j >= 0; j--)
        {
            for(int k = 0; k < imageWidth; k++)
                abyte0[i++] = imagePixels[k + j * imageWidth];

        }

        imagePixels = abyte0;
        anInt1455 = anInt1457 - anInt1453 - anInt1455;
    }

    public void method360(int i, int j, int k)
    {
        for(int i1 = 0; i1 < palette.length; i1++)
        {
            int j1 = palette[i1] >> 16 & 0xff;
            j1 += i;
            if(j1 < 0)
                j1 = 0;
            else
            if(j1 > 255)
                j1 = 255;
            int k1 = palette[i1] >> 8 & 0xff;
            k1 += j;
            if(k1 < 0)
                k1 = 0;
            else
            if(k1 > 255)
                k1 = 255;
            int l1 = palette[i1] & 0xff;
            l1 += k;
            if(l1 < 0)
                l1 = 0;
            else
            if(l1 > 255)
                l1 = 255;
            palette[i1] = (j1 << 16) + (k1 << 8) + l1;
        }
    }

    public void method361(int i, int k)
    {
        i += anInt1454;
        k += anInt1455;
        int l = i + k * DrawingArea.width;
        int i1 = 0;
        int j1 = anInt1453;
        int k1 = imageWidth;
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
            method362(j1, DrawingArea.pixels, imagePixels, l1, l, k1, i1, palette, i2);
        }
    }

    private void method362(int i, int ai[], byte abyte0[], int j, int k, int l,
                           int i1, int ai1[], int j1)
    {
        int k1 = -(l >> 2);
        l = -(l & 3);
        for(int l1 = -i; l1 < 0; l1++)
        {
            for(int i2 = k1; i2 < 0; i2++)
            {
                byte byte1 = abyte0[i1++];
                if(byte1 != 0)
                    ai[k++] = ai1[byte1 & 0xff];
                else
                    k++;
                byte1 = abyte0[i1++];
                if(byte1 != 0)
                    ai[k++] = ai1[byte1 & 0xff];
                else
                    k++;
                byte1 = abyte0[i1++];
                if(byte1 != 0)
                    ai[k++] = ai1[byte1 & 0xff];
                else
                    k++;
                byte1 = abyte0[i1++];
                if(byte1 != 0)
                    ai[k++] = ai1[byte1 & 0xff];
                else
                    k++;
            }

            for(int j2 = l; j2 < 0; j2++)
            {
                byte byte2 = abyte0[i1++];
                if(byte2 != 0)
                    ai[k++] = ai1[byte2 & 0xff];
                else
                    k++;
            }

            k += j;
            i1 += j1;
        }

    }

    public byte imagePixels[];
    public final int[] palette;
    public int imageWidth;
    public int anInt1453;
    public int anInt1454;
    public int anInt1455;
    public int libWidth;
    private int anInt1457;
}
