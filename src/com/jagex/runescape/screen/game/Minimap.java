package com.jagex.runescape.screen.game;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.definition.EntityDefinition;
import com.jagex.runescape.definition.GameObjectDefinition;
import com.jagex.runescape.scene.WorldController;
import jdk.nashorn.internal.objects.Global;

import java.awt.*;

public class Minimap {
    public int state;
    public int rotation;
    public int zoom;

    private RSImageProducer minimapImageProducer;
    private IndexedImage minimapBackgroundImage;
    private Sprite minimapCompassImage;
    private Sprite minimapEdgeImage;
    public Sprite minimapImage;

    private Sprite mapFlag;
    private Sprite mapMarker;
    private Sprite mapDotItem;
    private Sprite mapDotNPC;
    private Sprite mapDotPlayer;
    private Sprite mapDotFriend;
    private Sprite mapDotTeam;

    public int minimapHintCount;
    public int[] minimapHintX;
    public int[] minimapHintY;
    public Sprite[] minimapHint;

    private int[] compassHingeSize;
    private int[] compassWidthMap;
    private int[] minimapLeft;
    private int[] minimapLineWidth;

    private IndexedImage[] mapSceneImage;
    private Sprite[] mapFunctionImage;

    public Minimap() {
        minimapHint = new Sprite[1000];
        minimapHintX = new int[1000];
        minimapHintY = new int[1000];
        compassHingeSize = new int[33];
        compassWidthMap = new int[33];
        minimapLeft = new int[151];
        minimapLineWidth = new int[151];
        mapSceneImage = new IndexedImage[100];
        mapFunctionImage = new Sprite[100];
    }

    public void setupImageProducer(Component component) {
        minimapImageProducer = new RSImageProducer(172, 156, component);
        DrawingArea.clear();
        minimapBackgroundImage.draw(0, 0);
    }

    public void draw(Graphics graphics) {
        minimapImageProducer.drawGraphics(4, graphics, 550);
    }

    public void load(Archive archiveMedia) {
        minimapImage = new Sprite(512, 512);
        minimapBackgroundImage = new IndexedImage(archiveMedia, "mapback", 0);
        minimapCompassImage = new Sprite(archiveMedia, "compass", 0);
        mapFlag = new Sprite(archiveMedia, "mapmarker", 0);
        mapMarker = new Sprite(archiveMedia, "mapmarker", 1);
        mapDotItem = new Sprite(archiveMedia, "mapdots", 0);
        mapDotNPC = new Sprite(archiveMedia, "mapdots", 1);
        mapDotPlayer = new Sprite(archiveMedia, "mapdots", 2);
        mapDotFriend = new Sprite(archiveMedia, "mapdots", 3);
        mapDotTeam = new Sprite(archiveMedia, "mapdots", 4);
        minimapEdgeImage = new Sprite(archiveMedia, "mapedge", 0);
        minimapEdgeImage.trim();

        try {
            for (int i = 0; i < 100; i++)
                mapSceneImage[i] = new IndexedImage(archiveMedia, "mapscene", i);
        } catch (Exception _ex) {
        }

        try {
            for (int i = 0; i < 100; i++)
                mapFunctionImage[i] = new Sprite(archiveMedia, "mapfunction", i);
        } catch (Exception _ex) {
        }

        int randomRed = (int) (Math.random() * 21D) - 10;
        int randomGreen = (int) (Math.random() * 21D) - 10;
        int randomBlue = (int) (Math.random() * 21D) - 10;
        int randomColour = (int) (Math.random() * 41D) - 20;
        for (int i = 0; i < 100; i++) {
            if (mapFunctionImage[i] != null)
                mapFunctionImage[i].adjustRGB(randomRed + randomColour, randomGreen + randomColour,
                        randomBlue + randomColour);
            if (mapSceneImage[i] != null)
                mapSceneImage[i].mixPalette(randomRed + randomColour, randomGreen + randomColour,
                        randomBlue + randomColour);
        }

        calculateSizes();
    }

