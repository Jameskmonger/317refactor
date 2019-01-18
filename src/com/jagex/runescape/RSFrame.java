package com.jagex.runescape;

import java.awt.*;

@SuppressWarnings("serial")
final class RSFrame extends Frame {

	private final RSApplet applet;

	public RSFrame(RSApplet applet, int width, int height) {
		this.applet = applet;
		this.setTitle("Jagex");
		this.setResizable(false);
		this.setVisible(true);
		this.toFront();
		this.setSize(width + 8, height + 28);
	}

	@Override
	public Graphics getGraphics() {
		Graphics graphics = super.getGraphics();
		graphics.translate(4, 24);
		return graphics;
	}

	@Override
	public void paint(Graphics graphics) {
        this.applet.paint(graphics);
	}

	@Override
	public void update(Graphics graphics) {
        this.applet.update(graphics);
	}
}
