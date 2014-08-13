package com.jagex.runescape;

public final class IdentityKit {

    public static void load(Archive streamLoader)
    {
        Stream stream = new Stream(streamLoader.getFile("idk.dat"));
        count = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new IdentityKit[count];
        for(int kit = 0; kit < count; kit++)
        {
            if(cache[kit] == null)
                cache[kit] = new IdentityKit();
            cache[kit].loadDefinition(stream);
        }
    }

    private void loadDefinition(Stream stream)
    {
        do
        {
            int attribute = stream.getUnsignedByte();
            if(attribute == 0)
                return;
            if(attribute == 1)
                partId = stream.getUnsignedByte();
            else
            if(attribute == 2)
            {
                int modelCount = stream.getUnsignedByte();
                modelIds = new int[modelCount];
                for(int m = 0; m < modelCount; m++)
                {
                    modelIds[m] = stream.getUnsignedLEShort();
                }

            } else
            if(attribute == 3)
                widgetDisplayed = true;
            else
            if(attribute >= 40 && attribute < 50)
                originalModelColours[attribute - 40] = stream.getUnsignedLEShort();
            else
            if(attribute >= 50 && attribute < 60)
                modifiedModelColours[attribute - 50] = stream.getUnsignedLEShort();
            else
            if(attribute >= 60 && attribute < 70)
                headModelIds[attribute - 60] = stream.getUnsignedLEShort();
            else
                System.out.println("Error unrecognised config code: " + attribute);
        } while(true);
    }

    public boolean bodyModelCached()
    {
        if(modelIds == null)
            return true;
        boolean cached = true;
        for(int m = 0; m < modelIds.length; m++)
            if(!Model.isCached(modelIds[m]))
                cached = false;

        return cached;
    }

    public Model getBodyModel()
    {
        if(modelIds == null)
            return null;
        Model models[] = new Model[modelIds.length];
        for(int m = 0; m < modelIds.length; m++)
            models[m] = Model.getModel(modelIds[m]);

        Model model;
        if(models.length == 1)
            model = models[0];
        else
            model = new Model(models.length, models);
        for(int colour = 0; colour < 6; colour++)
        {
            if(originalModelColours[colour] == 0)
                break;
            model.recolour(originalModelColours[colour], modifiedModelColours[colour]);
        }

        return model;
    }

    public boolean headModelCached()
    {
        boolean cached = true;
        for(int m = 0; m < 5; m++)
            if(headModelIds[m] != -1 && !Model.isCached(headModelIds[m]))
                cached = false;

        return cached;
    }

    public Model getHeadModel()
    {
        Model models[] = new Model[5];
        int modelCount = 0;
        for(int m = 0; m < 5; m++)
            if(headModelIds[m] != -1)
                models[modelCount++] = Model.getModel(headModelIds[m]);

        Model model = new Model(modelCount, models);
        for(int colour = 0; colour < 6; colour++)
        {
            if(originalModelColours[colour] == 0)
                break;
            model.recolour(originalModelColours[colour], modifiedModelColours[colour]);
        }

        return model;
    }

    private IdentityKit()
    {
        partId = -1;
        originalModelColours = new int[6];
        modifiedModelColours = new int[6];
        widgetDisplayed = false;
    }

    public static int count;
    public static IdentityKit cache[];
    public int partId;
    private int[] modelIds;
    private final int[] originalModelColours;
    private final int[] modifiedModelColours;
    private final int[] headModelIds = {
        -1, -1, -1, -1, -1
    };
    public boolean widgetDisplayed;
}
