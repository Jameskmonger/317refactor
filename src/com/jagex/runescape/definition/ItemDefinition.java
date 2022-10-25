package com.jagex.runescape.definition;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.Cache;

public final class ItemDefinition {

    public static ItemDefinition getDefinition(final int id) {
        for (int i = 0; i < 10; i++) {
            if (cache[i].id == id) {
                return cache[i];
            }
        }

        cacheIndex = (cacheIndex + 1) % 10;
        final ItemDefinition definition = cache[cacheIndex];
        stream.position = streamOffsets[id];
        definition.id = id;
        definition.setDefaults();
        definition.readValues(stream);
        if (definition.noteTemplateId != -1) {
            definition.toNote();
        }
        if (!membersWorld && definition.membersObject) {
            definition.name = "Members Object";
            definition.description = "Login to a members' server to use this object.".getBytes();
            definition.groundActions = null;
            definition.actions = null;
            definition.teamId = 0;
        }
        return definition;
    }

    public static Sprite getSprite(final int itemId, int itemAmount, final int type) {
        if (type == 0) {
            Sprite sprite = (Sprite) spriteCache.get(itemId);
            if (sprite != null && sprite.maxHeight != itemAmount && sprite.maxHeight != -1) {
                sprite.unlink();
                sprite = null;
            }
            if (sprite != null) {
                return sprite;
            }
        }
        ItemDefinition definition = getDefinition(itemId);
        if (definition.stackableIds == null) {
            itemAmount = -1;
        }
        if (itemAmount > 1) {
            int stackedId = -1;
            for (int amount = 0; amount < 10; amount++) {
                if (itemAmount >= definition.stackableAmounts[amount] && definition.stackableAmounts[amount] != 0) {
                    stackedId = definition.stackableIds[amount];
                }
            }

            if (stackedId != -1) {
                definition = getDefinition(stackedId);
            }
        }
        final Model model = definition.getAmountModel(1);
        if (model == null) {
            return null;
        }
        Sprite noteSprite = null;
        if (definition.noteTemplateId != -1) {
            noteSprite = getSprite(definition.noteId, 10, -1);
            if (noteSprite == null) {
                return null;
            }
        }
        final Sprite itemSprite = new Sprite(32, 32);
        final int textureCentreX = Rasterizer.centreX;
        final int textureCentreY = Rasterizer.centreY;
        final int[] lineOffsets = Rasterizer.lineOffsets;
        final int[] pixels = DrawingArea.pixels;
        final int width = DrawingArea.width;
        final int height = DrawingArea.height;
        final int topX = DrawingArea.topX;
        final int bottomX = DrawingArea.bottomX;
        final int topY = DrawingArea.topY;
        final int bottomY = DrawingArea.bottomY;
        Rasterizer.textured = false;
        DrawingArea.initDrawingArea(32, 32, itemSprite.pixels);
        DrawingArea.drawFilledRectangle(0, 0, 32, 32, 0);
        Rasterizer.setDefaultBounds();
        int zoom = definition.modelZoom;
        if (type == -1) {
            zoom = (int) (zoom * 1.5D);
        }
        if (type > 0) {
            zoom = (int) (zoom * 1.04D);
        }
        final int l3 = Rasterizer.SINE[definition.modelRotationX] * zoom >> 16;
        final int i4 = Rasterizer.COSINE[definition.modelRotationX] * zoom >> 16;
        model.renderSingle(definition.modelRotationY, definition.modelRotationZ, definition.modelRotationX,
            definition.modelOffset1, l3 + model.modelHeight / 2 + definition.modelOffset2,
            i4 + definition.modelOffset2);
        for (int _x = 31; _x >= 0; _x--) {
            for (int _y = 31; _y >= 0; _y--) {
                if (itemSprite.pixels[_x + _y * 32] == 0) {
                    if (_x > 0 && itemSprite.pixels[(_x - 1) + _y * 32] > 1) {
                        itemSprite.pixels[_x + _y * 32] = 1;
                    } else if (_y > 0 && itemSprite.pixels[_x + (_y - 1) * 32] > 1) {
                        itemSprite.pixels[_x + _y * 32] = 1;
                    } else if (_x < 31 && itemSprite.pixels[_x + 1 + _y * 32] > 1) {
                        itemSprite.pixels[_x + _y * 32] = 1;
                    } else if (_y < 31 && itemSprite.pixels[_x + (_y + 1) * 32] > 1) {
                        itemSprite.pixels[_x + _y * 32] = 1;
                    }
                }
            }

        }

        if (type > 0) {
            for (int _x = 31; _x >= 0; _x--) {
                for (int _y = 31; _y >= 0; _y--) {
                    if (itemSprite.pixels[_x + _y * 32] == 0) {
                        if (_x > 0 && itemSprite.pixels[(_x - 1) + _y * 32] == 1) {
                            itemSprite.pixels[_x + _y * 32] = type;
                        } else if (_y > 0 && itemSprite.pixels[_x + (_y - 1) * 32] == 1) {
                            itemSprite.pixels[_x + _y * 32] = type;
                        } else if (_x < 31 && itemSprite.pixels[_x + 1 + _y * 32] == 1) {
                            itemSprite.pixels[_x + _y * 32] = type;
                        } else if (_y < 31 && itemSprite.pixels[_x + (_y + 1) * 32] == 1) {
                            itemSprite.pixels[_x + _y * 32] = type;
                        }
                    }
                }

            }

        } else if (type == 0) {
            for (int _x = 31; _x >= 0; _x--) {
                for (int _y = 31; _y >= 0; _y--) {
                    if (itemSprite.pixels[_x + _y * 32] == 0 && _x > 0 && _y > 0
                        && itemSprite.pixels[(_x - 1) + (_y - 1) * 32] > 0) {
                        itemSprite.pixels[_x + _y * 32] = 0x302020;
                    }
                }

            }

        }
        if (definition.noteTemplateId != -1) {
            final int _maxWidth = noteSprite.maxWidth;
            final int _maxHeight = noteSprite.maxHeight;
            noteSprite.maxWidth = 32;
            noteSprite.maxHeight = 32;
            noteSprite.drawImage(0, 0);
            noteSprite.maxWidth = _maxWidth;
            noteSprite.maxHeight = _maxHeight;
        }
        if (type == 0) {
            spriteCache.put(itemSprite, itemId);
        }
        DrawingArea.initDrawingArea(height, width, pixels);
        DrawingArea.setDrawingArea(bottomY, topX, bottomX, topY);
        Rasterizer.centreX = textureCentreX;
        Rasterizer.centreY = textureCentreY;
        Rasterizer.lineOffsets = lineOffsets;
        Rasterizer.textured = true;
        if (definition.stackable) {
            itemSprite.maxWidth = 33;
        } else {
            itemSprite.maxWidth = 32;
        }
        itemSprite.maxHeight = itemAmount;
        return itemSprite;
    }

