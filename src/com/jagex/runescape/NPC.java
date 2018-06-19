package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

/* 
 * This file was renamed as part of the 317refactor project.
 */

public final class NPC extends Entity {

	public EntityDefinition npcDefinition;

	NPC() {
	}

	private Model getChildModel() {
		if (super.animation >= 0 && super.animationDelay == 0) {
			int frameId2 = AnimationSequence.animations[super.animation].frame2Ids[super.currentAnimationFrame];
			int frameId1 = -1;
			if (super.queuedAnimationId >= 0 && super.queuedAnimationId != super.standAnimationId)
				frameId1 = AnimationSequence.animations[super.queuedAnimationId].frame2Ids[super.queuedAnimationFrame];
			return npcDefinition.getChildModel(frameId1, frameId2,
					AnimationSequence.animations[super.animation].flowControl);
		}
		int frameId2 = -1;
		if (super.queuedAnimationId >= 0)
			frameId2 = AnimationSequence.animations[super.queuedAnimationId].frame2Ids[super.queuedAnimationFrame];
		return npcDefinition.getChildModel(-1, frameId2, null);
	}

	@Override
	public Model getRotatedModel() {
		if (npcDefinition == null)
			return null;
		Model rotatedModel = getChildModel();
		if (rotatedModel == null)
			return null;
		super.height = rotatedModel.modelHeight;
		if (super.graphicId != -1 && super.currentAnimationId != -1) {
			SpotAnimation spotAnimation = SpotAnimation.cache[super.graphicId];
			Model animationModel = spotAnimation.getModel();
			if (animationModel != null) {
				int frameId = spotAnimation.sequences.frame2Ids[super.currentAnimationId];
				Model animatedModel = new Model(true, Animation.isNullFrame(frameId), false, animationModel);
				animatedModel.translate(0, -super.graphicHeight, 0);
				animatedModel.createBones();
				animatedModel.applyTransformation(frameId);
				animatedModel.triangleSkin = null;
				animatedModel.vertexSkin = null;
				if (spotAnimation.scaleXY != 128 || spotAnimation.scaleZ != 128)
					animatedModel.scaleT(spotAnimation.scaleXY, spotAnimation.scaleXY, spotAnimation.scaleZ);
				animatedModel.applyLighting(64 + spotAnimation.modelLightFalloff, 850 + spotAnimation.modelLightAmbient,
						-30, -50, -30, true);
				Model models[] = { rotatedModel, animatedModel };
				rotatedModel = new Model(models);
			}
		}
		if (npcDefinition.boundaryDimension == 1)
			rotatedModel.singleTile = true;
		return rotatedModel;
	}

	@Override
	public boolean isVisible() {
		return npcDefinition != null;
	}
}
