package com.jagex.runescape.definition;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.Cache;

public final class GameObjectDefinition {

    public static GameObjectDefinition getDefinition(final int objectId) {
        for (int c = 0; c < 20; c++) {
            if (cache[c].id == objectId) {
                return cache[c];
            }
        }

        cacheIndex = (cacheIndex + 1) % 20;
        final GameObjectDefinition definition = cache[cacheIndex];
        stream.position = streamOffsets[objectId];
        definition.id = objectId;
        definition.setDefaults();
        definition.loadDefinition(stream);
        return definition;
    }

    public static void load(final Archive archive) {
        stream = new Buffer(archive.decompressFile("loc.dat"));
        final Buffer stream = new Buffer(archive.decompressFile("loc.idx"));
        final int objectCount = stream.getUnsignedLEShort();
        streamOffsets = new int[objectCount];
        int offset = 2;
        for (int index = 0; index < objectCount; index++) {
            streamOffsets[index] = offset;
            offset += stream.getUnsignedLEShort();
        }

        cache = new GameObjectDefinition[20];
        for (int c = 0; c < 20; c++) {
            cache[c] = new GameObjectDefinition();
        }

    }

    public static void nullLoader() {
        modelCache = null;
        animatedModelCache = null;
        streamOffsets = null;
        cache = null;
        stream = null;
    }

    public boolean unknownAttribute;

    private byte ambient;

    private int translateX;

    public String name;

    private int scaleZ;

    private static final Model[] models = new Model[4];

    private byte diffuse;

    public int sizeX;

    private int translateY;

    public int icon;
    private int[] originalModelColors;
    private int scaleX;
    public int configIds;
    private boolean rotated;
    public static boolean lowMemory;
    private static Buffer stream;
    public int id;
    private static int[] streamOffsets;
    public boolean walkable;
    public int mapScene;
    public int[] childIds;
    private int _solid;
    public int sizeY;
    public boolean adjustToTerrain;
    public boolean wall;
    public static Client clientInstance;
    private boolean unwalkableSolid;
    public boolean solid;
    public int face;
    private boolean delayShading;
    private static int cacheIndex;
    private int scaleY;
    private int[] modelIds;
    public int varBitId;
    public int offsetAmplifier;
    private int[] modelTypes;
    public byte[] description;
    public boolean hasActions;
    public boolean castsShadow;
    public static Cache animatedModelCache = new Cache(30);
    public int animationId;
    private static GameObjectDefinition[] cache;
    private int translateZ;
    private int[] modifiedModelColors;
    public static Cache modelCache = new Cache(500);
    public String[] actions;

    private GameObjectDefinition() {
        this.id = -1;
    }

