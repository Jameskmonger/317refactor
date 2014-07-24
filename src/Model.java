public final class Model extends Animable {

    public static void nullLoader()
    {
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

    public static void init(int modelCount, OnDemandFetcherParent requester)
    {
        modelHeaders = new ModelHeader[modelCount];
        Model.requester = requester;
    }

    public static void loadModelHeader(byte modelData[], int modelId)
    {
        if(modelData == null)
        {
            ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader();
            modelHeader.vertexCount = 0;
            modelHeader.triangleCount = 0;
            modelHeader.texturedTriangleCount = 0;
            return;
        }
        Stream stream = new Stream(modelData);
        stream.currentOffset = modelData.length - 18;
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
        if(useTrianglePriority == 255)
            offset += modelHeader.triangleCount;
        else
            modelHeader.trianglePriorityOffset = -useTrianglePriority - 1;
        modelHeader.triangleSkinOffset = offset;
        if(useTriangleSkins == 1)
            offset += modelHeader.triangleCount;
        else
            modelHeader.triangleSkinOffset = -1;
        modelHeader.texturePointerOffset = offset;
        if(useTextures == 1)
            offset += modelHeader.triangleCount;
        else
            modelHeader.texturePointerOffset = -1;
        modelHeader.vertexSkinOffset = offset;
        if(useVertexSkins == 1)
            offset += modelHeader.vertexCount;
        else
            modelHeader.vertexSkinOffset = -1;
        modelHeader.triangleAlphaOffset = offset;
        if(useAlpha == 1)
            offset += modelHeader.triangleCount;
        else
            modelHeader.triangleAlphaOffset = -1;
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

    public static void resetModel(int model)
    {
        modelHeaders[model] = null;
    }

    public static Model getModel(int model)
    {
        if(modelHeaders == null)
            return null;
        ModelHeader modelHeader = modelHeaders[model];
        if(modelHeader == null)
        {
            requester.request(model);
            return null;
        } else
        {
            return new Model(model);
        }
    }

    public static boolean isCached(int model)
    {
        if(modelHeaders == null)
            return false;
        ModelHeader modelHeader = modelHeaders[model];
        if(modelHeader == null)
        {
            requester.request(model);
            return false;
        } else
        {
            return true;
        }
    }

    private Model()
    {
        singleTile = false;
    }

    private Model(int model)
    {
        singleTile = false;
        ModelHeader modelHeader = modelHeaders[model];
        vertexCount = modelHeader.vertexCount;
        triangleCount = modelHeader.triangleCount;
        texturedTriangleCount = modelHeader.texturedTriangleCount;
        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        triangleX = new int[triangleCount];
        triangleY = new int[triangleCount];
        triangleZ = new int[triangleCount];
        texturedTrianglePointsX = new int[texturedTriangleCount];
        texturedTrianglePointsY = new int[texturedTriangleCount];
        texturedTrianglePointsZ = new int[texturedTriangleCount];
        if(modelHeader.vertexSkinOffset >= 0)
            vertexSkins = new int[vertexCount];
        if(modelHeader.texturePointerOffset >= 0)
            triangleDrawType = new int[triangleCount];
        if(modelHeader.trianglePriorityOffset >= 0)
            trianglePriorities = new int[triangleCount];
        else
            anInt1641 = -modelHeader.trianglePriorityOffset - 1;
        if(modelHeader.triangleAlphaOffset >= 0)
            triangleAlpha = new int[triangleCount];
        if(modelHeader.triangleSkinOffset >= 0)
            triangleSkins = new int[triangleCount];
        triangleColours = new int[triangleCount];
        Stream vertexDirectionOffsetStream = new Stream(modelHeader.modelData);
        vertexDirectionOffsetStream.currentOffset = modelHeader.vertexDirectionOffset;
        Stream xDataOffsetStream = new Stream(modelHeader.modelData);
        xDataOffsetStream.currentOffset = modelHeader.dataOffsetX;
        Stream yDataOffsetStream = new Stream(modelHeader.modelData);
        yDataOffsetStream.currentOffset = modelHeader.dataOffsetY;
        Stream zDataOffsetStream = new Stream(modelHeader.modelData);
        zDataOffsetStream.currentOffset = modelHeader.dataOffsetZ;
        Stream vertexSkinOffsetStream = new Stream(modelHeader.modelData);
        vertexSkinOffsetStream.currentOffset = modelHeader.vertexSkinOffset;
        int baseOffsetX = 0;
        int baseOffsetY = 0;
        int baseOffsetZ = 0;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int flag = vertexDirectionOffsetStream.getUnsignedByte();
            int currentOffsetX = 0;
            if((flag & 1) != 0)
                currentOffsetX = xDataOffsetStream.getSmartA();
            int currentOffsetY = 0;
            if((flag & 2) != 0)
                currentOffsetY = yDataOffsetStream.getSmartA();
            int currentOffsetZ = 0;
            if((flag & 4) != 0)
                currentOffsetZ = zDataOffsetStream.getSmartA();
            verticesX[vertex] = baseOffsetX + currentOffsetX;
            verticesY[vertex] = baseOffsetY + currentOffsetY;
            verticesZ[vertex] = baseOffsetZ + currentOffsetZ;
            baseOffsetX = verticesX[vertex];
            baseOffsetY = verticesY[vertex];
            baseOffsetZ = verticesZ[vertex];
            if(vertexSkins != null)
                vertexSkins[vertex] = vertexSkinOffsetStream.getUnsignedByte();
        }

        vertexDirectionOffsetStream.currentOffset = modelHeader.colourDataOffset;
        xDataOffsetStream.currentOffset = modelHeader.texturePointerOffset;
        yDataOffsetStream.currentOffset = modelHeader.trianglePriorityOffset;
        zDataOffsetStream.currentOffset = modelHeader.triangleAlphaOffset;
        vertexSkinOffsetStream.currentOffset = modelHeader.triangleSkinOffset;
        for(int triangle = 0; triangle < triangleCount; triangle++)
        {
            triangleColours[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
            if(triangleDrawType != null)
                triangleDrawType[triangle] = xDataOffsetStream.getUnsignedByte();
            if(trianglePriorities != null)
                trianglePriorities[triangle] = yDataOffsetStream.getUnsignedByte();
            if(triangleAlpha != null)
                triangleAlpha[triangle] = zDataOffsetStream.getUnsignedByte();
            if(triangleSkins != null)
                triangleSkins[triangle] = vertexSkinOffsetStream.getUnsignedByte();
        }

        vertexDirectionOffsetStream.currentOffset = modelHeader.triangleDataOffset;
        xDataOffsetStream.currentOffset = modelHeader.triangleTypeOffset;
        int trianglePointOffsetX = 0;
        int trianglePointOffsetY = 0;
        int trianglePointOffsetZ = 0;
        int offset = 0;
        for(int triangle = 0; triangle < triangleCount; triangle++)
        {
            int type = xDataOffsetStream.getUnsignedByte();
            if(type == 1)
            {
                trianglePointOffsetX = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetX;
                trianglePointOffsetY = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetY;
                trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetZ;
                triangleX[triangle] = trianglePointOffsetX;
                triangleY[triangle] = trianglePointOffsetY;
                triangleZ[triangle] = trianglePointOffsetZ;
            }
            if(type == 2)
            {
                trianglePointOffsetY = trianglePointOffsetZ;
                trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetZ;
                triangleX[triangle] = trianglePointOffsetX;
                triangleY[triangle] = trianglePointOffsetY;
                triangleZ[triangle] = trianglePointOffsetZ;
            }
            if(type == 3)
            {
                trianglePointOffsetX = trianglePointOffsetZ;
                trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetZ;
                triangleX[triangle] = trianglePointOffsetX;
                triangleY[triangle] = trianglePointOffsetY;
                triangleZ[triangle] = trianglePointOffsetZ;
            }
            if(type == 4)
            {
                int oldTrianglePointOffsetX = trianglePointOffsetX;
                trianglePointOffsetX = trianglePointOffsetY;
                trianglePointOffsetY = oldTrianglePointOffsetX;
                trianglePointOffsetZ = vertexDirectionOffsetStream.getSmartA() + offset;
                offset = trianglePointOffsetZ;
                triangleX[triangle] = trianglePointOffsetX;
                triangleY[triangle] = trianglePointOffsetY;
                triangleZ[triangle] = trianglePointOffsetZ;
            }
        }

        vertexDirectionOffsetStream.currentOffset = modelHeader.texturedTriangleOffset;
        for(int triangle = 0; triangle < texturedTriangleCount; triangle++)
        {
            texturedTrianglePointsX[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
            texturedTrianglePointsY[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
            texturedTrianglePointsZ[triangle] = vertexDirectionOffsetStream.getUnsignedLEShort();
        }

    }

    public Model(int modelCount, Model models[])
    {
        singleTile = false;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        vertexCount = 0;
        triangleCount = 0;
        texturedTriangleCount = 0;
        anInt1641 = -1;
        for(int m = 0; m < modelCount; m++)
        {
            Model model = models[m];
            if(model != null)
            {
                vertexCount += model.vertexCount;
                triangleCount += model.triangleCount;
                texturedTriangleCount += model.texturedTriangleCount;
                flag |= model.triangleDrawType != null;
                if(model.trianglePriorities != null)
                {
                    flag1 = true;
                } else
                {
                    if(anInt1641 == -1)
                        anInt1641 = model.anInt1641;
                    if(anInt1641 != model.anInt1641)
                        flag1 = true;
                }
                flag2 |= model.triangleAlpha != null;
                flag3 |= model.triangleSkins != null;
            }
        }

        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        vertexSkins = new int[vertexCount];
        triangleX = new int[triangleCount];
        triangleY = new int[triangleCount];
        triangleZ = new int[triangleCount];
        texturedTrianglePointsX = new int[texturedTriangleCount];
        texturedTrianglePointsY = new int[texturedTriangleCount];
        texturedTrianglePointsZ = new int[texturedTriangleCount];
        if(flag)
            triangleDrawType = new int[triangleCount];
        if(flag1)
            trianglePriorities = new int[triangleCount];
        if(flag2)
            triangleAlpha = new int[triangleCount];
        if(flag3)
            triangleSkins = new int[triangleCount];
        triangleColours = new int[triangleCount];
        vertexCount = 0;
        triangleCount = 0;
        texturedTriangleCount = 0;
        int count = 0;
        for(int m = 0; m < modelCount; m++)
        {
            Model model = models[m];
            if(model != null)
            {
                for(int triangle = 0; triangle < model.triangleCount; triangle++)
                {
                    if(flag)
                        if(model.triangleDrawType == null)
                        {
                            triangleDrawType[triangleCount] = 0;
                        } else
                        {
                            int drawType = model.triangleDrawType[triangle];
                            if((drawType & 2) == 2)
                                drawType += count << 2;
                            triangleDrawType[triangleCount] = drawType;
                        }
                    if(flag1)
                        if(model.trianglePriorities == null)
                            trianglePriorities[triangleCount] = model.anInt1641;
                        else
                            trianglePriorities[triangleCount] = model.trianglePriorities[triangle];
                    if(flag2)
                        if(model.triangleAlpha == null)
                            triangleAlpha[triangleCount] = 0;
                        else
                            triangleAlpha[triangleCount] = model.triangleAlpha[triangle];
                    if(flag3 && model.triangleSkins != null)
                        triangleSkins[triangleCount] = model.triangleSkins[triangle];
                    triangleColours[triangleCount] = model.triangleColours[triangle];
                    triangleX[triangleCount] = getFirstIdenticalVertexId(model, model.triangleX[triangle]);
                    triangleY[triangleCount] = getFirstIdenticalVertexId(model, model.triangleY[triangle]);
                    triangleZ[triangleCount] = getFirstIdenticalVertexId(model, model.triangleZ[triangle]);
                    triangleCount++;
                }

                for(int triangle = 0; triangle < model.texturedTriangleCount; triangle++)
                {
                    texturedTrianglePointsX[texturedTriangleCount] = getFirstIdenticalVertexId(model, model.texturedTrianglePointsX[triangle]);
                    texturedTrianglePointsY[texturedTriangleCount] = getFirstIdenticalVertexId(model, model.texturedTrianglePointsY[triangle]);
                    texturedTrianglePointsZ[texturedTriangleCount] = getFirstIdenticalVertexId(model, model.texturedTrianglePointsZ[triangle]);
                    texturedTriangleCount++;
                }

                count += model.texturedTriangleCount;
            }
        }

    }

    public Model(Model models[])
    {
        int modelCount = 2;//was parameter
        singleTile = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        vertexCount = 0;
        triangleCount = 0;
        texturedTriangleCount = 0;
        anInt1641 = -1;
        for(int m = 0; m < modelCount; m++)
        {
            Model model = models[m];
            if(model != null)
            {
                vertexCount += model.vertexCount;
                triangleCount += model.triangleCount;
                texturedTriangleCount += model.texturedTriangleCount;
                flag1 |= model.triangleDrawType != null;
                if(model.trianglePriorities != null)
                {
                    flag2 = true;
                } else
                {
                    if(anInt1641 == -1)
                        anInt1641 = model.anInt1641;
                    if(anInt1641 != model.anInt1641)
                        flag2 = true;
                }
                flag3 |= model.triangleAlpha != null;
                flag4 |= model.triangleColours != null;
            }
        }

        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        triangleX = new int[triangleCount];
        triangleY = new int[triangleCount];
        triangleZ = new int[triangleCount];
        triangleHSLA = new int[triangleCount];
        triangleHSLB = new int[triangleCount];
        triangleHSLC = new int[triangleCount];
        texturedTrianglePointsX = new int[texturedTriangleCount];
        texturedTrianglePointsY = new int[texturedTriangleCount];
        texturedTrianglePointsZ = new int[texturedTriangleCount];
        if(flag1)
            triangleDrawType = new int[triangleCount];
        if(flag2)
            trianglePriorities = new int[triangleCount];
        if(flag3)
            triangleAlpha = new int[triangleCount];
        if(flag4)
            triangleColours = new int[triangleCount];
        vertexCount = 0;
        triangleCount = 0;
        texturedTriangleCount = 0;
        int count = 0;
        for(int m = 0; m < modelCount; m++)
        {
            Model model = models[m];
            if(model != null)
            {
                int v = vertexCount;
                for(int vertex = 0; vertex < model.vertexCount; vertex++)
                {
                    verticesX[vertexCount] = model.verticesX[vertex];
                    verticesY[vertexCount] = model.verticesY[vertex];
                    verticesZ[vertexCount] = model.verticesZ[vertex];
                    vertexCount++;
                }

                for(int triangle = 0; triangle < model.triangleCount; triangle++)
                {
                    triangleX[triangleCount] = model.triangleX[triangle] + v;
                    triangleY[triangleCount] = model.triangleY[triangle] + v;
                    triangleZ[triangleCount] = model.triangleZ[triangle] + v;
                    triangleHSLA[triangleCount] = model.triangleHSLA[triangle];
                    triangleHSLB[triangleCount] = model.triangleHSLB[triangle];
                    triangleHSLC[triangleCount] = model.triangleHSLC[triangle];
                    if(flag1)
                        if(model.triangleDrawType == null)
                        {
                            triangleDrawType[triangleCount] = 0;
                        } else
                        {
                            int drawType = model.triangleDrawType[triangle];
                            if((drawType & 2) == 2)
                                drawType += count << 2;
                            triangleDrawType[triangleCount] = drawType;
                        }
                    if(flag2)
                        if(model.trianglePriorities == null)
                            trianglePriorities[triangleCount] = model.anInt1641;
                        else
                            trianglePriorities[triangleCount] = model.trianglePriorities[triangle];
                    if(flag3)
                        if(model.triangleAlpha == null)
                            triangleAlpha[triangleCount] = 0;
                        else
                            triangleAlpha[triangleCount] = model.triangleAlpha[triangle];
                    if(flag4 && model.triangleColours != null)
                        triangleColours[triangleCount] = model.triangleColours[triangle];
                    triangleCount++;
                }

                for(int triangle = 0; triangle < model.texturedTriangleCount; triangle++)
                {
                    texturedTrianglePointsX[texturedTriangleCount] = model.texturedTrianglePointsX[triangle] + v;
                    texturedTrianglePointsY[texturedTriangleCount] = model.texturedTrianglePointsY[triangle] + v;
                    texturedTrianglePointsZ[texturedTriangleCount] = model.texturedTrianglePointsZ[triangle] + v;
                    texturedTriangleCount++;
                }

                count += model.texturedTriangleCount;
            }
        }

        calculateDiagonals();
    }

    public Model(boolean flag, boolean flag1, boolean flag2, Model model)
    {
        singleTile = false;
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        texturedTriangleCount = model.texturedTriangleCount;
        if(flag2)
        {
            verticesX = model.verticesX;
            verticesY = model.verticesY;
            verticesZ = model.verticesZ;
        } else
        {
            verticesX = new int[vertexCount];
            verticesY = new int[vertexCount];
            verticesZ = new int[vertexCount];
            for(int vertex = 0; vertex < vertexCount; vertex++)
            {
                verticesX[vertex] = model.verticesX[vertex];
                verticesY[vertex] = model.verticesY[vertex];
                verticesZ[vertex] = model.verticesZ[vertex];
            }

        }
        if(flag)
        {
            triangleColours = model.triangleColours;
        } else
        {
            triangleColours = new int[triangleCount];
            System.arraycopy(model.triangleColours, 0, triangleColours, 0, triangleCount);

        }
        if(flag1)
        {
            triangleAlpha = model.triangleAlpha;
        } else
        {
            triangleAlpha = new int[triangleCount];
            if(model.triangleAlpha == null)
            {
                for(int triangle = 0; triangle < triangleCount; triangle++)
                    triangleAlpha[triangle] = 0;

            } else
            {
                System.arraycopy(model.triangleAlpha, 0, triangleAlpha, 0, triangleCount);

            }
        }
        vertexSkins = model.vertexSkins;
        triangleSkins = model.triangleSkins;
        triangleDrawType = model.triangleDrawType;
        triangleX = model.triangleX;
        triangleY = model.triangleY;
        triangleZ = model.triangleZ;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        texturedTrianglePointsX = model.texturedTrianglePointsX;
        texturedTrianglePointsY = model.texturedTrianglePointsY;
        texturedTrianglePointsZ = model.texturedTrianglePointsZ;
    }

    public Model(boolean flag, boolean flag1, Model model)
    {
        singleTile = false;
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        texturedTriangleCount = model.texturedTriangleCount;
        if(flag)
        {
            verticesY = new int[vertexCount];
            System.arraycopy(model.verticesY, 0, verticesY, 0, vertexCount);

        } else
        {
            verticesY = model.verticesY;
        }
        if(flag1)
        {
            triangleHSLA = new int[triangleCount];
            triangleHSLB = new int[triangleCount];
            triangleHSLC = new int[triangleCount];
            for(int triangle = 0; triangle < triangleCount; triangle++)
            {
                triangleHSLA[triangle] = model.triangleHSLA[triangle];
                triangleHSLB[triangle] = model.triangleHSLB[triangle];
                triangleHSLC[triangle] = model.triangleHSLC[triangle];
            }

            triangleDrawType = new int[triangleCount];
            if(model.triangleDrawType == null)
            {
                for(int triangle = 0; triangle < triangleCount; triangle++)
                    triangleDrawType[triangle] = 0;

            } else
            {
                System.arraycopy(model.triangleDrawType, 0, triangleDrawType, 0, triangleCount);

            }
            super.vertexNormals = new VertexNormal[vertexCount];
            for(int vertex = 0; vertex < vertexCount; vertex++)
            {
                VertexNormal vertexNormalNew = super.vertexNormals[vertex] = new VertexNormal();
                VertexNormal vertexNormalOld = model.vertexNormals[vertex];
                vertexNormalNew.x = vertexNormalOld.x;
                vertexNormalNew.y = vertexNormalOld.y;
                vertexNormalNew.z = vertexNormalOld.z;
                vertexNormalNew.magnitude = vertexNormalOld.magnitude;
            }

            vertexNormalOffset = model.vertexNormalOffset;
        } else
        {
            triangleHSLA = model.triangleHSLA;
            triangleHSLB = model.triangleHSLB;
            triangleHSLC = model.triangleHSLC;
            triangleDrawType = model.triangleDrawType;
        }
        verticesX = model.verticesX;
        verticesZ = model.verticesZ;
        triangleColours = model.triangleColours;
        triangleAlpha = model.triangleAlpha;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        triangleX = model.triangleX;
        triangleY = model.triangleY;
        triangleZ = model.triangleZ;
        texturedTrianglePointsX = model.texturedTrianglePointsX;
        texturedTrianglePointsY = model.texturedTrianglePointsY;
        texturedTrianglePointsZ = model.texturedTrianglePointsZ;
        super.modelHeight = model.modelHeight;
        maxY = model.maxY; 
        diagonal2DAboveOrigin = model.diagonal2DAboveOrigin;
        diagonal3DAboveOrigin = model.diagonal3DAboveOrigin;
        diagonal3D = model.diagonal3D;
        minX = model.minX;
        maxZ = model.maxZ;
        minZ = model.minZ;
        maxX = model.maxX;
    }

    public void replaceWithModel(Model model, boolean replaceAlpha)
    {
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        texturedTriangleCount = model.texturedTriangleCount;
        if(anIntArray1622.length < vertexCount)
        {
            anIntArray1622 = new int[vertexCount + 100];
            anIntArray1623 = new int[vertexCount + 100];
            anIntArray1624 = new int[vertexCount + 100];
        }
        verticesX = anIntArray1622;
        verticesY = anIntArray1623;
        verticesZ = anIntArray1624;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            verticesX[vertex] = model.verticesX[vertex];
            verticesY[vertex] = model.verticesY[vertex];
            verticesZ[vertex] = model.verticesZ[vertex];
        }

        if(replaceAlpha)
        {
            triangleAlpha = model.triangleAlpha;
        } else
        {
            if(anIntArray1625.length < triangleCount)
                anIntArray1625 = new int[triangleCount + 100];
            triangleAlpha = anIntArray1625;
            if(model.triangleAlpha == null)
            {
                for(int triangle = 0; triangle < triangleCount; triangle++)
                    triangleAlpha[triangle] = 0;

            } else
            {
                System.arraycopy(model.triangleAlpha, 0, triangleAlpha, 0, triangleCount);

            }
        }
        triangleDrawType = model.triangleDrawType;
        triangleColours = model.triangleColours;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        triangleSkin = model.triangleSkin;
        vertexSkin = model.vertexSkin;
        triangleX = model.triangleX;
        triangleY = model.triangleY;
        triangleZ = model.triangleZ;
        triangleHSLA = model.triangleHSLA;
        triangleHSLB = model.triangleHSLB;
        triangleHSLC = model.triangleHSLC;
        texturedTrianglePointsX = model.texturedTrianglePointsX;
        texturedTrianglePointsY = model.texturedTrianglePointsY;
        texturedTrianglePointsZ = model.texturedTrianglePointsZ;
    }

    private int getFirstIdenticalVertexId(Model model, int vertex)
    {
        int vertexId = -1;
        int x = model.verticesX[vertex];
        int y = model.verticesY[vertex];
        int z = model.verticesZ[vertex];
        for(int v = 0; v < vertexCount; v++)
        {
            if(x != verticesX[v] || y != verticesY[v] || z != verticesZ[v])
                continue;
            vertexId = v;
            break;
        }

        if(vertexId == -1)
        {
            verticesX[vertexCount] = x;
            verticesY[vertexCount] = y;
            verticesZ[vertexCount] = z;
            if(model.vertexSkins != null)
                vertexSkins[vertexCount] = model.vertexSkins[vertex];
            vertexId = vertexCount++;
        }
        return vertexId;
    }

    public void calculateDiagonals()
    {
        super.modelHeight = 0;
        diagonal2DAboveOrigin = 0;
        maxY = 0;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int x = verticesX[vertex];
            int y = verticesY[vertex];
            int z = verticesZ[vertex];
            if(-y > super.modelHeight)
                super.modelHeight = -y;
            if(y > maxY)
                maxY = y;
            int bounds = x * x + z * z;
            if(bounds > diagonal2DAboveOrigin)
                diagonal2DAboveOrigin = bounds;
        }
        diagonal2DAboveOrigin = (int)(Math.sqrt(diagonal2DAboveOrigin) + 0.98999999999999999D);
        diagonal3DAboveOrigin = (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        diagonal3D = diagonal3DAboveOrigin + (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + maxY * maxY) + 0.98999999999999999D);
    }

    public void normalise()
    {
        super.modelHeight = 0;
        maxY = 0;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int y = verticesY[vertex];
            if(-y > super.modelHeight)
                super.modelHeight = -y;
            if(y > maxY)
                maxY = y;
        }

        diagonal3DAboveOrigin = (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
        diagonal3D = diagonal3DAboveOrigin + (int)(Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + maxY * maxY) + 0.98999999999999999D);
    }

    private void calculateDiagonalsAndBounds()
    {
        super.modelHeight = 0;
        diagonal2DAboveOrigin = 0;
        maxY = 0;
        minX = 0xf423f;
        maxX = 0xfff0bdc1;
        maxZ = 0xfffe7961;
        minZ = 0x1869f;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int x = verticesX[vertex];
            int y = verticesY[vertex];
            int z = verticesZ[vertex];
            if(x < minX)
                minX = x;
            if(x > maxX)
                maxX = x;
            if(z < minZ)
                minZ = z;
            if(z > maxZ)
                maxZ = z;
            if(-y > super.modelHeight)
                super.modelHeight = -y;
            if(y > maxY)
                maxY = y;
            int bounds = x * x + z * z;
            if(bounds > diagonal2DAboveOrigin)
                diagonal2DAboveOrigin = bounds;
        }

        diagonal2DAboveOrigin = (int)Math.sqrt(diagonal2DAboveOrigin);
        diagonal3DAboveOrigin = (int)Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + super.modelHeight * super.modelHeight);
        diagonal3D = diagonal3DAboveOrigin + (int)Math.sqrt(diagonal2DAboveOrigin * diagonal2DAboveOrigin + maxY * maxY);
    }

    public void createBones()
    {
        if(vertexSkins != null)
        {
            int ai[] = new int[256];
            int count = 0;
            for(int vertex = 0; vertex < vertexCount; vertex++)
            {
                int skins = vertexSkins[vertex];
                ai[skins]++;
                if(skins > count)
                    count = skins;
            }

            vertexSkin = new int[count + 1][];
            for(int vertex = 0; vertex <= count; vertex++)
            {
                vertexSkin[vertex] = new int[ai[vertex]];
                ai[vertex] = 0;
            }

            for(int vertex = 0; vertex < vertexCount; vertex++)
            {
                int skin = vertexSkins[vertex];
                vertexSkin[skin][ai[skin]++] = vertex;
            }

            vertexSkins = null;
        }
        if(triangleSkins != null)
        {
            int ai1[] = new int[256];
            int count = 0;
            for(int triangle = 0; triangle < triangleCount; triangle++)
            {
                int skins = triangleSkins[triangle];
                ai1[skins]++;
                if(skins > count)
                    count = skins;
            }

            triangleSkin = new int[count + 1][];
            for(int triangle = 0; triangle <= count; triangle++)
            {
                triangleSkin[triangle] = new int[ai1[triangle]];
                ai1[triangle] = 0;
            }

            for(int triangle = 0; triangle < triangleCount; triangle++)
            {
                int skins = triangleSkins[triangle];
                triangleSkin[skins][ai1[skins]++] = triangle;
            }

            triangleSkins = null;
        }
    }

    public void applyTransformation(int frameId)
    {
        if(vertexSkin == null)
            return;
        if(frameId == -1)
            return;
        Class36 animationFrame = Class36.forFrameId(frameId);
        if(animationFrame == null)
            return;
        Skins skins = animationFrame.animationSkins;
        vertexModifierX = 0;
        vertexModifierY = 0;
        vertexModifierZ = 0;
        for(int stepId = 0; stepId < animationFrame.frameCount; stepId++)
        {
            int opcode = animationFrame.opcodeTable[stepId];
            transformFrame(skins.opcodes[opcode], skins.skinList[opcode], animationFrame.transformationX[stepId], animationFrame.transformationY[stepId], animationFrame.transformationZ[stepId]);
        }
    }

    public void mixAnimationFrames(int framesFrom2[], int frameId2, int frameId1)
    {
        if(frameId1 == -1)
            return;
        if(framesFrom2 == null || frameId2 == -1)
        {
            applyTransformation(frameId1);
            return;
        }
        Class36 animationFrame1 = Class36.forFrameId(frameId1);
        if(animationFrame1 == null)
            return;
        Class36 animationFrame2 = Class36.forFrameId(frameId2);
        if(animationFrame2 == null)
        {
            applyTransformation(frameId1);
            return;
        }
        Skins skins = animationFrame1.animationSkins;
        vertexModifierX = 0;
        vertexModifierY = 0;
        vertexModifierZ = 0;
        int counter = 0;
        int frameCount = framesFrom2[counter++];
        for(int frame = 0; frame < animationFrame1.frameCount; frame++)
        {
            int skin;
            for(skin = animationFrame1.opcodeTable[frame]; skin > frameCount; frameCount = framesFrom2[counter++]);
            if(skin != frameCount || skins.opcodes[skin] == 0)
                transformFrame(skins.opcodes[skin], skins.skinList[skin], animationFrame1.transformationX[frame], animationFrame1.transformationY[frame], animationFrame1.transformationZ[frame]);
        }

        vertexModifierX = 0;
        vertexModifierY = 0;
        vertexModifierZ = 0;
        counter = 0;
        frameCount = framesFrom2[counter++];
        for(int frame = 0; frame < animationFrame2.frameCount; frame++)
        {
            int skin;
            for(skin = animationFrame2.opcodeTable[frame]; skin > frameCount; frameCount = framesFrom2[counter++]);
            if(skin == frameCount || skins.opcodes[skin] == 0)
                transformFrame(skins.opcodes[skin], skins.skinList[skin], animationFrame2.transformationX[frame], animationFrame2.transformationY[frame], animationFrame2.transformationZ[frame]);
        }

    }

    private void transformFrame(int opcode, int skinList[], int vertexTransformationX, int vertexTransformationY, int vertexTransformationZ)
    {
        int skinListCount = skinList.length;
        if(opcode == 0)
        {
            int affectedSkins = 0;
            vertexModifierX = 0;
            vertexModifierY = 0;
            vertexModifierZ = 0;
            for(int skinListId = 0; skinListId < skinListCount; skinListId++)
            {
                int skinId = skinList[skinListId];
                if(skinId < vertexSkin.length)
                {
                    int vertexSkins[] = vertexSkin[skinId];
                    for(int skin = 0; skin < vertexSkins.length; skin++)
                    {
                        int vertex = vertexSkins[skin];
                        vertexModifierX += verticesX[vertex];
                        vertexModifierY += verticesY[vertex];
                        vertexModifierZ += verticesZ[vertex];
                        affectedSkins++;
                    }

                }
            }

            if(affectedSkins > 0)
            {
                vertexModifierX = vertexModifierX / affectedSkins + vertexTransformationX;
                vertexModifierY = vertexModifierY / affectedSkins + vertexTransformationY;
                vertexModifierZ = vertexModifierZ / affectedSkins + vertexTransformationZ;
                return;
            } else
            {
                vertexModifierX = vertexTransformationX;
                vertexModifierY = vertexTransformationY;
                vertexModifierZ = vertexTransformationZ;
                return;
            }
        }
        if(opcode == 1)
        {
            for(int skinListId = 0; skinListId < skinListCount; skinListId++)
            {
                int skinId = skinList[skinListId];
                if(skinId < vertexSkin.length)
                {
                    int vertexSkins[] = vertexSkin[skinId];
                    for(int skin = 0; skin < vertexSkins.length; skin++)
                    {
                        int vertex = vertexSkins[skin];
                        verticesX[vertex] += vertexTransformationX;
                        verticesY[vertex] += vertexTransformationY;
                        verticesZ[vertex] += vertexTransformationZ;
                    }

                }
            }

            return;
        }
        if(opcode == 2)
        {
            for(int skinListId = 0; skinListId < skinListCount; skinListId++)
            {
                int skinId = skinList[skinListId];
                if(skinId < vertexSkin.length)
                {
                    int vertexSkins[] = vertexSkin[skinId];
                    for(int skin = 0; skin < vertexSkins.length; skin++)
                    {
                        int vertex = vertexSkins[skin];
                        verticesX[vertex] -= vertexModifierX;
                        verticesY[vertex] -= vertexModifierY;
                        verticesZ[vertex] -= vertexModifierZ;
                        int rotationX = (vertexTransformationX & 0xff) * 8;
                        int rotationY = (vertexTransformationY & 0xff) * 8;
                        int rotationZ = (vertexTransformationZ & 0xff) * 8;
                        if(rotationZ != 0)
                        {
                            int sine = SINE[rotationZ];
                            int cosine = COSINE[rotationZ];
                            int newVertexX = verticesY[vertex] * sine + verticesX[vertex] * cosine >> 16;
                            verticesY[vertex] = verticesY[vertex] * cosine - verticesX[vertex] * sine >> 16;
                            verticesX[vertex] = newVertexX;
                        }
                        if(rotationX != 0)
                        {
                            int sine = SINE[rotationX];
                            int cosine = COSINE[rotationX];
                            int newVertexY = verticesY[vertex] * cosine - verticesZ[vertex] * sine >> 16;
                            verticesZ[vertex] = verticesY[vertex] * sine + verticesZ[vertex] * cosine >> 16;
                            verticesY[vertex] = newVertexY;
                        }
                        if(rotationY != 0)
                        {
                            int sine = SINE[rotationY];
                            int cosine = COSINE[rotationY];
                            int newVertexZ = verticesZ[vertex] * sine + verticesX[vertex] * cosine >> 16;
                            verticesZ[vertex] = verticesZ[vertex] * cosine - verticesX[vertex] * sine >> 16;
                            verticesX[vertex] = newVertexZ;
                        }
                        verticesX[vertex] += vertexModifierX;
                        verticesY[vertex] += vertexModifierY;
                        verticesZ[vertex] += vertexModifierZ;
                    }

                }
            }

            return;
        }
        if(opcode == 3)
        {
            for(int skinListId = 0; skinListId < skinListCount; skinListId++)
            {
                int skinId = skinList[skinListId];
                if(skinId < vertexSkin.length)
                {
                    int vertexSkins[] = vertexSkin[skinId];
                    for(int skin = 0; skin < vertexSkins.length; skin++)
                    {
                        int vertex = vertexSkins[skin];
                        verticesX[vertex] -= vertexModifierX;
                        verticesY[vertex] -= vertexModifierY;
                        verticesZ[vertex] -= vertexModifierZ;
                        verticesX[vertex] = (verticesX[vertex] * vertexTransformationX) / 128;
                        verticesY[vertex] = (verticesY[vertex] * vertexTransformationY) / 128;
                        verticesZ[vertex] = (verticesZ[vertex] * vertexTransformationZ) / 128;
                        verticesX[vertex] += vertexModifierX;
                        verticesY[vertex] += vertexModifierY;
                        verticesZ[vertex] += vertexModifierZ;
                    }

                }
            }

            return;
        }
        if(opcode == 5 && triangleSkin != null && triangleAlpha != null)
        {
            for(int skinListId = 0; skinListId < skinListCount; skinListId++)
            {
                int skinId = skinList[skinListId];
                if(skinId < triangleSkin.length)
                {
                    int triangleSkins[] = triangleSkin[skinId];
                    for(int skin = 0; skin < triangleSkins.length; skin++)
                    {
                        int triangle = triangleSkins[skin];
                        triangleAlpha[triangle] += vertexTransformationX * 8;
                        if(triangleAlpha[triangle] < 0)
                            triangleAlpha[triangle] = 0;
                        if(triangleAlpha[triangle] > 255)
                            triangleAlpha[triangle] = 255;
                    }

                }
            }

        }
    }

    public void rotate90Degrees()
    {
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int vertexX = verticesX[vertex];
            verticesX[vertex] = verticesZ[vertex];
            verticesZ[vertex] = -vertexX;
        }
    }

    public void rotateX(int degrees)
    {
        int sine = SINE[degrees];
        int cosine = COSINE[degrees];
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int newVertexY = verticesY[vertex] * cosine - verticesZ[vertex] * sine >> 16;
            verticesZ[vertex] = verticesY[vertex] * sine + verticesZ[vertex] * cosine >> 16;
            verticesY[vertex] = newVertexY;
        }
    }

    public void translate(int x, int y, int z)
    {
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            verticesX[vertex] += x;
            verticesY[vertex] += y;
            verticesZ[vertex] += z;
        }

    }

    public void recolour(int targetColour, int replacementColour)
    {
        for(int triangle = 0; triangle < triangleCount; triangle++)
            if(triangleColours[triangle] == targetColour)
                triangleColours[triangle] = replacementColour;

    }

    public void mirror()
    {
        for(int vertex = 0; vertex < vertexCount; vertex++)
            verticesZ[vertex] = -verticesZ[vertex];

        for(int triangle = 0; triangle < triangleCount; triangle++)
        {
            int newTriangleC = triangleX[triangle];
            triangleX[triangle] = triangleZ[triangle];
            triangleZ[triangle] = newTriangleC;
        }
    }

    public void scaleT(int x, int z, int y)
    {
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            verticesX[vertex] = (verticesX[vertex] * x) / 128;
            verticesY[vertex] = (verticesY[vertex] * y) / 128;
            verticesZ[vertex] = (verticesZ[vertex] * z) / 128;
        }

    }

    public void applyLighting(int lightMod, int magnitudeMultiplier, int lightX, int lightY, int lightZ, boolean flatShading)
    {
        int lightMagnitude = (int)Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
        int magnitude = magnitudeMultiplier * lightMagnitude >> 8;
        if(triangleHSLA == null)
        {
            triangleHSLA = new int[triangleCount];
            triangleHSLB = new int[triangleCount];
            triangleHSLC = new int[triangleCount];
        }
        if(super.vertexNormals == null)
        {
            super.vertexNormals = new VertexNormal[vertexCount];
            for(int vertex = 0; vertex < vertexCount; vertex++)
                super.vertexNormals[vertex] = new VertexNormal();

        }
        for(int triangle = 0; triangle < triangleCount; triangle++)
        {
            int _triangleX = triangleX[triangle];
            int _triangleY = triangleY[triangle];
            int _triangleZ = triangleZ[triangle];
            int distanceXXY = verticesX[_triangleY] - verticesX[_triangleX];
            int distanceYXY = verticesY[_triangleY] - verticesY[_triangleX];
            int distanceZXY = verticesZ[_triangleY] - verticesZ[_triangleX];
            int distanceXZX = verticesX[_triangleZ] - verticesX[_triangleX];
            int distanceYZX = verticesY[_triangleZ] - verticesY[_triangleX];
            int distanceZZX = verticesZ[_triangleZ] - verticesZ[_triangleX];
            int normalX = distanceYXY * distanceZZX - distanceYZX * distanceZXY;
            int normalY = distanceZXY * distanceXZX - distanceZZX * distanceXXY;
            int normalZ;
            for(normalZ = distanceXXY * distanceYZX - distanceXZX * distanceYXY; normalX > 8192 || normalY > 8192 || normalZ > 8192 || normalX < -8192 || normalY < -8192 || normalZ < -8192; normalZ >>= 1)
            {
                normalX >>= 1;
                normalY >>= 1;
            }

            int normalLength = (int)Math.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
            if(normalLength <= 0)
                normalLength = 1;
            normalX = (normalX * 256) / normalLength;
            normalY = (normalY * 256) / normalLength;
            normalZ = (normalZ * 256) / normalLength;
            if(triangleDrawType == null || (triangleDrawType[triangle] & 1) == 0)
            {
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
            } else
            {
                int lightness = lightMod + (lightX * normalX + lightY * normalY + lightZ * normalZ) / (magnitude + magnitude / 2);
                triangleHSLA[triangle] = mixLightness(triangleColours[triangle], lightness, triangleDrawType[triangle]);
            }
        }

        if(flatShading)
        {
            handleShading(lightMod, magnitude, lightX, lightY, lightZ);
        } else
        {
            vertexNormalOffset = new VertexNormal[vertexCount];
            for(int vertex = 0; vertex < vertexCount; vertex++)
            {
                VertexNormal vertexNormal = super.vertexNormals[vertex];
                VertexNormal shadowVertexNormal = vertexNormalOffset[vertex] = new VertexNormal();
                shadowVertexNormal.x = vertexNormal.x;
                shadowVertexNormal.y = vertexNormal.y;
                shadowVertexNormal.z = vertexNormal.z;
                shadowVertexNormal.magnitude = vertexNormal.magnitude;
            }

        }
        if(flatShading)
        {
            calculateDiagonals();
        } else
        {
            calculateDiagonalsAndBounds();
        }
    }

    public void handleShading(int intensity, int falloff, int lightX, int lightY, int lightZ)
    {
        for(int triangle = 0; triangle < triangleCount; triangle++)
        {
            int x = triangleX[triangle];
            int y = triangleY[triangle];
            int z = triangleZ[triangle];
            if(triangleDrawType == null)
            {
                int colour = triangleColours[triangle];
                VertexNormal vertexNormal = super.vertexNormals[x];
                int lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLA[triangle] = mixLightness(colour, lightness, 0);
                vertexNormal = super.vertexNormals[y];
                lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLB[triangle] = mixLightness(colour, lightness, 0);
                vertexNormal = super.vertexNormals[z];
                lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLC[triangle] = mixLightness(colour, lightness, 0);
            } else
            if((triangleDrawType[triangle] & 1) == 0)
            {
                int colour = triangleColours[triangle];
                int drawType = triangleDrawType[triangle];
                VertexNormal vertexNormal = super.vertexNormals[x];
                int lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLA[triangle] = mixLightness(colour, lightness, drawType);
                vertexNormal = super.vertexNormals[y];
                lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLB[triangle] = mixLightness(colour, lightness, drawType);
                vertexNormal = super.vertexNormals[z];
                lightness = intensity + (lightX * vertexNormal.x + lightY * vertexNormal.y + lightZ * vertexNormal.z) / (falloff * vertexNormal.magnitude);
                triangleHSLC[triangle] = mixLightness(colour, lightness, drawType);
            }
        }

        super.vertexNormals = null;
        vertexNormalOffset = null;
        vertexSkins = null;
        triangleSkins = null;
        if(triangleDrawType != null)
        {
            for(int triangle = 0; triangle < triangleCount; triangle++)
                if((triangleDrawType[triangle] & 2) == 2)
                    return;

        }
        triangleColours = null;
    }

    private static int mixLightness(int colour, int lightness, int drawType)
    {
        if((drawType & 2) == 2)
        {
            if(lightness < 0)
                lightness = 0;
            else
            if(lightness > 127)
                lightness = 127;
            lightness = 127 - lightness;
            return lightness;
        }
        lightness = lightness * (colour & 0x7f) >> 7;
        if(lightness < 2)
            lightness = 2;
        else
        if(lightness > 126)
            lightness = 126;
        return (colour & 0xff80) + lightness;
    }

    public void renderSingle(int rotationY, int rotationZ, int rotationXW, int translationX, int translationY, int translationZ)
    {
        int rotationX = 0; //was a parameter
        int centerX = Texture.centreX;
        int centerY = Texture.centreY;
        int sineX = SINE[rotationX];
        int cosineX = COSINE[rotationX];
        int sineY = SINE[rotationY];
        int cosineY = COSINE[rotationY];
        int sineZ = SINE[rotationZ];
        int cosineZ = COSINE[rotationZ];
        int sineXW = SINE[rotationXW];
        int cosineXW = COSINE[rotationXW];
        int transformation = translationY * sineXW + translationZ * cosineXW >> 16;
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int x = verticesX[vertex];
            int y = verticesY[vertex];
            int z = verticesZ[vertex];
            if(rotationZ != 0)
            {
                int newX = y * sineZ + x * cosineZ >> 16;
                y = y * cosineZ - x * sineZ >> 16;
                x = newX;
            }
            if(rotationX != 0)
            {
                int newY = y * cosineX - z * sineX >> 16;
                z = y * sineX + z * cosineX >> 16;
                y = newY;
            }
            if(rotationY != 0)
            {
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
            if(texturedTriangleCount > 0)
            {
                vertexMovedX[vertex] = x;
                vertexMovedY[vertex] = y;
                vertexMovedZ[vertex] = z;
            }
        }

        try
        {
            method483(false, false, 0);
        }
        catch(Exception _ex)
        {
        }
    }

    public void renderAtPoint(int i, int yCameraSine, int yCameraCosine, int xCameraSine, int xCameraCosine, int x, int y, 
            int z, int i2)
    {
        int j2 = z * xCameraCosine - x * xCameraSine >> 16;
        int k2 = y * yCameraSine + j2 * yCameraCosine >> 16;
        int l2 = diagonal2DAboveOrigin * yCameraCosine >> 16;
        int i3 = k2 + l2;
        if(i3 <= 50 || k2 >= 3500)
            return;
        int j3 = z * xCameraSine + x * xCameraCosine >> 16;
        int k3 = j3 - diagonal2DAboveOrigin << 9;
        if(k3 / i3 >= DrawingArea.viewportCentreX)
            return;
        int l3 = j3 + diagonal2DAboveOrigin << 9;
        if(l3 / i3 <= -DrawingArea.viewportCentreX)
            return;
        int i4 = y * yCameraCosine - j2 * yCameraSine >> 16;
        int j4 = diagonal2DAboveOrigin * yCameraSine >> 16;
        int k4 = i4 + j4 << 9;
        if(k4 / i3 <= -DrawingArea.viewportCentreY)
            return;
        int l4 = j4 + (super.modelHeight * yCameraCosine >> 16);
        int i5 = i4 - l4 << 9;
        if(i5 / i3 >= DrawingArea.viewportCentreY)
            return;
        int j5 = l2 + (super.modelHeight * yCameraSine >> 16);
        boolean flag = false;
        if(k2 - j5 <= 50)
            flag = true;
        boolean flag1 = false;
        if(i2 > 0 && aBoolean1684)
        {
            int k5 = k2 - l2;
            if(k5 <= 50)
                k5 = 50;
            if(j3 > 0)
            {
                k3 /= i3;
                l3 /= k5;
            } else
            {
                l3 /= i3;
                k3 /= k5;
            }
            if(i4 > 0)
            {
                i5 /= i3;
                k4 /= k5;
            } else
            {
                k4 /= i3;
                i5 /= k5;
            }
            int i6 = cursorX - Texture.centreX;
            int k6 = cursorY - Texture.centreY;
            if(i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
                if(singleTile)
                    anIntArray1688[anInt1687++] = i2;
                else
                    flag1 = true;
        }
        int centreX = Texture.centreX;
        int centreY = Texture.centreY;
        int sine = 0;
        int cosine = 0;
        if(i != 0)
        {
            sine = SINE[i];
            cosine = COSINE[i];
        }
        for(int vertex = 0; vertex < vertexCount; vertex++)
        {
            int x2 = verticesX[vertex];
            int y2 = verticesY[vertex];
            int z2 = verticesZ[vertex];
            if(i != 0)
            {
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
            if(z2 >= 50)
            {
                vertexScreenX[vertex] = centreX + (x2 << 9) / z2;
                vertexScreenY[vertex] = centreY + (y2 << 9) / z2;
            } else
            {
                vertexScreenX[vertex] = -5000;
                flag = true;
            }
            if(flag || texturedTriangleCount > 0)
            {
                vertexMovedX[vertex] = x2;
                vertexMovedY[vertex] = y2;
                vertexMovedZ[vertex] = z2;
            }
        }

        try
        {
            method483(flag, flag1, i2);
        }
        catch(Exception _ex)
        {
        }
    }

    private void method483(boolean flag, boolean flag1, int i)
    {
        for(int j = 0; j < diagonal3D; j++)
            anIntArray1671[j] = 0;

        for(int triangle = 0; triangle < triangleCount; triangle++)
            if(triangleDrawType == null || triangleDrawType[triangle] != -1)
            {
                int x = triangleX[triangle];
                int y = triangleY[triangle];
                int z = triangleZ[triangle];
                int screenXX = vertexScreenX[x];
                int screenXY = vertexScreenX[y];
                int screenXZ = vertexScreenX[z];
                if(flag && (screenXX == -5000 || screenXY == -5000 || screenXZ == -5000))
                {
                    aBooleanArray1664[triangle] = true;
                    int j5 = (vertexScreenZ[x] + vertexScreenZ[y] + vertexScreenZ[z]) / 3 + diagonal3DAboveOrigin;
                    anIntArrayArray1672[j5][anIntArray1671[j5]++] = triangle;
                } else
                {
                    if(flag1 && method486(cursorX, cursorY, vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], screenXX, screenXY, screenXZ))
                    {
                        anIntArray1688[anInt1687++] = i;
                        flag1 = false;
                    }
                    if((screenXX - screenXY) * (vertexScreenY[z] - vertexScreenY[y]) - (vertexScreenY[x] - vertexScreenY[y]) * (screenXZ - screenXY) > 0)
                    {
                        aBooleanArray1664[triangle] = false;
                        restrictEdges[triangle] = screenXX < 0 || screenXY < 0 || screenXZ < 0 || screenXX > DrawingArea.centerX || screenXY > DrawingArea.centerX || screenXZ > DrawingArea.centerX;
                        int k5 = (vertexScreenZ[x] + vertexScreenZ[y] + vertexScreenZ[z]) / 3 + diagonal3DAboveOrigin;
                        anIntArrayArray1672[k5][anIntArray1671[k5]++] = triangle;
                    }
                }
            }

        if(trianglePriorities == null)
        {
            for(int i1 = diagonal3D - 1; i1 >= 0; i1--)
            {
                int l1 = anIntArray1671[i1];
                if(l1 > 0)
                {
                    int ai[] = anIntArrayArray1672[i1];
                    for(int j3 = 0; j3 < l1; j3++)
                        rasterise(ai[j3]);

                }
            }

            return;
        }
        for(int j1 = 0; j1 < 12; j1++)
        {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }

        for(int i2 = diagonal3D - 1; i2 >= 0; i2--)
        {
            int k2 = anIntArray1671[i2];
            if(k2 > 0)
            {
                int ai1[] = anIntArrayArray1672[i2];
                for(int i4 = 0; i4 < k2; i4++)
                {
                    int l4 = ai1[i4];
                    int l5 = trianglePriorities[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if(l5 < 10)
                        anIntArray1677[l5] += i2;
                    else
                    if(l5 == 10)
                        anIntArray1675[j6] = i2;
                    else
                        anIntArray1676[j6] = i2;
                }

            }
        }

        int l2 = 0;
        if(anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
            l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
        int k3 = 0;
        if(anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
            k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
        int j4 = 0;
        if(anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
            j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
        int i6 = 0;
        int k6 = anIntArray1673[10];
        int ai2[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if(i6 == k6)
        {
            i6 = 0;
            k6 = anIntArray1673[11];
            ai2 = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if(i6 < k6)
            i5 = ai3[i6];
        else
            i5 = -1000;
        for(int l6 = 0; l6 < 10; l6++)
        {
            while(l6 == 0 && i5 > l2) 
            {
                rasterise(ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while(l6 == 3 && i5 > k3) 
            {
                rasterise(ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            while(l6 == 5 && i5 > j4) 
            {
                rasterise(ai2[i6++]);
                if(i6 == k6 && ai2 != anIntArrayArray1674[11])
                {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if(i6 < k6)
                    i5 = ai3[i6];
                else
                    i5 = -1000;
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for(int j7 = 0; j7 < i7; j7++)
                rasterise(ai4[j7]);

        }

        while(i5 != -1000) 
        {
            rasterise(ai2[i6++]);
            if(i6 == k6 && ai2 != anIntArrayArray1674[11])
            {
                i6 = 0;
                ai2 = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if(i6 < k6)
                i5 = ai3[i6];
            else
                i5 = -1000;
        }
    }

    private void rasterise(int i)
    {
        if(aBooleanArray1664[i])
        {
            method485(i);
            return;
        }
        int x = triangleX[i];
        int y = triangleY[i];
        int z = triangleZ[i];
        Texture.restrictEdges = restrictEdges[i];
        if(triangleAlpha == null)
            Texture.alpha = 0;
        else
            Texture.alpha = triangleAlpha[i];
        int drawType;
        if(triangleDrawType == null)
            drawType = 0;
        else
            drawType = triangleDrawType[i] & 3;
        if(drawType == 0)
        {
            Texture.drawShadedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x], vertexScreenX[y], vertexScreenX[z], triangleHSLA[i], triangleHSLB[i], triangleHSLC[i]);
            return;
        }
        if(drawType == 1)
        {
            Texture.drawFlatTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x], vertexScreenX[y], vertexScreenX[z], HSLtoRGB[triangleHSLA[i]]);
            return;
        }
        if(drawType == 2)
        {
            int triangle = triangleDrawType[i] >> 2;
            int x2 = texturedTrianglePointsX[triangle];
            int y2 = texturedTrianglePointsY[triangle];
            int z2 = texturedTrianglePointsZ[triangle];
            Texture.drawTexturedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x], vertexScreenX[y], vertexScreenX[z], triangleHSLA[i], triangleHSLB[i], triangleHSLC[i], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[i]);
            return;
        }
        if(drawType == 3)
        {
            int triangle = triangleDrawType[i] >> 2;
            int x2 = texturedTrianglePointsX[triangle];
            int y2 = texturedTrianglePointsY[triangle];
            int z2 = texturedTrianglePointsZ[triangle];
            Texture.drawTexturedTriangle(vertexScreenY[x], vertexScreenY[y], vertexScreenY[z], vertexScreenX[x], vertexScreenX[y], vertexScreenX[z], triangleHSLA[i], triangleHSLA[i], triangleHSLA[i], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[i]);
        }
    }

    private void method485(int triangle)
    {
        int centreX = Texture.centreX;
        int centreY = Texture.centreY;
        int counter = 0;
        int x = triangleX[triangle];
        int y = triangleY[triangle];
        int z = triangleZ[triangle];
        int movedX = vertexMovedZ[x];
        int movedY = vertexMovedZ[y];
        int movedZ = vertexMovedZ[z];
        if(movedX >= 50)
        {
            xPosition[counter] = vertexScreenX[x];
            yPosition[counter] = vertexScreenY[x];
            zPosition[counter++] = triangleHSLA[triangle];
        } else
        {
            int movedX2 = vertexMovedX[x];
            int movedY2 = vertexMovedY[x];
            int colour = triangleHSLA[triangle];
            if(movedZ >= 50)
            {
                int k5 = (50 - movedX) * modelIntArray4[movedZ - movedX];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * k5 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * k5 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLC[triangle] - colour) * k5 >> 16);
            }
            if(movedY >= 50)
            {
                int l5 = (50 - movedX) * modelIntArray4[movedY - movedX];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * l5 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * l5 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLB[triangle] - colour) * l5 >> 16);
            }
        }
        if(movedY >= 50)
        {
            xPosition[counter] = vertexScreenX[y];
            yPosition[counter] = vertexScreenY[y];
            zPosition[counter++] = triangleHSLB[triangle];
        } else
        {
            int movedX2 = vertexMovedX[y];
            int movedY2 = vertexMovedY[y];
            int colour = triangleHSLB[triangle];
            if(movedX >= 50)
            {
                int i6 = (50 - movedY) * modelIntArray4[movedX - movedY];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * i6 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * i6 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLA[triangle] - colour) * i6 >> 16);
            }
            if(movedZ >= 50)
            {
                int j6 = (50 - movedY) * modelIntArray4[movedZ - movedY];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[z] - movedX2) * j6 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[z] - movedY2) * j6 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLC[triangle] - colour) * j6 >> 16);
            }
        }
        if(movedZ >= 50)
        {
            xPosition[counter] = vertexScreenX[z];
            yPosition[counter] = vertexScreenY[z];
            zPosition[counter++] = triangleHSLC[triangle];
        } else
        {
            int movedX2 = vertexMovedX[z];
            int movedY2 = vertexMovedY[z];
            int colour = triangleHSLC[triangle];
            if(movedY >= 50)
            {
                int k6 = (50 - movedZ) * modelIntArray4[movedY - movedZ];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[y] - movedX2) * k6 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[y] - movedY2) * k6 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLB[triangle] - colour) * k6 >> 16);
            }
            if(movedX >= 50)
            {
                int l6 = (50 - movedZ) * modelIntArray4[movedX - movedZ];
                xPosition[counter] = centreX + (movedX2 + ((vertexMovedX[x] - movedX2) * l6 >> 16) << 9) / 50;
                yPosition[counter] = centreY + (movedY2 + ((vertexMovedY[x] - movedY2) * l6 >> 16) << 9) / 50;
                zPosition[counter++] = colour + ((triangleHSLA[triangle] - colour) * l6 >> 16);
            }
        }
        int xA = xPosition[0];
        int xB = xPosition[1];
        int xC = xPosition[2];
        int yA = yPosition[0];
        int yB = yPosition[1];
        int yC = yPosition[2];
        if((xA - xB) * (yC - yB) - (yA - yB) * (xC - xB) > 0)
        {
            Texture.restrictEdges = false;
            if(counter == 3)
            {
                if(xA < 0 || xB < 0 || xC < 0 || xA > DrawingArea.centerX || xB > DrawingArea.centerX || xC > DrawingArea.centerX)
                    Texture.restrictEdges = true;
                int drawType;
                if(triangleDrawType == null)
                    drawType = 0;
                else
                    drawType = triangleDrawType[triangle] & 3;
                if(drawType == 0)
                    Texture.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
                else
                if(drawType == 1)
                    Texture.drawFlatTriangle(yA, yB, yC, xA, xB, xC, HSLtoRGB[triangleHSLA[triangle]]);
                else
                if(drawType == 2)
                {
                    int tri = triangleDrawType[triangle] >> 2;
                    int x2 = texturedTrianglePointsX[tri];
                    int y2 = texturedTrianglePointsY[tri];
                    int z2 = texturedTrianglePointsZ[tri];
                    Texture.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                } else
                if(drawType == 3)
                {
                    int tri = triangleDrawType[triangle] >> 2;
                    int x2 = texturedTrianglePointsX[tri];
                    int y2 = texturedTrianglePointsY[tri];
                    int z2 = texturedTrianglePointsZ[tri];
                    Texture.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, triangleHSLA[triangle], triangleHSLA[triangle], triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                }
            }
            if(counter == 4)
            {
                if(xA < 0 || xB < 0 || xC < 0 || xA > DrawingArea.centerX || xB > DrawingArea.centerX || xC > DrawingArea.centerX || xPosition[3] < 0 || xPosition[3] > DrawingArea.centerX)
                    Texture.restrictEdges = true;
                int drawType;
                if(triangleDrawType == null)
                    drawType = 0;
                else
                    drawType = triangleDrawType[triangle] & 3;
                if(drawType == 0)
                {
                    Texture.drawShadedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2]);
                    Texture.drawShadedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], zPosition[0], zPosition[2], zPosition[3]);
                    return;
                }
                if(drawType == 1)
                {
                    int colour = HSLtoRGB[triangleHSLA[triangle]];
                    Texture.drawFlatTriangle(yA, yB, yC, xA, xB, xC, colour);
                    Texture.drawFlatTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], colour);
                    return;
                }
                if(drawType == 2)
                {
                    int tri = triangleDrawType[triangle] >> 2;
                    int x2 = texturedTrianglePointsX[tri];
                    int y2 = texturedTrianglePointsY[tri];
                    int z2 = texturedTrianglePointsZ[tri];
                    Texture.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, zPosition[0], zPosition[1], zPosition[2], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                    Texture.drawTexturedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], zPosition[0], zPosition[2], zPosition[3], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                    return;
                }
                if(drawType == 3)
                {
                    int tri = triangleDrawType[triangle] >> 2;
                    int x2 = texturedTrianglePointsX[tri];
                    int y2 = texturedTrianglePointsY[tri];
                    int z2 = texturedTrianglePointsZ[tri];
                    Texture.drawTexturedTriangle(yA, yB, yC, xA, xB, xC, triangleHSLA[triangle], triangleHSLA[triangle], triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                    Texture.drawTexturedTriangle(yA, yC, yPosition[3], xA, xC, xPosition[3], triangleHSLA[triangle], triangleHSLA[triangle], triangleHSLA[triangle], vertexMovedX[x2], vertexMovedX[y2], vertexMovedX[z2], vertexMovedY[x2], vertexMovedY[y2], vertexMovedY[z2], vertexMovedZ[x2], vertexMovedZ[y2], vertexMovedZ[z2], triangleColours[triangle]);
                }
            }
        }
    }

    private boolean method486(int i, int j, int k, int l, int i1, int j1, int k1,
            int l1)
    {
        if(j < k && j < l && j < i1)
            return false;
        if(j > k && j > l && j > i1)
            return false;
        return !(i < j1 && i < k1 && i < l1) && (i <= j1 || i <= k1 || i <= l1);
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
    private int anInt1641;
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
    VertexNormal vertexNormalOffset[];
    private static ModelHeader[] modelHeaders;
    private static OnDemandFetcherParent requester;
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
    public static int anInt1687;
    public static final int[] anIntArray1688 = new int[1000];
    public static int SINE[];
    public static int COSINE[];
    private static int[] HSLtoRGB;
    private static int[] modelIntArray4;

    static 
    {
        SINE = Texture.anIntArray1470;
        COSINE = Texture.anIntArray1471;
        HSLtoRGB = Texture.anIntArray1482;
        modelIntArray4 = Texture.anIntArray1469;
    }
}
