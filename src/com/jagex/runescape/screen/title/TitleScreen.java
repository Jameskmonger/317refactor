package com.jagex.runescape.screen.title;

import com.jagex.runescape.*;
import com.jagex.runescape.definition.GameFont;

import java.awt.*;

public class TitleScreen {
    private final int[] anIntArray969;
    public RSImageProducer loginBoxLeftBackgroundTile;
    public boolean welcomeScreenRaised;
    public volatile boolean drawingFlames;
    public volatile boolean currentlyDrawingFlames;
    private IndexedImage titleBoxImage;
    private IndexedImage titleButtonImage;
    private IndexedImage[] titleFlameRuneImages;
    private Sprite flameLeftBackground2;
    private Sprite flameRightBackground2;
    private RSImageProducer flameLeftBackground;
    private RSImageProducer flameRightBackground;
    private int[] titleFlames;
    private int[] titleFlamesTemp;
    private RSImageProducer topCentreBackgroundTile;
    private RSImageProducer bottomCentreBackgroundTile;
    private RSImageProducer bottomLeftBackgroundTile;
    private RSImageProducer bottomRightBackgroundTile;
    private RSImageProducer middleLeftBackgroundTile;
    private RSImageProducer middleRightBackgroundTile;
    private int flameShapeIndex;
    private Archive archive;
    private GameFont fontSmall;
    private GameFont fontPlain;
    private GameFont fontBold;
    private int[] flameStrengths;
    private int[] anIntArray829;
    private FlameColours flameColours;

    public TitleScreen() {
        anIntArray969 = new int[256];
        welcomeScreenRaised = false;
        drawingFlames = false;
        currentlyDrawingFlames = false;

        this.flameColours = new FlameColours();
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

        for (int r = 0; r < 12; r++) {
            titleFlameRuneImages[r] = new IndexedImage(archive, "runes", r);
        }

        flameLeftBackground2 = new Sprite(128, 265);
        flameRightBackground2 = new Sprite(128, 265);
        System.arraycopy(flameLeftBackground.pixels, 0, flameLeftBackground2.pixels, 0, 33920);

        System.arraycopy(flameRightBackground.pixels, 0, flameRightBackground2.pixels, 0, 33920);

        titleFlames = new int[32768];
        titleFlamesTemp = new int[32768];

        flameStrengths = new int[32768];
        anIntArray829 = new int[32768];

        updateFlameShape(null);
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
        titleFlames = null;
        titleFlamesTemp = null;
        flameLeftBackground2 = null;
        flameRightBackground2 = null;

        flameStrengths = null;
        anIntArray829 = null;

        this.clearImageProducers();
    }

