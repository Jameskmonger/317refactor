package com.jagex.runescape;

public final class Skins {

    public final int[] opcodes;

    public final int[][] skinList;

    public Skins(final Buffer buffer) {
        final int count = buffer.getUnsignedByte();
        this.opcodes = new int[count];
        this.skinList = new int[count][];
        for (int opcode = 0; opcode < count; opcode++) {
            this.opcodes[opcode] = buffer.getUnsignedByte();
        }

        for (int skin = 0; skin < count; skin++) {
            final int subSkinAmount = buffer.getUnsignedByte();
            this.skinList[skin] = new int[subSkinAmount];
            for (int subSkin = 0; subSkin < subSkinAmount; subSkin++) {
                this.skinList[skin][subSkin] = buffer.getUnsignedByte();
            }

        }
    }
}
