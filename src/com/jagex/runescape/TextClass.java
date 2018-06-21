package com.jagex.runescape;

import com.jagex.runescape.sign.signlink;

final class TextClass {

	public static String asterisksForString(String string) {
		StringBuffer asterisks = new StringBuffer();
		for (int c = 0; c < string.length(); c++)
			asterisks.append("*");
		return asterisks.toString();
	}

	public static String decodeDNS(int address) {
		return (address >> 24 & 0xff) + "." + (address >> 16 & 0xff) + "." + (address >> 8 & 0xff) + "."
				+ (address & 0xff);
	}

	public static String formatName(String name) {
		if (name.length() > 0) {
			char characters[] = name.toCharArray();
			for (int c = 0; c < characters.length; c++)
				if (characters[c] == '_') {
					characters[c] = ' ';
					if (c + 1 < characters.length && characters[c + 1] >= 'a' && characters[c + 1] <= 'z')
						characters[c + 1] = (char) ((characters[c + 1] + 65) - 97);
				}

			if (characters[0] >= 'a' && characters[0] <= 'z')
				characters[0] = (char) ((characters[0] + 65) - 97);
			return new String(characters);
		} else {
			return name;
		}
	}

	public static String longToName(long longName) {
		try {
			if (longName <= 0L || longName >= 0x5b5b57f8a98a5dd1L)
				return "invalid_name";
			if (longName % 37L == 0L)
				return "invalid_name";
			int i = 0;
			char name[] = new char[12];
			while (longName != 0L) {
				long n = longName;
				longName /= 37L;
				name[11 - i++] = VALID_CHARACTERS[(int) (n - longName * 37L)];
			}
			return new String(name, 12 - i, i);
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("81570, " + longName + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public static long nameToLong(String name) {
		long longName = 0L;
		for (int c = 0; c < name.length() && c < 12; c++) {
			char character = name.charAt(c);
			longName *= 37L;
			if (character >= 'A' && character <= 'Z')
				longName += (1 + character) - 65;
			else if (character >= 'a' && character <= 'z')
				longName += (1 + character) - 97;
			else if (character >= '0' && character <= '9')
				longName += (27 + character) - 48;
		}

		for (; longName % 37L == 0L && longName != 0L; longName /= 37L)
			;
		return longName;
	}

	public static long spriteNameToHash(String spriteName) {
		spriteName = spriteName.toUpperCase();
		long longSpriteName = 0L;
		for (int c = 0; c < spriteName.length(); c++) {
			longSpriteName = (longSpriteName * 61L + spriteName.charAt(c)) - 32L;
			longSpriteName = longSpriteName + (longSpriteName >> 56) & 0xffffffffffffffL;
		}
		return longSpriteName;
	}

	private static final char[] VALID_CHARACTERS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9' };

}
