package com.jagex.runescape;

public final class CollisionMap {

    private final int insetX;

    private final int insetY;

    private final int width;

    private final int height;

    public final int[][] clippingData;

    public CollisionMap() {
        this.insetX = 0;
        this.insetY = 0;
        this.width = 104;
        this.height = 104;
        this.clippingData = new int[this.width][this.height];
        this.reset();
    }

    public void markBlocked(int x, int y) {
        x -= this.insetX;
        y -= this.insetY;
        this.clippingData[x][y] |= 0x200000;
    }

    public void markSolidOccupant(int x, int y, int width, int height, final int orientation, final boolean impenetrable) {
        int occupied = 256;
        if (impenetrable) {
            occupied += 0x20000;
        }
        x -= this.insetX;
        y -= this.insetY;
        if (orientation == 1 || orientation == 3) {
            final int temp = width;
            width = height;
            height = temp;
        }
        for (int _x = x; _x < x + width; _x++) {
            if (_x >= 0 && _x < this.width) {
                for (int _y = y; _y < y + height; _y++) {
                    if (_y >= 0 && _y < this.height) {
                        this.set(_x, _y, occupied);
                    }
                }

            }
        }

    }

    public void markWall(int y, final int orientation, int x, final int position, final boolean impenetrable) {
        x -= this.insetX;
        y -= this.insetY;

        if (position == 0) {
            if (orientation == 0) {
                this.set(x, y, 128);
                this.set(x - 1, y, 8);
            }
            if (orientation == 1) {
                this.set(x, y, 2);
                this.set(x, y + 1, 32);
            }
            if (orientation == 2) {
                this.set(x, y, 8);
                this.set(x + 1, y, 128);
            }
            if (orientation == 3) {
                this.set(x, y, 32);
                this.set(x, y - 1, 2);
            }
        }
        if (position == 1 || position == 3) {
            if (orientation == 0) {
                this.set(x, y, 1);
                this.set(x - 1, y + 1, 16);
            }
            if (orientation == 1) {
                this.set(x, y, 4);
                this.set(x + 1, y + 1, 64);
            }
            if (orientation == 2) {
                this.set(x, y, 16);
                this.set(x + 1, y - 1, 1);
            }
            if (orientation == 3) {
                this.set(x, y, 64);
                this.set(x - 1, y - 1, 4);
            }
        }
        if (position == 2) {
            if (orientation == 0) {
                this.set(x, y, 130);
                this.set(x - 1, y, 8);
                this.set(x, y + 1, 32);
            }
            if (orientation == 1) {
                this.set(x, y, 10);
                this.set(x, y + 1, 32);
                this.set(x + 1, y, 128);
            }
            if (orientation == 2) {
                this.set(x, y, 40);
                this.set(x + 1, y, 128);
                this.set(x, y - 1, 2);
            }
            if (orientation == 3) {
                this.set(x, y, 160);
                this.set(x, y - 1, 2);
                this.set(x - 1, y, 8);
            }
        }
        if (impenetrable) {
            if (position == 0) {
                if (orientation == 0) {
                    this.set(x, y, 0x10000);
                    this.set(x - 1, y, 4096);
                }
                if (orientation == 1) {
                    this.set(x, y, 1024);
                    this.set(x, y + 1, 16384);
                }
                if (orientation == 2) {
                    this.set(x, y, 4096);
                    this.set(x + 1, y, 0x10000);
                }
                if (orientation == 3) {
                    this.set(x, y, 16384);
                    this.set(x, y - 1, 1024);
                }
            }
            if (position == 1 || position == 3) {
                if (orientation == 0) {
                    this.set(x, y, 512);
                    this.set(x - 1, y + 1, 8192);
                }
                if (orientation == 1) {
                    this.set(x, y, 2048);
                    this.set(x + 1, y + 1, 32768);
                }
                if (orientation == 2) {
                    this.set(x, y, 8192);
                    this.set(x + 1, y - 1, 512);
                }
                if (orientation == 3) {
                    this.set(x, y, 32768);
                    this.set(x - 1, y - 1, 2048);
                }
            }
            if (position == 2) {
                if (orientation == 0) {
                    this.set(x, y, 0x10400);
                    this.set(x - 1, y, 4096);
                    this.set(x, y + 1, 16384);
                }
                if (orientation == 1) {
                    this.set(x, y, 5120);
                    this.set(x, y + 1, 16384);
                    this.set(x + 1, y, 0x10000);
                }
                if (orientation == 2) {
                    this.set(x, y, 20480);
                    this.set(x + 1, y, 0x10000);
                    this.set(x, y - 1, 1024);
                }
                if (orientation == 3) {
                    this.set(x, y, 0x14000);
                    this.set(x, y - 1, 1024);
                    this.set(x - 1, y, 4096);
                }
            }
        }
    }

