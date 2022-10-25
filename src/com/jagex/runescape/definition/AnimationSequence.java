package com.jagex.runescape.definition;

import com.jagex.runescape.Animation;
import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;

public final class AnimationSequence {

    public static void unpackConfig(final Archive streamLoader) {
        final Buffer stream = new Buffer(streamLoader.decompressFile("seq.dat"));
        final int length = stream.getUnsignedLEShort();
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

    public static AnimationSequence[] animations;

    public int frameCount;

    public int[] primaryFrames;

    public int[] secondaryFrames;
    private int[] frameLengths;
    public int frameStep;
    public int[] flowControl;
    public boolean dynamic;
    public int priority;
    public int playerReplacementShield;
    public int playerReplacementWeapon;
    public int maximumLoops;
    public int precedenceAnimating;
    public int precedenceWalking;
    public int replayMode;

    private AnimationSequence() {
        this.frameStep = -1;
        this.dynamic = false;
        this.priority = 5;
        this.playerReplacementShield = -1;
        this.playerReplacementWeapon = -1;
        this.maximumLoops = 99;
        this.precedenceAnimating = -1;
        this.precedenceWalking = -1;
        this.replayMode = 2;
    }

    public int getFrameLength(final int frame) {
        int frameLength = this.frameLengths[frame];
        if (frameLength == 0) {
            final Animation animation = Animation.forFrameId(this.primaryFrames[frame]);
            if (animation != null) {
                frameLength = this.frameLengths[frame] = animation.displayLength;
            }
        }
        if (frameLength == 0) {
            frameLength = 1;
        }
        return frameLength;
    }

    private void readValues(final Buffer stream) {
        do {
            final int opcode = stream.getUnsignedByte();
            if (opcode == 0) {
                break;
            }
            if (opcode == 1) {
                this.frameCount = stream.getUnsignedByte();
                this.primaryFrames = new int[this.frameCount];
                this.secondaryFrames = new int[this.frameCount];
                this.frameLengths = new int[this.frameCount];
                for (int frame = 0; frame < this.frameCount; frame++) {
                    this.primaryFrames[frame] = stream.getUnsignedLEShort();
                    this.secondaryFrames[frame] = stream.getUnsignedLEShort();
                    if (this.secondaryFrames[frame] == 65535) {
                        this.secondaryFrames[frame] = -1;
                    }
                    this.frameLengths[frame] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 2) {
                this.frameStep = stream.getUnsignedLEShort();
            } else if (opcode == 3) {
                final int flowCount = stream.getUnsignedByte();
                this.flowControl = new int[flowCount + 1];
                for (int flow = 0; flow < flowCount; flow++) {
                    this.flowControl[flow] = stream.getUnsignedByte();
                }

                this.flowControl[flowCount] = 0x98967f;
            } else if (opcode == 4) {
                this.dynamic = true;
            } else if (opcode == 5) {
                this.priority = stream.getUnsignedByte();
            } else if (opcode == 6) {
                this.playerReplacementShield = stream.getUnsignedLEShort();
            } else if (opcode == 7) {
                this.playerReplacementWeapon = stream.getUnsignedLEShort();
            } else if (opcode == 8) {
                this.maximumLoops = stream.getUnsignedByte();
            } else if (opcode == 9)
                /*
                 * when animating, 0 -> block walking, 1 -> yield to walking, 2 -> interleave
                 * with walking
                 */ {
                this.precedenceAnimating = stream.getUnsignedByte();
            } else if (opcode == 10)
                /*
                 * when walking, 0 -> block walking, 1 -> yield to walking, 2 -> never used...
                 * interleave with walking?
                 */ {
                this.precedenceWalking = stream.getUnsignedByte();
            } else if (opcode == 11) {
                this.replayMode = stream.getUnsignedByte();
            } else if (opcode == 12) {
                stream.getInt();
            } else {
                System.out.println("Error unrecognised seq config code: " + opcode);
            }
        } while (true);
        if (this.frameCount == 0) {
            this.frameCount = 1;
            this.primaryFrames = new int[1];
            this.primaryFrames[0] = -1;
            this.secondaryFrames = new int[1];
            this.secondaryFrames[0] = -1;
            this.frameLengths = new int[1];
            this.frameLengths[0] = -1;
        }
        if (this.precedenceAnimating == -1) {
            if (this.flowControl != null) {
                this.precedenceAnimating = 2;
            } else {
                this.precedenceAnimating = 0;
            }
        }
        if (this.precedenceWalking == -1) {
            if (this.flowControl != null) {
                this.precedenceWalking = 2;
                return;
            }
            this.precedenceWalking = 0;
        }
    }
}
