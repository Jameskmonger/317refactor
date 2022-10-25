package com.jagex.runescape.definition;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.Cache;

public final class EntityDefinition {

    public static EntityDefinition getDefinition(final int id) {
        for (int c = 0; c < 20; c++) {
            if (EntityDefinition.cache[c].id == id) {
                return EntityDefinition.cache[c];
            }
        }

        EntityDefinition.bufferIndex = (EntityDefinition.bufferIndex + 1) % 20;
        final EntityDefinition definition = EntityDefinition.cache[EntityDefinition.bufferIndex] = new EntityDefinition();
        EntityDefinition.stream.position = EntityDefinition.streamOffsets[id];
        definition.id = id;
        definition.loadDefinition(EntityDefinition.stream);
        return definition;
    }

    public static void load(final Archive streamLoader) {
        EntityDefinition.stream = new Buffer(streamLoader.decompressFile("npc.dat"));
        final Buffer stream = new Buffer(streamLoader.decompressFile("npc.idx"));
        final int size = stream.getUnsignedLEShort();
        EntityDefinition.streamOffsets = new int[size];
        int offset = 2;
        for (int n = 0; n < size; n++) {
            EntityDefinition.streamOffsets[n] = offset;
            offset += stream.getUnsignedLEShort();
        }

        EntityDefinition.cache = new EntityDefinition[20];
        for (int c = 0; c < 20; c++) {
            EntityDefinition.cache[c] = new EntityDefinition();
        }

    }

    public static void nullLoader() {
        modelCache = null;
        streamOffsets = null;
        cache = null;
        stream = null;
    }

    public int turnLeftAnimationId;

    private static int bufferIndex;

    private int varBitId;

    public int turnAboutAnimationId;

    private int settingId;

    private static Buffer stream;
    public int combatLevel;
    public String name;
    public String[] actions;
    public int walkAnimationId;
    public byte boundaryDimension;
    private int[] originalModelColours;
    private static int[] streamOffsets;
    private int[] headModelIds;
    public int headIcon;
    private int[] modifiedModelColours;
    public int standAnimationId;
    public long id;
    public int degreesToTurn;
    private static EntityDefinition[] cache;
    public static Client clientInstance;
    public int turnRightAnimationId;
    public boolean clickable;
    private int brightness;
    private int scaleZ;
    public boolean visibleMinimap;
    public int[] childrenIDs;
    public byte[] description;
    private int scaleXY;
    private int contrast;
    public boolean visible;
    private int[] modelIds;
    public static Cache modelCache = new Cache(30);

    private EntityDefinition() {
        this.turnLeftAnimationId = -1;
        this.varBitId = -1;
        this.turnAboutAnimationId = -1;
        this.settingId = -1;
        this.combatLevel = -1;
        this.walkAnimationId = -1;
        this.boundaryDimension = 1;
        this.headIcon = -1;
        this.standAnimationId = -1;
        this.id = -1L;
        this.degreesToTurn = 32;
        this.turnRightAnimationId = -1;
        this.clickable = true;
        this.scaleZ = 128;
        this.visibleMinimap = true;
        this.scaleXY = 128;
        this.visible = false;
    }

    public EntityDefinition getChildDefinition() {
        int childId = -1;
        if (this.varBitId != -1) {
            final VarBit varBit = VarBit.values[this.varBitId];
            final int configId = varBit.configId;
            final int lsb = varBit.leastSignificantBit;
            final int msb = varBit.mostSignificantBit;
            final int bit = Client.BITFIELD_MAX_VALUE[msb - lsb];
            childId = clientInstance.interfaceSettings[configId] >> lsb & bit;
        } else if (this.settingId != -1) {
            childId = clientInstance.interfaceSettings[this.settingId];
        }
        if (childId < 0 || childId >= this.childrenIDs.length || this.childrenIDs[childId] == -1) {
            return null;
        } else {
            return getDefinition(this.childrenIDs[childId]);
        }
    }

    public Model getChildModel(final int frameId2, final int frameId1, final int[] framesFrom2) {
        if (this.childrenIDs != null) {
            final EntityDefinition childDefinition = this.getChildDefinition();
            if (childDefinition == null) {
                return null;
            } else {
                return childDefinition.getChildModel(frameId2, frameId1, framesFrom2);
            }
        }
        Model model = (Model) modelCache.get(this.id);
        if (model == null) {
            boolean notCached = false;
            for (int m = 0; m < this.modelIds.length; m++) {
                if (!Model.isCached(this.modelIds[m])) {
                    notCached = true;
                }
            }

            if (notCached) {
                return null;
            }
            final Model[] childModels = new Model[this.modelIds.length];
            for (int m = 0; m < this.modelIds.length; m++) {
                childModels[m] = Model.getModel(this.modelIds[m]);
            }

            if (childModels.length == 1) {
                model = childModels[0];
            } else {
                model = new Model(childModels.length, childModels);
            }
            if (this.modifiedModelColours != null) {
                for (int c = 0; c < this.modifiedModelColours.length; c++) {
                    model.recolour(this.modifiedModelColours[c], this.originalModelColours[c]);
                }

            }
            model.createBones();
            model.applyLighting(64 + this.brightness, 850 + this.contrast, -30, -50, -30, true);
            modelCache.put(model, this.id);
        }
        final Model childModel = Model.aModel_1621;
        childModel.replaceWithModel(model, Animation.isNullFrame(frameId1) & Animation.isNullFrame(frameId2));
        if (frameId1 != -1 && frameId2 != -1) {
            childModel.mixAnimationFrames(framesFrom2, frameId2, frameId1);
        } else if (frameId1 != -1) {
            childModel.applyTransformation(frameId1);
        }
        if (this.scaleXY != 128 || this.scaleZ != 128) {
            childModel.scaleT(this.scaleXY, this.scaleXY, this.scaleZ);
        }
        childModel.calculateDiagonals();
        childModel.triangleSkin = null;
        childModel.vertexSkin = null;
        if (this.boundaryDimension == 1) {
            childModel.singleTile = true;
        }
        return childModel;
    }

