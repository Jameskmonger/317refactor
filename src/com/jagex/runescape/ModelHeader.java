package com.jagex.runescape;

final class ModelHeader {

    public byte[] modelData;

    public int vertexCount;
    public int triangleCount;
    public int texturedTriangleCount;
    public int vertexDirectionOffset;
    public int dataOffsetX;
    public int dataOffsetY;
    public int dataOffsetZ;
    public int vertexSkinOffset;
    public int triangleDataOffset;
    public int triangleTypeOffset;
    public int colourDataOffset;
    public int texturePointerOffset;
    public int trianglePriorityOffset;
    public int triangleAlphaOffset;
    public int triangleSkinOffset;
    public int texturedTriangleOffset;

    public ModelHeader() {
    }
}
