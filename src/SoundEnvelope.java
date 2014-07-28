final class SoundEnvelope
{

    public void decode(Stream stream)
    {
        form = stream.getUnsignedByte();
            smart = stream.getInt();
            end = stream.getInt();
            decodeShape(stream);
    }

    public void decodeShape(Stream stream)
    {
        phaseCount = stream.getUnsignedByte();
        phaseDuration = new int[phaseCount];
        phasePeak = new int[phaseCount];
        for(int p = 0; p < phaseCount; p++)
        {
            phaseDuration[p] = stream.getUnsignedLEShort();
            phasePeak[p] = stream.getUnsignedLEShort();
        }

    }

    void resetValues()
    {
        critical = 0;
        phaseIndex = 0;
        step = 0;
        amplitude = 0;
        ticks = 0;
    }

    int step(int period)
    {
        if(ticks >= critical)
        {
            amplitude = phasePeak[phaseIndex++] << 15;
            if(phaseIndex >= phaseCount)
                phaseIndex = phaseCount - 1;
            critical = (int)(((double)phaseDuration[phaseIndex] / 65536D) * (double)period);
            if(critical > ticks)
                step = ((phasePeak[phaseIndex] << 15) - amplitude) / (critical - ticks);
        }
        amplitude += step;
        ticks++;
        return amplitude - step >> 15;
    }

    public SoundEnvelope()
    {
    }

    private int phaseCount;
    private int[] phaseDuration;
    private int[] phasePeak;
    int smart;
    int end;
    int form;
    private int critical;
    private int phaseIndex;
    private int step;
    private int amplitude;
    private int ticks;
    public static int anInt546;
}
