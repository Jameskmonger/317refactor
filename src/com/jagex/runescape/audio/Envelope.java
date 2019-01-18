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
	
	public void decode(Buffer stream) {
		form = stream.getUnsignedByte();
		start = stream.getInt();
		end = stream.getInt();
		decodeShape(stream);
	}

	public void decodeShape(Buffer stream) {
		phaseCount = stream.getUnsignedByte();
		phaseDuration = new int[phaseCount];
		phasePeak = new int[phaseCount];
		for (int p = 0; p < phaseCount; p++) {
			phaseDuration[p] = stream.getUnsignedLEShort();
			phasePeak[p] = stream.getUnsignedLEShort();
		}

	}

	void resetValues() {
		critical = 0;
		phaseId = 0;
		step = 0;
		amplitude = 0;
		ticks = 0;
	}

	int step(int period) {
		if (ticks >= critical) {
			amplitude = phasePeak[phaseId++] << 15;
			if (phaseId >= phaseCount) {
                phaseId = phaseCount - 1;
            }
			critical = (int) ((phaseDuration[phaseId] / 65536D) * period);
			if (critical > ticks) {
                step = ((phasePeak[phaseId] << 15) - amplitude) / (critical - ticks);
            }
		}
		amplitude += step;
		ticks++;
		return amplitude - step >> 15;
	}
}
