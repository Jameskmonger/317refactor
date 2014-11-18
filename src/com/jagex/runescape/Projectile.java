package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
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

final class Projectile extends Animable {

    public void trackTarget(int currentCycle, int targetY, int targetZ, int targetX)
    {
        if(!moving)
        {
            double distanceX = targetX - startX;
            double distanceY = targetY - startY;
            double distanceScalar = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            currentX = (double)startX + (distanceX * (double)startDistance) / distanceScalar;
            currentY = (double)startY + (distanceY * (double)startDistance) / distanceScalar;
            currentZ = startZ;
        }
        double cyclesRemaining = (endCycle + 1) - currentCycle;
        speedVectorX = ((double)targetX - currentX) / cyclesRemaining;
        speedVectorY = ((double)targetY - currentY) / cyclesRemaining;
        speedScalar = Math.sqrt(speedVectorX * speedVectorX + speedVectorY * speedVectorY);
        if(!moving)
            speedVectorZ = -speedScalar * Math.tan((double)startSlope * 0.02454369D);
        offsetZ = (2D * ((double)targetZ - currentZ - speedVectorZ * cyclesRemaining)) / (cyclesRemaining * cyclesRemaining);
    }

    public Model getRotatedModel()
    {
        Model model = animation.getModel();
        if(model == null)
            return null;
        int frameId = -1;
        if(animation.sequences != null)
            frameId = animation.sequences.frame2Ids[animationFrame];
        Model rotatedModel = new Model(true, Animation.isNullFrame(frameId), false, model);
        if(frameId != -1)
        {
            rotatedModel.createBones();
            rotatedModel.applyTransformation(frameId);
            rotatedModel.triangleSkin = null;
            rotatedModel.vertexSkin = null;
        }
        if(animation.scaleXY != 128 || animation.scaleZ != 128)
            rotatedModel.scaleT(animation.scaleXY, animation.scaleXY, animation.scaleZ);
        rotatedModel.rotateX(rotationX);
        rotatedModel.applyLighting(64 + animation.modelLightFalloff, 850 + animation.modelLightAmbient, -30, -50, -30, true);
            return rotatedModel;
    }

    public Projectile(int startSlope, int endZ, int delay, int endCycle, int startDistance, int plane,
                         int startZ, int startY, int startX, int targetId, int l2)
    {
        moving = false;
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

    public void move(int time)
    {
        moving = true;
        currentX += speedVectorX * (double)time;
        currentY += speedVectorY * (double)time;
        currentZ += speedVectorZ * (double)time + 0.5D * offsetZ * (double)time * (double)time;
        speedVectorZ += offsetZ * (double)time;
        rotationY = (int)(Math.atan2(speedVectorX, speedVectorY) * 325.94900000000001D) + 1024 & 0x7ff;
        rotationX = (int)(Math.atan2(speedVectorZ, speedScalar) * 325.94900000000001D) & 0x7ff;
        if(animation.sequences != null)
            for(duration += time; duration > animation.sequences.getFrameLength(animationFrame);)
            {
                duration -= animation.sequences.getFrameLength(animationFrame) + 1;
                animationFrame++;
                if(animationFrame >= animation.sequences.frameCount)
                    animationFrame = 0;
            }

    }

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
}