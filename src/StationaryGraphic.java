final class StationaryGraphic extends Animable {

    public StationaryGraphic(int plane, int loopCycle, int loopCycleOffset, int animationIndex, int j1, int k1,
                         int l1)
    {
        transformationCompleted = false;
        animation = SpotAnim.cache[animationIndex];
        this.plane = plane;
        anInt1561 = l1;
        anInt1562 = k1;
        anInt1563 = j1;
        this.stationaryGraphicLoopCycle = loopCycle + loopCycleOffset;
        transformationCompleted = false;
    }

    public Model getRotatedModel()
    {
        Model model = animation.getModel();
        if(model == null)
            return null;
        int frame = animation.sequences.frame2Ids[elapsedFrames];
        Model animatedModel = new Model(true, Class36.isNullFrame(frame), false, model);
        if(!transformationCompleted)
        {
            animatedModel.createBones();
            animatedModel.applyTransformation(frame);
            animatedModel.triangleSkin = null;
            animatedModel.vertexSkin = null;
        }
        if(animation.anInt410 != 128 || animation.anInt411 != 128)
            animatedModel.scaleT(animation.anInt410, animation.anInt410, animation.anInt411);
        if(animation.rotation != 0)
        {
            if(animation.rotation == 90)
                animatedModel.rotate90Degrees();
            if(animation.rotation == 180)
            {
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
            }
            if(animation.rotation == 270)
            {
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
                animatedModel.rotate90Degrees();
            }
        }
        animatedModel.applyLighting(64 + animation.modelLightFalloff, 850 + animation.modelLightAmbient, -30, -50, -30, true);
        return animatedModel;
    }

    public void animationStep(int i)
    {
        for(duration += i; duration > animation.sequences.getFrameLength(elapsedFrames);)
        {
            duration -= animation.sequences.getFrameLength(elapsedFrames) + 1;
            elapsedFrames++;
            if(elapsedFrames >= animation.sequences.frameCount && (elapsedFrames < 0 || elapsedFrames >= animation.sequences.frameCount))
            {
                elapsedFrames = 0;
                transformationCompleted = true;
            }
        }

    }

    public final int plane;
    public final int anInt1561;
    public final int anInt1562;
    public final int anInt1563;
    public final int stationaryGraphicLoopCycle;
    public boolean transformationCompleted;
    private final SpotAnim animation;
    private int elapsedFrames;
    private int duration;
}
