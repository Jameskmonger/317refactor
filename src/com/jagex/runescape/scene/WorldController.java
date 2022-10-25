package com.jagex.runescape.scene;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.scene.object.GroundDecoration;
import com.jagex.runescape.scene.object.GroundItemTile;
import com.jagex.runescape.scene.object.Wall;
import com.jagex.runescape.scene.object.WallDecoration;
import com.jagex.runescape.scene.tile.PlainTile;
import com.jagex.runescape.scene.tile.ShapedTile;
import com.jagex.runescape.scene.tile.Tile;

public final class WorldController {

    public static void createCullingCluster(final int z, final int highestX, final int lowestX, final int highestY, final int lowestY, final int highestZ,
                                            final int lowestZ, final int searchMask) {
        final CullingCluster cullingCluster = new CullingCluster();
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

    private static boolean onScreen(final int x, final int y, final int z) {
        final int l = y * curveSineX + x * curveCosineX >> 16;
        final int i1 = y * curveCosineX - x * curveSineX >> 16;
        final int j1 = z * curveSineY + i1 * curveCosineY >> 16;
        final int k1 = z * curveCosineY - i1 * curveSineY >> 16;
        if (j1 < 50 || j1 > 3500) {
            return false;
        }
        final int l1 = midX + (l << 9) / j1;
        final int i2 = midY + (k1 << 9) / j1;
        return l1 >= left && l1 <= right && i2 >= top && i2 <= bottom;
    }

    public static void setupViewport(final int i, final int j, final int viewportWidth, final int viewportHeight, final int[] ai) {
        left = 0;
        top = 0;
        right = viewportWidth;
        bottom = viewportHeight;
        midX = viewportWidth / 2;
        midY = viewportHeight / 2;
        final boolean[][][][] tileOnScreen = new boolean[9][32][53][53];
        for (int angleY = 128; angleY <= 384; angleY += 32) {
            for (int angleX = 0; angleX < 2048; angleX += 64) {
                curveSineY = Model.SINE[angleY];
                curveCosineY = Model.COSINE[angleY];
                curveSineX = Model.SINE[angleX];
                curveCosineX = Model.COSINE[angleX];
                final int anglePointerY = (angleY - 128) / 32;
                final int anglePointerX = angleX / 64;
                for (int x = -26; x <= 26; x++) {
                    for (int y = -26; y <= 26; y++) {
                        final int worldX = x * 128;
                        final int worldY = y * 128;
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
                        label0:
                        for (int f = -1; f <= 1; f++) {
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

    private final int[][][] vertexHeights;

    private final Tile[][][] tileArray;

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

    private static final int[] faceOffsetX2 = {53, -53, -53, 53};

    private static final int[] faceOffsetY2 = {-53, -53, 53, 53};

    private static final int[] faceOffsetX3 = {-45, 45, 45, -45};

    private static final int[] faceOffsetY3 = {45, 45, -45, -45};

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

    private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110, 137, 205, 76};

    private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80, 48, 160};

    private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};

    private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};

    private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};

    private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};

    private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};

