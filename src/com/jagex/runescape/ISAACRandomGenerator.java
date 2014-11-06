package com.jagex.runescape;

/**
 * Based on Rand.java by Bob Jenkins
 * http://burtleburtle.net/bob/java/rand/Rand.java
 */

public final class ISAACRandomGenerator {

	/**
	 *  log of size of results[] and memory[]
	 */
	final static int SIZEL = 8;
	
	/**
	 *  size of results[] and memory[]
	 */
	final static int SIZE = 1 << SIZEL; 
	
	/**
	 * for pseudorandom lookup
	 */
	final static int MASK = (SIZE - 1) << 2; 
	
	private int count;
	private final int[] results;
	private final int[] memory;
	private int accumulator;
	private int lastResult;
	private int counter;

	/**
	 * Create a new ISAAC generator with a given seed.
	 * @param seed The seed.
	 */
	public ISAACRandomGenerator(int seed[]) {
		memory = new int[SIZE];
		results = new int[SIZE];
		System.arraycopy(seed, 0, results, 0, seed.length);
		initialise();
	}

	/**
	 * Get a random value.
	 * @return A random value
	 */
	public int value() {
		if (count-- == 0) {
			isaac();
			count = (SIZE - 1);
		}
		return results[count];
	}

	/**
	 * Generate 256 random results.
	 */
	public final void isaac() {
		int a, b, x, y;

		lastResult += ++counter;
		for (a = 0, b = SIZE / 2; a < SIZE / 2;) {
			x = memory[a];
			accumulator ^= accumulator << 13;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator >>> 6;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator << 2;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator >>> 16;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;
		}

		for (b = 0; b < SIZE / 2;) {
			x = memory[a];
			accumulator ^= accumulator << 13;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator >>> 6;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator << 2;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;

			x = memory[a];
			accumulator ^= accumulator >>> 16;
			accumulator += memory[b++];
			memory[a] = y = memory[(x & MASK) >> 2] + accumulator + lastResult;
			results[a++] = lastResult = memory[((y >> SIZEL) & MASK) >> 2] + x;
		}
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
			a += results[i];
			b += results[i + 1];
			c += results[i + 2];
			d += results[i + 3];
			e += results[i + 4];
			f += results[i + 5];
			g += results[i + 6];
			h += results[i + 7];
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
			memory[i] = a;
			memory[i + 1] = b;
			memory[i + 2] = c;
			memory[i + 3] = d;
			memory[i + 4] = e;
			memory[i + 5] = f;
			memory[i + 6] = g;
			memory[i + 7] = h;
		}

		for (int i = 0; i < SIZE; i += 8) {
			a += memory[i];
			b += memory[i + 1];
			c += memory[i + 2];
			d += memory[i + 3];
			e += memory[i + 4];
			f += memory[i + 5];
			g += memory[i + 6];
			h += memory[i + 7];
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
			memory[i] = a;
			memory[i + 1] = b;
			memory[i + 2] = c;
			memory[i + 3] = d;
			memory[i + 4] = e;
			memory[i + 5] = f;
			memory[i + 6] = g;
			memory[i + 7] = h;
		}

		isaac();
		count = SIZE;
	}
}