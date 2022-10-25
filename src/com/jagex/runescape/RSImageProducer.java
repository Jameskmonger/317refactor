package com.jagex.runescape;

import java.awt.*;
import java.awt.image.*;

public final class RSImageProducer implements ImageProducer, ImageObserver {

    public final int[] pixels;

    private final int width;

    private final int height;

    private final ColorModel colourModel;

    private ImageConsumer imageConsumer;

    private final Image image;

    public RSImageProducer(final int width, final int height, final Component component) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        this.colourModel = new DirectColorModel(32, 0xff0000, 65280, 255);
        this.image = component.createImage(this);
        this.drawPixels();
        component.prepareImage(this.image, this);
        this.drawPixels();
        component.prepareImage(this.image, this);
        this.drawPixels();
        component.prepareImage(this.image, this);
        this.initDrawingArea();
    }

    @Override
    public synchronized void addConsumer(final ImageConsumer imageConsumer) {
        this.imageConsumer = imageConsumer;
        imageConsumer.setDimensions(this.width, this.height);
        imageConsumer.setProperties(null);
        imageConsumer.setColorModel(this.colourModel);
        imageConsumer.setHints(14);
    }

    public void drawGraphics(final int y, final Graphics g, final int x) {
        this.drawPixels();
        g.drawImage(this.image, x, y, this);
    }

    private synchronized void drawPixels() {
        if (this.imageConsumer != null) {
            this.imageConsumer.setPixels(0, 0, this.width, this.height, this.colourModel, this.pixels, 0, this.width);
            this.imageConsumer.imageComplete(2);
        }
    }

    @Override
    public boolean imageUpdate(final Image image, final int i, final int j, final int k, final int l, final int i1) {
        return true;
    }

    public void initDrawingArea() {
        DrawingArea.initDrawingArea(this.height, this.width, this.pixels);
    }

    @Override
    public synchronized boolean isConsumer(final ImageConsumer imageconsumer) {
        return this.imageConsumer == imageconsumer;
    }

    @Override
    public synchronized void removeConsumer(final ImageConsumer imageconsumer) {
        if (this.imageConsumer == imageconsumer) {
            this.imageConsumer = null;
        }
    }

    @Override
    public void requestTopDownLeftRightResend(final ImageConsumer imageconsumer) {
        System.out.println("TDLR");
    }

    @Override
    public void startProduction(final ImageConsumer imageconsumer) {
        this.addConsumer(imageconsumer);
    }
}
