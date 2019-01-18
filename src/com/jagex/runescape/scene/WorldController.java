package com.jagex.runescape.scene;

import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.scene.object.GroundDecoration;
import com.jagex.runescape.scene.object.GroundItemTile;
import com.jagex.runescape.scene.object.Wall;
import com.jagex.runescape.scene.object.WallDecoration;
import com.jagex.runescape.scene.tile.PlainTile;
import com.jagex.runescape.scene.tile.ShapedTile;
import com.jagex.runescape.scene.tile.Tile;

import com.jagex.runescape.Rasterizer;
import com.jagex.runescape.CullingCluster;
import com.jagex.runescape.InteractiveObject;
import com.jagex.runescape.Model;
import com.jagex.runescape.Animable;
import com.jagex.runescape.VertexNormal;
import com.jagex.runescape.DrawingArea;

public final class WorldController {

	public static void createCullingCluster(int z, int highestX, int lowestX, int highestY, int lowestY, int highestZ,
			int lowestZ, int searchMask) {
		CullingCluster cullingCluster = new CullingCluster();
		cullingCluster.tileStartX = lowestX / 128;
		cullingCluster.tileEndX = highestX / 128;
		cullingCluster.tileStartY = lowestY / 128;
		cullingCluster.tileEndY = highestY / 128;
		cullingCluster.searchMask = searchMask;
		cullingCluster.worldStartX = lowestX;
		cullingCluster.worldEndX = highestX;
		cullingCluster.worldStartY = lowestY;
		cullingCluster.worldEndY = highestY;
		cullingCluster.worldEndZ = highestZ;
		cullingCluster.worldStartZ = lowestZ;
		cullingClusters[z][cullingClusterPointer[z]++] = cullingCluster;
	}

	public static void nullLoader() {
		interactiveObjects = null;
		cullingClusterPointer = null;
		cullingClusters = null;
		tileList = null;
		TILE_VISIBILITY_MAPS = null;
		TILE_VISIBILITY_MAP = null;
	}

	private static boolean onScreen(int x, int y, int z) {
		int l = y * curveSineX + x * curveCosineX >> 16;
		int i1 = y * curveCosineX - x * curveSineX >> 16;
		int j1 = z * curveSineY + i1 * curveCosineY >> 16;
		int k1 = z * curveCosineY - i1 * curveSineY >> 16;
		if (j1 < 50 || j1 > 3500) {
			return false;
		}
		int l1 = midX + (l << 9) / j1;
		int i2 = midY + (k1 << 9) / j1;
		return l1 >= left && l1 <= right && i2 >= top && i2 <= bottom;
	}

	public static void setupViewport(int i, int j, int viewportWidth, int viewportHeight, int ai[]) {
		left = 0;
		top = 0;
		right = viewportWidth;
		bottom = viewportHeight;
		midX = viewportWidth / 2;
		midY = viewportHeight / 2;
		boolean tileOnScreen[][][][] = new boolean[9][32][53][53];
		for (int angleY = 128; angleY <= 384; angleY += 32) {
			for (int angleX = 0; angleX < 2048; angleX += 64) {
				curveSineY = Model.SINE[angleY];
				curveCosineY = Model.COSINE[angleY];
				curveSineX = Model.SINE[angleX];
				curveCosineX = Model.COSINE[angleX];
				int anglePointerY = (angleY - 128) / 32;
				int anglePointerX = angleX / 64;
				for (int x = -26; x <= 26; x++) {
					for (int y = -26; y <= 26; y++) {
						int worldX = x * 128;
						int worldY = y * 128;
						boolean visible = false;
						for (int worldZ = -i; worldZ <= j; worldZ += 128) {
							if (!onScreen(worldX, worldY, ai[anglePointerY] + worldZ)) {
								continue;
							}
							visible = true;
							break;
						}

						tileOnScreen[anglePointerY][anglePointerX][x + 25 + 1][y + 25 + 1] = visible;
					}

				}

			}

		}

		for (int anglePointerY = 0; anglePointerY < 8; anglePointerY++) {
			for (int anglePointerX = 0; anglePointerX < 32; anglePointerX++) {
				for (int relativeX = -25; relativeX < 25; relativeX++) {
					for (int relativeZ = -25; relativeZ < 25; relativeZ++) {
						boolean visible = false;
						label0: for (int f = -1; f <= 1; f++) {
							for (int g = -1; g <= 1; g++) {
								if (tileOnScreen[anglePointerY][anglePointerX][relativeX + f + 25 + 1][relativeZ + g
										+ 25 + 1]) {
									visible = true;
								} else if (tileOnScreen[anglePointerY][(anglePointerX + 1) % 31][relativeX + f + 25
										+ 1][relativeZ + g + 25 + 1]) {
									visible = true;
								} else if (tileOnScreen[anglePointerY + 1][anglePointerX][relativeX + f + 25
										+ 1][relativeZ + g + 25 + 1]) {
									visible = true;
								} else {
									if (!tileOnScreen[anglePointerY + 1][(anglePointerX + 1) % 31][relativeX + f + 25
											+ 1][relativeZ + g + 25 + 1]) {
										continue;
									}
									visible = true;
								}
								break label0;
							}

						}

						TILE_VISIBILITY_MAPS[anglePointerY][anglePointerX][relativeX + 25][relativeZ + 25] = visible;
					}

				}

			}

		}

	}

	public static boolean lowMemory = true;

	private final int mapSizeZ;

	private final int mapSizeX;

	private final int mapSizeY;

	private final int[][][] heightMap;

	private final Tile[][][] groundArray;

	private int currentPositionZ;

	private int interactiveObjectCacheCurrentPos;

	private final InteractiveObject[] interactiveObjectCache;

	private final int[][][] anIntArrayArrayArray445;

	private static int anInt446;

	private static int plane;

	private static int anInt448;

	private static int currentPositionX;

	private static int mapBoundsX;

	private static int currentPositionY;

	private static int mapBoundsY;

	private static int cameraPositionTileX;

	private static int cameraPositionTileY;

	private static int cameraPosX;

	private static int cameraPosZ;

	private static int cameraPosY;

	private static int curveSineY;

	private static int curveCosineY;

	private static int curveSineX;

	private static int curveCosineX;

	private static InteractiveObject[] interactiveObjects = new InteractiveObject[100];

	private static final int[] faceOffsetX2 = { 53, -53, -53, 53 };

	private static final int[] faceOffsetY2 = { -53, -53, 53, 53 };

	private static final int[] faceOffsetX3 = { -45, 45, 45, -45 };

	private static final int[] faceOffsetY3 = { 45, 45, -45, -45 };

	private static boolean clicked;

	private static int clickX;

	private static int clickY;

	public static int clickedTileX = -1;

	public static int clickedTileY = -1;

	private static final int anInt472;

	private static int[] cullingClusterPointer;

	private static CullingCluster[][] cullingClusters;

	private static int processedCullingClustersPointer;

	private static final CullingCluster[] processedCullingClusters = new CullingCluster[500];

	private static DoubleEndedQueue tileList = new DoubleEndedQueue();

	private static final int[] anIntArray478 = { 19, 55, 38, 155, 255, 110, 137, 205, 76 };

	private static final int[] anIntArray479 = { 160, 192, 80, 96, 0, 144, 80, 48, 160 };

	private static final int[] anIntArray480 = { 76, 8, 137, 4, 0, 1, 38, 2, 19 };

	private static final int[] anIntArray481 = { 0, 0, 2, 0, 0, 2, 1, 1, 0 };

	private static final int[] anIntArray482 = { 2, 0, 0, 2, 0, 0, 0, 4, 4 };

	private static final int[] anIntArray483 = { 0, 4, 4, 8, 0, 0, 8, 0, 0 };

	private static final int[] anIntArray484 = { 1, 1, 0, 0, 0, 8, 0, 0, 8 };

