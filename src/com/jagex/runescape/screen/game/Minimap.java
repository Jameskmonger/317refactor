package com.jagex.runescape.screen.game;

import com.jagex.runescape.*;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.definition.EntityDefinition;

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

    public Minimap() {
        minimapHint = new Sprite[1000];
        minimapHintX = new int[1000];
        minimapHintY = new int[1000];
        compassHingeSize = new int[33];
        compassWidthMap = new int[33];
        minimapLeft = new int[151];
        minimapLineWidth = new int[151];
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
            markMinimap(minimapHint[icon], mapX, mapY);
        }

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                DoubleEndedQueue itemStack = items[x][y];
                if (itemStack != null) {
                    int mapX = (x * 4 + 2) - Client.localPlayer.x / 32;
                    int mapY = (y * 4 + 2) - Client.localPlayer.y / 32;
                    markMinimap(mapDotItem, mapX, mapY);
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
                    markMinimap(mapDotNPC, mapX, mapY);
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
                    markMinimap(mapDotFriend, mapX, mapY);
                else if (team)
                    markMinimap(mapDotTeam, mapX, mapY);
                else
                    markMinimap(mapDotPlayer, mapX, mapY);
            }
        }

        if (hintIconType != 0 && tick % 20 < 10) {
            if (hintIconType == 1 && hintIconNpcId >= 0 && hintIconNpcId < npcs.length) {
                NPC npc = npcs[hintIconNpcId];
                if (npc != null) {
                    int mapX = npc.x / 32 - Client.localPlayer.x / 32;
                    int mapY = npc.y / 32 - Client.localPlayer.y / 32;
                    drawMinimapTarget(mapMarker, mapY, mapX);
                }
            }
            if (hintIconType == 2) {
                int mapX = ((hintIconX - baseX) * 4 + 2) - Client.localPlayer.x / 32;
                int mapY = ((hintIconY - baseY) * 4 + 2) - Client.localPlayer.y / 32;
                drawMinimapTarget(mapMarker, mapY, mapX);
            }
            if (hintIconType == 10 && hintIconPlayerId >= 0 && hintIconPlayerId < players.length) {
                Player player = players[hintIconPlayerId];
                if (player != null) {
                    int mapX = player.x / 32 - Client.localPlayer.x / 32;
                    int mapY = player.y / 32 - Client.localPlayer.y / 32;
                    drawMinimapTarget(mapMarker, mapY, mapX);
                }
            }
        }

        if (destinationX != 0) {
            int mapX = (destinationX * 4 + 2) - Client.localPlayer.x / 32;
            int mapY = (destinationY * 4 + 2) - Client.localPlayer.y / 32;
            markMinimap(mapFlag, mapX, mapY);
        }

        DrawingArea.drawFilledRectangle(97, 78, 3, 3, 0xFFFFFF);
    }

    public void calculateSizes() {
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

    private void drawMinimapTarget(Sprite sprite, int y, int x) {
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
            markMinimap(sprite, x, y);
        }
    }

    private void markMinimap(Sprite sprite, int x, int y) {
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
