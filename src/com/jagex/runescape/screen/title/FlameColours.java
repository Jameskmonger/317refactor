package com.jagex.runescape.screen.title;

public class FlameColours {
    private int[] currentFlameColours;
    private int[] redFlameColours;
    private int[] greenFlameColours;
    private int[] blueFlameColours;

    private int random1;
    private int random2;

    public FlameColours() {
        redFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            redFlameColours[c] = c * 0x040000;
        }

        for (int c = 0; c < 64; c++) {
            redFlameColours[c + 64] = 0xFF0000 + 0x000400 * c;
        }

        for (int c = 0; c < 64; c++) {
            redFlameColours[c + 128] = 0xFFFF00 + 0x000004 * c;
        }

        for (int c = 0; c < 64; c++) {
            redFlameColours[c + 192] = 0xFFFFFF;
        }

        greenFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            greenFlameColours[c] = c * 0x000400;
        }

        for (int c = 0; c < 64; c++) {
            greenFlameColours[c + 64] = 0x00FF00 + 0x000004 * c;
        }

        for (int c = 0; c < 64; c++) {
            greenFlameColours[c + 128] = 0x00FFFF + 0x040000 * c;
        }

        for (int c = 0; c < 64; c++) {
            greenFlameColours[c + 192] = 0xFFFFFF;
        }

        blueFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            blueFlameColours[c] = c * 0x000004;
        }

        for (int c = 0; c < 64; c++) {
            blueFlameColours[c + 64] = 0x0000FF + 0x040000 * c;
        }

        for (int c = 0; c < 64; c++) {
            blueFlameColours[c + 128] = 0xFF00FF + 0x000400 * c;
        }

        for (int c = 0; c < 64; c++) {
            blueFlameColours[c + 192] = 0xFFFFFF;
        }

        currentFlameColours = new int[256];
    }

    public int getCurrentColour(int strength) {
        return currentFlameColours[strength];
    }

    public void changeColours() {
        incrementRandoms();
        incrementRandoms();

        if (random1 > 0) {
            for (int strength = 0; strength < 256; strength++) {
                if (random1 > 768) {
                    currentFlameColours[strength] = rotateFlameColour(redFlameColours[strength], greenFlameColours[strength], 1024 - random1);
                } else if (random1 > 256) {
                    currentFlameColours[strength] = greenFlameColours[strength];
                } else {
                    currentFlameColours[strength] = rotateFlameColour(greenFlameColours[strength], redFlameColours[strength], 256 - random1);
                }
            }
        } else if (random2 > 0) {
            for (int strength = 0; strength < 256; strength++) {
                if (random2 > 768) {
                    currentFlameColours[strength] = rotateFlameColour(redFlameColours[strength], blueFlameColours[strength], 1024 - random2);
                } else if (random2 > 256) {
                    currentFlameColours[strength] = blueFlameColours[strength];
                } else {
                    currentFlameColours[strength] = rotateFlameColour(blueFlameColours[strength], redFlameColours[strength], 256 - random2);
                }
            }
        } else {
            System.arraycopy(redFlameColours, 0, currentFlameColours, 0, 256);
        }
    }

    private void incrementRandoms() {
        if (random1 > 0) {
            random1 -= 4;
        }
        if (random2 > 0) {
            random2 -= 4;
        }
        if (random1 == 0 && random2 == 0) {
            int rand = (int) (Math.random() * 2000D);
            if (rand == 0) {
                random1 = 1024;
            }
            if (rand == 1) {
                random2 = 1024;
            }
        }
    }

    private int rotateFlameColour(int r, int g, int b) {
        int alpha = 256 - b;

        return ((r & 0xFF00FF) * alpha + (g & 0xFF00FF) * b & 0xFF00FF00)
                + ((r & 0x00FF00) * alpha + (g & 0x00FF00) * b & 0xFF0000) >> 8;
    }
}
