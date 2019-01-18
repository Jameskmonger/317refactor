package com.jagex.runescape.scene.tile;

public final class ShapedTile {

	public final int[] originalVertexX;

	public final int[] originalVertexY;
	public final int[] originalVertexZ;
	public final int[] triangleHSLA;
	public final int[] triangleHSLB;
	public final int[] triangleHSLC;
	public final int[] triangleA;
	public final int[] triangleB;
	public final int[] triangleC;
	public int triangleTexture[];
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
	private static final int[][] shapedTilePointData = { { 1, 3, 5, 7 }, { 1, 3, 5, 7 }, { 1, 3, 5, 7 },
			{ 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 6 }, { 1, 3, 5, 7, 2, 6 },
			{ 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 2, 8 }, { 1, 3, 5, 7, 11, 12 }, { 1, 3, 5, 7, 11, 12 },
			{ 1, 3, 5, 7, 13, 14 } };
	private static final int[][] shapedTileElementData = { { 0, 1, 2, 3, 0, 0, 1, 3 }, { 1, 1, 2, 3, 1, 0, 1, 3 },
			{ 0, 1, 2, 3, 1, 0, 1, 3 }, { 0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3 }, { 0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4 },
			{ 0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4 }, { 0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3 },
			{ 0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3 }, { 0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5 },
			{ 0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5 },
			{ 0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3 },
			{ 1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3 },
			{ 1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5 } };

	public ShapedTile(final int tileX, final int yA, final int yB, final int yC, final int yD, final int tileZ, final int rotation, final int texture, final int shape, final int cA,
                      final int cAA, final int cB, final int cBA, final int cC, final int cCA, final int cD, final int cDA, final int overlayRGB, final int underlayRGB) {
        this.flat = !(yA != yB || yA != yD || yA != yC);
		this.shape = shape;
		this.rotation = rotation;
		this.underlayRGB = underlayRGB;
		this.overlayRGB = overlayRGB;
		final char const512 = '\200';
		final int const256 = const512 / 2;
		final int const128 = const512 / 4;
		final int const384 = (const512 * 3) / 4;
		final int[] shapedTileMesh = shapedTilePointData[shape];
		final int shapedTileMeshLength = shapedTileMesh.length;
        this.originalVertexX = new int[shapedTileMeshLength];
        this.originalVertexY = new int[shapedTileMeshLength];
        this.originalVertexZ = new int[shapedTileMeshLength];
		final int[] vertexColourOverlay = new int[shapedTileMeshLength];
		final int[] vertexColourUnderlay = new int[shapedTileMeshLength];
		final int x512 = tileX * const512;
		final int z512 = tileZ * const512;
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
				vertexX = x512;
				vertexZ = z512;
				vertexY = yA;
				vertexCOverlay = cA;
				vertexCUnderlay = cAA;
			} else if (vertexType == 2) {
				vertexX = x512 + const256;
				vertexZ = z512;
				vertexY = yA + yB >> 1;
				vertexCOverlay = cA + cB >> 1;
				vertexCUnderlay = cAA + cBA >> 1;
			} else if (vertexType == 3) {
				vertexX = x512 + const512;
				vertexZ = z512;
				vertexY = yB;
				vertexCOverlay = cB;
				vertexCUnderlay = cBA;
			} else if (vertexType == 4) {
				vertexX = x512 + const512;
				vertexZ = z512 + const256;
				vertexY = yB + yD >> 1;
				vertexCOverlay = cB + cD >> 1;
				vertexCUnderlay = cBA + cDA >> 1;
			} else if (vertexType == 5) {
				vertexX = x512 + const512;
				vertexZ = z512 + const512;
				vertexY = yD;
				vertexCOverlay = cD;
				vertexCUnderlay = cDA;
			} else if (vertexType == 6) {
				vertexX = x512 + const256;
				vertexZ = z512 + const512;
				vertexY = yD + yC >> 1;
				vertexCOverlay = cD + cC >> 1;
				vertexCUnderlay = cDA + cCA >> 1;
			} else if (vertexType == 7) {
				vertexX = x512;
				vertexZ = z512 + const512;
				vertexY = yC;
				vertexCOverlay = cC;
				vertexCUnderlay = cCA;
			} else if (vertexType == 8) {
				vertexX = x512;
				vertexZ = z512 + const256;
				vertexY = yC + yA >> 1;
				vertexCOverlay = cC + cA >> 1;
				vertexCUnderlay = cCA + cAA >> 1;
			} else if (vertexType == 9) {
				vertexX = x512 + const256;
				vertexZ = z512 + const128;
				vertexY = yA + yB >> 1;
				vertexCOverlay = cA + cB >> 1;
				vertexCUnderlay = cAA + cBA >> 1;
			} else if (vertexType == 10) {
				vertexX = x512 + const384;
				vertexZ = z512 + const256;
				vertexY = yB + yD >> 1;
				vertexCOverlay = cB + cD >> 1;
				vertexCUnderlay = cBA + cDA >> 1;
			} else if (vertexType == 11) {
				vertexX = x512 + const256;
				vertexZ = z512 + const384;
				vertexY = yD + yC >> 1;
				vertexCOverlay = cD + cC >> 1;
				vertexCUnderlay = cDA + cCA >> 1;
			} else if (vertexType == 12) {
				vertexX = x512 + const128;
				vertexZ = z512 + const256;
				vertexY = yC + yA >> 1;
				vertexCOverlay = cC + cA >> 1;
				vertexCUnderlay = cCA + cAA >> 1;
			} else if (vertexType == 13) {
				vertexX = x512 + const128;
				vertexZ = z512 + const128;
				vertexY = yA;
				vertexCOverlay = cA;
				vertexCUnderlay = cAA;
			} else if (vertexType == 14) {
				vertexX = x512 + const384;
				vertexZ = z512 + const128;
				vertexY = yB;
				vertexCOverlay = cB;
				vertexCUnderlay = cBA;
			} else if (vertexType == 15) {
				vertexX = x512 + const384;
				vertexZ = z512 + const384;
				vertexY = yD;
				vertexCOverlay = cD;
				vertexCUnderlay = cDA;
			} else {
				vertexX = x512 + const128;
				vertexZ = z512 + const384;
				vertexY = yC;
				vertexCOverlay = cC;
				vertexCUnderlay = cCA;
			}
            this.originalVertexX[vertex] = vertexX;
            this.originalVertexY[vertex] = vertexY;
            this.originalVertexZ[vertex] = vertexZ;
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

		int i9 = yA;
		int l9 = yB;
		if (yB < i9) {
            i9 = yB;
        }
		if (yB > l9) {
            l9 = yB;
        }
		if (yD < i9) {
            i9 = yD;
        }
		if (yD > l9) {
            l9 = yD;
        }
		if (yC < i9) {
            i9 = yC;
        }
		if (yC > l9) {
            l9 = yC;
        }
		i9 /= 14;
		l9 /= 14;
	}

}
