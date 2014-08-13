public final class GameObjectDefinition
{

    public static GameObjectDefinition forID(int objectId)
    {
        for(int c = 0; c < 20; c++)
            if(cache[c].type == objectId)
                return cache[c];

        cacheIndex = (cacheIndex + 1) % 20;
        GameObjectDefinition definition = cache[cacheIndex];
        stream.currentOffset = streamIndices[objectId];
        definition.type = objectId;
        definition.setDefaults();
        definition.readValues(stream);
        return definition;
    }

    private void setDefaults()
    {
        modelIds = null;
        modelTypes = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
        sizeX = 1;
        sizeY = 1;
        unwalkable = true;
        walkable = true;
        hasActions = false;
        adjustToTerrain = false;
        delayShading = false;
        aBoolean764 = false;
        anInt781 = -1;
        anInt775 = 16;
        aByte737 = 0;
        aByte742 = 0;
        actions = null;
        icon = -1;
        mapScene = -1;
        aBoolean751 = false;
        aBoolean779 = true;
        anInt748 = 128;
        anInt772 = 128;
        anInt740 = 128;
        face = 0;
        anInt738 = 0;
        anInt745 = 0;
        anInt783 = 0;
        aBoolean736 = false;
        aBoolean766 = false;
        anInt760 = -1;
        varBitId = -1;
        configIds = -1;
        childrenIds = null;
    }

    public void passivelyRequestModels(OnDemandFetcher requester)
    {
        if(modelIds == null)
            return;
        for(int modelId = 0; modelId < modelIds.length; modelId++)
            requester.passiveRequest(modelIds[modelId] & 0xffff, 0);
    }

    public static void nullLoader()
    {
        modelCache = null;
        mruNodes2 = null;
        streamIndices = null;
        cache = null;
        stream = null;
    }

    public static void load(StreamLoader archive)
    {
        stream = new Stream(archive.getDataForName("loc.dat"));
        Stream stream = new Stream(archive.getDataForName("loc.idx"));
        int totalObjects = stream.getUnsignedLEShort();
        streamIndices = new int[totalObjects];
        int offset = 2;
        for(int index = 0; index < totalObjects; index++)
        {
            streamIndices[index] = offset;
            offset += stream.getUnsignedLEShort();
        }

        cache = new GameObjectDefinition[20];
        for(int c = 0; c < 20; c++)
            cache[c] = new GameObjectDefinition();

    }

    public boolean modelTypeCached(int modelType)
    {
        if(modelTypes == null)
        {
            if(modelIds == null)
                return true;
            if(modelType != 10)
                return true;
            boolean cached = true;
            for(int id = 0; id < modelIds.length; id++)
                cached &= Model.isCached(modelIds[id] & 0xffff);

            return cached;
        }
        for(int type = 0; type < modelTypes.length; type++)
            if(modelTypes[type] == modelType)
                return Model.isCached(modelIds[type] & 0xffff);

        return true;
    }

    public Model getModelAt(int i, int j, int k, int l, int i1, int j1, int k1)
    {
        Model model = getAnimatedModel(i, k1, j);
        if(model == null)
            return null;
        if(adjustToTerrain || delayShading)
            model = new Model(adjustToTerrain, delayShading, model);
        if(adjustToTerrain)
        {
            int l1 = (k + l + i1 + j1) / 4;
            for(int v = 0; v < model.vertexCount; v++)
            {
                int x = model.verticesX[v];
                int z = model.verticesZ[v];
                int l2 = k + ((l - k) * (x + 64)) / 128;
                int i3 = j1 + ((i1 - j1) * (x + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.verticesY[v] += j3 - l1;
            }

            model.normalise();
        }
        return model;
    }

    public boolean modelCached()
    {
        if(modelIds == null)
            return true;
        boolean cached = true;
        for(int m = 0; m < modelIds.length; m++)
            cached &= Model.isCached(modelIds[m] & 0xffff);
            return cached;
    }

    public GameObjectDefinition getChildDefinition()
    {
        int child = -1;
        if(varBitId != -1)
        {
            VarBit varBit = VarBit.cache[varBitId];
            int configId = varBit.configId;
            int lsb = varBit.leastSignificantBit;
            int msb = varBit.mostSignificantBit;
            int bit = client.BITFIELD_MAX_VALUE[msb - lsb];
            child = clientInstance.variousSettings[configId] >> lsb & bit;
        } else
        if(configIds != -1)
            child = clientInstance.variousSettings[configIds];
        if(child < 0 || child >= childrenIds.length || childrenIds[child] == -1)
            return null;
        else
            return forID(childrenIds[child]);
    }

    private Model getAnimatedModel(int j, int k, int l)
    {
        Model model = null;
        long l1;
        if(modelTypes == null)
        {
            if(j != 10)
                return null;
            l1 = (long)((type << 6) + l) + ((long)(k + 1) << 32);
            Model model_1 = (Model) mruNodes2.get(l1);
            if(model_1 != null)
                return model_1;
            if(modelIds == null)
                return null;
            boolean flag1 = aBoolean751 ^ (l > 3);
            int k1 = modelIds.length;
            for(int i2 = 0; i2 < k1; i2++)
            {
                int l2 = modelIds[i2];
                if(flag1)
                    l2 += 0x10000;
                model = (Model) modelCache.get(l2);
                if(model == null)
                {
                    model = Model.getModel(l2 & 0xffff);
                    if(model == null)
                        return null;
                    if(flag1)
                        model.mirror();
                    modelCache.put(model, l2);
                }
                if(k1 > 1)
                    aModelArray741s[i2] = model;
            }

            if(k1 > 1)
                model = new Model(k1, aModelArray741s);
        } else
        {
            int i1 = -1;
            for(int j1 = 0; j1 < modelTypes.length; j1++)
            {
                if(modelTypes[j1] != j)
                    continue;
                i1 = j1;
                break;
            }

            if(i1 == -1)
                return null;
            l1 = (long)((type << 6) + (i1 << 3) + l) + ((long)(k + 1) << 32);
            Model model_2 = (Model) mruNodes2.get(l1);
            if(model_2 != null)
                return model_2;
            int j2 = modelIds[i1];
            boolean flag3 = aBoolean751 ^ (l > 3);
            if(flag3)
                j2 += 0x10000;
            model = (Model) modelCache.get(j2);
            if(model == null)
            {
                model = Model.getModel(j2 & 0xffff);
                if(model == null)
                    return null;
                if(flag3)
                    model.mirror();
                modelCache.put(model, j2);
            }
        }
        boolean flag;
        flag = anInt748 != 128 || anInt772 != 128 || anInt740 != 128;
        boolean flag2;
        flag2 = anInt738 != 0 || anInt745 != 0 || anInt783 != 0;
        Model model_3 = new Model(modifiedModelColors == null, Animation.isNullFrame(k), l == 0 && k == -1 && !flag && !flag2, model);
        if(k != -1)
        {
            model_3.createBones();
            model_3.applyTransformation(k);
            model_3.triangleSkin = null;
            model_3.vertexSkin = null;
        }
        while(l-- > 0) 
            model_3.rotate90Degrees();
        if(modifiedModelColors != null)
        {
            for(int k2 = 0; k2 < modifiedModelColors.length; k2++)
                model_3.recolour(modifiedModelColors[k2], originalModelColors[k2]);

        }
        if(flag)
            model_3.scaleT(anInt748, anInt740, anInt772);
        if(flag2)
            model_3.translate(anInt738, anInt745, anInt783);
        model_3.applyLighting(64 + aByte737, 768 + aByte742 * 5, -50, -10, -50, !delayShading);
        if(anInt760 == 1)
            model_3.anInt1654 = model_3.modelHeight;
        mruNodes2.put(model_3, l1);
        return model_3;
    }

    private void readValues(Stream stream)
    {
        int i = -1;
label0:
        do
        {
            int j;
            do
            {
                j = stream.getUnsignedByte();
                if(j == 0)
                    break label0;
                if(j == 1)
                {
                    int k = stream.getUnsignedByte();
                    if(k > 0)
                        if(modelIds == null || lowMem)
                        {
                            modelTypes = new int[k];
                            modelIds = new int[k];
                            for(int k1 = 0; k1 < k; k1++)
                            {
                                modelIds[k1] = stream.getUnsignedLEShort();
                                modelTypes[k1] = stream.getUnsignedByte();
                            }

                        } else
                        {
                            stream.currentOffset += k * 3;
                        }
                } else
                if(j == 2)
                    name = stream.getString();
                else
                if(j == 3)
                    description = stream.readBytes();
                else
                if(j == 5)
                {
                    int l = stream.getUnsignedByte();
                    if(l > 0)
                        if(modelIds == null || lowMem)
                        {
                            modelTypes = null;
                            modelIds = new int[l];
                            for(int l1 = 0; l1 < l; l1++)
                                modelIds[l1] = stream.getUnsignedLEShort();

                        } else
                        {
                            stream.currentOffset += l * 2;
                        }
                } else
                if(j == 14)
                    sizeX = stream.getUnsignedByte();
                else
                if(j == 15)
                    sizeY = stream.getUnsignedByte();
                else
                if(j == 17)
                    unwalkable = false;
                else
                if(j == 18)
                    walkable = false;
                else
                if(j == 19)
                {
                    i = stream.getUnsignedByte();
                    if(i == 1)
                        hasActions = true;
                } else
                if(j == 21)
                    adjustToTerrain = true;
                else
                if(j == 22)
                    delayShading = true;
                else
                if(j == 23)
                    aBoolean764 = true;
                else
                if(j == 24)
                {
                    anInt781 = stream.getUnsignedLEShort();
                    if(anInt781 == 65535)
                        anInt781 = -1;
                } else
                if(j == 28)
                    anInt775 = stream.getUnsignedByte();
                else
                if(j == 29)
                    aByte737 = stream.get();
                else
                if(j == 39)
                    aByte742 = stream.get();
                else
                if(j >= 30 && j < 39)
                {
                    if(actions == null)
                        actions = new String[5];
                    actions[j - 30] = stream.getString();
                    if(actions[j - 30].equalsIgnoreCase("hidden"))
                        actions[j - 30] = null;
                } else
                if(j == 40)
                {
                    int i1 = stream.getUnsignedByte();
                    modifiedModelColors = new int[i1];
                    originalModelColors = new int[i1];
                    for(int i2 = 0; i2 < i1; i2++)
                    {
                        modifiedModelColors[i2] = stream.getUnsignedLEShort();
                        originalModelColors[i2] = stream.getUnsignedLEShort();
                    }

                } else
                if(j == 60)
                    icon = stream.getUnsignedLEShort();
                else
                if(j == 62)
                    aBoolean751 = true;
                else
                if(j == 64)
                    aBoolean779 = false;
                else
                if(j == 65)
                    anInt748 = stream.getUnsignedLEShort();
                else
                if(j == 66)
                    anInt772 = stream.getUnsignedLEShort();
                else
                if(j == 67)
                    anInt740 = stream.getUnsignedLEShort();
                else
                if(j == 68)
                    mapScene = stream.getUnsignedLEShort();
                else
                if(j == 69)
                    face = stream.getUnsignedByte();
                else
                if(j == 70)
                    anInt738 = stream.getShort();
                else
                if(j == 71)
                    anInt745 = stream.getShort();
                else
                if(j == 72)
                    anInt783 = stream.getShort();
                else
                if(j == 73)
                    aBoolean736 = true;
                else
                if(j == 74)
                {
                    aBoolean766 = true;
                } else
                {
                    if(j != 75)
                        continue;
                    anInt760 = stream.getUnsignedByte();
                }
                continue label0;
            } while(j != 77);
            varBitId = stream.getUnsignedLEShort();
            if(varBitId == 65535)
                varBitId = -1;
            configIds = stream.getUnsignedLEShort();
            if(configIds == 65535)
                configIds = -1;
            int j1 = stream.getUnsignedByte();
            childrenIds = new int[j1 + 1];
            for(int j2 = 0; j2 <= j1; j2++)
            {
                childrenIds[j2] = stream.getUnsignedLEShort();
                if(childrenIds[j2] == 65535)
                    childrenIds[j2] = -1;
            }

        } while(true);
        if(i == -1)
        {
            hasActions = modelIds != null && (modelTypes == null || modelTypes[0] == 10);
            if(actions != null)
                hasActions = true;
        }
        if(aBoolean766)
        {
            unwalkable = false;
            walkable = false;
        }
        if(anInt760 == -1)
            anInt760 = unwalkable ? 1 : 0;
    }

    private GameObjectDefinition()
    {
        type = -1;
    }

    public boolean aBoolean736;
    private byte aByte737;
    private int anInt738;
    public String name;
    private int anInt740;
    private static final Model[] aModelArray741s = new Model[4];
    private byte aByte742;
    public int sizeX;
    private int anInt745;
    public int icon;
    private int[] originalModelColors;
    private int anInt748;
    public int configIds;
    private boolean aBoolean751;
    public static boolean lowMem;
    private static Stream stream;
    public int type;
    private static int[] streamIndices;
    public boolean walkable;
    public int mapScene;
    public int childrenIds[];
    private int anInt760;
    public int sizeY;
    public boolean adjustToTerrain;
    public boolean aBoolean764;
    public static client clientInstance;
    private boolean aBoolean766;
    public boolean unwalkable;
    public int face;
    private boolean delayShading;
    private static int cacheIndex;
    private int anInt772;
    private int[] modelIds;
    public int varBitId;
    public int anInt775;
    private int[] modelTypes;
    public byte description[];
    public boolean hasActions;
    public boolean aBoolean779;
    public static MRUNodes mruNodes2 = new MRUNodes(30);
    public int anInt781;
    private static GameObjectDefinition[] cache;
    private int anInt783;
    private int[] modifiedModelColors;
    public static MRUNodes modelCache = new MRUNodes(500);
    public String actions[];

}