    public Model getHeadModel() {
        if (this.childrenIDs != null) {
            final EntityDefinition definition = this.getChildDefinition();
            if (definition == null) {
                return null;
            } else {
                return definition.getHeadModel();
            }
        }
        if (this.headModelIds == null) {
            return null;
        }
        boolean someModelsNotCached = false;
        for (int i = 0; i < this.headModelIds.length; i++) {
            if (!Model.isCached(this.headModelIds[i])) {
                someModelsNotCached = true;
            }
        }

        if (someModelsNotCached) {
            return null;
        }
        final Model[] headModels = new Model[this.headModelIds.length];
        for (int j = 0; j < this.headModelIds.length; j++) {
            headModels[j] = Model.getModel(this.headModelIds[j]);
        }

        final Model headModel;
        if (headModels.length == 1) {
            headModel = headModels[0];
        } else {
            headModel = new Model(headModels.length, headModels);
        }
        if (this.modifiedModelColours != null) {
            for (int c = 0; c < this.modifiedModelColours.length; c++) {
                headModel.recolour(this.modifiedModelColours[c], this.originalModelColours[c]);
            }

        }
        return headModel;
    }

    private void loadDefinition(final Buffer stream) {
        do {
            final int opcode = stream.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                final int modelCount = stream.getUnsignedByte();
                this.modelIds = new int[modelCount];
                for (int m = 0; m < modelCount; m++) {
                    this.modelIds[m] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 2) {
                this.name = stream.getString();
            } else if (opcode == 3) {
                this.description = stream.readBytes();
            } else if (opcode == 12) {
                this.boundaryDimension = stream.get();
            } else if (opcode == 13) {
                this.standAnimationId = stream.getUnsignedLEShort();
            } else if (opcode == 14) {
                this.walkAnimationId = stream.getUnsignedLEShort();
            } else if (opcode == 17) {
                this.walkAnimationId = stream.getUnsignedLEShort();
                this.turnAboutAnimationId = stream.getUnsignedLEShort();
                this.turnRightAnimationId = stream.getUnsignedLEShort();
                this.turnLeftAnimationId = stream.getUnsignedLEShort();
            } else if (opcode >= 30 && opcode < 40) {
                if (this.actions == null) {
                    this.actions = new String[5];
                }
                this.actions[opcode - 30] = stream.getString();
                if (this.actions[opcode - 30].equalsIgnoreCase("hidden")) {
                    this.actions[opcode - 30] = null;
                }
            } else if (opcode == 40) {
                final int colourCount = stream.getUnsignedByte();
                this.modifiedModelColours = new int[colourCount];
                this.originalModelColours = new int[colourCount];
                for (int c = 0; c < colourCount; c++) {
                    this.modifiedModelColours[c] = stream.getUnsignedLEShort();
                    this.originalModelColours[c] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 60) {
                final int additionalModelCount = stream.getUnsignedByte();
                this.headModelIds = new int[additionalModelCount];
                for (int m = 0; m < additionalModelCount; m++) {
                    this.headModelIds[m] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 90) {
                stream.getUnsignedLEShort();
            } else if (opcode == 91) {
                stream.getUnsignedLEShort();
            } else if (opcode == 92) {
                stream.getUnsignedLEShort();
            } else if (opcode == 93) {
                this.visibleMinimap = false;
            } else if (opcode == 95) {
                this.combatLevel = stream.getUnsignedLEShort();
            } else if (opcode == 97) {
                this.scaleXY = stream.getUnsignedLEShort();
            } else if (opcode == 98) {
                this.scaleZ = stream.getUnsignedLEShort();
            } else if (opcode == 99) {
                this.visible = true;
            } else if (opcode == 100) {
                this.brightness = stream.get();
            } else if (opcode == 101) {
                this.contrast = stream.get() * 5;
            } else if (opcode == 102) {
                this.headIcon = stream.getUnsignedLEShort();
            } else if (opcode == 103) {
                this.degreesToTurn = stream.getUnsignedLEShort();
            } else if (opcode == 106) {
                this.varBitId = stream.getUnsignedLEShort();
                if (this.varBitId == 65535) {
                    this.varBitId = -1;
                }
                this.settingId = stream.getUnsignedLEShort();
                if (this.settingId == 65535) {
                    this.settingId = -1;
                }
                final int childCount = stream.getUnsignedByte();
                this.childrenIDs = new int[childCount + 1];
                for (int c = 0; c <= childCount; c++) {
                    this.childrenIDs[c] = stream.getUnsignedLEShort();
                    if (this.childrenIDs[c] == 65535) {
                        this.childrenIDs[c] = -1;
                    }
                }

            } else if (opcode == 107) {
                this.clickable = false;
            }
        } while (true);
    }

}
