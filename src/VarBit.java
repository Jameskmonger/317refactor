public final class VarBit {

    public static void unpackConfig(StreamLoader archive)
    {
        Stream stream = new Stream(archive.getDataForName("varbit.dat"));
        int cacheSize = stream.getUnsignedLEShort();
        if(cache == null)
            cache = new VarBit[cacheSize];
        for(int j = 0; j < cacheSize; j++)
        {
            if(cache[j] == null)
                cache[j] = new VarBit();
            cache[j].readValues(stream);
            if(cache[j].aBoolean651)
                Varp.cache[cache[j].configId].aBoolean713 = true;
        }

        if(stream.currentOffset != stream.buffer.length)
            System.out.println("varbit load mismatch");
    }

    private void readValues(Stream stream)
    {
        do
        {
            int j = stream.getUnsignedByte();
            if(j == 0)
                return;
            if(j == 1)
            {
                configId = stream.getUnsignedLEShort();
                leastSignificantBit = stream.getUnsignedByte();
                mostSignificantBit = stream.getUnsignedByte();
            } else
            if(j == 10)
                stream.getString();
            else
            if(j == 2)
                aBoolean651 = true;
            else
            if(j == 3)
                stream.getInt();
            else
            if(j == 4)
                stream.getInt();
            else
                System.out.println("Error unrecognised config code: " + j);
        } while(true);
    }

    private VarBit()
    {
        aBoolean651 = false;
    }

    public static VarBit cache[];
    public int configId;
    public int leastSignificantBit;
    public int mostSignificantBit;
    private boolean aBoolean651;
}
