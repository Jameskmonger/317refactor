package com.jagex.runescape.scene.tile;

import com.jagex.runescape.InteractiveObject;
import com.jagex.runescape.collection.Linkable;
import com.jagex.runescape.scene.object.*;

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
	public boolean aBoolean1322;
	public boolean aBoolean1323;
	public boolean aBoolean1324;
	public int anInt1325;
	public int anInt1326;
	public int anInt1327;
	public int anInt1328;
	public Tile tileBelow;

	public Tile(int i, int j, int k) {
        this.interactiveObjects = new InteractiveObject[5];
        this.interactiveObjectsSize = new int[5];
        this.anInt1310 = this.z = i;
        this.x = j;
        this.y = k;
	}
}
