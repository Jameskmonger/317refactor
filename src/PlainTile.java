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