    private void doFlamesDrawing(Graphics gameGraphics) {
        char c = '\u0100';

        flameColours.changeColours();

        System.arraycopy(flameLeftBackground2.pixels, 0, flameLeftBackground.pixels, 0, 33920);

        int i1 = 0;
        int pos = 1152;
        for (int k1 = 1; k1 < c - 1; k1++) {
            int l1 = (anIntArray969[k1] * (c - k1)) / c;

            if (l1 != 0) {
                System.out.println(l1);
            }

            int j2 = 22 + l1;
            if (j2 < 0) {
                j2 = 0;
            }
            i1 += j2;
            for (int l2 = j2; l2 < 128; l2++) {
                int strength = flameStrengths[i1++];
                if (strength != 0) {
                    int on = strength;
                    int off = 256 - strength;
                    int colour = flameColours.getCurrentColour(strength);
                    int bg = flameLeftBackground.pixels[pos];
                    flameLeftBackground.pixels[pos++] = ((colour & 0xFF00ff) * on + (bg & 0xFF00FF) * off & 0xFF00FF00)
                            + ((colour & 0xFF00) * on + (bg & 0xFF00) * off & 0xFF0000) >> 8;
                } else {
                    pos++;
                }
            }

            pos += j2;
        }

        flameLeftBackground.drawGraphics(0, gameGraphics, 0);

        System.arraycopy(flameRightBackground2.pixels, 0, flameRightBackground.pixels, 0, 33920);

        i1 = 0;
        pos = 1176;
        for (int k2 = 1; k2 < c - 1; k2++) {
            int i3 = (anIntArray969[k2] * (c - k2)) / c;
            if (i3 != 0) {
                System.out.println(i3);
            }
            int k3 = 103 - i3;
            pos += i3;
            for (int i4 = 0; i4 < k3; i4++) {
                int strength = flameStrengths[i1++];
                if (strength != 0) {
                    int on = strength;
                    int off = 256 - strength;
                    int colour = flameColours.getCurrentColour(strength);
                    int bg = flameRightBackground.pixels[pos];
                    flameRightBackground.pixels[pos++] = ((colour & 0xFF00FF) * on + (bg & 0xFF00FF) * off & 0xFF00FF00)
                            + ((colour & 0xFF00) * on + (bg & 0xFF00) * off & 0xFF0000) >> 8;
                } else {
                    pos++;
                }
            }

            i1 += 128 - k3;
            pos += 128 - k3 - i3;
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
            for (int column = 0; column < sprite.width; column++) {
                modifiedPixels[column] = sprite.pixels[(sprite.width - column - 1) + sprite.width * row];
            }

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

    private void updateFlameStrength(int tick) {
        char c = '\u0100';

        for (int j = 10; j < 117; j++) {
            int k = (int) (Math.random() * 100D);
            if (k < 50) {
                flameStrengths[j + (c - 2 << 7)] = 255;
            }
        }

        for (int i = 0; i < 100; i++) {
            int y = (int) (Math.random() * 124D) + 2;
            int x = (int) (Math.random() * 128D) + 128;
            int pos = y + (x << 7);
            flameStrengths[pos] = 192;
        }

        for (int x = 1; x < c - 1; x++) {
            for (int y = 1; y < 127; y++) {
                int pos = y + (x << 7);
                anIntArray829[pos] = (flameStrengths[pos - 1] + flameStrengths[pos + 1] + flameStrengths[pos - 128] + flameStrengths[pos + 128]) / 4;
            }
        }

        flameShapeIndex += 128;
        if (flameShapeIndex > titleFlames.length) {
            flameShapeIndex -= titleFlames.length;

            int image = (int) (Math.random() * 12D);
            updateFlameShape(titleFlameRuneImages[image]);
        }

        for (int x = 1; x < c - 1; x++) {
            for (int y = 1; y < 127; y++) {
                int pos = y + (x << 7);
                int i4 = anIntArray829[pos + 128] - titleFlames[pos + flameShapeIndex & titleFlames.length - 1] / 5;

                if (i4 < 0) {
                    i4 = 0;
                }

                flameStrengths[pos] = i4;
            }
        }

        System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

        anIntArray969[c - 1] = (int) (Math.sin(tick / 14D) * 16D + Math.sin(tick / 15D) * 14D + Math.sin(tick / 16D) * 12D);
    }

    public void drawFlames2(Graphics gameGraphics, int tick) {
        drawingFlames = true;
        try {
            long startTime = System.currentTimeMillis();
            int currentLoop = 0;
            int interval = 20;
            while (currentlyDrawingFlames) {
                updateFlameStrength(tick);
                updateFlameStrength(tick);
                doFlamesDrawing(gameGraphics);
                if (++currentLoop > 10) {
                    long currentTime = System.currentTimeMillis();
                    int difference = (int) (currentTime - startTime) / 10 - interval;
                    interval = 40 - difference;
                    if (interval < 5) {
                        interval = 5;
                    }
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

    private void updateFlameShape(IndexedImage runeImage) {
        int j = 256;
        for (int pos = 0; pos < titleFlames.length; pos++) {
            titleFlames[pos] = 0;
        }

        for (int i = 0; i < 5000; i++) {
            int pos = (int) (Math.random() * 128D * j);
            titleFlames[pos] = (int) (Math.random() * 256D);
        }

        for (int i = 0; i < 20; i++) {
            for (int x = 1; x < j - 1; x++) {
                for (int y = 1; y < 127; y++) {
                    int pos = y + (x << 7);
                    titleFlamesTemp[pos] = (
                            titleFlames[pos - 1]
                                    + titleFlames[pos + 1]
                                    + titleFlames[pos - 128]
                                    + titleFlames[pos + 128]
                    ) / 4;
                }
            }

            int temp[] = titleFlames;
            titleFlames = titleFlamesTemp;
            titleFlamesTemp = temp;
        }

        if (runeImage != null) {
            int imagePos = 0;
            for (int x = 0; x < runeImage.height; x++) {
                for (int y = 0; y < runeImage.width; y++) {
                    if (runeImage.pixels[imagePos++] != 0) {
                        int _y = y + 16 + runeImage.drawOffsetX;
                        int _x = x + 16 + runeImage.drawOffsetY;
                        int pos = _y + (_x << 7);
                        titleFlames[pos] = 0;
                    }
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
}
