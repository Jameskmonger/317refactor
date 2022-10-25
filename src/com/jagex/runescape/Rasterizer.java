package com.jagex.runescape;

public final class Rasterizer extends DrawingArea {

    private static int adjustBrightness(final int rgb, final double intensity) {
        double r = (rgb >> 16) / 256D;
        double g = (rgb >> 8 & 0xff) / 256D;
        double b = (rgb & 0xff) / 256D;
        r = Math.pow(r, intensity);
        g = Math.pow(g, intensity);
        b = Math.pow(b, intensity);
        final int byteR = (int) (r * 256D);
        final int byteG = (int) (g * 256D);
        final int byteB = (int) (b * 256D);
        return (byteR << 16) + (byteG << 8) + byteB;
    }

    public static void calculatePalette(double brightness) {
        brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
        int hsl = 0;
        for (int k = 0; k < 512; k++) {
            final double d1 = k / 8 / 64D + 0.0078125D;
            final double d2 = (k & 7) / 8D + 0.0625D;
            for (int k1 = 0; k1 < 128; k1++) {
                final double d3 = k1 / 128D;
                double r = d3;
                double g = d3;
                double b = d3;
                if (d2 != 0.0D) {
                    final double d7;
                    if (d3 < 0.5D) {
                        d7 = d3 * (1.0D + d2);
                    } else {
                        d7 = (d3 + d2) - d3 * d2;
                    }
                    final double d8 = 2D * d3 - d7;
                    double d9 = d1 + 0.33333333333333331D;
                    if (d9 > 1.0D) {
                        d9--;
                    }
                    double d11 = d1 - 0.33333333333333331D;
                    if (d11 < 0.0D) {
                        d11++;
                    }
                    if (6D * d9 < 1.0D) {
                        r = d8 + (d7 - d8) * 6D * d9;
                    } else if (2D * d9 < 1.0D) {
                        r = d7;
                    } else if (3D * d9 < 2D) {
                        r = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
                    } else {
                        r = d8;
                    }
                    if (6D * d1 < 1.0D) {
                        g = d8 + (d7 - d8) * 6D * d1;
                    } else if (2D * d1 < 1.0D) {
                        g = d7;
                    } else if (3D * d1 < 2D) {
                        g = d8 + (d7 - d8) * (0.66666666666666663D - d1) * 6D;
                    } else {
                        g = d8;
                    }
                    if (6D * d11 < 1.0D) {
                        b = d8 + (d7 - d8) * 6D * d11;
                    } else if (2D * d11 < 1.0D) {
                        b = d7;
                    } else if (3D * d11 < 2D) {
                        b = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
                    } else {
                        b = d8;
                    }
                }
                final int byteR = (int) (r * 256D);
                final int byteG = (int) (g * 256D);
                final int byteB = (int) (b * 256D);
                int rgb = (byteR << 16) + (byteG << 8) + byteB;
                rgb = adjustBrightness(rgb, brightness);
                if (rgb == 0) {
                    rgb = 1;
                }
                HSL_TO_RGB[hsl++] = rgb;
            }

        }

        for (int textureId = 0; textureId < 50; textureId++) {
            if (textureImages[textureId] != null) {
                final int[] palette = textureImages[textureId].palette;
                texturePalettes[textureId] = new int[palette.length];
                for (int colour = 0; colour < palette.length; colour++) {
                    texturePalettes[textureId][colour] = adjustBrightness(palette[colour], brightness);
                    if ((texturePalettes[textureId][colour] & 0xf8f8ff) == 0 && colour != 0) {
                        texturePalettes[textureId][colour] = 1;
                    }
                }

            }
        }

        for (int textureId = 0; textureId < 50; textureId++) {
            resetTexture(textureId);
        }

    }

    public static void clearTextureCache() {
        texelArrayPool = null;
        for (int i = 0; i < 50; i++) {
            texelCache[i] = null;
        }

    }

