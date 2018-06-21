package com.jagex.runescape;

import java.util.Random;

public final class GameFont extends DrawingArea {

	/*
	 * Referred to as a 'glyph' rather than a 'character' as a character remains the
	 * same between fonts ('a' is always 'a') however a glyph is a representation of
	 * a character within a font.
	 * 
	 * http://graphicdesign.stackexchange.com/questions/13438/whats-the-practical-
	 * difference-between-a-glyph-and-a-character
	 */

	/*
	 * An array containing the pixels used in the glyph. Stored as either 1 or 0
	 * signifying whether that pixel should be drawn or not.
	 */
	private final byte[][] glyphPixels;
	private final int[] glyphWidth;
	private final int[] glyphHeight;

	/*
	 * The spacing (kerning), horizontal and vertical, between glyphs in a font.
	 * 
	 * http://en.wikipedia.org/wiki/Kerning
	 */
	private final int[] horizontalKerning;
	private final int[] verticalKerning;

	private final int[] glyphDisplayWidth;

	public int fontHeight;

	private final Random random;

	private boolean strikethrough;

	/**
	 * Initialise a GameFont.
	 * 
	 * @param name      The name of the font.
	 * 
	 * @param archive   The archive containing the information about the font.
	 * 
	 * @param monospace Is the font monospace?
	 */
	public GameFont(String name, Archive archive, boolean monospace) {
		glyphPixels = new byte[256][];
		glyphWidth = new int[256];
		glyphHeight = new int[256];
		horizontalKerning = new int[256];
		verticalKerning = new int[256];
		glyphDisplayWidth = new int[256];
		random = new Random();
		strikethrough = false;

		/*
		 * The buffer containing data about this specific font. The position of the font
		 * within the parent archive (index.dat) and the pixel data for the glyph.
		 */
		Buffer glyphData = new Buffer(archive.decompressFile(name + ".dat"));

		/*
		 * Stores the information about the glyphs, such as the kerning and the
		 * dimensons.
		 */
		Buffer glyphInformation = new Buffer(archive.decompressFile("index.dat"));

		/*
		 * Find the glyph information within the parent archive.
		 */
		glyphInformation.position = glyphData.getUnsignedLEShort() + 4;

		/*
		 * Find the glyph data for this font within the parent archive.
		 */
		int startPosition = glyphInformation.getUnsignedByte();
		if (startPosition > 0)
			glyphInformation.position += 3 * (startPosition - 1);

		/*
		 * Get the data for each glyph.
		 */
		for (int g = 0; g < 256; g++) {
			horizontalKerning[g] = glyphInformation.getUnsignedByte();
			verticalKerning[g] = glyphInformation.getUnsignedByte();
			int width = glyphWidth[g] = glyphInformation.getUnsignedLEShort();
			int height = glyphHeight[g] = glyphInformation.getUnsignedLEShort();

			/*
			 * Is the glyph rectangular?
			 */
			int rectangular = glyphInformation.getUnsignedByte();

			/*
			 * Get the area of the glyph.
			 */
			int area = width * height;

			/*
			 * Initialise the pixels for this glyph.
			 */
			glyphPixels[g] = new byte[area];

			/*
			 * Set the pixels for the glyph based on whether it is square or rectangular.
			 */
			if (rectangular == 0) {
				for (int p = 0; p < area; p++) {
					glyphPixels[g][p] = glyphData.get();
				}
			} else if (rectangular == 1) {
				for (int w = 0; w < width; w++) {
					for (int h = 0; h < height; h++) {
						glyphPixels[g][w + h * width] = glyphData.get();
					}
				}
			}

			/*
			 * If the height of this glyph is higher than the highest glyph we've currently
			 * processed for this font, store this glyph's height as the highest.
			 * 
			 * 126 is the last visible character used in this client (~). No support for
			 * accented characters!
			 */
			if (height > fontHeight && g < 128)
				fontHeight = height;

			horizontalKerning[g] = 1;
			glyphDisplayWidth[g] = width + 2;

			int activePixels = 0;
			for (int h = height / 7; h < height; h++)
				activePixels += glyphPixels[g][h * width];

			if (activePixels <= height / 7) {
				glyphDisplayWidth[g]--;
				horizontalKerning[g] = 0;
			}

			activePixels = 0;
			for (int h = height / 7; h < height; h++)
				activePixels += glyphPixels[g][(width - 1) + h * width];

			if (activePixels <= height / 7)
				glyphDisplayWidth[g]--;
		}

		/*
		 * Character 32 is space, character 73 is uppercase I and character 105 is
		 * lowercase i.
		 */
		if (monospace) {
			glyphDisplayWidth[32] = glyphDisplayWidth[73];
		} else {
			glyphDisplayWidth[32] = glyphDisplayWidth[105];
		}
	}

