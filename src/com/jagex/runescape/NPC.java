package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class NPC extends Entity
{

    private Model method450()
    {
        if(super.animation >= 0 && super.animationDelay == 0)
        {
            int k = AnimationSequence.animations[super.animation].frame2Ids[super.currentAnimationFrame];
            int i1 = -1;
            if(super.queuedAnimationId >= 0 && super.queuedAnimationId != super.standAnimationId)
                i1 = AnimationSequence.animations[super.queuedAnimationId].frame2Ids[super.queuedAnimationFrame];
            return npcDefinition.getChildModel(i1, k, AnimationSequence.animations[super.animation].flowControl);
        }
        int l = -1;
        if(super.queuedAnimationId >= 0)
            l = AnimationSequence.animations[super.queuedAnimationId].frame2Ids[super.queuedAnimationFrame];
        return npcDefinition.getChildModel(-1, l, null);
    }

    public Model getRotatedModel()
    {
        if(npcDefinition == null)
            return null;
        Model model = method450();
        if(model == null)
            return null;
        super.height = model.modelHeight;
        if(super.graphicId != -1 && super.currentAnimationId != -1)
        {
            SpotAnimation spotAnim = SpotAnimation.cache[super.graphicId];
            Model model_1 = spotAnim.getModel();
            if(model_1 != null)
            {
                int j = spotAnim.sequences.frame2Ids[super.currentAnimationId];
                Model model_2 = new Model(true, Animation.isNullFrame(j), false, model_1);
                model_2.translate(0, -super.graphicHeight, 0);
                model_2.createBones();
                model_2.applyTransformation(j);
                model_2.triangleSkin = null;
                model_2.vertexSkin = null;
                if(spotAnim.scaleXY != 128 || spotAnim.scaleZ != 128)
                    model_2.scaleT(spotAnim.scaleXY, spotAnim.scaleXY, spotAnim.scaleZ);
                model_2.applyLighting(64 + spotAnim.modelLightFalloff, 850 + spotAnim.modelLightAmbient, -30, -50, -30, true);
                Model aModel[] = {
                        model, model_2
                };
                model = new Model(aModel);
            }
        }
        if(npcDefinition.boundaryDimension == 1)
            model.singleTile = true;
        return model;
    }

    public boolean isVisible()
    {
        return npcDefinition != null;
    }

    NPC()
    {
    }

    public EntityDefinition npcDefinition;
}