    private void drawMinimapScene(WorldController worldController, int lineColour, int interactiveColour, int x, int y, int z) {
        int uid = worldController.getWallObjectHash(x, y, z);
        if (uid != 0) {
            // Walls

            int config = worldController.getConfig(uid, x, y, z);
            int direction = config >> 6 & 3;
            int type = config & 0x1F;
            int colour = lineColour;
            if (uid > 0)
                colour = interactiveColour;
            int pixels[] = minimapImage.pixels;
            int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
            int objectId = uid >> 14 & 0x7FFF;
            GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                IndexedImage background = mapSceneImage[definition.mapScene];
                if (background != null) {
                    int _x = (definition.sizeX * 4 - background.width) / 2;
                    int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            } else {
                if (type == 0 || type == 2)
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
                if (type == 3)
                    if (direction == 0)
                        pixels[pixel] = colour;
                    else if (direction == 1)
                        pixels[pixel + 3] = colour;
                    else if (direction == 2)
                        pixels[pixel + 3 + 1536] = colour;
                    else if (direction == 3)
                        pixels[pixel + 1536] = colour;
                if (type == 2)
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

        uid = worldController.getInteractibleObjectHash(x, y, z);
        if (uid != 0) {
            int config = worldController.getConfig(uid, x, y, z);
            int direction = config >> 6 & 3;
            int type = config & 0x1F;
            int objectId = uid >> 14 & 0x7FFF;
            GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                IndexedImage background = mapSceneImage[definition.mapScene];
                if (background != null) {
                    int _x = (definition.sizeX * 4 - background.width) / 2;
                    int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            } else if (type == 9) {
                // Diagonal walls and doors

                int colour = 0xEEEEEE;
                if (uid > 0)
                    colour = 0xEE0000;
                int pixels[] = minimapImage.pixels;
                int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
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
            int objectId = uid >> 14 & 0x7FFF;
            GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
            if (definition.mapScene != -1) {
                IndexedImage background = mapSceneImage[definition.mapScene];
                if (background != null) {
                    int _x = (definition.sizeX * 4 - background.width) / 2;
                    int _y = (definition.sizeY * 4 - background.height) / 2;
                    background.draw(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
                }
            }
        }
    }

    public void updateImageProducer(
            int baseX,
            int baseY,
            int localPlayerCount,
            Player[] players,
            int[] localPlayers,
            int friendsCount,
            long[] friendsListAsLongs,
            int[] friendsWorldIds,
            DoubleEndedQueue[][] items,
            int npcCount,
            NPC[] npcs,
            int[] npcIds,
            int hintIconType,
            int hintIconNpcId,
            int hintIconPlayerId,
            int hintIconX,
            int hintIconY,
            int destinationX,
            int destinationY,
            int tick
    ) {
        minimapImageProducer.initDrawingArea();
        if (state == 2) {
            byte backgroundPixels[] = minimapBackgroundImage.pixels;
            int rasterPixels[] = DrawingArea.pixels;
            int pixelCount = backgroundPixels.length;
            for (int p = 0; p < pixelCount; p++)
                if (backgroundPixels[p] == 0)
                    rasterPixels[p] = 0;

            minimapCompassImage.rotate(33, Client.cameraHorizontal, compassWidthMap, 256, compassHingeSize, 25, 0, 0, 33, 25);
            return;
        }
        int angle = Client.cameraHorizontal + rotation & 0x7FF;
        int centreX = 48 + Client.localPlayer.x / 32;
        int centreY = 464 - Client.localPlayer.y / 32;
        minimapImage.rotate(151, angle, minimapLineWidth, 256 + zoom, minimapLeft, centreY, 5, 25, 146, centreX);
        minimapCompassImage.rotate(33, Client.cameraHorizontal, compassWidthMap, 256, compassHingeSize, 25, 0, 0, 33, 25);
        for (int icon = 0; icon < minimapHintCount; icon++) {
            int mapX = (minimapHintX[icon] * 4 + 2) - Client.localPlayer.x / 32;
            int mapY = (minimapHintY[icon] * 4 + 2) - Client.localPlayer.y / 32;
            drawSprite(minimapHint[icon], mapX, mapY);
        }

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                DoubleEndedQueue itemStack = items[x][y];
                if (itemStack != null) {
                    int mapX = (x * 4 + 2) - Client.localPlayer.x / 32;
                    int mapY = (y * 4 + 2) - Client.localPlayer.y / 32;
                    drawSprite(mapDotItem, mapX, mapY);
                }
            }
        }

        for (int n = 0; n < npcCount; n++) {
            NPC npc = npcs[npcIds[n]];
            if (npc != null && npc.isVisible()) {
                EntityDefinition definition = npc.npcDefinition;
                if (definition.childrenIDs != null)
                    definition = definition.getChildDefinition();
                if (definition != null && definition.visibleMinimap && definition.clickable) {
                    int mapX = npc.x / 32 - Client.localPlayer.x / 32;
                    int mapY = npc.y / 32 - Client.localPlayer.y / 32;
                    drawSprite(mapDotNPC, mapX, mapY);
                }
            }
        }

        for (int p = 0; p < localPlayerCount; p++) {
            Player player = players[localPlayers[p]];
            if (player != null && player.isVisible()) {
                int mapX = player.x / 32 - Client.localPlayer.x / 32;
                int mapY = player.y / 32 - Client.localPlayer.y / 32;
                boolean friend = false;
                long nameHash = TextClass.nameToLong(player.name);
                for (int f = 0; f < friendsCount; f++) {
                    if (nameHash != friendsListAsLongs[f] || friendsWorldIds[f] == 0)
                        continue;
                    friend = true;
                    break;
                }

                boolean team = false;
                if (Client.localPlayer.team != 0 && player.team != 0 && Client.localPlayer.team == player.team)
                    team = true;
                if (friend)
                    drawSprite(mapDotFriend, mapX, mapY);
                else if (team)
                    drawSprite(mapDotTeam, mapX, mapY);
                else
                    drawSprite(mapDotPlayer, mapX, mapY);
            }
        }

        if (hintIconType != 0 && tick % 20 < 10) {
            if (hintIconType == 1 && hintIconNpcId >= 0 && hintIconNpcId < npcs.length) {
                NPC npc = npcs[hintIconNpcId];
                if (npc != null) {
                    int mapX = npc.x / 32 - Client.localPlayer.x / 32;
                    int mapY = npc.y / 32 - Client.localPlayer.y / 32;
                    drawTarget(mapMarker, mapY, mapX);
                }
            }
            if (hintIconType == 2) {
                int mapX = ((hintIconX - baseX) * 4 + 2) - Client.localPlayer.x / 32;
                int mapY = ((hintIconY - baseY) * 4 + 2) - Client.localPlayer.y / 32;
                drawTarget(mapMarker, mapY, mapX);
            }
            if (hintIconType == 10 && hintIconPlayerId >= 0 && hintIconPlayerId < players.length) {
                Player player = players[hintIconPlayerId];
                if (player != null) {
                    int mapX = player.x / 32 - Client.localPlayer.x / 32;
                    int mapY = player.y / 32 - Client.localPlayer.y / 32;
                    drawTarget(mapMarker, mapY, mapX);
                }
            }
        }

        if (destinationX != 0) {
            int mapX = (destinationX * 4 + 2) - Client.localPlayer.x / 32;
            int mapY = (destinationY * 4 + 2) - Client.localPlayer.y / 32;
            drawSprite(mapFlag, mapX, mapY);
        }

        DrawingArea.drawFilledRectangle(97, 78, 3, 3, 0xFFFFFF);
    }

    public void render(WorldController worldController, int plane, byte[][][] tileFlags, CollisionMap[] collisionMap) {
        int pixels[] = minimapImage.pixels;
        int pixelCount = pixels.length;
        for (int pixel = 0; pixel < pixelCount; pixel++)
            pixels[pixel] = 0;

        for (int y = 1; y < 103; y++) {
            int pixel = 24628 + (103 - y) * 512 * 4;
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0)
                    worldController.drawMinimapTile(x, y, plane, pixels, pixel);
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0)
                    worldController.drawMinimapTile(x, y, plane + 1, pixels, pixel);
                pixel += 4;
            }
        }

