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

import com.jagex.runescape.Buffer;

public final class Effect {

	public static Buffer data(int i, int id) {
		if (effects[id] != null) {
			Effect effect = effects[id];
			return effect.encode(i);
		} else {
			return null;
		}
	}

	public static void load(Buffer stream) {
		_output = new byte[0x6baa8];
		output = new Buffer(_output);
		Instrument.initialise();
		do {
			int effect = stream.getUnsignedLEShort();
			if (effect == 65535)
				return;
			effects[effect] = new Effect();
			effects[effect].decode(stream);
			effectDelays[effect] = effects[effect].getDelay();
		} while (true);
	}

	private static final Effect[] effects = new Effect[5000];

	public static final int[] effectDelays = new int[5000];

	private static byte[] _output;

	private static Buffer output;

	private final Instrument[] instruments;

	private int loopStart;
	private int loopEnd;
	private Effect() {
		instruments = new Instrument[10];
	}
	private void decode(Buffer stream) {
		for (int instrument = 0; instrument < 10; instrument++) {
			int active = stream.getUnsignedByte();
			if (active != 0) {
				stream.position--;
				instruments[instrument] = new Instrument();
				instruments[instrument].decode(stream);
			}
		}
		loopStart = stream.getUnsignedLEShort();
		loopEnd = stream.getUnsignedLEShort();
	}
	private Buffer encode(int loops) {
		int size = mix(loops);
		output.position = 0;
		output.putInt(0x52494646);
		output.putLEInt(36 + size);
		output.putInt(0x57415645);
		output.putInt(0x666d7420);
		output.putLEInt(16);
		output.putLEShort(1);
		output.putLEShort(1);
		output.putLEInt(22050);
		output.putLEInt(22050);
		output.putLEShort(1);
		output.putLEShort(8);
		output.putInt(0x64617461);
		output.putLEInt(size);
		output.position += size;
		return output;
	}
	private int getDelay() {
		int delay = 0x98967f;
		for (int instrument = 0; instrument < 10; instrument++)
			if (instruments[instrument] != null
					&& instruments[instrument].begin / 20 < delay)
				delay = instruments[instrument].begin / 20;

		if (loopStart < loopEnd && loopStart / 20 < delay)
			delay = loopStart / 20;
		if (delay == 0x98967f || delay == 0)
			return 0;
		for (int instrument = 0; instrument < 10; instrument++)
			if (instruments[instrument] != null)
				instruments[instrument].begin -= delay * 20;

		if (loopStart < loopEnd) {
			loopStart -= delay * 20;
			loopEnd -= delay * 20;
		}
		return delay;
	}
	private int mix(int loopCount) {
		int _duration = 0;
		for (int instrument = 0; instrument < 10; instrument++)
			if (instruments[instrument] != null
					&& instruments[instrument].duration
							+ instruments[instrument].begin > _duration)
				_duration = instruments[instrument].duration
						+ instruments[instrument].begin;

		if (_duration == 0)
			return 0;
		int stepCount = (22050 * _duration) / 1000;
		int loopStart = (22050 * this.loopStart) / 1000;
		int loopEnd = (22050 * this.loopEnd) / 1000;
		if (loopStart < 0 || loopStart > stepCount || loopEnd < 0
				|| loopEnd > stepCount || loopStart >= loopEnd)
			loopCount = 0;
		int length = stepCount + (loopEnd - loopStart) * (loopCount - 1);
		for (int offset = 44; offset < length + 44; offset++)
			_output[offset] = -128;

		for (int instrument = 0; instrument < 10; instrument++)
			if (instruments[instrument] != null) {
				int duration = (instruments[instrument].duration * 22050) / 1000;
				int offset = (instruments[instrument].begin * 22050) / 1000;
				int samples[] = instruments[instrument].synthesise(duration,
						instruments[instrument].duration);
				for (int sample = 0; sample < duration; sample++)
					_output[sample + offset + 44] += (byte) (samples[sample] >> 8);

			}

		if (loopCount > 1) {
			loopStart += 44;
			loopEnd += 44;
			stepCount += 44;
			int offset = (length += 44) - stepCount;
			for (int step = stepCount - 1; step >= loopEnd; step--)
				_output[step + offset] = _output[step];

			for (int loop = 1; loop < loopCount; loop++) {
				int _offset = (loopEnd - loopStart) * loop;
				System.arraycopy(_output, loopStart, _output, loopStart
						+ _offset, loopEnd - loopStart);

			}

			length -= 44;
		}
		return length;
	}

}
