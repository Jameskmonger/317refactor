package com.jagex.runescape;

import com.jagex.runescape.collection.Cache;

public final class Player extends Entity {

	public int rights;

	private long aLong1697;

	public EntityDefinition npcAppearance;

	boolean preventRotation;

	final int[] bodyPartColour;

	public int team;

	private int gender;

	public String name;
	static Cache mruNodes = new Cache(260);
	public int combatLevel;
	public int headIcon;
	public int modifiedAppearanceStartTime;
	int modifiedAppearanceEndTime;
	int drawHeight2;
	boolean visible;
	int anInt1711;
	int drawHeight;
	int anInt1713;
	Model playerModel;
	public final int[] appearance;
	private long appearanceOffset;
	int localX;
	int localY;
	int playerTileHeight;
	int playerTileWidth;
	int skill;

	Player() {
		aLong1697 = -1L;
		preventRotation = false;
		bodyPartColour = new int[5];
		visible = false;
		appearance = new int[12];
	}

	private Model getAnimatedModel() {
		if (npcAppearance != null) {
			int frameId = -1;
			if (super.animation >= 0 && super.animationDelay == 0)
				frameId = AnimationSequence.animations[super.animation].primaryFrames[super.currentAnimationFrame];
			else if (super.queuedAnimationId >= 0)
				frameId = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
			Model model = npcAppearance.getChildModel(-1, frameId, null);
			return model;
		}
		long l = appearanceOffset;
		int k = -1;
		int i1 = -1;
		int j1 = -1;
		int k1 = -1;
		if (super.animation >= 0 && super.animationDelay == 0) {
			AnimationSequence animation = AnimationSequence.animations[super.animation];
			k = animation.primaryFrames[super.currentAnimationFrame];
			if (super.queuedAnimationId >= 0 && super.queuedAnimationId != super.standAnimationId)
				i1 = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
			if (animation.playerReplacementShield >= 0) {
				j1 = animation.playerReplacementShield;
				l += j1 - appearance[5] << 40;
			}
			if (animation.playerReplacementWeapon >= 0) {
				k1 = animation.playerReplacementWeapon;
				l += k1 - appearance[3] << 48;
			}
		} else if (super.queuedAnimationId >= 0)
			k = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
		Model model_1 = (Model) mruNodes.get(l);
		if (model_1 == null) {
			boolean flag = false;
			for (int i2 = 0; i2 < 12; i2++) {
				int k2 = appearance[i2];
				if (k1 >= 0 && i2 == 3)
					k2 = k1;
				if (j1 >= 0 && i2 == 5)
					k2 = j1;
				if (k2 >= 256 && k2 < 512 && !IdentityKit.cache[k2 - 256].bodyModelCached())
					flag = true;
				if (k2 >= 512 && !ItemDefinition.getDefinition(k2 - 512).equipModelCached(gender))
					flag = true;
			}

			if (flag) {
				if (aLong1697 != -1L)
					model_1 = (Model) mruNodes.get(aLong1697);
				if (model_1 == null)
					return null;
			}
		}
		if (model_1 == null) {
			Model models[] = new Model[12];
			int j2 = 0;
			for (int l2 = 0; l2 < 12; l2++) {
				int i3 = appearance[l2];
				if (k1 >= 0 && l2 == 3)
					i3 = k1;
				if (j1 >= 0 && l2 == 5)
					i3 = j1;
				if (i3 >= 256 && i3 < 512) {
					Model model_3 = IdentityKit.cache[i3 - 256].getBodyModel();
					if (model_3 != null)
						models[j2++] = model_3;
				}
				if (i3 >= 512) {
					Model model_4 = ItemDefinition.getDefinition(i3 - 512).getEquippedModel(gender);
					if (model_4 != null)
						models[j2++] = model_4;
				}
			}

			model_1 = new Model(j2, models);
			for (int part = 0; part < 5; part++)
				if (bodyPartColour[part] != 0) {
					model_1.recolour(Client.APPEARANCE_COLOURS[part][0],
							Client.APPEARANCE_COLOURS[part][bodyPartColour[part]]);
					if (part == 1)
						model_1.recolour(Client.BEARD_COLOURS[0], Client.BEARD_COLOURS[bodyPartColour[part]]);
				}

			model_1.createBones();
			model_1.applyLighting(64, 850, -30, -50, -30, true);
			mruNodes.put(model_1, l);
			aLong1697 = l;
		}
		if (preventRotation)
			return model_1;
		Model model_2 = Model.aModel_1621;
		model_2.replaceWithModel(model_1, Animation.isNullFrame(k) & Animation.isNullFrame(i1));
		if (k != -1 && i1 != -1)
			model_2.mixAnimationFrames(AnimationSequence.animations[super.animation].flowControl, i1, k);
		else if (k != -1)
			model_2.applyTransformation(k);
		model_2.calculateDiagonals();
		model_2.triangleSkin = null;
		model_2.vertexSkin = null;
		return model_2;
	}

