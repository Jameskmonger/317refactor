package com.jagex.runescape.audio;

/*
 * Some of this file was refactored by 'veer' of http://www.moparscape.org.
 */

import com.jagex.runescape.Buffer;

final class Instrument {

    public static void initialise() {
        noise = new int[32768];
        for (int noiseId = 0; noiseId < 32768; noiseId++) {
            if (Math.random() > 0.5D) {
                noise[noiseId] = 1;
            } else {
                noise[noiseId] = -1;
            }
        }

        sine = new int[32768];
        for (int sineId = 0; sineId < 32768; sineId++) {
            sine[sineId] = (int) (Math.sin(sineId / 5215.1903000000002D) * 16384D);
        }

        output = new int[0x35d54];
    }

    /*
     * an impl of a wavetable synthesizer that generates square/sine/saw/noise/flat
     * tables and supports phase and amplitude modulation
     *
     * more: http://musicdsp.org/files/Wavetable-101.pdf
     */

    private Envelope pitchEnvelope;

    private Envelope volumeEnvelope;

    private Envelope pitchModulationEnvelope;

    private Envelope pitchModulationAmplitudeEnvelope;

    private Envelope volumeModulationEnvelope;
    private Envelope volumeModulationAmplitude;
    private Envelope gatingReleaseEnvelope;
    private Envelope gatingAttackEnvelope;
    private final int[] oscillationVolume;
    private final int[] oscillationPitch;
    private final int[] oscillationDelay;
    private int delayTime;
    private int delayFeedback;
    private SoundFilter filter;
    private Envelope filterEnvelope;
    int duration;
    int begin;
    private static int[] output;
    private static int[] noise;
    private static int[] sine;
    private static final int[] phases = new int[5];
    private static final int[] delays = new int[5];
    private static final int[] volumeStep = new int[5];
    private static final int[] pitchStep = new int[5];
    private static final int[] pitchBaseStep = new int[5];

    public Instrument() {
        this.oscillationVolume = new int[5];
        this.oscillationPitch = new int[5];
        this.oscillationDelay = new int[5];
        this.delayFeedback = 100;
        this.duration = 500;
    }

    public void decode(final Buffer stream) {
        this.pitchEnvelope = new Envelope();
        this.pitchEnvelope.decode(stream);
        this.volumeEnvelope = new Envelope();
        this.volumeEnvelope.decode(stream);
        int option = stream.getUnsignedByte();
        if (option != 0) {
            stream.position--;
            this.pitchModulationEnvelope = new Envelope();
            this.pitchModulationEnvelope.decode(stream);
            this.pitchModulationAmplitudeEnvelope = new Envelope();
            this.pitchModulationAmplitudeEnvelope.decode(stream);
        }
        option = stream.getUnsignedByte();
        if (option != 0) {
            stream.position--;
            this.volumeModulationEnvelope = new Envelope();
            this.volumeModulationEnvelope.decode(stream);
            this.volumeModulationAmplitude = new Envelope();
            this.volumeModulationAmplitude.decode(stream);
        }
        option = stream.getUnsignedByte();
        if (option != 0) {
            stream.position--;
            this.gatingReleaseEnvelope = new Envelope();
            this.gatingReleaseEnvelope.decode(stream);
            this.gatingAttackEnvelope = new Envelope();
            this.gatingAttackEnvelope.decode(stream);
        }
        for (int oscillationId = 0; oscillationId < 10; oscillationId++) {
            final int volume = stream.getSmartB();
            if (volume == 0) {
                break;
            }
            this.oscillationVolume[oscillationId] = volume;
            this.oscillationPitch[oscillationId] = stream.getSmartA();
            this.oscillationDelay[oscillationId] = stream.getSmartB();
        }

        this.delayTime = stream.getSmartB();
        this.delayFeedback = stream.getSmartB();
        this.duration = stream.getUnsignedLEShort();
        this.begin = stream.getUnsignedLEShort();
        this.filter = new SoundFilter();
        this.filterEnvelope = new Envelope();
        this.filter.decode(stream, this.filterEnvelope);
    }