    public static void load(final Archive streamLoader) {
        stream = new Buffer(streamLoader.decompressFile("obj.dat"));
        final Buffer stream = new Buffer(streamLoader.decompressFile("obj.idx"));
        itemCount = stream.getUnsignedLEShort();
        streamOffsets = new int[itemCount];
        int offset = 2;
        for (int item = 0; item < itemCount; item++) {
            streamOffsets[item] = offset;
            offset += stream.getUnsignedLEShort();
        }

        cache = new ItemDefinition[10];
        for (int definition = 0; definition < 10; definition++) {
            cache[definition] = new ItemDefinition();
        }

    }

    public static void nullLoader() {
        modelCache = null;
        spriteCache = null;
        streamOffsets = null;
        cache = null;
        stream = null;
    }

    private byte equipModelTranslationFemale;

    public int value;

    private int[] modifiedModelColors;

    public int id;

    public static Cache spriteCache = new Cache(100);

    public static Cache modelCache = new Cache(50);

    private int[] originalModelColors;

    public boolean membersObject;

    private int femaleEquipModelIdEmblem;

    private int noteTemplateId;

    private int femaleEquipModelIdSecondary;
    private int maleEquipModelIdPrimary;
    private int maleDialogueHatModelId;
    private int modelScaleX;
    public String[] groundActions;
    private int modelOffset1;
    public String name;
    private static ItemDefinition[] cache;
    private int femaleDialogueHatModelId;
    private int modelId;
    private int maleDialogueModelId;
    public boolean stackable;
    public byte[] description;
    private int noteId;
    private static int cacheIndex;
    public int modelZoom;
    public static boolean membersWorld = true;
    private static Buffer stream;
    private int shadowModifier;
    private int maleEquipModelIdEmblem;
    private int maleEquipModelIdSecondary;
    public String[] actions;
    public int modelRotationX;
    private int modelScaleZ;
    private int modelScaleY;
    private int[] stackableIds;
    private int modelOffset2;
    private static int[] streamOffsets;
    private int lightModifier;
    private int femaleDialogueModelId;
    public int modelRotationY;
    private int femaleEquipModelIdPrimary;
    private int[] stackableAmounts;
    public int teamId;
    public static int itemCount;
    private int modelRotationZ;
    private byte equipModelTranslationMale;

