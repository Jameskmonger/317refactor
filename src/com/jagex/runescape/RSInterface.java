package com.jagex.runescape;

import com.jagex.runescape.collection.Cache;
import com.jagex.runescape.definition.EntityDefinition;
import com.jagex.runescape.definition.GameFont;
import com.jagex.runescape.definition.ItemDefinition;

public final class RSInterface {

    private static Sprite getImage(final int spriteId, final Archive streamLoader, final String spriteName) {
        final long spriteHash = (TextClass.spriteNameToHash(spriteName) << 8) + spriteId;
        Sprite sprite = (Sprite) spriteCache.get(spriteHash);
        if (sprite != null) {
            return sprite;
        }
        try {
            sprite = new Sprite(streamLoader, spriteName, spriteId);
            spriteCache.put(sprite, spriteHash);
        } catch (final Exception _ex) {
            return null;
        }
        return sprite;
    }

    public static void setModel(final Model model) {
        final int modelId = 0;// was parameter
        final int modelType = 5;// was parameter
        modelCache.clear();
        if (model != null && modelType != 4) {
            modelCache.put(model, (modelType << 16) + modelId);
        }
    }

    public static void unpack(final Archive streamLoader, final GameFont[] fonts, final Archive mediaArchive) {
        spriteCache = new Cache(50000);
        final Buffer stream = new Buffer(streamLoader.decompressFile("data"));
        int parentId = -1;
        final int interfaceCount = stream.getUnsignedLEShort();
        cache = new RSInterface[interfaceCount];
        while (stream.position < stream.buffer.length) {
            int id = stream.getUnsignedLEShort();
            if (id == 65535) {
                parentId = stream.getUnsignedLEShort();
                id = stream.getUnsignedLEShort();
            }
            final RSInterface rsInterface = cache[id] = new RSInterface();
            rsInterface.id = id;
            rsInterface.parentID = parentId;
            rsInterface.type = stream.getUnsignedByte();
            rsInterface.actionType = stream.getUnsignedByte();
            rsInterface.contentType = stream.getUnsignedLEShort();
            rsInterface.width = stream.getUnsignedLEShort();
            rsInterface.height = stream.getUnsignedLEShort();
            rsInterface.alpha = (byte) stream.getUnsignedByte();
            rsInterface.hoveredPopup = stream.getUnsignedByte();
            if (rsInterface.hoveredPopup != 0) {
                rsInterface.hoveredPopup = (rsInterface.hoveredPopup - 1 << 8) + stream.getUnsignedByte();
            } else {
                rsInterface.hoveredPopup = -1;
            }
            final int conditionCount = stream.getUnsignedByte();
            if (conditionCount > 0) {
                rsInterface.conditionType = new int[conditionCount];
                rsInterface.conditionValue = new int[conditionCount];
                for (int c = 0; c < conditionCount; c++) {
                    rsInterface.conditionType[c] = stream.getUnsignedByte();
                    rsInterface.conditionValue[c] = stream.getUnsignedLEShort();
                }

            }
            final int opcodeCount = stream.getUnsignedByte();
            if (opcodeCount > 0) {
                rsInterface.opcodes = new int[opcodeCount][];
                for (int c = 0; c < opcodeCount; c++) {
                    final int subOpcodeCount = stream.getUnsignedLEShort();
                    rsInterface.opcodes[c] = new int[subOpcodeCount];
                    for (int s = 0; s < subOpcodeCount; s++) {
                        rsInterface.opcodes[c][s] = stream.getUnsignedLEShort();
                    }

                }

            }
            if (rsInterface.type == 0) {
                rsInterface.scrollMax = stream.getUnsignedLEShort();
                rsInterface.hoverOnly = stream.getUnsignedByte() == 1;
                final int childCount = stream.getUnsignedLEShort();
                rsInterface.children = new int[childCount];
                rsInterface.childX = new int[childCount];
                rsInterface.childY = new int[childCount];
                for (int child = 0; child < childCount; child++) {
                    rsInterface.children[child] = stream.getUnsignedLEShort();
                    rsInterface.childX[child] = stream.getShort();
                    rsInterface.childY[child] = stream.getShort();
                }

            }
            if (rsInterface.type == 1) {
                stream.getUnsignedLEShort();
                stream.getUnsignedByte();
            }
            if (rsInterface.type == 2) {
                rsInterface.inventoryItemId = new int[rsInterface.width * rsInterface.height];
                rsInterface.inventoryStackSize = new int[rsInterface.width * rsInterface.height];
                rsInterface.itemSwappable = stream.getUnsignedByte() == 1;
                rsInterface.inventory = stream.getUnsignedByte() == 1;
                rsInterface.usableItemInterface = stream.getUnsignedByte() == 1;
                rsInterface.itemDeletesDragged = stream.getUnsignedByte() == 1;
                rsInterface.inventorySpritePaddingColumn = stream.getUnsignedByte();
                rsInterface.inventorySpritePaddingRow = stream.getUnsignedByte();
                rsInterface.spritesX = new int[20];
                rsInterface.spritesY = new int[20];
                rsInterface.sprites = new Sprite[20];
                for (int sprite = 0; sprite < 20; sprite++) {
                    final int spriteExists = stream.getUnsignedByte();
                    if (spriteExists == 1) {
                        rsInterface.spritesX[sprite] = stream.getShort();
                        rsInterface.spritesY[sprite] = stream.getShort();
                        final String name = stream.getString();
                        if (mediaArchive != null && name.length() > 0) {
                            final int spriteId = name.lastIndexOf(",");
                            rsInterface.sprites[sprite] = getImage(Integer.parseInt(name.substring(spriteId + 1)),
                                mediaArchive, name.substring(0, spriteId));
                        }
                    }
                }

                rsInterface.actions = new String[5];
                for (int action = 0; action < 5; action++) {
                    rsInterface.actions[action] = stream.getString();
                    if (rsInterface.actions[action].length() == 0) {
                        rsInterface.actions[action] = null;
                    }
                }

            }
            if (rsInterface.type == 3) {
                rsInterface.filled = stream.getUnsignedByte() == 1;
            }
            if (rsInterface.type == 4 || rsInterface.type == 1) {
                rsInterface.textCentred = stream.getUnsignedByte() == 1;
                final int font = stream.getUnsignedByte();
                if (fonts != null) {
                    rsInterface.textDrawingAreas = fonts[font];
                }
                rsInterface.textShadowed = stream.getUnsignedByte() == 1;
            }
            if (rsInterface.type == 4) {
                rsInterface.textDefault = stream.getString();
                rsInterface.textActive = stream.getString();
            }
            if (rsInterface.type == 1 || rsInterface.type == 3 || rsInterface.type == 4) {
                rsInterface.colourDefault = stream.getInt();
            }
            if (rsInterface.type == 3 || rsInterface.type == 4) {
                rsInterface.colourActive = stream.getInt();
                rsInterface.colourDefaultHover = stream.getInt();
                rsInterface.colourActiveHover = stream.getInt();
            }
            if (rsInterface.type == 5) {
                String spriteName = stream.getString();
                if (mediaArchive != null && spriteName.length() > 0) {
                    final int spriteId = spriteName.lastIndexOf(",");
                    rsInterface.spriteDefault = getImage(Integer.parseInt(spriteName.substring(spriteId + 1)),
                        mediaArchive, spriteName.substring(0, spriteId));
                }
                spriteName = stream.getString();
                if (mediaArchive != null && spriteName.length() > 0) {
                    final int spriteId = spriteName.lastIndexOf(",");
                    rsInterface.spriteActive = getImage(Integer.parseInt(spriteName.substring(spriteId + 1)),
                        mediaArchive, spriteName.substring(0, spriteId));
                }
            }
            if (rsInterface.type == 6) {
                int interfaceId = stream.getUnsignedByte();
                if (interfaceId != 0) {
                    rsInterface.modelTypeDefault = 1;
                    rsInterface.modelIdDefault = (interfaceId - 1 << 8) + stream.getUnsignedByte();
                }
                interfaceId = stream.getUnsignedByte();
                if (interfaceId != 0) {
                    rsInterface.modelTypeActive = 1;
                    rsInterface.modelIdActive = (interfaceId - 1 << 8) + stream.getUnsignedByte();
                }
                interfaceId = stream.getUnsignedByte();
                if (interfaceId != 0) {
                    rsInterface.animationIdDefault = (interfaceId - 1 << 8) + stream.getUnsignedByte();
                } else {
                    rsInterface.animationIdDefault = -1;
                }
                interfaceId = stream.getUnsignedByte();
                if (interfaceId != 0) {
                    rsInterface.animationIdActive = (interfaceId - 1 << 8) + stream.getUnsignedByte();
                } else {
                    rsInterface.animationIdActive = -1;
                }
                rsInterface.modelZoom = stream.getUnsignedLEShort();
                rsInterface.modelRotationX = stream.getUnsignedLEShort();
                rsInterface.modelRotationY = stream.getUnsignedLEShort();
            }
            if (rsInterface.type == 7) {
                rsInterface.inventoryItemId = new int[rsInterface.width * rsInterface.height];
                rsInterface.inventoryStackSize = new int[rsInterface.width * rsInterface.height];
                rsInterface.textCentred = stream.getUnsignedByte() == 1;
                final int font = stream.getUnsignedByte();
                if (fonts != null) {
                    rsInterface.textDrawingAreas = fonts[font];
                }
                rsInterface.textShadowed = stream.getUnsignedByte() == 1;
                rsInterface.colourDefault = stream.getInt();
                rsInterface.inventorySpritePaddingColumn = stream.getShort();
                rsInterface.inventorySpritePaddingRow = stream.getShort();
                rsInterface.inventory = stream.getUnsignedByte() == 1;
                rsInterface.actions = new String[5];
                for (int active = 0; active < 5; active++) {
                    rsInterface.actions[active] = stream.getString();
                    if (rsInterface.actions[active].length() == 0) {
                        rsInterface.actions[active] = null;
                    }
                }

            }
            if (rsInterface.actionType == 2 || rsInterface.type == 2) {
                rsInterface.selectedActionName = stream.getString();
                rsInterface.spellName = stream.getString();
                rsInterface.spellUsableOn = stream.getUnsignedLEShort();
            }

            if (rsInterface.type == 8) {
                rsInterface.textDefault = stream.getString();
            }

            if (rsInterface.actionType == 1 || rsInterface.actionType == 4 || rsInterface.actionType == 5
                || rsInterface.actionType == 6) {
                rsInterface.tooltip = stream.getString();
                if (rsInterface.tooltip.length() == 0) {
                    if (rsInterface.actionType == 1) {
                        rsInterface.tooltip = "Ok";
                    }
                    if (rsInterface.actionType == 4) {
                        rsInterface.tooltip = "Select";
                    }
                    if (rsInterface.actionType == 5) {
                        rsInterface.tooltip = "Select";
                    }
                    if (rsInterface.actionType == 6) {
                        rsInterface.tooltip = "Continue";
                    }
                }
            }
        }
        spriteCache = null;
    }

