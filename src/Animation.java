// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Animation {

    public static void unpackConfig(StreamLoader streamLoader)
    {
        Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
        int length = stream.getUnsignedLEShort();
        if(anims == null)
            anims = new Animation[length];
        for(int j = 0; j < length; j++)
        {
            if(anims[j] == null)
                anims[j] = new Animation();
            anims[j].readValues(stream);
        }
    }

    public int getFrameLength(int i)
    {
        int j = anIntArray355[i];
        if(j == 0)
        {
            Class36 class36 = Class36.forFrameId(frame2Ids[i]);
            if(class36 != null)
                j = anIntArray355[i] = class36.anInt636;
        }
        if(j == 0)
            j = 1;
        return j;
    }

    private void readValues(Stream stream)
    {
        do
        {
            int i = stream.getUnsignedByte();
            if(i == 0)
                break;
            if(i == 1)
            {
                frameCount = stream.getUnsignedByte();
                frame2Ids = new int[frameCount];
                anIntArray354 = new int[frameCount];
                anIntArray355 = new int[frameCount];
                for(int j = 0; j < frameCount; j++)
                {
                    frame2Ids[j] = stream.getUnsignedLEShort();
                    anIntArray354[j] = stream.getUnsignedLEShort();
                    if(anIntArray354[j] == 65535)
                        anIntArray354[j] = -1;
                    anIntArray355[j] = stream.getUnsignedLEShort();
                }

            } else
            if(i == 2)
                anInt356 = stream.getUnsignedLEShort();
            else
            if(i == 3)
            {
                int k = stream.getUnsignedByte();
                anIntArray357 = new int[k + 1];
                for(int l = 0; l < k; l++)
                    anIntArray357[l] = stream.getUnsignedByte();

                anIntArray357[k] = 0x98967f;
            } else
            if(i == 4)
                aBoolean358 = true;
            else
            if(i == 5)
                anInt359 = stream.getUnsignedByte();
            else
            if(i == 6)
                anInt360 = stream.getUnsignedLEShort();
            else
            if(i == 7)
                anInt361 = stream.getUnsignedLEShort();
            else
            if(i == 8)
                anInt362 = stream.getUnsignedByte();
            else
            if(i == 9)
                anInt363 = stream.getUnsignedByte();
            else
            if(i == 10)
                anInt364 = stream.getUnsignedByte();
            else
            if(i == 11)
                anInt365 = stream.getUnsignedByte();
            else
            if(i == 12)
                stream.readDWord();
            else
                System.out.println("Error unrecognised seq config code: " + i);
        } while(true);
        if(frameCount == 0)
        {
            frameCount = 1;
            frame2Ids = new int[1];
            frame2Ids[0] = -1;
            anIntArray354 = new int[1];
            anIntArray354[0] = -1;
            anIntArray355 = new int[1];
            anIntArray355[0] = -1;
        }
        if(anInt363 == -1)
            if(anIntArray357 != null)
                anInt363 = 2;
            else
                anInt363 = 0;
        if(anInt364 == -1)
        {
            if(anIntArray357 != null)
            {
                anInt364 = 2;
                return;
            }
            anInt364 = 0;
        }
    }

    private Animation()
    {
        anInt356 = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1;
        anInt361 = -1;
        anInt362 = 99;
        anInt363 = -1;
        anInt364 = -1;
        anInt365 = 2;
    }

    public static Animation anims[];
    public int frameCount;
    public int frame2Ids[];
    public int anIntArray354[];
    private int[] anIntArray355;
    public int anInt356;
    public int anIntArray357[];
    public boolean aBoolean358;
    public int anInt359;
    public int anInt360;
    public int anInt361;
    public int anInt362;
    public int anInt363;
    public int anInt364;
    public int anInt365;
    public static int anInt367;
}