    private ItemDefinition() {
        this.id = -1;
    }

    public boolean equipModelCached(final int gender) {
        int equipModelIdPrimary = this.maleEquipModelIdPrimary;
        int equipModelIdSecondary = this.maleEquipModelIdSecondary;
        int equipModelIdEmblem = this.maleEquipModelIdEmblem;
        if (gender == 1) {
            equipModelIdPrimary = this.femaleEquipModelIdPrimary;
            equipModelIdSecondary = this.femaleEquipModelIdSecondary;
            equipModelIdEmblem = this.femaleEquipModelIdEmblem;
        }
        if (equipModelIdPrimary == -1) {
            return true;
        }
        boolean cached = true;
        if (!Model.isCached(equipModelIdPrimary)) {
            cached = false;
        }
        if (equipModelIdSecondary != -1 && !Model.isCached(equipModelIdSecondary)) {
            cached = false;
        }
        if (equipModelIdEmblem != -1 && !Model.isCached(equipModelIdEmblem)) {
            cached = false;
        }
        return cached;
    }

    public Model getAmountModel(final int amount) {
        if (this.stackableIds != null && amount > 1) {
            int stackableId = -1;
            for (int i = 0; i < 10; i++) {
                if (amount >= this.stackableAmounts[i] && this.stackableAmounts[i] != 0) {
                    stackableId = this.stackableIds[i];
                }
            }

            if (stackableId != -1) {
                return getDefinition(stackableId).getAmountModel(1);
            }
        }
        Model stackedModel = (Model) modelCache.get(this.id);
        if (stackedModel != null) {
            return stackedModel;
        }
        stackedModel = Model.getModel(this.modelId);
        if (stackedModel == null) {
            return null;
        }
        if (this.modelScaleX != 128 || this.modelScaleY != 128 || this.modelScaleZ != 128) {
            stackedModel.scaleT(this.modelScaleX, this.modelScaleZ, this.modelScaleY);
        }
        if (this.modifiedModelColors != null) {
            for (int l = 0; l < this.modifiedModelColors.length; l++) {
                stackedModel.recolour(this.modifiedModelColors[l], this.originalModelColors[l]);
            }

        }
        stackedModel.applyLighting(64 + this.lightModifier, 768 + this.shadowModifier, -50, -10, -50, true);
        stackedModel.singleTile = true;
        modelCache.put(stackedModel, this.id);
        return stackedModel;
    }

