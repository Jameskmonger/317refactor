package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class AnimationSequence {

    public static void unpackConfig(Archive streamLoader)
    {
        Stream stream = new Stream(streamLoader.getFile("seq.dat"));
        int length = stream.getUnsignedLEShort();
        if(animations == null)
            animations = new AnimationSequence[length];
        for(int animation = 0; animation < length; animation++)
        {
            if(animations[animation] == null)
                animations[animation] = new AnimationSequence();
            animations[animation].readValues(stream);
        }
    }

    public int getFrameLength(int frame)
    {
        int frameLength = frameLengths[frame];
        if(frameLength == 0)
        {
            Animation animation = Animation.forFrameId(frame2Ids[frame]);
            if(animation != null)
                frameLength = frameLengths[frame] = animation.displayLength;
        }
        if(frameLength == 0)
            frameLength = 1;
        return frameLength;
    }

    private void readValues(Stream stream)
    {
        do
        {
            int attribute = stream.getUnsignedByte();
            if(attribute == 0)
                break;
            if(attribute == 1)
            {
                frameCount = stream.getUnsignedByte();
                frame2Ids = new int[frameCount];
                frame1Ids = new int[frameCount];
                frameLengths = new int[frameCount];
                for(int frame = 0; frame < frameCount; frame++)
                {
                    frame2Ids[frame] = stream.getUnsignedLEShort();
                    frame1Ids[frame] = stream.getUnsignedLEShort();
                    if(frame1Ids[frame] == 65535)
                        frame1Ids[frame] = -1;
                    frameLengths[frame] = stream.getUnsignedLEShort();
                }

            } else
            if(attribute == 2)
                frameStep = stream.getUnsignedLEShort();
            else
            if(attribute == 3)
            {
                int flowCount = stream.getUnsignedByte();
                flowControl = new int[flowCount + 1];
                for(int flow = 0; flow < flowCount; flow++)
                    flowControl[flow] = stream.getUnsignedByte();

                flowControl[flowCount] = 0x98967f;
            } else
            if(attribute == 4)
                dynamic = true;
            else
            if(attribute == 5)
                priority = stream.getUnsignedByte();
            else
            if(attribute == 6)
                playerReplacementShield = stream.getUnsignedLEShort();
            else
            if(attribute == 7)
                playerReplacementWeapon = stream.getUnsignedLEShort();
            else
            if(attribute == 8)
                maximumLoops = stream.getUnsignedByte();
            else
            if(attribute == 9)
            	/* when animating, 0 -> block walking, 1 -> yield to walking, 2 -> interleave with walking */
                precedenceAnimating = stream.getUnsignedByte();
            else
            if(attribute == 10)
            	/* when walking, 0 -> block walking, 1 -> yield to walking, 2 -> never used... interleave with walking? */
                precedenceWalking = stream.getUnsignedByte();
            else
            if(attribute == 11)
                replayMode = stream.getUnsignedByte();
            else
            if(attribute == 12)
                stream.getInt();
            else
                System.out.println("Error unrecognised seq config code: " + attribute);
        } while(true);
        if(frameCount == 0)
        {
            frameCount = 1;
            frame2Ids = new int[1];
            frame2Ids[0] = -1;
            frame1Ids = new int[1];
            frame1Ids[0] = -1;
            frameLengths = new int[1];
            frameLengths[0] = -1;
        }
        if(precedenceAnimating == -1)
            if(flowControl != null)
                precedenceAnimating = 2;
            else
                precedenceAnimating = 0;
        if(precedenceWalking == -1)
        {
            if(flowControl != null)
            {
                precedenceWalking = 2;
                return;
            }
            precedenceWalking = 0;
        }
    }

    private AnimationSequence()
    {
        frameStep = -1;
        dynamic = false;
        priority = 5;
        playerReplacementShield = -1;
        playerReplacementWeapon = -1;
        maximumLoops = 99;
        precedenceAnimating = -1;
        precedenceWalking = -1;
        replayMode = 2;
    }

    public static AnimationSequence animations[];
    public int frameCount;
    public int frame2Ids[];
    public int frame1Ids[];
    private int[] frameLengths;
    public int frameStep;
    public int flowControl[];
    public boolean dynamic;
    public int priority;
    public int playerReplacementShield;
    public int playerReplacementWeapon;
    public int maximumLoops;
    public int precedenceAnimating;
    public int precedenceWalking;
    public int replayMode;
    public static int anInt367;
}
