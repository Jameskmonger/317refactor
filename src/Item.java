// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class Item extends Animable {

    public final Model getRotatedModel()
    {
        ItemDef itemDef = ItemDef.forID(itemId);
            return itemDef.method201(itemCount);
    }

    public Item()
    {
    }

    public int itemId;
    public int x;
	public int y;
	public int itemCount;
}
