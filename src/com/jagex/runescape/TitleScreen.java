package com.jagex.runescape;

import com.jagex.runescape.definition.GameFont;

import java.awt.*;

public class TitleScreen {
    private IndexedImage titleBoxImage;
    private IndexedImage titleButtonImage;
    private IndexedImage[] titleFlameRuneImages;
    private Sprite flameLeftBackground2;
    private Sprite flameRightBackground2;
    private RSImageProducer flameLeftBackground;
    private RSImageProducer flameRightBackground;
    private int[] currentFlameColours;
    private int[] flameColour1;
    private int[] flameColour2;
    private int[] flameColour3;
    private int[] titleFlames;
    private int[] titleFlamesTemp;
    private RSImageProducer topCentreBackgroundTile;
    private RSImageProducer bottomCentreBackgroundTile;
    public RSImageProducer loginBoxLeftBackgroundTile;
    private RSImageProducer bottomLeftBackgroundTile;
    private RSImageProducer bottomRightBackgroundTile;
    private RSImageProducer middleLeftBackgroundTile;
    private RSImageProducer middleRightBackgroundTile;
    public boolean welcomeScreenRaised;
    private int anInt1040;
    private int anInt1041;
    private int anInt1275;

    private Archive archive;
    private GameFont fontSmall;
    private GameFont fontPlain;
    private GameFont fontBold;
    private final int[] anIntArray969;
    private int[] anIntArray828;
    private int[] anIntArray829;
    public volatile boolean drawingFlames;
    public volatile boolean currentlyDrawingFlames;

    public TitleScreen() {
        anIntArray969 = new int[256];
        welcomeScreenRaised = false;
        drawingFlames = false;
        currentlyDrawingFlames = false;
    }

    public void load(Component component, Archive archive, GameFont fontSmall, GameFont fontPlain, GameFont fontBold) {
        this.archive = archive;
        this.fontSmall = fontSmall;
        this.fontPlain = fontPlain;
        this.fontBold = fontBold;

        setupSprites(component, archive);

        titleBoxImage = new IndexedImage(archive, "titlebox", 0);
        titleButtonImage = new IndexedImage(archive, "titlebutton", 0);
        titleFlameRuneImages = new IndexedImage[12];

        for (int r = 0; r < 12; r++)
            titleFlameRuneImages[r] = new IndexedImage(archive, "runes", r);

        flameLeftBackground2 = new Sprite(128, 265);
        flameRightBackground2 = new Sprite(128, 265);
        System.arraycopy(flameLeftBackground.pixels, 0, flameLeftBackground2.pixels, 0, 33920);

        System.arraycopy(flameRightBackground.pixels, 0, flameRightBackground2.pixels, 0, 33920);

        flameColour1 = new int[256];
        for (int c = 0; c < 64; c++)
            flameColour1[c] = c * 0x40000;

        for (int c = 0; c < 64; c++)
            flameColour1[c + 64] = 0xFF0000 + 1024 * c;

        for (int c = 0; c < 64; c++)
            flameColour1[c + 128] = 0xFFFF00 + 4 * c;

        for (int c = 0; c < 64; c++)
            flameColour1[c + 192] = 0xFFFFFF;

        flameColour2 = new int[256];
        for (int c = 0; c < 64; c++)
            flameColour2[c] = c * 1024;

        for (int c = 0; c < 64; c++)
            flameColour2[c + 64] = 0x00FF00 + 4 * c;

        for (int c = 0; c < 64; c++)
            flameColour2[c + 128] = 0x00FFFF + 0x40000 * c;

        for (int c = 0; c < 64; c++)
            flameColour2[c + 192] = 0xFFFFFF;

        flameColour3 = new int[256];
        for (int c = 0; c < 64; c++)
            flameColour3[c] = c * 4;

        for (int c = 0; c < 64; c++)
            flameColour3[c + 64] = 255 + 0x40000 * c;

        for (int c = 0; c < 64; c++)
            flameColour3[c + 128] = 0xFF00ff + 1024 * c;

        for (int c = 0; c < 64; c++)
            flameColour3[c + 192] = 0xFFFFFF;

        currentFlameColours = new int[256];
        titleFlames = new int[32768];
        titleFlamesTemp = new int[32768];

        anIntArray828 = new int[32768];
        anIntArray829 = new int[32768];

        updateTitleFlames(null);
    }

