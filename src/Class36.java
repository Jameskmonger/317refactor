// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Class36
{

    public static void method528(int i)
    {
        aClass36Array635 = new Class36[i + 1];
        aBooleanArray643 = new boolean[i + 1];
        for(int j = 0; j < i + 1; j++)
            aBooleanArray643[j] = true;

    }

    public static void method529(byte abyte0[])
    {
        Stream stream = new Stream(abyte0);
        stream.currentOffset = abyte0.length - 8;
        int i = stream.getUnsignedLEShort();
        int j = stream.getUnsignedLEShort();
        int k = stream.getUnsignedLEShort();
        int l = stream.getUnsignedLEShort();
        int i1 = 0;
        Stream stream_1 = new Stream(abyte0);
        stream_1.currentOffset = i1;
        i1 += i + 2;
        Stream stream_2 = new Stream(abyte0);
        stream_2.currentOffset = i1;
        i1 += j;
        Stream stream_3 = new Stream(abyte0);
        stream_3.currentOffset = i1;
        i1 += k;
        Stream stream_4 = new Stream(abyte0);
        stream_4.currentOffset = i1;
        i1 += l;
        Stream stream_5 = new Stream(abyte0);
        stream_5.currentOffset = i1;
        Skins class18 = new Skins(stream_5);
        int k1 = stream_1.getUnsignedLEShort();
        int ai[] = new int[500];
        int ai1[] = new int[500];
        int ai2[] = new int[500];
        int ai3[] = new int[500];
        for(int l1 = 0; l1 < k1; l1++)
        {
            int i2 = stream_1.getUnsignedLEShort();
            Class36 class36 = aClass36Array635[i2] = new Class36();
            class36.anInt636 = stream_4.getUnsignedByte();
            class36.animationSkins = class18;
            int j2 = stream_1.getUnsignedByte();
            int k2 = -1;
            int l2 = 0;
            for(int i3 = 0; i3 < j2; i3++)
            {
                int j3 = stream_2.getUnsignedByte();
                if(j3 > 0)
                {
                    if(class18.opcodes[i3] != 0)
                    {
                        for(int l3 = i3 - 1; l3 > k2; l3--)
                        {
                            if(class18.opcodes[l3] != 0)
                                continue;
                            ai[l2] = l3;
                            ai1[l2] = 0;
                            ai2[l2] = 0;
                            ai3[l2] = 0;
                            l2++;
                            break;
                        }

                    }
                    ai[l2] = i3;
                    char c = '\0';
                    if(class18.opcodes[i3] == 3)
                        c = '\200';
                    if((j3 & 1) != 0)
                        ai1[l2] = stream_3.getSmartA();
                    else
                        ai1[l2] = c;
                    if((j3 & 2) != 0)
                        ai2[l2] = stream_3.getSmartA();
                    else
                        ai2[l2] = c;
                    if((j3 & 4) != 0)
                        ai3[l2] = stream_3.getSmartA();
                    else
                        ai3[l2] = c;
                    k2 = i3;
                    l2++;
                    if(class18.opcodes[i3] == 5)
                        aBooleanArray643[i2] = false;
                }
            }

            class36.frameCount = l2;
            class36.opcodeTable = new int[l2];
            class36.transformationX = new int[l2];
            class36.transformationY = new int[l2];
            class36.transformationZ = new int[l2];
            for(int k3 = 0; k3 < l2; k3++)
            {
                class36.opcodeTable[k3] = ai[k3];
                class36.transformationX[k3] = ai1[k3];
                class36.transformationY[k3] = ai2[k3];
                class36.transformationZ[k3] = ai3[k3];
            }

        }

    }

    public static void nullLoader()
    {
        aClass36Array635 = null;
    }

    public static Class36 forFrameId(int j)
    {
        if(aClass36Array635 == null)
            return null;
        else
            return aClass36Array635[j];
    }

    public static boolean isNullFrame(int i)
    {
        return i == -1;
    }

    private Class36()
    {
    }

    private static Class36[] aClass36Array635;
    public int anInt636;
    public Skins animationSkins;
    public int frameCount;
    public int opcodeTable[];
    public int transformationX[];
    public int transformationY[];
    public int transformationZ[];
    private static boolean[] aBooleanArray643;

}
