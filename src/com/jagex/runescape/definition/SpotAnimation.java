package com.jagex.runescape.definition;

import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;
import com.jagex.runescape.Model;
import com.jagex.runescape.collection.Cache;

public final class SpotAnimation {

    public static void load(final Archive archive) {
        final Buffer buffer = new Buffer(archive.decompressFile("spotanim.dat"));
        final int count = buffer.getUnsignedLEShort();
        if (cache == null) {
            cache = new SpotAnimation[count];
        }
        for (int anim = 0; anim < count; anim++) {
            if (cache[anim] == null) {
                cache[anim] = new SpotAnimation();
            }
            cache[anim].id = anim;
            cache[anim].read(buffer);
        }

    }

    public static SpotAnimation[] cache;

    private int id;

    private int modelId;

    private int animationId;
    public AnimationSequence sequences;
    private final int[] originalModelColours;
    private final int[] modifiedModelColours;
    public int scaleXY;
    public int scaleZ;
    public int rotation;
    public int modelLightFalloff;
    public int modelLightAmbient;
    public static Cache modelCache = new Cache(30);

    private SpotAnimation() {
        this.animationId = -1;
        this.originalModelColours = new int[6];
        this.modifiedModelColours = new int[6];
        this.scaleXY = 128;
        this.scaleZ = 128;
    }

    public Model getModel() {
        Model model = (Model) modelCache.get(this.id);
        if (model != null) {
            return model;
        }
        model = Model.getModel(this.modelId);
        if (model == null) {
            return null;
        }
        for (int colour = 0; colour < 6; colour++) {
            if (this.originalModelColours[0] != 0) {
                model.recolour(this.originalModelColours[colour], this.modifiedModelColours[colour]);
            }
        }

        modelCache.put(model, this.id);
        return model;
    }

    private void read(final Buffer stream) {
        do {
            final int opcode = stream.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                this.modelId = stream.getUnsignedLEShort();
            } else if (opcode == 2) {
                this.animationId = stream.getUnsignedLEShort();
                if (AnimationSequence.animations != null) {
                    this.sequences = AnimationSequence.animations[this.animationId];
                }
            } else if (opcode == 4) {
                this.scaleXY = stream.getUnsignedLEShort();
            } else if (opcode == 5) {
                this.scaleZ = stream.getUnsignedLEShort();
            } else if (opcode == 6) {
                this.rotation = stream.getUnsignedLEShort();
            } else if (opcode == 7) {
                this.modelLightFalloff = stream.getUnsignedByte();
            } else if (opcode == 8) {
                this.modelLightAmbient = stream.getUnsignedByte();
            } else if (opcode >= 40 && opcode < 50) {
                this.originalModelColours[opcode - 40] = stream.getUnsignedLEShort();
            } else if (opcode >= 50 && opcode < 60) {
                this.modifiedModelColours[opcode - 50] = stream.getUnsignedLEShort();
            } else {
                System.out.println("Error unrecognised spotanim config code: " + opcode);
            }
        } while (true);
    }
}