	private static final int[] textureRGB = { 41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086,
			41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41,
			41, 41, 41, 41, 41, 3131, 41, 41, 41 };
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] tileShapePoints = { new int[16], { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 } };
	private final int[][] tileShapeIndices = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3 },
			{ 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 },
			{ 3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12 } };
	private static boolean[][][][] TILE_VISIBILITY_MAPS = new boolean[8][32][51][51];
	private static boolean[][] TILE_VISIBILITY_MAP;
	private static int midX;
	private static int midY;
	private static int left;
	private static int top;
	private static int right;
	private static int bottom;
	static {
		anInt472 = 4;
		cullingClusterPointer = new int[anInt472];
		cullingClusters = new CullingCluster[anInt472][500];
	}

	public WorldController(int heightMap[][][]) {
		int length = 104;// was parameter
		int width = 104;// was parameter
		int height = 4;// was parameter
		this.interactiveObjectCache = new InteractiveObject[5000];
		this.anIntArray486 = new int[10000];
		this.anIntArray487 = new int[10000];
		this.mapSizeZ = height;
		this.mapSizeX = width;
		this.mapSizeY = length;
		this.groundArray = new Tile[height][width][length];
		this.anIntArrayArrayArray445 = new int[height][width + 1][length + 1];
		this.heightMap = heightMap;
		this.initToNull();
	}

	public boolean addEntity(int x, int y, int z, int worldX, int worldY, int worldZ, int rotation, int tileWidth,
			int tileHeight, Animable entity, int uid) {
		return entity == null || this.addEntityC(x, y, z, worldX, worldY, worldZ, rotation, (tileWidth - y) + 1,
				(tileHeight - x) + 1, uid, entity, true, (byte) 0);
	}

	public boolean addEntity(int z, int worldX, int worldY, int worldZ, int yaw, Animable entity, int uid, int delta,
			boolean accountForYaw) {
		if (entity == null) {
			return true;
		}
		int minX = worldX - delta;
		int minY = worldY - delta;
		int maxX = worldX + delta;
		int maxY = worldY + delta;

		if (accountForYaw) {
			if (yaw > 640 && yaw < 1408) {
				maxY += 128;
			}
			if (yaw > 1152 && yaw < 1920) {
				maxX += 128;
			}
			if (yaw > 1664 || yaw < 384) {
				minY -= 128;
			}
			if (yaw > 128 && yaw < 896) {
				minX -= 128;
			}
		}

		minX /= 128;
		minY /= 128;
		maxX /= 128;
		maxY /= 128;

		return this.addEntityC(minX, minY, z, worldX, worldY, worldZ, yaw, (maxY - minY) + 1, (maxX - minX) + 1, uid,
				entity, true, (byte) 0);
	}

	public boolean addEntityB(int x, int y, int z, int worldZ, int rotation, int tileWidth, int tileHeight, int uid,
			Animable entity, byte objConf) {
		if (entity == null) {
			return true;
		} else {
			int worldX = x * 128 + 64 * tileHeight;
			int worldY = y * 128 + 64 * tileWidth;
			return this.addEntityC(x, y, z, worldX, worldY, worldZ, rotation, tileWidth, tileHeight, uid, entity, false,
					objConf);
		}
	}

	private boolean addEntityC(int minX, int minY, int z, int worldX, int worldY, int worldZ, int rotation, int tileWidth,
			int tileHeight, int uid, Animable renderable, boolean isDynamic, byte objConf) {
		for (int x = minX; x < minX + tileHeight; x++) {
			for (int y = minY; y < minY + tileWidth; y++) {
				if (x < 0 || y < 0 || x >= this.mapSizeX || y >= this.mapSizeY) {
					return false;
				}
				Tile tile = this.groundArray[z][x][y];
				if (tile != null && tile.entityCount >= 5) {
					return false;
				}
			}

		}

		InteractiveObject entity = new InteractiveObject();
		entity.uid = uid;
		entity.objConf = objConf;
		entity.z = z;
		entity.worldX = worldX;
		entity.worldY = worldY;
		entity.worldZ = worldZ;
		entity.renderable = renderable;
		entity.rotation = rotation;
		entity.tileLeft = minX;
		entity.tileTop = minY;
		entity.tileRight = (minX + tileHeight) - 1;
		entity.tileBottom = (minY + tileWidth) - 1;
		for (int x = minX; x < minX + tileHeight; x++) {
			for (int y = minY; y < minY + tileWidth; y++) {
				int size = 0;
				if (x > minX) {
					size += 0b0001;
				}
				if (x < (minX + tileHeight) - 1) {
					size += 0b0100;
				}
				if (y > minY) {
					size += 0b1000;
				}
				if (y < (minY + tileWidth) - 1) {
					size += 0b0010;
				}
					
				for (int _z = z; _z >= 0; _z--) {
					if (this.groundArray[_z][x][y] == null) {
						this.groundArray[_z][x][y] = new Tile(_z, x, y);
					}
				}

				Tile tile = this.groundArray[z][x][y];
				tile.interactiveObjects[tile.entityCount] = entity;
				tile.interactiveObjectsSize[tile.entityCount] = size;
				tile.interactiveObjectsSizeOR |= size;
				tile.entityCount++;
			}

		}

		if (isDynamic) {
			this.interactiveObjectCache[this.interactiveObjectCacheCurrentPos++] = entity;
		}
		return true;
	}

	public void addGroundDecoration(int x, int y, int z, int drawHeight, int uid, Animable renderable, byte objConf) {
		if (renderable == null) {
			return;
		}
		GroundDecoration groundDecoration = new GroundDecoration();
		groundDecoration.renderable = renderable;
		groundDecoration.x = x * 128 + 64;
		groundDecoration.y = y * 128 + 64;
		groundDecoration.z = drawHeight;
		groundDecoration.uid = uid;
		groundDecoration.objConf = objConf;
		if (this.groundArray[z][x][y] == null) {
			this.groundArray[z][x][y] = new Tile(z, x, y);
		}
		this.groundArray[z][x][y].groundDecoration = groundDecoration;
	}

	public void addGroundItemTile(int x, int y, int z, int drawHeight, int uid, Animable firstGroundItem,
			Animable secondGroundItem, Animable thirdGroundItem) {
		GroundItemTile groundItemTile = new GroundItemTile();
		groundItemTile.firstGroundItem = firstGroundItem;
		groundItemTile.x = x * 128 + 64;
		groundItemTile.y = y * 128 + 64;
		groundItemTile.z = drawHeight;
		groundItemTile.uid = uid;
		groundItemTile.secondGroundItem = secondGroundItem;
		groundItemTile.thirdGroundItem = thirdGroundItem;
		int j1 = 0;
		Tile tile = this.groundArray[z][x][y];
		if (tile != null) {
			for (int e = 0; e < tile.entityCount; e++) {
				if (tile.interactiveObjects[e].renderable instanceof Model) {
					int l1 = ((Model) tile.interactiveObjects[e].renderable).anInt1654;
					if (l1 > j1) {
						j1 = l1;
					}
				}
			}

		}
		groundItemTile.anInt52 = j1;
		if (this.groundArray[z][x][y] == null) {
			this.groundArray[z][x][y] = new Tile(z, x, y);
		}
		this.groundArray[z][x][y].groundItemTile = groundItemTile;
	}

	public void addWallDecoration(int x, int y, int z, int drawHeight, int offsetX, int offsetY, int face, int uid,
			Animable renderable, byte objConf, int faceBits) {
		if (renderable == null) {
			return;
		}
		WallDecoration wallDecoration = new WallDecoration();
		wallDecoration.uid = uid;
		wallDecoration.objConf = objConf;
		wallDecoration.x = x * 128 + 64 + offsetX;
		wallDecoration.y = y * 128 + 64 + offsetY;
		wallDecoration.z = drawHeight;
		wallDecoration.renderable = renderable;
		wallDecoration.configBits = faceBits;
		wallDecoration.face = face;
		for (int _z = z; _z >= 0; _z--) {
			if (this.groundArray[_z][x][y] == null) {
				this.groundArray[_z][x][y] = new Tile(_z, x, y);
			}
		}

		this.groundArray[z][x][y].wallDecoration = wallDecoration;
	}

	public void addWall(int x, int y, int z, int drawHeight, int orientation, int orientation2, int uid,
			Animable primary, Animable secondary, byte objConf) {
		if (primary == null && secondary == null) {
			return;
		}

		Wall wall = new Wall();
		wall.uid = uid;
		wall.objConf = objConf;
		wall.x = x * 128 + 64;
		wall.y = y * 128 + 64;
		wall.z = drawHeight;
		wall.primary = primary;
		wall.secondary = secondary;
		wall.orientation = orientation;
		wall.orientation2 = orientation2;

		for (int _z = z; _z >= 0; _z--) {
			if (this.groundArray[_z][x][y] == null) {
				this.groundArray[_z][x][y] = new Tile(_z, x, y);
			}
		}

		this.groundArray[z][x][y].wall = wall;
	}

	public void applyBridgeMode(int x, int y) {
		Tile tile = this.groundArray[0][x][y];
		for (int z = 0; z < 3; z++) {
			Tile _tile = this.groundArray[z][x][y] = this.groundArray[z + 1][x][y];
			if (_tile != null) {
				_tile.z--;
				for (int e = 0; e < _tile.entityCount; e++) {
					InteractiveObject entity = _tile.interactiveObjects[e];
					if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
						entity.z--;
					}
				}

			}
		}
		if (this.groundArray[0][x][y] == null) {
			this.groundArray[0][x][y] = new Tile(0, x, y);
		}
		this.groundArray[0][x][y].tileBelow = tile;
		this.groundArray[3][x][y] = null;
	}

	public void clearInteractiveObjectCache() {
		for (int i = 0; i < this.interactiveObjectCacheCurrentPos; i++) {
			InteractiveObject entity = this.interactiveObjectCache[i];
			this.remove(entity);
			this.interactiveObjectCache[i] = null;
		}

		this.interactiveObjectCacheCurrentPos = 0;
	}

	public void drawMinimapTile(int x, int y, int z, int pixels[], int pixelPointer) {
		int scanLength = 512;// was parameter
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return;
		}
		PlainTile plainTile = tile.plainTile;
		if (plainTile != null) {
			int colourRGB = plainTile.colourRGB;
			if (colourRGB == 0) {
				return;
			}
			for (int line = 0; line < 4; line++) {
				pixels[pixelPointer] = colourRGB;
				pixels[pixelPointer + 1] = colourRGB;
				pixels[pixelPointer + 2] = colourRGB;
				pixels[pixelPointer + 3] = colourRGB;
				pixelPointer += scanLength;
			}

			return;
		}
		ShapedTile shapedTile = tile.shapedTile;
		if (shapedTile == null) {
			return;
		}
		int shape = shapedTile.shape;
		int rotation = shapedTile.rotation;
		int underlayRGB = shapedTile.underlayRGB;
		int overlayRGB = shapedTile.overlayRGB;
		int shapePoints[] = this.tileShapePoints[shape];
		int shapeIndices[] = this.tileShapeIndices[rotation];
		int shapePointer = 0;
		if (underlayRGB != 0) {
			for (int line = 0; line < 4; line++) {
				pixels[pixelPointer] = shapePoints[shapeIndices[shapePointer++]] != 0 ? overlayRGB : underlayRGB;
				pixels[pixelPointer + 1] = shapePoints[shapeIndices[shapePointer++]] != 0 ? overlayRGB : underlayRGB;
				pixels[pixelPointer + 2] = shapePoints[shapeIndices[shapePointer++]] != 0 ? overlayRGB : underlayRGB;
				pixels[pixelPointer + 3] = shapePoints[shapeIndices[shapePointer++]] != 0 ? overlayRGB : underlayRGB;
				pixelPointer += scanLength;
			}

			return;
		}
		for (int line = 0; line < 4; line++) {
			if (shapePoints[shapeIndices[shapePointer++]] != 0) {
				pixels[pixelPointer] = overlayRGB;
			}
			if (shapePoints[shapeIndices[shapePointer++]] != 0) {
				pixels[pixelPointer + 1] = overlayRGB;
			}
			if (shapePoints[shapeIndices[shapePointer++]] != 0) {
				pixels[pixelPointer + 2] = overlayRGB;
			}
			if (shapePoints[shapeIndices[shapePointer++]] != 0) {
				pixels[pixelPointer + 3] = overlayRGB;
			}
			pixelPointer += scanLength;
		}

	}

	public int getConfig(int uid, int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return -1;
		}
		if (tile.wall != null && tile.wall.uid == uid) {
			return tile.wall.objConf & 0xff;
		}
		if (tile.wallDecoration != null && tile.wallDecoration.uid == uid) {
			return tile.wallDecoration.objConf & 0xff;
		}
		if (tile.groundDecoration != null && tile.groundDecoration.uid == uid) {
			return tile.groundDecoration.objConf & 0xff;
		}
		for (int e = 0; e < tile.entityCount; e++) {
			if (tile.interactiveObjects[e].uid == uid) {
				return tile.interactiveObjects[e].objConf & 0xff;
			}
		}

		return -1;
	}

	public GroundDecoration getGroundDecoration(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null || tile.groundDecoration == null) {
			return null;
		} else {
			return tile.groundDecoration;
		}
	}

	public int getGroundDecorationHash(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null || tile.groundDecoration == null) {
			return 0;
		} else {
			return tile.groundDecoration.uid;
		}
	}

	public int getInteractibleObjectHash(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return 0;
		}
		for (int e = 0; e < tile.entityCount; e++) {
			InteractiveObject entity = tile.interactiveObjects[e];
			if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
				return entity.uid;
			}
		}

		return 0;
	}

	public InteractiveObject getInteractiveObject(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return null;
		}
		for (int e = 0; e < tile.entityCount; e++) {
			InteractiveObject entity = tile.interactiveObjects[e];
			if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
				return entity;
			}
		}
		return null;
	}

	public WallDecoration getWallDecoration(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return null;
		} else {
			return tile.wallDecoration;
		}
	}

	public int getWallDecorationHash(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null || tile.wallDecoration == null) {
			return 0;
		} else {
			return tile.wallDecoration.uid;
		}
	}

	public Wall getWallObject(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return null;
		} else {
			return tile.wall;
		}
	}

	public int getWallObjectHash(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null || tile.wall == null) {
			return 0;
		} else {
			return tile.wall.uid;
		}
	}

	public void initToNull() {
		for (int z = 0; z < this.mapSizeZ; z++) {
			for (int x = 0; x < this.mapSizeX; x++) {
				for (int y = 0; y < this.mapSizeY; y++) {
					this.groundArray[z][x][y] = null;
				}

			}

		}
		for (int l = 0; l < anInt472; l++) {
			for (int j1 = 0; j1 < cullingClusterPointer[l]; j1++) {
				cullingClusters[l][j1] = null;
			}

			cullingClusterPointer[l] = 0;
		}

		for (int k1 = 0; k1 < this.interactiveObjectCacheCurrentPos; k1++) {
			this.interactiveObjectCache[k1] = null;
		}

		this.interactiveObjectCacheCurrentPos = 0;
		for (int l1 = 0; l1 < interactiveObjects.length; l1++) {
			interactiveObjects[l1] = null;
		}

	}

	private boolean isMouseWithinTriangle(int mouseX, int mouseY, int pointAY, int pointBY, int pointCY, int pointAX,
			int pointBX, int pointCX) {
		if (mouseY < pointAY && mouseY < pointBY && mouseY < pointCY) {
			return false;
		}
		if (mouseY > pointAY && mouseY > pointBY && mouseY > pointCY) {
			return false;
		}
		if (mouseX < pointAX && mouseX < pointBX && mouseX < pointCX) {
			return false;
		}
		if (mouseX > pointAX && mouseX > pointBX && mouseX > pointCX) {
			return false;
		}

		int b1 = (mouseY - pointAY) * (pointBX - pointAX) - (mouseX - pointAX) * (pointBY - pointAY);
		int b2 = (mouseY - pointCY) * (pointAX - pointCX) - (mouseX - pointCX) * (pointAY - pointCY);
		int b3 = (mouseY - pointBY) * (pointCX - pointBX) - (mouseX - pointBX) * (pointCY - pointBY);
		return b1 * b3 > 0 && b3 * b2 > 0;
	}

	public void displaceWallDecoration(int y, int displacement, int x, int z) {
		Tile tile = this.groundArray[z][x][y];
		
		if (tile == null) {
			return;
		}

		WallDecoration wallDecoration = tile.wallDecoration;

		if (wallDecoration != null) {
			int absX = x * 128 + 64;
			int absY = y * 128 + 64;
			wallDecoration.x = absX + ((wallDecoration.x - absX) * displacement) / 16;
			wallDecoration.y = absY + ((wallDecoration.y - absY) * displacement) / 16;
		}
	}

	private void method306(Model model, int x, int y, int z) {
		if (x < this.mapSizeX) {
			Tile tile = this.groundArray[z][x + 1][y];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
				this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 0, true);
			}
		}
		if (y < this.mapSizeX) {
			Tile tile = this.groundArray[z][x][y + 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
				this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 0, 0, 128, true);
			}
		}
		if (x < this.mapSizeX && y < this.mapSizeY) {
			Tile tile = this.groundArray[z][x + 1][y + 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
				this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 128, true);
			}
		}
		if (x < this.mapSizeX && y > 0) {
			Tile tile = this.groundArray[z][x + 1][y - 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
				this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, -128, true);
			}
		}
	}

	private void method307(int z, int j, int k, int x, int y, Model model) {
		boolean flag = true;
		int positionX = x;
		int position2X = x + j;
		int positionY = y - 1;
		int position2Y = y + k;
		for (int _z = z; _z <= z + 1; _z++) {
			if (_z != this.mapSizeZ) {
				for (int _x = positionX; _x <= position2X; _x++) {
					if (_x >= 0 && _x < this.mapSizeX) {
						for (int _y = positionY; _y <= position2Y; _y++) {
							if (_y >= 0 && _y < this.mapSizeY
									&& (!flag || _x >= position2X || _y >= position2Y || _y < y && _x != x)) {
								Tile tile = this.groundArray[_z][_x][_y];
								if (tile != null) {
									int i3 = (this.heightMap[_z][_x][_y] + this.heightMap[_z][_x + 1][_y]
											+ this.heightMap[_z][_x][_y + 1] + this.heightMap[_z][_x + 1][_y + 1]) / 4
											- (this.heightMap[z][x][y] + this.heightMap[z][x + 1][y] + this.heightMap[z][x][y + 1]
											+ this.heightMap[z][x + 1][y + 1]) / 4;
									Wall wallObject = tile.wall;
									if (wallObject != null && wallObject.primary != null
											&& wallObject.primary.vertexNormals != null) {
										this.mergeNormals(model, (Model) wallObject.primary, (_x - x) * 128 + (1 - j) * 64,
												i3, (_y - y) * 128 + (1 - k) * 64, flag);
									}
									if (wallObject != null && wallObject.secondary != null
											&& wallObject.secondary.vertexNormals != null) {
										this.mergeNormals(model, (Model) wallObject.secondary, (_x - x) * 128 + (1 - j) * 64,
												i3, (_y - y) * 128 + (1 - k) * 64, flag);
									}
									for (int e = 0; e < tile.entityCount; e++) {
										InteractiveObject entity = tile.interactiveObjects[e];
										if (entity != null && entity.renderable != null
												&& entity.renderable.vertexNormals != null) {
											int k3 = (entity.tileRight - entity.tileLeft) + 1;
											int l3 = (entity.tileBottom - entity.tileTop) + 1;
											this.mergeNormals(model, (Model) entity.renderable,
													(entity.tileLeft - x) * 128 + (k3 - j) * 64, i3,
													(entity.tileTop - y) * 128 + (l3 - k) * 64, flag);
										}
									}

								}
							}
						}

					}
				}

				positionX--;
				flag = false;
			}
		}

	}

	private void mergeNormals(Model model, Model secondModel, int posX, int posY, int posZ, boolean flag) {
		this.anInt488++;
		int count = 0;
		int vertices[] = secondModel.verticesX;
		int vertexCount = secondModel.vertexCount;

		for (int vertex = 0; vertex < model.vertexCount; vertex++) {
			VertexNormal vertexNormal = model.vertexNormals[vertex];
			VertexNormal offsetVertexNormal = model.vertexNormalOffset[vertex];
			if (offsetVertexNormal.magnitude != 0) {
				int y = model.verticesY[vertex] - posY;
				if (y <= secondModel.maxY) {
					int x = model.verticesX[vertex] - posX;
					if (x >= secondModel.maxY && x <= secondModel.maxX) {
						int z = model.verticesZ[vertex] - posZ;
						if (z >= secondModel.minZ && z <= secondModel.maxZ) {
							for (int v = 0; v < vertexCount; v++) {
								VertexNormal vertexNormal2 = secondModel.vertexNormals[v];
								VertexNormal offsetVertexNormal2 = secondModel.vertexNormalOffset[v];
								if (x == vertices[v] && z == secondModel.verticesZ[v] && y == secondModel.verticesY[v]
										&& offsetVertexNormal2.magnitude != 0) {
									vertexNormal.x += offsetVertexNormal2.x;
									vertexNormal.y += offsetVertexNormal2.y;
									vertexNormal.z += offsetVertexNormal2.z;
									vertexNormal.magnitude += offsetVertexNormal2.magnitude;
									vertexNormal2.x += offsetVertexNormal.x;
									vertexNormal2.y += offsetVertexNormal.y;
									vertexNormal2.z += offsetVertexNormal.z;
									vertexNormal2.magnitude += offsetVertexNormal.magnitude;
									count++;
									this.anIntArray486[vertex] = this.anInt488;
									this.anIntArray487[v] = this.anInt488;
								}
							}

						}
					}
				}
			}
		}

		if (count < 3 || !flag) {
			return;
		}
		
		for (int triangle = 0; triangle < model.triangleCount; triangle++) {
			if (this.anIntArray486[model.triangleX[triangle]] == this.anInt488
					&& this.anIntArray486[model.triangleY[triangle]] == this.anInt488
					&& this.anIntArray486[model.triangleZ[triangle]] == this.anInt488) {
				model.triangleDrawType[triangle] = -1;
			}
		}

		for (int triangle = 0; triangle < secondModel.triangleCount; triangle++) {
			if (this.anIntArray487[secondModel.triangleX[triangle]] == this.anInt488
					&& this.anIntArray487[secondModel.triangleY[triangle]] == this.anInt488
					&& this.anIntArray487[secondModel.triangleZ[triangle]] == this.anInt488) {
				secondModel.triangleDrawType[triangle] = -1;
			}
		}

	}

	private boolean method320(int x, int y, int z) {
		int l = this.anIntArrayArrayArray445[z][x][y];
		if (l == -anInt448) {
			return false;
		}
		if (l == anInt448) {
			return true;
		}
		int worldX = x << 7;
		int worldY = y << 7;
		if (this.method324(worldX + 1, worldY + 1, this.heightMap[z][x][y])
				&& this.method324((worldX + 128) - 1, worldY + 1, this.heightMap[z][x + 1][y])
				&& this.method324((worldX + 128) - 1, (worldY + 128) - 1, this.heightMap[z][x + 1][y + 1])
				&& this.method324(worldX + 1, (worldY + 128) - 1, this.heightMap[z][x][y + 1])) {
			this.anIntArrayArrayArray445[z][x][y] = anInt448;
			return true;
		} else {
			this.anIntArrayArrayArray445[z][x][y] = -anInt448;
			return false;
		}
	}

	private boolean method321(int x, int y, int z, int wallType) {
		if (!this.method320(x, y, z)) {
			return false;
		}
		int posX = x << 7;
		int posY = y << 7;
		int posZ = this.heightMap[z][x][y] - 1;
		int z1 = posZ - 120;
		int z2 = posZ - 230;
		int z3 = posZ - 238;
		if (wallType < 16) {
			if (wallType == 1) {
				if (posX > cameraPosX) {
					if (!this.method324(posX, posY, posZ)) {
						return false;
					}
					if (!this.method324(posX, posY + 128, posZ)) {
						return false;
					}
				}
				if (z > 0) {
					if (!this.method324(posX, posY, z1)) {
						return false;
					}
					if (!this.method324(posX, posY + 128, z1)) {
						return false;
					}
				}
				return this.method324(posX, posY, z2) && this.method324(posX, posY + 128, z2);
			}
			if (wallType == 2) {
				if (posY < cameraPosY) {
					if (!this.method324(posX, posY + 128, posZ)) {
						return false;
					}
					if (!this.method324(posX + 128, posY + 128, posZ)) {
						return false;
					}
				}
				if (z > 0) {
					if (!this.method324(posX, posY + 128, z1)) {
						return false;
					}
					if (!this.method324(posX + 128, posY + 128, z1)) {
						return false;
					}
				}
				return this.method324(posX, posY + 128, z2) && this.method324(posX + 128, posY + 128, z2);
			}
			if (wallType == 4) {
				if (posX < cameraPosX) {
					if (!this.method324(posX + 128, posY, posZ)) {
						return false;
					}
					if (!this.method324(posX + 128, posY + 128, posZ)) {
						return false;
					}
				}
				if (z > 0) {
					if (!this.method324(posX + 128, posY, z1)) {
						return false;
					}
					if (!this.method324(posX + 128, posY + 128, z1)) {
						return false;
					}
				}
				return this.method324(posX + 128, posY, z2) && this.method324(posX + 128, posY + 128, z2);
			}
			if (wallType == 8) {
				if (posY > cameraPosY) {
					if (!this.method324(posX, posY, posZ)) {
						return false;
					}
					if (!this.method324(posX + 128, posY, posZ)) {
						return false;
					}
				}
				if (z > 0) {
					if (!this.method324(posX, posY, z1)) {
						return false;
					}
					if (!this.method324(posX + 128, posY, z1)) {
						return false;
					}
				}
				return this.method324(posX, posY, z2) && this.method324(posX + 128, posY, z2);
			}
		}
		if (!this.method324(posX + 64, posY + 64, z3)) {
			return false;
		}
		if (wallType == 16) {
			return this.method324(posX, posY + 128, z2);
		}
		if (wallType == 32) {
			return this.method324(posX + 128, posY + 128, z2);
		}
		if (wallType == 64) {
			return this.method324(posX + 128, posY, z2);
		}
		if (wallType == 128) {
			return this.method324(posX, posY, z2);
		} else {
			System.out.println("Warning unsupported wall type");
			return true;
		}
	}

	private boolean method322(int z, int x, int y, int offsetZ) {
		if (!this.method320(x, y, z)) {
			return false;
		}
		int _x = x << 7;
		int _y = y << 7;
		return this.method324(_x + 1, _y + 1, this.heightMap[z][x][y] - offsetZ)
				&& this.method324((_x + 128) - 1, _y + 1, this.heightMap[z][x + 1][y] - offsetZ)
				&& this.method324((_x + 128) - 1, (_y + 128) - 1, this.heightMap[z][x + 1][y + 1] - offsetZ)
				&& this.method324(_x + 1, (_y + 128) - 1, this.heightMap[z][x][y + 1] - offsetZ);
	}

	private boolean method323(int minimumX, int maximumX, int minimumY, int maximumY, int z, int offsetZ) {
		if (minimumX == maximumX && minimumY == maximumY) {
			if (!this.method320(minimumX, minimumY, z)) {
				return false;
			}
			int _x = minimumX << 7;
			int _y = minimumY << 7;
			return this.method324(_x + 1, _y + 1, this.heightMap[z][minimumX][minimumY] - offsetZ)
					&& this.method324((_x + 128) - 1, _y + 1, this.heightMap[z][minimumX + 1][minimumY] - offsetZ)
					&& this.method324((_x + 128) - 1, (_y + 128) - 1, this.heightMap[z][minimumX + 1][minimumY + 1] - offsetZ)
					&& this.method324(_x + 1, (_y + 128) - 1, this.heightMap[z][minimumX][minimumY + 1] - offsetZ);
		}
		for (int x = minimumX; x <= maximumX; x++) {
			for (int y = minimumY; y <= maximumY; y++) {
				if (this.anIntArrayArrayArray445[z][x][y] == -anInt448) {
					return false;
				}
			}

		}

		int _x = (minimumX << 7) + 1;
		int _y = (minimumY << 7) + 2;
		int _z = this.heightMap[z][minimumX][minimumY] - offsetZ;
		if (!this.method324(_x, _y, _z)) {
			return false;
		}
		int x = (maximumX << 7) - 1;
		if (!this.method324(x, _y, _z)) {
			return false;
		}
		int y = (maximumY << 7) - 1;
		return this.method324(_x, y, _z) && this.method324(x, y, _z);
	}

	private boolean method324(int x, int y, int z) {
		for (int c = 0; c < processedCullingClustersPointer; c++) {
			CullingCluster cluster = processedCullingClusters[c];
			if (cluster.tileDistanceEnum == 1) {
				int i1 = cluster.worldStartX - x;
				if (i1 > 0) {
					int j2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i1 >> 8);
					int k3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i1 >> 8);
					int l4 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * i1 >> 8);
					int i6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * i1 >> 8);
					if (y >= j2 && y <= k3 && z >= l4 && z <= i6) {
						return true;
					}
				}
			} else if (cluster.tileDistanceEnum == 2) {
				int j1 = x - cluster.worldStartX;
				if (j1 > 0) {
					int k2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * j1 >> 8);
					int l3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * j1 >> 8);
					int i5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * j1 >> 8);
					int j6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * j1 >> 8);
					if (y >= k2 && y <= l3 && z >= i5 && z <= j6) {
						return true;
					}
				}
			} else if (cluster.tileDistanceEnum == 3) {
				int k1 = cluster.worldStartY - y;
				if (k1 > 0) {
					int l2 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * k1 >> 8);
					int i4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * k1 >> 8);
					int j5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * k1 >> 8);
					int k6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * k1 >> 8);
					if (x >= l2 && x <= i4 && z >= j5 && z <= k6) {
						return true;
					}
				}
			} else if (cluster.tileDistanceEnum == 4) {
				int l1 = y - cluster.worldStartY;
				if (l1 > 0) {
					int i3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * l1 >> 8);
					int j4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * l1 >> 8);
					int k5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * l1 >> 8);
					int l6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * l1 >> 8);
					if (x >= i3 && x <= j4 && z >= k5 && z <= l6) {
						return true;
					}
				}
			} else if (cluster.tileDistanceEnum == 5) {
				int i2 = z - cluster.worldEndZ;
				if (i2 > 0) {
					int j3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * i2 >> 8);
					int k4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * i2 >> 8);
					int l5 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i2 >> 8);
					int i7 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i2 >> 8);
					if (x >= j3 && x <= k4 && y >= l5 && y <= i7) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private int mixColours(int colourA, int colourB) {
		colourB = 127 - colourB;
		colourB = (colourB * (colourA & 0x7f)) / 160;
		if (colourB < 2) {
			colourB = 2;
		} else if (colourB > 126) {
			colourB = 126;
		}
		return (colourA & 0xff80) + colourB;
	}

	private void processCulling() {
		int clusterCount = cullingClusterPointer[plane];
		CullingCluster clusters[] = cullingClusters[plane];
		processedCullingClustersPointer = 0;
		for (int c = 0; c < clusterCount; c++) {
			CullingCluster cluster = clusters[c];
			if (cluster.searchMask == 1) {
				int distanceFromCameraStartX = (cluster.tileStartX - cameraPositionTileX) + 25;
				if (distanceFromCameraStartX < 0 || distanceFromCameraStartX > 50) {
					continue;
				}
				int distanceFromCameraStartY = (cluster.tileStartY - cameraPositionTileY) + 25;
				if (distanceFromCameraStartY < 0) {
					distanceFromCameraStartY = 0;
				}
				int distanceFromCameraEndY = (cluster.tileEndY - cameraPositionTileY) + 25;
				if (distanceFromCameraEndY > 50) {
					distanceFromCameraEndY = 50;
				}
				boolean visible = false;
				while (distanceFromCameraStartY <= distanceFromCameraEndY) {
					if (TILE_VISIBILITY_MAP[distanceFromCameraStartX][distanceFromCameraStartY++]) {
						visible = true;
						break;
					}
				}
				if (!visible) {
					continue;
				}
				int realDistanceFromCameraStartX = cameraPosX - cluster.worldStartX;
				if (realDistanceFromCameraStartX > 32) {
					cluster.tileDistanceEnum = 1;
				} else {
					if (realDistanceFromCameraStartX >= -32) {
						continue;
					}
					cluster.tileDistanceEnum = 2;
					realDistanceFromCameraStartX = -realDistanceFromCameraStartX;
				}
				cluster.worldDistanceFromCameraStartY = (cluster.worldStartY - cameraPosY << 8)
						/ realDistanceFromCameraStartX;
				cluster.worldDistanceFromCameraEndY = (cluster.worldEndY - cameraPosY << 8)
						/ realDistanceFromCameraStartX;
				cluster.worldDistanceFromCameraStartZ = (cluster.worldEndZ - cameraPosZ << 8)
						/ realDistanceFromCameraStartX;
				cluster.worldDistanceFromCameraEndZ = (cluster.worldStartZ - cameraPosZ << 8)
						/ realDistanceFromCameraStartX;
				processedCullingClusters[processedCullingClustersPointer++] = cluster;
				continue;
			}
			if (cluster.searchMask == 2) {
				int distanceFromCameraStartY = (cluster.tileStartY - cameraPositionTileY) + 25;
				if (distanceFromCameraStartY < 0 || distanceFromCameraStartY > 50) {
					continue;
				}
				int distanceFromCameraStartX = (cluster.tileStartX - cameraPositionTileX) + 25;
				if (distanceFromCameraStartX < 0) {
					distanceFromCameraStartX = 0;
				}
				int distanceFromCameraEndX = (cluster.tileEndX - cameraPositionTileX) + 25;
				if (distanceFromCameraEndX > 50) {
					distanceFromCameraEndX = 50;
				}
				boolean visible = false;
				while (distanceFromCameraStartX <= distanceFromCameraEndX) {
					if (TILE_VISIBILITY_MAP[distanceFromCameraStartX++][distanceFromCameraStartY]) {
						visible = true;
						break;
					}
				}
				if (!visible) {
					continue;
				}
				int realDistanceFromCameraStartY = cameraPosY - cluster.worldStartY;
				if (realDistanceFromCameraStartY > 32) {
					cluster.tileDistanceEnum = 3;
				} else {
					if (realDistanceFromCameraStartY >= -32) {
						continue;
					}
					cluster.tileDistanceEnum = 4;
					realDistanceFromCameraStartY = -realDistanceFromCameraStartY;
				}
				cluster.worldDistanceFromCameraStartX = (cluster.worldStartX - cameraPosX << 8)
						/ realDistanceFromCameraStartY;
				cluster.worldDistanceFromCameraEndX = (cluster.worldEndX - cameraPosX << 8)
						/ realDistanceFromCameraStartY;
				cluster.worldDistanceFromCameraStartZ = (cluster.worldEndZ - cameraPosZ << 8)
						/ realDistanceFromCameraStartY;
				cluster.worldDistanceFromCameraEndZ = (cluster.worldStartZ - cameraPosZ << 8)
						/ realDistanceFromCameraStartY;
				processedCullingClusters[processedCullingClustersPointer++] = cluster;
			} else if (cluster.searchMask == 4) {
				int realDistanceFromCameraStartZ = cluster.worldEndZ - cameraPosZ;
				if (realDistanceFromCameraStartZ > 128) {
					int distanceFromCameraStartY = (cluster.tileStartY - cameraPositionTileY) + 25;
					if (distanceFromCameraStartY < 0) {
						distanceFromCameraStartY = 0;
					}
					int distanceFromCameraEndY = (cluster.tileEndY - cameraPositionTileY) + 25;
					if (distanceFromCameraEndY > 50) {
						distanceFromCameraEndY = 50;
					}
					if (distanceFromCameraStartY <= distanceFromCameraEndY) {
						int distanceFromCameraStartX = (cluster.tileStartX - cameraPositionTileX) + 25;
						if (distanceFromCameraStartX < 0) {
							distanceFromCameraStartX = 0;
						}
						int distanceFromCameraEndX = (cluster.tileEndX - cameraPositionTileX) + 25;
						if (distanceFromCameraEndX > 50) {
							distanceFromCameraEndX = 50;
						}
						boolean visible = false;
						label0: for (int x = distanceFromCameraStartX; x <= distanceFromCameraEndX; x++) {
							for (int y = distanceFromCameraStartY; y <= distanceFromCameraEndY; y++) {
								if (!TILE_VISIBILITY_MAP[x][y]) {
									continue;
								}
								visible = true;
								break label0;
							}

						}

						if (visible) {
							cluster.tileDistanceEnum = 5;
							cluster.worldDistanceFromCameraStartX = (cluster.worldStartX - cameraPosX << 8)
									/ realDistanceFromCameraStartZ;
							cluster.worldDistanceFromCameraEndX = (cluster.worldEndX - cameraPosX << 8)
									/ realDistanceFromCameraStartZ;
							cluster.worldDistanceFromCameraStartY = (cluster.worldStartY - cameraPosY << 8)
									/ realDistanceFromCameraStartZ;
							cluster.worldDistanceFromCameraEndY = (cluster.worldEndY - cameraPosY << 8)
									/ realDistanceFromCameraStartZ;
							processedCullingClusters[processedCullingClustersPointer++] = cluster;
						}
					}
				}
			}
		}

	}

	private void remove(InteractiveObject entity) {
		for (int x = entity.tileLeft; x <= entity.tileRight; x++) {
			for (int y = entity.tileTop; y <= entity.tileBottom; y++) {
				Tile tile = this.groundArray[entity.z][x][y];
				if (tile != null) {
					for (int e = 0; e < tile.entityCount; e++) {
						if (tile.interactiveObjects[e] != entity) {
							continue;
						}
						tile.entityCount--;
						for (int e2 = e; e2 < tile.entityCount; e2++) {
							tile.interactiveObjects[e2] = tile.interactiveObjects[e2 + 1];
							tile.interactiveObjectsSize[e2] = tile.interactiveObjectsSize[e2 + 1];
						}

						tile.interactiveObjects[tile.entityCount] = null;
						break;
					}

					tile.interactiveObjectsSizeOR = 0;
					for (int e = 0; e < tile.entityCount; e++) {
						tile.interactiveObjectsSizeOR |= tile.interactiveObjectsSize[e];
					}

				}
			}
		}
	}

	public void removeGroundDecoration(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return;
		}
		tile.groundDecoration = null;
	}

	public void removeGroundItemTile(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile != null) {
			tile.groundItemTile = null;
		}
	}

	public void removeInteractiveObject(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile == null) {
			return;
		}
		for (int e = 0; e < tile.entityCount; e++) {
			InteractiveObject entity = tile.interactiveObjects[e];
			if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
				this.remove(entity);
				return;
			}
		}

	}

	public void removeWallDecoration(int x, int y, int z) {
		Tile tile = this.groundArray[z][x][y];
		if (tile != null) {
			tile.wallDecoration = null;
		}
	}

	public void removeWallObject(int x, int z, int y) {
		Tile tile = this.groundArray[z][x][y];
		if (tile != null) {
			tile.wall = null;
		}
	}

	public void render(int cameraPosX, int cameraPosY, int curveX, int cameraPosZ, int plane, int curveY) {
		if (cameraPosX < 0) {
			cameraPosX = 0;
		} else if (cameraPosX >= this.mapSizeX * 128) {
			cameraPosX = this.mapSizeX * 128 - 1;
		}
		if (cameraPosY < 0) {
			cameraPosY = 0;
		} else if (cameraPosY >= this.mapSizeY * 128) {
			cameraPosY = this.mapSizeY * 128 - 1;
		}
		anInt448++;
		curveSineY = Model.SINE[curveY];
		curveCosineY = Model.COSINE[curveY];
		curveSineX = Model.SINE[curveX];
		curveCosineX = Model.COSINE[curveX];
		TILE_VISIBILITY_MAP = TILE_VISIBILITY_MAPS[(curveY - 128) / 32][curveX / 64];
		WorldController.cameraPosX = cameraPosX;
		WorldController.cameraPosZ = cameraPosZ;
		WorldController.cameraPosY = cameraPosY;
		cameraPositionTileX = cameraPosX / 128;
		cameraPositionTileY = cameraPosY / 128;
		WorldController.plane = plane;
		currentPositionX = cameraPositionTileX - 25;
		if (currentPositionX < 0) {
			currentPositionX = 0;
		}
		currentPositionY = cameraPositionTileY - 25;
		if (currentPositionY < 0) {
			currentPositionY = 0;
		}
		mapBoundsX = cameraPositionTileX + 25;
		if (mapBoundsX > this.mapSizeX) {
			mapBoundsX = this.mapSizeX;
		}
		mapBoundsY = cameraPositionTileY + 25;
		if (mapBoundsY > this.mapSizeY) {
			mapBoundsY = this.mapSizeY;
		}
		this.processCulling();
		anInt446 = 0;
		for (int z = this.currentPositionZ; z < this.mapSizeZ; z++) {
			Tile tiles[][] = this.groundArray[z];
			for (int x = currentPositionX; x < mapBoundsX; x++) {
				for (int y = currentPositionY; y < mapBoundsY; y++) {
					Tile tile = tiles[x][y];
					if (tile != null) {
						if (tile.logicHeight > plane
								|| !TILE_VISIBILITY_MAP[(x - cameraPositionTileX) + 25][(y - cameraPositionTileY) + 25]
										&& this.heightMap[z][x][y] - cameraPosZ < 2000) {
							tile.aBoolean1322 = false;
							tile.aBoolean1323 = false;
							tile.anInt1325 = 0;
						} else {
							tile.aBoolean1322 = true;
							tile.aBoolean1323 = true;
							tile.aBoolean1324 = tile.entityCount > 0;
							anInt446++;
						}
					}
				}

			}

		}

		for (int z = this.currentPositionZ; z < this.mapSizeZ; z++) {
			Tile tiles[][] = this.groundArray[z];
			for (int offsetX = -25; offsetX <= 0; offsetX++) {
				int x = cameraPositionTileX + offsetX;
				int x2 = cameraPositionTileX - offsetX;
				if (x >= currentPositionX || x2 < mapBoundsX) {
					for (int offsetY = -25; offsetY <= 0; offsetY++) {
						int y = cameraPositionTileY + offsetY;
						int y2 = cameraPositionTileY - offsetY;
						if (x >= currentPositionX) {
							if (y >= currentPositionY) {
								Tile tile = tiles[x][y];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, true);
								}
							}
							if (y2 < mapBoundsY) {
								Tile tile = tiles[x][y2];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, true);
								}
							}
						}
						if (x2 < mapBoundsX) {
							if (y >= currentPositionY) {
								Tile tile = tiles[x2][y];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, true);
								}
							}
							if (y2 < mapBoundsY) {
								Tile tile = tiles[x2][y2];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, true);
								}
							}
						}
						if (anInt446 == 0) {
							clicked = false;
							return;
						}
					}

				}
			}

		}

		for (int z = this.currentPositionZ; z < this.mapSizeZ; z++) {
			Tile tiles[][] = this.groundArray[z];
			for (int offsetX = -25; offsetX <= 0; offsetX++) {
				int x = cameraPositionTileX + offsetX;
				int x2 = cameraPositionTileX - offsetX;
				if (x >= currentPositionX || x2 < mapBoundsX) {
					for (int offsetY = -25; offsetY <= 0; offsetY++) {
						int y = cameraPositionTileY + offsetY;
						int y2 = cameraPositionTileY - offsetY;
						if (x >= currentPositionX) {
							if (y >= currentPositionY) {
								Tile tile = tiles[x][y];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, false);
								}
							}
							if (y2 < mapBoundsY) {
								Tile tile = tiles[x][y2];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, false);
								}
							}
						}
						if (x2 < mapBoundsX) {
							if (y >= currentPositionY) {
								Tile tile = tiles[x2][y];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, false);
								}
							}
							if (y2 < mapBoundsY) {
								Tile tile = tiles[x2][y2];
								if (tile != null && tile.aBoolean1322) {
									this.renderTile(tile, false);
								}
							}
						}
						if (anInt446 == 0) {
							clicked = false;
							return;
						}
					}

				}
			}

		}

		clicked = false;
	}

	void renderPlainTile(PlainTile plainTile, int tileX, int tileY, int tileZ, int sinX, int cosineX, int sinY,
			int cosineY) {
		int xC;
		int xA = xC = (tileX << 7) - cameraPosX;
		int yB;
		int yA = yB = (tileY << 7) - cameraPosY;
		int xD;
		int xB = xD = xA + 128;
		int yC;
		int yD = yC = yA + 128;
		int zA = this.heightMap[tileZ][tileX][tileY] - cameraPosZ;
		int zB = this.heightMap[tileZ][tileX + 1][tileY] - cameraPosZ;
		int zC = this.heightMap[tileZ][tileX + 1][tileY + 1] - cameraPosZ;
		int zD = this.heightMap[tileZ][tileX][tileY + 1] - cameraPosZ;
		int temp = yA * sinX + xA * cosineX >> 16;
		yA = yA * cosineX - xA * sinX >> 16;
		xA = temp;
		temp = zA * cosineY - yA * sinY >> 16;
		yA = zA * sinY + yA * cosineY >> 16;
		zA = temp;
		if (yA < 50) {
			return;
		}
		temp = yB * sinX + xB * cosineX >> 16;
		yB = yB * cosineX - xB * sinX >> 16;
		xB = temp;
		temp = zB * cosineY - yB * sinY >> 16;
		yB = zB * sinY + yB * cosineY >> 16;
		zB = temp;
		if (yB < 50) {
			return;
		}
		temp = yD * sinX + xD * cosineX >> 16;
		yD = yD * cosineX - xD * sinX >> 16;
		xD = temp;
		temp = zC * cosineY - yD * sinY >> 16;
		yD = zC * sinY + yD * cosineY >> 16;
		zC = temp;
		if (yD < 50) {
			return;
		}
		temp = yC * sinX + xC * cosineX >> 16;
		yC = yC * cosineX - xC * sinX >> 16;
		xC = temp;
		temp = zD * cosineY - yC * sinY >> 16;
		yC = zD * sinY + yC * cosineY >> 16;
		zD = temp;
		if (yC < 50) {
			return;
		}
		int screenXA = Rasterizer.centreX + (xA << 9) / yA;
		int screenYA = Rasterizer.centreY + (zA << 9) / yA;
		int screenXB = Rasterizer.centreX + (xB << 9) / yB;
		int screenYB = Rasterizer.centreY + (zB << 9) / yB;
		int screenXD = Rasterizer.centreX + (xD << 9) / yD;
		int screenYD = Rasterizer.centreY + (zC << 9) / yD;
		int screenXC = Rasterizer.centreX + (xC << 9) / yC;
		int screenYC = Rasterizer.centreY + (zD << 9) / yC;
		Rasterizer.alpha = 0;
		if ((screenXD - screenXC) * (screenYB - screenYC) - (screenYD - screenYC) * (screenXB - screenXC) > 0) {
			Rasterizer.restrictEdges = screenXD < 0 || screenXC < 0 || screenXB < 0 || screenXD > DrawingArea.centerX
					|| screenXC > DrawingArea.centerX || screenXB > DrawingArea.centerX;
			if (clicked && this.isMouseWithinTriangle(clickX, clickY, screenYD, screenYC, screenYB, screenXD, screenXC,
					screenXB)) {
				clickedTileX = tileX;
				clickedTileY = tileY;
			}
			if (plainTile.texture == -1) {
				if (plainTile.colourD != 0xbc614e) {
					Rasterizer.drawShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB,
							plainTile.colourD, plainTile.colourC, plainTile.colourB);
				}
			} else if (!lowMemory) {
				if (plainTile.flat) {
					Rasterizer.drawTexturedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB,
							plainTile.colourD, plainTile.colourC, plainTile.colourB, xA, xB, xC, zA, zB, zD, yA, yB, yC,
							plainTile.texture);
				} else {
					Rasterizer.drawTexturedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB,
							plainTile.colourD, plainTile.colourC, plainTile.colourB, xD, xC, xB, zC, zD, zB, yD, yC, yB,
							plainTile.texture);
				}
			} else {
				int rgb = textureRGB[plainTile.texture];
				Rasterizer.drawShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB,
						this.mixColours(rgb, plainTile.colourD), this.mixColours(rgb, plainTile.colourC),
						this.mixColours(rgb, plainTile.colourB));
			}
		}
		if ((screenXA - screenXB) * (screenYC - screenYB) - (screenYA - screenYB) * (screenXC - screenXB) > 0) {
			Rasterizer.restrictEdges = screenXA < 0 || screenXB < 0 || screenXC < 0 || screenXA > DrawingArea.centerX
					|| screenXB > DrawingArea.centerX || screenXC > DrawingArea.centerX;
			if (clicked && this.isMouseWithinTriangle(clickX, clickY, screenYA, screenYB, screenYC, screenXA, screenXB,
					screenXC)) {
				clickedTileX = tileX;
				clickedTileY = tileY;
			}
			if (plainTile.texture == -1) {
				if (plainTile.colourA != 0xbc614e) {
					Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
							plainTile.colourA, plainTile.colourB, plainTile.colourC);
				}
			} else {
				if (!lowMemory) {
					Rasterizer.drawTexturedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
							plainTile.colourA, plainTile.colourB, plainTile.colourC, xA, xB, xC, zA, zB, zD, yA, yB, yC,
							plainTile.texture);
					return;
				}
				int rgb = textureRGB[plainTile.texture];
				Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
						this.mixColours(rgb, plainTile.colourA), this.mixColours(rgb, plainTile.colourB),
						this.mixColours(rgb, plainTile.colourC));
			}
		}
	}

	private void renderShapedTile(ShapedTile shapedTile, int tileX, int tileY, int sineX, int cosineX, int sineY,
			int cosineY) {
		int triangleCount = shapedTile.originalVertexX.length;
		for (int triangle = 0; triangle < triangleCount; triangle++) {
			int viewspaceX = shapedTile.originalVertexX[triangle] - cameraPosX;
			int viewspaceY = shapedTile.originalVertexY[triangle] - cameraPosZ;
			int viewspaceZ = shapedTile.originalVertexZ[triangle] - cameraPosY;
			int temp = viewspaceZ * sineX + viewspaceX * cosineX >> 16;
			viewspaceZ = viewspaceZ * cosineX - viewspaceX * sineX >> 16;
			viewspaceX = temp;
			temp = viewspaceY * cosineY - viewspaceZ * sineY >> 16;
			viewspaceZ = viewspaceY * sineY + viewspaceZ * cosineY >> 16;
			viewspaceY = temp;
			if (viewspaceZ < 50) {
				return;
			}
			if (shapedTile.triangleTexture != null) {
				ShapedTile.viewspaceX[triangle] = viewspaceX;
				ShapedTile.viewspaceY[triangle] = viewspaceY;
				ShapedTile.viewspaceZ[triangle] = viewspaceZ;
			}
			ShapedTile.screenX[triangle] = Rasterizer.centreX + (viewspaceX << 9) / viewspaceZ;
			ShapedTile.screenY[triangle] = Rasterizer.centreY + (viewspaceY << 9) / viewspaceZ;
		}

		Rasterizer.alpha = 0;
		triangleCount = shapedTile.triangleA.length;
		for (int triangle = 0; triangle < triangleCount; triangle++) {
			int a = shapedTile.triangleA[triangle];
			int b = shapedTile.triangleB[triangle];
			int c = shapedTile.triangleC[triangle];
			int screenXA = ShapedTile.screenX[a];
			int screenXB = ShapedTile.screenX[b];
			int screenXC = ShapedTile.screenX[c];
			int screenYA = ShapedTile.screenY[a];
			int screenYB = ShapedTile.screenY[b];
			int screenYC = ShapedTile.screenY[c];
			if ((screenXA - screenXB) * (screenYC - screenYB) - (screenYA - screenYB) * (screenXC - screenXB) > 0) {
				Rasterizer.restrictEdges = screenXA < 0 || screenXB < 0 || screenXC < 0
						|| screenXA > DrawingArea.centerX || screenXB > DrawingArea.centerX
						|| screenXC > DrawingArea.centerX;
				if (clicked && this.isMouseWithinTriangle(clickX, clickY, screenYA, screenYB, screenYC, screenXA, screenXB,
						screenXC)) {
					clickedTileX = tileX;
					clickedTileY = tileY;
				}
				if (shapedTile.triangleTexture == null || shapedTile.triangleTexture[triangle] == -1) {
					if (shapedTile.triangleHSLA[triangle] != 0xbc614e) {
						Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
								shapedTile.triangleHSLA[triangle], shapedTile.triangleHSLB[triangle],
								shapedTile.triangleHSLC[triangle]);
					}
				} else if (!lowMemory) {
					if (shapedTile.flat) {
						Rasterizer.drawTexturedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
								shapedTile.triangleHSLA[triangle], shapedTile.triangleHSLB[triangle],
								shapedTile.triangleHSLC[triangle], ShapedTile.viewspaceX[0], ShapedTile.viewspaceX[1],
								ShapedTile.viewspaceX[3], ShapedTile.viewspaceY[0], ShapedTile.viewspaceY[1],
								ShapedTile.viewspaceY[3], ShapedTile.viewspaceZ[0], ShapedTile.viewspaceZ[1],
								ShapedTile.viewspaceZ[3], shapedTile.triangleTexture[triangle]);
					} else {
						Rasterizer.drawTexturedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
								shapedTile.triangleHSLA[triangle], shapedTile.triangleHSLB[triangle],
								shapedTile.triangleHSLC[triangle], ShapedTile.viewspaceX[a], ShapedTile.viewspaceX[b],
								ShapedTile.viewspaceX[c], ShapedTile.viewspaceY[a], ShapedTile.viewspaceY[b],
								ShapedTile.viewspaceY[c], ShapedTile.viewspaceZ[a], ShapedTile.viewspaceZ[b],
								ShapedTile.viewspaceZ[c], shapedTile.triangleTexture[triangle]);
					}
				} else {
					int rgb = textureRGB[shapedTile.triangleTexture[triangle]];
					Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
							this.mixColours(rgb, shapedTile.triangleHSLA[triangle]),
							this.mixColours(rgb, shapedTile.triangleHSLB[triangle]),
							this.mixColours(rgb, shapedTile.triangleHSLC[triangle]));
				}
			}
		}

	}

	private void renderTile(Tile _tile, boolean flag) {
		tileList.pushBack(_tile);
		do {
			Tile groundTile;
			do {
				groundTile = (Tile) tileList.popFront();
				if (groundTile == null) {
					return;
				}
			} while (!groundTile.aBoolean1323);
			int x = groundTile.x;
			int y = groundTile.y;
			int z = groundTile.z;
			int l = groundTile.anInt1310;
			Tile tiles[][] = this.groundArray[z];
			if (groundTile.aBoolean1322) {
				if (flag) {
					if (z > 0) {
						Tile tile = this.groundArray[z - 1][x][y];
						if (tile != null && tile.aBoolean1323) {
							continue;
						}
					}
					if (x <= cameraPositionTileX && x > currentPositionX) {
						Tile tile = tiles[x - 1][y];
						if (tile != null && tile.aBoolean1323
								&& (tile.aBoolean1322 || (groundTile.interactiveObjectsSizeOR & 1) == 0)) {
							continue;
						}
					}
					if (x >= cameraPositionTileX && x < mapBoundsX - 1) {
						Tile tile = tiles[x + 1][y];
						if (tile != null && tile.aBoolean1323
								&& (tile.aBoolean1322 || (groundTile.interactiveObjectsSizeOR & 4) == 0)) {
							continue;
						}
					}
					if (y <= cameraPositionTileY && y > currentPositionY) {
						Tile tile = tiles[x][y - 1];
						if (tile != null && tile.aBoolean1323
								&& (tile.aBoolean1322 || (groundTile.interactiveObjectsSizeOR & 8) == 0)) {
							continue;
						}
					}
					if (y >= cameraPositionTileY && y < mapBoundsY - 1) {
						Tile tile = tiles[x][y + 1];
						if (tile != null && tile.aBoolean1323
								&& (tile.aBoolean1322 || (groundTile.interactiveObjectsSizeOR & 2) == 0)) {
							continue;
						}
					}
				} else {
					flag = true;
				}
				groundTile.aBoolean1322 = false;
				if (groundTile.tileBelow != null) {
					Tile tile = groundTile.tileBelow;
					if (tile.plainTile != null) {
						if (!this.method320(x, y, 0)) {
							this.renderPlainTile(tile.plainTile, x, y, 0, curveSineX, curveCosineX, curveSineY,
									curveCosineY);
						}
					} else if (tile.shapedTile != null && !this.method320(x, y, 0)) {
						this.renderShapedTile(tile.shapedTile, x, y, curveSineX, curveCosineX, curveSineY, curveCosineY);
					}
					Wall wallObject = tile.wall;
					if (wallObject != null) {
						wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
					for (int e = 0; e < tile.entityCount; e++) {
						InteractiveObject entity = tile.interactiveObjects[e];
						if (entity != null) {
							entity.renderable.renderAtPoint(entity.rotation, curveSineY, curveCosineY, curveSineX,
									curveCosineX, entity.worldX - cameraPosX, entity.worldZ - cameraPosZ,
									entity.worldY - cameraPosY, entity.uid);
						}
					}

				}
				boolean flag1 = false;
				if (groundTile.plainTile != null) {
					if (!this.method320(x, y, l)) {
						flag1 = true;
						this.renderPlainTile(groundTile.plainTile, x, y, l, curveSineX, curveCosineX, curveSineY,
								curveCosineY);
					}
				} else if (groundTile.shapedTile != null && !this.method320(x, y, l)) {
					flag1 = true;
					this.renderShapedTile(groundTile.shapedTile, x, y, curveSineX, curveCosineX, curveSineY, curveCosineY);
				}
				int j1 = 0;
				int j2 = 0;
				Wall wallObject = groundTile.wall;
				WallDecoration wallDecoration = groundTile.wallDecoration;
				if (wallObject != null || wallDecoration != null) {
					if (cameraPositionTileX == x) {
						j1++;
					} else if (cameraPositionTileX < x) {
						j1 += 2;
					}
					if (cameraPositionTileY == y) {
						j1 += 3;
					} else if (cameraPositionTileY > y) {
						j1 += 6;
					}
					j2 = anIntArray478[j1];
					groundTile.anInt1328 = anIntArray480[j1];
				}
				if (wallObject != null) {
					if ((wallObject.orientation & anIntArray479[j1]) != 0) {
						if (wallObject.orientation == 16) {
							groundTile.anInt1325 = 3;
							groundTile.anInt1326 = anIntArray481[j1];
							groundTile.anInt1327 = 3 - groundTile.anInt1326;
						} else if (wallObject.orientation == 32) {
							groundTile.anInt1325 = 6;
							groundTile.anInt1326 = anIntArray482[j1];
							groundTile.anInt1327 = 6 - groundTile.anInt1326;
						} else if (wallObject.orientation == 64) {
							groundTile.anInt1325 = 12;
							groundTile.anInt1326 = anIntArray483[j1];
							groundTile.anInt1327 = 12 - groundTile.anInt1326;
						} else {
							groundTile.anInt1325 = 9;
							groundTile.anInt1326 = anIntArray484[j1];
							groundTile.anInt1327 = 9 - groundTile.anInt1326;
						}
					} else {
						groundTile.anInt1325 = 0;
					}
					if ((wallObject.orientation & j2) != 0 && !this.method321(x, y, l, wallObject.orientation)) {
						wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
					if ((wallObject.orientation2 & j2) != 0 && !this.method321(x, y, l, wallObject.orientation2)) {
						wallObject.secondary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
				}
				if (wallDecoration != null && !this.method322(l, x, y, wallDecoration.renderable.modelHeight)) {
					if ((wallDecoration.configBits & j2) != 0) {
						wallDecoration.renderable.renderAtPoint(wallDecoration.face, curveSineY, curveCosineY,
								curveSineX, curveCosineX, wallDecoration.x - cameraPosX, wallDecoration.z - cameraPosZ,
								wallDecoration.y - cameraPosY, wallDecoration.uid);
					} else if ((wallDecoration.configBits & 0x300) != 0) {
						int j4 = wallDecoration.x - cameraPosX;
						int l5 = wallDecoration.z - cameraPosZ;
						int k6 = wallDecoration.y - cameraPosY;
						int i8 = wallDecoration.face;
						int k9;
						if (i8 == 1 || i8 == 2) {
							k9 = -j4;
						} else {
							k9 = j4;
						}
						int k10;
						if (i8 == 2 || i8 == 3) {
							k10 = -k6;
						} else {
							k10 = k6;
						}
						if ((wallDecoration.configBits & 0x100) != 0 && k10 < k9) {
							int i11 = j4 + faceOffsetX2[i8];
							int k11 = k6 + faceOffsetY2[i8];
							wallDecoration.renderable.renderAtPoint(i8 * 512 + 256, curveSineY, curveCosineY,
									curveSineX, curveCosineX, i11, l5, k11, wallDecoration.uid);
						}
						if ((wallDecoration.configBits & 0x200) != 0 && k10 > k9) {
							int j11 = j4 + faceOffsetX3[i8];
							int l11 = k6 + faceOffsetY3[i8];
							wallDecoration.renderable.renderAtPoint(i8 * 512 + 1280 & 0x7ff, curveSineY, curveCosineY,
									curveSineX, curveCosineX, j11, l5, l11, wallDecoration.uid);
						}
					}
				}
				if (flag1) {
					GroundDecoration groundDecoration = groundTile.groundDecoration;
					if (groundDecoration != null) {
						groundDecoration.renderable.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								groundDecoration.x - cameraPosX, groundDecoration.z - cameraPosZ,
								groundDecoration.y - cameraPosY, groundDecoration.uid);
					}
					GroundItemTile groundItemTile = groundTile.groundItemTile;
					if (groundItemTile != null && groundItemTile.anInt52 == 0) {
						if (groundItemTile.secondGroundItem != null) {
							groundItemTile.secondGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX,
									curveCosineX, groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ,
									groundItemTile.y - cameraPosY, groundItemTile.uid);
						}
						if (groundItemTile.thirdGroundItem != null) {
							groundItemTile.thirdGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX,
									curveCosineX, groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ,
									groundItemTile.y - cameraPosY, groundItemTile.uid);
						}
						if (groundItemTile.firstGroundItem != null) {
							groundItemTile.firstGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX,
									curveCosineX, groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ,
									groundItemTile.y - cameraPosY, groundItemTile.uid);
						}
					}
				}
				int interactiveObjectsSizeOR = groundTile.interactiveObjectsSizeOR;
				if (interactiveObjectsSizeOR != 0) {
					if (x < cameraPositionTileX && (interactiveObjectsSizeOR & 4) != 0) {
						Tile tile = tiles[x + 1][y];
						if (tile != null && tile.aBoolean1323) {
							tileList.pushBack(tile);
						}
					}
					if (y < cameraPositionTileY && (interactiveObjectsSizeOR & 2) != 0) {
						Tile tile = tiles[x][y + 1];
						if (tile != null && tile.aBoolean1323) {
							tileList.pushBack(tile);
						}
					}
					if (x > cameraPositionTileX && (interactiveObjectsSizeOR & 1) != 0) {
						Tile tile = tiles[x - 1][y];
						if (tile != null && tile.aBoolean1323) {
							tileList.pushBack(tile);
						}
					}
					if (y > cameraPositionTileY && (interactiveObjectsSizeOR & 8) != 0) {
						Tile tile = tiles[x][y - 1];
						if (tile != null && tile.aBoolean1323) {
							tileList.pushBack(tile);
						}
					}
				}
			}
			if (groundTile.anInt1325 != 0) {
				boolean flag2 = true;
				for (int e = 0; e < groundTile.entityCount; e++) {
					if (groundTile.interactiveObjects[e].anInt528 == anInt448
							|| (groundTile.interactiveObjectsSize[e] & groundTile.anInt1325) != groundTile.anInt1326) {
						continue;
					}
					flag2 = false;
					break;
				}

				if (flag2) {
					Wall wallObject = groundTile.wall;
					if (!this.method321(x, y, l, wallObject.orientation)) {
						wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
					groundTile.anInt1325 = 0;
				}
			}
			if (groundTile.aBoolean1324) {
				try {
					int entityCount = groundTile.entityCount;
					groundTile.aBoolean1324 = false;
					int l1 = 0;
					label0: for (int e = 0; e < entityCount; e++) {
						InteractiveObject entity = groundTile.interactiveObjects[e];
						if (entity.anInt528 == anInt448) {
							continue;
						}
						for (int _x = entity.tileLeft; _x <= entity.tileRight; _x++) {
							for (int _y = entity.tileTop; _y <= entity.tileBottom; _y++) {
								Tile tile = tiles[_x][_y];
								if (tile.aBoolean1322) {
									groundTile.aBoolean1324 = true;
								} else {
									if (tile.anInt1325 == 0) {
										continue;
									}
									int l6 = 0;
									if (_x > entity.tileLeft) {
										l6++;
									}
									if (_x < entity.tileRight) {
										l6 += 4;
									}
									if (_y > entity.tileTop) {
										l6 += 8;
									}
									if (_y < entity.tileBottom) {
										l6 += 2;
									}
									if ((l6 & tile.anInt1325) != groundTile.anInt1327) {
										continue;
									}
									groundTile.aBoolean1324 = true;
								}
								continue label0;
							}

						}

						interactiveObjects[l1++] = entity;
						int i5 = cameraPositionTileX - entity.tileLeft;
						int i6 = entity.tileRight - cameraPositionTileX;
						if (i6 > i5) {
							i5 = i6;
						}
						int i7 = cameraPositionTileY - entity.tileTop;
						int j8 = entity.tileBottom - cameraPositionTileY;
						if (j8 > i7) {
							entity.anInt527 = i5 + j8;
						} else {
							entity.anInt527 = i5 + i7;
						}
					}

					while (l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for (int j5 = 0; j5 < l1; j5++) {
							InteractiveObject entity = interactiveObjects[j5];
							if (entity.anInt528 != anInt448) {
								if (entity.anInt527 > i3) {
									i3 = entity.anInt527;
									l3 = j5;
								} else if (entity.anInt527 == i3) {
									int j7 = entity.worldX - cameraPosX;
									int k8 = entity.worldY - cameraPosY;
									int l9 = interactiveObjects[l3].worldX - cameraPosX;
									int l10 = interactiveObjects[l3].worldY - cameraPosY;
									if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10) {
										l3 = j5;
									}
								}
							}
						}

						if (l3 == -1) {
							break;
						}
						InteractiveObject entity = interactiveObjects[l3];
						entity.anInt528 = anInt448;

						if (!this.method323(entity.tileLeft, entity.tileRight, entity.tileTop, entity.tileBottom, l,
								entity.renderable.modelHeight)) {
							entity.renderable.renderAtPoint(entity.rotation, curveSineY, curveCosineY, curveSineX,
									curveCosineX, entity.worldX - cameraPosX, entity.worldZ - cameraPosZ,
									entity.worldY - cameraPosY, entity.uid);
						}

						for (int _x = entity.tileLeft; _x <= entity.tileRight; _x++) {
							for (int _y = entity.tileTop; _y <= entity.tileBottom; _y++) {
								Tile tile = tiles[_x][_y];
								if (tile.anInt1325 != 0) {
									tileList.pushBack(tile);
								} else if ((_x != x || _y != y) && tile.aBoolean1323) {
									tileList.pushBack(tile);
								}
							}

						}

					}
					if (groundTile.aBoolean1324) {
						continue;
					}
				} catch (Exception _ex) {
					groundTile.aBoolean1324 = false;
				}
			}
			if (!groundTile.aBoolean1323 || groundTile.anInt1325 != 0) {
				continue;
			}
			if (x <= cameraPositionTileX && x > currentPositionX) {
				Tile tile = tiles[x - 1][y];
				if (tile != null && tile.aBoolean1323) {
					continue;
				}
			}
			if (x >= cameraPositionTileX && x < mapBoundsX - 1) {
				Tile tile = tiles[x + 1][y];
				if (tile != null && tile.aBoolean1323) {
					continue;
				}
			}
			if (y <= cameraPositionTileY && y > currentPositionY) {
				Tile tile = tiles[x][y - 1];
				if (tile != null && tile.aBoolean1323) {
					continue;
				}
			}
			if (y >= cameraPositionTileY && y < mapBoundsY - 1) {
				Tile tile = tiles[x][y + 1];
				if (tile != null && tile.aBoolean1323) {
					continue;
				}
			}
			groundTile.aBoolean1323 = false;
			anInt446--;
			GroundItemTile groundItemTile = groundTile.groundItemTile;
			if (groundItemTile != null && groundItemTile.anInt52 != 0) {
				if (groundItemTile.secondGroundItem != null) {
					groundItemTile.secondGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
							groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ - groundItemTile.anInt52,
							groundItemTile.y - cameraPosY, groundItemTile.uid);
				}
				if (groundItemTile.thirdGroundItem != null) {
					groundItemTile.thirdGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
							groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ - groundItemTile.anInt52,
							groundItemTile.y - cameraPosY, groundItemTile.uid);
				}
				if (groundItemTile.firstGroundItem != null) {
					groundItemTile.firstGroundItem.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
							groundItemTile.x - cameraPosX, groundItemTile.z - cameraPosZ - groundItemTile.anInt52,
							groundItemTile.y - cameraPosY, groundItemTile.uid);
				}
			}
			if (groundTile.anInt1328 != 0) {
				WallDecoration wallDecoration = groundTile.wallDecoration;
				if (wallDecoration != null && !this.method322(l, x, y, wallDecoration.renderable.modelHeight)) {
					if ((wallDecoration.configBits & groundTile.anInt1328) != 0) {
						wallDecoration.renderable.renderAtPoint(wallDecoration.face, curveSineY, curveCosineY,
								curveSineX, curveCosineX, wallDecoration.x - cameraPosX, wallDecoration.z - cameraPosZ,
								wallDecoration.y - cameraPosY, wallDecoration.uid);
					} else if ((wallDecoration.configBits & 0x300) != 0) {
						int l2 = wallDecoration.x - cameraPosX;
						int j3 = wallDecoration.z - cameraPosZ;
						int i4 = wallDecoration.y - cameraPosY;
						int face = wallDecoration.face;
						int j6;
						if (face == 1 || face == 2) {
							j6 = -l2;
						} else {
							j6 = l2;
						}
						int l7;
						if (face == 2 || face == 3) {
							l7 = -i4;
						} else {
							l7 = i4;
						}
						if ((wallDecoration.configBits & 0x100) != 0 && l7 >= j6) {
							int i9 = l2 + faceOffsetX2[face];
							int i10 = i4 + faceOffsetY2[face];
							wallDecoration.renderable.renderAtPoint(face * 512 + 256, curveSineY, curveCosineY,
									curveSineX, curveCosineX, i9, j3, i10, wallDecoration.uid);
						}
						if ((wallDecoration.configBits & 0x200) != 0 && l7 <= j6) {
							int j9 = l2 + faceOffsetX3[face];
							int j10 = i4 + faceOffsetY3[face];
							wallDecoration.renderable.renderAtPoint(face * 512 + 1280 & 0x7ff, curveSineY, curveCosineY,
									curveSineX, curveCosineX, j9, j3, j10, wallDecoration.uid);
						}
					}
				}
				Wall wallObject = groundTile.wall;
				if (wallObject != null) {
					if ((wallObject.orientation2 & groundTile.anInt1328) != 0
							&& !this.method321(x, y, l, wallObject.orientation2)) {
						wallObject.secondary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
					if ((wallObject.orientation & groundTile.anInt1328) != 0
							&& !this.method321(x, y, l, wallObject.orientation)) {
						wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
								wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
								wallObject.uid);
					}
				}
			}
			if (z < this.mapSizeZ - 1) {
				Tile tile = this.groundArray[z + 1][x][y];
				if (tile != null && tile.aBoolean1323) {
					tileList.pushBack(tile);
				}
			}
			if (x < cameraPositionTileX) {
				Tile tile = tiles[x + 1][y];
				if (tile != null && tile.aBoolean1323) {
					tileList.pushBack(tile);
				}
			}
			if (y < cameraPositionTileY) {
				Tile tile = tiles[x][y + 1];
				if (tile != null && tile.aBoolean1323) {
					tileList.pushBack(tile);
				}
			}
			if (x > cameraPositionTileX) {
				Tile tile = tiles[x - 1][y];
				if (tile != null && tile.aBoolean1323) {
					tileList.pushBack(tile);
				}
			}
			if (y > cameraPositionTileY) {
				Tile tile = tiles[x][y - 1];
				if (tile != null && tile.aBoolean1323) {
					tileList.pushBack(tile);
				}
			}
		} while (true);
	}

	public void renderTile(int plane, int x, int y, int clippingPath, int clippingPathRotation, int textureId,
			int vertexHeightSW, int vertexHeightSE, int vertexHeightNE, int vertexHeightNW, int k2, int l2, int i3,
			int j3, int k3, int l3, int i4, int j4, int k4, int l4) {
		if (clippingPath == 0) {
			PlainTile tile = new PlainTile(k2, l2, j3, i3, k4, -1, false);
			for (int _z = plane; _z >= 0; _z--) {
				if (this.groundArray[_z][x][y] == null) {
					this.groundArray[_z][x][y] = new Tile(_z, x, y);
				}
			}

			this.groundArray[plane][x][y].plainTile = tile;
			return;
		}
		if (clippingPath == 1) {
			PlainTile tile = new PlainTile(k3, l3, j4, i4, l4, textureId, vertexHeightSW == vertexHeightSE
					&& vertexHeightSW == vertexHeightNE && vertexHeightSW == vertexHeightNW);
			for (int _z = plane; _z >= 0; _z--) {
				if (this.groundArray[_z][x][y] == null) {
					this.groundArray[_z][x][y] = new Tile(_z, x, y);
				}
			}

			this.groundArray[plane][x][y].plainTile = tile;
			return;
		}
		ShapedTile tile = new ShapedTile(x, vertexHeightSW, vertexHeightSE, vertexHeightNW, vertexHeightNE, y,
				clippingPathRotation, textureId, clippingPath, k2, k3, l2, l3, j3, j4, i3, i4, l4, k4);
		for (int _z = plane; _z >= 0; _z--) {
			if (this.groundArray[_z][x][y] == null) {
				this.groundArray[_z][x][y] = new Tile(_z, x, y);
			}
		}

		this.groundArray[plane][x][y].shapedTile = tile;
	}

	public void request2DTrace(int x, int y) {
		clicked = true;
		clickX = x;
		clickY = y;
		clickedTileX = -1;
		clickedTileY = -1;
	}

	public void setHeightLevel(int z) {
		this.currentPositionZ = z;
		for (int x = 0; x < this.mapSizeX; x++) {
			for (int y = 0; y < this.mapSizeY; y++) {
				if (this.groundArray[z][x][y] == null) {
					this.groundArray[z][x][y] = new Tile(z, x, y);
				}
			}

		}

	}

	public void setTileLogicHeight(int x, int y, int z, int logicHeight) {
		Tile tile = this.groundArray[z][x][y];
		if (tile != null) {
			this.groundArray[z][x][y].logicHeight = logicHeight;
		}
	}

	public void shadeModels(int y, int x, int z) {
		int lightness = 64;// was parameter
		int magnitudeMultiplier = 768;// was parameter
		int distanceFromOrigin = (int) Math.sqrt(x * x + y * y + z * z);
		int magnitude = magnitudeMultiplier * distanceFromOrigin >> 8;
		for (int _z = 0; _z < this.mapSizeZ; _z++) {
			for (int _x = 0; _x < this.mapSizeX; _x++) {
				for (int _y = 0; _y < this.mapSizeY; _y++) {
					Tile tile = this.groundArray[_z][_x][_y];
					if (tile != null) {
						Wall wallObject = tile.wall;
						if (wallObject != null && wallObject.primary != null
								&& wallObject.primary.vertexNormals != null) {
							this.method307(_z, 1, 1, _x, _y, (Model) wallObject.primary);
							if (wallObject.secondary != null && wallObject.secondary.vertexNormals != null) {
								this.method307(_z, 1, 1, _x, _y, (Model) wallObject.secondary);
								this.mergeNormals((Model) wallObject.primary, (Model) wallObject.secondary, 0, 0, 0,
										false);
								((Model) wallObject.secondary).handleShading(lightness, magnitude, x, y, z);
							}
							((Model) wallObject.primary).handleShading(lightness, magnitude, x, y, z);
						}
						for (int e = 0; e < tile.entityCount; e++) {
							InteractiveObject entity = tile.interactiveObjects[e];
							if (entity != null && entity.renderable != null
									&& entity.renderable.vertexNormals != null) {
								this.method307(_z, (entity.tileRight - entity.tileLeft) + 1,
										(entity.tileBottom - entity.tileTop) + 1, _x, _y, (Model) entity.renderable);
								((Model) entity.renderable).handleShading(lightness, magnitude, x, y, z);
							}
						}

						GroundDecoration groundDecoration = tile.groundDecoration;
						if (groundDecoration != null && groundDecoration.renderable.vertexNormals != null) {
							this.method306((Model) groundDecoration.renderable, _x, _y, _z);
							((Model) groundDecoration.renderable).handleShading(lightness, magnitude, x, y, z);
						}
					}
				}
			}
		}
	}
}
