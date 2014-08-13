package com.jagex.runescape.audio;

import com.jagex.runescape.Stream;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Sounds {

    private Sounds()
    {
        samples = new SoundInstrument[10];
    }

    public static void unpack(Stream stream)
    {
        aByteArray327 = new byte[0x6baa8];
        aStream_328 = new Stream(aByteArray327);
        SoundInstrument.initialise();
        do
        {
            int j = stream.getUnsignedLEShort();
            if(j == 65535)
                return;
            aSoundsArray325s[j] = new Sounds();
            aSoundsArray325s[j].method242(stream);
            trackDelays[j] = aSoundsArray325s[j].method243();
        } while(true);
    }

    public static Stream data(int i, int j)
    {
        if(aSoundsArray325s[j] != null)
        {
            Sounds sounds = aSoundsArray325s[j];
            return sounds.method244(i);
        } else
        {
            return null;
        }
    }

    private void method242(Stream stream)
    {
        for(int i = 0; i < 10; i++)
        {
            int j = stream.getUnsignedByte();
            if(j != 0)
            {
                stream.currentOffset--;
                samples[i] = new SoundInstrument();
                samples[i].decode(stream);
            }
        }
        anInt330 = stream.getUnsignedLEShort();
        anInt331 = stream.getUnsignedLEShort();
    }

    private int method243()
    {
        int j = 0x98967f;
        for(int k = 0; k < 10; k++)
            if(samples[k] != null && samples[k].begin / 20 < j)
                j = samples[k].begin / 20;

        if(anInt330 < anInt331 && anInt330 / 20 < j)
            j = anInt330 / 20;
        if(j == 0x98967f || j == 0)
            return 0;
        for(int l = 0; l < 10; l++)
            if(samples[l] != null)
                samples[l].begin -= j * 20;

        if(anInt330 < anInt331)
        {
            anInt330 -= j * 20;
            anInt331 -= j * 20;
        }
        return j;
    }

    private Stream method244(int i)
    {
        int k = method245(i);
        aStream_328.currentOffset = 0;
        aStream_328.putInt(0x52494646);
        aStream_328.method403(36 + k);
        aStream_328.putInt(0x57415645);
        aStream_328.putInt(0x666d7420);
        aStream_328.method403(16);
        aStream_328.method400(1);
        aStream_328.method400(1);
        aStream_328.method403(22050);
        aStream_328.method403(22050);
        aStream_328.method400(1);
        aStream_328.method400(8);
        aStream_328.putInt(0x64617461);
        aStream_328.method403(k);
        aStream_328.currentOffset += k;
        return aStream_328;
    }

    private int method245(int i)
    {
        int j = 0;
        for(int k = 0; k < 10; k++)
            if(samples[k] != null && samples[k].duration + samples[k].begin > j)
                j = samples[k].duration + samples[k].begin;

        if(j == 0)
            return 0;
        int l = (22050 * j) / 1000;
        int i1 = (22050 * anInt330) / 1000;
        int j1 = (22050 * anInt331) / 1000;
        if(i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1)
            i = 0;
        int k1 = l + (j1 - i1) * (i - 1);
        for(int l1 = 44; l1 < k1 + 44; l1++)
            aByteArray327[l1] = -128;

        for(int i2 = 0; i2 < 10; i2++)
            if(samples[i2] != null)
            {
                int j2 = (samples[i2].duration * 22050) / 1000;
                int i3 = (samples[i2].begin * 22050) / 1000;
                int ai[] = samples[i2].synthesise(j2, samples[i2].duration);
                for(int l3 = 0; l3 < j2; l3++)
                    aByteArray327[l3 + i3 + 44] += (byte)(ai[l3] >> 8);

            }

        if(i > 1)
        {
            i1 += 44;
            j1 += 44;
            l += 44;
            int k2 = (k1 += 44) - l;
            for(int j3 = l - 1; j3 >= j1; j3--)
                aByteArray327[j3 + k2] = aByteArray327[j3];

            for(int k3 = 1; k3 < i; k3++)
            {
                int l2 = (j1 - i1) * k3;
                System.arraycopy(aByteArray327, i1, aByteArray327, i1 + l2, j1 - i1);

            }

            k1 -= 44;
        }
        return k1;
    }

    private static final Sounds[] aSoundsArray325s = new Sounds[5000];
    public static final int[] trackDelays = new int[5000];
    private static byte[] aByteArray327;
    private static Stream aStream_328;
    private final SoundInstrument[] samples;
    private int anInt330;
    private int anInt331;

}
