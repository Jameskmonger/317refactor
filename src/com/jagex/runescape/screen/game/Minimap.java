package com.jagex.runescape.screen.game;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.definition.EntityDefinition;
import com.jagex.runescape.definition.GameObjectDefinition;
import com.jagex.runescape.scene.WorldController;

import java.awt.*;

public class Minimap {
    public int state;
    public int rotation;
    public int zoom;

    private RSImageProducer minimapImageProducer;
    private IndexedImage minimapBackgroundImage;
    private Sprite minimapCompassImage;
    private Sprite minimapEdgeImage;
    private Sprite minimapImage;

    private Sprite mapFlag;
    private Sprite mapMarker;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;

    private int minimapHintCount;
    private final int[] minimapHintX;
    private final int[] minimapHintY;
    private final Sprite[] minimapHint;

    private final int[] compassHingeSize;
    private final int[] compassWidthMap;
    private final int[] minimapLeft;
    private final int[] minimapLineWidth;

    private final IndexedImage[] mapSceneImage;
    private final Sprite[] mapFunctionImage;

    public Minimap() {
        this.minimapHint = new Sprite[1000];
        this.minimapHintX = new int[1000];
        this.minimapHintY = new int[1000];
        this.compassHingeSize = new int[33];
        this.compassWidthMap = new int[33];
        this.minimapLeft = new int[151];
        this.minimapLineWidth = new int[151];
        this.mapSceneImage = new IndexedImage[100];
        this.mapFunctionImage = new Sprite[100];
    }

    public void setupImageProducer(final Component component) {
        this.minimapImageProducer = new RSImageProducer(172, 156, component);
        DrawingArea.clear();
        this.minimapBackgroundImage.draw(0, 0);
    }

    public void draw(final Graphics graphics) {
        this.minimapImageProducer.drawGraphics(4, graphics, 550);
    }

    public void load(final Archive archiveMedia) {
        this.minimapImage = new Sprite(512, 512);
        this.minimapBackgroundImage = new IndexedImage(archiveMedia, "mapback", 0);
        this.minimapCompassImage = new Sprite(archiveMedia, "compass", 0);
        this.mapFlag = new Sprite(archiveMedia, "mapmarker", 0);
        this.mapMarker = new Sprite(archiveMedia, "mapmarker", 1);
        this.mapDotItem = new Sprite(archiveMedia, "mapdots", 0);
        this.mapDotNPC = new Sprite(archiveMedia, "mapdots", 1);
        this.mapDotPlayer = new Sprite(archiveMedia, "mapdots", 2);
        this.mapDotFriend = new Sprite(archiveMedia, "mapdots", 3);
        this.mapDotTeam = new Sprite(archiveMedia, "mapdots", 4);
        this.minimapEdgeImage = new Sprite(archiveMedia, "mapedge", 0);
        this.minimapEdgeImage.trim();

        try {
            for (int i = 0; i < 100; i++) {
                this.mapSceneImage[i] = new IndexedImage(archiveMedia, "mapscene", i);
            }
        } catch (final Exception _ex) {
        }

        try {
            for (int i = 0; i < 100; i++) {
                this.mapFunctionImage[i] = new Sprite(archiveMedia, "mapfunction", i);
            }
        } catch (final Exception _ex) {
        }

        final int randomRed = (int) (Math.random() * 21D) - 10;
        final int randomGreen = (int) (Math.random() * 21D) - 10;
        final int randomBlue = (int) (Math.random() * 21D) - 10;
        final int randomColour = (int) (Math.random() * 41D) - 20;
        for (int i = 0; i < 100; i++) {
            if (this.mapFunctionImage[i] != null) {
                this.mapFunctionImage[i].adjustRGB(randomRed + randomColour, randomGreen + randomColour,
                    randomBlue + randomColour);
            }
            if (this.mapSceneImage[i] != null) {
                this.mapSceneImage[i].mixPalette(randomRed + randomColour, randomGreen + randomColour,
                    randomBlue + randomColour);
            }
        }

        this.calculateSizes();
    }

