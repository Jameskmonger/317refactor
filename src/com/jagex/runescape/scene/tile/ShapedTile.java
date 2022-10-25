package com.jagex.runescape.scene.tile;

public final class ShapedTile {

    public final int[] vertexX;
    public final int[] vertexY;
    public final int[] vertexZ;
    public final int[] triangleHSLA;
    public final int[] triangleHSLB;
    public final int[] triangleHSLC;
    public final int[] triangleA;
    public final int[] triangleB;
    public final int[] triangleC;
    public int[] triangleTexture;
    public final boolean flat;
    public final int shape;
    public final int rotation;
    public final int underlayRGB;
    public final int overlayRGB;
    public static final int[] screenX = new int[6];
    public static final int[] screenY = new int[6];
    public static final int[] viewspaceX = new int[6];
    public static final int[] viewspaceY = new int[6];
    public static final int[] viewspaceZ = new int[6];
    private static final int[][] shapedTilePointData = {{1, 3, 5, 7}, {1, 3, 5, 7}, {1, 3, 5, 7},
        {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 6}, {1, 3, 5, 7, 2, 6},
        {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 2, 8}, {1, 3, 5, 7, 11, 12}, {1, 3, 5, 7, 11, 12},
        {1, 3, 5, 7, 13, 14}};
    private static final int[][] shapedTileElementData = {{0, 1, 2, 3, 0, 0, 1, 3}, {1, 1, 2, 3, 1, 0, 1, 3},
        {0, 1, 2, 3, 1, 0, 1, 3}, {0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3}, {0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4},
        {0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4}, {0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3},
        {0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3}, {0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5},
        {0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5},
        {0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3},
        {1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3},
        {1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5}};

