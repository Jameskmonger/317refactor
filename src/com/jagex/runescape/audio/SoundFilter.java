package com.jagex.runescape.audio;

/*
 * Some of this file was refactored by 'veer' of http://www.moparscape.org.
 */

import com.jagex.runescape.Buffer;

/*
 * an impl of an adaptive iir filter that calculates
 * coefficients from pole magnitude/phases and a serial
 * configuration of cascading biquad sections
 */
final class SoundFilter {

    final int[] pairCount;

    private final int[][][] pairPhase;

    private final int[][][] pairMagnitude;

    private final int[] unity;

    private static final float[][] _coefficient = new float[2][8];

    static final int[][] coefficient = new int[2][8];

    private static float _invUnity;
    static int invUnity;

    public SoundFilter() {
        this.pairCount = new int[2];
        this.pairPhase = new int[2][2][4];
        this.pairMagnitude = new int[2][2][4];
        this.unity = new int[2];
    }

    private float adaptMagnitude(final int direction, final int i, final float f) {
        float alpha = this.pairMagnitude[direction][0][i]
            + f * (this.pairMagnitude[direction][1][i] - this.pairMagnitude[direction][0][i]);
        alpha *= 0.001525879F;
        return 1.0F - (float) Math.pow(10D, -alpha / 20F);
    }

    private float adaptPhase(final float f, final int i, final int direction) {
        float alpha = this.pairPhase[direction][0][i] + f * (this.pairPhase[direction][1][i] - this.pairPhase[direction][0][i]);
        alpha *= 0.0001220703F;
        return this.normalise(alpha);
    }

    /* dir: 0 -> feedforward, 1 -> feedback */
    public int compute(final int direction, final float f) {
        if (direction == 0) {
            float f1 = this.unity[0] + (this.unity[1] - this.unity[0]) * f;
            f1 *= 0.003051758F;
            _invUnity = (float) Math.pow(0.10000000000000001D, f1 / 20F);
            invUnity = (int) (_invUnity * 65536F);
        }
        if (this.pairCount[direction] == 0) {
            return 0;
        }
        final float _mag = this.adaptMagnitude(direction, 0, f);
        _coefficient[direction][0] = -2F * _mag * (float) Math.cos(this.adaptPhase(f, 0, direction));
        _coefficient[direction][1] = _mag * _mag;
        for (int pair = 1; pair < this.pairCount[direction]; pair++) {
            final float mag = this.adaptMagnitude(direction, pair, f);
            final float phase = -2F * mag * (float) Math.cos(this.adaptPhase(f, pair, direction));
            final float coeff = mag * mag;
            _coefficient[direction][pair * 2 + 1] = _coefficient[direction][pair * 2 - 1] * coeff;
            _coefficient[direction][pair * 2] = _coefficient[direction][pair * 2 - 1] * phase
                + _coefficient[direction][pair * 2 - 2] * coeff;
            for (int j1 = pair * 2 - 1; j1 >= 2; j1--) {
                _coefficient[direction][j1] += _coefficient[direction][j1 - 1] * phase
                    + _coefficient[direction][j1 - 2] * coeff;
            }

            _coefficient[direction][1] += _coefficient[direction][0] * phase + coeff;
            _coefficient[direction][0] += phase;
        }

        if (direction == 0) {
            for (int pair = 0; pair < this.pairCount[0] * 2; pair++) {
                _coefficient[0][pair] *= _invUnity;
            }

        }
        for (int pair = 0; pair < this.pairCount[direction] * 2; pair++) {
            coefficient[direction][pair] = (int) (_coefficient[direction][pair] * 65536F);
        }

        return this.pairCount[direction] * 2;
    }

    public void decode(final Buffer stream, final Envelope envelope) {
        final int count = stream.getUnsignedByte();
        this.pairCount[0] = count >> 4;
        this.pairCount[1] = count & 0xf;
        if (count != 0) {
            this.unity[0] = stream.getUnsignedLEShort();
            this.unity[1] = stream.getUnsignedLEShort();
            final int migrated = stream.getUnsignedByte();
            for (int direction = 0; direction < 2; direction++) {
                for (int pair = 0; pair < this.pairCount[direction]; pair++) {
                    this.pairPhase[direction][0][pair] = stream.getUnsignedLEShort();
                    this.pairMagnitude[direction][0][pair] = stream.getUnsignedLEShort();
                }

            }

            for (int direction = 0; direction < 2; direction++) {
                for (int pair = 0; pair < this.pairCount[direction]; pair++) {
                    if ((migrated & 1 << direction * 4 << pair) != 0) {
                        this.pairPhase[direction][1][pair] = stream.getUnsignedLEShort();
                        this.pairMagnitude[direction][1][pair] = stream.getUnsignedLEShort();
                    } else {
                        this.pairPhase[direction][1][pair] = this.pairPhase[direction][0][pair];
                        this.pairMagnitude[direction][1][pair] = this.pairMagnitude[direction][0][pair];
                    }
                }

            }

            if (migrated != 0 || this.unity[1] != this.unity[0]) {
                envelope.decodeShape(stream);
            }
        } else {
            this.unity[0] = this.unity[1] = 0;
        }
    }

    private float normalise(final float alpha) {
        final float f = 32.7032F * (float) Math.pow(2D, alpha);
        return (f * 3.141593F) / 11025F;
    }

}