    public static void drawFlatTriangle(int i, int j, int k, int l, int i1, int j1, final int k1) {
        int l1 = 0;
        if (j != i) {
            l1 = (i1 - l << 16) / (j - i);
        }
        int i2 = 0;
        if (k != j) {
            i2 = (j1 - i1 << 16) / (k - j);
        }
        int j2 = 0;
        if (k != i) {
            j2 = (l - j1 << 16) / (i - k);
        }
        if (i <= j && i <= k) {
            if (i >= DrawingArea.bottomY) {
                return;
            }
            if (j > DrawingArea.bottomY) {
                j = DrawingArea.bottomY;
            }
            if (k > DrawingArea.bottomY) {
                k = DrawingArea.bottomY;
            }
            if (j < k) {
                j1 = l <<= 16;
                if (i < 0) {
                    j1 -= j2 * i;
                    l -= l1 * i;
                    i = 0;
                }
                i1 <<= 16;
                if (j < 0) {
                    i1 -= i2 * j;
                    j = 0;
                }
                if (i != j && j2 < l1 || i == j && j2 > i2) {
                    k -= j;
                    j -= i;
                    for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
                        method377(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
                        j1 += j2;
                        l += l1;
                    }

                    while (--k >= 0) {
                        method377(DrawingArea.pixels, i, k1, j1 >> 16, i1 >> 16);
                        j1 += j2;
                        i1 += i2;
                        i += DrawingArea.width;
                    }
                    return;
                }
                k -= j;
                j -= i;
                for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
                    method377(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
                    j1 += j2;
                    l += l1;
                }

                while (--k >= 0) {
                    method377(DrawingArea.pixels, i, k1, i1 >> 16, j1 >> 16);
                    j1 += j2;
                    i1 += i2;
                    i += DrawingArea.width;
                }
                return;
            }
            i1 = l <<= 16;
            if (i < 0) {
                i1 -= j2 * i;
                l -= l1 * i;
                i = 0;
            }
            j1 <<= 16;
            if (k < 0) {
                j1 -= i2 * k;
                k = 0;
            }
            if (i != k && j2 < l1 || i == k && i2 > l1) {
                j -= k;
                k -= i;
                for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
                    method377(DrawingArea.pixels, i, k1, i1 >> 16, l >> 16);
                    i1 += j2;
                    l += l1;
                }

                while (--j >= 0) {
                    method377(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
                    j1 += i2;
                    l += l1;
                    i += DrawingArea.width;
                }
                return;
            }
            j -= k;
            k -= i;
            for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
                method377(DrawingArea.pixels, i, k1, l >> 16, i1 >> 16);
                i1 += j2;
                l += l1;
            }

            while (--j >= 0) {
                method377(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
                j1 += i2;
                l += l1;
                i += DrawingArea.width;
            }
            return;
        }
        if (j <= k) {
            if (j >= DrawingArea.bottomY) {
                return;
            }
            if (k > DrawingArea.bottomY) {
                k = DrawingArea.bottomY;
            }
            if (i > DrawingArea.bottomY) {
                i = DrawingArea.bottomY;
            }
            if (k < i) {
                l = i1 <<= 16;
                if (j < 0) {
                    l -= l1 * j;
                    i1 -= i2 * j;
                    j = 0;
                }
                j1 <<= 16;
                if (k < 0) {
                    j1 -= j2 * k;
                    k = 0;
                }
                if (j != k && l1 < i2 || j == k && l1 > j2) {
                    i -= k;
                    k -= j;
                    for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
                        method377(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
                        l += l1;
                        i1 += i2;
                    }

                    while (--i >= 0) {
                        method377(DrawingArea.pixels, j, k1, l >> 16, j1 >> 16);
                        l += l1;
                        j1 += j2;
                        j += DrawingArea.width;
                    }
                    return;
                }
                i -= k;
                k -= j;
                for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
                    method377(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
                    l += l1;
                    i1 += i2;
                }

                while (--i >= 0) {
                    method377(DrawingArea.pixels, j, k1, j1 >> 16, l >> 16);
                    l += l1;
                    j1 += j2;
                    j += DrawingArea.width;
                }
                return;
            }
            j1 = i1 <<= 16;
            if (j < 0) {
                j1 -= l1 * j;
                i1 -= i2 * j;
                j = 0;
            }
            l <<= 16;
            if (i < 0) {
                l -= j2 * i;
                i = 0;
            }
            if (l1 < i2) {
                k -= i;
                i -= j;
                for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
                    method377(DrawingArea.pixels, j, k1, j1 >> 16, i1 >> 16);
                    j1 += l1;
                    i1 += i2;
                }

                while (--k >= 0) {
                    method377(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
                    l += j2;
                    i1 += i2;
                    j += DrawingArea.width;
                }
                return;
            }
            k -= i;
            i -= j;
            for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
                method377(DrawingArea.pixels, j, k1, i1 >> 16, j1 >> 16);
                j1 += l1;
                i1 += i2;
            }

            while (--k >= 0) {
                method377(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
                l += j2;
                i1 += i2;
                j += DrawingArea.width;
            }
            return;
        }
        if (k >= DrawingArea.bottomY) {
            return;
        }
        if (i > DrawingArea.bottomY) {
            i = DrawingArea.bottomY;
        }
        if (j > DrawingArea.bottomY) {
            j = DrawingArea.bottomY;
        }
        if (i < j) {
            i1 = j1 <<= 16;
            if (k < 0) {
                i1 -= i2 * k;
                j1 -= j2 * k;
                k = 0;
            }
            l <<= 16;
            if (i < 0) {
                l -= l1 * i;
                i = 0;
            }
            if (i2 < j2) {
                j -= i;
                i -= k;
                for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
                    method377(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
                    i1 += i2;
                    j1 += j2;
                }

                while (--j >= 0) {
                    method377(DrawingArea.pixels, k, k1, i1 >> 16, l >> 16);
                    i1 += i2;
                    l += l1;
                    k += DrawingArea.width;
                }
                return;
            }
            j -= i;
            i -= k;
            for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
                method377(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
                i1 += i2;
                j1 += j2;
            }

            while (--j >= 0) {
                method377(DrawingArea.pixels, k, k1, l >> 16, i1 >> 16);
                i1 += i2;
                l += l1;
                k += DrawingArea.width;
            }
            return;
        }
        l = j1 <<= 16;
        if (k < 0) {
            l -= i2 * k;
            j1 -= j2 * k;
            k = 0;
        }
        i1 <<= 16;
        if (j < 0) {
            i1 -= l1 * j;
            j = 0;
        }
        if (i2 < j2) {
            i -= j;
            j -= k;
            for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
                method377(DrawingArea.pixels, k, k1, l >> 16, j1 >> 16);
                l += i2;
                j1 += j2;
            }

            while (--i >= 0) {
                method377(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
                i1 += l1;
                j1 += j2;
                k += DrawingArea.width;
            }
            return;
        }
        i -= j;
        j -= k;
        for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
            method377(DrawingArea.pixels, k, k1, j1 >> 16, l >> 16);
            l += i2;
            j1 += j2;
        }

        while (--i >= 0) {
            method377(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
            i1 += l1;
            j1 += j2;
            k += DrawingArea.width;
        }
    }

