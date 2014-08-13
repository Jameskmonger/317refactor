package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

final class ObjectManager {

    public ObjectManager(byte abyte0[][][], int ai[][][])
    {
        setZ = 99;
        anInt146 = 104;
        anInt147 = 104;
        vertexHeights = ai;
        renderRuleFlags = abyte0;
        aByteArrayArrayArray142 = new byte[4][anInt146][anInt147];
        aByteArrayArrayArray130 = new byte[4][anInt146][anInt147];
        aByteArrayArrayArray136 = new byte[4][anInt146][anInt147];
        aByteArrayArrayArray148 = new byte[4][anInt146][anInt147];
        tileCullingBitsets = new int[4][anInt146 + 1][anInt147 + 1];
        tileShadowIntensity = new byte[4][anInt146 + 1][anInt147 + 1];
        anIntArrayArray139 = new int[anInt146 + 1][anInt147 + 1];
        anIntArray124 = new int[anInt147];
        anIntArray125 = new int[anInt147];
        anIntArray126 = new int[anInt147];
        anIntArray127 = new int[anInt147];
        anIntArray128 = new int[anInt147];
    }

    private static int method170(int i, int j)
    {
        int k = i + j * 57;
        k = k << 13 ^ k;
        int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }

    public final void addTiles(CollisionMap aclass11[], WorldController worldController)
    {
        for(int j = 0; j < 4; j++)
        {
            for(int k = 0; k < 104; k++)
            {
                for(int i1 = 0; i1 < 104; i1++)
                    if((renderRuleFlags[j][k][i1] & 1) == 1)
                    {
                        int k1 = j;
                        if((renderRuleFlags[1][k][i1] & 2) == 2)
                            k1--;
                        if(k1 >= 0)
                            aclass11[k1].markBlocked(k, i1);
                    }

            }

        }
        anInt123 += (int)(Math.random() * 5D) - 2;
        if(anInt123 < -8)
            anInt123 = -8;
        if(anInt123 > 8)
            anInt123 = 8;
        anInt133 += (int)(Math.random() * 5D) - 2;
        if(anInt133 < -16)
            anInt133 = -16;
        if(anInt133 > 16)
            anInt133 = 16;
        for(int l = 0; l < 4; l++)
        {
            byte abyte0[][] = tileShadowIntensity[l];
            byte byte0 = 96;
            char c = '\u0300';
            byte byte1 = -50;
            byte byte2 = -10;
            byte byte3 = -50;
            int j3 = (int)Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
            int l3 = c * j3 >> 8;
            for(int j4 = 1; j4 < anInt147 - 1; j4++)
            {
                for(int j5 = 1; j5 < anInt146 - 1; j5++)
                {
                    int k6 = vertexHeights[l][j5 + 1][j4] - vertexHeights[l][j5 - 1][j4];
                    int l7 = vertexHeights[l][j5][j4 + 1] - vertexHeights[l][j5][j4 - 1];
                    int j9 = (int)Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
                    int k12 = (k6 << 8) / j9;
                    int l13 = 0x10000 / j9;
                    int j15 = (l7 << 8) / j9;
                    int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
                    int j17 = (abyte0[j5 - 1][j4] >> 2) + (abyte0[j5 + 1][j4] >> 3) + (abyte0[j5][j4 - 1] >> 2) + (abyte0[j5][j4 + 1] >> 3) + (abyte0[j5][j4] >> 1);
                    anIntArrayArray139[j5][j4] = j16 - j17;
                }

            }

            for(int k5 = 0; k5 < anInt147; k5++)
            {
                anIntArray124[k5] = 0;
                anIntArray125[k5] = 0;
                anIntArray126[k5] = 0;
                anIntArray127[k5] = 0;
                anIntArray128[k5] = 0;
            }

            for(int l6 = -5; l6 < anInt146 + 5; l6++)
            {
                for(int i8 = 0; i8 < anInt147; i8++)
                {
                    int k9 = l6 + 5;
                    if(k9 >= 0 && k9 < anInt146)
                    {
                        int l12 = aByteArrayArrayArray142[l][k9][i8] & 0xff;
                        if(l12 > 0)
                        {
                            Flo flo = Flo.cache[l12 - 1];
                            anIntArray124[i8] += flo.hue2;
                            anIntArray125[i8] += flo.saturation;
                            anIntArray126[i8] += flo.lightness;
                            anIntArray127[i8] += flo.pCDivider;
                            anIntArray128[i8]++;
                        }
                    }
                    int i13 = l6 - 5;
                    if(i13 >= 0 && i13 < anInt146)
                    {
                        int i14 = aByteArrayArrayArray142[l][i13][i8] & 0xff;
                        if(i14 > 0)
                        {
                            Flo flo_1 = Flo.cache[i14 - 1];
                            anIntArray124[i8] -= flo_1.hue2;
                            anIntArray125[i8] -= flo_1.saturation;
                            anIntArray126[i8] -= flo_1.lightness;
                            anIntArray127[i8] -= flo_1.pCDivider;
                            anIntArray128[i8]--;
                        }
                    }
                }

                if(l6 >= 1 && l6 < anInt146 - 1)
                {
                    int l9 = 0;
                    int j13 = 0;
                    int j14 = 0;
                    int k15 = 0;
                    int k16 = 0;
                    for(int k17 = -5; k17 < anInt147 + 5; k17++)
                    {
                        int j18 = k17 + 5;
                        if(j18 >= 0 && j18 < anInt147)
                        {
                            l9 += anIntArray124[j18];
                            j13 += anIntArray125[j18];
                            j14 += anIntArray126[j18];
                            k15 += anIntArray127[j18];
                            k16 += anIntArray128[j18];
                        }
                        int k18 = k17 - 5;
                        if(k18 >= 0 && k18 < anInt147)
                        {
                            l9 -= anIntArray124[k18];
                            j13 -= anIntArray125[k18];
                            j14 -= anIntArray126[k18];
                            k15 -= anIntArray127[k18];
                            k16 -= anIntArray128[k18];
                        }
                        if(k17 >= 1 && k17 < anInt147 - 1 && (!lowMem || (renderRuleFlags[0][l6][k17] & 2) != 0 || (renderRuleFlags[l][l6][k17] & 0x10) == 0 && getVisibilityPlane(k17, l, l6) == plane))
                        {
                            if(l < setZ)
                                setZ = l;
                            int l18 = aByteArrayArrayArray142[l][l6][k17] & 0xff;
                            int i19 = aByteArrayArrayArray130[l][l6][k17] & 0xff;
                            if(l18 > 0 || i19 > 0)
                            {
                                int j19 = vertexHeights[l][l6][k17];
                                int k19 = vertexHeights[l][l6 + 1][k17];
                                int l19 = vertexHeights[l][l6 + 1][k17 + 1];
                                int i20 = vertexHeights[l][l6][k17 + 1];
                                int j20 = anIntArrayArray139[l6][k17];
                                int k20 = anIntArrayArray139[l6 + 1][k17];
                                int l20 = anIntArrayArray139[l6 + 1][k17 + 1];
                                int i21 = anIntArrayArray139[l6][k17 + 1];
                                int j21 = -1;
                                int k21 = -1;
                                if(l18 > 0)
                                {
                                    int l21 = (l9 * 256) / k15;
                                    int j22 = j13 / k16;
                                    int l22 = j14 / k16;
                                    j21 = method177(l21, j22, l22);
                                    l21 = l21 + anInt123 & 0xff;
                                    l22 += anInt133;
                                    if(l22 < 0)
                                        l22 = 0;
                                    else
                                    if(l22 > 255)
                                        l22 = 255;
                                    k21 = method177(l21, j22, l22);
                                }
                                if(l > 0)
                                {
                                    boolean flag = true;
                                    if(l18 == 0 && aByteArrayArrayArray136[l][l6][k17] != 0)
                                        flag = false;
                                    if(i19 > 0 && !Flo.cache[i19 - 1].occlude)
                                        flag = false;
                                    if(flag && j19 == k19 && j19 == l19 && j19 == i20)
                                        tileCullingBitsets[l][l6][k17] |= 0x924;
                                }
                                int i22 = 0;
                                if(j21 != -1)
                                    i22 = Texture.HSL_TO_RGB[method187(k21, 96)];
                                if(i19 == 0)
                                {
                                    worldController.method279(l, l6, k17, 0, 0, -1, j19, k19, l19, i20, method187(j21, j20), method187(j21, k20), method187(j21, l20), method187(j21, i21), 0, 0, 0, 0, i22, 0);
                                } else
                                {
                                    int k22 = aByteArrayArrayArray136[l][l6][k17] + 1;
                                    byte byte4 = aByteArrayArrayArray148[l][l6][k17];
                                    Flo flo_2 = Flo.cache[i19 - 1];
                                    int i23 = flo_2.texture;
                                    int j23;
                                    int k23;
                                    if(i23 >= 0)
                                    {
                                        k23 = Texture.getAverageTextureColour(i23);
                                        j23 = -1;
                                    } else
                                    if(flo_2.colour2 == 0xff00ff)
                                    {
                                        k23 = 0;
                                        j23 = -2;
                                        i23 = -1;
                                    } else
                                    {
                                        j23 = method177(flo_2.hue, flo_2.saturation, flo_2.lightness);
                                        k23 = Texture.HSL_TO_RGB[method185(flo_2.hsl, 96)];
                                    }
                                    worldController.method279(l, l6, k17, k22, byte4, i23, j19, k19, l19, i20, method187(j21, j20), method187(j21, k20), method187(j21, l20), method187(j21, i21), method185(j23, j20), method185(j23, k20), method185(j23, l20), method185(j23, i21), i22, k23);
                                }
                            }
                        }
                    }

                }
            }

            for(int j8 = 1; j8 < anInt147 - 1; j8++)
            {
                for(int i10 = 1; i10 < anInt146 - 1; i10++)
                    worldController.setTileLogicHeight(i10, j8, l, getVisibilityPlane(j8, l, i10));

            }

        }

        worldController.shadeModels(-10, -50, -50);
        for(int j1 = 0; j1 < anInt146; j1++)
        {
            for(int l1 = 0; l1 < anInt147; l1++)
                if((renderRuleFlags[1][j1][l1] & 2) == 2)
                    worldController.applyBridgeMode(j1, l1);

        }

        int i2 = 1;
        int j2 = 2;
        int k2 = 4;
        for(int l2 = 0; l2 < 4; l2++)
        {
            if(l2 > 0)
            {
                i2 <<= 3;
                j2 <<= 3;
                k2 <<= 3;
            }
            for(int i3 = 0; i3 <= l2; i3++)
            {
                for(int k3 = 0; k3 <= anInt147; k3++)
                {
                    for(int i4 = 0; i4 <= anInt146; i4++)
                    {
                        if((tileCullingBitsets[i3][i4][k3] & i2) != 0)
                        {
                            int k4 = k3;
                            int l5 = k3;
                            int i7 = i3;
                            int k8 = i3;
                            for(; k4 > 0 && (tileCullingBitsets[i3][i4][k4 - 1] & i2) != 0; k4--);
                            for(; l5 < anInt147 && (tileCullingBitsets[i3][i4][l5 + 1] & i2) != 0; l5++);
label0:
                            for(; i7 > 0; i7--)
                            {
                                for(int j10 = k4; j10 <= l5; j10++)
                                    if((tileCullingBitsets[i7 - 1][i4][j10] & i2) == 0)
                                        break label0;

                            }

label1:
                            for(; k8 < l2; k8++)
                            {
                                for(int k10 = k4; k10 <= l5; k10++)
                                    if((tileCullingBitsets[k8 + 1][i4][k10] & i2) == 0)
                                        break label1;

                            }

                            int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
                            if(l10 >= 8)
                            {
                                char c1 = '\360';
                                int k14 = vertexHeights[k8][i4][k4] - c1;
                                int l15 = vertexHeights[i7][i4][k4];
                                WorldController.createCullingCluster(l2, i4 * 128, i4 * 128, l5 * 128 + 128, k4 * 128, k14, l15, 1);
                                for(int l16 = i7; l16 <= k8; l16++)
                                {
                                    for(int l17 = k4; l17 <= l5; l17++)
                                        tileCullingBitsets[l16][i4][l17] &= ~i2;

                                }

                            }
                        }
                        if((tileCullingBitsets[i3][i4][k3] & j2) != 0)
                        {
                            int l4 = i4;
                            int i6 = i4;
                            int j7 = i3;
                            int l8 = i3;
                            for(; l4 > 0 && (tileCullingBitsets[i3][l4 - 1][k3] & j2) != 0; l4--);
                            for(; i6 < anInt146 && (tileCullingBitsets[i3][i6 + 1][k3] & j2) != 0; i6++);
label2:
                            for(; j7 > 0; j7--)
                            {
                                for(int i11 = l4; i11 <= i6; i11++)
                                    if((tileCullingBitsets[j7 - 1][i11][k3] & j2) == 0)
                                        break label2;

                            }

label3:
                            for(; l8 < l2; l8++)
                            {
                                for(int j11 = l4; j11 <= i6; j11++)
                                    if((tileCullingBitsets[l8 + 1][j11][k3] & j2) == 0)
                                        break label3;

                            }

                            int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
                            if(k11 >= 8)
                            {
                                char c2 = '\360';
                                int l14 = vertexHeights[l8][l4][k3] - c2;
                                int i16 = vertexHeights[j7][l4][k3];
                                WorldController.createCullingCluster(l2, i6 * 128 + 128, l4 * 128, k3 * 128, k3 * 128, l14, i16, 2);
                                for(int i17 = j7; i17 <= l8; i17++)
                                {
                                    for(int i18 = l4; i18 <= i6; i18++)
                                        tileCullingBitsets[i17][i18][k3] &= ~j2;

                                }

                            }
                        }
                        if((tileCullingBitsets[i3][i4][k3] & k2) != 0)
                        {
                            int i5 = i4;
                            int j6 = i4;
                            int k7 = k3;
                            int i9 = k3;
                            for(; k7 > 0 && (tileCullingBitsets[i3][i4][k7 - 1] & k2) != 0; k7--);
                            for(; i9 < anInt147 && (tileCullingBitsets[i3][i4][i9 + 1] & k2) != 0; i9++);
label4:
                            for(; i5 > 0; i5--)
                            {
                                for(int l11 = k7; l11 <= i9; l11++)
                                    if((tileCullingBitsets[i3][i5 - 1][l11] & k2) == 0)
                                        break label4;

                            }

label5:
                            for(; j6 < anInt146; j6++)
                            {
                                for(int i12 = k7; i12 <= i9; i12++)
                                    if((tileCullingBitsets[i3][j6 + 1][i12] & k2) == 0)
                                        break label5;

                            }

                            if(((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4)
                            {
                                int j12 = vertexHeights[i3][i5][k7];
                                WorldController.createCullingCluster(l2, j6 * 128 + 128, i5 * 128, i9 * 128 + 128, k7 * 128, j12, j12, 4);
                                for(int k13 = i5; k13 <= j6; k13++)
                                {
                                    for(int i15 = k7; i15 <= i9; i15++)
                                        tileCullingBitsets[i3][k13][i15] &= ~k2;

                                }

                            }
                        }
                    }

                }

            }

        }

    }

    private static int method172(int i, int j)
    {
        int k = (method176(i + 45365, j + 0x16713, 4) - 128) + (method176(i + 10294, j + 37821, 2) - 128 >> 1) + (method176(i, j, 1) - 128 >> 2);
        k = (int)((double)k * 0.29999999999999999D) + 35;
        if(k < 10)
            k = 10;
        else
        if(k > 60)
            k = 60;
        return k;
    }

    public static void method173(Stream stream, OnDemandFetcher class42_sub1)
    {
label0:
        {
            int i = -1;
            do
            {
                int j = stream.getSmartB();
                if(j == 0)
                    break label0;
                i += j;
                GameObjectDefinition class46 = GameObjectDefinition.getDefinition(i);
                class46.passivelyRequestModels(class42_sub1);
                do
                {
                    int k = stream.getSmartB();
                    if(k == 0)
                        break;
                    stream.getUnsignedByte();
                } while(true);
            } while(true);
        }
    }

    public final void clearRegion(int i, int j, int l, int i1)
    {
        for(int j1 = i; j1 <= i + j; j1++)
        {
            for(int k1 = i1; k1 <= i1 + l; k1++)
                if(k1 >= 0 && k1 < anInt146 && j1 >= 0 && j1 < anInt147)
                {
                    tileShadowIntensity[0][k1][j1] = 127;
                    if(k1 == i1 && k1 > 0)
                        vertexHeights[0][k1][j1] = vertexHeights[0][k1 - 1][j1];
                    if(k1 == i1 + l && k1 < anInt146 - 1)
                        vertexHeights[0][k1][j1] = vertexHeights[0][k1 + 1][j1];
                    if(j1 == i && j1 > 0)
                        vertexHeights[0][k1][j1] = vertexHeights[0][k1][j1 - 1];
                    if(j1 == i + j && j1 < anInt147 - 1)
                        vertexHeights[0][k1][j1] = vertexHeights[0][k1][j1 + 1];
                }

        }
    }

    private void renderObject(int y, WorldController worldController, CollisionMap collisionMap, int type, int z, int x, int objectId,
                                 int face)
    {
        if(lowMem && (renderRuleFlags[0][x][y] & 2) == 0)
        {
            if((renderRuleFlags[z][x][y] & 0x10) != 0)
                return;
            if(getVisibilityPlane(y, z, x) != plane)
                return;
        }
        if(z < setZ)
            setZ = z;
        int k1 = vertexHeights[z][x][y];
        int l1 = vertexHeights[z][x + 1][y];
        int i2 = vertexHeights[z][x + 1][y + 1];
        int j2 = vertexHeights[z][x][y + 1];
        int k2 = k1 + l1 + i2 + j2 >> 2;
        GameObjectDefinition objectDefinition = GameObjectDefinition.getDefinition(objectId);
        int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
        if(!objectDefinition.hasActions)
            hash += 0x80000000;
        byte config = (byte)((face << 6) + type);
        if(type == 22)
        {
            if(lowMem && !objectDefinition.hasActions && !objectDefinition.unknownAttribute3)
                return;
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(22, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, 22, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addGroundDecoration(x, y, z, k2, hash, ((Animable) (animable)), config);
            if(objectDefinition.solid && objectDefinition.hasActions && collisionMap != null)
                collisionMap.markBlocked(x, y);
            return;
        }
        if(type == 10 || type == 11)
        {
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(10, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, 10, l1, i2, k1, j2, objectDefinition.animationId, true);
            if(animable != null)
            {
                int i5 = 0;
                if(type == 11)
                    i5 += 256;
                int sizeX;
                int sizeY;
                if(face == 1 || face == 3)
                {
                    sizeX = objectDefinition.sizeY;
                    sizeY = objectDefinition.sizeX;
                } else
                {
                    sizeX = objectDefinition.sizeX;
                    sizeY = objectDefinition.sizeY;
                }
                if(worldController.addEntityB(x, y, z, k2, i5, sizeY, sizeX, hash, ((Animable) (animable)), config) && objectDefinition.castsShadow)
                {
                    Model model;
                    if(animable instanceof Model)
                        model = (Model)animable;
                    else
                        model = objectDefinition.getModelAt(10, face, k1, l1, i2, j2, -1);
                    if(model != null)
                    {
                        for(int _x = 0; _x <= sizeX; _x++)
                        {
                            for(int _y = 0; _y <= sizeY; _y++)
                            {
                                int intensity = model.diagonal2DAboveOrigin / 4;
                                if(intensity > 30)
                                    intensity = 30;
                                if(intensity > tileShadowIntensity[z][x + _x][y + _y])
                                    tileShadowIntensity[z][x + _x][y + _y] = (byte)intensity;
                            }

                        }

                    }
                }
            }
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face, objectDefinition.walkable);
            return;
        }
        if(type >= 12)
        {
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(type, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, type, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addEntityB(x, y, z, k2, 0, 1, 1, hash, ((Animable) (animable)), config);
            if(type >= 12 && type <= 17 && type != 13 && z > 0)
                tileCullingBitsets[z][x][y] |= 0x924;
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face, objectDefinition.walkable);
            return;
        }
        if(type == 0)
        {
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(0, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, 0, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallObject(x, y, z, k2, anIntArray152[face], 0, hash, ((Animable) (animable)), null, config);
            if(face == 0)
            {
                if(objectDefinition.castsShadow)
                {
                    tileShadowIntensity[z][x][y] = 50;
                    tileShadowIntensity[z][x][y + 1] = 50;
                }
                if(objectDefinition.unknownAttribute1)
                    tileCullingBitsets[z][x][y] |= 0x249;
            } else
            if(face == 1)
            {
                if(objectDefinition.castsShadow)
                {
                    tileShadowIntensity[z][x][y + 1] = 50;
                    tileShadowIntensity[z][x + 1][y + 1] = 50;
                }
                if(objectDefinition.unknownAttribute1)
                    tileCullingBitsets[z][x][y + 1] |= 0x492;
            } else
            if(face == 2)
            {
                if(objectDefinition.castsShadow)
                {
                    tileShadowIntensity[z][x + 1][y] = 50;
                    tileShadowIntensity[z][x + 1][y + 1] = 50;
                }
                if(objectDefinition.unknownAttribute1)
                    tileCullingBitsets[z][x + 1][y] |= 0x249;
            } else
            if(face == 3)
            {
                if(objectDefinition.castsShadow)
                {
                    tileShadowIntensity[z][x][y] = 50;
                    tileShadowIntensity[z][x + 1][y] = 50;
                }
                if(objectDefinition.unknownAttribute1)
                    tileCullingBitsets[z][x][y] |= 0x492;
            }
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
            if(objectDefinition.unknownAttribute2 != 16)
                worldController.method290(y, objectDefinition.unknownAttribute2, x, z);
            return;
        }
        if(type == 1)
        {
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(1, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, 1, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallObject(x, y, z, k2, wallCornerOrientation[face], 0, hash, ((Animable) (animable)), null, config);
            if(objectDefinition.castsShadow)
                if(face == 0)
                    tileShadowIntensity[z][x][y + 1] = 50;
                else
                if(face == 1)
                    tileShadowIntensity[z][x + 1][y + 1] = 50;
                else
                if(face == 2)
                    tileShadowIntensity[z][x + 1][y] = 50;
                else
                if(face == 3)
                    tileShadowIntensity[z][x][y] = 50;
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
            return;
        }
        if(type == 2)
        {
            int i3 = face + 1 & 3;
            Object obj11;
            Object obj12;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
            {
                obj11 = objectDefinition.getModelAt(2, 4 + face, k1, l1, i2, j2, -1);
                obj12 = objectDefinition.getModelAt(2, i3, k1, l1, i2, j2, -1);
            } else
            {
                obj11 = new GameObject(objectId, 4 + face, 2, l1, i2, k1, j2, objectDefinition.animationId, true);
                obj12 = new GameObject(objectId, i3, 2, l1, i2, k1, j2, objectDefinition.animationId, true);
            }
            worldController.addWallObject(x, y, z, k2, anIntArray152[face], anIntArray152[i3], hash, ((Animable) (obj11)), ((Animable) (obj12)), config);
            if(objectDefinition.unknownAttribute1)
                if(face == 0)
                {
                    tileCullingBitsets[z][x][y] |= 0x249;
                    tileCullingBitsets[z][x][y + 1] |= 0x492;
                } else
                if(face == 1)
                {
                    tileCullingBitsets[z][x][y + 1] |= 0x492;
                    tileCullingBitsets[z][x + 1][y] |= 0x249;
                } else
                if(face == 2)
                {
                    tileCullingBitsets[z][x + 1][y] |= 0x249;
                    tileCullingBitsets[z][x][y] |= 0x492;
                } else
                if(face == 3)
                {
                    tileCullingBitsets[z][x][y] |= 0x492;
                    tileCullingBitsets[z][x][y] |= 0x249;
                }
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
            if(objectDefinition.unknownAttribute2 != 16)
                worldController.method290(y, objectDefinition.unknownAttribute2, x, z);
            return;
        }
        if(type == 3)
        {
            Object obj5;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj5 = objectDefinition.getModelAt(3, face, k1, l1, i2, j2, -1);
            else
                obj5 = new GameObject(objectId, face, 3, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallObject(x, y, z, k2, wallCornerOrientation[face], 0, hash, ((Animable) (obj5)), null, config);
            if(objectDefinition.castsShadow)
                if(face == 0)
                    tileShadowIntensity[z][x][y + 1] = 50;
                else
                if(face == 1)
                    tileShadowIntensity[z][x + 1][y + 1] = 50;
                else
                if(face == 2)
                    tileShadowIntensity[z][x + 1][y] = 50;
                else
                if(face == 3)
                    tileShadowIntensity[z][x][y] = 50;
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
            return;
        }
        if(type == 9)
        {
            Object obj6;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj6 = objectDefinition.getModelAt(type, face, k1, l1, i2, j2, -1);
            else
                obj6 = new GameObject(objectId, face, type, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addEntityB(x, y, z, k2, 0, 1, 1, hash, ((Animable) (obj6)), config);
            if(objectDefinition.solid && collisionMap != null)
                collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face, objectDefinition.walkable);
            return;
        }
        if(objectDefinition.adjustToTerrain)
            if(face == 1)
            {
                int j3 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k1;
                k1 = j3;
            } else
            if(face == 2)
            {
                int k3 = j2;
                j2 = l1;
                l1 = k3;
                k3 = i2;
                i2 = k1;
                k1 = k3;
            } else
            if(face == 3)
            {
                int l3 = j2;
                j2 = k1;
                k1 = l1;
                l1 = i2;
                i2 = l3;
            }
        if(type == 4)
        {
            Object obj7;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj7 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj7 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, 0, 0, face * 512, hash, ((Animable) (obj7)), config, anIntArray152[face]);
            return;
        }
        if(type == 5)
        {
            int i4 = 16;
            int k4 = worldController.getWallObjectUID(x, y, z);
            if(k4 > 0)
                i4 = GameObjectDefinition.getDefinition(k4 >> 14 & 0x7fff).unknownAttribute2;
            Object obj13;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj13 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj13 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, anIntArray137[face] * i4, anIntArray144[face] * i4, face * 512, hash, ((Animable) (obj13)), config, anIntArray152[face]);
            return;
        }
        if(type == 6)
        {
            Object obj8;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj8 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj8 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, 0, 0, face, hash, ((Animable) (obj8)), config, 256);
            return;
        }
        if(type == 7)
        {
            Object obj9;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj9 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj9 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, 0, 0, face, hash, ((Animable) (obj9)), config, 512);
            return;
        }
        if(type == 8)
        {
            Object obj10;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj10 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj10 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, 0, 0, face, hash, ((Animable) (obj10)), config, 768);
        }
    }

