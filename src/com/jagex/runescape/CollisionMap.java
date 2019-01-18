package com.jagex.runescape;

public final class CollisionMap {

	private final int insetX;

	private final int insetY;

	private final int width;

	private final int height;

	public final int[][] clippingData;

	public CollisionMap() {
		insetX = 0;
		insetY = 0;
		width = 104;
		height = 104;
		clippingData = new int[width][height];
		reset();
	}

	public void markBlocked(int x, int y) {
		x -= insetX;
		y -= insetY;
		clippingData[x][y] |= 0x200000;
	}

	public void markSolidOccupant(int x, int y, int width, int height, int orientation, boolean impenetrable) {
		int occupied = 256;
		if (impenetrable) {
            occupied += 0x20000;
        }
		x -= insetX;
		y -= insetY;
		if (orientation == 1 || orientation == 3) {
			int temp = width;
			width = height;
			height = temp;
		}
		for (int _x = x; _x < x + width; _x++) {
            if (_x >= 0 && _x < this.width) {
                for (int _y = y; _y < y + height; _y++) {
                    if (_y >= 0 && _y < this.height) {
                        set(_x, _y, occupied);
                    }
                }

            }
        }

	}

	public void markWall(int y, int orientation, int x, int position, boolean impenetrable) {
		x -= insetX;
		y -= insetY;

		if (position == 0) {
			if (orientation == 0) {
				set(x, y, 128);
				set(x - 1, y, 8);
			}
			if (orientation == 1) {
				set(x, y, 2);
				set(x, y + 1, 32);
			}
			if (orientation == 2) {
				set(x, y, 8);
				set(x + 1, y, 128);
			}
			if (orientation == 3) {
				set(x, y, 32);
				set(x, y - 1, 2);
			}
		}
		if (position == 1 || position == 3) {
			if (orientation == 0) {
				set(x, y, 1);
				set(x - 1, y + 1, 16);
			}
			if (orientation == 1) {
				set(x, y, 4);
				set(x + 1, y + 1, 64);
			}
			if (orientation == 2) {
				set(x, y, 16);
				set(x + 1, y - 1, 1);
			}
			if (orientation == 3) {
				set(x, y, 64);
				set(x - 1, y - 1, 4);
			}
		}
		if (position == 2) {
			if (orientation == 0) {
				set(x, y, 130);
				set(x - 1, y, 8);
				set(x, y + 1, 32);
			}
			if (orientation == 1) {
				set(x, y, 10);
				set(x, y + 1, 32);
				set(x + 1, y, 128);
			}
			if (orientation == 2) {
				set(x, y, 40);
				set(x + 1, y, 128);
				set(x, y - 1, 2);
			}
			if (orientation == 3) {
				set(x, y, 160);
				set(x, y - 1, 2);
				set(x - 1, y, 8);
			}
		}
		if (impenetrable) {
			if (position == 0) {
				if (orientation == 0) {
					set(x, y, 0x10000);
					set(x - 1, y, 4096);
				}
				if (orientation == 1) {
					set(x, y, 1024);
					set(x, y + 1, 16384);
				}
				if (orientation == 2) {
					set(x, y, 4096);
					set(x + 1, y, 0x10000);
				}
				if (orientation == 3) {
					set(x, y, 16384);
					set(x, y - 1, 1024);
				}
			}
			if (position == 1 || position == 3) {
				if (orientation == 0) {
					set(x, y, 512);
					set(x - 1, y + 1, 8192);
				}
				if (orientation == 1) {
					set(x, y, 2048);
					set(x + 1, y + 1, 32768);
				}
				if (orientation == 2) {
					set(x, y, 8192);
					set(x + 1, y - 1, 512);
				}
				if (orientation == 3) {
					set(x, y, 32768);
					set(x - 1, y - 1, 2048);
				}
			}
			if (position == 2) {
				if (orientation == 0) {
					set(x, y, 0x10400);
					set(x - 1, y, 4096);
					set(x, y + 1, 16384);
				}
				if (orientation == 1) {
					set(x, y, 5120);
					set(x, y + 1, 16384);
					set(x + 1, y, 0x10000);
				}
				if (orientation == 2) {
					set(x, y, 20480);
					set(x + 1, y, 0x10000);
					set(x, y - 1, 1024);
				}
				if (orientation == 3) {
					set(x, y, 0x14000);
					set(x, y - 1, 1024);
					set(x - 1, y, 4096);
				}
			}
		}
	}

