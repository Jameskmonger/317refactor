package com.jagex.runescape.screen.title;

public class FlameColours {
    private int[] currentFlameColours;
    private int[] redFlameColours;
    private int[] greenFlameColours;
    private int[] blueFlameColours;

    public FlameColours() {
        redFlameColours = new int[256];
        for (int c = 0; c < 64; c++)
            redFlameColours[c] = c * 0x040000;

        for (int c = 0; c < 64; c++)
            redFlameColours[c + 64] = 0xFF0000 + 0x000400 * c;

        for (int c = 0; c < 64; c++)
            redFlameColours[c + 128] = 0xFFFF00 + 0x000004 * c;

        for (int c = 0; c < 64; c++)
            redFlameColours[c + 192] = 0xFFFFFF;

        greenFlameColours = new int[256];
        for (int c = 0; c < 64; c++)
            greenFlameColours[c] = c * 0x000400;

        for (int c = 0; c < 64; c++)
            greenFlameColours[c + 64] = 0x00FF00 + 0x000004 * c;

        for (int c = 0; c < 64; c++)
            greenFlameColours[c + 128] = 0x00FFFF + 0x040000 * c;

        for (int c = 0; c < 64; c++)
            greenFlameColours[c + 192] = 0xFFFFFF;

        blueFlameColours = new int[256];
        for (int c = 0; c < 64; c++)
            blueFlameColours[c] = c * 0x000004;

        for (int c = 0; c < 64; c++)
            blueFlameColours[c + 64] = 0x0000FF + 0x040000 * c;

        for (int c = 0; c < 64; c++)
            blueFlameColours[c + 128] = 0xFF00FF + 0x000400 * c;

        for (int c = 0; c < 64; c++)
            blueFlameColours[c + 192] = 0xFFFFFF;

        currentFlameColours = new int[256];
    }

    public int getCurrentColour(int position) {
        return currentFlameColours[position];
    }

    public void changeColours(int random1, int random2) {
        if (random1 > 0) {
            for (int i = 0; i < 256; i++)
                if (random1 > 768)
                    currentFlameColours[i] = rotateFlameColour(redFlameColours[i], greenFlameColours[i], 1024 - random1);
                else if (random1 > 256)
                    currentFlameColours[i] = greenFlameColours[i];
                else
                    currentFlameColours[i] = rotateFlameColour(greenFlameColours[i], redFlameColours[i], 256 - random1);

        } else if (random2 > 0) {
            for (int j = 0; j < 256; j++)
                if (random2 > 768)
                    currentFlameColours[j] = rotateFlameColour(redFlameColours[j], blueFlameColours[j], 1024 - random2);
                else if (random2 > 256)
                    currentFlameColours[j] = blueFlameColours[j];
                else
                    currentFlameColours[j] = rotateFlameColour(blueFlameColours[j], redFlameColours[j], 256 - random2);

        } else {
            System.arraycopy(redFlameColours, 0, currentFlameColours, 0, 256);
        }
    }

    private int rotateFlameColour(int r, int g, int b) {
        int alpha = 256 - b;

        return ((r & 0xFF00FF) * alpha + (g & 0xFF00FF) * b & 0xFF00FF00)
                + ((r & 0x00FF00) * alpha + (g & 0x00FF00) * b & 0xFF0000) >> 8;
    }
}