    private static int method176(int i, int j, int k)
    {
        int l = i / k;
        int i1 = i & k - 1;
        int j1 = j / k;
        int k1 = j & k - 1;
        int l1 = method186(l, j1);
        int i2 = method186(l + 1, j1);
        int j2 = method186(l, j1 + 1);
        int k2 = method186(l + 1, j1 + 1);
        int l2 = method184(l1, i2, i1, k);
        int i3 = method184(j2, k2, i1, k);
        return method184(l2, i3, k1, k);
    }

    private int method177(int i, int j, int k)
    {
        if(k > 179)
            j /= 2;
        if(k > 192)
            j /= 2;
        if(k > 217)
            j /= 2;
        if(k > 243)
            j /= 2;
        return (i / 4 << 10) + (j / 32 << 7) + k / 2;
    }

    public static boolean method178(int objectId, int j)
    {
        GameObjectDefinition class46 = GameObjectDefinition.getDefinition(objectId);
        if(j == 11)
            j = 10;
        if(j >= 5 && j <= 8)
            j = 4;
        return class46.modelTypeCached(j);
    }

    public final void loadTerrainSubblock(int i, int j, CollisionMap aclass11[], int l, int i1, byte abyte0[],
                                int j1, int k1, int l1)
    {
        for(int i2 = 0; i2 < 8; i2++)
        {
            for(int j2 = 0; j2 < 8; j2++)
                if(l + i2 > 0 && l + i2 < 103 && l1 + j2 > 0 && l1 + j2 < 103)
                    aclass11[k1].clippingData[l + i2][l1 + j2] &= 0xfeffffff;

        }
        Stream stream = new Stream(abyte0);
        for(int l2 = 0; l2 < 4; l2++)
        {
            for(int i3 = 0; i3 < 64; i3++)
            {
                for(int j3 = 0; j3 < 64; j3++)
                    if(l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8)
                        method181(l1 + TiledUtils.getRotatedMapChunkY(j3 & 7, j, i3 & 7), 0, stream, l + TiledUtils.getRotatedMapChunkX(j, j3 & 7, i3 & 7), k1, j, 0);
                    else
                        method181(-1, 0, stream, -1, 0, 0, 0);

            }

        }

    }

