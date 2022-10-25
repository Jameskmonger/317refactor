package com.jagex.runescape.screen.title;

class FlameColours {
    private final int[] currentFlameColours;
    private final int[] redFlameColours;
    private final int[] greenFlameColours;
    private final int[] blueFlameColours;

    private int random1;
    private int random2;

    public FlameColours() {
        this.redFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            this.redFlameColours[c] = c * 0x040000;
        }

        for (int c = 0; c < 64; c++) {
            this.redFlameColours[c + 64] = 0xFF0000 + 0x000400 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.redFlameColours[c + 128] = 0xFFFF00 + 0x000004 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.redFlameColours[c + 192] = 0xFFFFFF;
        }

        this.greenFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            this.greenFlameColours[c] = c * 0x000400;
        }

        for (int c = 0; c < 64; c++) {
            this.greenFlameColours[c + 64] = 0x00FF00 + 0x000004 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.greenFlameColours[c + 128] = 0x00FFFF + 0x040000 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.greenFlameColours[c + 192] = 0xFFFFFF;
        }

        this.blueFlameColours = new int[256];
        for (int c = 0; c < 64; c++) {
            this.blueFlameColours[c] = c * 0x000004;
        }

        for (int c = 0; c < 64; c++) {
            this.blueFlameColours[c + 64] = 0x0000FF + 0x040000 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.blueFlameColours[c + 128] = 0xFF00FF + 0x000400 * c;
        }

        for (int c = 0; c < 64; c++) {
            this.blueFlameColours[c + 192] = 0xFFFFFF;
        }

        this.currentFlameColours = new int[256];
    }

    public int getCurrentColour(final int strength) {
        return this.currentFlameColours[strength];
    }

    public void changeColours() {
        this.incrementRandoms();
        this.incrementRandoms();

        if (this.random1 > 0) {
            for (int strength = 0; strength < 256; strength++) {
                if (this.random1 > 768) {
                    this.currentFlameColours[strength] = this.rotateFlameColour(this.redFlameColours[strength], this.greenFlameColours[strength], 1024 - this.random1);
                } else if (this.random1 > 256) {
                    this.currentFlameColours[strength] = this.greenFlameColours[strength];
                } else {
                    this.currentFlameColours[strength] = this.rotateFlameColour(this.greenFlameColours[strength], this.redFlameColours[strength], 256 - this.random1);
                }
            }
        } else if (this.random2 > 0) {
            for (int strength = 0; strength < 256; strength++) {
                if (this.random2 > 768) {
                    this.currentFlameColours[strength] = this.rotateFlameColour(this.redFlameColours[strength], this.blueFlameColours[strength], 1024 - this.random2);
                } else if (this.random2 > 256) {
                    this.currentFlameColours[strength] = this.blueFlameColours[strength];
                } else {
                    this.currentFlameColours[strength] = this.rotateFlameColour(this.blueFlameColours[strength], this.redFlameColours[strength], 256 - this.random2);
                }
            }
        } else {
            System.arraycopy(this.redFlameColours, 0, this.currentFlameColours, 0, 256);
        }
    }

    private void incrementRandoms() {
        if (this.random1 > 0) {
            this.random1 -= 4;
        }
        if (this.random2 > 0) {
            this.random2 -= 4;
        }
        if (this.random1 == 0 && this.random2 == 0) {
            final int rand = (int) (Math.random() * 2000D);
            if (rand == 0) {
                this.random1 = 1024;
            }
            if (rand == 1) {
                this.random2 = 1024;
            }
        }
    }

    private int rotateFlameColour(final int r, final int g, final int b) {
        final int alpha = 256 - b;

        return ((r & 0xFF00FF) * alpha + (g & 0xFF00FF) * b & 0xFF00FF00)
            + ((r & 0x00FF00) * alpha + (g & 0x00FF00) * b & 0xFF0000) >> 8;
    }
}
