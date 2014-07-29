// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class RSInterface
{

    public void swapInventoryItems(int i, int j)
    {
        int k = inventoryItemId[i];
        inventoryItemId[i] = inventoryItemId[j];
        inventoryItemId[j] = k;
        k = inventoryStackSize[i];
        inventoryStackSize[i] = inventoryStackSize[j];
        inventoryStackSize[j] = k;
    }

    public static void unpack(StreamLoader streamLoader, TextDrawingArea textDrawingAreas[], StreamLoader streamLoader_1)
    {
        aMRUNodes_238 = new MRUNodes(50000);
        Stream stream = new Stream(streamLoader.getDataForName("data"));
        int i = -1;
        int j = stream.getUnsignedLEShort();
        interfaceCache = new RSInterface[j];
        while(stream.currentOffset < stream.buffer.length)
        {
            int k = stream.getUnsignedLEShort();
            if(k == 65535)
            {
                i = stream.getUnsignedLEShort();
                k = stream.getUnsignedLEShort();
            }
            RSInterface rsInterface = interfaceCache[k] = new RSInterface();
            rsInterface.id = k;
            rsInterface.parentID = i;
            rsInterface.type = stream.getUnsignedByte();
            rsInterface.actionType = stream.getUnsignedByte();
            rsInterface.contentType = stream.getUnsignedLEShort();
            rsInterface.width = stream.getUnsignedLEShort();
            rsInterface.height = stream.getUnsignedLEShort();
            rsInterface.alpha = (byte) stream.getUnsignedByte();
            rsInterface.anInt230 = stream.getUnsignedByte();
            if(rsInterface.anInt230 != 0)
                rsInterface.anInt230 = (rsInterface.anInt230 - 1 << 8) + stream.getUnsignedByte();
            else
                rsInterface.anInt230 = -1;
            int i1 = stream.getUnsignedByte();
            if(i1 > 0)
            {
                rsInterface.anIntArray245 = new int[i1];
                rsInterface.anIntArray212 = new int[i1];
                for(int j1 = 0; j1 < i1; j1++)
                {
                    rsInterface.anIntArray245[j1] = stream.getUnsignedByte();
                    rsInterface.anIntArray212[j1] = stream.getUnsignedLEShort();
                }

            }
            int k1 = stream.getUnsignedByte();
            if(k1 > 0)
            {
                rsInterface.valueIndexArray = new int[k1][];
                for(int l1 = 0; l1 < k1; l1++)
                {
                    int i3 = stream.getUnsignedLEShort();
                    rsInterface.valueIndexArray[l1] = new int[i3];
                    for(int l4 = 0; l4 < i3; l4++)
                        rsInterface.valueIndexArray[l1][l4] = stream.getUnsignedLEShort();

                }

            }
            if(rsInterface.type == 0)
            {
                rsInterface.scrollMax = stream.getUnsignedLEShort();
                rsInterface.hoverOnly = stream.getUnsignedByte() == 1;
                int i2 = stream.getUnsignedLEShort();
                rsInterface.children = new int[i2];
                rsInterface.childX = new int[i2];
                rsInterface.childY = new int[i2];
                for(int j3 = 0; j3 < i2; j3++)
                {
                    rsInterface.children[j3] = stream.getUnsignedLEShort();
                    rsInterface.childX[j3] = stream.getShort();
                    rsInterface.childY[j3] = stream.getShort();
                }

            }
            if(rsInterface.type == 1)
            {
                stream.getUnsignedLEShort();
                stream.getUnsignedByte();
            }
            if(rsInterface.type == 2)
            {
                rsInterface.inventoryItemId = new int[rsInterface.width * rsInterface.height];
                rsInterface.inventoryStackSize = new int[rsInterface.width * rsInterface.height];
                rsInterface.itemSwappable = stream.getUnsignedByte() == 1;
                rsInterface.isInventoryInterface = stream.getUnsignedByte() == 1;
                rsInterface.usableItemInterface = stream.getUnsignedByte() == 1;
                rsInterface.itemDeletesDragged = stream.getUnsignedByte() == 1;
                rsInterface.inventorySpritePaddingColumn = stream.getUnsignedByte();
                rsInterface.inventorySpritePaddingRow = stream.getUnsignedByte();
                rsInterface.spritesX = new int[20];
                rsInterface.spritesY = new int[20];
                rsInterface.sprites = new Sprite[20];
                for(int j2 = 0; j2 < 20; j2++)
                {
                    int k3 = stream.getUnsignedByte();
                    if(k3 == 1)
                    {
                        rsInterface.spritesX[j2] = stream.getShort();
                        rsInterface.spritesY[j2] = stream.getShort();
                        String s1 = stream.getString();
                        if(streamLoader_1 != null && s1.length() > 0)
                        {
                            int i5 = s1.lastIndexOf(",");
                            rsInterface.sprites[j2] = method207(Integer.parseInt(s1.substring(i5 + 1)), streamLoader_1, s1.substring(0, i5));
                        }
                    }
                }

                rsInterface.actions = new String[5];
                for(int l3 = 0; l3 < 5; l3++)
                {
                    rsInterface.actions[l3] = stream.getString();
                    if(rsInterface.actions[l3].length() == 0)
                        rsInterface.actions[l3] = null;
                }

            }
            if(rsInterface.type == 3)
                rsInterface.filled = stream.getUnsignedByte() == 1;
            if(rsInterface.type == 4 || rsInterface.type == 1)
            {
                rsInterface.textCentred = stream.getUnsignedByte() == 1;
                int k2 = stream.getUnsignedByte();
                if(textDrawingAreas != null)
                    rsInterface.textDrawingAreas = textDrawingAreas[k2];
                rsInterface.aBoolean268 = stream.getUnsignedByte() == 1;
            }
            if(rsInterface.type == 4)
            {
                rsInterface.message = stream.getString();
                rsInterface.aString228 = stream.getString();
            }
            if(rsInterface.type == 1 || rsInterface.type == 3 || rsInterface.type == 4)
                rsInterface.colourDefault = stream.getInt();
            if(rsInterface.type == 3 || rsInterface.type == 4)
            {
                rsInterface.colourActive = stream.getInt();
                rsInterface.colourDefaultHover = stream.getInt();
                rsInterface.colourActiveHover = stream.getInt();
            }
            if(rsInterface.type == 5)
            {
                String s = stream.getString();
                if(streamLoader_1 != null && s.length() > 0)
                {
                    int i4 = s.lastIndexOf(",");
                    rsInterface.spriteDefault = method207(Integer.parseInt(s.substring(i4 + 1)), streamLoader_1, s.substring(0, i4));
                }
                s = stream.getString();
                if(streamLoader_1 != null && s.length() > 0)
                {
                    int j4 = s.lastIndexOf(",");
                    rsInterface.spriteSelected = method207(Integer.parseInt(s.substring(j4 + 1)), streamLoader_1, s.substring(0, j4));
                }
            }
            if(rsInterface.type == 6)
            {
                int l = stream.getUnsignedByte();
                if(l != 0)
                {
                    rsInterface.modelType = 1;
                    rsInterface.modelId = (l - 1 << 8) + stream.getUnsignedByte();
                }
                l = stream.getUnsignedByte();
                if(l != 0)
                {
                    rsInterface.anInt255 = 1;
                    rsInterface.anInt256 = (l - 1 << 8) + stream.getUnsignedByte();
                }
                l = stream.getUnsignedByte();
                if(l != 0)
                    rsInterface.animationDefaultId = (l - 1 << 8) + stream.getUnsignedByte();
                else
                    rsInterface.animationDefaultId = -1;
                l = stream.getUnsignedByte();
                if(l != 0)
                    rsInterface.animationActiveId = (l - 1 << 8) + stream.getUnsignedByte();
                else
                    rsInterface.animationActiveId = -1;
                rsInterface.modelZoom = stream.getUnsignedLEShort();
                rsInterface.modelRotationX = stream.getUnsignedLEShort();
                rsInterface.modelRotationY = stream.getUnsignedLEShort();
            }
            if(rsInterface.type == 7)
            {
                rsInterface.inventoryItemId = new int[rsInterface.width * rsInterface.height];
                rsInterface.inventoryStackSize = new int[rsInterface.width * rsInterface.height];
                rsInterface.textCentred = stream.getUnsignedByte() == 1;
                int l2 = stream.getUnsignedByte();
                if(textDrawingAreas != null)
                    rsInterface.textDrawingAreas = textDrawingAreas[l2];
                rsInterface.aBoolean268 = stream.getUnsignedByte() == 1;
                rsInterface.colourDefault = stream.getInt();
                rsInterface.inventorySpritePaddingColumn = stream.getShort();
                rsInterface.inventorySpritePaddingRow = stream.getShort();
                rsInterface.isInventoryInterface = stream.getUnsignedByte() == 1;
                rsInterface.actions = new String[5];
                for(int k4 = 0; k4 < 5; k4++)
                {
                    rsInterface.actions[k4] = stream.getString();
                    if(rsInterface.actions[k4].length() == 0)
                        rsInterface.actions[k4] = null;
                }

            }
            if(rsInterface.actionType == 2 || rsInterface.type == 2)
            {
                rsInterface.selectedActionName = stream.getString();
                rsInterface.spellName = stream.getString();
                rsInterface.spellUsableOn = stream.getUnsignedLEShort();
            }

            if(rsInterface.type == 8)
			  rsInterface.message = stream.getString();

            if(rsInterface.actionType == 1 || rsInterface.actionType == 4 || rsInterface.actionType == 5 || rsInterface.actionType == 6)
            {
                rsInterface.tooltip = stream.getString();
                if(rsInterface.tooltip.length() == 0)
                {
                    if(rsInterface.actionType == 1)
                        rsInterface.tooltip = "Ok";
                    if(rsInterface.actionType == 4)
                        rsInterface.tooltip = "Select";
                    if(rsInterface.actionType == 5)
                        rsInterface.tooltip = "Select";
                    if(rsInterface.actionType == 6)
                        rsInterface.tooltip = "Continue";
                }
            }
        
//aryan	Bot.notifyInterface(rsInterface);
	}
        aMRUNodes_238 = null;
    }

    private Model method206(int i, int j)
    {
        Model model = (Model) aMRUNodes_264.insertFromCache((i << 16) + j);
        if(model != null)
            return model;
        if(i == 1)
            model = Model.getModel(j);
        if(i == 2)
            model = EntityDef.forID(j).method160();
        if(i == 3)
            model = client.localPlayer.method453();
        if(i == 4)
            model = ItemDef.forID(j).method202(50);
        if(i == 5)
            model = null;
        if(model != null)
            aMRUNodes_264.removeFromCache(model, (i << 16) + j);
        return model;
    }

    private static Sprite method207(int i, StreamLoader streamLoader, String s)
    {
        long l = (TextClass.encodeSpriteName(s) << 8) + (long)i;
        Sprite sprite = (Sprite) aMRUNodes_238.insertFromCache(l);
        if(sprite != null)
            return sprite;
        try
        {
            sprite = new Sprite(streamLoader, s, i);
            aMRUNodes_238.removeFromCache(sprite, l);
        }
        catch(Exception _ex)
        {
            return null;
        }
        return sprite;
    }

    public static void method208(boolean flag, Model model)
    {
        int i = 0;//was parameter
        int j = 5;//was parameter
        if(flag)
            return;
        aMRUNodes_264.unlinkAll();
        if(model != null && j != 4)
            aMRUNodes_264.removeFromCache(model, (j << 16) + i);
    }

    public Model getAnimatedModel(int j, int k, boolean flag)
    {
        Model model;
        if(flag)
            model = method206(anInt255, anInt256);
        else
            model = method206(modelType, modelId);
        if(model == null)
            return null;
        if(k == -1 && j == -1 && model.triangleColours == null)
            return model;
        Model model_1 = new Model(true, Class36.isNullFrame(k) & Class36.isNullFrame(j), false, model);
        if(k != -1 || j != -1)
            model_1.createBones();
        if(k != -1)
            model_1.applyTransformation(k);
        if(j != -1)
            model_1.applyTransformation(j);
        model_1.applyLighting(64, 768, -50, -10, -50, true);
            return model_1;
    }

    public RSInterface()
    {
    }

    public Sprite spriteDefault;
    public int animationDuration;
    public Sprite sprites[];
    public static RSInterface interfaceCache[];
    public int anIntArray212[];
    public int contentType;
    public int spritesX[];
    public int colourDefaultHover;
    public int actionType;
    public String spellName;
    public int colourActive;
    public int width;
    public String tooltip;
    public String selectedActionName;
    public boolean textCentred;
    public int scrollPosition;
    public String actions[];
    public int valueIndexArray[][];
    public boolean filled;
    public String aString228;
    public int anInt230;
    public int inventorySpritePaddingColumn;
    public int colourDefault;
    public int modelType;
    public int modelId;
    public boolean itemDeletesDragged;
    public int parentID;
    public int spellUsableOn;
    private static MRUNodes aMRUNodes_238;
    public int colourActiveHover;
    public int children[];
    public int childX[];
    public boolean usableItemInterface;
    public TextDrawingArea textDrawingAreas;
    public int inventorySpritePaddingRow;
    public int anIntArray245[];
    public int animationFrame;
    public int spritesY[];
    public String message;
    public boolean isInventoryInterface;
    public int id;
    public int inventoryStackSize[];
    public int inventoryItemId[];
    public byte alpha;
    private int anInt255;
    private int anInt256;
    public int animationDefaultId;
    public int animationActiveId;
    public boolean itemSwappable;
    public Sprite spriteSelected;
    public int scrollMax;
    public int type;
    public int x;
    private static final MRUNodes aMRUNodes_264 = new MRUNodes(30);
    public int y;
    public boolean hoverOnly;
    public int height;
    public boolean aBoolean268;
    public int modelZoom;
    public int modelRotationX;
    public int modelRotationY;
    public int childY[];

}
