package com.jagex.runescape;

import com.jagex.runescape.definition.SpotAnimation;

final class Projectile extends Animable {

    public final int delay;

    public final int endCycle;

    private double speedVectorX;

    private double speedVectorY;

    private double speedScalar;
    private double speedVectorZ;
    private double offsetZ;
    private boolean moving;
    private final int startX;
    private final int startY;
    private final int startZ;
    public final int endZ;
    public double currentX;
    public double currentY;
    public double currentZ;
    private final int startSlope;
    private final int startDistance;
    public final int targetId;
    private final SpotAnimation animation;
    private int animationFrame;
    private int duration;
    public int rotationY;
    private int rotationX;
    public final int plane;

    public Projectile(final int startSlope, final int endZ, final int delay, final int endCycle, final int startDistance, final int plane, final int startZ,
                      final int startY, final int startX, final int targetId, final int l2) {
        this.moving = false;
        this.animation = SpotAnimation.cache[l2];
        this.plane = plane;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.delay = delay;
        this.endCycle = endCycle;
        this.startSlope = startSlope;
        this.startDistance = startDistance;
        this.targetId = targetId;
        this.endZ = endZ;
        this.moving = false;
    }

    @Override
    public Model getRotatedModel() {
        final Model model = this.animation.getModel();
        if (model == null) {
            return null;
        }
        int frameId = -1;
        if (this.animation.sequences != null) {
            frameId = this.animation.sequences.primaryFrames[this.animationFrame];
        }
        final Model rotatedModel = new Model(true, Animation.isNullFrame(frameId), false, model);
        if (frameId != -1) {
            rotatedModel.createBones();
            rotatedModel.applyTransformation(frameId);
            rotatedModel.triangleSkin = null;
            rotatedModel.vertexSkin = null;
        }
        if (this.animation.scaleXY != 128 || this.animation.scaleZ != 128) {
            rotatedModel.scaleT(this.animation.scaleXY, this.animation.scaleXY, this.animation.scaleZ);
        }
        rotatedModel.rotateX(this.rotationX);
        rotatedModel.applyLighting(64 + this.animation.modelLightFalloff, 850 + this.animation.modelLightAmbient, -30, -50, -30,
            true);
        return rotatedModel;
    }

    public void move(final int time) {
        this.moving = true;
        this.currentX += this.speedVectorX * time;
        this.currentY += this.speedVectorY * time;
        this.currentZ += this.speedVectorZ * time + 0.5D * this.offsetZ * time * time;
        this.speedVectorZ += this.offsetZ * time;
        this.rotationY = (int) (Math.atan2(this.speedVectorX, this.speedVectorY) * 325.94900000000001D) + 1024 & 0x7ff;
        this.rotationX = (int) (Math.atan2(this.speedVectorZ, this.speedScalar) * 325.94900000000001D) & 0x7ff;
        if (this.animation.sequences != null) {
            for (this.duration += time; this.duration > this.animation.sequences.getFrameLength(this.animationFrame); ) {
                this.duration -= this.animation.sequences.getFrameLength(this.animationFrame) + 1;
                this.animationFrame++;
                if (this.animationFrame >= this.animation.sequences.frameCount) {
                    this.animationFrame = 0;
                }
            }
        }

    }

    public void trackTarget(final int currentCycle, final int targetY, final int targetZ, final int targetX) {
        if (!this.moving) {
            final double distanceX = targetX - this.startX;
            final double distanceY = targetY - this.startY;
            final double distanceScalar = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            this.currentX = this.startX + (distanceX * this.startDistance) / distanceScalar;
            this.currentY = this.startY + (distanceY * this.startDistance) / distanceScalar;
            this.currentZ = this.startZ;
        }
        final double cyclesRemaining = (this.endCycle + 1) - currentCycle;
        this.speedVectorX = (targetX - this.currentX) / cyclesRemaining;
        this.speedVectorY = (targetY - this.currentY) / cyclesRemaining;
        this.speedScalar = Math.sqrt(this.speedVectorX * this.speedVectorX + this.speedVectorY * this.speedVectorY);
        if (!this.moving) {
            this.speedVectorZ = -this.speedScalar * Math.tan(this.startSlope * 0.02454369D);
        }
        this.offsetZ = (2D * (targetZ - this.currentZ - this.speedVectorZ * cyclesRemaining)) / (cyclesRemaining * cyclesRemaining);
    }
}