	public Model getHeadModel() {
		if (!visible)
			return null;
		if (npcAppearance != null)
			return npcAppearance.getHeadModel();
		boolean flag = false;
		for (int i = 0; i < 12; i++) {
			int j = appearance[i];
			if (j >= 256 && j < 512 && !IdentityKit.cache[j - 256].headModelCached())
				flag = true;
			if (j >= 512 && !ItemDefinition.getDefinition(j - 512).isDialogueModelCached(gender))
				flag = true;
		}

		if (flag)
			return null;
		Model models[] = new Model[12];
		int k = 0;
		for (int l = 0; l < 12; l++) {
			int i1 = appearance[l];
			if (i1 >= 256 && i1 < 512) {
				Model model_1 = IdentityKit.cache[i1 - 256].getHeadModel();
				if (model_1 != null)
					models[k++] = model_1;
			}
			if (i1 >= 512) {
				Model model_2 = ItemDefinition.getDefinition(i1 - 512).getDialogueModel(gender);
				if (model_2 != null)
					models[k++] = model_2;
			}
		}

		Model model = new Model(k, models);
		for (int j1 = 0; j1 < 5; j1++)
			if (bodyPartColour[j1] != 0) {
				model.recolour(Client.APPEARANCE_COLOURS[j1][0], Client.APPEARANCE_COLOURS[j1][bodyPartColour[j1]]);
				if (j1 == 1)
					model.recolour(Client.BEARD_COLOURS[0], Client.BEARD_COLOURS[bodyPartColour[j1]]);
			}

		return model;
	}

