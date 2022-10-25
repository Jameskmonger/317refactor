package com.jagex.runescape;

import com.jagex.runescape.definition.AnimationSequence;
import com.jagex.runescape.definition.GameObjectDefinition;
import com.jagex.runescape.definition.VarBit;

final class GameObject extends Animable {

    private int frame;

    private final int[] childrenIds;

    private final int varBitId;

    private final int configId;
    private final int vertexHeightBottomLeft;
    private final int vertexHeightBottomRight;
    private final int vertexHeightTopRight;
    private final int vertexHeightTopLeft;
    private AnimationSequence animation;
    private int nextFrameTime;
    public static Client clientInstance;
    private final int objectId;
    private final int type;
    private final int orientation;

    public GameObject(final int objectId, final int orientation, final int type, final int vertexHeightBottomRight, final int vertexHeightTopRight,
                      final int vertexHeightBottomLeft, final int vertexHeightTopLeft, final int animationId, final boolean animating) {
        this.objectId = objectId;
        this.type = type;
        this.orientation = orientation;
        this.vertexHeightBottomLeft = vertexHeightBottomLeft;
        this.vertexHeightBottomRight = vertexHeightBottomRight;
        this.vertexHeightTopRight = vertexHeightTopRight;
        this.vertexHeightTopLeft = vertexHeightTopLeft;
        if (animationId != -1) {
            this.animation = AnimationSequence.animations[animationId];
            this.frame = 0;
            this.nextFrameTime = Client.tick;
            if (animating && this.animation.frameStep != -1) {
                this.frame = (int) (Math.random() * this.animation.frameCount);
                this.nextFrameTime -= (int) (Math.random() * this.animation.getFrameLength(this.frame));
            }
        }
        final GameObjectDefinition definition = GameObjectDefinition.getDefinition(this.objectId);
        this.varBitId = definition.varBitId;
        this.configId = definition.configIds;
        this.childrenIds = definition.childIds;
    }

    private GameObjectDefinition getChildDefinition() {
        int child = -1;
        if (this.varBitId != -1) {
            final VarBit varBit = VarBit.values[this.varBitId];
            final int configId = varBit.configId;
            final int lsb = varBit.leastSignificantBit;
            final int msb = varBit.mostSignificantBit;
            final int bit = Client.BITFIELD_MAX_VALUE[msb - lsb];
            child = clientInstance.interfaceSettings[configId] >> lsb & bit;
        } else if (this.configId != -1) {
            child = clientInstance.interfaceSettings[this.configId];
        }
        if (child < 0 || child >= this.childrenIds.length || this.childrenIds[child] == -1) {
            return null;
        } else {
            return GameObjectDefinition.getDefinition(this.childrenIds[child]);
        }
    }

    @Override
    public Model getRotatedModel() {
        int animationId = -1;
        if (this.animation != null) {
            int step = Client.tick - this.nextFrameTime;
            if (step > 100 && this.animation.frameStep > 0) {
                step = 100;
            }
            while (step > this.animation.getFrameLength(this.frame)) {
                step -= this.animation.getFrameLength(this.frame);
                this.frame++;
                if (this.frame < this.animation.frameCount) {
                    continue;
                }
                this.frame -= this.animation.frameStep;
                if (this.frame >= 0 && this.frame < this.animation.frameCount) {
                    continue;
                }
                this.animation = null;
                break;
            }
            this.nextFrameTime = Client.tick - step;
            if (this.animation != null) {
                animationId = this.animation.primaryFrames[this.frame];
            }
        }
        final GameObjectDefinition definition;
        if (this.childrenIds != null) {
            definition = this.getChildDefinition();
        } else {
            definition = GameObjectDefinition.getDefinition(this.objectId);
        }
        if (definition == null) {
            return null;
        } else {
            return definition.getModelAt(this.type, this.orientation, this.vertexHeightBottomLeft, this.vertexHeightBottomRight,
                this.vertexHeightTopRight, this.vertexHeightTopLeft, animationId);
        }
    }
}
