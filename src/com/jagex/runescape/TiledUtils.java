package com.jagex.runescape;

final class TiledUtils {

    public static int getRotatedLandscapeChunkX(int rotation, final int objectSizeY, final int x, final int y, final int objectSizeX) {
        rotation &= 3;
        if (rotation == 0) {
            return x;
        }
        if (rotation == 1) {
            return y;
        }
        if (rotation == 2) {
            return 7 - x - (objectSizeX - 1);
        } else {
            return 7 - y - (objectSizeY - 1);
        }
    }

    public static int getRotatedLandscapeChunkY(final int y, final int objectSizeY, int rotation, final int objectSizeX, final int x) {
        rotation &= 3;
        if (rotation == 0) {
            return y;
        }
        if (rotation == 1) {
            return 7 - x - (objectSizeX - 1);
        }
        if (rotation == 2) {
            return 7 - y - (objectSizeY - 1);
        } else {
            return x;
        }
    }

    public static int getRotatedMapChunkX(int rotation, final int y, final int x) {
        rotation &= 3;
        if (rotation == 0) {
            return x;
        }
        if (rotation == 1) {
            return y;
        }
        if (rotation == 2) {
            return 7 - x;
        } else {
            return 7 - y;
        }
    }

    public static int getRotatedMapChunkY(final int y, int rotation, final int x) {
        rotation &= 3;
        if (rotation == 0) {
            return y;
        }
        if (rotation == 1) {
            return 7 - x;
        }
        if (rotation == 2) {
            return 7 - y;
        } else {
            return x;
        }
    }

}