    public Model getDialogueModel(final int gender) {
        int dialogueModelId = this.maleDialogueModelId;
        int dialogueHatModelId = this.maleDialogueHatModelId;
        if (gender == 1) {
            dialogueModelId = this.femaleDialogueModelId;
            dialogueHatModelId = this.femaleDialogueHatModelId;
        }
        if (dialogueModelId == -1) {
            return null;
        }
        Model dialogueModel = Model.getModel(dialogueModelId);
        if (dialogueHatModelId != -1) {
            final Model dialogueHatModel = Model.getModel(dialogueHatModelId);
            final Model[] dialogueModels = {dialogueModel, dialogueHatModel};
            dialogueModel = new Model(2, dialogueModels);
        }
        if (this.modifiedModelColors != null) {
            for (int c = 0; c < this.modifiedModelColors.length; c++) {
                dialogueModel.recolour(this.modifiedModelColors[c], this.originalModelColors[c]);
            }

        }
        return dialogueModel;
    }

    public Model getEquippedModel(final int gender) {
        int equipModelIdPrimary = this.maleEquipModelIdPrimary;
        int equipModelIdSecondary = this.maleEquipModelIdSecondary;
        int equipModelIdEmblem = this.maleEquipModelIdEmblem;
        if (gender == 1) {
            equipModelIdPrimary = this.femaleEquipModelIdPrimary;
            equipModelIdSecondary = this.femaleEquipModelIdSecondary;
            equipModelIdEmblem = this.femaleEquipModelIdEmblem;
        }
        if (equipModelIdPrimary == -1) {
            return null;
        }
        Model modelPrimary = Model.getModel(equipModelIdPrimary);
        if (equipModelIdSecondary != -1) {
            if (equipModelIdEmblem != -1) {
                final Model modelSecondary = Model.getModel(equipModelIdSecondary);
                final Model modelEmblem = Model.getModel(equipModelIdEmblem);
                final Model[] models = {modelPrimary, modelSecondary, modelEmblem};
                modelPrimary = new Model(3, models);
            } else {
                final Model modelSecondary = Model.getModel(equipModelIdSecondary);
                final Model[] models = {modelPrimary, modelSecondary};
                modelPrimary = new Model(2, models);
            }
        }
        if (gender == 0 && this.equipModelTranslationMale != 0) {
            modelPrimary.translate(0, this.equipModelTranslationMale, 0);
        }
        if (gender == 1 && this.equipModelTranslationFemale != 0) {
            modelPrimary.translate(0, this.equipModelTranslationFemale, 0);
        }
        if (this.modifiedModelColors != null) {
            for (int c = 0; c < this.modifiedModelColors.length; c++) {
                modelPrimary.recolour(this.modifiedModelColors[c], this.originalModelColors[c]);
            }

        }
        return modelPrimary;
    }

    public Model getInventoryModel(final int amount) {
        if (this.stackableIds != null && amount > 1) {
            int stackableId = -1;
            for (int i = 0; i < 10; i++) {
                if (amount >= this.stackableAmounts[i] && this.stackableAmounts[i] != 0) {
                    stackableId = this.stackableIds[i];
                }
            }

            if (stackableId != -1) {
                return getDefinition(stackableId).getInventoryModel(1);
            }
        }
        final Model stackedModel = Model.getModel(this.modelId);
        if (stackedModel == null) {
            return null;
        }
        if (this.modifiedModelColors != null) {
            for (int c = 0; c < this.modifiedModelColors.length; c++) {
                stackedModel.recolour(this.modifiedModelColors[c], this.originalModelColors[c]);
            }

        }
        return stackedModel;
    }

    public boolean isDialogueModelCached(final int gender) {
        int dialogueModelId = this.maleDialogueModelId;
        int dialogueHatModelId = this.maleDialogueHatModelId;
        if (gender == 1) {
            dialogueModelId = this.femaleDialogueModelId;
            dialogueHatModelId = this.femaleDialogueHatModelId;
        }
        if (dialogueModelId == -1) {
            return true;
        }
        boolean cached = true;
        if (!Model.isCached(dialogueModelId)) {
            cached = false;
        }
        if (dialogueHatModelId != -1 && !Model.isCached(dialogueHatModelId)) {
            cached = false;
        }
        return cached;
    }

