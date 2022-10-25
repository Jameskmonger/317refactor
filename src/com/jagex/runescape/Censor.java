package com.jagex.runescape;

/*
 * Based on the work of Dane from Rune-Server
 *
 * https://www.rune-server.ee/runescape-development/rs2-client/snippets/578391-censor-java.html
 */

class Censor {

    private static int[] fragments;
    private static char[][] bads;
    private static byte[][][] badCombinations;
    private static char[][] domains;
    private static char[][] tlds;
    private static int[] tldTypes;

    private static final String[] WHITELISTED_WORDS = {"cook", "cook's", "cooks", "seeks", "sheet", "woop", "woops", "faq", "noob", "noobs"};
    private static final char[] SPELLED_AT_SYMBOL = {'(', 'a', ')'};
    private static final char[] SPELLED_DOT = {'d', 'o', 't'};
    private static final char[] SPELLED_SLASH = {'s', 'l', 'a', 's', 'h'};

    public static void load(final Archive archive) {
        final Buffer fragmentsenc = new Buffer(archive.decompressFile("fragmentsenc.txt"));
        final Buffer badenc = new Buffer(archive.decompressFile("badenc.txt"));
        final Buffer domainenc = new Buffer(archive.decompressFile("domainenc.txt"));
        final Buffer tldlist = new Buffer(archive.decompressFile("tldlist.txt"));
        load(fragmentsenc, badenc, domainenc, tldlist);
    }

    private static void load(final Buffer fragmentsenc, final Buffer badenc, final Buffer domainenc, final Buffer tldlist) {
        loadBadEnc(badenc);
        loadDomainEnc(domainenc);
        loadFragmentsEnc(fragmentsenc);
        loadTldList(tldlist);
    }

    private static void loadTldList(final Buffer buffer) {
        final int length = buffer.getInt();
        tlds = new char[length][];
        tldTypes = new int[length];

        for (int n = 0; n < length; n++) {
            tldTypes[n] = buffer.getUnsignedByte();

            final char[] string = new char[buffer.getUnsignedByte()];
            for (int k = 0; k < string.length; k++) {
                string[k] = (char) buffer.getUnsignedByte();
            }

            tlds[n] = string;
        }
    }

    private static void loadBadEnc(final Buffer buffer) {
        final int length = buffer.getInt();
        bads = new char[length][];
        badCombinations = new byte[length][][];
        loadBadEnc(buffer, badCombinations, bads);
    }

    private static void loadDomainEnc(final Buffer buffer) {
        final int i = buffer.getInt();
        domains = new char[i][];
        loadDomainEnc(buffer, domains);
    }

    private static void loadFragmentsEnc(final Buffer buffer) {
        fragments = new int[buffer.getInt()];
        for (int i = 0; i < fragments.length; i++) {
            fragments[i] = buffer.getUnsignedShort();
        }
    }

    private static void loadBadEnc(final Buffer buffer, final byte[][][] badCombinations, final char[][] bads) {
        for (int n = 0; n < bads.length; n++) {
            final char[] chars = new char[buffer.getUnsignedByte()];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) buffer.getUnsignedByte();
            }
            bads[n] = chars;

            final byte[][] combo = new byte[buffer.getUnsignedByte()][2];

            for (int l = 0; l < combo.length; l++) {
                combo[l][0] = (byte) buffer.getUnsignedByte();
                combo[l][1] = (byte) buffer.getUnsignedByte();
            }

