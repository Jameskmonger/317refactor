package com.jagex.runescape.audio;

/*
 * Some of this file was refactored by 'veer' of http://www.moparscape.org.
 */

import com.jagex.runescape.Buffer;

final class Envelope {

    private int phaseCount;

    private int[] phaseDuration;

    private int[] phasePeak;

    int start;

    int end;

    int form;
    private int critical;
    private int phaseId;
    private int step;
    private int amplitude;
    private int ticks;

    public void decode(final Buffer stream) {
        this.form = stream.getUnsignedByte();
        this.start = stream.getInt();
        this.end = stream.getInt();
        this.decodeShape(stream);
    }

    public void decodeShape(final Buffer stream) {
        this.phaseCount = stream.getUnsignedByte();
        this.phaseDuration = new int[this.phaseCount];
        this.phasePeak = new int[this.phaseCount];
        for (int p = 0; p < this.phaseCount; p++) {
            this.phaseDuration[p] = stream.getUnsignedLEShort();
            this.phasePeak[p] = stream.getUnsignedLEShort();
        }

    }

    void resetValues() {
        this.critical = 0;
        this.phaseId = 0;
        this.step = 0;
        this.amplitude = 0;
        this.ticks = 0;
    }

    int step(final int period) {
        if (this.ticks >= this.critical) {
            this.amplitude = this.phasePeak[this.phaseId++] << 15;
            if (this.phaseId >= this.phaseCount) {
                this.phaseId = this.phaseCount - 1;
            }
            this.critical = (int) ((this.phaseDuration[this.phaseId] / 65536D) * period);
            if (this.critical > this.ticks) {
                this.step = ((this.phasePeak[this.phaseId] << 15) - this.amplitude) / (this.critical - this.ticks);
            }
        }
        this.amplitude += this.step;
        this.ticks++;
        return this.amplitude - this.step >> 15;
    }
}
