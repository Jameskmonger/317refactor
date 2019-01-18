package com.jagex.runescape.definition;

import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;

public final class FloorDefinition {

	public static void load(Archive archive) {
		Buffer stream = new Buffer(archive.decompressFile("flo.dat"));
		int cacheSize = stream.getUnsignedLEShort();
		if (cache == null) {
            cache = new FloorDefinition[cacheSize];
        }
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null) {
                cache[j] = new FloorDefinition();
            }
			cache[j].loadDefinition(stream);
		}

	}

	public static FloorDefinition cache[];

	public int rgbColour;

	public int textureId;

	public boolean occlude;

	public int hue2;
	public int saturation;
	public int lightness;
	public int hue;
	public int hueDivisor;
	public int hsl;
	public String name;

	private FloorDefinition() {
        this.textureId = -1;
        this.occlude = true;
	}

	private void loadDefinition(Buffer stream) {
		do {
			int opcode = stream.getUnsignedByte();
			if (opcode == 0) {
                return;
            } else if (opcode == 1) {
                this.rgbColour = stream.get3Bytes();
				this.rgbToHls(this.rgbColour);
			} else if (opcode == 2) {
                this.textureId = stream.getUnsignedByte();
            } else if (opcode == 3) {
			} // dummy attribute
			else if (opcode == 5) {
                this.occlude = false;
            } else if (opcode == 6) {
                this.name = stream.getString();
            } else if (opcode == 7) {
				int oldHue2 = this.hue2;
				int oldSat = this.saturation;
				int oldLight = this.lightness;
				int oldHue = this.hue;
				int rgb = stream.get3Bytes();
				this.rgbToHls(rgb);
                this.hue2 = oldHue2;
                this.saturation = oldSat;
                this.lightness = oldLight;
                this.hue = oldHue;
                this.hueDivisor = oldHue;
			} else {
				System.out.println("Error unrecognised config code: " + opcode);
			}

		} while (true);
	}

	private int packHSL(int h, int s, int l) {
		if (l > 179) {
            s /= 2;
        }
		if (l > 192) {
            s /= 2;
        }
		if (l > 217) {
            s /= 2;
        }
		if (l > 243) {
            s /= 2;
        }
		return (h / 4 << 10) + (s / 32 << 7) + l / 2;
	}

	private void rgbToHls(int rgb) {
		double red = (rgb >> 16 & 0xff) / 256D;
		double green = (rgb >> 8 & 0xff) / 256D;
		double blue = (rgb & 0xff) / 256D;
		double minC = red;
		if (green < minC) {
            minC = green;
        }
		if (blue < minC) {
            minC = blue;
        }
		double maxC = red;
		if (green > maxC) {
            maxC = green;
        }
		if (blue > maxC) {
            maxC = blue;
        }
		double h = 0.0D;
		double s = 0.0D;
		double l = (minC + maxC) / 2D;
		if (minC != maxC) {
			if (l < 0.5D) {
                s = (maxC - minC) / (maxC + minC);
            }
			if (l >= 0.5D) {
                s = (maxC - minC) / (2D - maxC - minC);
            }
			if (red == maxC) {
                h = (green - blue) / (maxC - minC);
            } else if (green == maxC) {
                h = 2D + (blue - red) / (maxC - minC);
            } else if (blue == maxC) {
                h = 4D + (red - green) / (maxC - minC);
            }
		}
		h /= 6D;
        this.hue2 = (int) (h * 256D);
        this.saturation = (int) (s * 256D);
        this.lightness = (int) (l * 256D);
		if (this.saturation < 0) {
            this.saturation = 0;
        } else if (this.saturation > 255) {
            this.saturation = 255;
        }
		if (this.lightness < 0) {
            this.lightness = 0;
        } else if (this.lightness > 255) {
            this.lightness = 255;
        }
		if (l > 0.5D) {
            this.hueDivisor = (int) ((1.0D - l) * s * 512D);
        } else {
            this.hueDivisor = (int) (l * s * 512D);
        }
		if (this.hueDivisor < 1) {
            this.hueDivisor = 1;
        }
        this.hue = (int) (h * this.hueDivisor);
		int randomHue = (this.hue2 + (int) (Math.random() * 16D)) - 8;
		if (randomHue < 0) {
            randomHue = 0;
        } else if (randomHue > 255) {
            randomHue = 255;
        }
		int randomSaturation = (this.saturation + (int) (Math.random() * 48D)) - 24;
		if (randomSaturation < 0) {
            randomSaturation = 0;
        } else if (randomSaturation > 255) {
            randomSaturation = 255;
        }
		int randomLightness = (this.lightness + (int) (Math.random() * 48D)) - 24;
		if (randomLightness < 0) {
            randomLightness = 0;
        } else if (randomLightness > 255) {
            randomLightness = 255;
        }
        this.hsl = this.packHSL(randomHue, randomSaturation, randomLightness);
	}
}
