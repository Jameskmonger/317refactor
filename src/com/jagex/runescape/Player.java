package com.jagex.runescape;

import com.jagex.runescape.collection.Cache;
import com.jagex.runescape.definition.*;

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
        this.aLong1697 = -1L;
        this.preventRotation = false;
        this.bodyPartColour = new int[5];
        this.visible = false;
        this.appearance = new int[12];
    }

    private Model getAnimatedModel() {
        if (this.npcAppearance != null) {
            int frameId = -1;
            if (super.animation >= 0 && super.animationDelay == 0) {
                frameId = AnimationSequence.animations[super.animation].primaryFrames[super.currentAnimationFrame];
            } else if (super.queuedAnimationId >= 0) {
                frameId = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
            }
            return this.npcAppearance.getChildModel(-1, frameId, null);
        }
        long l = this.appearanceOffset;
        int k = -1;
        int i1 = -1;
        int j1 = -1;
        int k1 = -1;
        if (super.animation >= 0 && super.animationDelay == 0) {
            final AnimationSequence animation = AnimationSequence.animations[super.animation];
            k = animation.primaryFrames[super.currentAnimationFrame];
            if (super.queuedAnimationId >= 0 && super.queuedAnimationId != super.standAnimationId) {
                i1 = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
            }
            if (animation.playerReplacementShield >= 0) {
                j1 = animation.playerReplacementShield;
                l += (long) j1 - this.appearance[5] << 40;
            }
            if (animation.playerReplacementWeapon >= 0) {
                k1 = animation.playerReplacementWeapon;
                l += (long) k1 - this.appearance[3] << 48;
            }
        } else if (super.queuedAnimationId >= 0) {
            k = AnimationSequence.animations[super.queuedAnimationId].primaryFrames[super.queuedAnimationFrame];
        }
        Model model_1 = (Model) mruNodes.get(l);
        if (model_1 == null) {
            boolean flag = false;
            for (int i2 = 0; i2 < 12; i2++) {
                int k2 = this.appearance[i2];
                if (k1 >= 0 && i2 == 3) {
                    k2 = k1;
                }
                if (j1 >= 0 && i2 == 5) {
                    k2 = j1;
                }
                if (k2 >= 256 && k2 < 512 && !IdentityKit.cache[k2 - 256].bodyModelCached()) {
                    flag = true;
                }
                if (k2 >= 512 && !ItemDefinition.getDefinition(k2 - 512).equipModelCached(this.gender)) {
                    flag = true;
                }
            }

            if (flag) {
                if (this.aLong1697 != -1L) {
                    model_1 = (Model) mruNodes.get(this.aLong1697);
                }
                if (model_1 == null) {
                    return null;
                }
            }
        }
        if (model_1 == null) {
            final Model[] models = new Model[12];
            int j2 = 0;
            for (int l2 = 0; l2 < 12; l2++) {
                int i3 = this.appearance[l2];
                if (k1 >= 0 && l2 == 3) {
                    i3 = k1;
                }
                if (j1 >= 0 && l2 == 5) {
                    i3 = j1;
                }
                if (i3 >= 256 && i3 < 512) {
                    final Model model_3 = IdentityKit.cache[i3 - 256].getBodyModel();
                    if (model_3 != null) {
                        models[j2++] = model_3;
                    }
                }
                if (i3 >= 512) {
                    final Model model_4 = ItemDefinition.getDefinition(i3 - 512).getEquippedModel(this.gender);
                    if (model_4 != null) {
                        models[j2++] = model_4;
                    }
                }
            }

            model_1 = new Model(j2, models);
            for (int part = 0; part < 5; part++) {
                if (this.bodyPartColour[part] != 0) {
                    model_1.recolour(Client.APPEARANCE_COLOURS[part][0],
                        Client.APPEARANCE_COLOURS[part][this.bodyPartColour[part]]);
                    if (part == 1) {
                        model_1.recolour(Client.BEARD_COLOURS[0], Client.BEARD_COLOURS[this.bodyPartColour[part]]);
                    }
                }
            }

            model_1.createBones();
            model_1.applyLighting(64, 850, -30, -50, -30, true);
            mruNodes.put(model_1, l);
            this.aLong1697 = l;
        }
        if (this.preventRotation) {
            return model_1;
        }
        final Model model_2 = Model.aModel_1621;
        model_2.replaceWithModel(model_1, Animation.isNullFrame(k) & Animation.isNullFrame(i1));
        if (k != -1 && i1 != -1) {
            model_2.mixAnimationFrames(AnimationSequence.animations[super.animation].flowControl, i1, k);
        } else if (k != -1) {
            model_2.applyTransformation(k);
        }
        model_2.calculateDiagonals();
        model_2.triangleSkin = null;
        model_2.vertexSkin = null;
        return model_2;
    }

    public Model getHeadModel() {
        if (!this.visible) {
            return null;
        }
        if (this.npcAppearance != null) {
            return this.npcAppearance.getHeadModel();
        }
        boolean flag = false;
        for (int i = 0; i < 12; i++) {
            final int j = this.appearance[i];
            if (j >= 256 && j < 512 && !IdentityKit.cache[j - 256].headModelCached()) {
                flag = true;
            }
            if (j >= 512 && !ItemDefinition.getDefinition(j - 512).isDialogueModelCached(this.gender)) {
                flag = true;
            }
        }

        if (flag) {
            return null;
        }
        final Model[] models = new Model[12];
        int k = 0;
        for (int l = 0; l < 12; l++) {
            final int i1 = this.appearance[l];
            if (i1 >= 256 && i1 < 512) {
                final Model model_1 = IdentityKit.cache[i1 - 256].getHeadModel();
                if (model_1 != null) {
                    models[k++] = model_1;
                }
            }
            if (i1 >= 512) {
                final Model model_2 = ItemDefinition.getDefinition(i1 - 512).getDialogueModel(this.gender);
                if (model_2 != null) {
                    models[k++] = model_2;
                }
            }
        }

        final Model model = new Model(k, models);
        for (int j1 = 0; j1 < 5; j1++) {
            if (this.bodyPartColour[j1] != 0) {
                model.recolour(Client.APPEARANCE_COLOURS[j1][0], Client.APPEARANCE_COLOURS[j1][this.bodyPartColour[j1]]);
                if (j1 == 1) {
                    model.recolour(Client.BEARD_COLOURS[0], Client.BEARD_COLOURS[this.bodyPartColour[j1]]);
                }
            }
        }

        return model;
    }

    @Override
    public Model getRotatedModel() {
        if (!this.visible) {
            return null;
        }
        Model appearanceModel = this.getAnimatedModel();
        if (appearanceModel == null) {
            return null;
        }
        super.height = appearanceModel.modelHeight;
        appearanceModel.singleTile = true;
        if (this.preventRotation) {
            return appearanceModel;
        }
        if (super.graphicId != -1 && super.currentAnimationId != -1) {
            final SpotAnimation animation = SpotAnimation.cache[super.graphicId];
            final Model graphicModel = animation.getModel();
            if (graphicModel != null) {
                final Model model = new Model(true, Animation.isNullFrame(super.currentAnimationId), false, graphicModel);
                model.translate(0, -super.graphicHeight, 0);
                model.createBones();
                model.applyTransformation(animation.sequences.primaryFrames[super.currentAnimationId]);
                model.triangleSkin = null;
                model.vertexSkin = null;
                if (animation.scaleXY != 128 || animation.scaleZ != 128) {
                    model.scaleT(animation.scaleXY, animation.scaleXY, animation.scaleZ);
                }
                model.applyLighting(64 + animation.modelLightFalloff, 850 + animation.modelLightAmbient, -30, -50, -30,
                    true);
                final Model[] models = {appearanceModel, model};
                appearanceModel = new Model(models);
            }
        }
        if (this.playerModel != null) {
            if (Client.tick >= this.modifiedAppearanceEndTime) {
                this.playerModel = null;
            }
            if (Client.tick >= this.modifiedAppearanceStartTime && Client.tick < this.modifiedAppearanceEndTime) {
                final Model model = this.playerModel;
                model.translate(this.anInt1711 - super.x, this.drawHeight - this.drawHeight2, this.anInt1713 - super.y);
                if (super.turnDirection == 512) {
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                } else if (super.turnDirection == 1024) {
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                } else if (super.turnDirection == 1536) {
                    model.rotate90Degrees();
                }
                final Model[] models = {appearanceModel, model};
                appearanceModel = new Model(models);
                if (super.turnDirection == 512) {
                    model.rotate90Degrees();
                } else if (super.turnDirection == 1024) {
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                } else if (super.turnDirection == 1536) {
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                    model.rotate90Degrees();
                }
                model.translate(super.x - this.anInt1711, this.drawHeight2 - this.drawHeight, super.y - this.anInt1713);
            }
        }
        appearanceModel.singleTile = true;
        return appearanceModel;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    public void updatePlayerAppearance(final Buffer stream) {
        stream.position = 0;
        this.gender = stream.getUnsignedByte();
        this.headIcon = stream.getUnsignedByte();
        this.npcAppearance = null;
        this.team = 0;
        for (int slot = 0; slot < 12; slot++) {
            final int itemId1 = stream.getUnsignedByte();
            if (itemId1 == 0) {
                this.appearance[slot] = 0;
                continue;
            }
            final int itemId2 = stream.getUnsignedByte();
            this.appearance[slot] = (itemId1 << 8) + itemId2;
            if (slot == 0 && this.appearance[0] == 65535) {
                this.npcAppearance = EntityDefinition.getDefinition(stream.getUnsignedLEShort());
                break;
            }
            if (this.appearance[slot] >= 512 && this.appearance[slot] - 512 < ItemDefinition.itemCount) {
                final int team = ItemDefinition.getDefinition(this.appearance[slot] - 512).teamId;
                if (team != 0) {
                    this.team = team;
                }
            }
        }

        for (int bodyPart = 0; bodyPart < 5; bodyPart++) {
            int colour = stream.getUnsignedByte();
            if (colour < 0 || colour >= Client.APPEARANCE_COLOURS[bodyPart].length) {
                colour = 0;
            }
            this.bodyPartColour[bodyPart] = colour;
        }

        super.standAnimationId = stream.getUnsignedLEShort();
        if (super.standAnimationId == 65535) {
            super.standAnimationId = -1;
        }
        super.standTurnAnimationId = stream.getUnsignedLEShort();
        if (super.standTurnAnimationId == 65535) {
            super.standTurnAnimationId = -1;
        }
        super.walkAnimationId = stream.getUnsignedLEShort();
        if (super.walkAnimationId == 65535) {
            super.walkAnimationId = -1;
        }
        super.turnAboutAnimationId = stream.getUnsignedLEShort();
        if (super.turnAboutAnimationId == 65535) {
            super.turnAboutAnimationId = -1;
        }
        super.turnRightAnimationId = stream.getUnsignedLEShort();
        if (super.turnRightAnimationId == 65535) {
            super.turnRightAnimationId = -1;
        }
        super.turnLeftAnimationId = stream.getUnsignedLEShort();
        if (super.turnLeftAnimationId == 65535) {
            super.turnLeftAnimationId = -1;
        }
        super.runAnimationId = stream.getUnsignedLEShort();
        if (super.runAnimationId == 65535) {
            super.runAnimationId = -1;
        }
        this.name = TextClass.formatName(TextClass.longToName(stream.getLong()));
        this.combatLevel = stream.getUnsignedByte();
        this.skill = stream.getUnsignedLEShort();
        this.visible = true;
        this.appearanceOffset = 0L;
        for (int slot = 0; slot < 12; slot++) {
            this.appearanceOffset <<= 4;
            if (this.appearance[slot] >= 256) {
                this.appearanceOffset += this.appearance[slot] - 256;
            }
        }

        if (this.appearance[0] >= 256) {
            this.appearanceOffset += this.appearance[0] - 256 >> 4;
        }
        if (this.appearance[1] >= 256) {
            this.appearanceOffset += this.appearance[1] - 256 >> 8;
        }
        for (int bodyPart = 0; bodyPart < 5; bodyPart++) {
            this.appearanceOffset <<= 3;
            this.appearanceOffset += this.bodyPartColour[bodyPart];
        }

        this.appearanceOffset <<= 1;
        this.appearanceOffset += this.gender;
    }
}