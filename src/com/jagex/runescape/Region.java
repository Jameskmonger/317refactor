package com.jagex.runescape;

import com.jagex.runescape.definition.FloorDefinition;
import com.jagex.runescape.definition.GameObjectDefinition;
import com.jagex.runescape.scene.WorldController;

final class Region {

    private static int calculateNoise(final int x, final int seed) {
        int n = x + seed * 57;
        n = n << 13 ^ n;
        final int noise = n * (n * n * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return noise >> 19 & 0xff;
    }

    private static int calculateVertexHeight(final int x, final int y) {
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

    public static void forceRenderObject(final WorldController worldController, final int face, final int y, final int type, final int plane,
                                         final CollisionMap collisionMap, final int[][][] groundArray, final int x, final int objectId, final int z) {
        int vertexHeightSW = groundArray[plane][x][y];
        int vertexHeightSE = groundArray[plane][x + 1][y];
        int vertexHeightNE = groundArray[plane][x + 1][y + 1];
        int vertexHeightNW = groundArray[plane][x][y + 1];
        final int drawHeight = vertexHeightSW + vertexHeightSE + vertexHeightNE + vertexHeightNW >> 2;
        final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
        int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
        if (!definition.hasActions) {
            hash += 0x80000000;
        }
        final byte config = (byte) ((face << 6) + type);
        if (type == 22) {
            final Animable animable;
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
            final Animable animable;
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
                final int sizeX;
                final int sizeY;
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
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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
            final int _face = face + 1 & 3;
            final Animable animable1;
            final Animable animable2;
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
            final Animable animable;
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
            final Animable animable;
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
                final int temp = vertexHeightNW;
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
                final int temp = vertexHeightNW;
                vertexHeightNW = vertexHeightSW;
                vertexHeightSW = vertexHeightSE;
                vertexHeightSE = vertexHeightNE;
                vertexHeightNE = temp;
            }
        }
        if (type == 4) {
            final Animable animable;
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
            final int objectHash = worldController.getWallObjectHash(x, y, z);
            if (objectHash > 0) {
                offsetAmplifier = GameObjectDefinition.getDefinition(objectHash >> 14 & 0x7fff).offsetAmplifier;
            }
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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

    private static int interpolate(final int a, final int b, final int delta, final int deltaScale) {
        final int f = 0x10000 - Rasterizer.COSINE[(delta * 1024) / deltaScale] >> 1;
        return (a * (0x10000 - f) >> 16) + (b * f >> 16);
    }

    private static int method176(final int deltaX, final int deltaY, final int deltaScale) {
        final int x = deltaX / deltaScale;
        final int deltaPrimary = deltaX & deltaScale - 1;
        final int y = deltaY / deltaScale;
        final int deltaSecondary = deltaY & deltaScale - 1;
        final int noiseSW = randomNoiseWeighedSum(x, y);
        final int noiseSE = randomNoiseWeighedSum(x + 1, y);
        final int noiseNE = randomNoiseWeighedSum(x, y + 1);
        final int noiseNW = randomNoiseWeighedSum(x + 1, y + 1);
        final int interpolationA = interpolate(noiseSW, noiseSE, deltaPrimary, deltaScale);
        final int interpolationB = interpolate(noiseNE, noiseNW, deltaPrimary, deltaScale);
        return interpolate(interpolationA, interpolationB, deltaSecondary, deltaScale);
    }

    private static int mixLightness(final int hsl, int lightness) {
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

    public static boolean modelTypeCached(final int objectId, int type) {
        final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
        if (type == 11) {
            type = 10;
        }
        if (type >= 5 && type <= 8) {
            type = 4;
        }
        return definition.modelTypeCached(type);
    }

    public static void passivelyRequestGameObjectModels(final Buffer stream, final OnDemandFetcher onDemandFetcher) {
        start:
        {
            int objectId = -1;
            do {
                final int objectIdOffset = stream.getSmartB();
                if (objectIdOffset == 0) {
                    break start;
                }
                objectId += objectIdOffset;
                final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
                definition.passivelyRequestModels(onDemandFetcher);
                do {
                    final int terminate = stream.getSmartB();
                    if (terminate == 0) {
                        break;
                    }
                    stream.getUnsignedByte();
                } while (true);
            } while (true);
        }
    }

    private static int randomNoiseWeighedSum(final int x, final int y) {
        final int vDist2 = calculateNoise(x - 1, y - 1) + calculateNoise(x + 1, y - 1) + calculateNoise(x - 1, y + 1)
            + calculateNoise(x + 1, y + 1);
        final int vDist1 = calculateNoise(x - 1, y) + calculateNoise(x + 1, y) + calculateNoise(x, y - 1)
            + calculateNoise(x, y + 1);
        final int vLocal = calculateNoise(x, y);
        return vDist2 / 16 + vDist1 / 8 + vLocal / 4;
    }

    public static boolean regionCached(final int regionX, final int regionY, final byte[] objectData) {
        boolean cached = true;
        final Buffer objectDataStream = new Buffer(objectData);
        int objectId = -1;
        do {
            final int objectIdIncrement = objectDataStream.getSmartB();
            if (objectIdIncrement == 0) {
                break;
            }
            objectId += objectIdIncrement;
            int pos = 0;
            boolean readSecondValue = false;
            do {
                if (readSecondValue) {
                    final int secondValue = objectDataStream.getSmartB();
                    if (secondValue == 0) {
                        break;
                    }
                    objectDataStream.getUnsignedByte();
                } else {
                    final int positionOffset = objectDataStream.getSmartB();
                    if (positionOffset == 0) {
                        break;
                    }
                    pos += positionOffset - 1;
                    final int regionOffsetY = pos & 0x3f;
                    final int regionOffsetX = pos >> 6 & 0x3f;
                    final int objectType = objectDataStream.getUnsignedByte() >> 2;
                    final int objectX = regionOffsetX + regionX;
                    final int objectY = regionOffsetY + regionY;
                    if (objectX > 0 && objectY > 0 && objectX < 103 && objectY < 103) {
                        final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
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
    private static final int[] FACE_OFFSET_X = {1, 0, -1, 0};
    private final int[][] tileLightIntensity;
    private static final int[] WALL_CORNER_ORIENTATION = {16, 32, 64, 128};
    private final byte[][][] underlayFloorIds;
    private static final int[] FACE_OFFSET_Y = {0, -1, 0, 1};
    static int lowestPlane = 99;
    private final int regionSizeX;
    private final int regionSizeY;
    private final byte[][][] overlayOrientations;
    private final byte[][][] renderRuleFlags;
    static boolean lowMemory = true;
    private static final int[] POWERS_OF_TWO = {1, 2, 4, 8};

    public Region(final byte[][][] renderRuleFlags, final int[][][] vertexHeights) {
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

    public void createRegion(final CollisionMap[] collisionMap, final WorldController worldController) {
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
            final byte[][] shadowIntensity = this.tileShadowIntensity[_plane];
            final int directionalLightIntensityInitial = 96;
            final int specularDistributionFactor = 768;
            final int directionalLightX = -50;
            final int directionalLightZ = -10;
            final int directionalLightY = -50;
            final int directionalLightLength = (int) Math.sqrt(directionalLightX * directionalLightX
                + directionalLightZ * directionalLightZ + directionalLightY * directionalLightY);
            final int specularDistribution = specularDistributionFactor * directionalLightLength >> 8;
            for (int y = 1; y < this.regionSizeY - 1; y++) {
                for (int x = 1; x < this.regionSizeX - 1; x++) {
                    final int heightDifferenceX = this.vertexHeights[_plane][x + 1][y] - this.vertexHeights[_plane][x - 1][y];
                    final int heightDifferenceY = this.vertexHeights[_plane][x][y + 1] - this.vertexHeights[_plane][x][y - 1];
                    final int normalisedLength = (int) Math.sqrt(
                        heightDifferenceX * heightDifferenceX + 0x10000 + heightDifferenceY * heightDifferenceY);
                    final int normalisedX = (heightDifferenceX << 8) / normalisedLength;
                    final int normalisedZ = 0x10000 / normalisedLength;
                    final int normalisedY = (heightDifferenceY << 8) / normalisedLength;
                    final int directionalLightIntensity = directionalLightIntensityInitial + (directionalLightX * normalisedX
                        + directionalLightZ * normalisedZ + directionalLightY * normalisedY) / specularDistribution;
                    final int weightedShadowIntensity = (shadowIntensity[x - 1][y] >> 2) + (shadowIntensity[x + 1][y] >> 3)
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
                    final int positiveX = x + 5;
                    if (positiveX >= 0 && positiveX < this.regionSizeX) {
                        final int floorId = this.underlayFloorIds[_plane][positiveX][y] & 0xff;
                        if (floorId > 0) {
                            final FloorDefinition definition = FloorDefinition.cache[floorId - 1];
                            this.blendedHue[y] += definition.hue;
                            this.blendedSaturation[y] += definition.saturation;
                            this.blendedLightness[y] += definition.lightness;
                            this.blendedHueDivisor[y] += definition.hueDivisor;
                            this.blendDirectionTracker[y]++;
                        }
                    }
                    final int negativeX = x - 5;
                    if (negativeX >= 0 && negativeX < this.regionSizeX) {
                        final int floorId = this.underlayFloorIds[_plane][negativeX][y] & 0xff;
                        if (floorId > 0) {
                            final FloorDefinition definition = FloorDefinition.cache[floorId - 1];
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
                        final int positiveY = y + 5;
                        if (positiveY >= 0 && positiveY < this.regionSizeY) {
                            hue += this.blendedHue[positiveY];
                            saturation += this.blendedSaturation[positiveY];
                            lightness += this.blendedLightness[positiveY];
                            hueDivisor += this.blendedHueDivisor[positiveY];
                            direction += this.blendDirectionTracker[positiveY];
                        }
                        final int negativeY = y - 5;
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
                            final int underlayFloorId = this.underlayFloorIds[_plane][x][y] & 0xff;
                            final int overlayFloorId = this.overlayFloorIds[_plane][x][y] & 0xff;
                            if (underlayFloorId > 0 || overlayFloorId > 0) {
                                final int vertexHeightSW = this.vertexHeights[_plane][x][y];
                                final int vertexHeightSE = this.vertexHeights[_plane][x + 1][y];
                                final int vertexHeightNE = this.vertexHeights[_plane][x + 1][y + 1];
                                final int vertexHeightNW = this.vertexHeights[_plane][x][y + 1];
                                final int lightIntensitySW = this.tileLightIntensity[x][y];
                                final int lightIntensitySE = this.tileLightIntensity[x + 1][y];
                                final int lightIntensityNE = this.tileLightIntensity[x + 1][y + 1];
                                final int lightIntensityNW = this.tileLightIntensity[x][y + 1];
                                int hslBitsetOriginal = -1;
                                int hslBitsetRandomised = -1;
                                if (underlayFloorId > 0) {
                                    int h = (hue * 256) / hueDivisor;
                                    final int s = saturation / direction;
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
                                    worldController.renderTile(
                                        _plane,
                                        x,
                                        y,
                                        0,
                                        0,
                                        -1,
                                        vertexHeightSW,
                                        vertexHeightSE,
                                        vertexHeightNE,
                                        vertexHeightNW,
                                        mixLightness(hslBitsetOriginal, lightIntensitySW),
                                        mixLightness(hslBitsetOriginal, lightIntensitySE),
                                        mixLightness(hslBitsetOriginal, lightIntensityNE),
                                        mixLightness(hslBitsetOriginal, lightIntensityNW),
                                        0,
                                        0,
                                        0,
                                        0,
                                        underlayMinimapColour,
                                        0
                                    );
                                } else {
                                    final int clippingPath = this.overlayClippingPaths[_plane][x][y] + 1;
                                    final byte clippingPathRotation = this.overlayOrientations[_plane][x][y];
                                    final FloorDefinition definition = FloorDefinition.cache[overlayFloorId - 1];
                                    int textureId = definition.textureId;
                                    final int hslBitset;
                                    final int overlayMinimapColour;
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
                            }
                            for (; highestOcclusionY < this.regionSizeY
                                && (this.tileCullingBitsets[_plane][x][highestOcclusionY + 1]
                                & renderRule1) != 0; highestOcclusionY++) {
                            }
                            findLowestOcclusionPlane:
                            for (; lowestOcclusionPlane > 0; lowestOcclusionPlane--) {
                                for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
                                    if ((this.tileCullingBitsets[lowestOcclusionPlane - 1][x][occludedY] & renderRule1) == 0) {
                                        break findLowestOcclusionPlane;
                                    }
                                }

                            }

                            findHighestOcclusionPlane:
                            for (; highestOcclusionPlane < plane; highestOcclusionPlane++) {
                                for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
                                    if ((this.tileCullingBitsets[highestOcclusionPlane + 1][x][occludedY]
                                        & renderRule1) == 0) {
                                        break findHighestOcclusionPlane;
                                    }
                                }
                            }

                            final int occlusionSurface = ((highestOcclusionPlane + 1) - lowestOcclusionPlane)
                                * ((highestOcclusionY - lowestOcclusionY) + 1);
                            if (occlusionSurface >= 8) {
                                final int highestOcclusionVertexHeightOffset = 240;
                                final int highestOcclusionVertexHeight = this.vertexHeights[highestOcclusionPlane][x][lowestOcclusionY]
                                    - highestOcclusionVertexHeightOffset;
                                final int lowestOcclusionVertexHeight = this.vertexHeights[lowestOcclusionPlane][x][lowestOcclusionY];
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
                            }
                            for (; highestOcclusionX < this.regionSizeX
                                && (this.tileCullingBitsets[plane][highestOcclusionX + 1][y]
                                & renderRule2) != 0; highestOcclusionX++) {
                            }
                            findLowestocclusionPlane:
                            for (; lowestocclusionPlane > 0; lowestocclusionPlane--) {
                                for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
                                    if ((this.tileCullingBitsets[lowestocclusionPlane - 1][occludedX][y] & renderRule2) == 0) {
                                        break findLowestocclusionPlane;
                                    }
                                }
                            }
                            findHighestocclusionPlane:
                            for (; highestocclusionPlane < plane; highestocclusionPlane++) {
                                for (int occludedX = lowestOcclusionX; occludedX <= highestOcclusionX; occludedX++) {
                                    if ((this.tileCullingBitsets[highestocclusionPlane + 1][occludedX][y]
                                        & renderRule2) == 0) {
                                        break findHighestocclusionPlane;
                                    }
                                }

                            }

                            final int occlusionSurface = ((highestocclusionPlane + 1) - lowestocclusionPlane)
                                * ((highestOcclusionX - lowestOcclusionX) + 1);
                            if (occlusionSurface >= 8) {
                                final int highestOcclusionVertexHeightOffset = 240;
                                final int highestOcclusionVertexHeight = this.vertexHeights[highestocclusionPlane][lowestOcclusionX][y]
                                    - highestOcclusionVertexHeightOffset;
                                final int lowestOcclusionVertexHeight = this.vertexHeights[lowestocclusionPlane][lowestOcclusionX][y];
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
                            }
                            for (; highestOcclusionY < this.regionSizeY
                                && (this.tileCullingBitsets[plane][x][highestOcclusionY + 1]
                                & renderRule3) != 0; highestOcclusionY++) {
                            }
                            findLowestOcclusionX:
                            for (; lowestOcclusionX > 0; lowestOcclusionX--) {
                                for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
                                    if ((this.tileCullingBitsets[plane][lowestOcclusionX - 1][occludedY] & renderRule3) == 0) {
                                        break findLowestOcclusionX;
                                    }
                                }

                            }

                            findHighestOcclusionX:
                            for (; highestOcclusionX < this.regionSizeX; highestOcclusionX++) {
                                for (int occludedY = lowestOcclusionY; occludedY <= highestOcclusionY; occludedY++) {
                                    if ((this.tileCullingBitsets[plane][highestOcclusionX + 1][occludedY]
                                        & renderRule3) == 0) {
                                        break findHighestOcclusionX;
                                    }
                                }

                            }

                            if (((highestOcclusionX - lowestOcclusionX) + 1)
                                * ((highestOcclusionY - lowestOcclusionY) + 1) >= 4) {
                                final int lowestOcclusionVertexHeight = this.vertexHeights[plane][lowestOcclusionX][lowestOcclusionY];
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

    private int generateHSLBitset(final int h, int s, final int l) {
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

    private int getVisibilityPlane(final int y, final int plane, final int x) {
        if ((this.renderRuleFlags[plane][x][y] & 8) != 0) {
            return 0;
        }
        if (plane > 0 && (this.renderRuleFlags[1][x][y] & 2) != 0) {
            return plane - 1;
        } else {
            return plane;
        }
    }

    public void initiateVertexHeights(final int startY, final int countY, final int countX, final int startX) {
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

    public void loadObjectBlock(final int blockX, final CollisionMap[] collisionMap, final int blockY,
                                final WorldController worldController, final byte[] blockData) {
        start:
        {
            final Buffer stream = new Buffer(blockData);
            int objectId = -1;
            do {
                final int objectIdOffset = stream.getSmartB();
                if (objectIdOffset == 0) {
                    break start;
                }
                objectId += objectIdOffset;
                int position = 0;
                do {
                    final int positionOffset = stream.getSmartB();
                    if (positionOffset == 0) {
                        break;
                    }
                    position += positionOffset - 1;
                    final int tileY = position & 0x3f;
                    final int tileX = position >> 6 & 0x3f;
                    final int tilePlane = position >> 12;
                    final int hash = stream.getUnsignedByte();
                    final int type = hash >> 2;
                    final int orientation = hash & 3;
                    final int x = tileX + blockX;
                    final int y = tileY + blockY;
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

    public void loadObjectSubblock(final CollisionMap[] collisionMap, final WorldController worldController, final int i, final int j,
                                   final int k, final int objectPlane, final byte[] blockData, final int i1, final int rotation, final int k1) {
        start:
        {
            final Buffer stream = new Buffer(blockData);
            int objectId = -1;
            do {
                final int objectIdOffset = stream.getSmartB();
                if (objectIdOffset == 0) {
                    break start;
                }
                objectId += objectIdOffset;
                int position = 0;
                do {
                    final int positionOffset = stream.getSmartB();
                    if (positionOffset == 0) {
                        break;
                    }
                    position += positionOffset - 1;
                    final int regionY = position & 0x3f;
                    final int regionX = position >> 6 & 0x3f;
                    final int plane = position >> 12;
                    final int hash = stream.getUnsignedByte();
                    final int type = hash >> 2;
                    final int orientation = hash & 3;
                    if (plane == i && regionX >= i1 && regionX < i1 + 8 && regionY >= k && regionY < k + 8) {
                        final GameObjectDefinition objectDefinition = GameObjectDefinition.getDefinition(objectId);
                        final int x = j + TiledUtils.getRotatedLandscapeChunkX(rotation, objectDefinition.sizeY, regionX & 7,
                            regionY & 7, objectDefinition.sizeX);
                        final int y = k1 + TiledUtils.getRotatedLandscapeChunkY(regionY & 7, objectDefinition.sizeY, rotation,
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

    public void loadTerrainBlock(final byte[] blockData, final int blockY, final int blockX, final int k, final int l,
                                 final CollisionMap[] collisionMap) {
        for (int plane = 0; plane < 4; plane++) {
            for (int tileX = 0; tileX < 64; tileX++) {
                for (int tileY = 0; tileY < 64; tileY++) {
                    if (blockX + tileX > 0 && blockX + tileX < 103 && blockY + tileY > 0 && blockY + tileY < 103) {
                        collisionMap[plane].clippingData[blockX + tileX][blockY + tileY] &= 0xfeffffff;
                    }
                }

            }

        }

        final Buffer stream = new Buffer(blockData);
        for (int plane = 0; plane < 4; plane++) {
            for (int tileX = 0; tileX < 64; tileX++) {
                for (int tileY = 0; tileY < 64; tileY++) {
                    this.loadTerrainTile(tileY + blockY, l, stream, tileX + blockX, plane, 0, k);
                }

            }

        }
    }

    public void loadTerrainSubblock(final int subBlockZ, final int rotation, final CollisionMap[] collisionMap, final int mapRegionX,
                                    final int subBlockX, final byte[] terrainData, final int subBlockY, final int blockPlane, final int mapRegionY) {
        for (int regionX = 0; regionX < 8; regionX++) {
            for (int regionY = 0; regionY < 8; regionY++) {
                if (mapRegionX + regionX > 0 && mapRegionX + regionX < 103 && mapRegionY + regionY > 0
                    && mapRegionY + regionY < 103) {
                    collisionMap[blockPlane].clippingData[mapRegionX + regionX][mapRegionY + regionY] &= 0xfeffffff;
                }
            }

        }
        final Buffer terrainDataStream = new Buffer(terrainData);
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

    private void loadTerrainTile(final int tileY, final int offsetY, final Buffer stream, final int tileX, final int tileZ, final int i1, final int offsetX) {
        if (tileX >= 0 && tileX < 104 && tileY >= 0 && tileY < 104) {
            this.renderRuleFlags[tileZ][tileX][tileY] = 0;
            do {
                final int value = stream.getUnsignedByte();
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
            final int value = stream.getUnsignedByte();
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

    private int mixLightnessSigned(final int hsl, int lightness) {
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

    private void renderObject(final int y, final WorldController worldController, final CollisionMap collisionMap, final int type, final int plane,
                              final int x, final int objectId, final int face) {
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
        final int drawHeight = vertexHeightSW + vertexHeightSE + vertexHeightNE + vertexHeightNW >> 2;
        final GameObjectDefinition objectDefinition = GameObjectDefinition.getDefinition(objectId);
        int hash = x + (y << 7) + (objectId << 14) + 0x40000000;
        if (!objectDefinition.hasActions) {
            hash += 0x80000000;
        }
        final byte config = (byte) ((face << 6) + type);
        if (type == 22) {
            if (lowMemory && !objectDefinition.hasActions && !objectDefinition.unknownAttribute) {
                return;
            }
            final Animable animable;
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
            final Animable animable;
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
                final int sizeX;
                final int sizeY;
                if (face == 1 || face == 3) {
                    sizeX = objectDefinition.sizeY;
                    sizeY = objectDefinition.sizeX;
                } else {
                    sizeX = objectDefinition.sizeX;
                    sizeY = objectDefinition.sizeY;
                }
                if (worldController.addEntityB(x, y, plane, drawHeight, rotation, sizeY, sizeX, hash, ((animable)),
                    config) && objectDefinition.castsShadow) {
                    final Model model;
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
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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
            final int orientation = face + 1 & 3;
            final Animable animable1;
            final Animable animable2;
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
            final Animable animable;
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
            final Animable animable;
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
                final int temp = vertexHeightNW;
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
                final int temp = vertexHeightNW;
                vertexHeightNW = vertexHeightSW;
                vertexHeightSW = vertexHeightSE;
                vertexHeightSE = vertexHeightNE;
                vertexHeightNE = temp;
            }
        }
        if (type == 4) {
            final Animable animable;
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
            final int hash_ = worldController.getWallObjectHash(x, y, plane);
            if (hash_ > 0) {
                offsetAmplifier = GameObjectDefinition.getDefinition(hash_ >> 14 & 0x7fff).offsetAmplifier;
            }
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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
            final Animable animable;
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