    public static void drawShadedTriangle(int yA, int yB, int yC, int xA, int xB, int xC, int zA, int zB, int zC) {
        int j2 = 0;
        int k2 = 0;
        if (yB != yA) {
            j2 = (xB - xA << 16) / (yB - yA);
            k2 = (zB - zA << 15) / (yB - yA);
        }
        int l2 = 0;
        int i3 = 0;
        if (yC != yB) {
            l2 = (xC - xB << 16) / (yC - yB);
            i3 = (zC - zB << 15) / (yC - yB);
        }
        int j3 = 0;
        int k3 = 0;
        if (yC != yA) {
            j3 = (xA - xC << 16) / (yA - yC);
            k3 = (zA - zC << 15) / (yA - yC);
        }
        if (yA <= yB && yA <= yC) {
            if (yA >= DrawingArea.bottomY) {
                return;
            }
            if (yB > DrawingArea.bottomY) {
                yB = DrawingArea.bottomY;
            }
            if (yC > DrawingArea.bottomY) {
                yC = DrawingArea.bottomY;
            }
            if (yB < yC) {
                xC = xA <<= 16;
                zC = zA <<= 15;
                if (yA < 0) {
                    xC -= j3 * yA;
                    xA -= j2 * yA;
                    zC -= k3 * yA;
                    zA -= k2 * yA;
                    yA = 0;
                }
                xB <<= 16;
                zB <<= 15;
                if (yB < 0) {
                    xB -= l2 * yB;
                    zB -= i3 * yB;
                    yB = 0;
                }
                if (yA != yB && j3 < j2 || yA == yB && j3 > l2) {
                    yC -= yB;
                    yB -= yA;
                    for (yA = lineOffsets[yA]; --yB >= 0; yA += DrawingArea.width) {
                        method375(DrawingArea.pixels, yA, xC >> 16, xA >> 16, zC >> 7, zA >> 7);
                        xC += j3;
                        xA += j2;
                        zC += k3;
                        zA += k2;
                    }

                    while (--yC >= 0) {
                        method375(DrawingArea.pixels, yA, xC >> 16, xB >> 16, zC >> 7, zB >> 7);
                        xC += j3;
                        xB += l2;
                        zC += k3;
                        zB += i3;
                        yA += DrawingArea.width;
                    }
                    return;
                }
                yC -= yB;
                yB -= yA;
                for (yA = lineOffsets[yA]; --yB >= 0; yA += DrawingArea.width) {
                    method375(DrawingArea.pixels, yA, xA >> 16, xC >> 16, zA >> 7, zC >> 7);
                    xC += j3;
                    xA += j2;
                    zC += k3;
                    zA += k2;
                }

                while (--yC >= 0) {
                    method375(DrawingArea.pixels, yA, xB >> 16, xC >> 16, zB >> 7, zC >> 7);
                    xC += j3;
                    xB += l2;
                    zC += k3;
                    zB += i3;
                    yA += DrawingArea.width;
                }
                return;
            }
            xB = xA <<= 16;
            zB = zA <<= 15;
            if (yA < 0) {
                xB -= j3 * yA;
                xA -= j2 * yA;
                zB -= k3 * yA;
                zA -= k2 * yA;
                yA = 0;
            }
            xC <<= 16;
            zC <<= 15;
            if (yC < 0) {
                xC -= l2 * yC;
                zC -= i3 * yC;
                yC = 0;
            }
            if (yA != yC && j3 < j2 || yA == yC && l2 > j2) {
                yB -= yC;
                yC -= yA;
                for (yA = lineOffsets[yA]; --yC >= 0; yA += DrawingArea.width) {
                    method375(DrawingArea.pixels, yA, xB >> 16, xA >> 16, zB >> 7, zA >> 7);
                    xB += j3;
                    xA += j2;
                    zB += k3;
                    zA += k2;
                }

                while (--yB >= 0) {
                    method375(DrawingArea.pixels, yA, xC >> 16, xA >> 16, zC >> 7, zA >> 7);
                    xC += l2;
                    xA += j2;
                    zC += i3;
                    zA += k2;
                    yA += DrawingArea.width;
                }
                return;
            }
            yB -= yC;
            yC -= yA;
            for (yA = lineOffsets[yA]; --yC >= 0; yA += DrawingArea.width) {
                method375(DrawingArea.pixels, yA, xA >> 16, xB >> 16, zA >> 7, zB >> 7);
                xB += j3;
                xA += j2;
                zB += k3;
                zA += k2;
            }

            while (--yB >= 0) {
                method375(DrawingArea.pixels, yA, xA >> 16, xC >> 16, zA >> 7, zC >> 7);
                xC += l2;
                xA += j2;
                zC += i3;
                zA += k2;
                yA += DrawingArea.width;
            }
            return;
        }
        if (yB <= yC) {
            if (yB >= DrawingArea.bottomY) {
                return;
            }
            if (yC > DrawingArea.bottomY) {
                yC = DrawingArea.bottomY;
            }
            if (yA > DrawingArea.bottomY) {
                yA = DrawingArea.bottomY;
            }
            if (yC < yA) {
                xA = xB <<= 16;
                zA = zB <<= 15;
                if (yB < 0) {
                    xA -= j2 * yB;
                    xB -= l2 * yB;
                    zA -= k2 * yB;
                    zB -= i3 * yB;
                    yB = 0;
                }
                xC <<= 16;
                zC <<= 15;
                if (yC < 0) {
                    xC -= j3 * yC;
                    zC -= k3 * yC;
                    yC = 0;
                }
                if (yB != yC && j2 < l2 || yB == yC && j2 > j3) {
                    yA -= yC;
                    yC -= yB;
                    for (yB = lineOffsets[yB]; --yC >= 0; yB += DrawingArea.width) {
                        method375(DrawingArea.pixels, yB, xA >> 16, xB >> 16, zA >> 7, zB >> 7);
                        xA += j2;
                        xB += l2;
                        zA += k2;
                        zB += i3;
                    }

                    while (--yA >= 0) {
                        method375(DrawingArea.pixels, yB, xA >> 16, xC >> 16, zA >> 7, zC >> 7);
                        xA += j2;
                        xC += j3;
                        zA += k2;
                        zC += k3;
                        yB += DrawingArea.width;
                    }
                    return;
                }
                yA -= yC;
                yC -= yB;
                for (yB = lineOffsets[yB]; --yC >= 0; yB += DrawingArea.width) {
                    method375(DrawingArea.pixels, yB, xB >> 16, xA >> 16, zB >> 7, zA >> 7);
                    xA += j2;
                    xB += l2;
                    zA += k2;
                    zB += i3;
                }

                while (--yA >= 0) {
                    method375(DrawingArea.pixels, yB, xC >> 16, xA >> 16, zC >> 7, zA >> 7);
                    xA += j2;
                    xC += j3;
                    zA += k2;
                    zC += k3;
                    yB += DrawingArea.width;
                }
                return;
            }
            xC = xB <<= 16;
            zC = zB <<= 15;
            if (yB < 0) {
                xC -= j2 * yB;
                xB -= l2 * yB;
                zC -= k2 * yB;
                zB -= i3 * yB;
                yB = 0;
            }
            xA <<= 16;
            zA <<= 15;
            if (yA < 0) {
                xA -= j3 * yA;
                zA -= k3 * yA;
                yA = 0;
            }
            if (j2 < l2) {
                yC -= yA;
                yA -= yB;
                for (yB = lineOffsets[yB]; --yA >= 0; yB += DrawingArea.width) {
                    method375(DrawingArea.pixels, yB, xC >> 16, xB >> 16, zC >> 7, zB >> 7);
                    xC += j2;
                    xB += l2;
                    zC += k2;
                    zB += i3;
                }

                while (--yC >= 0) {
                    method375(DrawingArea.pixels, yB, xA >> 16, xB >> 16, zA >> 7, zB >> 7);
                    xA += j3;
                    xB += l2;
                    zA += k3;
                    zB += i3;
                    yB += DrawingArea.width;
                }
                return;
            }
            yC -= yA;
            yA -= yB;
            for (yB = lineOffsets[yB]; --yA >= 0; yB += DrawingArea.width) {
                method375(DrawingArea.pixels, yB, xB >> 16, xC >> 16, zB >> 7, zC >> 7);
                xC += j2;
                xB += l2;
                zC += k2;
                zB += i3;
            }

            while (--yC >= 0) {
                method375(DrawingArea.pixels, yB, xB >> 16, xA >> 16, zB >> 7, zA >> 7);
                xA += j3;
                xB += l2;
                zA += k3;
                zB += i3;
                yB += DrawingArea.width;
            }
            return;
        }
        if (yC >= DrawingArea.bottomY) {
            return;
        }
        if (yA > DrawingArea.bottomY) {
            yA = DrawingArea.bottomY;
        }
        if (yB > DrawingArea.bottomY) {
            yB = DrawingArea.bottomY;
        }
        if (yA < yB) {
            xB = xC <<= 16;
            zB = zC <<= 15;
            if (yC < 0) {
                xB -= l2 * yC;
                xC -= j3 * yC;
                zB -= i3 * yC;
                zC -= k3 * yC;
                yC = 0;
            }
            xA <<= 16;
            zA <<= 15;
            if (yA < 0) {
                xA -= j2 * yA;
                zA -= k2 * yA;
                yA = 0;
            }
            if (l2 < j3) {
                yB -= yA;
                yA -= yC;
                for (yC = lineOffsets[yC]; --yA >= 0; yC += DrawingArea.width) {
                    method375(DrawingArea.pixels, yC, xB >> 16, xC >> 16, zB >> 7, zC >> 7);
                    xB += l2;
                    xC += j3;
                    zB += i3;
                    zC += k3;
                }

                while (--yB >= 0) {
                    method375(DrawingArea.pixels, yC, xB >> 16, xA >> 16, zB >> 7, zA >> 7);
                    xB += l2;
                    xA += j2;
                    zB += i3;
                    zA += k2;
                    yC += DrawingArea.width;
                }
                return;
            }
            yB -= yA;
            yA -= yC;
            for (yC = lineOffsets[yC]; --yA >= 0; yC += DrawingArea.width) {
                method375(DrawingArea.pixels, yC, xC >> 16, xB >> 16, zC >> 7, zB >> 7);
                xB += l2;
                xC += j3;
                zB += i3;
                zC += k3;
            }

            while (--yB >= 0) {
                method375(DrawingArea.pixels, yC, xA >> 16, xB >> 16, zA >> 7, zB >> 7);
                xB += l2;
                xA += j2;
                zB += i3;
                zA += k2;
                yC += DrawingArea.width;
            }
            return;
        }
        xA = xC <<= 16;
        zA = zC <<= 15;
        if (yC < 0) {
            xA -= l2 * yC;
            xC -= j3 * yC;
            zA -= i3 * yC;
            zC -= k3 * yC;
            yC = 0;
        }
        xB <<= 16;
        zB <<= 15;
        if (yB < 0) {
            xB -= j2 * yB;
            zB -= k2 * yB;
            yB = 0;
        }
        if (l2 < j3) {
            yA -= yB;
            yB -= yC;
            for (yC = lineOffsets[yC]; --yB >= 0; yC += DrawingArea.width) {
                method375(DrawingArea.pixels, yC, xA >> 16, xC >> 16, zA >> 7, zC >> 7);
                xA += l2;
                xC += j3;
                zA += i3;
                zC += k3;
            }

            while (--yA >= 0) {
                method375(DrawingArea.pixels, yC, xB >> 16, xC >> 16, zB >> 7, zC >> 7);
                xB += j2;
                xC += j3;
                zB += k2;
                zC += k3;
                yC += DrawingArea.width;
            }
            return;
        }
        yA -= yB;
        yB -= yC;
        for (yC = lineOffsets[yC]; --yB >= 0; yC += DrawingArea.width) {
            method375(DrawingArea.pixels, yC, xC >> 16, xA >> 16, zC >> 7, zA >> 7);
            xA += l2;
            xC += j3;
            zA += i3;
            zC += k3;
        }

        while (--yA >= 0) {
            method375(DrawingArea.pixels, yC, xC >> 16, xB >> 16, zC >> 7, zB >> 7);
            xB += j2;
            xC += j3;
            zB += k2;
            zC += k3;
            yC += DrawingArea.width;
        }
    }

