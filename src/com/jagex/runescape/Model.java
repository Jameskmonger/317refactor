package com.jagex.runescape;

public final class Model extends Animable {

	public static Model getModel(int model) {
		if (modelHeaders == null) {
            return null;
        }
		ModelHeader modelHeader = modelHeaders[model];
		if (modelHeader == null) {
			requester.request(model);
			return null;
		} else {
			return new Model(model);
		}
	}

	public static void init(int modelCount, OnDemandFetcher requester) {
		modelHeaders = new ModelHeader[modelCount];
		Model.requester = requester;
	}

	public static boolean isCached(int model) {
		if (modelHeaders == null) {
            return false;
        }
		ModelHeader modelHeader = modelHeaders[model];
		if (modelHeader == null) {
			requester.request(model);
			return false;
		} else {
			return true;
		}
	}

	public static void loadModelHeader(byte modelData[], int modelId) {
		if (modelData == null) {
			ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader();
			modelHeader.vertexCount = 0;
			modelHeader.triangleCount = 0;
			modelHeader.texturedTriangleCount = 0;
			return;
		}
		Buffer stream = new Buffer(modelData);
		stream.position = modelData.length - 18;
		ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader();
		modelHeader.modelData = modelData;
		modelHeader.vertexCount = stream.getUnsignedLEShort();
		modelHeader.triangleCount = stream.getUnsignedLEShort();
		modelHeader.texturedTriangleCount = stream.getUnsignedByte();
		int useTextures = stream.getUnsignedByte();
		int useTrianglePriority = stream.getUnsignedByte();
		int useAlpha = stream.getUnsignedByte();
		int useTriangleSkins = stream.getUnsignedByte();
		int useVertexSkins = stream.getUnsignedByte();
		int dataLengthX = stream.getUnsignedLEShort();
		int dataLengthY = stream.getUnsignedLEShort();
		int dataLengthZ = stream.getUnsignedLEShort();
		int dataLengthTriangle = stream.getUnsignedLEShort();
		int offset = 0;
		modelHeader.vertexDirectionOffset = offset;
		offset += modelHeader.vertexCount;
		modelHeader.triangleTypeOffset = offset;
		offset += modelHeader.triangleCount;
		modelHeader.trianglePriorityOffset = offset;
		if (useTrianglePriority == 255) {
            offset += modelHeader.triangleCount;
        } else {
            modelHeader.trianglePriorityOffset = -useTrianglePriority - 1;
        }
		modelHeader.triangleSkinOffset = offset;
		if (useTriangleSkins == 1) {
            offset += modelHeader.triangleCount;
        } else {
            modelHeader.triangleSkinOffset = -1;
        }
		modelHeader.texturePointerOffset = offset;
		if (useTextures == 1) {
            offset += modelHeader.triangleCount;
        } else {
            modelHeader.texturePointerOffset = -1;
        }
		modelHeader.vertexSkinOffset = offset;
		if (useVertexSkins == 1) {
            offset += modelHeader.vertexCount;
        } else {
            modelHeader.vertexSkinOffset = -1;
        }
		modelHeader.triangleAlphaOffset = offset;
		if (useAlpha == 1) {
            offset += modelHeader.triangleCount;
        } else {
            modelHeader.triangleAlphaOffset = -1;
        }
		modelHeader.triangleDataOffset = offset;
		offset += dataLengthTriangle;
		modelHeader.colourDataOffset = offset;
		offset += modelHeader.triangleCount * 2;
		modelHeader.texturedTriangleOffset = offset;
		offset += modelHeader.texturedTriangleCount * 6;
		modelHeader.dataOffsetX = offset;
		offset += dataLengthX;
		modelHeader.dataOffsetY = offset;
		offset += dataLengthY;
		modelHeader.dataOffsetZ = offset;
		offset += dataLengthZ;
	}

	private static int mixLightness(int colour, int lightness, int drawType) {
		if ((drawType & 2) == 2) {
			if (lightness < 0) {
                lightness = 0;
            } else if (lightness > 127) {
                lightness = 127;
            }
			lightness = 127 - lightness;
			return lightness;
		}
		lightness = lightness * (colour & 0x7f) >> 7;
		if (lightness < 2) {
            lightness = 2;
        } else if (lightness > 126) {
            lightness = 126;
        }
		return (colour & 0xff80) + lightness;
	}

	public static void nullLoader() {
		modelHeaders = null;
		restrictEdges = null;
		aBooleanArray1664 = null;
		vertexScreenX = null;
		vertexScreenY = null;
		vertexScreenZ = null;
		vertexMovedX = null;
		vertexMovedY = null;
		vertexMovedZ = null;
		anIntArray1671 = null;
		anIntArrayArray1672 = null;
		anIntArray1673 = null;
		anIntArrayArray1674 = null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		SINE = null;
		COSINE = null;
		HSLtoRGB = null;
		modelIntArray4 = null;
	}

	public static void resetModel(int model) {
		modelHeaders[model] = null;
	}

	public static final Model aModel_1621 = new Model();

	private static int[] anIntArray1622 = new int[2000];

	private static int[] anIntArray1623 = new int[2000];

	private static int[] anIntArray1624 = new int[2000];

	private static int[] anIntArray1625 = new int[2000];

	public int vertexCount;

	public int verticesX[];

	public int verticesY[];

	public int verticesZ[];

	public int triangleCount;

	public int triangleX[];

	public int triangleY[];

	public int triangleZ[];

	private int[] triangleHSLA;

	private int[] triangleHSLB;

	private int[] triangleHSLC;

	public int triangleDrawType[];

	private int[] trianglePriorities;

	private int[] triangleAlpha;

	public int triangleColours[];

	private int trianglePriority;

	private int texturedTriangleCount;

	private int[] texturedTrianglePointsX;

	private int[] texturedTrianglePointsY;

	private int[] texturedTrianglePointsZ;

	public int maxY;

	public int maxX;

	public int maxZ;

	public int minZ;

	public int diagonal2DAboveOrigin;
	public int minX;
	private int diagonal3D;
	private int diagonal3DAboveOrigin;
	public int anInt1654;
	private int[] vertexSkins;
	private int[] triangleSkins;
	public int vertexSkin[][];
	public int triangleSkin[][];
	public boolean singleTile;
	public VertexNormal vertexNormalOffset[];
	private static ModelHeader[] modelHeaders;
	private static OnDemandFetcher requester;
	private static boolean[] restrictEdges = new boolean[4096];
	private static boolean[] aBooleanArray1664 = new boolean[4096];
	private static int[] vertexScreenX = new int[4096];
	private static int[] vertexScreenY = new int[4096];
	private static int[] vertexScreenZ = new int[4096];
	private static int[] vertexMovedX = new int[4096];
	private static int[] vertexMovedY = new int[4096];
	private static int[] vertexMovedZ = new int[4096];
	private static int[] anIntArray1671 = new int[1500];
	private static int[][] anIntArrayArray1672 = new int[1500][512];
	private static int[] anIntArray1673 = new int[12];
	private static int[][] anIntArrayArray1674 = new int[12][2000];
	private static int[] anIntArray1675 = new int[2000];
	private static int[] anIntArray1676 = new int[2000];
	private static int[] anIntArray1677 = new int[12];
	private static final int[] xPosition = new int[10];
	private static final int[] yPosition = new int[10];
	private static final int[] zPosition = new int[10];
	private static int vertexModifierX;
	private static int vertexModifierY;
	private static int vertexModifierZ;
	public static boolean aBoolean1684;
	public static int cursorX;
	public static int cursorY;
	public static int resourceCount;
	public static final int[] resourceId = new int[1000];
	public static int SINE[];
	public static int COSINE[];
	private static int[] HSLtoRGB;
	private static int[] modelIntArray4;
	static {
		SINE = Rasterizer.SINE;
		COSINE = Rasterizer.COSINE;
		HSLtoRGB = Rasterizer.HSL_TO_RGB;
		modelIntArray4 = Rasterizer.anIntArray1469;
	}

	private Model() {
        this.singleTile = false;
	}