    public final void loadTerrainBlock(byte abyte0[], int i, int j, int k, int l, CollisionMap aclass11[])
    {
        for(int i1 = 0; i1 < 4; i1++)
        {
            for(int j1 = 0; j1 < 64; j1++)
            {
                for(int k1 = 0; k1 < 64; k1++)
                    if(j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103)
                        aclass11[i1].clippingData[j + j1][i + k1] &= 0xfeffffff;

            }

        }

        Stream stream = new Stream(abyte0);
        for(int l1 = 0; l1 < 4; l1++)
        {
            for(int i2 = 0; i2 < 64; i2++)
            {
                for(int j2 = 0; j2 < 64; j2++)
                    method181(j2 + i, l, stream, i2 + j, l1, 0, k);

            }

        }
    }

    private void method181(int i, int j, Stream stream, int k, int l, int i1,
                                 int k1)
    {
        if(k >= 0 && k < 104 && i >= 0 && i < 104)
        {
            renderRuleFlags[l][k][i] = 0;
            do
            {
                int l1 = stream.getUnsignedByte();
                if(l1 == 0)
                    if(l == 0)
                    {
                        vertexHeights[0][k][i] = -method172(0xe3b7b + k + k1, 0x87cce + i + j) * 8;
                        return;
                    } else
                    {
                        vertexHeights[l][k][i] = vertexHeights[l - 1][k][i] - 240;
                        return;
                    }
                if(l1 == 1)
                {
                    int j2 = stream.getUnsignedByte();
                    if(j2 == 1)
                        j2 = 0;
                    if(l == 0)
                    {
                        vertexHeights[0][k][i] = -j2 * 8;
                        return;
                    } else
                    {
                        vertexHeights[l][k][i] = vertexHeights[l - 1][k][i] - j2 * 8;
                        return;
                    }
                }
                if(l1 <= 49)
                {
                    aByteArrayArrayArray130[l][k][i] = stream.get();
                    aByteArrayArrayArray136[l][k][i] = (byte)((l1 - 2) / 4);
                    aByteArrayArrayArray148[l][k][i] = (byte)((l1 - 2) + i1 & 3);
                } else
                if(l1 <= 81)
                    renderRuleFlags[l][k][i] = (byte)(l1 - 49);
                else
                    aByteArrayArrayArray142[l][k][i] = (byte)(l1 - 81);
            } while(true);
        }
        do
        {
            int i2 = stream.getUnsignedByte();
            if(i2 == 0)
                break;
            if(i2 == 1)
            {
                stream.getUnsignedByte();
                return;
            }
            if(i2 <= 49)
                stream.getUnsignedByte();
        } while(true);
    }