    public void drawLoadingText(Graphics gameGraphics, int percentage, String text) {
        loginBoxLeftBackgroundTile.initDrawingArea();

        int horizontalOffset = 360;
        int verticalOffset1 = 200;
        int verticalOffset2 = 20;
        fontBold.drawCentredText("RuneScape is loading - please wait...", horizontalOffset / 2,
                verticalOffset1 / 2 - 26 - verticalOffset2, 0xFFFFFF);
        int loadingBarHeight = verticalOffset1 / 2 - 18 - verticalOffset2;

        DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 152, 304, 34, 0x8C1111, loadingBarHeight);
        DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 151, 302, 32, 0, loadingBarHeight + 1);
        DrawingArea.drawFilledRectangle(horizontalOffset / 2 - 150, loadingBarHeight + 2, percentage * 3, 30, 0x8C1111);
        DrawingArea.drawFilledRectangle((horizontalOffset / 2 - 150) + percentage * 3, loadingBarHeight + 2,
                300 - percentage * 3, 30, 0);
        fontBold.drawCentredText(text, horizontalOffset / 2, (verticalOffset1 / 2 + 5) - verticalOffset2, 0xFFFFFF);
        loginBoxLeftBackgroundTile.drawGraphics(171, gameGraphics, 202);

        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            if (!currentlyDrawingFlames) {
                drawFlames(gameGraphics);
            }

            drawTiles(gameGraphics);
        }
    }

    public void setupImageProducers(Component gameComponent) {
        flameLeftBackground = new RSImageProducer(128, 265, gameComponent);
        DrawingArea.clear();
        flameRightBackground = new RSImageProducer(128, 265, gameComponent);
        DrawingArea.clear();
        topCentreBackgroundTile = new RSImageProducer(509, 171, gameComponent);
        DrawingArea.clear();
        bottomCentreBackgroundTile = new RSImageProducer(360, 132, gameComponent);
        DrawingArea.clear();
        loginBoxLeftBackgroundTile = new RSImageProducer(360, 200, gameComponent);
        DrawingArea.clear();
        bottomLeftBackgroundTile = new RSImageProducer(202, 238, gameComponent);
        DrawingArea.clear();
        bottomRightBackgroundTile = new RSImageProducer(203, 238, gameComponent);
        DrawingArea.clear();
        middleLeftBackgroundTile = new RSImageProducer(74, 94, gameComponent);
        DrawingArea.clear();
        middleRightBackgroundTile = new RSImageProducer(75, 94, gameComponent);
        DrawingArea.clear();
    }

    public void clearImageProducers() {
        topCentreBackgroundTile = null;
        bottomCentreBackgroundTile = null;
        loginBoxLeftBackgroundTile = null;
        flameLeftBackground = null;
        flameRightBackground = null;
        bottomLeftBackgroundTile = null;
        bottomRightBackgroundTile = null;
        middleLeftBackgroundTile = null;
        middleRightBackgroundTile = null;
    }

    public boolean imageProducersInitialised() {
        return topCentreBackgroundTile != null;
    }

    public void nullLoader() {
        titleBoxImage = null;
        titleButtonImage = null;
        titleFlameRuneImages = null;
        currentFlameColours = null;
        flameColour1 = null;
        flameColour2 = null;
        flameColour3 = null;
        titleFlames = null;
        titleFlamesTemp = null;
        flameLeftBackground2 = null;
        flameRightBackground2 = null;

        anIntArray828 = null;
        anIntArray829 = null;

        this.clearImageProducers();
    }

    private void doFlamesDrawing(Graphics gameGraphics) {
        char c = '\u0100';
        if (anInt1040 > 0) {
            for (int i = 0; i < 256; i++)
                if (anInt1040 > 768)
                    currentFlameColours[i] = rotateFlameColour(flameColour1[i], flameColour2[i], 1024 - anInt1040);
                else if (anInt1040 > 256)
                    currentFlameColours[i] = flameColour2[i];
                else
                    currentFlameColours[i] = rotateFlameColour(flameColour2[i], flameColour1[i], 256 - anInt1040);

        } else if (anInt1041 > 0) {
            for (int j = 0; j < 256; j++)
                if (anInt1041 > 768)
                    currentFlameColours[j] = rotateFlameColour(flameColour1[j], flameColour3[j], 1024 - anInt1041);
                else if (anInt1041 > 256)
                    currentFlameColours[j] = flameColour3[j];
                else
                    currentFlameColours[j] = rotateFlameColour(flameColour3[j], flameColour1[j], 256 - anInt1041);

        } else {
            System.arraycopy(flameColour1, 0, currentFlameColours, 0, 256);
        }

        System.arraycopy(flameLeftBackground2.pixels, 0, flameLeftBackground.pixels, 0, 33920);

        int i1 = 0;
        int j1 = 1152;
        for (int k1 = 1; k1 < c - 1; k1++) {
            int l1 = (anIntArray969[k1] * (c - k1)) / c;
            int j2 = 22 + l1;
            if (j2 < 0)
                j2 = 0;
            i1 += j2;
            for (int l2 = j2; l2 < 128; l2++) {
                int j3 = anIntArray828[i1++];
                if (j3 != 0) {
                    int l3 = j3;
                    int j4 = 256 - j3;
                    j3 = currentFlameColours[j3];
                    int l4 = flameLeftBackground.pixels[j1];
                    flameLeftBackground.pixels[j1++] = ((j3 & 0xFF00ff) * l3 + (l4 & 0xFF00FF) * j4 & 0xFF00FF00)
                            + ((j3 & 0xFF00) * l3 + (l4 & 0xFF00) * j4 & 0xFF0000) >> 8;
                } else {
                    j1++;
                }
            }

            j1 += j2;
        }

        flameLeftBackground.drawGraphics(0, gameGraphics, 0);

        System.arraycopy(flameRightBackground2.pixels, 0, flameRightBackground.pixels, 0, 33920);

        i1 = 0;
        j1 = 1176;
        for (int k2 = 1; k2 < c - 1; k2++) {
            int i3 = (anIntArray969[k2] * (c - k2)) / c;
            int k3 = 103 - i3;
            j1 += i3;
            for (int i4 = 0; i4 < k3; i4++) {
                int k4 = anIntArray828[i1++];
                if (k4 != 0) {
                    int i5 = k4;
                    int j5 = 256 - k4;
                    k4 = currentFlameColours[k4];
                    int k5 = flameRightBackground.pixels[j1];
                    flameRightBackground.pixels[j1++] = ((k4 & 0xFF00FF) * i5 + (k5 & 0xFF00FF) * j5 & 0xFF00FF00)
                            + ((k4 & 0xFF00) * i5 + (k5 & 0xFF00) * j5 & 0xFF0000) >> 8;
                } else {
                    j1++;
                }
            }

            i1 += 128 - k3;
            j1 += 128 - k3 - i3;
        }

        flameRightBackground.drawGraphics(0, gameGraphics, 637);
    }

    private void setupSprites(Component component, Archive archive) {
        byte titleData[] = archive.decompressFile("title.dat");
        Sprite sprite = new Sprite(titleData, component);

        flameLeftBackground.initDrawingArea();
        sprite.drawInverse(0, 0);
        flameRightBackground.initDrawingArea();
        sprite.drawInverse(-637, 0);
        topCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(-128, 0);
        bottomCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(-202, -371);
        loginBoxLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(-202, -171);
        bottomLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(0, -265);
        bottomRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-562, -265);
        middleLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(-128, -171);
        middleRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-562, -171);

        int modifiedPixels[] = new int[sprite.width];
        for (int row = 0; row < sprite.height; row++) {
            for (int column = 0; column < sprite.width; column++)
                modifiedPixels[column] = sprite.pixels[(sprite.width - column - 1) + sprite.width * row];

            System.arraycopy(modifiedPixels, 0, sprite.pixels, sprite.width * row, sprite.width);

        }

        flameLeftBackground.initDrawingArea();
        sprite.drawInverse(382, 0);
        flameRightBackground.initDrawingArea();
        sprite.drawInverse(-255, 0);
        topCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(254, 0);
        bottomCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(180, -371);
        loginBoxLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(180, -171);
        bottomLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(382, -265);
        bottomRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-180, -265);
        middleLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(254, -171);
        middleRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-180, -171);
        sprite = new Sprite(archive, "logo", 0);
        topCentreBackgroundTile.initDrawingArea();
        sprite.drawImage(382 - sprite.width / 2 - 128, 18);
    }

    public void drawFlames(Graphics gameGraphics) {
        flameLeftBackground.drawGraphics(0, gameGraphics, 0);
        flameRightBackground.drawGraphics(0, gameGraphics, 637);
    }

    public void drawTiles(Graphics gameGraphics) {
        topCentreBackgroundTile.drawGraphics(0, gameGraphics, 128);
        bottomCentreBackgroundTile.drawGraphics(371, gameGraphics, 202);
        bottomLeftBackgroundTile.drawGraphics(265, gameGraphics, 0);
        bottomRightBackgroundTile.drawGraphics(265, gameGraphics, 562);
        middleLeftBackgroundTile.drawGraphics(171, gameGraphics, 128);
        middleRightBackgroundTile.drawGraphics(171, gameGraphics, 562);
    }

    private void calcFlamesPosition(int tick) {
        char c = '\u0100';
        for (int j = 10; j < 117; j++) {
            int k = (int) (Math.random() * 100D);
            if (k < 50)
                anIntArray828[j + (c - 2 << 7)] = 255;
        }
        for (int i = 0; i < 100; i++) {
            int a = (int) (Math.random() * 124D) + 2;
            int b = (int) (Math.random() * 128D) + 128;
            int index = a + (b << 7);
            anIntArray828[index] = 192;
        }

        for (int j1 = 1; j1 < c - 1; j1++) {
            for (int l1 = 1; l1 < 127; l1++) {
                int l2 = l1 + (j1 << 7);
                anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128]
                        + anIntArray828[l2 + 128]) / 4;
            }

        }

        anInt1275 += 128;
        if (anInt1275 > titleFlames.length) {
            anInt1275 -= titleFlames.length;
            int image = (int) (Math.random() * 12D);
            updateTitleFlames(titleFlameRuneImages[image]);
        }
        for (int j2 = 1; j2 < c - 1; j2++) {
            for (int i3 = 1; i3 < 127; i3++) {
                int k3 = i3 + (j2 << 7);
                int i4 = anIntArray829[k3 + 128] - titleFlames[k3 + anInt1275 & titleFlames.length - 1] / 5;
                if (i4 < 0)
                    i4 = 0;
                anIntArray828[k3] = i4;
            }

        }

        System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

        anIntArray969[c
                - 1] = (int) (Math.sin(tick / 14D) * 16D + Math.sin(tick / 15D) * 14D + Math.sin(tick / 16D) * 12D);
        if (anInt1040 > 0)
            anInt1040 -= 4;
        if (anInt1041 > 0)
            anInt1041 -= 4;
        if (anInt1040 == 0 && anInt1041 == 0) {
            int rand = (int) (Math.random() * 2000D);
            if (rand == 0)
                anInt1040 = 1024;
            if (rand == 1)
                anInt1041 = 1024;
        }
    }

    public void drawFlames2(Graphics gameGraphics, int tick) {
        drawingFlames = true;
        try {
            long startTime = System.currentTimeMillis();
            int currentLoop = 0;
            int interval = 20;
            while (currentlyDrawingFlames) {
                calcFlamesPosition(tick);
                calcFlamesPosition(tick);
                doFlamesDrawing(gameGraphics);
                if (++currentLoop > 10) {
                    long currentTime = System.currentTimeMillis();
                    int difference = (int) (currentTime - startTime) / 10 - interval;
                    interval = 40 - difference;
                    if (interval < 5)
                        interval = 5;
                    currentLoop = 0;
                    startTime = currentTime;
                }
                try {
                    Thread.sleep(interval);
                } catch (Exception _ex) {
                }
            }
        } catch (Exception _ex) {
        }
        drawingFlames = false;
    }

    private void updateTitleFlames(IndexedImage background) {
        int j = 256;
        for (int k = 0; k < titleFlames.length; k++)
            titleFlames[k] = 0;

        for (int l = 0; l < 5000; l++) {
            int i1 = (int) (Math.random() * 128D * j);
            titleFlames[i1] = (int) (Math.random() * 256D);
        }

        for (int j1 = 0; j1 < 20; j1++) {
            for (int k1 = 1; k1 < j - 1; k1++) {
                for (int i2 = 1; i2 < 127; i2++) {
                    int k2 = i2 + (k1 << 7);
                    titleFlamesTemp[k2] = (titleFlames[k2 - 1] + titleFlames[k2 + 1] + titleFlames[k2 - 128]
                            + titleFlames[k2 + 128]) / 4;
                }

            }

            int ai[] = titleFlames;
            titleFlames = titleFlamesTemp;
            titleFlamesTemp = ai;
        }

        if (background != null) {
            int l1 = 0;
            for (int row = 0; row < background.height; row++) {
                for (int column = 0; column < background.width; column++)
                    if (background.pixels[l1++] != 0) {
                        int i3 = column + 16 + background.drawOffsetX;
                        int j3 = row + 16 + background.drawOffsetY;
                        int k3 = i3 + (j3 << 7);
                        titleFlames[k3] = 0;
                    }

            }

        }
    }

    public void drawLoginScreen(
            Graphics gameGraphics,
            boolean originalLoginScreen,
            int loginScreenState,
            String statusString,
            String message1,
            String message2,
            String enteredUsername,
            String enteredPassword,
            int tick,
            int focus
    ) {
        loginBoxLeftBackgroundTile.initDrawingArea();
        titleBoxImage.draw(0, 0);
        int x = 360;
        int y = 200;
        if (loginScreenState == 0) {
            int _y = y / 2 + 80;
            fontSmall.drawCentredTextWithPotentialShadow(statusString, x / 2, _y, 0x75A9A9, true);
            _y = y / 2 - 20;
            fontBold.drawCentredTextWithPotentialShadow("Welcome to RuneScape", x / 2, _y, 0xFFFF00, true);
            _y += 30;
            int _x = x / 2 - 80;
            int __y = y / 2 + 20;
            titleButtonImage.draw(_x - 73, __y - 20);
            fontBold.drawCentredTextWithPotentialShadow("New User", _x, __y + 5, 0xFFFFFF, true);
            _x = x / 2 + 80;
            titleButtonImage.draw(_x - 73, __y - 20);
            fontBold.drawCentredTextWithPotentialShadow("Existing User", _x, __y + 5, 0xFFFFFF, true);
        }
        if (loginScreenState == 2) {
            int _y = y / 2 - 40;
            if (message1.length() > 0) {
                fontBold.drawCentredTextWithPotentialShadow(message1, x / 2, _y - 15, 0xFFFF00, true);
                fontBold.drawCentredTextWithPotentialShadow(message2, x / 2, _y, 0xFFFF00, true);
                _y += 30;
            } else {
                fontBold.drawCentredTextWithPotentialShadow(message2, x / 2, _y - 7, 0xFFFF00, true);
                _y += 30;
            }
            fontBold.drawTextWithPotentialShadow(
                    "Username: " + enteredUsername + ((focus == 0) & (tick % 40 < 20) ? "@yel@|" : ""),
                    x / 2 - 90, _y, 0xFFFFFF, true);
            _y += 15;
            fontBold.drawTextWithPotentialShadow(
                    "Password: " + TextClass.asterisksForString(enteredPassword)
                            + ((focus == 1) & (tick % 40 < 20) ? "@yel@|" : ""),
                    x / 2 - 88, _y, 0xFFFFFF, true);
            _y += 15;
            if (!originalLoginScreen) {
                int _x = x / 2 - 80;
                int __y = y / 2 + 50;
                titleButtonImage.draw(_x - 73, __y - 20);
                fontBold.drawCentredTextWithPotentialShadow("Login", _x, __y + 5, 0xFFFFFF, true);
                _x = x / 2 + 80;
                titleButtonImage.draw(_x - 73, __y - 20);
                fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xFFFFFF, true);
            }
        }
        if (loginScreenState == 3) {
            fontBold.drawCentredTextWithPotentialShadow("Create a free account", x / 2, y / 2 - 60, 0xFFFF00, true);
            int _y = y / 2 - 35;
            fontBold.drawCentredTextWithPotentialShadow("To create a new account you need to", x / 2, _y, 0xFFFFFF,
                    true);
            _y += 15;
            fontBold.drawCentredTextWithPotentialShadow("go back to the main RuneScape webpage", x / 2, _y, 0xFFFFFF,
                    true);
            _y += 15;
            fontBold.drawCentredTextWithPotentialShadow("and choose the red 'create account'", x / 2, _y, 0xFFFFFF,
                    true);
            _y += 15;
            fontBold.drawCentredTextWithPotentialShadow("button at the top right of that page.", x / 2, _y, 0xFFFFFF,
                    true);
            _y += 15;
            int _x = x / 2;
            int __y = y / 2 + 50;
            titleButtonImage.draw(_x - 73, __y - 20);
            fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xFFFFFF, true);
        }
        loginBoxLeftBackgroundTile.drawGraphics(171, gameGraphics, 202);
        if (welcomeScreenRaised) {
            welcomeScreenRaised = false;
            topCentreBackgroundTile.drawGraphics(0, gameGraphics, 128);
            bottomCentreBackgroundTile.drawGraphics(371, gameGraphics, 202);
            bottomLeftBackgroundTile.drawGraphics(265, gameGraphics, 0);
            bottomRightBackgroundTile.drawGraphics(265, gameGraphics, 562);
            middleLeftBackgroundTile.drawGraphics(171, gameGraphics, 128);
            middleRightBackgroundTile.drawGraphics(171, gameGraphics, 562);
        }
    }

    private int rotateFlameColour(int r, int g, int b) {
        int alpha = 256 - b;
        return ((r & 0xFF00ff) * alpha + (g & 0xFF00ff) * b & 0xFF00FF00)
                + ((r & 0xFF00) * alpha + (g & 0xFF00) * b & 0xFF0000) >> 8;
    }
}
