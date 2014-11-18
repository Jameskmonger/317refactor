package com.jagex.runescape.audio;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
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
 * Some of this file was refactored by 'veer'
 * of http://www.moparscape.org.
 */

import com.jagex.runescape.Stream;

/*
 * an impl of an adaptive iir filter that calculates
 * coefficients from pole magnitude/phases and a serial
 * configuration of cascading biquad sections
 */
final class SoundFilter
{

    private float adaptMagnitude(int direction, int i, float f)
    {
        float alpha = (float)pairMagnitude[direction][0][i] + f * (float)(pairMagnitude[direction][1][i] - pairMagnitude[direction][0][i]);
            alpha *= 0.001525879F;
            return 1.0F - (float)Math.pow(10D, -alpha / 20F);
    }

    private float normalise(float alpha)
    {
        float f = 32.7032F * (float)Math.pow(2D, alpha);
        return (f * 3.141593F) / 11025F;
    }

    private float adaptPhase(float f, int i, int direction)
    {
        float alpha = (float)pairPhase[direction][0][i] + f * (float)(pairPhase[direction][1][i] - pairPhase[direction][0][i]);
        alpha *= 0.0001220703F;
        return normalise(alpha);
    }

    /* dir: 0 -> feedforward, 1 -> feedback */
    public int compute(int direction, float f)
    {
        if(direction == 0)
        {
            float f1 = (float)unity[0] + (float)(unity[1] - unity[0]) * f;
            f1 *= 0.003051758F;
            _invUnity = (float)Math.pow(0.10000000000000001D, f1 / 20F);
            invUnity = (int)(_invUnity * 65536F);
        }
        if(pairCount[direction] == 0)
            return 0;
        float _mag = adaptMagnitude(direction, 0, f);
        _coefficient[direction][0] = -2F * _mag * (float)Math.cos(adaptPhase(f, 0, direction));
        _coefficient[direction][1] = _mag * _mag;
        for(int pair = 1; pair < pairCount[direction]; pair++)
        {
            float mag = adaptMagnitude(direction, pair, f);
            float phase = -2F * mag * (float)Math.cos(adaptPhase(f, pair, direction));
            float coeff = mag * mag;
            _coefficient[direction][pair * 2 + 1] = _coefficient[direction][pair * 2 - 1] * coeff;
            _coefficient[direction][pair * 2] = _coefficient[direction][pair * 2 - 1] * phase + _coefficient[direction][pair * 2 - 2] * coeff;
            for(int j1 = pair * 2 - 1; j1 >= 2; j1--)
                _coefficient[direction][j1] += _coefficient[direction][j1 - 1] * phase + _coefficient[direction][j1 - 2] * coeff;

            _coefficient[direction][1] += _coefficient[direction][0] * phase + coeff;
            _coefficient[direction][0] += phase;
        }

        if(direction == 0)
        {
            for(int pair = 0; pair < pairCount[0] * 2; pair++)
                _coefficient[0][pair] *= _invUnity;

        }
        for(int pair = 0; pair < pairCount[direction] * 2; pair++)
            coefficient[direction][pair] = (int)(_coefficient[direction][pair] * 65536F);

        return pairCount[direction] * 2;
    }

    public void decode(Stream stream, Envelope envelope)
    {
        int count = stream.getUnsignedByte();
        pairCount[0] = count >> 4;
        pairCount[1] = count & 0xf;
        if(count != 0)
        {
            unity[0] = stream.getUnsignedLEShort();
            unity[1] = stream.getUnsignedLEShort();
            int migrated = stream.getUnsignedByte();
            for(int direction = 0; direction < 2; direction++)
            {
                for(int pair = 0; pair < pairCount[direction]; pair++)
                {
                    pairPhase[direction][0][pair] = stream.getUnsignedLEShort();
                    pairMagnitude[direction][0][pair] = stream.getUnsignedLEShort();
                }

            }

            for(int direction = 0; direction < 2; direction++)
            {
                for(int pair = 0; pair < pairCount[direction]; pair++)
                    if((migrated & 1 << direction * 4 << pair) != 0)
                    {
                        pairPhase[direction][1][pair] = stream.getUnsignedLEShort();
                        pairMagnitude[direction][1][pair] = stream.getUnsignedLEShort();
                    } else
                    {
                        pairPhase[direction][1][pair] = pairPhase[direction][0][pair];
                        pairMagnitude[direction][1][pair] = pairMagnitude[direction][0][pair];
                    }

            }

            if(migrated != 0 || unity[1] != unity[0])
                envelope.decodeShape(stream);
        } else
        {
            unity[0] = unity[1] = 0;
        }
    }

    public SoundFilter()
    {
        pairCount = new int[2];
        pairPhase = new int[2][2][4];
        pairMagnitude = new int[2][2][4];
        unity = new int[2];
    }

    final int[] pairCount;
    private final int[][][] pairPhase;
    private final int[][][] pairMagnitude;
    private final int[] unity;
    private static final float[][] _coefficient = new float[2][8];
    static final int[][] coefficient = new int[2][8];
    private static float _invUnity;
    static int invUnity;

}