    private void drawMinimapScene(final WorldController worldController, final int lineColour, final int interactiveColour, final int x, final int y, final int z) {
        int uid = worldController.getWallObjectHash(x, y, z);
        if (uid != 0) {
            // Walls

            final int config = worldController.getConfig(uid, x, y, z);
            final int direction = config >> 6 & 3;
            final int type = config & 0x1F;
            int colour = lineColour;
            if (uid > 0) {
                colour = interactiveColour;
            }
            final int[] pixels = this.minimapImage.pixels;
            final int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
            final int objectId = uid >> 14 & 0x7FFF;
            final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                final IndexedImage background = this.mapSceneImage[definition.mapScene];
                if (background != null) {
                    final int _x = (definition.sizeX * 4 - background.width) / 2;
                    final int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            } else {
                if (type == 0 || type == 2) {
                    if (direction == 0) {
                        pixels[pixel] = colour;
                        pixels[pixel + 512] = colour;
                        pixels[pixel + 1024] = colour;
                        pixels[pixel + 1536] = colour;
                    } else if (direction == 1) {
                        pixels[pixel] = colour;
                        pixels[pixel + 1] = colour;
                        pixels[pixel + 2] = colour;
                        pixels[pixel + 3] = colour;
                    } else if (direction == 2) {
                        pixels[pixel + 3] = colour;
                        pixels[pixel + 3 + 512] = colour;
                        pixels[pixel + 3 + 1024] = colour;
                        pixels[pixel + 3 + 1536] = colour;
                    } else if (direction == 3) {
                        pixels[pixel + 1536] = colour;
                        pixels[pixel + 1536 + 1] = colour;
                        pixels[pixel + 1536 + 2] = colour;
                        pixels[pixel + 1536 + 3] = colour;
                    }
                }
                if (type == 3) {
                    if (direction == 0) {
                        pixels[pixel] = colour;
                    } else if (direction == 1) {
                        pixels[pixel + 3] = colour;
                    } else if (direction == 2) {
                        pixels[pixel + 3 + 1536] = colour;
                    } else if (direction == 3) {
                        pixels[pixel + 1536] = colour;
                    }
                }
                if (type == 2) {
                    if (direction == 3) {
                        pixels[pixel] = colour;
                        pixels[pixel + 512] = colour;
                        pixels[pixel + 1024] = colour;
                        pixels[pixel + 1536] = colour;
                    } else if (direction == 0) {
                        pixels[pixel] = colour;
                        pixels[pixel + 1] = colour;
                        pixels[pixel + 2] = colour;
                        pixels[pixel + 3] = colour;
                    } else if (direction == 1) {
                        pixels[pixel + 3] = colour;
                        pixels[pixel + 3 + 512] = colour;
                        pixels[pixel + 3 + 1024] = colour;
                        pixels[pixel + 3 + 1536] = colour;
                    } else if (direction == 2) {
                        pixels[pixel + 1536] = colour;
                        pixels[pixel + 1536 + 1] = colour;
                        pixels[pixel + 1536 + 2] = colour;
                        pixels[pixel + 1536 + 3] = colour;
                    }
                }
            }
        }

        uid = worldController.getInteractibleObjectHash(x, y, z);
        if (uid != 0) {
            final int config = worldController.getConfig(uid, x, y, z);
            final int direction = config >> 6 & 3;
            final int type = config & 0x1F;
            final int objectId = uid >> 14 & 0x7FFF;
            final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                final IndexedImage background = this.mapSceneImage[definition.mapScene];
                if (background != null) {
                    final int _x = (definition.sizeX * 4 - background.width) / 2;
                    final int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            } else if (type == 9) {
                // Diagonal walls and doors

                int colour = 0xEEEEEE;
                if (uid > 0) {
                    colour = 0xEE0000;
                }
                final int[] pixels = this.minimapImage.pixels;
                final int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
                if (direction == 0 || direction == 2) {
                    pixels[pixel + 1536] = colour;
                    pixels[pixel + 1024 + 1] = colour;
                    pixels[pixel + 512 + 2] = colour;
                    pixels[pixel + 3] = colour;
                } else {
                    pixels[pixel] = colour;
                    pixels[pixel + 512 + 1] = colour;
                    pixels[pixel + 1024 + 2] = colour;
                    pixels[pixel + 1536 + 3] = colour;
                }
            }
        }

        uid = worldController.getGroundDecorationHash(x, y, z);
        if (uid != 0) {
            final int objectId = uid >> 14 & 0x7FFF;
            final GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                final IndexedImage background = this.mapSceneImage[definition.mapScene];
                if (background != null) {
                    final int _x = (definition.sizeX * 4 - background.width) / 2;
                    final int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            }
        }
    }

