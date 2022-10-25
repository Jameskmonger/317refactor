package com.jagex.runescape;

import java.awt.*;

@SuppressWarnings("serial")
final class RSFrame extends Frame {

    private final RSApplet applet;

    public RSFrame(final RSApplet applet, final int width, final int height) {
        this.applet = applet;
        this.setTitle("Jagex");
        this.setResizable(false);
        this.setVisible(true);
        this.toFront();
        this.setSize(width + 8, height + 28);
    }

    @Override
    public Graphics getGraphics() {
        final Graphics graphics = super.getGraphics();
        graphics.translate(4, 24);
        return graphics;
    }

    @Override
    public void paint(final Graphics graphics) {
        this.applet.paint(graphics);
    }

    @Override
    public void update(final Graphics graphics) {
        this.applet.update(graphics);
    }
}