    public boolean reachedFacingObject(final int startX, final int startY, final int endX, final int endY, final int endDistanceX, final int endDistanceY,
                                       final int surroundings) {
        final int endX2 = (endX + endDistanceX) - 1;
        final int endY2 = (endY + endDistanceY) - 1;
        if (startX >= endX && startX <= endX2 && startY >= endY && startY <= endY2) {
            return true;
        }
        if (startX == endX - 1 && startY >= endY && startY <= endY2
            && (this.clippingData[startX - this.insetX][startY - this.insetY] & 8) == 0 && (surroundings & 8) == 0) {
            return true;
        }
        if (startX == endX2 + 1 && startY >= endY && startY <= endY2
            && (this.clippingData[startX - this.insetX][startY - this.insetY] & 0x80) == 0 && (surroundings & 2) == 0) {
            return true;
        }
        return startY == endY - 1 && startX >= endX && startX <= endX2
            && (this.clippingData[startX - this.insetX][startY - this.insetY] & 2) == 0 && (surroundings & 4) == 0
            || startY == endY2 + 1 && startX >= endX && startX <= endX2
            && (this.clippingData[startX - this.insetX][startY - this.insetY] & 0x20) == 0 && (surroundings & 1) == 0;
    }

    public boolean reachedWall(int startX, int startY, int endX, int endY, final int endPosition, final int endOrientation) {
        if (startX == endX && startY == endY) {
            return true;
        }
        startX -= this.insetX;
        startY -= this.insetY;
        endX -= this.insetX;
        endY -= this.insetY;
        if (endPosition == 0) {
            if (endOrientation == 0) {
                if (startX == endX - 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 1) {
                if (startX == endX && startY == endY + 1) {
                    return true;
                }
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
            } else if (endOrientation == 2) {
                if (startX == endX + 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 3) {
                if (startX == endX && startY == endY - 1) {
                    return true;
                }
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
            }
        }
        if (endPosition == 2) {
            if (endOrientation == 0) {
                if (startX == endX - 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 1) {
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 2) {
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY - 1) {
                    return true;
                }
            } else if (endOrientation == 3) {
                if (startX == endX - 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1) {
                    return true;
                }
            }
        }
        if (endPosition == 9) {
            if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x20) == 0) {
                return true;
            }
            if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x02) == 0) {
                return true;
            }
            if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x08) == 0) {
                return true;
            }
            if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x80) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean reachedWallDecoration(int startX, int startY, int endX, int endY, final int endPosition,
                                         int endOrientation) {
        if (startX == endX && startY == endY) {
            return true;
        }
        startX -= this.insetX;
        startY -= this.insetY;
        endX -= this.insetX;
        endY -= this.insetY;
        if (endPosition == 6 || endPosition == 7) {
            if (endPosition == 7) {
                endOrientation = endOrientation + 2 & 3;
            }
            if (endOrientation == 0) {
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x80) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 2) == 0) {
                    return true;
                }
            } else if (endOrientation == 1) {
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 8) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 2) == 0) {
                    return true;
                }
            } else if (endOrientation == 2) {
                if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 8) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x20) == 0) {
                    return true;
                }
            } else if (endOrientation == 3) {
                if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x80) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x20) == 0) {
                    return true;
                }
            }
        }
        if (endPosition == 8) {
            if (startX == endX && startY == endY + 1 && (this.clippingData[startX][startY] & 0x20) == 0) {
                return true;
            }
            if (startX == endX && startY == endY - 1 && (this.clippingData[startX][startY] & 0x02) == 0) {
                return true;
            }
            if (startX == endX - 1 && startY == endY && (this.clippingData[startX][startY] & 0x08) == 0) {
                return true;
            }            
            if (startX == endX + 1 && startY == endY && (this.clippingData[startX][startY] & 0x80) == 0) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1) {
                    this.clippingData[x][y] = 0xffffff;
                } else {
                    this.clippingData[x][y] = 0x1000000;
                }
            }

        }

    }

    private void set(final int x, final int y, final int flag) {
        this.clippingData[x][y] |= flag;
    }

    public void unmarkConcealed(int x, int y) {
        x -= this.insetX;
        y -= this.insetY;
        this.clippingData[x][y] &= 0xdfffff;
    }

    public void unmarkSolidOccupant(int x, int y, int width, int height, final int orientation, final boolean impenetrable) {
        int occupied = 256;
        if (impenetrable) {
            occupied += 0x20000;
        }
        x -= this.insetX;
        y -= this.insetY;
        if (orientation == 1 || orientation == 3) {
            final int temp = width;
            width = height;
            height = temp;
        }
        for (int _x = x; _x < x + width; _x++) {
            if (_x >= 0 && _x < this.width) {
                for (int _y = y; _y < y + height; _y++) {
                    if (_y >= 0 && _y < this.height) {
                        this.unset(_x, _y, occupied);
                    }
                }

            }
        }

    }

    public void unmarkWall(int x, int y, final int position, final int orientation, final boolean impenetrable) {
        x -= this.insetX;
        y -= this.insetY;
        if (position == 0) {
            if (orientation == 0) {
                this.unset(x, y, 128);
                this.unset(x - 1, y, 8);
            }
            if (orientation == 1) {
                this.unset(x, y, 2);
                this.unset(x, y + 1, 32);
            }
            if (orientation == 2) {
                this.unset(x, y, 8);
                this.unset(x + 1, y, 128);
            }
            if (orientation == 3) {
                this.unset(x, y, 32);
                this.unset(x, y - 1, 2);
            }
        }
        if (position == 1 || position == 3) {
            if (orientation == 0) {
                this.unset(x, y, 1);
                this.unset(x - 1, y + 1, 16);
            }
            if (orientation == 1) {
                this.unset(x, y, 4);
                this.unset(x + 1, y + 1, 64);
            }
            if (orientation == 2) {
                this.unset(x, y, 16);
                this.unset(x + 1, y - 1, 1);
            }
            if (orientation == 3) {
                this.unset(x, y, 64);
                this.unset(x - 1, y - 1, 4);
            }
        }
        if (position == 2) {
            if (orientation == 0) {
                this.unset(x, y, 130);
                this.unset(x - 1, y, 8);
                this.unset(x, y + 1, 32);
            }
            if (orientation == 1) {
                this.unset(x, y, 10);
                this.unset(x, y + 1, 32);
                this.unset(x + 1, y, 128);
            }
            if (orientation == 2) {
                this.unset(x, y, 40);
                this.unset(x + 1, y, 128);
                this.unset(x, y - 1, 2);
            }
            if (orientation == 3) {
                this.unset(x, y, 160);
                this.unset(x, y - 1, 2);
                this.unset(x - 1, y, 8);
            }
        }
        if (impenetrable) {
            if (position == 0) {
                if (orientation == 0) {
                    this.unset(x, y, 0x10000);
                    this.unset(x - 1, y, 4096);
                }
                if (orientation == 1) {
                    this.unset(x, y, 1024);
                    this.unset(x, y + 1, 16384);
                }
                if (orientation == 2) {
                    this.unset(x, y, 4096);
                    this.unset(x + 1, y, 0x10000);
                }
                if (orientation == 3) {
                    this.unset(x, y, 16384);
                    this.unset(x, y - 1, 1024);
                }
            }
            if (position == 1 || position == 3) {
                if (orientation == 0) {
                    this.unset(x, y, 512);
                    this.unset(x - 1, y + 1, 8192);
                }
                if (orientation == 1) {
                    this.unset(x, y, 2048);
                    this.unset(x + 1, y + 1, 32768);
                }
                if (orientation == 2) {
                    this.unset(x, y, 8192);
                    this.unset(x + 1, y - 1, 512);
                }
                if (orientation == 3) {
                    this.unset(x, y, 32768);
                    this.unset(x - 1, y - 1, 2048);
                }
            }
            if (position == 2) {
                if (orientation == 0) {
                    this.unset(x, y, 0x10400);
                    this.unset(x - 1, y, 4096);
                    this.unset(x, y + 1, 16384);
                }
                if (orientation == 1) {
                    this.unset(x, y, 5120);
                    this.unset(x, y + 1, 16384);
                    this.unset(x + 1, y, 0x10000);
                }
                if (orientation == 2) {
                    this.unset(x, y, 20480);
                    this.unset(x + 1, y, 0x10000);
                    this.unset(x, y - 1, 1024);
                }
                if (orientation == 3) {
                    this.unset(x, y, 0x14000);
                    this.unset(x, y - 1, 1024);
                    this.unset(x - 1, y, 4096);
                }
            }
        }
    }

    private void unset(final int x, final int y, final int flag) {
        this.clippingData[x][y] &= 0xffffff - flag;
    }
}