    public void updateImageProducer(
        final int baseX,
        final int baseY,
        final int localPlayerCount,
        final Player[] players,
        final int[] localPlayers,
        final int friendsCount,
        final long[] friendsListAsLongs,
        final int[] friendsWorldIds,
        final DoubleEndedQueue[][] items,
        final int npcCount,
        final NPC[] npcs,
        final int[] npcIds,
        final int hintIconType,
        final int hintIconNpcId,
        final int hintIconPlayerId,
        final int hintIconX,
        final int hintIconY,
        final int destinationX,
        final int destinationY,
        final int tick
    ) {
        this.minimapImageProducer.initDrawingArea();
        if (this.state == 2) {
            final byte[] backgroundPixels = this.minimapBackgroundImage.pixels;
            final int[] rasterPixels = DrawingArea.pixels;
            final int pixelCount = backgroundPixels.length;
            for (int p = 0; p < pixelCount; p++) {
                if (backgroundPixels[p] == 0) {
                    rasterPixels[p] = 0;
                }
            }

            this.minimapCompassImage.rotate(33, Client.cameraHorizontal, this.compassWidthMap, 256, this.compassHingeSize, 25, 0, 0, 33, 25);
            return;
        }
        final int angle = Client.cameraHorizontal + this.rotation & 0x7FF;
        final int centreX = 48 + Client.localPlayer.x / 32;
        final int centreY = 464 - Client.localPlayer.y / 32;
        this.minimapImage.rotate(151, angle, this.minimapLineWidth, 256 + this.zoom, this.minimapLeft, centreY, 5, 25, 146, centreX);
        this.minimapCompassImage.rotate(33, Client.cameraHorizontal, this.compassWidthMap, 256, this.compassHingeSize, 25, 0, 0, 33, 25);
        for (int icon = 0; icon < this.minimapHintCount; icon++) {
            final int mapX = (this.minimapHintX[icon] * 4 + 2) - Client.localPlayer.x / 32;
            final int mapY = (this.minimapHintY[icon] * 4 + 2) - Client.localPlayer.y / 32;
            this.drawSprite(this.minimapHint[icon], mapX, mapY);
        }

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                final DoubleEndedQueue itemStack = items[x][y];
                if (itemStack != null) {
                    final int mapX = (x * 4 + 2) - Client.localPlayer.x / 32;
                    final int mapY = (y * 4 + 2) - Client.localPlayer.y / 32;
                    this.drawSprite(this.mapDotItem, mapX, mapY);
                }
            }
        }

        for (int n = 0; n < npcCount; n++) {
            final NPC npc = npcs[npcIds[n]];
            if (npc != null && npc.isVisible()) {
                EntityDefinition definition = npc.npcDefinition;
                if (definition.childrenIDs != null) {
                    definition = definition.getChildDefinition();
                }
                if (definition != null && definition.visibleMinimap && definition.clickable) {
                    final int mapX = npc.x / 32 - Client.localPlayer.x / 32;
                    final int mapY = npc.y / 32 - Client.localPlayer.y / 32;
                    this.drawSprite(this.mapDotNPC, mapX, mapY);
                }
            }
        }

        for (int p = 0; p < localPlayerCount; p++) {
            final Player player = players[localPlayers[p]];
            if (player != null && player.isVisible()) {
                final int mapX = player.x / 32 - Client.localPlayer.x / 32;
                final int mapY = player.y / 32 - Client.localPlayer.y / 32;
                boolean friend = false;
                final long nameHash = TextClass.nameToLong(player.name);
                for (int f = 0; f < friendsCount; f++) {
                    if (nameHash != friendsListAsLongs[f] || friendsWorldIds[f] == 0) {
                        continue;
                    }
                    friend = true;
                    break;
                }

                boolean team = player.team != 0 && Client.localPlayer.team == player.team;
                if (friend) {
                    this.drawSprite(this.mapDotFriend, mapX, mapY);
                } else if (team) {
                    this.drawSprite(this.mapDotTeam, mapX, mapY);
                } else {
                    this.drawSprite(this.mapDotPlayer, mapX, mapY);
                }
            }
        }

        if (hintIconType != 0 && tick % 20 < 10) {
            if (hintIconType == 1 && hintIconNpcId >= 0 && hintIconNpcId < npcs.length) {
                final NPC npc = npcs[hintIconNpcId];
                if (npc != null) {
                    final int mapX = npc.x / 32 - Client.localPlayer.x / 32;
                    final int mapY = npc.y / 32 - Client.localPlayer.y / 32;
                    this.drawTarget(this.mapMarker, mapY, mapX);
                }
            }
            if (hintIconType == 2) {
                final int mapX = ((hintIconX - baseX) * 4 + 2) - Client.localPlayer.x / 32;
                final int mapY = ((hintIconY - baseY) * 4 + 2) - Client.localPlayer.y / 32;
                this.drawTarget(this.mapMarker, mapY, mapX);
            }
            if (hintIconType == 10 && hintIconPlayerId >= 0 && hintIconPlayerId < players.length) {
                final Player player = players[hintIconPlayerId];
                if (player != null) {
                    final int mapX = player.x / 32 - Client.localPlayer.x / 32;
                    final int mapY = player.y / 32 - Client.localPlayer.y / 32;
                    this.drawTarget(this.mapMarker, mapY, mapX);
                }
            }
        }

        if (destinationX != 0) {
            final int mapX = (destinationX * 4 + 2) - Client.localPlayer.x / 32;
            final int mapY = (destinationY * 4 + 2) - Client.localPlayer.y / 32;
            this.drawSprite(this.mapFlag, mapX, mapY);
        }

        DrawingArea.drawFilledRectangle(97, 78, 3, 3, 0xFFFFFF);
    }

    public void render(final WorldController worldController, final int plane, final byte[][][] tileFlags, final CollisionMap[] collisionMap) {
        final int[] pixels = this.minimapImage.pixels;
        final int pixelCount = pixels.length;
        for (int pixel = 0; pixel < pixelCount; pixel++) {
            pixels[pixel] = 0;
        }

        for (int y = 1; y < 103; y++) {
            int pixel = 24628 + (103 - y) * 512 * 4;
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0) {
                    worldController.drawMinimapTile(x, y, plane, pixels, pixel);
                }
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0) {
                    worldController.drawMinimapTile(x, y, plane + 1, pixels, pixel);
                }
                pixel += 4;
            }
        }

        final int primaryColour = ((238 + (int) (Math.random() * 20D)) - 10 << 16)
            + ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
        final int secondaryColour = (238 + (int) (Math.random() * 20D)) - 10 << 16;
        this.minimapImage.initDrawingArea();
        for (int y = 1; y < 103; y++) {
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0) {
                    this.drawMinimapScene(worldController, primaryColour, secondaryColour, x, y, plane);
                }
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0) {
                    this.drawMinimapScene(worldController, primaryColour, secondaryColour, x, y, plane + 1);
                }
            }
        }

        this.offsetMinimapIcons(worldController, collisionMap, plane);
    }

    private void offsetMinimapIcons(final WorldController worldController, final CollisionMap[] collisionMap, final int plane) {
        this.minimapHintCount = 0;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                int hash = worldController.getGroundDecorationHash(x, y, plane);
                if (hash != 0) {
                    hash = hash >> 14 & 0x7FFF;
                    final int icon = GameObjectDefinition.getDefinition(hash).icon;

                    if (icon >= 0) {
                        int drawPointX = x;
                        int drawPointY = y;

                        // All the shop icons, it seems
                        if (this.shouldMoveIcon(icon)) {
                            final byte regionWidth = 104;
                            final byte regionHeight = 104;
                            final int[][] clippingFlags = collisionMap[plane].clippingData;
                            for (int off = 0; off < 10; off++) {
                                final int randomDirection = (int) (Math.random() * 4D);
                                if (randomDirection == 0 && drawPointX > 0 && drawPointX > x - 3
                                    && (clippingFlags[drawPointX - 1][drawPointY] & 0x1280108) == 0) {
                                    drawPointX--;
                                }
                                if (randomDirection == 1 && drawPointX < regionWidth - 1 && drawPointX < x + 3
                                    && (clippingFlags[drawPointX + 1][drawPointY] & 0x1280180) == 0) {
                                    drawPointX++;
                                }
                                if (randomDirection == 2 && drawPointY > 0 && drawPointY > y - 3
                                    && (clippingFlags[drawPointX][drawPointY - 1] & 0x1280102) == 0) {
                                    drawPointY--;
                                }
                                if (randomDirection == 3 && drawPointY < regionHeight - 1 && drawPointY < y + 3
                                    && (clippingFlags[drawPointX][drawPointY + 1] & 0x1280120) == 0) {
                                    drawPointY++;
                                }
                            }
                        }

                        this.minimapHint[this.minimapHintCount] = this.mapFunctionImage[icon];
                        this.minimapHintX[this.minimapHintCount] = drawPointX;
                        this.minimapHintY[this.minimapHintCount] = drawPointY;
                        this.minimapHintCount++;
                    }
                }
            }
        }
    }

    private boolean shouldMoveIcon(final int icon) {
        return icon != 22 && icon != 29 && icon != 34 && icon != 36 && icon != 46 && icon != 47 && icon != 48;
    }

    private void calculateSizes() {
        for (int _y = 0; _y < 33; _y++) {
            int firstXOfLine = 999;
            int lastXOfLine = 0;
            for (int _x = 0; _x < 34; _x++) {
                if (this.minimapBackgroundImage.pixels[_x + _y * this.minimapBackgroundImage.width] == 0) {
                    if (firstXOfLine == 999) {
                        firstXOfLine = _x;
                    }
                    continue;
                }
                if (firstXOfLine == 999) {
                    continue;
                }
                lastXOfLine = _x;
                break;
            }

            this.compassHingeSize[_y] = firstXOfLine;
            this.compassWidthMap[_y] = lastXOfLine - firstXOfLine;
        }

        for (int _y = 5; _y < 156; _y++) {
            int min = 999;
            int max = 0;
            for (int _x = 25; _x < 172; _x++) {
                if (this.minimapBackgroundImage.pixels[_x + _y * this.minimapBackgroundImage.width] == 0
                    && (_x > 34 || _y > 34)) {
                    if (min == 999) {
                        min = _x;
                    }
                    continue;
                }
                if (min == 999) {
                    continue;
                }
                max = _x;
                break;
            }

            this.minimapLeft[_y - 5] = min - 25;
            this.minimapLineWidth[_y - 5] = max - min;
        }
    }

    private void drawTarget(final Sprite sprite, final int y, final int x) {
        final int l = x * x + y * y;
        if (l > 4225 && l < 0x15F90) {
            final int angle = Client.cameraHorizontal + this.rotation & 0x7FF;
            int sine = Model.SINE[angle];
            int cosine = Model.COSINE[angle];
            sine = (sine * 256) / (this.zoom + 256);
            cosine = (cosine * 256) / (this.zoom + 256);
            final int l1 = y * sine + x * cosine >> 16;
            final int i2 = y * cosine - x * sine >> 16;
            final double d = Math.atan2(l1, i2);
            final int randomX = (int) (Math.sin(d) * 63D);
            final int randomY = (int) (Math.cos(d) * 57D);
            this.minimapEdgeImage.rotate(88 + randomX, 63 - randomY, d);
        } else {
            this.drawSprite(sprite, x, y);
        }
    }

    private void drawSprite(final Sprite sprite, final int x, final int y) {
        final int angle = Client.cameraHorizontal + this.rotation & 0x7FF;
        final int l = x * x + y * y;
        if (l > 6400) {
            return;
        }
        int sineAngle = Model.SINE[angle];
        int cosineAngle = Model.COSINE[angle];
        sineAngle = (sineAngle * 256) / (this.zoom + 256);
        cosineAngle = (cosineAngle * 256) / (this.zoom + 256);
        final int spriteOffsetX = y * sineAngle + x * cosineAngle >> 16;
        final int spriteOffsetY = y * cosineAngle - x * sineAngle >> 16;
        if (l > 2500) {
            sprite.method354(this.minimapBackgroundImage, 83 - spriteOffsetY - sprite.maxHeight / 2 - 4,
                ((94 + spriteOffsetX) - sprite.maxWidth / 2) + 4);
        } else {
            sprite.drawImage(((94 + spriteOffsetX) - sprite.maxWidth / 2) + 4,
                83 - spriteOffsetY - sprite.maxHeight / 2 - 4);
        }

        this.minimapImageProducer.initDrawingArea();
    }
}