    public ShapedTile(final int tileX, final int heightSW, final int heightSE, final int heightNW, final int heightNE, final int tileZ, final int rotation, final int texture, final int shape, final int overlaySW,
                      final int underlaySW, final int overlaySE, final int underlaySE, final int overlayNW, final int underlayNW, final int overlayNE, final int underlayNE, final int overlayRGB, final int underlayRGB) {
        this.flat = !(heightSW != heightSE || heightSW != heightNE || heightSW != heightNW);
        this.shape = shape;
        this.rotation = rotation;
        this.underlayRGB = underlayRGB;
        this.overlayRGB = overlayRGB;
        final char TILE_WIDTH = '\200';
        final int HALF_TILE = TILE_WIDTH / 2;
        final int QUARTER_TILE = TILE_WIDTH / 4;
        final int THREE_QUARTER_TILE = (TILE_WIDTH * 3) / 4;
        final int[] shapedTileMesh = shapedTilePointData[shape];
        final int shapedTileMeshLength = shapedTileMesh.length;
        this.vertexX = new int[shapedTileMeshLength];
        this.vertexY = new int[shapedTileMeshLength];
        this.vertexZ = new int[shapedTileMeshLength];
        final int[] vertexColourOverlay = new int[shapedTileMeshLength];
        final int[] vertexColourUnderlay = new int[shapedTileMeshLength];
        final int tilePosX = tileX * TILE_WIDTH;
        final int tilePosY = tileZ * TILE_WIDTH;
        for (int vertex = 0; vertex < shapedTileMeshLength; vertex++) {
            int vertexType = shapedTileMesh[vertex];
            if ((vertexType & 1) == 0 && vertexType <= 8) {
                vertexType = (vertexType - rotation - rotation - 1 & 7) + 1;
            }
            if (vertexType > 8 && vertexType <= 12) {
                vertexType = (vertexType - 9 - rotation & 3) + 9;
            }
            if (vertexType > 12 && vertexType <= 16) {
                vertexType = (vertexType - 13 - rotation & 3) + 13;
            }
            final int vertexX;
            final int vertexZ;
            final int vertexY;
            final int vertexCOverlay;
            final int vertexCUnderlay;
            if (vertexType == 1) {
                vertexX = tilePosX;
                vertexZ = tilePosY;
                vertexY = heightSW;
                vertexCOverlay = overlaySW;
                vertexCUnderlay = underlaySW;
            } else if (vertexType == 2) {
                vertexX = tilePosX + HALF_TILE;
                vertexZ = tilePosY;
                vertexY = heightSW + heightSE >> 1;
                vertexCOverlay = overlaySW + overlaySE >> 1;
                vertexCUnderlay = underlaySW + underlaySE >> 1;
            } else if (vertexType == 3) {
                vertexX = tilePosX + TILE_WIDTH;
                vertexZ = tilePosY;
                vertexY = heightSE;
                vertexCOverlay = overlaySE;
                vertexCUnderlay = underlaySE;
            } else if (vertexType == 4) {
                vertexX = tilePosX + TILE_WIDTH;
                vertexZ = tilePosY + HALF_TILE;
                vertexY = heightSE + heightNE >> 1;
                vertexCOverlay = overlaySE + overlayNE >> 1;
                vertexCUnderlay = underlaySE + underlayNE >> 1;
            } else if (vertexType == 5) {
                vertexX = tilePosX + TILE_WIDTH;
                vertexZ = tilePosY + TILE_WIDTH;
                vertexY = heightNE;
                vertexCOverlay = overlayNE;
                vertexCUnderlay = underlayNE;
            } else if (vertexType == 6) {
                vertexX = tilePosX + HALF_TILE;
                vertexZ = tilePosY + TILE_WIDTH;
                vertexY = heightNE + heightNW >> 1;
                vertexCOverlay = overlayNE + overlayNW >> 1;
                vertexCUnderlay = underlayNE + underlayNW >> 1;
            } else if (vertexType == 7) {
                vertexX = tilePosX;
                vertexZ = tilePosY + TILE_WIDTH;
                vertexY = heightNW;
                vertexCOverlay = overlayNW;
                vertexCUnderlay = underlayNW;
            } else if (vertexType == 8) {
                vertexX = tilePosX;
                vertexZ = tilePosY + HALF_TILE;
                vertexY = heightNW + heightSW >> 1;
                vertexCOverlay = overlayNW + overlaySW >> 1;
                vertexCUnderlay = underlayNW + underlaySW >> 1;
            } else if (vertexType == 9) {
                vertexX = tilePosX + HALF_TILE;
                vertexZ = tilePosY + QUARTER_TILE;
                vertexY = heightSW + heightSE >> 1;
                vertexCOverlay = overlaySW + overlaySE >> 1;
                vertexCUnderlay = underlaySW + underlaySE >> 1;
            } else if (vertexType == 10) {
                vertexX = tilePosX + THREE_QUARTER_TILE;
                vertexZ = tilePosY + HALF_TILE;
                vertexY = heightSE + heightNE >> 1;
                vertexCOverlay = overlaySE + overlayNE >> 1;
                vertexCUnderlay = underlaySE + underlayNE >> 1;
            } else if (vertexType == 11) {
                vertexX = tilePosX + HALF_TILE;
                vertexZ = tilePosY + THREE_QUARTER_TILE;
                vertexY = heightNE + heightNW >> 1;
                vertexCOverlay = overlayNE + overlayNW >> 1;
                vertexCUnderlay = underlayNE + underlayNW >> 1;
            } else if (vertexType == 12) {
                vertexX = tilePosX + QUARTER_TILE;
                vertexZ = tilePosY + HALF_TILE;
                vertexY = heightNW + heightSW >> 1;
                vertexCOverlay = overlayNW + overlaySW >> 1;
                vertexCUnderlay = underlayNW + underlaySW >> 1;
            } else if (vertexType == 13) {
                vertexX = tilePosX + QUARTER_TILE;
                vertexZ = tilePosY + QUARTER_TILE;
                vertexY = heightSW;
                vertexCOverlay = overlaySW;
                vertexCUnderlay = underlaySW;
            } else if (vertexType == 14) {
                vertexX = tilePosX + THREE_QUARTER_TILE;
                vertexZ = tilePosY + QUARTER_TILE;
                vertexY = heightSE;
                vertexCOverlay = overlaySE;
                vertexCUnderlay = underlaySE;
            } else if (vertexType == 15) {
                vertexX = tilePosX + THREE_QUARTER_TILE;
                vertexZ = tilePosY + THREE_QUARTER_TILE;
                vertexY = heightNE;
                vertexCOverlay = overlayNE;
                vertexCUnderlay = underlayNE;
            } else {
                vertexX = tilePosX + QUARTER_TILE;
                vertexZ = tilePosY + THREE_QUARTER_TILE;
                vertexY = heightNW;
                vertexCOverlay = overlayNW;
                vertexCUnderlay = underlayNW;
            }
            this.vertexX[vertex] = vertexX;
            this.vertexY[vertex] = vertexY;
            this.vertexZ[vertex] = vertexZ;
            vertexColourOverlay[vertex] = vertexCOverlay;
            vertexColourUnderlay[vertex] = vertexCUnderlay;
        }

        final int[] shapedTileElements = shapedTileElementData[shape];
        final int vertexCount = shapedTileElements.length / 4;
        this.triangleA = new int[vertexCount];
        this.triangleB = new int[vertexCount];
        this.triangleC = new int[vertexCount];
        this.triangleHSLA = new int[vertexCount];
        this.triangleHSLB = new int[vertexCount];
        this.triangleHSLC = new int[vertexCount];
        if (texture != -1) {
            this.triangleTexture = new int[vertexCount];
        }
        int offset = 0;
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            final int overlayOrUnderlay = shapedTileElements[offset];
            int idxA = shapedTileElements[offset + 1];
            int idxB = shapedTileElements[offset + 2];
            int idxC = shapedTileElements[offset + 3];
            offset += 4;
            if (idxA < 4) {
                idxA = idxA - rotation & 3;
            }
            if (idxB < 4) {
                idxB = idxB - rotation & 3;
            }
            if (idxC < 4) {
                idxC = idxC - rotation & 3;
            }
            this.triangleA[vertex] = idxA;
            this.triangleB[vertex] = idxB;
            this.triangleC[vertex] = idxC;
            if (overlayOrUnderlay == 0) {
                this.triangleHSLA[vertex] = vertexColourOverlay[idxA];
                this.triangleHSLB[vertex] = vertexColourOverlay[idxB];
                this.triangleHSLC[vertex] = vertexColourOverlay[idxC];
                if (this.triangleTexture != null) {
                    this.triangleTexture[vertex] = -1;
                }
            } else {
                this.triangleHSLA[vertex] = vertexColourUnderlay[idxA];
                this.triangleHSLB[vertex] = vertexColourUnderlay[idxB];
                this.triangleHSLC[vertex] = vertexColourUnderlay[idxC];
                if (this.triangleTexture != null) {
                    this.triangleTexture[vertex] = texture;
                }
            }
        }

        int i9 = heightSW;
        int l9 = heightSE;
        if (heightSE < i9) {
            i9 = heightSE;
        }
        if (heightSE > l9) {
            l9 = heightSE;
        }
        if (heightNE < i9) {
            i9 = heightNE;
        }
        if (heightNE > l9) {
            l9 = heightNE;
        }
        if (heightNW < i9) {
            i9 = heightNW;
        }
        if (heightNW > l9) {
            l9 = heightNW;
        }
        i9 /= 14;
        l9 /= 14;
    }

}