	/**
	 * Renders an array of pixels into an output buffer with a specified colour.
	 * 
	 * @param input          The array of pixel states (1 if drawn, 0 if not drawn.)
	 * 
	 * @param inputPosition  The position in the input array to start at.
	 * 
	 * @param inputWidth     The amount to increase the position in the input array
	 *                       by after each iteration.
	 * 
	 * @param output         The array to store the coloured pixels in.
	 * 
	 * @param outputPosition The position in the output array to start at.
	 * 
	 * @param outputWidth    The amount to increase the position in the output array
	 *                       by after each iteration.
	 * 
	 * @param width          The width of the group of pixels.
	 * 
	 * @param height         The height of the group of pixels.
	 * 
	 * @param colour         The colour to draw the pixels in.
	 */
	private void render(byte input[], int inputPosition, int inputWidth, int output[], int outputPosition,
			int outputWidth, int width, int height, int colour) {
		int _width = -(width >> 2);
		width = -(width & 3);

		/*
		 * Iterate through the pixels.
		 */
		for (int row = -height; row < 0; row++) {
			for (int col = _width; col < 0; col++) {
				/*
				 * If the pixel in the current position is set, draw it with a given colour. If
				 * not, simply leave a space.
				 */

				if (input[inputPosition++] != 0)
					output[outputPosition++] = colour;
				else
					outputPosition++;
				if (input[inputPosition++] != 0)
					output[outputPosition++] = colour;
				else
					outputPosition++;
				if (input[inputPosition++] != 0)
					output[outputPosition++] = colour;
				else
					outputPosition++;
				if (input[inputPosition++] != 0)
					output[outputPosition++] = colour;
				else
					outputPosition++;
			}

			for (int i = width; i < 0; i++)
				if (input[inputPosition++] != 0)
					output[outputPosition++] = colour;
				else
					outputPosition++;

			outputPosition += outputWidth;
			inputPosition += inputWidth;
		}
	}

	/**
	 * Draw a pixel with a given alpha value.
	 * 
	 * @param input          The array of pixel states (1 if drawn, 0 if not drawn.)
	 * 
	 * @param inputPosition  The position in the input array to start at.
	 * 
	 * @param inputWidth     The amount to increase the position in the input array
	 *                       by after each iteration.
	 * 
	 * @param output         The array to store the coloured pixels in.
	 * 
	 * @param outputPosition The position in the output array to start at.
	 * 
	 * @param outputWidth    The amount to increase the position in the output array
	 *                       by after each iteration.
	 * 
	 * @param width          The width of the group of pixels.
	 * 
	 * @param height         The height of the group of pixels.
	 * 
	 * @param colour         The colour to draw the pixels in.
	 * 
	 * @param alpha          The alpha value for the pixels.
	 */
	private void renderAlpha(byte input[], int inputPosition, int inputWidth, int output[], int outputPosition,
			int outputWidth, int width, int height, int colour, int alpha) {
		/*
		 * Calculate the colour and the alpha values.
		 */
		colour = ((colour & 0xff00ff) * alpha & 0xff00ff00) + ((colour & 0xff00) * alpha & 0xff0000) >> 8;
		alpha = 256 - alpha;

		/*
		 * Iterate through the pixels.
		 */
		for (int row = -height; row < 0; row++) {
			for (int col = -width; col < 0; col++) {
				/*
				 * If the pixel is set then draw it, if not simply leave a space.
				 */
				if (input[inputPosition++] != 0) {
					int outputColour = output[outputPosition];
					output[outputPosition++] = (((outputColour & 0xff00ff) * alpha & 0xff00ff00)
							+ ((outputColour & 0xff00) * alpha & 0xff0000) >> 8) + colour;
				} else {
					outputPosition++;
				}
			}
			outputPosition += outputWidth;
			inputPosition += inputWidth;
		}
	}