        int primaryColour = ((238 + (int) (Math.random() * 20D)) - 10 << 16)
                + ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
        int secondaryColour = (238 + (int) (Math.random() * 20D)) - 10 << 16;
        minimapImage.initDrawingArea();
        for (int y = 1; y < 103; y++) {
            for (int x = 1; x < 103; x++) {
                if ((tileFlags[plane][x][y] & 0x18) == 0)
                    drawMinimapScene(worldController, primaryColour, secondaryColour, x, y, plane);
                if (plane < 3 && (tileFlags[plane + 1][x][y] & 8) != 0)
                    drawMinimapScene(worldController, primaryColour, secondaryColour, x, y, plane + 1);
            }
        }

        offsetMinimapIcons(worldController, collisionMap, plane);
    }

    private void offsetMinimapIcons(WorldController worldController, CollisionMap[] collisionMap, int plane) {
        minimapHintCount = 0;
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                int hash = worldController.getGroundDecorationHash(x, y, plane);
                if (hash != 0) {
                    hash = hash >> 14 & 0x7FFF;
                    int icon = GameObjectDefinition.getDefinition(hash).icon;

                    if (icon >= 0) {
                        int drawPointX = x;
                        int drawPointY = y;

                        // All the shop icons, it seems
                        if (shouldMoveIcon(icon)) {
                            byte regionWidth = 104;
                            byte regionHeight = 104;
                            int clippingFlags[][] = collisionMap[plane].clippingData;
                            for (int off = 0; off < 10; off++) {
                                int randomDirection = (int) (Math.random() * 4D);
                                if (randomDirection == 0 && drawPointX > 0 && drawPointX > x - 3
                                        && (clippingFlags[drawPointX - 1][drawPointY] & 0x1280108) == 0)
                                    drawPointX--;
                                if (randomDirection == 1 && drawPointX < regionWidth - 1 && drawPointX < x + 3
                                        && (clippingFlags[drawPointX + 1][drawPointY] & 0x1280180) == 0)
                                    drawPointX++;
                                if (randomDirection == 2 && drawPointY > 0 && drawPointY > y - 3
                                        && (clippingFlags[drawPointX][drawPointY - 1] & 0x1280102) == 0)
                                    drawPointY--;
                                if (randomDirection == 3 && drawPointY < regionHeight - 1 && drawPointY < y + 3
                                        && (clippingFlags[drawPointX][drawPointY + 1] & 0x1280120) == 0)
                                    drawPointY++;
                            }
                        }

                        minimapHint[minimapHintCount] = mapFunctionImage[icon];
                        minimapHintX[minimapHintCount] = drawPointX;
                        minimapHintY[minimapHintCount] = drawPointY;
                        minimapHintCount++;
                    }
                }
            }
        }
    }

    private boolean shouldMoveIcon(int icon) {
        return icon != 22 && icon != 29 && icon != 34 && icon != 36 && icon != 46 && icon != 47 && icon != 48;
    }

    private void calculateSizes() {
        for (int _y = 0; _y < 33; _y++) {
            int firstXOfLine = 999;
            int lastXOfLine = 0;
            for (int _x = 0; _x < 34; _x++) {
                if (minimapBackgroundImage.pixels[_x + _y * minimapBackgroundImage.width] == 0) {
                    if (firstXOfLine == 999)
                        firstXOfLine = _x;
                    continue;
                }
                if (firstXOfLine == 999)
                    continue;
                lastXOfLine = _x;
                break;
            }

            compassHingeSize[_y] = firstXOfLine;
            compassWidthMap[_y] = lastXOfLine - firstXOfLine;
        }

        for (int _y = 5; _y < 156; _y++) {
            int min = 999;
            int max = 0;
            for (int _x = 25; _x < 172; _x++) {
                if (minimapBackgroundImage.pixels[_x + _y * minimapBackgroundImage.width] == 0
                        && (_x > 34 || _y > 34)) {
                    if (min == 999)
                        min = _x;
                    continue;
                }
                if (min == 999)
                    continue;
                max = _x;
                break;
            }

            minimapLeft[_y - 5] = min - 25;
            minimapLineWidth[_y - 5] = max - min;
        }
    }

    private void drawTarget(Sprite sprite, int y, int x) {
        int l = x * x + y * y;
        if (l > 4225 && l < 0x15F90) {
            int angle = Client.cameraHorizontal + rotation & 0x7FF;
            int sine = Model.SINE[angle];
            int cosine = Model.COSINE[angle];
            sine = (sine * 256) / (zoom + 256);
            cosine = (cosine * 256) / (zoom + 256);
            int l1 = y * sine + x * cosine >> 16;
            int i2 = y * cosine - x * sine >> 16;
            double d = Math.atan2(l1, i2);
            int randomX = (int) (Math.sin(d) * 63D);
            int randomY = (int) (Math.cos(d) * 57D);
            minimapEdgeImage.rotate(88 + randomX, 63 - randomY, d);
        } else {
            drawSprite(sprite, x, y);
        }
    }

    private void drawSprite(Sprite sprite, int x, int y) {
        int angle = Client.cameraHorizontal + rotation & 0x7FF;
        int l = x * x + y * y;
        if (l > 6400)
            return;
        int sineAngle = Model.SINE[angle];
        int cosineAngle = Model.COSINE[angle];
        sineAngle = (sineAngle * 256) / (zoom + 256);
        cosineAngle = (cosineAngle * 256) / (zoom + 256);
        int spriteOffsetX = y * sineAngle + x * cosineAngle >> 16;
        int spriteOffsetY = y * cosineAngle - x * sineAngle >> 16;
        if (l > 2500) {
            sprite.method354(minimapBackgroundImage, 83 - spriteOffsetY - sprite.maxHeight / 2 - 4,
                    ((94 + spriteOffsetX) - sprite.maxWidth / 2) + 4);
        } else {
            sprite.drawImage(((94 + spriteOffsetX) - sprite.maxWidth / 2) + 4,
                    83 - spriteOffsetY - sprite.maxHeight / 2 - 4);
        }

        minimapImageProducer.initDrawingArea();
    }
}