    private static final int[] textureRGB = {41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086,
        41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41,
        41, 41, 41, 41, 41, 3131, 41, 41, 41};
    private final int[] anIntArray486;
    private final int[] anIntArray487;
    private int anInt488;
    private final int[][] tileShapePoints = {new int[16], {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
        {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}, {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
        {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
    private final int[][] tileShapeIndices = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
        {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
        {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
        {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
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

    public WorldController(final int[][][] vertexHeights) {
        final int length = 104;// was parameter
        final int width = 104;// was parameter
        final int height = 4;// was parameter
        this.interactiveObjectCache = new InteractiveObject[5000];
        this.anIntArray486 = new int[10000];
        this.anIntArray487 = new int[10000];
        this.mapSizeZ = height;
        this.mapSizeX = width;
        this.mapSizeY = length;
        this.tileArray = new Tile[height][width][length];
        this.anIntArrayArrayArray445 = new int[height][width + 1][length + 1];
        this.vertexHeights = vertexHeights;
        this.initToNull();
    }

    public boolean addEntity(final int x, final int y, final int z, final int worldX, final int worldY, final int worldZ, final int rotation, final int tileWidth,
                             final int tileHeight, final Animable entity, final int uid) {
        return entity == null || this.addEntityC(x, y, z, worldX, worldY, worldZ, rotation, (tileWidth - y) + 1,
            (tileHeight - x) + 1, uid, entity, true, (byte) 0);
    }

    public boolean addEntity(final int z, final int worldX, final int worldY, final int worldZ, final int yaw, final Animable entity, final int uid, final int delta,
                             final boolean accountForYaw) {
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

    public boolean addEntityB(final int x, final int y, final int z, final int worldZ, final int rotation, final int tileWidth, final int tileHeight, final int uid,
                              final Animable entity, final byte objConf) {
        if (entity == null) {
            return true;
        } else {
            final int worldX = x * 128 + 64 * tileHeight;
            final int worldY = y * 128 + 64 * tileWidth;
            return this.addEntityC(x, y, z, worldX, worldY, worldZ, rotation, tileWidth, tileHeight, uid, entity, false,
                objConf);
        }
    }

    private boolean addEntityC(final int minX, final int minY, final int z, final int worldX, final int worldY, final int worldZ, final int rotation, final int tileWidth,
                               final int tileHeight, final int uid, final Animable renderable, final boolean isDynamic, final byte objConf) {
        for (int x = minX; x < minX + tileHeight; x++) {
            for (int y = minY; y < minY + tileWidth; y++) {
                if (x < 0 || y < 0 || x >= this.mapSizeX || y >= this.mapSizeY) {
                    return false;
                }
                final Tile tile = this.tileArray[z][x][y];
                if (tile != null && tile.entityCount >= 5) {
                    return false;
                }
            }

        }

        final InteractiveObject entity = new InteractiveObject();
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
                    if (this.tileArray[_z][x][y] == null) {
                        this.tileArray[_z][x][y] = new Tile(_z, x, y);
                    }
                }

                final Tile tile = this.tileArray[z][x][y];
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

    public void addGroundDecoration(final int x, final int y, final int z, final int drawHeight, final int uid, final Animable renderable, final byte objConf) {
        if (renderable == null) {
            return;
        }
        final GroundDecoration groundDecoration = new GroundDecoration();
        groundDecoration.renderable = renderable;
        groundDecoration.x = x * 128 + 64;
        groundDecoration.y = y * 128 + 64;
        groundDecoration.z = drawHeight;
        groundDecoration.uid = uid;
        groundDecoration.objConf = objConf;
        if (this.tileArray[z][x][y] == null) {
            this.tileArray[z][x][y] = new Tile(z, x, y);
        }
        this.tileArray[z][x][y].groundDecoration = groundDecoration;
    }

    public void addGroundItemTile(final int x, final int y, final int z, final int drawHeight, final int uid, final Animable firstGroundItem,
                                  final Animable secondGroundItem, final Animable thirdGroundItem) {
        final GroundItemTile groundItemTile = new GroundItemTile();
        groundItemTile.firstGroundItem = firstGroundItem;
        groundItemTile.x = x * 128 + 64;
        groundItemTile.y = y * 128 + 64;
        groundItemTile.z = drawHeight;
        groundItemTile.uid = uid;
        groundItemTile.secondGroundItem = secondGroundItem;
        groundItemTile.thirdGroundItem = thirdGroundItem;
        int j1 = 0;
        final Tile tile = this.tileArray[z][x][y];
        if (tile != null) {
            for (int e = 0; e < tile.entityCount; e++) {
                if (tile.interactiveObjects[e].renderable instanceof Model) {
                    final int l1 = ((Model) tile.interactiveObjects[e].renderable).anInt1654;
                    if (l1 > j1) {
                        j1 = l1;
                    }
                }
            }

        }
        groundItemTile.anInt52 = j1;
        if (this.tileArray[z][x][y] == null) {
            this.tileArray[z][x][y] = new Tile(z, x, y);
        }
        this.tileArray[z][x][y].groundItemTile = groundItemTile;
    }

    public void addWallDecoration(final int x, final int y, final int z, final int drawHeight, final int offsetX, final int offsetY, final int face, final int uid,
                                  final Animable renderable, final byte objConf, final int faceBits) {
        if (renderable == null) {
            return;
        }
        final WallDecoration wallDecoration = new WallDecoration();
        wallDecoration.uid = uid;
        wallDecoration.objConf = objConf;
        wallDecoration.x = x * 128 + 64 + offsetX;
        wallDecoration.y = y * 128 + 64 + offsetY;
        wallDecoration.z = drawHeight;
        wallDecoration.renderable = renderable;
        wallDecoration.configBits = faceBits;
        wallDecoration.face = face;
        for (int _z = z; _z >= 0; _z--) {
            if (this.tileArray[_z][x][y] == null) {
                this.tileArray[_z][x][y] = new Tile(_z, x, y);
            }
        }

        this.tileArray[z][x][y].wallDecoration = wallDecoration;
    }

    public void addWall(final int x, final int y, final int z, final int drawHeight, final int orientation, final int orientation2, final int uid,
                        final Animable primary, final Animable secondary, final byte objConf) {
        if (primary == null && secondary == null) {
            return;
        }

        final Wall wall = new Wall();
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
            if (this.tileArray[_z][x][y] == null) {
                this.tileArray[_z][x][y] = new Tile(_z, x, y);
            }
        }

        this.tileArray[z][x][y].wall = wall;
    }

    public void applyBridgeMode(final int x, final int y) {
        final Tile tile = this.tileArray[0][x][y];
        for (int z = 0; z < 3; z++) {
            final Tile _tile = this.tileArray[z][x][y] = this.tileArray[z + 1][x][y];
            if (_tile != null) {
                _tile.z--;
                for (int e = 0; e < _tile.entityCount; e++) {
                    final InteractiveObject entity = _tile.interactiveObjects[e];
                    if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
                        entity.z--;
                    }
                }

            }
        }
        if (this.tileArray[0][x][y] == null) {
            this.tileArray[0][x][y] = new Tile(0, x, y);
        }
        this.tileArray[0][x][y].tileBelow = tile;
        this.tileArray[3][x][y] = null;
    }

    public void clearInteractiveObjectCache() {
        for (int i = 0; i < this.interactiveObjectCacheCurrentPos; i++) {
            final InteractiveObject entity = this.interactiveObjectCache[i];
            this.remove(entity);
            this.interactiveObjectCache[i] = null;
        }

        this.interactiveObjectCacheCurrentPos = 0;
    }

    public void drawMinimapTile(final int x, final int y, final int z, final int[] pixels, int pixelPointer) {
        final int scanLength = 512;// was parameter
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return;
        }
        final PlainTile plainTile = tile.plainTile;
        if (plainTile != null) {
            final int colourRGB = plainTile.colourRGB;
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
        final ShapedTile shapedTile = tile.shapedTile;
        if (shapedTile == null) {
            return;
        }
        final int shape = shapedTile.shape;
        final int rotation = shapedTile.rotation;
        final int underlayRGB = shapedTile.underlayRGB;
        final int overlayRGB = shapedTile.overlayRGB;
        final int[] shapePoints = this.tileShapePoints[shape];
        final int[] shapeIndices = this.tileShapeIndices[rotation];
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

    public int getConfig(final int uid, final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
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

    public GroundDecoration getGroundDecoration(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null || tile.groundDecoration == null) {
            return null;
        } else {
            return tile.groundDecoration;
        }
    }

    public int getGroundDecorationHash(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null || tile.groundDecoration == null) {
            return 0;
        } else {
            return tile.groundDecoration.uid;
        }
    }

    public int getInteractibleObjectHash(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return 0;
        }
        for (int e = 0; e < tile.entityCount; e++) {
            final InteractiveObject entity = tile.interactiveObjects[e];
            if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
                return entity.uid;
            }
        }

        return 0;
    }

    public InteractiveObject getInteractiveObject(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return null;
        }
        for (int e = 0; e < tile.entityCount; e++) {
            final InteractiveObject entity = tile.interactiveObjects[e];
            if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
                return entity;
            }
        }
        return null;
    }

    public WallDecoration getWallDecoration(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return null;
        } else {
            return tile.wallDecoration;
        }
    }

    public int getWallDecorationHash(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null || tile.wallDecoration == null) {
            return 0;
        } else {
            return tile.wallDecoration.uid;
        }
    }