    private Model getAnimatedModel(final int type, final int animationId, int face) {
        Model subModel = null;
        final long hash;
        if (this.modelTypes == null) {
            if (type != 10) {
                return null;
            }
            hash = ((long) this.id << 6) + face + ((long) (animationId + 1) << 32);
            final Model cachedModel = (Model) animatedModelCache.get(hash);
            if (cachedModel != null) {
                return cachedModel;
            }
            if (this.modelIds == null) {
                return null;
            }
            final boolean mirror = this.rotated ^ (face > 3);
            final int modelCount = this.modelIds.length;
            for (int m = 0; m < modelCount; m++) {
                int subModelId = this.modelIds[m];
                if (mirror) {
                    subModelId += 0x10000;
                }
                subModel = (Model) modelCache.get(subModelId);
                if (subModel == null) {
                    subModel = Model.getModel(subModelId & 0xffff);
                    if (subModel == null) {
                        return null;
                    }
                    if (mirror) {
                        subModel.mirror();
                    }
                    modelCache.put(subModel, subModelId);
                }
                if (modelCount > 1) {
                    models[m] = subModel;
                }
            }

            if (modelCount > 1) {
                subModel = new Model(modelCount, models);
            }
        } else {
            int modelType = -1;
            for (int t = 0; t < this.modelTypes.length; t++) {
                if (this.modelTypes[t] != type) {
                    continue;
                }
                modelType = t;
                break;
            }

            if (modelType == -1) {
                return null;
            }
            hash = ((long) this.id << 6) + ((long) modelType << 3) + face + ((long) (animationId + 1) << 32);
            final Model model = (Model) animatedModelCache.get(hash);
            if (model != null) {
                return model;
            }
            int modelId = this.modelIds[modelType];
            final boolean mirror = this.rotated ^ (face > 3);
            if (mirror) {
                modelId += 0x10000;
            }
            subModel = (Model) modelCache.get(modelId);
            if (subModel == null) {
                subModel = Model.getModel(modelId & 0xffff);
                if (subModel == null) {
                    return null;
                }
                if (mirror) {
                    subModel.mirror();
                }
                modelCache.put(subModel, modelId);
            }
        }
        final boolean scale;
        scale = this.scaleX != 128 || this.scaleY != 128 || this.scaleZ != 128;
        final boolean translate;
        translate = this.translateX != 0 || this.translateY != 0 || this.translateZ != 0;
        final Model animatedModel = new Model(this.modifiedModelColors == null, Animation.isNullFrame(animationId),
            face == 0 && animationId == -1 && !scale && !translate, subModel);
        if (animationId != -1) {
            animatedModel.createBones();
            animatedModel.applyTransformation(animationId);
            animatedModel.triangleSkin = null;
            animatedModel.vertexSkin = null;
        }
        while (face-- > 0) {
            animatedModel.rotate90Degrees();
        }
        if (this.modifiedModelColors != null) {
            for (int c = 0; c < this.modifiedModelColors.length; c++) {
                animatedModel.recolour(this.modifiedModelColors[c], this.originalModelColors[c]);
            }

        }
        if (scale) {
            animatedModel.scaleT(this.scaleX, this.scaleZ, this.scaleY);
        }
        if (translate) {
            animatedModel.translate(this.translateX, this.translateY, this.translateZ);
        }
        animatedModel.applyLighting(64 + this.ambient, 768 + this.diffuse * 5, -50, -10, -50, !this.delayShading);
        if (this._solid == 1) {
            animatedModel.anInt1654 = animatedModel.modelHeight;
        }
        animatedModelCache.put(animatedModel, hash);
        return animatedModel;
    }

    public GameObjectDefinition getChildDefinition() {
        int child = -1;
        if (this.varBitId != -1) {
            final VarBit varBit = VarBit.values[this.varBitId];
            final int configId = varBit.configId;
            final int lsb = varBit.leastSignificantBit;
            final int msb = varBit.mostSignificantBit;
            final int bit = Client.BITFIELD_MAX_VALUE[msb - lsb];
            child = clientInstance.interfaceSettings[configId] >> lsb & bit;
        } else if (this.configIds != -1) {
            child = clientInstance.interfaceSettings[this.configIds];
        }
        if (child < 0 || child >= this.childIds.length || this.childIds[child] == -1) {
            return null;
        } else {
            return getDefinition(this.childIds[child]);
        }
    }

    public Model getModelAt(final int i, final int j, final int k, final int l, final int i1, final int j1, final int k1) {
        Model model = this.getAnimatedModel(i, k1, j);
        if (model == null) {
            return null;
        }
        if (this.adjustToTerrain || this.delayShading) {
            model = new Model(this.adjustToTerrain, this.delayShading, model);
        }
        if (this.adjustToTerrain) {
            final int l1 = (k + l + i1 + j1) / 4;
            for (int v = 0; v < model.vertexCount; v++) {
                final int x = model.verticesX[v];
                final int z = model.verticesZ[v];
                final int l2 = k + ((l - k) * (x + 64)) / 128;
                final int i3 = j1 + ((i1 - j1) * (x + 64)) / 128;
                final int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.verticesY[v] += j3 - l1;
            }

            model.normalise();
        }
        return model;
    }