	/**
	 * Draws a glyph using given pixels.
	 * 
	 * @param glyphPixels The pixels of the glyph.
	 * 
	 * @param x           The x position of the glyph.
	 * 
	 * @param y           The y position of the glyph.
	 * 
	 * @param width       The width of the glyph.
	 * 
	 * @param height      The height of the glyph.
	 * 
	 * @param colour      The colour of the glyph.
	 */
	private void drawGlyph(byte glyphPixels[], int x, int y, int width, int height, int colour) {
		int outputPosition = x + y * DrawingArea.width;
		int outputWidth = DrawingArea.width - width;
		int inputWidth = 0;
		int inputPosition = 0;

		/*
		 * Calculate the height of the glyph.
		 */
		if (y < DrawingArea.topY) {
			int size = DrawingArea.topY - y;
			height -= size;
			y = DrawingArea.topY;
			inputPosition += size * width;
			outputPosition += size * DrawingArea.width;
		}
		if (y + height >= DrawingArea.bottomY) {
			height -= ((y + height) - DrawingArea.bottomY) + 1;
		}

		/*
		 * Calculate the width of the glyph.
		 */
		if (x < DrawingArea.topX) {
			int size = DrawingArea.topX - x;
			width -= size;
			x = DrawingArea.topX;
			inputPosition += size;
			outputPosition += size;
			inputWidth += size;
			outputWidth += size;
		}
		if (x + width >= DrawingArea.bottomX) {
			int size = ((x + width) - DrawingArea.bottomX) + 1;
			width -= size;
			inputWidth += size;
			outputWidth += size;
		}

		/*
		 * If the glyph is a valid size then render it.
		 */
		if (width > 0 && height > 0) {
			render(glyphPixels, inputPosition, inputWidth, DrawingArea.pixels, outputPosition, outputWidth, width,
					height, colour);
		}
	}

	private void drawGlyphAlpha(byte glyphPixels[], int x, int y, int width, int height, int colour, int alpha) {
		int outputPosition = x + y * DrawingArea.width;
		int outputWidth = DrawingArea.width - width;
		int inputWidth = 0;
		int inputPosition = 0;

		/*
		 * Calculate the height of the glyph.
		 */
		if (y < DrawingArea.topY) {
			int size = DrawingArea.topY - y;
			height -= size;
			y = DrawingArea.topY;
			inputPosition += size * width;
			outputPosition += size * DrawingArea.width;
		}
		if (y + height >= DrawingArea.bottomY)
			height -= ((y + height) - DrawingArea.bottomY) + 1;

		/*
		 * Calculate the width of the glyph.
		 */
		if (x < DrawingArea.topX) {
			int size = DrawingArea.topX - x;
			width -= size;
			x = DrawingArea.topX;
			inputPosition += size;
			outputPosition += size;
			inputWidth += size;
			outputWidth += size;
		}
		if (x + width >= DrawingArea.bottomX) {
			int size = ((x + width) - DrawingArea.bottomX) + 1;
			width -= size;
			inputWidth += size;
			outputWidth += size;
		}

		/*
		 * If the glyph is valid then render it!
		 */
		if (width > 0 && height > 0)
			renderAlpha(glyphPixels, inputPosition, inputWidth, DrawingArea.pixels, outputPosition, outputWidth, width,
					height, colour, alpha);
	}

