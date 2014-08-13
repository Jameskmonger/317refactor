package com.jagex.runescape;
public final class Flo {

    public static void unpackConfig(Archive archive)
    {
        Stream stream = new Stream(archive.getFile("flo.dat"));
        int cacheSize = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new Flo[cacheSize];
        for(int j = 0; j < cacheSize; j++)
        {
            if(cache[j] == null)
                cache[j] = new Flo();
            cache[j].readValues(stream);
        }

    }

    private void readValues(Stream stream)
    {
        do
        {
            int opcode = stream.getUnsignedByte();
            boolean dummy;
            if(opcode == 0)
                return;
            else
            if(opcode == 1)
            {
                colour2 = stream.read3Bytes();
                rgbToHls(colour2);
            } else
            if(opcode == 2)
                texture = stream.getUnsignedByte();
            else
            if(opcode == 3)
                dummy = true;
            else
            if(opcode == 5)
                occlude = false;
            else
            if(opcode == 6)
                stream.getString();
            else
            if(opcode == 7)
            {
                int h = hue;
                int s = saturation;
                int l = lightness;
                int h2 = hue2;
                int rgb = stream.read3Bytes();
                rgbToHls(rgb);
                hue = h;
                saturation = s;
                lightness = l;
                hue2 = h2;
                pCDivider = h2;
            } else
            {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while(true);
    }

    private void rgbToHls(int rgb)
    {
        double red = (double)(rgb >> 16 & 0xff) / 256D;
        double green = (double)(rgb >> 8 & 0xff) / 256D;
        double blue = (double)(rgb & 0xff) / 256D;
        double minC = red;
        if(green < minC)
            minC = green;
        if(blue < minC)
            minC = blue;
        double maxC = red;
        if(green > maxC)
            maxC = green;
        if(blue > maxC)
            maxC = blue;
        double h = 0.0D;
        double s = 0.0D;
        double l = (minC + maxC) / 2D;
        if(minC != maxC)
        {
            if(l < 0.5D)
                s = (maxC - minC) / (maxC + minC);
            if(l >= 0.5D)
                s = (maxC - minC) / (2D - maxC - minC);
            if(red == maxC)
                h = (green - blue) / (maxC - minC);
            else
            if(green == maxC)
                h = 2D + (blue - red) / (maxC - minC);
            else
            if(blue == maxC)
                h = 4D + (red - green) / (maxC - minC);
        }
        h /= 6D;
        hue = (int)(h * 256D);
        saturation = (int)(s * 256D);
        lightness = (int)(l * 256D);
        if(saturation < 0)
            saturation = 0;
        else
        if(saturation > 255)
            saturation = 255;
        if(lightness < 0)
            lightness = 0;
        else
        if(lightness > 255)
            lightness = 255;
        if(l > 0.5D)
            pCDivider = (int)((1.0D - l) * s * 512D);
        else
            pCDivider = (int)(l * s * 512D);
        if(pCDivider < 1)
            pCDivider = 1;
        hue2 = (int)(h * (double)pCDivider);
        int randomHue = (hue + (int)(Math.random() * 16D)) - 8;
        if(randomHue < 0)
            randomHue = 0;
        else
        if(randomHue > 255)
            randomHue = 255;
        int randomSaturation = (saturation + (int)(Math.random() * 48D)) - 24;
        if(randomSaturation < 0)
            randomSaturation = 0;
        else
        if(randomSaturation > 255)
            randomSaturation = 255;
        int randomLightness = (lightness + (int)(Math.random() * 48D)) - 24;
        if(randomLightness < 0)
            randomLightness = 0;
        else
        if(randomLightness > 255)
            randomLightness = 255;
        hsl = packHSL(randomHue, randomSaturation, randomLightness);
    }

    private int packHSL(int h, int s, int l)
    {
        if(l > 179)
            s /= 2;
        if(l > 192)
            s /= 2;
        if(l > 217)
            s /= 2;
        if(l > 243)
            s /= 2;
        return (h / 4 << 10) + (s / 32 << 7) + l / 2;
    }

    private Flo()
    {
        texture = -1;
        occlude = true;
    }

    public static Flo cache[];
    public int colour2;
    public int texture;
    public boolean occlude;
    public int hue;
    public int saturation;
    public int lightness;
    public int hue2;
    public int pCDivider;
    public int hsl;
}
