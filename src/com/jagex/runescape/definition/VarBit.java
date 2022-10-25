package com.jagex.runescape.definition;

import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;

public final class VarBit {

    public static VarBit[] values;
    public int configId;
    public int leastSignificantBit;
    public int mostSignificantBit;

    public static void load(final Archive archive) {
        final Buffer buffer = new Buffer(archive.decompressFile("varbit.dat"));
        final int count = buffer.getUnsignedLEShort();

        if (values == null) {
            values = new VarBit[count];
        }

        for (int i = 0; i < count; i++) {
            if (values[i] == null) {
                values[i] = new VarBit();
            }

            values[i].load(buffer);
        }

        if (buffer.position != buffer.buffer.length) {
            System.out.println("varbit load mismatch");
        }
    }

    private void load(final Buffer buffer) {
        do {
            final int opcode = buffer.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                this.configId = buffer.getUnsignedLEShort();
                this.leastSignificantBit = buffer.getUnsignedByte();
                this.mostSignificantBit = buffer.getUnsignedByte();
            } else if (opcode == 10) {
                buffer.getString();
            } else if (opcode == 2) {
            } // dummy
            else if (opcode == 3) {
                buffer.getInt();
            } else if (opcode == 4) {
                buffer.getInt();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }
}