	/**
	 * Draws a string, including a shadow behind it if specified.
	 * 
	 * Used for interface drawing where the text could be shadowed but also could be
	 * shadowless.
	 * 
	 * @param text          The text to draw.
	 * 
	 * @param x             The x position of the text.
	 * 
	 * @param y             The y position of the text.
	 * 
	 * @param currentColour The colour to draw the text.
	 * 
	 * @param shadowed      Whether the text is shadowed or not.
	 */
	public void drawTextWithPotentialShadow(String text, int x, int y, int currentColour, boolean shadowed) {
		strikethrough = false;
		int originalX = x;
		if (text == null)
			return;

		/*
		 * Draw from the top-left rather than the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through every character in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			/*
			 * Check if the current piece of text being processed is a colour for (@dre@ for
			 * example)
			 */
			if (text.charAt(c) == '@' && c + 4 < text.length() && text.charAt(c + 4) == '@') {
				int colour = handleEmbeddedEffect(text.substring(c + 1, c + 4));
				if (colour != -1)
					currentColour = colour;
				c += 4;
			} else {
				char character = text.charAt(c);

				/*
				 * If the character is a space, we don't need to draw it (but we do still offset
				 * the current x pointer using the glyph's width).
				 */
				if (character != ' ') {
					/*
					 * If the text has a shadow, we draw the shadow at a slight offset and then draw
					 * the normal coloured text on top of it.
					 */
					if (shadowed) {
						drawGlyph(glyphPixels[character], x + horizontalKerning[character] + 1,
								y + verticalKerning[character] + 1, glyphWidth[character], glyphHeight[character], 0);
					}
					drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character],
							glyphWidth[character], glyphHeight[character], currentColour);
				}
				x += glyphDisplayWidth[character];
			}
		}

		/*
		 * If the current piece of text has a strikethrough applied, draw a horizontal
		 * line from the start of the text to the current position.
		 */
		if (strikethrough) {
			DrawingArea.drawHorizontalLine(y + (int) (fontHeight * 0.7D), originalX, x - originalX, 0x800000);
		}
	}

	/**
	 * Draws centrally-aligned text, with a shadow if desired.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the text.
	 * 
	 * @param y      The y position of the text.
	 * 
	 * @param colour The colour of the text.
	 * 
	 * @param shadow Whether or not a shadow should be drawn.
	 */
	public void drawCentredTextWithPotentialShadow(String text, int x, int y, int colour, boolean shadow) {
		drawTextWithPotentialShadow(text, x - getTextDisplayedWidth(text) / 2, y, colour, shadow);
	}

	/**
	 * Draws text with a random opacity over a shadow.
	 *
	 * @param text          The text to draw.
	 * 
	 * @param x             The x position of the text.
	 * 
	 * @param y             The y position of the text.
	 * 
	 * @param currentColour The colour of the text.
	 * 
	 * @param seed          The seed for the random number generator.
	 */
	public void drawAlphaTextWithShadow(String text, int x, int y, int currentColour, int seed) {
		if (text == null)
			return;

		/*
		 * Set the seet for the RNG.
		 */
		random.setSeed(seed);

		/*
		 * Generate a random alpha value that is more opaque than the shadow (192 is the
		 * alpha value for the shadow).
		 */
		int alpha = 192 + (random.nextInt() & 0x1f);

		/*
		 * Draw from the top-left instead of the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through all the characters in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			/*
			 * Check if the current piece of text being processed is a colour for (@dre@ for
			 * example)
			 */
			if (text.charAt(c) == '@' && c + 4 < text.length() && text.charAt(c + 4) == '@') {
				int colour = handleEmbeddedEffect(text.substring(c + 1, c + 4));
				if (colour != -1)
					currentColour = colour;
				c += 4;
			} else {
				char character = text.charAt(c);

				/*
				 * If the character is a space, we don't draw it - we just add the width of the
				 * space to the current x position.
				 */
				if (character != ' ') {
					drawGlyphAlpha(glyphPixels[character], x + horizontalKerning[character] + 1,
							y + verticalKerning[character] + 1, glyphWidth[character], glyphHeight[character], 0, 192);
					drawGlyphAlpha(glyphPixels[character], x + horizontalKerning[character],
							y + verticalKerning[character], glyphWidth[character], glyphHeight[character],
							currentColour, alpha);
				}
				x += glyphDisplayWidth[character];

				/*
				 * Apply a slight offset to the text occasionally.
				 * 
				 * Not sure why.
				 */
				if ((random.nextInt() & 3) == 0)
					x++;
			}
		}
	}

	/**
	 * Draw a text in a given colour.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the text.
	 * 
	 * @param y      The y position of the text.
	 * 
	 * @param colour The colour of the text.
	 */
	public void drawText(String text, int x, int y, int colour) {
		if (text == null)
			return;

		/*
		 * Draw from the top-left, not the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through every character in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			char character = text.charAt(c);

			/*
			 * If the character is a space, we don't draw it - we just add the width of the
			 * space to the current x position.
			 */
			if (character != ' ') {
				drawGlyph(glyphPixels[character], x + horizontalKerning[character], y + verticalKerning[character],
						glyphWidth[character], glyphHeight[character], colour);
			}
			x += glyphDisplayWidth[character];
		}
	}

	/**
	 * Draw centrally-aligned text in a given colour.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the anchor point.
	 * 
	 * @param y      The y position of the anchor point.
	 * 
	 * @param colour The colour of the text.
	 */
	public void drawCentredText(String text, int x, int y, int colour) {
		/*
		 * Draw the text with half of it to the left of the anchor, and half of it to
		 * the right of the anchor.
		 */
		drawText(text, x - getTextWidth(text) / 2, y, colour);
	}

	/**
	 * Draw text to the left of an anchor point.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the anchor point.
	 * 
	 * @param y      The y position of the anchor point.
	 * 
	 * @param colour The colour to draw the text in.
	 */
	public void drawTextLeft(String text, int x, int y, int colour) {
		/*
		 * Draw text with all of the text to the left of an anchor point.
		 */
		drawText(text, x - getTextWidth(text), y, colour);
	}

	/**
	 * Draw text that is waving based on a given tick. The text is drawn around a
	 * central anchor point. The text follows a sine wave vertically.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the anchor point.
	 * 
	 * @param y      The y position of the anchor point.
	 * 
	 * @param colour The colour to draw the text.
	 * 
	 * @param tick   The current tick (used to make the text wave).
	 */
	public void drawVerticalSineWaveText(String text, int x, int y, int colour, int tick) {
		if (text == null)
			return;

		/*
		 * Place half the text to the left of the anchor point and half the text to the
		 * right. This centres the text horizontally.
		 */
		x -= getTextWidth(text) / 2;

		/*
		 * Draw text from the top-left instead of the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through the characters in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			char character = text.charAt(c);
			/*
			 * If the character is a space we do not draw it, but we still move the x
			 * position by the width of the space.
			 */
			if (character != ' ') {
				/*
				 * The y position of the text is determined using a sine wave.
				 */
				drawGlyph(glyphPixels[character], x + horizontalKerning[character],
						y + verticalKerning[character] + (int) (Math.sin(c / 2D + tick / 5D) * 5D),
						glyphWidth[character], glyphHeight[character], colour);
			}
			x += glyphDisplayWidth[character];
		}
	}

	/**
	 * Draws text that shakes based on a given tick and how long the text has
	 * existed for.
	 * 
	 * @param text    The text to draw.
	 * 
	 * @param x       The x position of the anchor point.
	 * 
	 * @param y       The y position of the anchor point.
	 * 
	 * @param colour  The colour to draw the text.
	 * 
	 * @param elapsed The time the text has been drawn for.
	 * 
	 * @param tick    The current tick (used to make the text wave).
	 */
	public void drawShakingText(String text, int x, int y, int colour, int elapsed, int tick) {
		if (text == null)
			return;

		/*
		 * Calculate the current amplitude of the shake based on how long the text has
		 * been shaking for.
		 */
		double amplitude = 7D - elapsed / 8D;
		if (amplitude < 0.0D)
			amplitude = 0.0D;

		/*
		 * Place half the text to the left of the anchor point and half the text to the
		 * right. This centres the text horizontally.
		 */
		x -= getTextWidth(text) / 2;

		/*
		 * Draw text from the top-left instead of the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through the characters in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			char character = text.charAt(c);

			/*
			 * If the character is a space we do not draw it, but we still move the x
			 * position by the width of the space.
			 */
			if (character != ' ') {
				/*
				 * The vertical position of the text is decided based on a sine wave, taking
				 * into account the current time and the amount of time the text has been
				 * shaking for.
				 */
				drawGlyph(glyphPixels[character], x + horizontalKerning[character],
						y + verticalKerning[character] + (int) (Math.sin(c / 1.5D + tick) * amplitude),
						glyphWidth[character], glyphHeight[character], colour);
			}
			x += glyphDisplayWidth[character];
		}
	}

	/**
	 * Draw text that is waving based on a given tick. The text is drawn around a
	 * central anchor point. The text follows a sine wave vertically and
	 * horizontally.
	 * 
	 * @param text   The text to draw.
	 * 
	 * @param x      The x position of the anchor point.
	 * 
	 * @param y      The y position of the anchor point.
	 * 
	 * @param colour The colour to draw the text.
	 * 
	 * @param tick   The current tick (used to make the text wave).
	 */
	public void drawVerticalHorizontalSineWaveText(String text, int x, int y, int colour, int tick) {
		if (text == null)
			return;

		/*
		 * Place half the text to the left of the anchor point and half the text to the
		 * right. This centres the text horizontally.
		 */
		x -= getTextWidth(text) / 2;

		/*
		 * Draw text from the top-left instead of the bottom.
		 */
		y -= fontHeight;

		/*
		 * Iterate through the characters in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			char character = text.charAt(c);
			/*
			 * If the character is a space we do not draw it, but we still move the x
			 * position by the width of the space.
			 */
			if (character != ' ') {
				/*
				 * The x and y positions of the text are determined using sine waves.
				 */
				drawGlyph(glyphPixels[character],
						x + horizontalKerning[character] + (int) (Math.sin(c / 5D + tick / 5D) * 5D),
						y + verticalKerning[character] + (int) (Math.sin(c / 3D + tick / 5D) * 5D),
						glyphWidth[character], glyphHeight[character], colour);
			}
			x += glyphDisplayWidth[character];
		}
	}

	/**
	 * Gets the numeric representation of a colour when given the RuneScape
	 * embedding value. Also handles strikethrough if applicable.
	 * 
	 * @param name The embed code of the effect.
	 * 
	 * @return The numeric representation of the colour, or -1 if the colour does
	 *         not exist.
	 */
	private int handleEmbeddedEffect(String name) {
		if (name.equals("red"))
			return 0xff0000;
		if (name.equals("gre"))
			return 65280;
		if (name.equals("blu"))
			return 255;
		if (name.equals("yel"))
			return 0xffff00;
		if (name.equals("cya"))
			return 65535;
		if (name.equals("mag"))
			return 0xff00ff;
		if (name.equals("whi"))
			return 0xffffff;
		if (name.equals("bla"))
			return 0;
		if (name.equals("lre"))
			return 0xff9040;
		if (name.equals("dre"))
			return 0x800000;
		if (name.equals("dbl"))
			return 128;
		if (name.equals("or1"))
			return 0xffb000;
		if (name.equals("or2"))
			return 0xff7000;
		if (name.equals("or3"))
			return 0xff3000;
		if (name.equals("gr1"))
			return 0xc0ff00;
		if (name.equals("gr2"))
			return 0x80ff00;
		if (name.equals("gr3"))
			return 0x40ff00;
		if (name.equals("str"))
			strikethrough = true;
		if (name.equals("end"))
			strikethrough = false;
		return -1;
	}

	/**
	 * Get the displayed width of a text containing embedded colours, crowns etc.
	 * 
	 * @param text The string to get the width of.
	 * 
	 * @return The width of the string after the embedded elements are removed.
	 */
	public int getTextDisplayedWidth(String text) {
		if (text == null)
			return 0;
		int width = 0;

		/*
		 * Iterate through every character in the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			/*
			 * If the current character is the start of a colour code or a crown code we can
			 * skip it as it will be stripped out later and therefore not be displayed.
			 */
			if (text.charAt(c) == '@' && c + 4 < text.length() && text.charAt(c + 4) == '@') {
				c += 4;
			} else {
				/*
				 * Add the displayed width of the current character's glyph to the total width
				 * of the text.
				 */
				width += glyphDisplayWidth[text.charAt(c)];
			}
		}

		return width;
	}

	/**
	 * Get the width of a text.
	 * 
	 * @param text The text to get the width of.
	 * 
	 * @return The width of the text.
	 */
	public int getTextWidth(String text) {
		if (text == null)
			return 0;
		int width = 0;

		/*
		 * Iterate through every character in the text and add the displayed width of
		 * that character's glyph to the total width of the text.
		 */
		for (int c = 0; c < text.length(); c++) {
			width += glyphDisplayWidth[text.charAt(c)];
		}

		return width;
	}
}