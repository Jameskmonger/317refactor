import java.util.Random;

public final class TextDrawingArea extends DrawingArea {

    public TextDrawingArea(boolean flag, String s, StreamLoader streamLoader)
    {
        glyphPixels = new byte[256][];
        glyphWidth = new int[256];
        glyphHeight = new int[256];
        horizontalKerning = new int[256];
        verticalKerning = new int[256];
        characterEffectiveWidth = new int[256];
        random = new Random();
        strikethrough = false;
        Stream data = new Stream(streamLoader.getDataForName(s + ".dat"));
        Stream index = new Stream(streamLoader.getDataForName("index.dat"));
        index.currentOffset = data.getUnsignedLEShort() + 4;
        int k = index.getUnsignedByte();
        if(k > 0)
            index.currentOffset += 3 * (k - 1);
        for(int c = 0; c < 256; c++)
        {
            horizontalKerning[c] = index.getUnsignedByte();
            verticalKerning[c] = index.getUnsignedByte();
            int width = glyphWidth[c] = index.getUnsignedLEShort();
            int height = glyphHeight[c] = index.getUnsignedLEShort();
            int rectangular = index.getUnsignedByte();
            int area = width * height;
            glyphPixels[c] = new byte[area];
            if(rectangular == 0)
            {
                for(int p = 0; p < area; p++)
                    glyphPixels[c][p] = data.readSignedByte();

            } else
            if(rectangular == 1)
            {
                for(int w = 0; w < width; w++)
                {
                    for(int h = 0; h < height; h++)
                        glyphPixels[c][w + h * width] = data.readSignedByte();

                }

            }
            if(height > charHeight && c < 128)
                charHeight = height;
            horizontalKerning[c] = 1;
            characterEffectiveWidth[c] = width + 2;
            int pixelCount = 0;
            for(int h = height / 7; h < height; h++)
                pixelCount += glyphPixels[c][h * width];

            if(pixelCount <= height / 7)
            {
                characterEffectiveWidth[c]--;
                horizontalKerning[c] = 0;
            }
            pixelCount = 0;
            for(int h = height / 7; h < height; h++)
                pixelCount += glyphPixels[c][(width - 1) + h * width];

            if(pixelCount <= height / 7)
                characterEffectiveWidth[c]--;
        }

        if(flag)
        {
            characterEffectiveWidth[32] = characterEffectiveWidth[73];
        } else
        {
            characterEffectiveWidth[32] = characterEffectiveWidth[105];
        }
    }

    public void drawTextHRightVTop(String text, int x, int y, int colour)
    {
        drawTextHLeftVTop(text, x - getStringWidth(text), y, colour);
    }

    public void drawTextHMidVTop(String text, int x, int y, int colour)
    {
        drawTextHLeftVTop(text, x - getStringWidth(text) / 2, y, colour);
    }

    public void drawShadowTextHMidVTop(int i, int x, String text, int l, boolean flag)
    {
        drawShadowTextHLeftVTop(text, x - getFormattedStringWidth(text) / 2, l, i, flag);
    }

    public int getFormattedStringWidth(String string)
    {
        if(string == null)
            return 0;
        int width = 0;
        for(int c = 0; c < string.length(); c++)
            if(string.charAt(c) == '@' && c + 4 < string.length() && string.charAt(c + 4) == '@')
                c += 4;
            else
                width += characterEffectiveWidth[string.charAt(c)];

        return width;
    }

    public int getStringWidth(String string)
    {
        if(string == null)
            return 0;
        int width = 0;
        for(int c = 0; c < string.length(); c++)
            width += characterEffectiveWidth[string.charAt(c)];
        return width;
    }

