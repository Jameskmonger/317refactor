package com.jagex.runescape;

import com.jagex.runescape.definition.AnimationSequence;
import com.jagex.runescape.definition.EntityDefinition;
import com.jagex.runescape.definition.SpotAnimation;

public final class NPC extends Entity {

    public EntityDefinition npcDefinition;

    NPC() {
    }

    private Model getChildModel() {
        if (super.animation >= 0 && super.animationDelay == 0) {
            final int frameId2 = AnimationSequence.animations[super.animation].primaryFrames[super.currentAnimationFrame];
            int frameId1 = -1;
            if (super.queuedAnimationId >= 0 && super.queuedAnimationId != super.standAnimationId) {
                frameId1 = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
            }
            return this.npcDefinition.getChildModel(frameId1, frameId2,
                AnimationSequence.animations[super.animation].flowControl);
        }
        int frameId2 = -1;
        if (super.queuedAnimationId >= 0) {
            frameId2 = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
        }
        return this.npcDefinition.getChildModel(-1, frameId2, null);
    }

    @Override
    public Model getRotatedModel() {
        if (this.npcDefinition == null) {
            return null;
        }
        Model rotatedModel = this.getChildModel();
        if (rotatedModel == null) {
            return null;
        }
        super.height = rotatedModel.modelHeight;
        if (super.graphicId != -1 && super.currentAnimationId != -1) {
            final SpotAnimation spotAnimation = SpotAnimation.cache[super.graphicId];
            final Model animationModel = spotAnimation.getModel();
            if (animationModel != null) {
                final int frameId = spotAnimation.sequences.primaryFrames[super.currentAnimationId];
                final Model animatedModel = new Model(true, Animation.isNullFrame(frameId), false, animationModel);
                animatedModel.translate(0, -super.graphicHeight, 0);
                animatedModel.createBones();
                animatedModel.applyTransformation(frameId);
                animatedModel.triangleSkin = null;
                animatedModel.vertexSkin = null;
                if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128) {
                    animatedModel.scaleT(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
                }
                animatedModel.applyLighting(64 + spotAnimation.modelLightFalloff, 850 + spotAnimation.modelLightAmbient,
                    -30, -50, -30, true);
                final Model[] models = {rotatedModel, animatedModel};
                rotatedModel = new Model(models);
            }
        }
        if (this.npcDefinition.boundaryDimension == 1) {
            rotatedModel.singleTile = true;
        }
        return rotatedModel;
    }

    @Override
    public boolean isVisible() {
        return this.npcDefinition != null;
    }
}