    private int getVisibilityPlane(int i, int j, int k)
    {
        if((renderRuleFlags[j][k][i] & 8) != 0)
            return 0;
        if(j > 0 && (renderRuleFlags[1][k][i] & 2) != 0)
            return j - 1;
        else
            return j;
    }

    public final void loadObjectSubblock(CollisionMap aclass11[], WorldController worldController, int i, int j, int k, int l,
                                byte abyte0[], int i1, int j1, int k1)
    {
label0:
        {
            Stream stream = new Stream(abyte0);
            int l1 = -1;
            do
            {
                int i2 = stream.getSmartB();
                if(i2 == 0)
                    break label0;
                l1 += i2;
                int j2 = 0;
                do
                {
                    int k2 = stream.getSmartB();
                    if(k2 == 0)
                        break;
                    j2 += k2 - 1;
                    int l2 = j2 & 0x3f;
                    int i3 = j2 >> 6 & 0x3f;
                    int j3 = j2 >> 12;
                    int k3 = stream.getUnsignedByte();
                    int l3 = k3 >> 2;
                    int i4 = k3 & 3;
                    if(j3 == i && i3 >= i1 && i3 < i1 + 8 && l2 >= k && l2 < k + 8)
                    {
                        GameObjectDefinition class46 = GameObjectDefinition.getDefinition(l1);
                        int j4 = j + TiledUtils.getRotatedLandscapeChunkX(j1, class46.sizeY, i3 & 7, l2 & 7, class46.sizeX);
                        int k4 = k1 + TiledUtils.getRotatedLandscapeChunkY(l2 & 7, class46.sizeY, j1, class46.sizeX, i3 & 7);
                        if(j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103)
                        {
                            int l4 = j3;
                            if((renderRuleFlags[1][j4][k4] & 2) == 2)
                                l4--;
                            CollisionMap class11 = null;
                            if(l4 >= 0)
                                class11 = aclass11[l4];
                            renderObject(k4, worldController, class11, l3, l, j4, l1, i4 + j1 & 3);
                        }
                    }
                } while(true);
            } while(true);
        }
    }

