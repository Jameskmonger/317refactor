// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


final class PlainTile
{

    public PlainTile(int i, int j, int k, int l, int i1, int j1, boolean flag)
    {
        flat = true;
        colourA = i;
        colourB = j;
        colourD = k;
        colourC = l;
        texture = i1;
        colourRGB = j1;
        flat = flag;
    }

    final int colourA;
    final int colourB;
    final int colourD;
    final int colourC;
    final int texture;
    boolean flat;
    final int colourRGB;
}