    private int evaluateWave(final int amplitude, final int phase, final int table) {
        if (table == 1) {
            if ((phase & 0x7fff) < 16384) {
                return amplitude;
            } else {
                return -amplitude;
            }
        }
        if (table == 2) {
            return sine[phase & 0x7fff] * amplitude >> 14;
        }
        if (table == 3) {
            return ((phase & 0x7fff) * amplitude >> 14) - amplitude;
        }
        if (table == 4) {
            return noise[phase / 2607 & 0x7fff] * amplitude;
        } else {
            return 0;
        }
    }

    public int[] synthesise(final int steps, final int j) {
        for (int position = 0; position < steps; position++) {
            output[position] = 0;
        }

        if (j < 10) {
            return output;
        }
        final double d = steps / (j + 0.0D);
        this.pitchEnvelope.resetValues();
        this.volumeEnvelope.resetValues();
        int pitchModulationStep = 0;
        int pitchModulationBaseStep = 0;
        int pitchModulationPhase = 0;
        if (this.pitchModulationEnvelope != null) {
            this.pitchModulationEnvelope.resetValues();
            this.pitchModulationAmplitudeEnvelope.resetValues();
            pitchModulationStep = (int) (((this.pitchModulationEnvelope.end - this.pitchModulationEnvelope.start)
                * 32.768000000000001D) / d);
            pitchModulationBaseStep = (int) ((this.pitchModulationEnvelope.start * 32.768000000000001D) / d);
        }
        int volumeModulationStep = 0;
        int volumeModulationBaseStep = 0;
        int volumeModulationPhase = 0;
        if (this.volumeModulationEnvelope != null) {
            this.volumeModulationEnvelope.resetValues();
            this.volumeModulationAmplitude.resetValues();
            volumeModulationStep = (int) (((this.volumeModulationEnvelope.end - this.volumeModulationEnvelope.start)
                * 32.768000000000001D) / d);
            volumeModulationBaseStep = (int) ((this.volumeModulationEnvelope.start * 32.768000000000001D) / d);
        }
        for (int oscillationVolumeId = 0; oscillationVolumeId < 5; oscillationVolumeId++) {
            if (this.oscillationVolume[oscillationVolumeId] != 0) {
                phases[oscillationVolumeId] = 0;
                delays[oscillationVolumeId] = (int) (this.oscillationDelay[oscillationVolumeId] * d);
                volumeStep[oscillationVolumeId] = (this.oscillationVolume[oscillationVolumeId] << 14) / 100;
                pitchStep[oscillationVolumeId] = (int) (((this.pitchEnvelope.end - this.pitchEnvelope.start) * 32.768000000000001D
                    * Math.pow(1.0057929410678534D, this.oscillationPitch[oscillationVolumeId])) / d);
                pitchBaseStep[oscillationVolumeId] = (int) ((this.pitchEnvelope.start * 32.768000000000001D) / d);
            }
        }

        for (int offset = 0; offset < steps; offset++) {
            int pitchChange = this.pitchEnvelope.step(steps);
            int volumeChange = this.volumeEnvelope.step(steps);
            if (this.pitchModulationEnvelope != null) {
                final int modulation = this.pitchModulationEnvelope.step(steps);
                final int modulationAmplitude = this.pitchModulationAmplitudeEnvelope.step(steps);
                pitchChange += this.evaluateWave(modulationAmplitude, pitchModulationPhase,
                    this.pitchModulationEnvelope.form) >> 1;
                pitchModulationPhase += (modulation * pitchModulationStep >> 16) + pitchModulationBaseStep;
            }
            if (this.volumeModulationEnvelope != null) {
                final int modulation = this.volumeModulationEnvelope.step(steps);
                final int modulationAmplitude = this.volumeModulationAmplitude.step(steps);
                volumeChange = volumeChange * ((this.evaluateWave(modulationAmplitude, volumeModulationPhase,
                    this.volumeModulationEnvelope.form) >> 1) + 32768) >> 15;
                volumeModulationPhase += (modulation * volumeModulationStep >> 16) + volumeModulationBaseStep;
            }
            for (int oscillationId = 0; oscillationId < 5; oscillationId++) {
                if (this.oscillationVolume[oscillationId] != 0) {
                    final int position = offset + delays[oscillationId];
                    if (position < steps) {
                        output[position] += this.evaluateWave(volumeChange * volumeStep[oscillationId] >> 15,
                            phases[oscillationId], this.pitchEnvelope.form);
                        phases[oscillationId] += (pitchChange * pitchStep[oscillationId] >> 16)
                            + pitchBaseStep[oscillationId];
                    }
                }
            }

        }

        if (this.gatingReleaseEnvelope != null) {
            this.gatingReleaseEnvelope.resetValues();
            this.gatingAttackEnvelope.resetValues();
            int counter = 0;
            boolean muted = true;
            for (int position = 0; position < steps; position++) {
                final int stepOn = this.gatingReleaseEnvelope.step(steps);
                final int stepOff = this.gatingAttackEnvelope.step(steps);
                final int threshold;
                if (muted) {
                    threshold = this.gatingReleaseEnvelope.start
                        + ((this.gatingReleaseEnvelope.end - this.gatingReleaseEnvelope.start) * stepOn >> 8);
                } else {
                    threshold = this.gatingReleaseEnvelope.start
                        + ((this.gatingReleaseEnvelope.end - this.gatingReleaseEnvelope.start) * stepOff >> 8);
                }
                if ((counter += 256) >= threshold) {
                    counter = 0;
                    muted = !muted;
                }
                if (muted) {
                    output[position] = 0;
                }
            }

        }
        if (this.delayTime > 0 && this.delayFeedback > 0) {
            final int delay = (int) (this.delayTime * d);
            for (int position = delay; position < steps; position++) {
                output[position] += (output[position - delay] * this.delayFeedback) / 100;
            }

        }
        if (this.filter.pairCount[0] > 0 || this.filter.pairCount[1] > 0) {
            this.filterEnvelope.resetValues();
            int t = this.filterEnvelope.step(steps + 1);
            int M = this.filter.compute(0, t / 65536F);
            int N = this.filter.compute(1, t / 65536F);
            if (steps >= M + N) {
                int n = 0;
                int delay = N;
                if (delay > steps - M) {
                    delay = steps - M;
                }
                for (; n < delay; n++) {
                    int y = (int) ((long) output[n + M] * (long) SoundFilter.invUnity >> 16);
                    for (int k8 = 0; k8 < M; k8++) {
                        y += (int) ((long) output[(n + M) - 1 - k8] * (long) SoundFilter.coefficient[0][k8] >> 16);
                    }

                    for (int j9 = 0; j9 < n; j9++) {
                        y -= (int) ((long) output[n - 1 - j9] * (long) SoundFilter.coefficient[1][j9] >> 16);
                    }

                    output[n] = y;
                    t = this.filterEnvelope.step(steps + 1);
                }

                final char offset = '\200'; // 128
                delay = offset;
                do {
                    if (delay > steps - M) {
                        delay = steps - M;
                    }
                    for (; n < delay; n++) {
                        int y = (int) ((long) output[n + M] * (long) SoundFilter.invUnity >> 16);
                        for (int position = 0; position < M; position++) {
                            y += (int) ((long) output[(n + M) - 1 - position]
                                * (long) SoundFilter.coefficient[0][position] >> 16);
                        }

                        for (int position = 0; position < N; position++) {
                            y -= (int) ((long) output[n - 1 - position]
                                * (long) SoundFilter.coefficient[1][position] >> 16);
                        }

                        output[n] = y;
                        t = this.filterEnvelope.step(steps + 1);
                    }

                    if (n >= steps - M) {
                        break;
                    }
                    M = this.filter.compute(0, t / 65536F);
                    N = this.filter.compute(1, t / 65536F);
                    delay += offset;
                } while (true);
                for (; n < steps; n++) {
                    int y = 0;
                    for (int position = (n + M) - steps; position < M; position++) {
                        y += (int) ((long) output[(n + M) - 1 - position]
                            * (long) SoundFilter.coefficient[0][position] >> 16);
                    }

                    for (int position = 0; position < N; position++) {
                        y -= (int) ((long) output[n - 1 - position]
                            * (long) SoundFilter.coefficient[1][position] >> 16);
                    }

                    output[n] = y;
                }
            }
        }
        for (int position = 0; position < steps; position++) {
            if (output[position] < -32768) {
                output[position] = -32768;
            }
            if (output[position] > 32767) {
                output[position] = 32767;
            }
        }

        return output;
    }
}