    public void drawTextHLeftVTop(String text, int x, int y, int colour)
    {
        if(text == null)
            return;
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
        {
            char character = text.charAt(c);
            if(character != ' ')
                drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character], glyphWidth[character], glyphHeight[character], colour);
            x += characterEffectiveWidth[character];
        }
    }

    public void drawTextHRMidVTopWaving(String text, int x, int y, int colour, int wavePart)
    {
        if(text == null)
            return;
        x -= getStringWidth(text) / 2;
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
        {
            char character = text.charAt(c);
            if(character != ' ')
                drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character] + (int)(Math.sin((double)c / 2D + (double)wavePart / 5D) * 5D), glyphWidth[character], glyphHeight[character], colour);
            x += characterEffectiveWidth[character];
        }

    }

    public void drawTextHRMidVTopWaving2(String text, int x, int y, int colour, int wavePart)
    {
        if(text == null)
            return;
        x -= getStringWidth(text) / 2;
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
        {
            char character = text.charAt(c);
            if(character != ' ')
                drawGlyph(glyphPixels[character], x + horizontalKerning[character] + (int)(Math.sin((double)c / 5D + (double)wavePart / 5D) * 5D), y + verticalKerning[character] + (int)(Math.sin((double)c / 3D + (double)wavePart / 5D) * 5D), glyphWidth[character], glyphHeight[character], colour);
            x += characterEffectiveWidth[character];
        }

    }

    public void drawTextHRMidVTopWaving(String text, int x, int y, int colour, int wavePart, int wavePart2)
    {
        if(text == null)
            return;
        double d = 7D - (double)wavePart / 8D;
        if(d < 0.0D)
            d = 0.0D;
        x -= getStringWidth(text) / 2;
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
        {
            char character = text.charAt(c);
            if(character != ' ')
                drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character] + (int)(Math.sin((double)c / 1.5D + (double)wavePart2) * d), glyphWidth[character], glyphHeight[character], colour);
            x += characterEffectiveWidth[character];
        }

    }

    public void drawShadowTextHLeftVTop(String text, int x, int y, int colour, boolean flag1)
    {
        strikethrough = false;
        int originalX = x;
        if(text == null)
            return;
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
            if(text.charAt(c) == '@' && c + 4 < text.length() && text.charAt(c + 4) == '@')
            {
                int _colour = getColorByName(text.substring(c + 1, c + 4));
                if(_colour != -1)
                    colour = _colour;
                c += 4;
            } else
            {
                char character = text.charAt(c);
                if(character != ' ')
                {
                    if(flag1)
                        drawGlyph(glyphPixels[character], x + horizontalKerning[character] + 1, y + verticalKerning[character] + 1, glyphWidth[character], glyphHeight[character], 0);
                    drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character], glyphWidth[character], glyphHeight[character], colour);
                }
                x += characterEffectiveWidth[character];
            }
        if(strikethrough)
            DrawingArea.drawHLine(y + (int)((double)charHeight * 0.69999999999999996D), 0x800000, x - originalX, originalX);
    }

    public void drawShadowedTextRight(String text, int x, int y, int colour, int seed)
    {
        if(text == null)
            return;
        random.setSeed(seed);
        int j1 = 192 + (random.nextInt() & 0x1f);
        y -= charHeight;
        for(int c = 0; c < text.length(); c++)
            if(text.charAt(c) == '@' && c + 4 < text.length() && text.charAt(c + 4) == '@')
            {
                int _colour = getColorByName(text.substring(c + 1, c + 4));
                if(_colour != -1)
                    colour = _colour;
                c += 4;
            } else
            {
                char character = text.charAt(c);
                if(character != ' ')
                {
                        drawShadowedChar(192, x + horizontalKerning[character] + 1, glyphPixels[character], glyphWidth[character], y + verticalKerning[character] + 1, glyphHeight[character], 0);
                    drawShadowedChar(j1, x + horizontalKerning[character], glyphPixels[character], glyphWidth[character], y + verticalKerning[character], glyphHeight[character], colour);
                }
                x += characterEffectiveWidth[character];
                if((random.nextInt() & 3) == 0)
                    x++;
            }

    }

    private int getColorByName(String name)
    {
        if(name.equals("red"))
            return 0xff0000;
        if(name.equals("gre"))
            return 65280;
        if(name.equals("blu"))
            return 255;
        if(name.equals("yel"))
            return 0xffff00;
        if(name.equals("cya"))
            return 65535;
        if(name.equals("mag"))
            return 0xff00ff;
        if(name.equals("whi"))
            return 0xffffff;
        if(name.equals("bla"))
            return 0;
        if(name.equals("lre"))
            return 0xff9040;
        if(name.equals("dre"))
            return 0x800000;
        if(name.equals("dbl"))
            return 128;
        if(name.equals("or1"))
            return 0xffb000;
        if(name.equals("or2"))
            return 0xff7000;
        if(name.equals("or3"))
            return 0xff3000;
        if(name.equals("gr1"))
            return 0xc0ff00;
        if(name.equals("gr2"))
            return 0x80ff00;
        if(name.equals("gr3"))
            return 0x40ff00;
        if(name.equals("str"))
            strikethrough = true;
        if(name.equals("end"))
            strikethrough = false;
        return -1;
    }

    private void drawGlyph(byte glyphPixels[], int x, int y, int width, int height, int colour)
    {
        int j1 = x + y * DrawingArea.width;
        int k1 = DrawingArea.width - width;
        int l1 = 0;
        int i2 = 0;
        if(y < DrawingArea.topY)
        {
            int size = DrawingArea.topY - y;
            height -= size;
            y = DrawingArea.topY;
            i2 += size * width;
            j1 += size * DrawingArea.width;
        }
        if(y + height >= DrawingArea.bottomY)
            height -= ((y + height) - DrawingArea.bottomY) + 1;
        if(x < DrawingArea.topX)
        {
            int size = DrawingArea.topX - x;
            width -= size;
            x = DrawingArea.topX;
            i2 += size;
            j1 += size;
            l1 += size;
            k1 += size;
        }
        if(x + width >= DrawingArea.bottomX)
        {
            int l2 = ((x + width) - DrawingArea.bottomX) + 1;
            width -= l2;
            l1 += l2;
            k1 += l2;
        }
        if(!(width <= 0 || height <= 0))
        {
            colourPixels(DrawingArea.pixels, glyphPixels, colour, i2, j1, width, height, k1, l1);
        }
    }

    private void colourPixels(int pixels[], byte originalPixels[], int colour, int j, int k, int width, int height,
            int j1, int k1)
    {
        int _width = -(width >> 2);
        width = -(width & 3);
        for(int h = -height; h < 0; h++)
        {
            for(int w = _width; w < 0; w++)
            {
                if(originalPixels[j++] != 0)
                    pixels[k++] = colour;
                else
                    k++;
                if(originalPixels[j++] != 0)
                    pixels[k++] = colour;
                else
                    k++;
                if(originalPixels[j++] != 0)
                    pixels[k++] = colour;
                else
                    k++;
                if(originalPixels[j++] != 0)
                    pixels[k++] = colour;
                else
                    k++;
            }

            for(int k2 = width; k2 < 0; k2++)
                if(originalPixels[j++] != 0)
                    pixels[k++] = colour;
                else
                    k++;

            k += j1;
            j += k1;
        }

    }

    private void drawShadowedChar(int i, int j, byte abyte0[], int k, int l, int i1,
                           int j1)
    {
        int k1 = j + l * DrawingArea.width;
        int l1 = DrawingArea.width - k;
        int i2 = 0;
        int j2 = 0;
        if(l < DrawingArea.topY)
        {
            int k2 = DrawingArea.topY - l;
            i1 -= k2;
            l = DrawingArea.topY;
            j2 += k2 * k;
            k1 += k2 * DrawingArea.width;
        }
        if(l + i1 >= DrawingArea.bottomY)
            i1 -= ((l + i1) - DrawingArea.bottomY) + 1;
        if(j < DrawingArea.topX)
        {
            int l2 = DrawingArea.topX - j;
            k -= l2;
            j = DrawingArea.topX;
            j2 += l2;
            k1 += l2;
            i2 += l2;
            l1 += l2;
        }
        if(j + k >= DrawingArea.bottomX)
        {
            int i3 = ((j + k) - DrawingArea.bottomX) + 1;
            k -= i3;
            i2 += i3;
            l1 += i3;
        }
        if(k <= 0 || i1 <= 0)
            return;
        recolourAlpha(abyte0, i1, k1, DrawingArea.pixels, j2, k, i2, l1, j1, i);
    }

    private void recolourAlpha(byte abyte0[], int i, int j, int ai[], int l, int i1,
                           int j1, int k1, int colour, int alpha)
    {
        colour = ((colour & 0xff00ff) * alpha & 0xff00ff00) + ((colour & 0xff00) * alpha & 0xff0000) >> 8;
        alpha = 256 - alpha;
        for(int j2 = -i; j2 < 0; j2++)
        {
            for(int k2 = -i1; k2 < 0; k2++)
                if(abyte0[l++] != 0)
                {
                    int l2 = ai[j];
                    ai[j++] = (((l2 & 0xff00ff) * alpha & 0xff00ff00) + ((l2 & 0xff00) * alpha & 0xff0000) >> 8) + colour;
                } else
                {
                    j++;
                }

            j += k1;
            l += j1;
        }

    }

    private final byte[][] glyphPixels;
    private final int[] glyphWidth;
    private final int[] glyphHeight;
    private final int[] horizontalKerning;
    private final int[] verticalKerning;
    private final int[] characterEffectiveWidth;
    public int charHeight;
    private final Random random;
    private boolean strikethrough;
}
