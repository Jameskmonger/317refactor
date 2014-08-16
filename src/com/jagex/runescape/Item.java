package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class Item extends Animable {

    public final Model getRotatedModel()
    {
        ItemDefinition itemDef = ItemDefinition.getDefinition(itemId);
            return itemDef.getAmountModel(itemCount);
    }

    public Item()
    {
    }

    public int itemId;
    public int x;
	public int y;
	public int itemCount;
}
