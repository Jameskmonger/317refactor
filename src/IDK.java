// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class IDK {

    public static void unpackConfig(StreamLoader streamLoader)
    {
        Stream stream = new Stream(streamLoader.getDataForName("idk.dat"));
        length = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new IDK[length];
        for(int j = 0; j < length; j++)
        {
            if(cache[j] == null)
                cache[j] = new IDK();
            cache[j].readValues(stream);
        }
    }

    private void readValues(Stream stream)
    {
        do
        {
            int i = stream.getUnsignedByte();
            if(i == 0)
                return;
            if(i == 1)
                anInt657 = stream.getUnsignedByte();
            else
            if(i == 2)
            {
                int j = stream.getUnsignedByte();
                anIntArray658 = new int[j];
                for(int k = 0; k < j; k++)
                    anIntArray658[k] = stream.getUnsignedLEShort();

            } else
            if(i == 3)
                aBoolean662 = true;
            else
            if(i >= 40 && i < 50)
                anIntArray659[i - 40] = stream.getUnsignedLEShort();
            else
            if(i >= 50 && i < 60)
                anIntArray660[i - 50] = stream.getUnsignedLEShort();
            else
            if(i >= 60 && i < 70)
                anIntArray661[i - 60] = stream.getUnsignedLEShort();
            else
                System.out.println("Error unrecognised config code: " + i);
        } while(true);
    }

    public boolean method537()
    {
        if(anIntArray658 == null)
            return true;
        boolean flag = true;
        for(int j = 0; j < anIntArray658.length; j++)
            if(!Model.isCached(anIntArray658[j]))
                flag = false;

        return flag;
    }

    public Model method538()
    {
        if(anIntArray658 == null)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[anIntArray658.length];
        for(int i = 0; i < anIntArray658.length; i++)
            aclass30_sub2_sub4_sub6s[i] = Model.getModel(anIntArray658[i]);

        Model model;
        if(aclass30_sub2_sub4_sub6s.length == 1)
            model = aclass30_sub2_sub4_sub6s[0];
        else
            model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
        for(int j = 0; j < 6; j++)
        {
            if(anIntArray659[j] == 0)
                break;
            model.recolour(anIntArray659[j], anIntArray660[j]);
        }

        return model;
    }

    public boolean method539()
    {
        boolean flag1 = true;
        for(int i = 0; i < 5; i++)
            if(anIntArray661[i] != -1 && !Model.isCached(anIntArray661[i]))
                flag1 = false;

        return flag1;
    }

    public Model method540()
    {
        Model aclass30_sub2_sub4_sub6s[] = new Model[5];
        int j = 0;
        for(int k = 0; k < 5; k++)
            if(anIntArray661[k] != -1)
                aclass30_sub2_sub4_sub6s[j++] = Model.getModel(anIntArray661[k]);

        Model model = new Model(j, aclass30_sub2_sub4_sub6s);
        for(int l = 0; l < 6; l++)
        {
            if(anIntArray659[l] == 0)
                break;
            model.recolour(anIntArray659[l], anIntArray660[l]);
        }

        return model;
    }

    private IDK()
    {
        anInt657 = -1;
        anIntArray659 = new int[6];
        anIntArray660 = new int[6];
        aBoolean662 = false;
    }

    public static int length;
    public static IDK cache[];
    public int anInt657;
    private int[] anIntArray658;
    private final int[] anIntArray659;
    private final int[] anIntArray660;
    private final int[] anIntArray661 = {
        -1, -1, -1, -1, -1
    };
    public boolean aBoolean662;
}
