package com.jagex.runescape.scene.tile;

import com.jagex.runescape.InteractiveObject;
import com.jagex.runescape.collection.Linkable;
import com.jagex.runescape.scene.object.GroundDecoration;
import com.jagex.runescape.scene.object.GroundItemTile;
import com.jagex.runescape.scene.object.Wall;
import com.jagex.runescape.scene.object.WallDecoration;

public final class Tile extends Linkable {

    public int z;

    public final int x;
    public final int y;
    public final int anInt1310;
    public PlainTile plainTile;
    public ShapedTile shapedTile;
    public Wall wall;
    public WallDecoration wallDecoration;
    public GroundDecoration groundDecoration;
    public GroundItemTile groundItemTile;
    public int entityCount;
    public final InteractiveObject[] interactiveObjects;
    public final int[] interactiveObjectsSize;
    public int interactiveObjectsSizeOR;
    public int logicHeight;
    public boolean draw;
    public boolean visible;
    public boolean drawEntities;
    public int wallCullDirection;
    public int anInt1326;
    public int anInt1327;
    public int anInt1328;
    public Tile tileBelow;

    public Tile(final int i, final int j, final int k) {
        this.interactiveObjects = new InteractiveObject[5];
        this.interactiveObjectsSize = new int[5];
        this.anInt1310 = this.z = i;
        this.x = j;
        this.y = k;
    }
}
