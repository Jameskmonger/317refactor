// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class NPC extends Entity
{

    private Model method450()
    {
        if(super.anim >= 0 && super.anInt1529 == 0)
        {
            int k = Animation.anims[super.anim].frame2Ids[super.anInt1527];
            int i1 = -1;
            if(super.anInt1517 >= 0 && super.anInt1517 != super.anInt1511)
                i1 = Animation.anims[super.anInt1517].frame2Ids[super.anInt1518];
            return desc.method164(i1, k, Animation.anims[super.anim].anIntArray357);
        }
        int l = -1;
        if(super.anInt1517 >= 0)
            l = Animation.anims[super.anInt1517].frame2Ids[super.anInt1518];
        return desc.method164(-1, l, null);
    }

    public Model getRotatedModel()
    {
        if(desc == null)
            return null;
        Model model = method450();
        if(model == null)
            return null;
        super.height = model.modelHeight;
        if(super.anInt1520 != -1 && super.anInt1521 != -1)
        {
            SpotAnim spotAnim = SpotAnim.cache[super.anInt1520];
            Model model_1 = spotAnim.getModel();
            if(model_1 != null)
            {
                int j = spotAnim.sequences.frame2Ids[super.anInt1521];
                Model model_2 = new Model(true, Class36.isNullFrame(j), false, model_1);
                model_2.translate(0, -super.anInt1524, 0);
                model_2.createBones();
                model_2.applyTransformation(j);
                model_2.triangleSkin = null;
                model_2.vertexSkin = null;
                if(spotAnim.anInt410 != 128 || spotAnim.anInt411 != 128)
                    model_2.scaleT(spotAnim.anInt410, spotAnim.anInt410, spotAnim.anInt411);
                model_2.applyLighting(64 + spotAnim.modelLightFalloff, 850 + spotAnim.modelLightAmbient, -30, -50, -30, true);
                Model aModel[] = {
                        model, model_2
                };
                model = new Model(aModel);
            }
        }
        if(desc.aByte68 == 1)
            model.singleTile = true;
        return model;
    }

    public boolean isVisible()
    {
        return desc != null;
    }

    NPC()
    {
    }

    public EntityDef desc;
}
