package com.jagex.runescape.audio;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

/* 
 * This file was renamed as part of the 317refactor project.
 */

/*
 * Some of this file was refactored by 'veer' of http://www.moparscape.org.
 */

import com.jagex.runescape.Stream;

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
	public static int anInt546;
	public Envelope() {
	}
	public void decode(Stream stream) {
		form = stream.getUnsignedByte();
		start = stream.getInt();
		end = stream.getInt();
		decodeShape(stream);
	}
	public void decodeShape(Stream stream) {
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
			if (phaseId >= phaseCount)
				phaseId = phaseCount - 1;
			critical = (int) ((phaseDuration[phaseId] / 65536D) * period);
			if (critical > ticks)
				step = ((phasePeak[phaseId] << 15) - amplitude)
						/ (critical - ticks);
		}
		amplitude += step;
		ticks++;
		return amplitude - step >> 15;
	}
}
