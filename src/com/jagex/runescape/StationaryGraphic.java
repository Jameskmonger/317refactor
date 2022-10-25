package com.jagex.runescape;

import com.jagex.runescape.definition.SpotAnimation;

final class StationaryGraphic extends Animable {

    public final int z;

    public final int x;

    public final int y;

    public final int drawHeight;
    public final int stationaryGraphicLoopCycle;
    public boolean transformationCompleted;
    private final SpotAnimation animation;
    private int elapsedFrames;
    private int duration;

    public StationaryGraphic(final int x, final int y, final int z, final int drawHeight, final int animationIndex, final int loopCycle,
                             final int loopCycleOffset) {
        this.transformationCompleted = false;
        this.animation = SpotAnimation.cache[animationIndex];
        this.z = z;
        this.x = x;
        this.y = y;
        this.drawHeight = drawHeight;
        this.stationaryGraphicLoopCycle = loopCycle + loopCycleOffset;
        this.transformationCompleted = false;
    }

    public void animationStep(final int i) {
        for (this.duration += i; this.duration > this.animation.sequences.getFrameLength(this.elapsedFrames); ) {
            this.duration -= this.animation.sequences.getFrameLength(this.elapsedFrames) + 1;
            this.elapsedFrames++;
            if (this.elapsedFrames >= this.animation.sequences.frameCount
                && (this.elapsedFrames < 0 || this.elapsedFrames >= this.animation.sequences.frameCount)) {
                this.elapsedFrames = 0;
                this.transformationCompleted = true;
            }
        }

    }

    @Override
    public Model getRotatedModel() {
        final Model model = this.animation.getModel();
        if (model == null) {
            return null;
        }
        final int frame = this.animation.sequences.primaryFrames[this.elapsedFrames];
        final Model animatedModel = new Model(true, Animation.isNullFrame(frame), false, model);
        if (!this.transformationCompleted) {
            animatedModel.createBones();
            animatedModel.applyTransformation(frame);
            animatedModel.triangleSkin = null;
            animatedModel.vertexSkin = null;
        }
        if (this.animation.scaleXY != 128 || this.animation.scaleZ != 128) {
            animatedModel.scaleT(this.animation.scaleXY, this.animation.scaleXY, this.animation.scaleZ);
        }
        if (this.animation.rotation != 0) {
            if (this.animation.rotation == 90) {
                animatedModel.rotate90Degrees();
            }
            if (this.animation.rotation == 180) {
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
            }
            if (this.animation.rotation == 270) {
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
            }
        }
        animatedModel.applyLighting(64 + this.animation.modelLightFalloff, 850 + this.animation.modelLightAmbient, -30, -50, -30,
            true);
        return animatedModel;
    }
}