    public Sprite spriteDefault;

    public int animationDuration;

    public Sprite[] sprites;

    public static RSInterface[] cache;

    public int[] conditionValue;
    public int contentType;
    public int[] spritesX;
    public int colourDefaultHover;
    public int actionType;
    public String spellName;
    public int colourActive;
    public int width;
    public String tooltip;
    public String selectedActionName;
    public boolean textCentred;
    public int scrollPosition;
    public String[] actions;
    public int[][] opcodes;
    public boolean filled;
    public String textActive;
    public int hoveredPopup;
    public int inventorySpritePaddingColumn;
    public int colourDefault;
    public int modelTypeDefault;
    public int modelIdDefault;
    public boolean itemDeletesDragged;
    public int parentID;
    public int spellUsableOn;
    private static Cache spriteCache;
    public int colourActiveHover;
    public int[] children;
    public int[] childX;
    public boolean usableItemInterface;
    public GameFont textDrawingAreas;
    public int inventorySpritePaddingRow;
    public int[] conditionType;
    public int animationFrame;
    public int[] spritesY;
    public String textDefault;
    public boolean inventory;
    public int id;
    public int[] inventoryStackSize;
    public int[] inventoryItemId;
    public byte alpha;
    private int modelTypeActive;
    private int modelIdActive;
    public int animationIdDefault;
    public int animationIdActive;
    public boolean itemSwappable;
    public Sprite spriteActive;
    public int scrollMax;
    public int type;
    public int x;
    private static final Cache modelCache = new Cache(30);
    public int y;
    public boolean hoverOnly;
    public int height;
    public boolean textShadowed;
    public int modelZoom;
    public int modelRotationX;
    public int modelRotationY;
    public int[] childY;

