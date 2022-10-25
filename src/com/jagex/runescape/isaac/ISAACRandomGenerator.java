package com.jagex.runescape.isaac;

/*
 * An implementation of the ISAAC cipher
 *
 * Based on Rand.java by Bob Jenkins
 * http://burtleburtle.net/bob/java/rand/Rand.java
 */

public final class ISAACRandomGenerator {

    /**
     * log of size of results[] and memory[]
     */
    private final static int SIZEL = 8;

    /**
     * size of results[] and memory[]
     */
    private final static int SIZE = 1 << SIZEL;

    /**
     * for pseudorandom lookup
     */
    private final static int MASK = (SIZE - 1) << 2;

    private int count;
    private final int[] results;
    private final int[] memory;
    private int accumulator;
    private int lastResult;
    private int counter;

    /**
     * Create a new ISAAC generator with a given seed.
     *
     * @param seed The seed.
     */
    public ISAACRandomGenerator(final int[] seed) {
        this.memory = new int[SIZE];
        this.results = new int[SIZE];
        System.arraycopy(seed, 0, this.results, 0, seed.length);
        this.initialise();
    }

    /**
     * Initialise or reinitialise the generator.
     */
    private void initialise() {
        int a, b, c, d, e, f, g, h;
        a = b = c = d = e = f = g = h = 0x9e3779b9;
        for (int i = 0; i < 4; i++) {
            a ^= b << 11;
            d += a;
            b += c;
            b ^= c >>> 2;
            e += b;
            c += d;
            c ^= d << 8;
            f += c;
            d += e;
            d ^= e >>> 16;
            g += d;
            e += f;
            e ^= f << 10;
            h += e;
            f += g;
            f ^= g >>> 4;
            a += f;
            g += h;
            g ^= h << 8;
            b += g;
            h += a;
            h ^= a >>> 9;
            c += h;
            a += b;
        }

        for (int i = 0; i < SIZE; i += 8) {
            a += this.results[i];
            b += this.results[i + 1];
            c += this.results[i + 2];
            d += this.results[i + 3];
            e += this.results[i + 4];
            f += this.results[i + 5];
            g += this.results[i + 6];
            h += this.results[i + 7];
            a ^= b << 11;
            d += a;
            b += c;
            b ^= c >>> 2;
            e += b;
            c += d;
            c ^= d << 8;
            f += c;
            d += e;
            d ^= e >>> 16;
            g += d;
            e += f;
            e ^= f << 10;
            h += e;
            f += g;
            f ^= g >>> 4;
            a += f;
            g += h;
            g ^= h << 8;
            b += g;
            h += a;
            h ^= a >>> 9;
            c += h;
            a += b;
            this.memory[i] = a;
            this.memory[i + 1] = b;
            this.memory[i + 2] = c;
            this.memory[i + 3] = d;
            this.memory[i + 4] = e;
            this.memory[i + 5] = f;
            this.memory[i + 6] = g;
            this.memory[i + 7] = h;
        }

        for (int i = 0; i < SIZE; i += 8) {
            a += this.memory[i];
            b += this.memory[i + 1];
            c += this.memory[i + 2];
            d += this.memory[i + 3];
            e += this.memory[i + 4];
            f += this.memory[i + 5];
            g += this.memory[i + 6];
            h += this.memory[i + 7];
            a ^= b << 11;
            d += a;
            b += c;
            b ^= c >>> 2;
            e += b;
            c += d;
            c ^= d << 8;
            f += c;
            d += e;
            d ^= e >>> 16;
            g += d;
            e += f;
            e ^= f << 10;
            h += e;
            f += g;
            f ^= g >>> 4;
            a += f;
            g += h;
            g ^= h << 8;
            b += g;
            h += a;
            h ^= a >>> 9;
            c += h;
            a += b;
            this.memory[i] = a;
            this.memory[i + 1] = b;
            this.memory[i + 2] = c;
            this.memory[i + 3] = d;
            this.memory[i + 4] = e;
            this.memory[i + 5] = f;
            this.memory[i + 6] = g;
            this.memory[i + 7] = h;
        }

        this.isaac();
        this.count = SIZE;
    }

    /**
     * Generate 256 random results.
     */
    private void isaac() {
        int a, b, x, y;

        this.lastResult += ++this.counter;
        for (a = 0, b = SIZE / 2; a < SIZE / 2; ) {
            x = this.memory[a];
            this.accumulator ^= this.accumulator << 13;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator >>> 6;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator << 2;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator >>> 16;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;
        }

        for (b = 0; b < SIZE / 2; ) {
            x = this.memory[a];
            this.accumulator ^= this.accumulator << 13;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator >>> 6;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator << 2;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;

            x = this.memory[a];
            this.accumulator ^= this.accumulator >>> 16;
            this.accumulator += this.memory[b++];
            this.memory[a] = y = this.memory[(x & MASK) >> 2] + this.accumulator + this.lastResult;
            this.results[a++] = this.lastResult = this.memory[((y >> SIZEL) & MASK) >> 2] + x;
        }
    }

    /**
     * Get a random value.
     *
     * @return A random value
     */
    public int value() {
        if (this.count-- == 0) {
            this.isaac();
            this.count = (SIZE - 1);
        }
        return this.results[this.count];
    }
}