    private static int method184(int i, int j, int k, int l)
    {
        int i1 = 0x10000 - Texture.COSINE[(k * 1024) / l] >> 1;
        return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
    }

    private int method185(int i, int j)
    {
        if(i == -2)
            return 0xbc614e;
        if(i == -1)
        {
            if(j < 0)
                j = 0;
            else
            if(j > 127)
                j = 127;
            j = 127 - j;
            return j;
        }
        j = (j * (i & 0x7f)) / 128;
        if(j < 2)
            j = 2;
        else
        if(j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    private static int method186(int i, int j)
    {
        int k = method170(i - 1, j - 1) + method170(i + 1, j - 1) + method170(i - 1, j + 1) + method170(i + 1, j + 1);
        int l = method170(i - 1, j) + method170(i + 1, j) + method170(i, j - 1) + method170(i, j + 1);
        int i1 = method170(i, j);
        return k / 16 + l / 8 + i1 / 4;
    }

    private static int method187(int i, int j)
    {
        if(i == -1)
            return 0xbc614e;
        j = (j * (i & 0x7f)) / 128;
        if(j < 2)
            j = 2;
        else
        if(j > 126)
            j = 126;
        return (i & 0xff80) + j;
    }

    public static void method188(WorldController worldController, int i, int j, int k, int l, CollisionMap collisionMap, int ai[][][], int i1,
                                 int j1, int k1)
    {
        int l1 = ai[l][i1][j];
        int i2 = ai[l][i1 + 1][j];
        int j2 = ai[l][i1 + 1][j + 1];
        int k2 = ai[l][i1][j + 1];
        int l2 = l1 + i2 + j2 + k2 >> 2;
        GameObjectDefinition class46 = GameObjectDefinition.getDefinition(j1);
        int i3 = i1 + (j << 7) + (j1 << 14) + 0x40000000;
        if(!class46.hasActions)
            i3 += 0x80000000;
        byte byte1 = (byte)((i << 6) + k);
        if(k == 22)
        {
            Object obj;
            if(class46.animationId == -1 && class46.childIds == null)
                obj = class46.getModelAt(22, i, l1, i2, j2, k2, -1);
            else
                obj = new GameObject(j1, i, 22, i2, j2, l1, k2, class46.animationId, true);
            worldController.addGroundDecoration(i1, j, k1, l2, i3, ((Animable) (obj)), byte1);
            if(class46.solid && class46.hasActions)
                collisionMap.markBlocked(i1, j);
            return;
        }
        if(k == 10 || k == 11)
        {
            Object obj1;
            if(class46.animationId == -1 && class46.childIds == null)
                obj1 = class46.getModelAt(10, i, l1, i2, j2, k2, -1);
            else
                obj1 = new GameObject(j1, i, 10, i2, j2, l1, k2, class46.animationId, true);
            if(obj1 != null)
            {
                int j5 = 0;
                if(k == 11)
                    j5 += 256;
                int k4;
                int i5;
                if(i == 1 || i == 3)
                {
                    k4 = class46.sizeY;
                    i5 = class46.sizeX;
                } else
                {
                    k4 = class46.sizeX;
                    i5 = class46.sizeY;
                }
                worldController.addEntityB(i1, j, k1, l2, j5, i5, k4, i3, ((Animable) (obj1)), byte1);
            }
            if(class46.solid)
                collisionMap.markSolidOccupant(i1, j, class46.sizeX, class46.sizeY, i, class46.walkable);
            return;
        }
        if(k >= 12)
        {
            Object obj2;
            if(class46.animationId == -1 && class46.childIds == null)
                obj2 = class46.getModelAt(k, i, l1, i2, j2, k2, -1);
            else
                obj2 = new GameObject(j1, i, k, i2, j2, l1, k2, class46.animationId, true);
            worldController.addEntityB(i1, j, k1, l2, 0, 1, 1, i3, ((Animable) (obj2)), byte1);
            if(class46.solid)
                collisionMap.markSolidOccupant(i1, j, class46.sizeX, class46.sizeY, i, class46.walkable);
            return;
        }
        if(k == 0)
        {
            Object obj3;
            if(class46.animationId == -1 && class46.childIds == null)
                obj3 = class46.getModelAt(0, i, l1, i2, j2, k2, -1);
            else
                obj3 = new GameObject(j1, i, 0, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallObject(i1, j, k1, l2, anIntArray152[i], 0, i3, ((Animable) (obj3)), null, byte1);
            if(class46.solid)
                collisionMap.markWall(j, i, i1, k, class46.walkable);
            return;
        }
        if(k == 1)
        {
            Object obj4;
            if(class46.animationId == -1 && class46.childIds == null)
                obj4 = class46.getModelAt(1, i, l1, i2, j2, k2, -1);
            else
                obj4 = new GameObject(j1, i, 1, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallObject(i1, j, k1, l2, wallCornerOrientation[i], 0, i3, ((Animable) (obj4)), null, byte1);
            if(class46.solid)
                collisionMap.markWall(j, i, i1, k, class46.walkable);
            return;
        }
        if(k == 2)
        {
            int j3 = i + 1 & 3;
            Object obj11;
            Object obj12;
            if(class46.animationId == -1 && class46.childIds == null)
            {
                obj11 = class46.getModelAt(2, 4 + i, l1, i2, j2, k2, -1);
                obj12 = class46.getModelAt(2, j3, l1, i2, j2, k2, -1);
            } else
            {
                obj11 = new GameObject(j1, 4 + i, 2, i2, j2, l1, k2, class46.animationId, true);
                obj12 = new GameObject(j1, j3, 2, i2, j2, l1, k2, class46.animationId, true);
            }
            worldController.addWallObject(i1, j, k1, l2, anIntArray152[i], anIntArray152[j3], i3, ((Animable) (obj11)), ((Animable) (obj12)), byte1);
            if(class46.solid)
                collisionMap.markWall(j, i, i1, k, class46.walkable);
            return;
        }
        if(k == 3)
        {
            Object obj5;
            if(class46.animationId == -1 && class46.childIds == null)
                obj5 = class46.getModelAt(3, i, l1, i2, j2, k2, -1);
            else
                obj5 = new GameObject(j1, i, 3, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallObject(i1, j, k1, l2, wallCornerOrientation[i], 0, i3, ((Animable) (obj5)), null, byte1);
            if(class46.solid)
                collisionMap.markWall(j, i, i1, k, class46.walkable);
            return;
        }
        if(k == 9)
        {
            Object obj6;
            if(class46.animationId == -1 && class46.childIds == null)
                obj6 = class46.getModelAt(k, i, l1, i2, j2, k2, -1);
            else
                obj6 = new GameObject(j1, i, k, i2, j2, l1, k2, class46.animationId, true);
            worldController.addEntityB(i1, j, k1, l2, 0, 1, 1, i3, ((Animable) (obj6)), byte1);
            if(class46.solid)
                collisionMap.markSolidOccupant(i1, j, class46.sizeX, class46.sizeY, i, class46.walkable);
            return;
        }
        if(class46.adjustToTerrain)
            if(i == 1)
            {
                int k3 = k2;
                k2 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k3;
            } else
            if(i == 2)
            {
                int l3 = k2;
                k2 = i2;
                i2 = l3;
                l3 = j2;
                j2 = l1;
                l1 = l3;
            } else
            if(i == 3)
            {
                int i4 = k2;
                k2 = l1;
                l1 = i2;
                i2 = j2;
                j2 = i4;
            }
        if(k == 4)
        {
            Object obj7;
            if(class46.animationId == -1 && class46.childIds == null)
                obj7 = class46.getModelAt(4, 0, l1, i2, j2, k2, -1);
            else
                obj7 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallDecoration(i1, j, k1, l2, 0, 0, i * 512, i3, ((Animable) (obj7)), byte1, anIntArray152[i]);
            return;
        }
        if(k == 5)
        {
            int j4 = 16;
            int l4 = worldController.getWallObjectUID(i1, j, k1);
            if(l4 > 0)
                j4 = GameObjectDefinition.getDefinition(l4 >> 14 & 0x7fff).unknownAttribute2;
            Object obj13;
            if(class46.animationId == -1 && class46.childIds == null)
                obj13 = class46.getModelAt(4, 0, l1, i2, j2, k2, -1);
            else
                obj13 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallDecoration(i1, j, k1, l2, anIntArray137[i] * j4, anIntArray144[i] * j4, i * 512, i3, ((Animable) (obj13)), byte1, anIntArray152[i]);
            return;
        }
        if(k == 6)
        {
            Object obj8;
            if(class46.animationId == -1 && class46.childIds == null)
                obj8 = class46.getModelAt(4, 0, l1, i2, j2, k2, -1);
            else
                obj8 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallDecoration(i1, j, k1, l2, 0, 0, i, i3, ((Animable) (obj8)), byte1, 256);
            return;
        }
        if(k == 7)
        {
            Object obj9;
            if(class46.animationId == -1 && class46.childIds == null)
                obj9 = class46.getModelAt(4, 0, l1, i2, j2, k2, -1);
            else
                obj9 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallDecoration(i1, j, k1, l2, 0, 0, i, i3, ((Animable) (obj9)), byte1, 512);
            return;
        }
        if(k == 8)
        {
            Object obj10;
            if(class46.animationId == -1 && class46.childIds == null)
                obj10 = class46.getModelAt(4, 0, l1, i2, j2, k2, -1);
            else
                obj10 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.animationId, true);
            worldController.addWallDecoration(i1, j, k1, l2, 0, 0, i, i3, ((Animable) (obj10)), byte1, 768);
        }
    }

  public static boolean objectBlockCached(int i, byte[] is, int i_250_
  ) //xxx bad method, decompiled with JODE
  {
    boolean bool = true;
    Stream stream = new Stream(is);
    int i_252_ = -1;
    for (;;)
      {
	int i_253_ = stream.getSmartB ();
	if (i_253_ == 0)
	  break;
	i_252_ += i_253_;
	int i_254_ = 0;
	boolean bool_255_ = false;
	for (;;)
	  {
	    if (bool_255_)
	      {
		int i_256_ = stream.getSmartB ();
		if (i_256_ == 0)
		  break;
		stream.getUnsignedByte();
	      }
	    else
	      {
		int i_257_ = stream.getSmartB ();
		if (i_257_ == 0)
		  break;
		i_254_ += i_257_ - 1;
		int i_258_ = i_254_ & 0x3f;
		int i_259_ = i_254_ >> 6 & 0x3f;
		int i_260_ = stream.getUnsignedByte() >> 2;
		int i_261_ = i_259_ + i;
		int i_262_ = i_258_ + i_250_;
		if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103)
		  {
		    GameObjectDefinition class46 = GameObjectDefinition.getDefinition (i_252_);
		    if (i_260_ != 22 || !lowMem || class46.hasActions
                    || class46.unknownAttribute3)
		      {
			bool &= class46.modelCached ();
			bool_255_ = true;
		      }
		  }
	      }
	  }
      }
    return bool;
  }

    public final void loadObjectBlock(int i, CollisionMap aclass11[], int j, WorldController worldController, byte abyte0[])
    {
label0:
        {
            Stream stream = new Stream(abyte0);
            int l = -1;
            do
            {
                int i1 = stream.getSmartB();
                if(i1 == 0)
                    break label0;
                l += i1;
                int j1 = 0;
                do
                {
                    int k1 = stream.getSmartB();
                    if(k1 == 0)
                        break;
                    j1 += k1 - 1;
                    int l1 = j1 & 0x3f;
                    int i2 = j1 >> 6 & 0x3f;
                    int j2 = j1 >> 12;
                    int k2 = stream.getUnsignedByte();
                    int l2 = k2 >> 2;
                    int i3 = k2 & 3;
                    int j3 = i2 + i;
                    int k3 = l1 + j;
                    if(j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103)
                    {
                        int l3 = j2;
                        if((renderRuleFlags[1][j3][k3] & 2) == 2)
                            l3--;
                        CollisionMap class11 = null;
                        if(l3 >= 0)
                            class11 = aclass11[l3];
                        renderObject(k3, worldController, class11, l2, j2, j3, l, i3);
                    }
                } while(true);
            } while(true);
        }
    }

    private static int anInt123 = (int)(Math.random() * 17D) - 8;
    private final int[] anIntArray124;
    private final int[] anIntArray125;
    private final int[] anIntArray126;
    private final int[] anIntArray127;
    private final int[] anIntArray128;
    private final int[][][] vertexHeights;
    private final byte[][][] aByteArrayArrayArray130;
    static int plane;
    private static int anInt133 = (int)(Math.random() * 33D) - 16;
    private final byte[][][] tileShadowIntensity;
    private final int[][][] tileCullingBitsets;
    private final byte[][][] aByteArrayArrayArray136;
    private static final int anIntArray137[] = {
        1, 0, -1, 0
    };
    private static final int anInt138 = 323;
    private final int[][] anIntArrayArray139;
    private static final int wallCornerOrientation[] = {
        16, 32, 64, 128
    };
    private final byte[][][] aByteArrayArrayArray142;
    private static final int anIntArray144[] = {
        0, -1, 0, 1
    };
    static int setZ = 99;
    private final int anInt146;
    private final int anInt147;
    private final byte[][][] aByteArrayArrayArray148;
    private final byte[][][] renderRuleFlags;
    static boolean lowMem = true;
    private static final int anIntArray152[] = {
        1, 2, 4, 8
    };

}