	public boolean reachedFacingObject(int startX, int startY, int endX, int endY, int endDistanceX, int endDistanceY,
			int surroundings) {
		int endX2 = (endX + endDistanceX) - 1;
		int endY2 = (endY + endDistanceY) - 1;
		if (startX >= endX && startX <= endX2 && startY >= endY && startY <= endY2) {
            return true;
        }
		if (startX == endX - 1 && startY >= endY && startY <= endY2
				&& (clippingData[startX - insetX][startY - insetY] & 8) == 0 && (surroundings & 8) == 0) {
            return true;
        }
		if (startX == endX2 + 1 && startY >= endY && startY <= endY2
				&& (clippingData[startX - insetX][startY - insetY] & 0x80) == 0 && (surroundings & 2) == 0) {
            return true;
        }
		return startY == endY - 1 && startX >= endX && startX <= endX2
				&& (clippingData[startX - insetX][startY - insetY] & 2) == 0 && (surroundings & 4) == 0
				|| startY == endY2 + 1 && startX >= endX && startX <= endX2
						&& (clippingData[startX - insetX][startY - insetY] & 0x20) == 0 && (surroundings & 1) == 0;
	}

	public boolean reachedWall(int startX, int startY, int endX, int endY, int endPosition, int endOrientation) {
		if (startX == endX && startY == endY) {
            return true;
        }
		startX -= insetX;
		startY -= insetY;
		endX -= insetX;
		endY -= insetY;
		if (endPosition == 0) {
            if (endOrientation == 0) {
                if (startX == endX - 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 1) {
                if (startX == endX && startY == endY + 1) {
                    return true;
                }
                if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
            } else if (endOrientation == 2) {
                if (startX == endX + 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 3) {
                if (startX == endX && startY == endY - 1) {
                    return true;
                }
                if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x1280180) == 0) {
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
                if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 1) {
                if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY) {
                    return true;
                }
                if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 0x1280102) == 0) {
                    return true;
                }
            } else if (endOrientation == 2) {
                if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 0x1280108) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x1280120) == 0) {
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
                if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x1280120) == 0) {
                    return true;
                }
                if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x1280180) == 0) {
                    return true;
                }
                if (startX == endX && startY == endY - 1) {
                    return true;
                }
            }
        }
		if (endPosition == 9) {
			if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x20) == 0) {
                return true;
            }
			if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 2) == 0) {
                return true;
            }
			if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 8) == 0) {
                return true;
            }
			if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x80) == 0) {
                return true;
            }
		}
		return false;
	}

	public boolean reachedWallDecoration(int startX, int startY, int endX, int endY, int endPosition,
			int endOrientation) {
		if (startX == endX && startY == endY) {
            return true;
        }
		startX -= insetX;
		startY -= insetY;
		endX -= insetX;
		endY -= insetY;
		if (endPosition == 6 || endPosition == 7) {
			if (endPosition == 7) {
                endOrientation = endOrientation + 2 & 3;
            }
			if (endOrientation == 0) {
				if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x80) == 0) {
                    return true;
                }
				if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 2) == 0) {
                    return true;
                }
			} else if (endOrientation == 1) {
				if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 8) == 0) {
                    return true;
                }
				if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 2) == 0) {
                    return true;
                }
			} else if (endOrientation == 2) {
				if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 8) == 0) {
                    return true;
                }
				if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x20) == 0) {
                    return true;
                }
			} else if (endOrientation == 3) {
				if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x80) == 0) {
                    return true;
                }
				if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x20) == 0) {
                    return true;
                }
			}
		}
		if (endPosition == 8) {
			if (startX == endX && startY == endY + 1 && (clippingData[startX][startY] & 0x20) == 0) {
                return true;
            }
			if (startX == endX && startY == endY - 1 && (clippingData[startX][startY] & 2) == 0) {
                return true;
            }
			if (startX == endX - 1 && startY == endY && (clippingData[startX][startY] & 8) == 0) {
                return true;
            }
			if (startX == endX + 1 && startY == endY && (clippingData[startX][startY] & 0x80) == 0) {
                return true;
            }
		}
		return false;
	}

	public void reset() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    clippingData[x][y] = 0xffffff;
                } else {
                    clippingData[x][y] = 0x1000000;
                }
            }

		}

	}

	private void set(int x, int y, int flag) {
		clippingData[x][y] |= flag;
	}

	public void unmarkConcealed(int x, int y) {
		x -= insetX;
		y -= insetY;
		clippingData[x][y] &= 0xdfffff;
	}

	public void unmarkSolidOccupant(int x, int y, int width, int height, int orientation, boolean impenetrable) {
		int occupied = 256;
		if (impenetrable) {
            occupied += 0x20000;
        }
		x -= insetX;
		y -= insetY;
		if (orientation == 1 || orientation == 3) {
			int temp = width;
			width = height;
			height = temp;
		}
		for (int _x = x; _x < x + width; _x++) {
            if (_x >= 0 && _x < this.width) {
                for (int _y = y; _y < y + height; _y++) {
                    if (_y >= 0 && _y < this.height) {
                        unset(_x, _y, occupied);
                    }
                }

            }
        }

	}

	public void unmarkWall(int x, int y, int position, int orientation, boolean impenetrable) {
		x -= insetX;
		y -= insetY;
		if (position == 0) {
			if (orientation == 0) {
				unset(x, y, 128);
				unset(x - 1, y, 8);
			}
			if (orientation == 1) {
				unset(x, y, 2);
				unset(x, y + 1, 32);
			}
			if (orientation == 2) {
				unset(x, y, 8);
				unset(x + 1, y, 128);
			}
			if (orientation == 3) {
				unset(x, y, 32);
				unset(x, y - 1, 2);
			}
		}
		if (position == 1 || position == 3) {
			if (orientation == 0) {
				unset(x, y, 1);
				unset(x - 1, y + 1, 16);
			}
			if (orientation == 1) {
				unset(x, y, 4);
				unset(x + 1, y + 1, 64);
			}
			if (orientation == 2) {
				unset(x, y, 16);
				unset(x + 1, y - 1, 1);
			}
			if (orientation == 3) {
				unset(x, y, 64);
				unset(x - 1, y - 1, 4);
			}
		}
		if (position == 2) {
			if (orientation == 0) {
				unset(x, y, 130);
				unset(x - 1, y, 8);
				unset(x, y + 1, 32);
			}
			if (orientation == 1) {
				unset(x, y, 10);
				unset(x, y + 1, 32);
				unset(x + 1, y, 128);
			}
			if (orientation == 2) {
				unset(x, y, 40);
				unset(x + 1, y, 128);
				unset(x, y - 1, 2);
			}
			if (orientation == 3) {
				unset(x, y, 160);
				unset(x, y - 1, 2);
				unset(x - 1, y, 8);
			}
		}
		if (impenetrable) {
			if (position == 0) {
				if (orientation == 0) {
					unset(x, y, 0x10000);
					unset(x - 1, y, 4096);
				}
				if (orientation == 1) {
					unset(x, y, 1024);
					unset(x, y + 1, 16384);
				}
				if (orientation == 2) {
					unset(x, y, 4096);
					unset(x + 1, y, 0x10000);
				}
				if (orientation == 3) {
					unset(x, y, 16384);
					unset(x, y - 1, 1024);
				}
			}
			if (position == 1 || position == 3) {
				if (orientation == 0) {
					unset(x, y, 512);
					unset(x - 1, y + 1, 8192);
				}
				if (orientation == 1) {
					unset(x, y, 2048);
					unset(x + 1, y + 1, 32768);
				}
				if (orientation == 2) {
					unset(x, y, 8192);
					unset(x + 1, y - 1, 512);
				}
				if (orientation == 3) {
					unset(x, y, 32768);
					unset(x - 1, y - 1, 2048);
				}
			}
			if (position == 2) {
				if (orientation == 0) {
					unset(x, y, 0x10400);
					unset(x - 1, y, 4096);
					unset(x, y + 1, 16384);
				}
				if (orientation == 1) {
					unset(x, y, 5120);
					unset(x, y + 1, 16384);
					unset(x + 1, y, 0x10000);
				}
				if (orientation == 2) {
					unset(x, y, 20480);
					unset(x + 1, y, 0x10000);
					unset(x, y - 1, 1024);
				}
				if (orientation == 3) {
					unset(x, y, 0x14000);
					unset(x, y - 1, 1024);
					unset(x - 1, y, 4096);
				}
			}
		}
	}

	private void unset(int x, int y, int flag) {
		clippingData[x][y] &= 0xffffff - flag;
	}
}
