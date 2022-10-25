package com.jagex.runescape.definition;

import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;
import com.jagex.runescape.Model;

public final class IdentityKit {

    public static void load(final Archive streamLoader) {
        final Buffer stream = new Buffer(streamLoader.decompressFile("idk.dat"));
        count = stream.getUnsignedLEShort();
        if (cache == null) {
            cache = new IdentityKit[count];
        }
        for (int kit = 0; kit < count; kit++) {
            if (cache[kit] == null) {
                cache[kit] = new IdentityKit();
            }
            cache[kit].loadDefinition(stream);
        }
    }

    public static int count;

    public static IdentityKit[] cache;

    public int partId;

    private int[] modelIds;

    private final int[] originalModelColours;

    private final int[] modifiedModelColours;

    private final int[] headModelIds = {-1, -1, -1, -1, -1};
    public boolean widgetDisplayed;

    private IdentityKit() {
        this.partId = -1;
        this.originalModelColours = new int[6];
        this.modifiedModelColours = new int[6];
        this.widgetDisplayed = false;
    }

    public boolean bodyModelCached() {
        if (this.modelIds == null) {
            return true;
        }
        boolean cached = true;
        for (int m = 0; m < this.modelIds.length; m++) {
            if (!Model.isCached(this.modelIds[m])) {
                cached = false;
            }
        }

        return cached;
    }

    public Model getBodyModel() {
        if (this.modelIds == null) {
            return null;
        }
        final Model[] models = new Model[this.modelIds.length];
        for (int m = 0; m < this.modelIds.length; m++) {
            models[m] = Model.getModel(this.modelIds[m]);
        }

        final Model model;
        if (models.length == 1) {
            model = models[0];
        } else {
            model = new Model(models.length, models);
        }
        for (int colour = 0; colour < 6; colour++) {
            if (this.originalModelColours[colour] == 0) {
                break;
            }
            model.recolour(this.originalModelColours[colour], this.modifiedModelColours[colour]);
        }

        return model;
    }

    public Model getHeadModel() {
        final Model[] models = new Model[5];
        int modelCount = 0;
        for (int m = 0; m < 5; m++) {
            if (this.headModelIds[m] != -1) {
                models[modelCount++] = Model.getModel(this.headModelIds[m]);
            }
        }

        final Model model = new Model(modelCount, models);
        for (int colour = 0; colour < 6; colour++) {
            if (this.originalModelColours[colour] == 0) {
                break;
            }
            model.recolour(this.originalModelColours[colour], this.modifiedModelColours[colour]);
        }

        return model;
    }

    public boolean headModelCached() {
        boolean cached = true;
        for (int m = 0; m < 5; m++) {
            if (this.headModelIds[m] != -1 && !Model.isCached(this.headModelIds[m])) {
                cached = false;
            }
        }

        return cached;
    }

    private void loadDefinition(final Buffer stream) {
        do {
            final int opcode = stream.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                this.partId = stream.getUnsignedByte();
            } else if (opcode == 2) {
                final int modelCount = stream.getUnsignedByte();
                this.modelIds = new int[modelCount];
                for (int m = 0; m < modelCount; m++) {
                    this.modelIds[m] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 3) {
                this.widgetDisplayed = true;
            } else if (opcode >= 40 && opcode < 50) {
                this.originalModelColours[opcode - 40] = stream.getUnsignedLEShort();
            } else if (opcode >= 50 && opcode < 60) {
                this.modifiedModelColours[opcode - 50] = stream.getUnsignedLEShort();
            } else if (opcode >= 60 && opcode < 70) {
                this.headModelIds[opcode - 60] = stream.getUnsignedLEShort();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }
}
