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
        tileTypeLayer0 = new byte[4][anInt146][anInt147];
        tileTypeLayer1 = new byte[4][anInt146][anInt147];
        tileShapeLayer1 = new byte[4][anInt146][anInt147];
        tileOrientationLayer1 = new byte[4][anInt146][anInt147];
        tileCullingBitsets = new int[4][anInt146 + 1][anInt147 + 1];
        tileShadowIntensity = new byte[4][anInt146 + 1][anInt147 + 1];
        anIntArrayArray139 = new int[anInt146 + 1][anInt147 + 1];
        anIntArray124 = new int[anInt147];
        anIntArray125 = new int[anInt147];
        anIntArray126 = new int[anInt147];
        anIntArray127 = new int[anInt147];
        anIntArray128 = new int[anInt147];
    }

    private static int randomNoise(int i, int j)
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
        for(int _z = 0; _z < 4; _z++)
        {
            byte abyte0[][] = tileShadowIntensity[_z];
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
                    int k6 = vertexHeights[_z][j5 + 1][j4] - vertexHeights[_z][j5 - 1][j4];
                    int l7 = vertexHeights[_z][j5][j4 + 1] - vertexHeights[_z][j5][j4 - 1];
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

            for(int _x = -5; _x < anInt146 + 5; _x++)
            {
                for(int i8 = 0; i8 < anInt147; i8++)
                {
                    int k9 = _x + 5;
                    if(k9 >= 0 && k9 < anInt146)
                    {
                        int l12 = tileTypeLayer0[_z][k9][i8] & 0xff;
                        if(l12 > 0)
                        {
                            FloorDefinition flo = FloorDefinition.cache[l12 - 1];
                            anIntArray124[i8] += flo.hue;
                            anIntArray125[i8] += flo.saturation;
                            anIntArray126[i8] += flo.lightness;
                            anIntArray127[i8] += flo.hueDivisor;
                            anIntArray128[i8]++;
                        }
                    }
                    int i13 = _x - 5;
                    if(i13 >= 0 && i13 < anInt146)
                    {
                        int i14 = tileTypeLayer0[_z][i13][i8] & 0xff;
                        if(i14 > 0)
                        {
                            FloorDefinition flo_1 = FloorDefinition.cache[i14 - 1];
                            anIntArray124[i8] -= flo_1.hue;
                            anIntArray125[i8] -= flo_1.saturation;
                            anIntArray126[i8] -= flo_1.lightness;
                            anIntArray127[i8] -= flo_1.hueDivisor;
                            anIntArray128[i8]--;
                        }
                    }
                }

                if(_x >= 1 && _x < anInt146 - 1)
                {
                    int l9 = 0;
                    int j13 = 0;
                    int j14 = 0;
                    int k15 = 0;
                    int k16 = 0;
                    for(int _y = -5; _y < anInt147 + 5; _y++)
                    {
                        int j18 = _y + 5;
                        if(j18 >= 0 && j18 < anInt147)
                        {
                            l9 += anIntArray124[j18];
                            j13 += anIntArray125[j18];
                            j14 += anIntArray126[j18];
                            k15 += anIntArray127[j18];
                            k16 += anIntArray128[j18];
                        }
                        int k18 = _y - 5;
                        if(k18 >= 0 && k18 < anInt147)
                        {
                            l9 -= anIntArray124[k18];
                            j13 -= anIntArray125[k18];
                            j14 -= anIntArray126[k18];
                            k15 -= anIntArray127[k18];
                            k16 -= anIntArray128[k18];
                        }
                        if(_y >= 1 && _y < anInt147 - 1 && (!lowMem || (renderRuleFlags[0][_x][_y] & 2) != 0 || (renderRuleFlags[_z][_x][_y] & 0x10) == 0 && getVisibilityPlane(_y, _z, _x) == plane))
                        {
                            if(_z < setZ)
                                setZ = _z;
                            int l18 = tileTypeLayer0[_z][_x][_y] & 0xff;
                            int i19 = tileTypeLayer1[_z][_x][_y] & 0xff;
                            if(l18 > 0 || i19 > 0)
                            {
                                int j19 = vertexHeights[_z][_x][_y];
                                int k19 = vertexHeights[_z][_x + 1][_y];
                                int l19 = vertexHeights[_z][_x + 1][_y + 1];
                                int i20 = vertexHeights[_z][_x][_y + 1];
                                int j20 = anIntArrayArray139[_x][_y];
                                int k20 = anIntArrayArray139[_x + 1][_y];
                                int l20 = anIntArrayArray139[_x + 1][_y + 1];
                                int i21 = anIntArrayArray139[_x][_y + 1];
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
                                if(_z > 0)
                                {
                                    boolean flag = true;
                                    if(l18 == 0 && tileShapeLayer1[_z][_x][_y] != 0)
                                        flag = false;
                                    if(i19 > 0 && !FloorDefinition.cache[i19 - 1].occlude)
                                        flag = false;
                                    if(flag && j19 == k19 && j19 == l19 && j19 == i20)
                                        tileCullingBitsets[_z][_x][_y] |= 0x924;
                                }
                                int i22 = 0;
                                if(j21 != -1)
                                    i22 = Texture.HSL_TO_RGB[mixLightness(k21, 96)];
                                if(i19 == 0)
                                {
                                    worldController.method279(_z, _x, _y, 0, 0, -1, j19, k19, l19, i20, mixLightness(j21, j20), mixLightness(j21, k20), mixLightness(j21, l20), mixLightness(j21, i21), 0, 0, 0, 0, i22, 0);
                                } else
                                {
                                    int clippingPath = tileShapeLayer1[_z][_x][_y] + 1;
                                    byte clippingPathRotation = tileOrientationLayer1[_z][_x][_y];
                                    FloorDefinition definition = FloorDefinition.cache[i19 - 1];
                                    int textureId = definition.textureId;
                                    int hslBitset;
                                    int minimapColour;
                                    if(textureId >= 0)
                                    {
                                        minimapColour = Texture.getAverageTextureColour(textureId);
                                        hslBitset = -1;
                                    } else
                                    if(definition.rgbColour == 0xff00ff)
                                    {
                                        minimapColour = 0;
                                        hslBitset = -2;
                                        textureId = -1;
                                    } else
                                    {
                                        hslBitset = method177(definition.hue2, definition.saturation, definition.lightness);
                                        minimapColour = Texture.HSL_TO_RGB[mixLightnessSigned(definition.hsl, 96)];
                                    }
                                    worldController.method279(_z, _x, _y, clippingPath, clippingPathRotation, textureId, j19, k19, l19, i20, mixLightness(j21, j20), mixLightness(j21, k20), mixLightness(j21, l20), mixLightness(j21, i21), mixLightnessSigned(hslBitset, j20), mixLightnessSigned(hslBitset, k20), mixLightnessSigned(hslBitset, l20), mixLightnessSigned(hslBitset, i21), i22, minimapColour);
                                }
                            }
                        }
                    }

                }
            }

            for(int j8 = 1; j8 < anInt147 - 1; j8++)
            {
                for(int i10 = 1; i10 < anInt146 - 1; i10++)
                    worldController.setTileLogicHeight(i10, j8, _z, getVisibilityPlane(j8, _z, i10));

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

    private static int perlinNoise3Pass(int i, int j)
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
            worldController.addWallObject(x, y, z, k2, POWERS_OF_TWO[face], 0, hash, ((Animable) (animable)), null, config);
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
            if(objectDefinition.offsetAmplifier != 16)
                worldController.method290(y, objectDefinition.offsetAmplifier, x, z);
            return;
        }
        if(type == 1)
        {
            Animable animable;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                animable = objectDefinition.getModelAt(1, face, k1, l1, i2, j2, -1);
            else
                animable = new GameObject(objectId, face, 1, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallObject(x, y, z, k2, WALL_CORNER_ORIENTATION[face], 0, hash, ((Animable) (animable)), null, config);
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
            worldController.addWallObject(x, y, z, k2, POWERS_OF_TWO[face], POWERS_OF_TWO[i3], hash, ((Animable) (obj11)), ((Animable) (obj12)), config);
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
            if(objectDefinition.offsetAmplifier != 16)
                worldController.method290(y, objectDefinition.offsetAmplifier, x, z);
            return;
        }
        if(type == 3)
        {
            Object obj5;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj5 = objectDefinition.getModelAt(3, face, k1, l1, i2, j2, -1);
            else
                obj5 = new GameObject(objectId, face, 3, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallObject(x, y, z, k2, WALL_CORNER_ORIENTATION[face], 0, hash, ((Animable) (obj5)), null, config);
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
            worldController.addWallDecoration(x, y, z, k2, 0, 0, face * 512, hash, ((Animable) (obj7)), config, POWERS_OF_TWO[face]);
            return;
        }
        if(type == 5)
        {
            int i4 = 16;
            int k4 = worldController.getWallObjectHash(x, y, z);
            if(k4 > 0)
                i4 = GameObjectDefinition.getDefinition(k4 >> 14 & 0x7fff).offsetAmplifier;
            Object obj13;
            if(objectDefinition.animationId == -1 && objectDefinition.childIds == null)
                obj13 = objectDefinition.getModelAt(4, 0, k1, l1, i2, j2, -1);
            else
                obj13 = new GameObject(objectId, 0, 4, l1, i2, k1, j2, objectDefinition.animationId, true);
            worldController.addWallDecoration(x, y, z, k2, FACE_OFFSET_X[face] * i4, FACE_OFFSET_Y[face] * i4, face * 512, hash, ((Animable) (obj13)), config, POWERS_OF_TWO[face]);
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
        int l1 = randomNoiseWeighedSum(l, j1);
        int i2 = randomNoiseWeighedSum(l + 1, j1);
        int j2 = randomNoiseWeighedSum(l, j1 + 1);
        int k2 = randomNoiseWeighedSum(l + 1, j1 + 1);
        int l2 = interpolate(l1, i2, i1, k);
        int i3 = interpolate(j2, k2, i1, k);
        return interpolate(l2, i3, k1, k);
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

    public static boolean modelTypeCached(int objectId, int type)
    {
        GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
        if(type == 11)
            type = 10;
        if(type >= 5 && type <= 8)
            type = 4;
        return definition.modelTypeCached(type);
    }

    public final void loadTerrainSubblock(int subBlockZ, int rotation, CollisionMap collisionMap[], int blockX, int subBlockX, byte blockData[],
                                int subBlockY, int blockZ, int blockY)
    {
        for(int tileX = 0; tileX < 8; tileX++)
        {
            for(int tileY = 0; tileY < 8; tileY++)
                if(blockX + tileX > 0 && blockX + tileX < 103 && blockY + tileY > 0 && blockY + tileY < 103)
                    collisionMap[blockZ].clippingData[blockX + tileX][blockY + tileY] &= 0xfeffffff;

        }
        Stream stream = new Stream(blockData);
        for(int tileZ = 0; tileZ < 4; tileZ++)
        {
            for(int tileX = 0; tileX < 64; tileX++)
            {
                for(int tileY = 0; tileY < 64; tileY++)
                    if(tileZ == subBlockZ && tileX >= subBlockX && tileX < subBlockX + 8 && tileY >= subBlockY && tileY < subBlockY + 8)
                        loadTerrainTile(blockY + TiledUtils.getRotatedMapChunkY(tileY & 7, rotation, tileX & 7), 0, stream, blockX + TiledUtils.getRotatedMapChunkX(rotation, tileY & 7, tileX & 7), blockZ, rotation, 0);
                    else
                        loadTerrainTile(-1, 0, stream, -1, 0, 0, 0);

            }

        }

    }

    public final void loadTerrainBlock(byte abyte0[], int i, int j, int k, int l, CollisionMap collisionMap[])
    {
        for(int i1 = 0; i1 < 4; i1++)
        {
            for(int j1 = 0; j1 < 64; j1++)
            {
                for(int k1 = 0; k1 < 64; k1++)
                    if(j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103)
                        collisionMap[i1].clippingData[j + j1][i + k1] &= 0xfeffffff;

            }

        }

        Stream stream = new Stream(abyte0);
        for(int l1 = 0; l1 < 4; l1++)
        {
            for(int i2 = 0; i2 < 64; i2++)
            {
                for(int j2 = 0; j2 < 64; j2++)
                    loadTerrainTile(j2 + i, l, stream, i2 + j, l1, 0, k);

            }

        }
    }

    private void loadTerrainTile(int tileY, int j, Stream stream, int tileX, int tileZ, int i1,
                                 int k1)
    {
        if(tileX >= 0 && tileX < 104 && tileY >= 0 && tileY < 104)
        {
            renderRuleFlags[tileZ][tileX][tileY] = 0;
            do
            {
                int value = stream.getUnsignedByte();
                if(value == 0)
                    if(tileZ == 0)
                    {
                        vertexHeights[0][tileX][tileY] = -perlinNoise3Pass(0xe3b7b + tileX + k1, 0x87cce + tileY + j) * 8;
                        return;
                    } else
                    {
                        vertexHeights[tileZ][tileX][tileY] = vertexHeights[tileZ - 1][tileX][tileY] - 240;
                        return;
                    }
                if(value == 1)
                {
                    int height = stream.getUnsignedByte();
                    if(height == 1)
                        height = 0;
                    if(tileZ == 0)
                    {
                        vertexHeights[0][tileX][tileY] = -height * 8;
                        return;
                    } else
                    {
                        vertexHeights[tileZ][tileX][tileY] = vertexHeights[tileZ - 1][tileX][tileY] - height * 8;
                        return;
                    }
                }
                if(value <= 49)
                {
                    tileTypeLayer1[tileZ][tileX][tileY] = stream.get();
                    tileShapeLayer1[tileZ][tileX][tileY] = (byte)((value - 2) / 4);
                    tileOrientationLayer1[tileZ][tileX][tileY] = (byte)((value - 2) + i1 & 3);
                } else
                if(value <= 81)
                    renderRuleFlags[tileZ][tileX][tileY] = (byte)(value - 49);
                else
                    tileTypeLayer0[tileZ][tileX][tileY] = (byte)(value - 81);
            } while(true);
        }
        do
        {
            int value = stream.getUnsignedByte();
            if(value == 0)
                break;
            if(value == 1)
            {
                stream.getUnsignedByte();
                return;
            }
            if(value <= 49)
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

    private static int interpolate(int a, int b, int delta, int deltaScale)
    {
        int f = 0x10000 - Texture.COSINE[(delta * 1024) / deltaScale] >> 1;
        return (a * (0x10000 - f) >> 16) + (b * f >> 16);
    }

    private int mixLightnessSigned(int hsl, int lightness)
    {
        if(hsl == -2)
            return 0xbc614e;
        if(hsl == -1)
        {
            if(lightness < 0)
                lightness = 0;
            else
            if(lightness > 127)
                lightness = 127;
            lightness = 127 - lightness;
            return lightness;
        }
        lightness = (lightness * (hsl & 0x7f)) / 128;
        if(lightness < 2)
            lightness = 2;
        else
        if(lightness > 126)
            lightness = 126;
        return (hsl & 0xff80) + lightness;
    }

    private static int randomNoiseWeighedSum(int x, int y)
    {
        int vDist2 = randomNoise(x - 1, y - 1) + randomNoise(x + 1, y - 1) + randomNoise(x - 1, y + 1) + randomNoise(x + 1, y + 1);
        int vDist1 = randomNoise(x - 1, y) + randomNoise(x + 1, y) + randomNoise(x, y - 1) + randomNoise(x, y + 1);
        int vLocal = randomNoise(x, y);
        return vDist2 / 16 + vDist1 / 8 + vLocal / 4;
    }

    private static int mixLightness(int hsl, int lightness)
    {
        if(hsl == -1)
            return 0xbc614e;
        lightness = (lightness * (hsl & 0x7f)) / 128;
        if(lightness < 2)
            lightness = 2;
        else
        if(lightness > 126)
            lightness = 126;
        return (hsl & 0xff80) + lightness;
    }

    public static void method188(WorldController worldController, int face, int y, int type, int plane, CollisionMap collisionMap, int groundArray[][][], int x,
                                 int objectId, int z)
    {
        int vertexHeightBottomLeft = groundArray[plane][x][y];
        int vertexHeightBottomRight = groundArray[plane][x + 1][y];
        int vertexHeightTopRight = groundArray[plane][x + 1][y + 1];
        int vertexHeightTopLeft = groundArray[plane][x][y + 1];
        int drawHeight = vertexHeightBottomLeft + vertexHeightBottomRight + vertexHeightTopRight + vertexHeightTopLeft >> 2;
        GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
        int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
        if(!definition.hasActions)
            hash += 0x80000000;
        byte config = (byte)((face << 6) + type);
        if(type == 22)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(22, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, 22, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addGroundDecoration(x, y, z, drawHeight, hash, ((Animable) (animable)), config);
            if(definition.solid && definition.hasActions)
                collisionMap.markBlocked(x, y);
            return;
        }
        if(type == 10 || type == 11)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(10, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, 10, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            if(animable != null)
            {
                int rotation = 0;
                if(type == 11)
                    rotation += 256;
                int sizeX;
                int sizeY;
                if(face == 1 || face == 3)
                {
                    sizeX = definition.sizeY;
                    sizeY = definition.sizeX;
                } else
                {
                    sizeX = definition.sizeX;
                    sizeY = definition.sizeY;
                }
                worldController.addEntityB(x, y, z, drawHeight, rotation, sizeY, sizeX, hash, ((Animable) (animable)), config);
            }
            if(definition.solid)
                collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
            return;
        }
        if(type >= 12)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(type, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, type, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addEntityB(x, y, z, drawHeight, 0, 1, 1, hash, ((Animable) (animable)), config);
            if(definition.solid)
                collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
            return;
        }
        if(type == 0)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(0, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, 0, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallObject(x, y, z, drawHeight, POWERS_OF_TWO[face], 0, hash, ((Animable) (animable)), null, config);
            if(definition.solid)
                collisionMap.markWall(y, face, x, type, definition.walkable);
            return;
        }
        if(type == 1)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(1, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, 1, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallObject(x, y, z, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((Animable) (animable)), null, config);
            if(definition.solid)
                collisionMap.markWall(y, face, x, type, definition.walkable);
            return;
        }
        if(type == 2)
        {
            int _face = face + 1 & 3;
            Animable animable1;
            Animable animable2;
            if(definition.animationId == -1 && definition.childIds == null)
            {
                animable1 = definition.getModelAt(2, 4 + face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
                animable2 = definition.getModelAt(2, _face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            } else
            {
                animable1 = new GameObject(objectId, 4 + face, 2, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
                animable2 = new GameObject(objectId, _face, 2, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            }
            worldController.addWallObject(x, y, z, drawHeight, POWERS_OF_TWO[face], POWERS_OF_TWO[_face], hash, ((Animable) (animable1)), ((Animable) (animable2)), config);
            if(definition.solid)
                collisionMap.markWall(y, face, x, type, definition.walkable);
            return;
        }
        if(type == 3)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(3, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, 3, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallObject(x, y, z, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((Animable) (animable)), null, config);
            if(definition.solid)
                collisionMap.markWall(y, face, x, type, definition.walkable);
            return;
        }
        if(type == 9)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(type, face, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, face, type, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addEntityB(x, y, z, drawHeight, 0, 1, 1, hash, ((Animable) (animable)), config);
            if(definition.solid)
                collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
            return;
        }
        if(definition.adjustToTerrain)
            if(face == 1)
            {
                int temp = vertexHeightTopLeft;
                vertexHeightTopLeft = vertexHeightTopRight;
                vertexHeightTopRight = vertexHeightBottomRight;
                vertexHeightBottomRight = vertexHeightBottomLeft;
                vertexHeightBottomLeft = temp;
            } else
            if(face == 2)
            {
                int temp = vertexHeightTopLeft;
                vertexHeightTopLeft = vertexHeightBottomRight;
                vertexHeightBottomRight = temp;
                temp = vertexHeightTopRight;
                vertexHeightTopRight = vertexHeightBottomLeft;
                vertexHeightBottomLeft = temp;
            } else
            if(face == 3)
            {
                int temp = vertexHeightTopLeft;
                vertexHeightTopLeft = vertexHeightBottomLeft;
                vertexHeightBottomLeft = vertexHeightBottomRight;
                vertexHeightBottomRight = vertexHeightTopRight;
                vertexHeightTopRight = temp;
            }
        if(type == 4)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(4, 0, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, 0, 4, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face * 512, hash, ((Animable) (animable)), config, POWERS_OF_TWO[face]);
            return;
        }
        if(type == 5)
        {
            int offsetAmplifier = 16;
            int objectHash = worldController.getWallObjectHash(x, y, z);
            if(objectHash > 0)
                offsetAmplifier = GameObjectDefinition.getDefinition(objectHash >> 14 & 0x7fff).offsetAmplifier;
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(4, 0, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, 0, 4, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallDecoration(x, y, z, drawHeight, FACE_OFFSET_X[face] * offsetAmplifier, FACE_OFFSET_Y[face] * offsetAmplifier, face * 512, hash, ((Animable) (animable)), config, POWERS_OF_TWO[face]);
            return;
        }
        if(type == 6)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(4, 0, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, 0, 4, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((Animable) (animable)), config, 256);
            return;
        }
        if(type == 7)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(4, 0, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, 0, 4, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((Animable) (animable)), config, 512);
            return;
        }
        if(type == 8)
        {
            Animable animable;
            if(definition.animationId == -1 && definition.childIds == null)
                animable = definition.getModelAt(4, 0, vertexHeightBottomLeft, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightTopLeft, -1);
            else
                animable = new GameObject(objectId, 0, 4, vertexHeightBottomRight, vertexHeightTopRight, vertexHeightBottomLeft, vertexHeightTopLeft, definition.animationId, true);
            worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((Animable) (animable)), config, 768);
        }
    }

  public static boolean objectBlockCached(int blockX, byte[] blockData, int blockY
  ) //xxx bad method, decompiled with JODE
  {
    boolean cached = true;
    Stream stream = new Stream(blockData);
    int objectId = -1;
    for (;;)
      {
	int deltaId = stream.getSmartB ();
	if (deltaId == 0)
	  break;
	objectId += deltaId;
	int pos = 0;
	boolean foundInstance = false;
	for (;;)
	  {
	    if (foundInstance)
	      {
		int i_256_ = stream.getSmartB ();
		if (i_256_ == 0)
		  break;
		stream.getUnsignedByte();
	      }
	    else
	      {
		int deltaPos = stream.getSmartB ();
		if (deltaPos == 0)
		  break;
		pos += deltaPos - 1;
		int tileY = pos & 0x3f;
		int tileX = pos >> 6 & 0x3f;
		int objectType = stream.getUnsignedByte() >> 2;
		int objectX = tileX + blockX;
		int objectY = tileY + blockY;
		if (objectX > 0 && objectY > 0 && objectX < 103 && objectY < 103)
		  {
		    GameObjectDefinition definition = GameObjectDefinition.getDefinition (objectId);
		    if (objectType != 22 || !lowMem || definition.hasActions
                    || definition.unknownAttribute3)
		      {
			cached &= definition.modelCached ();
			foundInstance = true;
		      }
		  }
	      }
	  }
      }
    return cached;
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
    private final byte[][][] tileTypeLayer1;
    static int plane;
    private static int anInt133 = (int)(Math.random() * 33D) - 16;
    private final byte[][][] tileShadowIntensity;
    private final int[][][] tileCullingBitsets;
    private final byte[][][] tileShapeLayer1;
    private static final int FACE_OFFSET_X[] = {
        1, 0, -1, 0
    };
    private static final int anInt138 = 323;
    private final int[][] anIntArrayArray139;
    private static final int WALL_CORNER_ORIENTATION[] = {
        16, 32, 64, 128
    };
    private final byte[][][] tileTypeLayer0;
    private static final int FACE_OFFSET_Y[] = {
        0, -1, 0, 1
    };
    static int setZ = 99;
    private final int anInt146;
    private final int anInt147;
    private final byte[][][] tileOrientationLayer1;
    private final byte[][][] renderRuleFlags;
    static boolean lowMem = true;
    private static final int POWERS_OF_TWO[] = {
        1, 2, 4, 8
    };

}
