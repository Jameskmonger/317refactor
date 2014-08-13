package com.jagex.runescape;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 


public final class Ground extends Node {

    public Ground(int i, int j, int k)
    {
        interactiveObjects = new InteractiveObject[5];
        interactiveObjectsSize = new int[5];
        anInt1310 = z = i;
        x = j;
        y = k;
    }

    int z;
    final int x;
    final int y;
    final int anInt1310;
    public PlainTile plainTile;
    public ShapedTile shapedTile;
    public WallObject wallObject;
    public WallDecoration wallDecoration;
    public GroundDecoration groundDecoration;
    public GroundItemTile groundItemTile;
    int entityCount;
    public final InteractiveObject[] interactiveObjects;
    final int[] interactiveObjectsSize;
    int interactiveObjectsSizeOR;
    int logicHeight;
    boolean aBoolean1322;
    boolean aBoolean1323;
    boolean aBoolean1324;
    int anInt1325;
    int anInt1326;
    int anInt1327;
    int anInt1328;
    public Ground tileBelow;
}
