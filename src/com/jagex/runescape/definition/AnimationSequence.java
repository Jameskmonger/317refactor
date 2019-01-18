package com.jagex.runescape.definition;

import com.jagex.runescape.Animation;
import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;

public final class AnimationSequence {

	public static void unpackConfig(Archive streamLoader) {
		Buffer stream = new Buffer(streamLoader.decompressFile("seq.dat"));
		int length = stream.getUnsignedLEShort();
		if (animations == null) {
            animations = new AnimationSequence[length];
        }
		for (int animation = 0; animation < length; animation++) {
			if (animations[animation] == null) {
                animations[animation] = new AnimationSequence();
            }
			animations[animation].readValues(stream);
		}
	}

	public static AnimationSequence animations[];

	public int frameCount;

	public int primaryFrames[];

	public int secondaryFrames[];
	private int[] frameLengths;
	public int frameStep;
	public int flowControl[];
	public boolean dynamic;
	public int priority;
	public int playerReplacementShield;
	public int playerReplacementWeapon;
	public int maximumLoops;
	public int precedenceAnimating;
	public int precedenceWalking;
	public int replayMode;

	private AnimationSequence() {
		frameStep = -1;
		dynamic = false;
		priority = 5;
		playerReplacementShield = -1;
		playerReplacementWeapon = -1;
		maximumLoops = 99;
		precedenceAnimating = -1;
		precedenceWalking = -1;
		replayMode = 2;
	}

	public int getFrameLength(int frame) {
		int frameLength = frameLengths[frame];
		if (frameLength == 0) {
			Animation animation = Animation.forFrameId(primaryFrames[frame]);
			if (animation != null) {
                frameLength = frameLengths[frame] = animation.displayLength;
            }
		}
		if (frameLength == 0) {
            frameLength = 1;
        }
		return frameLength;
	}

	private void readValues(Buffer stream) {
		do {
			int opcode = stream.getUnsignedByte();
			if (opcode == 0) {
                break;
            }
			if (opcode == 1) {
				frameCount = stream.getUnsignedByte();
				primaryFrames = new int[frameCount];
				secondaryFrames = new int[frameCount];
				frameLengths = new int[frameCount];
				for (int frame = 0; frame < frameCount; frame++) {
					primaryFrames[frame] = stream.getUnsignedLEShort();
					secondaryFrames[frame] = stream.getUnsignedLEShort();
					if (secondaryFrames[frame] == 65535) {
                        secondaryFrames[frame] = -1;
                    }
					frameLengths[frame] = stream.getUnsignedLEShort();
				}

			} else if (opcode == 2) {
                frameStep = stream.getUnsignedLEShort();
            } else if (opcode == 3) {
				int flowCount = stream.getUnsignedByte();
				flowControl = new int[flowCount + 1];
				for (int flow = 0; flow < flowCount; flow++) {
                    flowControl[flow] = stream.getUnsignedByte();
                }

				flowControl[flowCount] = 0x98967f;
			} else if (opcode == 4) {
                dynamic = true;
            } else if (opcode == 5) {
                priority = stream.getUnsignedByte();
            } else if (opcode == 6) {
                playerReplacementShield = stream.getUnsignedLEShort();
            } else if (opcode == 7) {
                playerReplacementWeapon = stream.getUnsignedLEShort();
            } else if (opcode == 8) {
                maximumLoops = stream.getUnsignedByte();
            } else if (opcode == 9)
				/*
				 * when animating, 0 -> block walking, 1 -> yield to walking, 2 -> interleave
				 * with walking
				 */ {
                precedenceAnimating = stream.getUnsignedByte();
            } else if (opcode == 10)
				/*
				 * when walking, 0 -> block walking, 1 -> yield to walking, 2 -> never used...
				 * interleave with walking?
				 */ {
                precedenceWalking = stream.getUnsignedByte();
            } else if (opcode == 11) {
                replayMode = stream.getUnsignedByte();
            } else if (opcode == 12) {
                stream.getInt();
            } else {
                System.out.println("Error unrecognised seq config code: " + opcode);
            }
		} while (true);
		if (frameCount == 0) {
			frameCount = 1;
			primaryFrames = new int[1];
			primaryFrames[0] = -1;
			secondaryFrames = new int[1];
			secondaryFrames[0] = -1;
			frameLengths = new int[1];
			frameLengths[0] = -1;
		}
		if (precedenceAnimating == -1) {
            if (flowControl != null) {
                precedenceAnimating = 2;
            } else {
                precedenceAnimating = 0;
            }
        }
		if (precedenceWalking == -1) {
			if (flowControl != null) {
				precedenceWalking = 2;
				return;
			}
			precedenceWalking = 0;
		}
	}
}
