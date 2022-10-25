package com.jagex.runescape.screen.title;

import com.jagex.runescape.*;
import com.jagex.runescape.definition.GameFont;

import java.awt.*;

public class TitleScreen {
    private final int[] anIntArray969;
    private RSImageProducer loginBoxLeftBackgroundTile;
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
    private GameFont fontSmall;
    private GameFont fontBold;
    private int[] flameStrengths;
    private int[] anIntArray829;
    private final FlameColours flameColours;

    public TitleScreen() {
        this.anIntArray969 = new int[256];
        this.welcomeScreenRaised = false;
        this.drawingFlames = false;
        this.currentlyDrawingFlames = false;

        this.flameColours = new FlameColours();
    }

    public void load(final Component component, final Archive archive, final GameFont fontSmall, final GameFont fontPlain, final GameFont fontBold) {
        Archive archive1 = archive;
        this.fontSmall = fontSmall;
        GameFont fontPlain1 = fontPlain;
        this.fontBold = fontBold;

        this.setupSprites(component, archive);

        this.titleBoxImage = new IndexedImage(archive, "titlebox", 0);
        this.titleButtonImage = new IndexedImage(archive, "titlebutton", 0);
        this.titleFlameRuneImages = new IndexedImage[12];

        for (int r = 0; r < 12; r++) {
            this.titleFlameRuneImages[r] = new IndexedImage(archive, "runes", r);
        }

        this.flameLeftBackground2 = new Sprite(128, 265);
        this.flameRightBackground2 = new Sprite(128, 265);
        System.arraycopy(this.flameLeftBackground.pixels, 0, this.flameLeftBackground2.pixels, 0, 33920);

        System.arraycopy(this.flameRightBackground.pixels, 0, this.flameRightBackground2.pixels, 0, 33920);

        this.titleFlames = new int[32768];
        this.titleFlamesTemp = new int[32768];

        this.flameStrengths = new int[32768];
        this.anIntArray829 = new int[32768];

        this.updateFlameShape(null);
    }

