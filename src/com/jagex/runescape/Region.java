package com.jagex.runescape;

import com.jagex.runescape.definition.FloorDefinition;
import com.jagex.runescape.definition.GameObjectDefinition;
import com.jagex.runescape.scene.WorldController;

final class Region {

	private static int calculateNoise(int x, int seed) {
		int n = x + seed * 57;
		n = n << 13 ^ n;
		int noise = n * (n * n * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
		return noise >> 19 & 0xff;
	}

	private static int calculateVertexHeight(int x, int y) {
		int vertexHeight = (method176(x + 45365, y + 0x16713, 4) - 128)
				+ (method176(x + 10294, y + 37821, 2) - 128 >> 1) + (method176(x, y, 1) - 128 >> 2);
		vertexHeight = (int) (vertexHeight * 0.29999999999999999D) + 35;
		if (vertexHeight < 10) {
			vertexHeight = 10;
		} else if (vertexHeight > 60) {
			vertexHeight = 60;
		}
		return vertexHeight;
	}

	public static void forceRenderObject(WorldController worldController, int face, int y, int type, int plane,
			CollisionMap collisionMap, int groundArray[][][], int x, int objectId, int z) {
		int vertexHeightSW = groundArray[plane][x][y];
		int vertexHeightSE = groundArray[plane][x + 1][y];
		int vertexHeightNE = groundArray[plane][x + 1][y + 1];
		int vertexHeightNW = groundArray[plane][x][y + 1];
		int drawHeight = vertexHeightSW + vertexHeightSE + vertexHeightNE + vertexHeightNW >> 2;
		GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
		int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
		if (!definition.hasActions) {
			hash += 0x80000000;
		}
		byte config = (byte) ((face << 6) + type);
		if (type == 22) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(22, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 22, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addGroundDecoration(x, y, z, drawHeight, hash, ((animable)), config);
			if (definition.solid && definition.hasActions) {
				collisionMap.markBlocked(x, y);
			}
			return;
		}
		if (type == 10 || type == 11) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(10, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 10, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			if (animable != null) {
				int rotation = 0;
				if (type == 11) {
					rotation += 256;
				}
				int sizeX;
				int sizeY;
				if (face == 1 || face == 3) {
					sizeX = definition.sizeY;
					sizeY = definition.sizeX;
				} else {
					sizeX = definition.sizeX;
					sizeY = definition.sizeY;
				}
				worldController.addEntityB(x, y, z, drawHeight, rotation, sizeY, sizeX, hash, ((animable)), config);
			}
			if (definition.solid) {
				collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
			}
			return;
		}
		if (type >= 12) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(type, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, type, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addEntityB(x, y, z, drawHeight, 0, 1, 1, hash, ((animable)), config);
			if (definition.solid) {
				collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
			}
			return;
		}
		if (type == 0) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(0, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 0, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWall(x, y, z, drawHeight, POWERS_OF_TWO[face], 0, hash, ((animable)), null,
					config);
			if (definition.solid) {
				collisionMap.markWall(y, face, x, type, definition.walkable);
			}
			return;
		}
		if (type == 1) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(1, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 1, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWall(x, y, z, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((animable)),
					null, config);
			if (definition.solid) {
				collisionMap.markWall(y, face, x, type, definition.walkable);
			}
			return;
		}
		if (type == 2) {
			int _face = face + 1 & 3;
			Animable animable1;
			Animable animable2;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable1 = definition.getModelAt(2, 4 + face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
				animable2 = definition.getModelAt(2, _face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable1 = new GameObject(objectId, 4 + face, 2, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
				animable2 = new GameObject(objectId, _face, 2, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWall(x, y, z, drawHeight, POWERS_OF_TWO[face], POWERS_OF_TWO[_face], hash,
					((animable1)), ((animable2)), config);
			if (definition.solid) {
				collisionMap.markWall(y, face, x, type, definition.walkable);
			}
			return;
		}
		if (type == 3) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(3, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 3, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWall(x, y, z, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((animable)),
					null, config);
			if (definition.solid) {
				collisionMap.markWall(y, face, x, type, definition.walkable);
			}
			return;
		}
		if (type == 9) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(type, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, type, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addEntityB(x, y, z, drawHeight, 0, 1, 1, hash, ((animable)), config);
			if (definition.solid) {
				collisionMap.markSolidOccupant(x, y, definition.sizeX, definition.sizeY, face, definition.walkable);
			}
			return;
		}
		if (definition.adjustToTerrain) {
			if (face == 1) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightNE;
				vertexHeightNE = vertexHeightSE;
				vertexHeightSE = vertexHeightSW;
				vertexHeightSW = temp;
			} else if (face == 2) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightSE;
				vertexHeightSE = temp;
				temp = vertexHeightNE;
				vertexHeightNE = vertexHeightSW;
				vertexHeightSW = temp;
			} else if (face == 3) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightSW;
				vertexHeightSW = vertexHeightSE;
				vertexHeightSE = vertexHeightNE;
				vertexHeightNE = temp;
			}
		}
		if (type == 4) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
						-1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face * 512, hash, ((animable)), config,
					POWERS_OF_TWO[face]);
			return;
		}
		if (type == 5) {
			int offsetAmplifier = 16;
			int objectHash = worldController.getWallObjectHash(x, y, z);
			if (objectHash > 0) {
				offsetAmplifier = GameObjectDefinition.getDefinition(objectHash >> 14 & 0x7fff).offsetAmplifier;
			}
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
						-1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWallDecoration(x, y, z, drawHeight, FACE_OFFSET_X[face] * offsetAmplifier,
					FACE_OFFSET_Y[face] * offsetAmplifier, face * 512, hash, ((animable)), config, POWERS_OF_TWO[face]);
			return;
		}
		if (type == 6) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
						-1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((animable)), config, 256);
			return;
		}
		if (type == 7) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
						-1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((animable)), config, 512);
			return;
		}
		if (type == 8) {
			Animable animable;
			if (definition.animationId == -1 && definition.childIds == null) {
				animable = definition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
						-1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, definition.animationId, true);
			}
			worldController.addWallDecoration(x, y, z, drawHeight, 0, 0, face, hash, ((animable)), config, 768);
		}
	}

	private static int interpolate(int a, int b, int delta, int deltaScale) {
		int f = 0x10000 - Rasterizer.COSINE[(delta * 1024) / deltaScale] >> 1;
		return (a * (0x10000 - f) >> 16) + (b * f >> 16);
	}

	private static int method176(int deltaX, int deltaY, int deltaScale) {
		int x = deltaX / deltaScale;
		int deltaPrimary = deltaX & deltaScale - 1;
		int y = deltaY / deltaScale;
		int deltaSecondary = deltaY & deltaScale - 1;
		int noiseSW = randomNoiseWeighedSum(x, y);
		int noiseSE = randomNoiseWeighedSum(x + 1, y);
		int noiseNE = randomNoiseWeighedSum(x, y + 1);
		int noiseNW = randomNoiseWeighedSum(x + 1, y + 1);
		int interpolationA = interpolate(noiseSW, noiseSE, deltaPrimary, deltaScale);
		int interpolationB = interpolate(noiseNE, noiseNW, deltaPrimary, deltaScale);
		return interpolate(interpolationA, interpolationB, deltaSecondary, deltaScale);
	}

	private static int mixLightness(int hsl, int lightness) {
		if (hsl == -1) {
			return 0xbc614e;
		}
		lightness = (lightness * (hsl & 0x7f)) / 128;
		if (lightness < 2) {
			lightness = 2;
		} else if (lightness > 126) {
			lightness = 126;
		}
		return (hsl & 0xff80) + lightness;
	}

	public static boolean modelTypeCached(int objectId, int type) {
		GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
		if (type == 11) {
			type = 10;
		}
		if (type >= 5 && type <= 8) {
			type = 4;
		}
		return definition.modelTypeCached(type);
	}

	public static void passivelyRequestGameObjectModels(Buffer stream, OnDemandFetcher onDemandFetcher) {
		start: {
			int objectId = -1;
			do {
				int objectIdOffset = stream.getSmartB();
				if (objectIdOffset == 0) {
					break start;
				}
				objectId += objectIdOffset;
				GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
				definition.passivelyRequestModels(onDemandFetcher);
				do {
					int terminate = stream.getSmartB();
					if (terminate == 0) {
						break;
					}
					stream.getUnsignedByte();
				} while (true);
			} while (true);
		}
	}

	private static int randomNoiseWeighedSum(int x, int y) {
		int vDist2 = calculateNoise(x - 1, y - 1) + calculateNoise(x + 1, y - 1) + calculateNoise(x - 1, y + 1)
				+ calculateNoise(x + 1, y + 1);
		int vDist1 = calculateNoise(x - 1, y) + calculateNoise(x + 1, y) + calculateNoise(x, y - 1)
				+ calculateNoise(x, y + 1);
		int vLocal = calculateNoise(x, y);
		return vDist2 / 16 + vDist1 / 8 + vLocal / 4;
	}

	public static boolean regionCached(int regionX, int regionY, byte[] objectData) {
		boolean cached = true;
		Buffer objectDataStream = new Buffer(objectData);
		int objectId = -1;
		do {
			int objectIdIncrement = objectDataStream.getSmartB();
			if (objectIdIncrement == 0) {
				break;
			}
			objectId += objectIdIncrement;
			int pos = 0;
			boolean readSecondValue = false;
			do {
				if (readSecondValue) {
					int secondValue = objectDataStream.getSmartB();
					if (secondValue == 0) {
						break;
					}
					objectDataStream.getUnsignedByte();
				} else {
					int positionOffset = objectDataStream.getSmartB();
					if (positionOffset == 0) {
						break;
					}
					pos += positionOffset - 1;
					int regionOffsetY = pos & 0x3f;
					int regionOffsetX = pos >> 6 & 0x3f;
					int objectType = objectDataStream.getUnsignedByte() >> 2;
					int objectX = regionOffsetX + regionX;
					int objectY = regionOffsetY + regionY;
					if (objectX > 0 && objectY > 0 && objectX < 103 && objectY < 103) {
						GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
						if (objectType != 22 || !lowMemory || definition.hasActions || definition.unknownAttribute) {
							cached &= definition.modelCached();
							readSecondValue = true;
						}
					}
				}
			} while (true);
		} while (true);

		return cached;
	}

	private static int randomiserHue = (int) (Math.random() * 17D) - 8;

	private final int[] blendedHue;

	private final int[] blendedSaturation;

	private final int[] blendedLightness;

	private final int[] blendedHueDivisor;

	private final int[] blendDirectionTracker;

	private final int[][][] vertexHeights;

	private final byte[][][] overlayFloorIds;

	static int plane;

	private static int randomiserLightness = (int) (Math.random() * 33D) - 16;

	private final byte[][][] tileShadowIntensity;

	private final int[][][] tileCullingBitsets;

	private final byte[][][] overlayClippingPaths;
	private static final int FACE_OFFSET_X[] = { 1, 0, -1, 0 };
	private final int[][] tileLightIntensity;
	private static final int WALL_CORNER_ORIENTATION[] = { 16, 32, 64, 128 };
	private final byte[][][] underlayFloorIds;
	private static final int FACE_OFFSET_Y[] = { 0, -1, 0, 1 };
	static int lowestPlane = 99;
	private final int regionSizeX;
	private final int regionSizeY;
	private final byte[][][] overlayOrientations;
	private final byte[][][] renderRuleFlags;
	static boolean lowMemory = true;
	private static final int POWERS_OF_TWO[] = { 1, 2, 4, 8 };

	public Region(byte renderRuleFlags[][][], int vertexHeights[][][]) {
		lowestPlane = 99;
		this.regionSizeX = 104;
		this.regionSizeY = 104;
		this.vertexHeights = vertexHeights;
		this.renderRuleFlags = renderRuleFlags;
		this.underlayFloorIds = new byte[4][this.regionSizeX][this.regionSizeY];
		this.overlayFloorIds = new byte[4][this.regionSizeX][this.regionSizeY];
		this.overlayClippingPaths = new byte[4][this.regionSizeX][this.regionSizeY];
		this.overlayOrientations = new byte[4][this.regionSizeX][this.regionSizeY];
		this.tileCullingBitsets = new int[4][this.regionSizeX + 1][this.regionSizeY + 1];
		this.tileShadowIntensity = new byte[4][this.regionSizeX + 1][this.regionSizeY + 1];
		this.tileLightIntensity = new int[this.regionSizeX + 1][this.regionSizeY + 1];
		this.blendedHue = new int[this.regionSizeY];
		this.blendedSaturation = new int[this.regionSizeY];
		this.blendedLightness = new int[this.regionSizeY];
		this.blendedHueDivisor = new int[this.regionSizeY];
		this.blendDirectionTracker = new int[this.regionSizeY];
	}

	public final void createRegion(CollisionMap collisionMap[], WorldController worldController) {
		for (int plane = 0; plane < 4; plane++) {
			for (int x = 0; x < 104; x++) {
				for (int y = 0; y < 104; y++) {
					if ((this.renderRuleFlags[plane][x][y] & 1) == 1) {
						int markingPlane = plane;
						if ((this.renderRuleFlags[1][x][y] & 2) == 2) {
							markingPlane--;
						}
						if (markingPlane >= 0) {
							collisionMap[markingPlane].markBlocked(x, y);
						}
					}
				}
			}
		}
		randomiserHue += (int) (Math.random() * 5D) - 2;
		if (randomiserHue < -8) {
			randomiserHue = -8;
		}
		if (randomiserHue > 8) {
			randomiserHue = 8;
		}
		randomiserLightness += (int) (Math.random() * 5D) - 2;
		if (randomiserLightness < -16) {
			randomiserLightness = -16;
		}
		if (randomiserLightness > 16) {
			randomiserLightness = 16;
		}
		for (int _plane = 0; _plane < 4; _plane++) {
			byte shadowIntensity[][] = this.tileShadowIntensity[_plane];
			int directionalLightIntensityInitial = 96;
			int specularDistributionFactor = 768;
			int directionalLightX = -50;
			int directionalLightZ = -10;
			int directionalLightY = -50;
			int directionalLightLength = (int) Math.sqrt(directionalLightX * directionalLightX
					+ directionalLightZ * directionalLightZ + directionalLightY * directionalLightY);
			int specularDistribution = specularDistributionFactor * directionalLightLength >> 8;
			for (int y = 1; y < this.regionSizeY - 1; y++) {
				for (int x = 1; x < this.regionSizeX - 1; x++) {
					int heightDifferenceX = this.vertexHeights[_plane][x + 1][y] - this.vertexHeights[_plane][x - 1][y];
					int heightDifferenceY = this.vertexHeights[_plane][x][y + 1] - this.vertexHeights[_plane][x][y - 1];
					int normalisedLength = (int) Math.sqrt(
							heightDifferenceX * heightDifferenceX + 0x10000 + heightDifferenceY * heightDifferenceY);
					int normalisedX = (heightDifferenceX << 8) / normalisedLength;
					int normalisedZ = 0x10000 / normalisedLength;
					int normalisedY = (heightDifferenceY << 8) / normalisedLength;
					int directionalLightIntensity = directionalLightIntensityInitial + (directionalLightX * normalisedX
							+ directionalLightZ * normalisedZ + directionalLightY * normalisedY) / specularDistribution;
					int weightedShadowIntensity = (shadowIntensity[x - 1][y] >> 2) + (shadowIntensity[x + 1][y] >> 3)
							+ (shadowIntensity[x][y - 1] >> 2) + (shadowIntensity[x][y + 1] >> 3)
							+ (shadowIntensity[x][y] >> 1);
					this.tileLightIntensity[x][y] = directionalLightIntensity - weightedShadowIntensity;
				}
			}

			for (int y = 0; y < this.regionSizeY; y++) {
				this.blendedHue[y] = 0;
				this.blendedSaturation[y] = 0;
				this.blendedLightness[y] = 0;
				this.blendedHueDivisor[y] = 0;
				this.blendDirectionTracker[y] = 0;
			}

			for (int x = -5; x < this.regionSizeX + 5; x++) {
				for (int y = 0; y < this.regionSizeY; y++) {
					int positiveX = x + 5;
					if (positiveX >= 0 && positiveX < this.regionSizeX) {
						int floorId = this.underlayFloorIds[_plane][positiveX][y] & 0xff;
						if (floorId > 0) {
							FloorDefinition definition = FloorDefinition.cache[floorId - 1];
							this.blendedHue[y] += definition.hue;
							this.blendedSaturation[y] += definition.saturation;
							this.blendedLightness[y] += definition.lightness;
							this.blendedHueDivisor[y] += definition.hueDivisor;
							this.blendDirectionTracker[y]++;
						}
					}
					int negativeX = x - 5;
					if (negativeX >= 0 && negativeX < this.regionSizeX) {
						int floorId = this.underlayFloorIds[_plane][negativeX][y] & 0xff;
						if (floorId > 0) {
							FloorDefinition definition = FloorDefinition.cache[floorId - 1];
							this.blendedHue[y] -= definition.hue;
							this.blendedSaturation[y] -= definition.saturation;
							this.blendedLightness[y] -= definition.lightness;
							this.blendedHueDivisor[y] -= definition.hueDivisor;
							this.blendDirectionTracker[y]--;
						}
					}
				}

				if (x >= 1 && x < this.regionSizeX - 1) {
					int hue = 0;
					int saturation = 0;
					int lightness = 0;
					int hueDivisor = 0;
					int direction = 0;
					for (int y = -5; y < this.regionSizeY + 5; y++) {
						int positiveY = y + 5;
						if (positiveY >= 0 && positiveY < this.regionSizeY) {
							hue += this.blendedHue[positiveY];
							saturation += this.blendedSaturation[positiveY];
							lightness += this.blendedLightness[positiveY];
							hueDivisor += this.blendedHueDivisor[positiveY];
							direction += this.blendDirectionTracker[positiveY];
						}
						int negativeY = y - 5;
						if (negativeY >= 0 && negativeY < this.regionSizeY) {
							hue -= this.blendedHue[negativeY];
							saturation -= this.blendedSaturation[negativeY];
							lightness -= this.blendedLightness[negativeY];
							hueDivisor -= this.blendedHueDivisor[negativeY];
							direction -= this.blendDirectionTracker[negativeY];
						}
						if (y >= 1 && y < this.regionSizeY - 1
								&& (!lowMemory || (this.renderRuleFlags[0][x][y] & 2) != 0
										|| (this.renderRuleFlags[_plane][x][y] & 0x10) == 0
												&& this.getVisibilityPlane(y, _plane, x) == plane)) {
							if (_plane < lowestPlane) {
								lowestPlane = _plane;
							}
							int underlayFloorId = this.underlayFloorIds[_plane][x][y] & 0xff;
							int overlayFloorId = this.overlayFloorIds[_plane][x][y] & 0xff;
							if (underlayFloorId > 0 || overlayFloorId > 0) {
								int vertexHeightSW = this.vertexHeights[_plane][x][y];
								int vertexHeightSE = this.vertexHeights[_plane][x + 1][y];
								int vertexHeightNE = this.vertexHeights[_plane][x + 1][y + 1];
								int vertexHeightNW = this.vertexHeights[_plane][x][y + 1];
								int lightIntensitySW = this.tileLightIntensity[x][y];
								int lightIntensitySE = this.tileLightIntensity[x + 1][y];
								int lightIntensityNE = this.tileLightIntensity[x + 1][y + 1];
								int lightIntensityNW = this.tileLightIntensity[x][y + 1];
								int hslBitsetOriginal = -1;
								int hslBitsetRandomised = -1;
								if (underlayFloorId > 0) {
									int h = (hue * 256) / hueDivisor;
									int s = saturation / direction;
									int l = lightness / direction;
									hslBitsetOriginal = this.generateHSLBitset(h, s, l);
									h = h + randomiserHue & 0xff;
									l += randomiserLightness;
									if (l < 0) {
										l = 0;
									} else if (l > 255) {
										l = 255;
									}
									hslBitsetRandomised = this.generateHSLBitset(h, s, l);
								}
								if (_plane > 0) {
									boolean hideUnderlay = true;
									if (underlayFloorId == 0 && this.overlayClippingPaths[_plane][x][y] != 0) {
										hideUnderlay = false;
									}
									if (overlayFloorId > 0 && !FloorDefinition.cache[overlayFloorId - 1].occlude) {
										hideUnderlay = false;
									}
									if (hideUnderlay && vertexHeightSW == vertexHeightSE
											&& vertexHeightSW == vertexHeightNE && vertexHeightSW == vertexHeightNW) {
										this.tileCullingBitsets[_plane][x][y] |= 0x924;
									}
								}
								int underlayMinimapColour = 0;
								if (hslBitsetOriginal != -1) {
									underlayMinimapColour = Rasterizer.HSL_TO_RGB[mixLightness(hslBitsetRandomised,
											96)];
								}
								if (overlayFloorId == 0) {
									worldController.renderTile(_plane, x, y, 0, 0, -1, vertexHeightSW, vertexHeightSE,
											vertexHeightNE, vertexHeightNW,
											mixLightness(hslBitsetOriginal, lightIntensitySW),
											mixLightness(hslBitsetOriginal, lightIntensitySE),
											mixLightness(hslBitsetOriginal, lightIntensityNE),
											mixLightness(hslBitsetOriginal, lightIntensityNW), 0, 0, 0, 0,
											underlayMinimapColour, 0);
								} else {
									int clippingPath = this.overlayClippingPaths[_plane][x][y] + 1;
									byte clippingPathRotation = this.overlayOrientations[_plane][x][y];
									FloorDefinition definition = FloorDefinition.cache[overlayFloorId - 1];
									int textureId = definition.textureId;
									int hslBitset;
									int overlayMinimapColour;
									if (textureId >= 0) {
										overlayMinimapColour = Rasterizer.getAverageTextureColour(textureId);
										hslBitset = -1;
									} else if (definition.rgbColour == 0xff00ff) {
										overlayMinimapColour = 0;
										hslBitset = -2;
										textureId = -1;
									} else {
										hslBitset = this.generateHSLBitset(definition.hue2, definition.saturation,
												definition.lightness);
										overlayMinimapColour = Rasterizer.HSL_TO_RGB[this.mixLightnessSigned(definition.hsl,
												96)];
									}
									worldController.renderTile(_plane, x, y, clippingPath, clippingPathRotation,
											textureId, vertexHeightSW, vertexHeightSE, vertexHeightNE, vertexHeightNW,
											mixLightness(hslBitsetOriginal, lightIntensitySW),
											mixLightness(hslBitsetOriginal, lightIntensitySE),
											mixLightness(hslBitsetOriginal, lightIntensityNE),
											mixLightness(hslBitsetOriginal, lightIntensityNW),
											this.mixLightnessSigned(hslBitset, lightIntensitySW),
											this.mixLightnessSigned(hslBitset, lightIntensitySE),
											this.mixLightnessSigned(hslBitset, lightIntensityNE),
											this.mixLightnessSigned(hslBitset, lightIntensityNW), underlayMinimapColour,
											overlayMinimapColour);
								}
							}
						}
					}

				}
			}

			for (int y = 1; y < this.regionSizeY - 1; y++) {
				for (int x = 1; x < this.regionSizeX - 1; x++) {
					worldController.setTileLogicHeight(x, y, _plane, this.getVisibilityPlane(y, _plane, x));
				}

			}

		}

		worldController.shadeModels(-10, -50, -50);
		for (int x = 0; x < this.regionSizeX; x++) {
			for (int y = 0; y < this.regionSizeY; y++) {
				if ((this.renderRuleFlags[1][x][y] & 2) == 2) {
					worldController.applyBridgeMode(x, y);
				}
			}
		}

		int renderRule1 = 1;
		int renderRule2 = 2;
		int renderRule3 = 4;
		for (int plane = 0; plane < 4; plane++) {
			if (plane > 0) {
				renderRule1 <<= 3;
				renderRule2 <<= 3;
				renderRule3 <<= 3;
			}
			for (int _plane = 0; _plane <= plane; _plane++) {
				for (int y = 0; y <= this.regionSizeY; y++) {
					for (int x = 0; x <= this.regionSizeX; x++) {
						if ((this.tileCullingBitsets[_plane][x][y] & renderRule1) != 0) {
							int lowestOcclusionY = y;
							int highestOcclusionY = y;
							int lowestOcclusionPlane = _plane;
							int highestOcclusionPlane = _plane;
							for (; lowestOcclusionY > 0 && (this.tileCullingBitsets[_plane][x][lowestOcclusionY - 1]
									& renderRule1) != 0; lowestOcclusionY--) {
								;
							}
							for (; highestOcclusionY < this.regionSizeY
									&& (this.tileCullingBitsets[_plane][x][highestOcclusionY + 1]
											& renderRule1) != 0; highestOcclusionY++) {
								;
							}
							findLowestOcclusionPlane: for (; lowestOcclusionPlane > 0; lowestOcclusionPlane--) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
									if ((this.tileCullingBitsets[lowestOcclusionPlane - 1][x][occludedY] & renderRule1) == 0) {
										break findLowestOcclusionPlane;
									}
								}

							}

							findHighestOcclusionPlane: for (; highestOcclusionPlane < plane; highestOcclusionPlane++) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
									if ((this.tileCullingBitsets[highestOcclusionPlane + 1][x][occludedY]
											& renderRule1) == 0) {
										break findHighestOcclusionPlane;
									}
								}
							}

							int occlusionSurface = ((highestOcclusionPlane + 1) - lowestOcclusionPlane)
									* ((highestOcclusionY - lowestOcclusionY) + 1);
							if (occlusionSurface >= 8) {
								int highestOcclusionVertexHeightOffset = 240;
								int highestOcclusionVertexHeight = this.vertexHeights[highestOcclusionPlane][x][lowestOcclusionY]
										- highestOcclusionVertexHeightOffset;
								int lowestOcclusionVertexHeight = this.vertexHeights[lowestOcclusionPlane][x][lowestOcclusionY];
								WorldController.createCullingCluster(plane, x * 128, x * 128,
										highestOcclusionY * 128 + 128, lowestOcclusionY * 128,
										highestOcclusionVertexHeight, lowestOcclusionVertexHeight, 1);
								for (int occludedPlane = lowestOcclusionPlane; occludedPlane <= highestOcclusionPlane; occludedPlane++) {
									for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
										this.tileCullingBitsets[occludedPlane][x][occludedY] &= ~renderRule1;
									}

								}

							}
						}

						if ((this.tileCullingBitsets[plane][x][y] & renderRule2) != 0) {
							int lowestOcclusionX = x;
							int highestOcclusionX = x;
							int lowestocclusionPlane = plane;
							int highestocclusionPlane = plane;
							for (; lowestOcclusionX > 0 && (this.tileCullingBitsets[plane][lowestOcclusionX - 1][y]
									& renderRule2) != 0; lowestOcclusionX--) {
								;
							}
							for (; highestOcclusionX < this.regionSizeX
									&& (this.tileCullingBitsets[plane][highestOcclusionX + 1][y]
											& renderRule2) != 0; highestOcclusionX++) {
								;
							}
							findLowestocclusionPlane: for (; lowestocclusionPlane > 0; lowestocclusionPlane--) {
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
									if ((this.tileCullingBitsets[lowestocclusionPlane - 1][occludedX][y] & renderRule2) == 0) {
										break findLowestocclusionPlane;
									}
								}
							}
							findHighestocclusionPlane: for (; highestocclusionPlane < plane; highestocclusionPlane++) {
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
									if ((this.tileCullingBitsets[highestocclusionPlane + 1][occludedX][y]
											& renderRule2) == 0) {
										break findHighestocclusionPlane;
									}
								}

							}

							int occlusionSurface = ((highestocclusionPlane + 1) - lowestocclusionPlane)
									* ((highestOcclusionX - lowestOcclusionX) + 1);
							if (occlusionSurface >= 8) {
								int highestOcclusionVertexHeightOffset = 240;
								int highestOcclusionVertexHeight = this.vertexHeights[highestocclusionPlane][lowestOcclusionX][y]
										- highestOcclusionVertexHeightOffset;
								int lowestOcclusionVertexHeight = this.vertexHeights[lowestocclusionPlane][lowestOcclusionX][y];
								WorldController.createCullingCluster(plane, highestOcclusionX * 128 + 128,
										lowestOcclusionX * 128, y * 128, y * 128, highestOcclusionVertexHeight,
										lowestOcclusionVertexHeight, 2);
								for (int occludedPlane = lowestocclusionPlane; occludedPlane <= highestocclusionPlane; occludedPlane++) {
									for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
										this.tileCullingBitsets[occludedPlane][occludedX][y] &= ~renderRule2;
									}

								}

							}
						}
						if ((this.tileCullingBitsets[plane][x][y] & renderRule3) != 0) {
							int lowestOcclusionX = x;
							int highestOcclusionX = x;
							int lowestOcclusionY = y;
							int highestOcclusionY = y;
							for (; lowestOcclusionY > 0 && (this.tileCullingBitsets[plane][x][lowestOcclusionY - 1]
									& renderRule3) != 0; lowestOcclusionY--) {
								;
							}
							for (; highestOcclusionY < this.regionSizeY
									&& (this.tileCullingBitsets[plane][x][highestOcclusionY + 1]
											& renderRule3) != 0; highestOcclusionY++) {
								;
							}
							findLowestOcclusionX: for (; lowestOcclusionX > 0; lowestOcclusionX--) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
									if ((this.tileCullingBitsets[plane][lowestOcclusionX - 1][occludedY] & renderRule3) == 0) {
										break findLowestOcclusionX;
									}
								}

							}

							findHighestOcclusionX: for (; highestOcclusionX < this.regionSizeX; highestOcclusionX++) {
								for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
									if ((this.tileCullingBitsets[plane][highestOcclusionX + 1][occludedY]
											& renderRule3) == 0) {
										break findHighestOcclusionX;
									}
								}

							}

							if (((highestOcclusionX - lowestOcclusionX) + 1)
									* ((highestOcclusionY - lowestOcclusionY) + 1) >= 4) {
								int lowestOcclusionVertexHeight = this.vertexHeights[plane][lowestOcclusionX][lowestOcclusionY];
								WorldController.createCullingCluster(plane, highestOcclusionX * 128 + 128,
										lowestOcclusionX * 128, highestOcclusionY * 128 + 128, lowestOcclusionY * 128,
										lowestOcclusionVertexHeight, lowestOcclusionVertexHeight, 4);
								for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
									for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
										this.tileCullingBitsets[plane][occludedX][occludedY] &= ~renderRule3;
									}

								}

							}

						}
					}

				}

			}

		}

	}

	private int generateHSLBitset(int h, int s, int l) {
		if (l > 179) {
			s /= 2;
		}
		if (l > 192) {
			s /= 2;
		}
		if (l > 217) {
			s /= 2;
		}
		if (l > 243) {
			s /= 2;
		}
		return (h / 4 << 10) + (s / 32 << 7) + l / 2;
	}

	private int getVisibilityPlane(int y, int plane, int x) {
		if ((this.renderRuleFlags[plane][x][y] & 8) != 0) {
			return 0;
		}
		if (plane > 0 && (this.renderRuleFlags[1][x][y] & 2) != 0) {
			return plane - 1;
		} else {
			return plane;
		}
	}

	public final void initiateVertexHeights(int startY, int countY, int countX, int startX) {
		for (int y = startY; y <= startY + countY; y++) {
			for (int x = startX; x <= startX + countX; x++) {
				if (x >= 0 && x < this.regionSizeX && y >= 0 && y < this.regionSizeY) {
					this.tileShadowIntensity[0][x][y] = 127;
					if (x == startX && x > 0) {
						this.vertexHeights[0][x][y] = this.vertexHeights[0][x - 1][y];
					}
					if (x == startX + countX && x < this.regionSizeX - 1) {
						this.vertexHeights[0][x][y] = this.vertexHeights[0][x + 1][y];
					}
					if (y == startY && y > 0) {
						this.vertexHeights[0][x][y] = this.vertexHeights[0][x][y - 1];
					}
					if (y == startY + countY && y < this.regionSizeY - 1) {
						this.vertexHeights[0][x][y] = this.vertexHeights[0][x][y + 1];
					}
				}
			}

		}
	}

	public final void loadObjectBlock(int blockX, CollisionMap collisionMap[], int blockY,
			WorldController worldController, byte blockData[]) {
		start: {
			Buffer stream = new Buffer(blockData);
			int objectId = -1;
			do {
				int objectIdOffset = stream.getSmartB();
				if (objectIdOffset == 0) {
					break start;
				}
				objectId += objectIdOffset;
				int position = 0;
				do {
					int positionOffset = stream.getSmartB();
					if (positionOffset == 0) {
						break;
					}
					position += positionOffset - 1;
					int tileY = position & 0x3f;
					int tileX = position >> 6 & 0x3f;
					int tilePlane = position >> 12;
					int hash = stream.getUnsignedByte();
					int type = hash >> 2;
					int orientation = hash & 3;
					int x = tileX + blockX;
					int y = tileY + blockY;
					if (x > 0 && y > 0 && x < 103 && y < 103) {
						int markingPlane = tilePlane;
						if ((this.renderRuleFlags[1][x][y] & 2) == 2) {
							markingPlane--;
						}
						CollisionMap collisionMap_ = null;
						if (markingPlane >= 0) {
							collisionMap_ = collisionMap[markingPlane];
						}
						this.renderObject(y, worldController, collisionMap_, type, tilePlane, x, objectId, orientation);
					}
				} while (true);
			} while (true);
		}
	}

	public final void loadObjectSubblock(CollisionMap collisionMap[], WorldController worldController, int i, int j,
			int k, int objectPlane, byte blockData[], int i1, int rotation, int k1) {
		start: {
			Buffer stream = new Buffer(blockData);
			int objectId = -1;
			do {
				int objectIdOffset = stream.getSmartB();
				if (objectIdOffset == 0) {
					break start;
				}
				objectId += objectIdOffset;
				int position = 0;
				do {
					int positionOffset = stream.getSmartB();
					if (positionOffset == 0) {
						break;
					}
					position += positionOffset - 1;
					int regionY = position & 0x3f;
					int regionX = position >> 6 & 0x3f;
					int plane = position >> 12;
					int hash = stream.getUnsignedByte();
					int type = hash >> 2;
					int orientation = hash & 3;
					if (plane == i && regionX >= i1 && regionX < i1 + 8 && regionY >= k && regionY < k + 8) {
						GameObjectDefinition objectDefinition = GameObjectDefinition.getDefinition(objectId);
						int x = j + TiledUtils.getRotatedLandscapeChunkX(rotation, objectDefinition.sizeY, regionX & 7,
								regionY & 7, objectDefinition.sizeX);
						int y = k1 + TiledUtils.getRotatedLandscapeChunkY(regionY & 7, objectDefinition.sizeY, rotation,
								objectDefinition.sizeX, regionX & 7);
						if (x > 0 && y > 0 && x < 103 && y < 103) {
							int markingPlane = plane;
							if ((this.renderRuleFlags[1][x][y] & 2) == 2) {
								markingPlane--;
							}
							CollisionMap collisionMap_ = null;
							if (markingPlane >= 0) {
								collisionMap_ = collisionMap[markingPlane];
							}
							this.renderObject(y, worldController, collisionMap_, type, objectPlane, x, objectId,
									orientation + rotation & 3);
						}
					}
				} while (true);
			} while (true);
		}
	}

	public final void loadTerrainBlock(byte blockData[], int blockY, int blockX, int k, int l,
			CollisionMap collisionMap[]) {
		for (int plane = 0; plane < 4; plane++) {
			for (int tileX = 0; tileX < 64; tileX++) {
				for (int tileY = 0; tileY < 64; tileY++) {
					if (blockX + tileX > 0 && blockX + tileX < 103 && blockY + tileY > 0 && blockY + tileY < 103) {
						collisionMap[plane].clippingData[blockX + tileX][blockY + tileY] &= 0xfeffffff;
					}
				}

			}

		}

		Buffer stream = new Buffer(blockData);
		for (int plane = 0; plane < 4; plane++) {
			for (int tileX = 0; tileX < 64; tileX++) {
				for (int tileY = 0; tileY < 64; tileY++) {
					this.loadTerrainTile(tileY + blockY, l, stream, tileX + blockX, plane, 0, k);
				}

			}

		}
	}

	public final void loadTerrainSubblock(int subBlockZ, int rotation, CollisionMap collisionMap[], int mapRegionX,
			int subBlockX, byte terrainData[], int subBlockY, int blockPlane, int mapRegionY) {
		for (int regionX = 0; regionX < 8; regionX++) {
			for (int regionY = 0; regionY < 8; regionY++) {
				if (mapRegionX + regionX > 0 && mapRegionX + regionX < 103 && mapRegionY + regionY > 0
						&& mapRegionY + regionY < 103) {
					collisionMap[blockPlane].clippingData[mapRegionX + regionX][mapRegionY + regionY] &= 0xfeffffff;
				}
			}

		}
		Buffer terrainDataStream = new Buffer(terrainData);
		for (int plane = 0; plane < 4; plane++) {
			for (int regionX = 0; regionX < 64; regionX++) {
				for (int regionY = 0; regionY < 64; regionY++) {
					if (plane == subBlockZ && regionX >= subBlockX && regionX < subBlockX + 8 && regionY >= subBlockY
							&& regionY < subBlockY + 8) {
						this.loadTerrainTile(mapRegionY + TiledUtils.getRotatedMapChunkY(regionY & 7, rotation, regionX & 7),
								0, terrainDataStream,
								mapRegionX + TiledUtils.getRotatedMapChunkX(rotation, regionY & 7, regionX & 7),
								blockPlane, rotation, 0);
					} else {
						this.loadTerrainTile(-1, 0, terrainDataStream, -1, 0, 0, 0);
					}
				}

			}
		}
	}

	private void loadTerrainTile(int tileY, int offsetY, Buffer stream, int tileX, int tileZ, int i1, int offsetX) {
		if (tileX >= 0 && tileX < 104 && tileY >= 0 && tileY < 104) {
			this.renderRuleFlags[tileZ][tileX][tileY] = 0;
			do {
				int value = stream.getUnsignedByte();
				if (value == 0) {
					if (tileZ == 0) {
						this.vertexHeights[0][tileX][tileY] = -calculateVertexHeight(0xe3b7b + tileX + offsetX,
								0x87cce + tileY + offsetY) * 8;
						return;
					} else {
						this.vertexHeights[tileZ][tileX][tileY] = this.vertexHeights[tileZ - 1][tileX][tileY] - 240;
						return;
					}
				}
				if (value == 1) {
					int height = stream.getUnsignedByte();
					if (height == 1) {
						height = 0;
					}
					if (tileZ == 0) {
						this.vertexHeights[0][tileX][tileY] = -height * 8;
						return;
					} else {
						this.vertexHeights[tileZ][tileX][tileY] = this.vertexHeights[tileZ - 1][tileX][tileY] - height * 8;
						return;
					}
				}
				if (value <= 49) {
					this.overlayFloorIds[tileZ][tileX][tileY] = stream.get();
					this.overlayClippingPaths[tileZ][tileX][tileY] = (byte) ((value - 2) / 4);
					this.overlayOrientations[tileZ][tileX][tileY] = (byte) ((value - 2) + i1 & 3);
				} else if (value <= 81) {
					this.renderRuleFlags[tileZ][tileX][tileY] = (byte) (value - 49);
				} else {
					this.underlayFloorIds[tileZ][tileX][tileY] = (byte) (value - 81);
				}
			} while (true);
		}
		do {
			int value = stream.getUnsignedByte();
			if (value == 0) {
				break;
			}
			if (value == 1) {
				stream.getUnsignedByte();
				return;
			}
			if (value <= 49) {
				stream.getUnsignedByte();
			}
		} while (true);
	}

	private int mixLightnessSigned(int hsl, int lightness) {
		if (hsl == -2) {
			return 0xbc614e;
		}
		if (hsl == -1) {
			if (lightness < 0) {
				lightness = 0;
			} else if (lightness > 127) {
				lightness = 127;
			}
			lightness = 127 - lightness;
			return lightness;
		}
		lightness = (lightness * (hsl & 0x7f)) / 128;
		if (lightness < 2) {
			lightness = 2;
		} else if (lightness > 126) {
			lightness = 126;
		}
		return (hsl & 0xff80) + lightness;
	}

	private void renderObject(int y, WorldController worldController, CollisionMap collisionMap, int type, int plane,
			int x, int objectId, int face) {
		if (lowMemory && (this.renderRuleFlags[0][x][y] & 2) == 0) {
			if ((this.renderRuleFlags[plane][x][y] & 0x10) != 0) {
				return;
			}
			if (this.getVisibilityPlane(y, plane, x) != Region.plane) {
				return;
			}
		}
		if (plane < lowestPlane) {
			lowestPlane = plane;
		}
		int vertexHeightSW = this.vertexHeights[plane][x][y];
		int vertexHeightSE = this.vertexHeights[plane][x + 1][y];
		int vertexHeightNE = this.vertexHeights[plane][x + 1][y + 1];
		int vertexHeightNW = this.vertexHeights[plane][x][y + 1];
		int drawHeight = vertexHeightSW + vertexHeightSE + vertexHeightNE + vertexHeightNW >> 2;
		GameObjectDefinition objectDefinition = GameObjectDefinition.getDefinition(objectId);
		int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
		if (!objectDefinition.hasActions) {
			hash += 0x80000000;
		}
		byte config = (byte) ((face << 6) + type);
		if (type == 22) {
			if (lowMemory && !objectDefinition.hasActions && !objectDefinition.unknownAttribute) {
				return;
			}
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(22, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 22, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addGroundDecoration(x, y, plane, drawHeight, hash, ((animable)), config);
			if (objectDefinition.solid && objectDefinition.hasActions && collisionMap != null) {
				collisionMap.markBlocked(x, y);
			}
			return;
		}
		if (type == 10 || type == 11) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(10, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 10, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			if (animable != null) {
				int rotation = 0;
				if (type == 11) {
					rotation += 256;
				}
				int sizeX;
				int sizeY;
				if (face == 1 || face == 3) {
					sizeX = objectDefinition.sizeY;
					sizeY = objectDefinition.sizeX;
				} else {
					sizeX = objectDefinition.sizeX;
					sizeY = objectDefinition.sizeY;
				}
				if (worldController.addEntityB(x, y, plane, drawHeight, rotation, sizeY, sizeX, hash, ((animable)),
						config) && objectDefinition.castsShadow) {
					Model model;
					if (animable instanceof Model) {
						model = (Model) animable;
					} else {
						model = objectDefinition.getModelAt(10, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
								vertexHeightNW, -1);
					}
					if (model != null) {
						for (int _x = 0; _x <= sizeX; _x++) {
							for (int _y = 0; _y <= sizeY; _y++) {
								int intensity = model.diagonal2DAboveOrigin / 4;
								if (intensity > 30) {
									intensity = 30;
								}
								if (intensity > this.tileShadowIntensity[plane][x + _x][y + _y]) {
									this.tileShadowIntensity[plane][x + _x][y + _y] = (byte) intensity;
								}
							}

						}

					}
				}
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face,
						objectDefinition.walkable);
			}
			return;
		}
		if (type >= 12) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(type, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, type, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addEntityB(x, y, plane, drawHeight, 0, 1, 1, hash, ((animable)), config);
			if (type >= 12 && type <= 17 && type != 13 && plane > 0) {
				this.tileCullingBitsets[plane][x][y] |= 0x924;
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face,
						objectDefinition.walkable);
			}
			return;
		}
		if (type == 0) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(0, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 0, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWall(x, y, plane, drawHeight, POWERS_OF_TWO[face], 0, hash, ((animable)), null,
					config);
			if (face == 0) {
				if (objectDefinition.castsShadow) {
					this.tileShadowIntensity[plane][x][y] = 50;
					this.tileShadowIntensity[plane][x][y + 1] = 50;
				}
				if (objectDefinition.wall) {
					this.tileCullingBitsets[plane][x][y] |= 0x249;
				}
			} else if (face == 1) {
				if (objectDefinition.castsShadow) {
					this.tileShadowIntensity[plane][x][y + 1] = 50;
					this.tileShadowIntensity[plane][x + 1][y + 1] = 50;
				}
				if (objectDefinition.wall) {
					this.tileCullingBitsets[plane][x][y + 1] |= 0x492;
				}
			} else if (face == 2) {
				if (objectDefinition.castsShadow) {
					this.tileShadowIntensity[plane][x + 1][y] = 50;
					this.tileShadowIntensity[plane][x + 1][y + 1] = 50;
				}
				if (objectDefinition.wall) {
					this.tileCullingBitsets[plane][x + 1][y] |= 0x249;
				}
			} else if (face == 3) {
				if (objectDefinition.castsShadow) {
					this.tileShadowIntensity[plane][x][y] = 50;
					this.tileShadowIntensity[plane][x + 1][y] = 50;
				}
				if (objectDefinition.wall) {
					this.tileCullingBitsets[plane][x][y] |= 0x492;
				}
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
			}
			if (objectDefinition.offsetAmplifier != 16) {
				worldController.displaceWallDecoration(y, objectDefinition.offsetAmplifier, x, plane);
			}
			return;
		}
		if (type == 1) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(1, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 1, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWall(x, y, plane, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((animable)),
					null, config);
			if (objectDefinition.castsShadow) {
				if (face == 0) {
					this.tileShadowIntensity[plane][x][y + 1] = 50;
				} else if (face == 1) {
					this.tileShadowIntensity[plane][x + 1][y + 1] = 50;
				} else if (face == 2) {
					this.tileShadowIntensity[plane][x + 1][y] = 50;
				} else if (face == 3) {
					this.tileShadowIntensity[plane][x][y] = 50;
				}
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
			}
			return;
		}
		if (type == 2) {
			int orientation = face + 1 & 3;
			Animable animable1;
			Animable animable2;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable1 = objectDefinition.getModelAt(2, 4 + face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
				animable2 = objectDefinition.getModelAt(2, orientation, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable1 = new GameObject(objectId, 4 + face, 2, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
				animable2 = new GameObject(objectId, orientation, 2, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWall(x, y, plane, drawHeight, POWERS_OF_TWO[face], POWERS_OF_TWO[orientation],
					hash, ((animable1)), ((animable2)), config);
			if (objectDefinition.wall) {
				if (face == 0) {
					this.tileCullingBitsets[plane][x][y] |= 0x249;
					this.tileCullingBitsets[plane][x][y + 1] |= 0x492;
				} else if (face == 1) {
					this.tileCullingBitsets[plane][x][y + 1] |= 0x492;
					this.tileCullingBitsets[plane][x + 1][y] |= 0x249;
				} else if (face == 2) {
					this.tileCullingBitsets[plane][x + 1][y] |= 0x249;
					this.tileCullingBitsets[plane][x][y] |= 0x492;
				} else if (face == 3) {
					this.tileCullingBitsets[plane][x][y] |= 0x492;
					this.tileCullingBitsets[plane][x][y] |= 0x249;
				}
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
			}
			if (objectDefinition.offsetAmplifier != 16) {
				worldController.displaceWallDecoration(y, objectDefinition.offsetAmplifier, x, plane);
			}
			return;
		}
		if (type == 3) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(3, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, 3, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWall(x, y, plane, drawHeight, WALL_CORNER_ORIENTATION[face], 0, hash, ((animable)),
					null, config);
			if (objectDefinition.castsShadow) {
				if (face == 0) {
					this.tileShadowIntensity[plane][x][y + 1] = 50;
				} else if (face == 1) {
					this.tileShadowIntensity[plane][x + 1][y + 1] = 50;
				} else if (face == 2) {
					this.tileShadowIntensity[plane][x + 1][y] = 50;
				} else if (face == 3) {
					this.tileShadowIntensity[plane][x][y] = 50;
				}
			}
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markWall(y, face, x, type, objectDefinition.walkable);
			}
			return;
		}
		if (type == 9) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(type, face, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, face, type, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addEntityB(x, y, plane, drawHeight, 0, 1, 1, hash, ((animable)), config);
			if (objectDefinition.solid && collisionMap != null) {
				collisionMap.markSolidOccupant(x, y, objectDefinition.sizeX, objectDefinition.sizeY, face,
						objectDefinition.walkable);
			}
			return;
		}
		if (objectDefinition.adjustToTerrain) {
			if (face == 1) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightNE;
				vertexHeightNE = vertexHeightSE;
				vertexHeightSE = vertexHeightSW;
				vertexHeightSW = temp;
			} else if (face == 2) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightSE;
				vertexHeightSE = temp;
				temp = vertexHeightNE;
				vertexHeightNE = vertexHeightSW;
				vertexHeightSW = temp;
			} else if (face == 3) {
				int temp = vertexHeightNW;
				vertexHeightNW = vertexHeightSW;
				vertexHeightSW = vertexHeightSE;
				vertexHeightSE = vertexHeightNE;
				vertexHeightNE = temp;
			}
		}
		if (type == 4) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWallDecoration(x, y, plane, drawHeight, 0, 0, face * 512, hash, ((animable)), config,
					POWERS_OF_TWO[face]);
			return;
		}
		if (type == 5) {
			int offsetAmplifier = 16;
			int hash_ = worldController.getWallObjectHash(x, y, plane);
			if (hash_ > 0) {
				offsetAmplifier = GameObjectDefinition.getDefinition(hash_ >> 14 & 0x7fff).offsetAmplifier;
			}
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWallDecoration(x, y, plane, drawHeight, FACE_OFFSET_X[face] * offsetAmplifier,
					FACE_OFFSET_Y[face] * offsetAmplifier, face * 512, hash, ((animable)), config, POWERS_OF_TWO[face]);
			return;
		}
		if (type == 6) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWallDecoration(x, y, plane, drawHeight, 0, 0, face, hash, ((animable)), config, 256);
			return;
		}
		if (type == 7) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWallDecoration(x, y, plane, drawHeight, 0, 0, face, hash, ((animable)), config, 512);
			return;
		}
		if (type == 8) {
			Animable animable;
			if (objectDefinition.animationId == -1 && objectDefinition.childIds == null) {
				animable = objectDefinition.getModelAt(4, 0, vertexHeightSW, vertexHeightSE, vertexHeightNE,
						vertexHeightNW, -1);
			} else {
				animable = new GameObject(objectId, 0, 4, vertexHeightSE, vertexHeightNE, vertexHeightSW,
						vertexHeightNW, objectDefinition.animationId, true);
			}
			worldController.addWallDecoration(x, y, plane, drawHeight, 0, 0, face, hash, ((animable)), config, 768);
		}
	}

}
