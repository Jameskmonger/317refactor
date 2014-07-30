public final class SpotAnim {

    public static void unpackConfig(StreamLoader streamLoader)
    {
        Stream stream = new Stream(streamLoader.getDataForName("spotanim.dat"));
        int length = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new SpotAnim[length];
        for(int j = 0; j < length; j++)
        {
            if(cache[j] == null)
                cache[j] = new SpotAnim();
            cache[j].id = j;
            cache[j].readValues(stream);
        }

    }

    private void readValues(Stream stream)
    {
        do
        {
            int opcode = stream.getUnsignedByte();
            if(opcode == 0)
                return;
            if(opcode == 1)
                modelId = stream.getUnsignedLEShort();
            else
            if(opcode == 2)
            {
                animationId = stream.getUnsignedLEShort();
                if(AnimationSequence.anims != null)
                    sequences = AnimationSequence.anims[animationId];
            } else
            if(opcode == 4)
                resizeXY = stream.getUnsignedLEShort();
            else
            if(opcode == 5)
                resizeZ = stream.getUnsignedLEShort();
            else
            if(opcode == 6)
                rotation = stream.getUnsignedLEShort();
            else
            if(opcode == 7)
                modelLightFalloff = stream.getUnsignedByte();
            else
            if(opcode == 8)
                modelLightAmbient = stream.getUnsignedByte();
            else
            if(opcode >= 40 && opcode < 50)
                originalModelColours[opcode - 40] = stream.getUnsignedLEShort();
            else
            if(opcode >= 50 && opcode < 60)
                modifiedModelColours[opcode - 50] = stream.getUnsignedLEShort();
            else
                System.out.println("Error unrecognised spotanim config code: " + opcode);
        } while(true);
    }

    public Model getModel()
    {
        Model model = (Model) modelCache.get(id);
        if(model != null)
            return model;
        model = Model.getModel(modelId);
        if(model == null)
            return null;
        for(int c = 0; c < 6; c++)
            if(originalModelColours[0] != 0)
                model.recolour(originalModelColours[c], modifiedModelColours[c]);

        modelCache.put(model, id);
        return model;
    }

    private SpotAnim()
    {
        anInt400 = 9;
        animationId = -1;
        originalModelColours = new int[6];
        modifiedModelColours = new int[6];
        resizeXY = 128;
        resizeZ = 128;
    }

    private final int anInt400;
    public static SpotAnim cache[];
    private int id;
    private int modelId;
    private int animationId;
    public AnimationSequence sequences;
    private final int[] originalModelColours;
    private final int[] modifiedModelColours;
    public int resizeXY;
    public int resizeZ;
    public int rotation;
    public int modelLightFalloff;
    public int modelLightAmbient;
    public static MRUNodes modelCache = new MRUNodes(30);
}