    private void loadDefinition(final Buffer stream) {
        int _actions = -1;
        label0:
        do {
            int opcode;
            do {
                opcode = stream.getUnsignedByte();
                if (opcode == 0) {
                    break label0;
                }
                if (opcode == 1) {
                    final int modelCount = stream.getUnsignedByte();
                    if (modelCount > 0) {
                        if (this.modelIds == null || lowMemory) {
                            this.modelTypes = new int[modelCount];
                            this.modelIds = new int[modelCount];
                            for (int m = 0; m < modelCount; m++) {
                                this.modelIds[m] = stream.getUnsignedLEShort();
                                this.modelTypes[m] = stream.getUnsignedByte();
                            }

                        } else {
                            stream.position += modelCount * 3;
                        }
                    }
                } else if (opcode == 2) {
                    this.name = stream.getString();
                } else if (opcode == 3) {
                    this.description = stream.readBytes();
                } else if (opcode == 5) {
                    final int modelCount = stream.getUnsignedByte();
                    if (modelCount > 0) {
                        if (this.modelIds == null || lowMemory) {
                            this.modelTypes = null;
                            this.modelIds = new int[modelCount];
                            for (int m = 0; m < modelCount; m++) {
                                this.modelIds[m] = stream.getUnsignedLEShort();
                            }

                        } else {
                            stream.position += modelCount * 2;
                        }
                    }
                } else if (opcode == 14) {
                    this.sizeX = stream.getUnsignedByte();
                } else if (opcode == 15) {
                    this.sizeY = stream.getUnsignedByte();
                } else if (opcode == 17) {
                    this.solid = false;
                } else if (opcode == 18) {
                    this.walkable = false;
                } else if (opcode == 19) {
                    _actions = stream.getUnsignedByte();
                    if (_actions == 1) {
                        this.hasActions = true;
                    }
                } else if (opcode == 21) {
                    this.adjustToTerrain = true;
                } else if (opcode == 22) {
                    this.delayShading = true;
                } else if (opcode == 23) {
                    this.wall = true;
                } else if (opcode == 24) {
                    this.animationId = stream.getUnsignedLEShort();
                    if (this.animationId == 65535) {
                        this.animationId = -1;
                    }
                } else if (opcode == 28) {
                    this.offsetAmplifier = stream.getUnsignedByte();
                } else if (opcode == 29) {
                    this.ambient = stream.get();
                } else if (opcode == 39) {
                    this.diffuse = stream.get();
                } else if (opcode >= 30 && opcode < 39) {
                    if (this.actions == null) {
                        this.actions = new String[5];
                    }
                    this.actions[opcode - 30] = stream.getString();
                    if (this.actions[opcode - 30].equalsIgnoreCase("hidden")) {
                        this.actions[opcode - 30] = null;
                    }
                } else if (opcode == 40) {
                    final int colourCount = stream.getUnsignedByte();
                    this.modifiedModelColors = new int[colourCount];
                    this.originalModelColors = new int[colourCount];
                    for (int c = 0; c < colourCount; c++) {
                        this.modifiedModelColors[c] = stream.getUnsignedLEShort();
                        this.originalModelColors[c] = stream.getUnsignedLEShort();
                    }

                } else if (opcode == 60) {
                    this.icon = stream.getUnsignedLEShort();
                } else if (opcode == 62) {
                    this.rotated = true;
                } else if (opcode == 64) {
                    this.castsShadow = false;
                } else if (opcode == 65) {
                    this.scaleX = stream.getUnsignedLEShort();
                } else if (opcode == 66) {
                    this.scaleY = stream.getUnsignedLEShort();
                } else if (opcode == 67) {
                    this.scaleZ = stream.getUnsignedLEShort();
                } else if (opcode == 68) {
                    this.mapScene = stream.getUnsignedLEShort();
                } else if (opcode == 69) {
                    this.face = stream.getUnsignedByte();
                } else if (opcode == 70) {
                    this.translateX = stream.getShort();
                } else if (opcode == 71) {
                    this.translateY = stream.getShort();
                } else if (opcode == 72) {
                    this.translateZ = stream.getShort();
                } else if (opcode == 73) {
                    this.unknownAttribute = true;
                } else if (opcode == 74) {
                    this.unwalkableSolid = true;
                } else {
                    if (opcode != 75) {
                        continue;
                    }
                    this._solid = stream.getUnsignedByte();
                }
                continue label0;
            } while (opcode != 77);
            this.varBitId = stream.getUnsignedLEShort();
            if (this.varBitId == 65535) {
                this.varBitId = -1;
            }
            this.configIds = stream.getUnsignedLEShort();
            if (this.configIds == 65535) {
                this.configIds = -1;
            }
            final int childCount = stream.getUnsignedByte();
            this.childIds = new int[childCount + 1];
            for (int c = 0; c <= childCount; c++) {
                this.childIds[c] = stream.getUnsignedLEShort();
                if (this.childIds[c] == 65535) {
                    this.childIds[c] = -1;
                }
            }

        } while (true);
        if (_actions == -1) {
            this.hasActions = this.modelIds != null && (this.modelTypes == null || this.modelTypes[0] == 10);
            if (this.actions != null) {
                this.hasActions = true;
            }
        }
        if (this.unwalkableSolid) {
            this.solid = false;
            this.walkable = false;
        }
        if (this._solid == -1) {
            this._solid = this.solid ? 1 : 0;
        }
    }