	public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
        this.singleTile = false;
        this.vertexCount = model.vertexCount;
        this.triangleCount = model.triangleCount;
        this.texturedTriangleCount = model.texturedTriangleCount;
		if (flag2) {
            this.verticesX = model.verticesX;
            this.verticesY = model.verticesY;
            this.verticesZ = model.verticesZ;
		} else {
            this.verticesX = new int[this.vertexCount];
            this.verticesY = new int[this.vertexCount];
            this.verticesZ = new int[this.vertexCount];
			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
                this.verticesX[vertex] = model.verticesX[vertex];
                this.verticesY[vertex] = model.verticesY[vertex];
                this.verticesZ[vertex] = model.verticesZ[vertex];
			}

		}
		if (flag) {
            this.triangleColours = model.triangleColours;
		} else {
            this.triangleColours = new int[this.triangleCount];
			System.arraycopy(model.triangleColours, 0, this.triangleColours, 0, this.triangleCount);

		}
		if (flag1) {
            this.triangleAlpha = model.triangleAlpha;
		} else {
            this.triangleAlpha = new int[this.triangleCount];
			if (model.triangleAlpha == null) {
				for (int triangle = 0; triangle < this.triangleCount; triangle++) {
                    this.triangleAlpha[triangle] = 0;
                }

			} else {
				System.arraycopy(model.triangleAlpha, 0, this.triangleAlpha, 0, this.triangleCount);

			}
		}
        this.vertexSkins = model.vertexSkins;
        this.triangleSkins = model.triangleSkins;
        this.triangleDrawType = model.triangleDrawType;
        this.triangleX = model.triangleX;
        this.triangleY = model.triangleY;
        this.triangleZ = model.triangleZ;
        this.trianglePriorities = model.trianglePriorities;
        this.trianglePriority = model.trianglePriority;
        this.texturedTrianglePointsX = model.texturedTrianglePointsX;
        this.texturedTrianglePointsY = model.texturedTrianglePointsY;
        this.texturedTrianglePointsZ = model.texturedTrianglePointsZ;
	}

	public Model(boolean flag, boolean flag1, Model model) {
        this.singleTile = false;
        this.vertexCount = model.vertexCount;
        this.triangleCount = model.triangleCount;
        this.texturedTriangleCount = model.texturedTriangleCount;
		if (flag) {
            this.verticesY = new int[this.vertexCount];
			System.arraycopy(model.verticesY, 0, this.verticesY, 0, this.vertexCount);

		} else {
            this.verticesY = model.verticesY;
		}
		if (flag1) {
            this.triangleHSLA = new int[this.triangleCount];
            this.triangleHSLB = new int[this.triangleCount];
            this.triangleHSLC = new int[this.triangleCount];
			for (int triangle = 0; triangle < this.triangleCount; triangle++) {
                this.triangleHSLA[triangle] = model.triangleHSLA[triangle];
                this.triangleHSLB[triangle] = model.triangleHSLB[triangle];
                this.triangleHSLC[triangle] = model.triangleHSLC[triangle];
			}

            this.triangleDrawType = new int[this.triangleCount];
			if (model.triangleDrawType == null) {
				for (int triangle = 0; triangle < this.triangleCount; triangle++) {
                    this.triangleDrawType[triangle] = 0;
                }

			} else {
				System.arraycopy(model.triangleDrawType, 0, this.triangleDrawType, 0, this.triangleCount);

			}
			super.vertexNormals = new VertexNormal[this.vertexCount];
			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
				VertexNormal vertexNormalNew = super.vertexNormals[vertex] = new VertexNormal();
				VertexNormal vertexNormalOld = model.vertexNormals[vertex];
				vertexNormalNew.x = vertexNormalOld.x;
				vertexNormalNew.y = vertexNormalOld.y;
				vertexNormalNew.z = vertexNormalOld.z;
				vertexNormalNew.magnitude = vertexNormalOld.magnitude;
			}

            this.vertexNormalOffset = model.vertexNormalOffset;
		} else {
            this.triangleHSLA = model.triangleHSLA;
            this.triangleHSLB = model.triangleHSLB;
            this.triangleHSLC = model.triangleHSLC;
            this.triangleDrawType = model.triangleDrawType;
		}
        this.verticesX = model.verticesX;
        this.verticesZ = model.verticesZ;
        this.triangleColours = model.triangleColours;
        this.triangleAlpha = model.triangleAlpha;
        this.trianglePriorities = model.trianglePriorities;
        this.trianglePriority = model.trianglePriority;
        this.triangleX = model.triangleX;
        this.triangleY = model.triangleY;
        this.triangleZ = model.triangleZ;
        this.texturedTrianglePointsX = model.texturedTrianglePointsX;
        this.texturedTrianglePointsY = model.texturedTrianglePointsY;
        this.texturedTrianglePointsZ = model.texturedTrianglePointsZ;
		super.modelHeight = model.modelHeight;
        this.maxY = model.maxY;
        this.diagonal2DAboveOrigin = model.diagonal2DAboveOrigin;
        this.diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
        this.diagonal3D = model.diagonal3D;
        this.minX = model.minX;
        this.maxZ = model.maxZ;
        this.minZ = model.minZ;
        this.maxX = model.maxX;
	}

	private Model(int model) {
        this.singleTile = false;
		ModelHeader modelHeader = modelHeaders[model];
        this.vertexCount = modelHeader.vertexCount;
        this.triangleCount = modelHeader.triangleCount;
        this.texturedTriangleCount = modelHeader.texturedTriangleCount;
        this.verticesX = new int[this.vertexCount];
        this.verticesY = new int[this.vertexCount];
        this.verticesZ = new int[this.vertexCount];
        this.triangleX = new int[this.triangleCount];
        this.triangleY = new int[this.triangleCount];
        this.triangleZ = new int[this.triangleCount];
        this.texturedTrianglePointsX = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsY = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsZ = new int[this.texturedTriangleCount];
		if (modelHeader.vertexSkinOffset >= 0) {
            this.vertexSkins = new int[this.vertexCount];
        }
		if (modelHeader.texturePointerOffset >= 0) {
            this.triangleDrawType = new int[this.triangleCount];
        }
		if (modelHeader.trianglePriorityOffset >= 0) {
            this.trianglePriorities = new int[this.triangleCount];
        } else {
            this.trianglePriority = -modelHeader.trianglePriorityOffset - 1;
        }
		if (modelHeader.triangleAlphaOffset >= 0) {
            this.triangleAlpha = new int[this.triangleCount];
        }
		if (modelHeader.triangleSkinOffset >= 0) {
            this.triangleSkins = new int[this.triangleCount];
        }
        this.triangleColours = new int[this.triangleCount];
		Buffer vertexDirectionOffsetStream = new Buffer(modelHeader.modelData);
		vertexDirectionOffsetStream.position = modelHeader.vertexDirectionOffset;
		Buffer xDataOffsetStream = new Buffer(modelHeader.modelData);
		xDataOffsetStream.position = modelHeader.dataOffsetX;
		Buffer yDataOffsetStream = new Buffer(modelHeader.modelData);
		yDataOffsetStream.position = modelHeader.dataOffsetY;
		Buffer zDataOffsetStream = new Buffer(modelHeader.modelData);
		zDataOffsetStream.position = modelHeader.dataOffsetZ;
		Buffer vertexSkinOffsetStream = new Buffer(modelHeader.modelData);
		vertexSkinOffsetStream.position = modelHeader.vertexSkinOffset;
		int baseOffsetX = 0;
		int baseOffsetY = 0;
		int baseOffsetZ = 0;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int flag = vertexDirectionOffsetStream.getUnsignedByte();
			int currentOffsetX = 0;
			if ((flag & 1) != 0) {
                currentOffsetX = xDataOffsetStream.getSmartA();
            }
			int currentOffsetY = 0;
			if ((flag & 2) != 0) {
                currentOffsetY = yDataOffsetStream.getSmartA();
            }
			int currentOffsetZ = 0;
			if ((flag & 4) != 0) {
                currentOffsetZ = zDataOffsetStream.getSmartA();
            }
            this.verticesX[vertex] = baseOffsetX + currentOffsetX;
            this.verticesY[vertex] = baseOffsetY + currentOffsetY;
            this.verticesZ[vertex] = baseOffsetZ + currentOffsetZ;
			baseOffsetX = this.verticesX[vertex];
			baseOffsetY = this.verticesY[vertex];
			baseOffsetZ = this.verticesZ[vertex];
			if (this.vertexSkins != null) {
                this.vertexSkins[vertex] = vertexSkinOffsetStream.getUnsignedByte();
            }
		}

		vertexDirectionOffsetStream.position = modelHeader.colourDataOffset;
		xDataOffsetStream.position = modelHeader.texturePointerOffset;
		yDataOffsetStream.position = modelHeader.trianglePriorityOffset;
		zDataOffsetStream.position = modelHeader.triangleAlphaOffset;
		vertexSkinOffsetStream.position = modelHeader.triangleSkinOffset;
		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
            this.triangleColours[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
			if (this.triangleDrawType != null) {
                this.triangleDrawType[triangle] = xDataOffsetStream.getUnsignedByte();
            }
			if (this.trianglePriorities != null) {
                this.trianglePriorities[triangle] = yDataOffsetStream.getUnsignedByte();
            }
			if (this.triangleAlpha != null) {
                this.triangleAlpha[triangle] = zDataOffsetStream.getUnsignedByte();
            }
			if (this.triangleSkins != null) {
                this.triangleSkins[triangle] = vertexSkinOffsetStream.getUnsignedByte();
            }
		}

		vertexDirectionOffsetStream.position = modelHeader.triangleDataOffset;
		xDataOffsetStream.position = modelHeader.triangleTypeOffset;
		int trianglePointOffsetX = 0;
		int trianglePointOffsetY = 0;
		int trianglePointOffsetZ = 0;
		int offset = 0;
		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
			int type = xDataOffsetStream.getUnsignedByte();
			if (type == 1) {
				trianglePointOffsetX = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetX;
				trianglePointOffsetY = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetY;
				trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetZ;
                this.triangleX[triangle] = trianglePointOffsetX;
                this.triangleY[triangle] = trianglePointOffsetY;
                this.triangleZ[triangle] = trianglePointOffsetZ;
			}
			if (type == 2) {
				trianglePointOffsetY = trianglePointOffsetZ;
				trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetZ;
                this.triangleX[triangle] = trianglePointOffsetX;
                this.triangleY[triangle] = trianglePointOffsetY;
                this.triangleZ[triangle] = trianglePointOffsetZ;
			}
			if (type == 3) {
				trianglePointOffsetX = trianglePointOffsetZ;
				trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetZ;
                this.triangleX[triangle] = trianglePointOffsetX;
                this.triangleY[triangle] = trianglePointOffsetY;
                this.triangleZ[triangle] = trianglePointOffsetZ;
			}
			if (type == 4) {
				int oldTrianglePointOffsetX = trianglePointOffsetX;
				trianglePointOffsetX = trianglePointOffsetY;
				trianglePointOffsetY = oldTrianglePointOffsetX;
				trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
				offset = trianglePointOffsetZ;
                this.triangleX[triangle] = trianglePointOffsetX;
                this.triangleY[triangle] = trianglePointOffsetY;
                this.triangleZ[triangle] = trianglePointOffsetZ;
			}
		}

		vertexDirectionOffsetStream.position = modelHeader.texturedTriangleOffset;
		for (int triangle = 0; triangle < this.texturedTriangleCount; triangle++) {
            this.texturedTrianglePointsX[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
            this.texturedTrianglePointsY[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
            this.texturedTrianglePointsZ[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
		}

	}

	public Model(int modelCount, Model models[]) {
        this.singleTile = false;
		boolean setDrawType = false;
		boolean setPriority = false;
		boolean setAlpha = false;
		boolean setSkins = false;
        this.vertexCount = 0;
        this.triangleCount = 0;
        this.texturedTriangleCount = 0;
        this.trianglePriority = -1;
		for (int m = 0; m < modelCount; m++) {
			Model model = models[m];
			if (model != null) {
                this.vertexCount += model.vertexCount;
                this.triangleCount += model.triangleCount;
                this.texturedTriangleCount += model.texturedTriangleCount;
				setDrawType |= model.triangleDrawType != null;
				if (model.trianglePriorities != null) {
					setPriority = true;
				} else {
					if (this.trianglePriority == -1) {
                        this.trianglePriority = model.trianglePriority;
                    }
					if (this.trianglePriority != model.trianglePriority) {
                        setPriority = true;
                    }
				}
				setAlpha |= model.triangleAlpha != null;
				setSkins |= model.triangleSkins != null;
			}
		}

        this.verticesX = new int[this.vertexCount];
        this.verticesY = new int[this.vertexCount];
        this.verticesZ = new int[this.vertexCount];
        this.vertexSkins = new int[this.vertexCount];
        this.triangleX = new int[this.triangleCount];
        this.triangleY = new int[this.triangleCount];
        this.triangleZ = new int[this.triangleCount];
        this.texturedTrianglePointsX = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsY = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsZ = new int[this.texturedTriangleCount];
		if (setDrawType) {
            this.triangleDrawType = new int[this.triangleCount];
        }
		if (setPriority) {
            this.trianglePriorities = new int[this.triangleCount];
        }
		if (setAlpha) {
            this.triangleAlpha = new int[this.triangleCount];
        }
		if (setSkins) {
            this.triangleSkins = new int[this.triangleCount];
        }
        this.triangleColours = new int[this.triangleCount];
        this.vertexCount = 0;
        this.triangleCount = 0;
        this.texturedTriangleCount = 0;
		int count = 0;
		for (int m = 0; m < modelCount; m++) {
			Model model = models[m];
			if (model != null) {
				for (int triangle = 0; triangle < model.triangleCount; triangle++) {
					if (setDrawType) {
                        if (model.triangleDrawType == null) {
                            this.triangleDrawType[this.triangleCount] = 0;
                        } else {
                            int drawType = model.triangleDrawType[triangle];
                            if ((drawType & 2) == 2) {
                                drawType += count << 2;
                            }
                            this.triangleDrawType[this.triangleCount] = drawType;
                        }
                    }
					if (setPriority) {
                        if (model.trianglePriorities == null) {
                            this.trianglePriorities[this.triangleCount] = model.trianglePriority;
                        } else {
                            this.trianglePriorities[this.triangleCount] = model.trianglePriorities[triangle];
                        }
                    }
					if (setAlpha) {
                        if (model.triangleAlpha == null) {
                            this.triangleAlpha[this.triangleCount] = 0;
                        } else {
                            this.triangleAlpha[this.triangleCount] = model.triangleAlpha[triangle];
                        }
                    }
					if (setSkins && model.triangleSkins != null) {
                        this.triangleSkins[this.triangleCount] = model.triangleSkins[triangle];
                    }
                    this.triangleColours[this.triangleCount] = model.triangleColours[triangle];
                    this.triangleX[this.triangleCount] = this.getFirstIdenticalVertexId(model, model.triangleX[triangle]);
                    this.triangleY[this.triangleCount] = this.getFirstIdenticalVertexId(model, model.triangleY[triangle]);
                    this.triangleZ[this.triangleCount] = this.getFirstIdenticalVertexId(model, model.triangleZ[triangle]);
                    this.triangleCount++;
				}

				for (int triangle = 0; triangle < model.texturedTriangleCount; triangle++) {
                    this.texturedTrianglePointsX[this.texturedTriangleCount] = this.getFirstIdenticalVertexId(model,
							model.texturedTrianglePointsX[triangle]);
                    this.texturedTrianglePointsY[this.texturedTriangleCount] = this.getFirstIdenticalVertexId(model,
							model.texturedTrianglePointsY[triangle]);
                    this.texturedTrianglePointsZ[this.texturedTriangleCount] = this.getFirstIdenticalVertexId(model,
							model.texturedTrianglePointsZ[triangle]);
                    this.texturedTriangleCount++;
				}

				count += model.texturedTriangleCount;
			}
		}

	}

	public Model(Model models[]) {
		int modelCount = 2;// was parameter
        this.singleTile = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
        this.vertexCount = 0;
        this.triangleCount = 0;
        this.texturedTriangleCount = 0;
        this.trianglePriority = -1;
		for (int m = 0; m < modelCount; m++) {
			Model model = models[m];
			if (model != null) {
                this.vertexCount += model.vertexCount;
                this.triangleCount += model.triangleCount;
                this.texturedTriangleCount += model.texturedTriangleCount;
				flag1 |= model.triangleDrawType != null;
				if (model.trianglePriorities != null) {
					flag2 = true;
				} else {
					if (this.trianglePriority == -1) {
                        this.trianglePriority = model.trianglePriority;
                    }
					if (this.trianglePriority != model.trianglePriority) {
                        flag2 = true;
                    }
				}
				flag3 |= model.triangleAlpha != null;
				flag4 |= model.triangleColours != null;
			}
		}

        this.verticesX = new int[this.vertexCount];
        this.verticesY = new int[this.vertexCount];
        this.verticesZ = new int[this.vertexCount];
        this.triangleX = new int[this.triangleCount];
        this.triangleY = new int[this.triangleCount];
        this.triangleZ = new int[this.triangleCount];
        this.triangleHSLA = new int[this.triangleCount];
        this.triangleHSLB = new int[this.triangleCount];
        this.triangleHSLC = new int[this.triangleCount];
        this.texturedTrianglePointsX = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsY = new int[this.texturedTriangleCount];
        this.texturedTrianglePointsZ = new int[this.texturedTriangleCount];
		if (flag1) {
            this.triangleDrawType = new int[this.triangleCount];
        }
		if (flag2) {
            this.trianglePriorities = new int[this.triangleCount];
        }
		if (flag3) {
            this.triangleAlpha = new int[this.triangleCount];
        }
		if (flag4) {
            this.triangleColours = new int[this.triangleCount];
        }
        this.vertexCount = 0;
        this.triangleCount = 0;
        this.texturedTriangleCount = 0;
		int count = 0;
		for (int m = 0; m < modelCount; m++) {
			Model model = models[m];
			if (model != null) {
				int v = this.vertexCount;
				for (int vertex = 0; vertex < model.vertexCount; vertex++) {
                    this.verticesX[this.vertexCount] = model.verticesX[vertex];
                    this.verticesY[this.vertexCount] = model.verticesY[vertex];
                    this.verticesZ[this.vertexCount] = model.verticesZ[vertex];
                    this.vertexCount++;
				}

				for (int triangle = 0; triangle < model.triangleCount; triangle++) {
                    this.triangleX[this.triangleCount] = model.triangleX[triangle] + v;
                    this.triangleY[this.triangleCount] = model.triangleY[triangle] + v;
                    this.triangleZ[this.triangleCount] = model.triangleZ[triangle] + v;
                    this.triangleHSLA[this.triangleCount] = model.triangleHSLA[triangle];
                    this.triangleHSLB[this.triangleCount] = model.triangleHSLB[triangle];
                    this.triangleHSLC[this.triangleCount] = model.triangleHSLC[triangle];
					if (flag1) {
                        if (model.triangleDrawType == null) {
                            this.triangleDrawType[this.triangleCount] = 0;
                        } else {
                            int drawType = model.triangleDrawType[triangle];
                            if ((drawType & 2) == 2) {
                                drawType += count << 2;
                            }
                            this.triangleDrawType[this.triangleCount] = drawType;
                        }
                    }
					if (flag2) {
                        if (model.trianglePriorities == null) {
                            this.trianglePriorities[this.triangleCount] = model.trianglePriority;
                        } else {
                            this.trianglePriorities[this.triangleCount] = model.trianglePriorities[triangle];
                        }
                    }
					if (flag3) {
                        if (model.triangleAlpha == null) {
                            this.triangleAlpha[this.triangleCount] = 0;
                        } else {
                            this.triangleAlpha[this.triangleCount] = model.triangleAlpha[triangle];
                        }
                    }
					if (flag4 && model.triangleColours != null) {
                        this.triangleColours[this.triangleCount] = model.triangleColours[triangle];
                    }
                    this.triangleCount++;
				}

				for (int triangle = 0; triangle < model.texturedTriangleCount; triangle++) {
                    this.texturedTrianglePointsX[this.texturedTriangleCount] = model.texturedTrianglePointsX[triangle] + v;
                    this.texturedTrianglePointsY[this.texturedTriangleCount] = model.texturedTrianglePointsY[triangle] + v;
                    this.texturedTrianglePointsZ[this.texturedTriangleCount] = model.texturedTrianglePointsZ[triangle] + v;
                    this.texturedTriangleCount++;
				}

				count += model.texturedTriangleCount;
			}
		}

		this.calculateDiagonals();
	}

	public void applyLighting(int lightMod, int magnitudeMultiplier, int lightX, int lightY, int lightZ,
			boolean flatShading) {
		int lightMagnitude = (int) Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
		int magnitude = magnitudeMultiplier * lightMagnitude >> 8;
		if (this.triangleHSLA == null) {
            this.triangleHSLA = new int[this.triangleCount];
            this.triangleHSLB = new int[this.triangleCount];
            this.triangleHSLC = new int[this.triangleCount];
		}
		if (super.vertexNormals == null) {
			super.vertexNormals = new VertexNormal[this.vertexCount];
			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
                super.vertexNormals[vertex] = new VertexNormal();
            }

		}
		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
			int _triangleX = this.triangleX[triangle];
			int _triangleY = this.triangleY[triangle];
			int _triangleZ = this.triangleZ[triangle];
			int distanceXXY = this.verticesX[_triangleY] - this.verticesX[_triangleX];
			int distanceYXY = this.verticesY[_triangleY] - this.verticesY[_triangleX];
			int distanceZXY = this.verticesZ[_triangleY] - this.verticesZ[_triangleX];
			int distanceXZX = this.verticesX[_triangleZ] - this.verticesX[_triangleX];
			int distanceYZX = this.verticesY[_triangleZ] - this.verticesY[_triangleX];
			int distanceZZX = this.verticesZ[_triangleZ] - this.verticesZ[_triangleX];
			int normalX = distanceYXY * distanceZZX - distanceYZX * distanceZXY;
			int normalY = distanceZXY * distanceXZX - distanceZZX * distanceXXY;
			int normalZ;
			for (normalZ = distanceXXY * distanceYZX - distanceXZX * distanceYXY; normalX > 8192 || normalY > 8192
					|| normalZ > 8192 || normalX < -8192 || normalY < -8192 || normalZ < -8192; normalZ >>= 1) {
				normalX >>= 1;
				normalY >>= 1;
			}

			int normalLength = (int) Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
			if (normalLength <= 0) {
                normalLength = 1;
            }
			normalX = (normalX * 256) / normalLength;
			normalY = (normalY * 256) / normalLength;
			normalZ = (normalZ * 256) / normalLength;
			if (this.triangleDrawType == null || (this.triangleDrawType[triangle] & 1) == 0) {
				VertexNormal vertexNormal = super.vertexNormals[_triangleX];
				vertexNormal.x += normalX;
				vertexNormal.y += normalY;
				vertexNormal.z += normalZ;
				vertexNormal.magnitude++;
				vertexNormal = super.vertexNormals[_triangleY];
				vertexNormal.x += normalX;
				vertexNormal.y += normalY;
				vertexNormal.z += normalZ;
				vertexNormal.magnitude++;
				vertexNormal = super.vertexNormals[_triangleZ];
				vertexNormal.x += normalX;
				vertexNormal.y += normalY;
				vertexNormal.z += normalZ;
				vertexNormal.magnitude++;
			} else {
				int lightness = lightMod
						+ (lightX * normalX + lightY * normalY + lightZ * normalZ) / (magnitude + magnitude / 2);
                this.triangleHSLA[triangle] = mixLightness(this.triangleColours[triangle], lightness, this.triangleDrawType[triangle]);
			}
		}

		if (flatShading) {
			this.handleShading(lightMod, magnitude, lightX, lightY, lightZ);
		} else {
            this.vertexNormalOffset = new VertexNormal[this.vertexCount];
			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
				VertexNormal vertexNormal = super.vertexNormals[vertex];
				VertexNormal shadowVertexNormal = this.vertexNormalOffset[vertex] = new VertexNormal();
				shadowVertexNormal.x = vertexNormal.x;
				shadowVertexNormal.y = vertexNormal.y;
				shadowVertexNormal.z = vertexNormal.z;
				shadowVertexNormal.magnitude = vertexNormal.magnitude;
			}

		}
		if (flatShading) {
			this.calculateDiagonals();
		} else {
			this.calculateDiagonalsAndBounds();
		}
	}

	public void applyTransformation(int frameId) {
		if (this.vertexSkin == null) {
            return;
        }
		if (frameId == -1) {
            return;
        }
		Animation animationFrame = Animation.forFrameId(frameId);
		if (animationFrame == null) {
            return;
        }
		Skins skins = animationFrame.animationSkins;
		vertexModifierX = 0;
		vertexModifierY = 0;
		vertexModifierZ = 0;
		for (int stepId = 0; stepId < animationFrame.frameCount; stepId++) {
			int opcode = animationFrame.opcodeTable[stepId];
			this.transformFrame(skins.opcodes[opcode], skins.skinList[opcode], animationFrame.transformationX[stepId],
					animationFrame.transformationY[stepId], animationFrame.transformationZ[stepId]);
		}
	}

	public void calculateDiagonals() {
		super.modelHeight = 0;
        this.diagonal2DAboveOrigin = 0;
        this.maxY = 0;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int x = this.verticesX[vertex];
			int y = this.verticesY[vertex];
			int z = this.verticesZ[vertex];
			if (-y > super.modelHeight) {
                super.modelHeight = -y;
            }
			if (y > this.maxY) {
                this.maxY = y;
            }
			int bounds = x * x + z * z;
			if (bounds > this.diagonal2DAboveOrigin) {
                this.diagonal2DAboveOrigin = bounds;
            }
		}
        this.diagonal2DAboveOrigin = (int) (Math.sqrt(this.diagonal2DAboveOrigin) + 0.98999999999999999D);
        this.diagonal3DAboveOrigin = (int) (Math
				.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + super.modelHeight * super.modelHeight)
				+ 0.98999999999999999D);
        this.diagonal3D = this.diagonal3DAboveOrigin
				+ (int) (Math.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + this.maxY * this.maxY) + 0.98999999999999999D);
	}

	private void calculateDiagonalsAndBounds() {
		super.modelHeight = 0;
        this.diagonal2DAboveOrigin = 0;
        this.maxY = 0;
        this.minX = 0xf423f;
        this.maxX = 0xfff0bdc1;
        this.maxZ = 0xfffe7961;
        this.minZ = 0x1869f;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int x = this.verticesX[vertex];
			int y = this.verticesY[vertex];
			int z = this.verticesZ[vertex];
			if (x < this.minX) {
                this.minX = x;
            }
			if (x > this.maxX) {
                this.maxX = x;
            }
			if (z < this.minZ) {
                this.minZ = z;
            }
			if (z > this.maxZ) {
                this.maxZ = z;
            }
			if (-y > super.modelHeight) {
                super.modelHeight = -y;
            }
			if (y > this.maxY) {
                this.maxY = y;
            }
			int bounds = x * x + z * z;
			if (bounds > this.diagonal2DAboveOrigin) {
                this.diagonal2DAboveOrigin = bounds;
            }
		}

        this.diagonal2DAboveOrigin = (int) Math.sqrt(this.diagonal2DAboveOrigin);
        this.diagonal3DAboveOrigin = (int) Math
				.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + super.modelHeight * super.modelHeight);
        this.diagonal3D = this.diagonal3DAboveOrigin
				+ (int) Math.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + this.maxY * this.maxY);
	}

	public void createBones() {
		if (this.vertexSkins != null) {
			int ai[] = new int[256];
			int count = 0;
			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
				int skins = this.vertexSkins[vertex];
				ai[skins]++;
				if (skins > count) {
                    count = skins;
                }
			}

            this.vertexSkin = new int[count + 1][];
			for (int vertex = 0; vertex <= count; vertex++) {
                this.vertexSkin[vertex] = new int[ai[vertex]];
				ai[vertex] = 0;
			}

			for (int vertex = 0; vertex < this.vertexCount; vertex++) {
				int skin = this.vertexSkins[vertex];
                this.vertexSkin[skin][ai[skin]++] = vertex;
			}

            this.vertexSkins = null;
		}
		if (this.triangleSkins != null) {
			int ai1[] = new int[256];
			int count = 0;
			for (int triangle = 0; triangle < this.triangleCount; triangle++) {
				int skins = this.triangleSkins[triangle];
				ai1[skins]++;
				if (skins > count) {
                    count = skins;
                }
			}

            this.triangleSkin = new int[count + 1][];
			for (int triangle = 0; triangle <= count; triangle++) {
                this.triangleSkin[triangle] = new int[ai1[triangle]];
				ai1[triangle] = 0;
			}

			for (int triangle = 0; triangle < this.triangleCount; triangle++) {
				int skins = this.triangleSkins[triangle];
                this.triangleSkin[skins][ai1[skins]++] = triangle;
			}

            this.triangleSkins = null;
		}
	}

	private int getFirstIdenticalVertexId(Model model, int vertex) {
		int vertexId = -1;
		int x = model.verticesX[vertex];
		int y = model.verticesY[vertex];
		int z = model.verticesZ[vertex];
		for (int v = 0; v < this.vertexCount; v++) {
			if (x != this.verticesX[v] || y != this.verticesY[v] || z != this.verticesZ[v]) {
                continue;
            }
			vertexId = v;
			break;
		}

		if (vertexId == -1) {
            this.verticesX[this.vertexCount] = x;
            this.verticesY[this.vertexCount] = y;
            this.verticesZ[this.vertexCount] = z;
			if (model.vertexSkins != null) {
                this.vertexSkins[this.vertexCount] = model.vertexSkins[vertex];
            }
			vertexId = this.vertexCount++;
		}
		return vertexId;
	}

	public void handleShading(int intensity, int falloff, int lightX, int lightY, int lightZ) {
		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
			int x = this.triangleX[triangle];
			int y = this.triangleY[triangle];
			int z = this.triangleZ[triangle];
			if (this.triangleDrawType == null) {
				int colour = this.triangleColours[triangle];
				VertexNormal vertexNormal = super.vertexNormals[x];
				int lightness = intensity
						+ (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
								/ (falloff * vertexNormal.magnitude);
                this.triangleHSLA[triangle] = mixLightness(colour, lightness, 0);
				vertexNormal = super.vertexNormals[y];
				lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
						/ (falloff * vertexNormal.magnitude);
                this.triangleHSLB[triangle] = mixLightness(colour, lightness, 0);
				vertexNormal = super.vertexNormals[z];
				lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
						/ (falloff * vertexNormal.magnitude);
                this.triangleHSLC[triangle] = mixLightness(colour, lightness, 0);
			} else if ((this.triangleDrawType[triangle] & 1) == 0) {
				int colour = this.triangleColours[triangle];
				int drawType = this.triangleDrawType[triangle];
				VertexNormal vertexNormal = super.vertexNormals[x];
				int lightness = intensity
						+ (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
								/ (falloff * vertexNormal.magnitude);
                this.triangleHSLA[triangle] = mixLightness(colour, lightness, drawType);
				vertexNormal = super.vertexNormals[y];
				lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
						/ (falloff * vertexNormal.magnitude);
                this.triangleHSLB[triangle] = mixLightness(colour, lightness, drawType);
				vertexNormal = super.vertexNormals[z];
				lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z)
						/ (falloff * vertexNormal.magnitude);
                this.triangleHSLC[triangle] = mixLightness(colour, lightness, drawType);
			}
		}

		super.vertexNormals = null;
        this.vertexNormalOffset = null;
        this.vertexSkins = null;
        this.triangleSkins = null;
		if (this.triangleDrawType != null) {
			for (int triangle = 0; triangle < this.triangleCount; triangle++) {
                if ((this.triangleDrawType[triangle] & 2) == 2) {
                    return;
                }
            }

		}
        this.triangleColours = null;
	}

	private void method483(boolean flag, boolean flag1, int i) {
		for (int j = 0; j < this.diagonal3D; j++) {
            anIntArray1671[j] = 0;
        }

		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
            if (this.triangleDrawType == null || this.triangleDrawType[triangle] != -1) {
                int x = this.triangleX[triangle];
                int y = this.triangleY[triangle];
                int z = this.triangleZ[triangle];
                int screenXX = vertexScreenX[x];
                int screenXY = vertexScreenX[y];
                int screenXZ = vertexScreenX[z];
                if (flag && (screenXX == -5000 || screenXY == -5000 || screenXZ == -5000)) {
                    aBooleanArray1664[triangle] = true;
                    int j5 = (vertexScreenZ[x] + vertexScreenZ[y] + vertexScreenZ[z]) / 3 + this.diagonal3DAboveOrigin;
                    anIntArrayArray1672[j5][anIntArray1671[j5]++] = triangle;
                } else {
                    if (flag1 && this.method486(cursorX, cursorY, vertexScreenY[x], vertexScreenY[y], vertexScreenY[z],
                            screenXX, screenXY, screenXZ)) {
                        resourceId[resourceCount++] = i;
                        flag1 = false;
                    }
                    if ((screenXX - screenXY) * (vertexScreenY[z] - vertexScreenY[y])
                            - (vertexScreenY[x] - vertexScreenY[y]) * (screenXZ - screenXY) > 0) {
                        aBooleanArray1664[triangle] = false;
                        restrictEdges[triangle] = screenXX < 0 || screenXY < 0 || screenXZ < 0
                                || screenXX > DrawingArea.centerX || screenXY > DrawingArea.centerX
                                || screenXZ > DrawingArea.centerX;
                        int k5 = (vertexScreenZ[x] + vertexScreenZ[y] + vertexScreenZ[z]) / 3 + this.diagonal3DAboveOrigin;
                        anIntArrayArray1672[k5][anIntArray1671[k5]++] = triangle;
                    }
                }
            }
        }

		if (this.trianglePriorities == null) {
			for (int i1 = this.diagonal3D - 1; i1 >= 0; i1--) {
				int l1 = anIntArray1671[i1];
				if (l1 > 0) {
					int ai[] = anIntArrayArray1672[i1];
					for (int j3 = 0; j3 < l1; j3++) {
						this.rasterise(ai[j3]);
                    }

				}
			}

			return;
		}
		for (int j1 = 0; j1 < 12; j1++) {
			anIntArray1673[j1] = 0;
			anIntArray1677[j1] = 0;
		}

		for (int i2 = this.diagonal3D - 1; i2 >= 0; i2--) {
			int k2 = anIntArray1671[i2];
			if (k2 > 0) {
				int ai1[] = anIntArrayArray1672[i2];
				for (int i4 = 0; i4 < k2; i4++) {
					int l4 = ai1[i4];
					int l5 = this.trianglePriorities[l4];
					int j6 = anIntArray1673[l5]++;
					anIntArrayArray1674[l5][j6] = l4;
					if (l5 < 10) {
                        anIntArray1677[l5] += i2;
                    } else if (l5 == 10) {
                        anIntArray1675[j6] = i2;
                    } else {
                        anIntArray1676[j6] = i2;
                    }
				}

			}
		}

		int l2 = 0;
		if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0) {
            l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
        }
		int k3 = 0;
		if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0) {
            k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
        }
		int j4 = 0;
		if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0) {
            j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
        }
		int i6 = 0;
		int k6 = anIntArray1673[10];
		int ai2[] = anIntArrayArray1674[10];
		int ai3[] = anIntArray1675;
		if (i6 == k6) {
			i6 = 0;
			k6 = anIntArray1673[11];
			ai2 = anIntArrayArray1674[11];
			ai3 = anIntArray1676;
		}
		int i5;
		if (i6 < k6) {
            i5 = ai3[i6];
        } else {
            i5 = -1000;
        }
		for (int l6 = 0; l6 < 10; l6++) {
			while (l6 == 0 && i5 > l2) {
				this.rasterise(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
			}
			while (l6 == 3 && i5 > k3) {
				this.rasterise(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
			}
			while (l6 == 5 && i5 > j4) {
				this.rasterise(ai2[i6++]);
				if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
					i6 = 0;
					k6 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
			}
			int i7 = anIntArray1673[l6];
			int ai4[] = anIntArrayArray1674[l6];
			for (int j7 = 0; j7 < i7; j7++) {
				this.rasterise(ai4[j7]);
            }

		}

		while (i5 != -1000) {
			this.rasterise(ai2[i6++]);
			if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
				i6 = 0;
				ai2 = anIntArrayArray1674[11];
				k6 = anIntArray1673[11];
				ai3 = anIntArray1676;
			}
			if (i6 < k6) {
                i5 = ai3[i6];
            } else {
                i5 = -1000;
            }
		}
	}

	private void method485(int triangle) {
		int centreX = Rasterizer.centreX;
		int centreY = Rasterizer.centreY;
		int counter = 0;
		int x = this.triangleX[triangle];
		int y = this.triangleY[triangle];
		int z = this.triangleZ[triangle];
		int movedX = vertexMovedZ[x];
		int movedY = vertexMovedZ[y];
		int movedZ = vertexMovedZ[z];
		if (movedX >= 50) {
			xPosition[counter] = vertexScreenX[x];
			yPosition[counter] = vertexScreenY[x];
			zPosition[counter++] = this.triangleHSLA[triangle];
		} else {
			int movedX2 = vertexMovedX[x];
			int movedY2 = vertexMovedY[x];
			int colour = this.triangleHSLA[triangle];
			if (movedZ >= 50) {
				int k5 = (50 - movedX) * modelIntArray4[movedZ - movedX];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * k5 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * k5 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLC[triangle] - colour) * k5 >> 16);
			}
			if (movedY >= 50) {
				int l5 = (50 - movedX) * modelIntArray4[movedY - movedX];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * l5 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * l5 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLB[triangle] - colour) * l5 >> 16);
			}
		}
		if (movedY >= 50) {
			xPosition[counter] = vertexScreenX[y];
			yPosition[counter] = vertexScreenY[y];
			zPosition[counter++] = this.triangleHSLB[triangle];
		} else {
			int movedX2 = vertexMovedX[y];
			int movedY2 = vertexMovedY[y];
			int colour = this.triangleHSLB[triangle];
			if (movedX >= 50) {
				int i6 = (50 - movedY) * modelIntArray4[movedX - movedY];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * i6 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * i6 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLA[triangle] - colour) * i6 >> 16);
			}
			if (movedZ >= 50) {
				int j6 = (50 - movedY) * modelIntArray4[movedZ - movedY];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * j6 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * j6 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLC[triangle] - colour) * j6 >> 16);
			}
		}
		if (movedZ >= 50) {
			xPosition[counter] = vertexScreenX[z];
			yPosition[counter] = vertexScreenY[z];
			zPosition[counter++] = this.triangleHSLC[triangle];
		} else {
			int movedX2 = vertexMovedX[z];
			int movedY2 = vertexMovedY[z];
			int colour = this.triangleHSLC[triangle];
			if (movedY >= 50) {
				int k6 = (50 - movedZ) * modelIntArray4[movedY - movedZ];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * k6 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * k6 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLB[triangle] - colour) * k6 >> 16);
			}
			if (movedX >= 50) {
				int l6 = (50 - movedZ) * modelIntArray4[movedX - movedZ];
				xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * l6 >> 16) << 9) / 50;
				yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * l6 >> 16) << 9) / 50;
				zPosition[counter++] = colour + ((this.triangleHSLA[triangle] - colour) * l6 >> 16);
			}
		}
		int xA = xPosition[0];
		int xB = xPosition[1];
		int xC = xPosition[2];
		int yA = yPosition[0];
		int yB = yPosition[1];
		int yC = yPosition[2];
		if ((xA - xB) * (yC - yB) - (yA - yB) * (xC - xB) > 0) {
			Rasterizer.restrictEdges = false;
			if (counter == 3) {
				if (xA < 0 || xB < 0 || xC < 0 || xA > DrawingArea.centerX || xB > DrawingArea.centerX
						|| xC > DrawingArea.centerX) {
                    Rasterizer.restrictEdges = true;
                }
				int drawType;
				if (this.triangleDrawType == null) {
                    drawType = 0;
                } else {
                    drawType = this.triangleDrawType[triangle] & 3;
                }
				if (drawType == 0) {
                    Rasterizer.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
                } else if (drawType == 1) {
                    Rasterizer.drawFlatTriangle(yA, yB, yC, xA, xB, xC, HSLtoRGB[this.triangleHSLA[triangle]]);
                } else if (drawType == 2) {
					int tri = this.triangleDrawType[triangle] >> 2;
					int x2 = this.texturedTrianglePointsX[tri];
					int y2 = this.texturedTrianglePointsY[tri];
					int z2 = this.texturedTrianglePointsZ[tri];
					Rasterizer.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2],
							vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2],
							vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2],
                            this.triangleColours[triangle]);
				} else if (drawType == 3) {
					int tri = this.triangleDrawType[triangle] >> 2;
					int x2 = this.texturedTrianglePointsX[tri];
					int y2 = this.texturedTrianglePointsY[tri];
					int z2 = this.texturedTrianglePointsZ[tri];
					Rasterizer.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, this.triangleHSLA[triangle],
                            this.triangleHSLA[triangle], this.triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2],
							vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2],
							vertexMovedZ[y2], vertexMovedZ[z2], this.triangleColours[triangle]);
				}
			}
			if (counter == 4) {
				if (xA < 0 || xB < 0 || xC < 0 || xA > DrawingArea.centerX || xB > DrawingArea.centerX
						|| xC > DrawingArea.centerX || xPosition[3] < 0 || xPosition[3] > DrawingArea.centerX) {
                    Rasterizer.restrictEdges = true;
                }
				int drawType;
				if (this.triangleDrawType == null) {
                    drawType = 0;
                } else {
                    drawType = this.triangleDrawType[triangle] & 3;
                }
				if (drawType == 0) {
					Rasterizer.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
					Rasterizer.drawShadedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], zPosition[0],
							zPosition[2], zPosition[3]);
					return;
				}
				if (drawType == 1) {
					int colour = HSLtoRGB[this.triangleHSLA[triangle]];
					Rasterizer.drawFlatTriangle(yA, yB, yC, xA, xB, xC, colour);
					Rasterizer.drawFlatTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], colour);
					return;
				}
				if (drawType == 2) {
					int tri = this.triangleDrawType[triangle] >> 2;
					int x2 = this.texturedTrianglePointsX[tri];
					int y2 = this.texturedTrianglePointsY[tri];
					int z2 = this.texturedTrianglePointsZ[tri];
					Rasterizer.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2],
							vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2],
							vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2],
                            this.triangleColours[triangle]);
					Rasterizer.drawTexturedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], zPosition[0],
							zPosition[2], zPosition[3], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2],
							vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2],
							vertexMovedZ[z2], this.triangleColours[triangle]);
					return;
				}
				if (drawType == 3) {
					int tri = this.triangleDrawType[triangle] >> 2;
					int x2 = this.texturedTrianglePointsX[tri];
					int y2 = this.texturedTrianglePointsY[tri];
					int z2 = this.texturedTrianglePointsZ[tri];
					Rasterizer.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, this.triangleHSLA[triangle],
                            this.triangleHSLA[triangle], this.triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2],
							vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2],
							vertexMovedZ[y2], vertexMovedZ[z2], this.triangleColours[triangle]);
					Rasterizer.drawTexturedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], this.triangleHSLA[triangle],
                            this.triangleHSLA[triangle], this.triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2],
							vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2],
							vertexMovedZ[y2], vertexMovedZ[z2], this.triangleColours[triangle]);
				}
			}
		}
	}

	private boolean method486(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
		if (j < k && j < l && j < i1) {
            return false;
        }
		if (j > k && j > l && j > i1) {
            return false;
        }
		return !(i < j1 && i < k1 && i < l1) && (i <= j1 || i <= k1 || i <= l1);
	}

	public void mirror() {
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
            this.verticesZ[vertex] = -this.verticesZ[vertex];
        }

		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
			int newTriangleC = this.triangleX[triangle];
            this.triangleX[triangle] = this.triangleZ[triangle];
            this.triangleZ[triangle] = newTriangleC;
		}
	}

	public void mixAnimationFrames(int framesFrom2[], int frameId2, int frameId1) {
		if (frameId1 == -1) {
            return;
        }
		if (framesFrom2 == null || frameId2 == -1) {
			this.applyTransformation(frameId1);
			return;
		}
		Animation animationFrame1 = Animation.forFrameId(frameId1);
		if (animationFrame1 == null) {
            return;
        }
		Animation animationFrame2 = Animation.forFrameId(frameId2);
		if (animationFrame2 == null) {
			this.applyTransformation(frameId1);
			return;
		}
		Skins skins = animationFrame1.animationSkins;
		vertexModifierX = 0;
		vertexModifierY = 0;
		vertexModifierZ = 0;
		int counter = 0;
		int frameCount = framesFrom2[counter++];
		for (int frame = 0; frame < animationFrame1.frameCount; frame++) {
			int skin;
			for (skin = animationFrame1.opcodeTable[frame]; skin > frameCount; frameCount = framesFrom2[counter++]) {
                ;
            }
			if (skin != frameCount || skins.opcodes[skin] == 0) {
				this.transformFrame(skins.opcodes[skin], skins.skinList[skin], animationFrame1.transformationX[frame],
                        animationFrame1.transformationY[frame], animationFrame1.transformationZ[frame]);
            }
		}

		vertexModifierX = 0;
		vertexModifierY = 0;
		vertexModifierZ = 0;
		counter = 0;
		frameCount = framesFrom2[counter++];
		for (int frame = 0; frame < animationFrame2.frameCount; frame++) {
			int skin;
			for (skin = animationFrame2.opcodeTable[frame]; skin > frameCount; frameCount = framesFrom2[counter++]) {
                ;
            }
			if (skin == frameCount || skins.opcodes[skin] == 0) {
				this.transformFrame(skins.opcodes[skin], skins.skinList[skin], animationFrame2.transformationX[frame],
                        animationFrame2.transformationY[frame], animationFrame2.transformationZ[frame]);
            }
		}

	}

	public void normalise() {
		super.modelHeight = 0;
        this.maxY = 0;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int y = this.verticesY[vertex];
			if (-y > super.modelHeight) {
                super.modelHeight = -y;
            }
			if (y > this.maxY) {
                this.maxY = y;
            }
		}

        this.diagonal3DAboveOrigin = (int) (Math
				.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + super.modelHeight * super.modelHeight)
				+ 0.98999999999999999D);
        this.diagonal3D = this.diagonal3DAboveOrigin
				+ (int) (Math.sqrt(this.diagonal2DAboveOrigin * this.diagonal2DAboveOrigin + this.maxY * this.maxY) + 0.98999999999999999D);
	}

	private void rasterise(int i) {
		if (aBooleanArray1664[i]) {
			this.method485(i);
			return;
		}
		int x = this.triangleX[i];
		int y = this.triangleY[i];
		int z = this.triangleZ[i];
		Rasterizer.restrictEdges = restrictEdges[i];
		if (this.triangleAlpha == null) {
            Rasterizer.alpha = 0;
        } else {
            Rasterizer.alpha = this.triangleAlpha[i];
        }
		int drawType;
		if (this.triangleDrawType == null) {
            drawType = 0;
        } else {
            drawType = this.triangleDrawType[i] & 3;
        }
		if (drawType == 0) {
			Rasterizer.drawShadedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x],
					vertexScreenX[y], vertexScreenX[z], this.triangleHSLA[i], this.triangleHSLB[i], this.triangleHSLC[i]);
			return;
		}
		if (drawType == 1) {
			Rasterizer.drawFlatTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x],
					vertexScreenX[y], vertexScreenX[z], HSLtoRGB[this.triangleHSLA[i]]);
			return;
		}
		if (drawType == 2) {
			int triangle = this.triangleDrawType[i] >> 2;
			int x2 = this.texturedTrianglePointsX[triangle];
			int y2 = this.texturedTrianglePointsY[triangle];
			int z2 = this.texturedTrianglePointsZ[triangle];
			Rasterizer.drawTexturedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x],
					vertexScreenX[y], vertexScreenX[z], this.triangleHSLA[i], this.triangleHSLB[i], this.triangleHSLC[i],
					vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2],
					vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], this.triangleColours[i]);
			return;
		}
		if (drawType == 3) {
			int triangle = this.triangleDrawType[i] >> 2;
			int x2 = this.texturedTrianglePointsX[triangle];
			int y2 = this.texturedTrianglePointsY[triangle];
			int z2 = this.texturedTrianglePointsZ[triangle];
			Rasterizer.drawTexturedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x],
					vertexScreenX[y], vertexScreenX[z], this.triangleHSLA[i], this.triangleHSLA[i], this.triangleHSLA[i],
					vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2],
					vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], this.triangleColours[i]);
		}
	}

	public void recolour(int targetColour, int replacementColour) {
		for (int triangle = 0; triangle < this.triangleCount; triangle++) {
            if (this.triangleColours[triangle] == targetColour) {
                this.triangleColours[triangle] = replacementColour;
            }
        }

	}

	@Override
	public void renderAtPoint(int i, int yCameraSine, int yCameraCosine, int xCameraSine, int xCameraCosine, int x,
			int y, int z, int i2) {
		int j2 = z * xCameraCosine - x * xCameraSine >> 16;
		int k2 = y * yCameraSine + j2 * yCameraCosine >> 16;
		int l2 = this.diagonal2DAboveOrigin * yCameraCosine >> 16;
		int i3 = k2 + l2;
		if (i3 <= 50 || k2 >= 3500) {
            return;
        }
		int j3 = z * xCameraSine + x * xCameraCosine >> 16;
		int k3 = j3 - this.diagonal2DAboveOrigin << 9;
		if (k3 / i3 >= DrawingArea.viewportCentreX) {
            return;
        }
		int l3 = j3 + this.diagonal2DAboveOrigin << 9;
		if (l3 / i3 <= -DrawingArea.viewportCentreX) {
            return;
        }
		int i4 = y * yCameraCosine - j2 * yCameraSine >> 16;
		int j4 = this.diagonal2DAboveOrigin * yCameraSine >> 16;
		int k4 = i4 + j4 << 9;
		if (k4 / i3 <= -DrawingArea.viewportCentreY) {
            return;
        }
		int l4 = j4 + (super.modelHeight * yCameraCosine >> 16);
		int i5 = i4 - l4 << 9;
		if (i5 / i3 >= DrawingArea.viewportCentreY) {
            return;
        }
		int j5 = l2 + (super.modelHeight * yCameraSine >> 16);
		boolean flag = false;
		if (k2 - j5 <= 50) {
            flag = true;
        }
		boolean flag1 = false;
		if (i2 > 0 && aBoolean1684) {
			int k5 = k2 - l2;
			if (k5 <= 50) {
                k5 = 50;
            }
			if (j3 > 0) {
				k3 /= i3;
				l3 /= k5;
			} else {
				l3 /= i3;
				k3 /= k5;
			}
			if (i4 > 0) {
				i5 /= i3;
				k4 /= k5;
			} else {
				k4 /= i3;
				i5 /= k5;
			}
			int i6 = cursorX - Rasterizer.centreX;
			int k6 = cursorY - Rasterizer.centreY;
			if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4) {
                if (this.singleTile) {
                    resourceId[resourceCount++] = i2;
                } else {
                    flag1 = true;
                }
            }
		}
		int centreX = Rasterizer.centreX;
		int centreY = Rasterizer.centreY;
		int sine = 0;
		int cosine = 0;
		if (i != 0) {
			sine = SINE[i];
			cosine = COSINE[i];
		}
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int x2 = this.verticesX[vertex];
			int y2 = this.verticesY[vertex];
			int z2 = this.verticesZ[vertex];
			if (i != 0) {
				int newX2 = z2 * sine + x2 * cosine >> 16;
				z2 = z2 * cosine - x2 * sine >> 16;
				x2 = newX2;
			}
			x2 += x;
			y2 += y;
			z2 += z;
			int translation = z2 * xCameraSine + x2 * xCameraCosine >> 16;
			z2 = z2 * xCameraCosine - x2 * xCameraSine >> 16;
			x2 = translation;

			translation = y2 * yCameraCosine - z2 * yCameraSine >> 16;
			z2 = y2 * yCameraSine + z2 * yCameraCosine >> 16;
			y2 = translation;
			vertexScreenZ[vertex] = z2 - k2;
			if (z2 >= 50) {
				vertexScreenX[vertex] = centreX + (x2 << 9) / z2;
				vertexScreenY[vertex] = centreY + (y2 << 9) / z2;
			} else {
				vertexScreenX[vertex] = -5000;
				flag = true;
			}
			if (flag || this.texturedTriangleCount > 0) {
				vertexMovedX[vertex] = x2;
				vertexMovedY[vertex] = y2;
				vertexMovedZ[vertex] = z2;
			}
		}

		try {
			this.method483(flag, flag1, i2);
		} catch (Exception _ex) {
		}
	}

	public void renderSingle(int rotationY, int rotationZ, int rotationXW, int translationX, int translationY,
			int translationZ) {
		int rotationX = 0; // was a parameter
		int centerX = Rasterizer.centreX;
		int centerY = Rasterizer.centreY;
		int sineX = SINE[rotationX];
		int cosineX = COSINE[rotationX];
		int sineY = SINE[rotationY];
		int cosineY = COSINE[rotationY];
		int sineZ = SINE[rotationZ];
		int cosineZ = COSINE[rotationZ];
		int sineXW = SINE[rotationXW];
		int cosineXW = COSINE[rotationXW];
		int transformation = translationY * sineXW + translationZ * cosineXW >> 16;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int x = this.verticesX[vertex];
			int y = this.verticesY[vertex];
			int z = this.verticesZ[vertex];
			if (rotationZ != 0) {
				int newX = y * sineZ + x * cosineZ >> 16;
				y = y * cosineZ - x * sineZ >> 16;
				x = newX;
			}
			if (rotationX != 0) {
				int newY = y * cosineX - z * sineX >> 16;
				z = y * sineX + z * cosineX >> 16;
				y = newY;
			}
			if (rotationY != 0) {
				int newX = z * sineY + x * cosineY >> 16;
				z = z * cosineY - x * sineY >> 16;
				x = newX;
			}
			x += translationX;
			y += translationY;
			z += translationZ;
			int newY = y * cosineXW - z * sineXW >> 16;
			z = y * sineXW + z * cosineXW >> 16;
			y = newY;
			vertexScreenZ[vertex] = z - transformation;
			vertexScreenX[vertex] = centerX + (x << 9) / z;
			vertexScreenY[vertex] = centerY + (y << 9) / z;
			if (this.texturedTriangleCount > 0) {
				vertexMovedX[vertex] = x;
				vertexMovedY[vertex] = y;
				vertexMovedZ[vertex] = z;
			}
		}

		try {
			this.method483(false, false, 0);
		} catch (Exception _ex) {
		}
	}

	public void replaceWithModel(Model model, boolean replaceAlpha) {
        this.vertexCount = model.vertexCount;
        this.triangleCount = model.triangleCount;
        this.texturedTriangleCount = model.texturedTriangleCount;
		if (anIntArray1622.length < this.vertexCount) {
			anIntArray1622 = new int[this.vertexCount + 100];
			anIntArray1623 = new int[this.vertexCount + 100];
			anIntArray1624 = new int[this.vertexCount + 100];
		}
        this.verticesX = anIntArray1622;
        this.verticesY = anIntArray1623;
        this.verticesZ = anIntArray1624;
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
            this.verticesX[vertex] = model.verticesX[vertex];
            this.verticesY[vertex] = model.verticesY[vertex];
            this.verticesZ[vertex] = model.verticesZ[vertex];
		}

		if (replaceAlpha) {
            this.triangleAlpha = model.triangleAlpha;
		} else {
			if (anIntArray1625.length < this.triangleCount) {
                anIntArray1625 = new int[this.triangleCount + 100];
            }
            this.triangleAlpha = anIntArray1625;
			if (model.triangleAlpha == null) {
				for (int triangle = 0; triangle < this.triangleCount; triangle++) {
                    this.triangleAlpha[triangle] = 0;
                }

			} else {
				System.arraycopy(model.triangleAlpha, 0, this.triangleAlpha, 0, this.triangleCount);

			}
		}
        this.triangleDrawType = model.triangleDrawType;
        this.triangleColours = model.triangleColours;
        this.trianglePriorities = model.trianglePriorities;
        this.trianglePriority = model.trianglePriority;
        this.triangleSkin = model.triangleSkin;
        this.vertexSkin = model.vertexSkin;
        this.triangleX = model.triangleX;
        this.triangleY = model.triangleY;
        this.triangleZ = model.triangleZ;
        this.triangleHSLA = model.triangleHSLA;
        this.triangleHSLB = model.triangleHSLB;
        this.triangleHSLC = model.triangleHSLC;
        this.texturedTrianglePointsX = model.texturedTrianglePointsX;
        this.texturedTrianglePointsY = model.texturedTrianglePointsY;
        this.texturedTrianglePointsZ = model.texturedTrianglePointsZ;
	}

	public void rotate90Degrees() {
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int vertexX = this.verticesX[vertex];
            this.verticesX[vertex] = this.verticesZ[vertex];
            this.verticesZ[vertex] = -vertexX;
		}
	}

	public void rotateX(int degrees) {
		int sine = SINE[degrees];
		int cosine = COSINE[degrees];
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int newVertexY = this.verticesY[vertex] * cosine - this.verticesZ[vertex] * sine >> 16;
            this.verticesZ[vertex] = this.verticesY[vertex] * sine + this.verticesZ[vertex] * cosine >> 16;
            this.verticesY[vertex] = newVertexY;
		}
	}

	public void scaleT(int x, int z, int y) {
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
            this.verticesX[vertex] = (this.verticesX[vertex] * x) / 128;
            this.verticesY[vertex] = (this.verticesY[vertex] * y) / 128;
            this.verticesZ[vertex] = (this.verticesZ[vertex] * z) / 128;
		}

	}

	private void transformFrame(int opcode, int skinList[], int vertexTransformationX, int vertexTransformationY,
			int vertexTransformationZ) {
		int skinListCount = skinList.length;
		if (opcode == 0) {
			int affectedSkins = 0;
			vertexModifierX = 0;
			vertexModifierY = 0;
			vertexModifierZ = 0;
			for (int skinListId = 0; skinListId < skinListCount; skinListId++) {
				int skinId = skinList[skinListId];
				if (skinId < this.vertexSkin.length) {
					int vertexSkins[] = this.vertexSkin[skinId];
					for (int skin = 0; skin < vertexSkins.length; skin++) {
						int vertex = vertexSkins[skin];
						vertexModifierX += this.verticesX[vertex];
						vertexModifierY += this.verticesY[vertex];
						vertexModifierZ += this.verticesZ[vertex];
						affectedSkins++;
					}

				}
			}

			if (affectedSkins > 0) {
				vertexModifierX = vertexModifierX / affectedSkins + vertexTransformationX;
				vertexModifierY = vertexModifierY / affectedSkins + vertexTransformationY;
				vertexModifierZ = vertexModifierZ / affectedSkins + vertexTransformationZ;
				return;
			} else {
				vertexModifierX = vertexTransformationX;
				vertexModifierY = vertexTransformationY;
				vertexModifierZ = vertexTransformationZ;
				return;
			}
		}
		if (opcode == 1) {
			for (int skinListId = 0; skinListId < skinListCount; skinListId++) {
				int skinId = skinList[skinListId];
				if (skinId < this.vertexSkin.length) {
					int vertexSkins[] = this.vertexSkin[skinId];
					for (int skin = 0; skin < vertexSkins.length; skin++) {
						int vertex = vertexSkins[skin];
                        this.verticesX[vertex] += vertexTransformationX;
                        this.verticesY[vertex] += vertexTransformationY;
                        this.verticesZ[vertex] += vertexTransformationZ;
					}

				}
			}

			return;
		}
		if (opcode == 2) {
			for (int skinListId = 0; skinListId < skinListCount; skinListId++) {
				int skinId = skinList[skinListId];
				if (skinId < this.vertexSkin.length) {
					int vertexSkins[] = this.vertexSkin[skinId];
					for (int skin = 0; skin < vertexSkins.length; skin++) {
						int vertex = vertexSkins[skin];
                        this.verticesX[vertex] -= vertexModifierX;
                        this.verticesY[vertex] -= vertexModifierY;
                        this.verticesZ[vertex] -= vertexModifierZ;
						int rotationX = (vertexTransformationX & 0xff) * 8;
						int rotationY = (vertexTransformationY & 0xff) * 8;
						int rotationZ = (vertexTransformationZ & 0xff) * 8;
						if (rotationZ != 0) {
							int sine = SINE[rotationZ];
							int cosine = COSINE[rotationZ];
							int newVertexX = this.verticesY[vertex] * sine + this.verticesX[vertex] * cosine >> 16;
                            this.verticesY[vertex] = this.verticesY[vertex] * cosine - this.verticesX[vertex] * sine >> 16;
                            this.verticesX[vertex] = newVertexX;
						}
						if (rotationX != 0) {
							int sine = SINE[rotationX];
							int cosine = COSINE[rotationX];
							int newVertexY = this.verticesY[vertex] * cosine - this.verticesZ[vertex] * sine >> 16;
                            this.verticesZ[vertex] = this.verticesY[vertex] * sine + this.verticesZ[vertex] * cosine >> 16;
                            this.verticesY[vertex] = newVertexY;
						}
						if (rotationY != 0) {
							int sine = SINE[rotationY];
							int cosine = COSINE[rotationY];
							int newVertexZ = this.verticesZ[vertex] * sine + this.verticesX[vertex] * cosine >> 16;
                            this.verticesZ[vertex] = this.verticesZ[vertex] * cosine - this.verticesX[vertex] * sine >> 16;
                            this.verticesX[vertex] = newVertexZ;
						}
                        this.verticesX[vertex] += vertexModifierX;
                        this.verticesY[vertex] += vertexModifierY;
                        this.verticesZ[vertex] += vertexModifierZ;
					}

				}
			}

			return;
		}
		if (opcode == 3) {
			for (int skinListId = 0; skinListId < skinListCount; skinListId++) {
				int skinId = skinList[skinListId];
				if (skinId < this.vertexSkin.length) {
					int vertexSkins[] = this.vertexSkin[skinId];
					for (int skin = 0; skin < vertexSkins.length; skin++) {
						int vertex = vertexSkins[skin];
                        this.verticesX[vertex] -= vertexModifierX;
                        this.verticesY[vertex] -= vertexModifierY;
                        this.verticesZ[vertex] -= vertexModifierZ;
                        this.verticesX[vertex] = (this.verticesX[vertex] * vertexTransformationX) / 128;
                        this.verticesY[vertex] = (this.verticesY[vertex] * vertexTransformationY) / 128;
                        this.verticesZ[vertex] = (this.verticesZ[vertex] * vertexTransformationZ) / 128;
                        this.verticesX[vertex] += vertexModifierX;
                        this.verticesY[vertex] += vertexModifierY;
                        this.verticesZ[vertex] += vertexModifierZ;
					}

				}
			}

			return;
		}
		if (opcode == 5 && this.triangleSkin != null && this.triangleAlpha != null) {
			for (int skinListId = 0; skinListId < skinListCount; skinListId++) {
				int skinId = skinList[skinListId];
				if (skinId < this.triangleSkin.length) {
					int triangleSkins[] = this.triangleSkin[skinId];
					for (int skin = 0; skin < triangleSkins.length; skin++) {
						int triangle = triangleSkins[skin];
                        this.triangleAlpha[triangle] += vertexTransformationX * 8;
						if (this.triangleAlpha[triangle] < 0) {
                            this.triangleAlpha[triangle] = 0;
                        }
						if (this.triangleAlpha[triangle] > 255) {
                            this.triangleAlpha[triangle] = 255;
                        }
					}

				}
			}

		}
	}

	public void translate(int x, int y, int z) {
		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
            this.verticesX[vertex] += x;
            this.verticesY[vertex] += y;
            this.verticesZ[vertex] += z;
		}

	}
}
