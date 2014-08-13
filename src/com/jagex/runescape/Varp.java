package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class  Varp {

    public static void unpackConfig(StreamLoader archive)
    {
        Stream stream = new Stream(archive.getDataForName("varp.dat"));
        anInt702 = 0;
        int cacheSize = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new Varp[cacheSize];
        if(anIntArray703 == null)
            anIntArray703 = new int[cacheSize];
        for(int j = 0; j < cacheSize; j++)
        {
            if(cache[j] == null)
                cache[j] = new Varp();
            cache[j].readValues(stream, j);
        }
        if(stream.currentOffset != stream.buffer.length)
            System.out.println("varptype load mismatch");
    }

    private void readValues(Stream stream, int i)
    {
        do
        {
            int opcode = stream.getUnsignedByte();
            if(opcode == 0)
                return;
            int dummy;
            if(opcode == 1)
                 stream.getUnsignedByte();
            else
            if(opcode == 2)
                stream.getUnsignedByte();
            else
            if(opcode == 3)
                anIntArray703[anInt702++] = i;
            else
            if(opcode == 4)
                dummy = 2;
            else
            if(opcode == 5)
                anInt709 = stream.getUnsignedLEShort();
            else
            if(opcode == 6)
                dummy = 2;
            else
            if(opcode == 7)
                stream.getInt();
            else
            if(opcode == 8)
                aBoolean713 = true;
             else
            if(opcode == 10)
                 stream.getString();
            else
            if(opcode == 11)
                aBoolean713 = true;
            else
            if(opcode == 12)
                stream.getInt();
            else
            if(opcode == 13)
                dummy = 2;
            else
                System.out.println("Error unrecognised config code: " + opcode);
        } while(true);
    }

    private Varp()
    {
        aBoolean713 = false;
    }

    public static Varp cache[];
    private static int anInt702;
    private static int[] anIntArray703;
    public int anInt709;
    public boolean aBoolean713;

}