    public Wall getWallObject(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return null;
        } else {
            return tile.wall;
        }
    }

    public int getWallObjectHash(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
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
                    this.tileArray[z][x][y] = null;
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

    private boolean isMouseWithinTriangle(final int mouseX, final int mouseY, final int pointAY, final int pointBY, final int pointCY, final int pointAX,
                                          final int pointBX, final int pointCX) {
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

        final int b1 = (mouseY - pointAY) * (pointBX - pointAX) - (mouseX - pointAX) * (pointBY - pointAY);
        final int b2 = (mouseY - pointCY) * (pointAX - pointCX) - (mouseX - pointCX) * (pointAY - pointCY);
        final int b3 = (mouseY - pointBY) * (pointCX - pointBX) - (mouseX - pointBX) * (pointCY - pointBY);
        return b1 * b3 > 0 && b3 * b2 > 0;
    }

    public void displaceWallDecoration(final int y, final int displacement, final int x, final int z) {
        final Tile tile = this.tileArray[z][x][y];

        if (tile == null) {
            return;
        }

        final WallDecoration wallDecoration = tile.wallDecoration;

        if (wallDecoration != null) {
            final int absX = x * 128 + 64;
            final int absY = y * 128 + 64;
            wallDecoration.x = absX + ((wallDecoration.x - absX) * displacement) / 16;
            wallDecoration.y = absY + ((wallDecoration.y - absY) * displacement) / 16;
        }
    }

    private void method306(final Model model, final int x, final int y, final int z) {
        if (x < this.mapSizeX) {
            final Tile tile = this.tileArray[z][x + 1][y];
            if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
                this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 0, true);
            }
        }
        if (y < this.mapSizeX) {
            final Tile tile = this.tileArray[z][x][y + 1];
            if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
                this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 0, 0, 128, true);
            }
        }
        if (x < this.mapSizeX && y < this.mapSizeY) {
            final Tile tile = this.tileArray[z][x + 1][y + 1];
            if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
                this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 128, true);
            }
        }
        if (x < this.mapSizeX && y > 0) {
            final Tile tile = this.tileArray[z][x + 1][y - 1];
            if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null) {
                this.mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, -128, true);
            }
        }
    }

    private void method307(final int z, final int j, final int k, final int x, final int y, final Model model) {
        boolean flag = true;
        int positionX = x;
        final int position2X = x + j;
        final int positionY = y - 1;
        final int position2Y = y + k;
        for (int _z = z; _z <= z + 1; _z++) {
            if (_z != this.mapSizeZ) {
                for (int _x = positionX; _x <= position2X; _x++) {
                    if (_x >= 0 && _x < this.mapSizeX) {
                        for (int _y = positionY; _y <= position2Y; _y++) {
                            if (_y >= 0 && _y < this.mapSizeY
                                && (!flag || _x >= position2X || _y >= position2Y || _y < y && _x != x)) {
                                final Tile tile = this.tileArray[_z][_x][_y];
                                if (tile != null) {
                                    final int i3 = (this.vertexHeights[_z][_x][_y] + this.vertexHeights[_z][_x + 1][_y]
                                        + this.vertexHeights[_z][_x][_y + 1] + this.vertexHeights[_z][_x + 1][_y + 1]) / 4
                                        - (this.vertexHeights[z][x][y] + this.vertexHeights[z][x + 1][y] + this.vertexHeights[z][x][y + 1]
                                        + this.vertexHeights[z][x + 1][y + 1]) / 4;
                                    final Wall wallObject = tile.wall;
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
                                        final InteractiveObject entity = tile.interactiveObjects[e];
                                        if (entity != null && entity.renderable != null
                                            && entity.renderable.vertexNormals != null) {
                                            final int k3 = (entity.tileRight - entity.tileLeft) + 1;
                                            final int l3 = (entity.tileBottom - entity.tileTop) + 1;
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

    private void mergeNormals(final Model model, final Model secondModel, final int posX, final int posY, final int posZ, final boolean flag) {
        this.anInt488++;
        int count = 0;
        final int[] vertices = secondModel.verticesX;
        final int vertexCount = secondModel.vertexCount;

        for (int vertex = 0; vertex < model.vertexCount; vertex++) {
            final VertexNormal vertexNormal = model.vertexNormals[vertex];
            final VertexNormal offsetVertexNormal = model.vertexNormalOffset[vertex];
            if (offsetVertexNormal.magnitude != 0) {
                final int y = model.verticesY[vertex] - posY;
                if (y <= secondModel.maxY) {
                    final int x = model.verticesX[vertex] - posX;
                    if (x >= secondModel.minX && x <= secondModel.maxX) {
                        final int z = model.verticesZ[vertex] - posZ;
                        if (z >= secondModel.minZ && z <= secondModel.maxZ) {
                            for (int v = 0; v < vertexCount; v++) {
                                final VertexNormal vertexNormal2 = secondModel.vertexNormals[v];
                                final VertexNormal offsetVertexNormal2 = secondModel.vertexNormalOffset[v];
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

    private boolean method320(final int x, final int y, final int z) {
        final int l = this.anIntArrayArrayArray445[z][x][y];
        if (l == -anInt448) {
            return false;
        }
        if (l == anInt448) {
            return true;
        }
        final int worldX = x << 7;
        final int worldY = y << 7;
        if (this.method324(worldX + 1, worldY + 1, this.vertexHeights[z][x][y])
            && this.method324((worldX + 128) - 1, worldY + 1, this.vertexHeights[z][x + 1][y])
            && this.method324((worldX + 128) - 1, (worldY + 128) - 1, this.vertexHeights[z][x + 1][y + 1])
            && this.method324(worldX + 1, (worldY + 128) - 1, this.vertexHeights[z][x][y + 1])) {
            this.anIntArrayArrayArray445[z][x][y] = anInt448;
            return true;
        } else {
            this.anIntArrayArrayArray445[z][x][y] = -anInt448;
            return false;
        }
    }

    private boolean method321(final int x, final int y, final int z, final int wallType) {
        if (!this.method320(x, y, z)) {
            return false;
        }
        final int posX = x << 7;
        final int posY = y << 7;
        final int posZ = this.vertexHeights[z][x][y] - 1;
        final int z1 = posZ - 120;
        final int z2 = posZ - 230;
        final int z3 = posZ - 238;
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

    private boolean method322(final int z, final int x, final int y, final int offsetZ) {
        if (!this.method320(x, y, z)) {
            return false;
        }
        final int _x = x << 7;
        final int _y = y << 7;
        return this.method324(_x + 1, _y + 1, this.vertexHeights[z][x][y] - offsetZ)
            && this.method324((_x + 128) - 1, _y + 1, this.vertexHeights[z][x + 1][y] - offsetZ)
            && this.method324((_x + 128) - 1, (_y + 128) - 1, this.vertexHeights[z][x + 1][y + 1] - offsetZ)
            && this.method324(_x + 1, (_y + 128) - 1, this.vertexHeights[z][x][y + 1] - offsetZ);
    }

    private boolean method323(final int minimumX, final int maximumX, final int minimumY, final int maximumY, final int z, final int offsetZ) {
        if (minimumX == maximumX && minimumY == maximumY) {
            if (!this.method320(minimumX, minimumY, z)) {
                return false;
            }
            final int _x = minimumX << 7;
            final int _y = minimumY << 7;
            return this.method324(_x + 1, _y + 1, this.vertexHeights[z][minimumX][minimumY] - offsetZ)
                && this.method324((_x + 128) - 1, _y + 1, this.vertexHeights[z][minimumX + 1][minimumY] - offsetZ)
                && this.method324((_x + 128) - 1, (_y + 128) - 1, this.vertexHeights[z][minimumX + 1][minimumY + 1] - offsetZ)
                && this.method324(_x + 1, (_y + 128) - 1, this.vertexHeights[z][minimumX][minimumY + 1] - offsetZ);
        }
        for (int x = minimumX; x <= maximumX; x++) {
            for (int y = minimumY; y <= maximumY; y++) {
                if (this.anIntArrayArrayArray445[z][x][y] == -anInt448) {
                    return false;
                }
            }

        }

        final int _x = (minimumX << 7) + 1;
        final int _y = (minimumY << 7) + 2;
        final int _z = this.vertexHeights[z][minimumX][minimumY] - offsetZ;
        if (!this.method324(_x, _y, _z)) {
            return false;
        }
        final int x = (maximumX << 7) - 1;
        if (!this.method324(x, _y, _z)) {
            return false;
        }
        final int y = (maximumY << 7) - 1;
        return this.method324(_x, y, _z) && this.method324(x, y, _z);
    }

    private boolean method324(final int x, final int y, final int z) {
        for (int c = 0; c < processedCullingClustersPointer; c++) {
            final CullingCluster cluster = processedCullingClusters[c];
            if (cluster.tileDistanceEnum == 1) {
                final int i1 = cluster.worldStartX - x;
                if (i1 > 0) {
                    final int j2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i1 >> 8);
                    final int k3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i1 >> 8);
                    final int l4 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * i1 >> 8);
                    final int i6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * i1 >> 8);
                    if (y >= j2 && y <= k3 && z >= l4 && z <= i6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistanceEnum == 2) {
                final int j1 = x - cluster.worldStartX;
                if (j1 > 0) {
                    final int k2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * j1 >> 8);
                    final int l3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * j1 >> 8);
                    final int i5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * j1 >> 8);
                    final int j6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * j1 >> 8);
                    if (y >= k2 && y <= l3 && z >= i5 && z <= j6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistanceEnum == 3) {
                final int k1 = cluster.worldStartY - y;
                if (k1 > 0) {
                    final int l2 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * k1 >> 8);
                    final int i4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * k1 >> 8);
                    final int j5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * k1 >> 8);
                    final int k6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * k1 >> 8);
                    if (x >= l2 && x <= i4 && z >= j5 && z <= k6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistanceEnum == 4) {
                final int l1 = y - cluster.worldStartY;
                if (l1 > 0) {
                    final int i3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * l1 >> 8);
                    final int j4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * l1 >> 8);
                    final int k5 = cluster.worldEndZ + (cluster.worldDistanceFromCameraStartZ * l1 >> 8);
                    final int l6 = cluster.worldStartZ + (cluster.worldDistanceFromCameraEndZ * l1 >> 8);
                    if (x >= i3 && x <= j4 && z >= k5 && z <= l6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistanceEnum == 5) {
                final int i2 = z - cluster.worldEndZ;
                if (i2 > 0) {
                    final int j3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * i2 >> 8);
                    final int k4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * i2 >> 8);
                    final int l5 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i2 >> 8);
                    final int i7 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i2 >> 8);
                    if (x >= j3 && x <= k4 && y >= l5 && y <= i7) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int mixColours(final int colourA, int colourB) {
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
        final int clusterCount = cullingClusterPointer[plane];
        final CullingCluster[] clusters = cullingClusters[plane];
        processedCullingClustersPointer = 0;
        for (int c = 0; c < clusterCount; c++) {
            final CullingCluster cluster = clusters[c];
            if (cluster.searchMask == 1) {
                final int distanceFromCameraStartX = (cluster.tileStartX - cameraPositionTileX) + 25;
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
                final int distanceFromCameraStartY = (cluster.tileStartY - cameraPositionTileY) + 25;
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
                final int realDistanceFromCameraStartZ = cluster.worldEndZ - cameraPosZ;
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
                        label0:
                        for (int x = distanceFromCameraStartX; x <= distanceFromCameraEndX; x++) {
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

    private void remove(final InteractiveObject entity) {
        for (int x = entity.tileLeft; x <= entity.tileRight; x++) {
            for (int y = entity.tileTop; y <= entity.tileBottom; y++) {
                final Tile tile = this.tileArray[entity.z][x][y];
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

    public void removeGroundDecoration(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return;
        }
        tile.groundDecoration = null;
    }

    public void removeGroundItemTile(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile != null) {
            tile.groundItemTile = null;
        }
    }

    public void removeInteractiveObject(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile == null) {
            return;
        }
        for (int e = 0; e < tile.entityCount; e++) {
            final InteractiveObject entity = tile.interactiveObjects[e];
            if ((entity.uid >> 29 & 3) == 2 && entity.tileLeft == x && entity.tileTop == y) {
                this.remove(entity);
                return;
            }
        }

    }

    public void removeWallDecoration(final int x, final int y, final int z) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile != null) {
            tile.wallDecoration = null;
        }
    }

    public void removeWallObject(final int x, final int z, final int y) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile != null) {
            tile.wall = null;
        }
    }

    public void render(int cameraPosX, int cameraPosY, final int curveX, final int cameraPosZ, final int plane, final int curveY) {
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
            final Tile[][] tiles = this.tileArray[z];
            for (int x = currentPositionX; x < mapBoundsX; x++) {
                for (int y = currentPositionY; y < mapBoundsY; y++) {
                    final Tile tile = tiles[x][y];
                    if (tile != null) {
                        if (tile.logicHeight > plane
                            || !TILE_VISIBILITY_MAP[(x - cameraPositionTileX) + 25][(y - cameraPositionTileY) + 25]
                            && this.vertexHeights[z][x][y] - cameraPosZ < 2000) {
                            tile.draw = false;
                            tile.visible = false;
                            tile.wallCullDirection = 0;
                        } else {
                            tile.draw = true;
                            tile.visible = true;
                            tile.drawEntities = tile.entityCount > 0;
                            anInt446++;
                        }
                    }
                }

            }

        }

        for (int z = this.currentPositionZ; z < this.mapSizeZ; z++) {
            final Tile[][] tiles = this.tileArray[z];
            for (int offsetX = -25; offsetX <= 0; offsetX++) {
                final int x = cameraPositionTileX + offsetX;
                final int x2 = cameraPositionTileX - offsetX;
                if (x >= currentPositionX || x2 < mapBoundsX) {
                    for (int offsetY = -25; offsetY <= 0; offsetY++) {
                        final int y = cameraPositionTileY + offsetY;
                        final int y2 = cameraPositionTileY - offsetY;
                        if (x >= currentPositionX) {
                            if (y >= currentPositionY) {
                                final Tile tile = tiles[x][y];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, true);
                                }
                            }
                            if (y2 < mapBoundsY) {
                                final Tile tile = tiles[x][y2];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, true);
                                }
                            }
                        }
                        if (x2 < mapBoundsX) {
                            if (y >= currentPositionY) {
                                final Tile tile = tiles[x2][y];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, true);
                                }
                            }
                            if (y2 < mapBoundsY) {
                                final Tile tile = tiles[x2][y2];
                                if (tile != null && tile.draw) {
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
            final Tile[][] tiles = this.tileArray[z];
            for (int offsetX = -25; offsetX <= 0; offsetX++) {
                final int x = cameraPositionTileX + offsetX;
                final int x2 = cameraPositionTileX - offsetX;
                if (x >= currentPositionX || x2 < mapBoundsX) {
                    for (int offsetY = -25; offsetY <= 0; offsetY++) {
                        final int y = cameraPositionTileY + offsetY;
                        final int y2 = cameraPositionTileY - offsetY;
                        if (x >= currentPositionX) {
                            if (y >= currentPositionY) {
                                final Tile tile = tiles[x][y];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, false);
                                }
                            }
                            if (y2 < mapBoundsY) {
                                final Tile tile = tiles[x][y2];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, false);
                                }
                            }
                        }
                        if (x2 < mapBoundsX) {
                            if (y >= currentPositionY) {
                                final Tile tile = tiles[x2][y];
                                if (tile != null && tile.draw) {
                                    this.renderTile(tile, false);
                                }
                            }
                            if (y2 < mapBoundsY) {
                                final Tile tile = tiles[x2][y2];
                                if (tile != null && tile.draw) {
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

    private void renderPlainTile(final PlainTile plainTile, final int tileX, final int tileY, final int tileZ, final int sinX, final int cosineX, final int sinY,
                                 final int cosineY) {
        int xC;
        int xA = xC = (tileX << 7) - cameraPosX;
        int yB;
        int yA = yB = (tileY << 7) - cameraPosY;
        int xD;
        int xB = xD = xA + 128;
        int yC;
        int yD = yC = yA + 128;
        int zA = this.vertexHeights[tileZ][tileX][tileY] - cameraPosZ;
        int zB = this.vertexHeights[tileZ][tileX + 1][tileY] - cameraPosZ;
        int zC = this.vertexHeights[tileZ][tileX + 1][tileY + 1] - cameraPosZ;
        int zD = this.vertexHeights[tileZ][tileX][tileY + 1] - cameraPosZ;
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
        final int screenXA = Rasterizer.centreX + (xA << 9) / yA;
        final int screenYA = Rasterizer.centreY + (zA << 9) / yA;
        final int screenXB = Rasterizer.centreX + (xB << 9) / yB;
        final int screenYB = Rasterizer.centreY + (zB << 9) / yB;
        final int screenXD = Rasterizer.centreX + (xD << 9) / yD;
        final int screenYD = Rasterizer.centreY + (zC << 9) / yD;
        final int screenXC = Rasterizer.centreX + (xC << 9) / yC;
        final int screenYC = Rasterizer.centreY + (zD << 9) / yC;
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
                final int rgb = textureRGB[plainTile.texture];
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
                final int rgb = textureRGB[plainTile.texture];
                Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
                    this.mixColours(rgb, plainTile.colourA), this.mixColours(rgb, plainTile.colourB),
                    this.mixColours(rgb, plainTile.colourC));
            }
        }
    }

    private void renderShapedTile(final ShapedTile shapedTile, final int tileX, final int tileY, final int sineX, final int cosineX, final int sineY,
                                  final int cosineY) {
        int triangleCount = shapedTile.vertexX.length;
        for (int triangle = 0; triangle < triangleCount; triangle++) {
            int viewspaceX = shapedTile.vertexX[triangle] - cameraPosX;
            int viewspaceY = shapedTile.vertexY[triangle] - cameraPosZ;
            int viewspaceZ = shapedTile.vertexZ[triangle] - cameraPosY;
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
            final int a = shapedTile.triangleA[triangle];
            final int b = shapedTile.triangleB[triangle];
            final int c = shapedTile.triangleC[triangle];
            final int screenXA = ShapedTile.screenX[a];
            final int screenXB = ShapedTile.screenX[b];
            final int screenXC = ShapedTile.screenX[c];
            final int screenYA = ShapedTile.screenY[a];
            final int screenYB = ShapedTile.screenY[b];
            final int screenYC = ShapedTile.screenY[c];
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
                    final int rgb = textureRGB[shapedTile.triangleTexture[triangle]];
                    Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC,
                        this.mixColours(rgb, shapedTile.triangleHSLA[triangle]),
                        this.mixColours(rgb, shapedTile.triangleHSLB[triangle]),
                        this.mixColours(rgb, shapedTile.triangleHSLC[triangle]));
                }
            }
        }

    }

    private void renderTile(final Tile _tile, boolean flag) {
        tileList.pushBack(_tile);
        do {
            Tile groundTile;
            do {
                groundTile = (Tile) tileList.popFront();
                if (groundTile == null) {
                    return;
                }
            } while (!groundTile.visible);
            final int x = groundTile.x;
            final int y = groundTile.y;
            final int z = groundTile.z;
            final int l = groundTile.anInt1310;
            final Tile[][] tiles = this.tileArray[z];
            if (groundTile.draw) {
                if (flag) {
                    if (z > 0) {
                        final Tile tile = this.tileArray[z - 1][x][y];
                        if (tile != null && tile.visible) {
                            continue;
                        }
                    }
                    if (x <= cameraPositionTileX && x > currentPositionX) {
                        final Tile tile = tiles[x - 1][y];
                        if (tile != null && tile.visible
                            && (tile.draw || (groundTile.interactiveObjectsSizeOR & 1) == 0)) {
                            continue;
                        }
                    }
                    if (x >= cameraPositionTileX && x < mapBoundsX - 1) {
                        final Tile tile = tiles[x + 1][y];
                        if (tile != null && tile.visible
                            && (tile.draw || (groundTile.interactiveObjectsSizeOR & 4) == 0)) {
                            continue;
                        }
                    }
                    if (y <= cameraPositionTileY && y > currentPositionY) {
                        final Tile tile = tiles[x][y - 1];
                        if (tile != null && tile.visible
                            && (tile.draw || (groundTile.interactiveObjectsSizeOR & 8) == 0)) {
                            continue;
                        }
                    }
                    if (y >= cameraPositionTileY && y < mapBoundsY - 1) {
                        final Tile tile = tiles[x][y + 1];
                        if (tile != null && tile.visible
                            && (tile.draw || (groundTile.interactiveObjectsSizeOR & 2) == 0)) {
                            continue;
                        }
                    }
                } else {
                    flag = true;
                }
                groundTile.draw = false;
                if (groundTile.tileBelow != null) {
                    final Tile tile = groundTile.tileBelow;
                    if (tile.plainTile != null) {
                        if (!this.method320(x, y, 0)) {
                            this.renderPlainTile(tile.plainTile, x, y, 0, curveSineX, curveCosineX, curveSineY,
                                curveCosineY);
                        }
                    } else if (tile.shapedTile != null && !this.method320(x, y, 0)) {
                        this.renderShapedTile(tile.shapedTile, x, y, curveSineX, curveCosineX, curveSineY, curveCosineY);
                    }
                    final Wall wallObject = tile.wall;
                    if (wallObject != null) {
                        wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
                            wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
                            wallObject.uid);
                    }
                    for (int e = 0; e < tile.entityCount; e++) {
                        final InteractiveObject entity = tile.interactiveObjects[e];
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
                final Wall wallObject = groundTile.wall;
                final WallDecoration wallDecoration = groundTile.wallDecoration;
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
                            groundTile.wallCullDirection = 3;
                            groundTile.anInt1326 = anIntArray481[j1];
                            groundTile.anInt1327 = 3 - groundTile.anInt1326;
                        } else if (wallObject.orientation == 32) {
                            groundTile.wallCullDirection = 6;
                            groundTile.anInt1326 = anIntArray482[j1];
                            groundTile.anInt1327 = 6 - groundTile.anInt1326;
                        } else if (wallObject.orientation == 64) {
                            groundTile.wallCullDirection = 12;
                            groundTile.anInt1326 = anIntArray483[j1];
                            groundTile.anInt1327 = 12 - groundTile.anInt1326;
                        } else {
                            groundTile.wallCullDirection = 9;
                            groundTile.anInt1326 = anIntArray484[j1];
                            groundTile.anInt1327 = 9 - groundTile.anInt1326;
                        }
                    } else {
                        groundTile.wallCullDirection = 0;
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
                        final int j4 = wallDecoration.x - cameraPosX;
                        final int l5 = wallDecoration.z - cameraPosZ;
                        final int k6 = wallDecoration.y - cameraPosY;
                        final int i8 = wallDecoration.face;
                        final int k9;
                        if (i8 == 1 || i8 == 2) {
                            k9 = -j4;
                        } else {
                            k9 = j4;
                        }
                        final int k10;
                        if (i8 == 2 || i8 == 3) {
                            k10 = -k6;
                        } else {
                            k10 = k6;
                        }
                        if ((wallDecoration.configBits & 0x100) != 0 && k10 < k9) {
                            final int i11 = j4 + faceOffsetX2[i8];
                            final int k11 = k6 + faceOffsetY2[i8];
                            wallDecoration.renderable.renderAtPoint(i8 * 512 + 256, curveSineY, curveCosineY,
                                curveSineX, curveCosineX, i11, l5, k11, wallDecoration.uid);
                        }
                        if ((wallDecoration.configBits & 0x200) != 0 && k10 > k9) {
                            final int j11 = j4 + faceOffsetX3[i8];
                            final int l11 = k6 + faceOffsetY3[i8];
                            wallDecoration.renderable.renderAtPoint(i8 * 512 + 1280 & 0x7ff, curveSineY, curveCosineY,
                                curveSineX, curveCosineX, j11, l5, l11, wallDecoration.uid);
                        }
                    }
                }
                if (flag1) {
                    final GroundDecoration groundDecoration = groundTile.groundDecoration;
                    if (groundDecoration != null) {
                        groundDecoration.renderable.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
                            groundDecoration.x - cameraPosX, groundDecoration.z - cameraPosZ,
                            groundDecoration.y - cameraPosY, groundDecoration.uid);
                    }
                    final GroundItemTile groundItemTile = groundTile.groundItemTile;
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
                final int interactiveObjectsSizeOR = groundTile.interactiveObjectsSizeOR;
                if (interactiveObjectsSizeOR != 0) {
                    if (x < cameraPositionTileX && (interactiveObjectsSizeOR & 4) != 0) {
                        final Tile tile = tiles[x + 1][y];
                        if (tile != null && tile.visible) {
                            tileList.pushBack(tile);
                        }
                    }
                    if (y < cameraPositionTileY && (interactiveObjectsSizeOR & 2) != 0) {
                        final Tile tile = tiles[x][y + 1];
                        if (tile != null && tile.visible) {
                            tileList.pushBack(tile);
                        }
                    }
                    if (x > cameraPositionTileX && (interactiveObjectsSizeOR & 1) != 0) {
                        final Tile tile = tiles[x - 1][y];
                        if (tile != null && tile.visible) {
                            tileList.pushBack(tile);
                        }
                    }
                    if (y > cameraPositionTileY && (interactiveObjectsSizeOR & 8) != 0) {
                        final Tile tile = tiles[x][y - 1];
                        if (tile != null && tile.visible) {
                            tileList.pushBack(tile);
                        }
                    }
                }
            }
            if (groundTile.wallCullDirection != 0) {
                boolean flag2 = true;
                for (int e = 0; e < groundTile.entityCount; e++) {
                    if (groundTile.interactiveObjects[e].anInt528 == anInt448
                        || (groundTile.interactiveObjectsSize[e] & groundTile.wallCullDirection) != groundTile.anInt1326) {
                        continue;
                    }
                    flag2 = false;
                    break;
                }

                if (flag2) {
                    final Wall wallObject = groundTile.wall;
                    if (!this.method321(x, y, l, wallObject.orientation)) {
                        wallObject.primary.renderAtPoint(0, curveSineY, curveCosineY, curveSineX, curveCosineX,
                            wallObject.x - cameraPosX, wallObject.z - cameraPosZ, wallObject.y - cameraPosY,
                            wallObject.uid);
                    }
                    groundTile.wallCullDirection = 0;
                }
            }
            if (groundTile.drawEntities) {
                try {
                    final int entityCount = groundTile.entityCount;
                    groundTile.drawEntities = false;
                    int l1 = 0;
                    label0:
                    for (int e = 0; e < entityCount; e++) {
                        final InteractiveObject entity = groundTile.interactiveObjects[e];
                        if (entity.anInt528 == anInt448) {
                            continue;
                        }
                        for (int _x = entity.tileLeft; _x <= entity.tileRight; _x++) {
                            for (int _y = entity.tileTop; _y <= entity.tileBottom; _y++) {
                                final Tile tile = tiles[_x][_y];
                                if (tile.draw) {
                                    groundTile.drawEntities = true;
                                } else {
                                    if (tile.wallCullDirection == 0) {
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
                                    if ((l6 & tile.wallCullDirection) != groundTile.anInt1327) {
                                        continue;
                                    }
                                    groundTile.drawEntities = true;
                                }
                                continue label0;
                            }

                        }

                        interactiveObjects[l1++] = entity;
                        int i5 = cameraPositionTileX - entity.tileLeft;
                        final int i6 = entity.tileRight - cameraPositionTileX;
                        if (i6 > i5) {
                            i5 = i6;
                        }
                        final int i7 = cameraPositionTileY - entity.tileTop;
                        final int j8 = entity.tileBottom - cameraPositionTileY;
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
                            final InteractiveObject entity = interactiveObjects[j5];
                            if (entity.anInt528 != anInt448) {
                                if (entity.anInt527 > i3) {
                                    i3 = entity.anInt527;
                                    l3 = j5;
                                } else if (entity.anInt527 == i3) {
                                    final int j7 = entity.worldX - cameraPosX;
                                    final int k8 = entity.worldY - cameraPosY;
                                    final int l9 = interactiveObjects[l3].worldX - cameraPosX;
                                    final int l10 = interactiveObjects[l3].worldY - cameraPosY;
                                    if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10) {
                                        l3 = j5;
                                    }
                                }
                            }
                        }

                        if (l3 == -1) {
                            break;
                        }
                        final InteractiveObject entity = interactiveObjects[l3];
                        entity.anInt528 = anInt448;

                        if (!this.method323(entity.tileLeft, entity.tileRight, entity.tileTop, entity.tileBottom, l,
                            entity.renderable.modelHeight)) {
                            entity.renderable.renderAtPoint(entity.rotation, curveSineY, curveCosineY, curveSineX,
                                curveCosineX, entity.worldX - cameraPosX, entity.worldZ - cameraPosZ,
                                entity.worldY - cameraPosY, entity.uid);
                        }

                        for (int _x = entity.tileLeft; _x <= entity.tileRight; _x++) {
                            for (int _y = entity.tileTop; _y <= entity.tileBottom; _y++) {
                                final Tile tile = tiles[_x][_y];
                                if (tile.wallCullDirection != 0) {
                                    tileList.pushBack(tile);
                                } else if ((_x != x || _y != y) && tile.visible) {
                                    tileList.pushBack(tile);
                                }
                            }

                        }

                    }
                    if (groundTile.drawEntities) {
                        continue;
                    }
                } catch (final Exception _ex) {
                    groundTile.drawEntities = false;
                }
            }
            if (!groundTile.visible || groundTile.wallCullDirection != 0) {
                continue;
            }
            if (x <= cameraPositionTileX && x > currentPositionX) {
                final Tile tile = tiles[x - 1][y];
                if (tile != null && tile.visible) {
                    continue;
                }
            }
            if (x >= cameraPositionTileX && x < mapBoundsX - 1) {
                final Tile tile = tiles[x + 1][y];
                if (tile != null && tile.visible) {
                    continue;
                }
            }
            if (y <= cameraPositionTileY && y > currentPositionY) {
                final Tile tile = tiles[x][y - 1];
                if (tile != null && tile.visible) {
                    continue;
                }
            }
            if (y >= cameraPositionTileY && y < mapBoundsY - 1) {
                final Tile tile = tiles[x][y + 1];
                if (tile != null && tile.visible) {
                    continue;
                }
            }
            groundTile.visible = false;
            anInt446--;
            final GroundItemTile groundItemTile = groundTile.groundItemTile;
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
                final WallDecoration wallDecoration = groundTile.wallDecoration;
                if (wallDecoration != null && !this.method322(l, x, y, wallDecoration.renderable.modelHeight)) {
                    if ((wallDecoration.configBits & groundTile.anInt1328) != 0) {
                        wallDecoration.renderable.renderAtPoint(wallDecoration.face, curveSineY, curveCosineY,
                            curveSineX, curveCosineX, wallDecoration.x - cameraPosX, wallDecoration.z - cameraPosZ,
                            wallDecoration.y - cameraPosY, wallDecoration.uid);
                    } else if ((wallDecoration.configBits & 0x300) != 0) {
                        final int l2 = wallDecoration.x - cameraPosX;
                        final int j3 = wallDecoration.z - cameraPosZ;
                        final int i4 = wallDecoration.y - cameraPosY;
                        final int face = wallDecoration.face;
                        final int j6;
                        if (face == 1 || face == 2) {
                            j6 = -l2;
                        } else {
                            j6 = l2;
                        }
                        final int l7;
                        if (face == 2 || face == 3) {
                            l7 = -i4;
                        } else {
                            l7 = i4;
                        }
                        if ((wallDecoration.configBits & 0x100) != 0 && l7 >= j6) {
                            final int i9 = l2 + faceOffsetX2[face];
                            final int i10 = i4 + faceOffsetY2[face];
                            wallDecoration.renderable.renderAtPoint(face * 512 + 256, curveSineY, curveCosineY,
                                curveSineX, curveCosineX, i9, j3, i10, wallDecoration.uid);
                        }
                        if ((wallDecoration.configBits & 0x200) != 0 && l7 <= j6) {
                            final int j9 = l2 + faceOffsetX3[face];
                            final int j10 = i4 + faceOffsetY3[face];
                            wallDecoration.renderable.renderAtPoint(face * 512 + 1280 & 0x7ff, curveSineY, curveCosineY,
                                curveSineX, curveCosineX, j9, j3, j10, wallDecoration.uid);
                        }
                    }
                }
                final Wall wallObject = groundTile.wall;
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
                final Tile tile = this.tileArray[z + 1][x][y];
                if (tile != null && tile.visible) {
                    tileList.pushBack(tile);
                }
            }
            if (x < cameraPositionTileX) {
                final Tile tile = tiles[x + 1][y];
                if (tile != null && tile.visible) {
                    tileList.pushBack(tile);
                }
            }
            if (y < cameraPositionTileY) {
                final Tile tile = tiles[x][y + 1];
                if (tile != null && tile.visible) {
                    tileList.pushBack(tile);
                }
            }
            if (x > cameraPositionTileX) {
                final Tile tile = tiles[x - 1][y];
                if (tile != null && tile.visible) {
                    tileList.pushBack(tile);
                }
            }
            if (y > cameraPositionTileY) {
                final Tile tile = tiles[x][y - 1];
                if (tile != null && tile.visible) {
                    tileList.pushBack(tile);
                }
            }
        } while (true);
    }

    public void renderTile(final int plane, final int x, final int y, final int clippingPath, final int clippingPathRotation, final int textureId,
                           final int vertexHeightSW, final int vertexHeightSE, final int vertexHeightNE, final int vertexHeightNW, final int colorSW, final int colorSE, final int colorNE,
                           final int colorNW, final int colorSignedSW_maybe, final int colorSignedSE_maybe, final int colorSignedNE_maybe, final int colorSignedNW_maybe, final int k4, final int l4) {
        if (clippingPath == 0) {
            final PlainTile tile = new PlainTile(colorSW, colorSE, colorNW, colorNE, k4, -1, false);
            for (int _z = plane; _z >= 0; _z--) {
                if (this.tileArray[_z][x][y] == null) {
                    this.tileArray[_z][x][y] = new Tile(_z, x, y);
                }
            }

            this.tileArray[plane][x][y].plainTile = tile;
            return;
        }
        if (clippingPath == 1) {
            final PlainTile tile = new PlainTile(colorSignedSW_maybe, colorSignedSE_maybe, colorSignedNW_maybe, colorSignedNE_maybe, l4, textureId, vertexHeightSW == vertexHeightSE
                && vertexHeightSW == vertexHeightNE && vertexHeightSW == vertexHeightNW);
            for (int _z = plane; _z >= 0; _z--) {
                if (this.tileArray[_z][x][y] == null) {
                    this.tileArray[_z][x][y] = new Tile(_z, x, y);
                }
            }

            this.tileArray[plane][x][y].plainTile = tile;
            return;
        }
        final ShapedTile tile = new ShapedTile(x, vertexHeightSW, vertexHeightSE, vertexHeightNW, vertexHeightNE, y,
            clippingPathRotation, textureId, clippingPath, colorSW, colorSignedSW_maybe, colorSE, colorSignedSE_maybe, colorNW, colorSignedNW_maybe, colorNE, colorSignedNE_maybe, l4, k4);
        for (int _z = plane; _z >= 0; _z--) {
            if (this.tileArray[_z][x][y] == null) {
                this.tileArray[_z][x][y] = new Tile(_z, x, y);
            }
        }

        this.tileArray[plane][x][y].shapedTile = tile;
    }

    public void request2DTrace(final int x, final int y) {
        clicked = true;
        clickX = x;
        clickY = y;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    public void setHeightLevel(final int z) {
        this.currentPositionZ = z;
        for (int x = 0; x < this.mapSizeX; x++) {
            for (int y = 0; y < this.mapSizeY; y++) {
                if (this.tileArray[z][x][y] == null) {
                    this.tileArray[z][x][y] = new Tile(z, x, y);
                }
            }

        }

    }

    public void setTileLogicHeight(final int x, final int y, final int z, final int logicHeight) {
        final Tile tile = this.tileArray[z][x][y];
        if (tile != null) {
            this.tileArray[z][x][y].logicHeight = logicHeight;
        }
    }

    public void shadeModels(final int y, final int x, final int z) {
        final int lightness = 64;// was parameter
        final int magnitudeMultiplier = 768;// was parameter
        final int distanceFromOrigin = (int) Math.sqrt(x * x + y * y + z * z);
        final int magnitude = magnitudeMultiplier * distanceFromOrigin >> 8;
        for (int _z = 0; _z < this.mapSizeZ; _z++) {
            for (int _x = 0; _x < this.mapSizeX; _x++) {
                for (int _y = 0; _y < this.mapSizeY; _y++) {
                    final Tile tile = this.tileArray[_z][_x][_y];
                    if (tile != null) {
                        final Wall wallObject = tile.wall;
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
                            final InteractiveObject entity = tile.interactiveObjects[e];
                            if (entity != null && entity.renderable != null
                                && entity.renderable.vertexNormals != null) {
                                this.method307(_z, (entity.tileRight - entity.tileLeft) + 1,
                                    (entity.tileBottom - entity.tileTop) + 1, _x, _y, (Model) entity.renderable);
                                ((Model) entity.renderable).handleShading(lightness, magnitude, x, y, z);
                            }
                        }

                        final GroundDecoration groundDecoration = tile.groundDecoration;
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
