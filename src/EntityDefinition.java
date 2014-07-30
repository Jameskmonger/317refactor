// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class EntityDefinition
{

    public static EntityDefinition forID(int i)
    {
        for(int j = 0; j < 20; j++)
            if(cache[j].id == (long)i)
                return cache[j];

        anInt56 = (anInt56 + 1) % 20;
        EntityDefinition entityDef = cache[anInt56] = new EntityDefinition();
        stream.currentOffset = streamIndices[i];
        entityDef.id = i;
        entityDef.readValues(stream);
        return entityDef;
    }

    public Model getHeadModel()
    {
        if(childrenIDs != null)
        {
            EntityDefinition entityDef = getChildDefinition();
            if(entityDef == null)
                return null;
            else
                return entityDef.getHeadModel();
        }
        if(anIntArray73 == null)
            return null;
        boolean flag1 = false;
        for(int i = 0; i < anIntArray73.length; i++)
            if(!Model.isCached(anIntArray73[i]))
                flag1 = true;

        if(flag1)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray73.length];
        for(int j = 0; j < anIntArray73.length; j++)
            aclass30_sub2_sub4_sub6s[j] = Model.getModel(anIntArray73[j]);

        Model model;
        if(aclass30_sub2_sub4_sub6s.length == 1)
            model = aclass30_sub2_sub4_sub6s[0];
        else
            model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
        if(modifiedModelColours != null)
        {
            for(int k = 0; k < modifiedModelColours.length; k++)
                model.recolour(modifiedModelColours[k], originalModelColours[k]);

        }
        return model;
    }

    public EntityDefinition getChildDefinition()
    {
        int j = -1;
        if(anInt57 != -1)
        {
            VarBit varBit = VarBit.cache[anInt57];
            int k = varBit.configId;
            int l = varBit.leastSignificantBit;
            int i1 = varBit.mostSignificantBit;
            int j1 = client.anIntArray1232[i1 - l];
            j = clientInstance.variousSettings[k] >> l & j1;
        } else
        if(anInt59 != -1)
            j = clientInstance.variousSettings[anInt59];
        if(j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
            return null;
        else
            return forID(childrenIDs[j]);
    }

    public static void unpackConfig(StreamLoader streamLoader)
    {
        stream = new Stream(streamLoader.getDataForName("npc.dat"));
        Stream stream2 = new Stream(streamLoader.getDataForName("npc.idx"));
        int totalNPCs = stream2.getUnsignedLEShort();
        streamIndices = new int[totalNPCs];
        int i = 2;
        for(int j = 0; j < totalNPCs; j++)
        {
            streamIndices[j] = i;
            i += stream2.getUnsignedLEShort();
        }

        cache = new EntityDefinition[20];
        for(int k = 0; k < 20; k++)
            cache[k] = new EntityDefinition();

    }

    public static void nullLoader()
    {
        modelCache = null;
        streamIndices = null;
        cache = null;
        stream = null;
    }

    public Model getChildModel(int frameId2, int frameId1, int framesFrom2[])
    {
        if(childrenIDs != null)
        {
            EntityDefinition childDefinition = getChildDefinition();
            if(childDefinition == null)
                return null;
            else
                return childDefinition.getChildModel(frameId2, frameId1, framesFrom2);
        }
        Model model = (Model) modelCache.get(id);
        if(model == null)
        {
            boolean notCached = false;
            for(int m = 0; m < modelIds.length; m++)
                if(!Model.isCached(modelIds[m]))
                    notCached = true;

            if(notCached)
                return null;
            Model childModels[] = new Model[modelIds.length];
            for(int m = 0; m < modelIds.length; m++)
                childModels[m] = Model.getModel(modelIds[m]);

            if(childModels.length == 1)
                model = childModels[0];
            else
                model = new Model(childModels.length, childModels);
            if(modifiedModelColours != null)
            {
                for(int c = 0; c < modifiedModelColours.length; c++)
                    model.recolour(modifiedModelColours[c], originalModelColours[c]);

            }
            model.createBones();
            model.applyLighting(64 + brightness, 850 + contrast, -30, -50, -30, true);
            modelCache.put(model, id);
        }
        Model childModel = Model.aModel_1621;
        childModel.replaceWithModel(model, Animation.isNullFrame(frameId1) & Animation.isNullFrame(frameId2));
        if(frameId1 != -1 && frameId2 != -1)
            childModel.mixAnimationFrames(framesFrom2, frameId2, frameId1);
        else
        if(frameId1 != -1)
            childModel.applyTransformation(frameId1);
        if(resizeXY != 128 || resizeZ != 128)
            childModel.scaleT(resizeXY, resizeXY, resizeZ);
        childModel.calculateDiagonals();
        childModel.triangleSkin = null;
        childModel.vertexSkin = null;
        if(boundaryDimension == 1)
            childModel.singleTile = true;
        return childModel;
    }

    private void readValues(Stream stream)
    {
        do
        {
            int i = stream.getUnsignedByte();
            if(i == 0)
                return;
            if(i == 1)
            {
                int j = stream.getUnsignedByte();
                modelIds = new int[j];
                for(int j1 = 0; j1 < j; j1++)
                    modelIds[j1] = stream.getUnsignedLEShort();

            } else
            if(i == 2)
                name = stream.getString();
            else
            if(i == 3)
                description = stream.readBytes();
            else
            if(i == 12)
                boundaryDimension = stream.get();
            else
            if(i == 13)
                standAnimationId = stream.getUnsignedLEShort();
            else
            if(i == 14)
                walkAnimationId = stream.getUnsignedLEShort();
            else
            if(i == 17)
            {
                walkAnimationId = stream.getUnsignedLEShort();
                turnAboutAnimationId = stream.getUnsignedLEShort();
                turnRightAnimationId = stream.getUnsignedLEShort();
                turnLeftAnimationId = stream.getUnsignedLEShort();
            } else
            if(i >= 30 && i < 40)
            {
                if(actions == null)
                    actions = new String[5];
                actions[i - 30] = stream.getString();
                if(actions[i - 30].equalsIgnoreCase("hidden"))
                    actions[i - 30] = null;
            } else
            if(i == 40)
            {
                int k = stream.getUnsignedByte();
                modifiedModelColours = new int[k];
                originalModelColours = new int[k];
                for(int k1 = 0; k1 < k; k1++)
                {
                    modifiedModelColours[k1] = stream.getUnsignedLEShort();
                    originalModelColours[k1] = stream.getUnsignedLEShort();
                }

            } else
            if(i == 60)
            {
                int l = stream.getUnsignedByte();
                anIntArray73 = new int[l];
                for(int l1 = 0; l1 < l; l1++)
                    anIntArray73[l1] = stream.getUnsignedLEShort();

            } else
            if(i == 90)
                stream.getUnsignedLEShort();
            else
            if(i == 91)
                stream.getUnsignedLEShort();
            else
            if(i == 92)
                stream.getUnsignedLEShort();
            else
            if(i == 93)
                aBoolean87 = false;
            else
            if(i == 95)
                combatLevel = stream.getUnsignedLEShort();
            else
            if(i == 97)
                resizeXY = stream.getUnsignedLEShort();
            else
            if(i == 98)
                resizeZ = stream.getUnsignedLEShort();
            else
            if(i == 99)
                aBoolean93 = true;
            else
            if(i == 100)
                brightness = stream.get();
            else
            if(i == 101)
                contrast = stream.get() * 5;
            else
            if(i == 102)
                anInt75 = stream.getUnsignedLEShort();
            else
            if(i == 103)
                degreesToTurn = stream.getUnsignedLEShort();
            else
            if(i == 106)
            {
                anInt57 = stream.getUnsignedLEShort();
                if(anInt57 == 65535)
                    anInt57 = -1;
                anInt59 = stream.getUnsignedLEShort();
                if(anInt59 == 65535)
                    anInt59 = -1;
                int i1 = stream.getUnsignedByte();
                childrenIDs = new int[i1 + 1];
                for(int i2 = 0; i2 <= i1; i2++)
                {
                    childrenIDs[i2] = stream.getUnsignedLEShort();
                    if(childrenIDs[i2] == 65535)
                        childrenIDs[i2] = -1;
                }

            } else
            if(i == 107)
                clickable = false;
        } while(true);
    }

    private EntityDefinition()
    {
        turnLeftAnimationId = -1;
        anInt57 = -1;
        turnAboutAnimationId = -1;
        anInt59 = -1;
        combatLevel = -1;
        anInt64 = 1834;
        walkAnimationId = -1;
        boundaryDimension = 1;
        anInt75 = -1;
        standAnimationId = -1;
        id = -1L;
        degreesToTurn = 32;
        turnRightAnimationId = -1;
        clickable = true;
        resizeZ = 128;
        aBoolean87 = true;
        resizeXY = 128;
        aBoolean93 = false;
    }

    public int turnLeftAnimationId;
    private static int anInt56;
    private int anInt57;
    public int turnAboutAnimationId;
    private int anInt59;
    private static Stream stream;
    public int combatLevel;
    private final int anInt64;
    public String name;
    public String actions[];
    public int walkAnimationId;
    public byte boundaryDimension;
    private int[] originalModelColours;
    private static int[] streamIndices;
    private int[] anIntArray73;
    public int anInt75;
    private int[] modifiedModelColours;
    public int standAnimationId;
    public long id;
    public int degreesToTurn;
    private static EntityDefinition[] cache;
    public static client clientInstance;
    public int turnRightAnimationId;
    public boolean clickable;
    private int brightness;
    private int resizeZ;
    public boolean aBoolean87;
    public int childrenIDs[];
    public byte description[];
    private int resizeXY;
    private int contrast;
    public boolean aBoolean93;
    private int[] modelIds;
    public static MRUNodes modelCache = new MRUNodes(30);

}
