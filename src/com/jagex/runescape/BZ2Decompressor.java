package com.jagex.runescape;
final class BZ2Decompressor
{

    public static int method225(byte abyte0[], int i, byte abyte1[], int j, int k)
    {
        synchronized(aClass32_305)
        {
            aClass32_305.aByteArray563 = abyte1;
            aClass32_305.anInt564 = k;
            aClass32_305.aByteArray568 = abyte0;
            aClass32_305.anInt569 = 0;
            aClass32_305.anInt565 = j;
            aClass32_305.anInt570 = i;
            aClass32_305.anInt577 = 0;
            aClass32_305.anInt576 = 0;
            aClass32_305.anInt566 = 0;
            aClass32_305.anInt567 = 0;
            aClass32_305.anInt571 = 0;
            aClass32_305.anInt572 = 0;
            aClass32_305.anInt579 = 0;
            decompress(aClass32_305);
            i -= aClass32_305.anInt570;
            return i;
        }
    }

    private static void method226(BZ2Context context)
    {
        byte byte4 = context.aByte573;
        int i = context.anInt574;
        int j = context.anInt584;
        int k = context.anInt582;
        int ai[] = BZ2Context.anIntArray587;
        int l = context.anInt581;
        byte abyte0[] = context.aByteArray568;
        int i1 = context.anInt569;
        int j1 = context.anInt570;
        int k1 = j1;
        int l1 = context.anInt601 + 1;
label0:
        do
        {
            if(i > 0)
            {
                do
                {
                    if(j1 == 0)
                        break label0;
                    if(i == 1)
                        break;
                    abyte0[i1] = byte4;
                    i--;
                    i1++;
                    j1--;
                } while(true);
                if(j1 == 0)
                {
                    i = 1;
                    break;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
            }
            boolean flag = true;
            while(flag) 
            {
                flag = false;
                if(j == l1)
                {
                    i = 0;
                    break label0;
                }
                byte4 = (byte)k;
                l = ai[l];
                byte byte0 = (byte)(l & 0xff);
                l >>= 8;
                j++;
                if(byte0 != k)
                {
                    k = byte0;
                    if(j1 == 0)
                    {
                        i = 1;
                    } else
                    {
                        abyte0[i1] = byte4;
                        i1++;
                        j1--;
                        flag = true;
                        continue;
                    }
                    break label0;
                }
                if(j != l1)
                    continue;
                if(j1 == 0)
                {
                    i = 1;
                    break label0;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
                flag = true;
            }
            i = 2;
            l = ai[l];
            byte byte1 = (byte)(l & 0xff);
            l >>= 8;
            if(++j != l1)
                if(byte1 != k)
                {
                    k = byte1;
                } else
                {
                    i = 3;
                    l = ai[l];
                    byte byte2 = (byte)(l & 0xff);
                    l >>= 8;
                    if(++j != l1)
                        if(byte2 != k)
                        {
                            k = byte2;
                        } else
                        {
                            l = ai[l];
                            byte byte3 = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                            i = (byte3 & 0xff) + 4;
                            l = ai[l];
                            k = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                        }
                }
        } while(true);
        int i2 = context.anInt571;
        context.anInt571 += k1 - j1;
        if(context.anInt571 < i2)
            context.anInt572++;
        context.aByte573 = byte4;
        context.anInt574 = i;
        context.anInt584 = j;
        context.anInt582 = k;
        BZ2Context.anIntArray587 = ai;
        context.anInt581 = l;
        context.aByteArray568 = abyte0;
        context.anInt569 = i1;
        context.anInt570 = j1;
    }

    private static void decompress(BZ2Context block)
    {
        int tMinLen = 0;
        int tLimit[] = null;
        int tBase[] = null;
        int tPerm[] = null;
        block.blockSize_100k = 1;
        if(BZ2Context.anIntArray587 == null)
            BZ2Context.anIntArray587 = new int[block.blockSize_100k * 0x186a0];
        boolean reading = true;
        while(reading) 
        {
            byte head = readUnsignedChar(block);
            if(head == 23)
                return;
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            block.anInt579++;
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readUnsignedChar(block);
            head = readBit(block);
            block.randomised = head != 0;
            if(block.randomised)
                System.out.println("PANIC! RANDOMISED BLOCK!");
            block.originalPointer = 0;
            head = readUnsignedChar(block);
            block.originalPointer = block.originalPointer << 8 | head & 0xff;
            head = readUnsignedChar(block);
            block.originalPointer = block.originalPointer << 8 | head & 0xff;
            head = readUnsignedChar(block);
            block.originalPointer = block.originalPointer << 8 | head & 0xff;
            for(int i = 0; i < 16; i++)
            {
                byte used = readBit(block);
                block.inUse16[i] = used == 1;
            }

            for(int i = 0; i < 256; i++)
                block.inUse[i] = false;

            for(int i = 0; i < 16; i++)
                if(block.inUse16[i])
                {
                    for(int j = 0; j < 16; j++)
                    {
                        byte v = readBit(block);
                        if(v == 1)
                            block.inUse[i * 16 + j] = true;
                    }

                }

            makeMaps(block);
            int alphaSize = block.nInUse + 2;
            int groups = readBits(3, block);
            int selectors = readBits(15, block);
            for(int i = 0; i < selectors; i++)
            {
                int selectorValue = 0;
                do
                {
                    byte v = readBit(block);
                    if(v == 0)
                        break;
                    selectorValue++;
                } while(true);
                block.selectorMtf[i] = (byte)selectorValue;
            }

            byte abyte0[] = new byte[6];
            for(byte byte16 = 0; byte16 < groups; byte16++)
                abyte0[byte16] = byte16;

            for(int j1 = 0; j1 < selectors; j1++)
            {
                byte byte17 = block.selectorMtf[j1];
                byte byte15 = abyte0[byte17];
                for(; byte17 > 0; byte17--)
                    abyte0[byte17] = abyte0[byte17 - 1];

                abyte0[0] = byte15;
                block.aByteArray594[j1] = byte15;
            }

            for(int k3 = 0; k3 < groups; k3++)
            {
                int l6 = readBits(5, block);
                for(int k1 = 0; k1 < alphaSize; k1++)
                {
                    do
                    {
                        byte byte4 = readBit(block);
                        if(byte4 == 0)
                            break;
                        byte4 = readBit(block);
                        if(byte4 == 0)
                            l6++;
                        else
                            l6--;
                    } while(true);
                    block.aByteArrayArray596[k3][k1] = (byte)l6;
                }

            }

            for(int l3 = 0; l3 < groups; l3++)
            {
                byte byte8 = 32;
                int i = 0;
                for(int l1 = 0; l1 < alphaSize; l1++)
                {
                    if(block.aByteArrayArray596[l3][l1] > i)
                        i = block.aByteArrayArray596[l3][l1];
                    if(block.aByteArrayArray596[l3][l1] < byte8)
                        byte8 = block.aByteArrayArray596[l3][l1];
                }

                method232(block.anIntArrayArray597[l3], block.anIntArrayArray598[l3], block.anIntArrayArray599[l3], block.aByteArrayArray596[l3], byte8, i, alphaSize);
                block.anIntArray600[l3] = byte8;
            }

            int l4 = block.nInUse + 1;
            int l5 = 0x186a0 * block.blockSize_100k;
            int i5 = -1;
            int j5 = 0;
            for(int i2 = 0; i2 <= 255; i2++)
                block.anIntArray583[i2] = 0;

            int j9 = 4095;
            for(int l8 = 15; l8 >= 0; l8--)
            {
                for(int i9 = 15; i9 >= 0; i9--)
                {
                    block.aByteArray592[j9] = (byte)(l8 * 16 + i9);
                    j9--;
                }

                block.anIntArray593[l8] = j9 + 1;
            }

            int i6 = 0;
            if(j5 == 0)
            {
                i5++;
                j5 = 50;
                byte byte12 = block.aByteArray594[i5];
                tMinLen = block.anIntArray600[byte12];
                tLimit = block.anIntArrayArray597[byte12];
                tPerm = block.anIntArrayArray599[byte12];
                tBase = block.anIntArrayArray598[byte12];
            }
            j5--;
            int i7 = tMinLen;
            int l7;
            byte byte9;
            for(l7 = readBits(i7, block); l7 > tLimit[i7]; l7 = l7 << 1 | byte9)
            {
                i7++;
                byte9 = readBit(block);
            }

            for(int k5 = tPerm[l7 - tBase[i7]]; k5 != l4;)
                if(k5 == 0 || k5 == 1)
                {
                    int j6 = -1;
                    int k6 = 1;
                    do
                    {
                        if(k5 == 0)
                            j6 += k6;
                        else
                        if(k5 == 1)
                            j6 += 2 * k6;
                        k6 *= 2;
                        if(j5 == 0)
                        {
                            i5++;
                            j5 = 50;
                            byte byte13 = block.aByteArray594[i5];
                            tMinLen = block.anIntArray600[byte13];
                            tLimit = block.anIntArrayArray597[byte13];
                            tPerm = block.anIntArrayArray599[byte13];
                            tBase = block.anIntArrayArray598[byte13];
                        }
                        j5--;
                        int j7 = tMinLen;
                        int i8;
                        byte byte10;
                        for(i8 = readBits(j7, block); i8 > tLimit[j7]; i8 = i8 << 1 | byte10)
                        {
                            j7++;
                            byte10 = readBit(block);
                        }

                        k5 = tPerm[i8 - tBase[j7]];
                    } while(k5 == 0 || k5 == 1);
                    j6++;
                    byte byte5 = block.aByteArray591[block.aByteArray592[block.anIntArray593[0]] & 0xff];
                    block.anIntArray583[byte5 & 0xff] += j6;
                    for(; j6 > 0; j6--)
                    {
                        BZ2Context.anIntArray587[i6] = byte5 & 0xff;
                        i6++;
                    }

                } else
                {
                    int j11 = k5 - 1;
                    byte byte6;
                    if(j11 < 16)
                    {
                        int j10 = block.anIntArray593[0];
                        byte6 = block.aByteArray592[j10 + j11];
                        for(; j11 > 3; j11 -= 4)
                        {
                            int k11 = j10 + j11;
                            block.aByteArray592[k11] = block.aByteArray592[k11 - 1];
                            block.aByteArray592[k11 - 1] = block.aByteArray592[k11 - 2];
                            block.aByteArray592[k11 - 2] = block.aByteArray592[k11 - 3];
                            block.aByteArray592[k11 - 3] = block.aByteArray592[k11 - 4];
                        }

                        for(; j11 > 0; j11--)
                            block.aByteArray592[j10 + j11] = block.aByteArray592[(j10 + j11) - 1];

                        block.aByteArray592[j10] = byte6;
                    } else
                    {
                        int l10 = j11 / 16;
                        int i11 = j11 % 16;
                        int k10 = block.anIntArray593[l10] + i11;
                        byte6 = block.aByteArray592[k10];
                        for(; k10 > block.anIntArray593[l10]; k10--)
                            block.aByteArray592[k10] = block.aByteArray592[k10 - 1];

                        block.anIntArray593[l10]++;
                        for(; l10 > 0; l10--)
                        {
                            block.anIntArray593[l10]--;
                            block.aByteArray592[block.anIntArray593[l10]] = block.aByteArray592[(block.anIntArray593[l10 - 1] + 16) - 1];
                        }

                        block.anIntArray593[0]--;
                        block.aByteArray592[block.anIntArray593[0]] = byte6;
                        if(block.anIntArray593[0] == 0)
                        {
                            int i10 = 4095;
                            for(int k9 = 15; k9 >= 0; k9--)
                            {
                                for(int l9 = 15; l9 >= 0; l9--)
                                {
                                    block.aByteArray592[i10] = block.aByteArray592[block.anIntArray593[k9] + l9];
                                    i10--;
                                }

                                block.anIntArray593[k9] = i10 + 1;
                            }

                        }
                    }
                    block.anIntArray583[block.aByteArray591[byte6 & 0xff] & 0xff]++;
                    BZ2Context.anIntArray587[i6] = block.aByteArray591[byte6 & 0xff] & 0xff;
                    i6++;
                    if(j5 == 0)
                    {
                        i5++;
                        j5 = 50;
                        byte byte14 = block.aByteArray594[i5];
                        tMinLen = block.anIntArray600[byte14];
                        tLimit = block.anIntArrayArray597[byte14];
                        tPerm = block.anIntArrayArray599[byte14];
                        tBase = block.anIntArrayArray598[byte14];
                    }
                    j5--;
                    int k7 = tMinLen;
                    int j8;
                    byte byte11;
                    for(j8 = readBits(k7, block); j8 > tLimit[k7]; j8 = j8 << 1 | byte11)
                    {
                        k7++;
                        byte11 = readBit(block);
                    }

                    k5 = tPerm[j8 - tBase[k7]];
                }

            block.anInt574 = 0;
            block.aByte573 = 0;
            block.anIntArray585[0] = 0;
            for(int j2 = 1; j2 <= 256; j2++)
                block.anIntArray585[j2] = block.anIntArray583[j2 - 1];

            for(int k2 = 1; k2 <= 256; k2++)
                block.anIntArray585[k2] += block.anIntArray585[k2 - 1];

            for(int l2 = 0; l2 < i6; l2++)
            {
                byte byte7 = (byte)(BZ2Context.anIntArray587[l2] & 0xff);
                BZ2Context.anIntArray587[block.anIntArray585[byte7 & 0xff]] |= l2 << 8;
                block.anIntArray585[byte7 & 0xff]++;
            }

            block.anInt581 = BZ2Context.anIntArray587[block.originalPointer] >> 8;
            block.anInt584 = 0;
            block.anInt581 = BZ2Context.anIntArray587[block.anInt581];
            block.anInt582 = (byte)(block.anInt581 & 0xff);
            block.anInt581 >>= 8;
            block.anInt584++;
            block.anInt601 = i6;
            method226(block);
            reading = block.anInt584 == block.anInt601 + 1 && block.anInt574 == 0;
        }
    }

    private static byte readUnsignedChar(BZ2Context class32)
    {
        return (byte)readBits(8, class32);
    }

    private static byte readBit(BZ2Context class32)
    {
        return (byte)readBits(1, class32);
    }

    private static int readBits(int i, BZ2Context class32)
    {
        int j;
        do
        {
            if(class32.anInt577 >= i)
            {
                int k = class32.anInt576 >> class32.anInt577 - i & (1 << i) - 1;
                class32.anInt577 -= i;
                j = k;
                break;
            }
            class32.anInt576 = class32.anInt576 << 8 | class32.aByteArray563[class32.anInt564] & 0xff;
            class32.anInt577 += 8;
            class32.anInt564++;
            class32.anInt565--;
            class32.anInt566++;
            if(class32.anInt566 == 0)
                class32.anInt567++;
        } while(true);
        return j;
    }

    private static void makeMaps(BZ2Context class32)
    {
        class32.nInUse = 0;
        for(int i = 0; i < 256; i++)
            if(class32.inUse[i])
            {
                class32.aByteArray591[class32.nInUse] = (byte)i;
                class32.nInUse++;
            }

    }

    private static void method232(int ai[], int ai1[], int ai2[], byte abyte0[], int i, int j, int k)
    {
        int l = 0;
        for(int i1 = i; i1 <= j; i1++)
        {
            for(int l2 = 0; l2 < k; l2++)
                if(abyte0[l2] == i1)
                {
                    ai2[l] = l2;
                    l++;
                }

        }

        for(int j1 = 0; j1 < 23; j1++)
            ai1[j1] = 0;

        for(int k1 = 0; k1 < k; k1++)
            ai1[abyte0[k1] + 1]++;

        for(int l1 = 1; l1 < 23; l1++)
            ai1[l1] += ai1[l1 - 1];

        for(int i2 = 0; i2 < 23; i2++)
            ai[i2] = 0;

        int i3 = 0;
        for(int j2 = i; j2 <= j; j2++)
        {
            i3 += ai1[j2 + 1] - ai1[j2];
            ai[j2] = i3 - 1;
            i3 <<= 1;
        }

        for(int k2 = i + 1; k2 <= j; k2++)
            ai1[k2] = (ai[k2 - 1] + 1 << 1) - ai1[k2];

    }

    private static final BZ2Context aClass32_305 = new BZ2Context();

}
