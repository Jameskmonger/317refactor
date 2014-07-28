// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class ObjectInstance extends Animable {

    public Model getRotatedModel()
    {
        int j = -1;
        if(animation != null)
        {
            int k = client.loopCycle - nextFrameTime;
            if(k > 100 && animation.frameStep > 0)
                k = 100;
            while(k > animation.getFrameLength(frame))
            {
                k -= animation.getFrameLength(frame);
                frame++;
                if(frame < animation.frameCount)
                    continue;
                frame -= animation.frameStep;
                if(frame >= 0 && frame < animation.frameCount)
                    continue;
                animation = null;
                break;
            }
            nextFrameTime = client.loopCycle - k;
            if(animation != null)
                j = animation.frame2Ids[frame];
        }
        ObjectDef class46;
        if(anIntArray1600 != null)
            class46 = method457();
        else
            class46 = ObjectDef.forID(objectId);
        if(class46 == null)
        {
            return null;
        } else
        {
            return class46.getModelAt(type, orientation, anInt1603, anInt1604, anInt1605, anInt1606, j);
        }
    }

    private ObjectDef method457()
    {
        int i = -1;
        if(anInt1601 != -1)
        {
            VarBit varBit = VarBit.cache[anInt1601];
            int k = varBit.configId;
            int l = varBit.leastSignificantBit;
            int i1 = varBit.mostSignificantBit;
            int j1 = client.anIntArray1232[i1 - l];
            i = clientInstance.variousSettings[k] >> l & j1;
        } else
        if(anInt1602 != -1)
            i = clientInstance.variousSettings[anInt1602];
        if(i < 0 || i >= anIntArray1600.length || anIntArray1600[i] == -1)
            return null;
        else
            return ObjectDef.forID(anIntArray1600[i]);
    }

    public ObjectInstance(int objectId, int orientation, int type, int l, int i1, int j1,
                         int k1, int animationId, boolean flag)
    {
        this.objectId = objectId;
        this.type = type;
        this.orientation = orientation;
        anInt1603 = j1;
        anInt1604 = l;
        anInt1605 = i1;
        anInt1606 = k1;
        if(animationId != -1)
        {
            animation = Animation.anims[animationId];
            frame = 0;
            nextFrameTime = client.loopCycle;
            if(flag && animation.frameStep != -1)
            {
                frame = (int)(Math.random() * (double) animation.frameCount);
                nextFrameTime -= (int)(Math.random() * (double) animation.getFrameLength(frame));
            }
        }
        ObjectDef objectDef = ObjectDef.forID(this.objectId);
        anInt1601 = objectDef.anInt774;
        anInt1602 = objectDef.anInt749;
        anIntArray1600 = objectDef.childrenIDs;
    }

    private int frame;
    private final int[] anIntArray1600;
    private final int anInt1601;
    private final int anInt1602;
    private final int anInt1603;
    private final int anInt1604;
    private final int anInt1605;
    private final int anInt1606;
    private Animation animation;
    private int nextFrameTime;
    public static client clientInstance;
    private final int objectId;
    private final int type;
    private final int orientation;
}