    public static void drawTexturedTriangle(int yA, int yB, int yC, int xA, int xB, int xC, int zA, int zB, int zC,
                                            final int j2, int k2, int l2, final int i3, int j3, int k3, final int l3, int i4, int j4, final int textureId) {
        final int[] texture = getTexturePixels(textureId);
        opaque = !transparent[textureId];
        k2 = j2 - k2;
        j3 = i3 - j3;
        i4 = l3 - i4;
        l2 -= j2;
        k3 -= i3;
        j4 -= l3;
        int l4 = l2 * i3 - k3 * j2 << 14;
        final int i5 = k3 * l3 - j4 * i3 << 8;
        final int j5 = j4 * j2 - l2 * l3 << 5;
        int k5 = k2 * i3 - j3 * j2 << 14;
        final int l5 = j3 * l3 - i4 * i3 << 8;
        final int i6 = i4 * j2 - k2 * l3 << 5;
        int j6 = j3 * l2 - k2 * k3 << 14;
        final int k6 = i4 * k3 - j3 * j4 << 8;
        final int l6 = k2 * j4 - i4 * l2 << 5;
        int i7 = 0;
        int j7 = 0;
        if (yB != yA) {
            i7 = (xB - xA << 16) / (yB - yA);
            j7 = (zB - zA << 16) / (yB - yA);
        }
        int k7 = 0;
        int l7 = 0;
        if (yC != yB) {
            k7 = (xC - xB << 16) / (yC - yB);
            l7 = (zC - zB << 16) / (yC - yB);
        }
        int i8 = 0;
        int j8 = 0;
        if (yC != yA) {
            i8 = (xA - xC << 16) / (yA - yC);
            j8 = (zA - zC << 16) / (yA - yC);
        }
        if (yA <= yB && yA <= yC) {
            if (yA >= DrawingArea.bottomY) {
                return;
            }
            if (yB > DrawingArea.bottomY) {
                yB = DrawingArea.bottomY;
            }
            if (yC > DrawingArea.bottomY) {
                yC = DrawingArea.bottomY;
            }
            if (yB < yC) {
                xC = xA <<= 16;
                zC = zA <<= 16;
                if (yA < 0) {
                    xC -= i8 * yA;
                    xA -= i7 * yA;
                    zC -= j8 * yA;
                    zA -= j7 * yA;
                    yA = 0;
                }
                xB <<= 16;
                zB <<= 16;
                if (yB < 0) {
                    xB -= k7 * yB;
                    zB -= l7 * yB;
                    yB = 0;
                }
                final int k8 = yA - centreY;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (yA != yB && i8 < i7 || yA == yB && i8 > k7) {
                    yC -= yB;
                    yB -= yA;
                    yA = lineOffsets[yA];
                    while (--yB >= 0) {
                        method379(DrawingArea.pixels, texture, yA, xC >> 16, xA >> 16, zC >> 8, zA >> 8, l4, k5, j6, i5,
                            l5, k6);
                        xC += i8;
                        xA += i7;
                        zC += j8;
                        zA += j7;
                        yA += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--yC >= 0) {
                        method379(DrawingArea.pixels, texture, yA, xC >> 16, xB >> 16, zC >> 8, zB >> 8, l4, k5, j6, i5,
                            l5, k6);
                        xC += i8;
                        xB += k7;
                        zC += j8;
                        zB += l7;
                        yA += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                yC -= yB;
                yB -= yA;
                yA = lineOffsets[yA];
                while (--yB >= 0) {
                    method379(DrawingArea.pixels, texture, yA, xA >> 16, xC >> 16, zA >> 8, zC >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xC += i8;
                    xA += i7;
                    zC += j8;
                    zA += j7;
                    yA += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--yC >= 0) {
                    method379(DrawingArea.pixels, texture, yA, xB >> 16, xC >> 16, zB >> 8, zC >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xC += i8;
                    xB += k7;
                    zC += j8;
                    zB += l7;
                    yA += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            xB = xA <<= 16;
            zB = zA <<= 16;
            if (yA < 0) {
                xB -= i8 * yA;
                xA -= i7 * yA;
                zB -= j8 * yA;
                zA -= j7 * yA;
                yA = 0;
            }
            xC <<= 16;
            zC <<= 16;
            if (yC < 0) {
                xC -= k7 * yC;
                zC -= l7 * yC;
                yC = 0;
            }
            final int l8 = yA - centreY;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (yA != yC && i8 < i7 || yA == yC && k7 > i7) {
                yB -= yC;
                yC -= yA;
                yA = lineOffsets[yA];
                while (--yC >= 0) {
                    method379(DrawingArea.pixels, texture, yA, xB >> 16, xA >> 16, zB >> 8, zA >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xB += i8;
                    xA += i7;
                    zB += j8;
                    zA += j7;
                    yA += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--yB >= 0) {
                    method379(DrawingArea.pixels, texture, yA, xC >> 16, xA >> 16, zC >> 8, zA >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xC += k7;
                    xA += i7;
                    zC += l7;
                    zA += j7;
                    yA += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            yB -= yC;
            yC -= yA;
            yA = lineOffsets[yA];
            while (--yC >= 0) {
                method379(DrawingArea.pixels, texture, yA, xA >> 16, xB >> 16, zA >> 8, zB >> 8, l4, k5, j6, i5, l5,
                    k6);
                xB += i8;
                xA += i7;
                zB += j8;
                zA += j7;
                yA += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--yB >= 0) {
                method379(DrawingArea.pixels, texture, yA, xA >> 16, xC >> 16, zA >> 8, zC >> 8, l4, k5, j6, i5, l5,
                    k6);
                xC += k7;
                xA += i7;
                zC += l7;
                zA += j7;
                yA += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (yB <= yC) {
            if (yB >= DrawingArea.bottomY) {
                return;
            }
            if (yC > DrawingArea.bottomY) {
                yC = DrawingArea.bottomY;
            }
            if (yA > DrawingArea.bottomY) {
                yA = DrawingArea.bottomY;
            }
            if (yC < yA) {
                xA = xB <<= 16;
                zA = zB <<= 16;
                if (yB < 0) {
                    xA -= i7 * yB;
                    xB -= k7 * yB;
                    zA -= j7 * yB;
                    zB -= l7 * yB;
                    yB = 0;
                }
                xC <<= 16;
                zC <<= 16;
                if (yC < 0) {
                    xC -= i8 * yC;
                    zC -= j8 * yC;
                    yC = 0;
                }
                final int i9 = yB - centreY;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (yB != yC && i7 < k7 || yB == yC && i7 > i8) {
                    yA -= yC;
                    yC -= yB;
                    yB = lineOffsets[yB];
                    while (--yC >= 0) {
                        method379(DrawingArea.pixels, texture, yB, xA >> 16, xB >> 16, zA >> 8, zB >> 8, l4, k5, j6, i5,
                            l5, k6);
                        xA += i7;
                        xB += k7;
                        zA += j7;
                        zB += l7;
                        yB += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--yA >= 0) {
                        method379(DrawingArea.pixels, texture, yB, xA >> 16, xC >> 16, zA >> 8, zC >> 8, l4, k5, j6, i5,
                            l5, k6);
                        xA += i7;
                        xC += i8;
                        zA += j7;
                        zC += j8;
                        yB += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                yA -= yC;
                yC -= yB;
                yB = lineOffsets[yB];
                while (--yC >= 0) {
                    method379(DrawingArea.pixels, texture, yB, xB >> 16, xA >> 16, zB >> 8, zA >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xA += i7;
                    xB += k7;
                    zA += j7;
                    zB += l7;
                    yB += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--yA >= 0) {
                    method379(DrawingArea.pixels, texture, yB, xC >> 16, xA >> 16, zC >> 8, zA >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xA += i7;
                    xC += i8;
                    zA += j7;
                    zC += j8;
                    yB += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            xC = xB <<= 16;
            zC = zB <<= 16;
            if (yB < 0) {
                xC -= i7 * yB;
                xB -= k7 * yB;
                zC -= j7 * yB;
                zB -= l7 * yB;
                yB = 0;
            }
            xA <<= 16;
            zA <<= 16;
            if (yA < 0) {
                xA -= i8 * yA;
                zA -= j8 * yA;
                yA = 0;
            }
            final int j9 = yB - centreY;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                yC -= yA;
                yA -= yB;
                yB = lineOffsets[yB];
                while (--yA >= 0) {
                    method379(DrawingArea.pixels, texture, yB, xC >> 16, xB >> 16, zC >> 8, zB >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xC += i7;
                    xB += k7;
                    zC += j7;
                    zB += l7;
                    yB += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--yC >= 0) {
                    method379(DrawingArea.pixels, texture, yB, xA >> 16, xB >> 16, zA >> 8, zB >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xA += i8;
                    xB += k7;
                    zA += j8;
                    zB += l7;
                    yB += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            yC -= yA;
            yA -= yB;
            yB = lineOffsets[yB];
            while (--yA >= 0) {
                method379(DrawingArea.pixels, texture, yB, xB >> 16, xC >> 16, zB >> 8, zC >> 8, l4, k5, j6, i5, l5,
                    k6);
                xC += i7;
                xB += k7;
                zC += j7;
                zB += l7;
                yB += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--yC >= 0) {
                method379(DrawingArea.pixels, texture, yB, xB >> 16, xA >> 16, zB >> 8, zA >> 8, l4, k5, j6, i5, l5,
                    k6);
                xA += i8;
                xB += k7;
                zA += j8;
                zB += l7;
                yB += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (yC >= DrawingArea.bottomY) {
            return;
        }
        if (yA > DrawingArea.bottomY) {
            yA = DrawingArea.bottomY;
        }
        if (yB > DrawingArea.bottomY) {
            yB = DrawingArea.bottomY;
        }
        if (yA < yB) {
            xB = xC <<= 16;
            zB = zC <<= 16;
            if (yC < 0) {
                xB -= k7 * yC;
                xC -= i8 * yC;
                zB -= l7 * yC;
                zC -= j8 * yC;
                yC = 0;
            }
            xA <<= 16;
            zA <<= 16;
            if (yA < 0) {
                xA -= i7 * yA;
                zA -= j7 * yA;
                yA = 0;
            }
            final int k9 = yC - centreY;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                yB -= yA;
                yA -= yC;
                yC = lineOffsets[yC];
                while (--yA >= 0) {
                    method379(DrawingArea.pixels, texture, yC, xB >> 16, xC >> 16, zB >> 8, zC >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xB += k7;
                    xC += i8;
                    zB += l7;
                    zC += j8;
                    yC += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--yB >= 0) {
                    method379(DrawingArea.pixels, texture, yC, xB >> 16, xA >> 16, zB >> 8, zA >> 8, l4, k5, j6, i5, l5,
                        k6);
                    xB += k7;
                    xA += i7;
                    zB += l7;
                    zA += j7;
                    yC += DrawingArea.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            yB -= yA;
            yA -= yC;
            yC = lineOffsets[yC];
            while (--yA >= 0) {
                method379(DrawingArea.pixels, texture, yC, xC >> 16, xB >> 16, zC >> 8, zB >> 8, l4, k5, j6, i5, l5,
                    k6);
                xB += k7;
                xC += i8;
                zB += l7;
                zC += j8;
                yC += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--yB >= 0) {
                method379(DrawingArea.pixels, texture, yC, xA >> 16, xB >> 16, zA >> 8, zB >> 8, l4, k5, j6, i5, l5,
                    k6);
                xB += k7;
                xA += i7;
                zB += l7;
                zA += j7;
                yC += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        xA = xC <<= 16;
        zA = zC <<= 16;
        if (yC < 0) {
            xA -= k7 * yC;
            xC -= i8 * yC;
            zA -= l7 * yC;
            zC -= j8 * yC;
            yC = 0;
        }
        xB <<= 16;
        zB <<= 16;
        if (yB < 0) {
            xB -= i7 * yB;
            zB -= j7 * yB;
            yB = 0;
        }
        final int l9 = yC - centreY;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            yA -= yB;
            yB -= yC;
            yC = lineOffsets[yC];
            while (--yB >= 0) {
                method379(DrawingArea.pixels, texture, yC, xA >> 16, xC >> 16, zA >> 8, zC >> 8, l4, k5, j6, i5, l5,
                    k6);
                xA += k7;
                xC += i8;
                zA += l7;
                zC += j8;
                yC += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--yA >= 0) {
                method379(DrawingArea.pixels, texture, yC, xB >> 16, xC >> 16, zB >> 8, zC >> 8, l4, k5, j6, i5, l5,
                    k6);
                xB += i7;
                xC += i8;
                zB += j7;
                zC += j8;
                yC += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        yA -= yB;
        yB -= yC;
        yC = lineOffsets[yC];
        while (--yB >= 0) {
            method379(DrawingArea.pixels, texture, yC, xC >> 16, xA >> 16, zC >> 8, zA >> 8, l4, k5, j6, i5, l5, k6);
            xA += k7;
            xC += i8;
            zA += l7;
            zC += j8;
            yC += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--yA >= 0) {
            method379(DrawingArea.pixels, texture, yC, xC >> 16, xB >> 16, zC >> 8, zB >> 8, l4, k5, j6, i5, l5, k6);
            xB += i7;
            xC += i8;
            zB += j7;
            zC += j8;
            yC += DrawingArea.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
    }

    public static int getAverageTextureColour(final int textureId) {
        if (averageTextureColour[textureId] != 0) {
            return averageTextureColour[textureId];
        }
        int red = 0;
        int green = 0;
        int blue = 0;
        final int colourCount = texturePalettes[textureId].length;
        for (int k1 = 0; k1 < colourCount; k1++) {
            red += texturePalettes[textureId][k1] >> 16 & 0xff;
            green += texturePalettes[textureId][k1] >> 8 & 0xff;
            blue += texturePalettes[textureId][k1] & 0xff;
        }

        int rgb = (red / colourCount << 16) + (green / colourCount << 8) + blue / colourCount;
        rgb = adjustBrightness(rgb, 1.3999999999999999D);
        if (rgb == 0) {
            rgb = 1;
        }
        averageTextureColour[textureId] = rgb;
        return rgb;
    }

    private static int[] getTexturePixels(final int textureId) {
        textureLastUsed[textureId] = textureGetCount++;
        if (texelCache[textureId] != null) {
            return texelCache[textureId];
        }
        final int[] texels;
        if (texelPoolPointer > 0) {
            texels = texelArrayPool[--texelPoolPointer];
            texelArrayPool[texelPoolPointer] = null;
        } else {
            int lastUsed = 0;
            int target = -1;
            for (int t = 0; t < loadedTextureCount; t++) {
                if (texelCache[t] != null && (textureLastUsed[t] < lastUsed || target == -1)) {
                    lastUsed = textureLastUsed[t];
                    target = t;
                }
            }

            texels = texelCache[target];
            texelCache[target] = null;
        }
        texelCache[textureId] = texels;
        final IndexedImage background = textureImages[textureId];
        final int[] texturePalette = texturePalettes[textureId];
        if (lowMemory) {
            transparent[textureId] = false;
            for (int texelPointer = 0; texelPointer < 4096; texelPointer++) {
                final int texel = texels[texelPointer] = texturePalette[background.pixels[texelPointer]] & 0xf8f8ff;
                if (texel == 0) {
                    transparent[textureId] = true;
                }
                texels[4096 + texelPointer] = texel - (texel >>> 3) & 0xf8f8ff;
                texels[8192 + texelPointer] = texel - (texel >>> 2) & 0xf8f8ff;
                texels[12288 + texelPointer] = texel - (texel >>> 2) - (texel >>> 3) & 0xf8f8ff;
            }

        } else {
            if (background.width == 64) {
                for (int y = 0; y < 128; y++) {
                    for (int x = 0; x < 128; x++) {
                        texels[x + (y << 7)] = texturePalette[background.pixels[(x >> 1) + ((y >> 1) << 6)]];
                    }

                }

            } else {
                for (int texelPointer = 0; texelPointer < 16384; texelPointer++) {
                    texels[texelPointer] = texturePalette[background.pixels[texelPointer]];
                }

            }
            transparent[textureId] = false;
            for (int texelPointer = 0; texelPointer < 16384; texelPointer++) {
                texels[texelPointer] &= 0xf8f8ff;
                final int texel = texels[texelPointer];
                if (texel == 0) {
                    transparent[textureId] = true;
                }
                texels[16384 + texelPointer] = texel - (texel >>> 3) & 0xf8f8ff;
                texels[32768 + texelPointer] = texel - (texel >>> 2) & 0xf8f8ff;
                texels[49152 + texelPointer] = texel - (texel >>> 2) - (texel >>> 3) & 0xf8f8ff;
            }

        }
        return texels;
    }

    private static void method375(final int[] ai, int i, int l, int i1, int j1, final int k1) {
        int j;// was parameter
        int k;// was parameter
        if (textured) {
            int l1;
            if (restrictEdges) {
                if (i1 - l > 3) {
                    l1 = (k1 - j1) / (i1 - l);
                } else {
                    l1 = 0;
                }
                if (i1 > DrawingArea.centerX) {
                    i1 = DrawingArea.centerX;
                }
                if (l < 0) {
                    j1 -= l * l1;
                    l = 0;
                }
                if (l >= i1) {
                    return;
                }
                i += l;
                k = i1 - l >> 2;
                l1 <<= 2;
            } else {
                if (l >= i1) {
                    return;
                }
                i += l;
                k = i1 - l >> 2;
                if (k > 0) {
                    l1 = (k1 - j1) * anIntArray1468[k] >> 15;
                } else {
                    l1 = 0;
                }
            }
            if (alpha == 0) {
                while (--k >= 0) {
                    j = HSL_TO_RGB[j1 >> 8];
                    j1 += l1;
                    ai[i++] = j;
                    ai[i++] = j;
                    ai[i++] = j;
                    ai[i++] = j;
                }
                k = i1 - l & 3;
                if (k > 0) {
                    j = HSL_TO_RGB[j1 >> 8];
                    do {
                        ai[i++] = j;
                    }
                    while (--k > 0);
                    return;
                }
            } else {
                final int j2 = alpha;
                final int l2 = 256 - alpha;
                while (--k >= 0) {
                    j = HSL_TO_RGB[j1 >> 8];
                    j1 += l1;
                    j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
                    ai[i++] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
                    ai[i++] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
                    ai[i++] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
                    ai[i++] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
                }
                k = i1 - l & 3;
                if (k > 0) {
                    j = HSL_TO_RGB[j1 >> 8];
                    j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
                    do {
                        ai[i++] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
                    }
                    while (--k > 0);
                }
            }
            return;
        }
        if (l >= i1) {
            return;
        }
        final int i2 = (k1 - j1) / (i1 - l);
        if (restrictEdges) {
            if (i1 > DrawingArea.centerX) {
                i1 = DrawingArea.centerX;
            }
            if (l < 0) {
                j1 -= l * i2;
                l = 0;
            }
            if (l >= i1) {
                return;
            }
        }
        i += l;
        k = i1 - l;
        if (alpha == 0) {
            do {
                ai[i++] = HSL_TO_RGB[j1 >> 8];
                j1 += i2;
            } while (--k > 0);
            return;
        }
        final int k2 = alpha;
        final int i3 = 256 - alpha;
        do {
            j = HSL_TO_RGB[j1 >> 8];
            j1 += i2;
            j = ((j & 0xff00ff) * i3 >> 8 & 0xff00ff) + ((j & 0xff00) * i3 >> 8 & 0xff00);
            ai[i++] = j + ((ai[i] & 0xff00ff) * k2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * k2 >> 8 & 0xff00);
        } while (--k > 0);
    }

    private static void method377(final int[] ai, int i, int j, int l, int i1) {
        int k;// was parameter
        if (restrictEdges) {
            if (i1 > DrawingArea.centerX) {
                i1 = DrawingArea.centerX;
            }
            if (l < 0) {
                l = 0;
            }
        }
        if (l >= i1) {
            return;
        }
        i += l;
        k = i1 - l >> 2;
        if (alpha == 0) {
            while (--k >= 0) {
                ai[i++] = j;
                ai[i++] = j;
                ai[i++] = j;
                ai[i++] = j;
            }
            for (k = i1 - l & 3; --k >= 0; ) {
                ai[i++] = j;
            }

            return;
        }
        final int j1 = alpha;
        final int k1 = 256 - alpha;
        j = ((j & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((j & 0xff00) * k1 >> 8 & 0xff00);
        while (--k >= 0) {
            ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
            ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
            ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
            ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
        }
        for (k = i1 - l & 3; --k >= 0; ) {
            ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
        }

    }

    private static void method379(final int[] ai, final int[] ai1, int k, int l, int i1, int j1, final int k1, int l1, int i2, int j2,
                                  final int k2, final int l2, final int i3) {
        int i = 0;// was parameter
        int j = 0;// was parameter
        if (l >= i1) {
            return;
        }
        int j3;
        int k3;
        if (restrictEdges) {
            j3 = (k1 - j1) / (i1 - l);
            if (i1 > DrawingArea.centerX) {
                i1 = DrawingArea.centerX;
            }
            if (l < 0) {
                j1 -= l * j3;
                l = 0;
            }
            if (l >= i1) {
                return;
            }
            k3 = i1 - l >> 3;
            j3 <<= 12;
            j1 <<= 9;
        } else {
            if (i1 - l > 7) {
                k3 = i1 - l >> 3;
                j3 = (k1 - j1) * anIntArray1468[k3] >> 6;
            } else {
                k3 = 0;
                j3 = 0;
            }
            j1 <<= 9;
        }
        k += l;
        if (lowMemory) {
            int i4 = 0;
            int k4 = 0;
            final int k6 = l - centreX;
            l1 += (k2 >> 3) * k6;
            i2 += (l2 >> 3) * k6;
            j2 += (i3 >> 3) * k6;
            int i5 = j2 >> 12;
            if (i5 != 0) {
                i = l1 / i5;
                j = i2 / i5;
                if (i < 0) {
                    i = 0;
                } else if (i > 4032) {
                    i = 4032;
                }
            }
            l1 += k2;
            i2 += l2;
            j2 += i3;
            i5 = j2 >> 12;
            if (i5 != 0) {
                i4 = l1 / i5;
                k4 = i2 / i5;
                if (i4 < 7) {
                    i4 = 7;
                } else if (i4 > 4032) {
                    i4 = 4032;
                }
            }
            int i7 = i4 - i >> 3;
            int k7 = k4 - j >> 3;
            i += (j1 & 0x600000) >> 3;
            int i8 = j1 >> 23;
            if (opaque) {
                while (k3-- > 0) {
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i = i4;
                    j = k4;
                    l1 += k2;
                    i2 += l2;
                    j2 += i3;
                    final int j5 = j2 >> 12;
                    if (j5 != 0) {
                        i4 = l1 / j5;
                        k4 = i2 / j5;
                        if (i4 < 7) {
                            i4 = 7;
                        } else if (i4 > 4032) {
                            i4 = 4032;
                        }
                    }
                    i7 = i4 - i >> 3;
                    k7 = k4 - j >> 3;
                    j1 += j3;
                    i += (j1 & 0x600000) >> 3;
                    i8 = j1 >> 23;
                }
                for (k3 = i1 - l & 7; k3-- > 0; ) {
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                }

                return;
            }
            while (k3-- > 0) {
                int k8;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i = i4;
                j = k4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                final int k5 = j2 >> 12;
                if (k5 != 0) {
                    i4 = l1 / k5;
                    k4 = i2 / k5;
                    if (i4 < 7) {
                        i4 = 7;
                    } else if (i4 > 4032) {
                        i4 = 4032;
                    }
                }
                i7 = i4 - i >> 3;
                k7 = k4 - j >> 3;
                j1 += j3;
                i += (j1 & 0x600000) >> 3;
                i8 = j1 >> 23;
            }
            for (k3 = i1 - l & 7; k3-- > 0; ) {
                final int l8;
                if ((l8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = l8;
                }
                k++;
                i += i7;
                j += k7;
            }

            return;
        }
        int j4 = 0;
        int l4 = 0;
        final int l6 = l - centreX;
        l1 += (k2 >> 3) * l6;
        i2 += (l2 >> 3) * l6;
        j2 += (i3 >> 3) * l6;
        int l5 = j2 >> 14;
        if (l5 != 0) {
            i = l1 / l5;
            j = i2 / l5;
            if (i < 0) {
                i = 0;
            } else if (i > 16256) {
                i = 16256;
            }
        }
        l1 += k2;
        i2 += l2;
        j2 += i3;
        l5 = j2 >> 14;
        if (l5 != 0) {
            j4 = l1 / l5;
            l4 = i2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - i >> 3;
        int l7 = l4 - j >> 3;
        i += j1 & 0x600000;
        int j8 = j1 >> 23;
        if (opaque) {
            while (k3-- > 0) {
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i = j4;
                j = l4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                final int i6 = j2 >> 14;
                if (i6 != 0) {
                    j4 = l1 / i6;
                    l4 = i2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                j7 = j4 - i >> 3;
                l7 = l4 - j >> 3;
                j1 += j3;
                i += j1 & 0x600000;
                j8 = j1 >> 23;
            }
            for (k3 = i1 - l & 7; k3-- > 0; ) {
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
            }

            return;
        }
        while (k3-- > 0) {
            int i9;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i = j4;
            j = l4;
            l1 += k2;
            i2 += l2;
            j2 += i3;
            final int j6 = j2 >> 14;
            if (j6 != 0) {
                j4 = l1 / j6;
                l4 = i2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - i >> 3;
            l7 = l4 - j >> 3;
            j1 += j3;
            i += j1 & 0x600000;
            j8 = j1 >> 23;
        }
        for (int l3 = i1 - l & 7; l3-- > 0; ) {
            final int j9;
            if ((j9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = j9;
            }
            k++;
            i += j7;
            j += l7;
        }

    }

    public static void nullLoader() {
        anIntArray1468 = null;
        anIntArray1468 = null;
        SINE = null;
        COSINE = null;
        lineOffsets = null;
        textureImages = null;
        transparent = null;
        averageTextureColour = null;
        texelArrayPool = null;
        texelCache = null;
        textureLastUsed = null;
        HSL_TO_RGB = null;
        texturePalettes = null;
    }

    public static void resetTexture(final int textureId) {
        if (texelCache[textureId] == null) {
            return;
        }
        texelArrayPool[texelPoolPointer++] = texelCache[textureId];
        texelCache[textureId] = null;
    }

    public static void resetTextures() {
        if (texelArrayPool == null) {
            texelPoolPointer = 20;
            if (lowMemory) {
                texelArrayPool = new int[texelPoolPointer][16384];
            } else {
                texelArrayPool = new int[texelPoolPointer][0x10000];
            }
            for (int i = 0; i < 50; i++) {
                texelCache[i] = null;
            }

        }
    }

    public static void setBounds(final int width, final int height) {
        lineOffsets = new int[height];
        for (int y = 0; y < height; y++) {
            lineOffsets[y] = width * y;
        }

        centreX = width / 2;
        centreY = height / 2;
    }

    public static void setDefaultBounds() {
        lineOffsets = new int[DrawingArea.height];
        for (int y = 0; y < DrawingArea.height; y++) {
            lineOffsets[y] = DrawingArea.width * y;
        }

        centreX = DrawingArea.width / 2;
        centreY = DrawingArea.height / 2;
    }

    public static void unpackTextures(final Archive archive) {
        loadedTextureCount = 0;
        for (int i = 0; i < 50; i++) {
            try {
                textureImages[i] = new IndexedImage(archive, String.valueOf(i), 0);
                if (lowMemory && textureImages[i].resizeWidth == 128) {
                    textureImages[i].resizeToHalf();
                } else {
                    textureImages[i].resize();
                }
                loadedTextureCount++;
            } catch (final Exception _ex) {
            }
        }

    }

    public static final int anInt1459 = -477;
    public static boolean lowMemory = true;
    public static boolean restrictEdges;
    private static boolean opaque;
    public static boolean textured = true;
    public static int alpha;
    public static int centreX;
    public static int centreY;
    private static int[] anIntArray1468;
    public static final int[] anIntArray1469;
    public static int[] SINE;
    public static int[] COSINE;
    public static int[] lineOffsets;
    private static int loadedTextureCount;
    public static IndexedImage[] textureImages = new IndexedImage[50];
    private static boolean[] transparent = new boolean[50];
    private static int[] averageTextureColour = new int[50];
    private static int texelPoolPointer;
    private static int[][] texelArrayPool;
    private static int[][] texelCache = new int[50][];
    public static int[] textureLastUsed = new int[50];
    public static int textureGetCount;
    public static int[] HSL_TO_RGB = new int[0x10000];
    private static int[][] texturePalettes = new int[50][];

    static {
        anIntArray1468 = new int[512];
        anIntArray1469 = new int[2048];
        SINE = new int[2048];
        COSINE = new int[2048];
        for (int i = 1; i < 512; i++) {
            anIntArray1468[i] = 32768 / i;
        }

        for (int j = 1; j < 2048; j++) {
            anIntArray1469[j] = 0x10000 / j;
        }

        for (int k = 0; k < 2048; k++) {
            SINE[k] = (int) (65536D * Math.sin(k * 0.0030679614999999999D));
            COSINE[k] = (int) (65536D * Math.cos(k * 0.0030679614999999999D));
        }

    }
}
