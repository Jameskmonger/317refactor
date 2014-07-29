// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Player extends Entity
{

    public Model getRotatedModel()
    {
        if(!visible)
            return null;
        Model model = method452();
        if(model == null)
            return null;
        super.height = model.modelHeight;
        model.singleTile = true;
        if(preventRotation)
            return model;
        if(super.graphicId != -1 && super.currentAnimation != -1)
        {
            SpotAnim spotAnim = SpotAnim.cache[super.graphicId];
            Model model_2 = spotAnim.getModel();
            if(model_2 != null)
            {
                Model model_3 = new Model(true, Class36.isNullFrame(super.currentAnimation), false, model_2);
                model_3.translate(0, -super.graphicHeight, 0);
                model_3.createBones();
                model_3.applyTransformation(spotAnim.sequences.frame2Ids[super.currentAnimation]);
                model_3.triangleSkin = null;
                model_3.vertexSkin = null;
                if(spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
                    model_3.scaleT(spotAnim.resizeXY, spotAnim.resizeXY, spotAnim.resizeZ);
                model_3.applyLighting(64 + spotAnim.modelLightFalloff, 850 + spotAnim.modelLightAmbient, -30, -50, -30, true);
                Model aclass30_sub2_sub4_sub6_1s[] = {
                        model, model_3
                };
                model = new Model(aclass30_sub2_sub4_sub6_1s);
            }
        }
        if(playerModel != null)
        {
            if(client.loopCycle >= anInt1708)
                playerModel = null;
            if(client.loopCycle >= anInt1707 && client.loopCycle < anInt1708)
            {
                Model model_1 = playerModel;
                model_1.translate(anInt1711 - super.x, drawHeight - drawHeight2, anInt1713 - super.y);
                if(super.turnDirection == 512)
                {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else
                if(super.turnDirection == 1024)
                {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else
                if(super.turnDirection == 1536)
                    model_1.rotate90Degrees();
                Model aclass30_sub2_sub4_sub6s[] = {
                        model, model_1
                };
                model = new Model(aclass30_sub2_sub4_sub6s);
                if(super.turnDirection == 512)
                    model_1.rotate90Degrees();
                else
                if(super.turnDirection == 1024)
                {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                } else
                if(super.turnDirection == 1536)
                {
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                    model_1.rotate90Degrees();
                }
                model_1.translate(super.x - anInt1711, drawHeight2 - drawHeight, super.y - anInt1713);
            }
        }
        model.singleTile = true;
        return model;
    }

    public void updatePlayerAppearance(Stream stream)
    {
        stream.currentOffset = 0;
        gender = stream.getUnsignedByte();
        headIcon = stream.getUnsignedByte();
        npcAppearance = null;
        this.team = 0;
        for(int slot = 0; slot < 12; slot++)
        {
            int itemId1 = stream.getUnsignedByte();
            if(itemId1 == 0)
            {
                appearance[slot] = 0;
                continue;
            }
            int itemId2 = stream.getUnsignedByte();
            appearance[slot] = (itemId1 << 8) + itemId2;
            if(slot == 0 && appearance[0] == 65535)
            {
                npcAppearance = EntityDef.forID(stream.getUnsignedLEShort());
                break;
            }
            if(appearance[slot] >= 512 && appearance[slot] - 512 < ItemDef.totalItems)
            {
                int team = ItemDef.forID(appearance[slot] - 512).team;
                if(team != 0)
                    this.team = team;
            }
        }

        for(int bodyPart = 0; bodyPart < 5; bodyPart++)
        {
            int colour = stream.getUnsignedByte();
            if(colour < 0 || colour >= client.anIntArrayArray1003[bodyPart].length)
                colour = 0;
            bodyPartColour[bodyPart] = colour;
        }

        super.standAnimationId = stream.getUnsignedLEShort();
        if(super.standAnimationId == 65535)
            super.standAnimationId = -1;
        super.standTurnAnimationId = stream.getUnsignedLEShort();
        if(super.standTurnAnimationId == 65535)
            super.standTurnAnimationId = -1;
        super.walkAnimationId = stream.getUnsignedLEShort();
        if(super.walkAnimationId == 65535)
            super.walkAnimationId = -1;
        super.turnAboutAnimationId = stream.getUnsignedLEShort();
        if(super.turnAboutAnimationId == 65535)
            super.turnAboutAnimationId = -1;
        super.turnRightAnimationId = stream.getUnsignedLEShort();
        if(super.turnRightAnimationId == 65535)
            super.turnRightAnimationId = -1;
        super.turnLeftAnimationId = stream.getUnsignedLEShort();
        if(super.turnLeftAnimationId == 65535)
            super.turnLeftAnimationId = -1;
        super.runAnimationId = stream.getUnsignedLEShort();
        if(super.runAnimationId == 65535)
            super.runAnimationId = -1;
        name = TextClass.formatName(TextClass.nameForLong(stream.getLong()));
        combatLevel = stream.getUnsignedByte();
        skill = stream.getUnsignedLEShort();
        visible = true;
        appearanceOffset = 0L;
        for(int slot = 0; slot < 12; slot++)
        {
            appearanceOffset <<= 4;
            if(appearance[slot] >= 256)
                appearanceOffset += appearance[slot] - 256;
        }

        if(appearance[0] >= 256)
            appearanceOffset += appearance[0] - 256 >> 4;
        if(appearance[1] >= 256)
            appearanceOffset += appearance[1] - 256 >> 8;
        for(int bodyPart = 0; bodyPart < 5; bodyPart++)
        {
            appearanceOffset <<= 3;
            appearanceOffset += bodyPartColour[bodyPart];
        }

        appearanceOffset <<= 1;
        appearanceOffset += gender;
    }

    private Model method452()
    {
        if(npcAppearance != null)
        {
            int j = -1;
            if(super.animation >= 0 && super.animationDelay == 0)
                j = Animation.anims[super.animation].frame2Ids[super.anInt1527];
            else
            if(super.anInt1517 >= 0)
                j = Animation.anims[super.anInt1517].frame2Ids[super.anInt1518];
            Model model = npcAppearance.method164(-1, j, null);
            return model;
        }
        long l = appearanceOffset;
        int k = -1;
        int i1 = -1;
        int j1 = -1;
        int k1 = -1;
        if(super.animation >= 0 && super.animationDelay == 0)
        {
            Animation animation = Animation.anims[super.animation];
            k = animation.frame2Ids[super.anInt1527];
            if(super.anInt1517 >= 0 && super.anInt1517 != super.standAnimationId)
                i1 = Animation.anims[super.anInt1517].frame2Ids[super.anInt1518];
            if(animation.anInt360 >= 0)
            {
                j1 = animation.anInt360;
                l += j1 - appearance[5] << 40;
            }
            if(animation.anInt361 >= 0)
            {
                k1 = animation.anInt361;
                l += k1 - appearance[3] << 48;
            }
        } else
        if(super.anInt1517 >= 0)
            k = Animation.anims[super.anInt1517].frame2Ids[super.anInt1518];
        Model model_1 = (Model) mruNodes.insertFromCache(l);
        if(model_1 == null)
        {
            boolean flag = false;
            for(int i2 = 0; i2 < 12; i2++)
            {
                int k2 = appearance[i2];
                if(k1 >= 0 && i2 == 3)
                    k2 = k1;
                if(j1 >= 0 && i2 == 5)
                    k2 = j1;
                if(k2 >= 256 && k2 < 512 && !IDK.cache[k2 - 256].method537())
                    flag = true;
                if(k2 >= 512 && !ItemDef.forID(k2 - 512).method195(gender))
                    flag = true;
            }

            if(flag)
            {
                if(aLong1697 != -1L)
                    model_1 = (Model) mruNodes.insertFromCache(aLong1697);
                if(model_1 == null)
                    return null;
            }
        }
        if(model_1 == null)
        {
            Model aclass30_sub2_sub4_sub6s[] = new Model[12];
            int j2 = 0;
            for(int l2 = 0; l2 < 12; l2++)
            {
                int i3 = appearance[l2];
                if(k1 >= 0 && l2 == 3)
                    i3 = k1;
                if(j1 >= 0 && l2 == 5)
                    i3 = j1;
                if(i3 >= 256 && i3 < 512)
                {
                    Model model_3 = IDK.cache[i3 - 256].method538();
                    if(model_3 != null)
                        aclass30_sub2_sub4_sub6s[j2++] = model_3;
                }
                if(i3 >= 512)
                {
                    Model model_4 = ItemDef.forID(i3 - 512).method196(gender);
                    if(model_4 != null)
                        aclass30_sub2_sub4_sub6s[j2++] = model_4;
                }
            }

            model_1 = new Model(j2, aclass30_sub2_sub4_sub6s);
            for(int j3 = 0; j3 < 5; j3++)
                if(bodyPartColour[j3] != 0)
                {
                    model_1.recolour(client.anIntArrayArray1003[j3][0], client.anIntArrayArray1003[j3][bodyPartColour[j3]]);
                    if(j3 == 1)
                        model_1.recolour(client.anIntArray1204[0], client.anIntArray1204[bodyPartColour[j3]]);
                }

            model_1.createBones();
            model_1.applyLighting(64, 850, -30, -50, -30, true);
            mruNodes.removeFromCache(model_1, l);
            aLong1697 = l;
        }
        if(preventRotation)
            return model_1;
        Model model_2 = Model.aModel_1621;
        model_2.replaceWithModel(model_1, Class36.isNullFrame(k) & Class36.isNullFrame(i1));
        if(k != -1 && i1 != -1)
            model_2.mixAnimationFrames(Animation.anims[super.animation].anIntArray357, i1, k);
        else
        if(k != -1)
            model_2.applyTransformation(k);
        model_2.calculateDiagonals();
        model_2.triangleSkin = null;
        model_2.vertexSkin = null;
        return model_2;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public int rights;
	public Model method453()
    {
        if(!visible)
            return null;
        if(npcAppearance != null)
            return npcAppearance.method160();
        boolean flag = false;
        for(int i = 0; i < 12; i++)
        {
            int j = appearance[i];
            if(j >= 256 && j < 512 && !IDK.cache[j - 256].method539())
                flag = true;
            if(j >= 512 && !ItemDef.forID(j - 512).method192(gender))
                flag = true;
        }

        if(flag)
            return null;
        Model aclass30_sub2_sub4_sub6s[] = new Model[12];
        int k = 0;
        for(int l = 0; l < 12; l++)
        {
            int i1 = appearance[l];
            if(i1 >= 256 && i1 < 512)
            {
                Model model_1 = IDK.cache[i1 - 256].method540();
                if(model_1 != null)
                    aclass30_sub2_sub4_sub6s[k++] = model_1;
            }
            if(i1 >= 512)
            {
                Model model_2 = ItemDef.forID(i1 - 512).method194(gender);
                if(model_2 != null)
                    aclass30_sub2_sub4_sub6s[k++] = model_2;
            }
        }

        Model model = new Model(k, aclass30_sub2_sub4_sub6s);
        for(int j1 = 0; j1 < 5; j1++)
            if(bodyPartColour[j1] != 0)
            {
                model.recolour(client.anIntArrayArray1003[j1][0], client.anIntArrayArray1003[j1][bodyPartColour[j1]]);
                if(j1 == 1)
                    model.recolour(client.anIntArray1204[0], client.anIntArray1204[bodyPartColour[j1]]);
            }

        return model;
    }

    Player()
    {
        aLong1697 = -1L;
        preventRotation = false;
        bodyPartColour = new int[5];
        visible = false;
        anInt1715 = 9;
        appearance = new int[12];
    }

    private long aLong1697;
    public EntityDef npcAppearance;
    boolean preventRotation;
    final int[] bodyPartColour;
    public int team;
    private int gender;
    public String name;
    static MRUNodes mruNodes = new MRUNodes(260);
    public int combatLevel;
    public int headIcon;
    public int anInt1707;
    int anInt1708;
    int drawHeight2;
    boolean visible;
    int anInt1711;
    int drawHeight;
    int anInt1713;
    Model playerModel;
    private int anInt1715;
    public final int[] appearance;
    private long appearanceOffset;
    int localX;
    int localY;
    int playerTileHeight;
    int playerTileWidth;
    int skill;

}