    public void drawLoadingText(final Graphics gameGraphics, final int percentage, final String text) {
        this.loginBoxLeftBackgroundTile.initDrawingArea();

        final int horizontalOffset = 360;
        final int verticalOffset1 = 200;
        final int verticalOffset2 = 20;
        this.fontBold.drawCentredText("RuneScape is loading - please wait...", horizontalOffset / 2,
            verticalOffset1 / 2 - 26 - verticalOffset2, 0xFFFFFF);
        final int loadingBarHeight = verticalOffset1 / 2 - 18 - verticalOffset2;

        DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 152, 304, 34, 0x8C1111, loadingBarHeight);
        DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 151, 302, 32, 0, loadingBarHeight + 1);
        DrawingArea.drawFilledRectangle(horizontalOffset / 2 - 150, loadingBarHeight + 2, percentage * 3, 30, 0x8C1111);
        DrawingArea.drawFilledRectangle((horizontalOffset / 2 - 150) + percentage * 3, loadingBarHeight + 2,
            300 - percentage * 3, 30, 0);
        this.fontBold.drawCentredText(text, horizontalOffset / 2, (verticalOffset1 / 2 + 5) - verticalOffset2, 0xFFFFFF);
        this.loginBoxLeftBackgroundTile.drawGraphics(171, gameGraphics, 202);

        if (this.welcomeScreenRaised) {
            this.welcomeScreenRaised = false;
            if (!this.currentlyDrawingFlames) {
                this.drawFlames(gameGraphics);
            }

            this.drawTiles(gameGraphics);
        }
    }

    public void setupImageProducers(final Component gameComponent) {
        this.flameLeftBackground = new RSImageProducer(128, 265, gameComponent);
        DrawingArea.clear();
        this.flameRightBackground = new RSImageProducer(128, 265, gameComponent);
        DrawingArea.clear();
        this.topCentreBackgroundTile = new RSImageProducer(509, 171, gameComponent);
        DrawingArea.clear();
        this.bottomCentreBackgroundTile = new RSImageProducer(360, 132, gameComponent);
        DrawingArea.clear();
        this.loginBoxLeftBackgroundTile = new RSImageProducer(360, 200, gameComponent);
        DrawingArea.clear();
        this.bottomLeftBackgroundTile = new RSImageProducer(202, 238, gameComponent);
        DrawingArea.clear();
        this.bottomRightBackgroundTile = new RSImageProducer(203, 238, gameComponent);
        DrawingArea.clear();
        this.middleLeftBackgroundTile = new RSImageProducer(74, 94, gameComponent);
        DrawingArea.clear();
        this.middleRightBackgroundTile = new RSImageProducer(75, 94, gameComponent);
        DrawingArea.clear();
    }

    public void clearImageProducers() {
        this.topCentreBackgroundTile = null;
        this.bottomCentreBackgroundTile = null;
        this.loginBoxLeftBackgroundTile = null;
        this.flameLeftBackground = null;
        this.flameRightBackground = null;
        this.bottomLeftBackgroundTile = null;
        this.bottomRightBackgroundTile = null;
        this.middleLeftBackgroundTile = null;
        this.middleRightBackgroundTile = null;
    }

    public boolean imageProducersInitialised() {
        return this.topCentreBackgroundTile != null;
    }

    public void nullLoader() {
        this.titleBoxImage = null;
        this.titleButtonImage = null;
        this.titleFlameRuneImages = null;
        this.titleFlames = null;
        this.titleFlamesTemp = null;
        this.flameLeftBackground2 = null;
        this.flameRightBackground2 = null;

        this.flameStrengths = null;
        this.anIntArray829 = null;

        this.clearImageProducers();
    }

    private void doFlamesDrawing(final Graphics gameGraphics) {
        final char c = '\u0100';

        this.flameColours.changeColours();

        System.arraycopy(this.flameLeftBackground2.pixels, 0, this.flameLeftBackground.pixels, 0, 33920);

        int i1 = 0;
        int pos = 1152;
        for (int k1 = 1; k1 < c - 1; k1++) {
            final int l1 = (this.anIntArray969[k1] * (c - k1)) / c;

            int j2 = 22 + l1;
            if (j2 < 0) {
                j2 = 0;
            }
            i1 += j2;
            for (int l2 = j2; l2 < 128; l2++) {
                final int strength = this.flameStrengths[i1++];
                if (strength != 0) {
                    final int off = 256 - strength;
                    final int colour = this.flameColours.getCurrentColour(strength);
                    final int bg = this.flameLeftBackground.pixels[pos];
                    this.flameLeftBackground.pixels[pos++] = ((colour & 0xFF00ff) * strength + (bg & 0xFF00FF) * off & 0xFF00FF00)
                        + ((colour & 0xFF00) * strength + (bg & 0xFF00) * off & 0xFF0000) >> 8;
                } else {
                    pos++;
                }
            }

            pos += j2;
        }

        this.flameLeftBackground.drawGraphics(0, gameGraphics, 0);

        System.arraycopy(this.flameRightBackground2.pixels, 0, this.flameRightBackground.pixels, 0, 33920);

        i1 = 0;
        pos = 1176;
        for (int k2 = 1; k2 < c - 1; k2++) {
            final int i3 = (this.anIntArray969[k2] * (c - k2)) / c;
            final int k3 = 103 - i3;
            pos += i3;
            for (int i4 = 0; i4 < k3; i4++) {
                final int strength = this.flameStrengths[i1++];
                if (strength != 0) {
                    final int off = 256 - strength;
                    final int colour = this.flameColours.getCurrentColour(strength);
                    final int bg = this.flameRightBackground.pixels[pos];
                    this.flameRightBackground.pixels[pos++] = ((colour & 0xFF00FF) * strength + (bg & 0xFF00FF) * off & 0xFF00FF00)
                        + ((colour & 0xFF00) * strength + (bg & 0xFF00) * off & 0xFF0000) >> 8;
                } else {
                    pos++;
                }
            }

            i1 += 128 - k3;
            pos += 128 - k3 - i3;
        }

        this.flameRightBackground.drawGraphics(0, gameGraphics, 637);
    }

    private void setupSprites(final Component component, final Archive archive) {
        final byte[] titleData = archive.decompressFile("title.dat");
        Sprite sprite = new Sprite(titleData, component);

        this.flameLeftBackground.initDrawingArea();
        sprite.drawInverse(0, 0);
        this.flameRightBackground.initDrawingArea();
        sprite.drawInverse(-637, 0);
        this.topCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(-128, 0);
        this.bottomCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(-202, -371);
        this.loginBoxLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(-202, -171);
        this.bottomLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(0, -265);
        this.bottomRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-562, -265);
        this.middleLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(-128, -171);
        this.middleRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-562, -171);

        final int[] modifiedPixels = new int[sprite.width];
        for (int row = 0; row < sprite.height; row++) {
            for (int column = 0; column < sprite.width; column++) {
                modifiedPixels[column] = sprite.pixels[(sprite.width - column - 1) + sprite.width * row];
            }

            System.arraycopy(modifiedPixels, 0, sprite.pixels, sprite.width * row, sprite.width);

        }

        this.flameLeftBackground.initDrawingArea();
        sprite.drawInverse(382, 0);
        this.flameRightBackground.initDrawingArea();
        sprite.drawInverse(-255, 0);
        this.topCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(254, 0);
        this.bottomCentreBackgroundTile.initDrawingArea();
        sprite.drawInverse(180, -371);
        this.loginBoxLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(180, -171);
        this.bottomLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(382, -265);
        this.bottomRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-180, -265);
        this.middleLeftBackgroundTile.initDrawingArea();
        sprite.drawInverse(254, -171);
        this.middleRightBackgroundTile.initDrawingArea();
        sprite.drawInverse(-180, -171);
        sprite = new Sprite(archive, "logo", 0);
        this.topCentreBackgroundTile.initDrawingArea();
        sprite.drawImage(382 - sprite.width / 2 - 128, 18);
    }

    private void drawFlames(final Graphics gameGraphics) {
        this.flameLeftBackground.drawGraphics(0, gameGraphics, 0);
        this.flameRightBackground.drawGraphics(0, gameGraphics, 637);
    }

    private void drawTiles(final Graphics gameGraphics) {
        this.topCentreBackgroundTile.drawGraphics(0, gameGraphics, 128);
        this.bottomCentreBackgroundTile.drawGraphics(371, gameGraphics, 202);
        this.bottomLeftBackgroundTile.drawGraphics(265, gameGraphics, 0);
        this.bottomRightBackgroundTile.drawGraphics(265, gameGraphics, 562);
        this.middleLeftBackgroundTile.drawGraphics(171, gameGraphics, 128);
        this.middleRightBackgroundTile.drawGraphics(171, gameGraphics, 562);
    }

    private void updateFlameStrength(final int tick) {
        final char c = '\u0100';

        for (int j = 10; j < 117; j++) {
            final int k = (int) (Math.random() * 100D);
            if (k < 50) {
                this.flameStrengths[j + (c - 2 << 7)] = 255;
            }
        }

        for (int i = 0; i < 100; i++) {
            final int y = (int) (Math.random() * 124D) + 2;
            final int x = (int) (Math.random() * 128D) + 128;
            final int pos = y + (x << 7);
            this.flameStrengths[pos] = 192;
        }

        for (int x = 1; x < c - 1; x++) {
            for (int y = 1; y < 127; y++) {
                final int pos = y + (x << 7);
                this.anIntArray829[pos] = (this.flameStrengths[pos - 1] + this.flameStrengths[pos + 1] + this.flameStrengths[pos - 128] + this.flameStrengths[pos + 128]) / 4;
            }
        }

        this.flameShapeIndex += 128;
        if (this.flameShapeIndex > this.titleFlames.length) {
            this.flameShapeIndex -= this.titleFlames.length;

            final int image = (int) (Math.random() * 12D);
            this.updateFlameShape(this.titleFlameRuneImages[image]);
        }

        for (int x = 1; x < c - 1; x++) {
            for (int y = 1; y < 127; y++) {
                final int pos = y + (x << 7);
                int i4 = this.anIntArray829[pos + 128] - this.titleFlames[pos + this.flameShapeIndex & this.titleFlames.length - 1] / 5;

                if (i4 < 0) {
                    i4 = 0;
                }

                this.flameStrengths[pos] = i4;
            }
        }

        System.arraycopy(this.anIntArray969, 1, this.anIntArray969, 0, c - 1);

        this.anIntArray969[c - 1] = (int) (Math.sin(tick / 14D) * 16D + Math.sin(tick / 15D) * 14D + Math.sin(tick / 16D) * 12D);
    }

    public void drawFlames2(final Graphics gameGraphics, final int tick) {
        this.drawingFlames = true;
        try {
            long startTime = System.currentTimeMillis();
            int currentLoop = 0;
            int interval = 20;
            while (this.currentlyDrawingFlames) {
                this.updateFlameStrength(tick);
                this.updateFlameStrength(tick);
                this.doFlamesDrawing(gameGraphics);
                if (++currentLoop > 10) {
                    final long currentTime = System.currentTimeMillis();
                    final int difference = (int) (currentTime - startTime) / 10 - interval;
                    interval = 40 - difference;
                    if (interval < 5) {
                        interval = 5;
                    }
                    currentLoop = 0;
                    startTime = currentTime;
                }
                try {
                    Thread.sleep(interval);
                } catch (final Exception _ex) {
                }
            }
        } catch (final Exception _ex) {
        }
        this.drawingFlames = false;
    }

    private void updateFlameShape(final IndexedImage runeImage) {
        final int j = 256;
        for (int pos = 0; pos < this.titleFlames.length; pos++) {
            this.titleFlames[pos] = 0;
        }

        for (int i = 0; i < 5000; i++) {
            final int pos = (int) (Math.random() * 128D * j);
            this.titleFlames[pos] = (int) (Math.random() * 256D);
        }

        for (int i = 0; i < 20; i++) {
            for (int x = 1; x < j - 1; x++) {
                for (int y = 1; y < 127; y++) {
                    final int pos = y + (x << 7);
                    this.titleFlamesTemp[pos] = (
                        this.titleFlames[pos - 1]
                            + this.titleFlames[pos + 1]
                            + this.titleFlames[pos - 128]
                            + this.titleFlames[pos + 128]
                    ) / 4;
                }
            }

            final int[] temp = this.titleFlames;
            this.titleFlames = this.titleFlamesTemp;
            this.titleFlamesTemp = temp;
        }

        if (runeImage != null) {
            int imagePos = 0;
            for (int x = 0; x < runeImage.height; x++) {
                for (int y = 0; y < runeImage.width; y++) {
                    if (runeImage.pixels[imagePos++] != 0) {
                        final int _y = y + 16 + runeImage.drawOffsetX;
                        final int _x = x + 16 + runeImage.drawOffsetY;
                        final int pos = _y + (_x << 7);
                        this.titleFlames[pos] = 0;
                    }
                }
            }
        }
    }

    public void drawLoginScreen(
        final Graphics gameGraphics,
        final boolean originalLoginScreen,
        final int loginScreenState,
        final String statusString,
        final String message1,
        final String message2,
        final String enteredUsername,
        final String enteredPassword,
        final int tick,
        final int focus
    ) {
        this.loginBoxLeftBackgroundTile.initDrawingArea();
        this.titleBoxImage.draw(0, 0);
        final int x = 360;
        final int y = 200;
        if (loginScreenState == 0) {
            int _y = y / 2 + 80;
            this.fontSmall.drawCentredTextWithPotentialShadow(statusString, x / 2, _y, 0x75A9A9, true);
            _y = y / 2 - 20;
            this.fontBold.drawCentredTextWithPotentialShadow("Welcome to RuneScape", x / 2, _y, 0xFFFF00, true);
            _y += 30;
            int _x = x / 2 - 80;
            final int __y = y / 2 + 20;
            this.titleButtonImage.draw(_x - 73, __y - 20);
            this.fontBold.drawCentredTextWithPotentialShadow("New User", _x, __y + 5, 0xFFFFFF, true);
            _x = x / 2 + 80;
            this.titleButtonImage.draw(_x - 73, __y - 20);
            this.fontBold.drawCentredTextWithPotentialShadow("Existing User", _x, __y + 5, 0xFFFFFF, true);
        }
        if (loginScreenState == 2) {
            int _y = y / 2 - 40;
            if (message1.length() > 0) {
                this.fontBold.drawCentredTextWithPotentialShadow(message1, x / 2, _y - 15, 0xFFFF00, true);
                this.fontBold.drawCentredTextWithPotentialShadow(message2, x / 2, _y, 0xFFFF00, true);
                _y += 30;
            } else {
                this.fontBold.drawCentredTextWithPotentialShadow(message2, x / 2, _y - 7, 0xFFFF00, true);
                _y += 30;
            }
            this.fontBold.drawTextWithPotentialShadow(
                "Username: " + enteredUsername + ((focus == 0) & (tick % 40 < 20) ? "@yel@|" : ""),
                x / 2 - 90, _y, 0xFFFFFF, true);
            _y += 15;
            this.fontBold.drawTextWithPotentialShadow(
                "Password: " + TextClass.asterisksForString(enteredPassword)
                    + ((focus == 1) & (tick % 40 < 20) ? "@yel@|" : ""),
                x / 2 - 88, _y, 0xFFFFFF, true);
            _y += 15;
            if (!originalLoginScreen) {
                int _x = x / 2 - 80;
                final int __y = y / 2 + 50;
                this.titleButtonImage.draw(_x - 73, __y - 20);
                this.fontBold.drawCentredTextWithPotentialShadow("Login", _x, __y + 5, 0xFFFFFF, true);
                _x = x / 2 + 80;
                this.titleButtonImage.draw(_x - 73, __y - 20);
                this.fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xFFFFFF, true);
            }
        }
        if (loginScreenState == 3) {
            this.fontBold.drawCentredTextWithPotentialShadow("Create a free account", x / 2, y / 2 - 60, 0xFFFF00, true);
            int _y = y / 2 - 35;
            this.fontBold.drawCentredTextWithPotentialShadow("To create a new account you need to", x / 2, _y, 0xFFFFFF,
                true);
            _y += 15;
            this.fontBold.drawCentredTextWithPotentialShadow("go back to the main RuneScape webpage", x / 2, _y, 0xFFFFFF,
                true);
            _y += 15;
            this.fontBold.drawCentredTextWithPotentialShadow("and choose the red 'create account'", x / 2, _y, 0xFFFFFF,
                true);
            _y += 15;
            this.fontBold.drawCentredTextWithPotentialShadow("button at the top right of that page.", x / 2, _y, 0xFFFFFF,
                true);
            _y += 15;
            final int _x = x / 2;
            final int __y = y / 2 + 50;
            this.titleButtonImage.draw(_x - 73, __y - 20);
            this.fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xFFFFFF, true);
        }
        this.loginBoxLeftBackgroundTile.drawGraphics(171, gameGraphics, 202);
        if (this.welcomeScreenRaised) {
            this.welcomeScreenRaised = false;
            this.topCentreBackgroundTile.drawGraphics(0, gameGraphics, 128);
            this.bottomCentreBackgroundTile.drawGraphics(371, gameGraphics, 202);
            this.bottomLeftBackgroundTile.drawGraphics(265, gameGraphics, 0);
            this.bottomRightBackgroundTile.drawGraphics(265, gameGraphics, 562);
            this.middleLeftBackgroundTile.drawGraphics(171, gameGraphics, 128);
            this.middleRightBackgroundTile.drawGraphics(171, gameGraphics, 562);
        }
    }
}
