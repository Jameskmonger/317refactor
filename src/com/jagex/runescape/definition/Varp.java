package com.jagex.runescape.definition;

import com.jagex.runescape.Archive;
import com.jagex.runescape.Buffer;

public final class Varp {

    public static Varp[] values;
    public int type;

    public static void load(final Archive archive) {
        final Buffer stream = new Buffer(archive.decompressFile("varp.dat"));
        final int count = stream.getUnsignedLEShort();

        if (values == null) {
            values = new Varp[count];
        }

        for (int i = 0; i < count; i++) {
            if (values[i] == null) {
                values[i] = new Varp();
            }

            values[i].load(stream);
        }

        if (stream.position != stream.buffer.length) {
            System.out.println("varptype load mismatch");
        }
    }

    private void load(final Buffer buffer) {
        do {
            final int opcode = buffer.getUnsignedByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                buffer.getUnsignedByte();
            } else if (opcode == 2) {
                buffer.getUnsignedByte();
            } else if (opcode == 3) {
            } // dummy
            else if (opcode == 4) {
            } // dummy
            else if (opcode == 5) {
                this.type = buffer.getUnsignedLEShort();
            } else if (opcode == 6) {
            } // dummy
            else if (opcode == 7) {
                buffer.getInt();
            } else if (opcode == 8) {
            } // dummy
            else if (opcode == 10) {
                buffer.getString();
            } else if (opcode == 11) {
            } // dummy
            else if (opcode == 12) {
                buffer.getInt();
            } else if (opcode == 13) {
            } // dummy
            else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }

}
