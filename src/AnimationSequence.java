// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class AnimationSequence {

    public static void unpackConfig(StreamLoader streamLoader)
    {
        Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
        int length = stream.getUnsignedLEShort();
        if(anims == null)
            anims = new AnimationSequence[length];
        for(int animation = 0; animation < length; animation++)
        {
            if(anims[animation] == null)
                anims[animation] = new AnimationSequence();
            anims[animation].readValues(stream);
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
                anInt359 = stream.getUnsignedByte();
            else
            if(attribute == 6)
                anInt360 = stream.getUnsignedLEShort();
            else
            if(attribute == 7)
                anInt361 = stream.getUnsignedLEShort();
            else
            if(attribute == 8)
                anInt362 = stream.getUnsignedByte();
            else
            if(attribute == 9)
                anInt363 = stream.getUnsignedByte();
            else
            if(attribute == 10)
                priority = stream.getUnsignedByte();
            else
            if(attribute == 11)
                anInt365 = stream.getUnsignedByte();
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
        if(anInt363 == -1)
            if(flowControl != null)
                anInt363 = 2;
            else
                anInt363 = 0;
        if(priority == -1)
        {
            if(flowControl != null)
            {
                priority = 2;
                return;
            }
            priority = 0;
        }
    }

    private AnimationSequence()
    {
        frameStep = -1;
        dynamic = false;
        anInt359 = 5;
        anInt360 = -1;
        anInt361 = -1;
        anInt362 = 99;
        anInt363 = -1;
        priority = -1;
        anInt365 = 2;
    }

    public static AnimationSequence anims[];
    public int frameCount;
    public int frame2Ids[];
    public int frame1Ids[];
    private int[] frameLengths;
    public int frameStep;
    public int flowControl[];
    public boolean dynamic;
    public int anInt359;
    public int anInt360;
    public int anInt361;
    public int anInt362;
    public int anInt363;
    public int priority;
    public int anInt365;
    public static int anInt367;
}
