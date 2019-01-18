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

	public ShapedTile(int tileX, int yA, int yB, int yC, int yD, int tileZ, int rotation, int texture, int shape, int cA,
			int cAA, int cB, int cBA, int cC, int cCA, int cD, int cDA, int overlayRGB, int underlayRGB) {
		flat = !(yA != yB || yA != yD || yA != yC);
		this.shape = shape;
		this.rotation = rotation;
		this.underlayRGB = underlayRGB;
		this.overlayRGB = overlayRGB;
		char const512 = '\200';
		int const256 = const512 / 2;
		int const128 = const512 / 4;
		int const384 = (const512 * 3) / 4;
		int shapedTileMesh[] = shapedTilePointData[shape];
		int shapedTileMeshLength = shapedTileMesh.length;
		originalVertexX = new int[shapedTileMeshLength];
		originalVertexY = new int[shapedTileMeshLength];
		originalVertexZ = new int[shapedTileMeshLength];
		int vertexColourOverlay[] = new int[shapedTileMeshLength];
		int vertexColourUnderlay[] = new int[shapedTileMeshLength];
		int x512 = tileX * const512;
		int z512 = tileZ * const512;
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
			int vertexX;
			int vertexZ;
			int vertexY;
			int vertexCOverlay;
			int vertexCUnderlay;
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
			originalVertexX[vertex] = vertexX;
			originalVertexY[vertex] = vertexY;
			originalVertexZ[vertex] = vertexZ;
			vertexColourOverlay[vertex] = vertexCOverlay;
			vertexColourUnderlay[vertex] = vertexCUnderlay;
		}

		int shapedTileElements[] = shapedTileElementData[shape];
		int vertexCount = shapedTileElements.length / 4;
		triangleA = new int[vertexCount];
		triangleB = new int[vertexCount];
		triangleC = new int[vertexCount];
		triangleHSLA = new int[vertexCount];
		triangleHSLB = new int[vertexCount];
		triangleHSLC = new int[vertexCount];
		if (texture != -1) {
            triangleTexture = new int[vertexCount];
        }
		int offset = 0;
		for (int vertex = 0; vertex < vertexCount; vertex++) {
			int overlayOrUnderlay = shapedTileElements[offset];
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
			triangleA[vertex] = idxA;
			triangleB[vertex] = idxB;
			triangleC[vertex] = idxC;
			if (overlayOrUnderlay == 0) {
				triangleHSLA[vertex] = vertexColourOverlay[idxA];
				triangleHSLB[vertex] = vertexColourOverlay[idxB];
				triangleHSLC[vertex] = vertexColourOverlay[idxC];
				if (triangleTexture != null) {
                    triangleTexture[vertex] = -1;
                }
			} else {
				triangleHSLA[vertex] = vertexColourUnderlay[idxA];
				triangleHSLB[vertex] = vertexColourUnderlay[idxB];
				triangleHSLC[vertex] = vertexColourUnderlay[idxC];
				if (triangleTexture != null) {
                    triangleTexture[vertex] = texture;
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