	@Override
	public Model getRotatedModel() {
		if (!visible)
			return null;
		Model appearanceModel = getAnimatedModel();
		if (appearanceModel == null)
			return null;
		super.height = appearanceModel.modelHeight;
		appearanceModel.singleTile = true;
		if (preventRotation)
			return appearanceModel;
		if (super.graphicId != -1 && super.currentAnimationId != -1) {
			SpotAnimation animation = SpotAnimation.cache[super.graphicId];
			Model graphicModel = animation.getModel();
			if (graphicModel != null) {
				Model model = new Model(true, Animation.isNullFrame(super.currentAnimationId), false, graphicModel);
				model.translate(0, -super.graphicHeight, 0);
				model.createBones();
				model.applyTransformation(animation.sequences.primaryFrames[super.currentAnimationId]);
				model.triangleSkin = null;
				model.vertexSkin = null;
				if (animation.scaleXY != 128 || animation.scaleZ != 128)
					model.scaleT(animation.scaleXY, animation.scaleXY, animation.scaleZ);
				model.applyLighting(64 + animation.modelLightFalloff, 850 + animation.modelLightAmbient, -30, -50, -30,
						true);
				Model models[] = { appearanceModel, model };
				appearanceModel = new Model(models);
			}
		}
		if (playerModel != null) {
			if (Client.tick >= modifiedAppearanceEndTime)
				playerModel = null;
			if (Client.tick >= modifiedAppearanceStartTime && Client.tick < modifiedAppearanceEndTime) {
				Model model = playerModel;
				model.translate(anInt1711 - super.x, drawHeight - drawHeight2, anInt1713 - super.y);
				if (super.turnDirection == 512) {
					model.rotate90Degrees();
					model.rotate90Degrees();
					model.rotate90Degrees();
				} else if (super.turnDirection == 1024) {
					model.rotate90Degrees();
					model.rotate90Degrees();
				} else if (super.turnDirection == 1536)
					model.rotate90Degrees();
				Model models[] = { appearanceModel, model };
				appearanceModel = new Model(models);
				if (super.turnDirection == 512)
					model.rotate90Degrees();
				else if (super.turnDirection == 1024) {
					model.rotate90Degrees();
					model.rotate90Degrees();
				} else if (super.turnDirection == 1536) {
					model.rotate90Degrees();
					model.rotate90Degrees();
					model.rotate90Degrees();
				}
				model.translate(super.x - anInt1711, drawHeight2 - drawHeight, super.y - anInt1713);
			}
		}
		appearanceModel.singleTile = true;
		return appearanceModel;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void updatePlayerAppearance(Buffer stream) {
		stream.position = 0;
		gender = stream.getUnsignedByte();
		headIcon = stream.getUnsignedByte();
		npcAppearance = null;
		this.team = 0;
		for (int slot = 0; slot < 12; slot++) {
			int itemId1 = stream.getUnsignedByte();
			if (itemId1 == 0) {
				appearance[slot] = 0;
				continue;
			}
			int itemId2 = stream.getUnsignedByte();
			appearance[slot] = (itemId1 << 8) + itemId2;
			if (slot == 0 && appearance[0] == 65535) {
				npcAppearance = EntityDefinition.getDefinition(stream.getUnsignedLEShort());
				break;
			}
			if (appearance[slot] >= 512 && appearance[slot] - 512 < ItemDefinition.itemCount) {
				int team = ItemDefinition.getDefinition(appearance[slot] - 512).teamId;
				if (team != 0)
					this.team = team;
			}
		}

		for (int bodyPart = 0; bodyPart < 5; bodyPart++) {
			int colour = stream.getUnsignedByte();
			if (colour < 0 || colour >= Client.APPEARANCE_COLOURS[bodyPart].length)
				colour = 0;
			bodyPartColour[bodyPart] = colour;
		}

		super.standAnimationId = stream.getUnsignedLEShort();
		if (super.standAnimationId == 65535)
			super.standAnimationId = -1;
		super.standTurnAnimationId = stream.getUnsignedLEShort();
		if (super.standTurnAnimationId == 65535)
			super.standTurnAnimationId = -1;
		super.walkAnimationId = stream.getUnsignedLEShort();
		if (super.walkAnimationId == 65535)
			super.walkAnimationId = -1;
		super.turnAboutAnimationId = stream.getUnsignedLEShort();
		if (super.turnAboutAnimationId == 65535)
			super.turnAboutAnimationId = -1;
		super.turnRightAnimationId = stream.getUnsignedLEShort();
		if (super.turnRightAnimationId == 65535)
			super.turnRightAnimationId = -1;
		super.turnLeftAnimationId = stream.getUnsignedLEShort();
		if (super.turnLeftAnimationId == 65535)
			super.turnLeftAnimationId = -1;
		super.runAnimationId = stream.getUnsignedLEShort();
		if (super.runAnimationId == 65535)
			super.runAnimationId = -1;
		name = TextClass.formatName(TextClass.longToName(stream.getLong()));
		combatLevel = stream.getUnsignedByte();
		skill = stream.getUnsignedLEShort();
		visible = true;
		appearanceOffset = 0L;
		for (int slot = 0; slot < 12; slot++) {
			appearanceOffset <<= 4;
			if (appearance[slot] >= 256)
				appearanceOffset += appearance[slot] - 256;
		}

		if (appearance[0] >= 256)
			appearanceOffset += appearance[0] - 256 >> 4;
		if (appearance[1] >= 256)
			appearanceOffset += appearance[1] - 256 >> 8;
		for (int bodyPart = 0; bodyPart < 5; bodyPart++) {
			appearanceOffset <<= 3;
			appearanceOffset += bodyPartColour[bodyPart];
		}

		appearanceOffset <<= 1;
		appearanceOffset += gender;
	}
}