    private void readValues(final Buffer stream) {
        do {
            final int opcode = stream.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                this.modelId = stream.getUnsignedLEShort();
            } else if (opcode == 2) {
                this.name = stream.getString();
            } else if (opcode == 3) {
                this.description = stream.readBytes();
            } else if (opcode == 4) {
                this.modelZoom = stream.getUnsignedLEShort();
            } else if (opcode == 5) {
                this.modelRotationX = stream.getUnsignedLEShort();
            } else if (opcode == 6) {
                this.modelRotationY = stream.getUnsignedLEShort();
            } else if (opcode == 7) {
                this.modelOffset1 = stream.getUnsignedLEShort();
                if (this.modelOffset1 > 32767) {
                    this.modelOffset1 -= 0x10000;
                }
            } else if (opcode == 8) {
                this.modelOffset2 = stream.getUnsignedLEShort();
                if (this.modelOffset2 > 32767) {
                    this.modelOffset2 -= 0x10000;
                }
            } else if (opcode == 10) {
                stream.getUnsignedLEShort();
            } else if (opcode == 11) {
                this.stackable = true;
            } else if (opcode == 12) {
                this.value = stream.getInt();
            } else if (opcode == 16) {
                this.membersObject = true;
            } else if (opcode == 23) {
                this.maleEquipModelIdPrimary = stream.getUnsignedLEShort();
                this.equipModelTranslationMale = stream.get();
            } else if (opcode == 24) {
                this.maleEquipModelIdSecondary = stream.getUnsignedLEShort();
            } else if (opcode == 25) {
                this.femaleEquipModelIdPrimary = stream.getUnsignedLEShort();
                this.equipModelTranslationFemale = stream.get();
            } else if (opcode == 26) {
                this.femaleEquipModelIdSecondary = stream.getUnsignedLEShort();
            } else if (opcode >= 30 && opcode < 35) {
                if (this.groundActions == null) {
                    this.groundActions = new String[5];
                }
                this.groundActions[opcode - 30] = stream.getString();
                if (this.groundActions[opcode - 30].equalsIgnoreCase("hidden")) {
                    this.groundActions[opcode - 30] = null;
                }
            } else if (opcode >= 35 && opcode < 40) {
                if (this.actions == null) {
                    this.actions = new String[5];
                }
                this.actions[opcode - 35] = stream.getString();
            } else if (opcode == 40) {
                final int colourCount = stream.getUnsignedByte();
                this.modifiedModelColors = new int[colourCount];
                this.originalModelColors = new int[colourCount];
                for (int c = 0; c < colourCount; c++) {
                    this.modifiedModelColors[c] = stream.getUnsignedLEShort();
                    this.originalModelColors[c] = stream.getUnsignedLEShort();
                }

            } else if (opcode == 78) {
                this.maleEquipModelIdEmblem = stream.getUnsignedLEShort();
            } else if (opcode == 79) {
                this.femaleEquipModelIdEmblem = stream.getUnsignedLEShort();
            } else if (opcode == 90) {
                this.maleDialogueModelId = stream.getUnsignedLEShort();
            } else if (opcode == 91) {
                this.femaleDialogueModelId = stream.getUnsignedLEShort();
            } else if (opcode == 92) {
                this.maleDialogueHatModelId = stream.getUnsignedLEShort();
            } else if (opcode == 93) {
                this.femaleDialogueHatModelId = stream.getUnsignedLEShort();
            } else if (opcode == 95) {
                this.modelRotationZ = stream.getUnsignedLEShort();
            } else if (opcode == 97) {
                this.noteId = stream.getUnsignedLEShort();
            } else if (opcode == 98) {
                this.noteTemplateId = stream.getUnsignedLEShort();
            } else if (opcode >= 100 && opcode < 110) {
                if (this.stackableIds == null) {
                    this.stackableIds = new int[10];
                    this.stackableAmounts = new int[10];
                }
                this.stackableIds[opcode - 100] = stream.getUnsignedLEShort();
                this.stackableAmounts[opcode - 100] = stream.getUnsignedLEShort();
            } else if (opcode == 110) {
                this.modelScaleX = stream.getUnsignedLEShort();
            } else if (opcode == 111) {
                this.modelScaleY = stream.getUnsignedLEShort();
            } else if (opcode == 112) {
                this.modelScaleZ = stream.getUnsignedLEShort();
            } else if (opcode == 113) {
                this.lightModifier = stream.get();
            } else if (opcode == 114) {
                this.shadowModifier = stream.get() * 5;
            } else if (opcode == 115) {
                this.teamId = stream.getUnsignedByte();
            }
        } while (true);
    }

    private void setDefaults() {
        this.modelId = 0;
        this.name = null;
        this.description = null;
        this.modifiedModelColors = null;
        this.originalModelColors = null;
        this.modelZoom = 2000;
        this.modelRotationX = 0;
        this.modelRotationY = 0;
        this.modelRotationZ = 0;
        this.modelOffset1 = 0;
        this.modelOffset2 = 0;
        this.stackable = false;
        this.value = 1;
        this.membersObject = false;
        this.groundActions = null;
        this.actions = null;
        this.maleEquipModelIdPrimary = -1;
        this.maleEquipModelIdSecondary = -1;
        this.equipModelTranslationMale = 0;
        this.femaleEquipModelIdPrimary = -1;
        this.femaleEquipModelIdSecondary = -1;
        this.equipModelTranslationFemale = 0;
        this.maleEquipModelIdEmblem = -1;
        this.femaleEquipModelIdEmblem = -1;
        this.maleDialogueModelId = -1;
        this.maleDialogueHatModelId = -1;
        this.femaleDialogueModelId = -1;
        this.femaleDialogueHatModelId = -1;
        this.stackableIds = null;
        this.stackableAmounts = null;
        this.noteId = -1;
        this.noteTemplateId = -1;
        this.modelScaleX = 128;
        this.modelScaleY = 128;
        this.modelScaleZ = 128;
        this.lightModifier = 0;
        this.shadowModifier = 0;
        this.teamId = 0;
    }

    private void toNote() {
        final ItemDefinition noteTemplateDefinition = getDefinition(this.noteTemplateId);
        this.modelId = noteTemplateDefinition.modelId;
        this.modelZoom = noteTemplateDefinition.modelZoom;
        this.modelRotationX = noteTemplateDefinition.modelRotationX;
        this.modelRotationY = noteTemplateDefinition.modelRotationY;

        this.modelRotationZ = noteTemplateDefinition.modelRotationZ;
        this.modelOffset1 = noteTemplateDefinition.modelOffset1;
        this.modelOffset2 = noteTemplateDefinition.modelOffset2;
        this.modifiedModelColors = noteTemplateDefinition.modifiedModelColors;
        this.originalModelColors = noteTemplateDefinition.originalModelColors;

        final ItemDefinition noteDefinition = getDefinition(this.noteId);
        this.name = noteDefinition.name;
        this.membersObject = noteDefinition.membersObject;
        this.value = noteDefinition.value;
        String prefix = "a";
        final char firstCharacter = noteDefinition.name.charAt(0);
        if (firstCharacter == 'A' || firstCharacter == 'E' || firstCharacter == 'I' || firstCharacter == 'O'
            || firstCharacter == 'U') {
            prefix = "an";
        }
        this.description = ("Swap this note at any bank for " + prefix + " " + noteDefinition.name + ".").getBytes();
        this.stackable = true;
    }

}
