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

@SuppressWarnings("serial")
final class RSFrame extends Frame {

	private final RSApplet applet;

	public RSFrame(RSApplet applet, int width, int height) {
		this.applet = applet;
		setTitle("Jagex");
		setResizable(false);
		setVisible(true);
		toFront();
		setSize(width + 8, height + 28);
	}

	@Override
	public Graphics getGraphics() {
		Graphics graphics = super.getGraphics();
		graphics.translate(4, 24);
		return graphics;
	}

	@Override
	public void paint(Graphics graphics) {
		applet.paint(graphics);
	}

	@Override
	public void update(Graphics graphics) {
		applet.update(graphics);
	}
}