    public boolean modelCached() {
        if (this.modelIds == null) {
            return true;
        }
        boolean cached = true;
        for (int m = 0; m < this.modelIds.length; m++) {
            cached &= Model.isCached(this.modelIds[m] & 0xffff);
        }
        return cached;
    }

    public boolean modelTypeCached(final int modelType) {
        if (this.modelTypes == null) {
            if (this.modelIds == null) {
                return true;
            }
            if (modelType != 10) {
                return true;
            }
            boolean cached = true;
            for (int id = 0; id < this.modelIds.length; id++) {
                cached &= Model.isCached(this.modelIds[id] & 0xffff);
            }

            return cached;
        }
        for (int type = 0; type < this.modelTypes.length; type++) {
            if (this.modelTypes[type] == modelType) {
                return Model.isCached(this.modelIds[type] & 0xffff);
            }
        }

        return true;
    }

    public void passivelyRequestModels(final OnDemandFetcher requester) {
        if (this.modelIds == null) {
            return;
        }
        for (int modelId = 0; modelId < this.modelIds.length; modelId++) {
            requester.passiveRequest(this.modelIds[modelId] & 0xffff, 0);
        }
    }

    private void setDefaults() {
        this.modelIds = null;
        this.modelTypes = null;
        this.name = null;
        this.description = null;
        this.modifiedModelColors = null;
        this.originalModelColors = null;
        this.sizeX = 1;
        this.sizeY = 1;
        this.solid = true;
        this.walkable = true;
        this.hasActions = false;
        this.adjustToTerrain = false;
        this.delayShading = false;
        this.wall = false;
        this.animationId = -1;
        this.offsetAmplifier = 16;
        this.ambient = 0;
        this.diffuse = 0;
        this.actions = null;
        this.icon = -1;
        this.mapScene = -1;
        this.rotated = false;
        this.castsShadow = true;
        this.scaleX = 128;
        this.scaleY = 128;
        this.scaleZ = 128;
        this.face = 0;
        this.translateX = 0;
        this.translateY = 0;
        this.translateZ = 0;
        this.unknownAttribute = false;
        this.unwalkableSolid = false;
        this._solid = -1;
        this.varBitId = -1;
        this.configIds = -1;
        this.childIds = null;
    }
}