    public RSInterface() {
    }

    public Model getAnimatedModel(final int frame1Id, final int frame2Id, final boolean active) {
        final Model model;
        if (active) {
            model = this.getModel(this.modelTypeActive, this.modelIdActive);
        } else {
            model = this.getModel(this.modelTypeDefault, this.modelIdDefault);
        }
        if (model == null) {
            return null;
        }
        if (frame2Id == -1 && frame1Id == -1 && model.triangleColours == null) {
            return model;
        }

        final Model animatedModel = new Model(true, Animation.isNullFrame(frame2Id) & Animation.isNullFrame(frame1Id), false,
            model);
        if (frame2Id != -1 || frame1Id != -1) {
            animatedModel.createBones();
        }
        if (frame2Id != -1) {
            animatedModel.applyTransformation(frame2Id);
        }
        if (frame1Id != -1) {
            animatedModel.applyTransformation(frame1Id);
        }
        animatedModel.applyLighting(64, 768, -50, -10, -50, true);
        return animatedModel;
    }

    private Model getModel(final int modelType, final int modelId) {
        Model model = (Model) modelCache.get(((long) modelType << 16) + modelId);
        if (model != null) {
            return model;
        }
        if (modelType == 1) {
            model = Model.getModel(modelId);
        }
        if (modelType == 2) {
            model = EntityDefinition.getDefinition(modelId).getHeadModel();
        }
        if (modelType == 3) {
            model = Client.localPlayer.getHeadModel();
        }
        if (modelType == 4) {
            model = ItemDefinition.getDefinition(modelId).getInventoryModel(50);
        }
        if (modelType == 5) {
            model = null;
        }
        if (model != null) {
            modelCache.put(model, (modelType << 16) + modelId);
        }
        return model;
    }

    public void swapInventoryItems(final int originalSlot, final int newSlot) {
        int originalItem = this.inventoryItemId[originalSlot];
        this.inventoryItemId[originalSlot] = this.inventoryItemId[newSlot];
        this.inventoryItemId[newSlot] = originalItem;

        originalItem = this.inventoryStackSize[originalSlot];
        this.inventoryStackSize[originalSlot] = this.inventoryStackSize[newSlot];
        this.inventoryStackSize[newSlot] = originalItem;
    }

}