            if (combo.length > 0) {
                badCombinations[n] = combo;
            }
        }

    }

    private static void loadDomainEnc(final Buffer b, final char[][] domains) {
        for (int n = 0; n < domains.length; n++) {
            final char[] string = new char[b.getUnsignedByte()];
            for (int k = 0; k < string.length; k++) {
                string[k] = (char) b.getUnsignedByte();
            }
            domains[n] = string;
        }

    }

    private static void trimWhitespaces(final char[] chars) {
        int off = 0;

        for (int n = 0; n < chars.length; n++) {
            // allow all ascii, spaces, newlines, tabs, and 2 currency symbols.
            if (isValid(chars[n])) {
                chars[off] = chars[n];
            }
            // if it's bad, replace with a space
            else {
                chars[off] = ' ';
            }

            // increase position only if we are just starting or don't have a
            // space ahead of us.
            if (off == 0 || chars[off] != ' ' || chars[off - 1] != ' ') {
                off++;
            }
        }

        // replace the rest with spaces
        for (int n = off; n < chars.length; n++) {
            chars[n] = ' ';
        }
    }

    private static boolean isValid(final char c) {
        return c >= ' ' && c <= 127 || c == ' ' || c == '\n' || c == '\t' || c == '\243' || c == '\u20AC';
    }

    public static String censor(final String s) {
        char[] chars = s.toCharArray();
        trimWhitespaces(chars);

        final String trimmed = new String(chars).trim();
        chars = trimmed.toLowerCase().toCharArray();

        filterTlds(chars);
        filterBad(chars);
        filterDomains(chars);
        filterNumFragments(chars);

        final String lowercase = trimmed.toLowerCase();

        for (int n = 0; n < WHITELISTED_WORDS.length; n++) {
            for (int index = -1; (index = lowercase.indexOf(WHITELISTED_WORDS[n], index + 1)) != -1; ) {
                final char[] wchars = WHITELISTED_WORDS[n].toCharArray();
                System.arraycopy(wchars, 0, chars, index, wchars.length);
            }
        }

        replaceUppercases(trimmed.toCharArray(), chars);
        formatUppercases(chars);
        return new String(chars).trim();
    }

    private static void replaceUppercases(final char[] from, final char[] to) {
        for (int i = 0; i < from.length; i++) {
            if (to[i] != '*' && isUppercaseAlpha(from[i])) {
                to[i] = from[i];
            }
        }
    }

    private static void formatUppercases(final char[] chars) {
        boolean flag = true;
        for (int n = 0; n < chars.length; n++) {
            final char c = chars[n];

            if (isAlpha(c)) {
                if (flag) {
                    if (isLowercaseAlpha(c)) {
                        flag = false;
                    }
                } else if (isUppercaseAlpha(c)) {
                    chars[n] = (char) ((c + 'a') - 'A');
                }
            } else {
                flag = true;
            }
        }
    }

    private static void filterBad(final char[] chars) {
        for (int iterations = 0; iterations < 2; iterations++) {
            for (int n = bads.length - 1; n >= 0; n--) {
                filterBad(chars, bads[n], badCombinations[n]);
            }
        }
    }

    private static void filterDomains(final char[] chars) {
        final char[] filteredAts = chars.clone();
        filterBad(filteredAts, SPELLED_AT_SYMBOL, null);

        final char[] filteredDots = chars.clone();
        filterBad(filteredDots, SPELLED_DOT, null);

        for (int i = domains.length - 1; i >= 0; i--) {
            filterDomain(chars, domains[i], filteredDots, filteredAts);
        }
    }

    private static void filterDomain(final char[] chars, final char[] domain, final char[] filteredDots, final char[] filteredAts) {
        if (domain.length > chars.length) {
            return;
        }

        int stride;
        for (int start = 0; start <= chars.length - domain.length; start += stride) {
            int end = start;
            int off = 0;
            stride = 1;
            while (end < chars.length) {
                int charLen = 0;
                final char b = chars[end];
                char c = '\0';

                if (end + 1 < chars.length) {
                    c = chars[end + 1];
                }

                if (off < domain.length && (charLen = getEmulatedDomainCharLen(domain[off], b, c)) > 0) {
                    end += charLen;
                    off++;
                    continue;
                }

                if (off == 0) {
                    break;
                }

                if ((charLen = getEmulatedDomainCharLen(domain[off - 1], b, c)) > 0) {
                    end += charLen;
                    if (off == 1) {
                        stride++;
                    }
                    continue;
                }
                if (off >= domain.length || !isSymbol(b)) {
                    break;
                }
                end++;
            }

            if (off >= domain.length) {
                boolean bad = false;
                final int status0 = getDomainAtFilterStatus(start, chars, filteredAts);
                final int status1 = getDomainDotFilterStatus(end - 1, chars, filteredDots);

                if (status0 > 2 || status1 > 2) {
                    bad = true;
                }

                if (bad) {
                    for (int i = start; i < end; i++) {
                        chars[i] = '*';
                    }
                }
            }
        }

    }

    private static int getDomainAtFilterStatus(final int end, final char[] a, final char[] b) {
        // i aint got no type
        if (end == 0) {
            return 2;
        }

        // scan until it finds an @ or a non-symbol
        for (int i = end - 1; i >= 0; i--) {
            if (!isSymbol(a[i])) {
                break;
            }

            if (a[i] == '@') {
                return 3;
            }
        }

        // scan for series of asterisks
        int asteriskCount = 0;
        for (int i = end - 1; i >= 0; i--) {
            if (!isSymbol(b[i])) {
                break;
            }

            if (b[i] == '*') {
                asteriskCount++;
            }
        }

        if (asteriskCount >= 3) {
            return 4;
        }

        // return whether the last char is a symbol or not.
        return !isSymbol(a[end - 1]) ? 0 : 1;
    }

    private static int getDomainDotFilterStatus(final int start, final char[] a, final char[] b) {
        // out of bounds, no type
        if (start + 1 == a.length) {
            return 2;
        }

        // scan until it finds a period or comma or a non-symbol
        for (int i = start + 1; i < a.length; i++) {
            if (!isSymbol(a[i])) {
                break;
            }

            if (a[i] == '.' || a[i] == ',') {
                return 3;
            }
        }

        // scan for series of asterisks
        int asteriskCount = 0;
        for (int i = start + 1; i < a.length; i++) {
            if (!isSymbol(b[i])) {
                break;
            }

            if (b[i] == '*') {
                asteriskCount++;
            }
        }

        if (asteriskCount >= 3) {
            return 4;
        }

        // return whether the first char is a symbol or not
        return !isSymbol(a[start + 1]) ? 0 : 1;
    }

    private static void filterTlds(final char[] chars) {
        final char[] filteredDot = chars.clone();
        filterBad(filteredDot, SPELLED_DOT, null);

        final char[] filteredSlash = chars.clone();
        filterBad(filteredSlash, SPELLED_SLASH, null);

        for (int n = 0; n < tlds.length; n++) {
            filterTld(chars, tlds[n], tldTypes[n], filteredDot, filteredSlash);
        }
    }

    private static void filterTld(final char[] chars, final char[] tld, final int type, final char[] filteredDot, final char[] filteredSlash) {
        if (tld.length > chars.length) {
            return;
        }

        int stride;
        for (int start = 0; start <= chars.length - tld.length; start += stride) {
            int end = start;
            int off = 0;
            stride = 1;

            while (end < chars.length) {
                int charLen = 0;
                final char b = chars[end];
                char c = '\0';

                if (end + 1 < chars.length) {
                    c = chars[end + 1];
                }

                if (off < tld.length && (charLen = getEmulatedDomainCharLen(tld[off], b, c)) > 0) {
                    end += charLen;
                    off++;
                    continue;
                }

                if (off == 0) {
                    break;
                }

                if ((charLen = getEmulatedDomainCharLen(tld[off - 1], b, c)) > 0) {
                    end += charLen;
                    if (off == 1) {
                        stride++;
                    }
                    continue;
                }

                if (off >= tld.length || !isSymbol(b)) {
                    break;
                }

                end++;
            }

            if (off >= tld.length) {
                boolean bad = false;
                final int status0 = getTldDotFilterStatus(chars, start, filteredDot);
                final int status1 = getTldSlashFilterStatus(chars, end - 1, filteredSlash);

                // status0 number meanings
                // 0 = found no symbols
                // 1 = found symbol but not comma, period, or >= 3 asterisks
                // 2 = start pos was 0
                // 3 = found comma or period
                // 4 = found a string of 3 or more asterisks

                // status1 number meanings
                // 0 = found no symbols
                // 1 = found symbol but not comma, period, or >= 5 asterisks
                // 2 = end pos was 0
                // 3 = found forward or backwards slash
                // 4 = found a string of 5 or more asterisks

                if (type == 1 && status0 > 0 && status1 > 0) {
                    bad = true;
                }
                if (type == 2 && (status0 > 2 && status1 > 0 || status0 > 0 && status1 > 2)) {
                    bad = true;
                }
                if (type == 3 && status0 > 0 && status1 > 2) {
                    bad = true;
                }

                if (bad) {
                    int first = start;
                    int last = end - 1;

                    // if we found comma, period, or a string of 3 or more
                    // asterisks in our filteredDot[]
                    if (status0 > 2) {
                        if (status0 == 4) {
                            boolean findStart = false;
                            for (int i = first - 1; i >= 0; i--) {
                                if (findStart) {
                                    if (filteredDot[i] != '*') {
                                        break;
                                    }
                                    first = i;
                                } else if (filteredDot[i] == '*') {
                                    first = i;
                                    findStart = true;
                                }
                            }
                        }

                        boolean findStart = false;
                        for (int i = first - 1; i >= 0; i--) {
                            if (findStart) {
                                if (isSymbol(chars[i])) {
                                    break;
                                }
                                first = i;
                            } else if (!isSymbol(chars[i])) {
                                findStart = true;
                                first = i;
                            }
                        }
                    }

                    // we found a slash or string of 5 or more asterisks in our
                    // filteredSlash[]
                    if (status1 > 2) {
                        // there was a string of asterisks.
                        if (status1 == 4) {
                            boolean findLast = false;
                            for (int i = last + 1; i < chars.length; i++) {
                                if (findLast) {
                                    if (filteredSlash[i] != '*') {
                                        break;
                                    }
                                    last = i;
                                } else if (filteredSlash[i] == '*') {
                                    last = i;
                                    findLast = true;
                                }
                            }
                        }

                        boolean findLast = false;
                        for (int i = last + 1; i < chars.length; i++) {
                            if (findLast) {
                                if (isSymbol(chars[i])) {
                                    break;
                                }
                                last = i;
                            } else if (!isSymbol(chars[i])) {
                                findLast = true;
                                last = i;
                            }
                        }
                    }

                    // finally! censor that shit!
                    for (int i = first; i <= last; i++) {
                        chars[i] = '*';
                    }
                }
            }
        }
    }

    private static int getTldDotFilterStatus(final char[] chars, final int start, final char[] filteredDot) {
        if (start == 0) {
            return 2;
        }

        for (int i = start - 1; i >= 0; i--) {
            if (!isSymbol(chars[i])) {
                break;
            }
            if (chars[i] == ',' || chars[i] == '.') {
                return 3;
            }
        }

        int asteriskCount = 0;
        for (int i = start - 1; i >= 0; i--) {
            if (!isSymbol(filteredDot[i])) {
                break;
            }

            if (filteredDot[i] == '*') {
                asteriskCount++;
            }
        }

        if (asteriskCount >= 3) {
            return 4;
        }
        return !isSymbol(chars[start - 1]) ? 0 : 1;
    }

    private static int getTldSlashFilterStatus(final char[] chars, final int end, final char[] filteredSlash) {
        if (end + 1 == chars.length) {
            return 2;
        }

        for (int j = end + 1; j < chars.length; j++) {
            if (!isSymbol(chars[j])) {
                break;
            }
            if (chars[j] == '\\' || chars[j] == '/') {
                return 3;
            }
        }

        int asterisks = 0;
        for (int l = end + 1; l < chars.length; l++) {
            if (!isSymbol(filteredSlash[l])) {
                break;
            }
            if (filteredSlash[l] == '*') {
                asterisks++;
            }
        }

        if (asterisks >= 5) {
            return 4;
        }
        return !isSymbol(chars[end + 1]) ? 0 : 1;
    }

    private static void filterBad(final char[] chars, final char[] fragment, final byte[][] badCombinations) {
        if (fragment.length > chars.length) {
            return;
        }

        int stride;
        for (int start = 0; start <= chars.length - fragment.length; start += stride) {
            int end = start;
            int fragOff = 0;
            int iterations = 0;
            stride = 1;

            boolean isSymbol = false;
            boolean isEmulated = false;
            boolean isNumeral = false;

            while (end < chars.length && (!isEmulated || !isNumeral)) {
                int charLen = 0;
                final char b = chars[end];
                char c = '\0';

                if (end + 1 < chars.length) {
                    c = chars[end + 1];
                }

                if (fragOff < fragment.length && (charLen = getEmulatedBadCharLen(fragment[fragOff], b, c)) > 0) {
                    if (charLen == 1 && isNumeral(b)) {
                        isEmulated = true;
                    }

                    if (charLen == 2 && (isNumeral(b) || isNumeral(c))) {
                        isEmulated = true;
                    }

                    end += charLen;
                    fragOff++;
                    continue;
                }

                if (fragOff == 0) {
                    break;
                }

                if ((charLen = getEmulatedBadCharLen(fragment[fragOff - 1], b, c)) > 0) {
                    end += charLen;

                    if (fragOff == 1) {
                        stride++;
                    }

                    continue;
                }

                if (fragOff >= fragment.length || !isNotLowercaseAlpha(b)) {
                    break;
                }

                if (isSymbol(b) && b != '\'') {
                    isSymbol = true;
                }

                if (isNumeral(b)) {
                    isNumeral = true;
                }

                end++;

                if ((++iterations * 100) / (end - start) > 90) {
                    break;
                }
            }

            if (fragOff >= fragment.length && (!isEmulated || !isNumeral)) {
                boolean bad = true;

                if (!isSymbol) {
                    char a = ' ';

                    if (start - 1 >= 0) {
                        a = chars[start - 1];
                    }

                    char b = ' ';

                    if (end < chars.length) {
                        b = chars[end];
                    }

                    if (badCombinations != null && comboMatches(getIndex(a), getIndex(b), badCombinations)) {
                        bad = false;
                    }
                } else {
                    boolean badCurrent = false;
                    boolean badNext = false;

                    // if the previous is out of range or a symbol
                    if (start - 1 < 0 || isSymbol(chars[start - 1]) && chars[start - 1] != '\'') {
                        badCurrent = true;
                    }

                    // if the current is out of range or a symbol
                    if (end >= chars.length || isSymbol(chars[end]) && chars[end] != '\'') {
                        badNext = true;
                    }

                    if (!badCurrent || !badNext) {
                        boolean good = false;
                        int cur = start - 2;

                        if (badCurrent) {
                            cur = start;
                        }

                        for (; !good && cur < end; cur++) {
                            if (cur >= 0 && (!isSymbol(chars[cur]) || chars[cur] == '\'')) {
                                final char[] frag = new char[3];
                                int off;
                                for (off = 0; off < 3; off++) {
                                    if (cur + off >= chars.length || isSymbol(chars[cur + off]) && chars[cur + off] != '\'') {
                                        break;
                                    }
                                    frag[off] = chars[cur + off];
                                }

                                // if we read zero chars
                                boolean valid = off != 0;

                                // if we read less than 3 chars, our cur is
                                // within bounds, and isn't a symbol
                                if (off < 3 && cur - 1 >= 0 && (!isSymbol(chars[cur - 1]) || chars[cur - 1] == '\'')) {
                                    valid = false;
                                }

                                if (valid && !isBadFragment(frag)) {
                                    good = true;
                                }
                            }
                        }

                        if (!good) {
                            bad = false;
                        }
                    }
                }

                if (bad) {
                    int numeralCount = 0;
                    int alphaCount = 0;
                    int alphaIndex = -1;

                    for (int n = start; n < end; n++) {
                        if (isNumeral(chars[n])) {
                            numeralCount++;
                        } else if (isAlpha(chars[n])) {
                            alphaCount++;
                            alphaIndex = n;
                        }
                    }

                    if (alphaIndex > -1) {
                        numeralCount -= end - 1 - alphaIndex;
                    }

                    if (numeralCount <= alphaCount) {
                        for (int n = start; n < end; n++) {
                            chars[n] = '*';
                        }
                    } else {
                        stride = 1;
                    }
                }
            }
        }

    }

    private static boolean comboMatches(final byte a, final byte b, final byte[][] combos) {
        int first = 0;
        if (combos[first][0] == a && combos[first][1] == b) {
            return true;
        }

        int last = combos.length - 1;
        if (combos[last][0] == a && combos[last][1] == b) {
            return true;
        }

        do {
            final int middle = (first + last) / 2;

            if (combos[middle][0] == a && combos[middle][1] == b) {
                return true;
            }

            if (a < combos[middle][0] || a == combos[middle][0] && b < combos[middle][1]) {
                last = middle;
            } else {
                first = middle;
            }
        } while (first != last && first + 1 != last);
        return false;
    }

    /**
     * Returns the lengths of the emulated characters for 'o', 'c', 'e', 's',
     * and 'l' e.g.: "()" for 'o' would return 2.
     *
     * @param a the first char
     * @param b the second char
     * @param c the third char
     * @return the length
     */
    private static int getEmulatedDomainCharLen(final char a, final char b, final char c) {
        if (a == b) {
            return 1;
        }
        if (a == 'o' && b == '0') {
            return 1;
        }
        if (a == 'o' && b == '(' && c == ')') {
            return 2;
        }
        if (a == 'c' && (b == '(' || b == '<' || b == '[')) {
            return 1;
        }
        if (a == 'e' && b == '\u20AC') {
            return 1;
        }
        if (a == 's' && b == '$') {
            return 1;
        }
        if (a == 'l' && b == 'i') {
            return 1;
        }
        return 0;
    }

    /**
     * Used for getting the length of an emulated character. e.g.; [) for 'd'
     * would return 2 since it uses 2 characters to emulate the letter d.
     *
     * @param a the first char
     * @param b the second char
     * @param c the third char
     * @return the length
     */
    private static int getEmulatedBadCharLen(final char a, final char b, final char c) {
        if (a == b) {
            return 1;
        }

        if (a >= 'a' && a <= 'm') {
            if (a == 'a') {
                if (b == '4' || b == '@' || b == '^') {
                    return 1;
                }
                return b != '/' || c != '\\' ? 0 : 2;
            }

            if (a == 'b') {
                if (b == '6' || b == '8') {
                    return 1;
                }
                return (b != '1' || c != '3') && (b != 'i' || c != '3') ? 0 : 2;
            }

            if (a == 'c') {
                return b != '(' && b != '<' && b != '{' && b != '[' ? 0 : 1;
            }

            if (a == 'd') {
                return (b != '[' || c != ')') && (b != 'i' || c != ')') ? 0 : 2;
            }

            if (a == 'e') {
                return b != '3' && b != '\u20AC' ? 0 : 1;
            }

            if (a == 'f') {
                if (b == 'p' && c == 'h') {
                    return 2;
                }
                return b != '\243' ? 0 : 1;
            }

            if (a == 'g') {
                return b != '9' && b != '6' && b != 'q' ? 0 : 1;
            }

            if (a == 'h') {
                return b != '#' ? 0 : 1;
            }

            if (a == 'i') {
                return b != 'y' && b != 'l' && b != 'j' && b != '1' && b != '!' && b != ':' && b != ';' && b != '|' ? 0 : 1;
            }

            if (a == 'j') {
                return 0;
            }

            if (a == 'k') {
                return 0;
            }

            if (a == 'l') {
                return b != '1' && b != '|' && b != 'i' ? 0 : 1;
            }

            if (a == 'm') {
                return 0;
            }
        }
        if (a >= 'n' && a <= 'z') {
            if (a == 'n') {
                return 0;
            }

            if (a == 'o') {
                if (b == '0' || b == '*') {
                    return 1;
                }
                return (b != '(' || c != ')') && (b != '[' || c != ']') && (b != '{' || c != '}') && (b != '<' || c != '>') ? 0 : 2;
            }

            if (a == 'p') {
                return 0;
            }

            if (a == 'q') {
                return 0;
            }

            if (a == 'r') {
                return 0;
            }

            if (a == 's') {
                return b != '5' && b != 'z' && b != '$' && b != '2' ? 0 : 1;
            }

            if (a == 't') {
                return b != '7' && b != '+' ? 0 : 1;
            }

            if (a == 'u') {
                if (b == 'v') {
                    return 1;
                }
                return (b != '\\' || c != '/') && (b != '\\' || c != '|') && (b != '|' || c != '/') ? 0 : 2;
            }

            if (a == 'v') {
                return (b != '\\' || c != '/') && (b != '\\' || c != '|') && (b != '|' || c != '/') ? 0 : 2;
            }

            if (a == 'w') {
                return b != 'v' || c != 'v' ? 0 : 2;
            }

            if (a == 'x') {
                return (b != ')' || c != '(') && (b != '}' || c != '{') && (b != ']' || c != '[') && (b != '>' || c != '<') ? 0 : 2;
            }

            if (a == 'y') {
                return 0;
            }

            if (a == 'z') {
                return 0;
            }
        }

        if (a >= '0' && a <= '9') {
            if (a == '0') {
                if (b == 'o' || b == 'O') {
                    return 1;
                }
                return (b != '(' || c != ')') && (b != '{' || c != '}') && (b != '[' || c != ']') ? 0 : 2;
            }

            if (a == '1') {
                return b != 'l' ? 0 : 1;
            } else {
                return 0;
            }
        }

        if (a == ',') {
            return b != '.' ? 0 : 1;
        }

        if (a == '.') {
            return b != ',' ? 0 : 1;
        }

        if (a == '!') {
            return b != 'i' ? 0 : 1;
        } else {
            return 0;
        }
    }

    // [a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s,
    // t, u, v, w, x, y, z, null?, ', 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
    private static byte getIndex(final char c) {
        if (c >= 'a' && c <= 'z') {
            return (byte) ((c - 'a') + 1);
        } else if (c == '\'') {
            return 28;
        } else if (c >= '0' && c <= '9') {
            return (byte) ((c - '0') + 29);
        }
        return 27;
    }

    private static void filterNumFragments(final char[] chars) {
        int index = 0;
        int end = 0;
        int count = 0;
        int start = 0;

        while ((index = indexOfNumber(chars, end)) != -1) {
            boolean foundLowercase = false;

            // scan for lowercase char
            for (int i = end; i >= 0 && i < index && !foundLowercase; i++) {
                if (!isSymbol(chars[i]) && !isNotLowercaseAlpha(chars[i])) {
                    foundLowercase = true;
                }
            }

            if (foundLowercase) {
                count = 0;
            }

            if (count == 0) {
                start = index;
            }

            // get the char index after our found number
            end = indexOfNonNumber(chars, index);

            // parsed number from string
            int value = 0;
            for (int n = index; n < end; n++) {
                value = ((value * 10) + chars[n]) - '0';
            }

            // if our value is over 0xFF or the number uses over 8 characters
            // then reset the counter
            if (value > 255 || end - index > 8) {
                count = 0;
            } else {
                count++;
            }

            // If we found 4 separate numbers with their parsed values under
            // 255 then replace everything from start to end of these number
            // with asterisks.
            if (count == 4) {
                for (int n = start; n < end; n++) {
                    chars[n] = '*';
                }
                count = 0;
            }
        }
    }

    private static int indexOfNumber(final char[] chars, final int off) {
        for (int i = off; i < chars.length && i >= 0; i++) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return i;
            }
        }
        return -1;
    }

    private static int indexOfNonNumber(final char[] chars, final int off) {
        for (int i = off; i < chars.length && i >= 0; i++) {
            if (chars[i] < '0' || chars[i] > '9') {
                return i;
            }
        }
        return chars.length;
    }

    private static boolean isSymbol(final char c) {
        return !isAlpha(c) && !isNumeral(c);
    }

    private static boolean isNotLowercaseAlpha(final char c) {
        if (c < 'a' || c > 'z') {
            return true;
        }
        return c == 'v' || c == 'x' || c == 'j' || c == 'q' || c == 'z';
    }

    private static boolean isAlpha(final char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    private static boolean isNumeral(final char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLowercaseAlpha(final char c) {
        return c >= 'a' && c <= 'z';
    }

    private static boolean isUppercaseAlpha(final char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isBadFragment(final char[] chars) {
        boolean skip = true;

        for (int i = 0; i < chars.length; i++) {
            // if char not numeral and not null
            if (!isNumeral(chars[i]) && chars[i] != 0) {
                skip = false;
            }
        }

        // our string had a number or null character in it.
        if (skip) {
            return true;
        }

        final int i = hash(chars);
        int start = 0;
        int end = fragments.length - 1;

        if (i == fragments[start] || i == fragments[end]) {
            return true;
        }

        do {
            final int middle = (start + end) / 2;

            if (i == fragments[middle]) {
                return true;
            }

            if (i < fragments[middle]) {
                end = middle;
            } else {
                start = middle;
            }
        } while (start != end && start + 1 != end);
        return false;
    }

    private static int hash(final char[] chars) {
        if (chars.length > 6) {
            return 0;
        }

        int k = 0;
        for (int n = 0; n < chars.length; n++) {
            // read backwards
            final char c = chars[chars.length - n - 1];

            if (c >= 'a' && c <= 'z') {
                k = (k * 38) + ((c - 'a') + 1);
            } else if (c == '\'') {
                k = (k * 38) + 27;
            } else if (c >= '0' && c <= '9') {
                k = (k * 38) + ((c - '0') + 28);
            } else if (c != 0) {
                return 0;
            }
        }
        return k;
    }

}