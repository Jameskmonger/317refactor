package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

import java.awt.*;
import java.awt.image.*;

final class RSImageProducer
        implements ImageProducer, ImageObserver
{

    public RSImageProducer(int width, int height, Component component)
    {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        colourModel = new DirectColorModel(32, 0xff0000, 65280, 255);
        image = component.createImage(this);
        drawPixels();
        component.prepareImage(image, this);
        drawPixels();
        component.prepareImage(image, this);
        drawPixels();
        component.prepareImage(image, this);
        initDrawingArea();
    }

    public void initDrawingArea()
    {
        DrawingArea.initDrawingArea(height, width, pixels);
    }

    public void drawGraphics(int y, Graphics g, int x)
    {
        drawPixels();
        g.drawImage(image, x, y, this);
    }

    public synchronized void addConsumer(ImageConsumer imageConsumer)
    {
        this.imageConsumer = imageConsumer;
        imageConsumer.setDimensions(width, height);
        imageConsumer.setProperties(null);
        imageConsumer.setColorModel(colourModel);
        imageConsumer.setHints(14);
    }

    public synchronized boolean isConsumer(ImageConsumer imageconsumer)
    {
        return imageConsumer == imageconsumer;
    }

    public synchronized void removeConsumer(ImageConsumer imageconsumer)
    {
        if(imageConsumer == imageconsumer)
            imageConsumer = null;
    }

    public void startProduction(ImageConsumer imageconsumer)
    {
        addConsumer(imageconsumer);
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer)
    {
        System.out.println("TDLR");
    }

    private synchronized void drawPixels()
    {
        if(imageConsumer != null)
        {
            imageConsumer.setPixels(0, 0, width, height, colourModel, pixels, 0, width);
            imageConsumer.imageComplete(2);
        }
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1)
    {
        return true;
    }

    public final int[] pixels;
    public final int width;
    public final int height;
    private final ColorModel colourModel;
    private ImageConsumer imageConsumer;
    private final Image image;
}
