package com.jagex.runescape;

import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.net.*;

import com.jagex.runescape.audio.Effect;
import com.jagex.runescape.collection.*;
import com.jagex.runescape.definition.*;
import com.jagex.runescape.screen.game.Minimap;
import com.jagex.runescape.screen.title.TitleScreen;
import com.jagex.runescape.sign.signlink;
import com.jagex.runescape.isaac.ISAACRandomGenerator;
import com.jagex.runescape.scene.WorldController;
import com.jagex.runescape.scene.object.GroundDecoration;
import com.jagex.runescape.scene.object.Wall;
import com.jagex.runescape.scene.object.WallDecoration;

@SuppressWarnings("serial")
public final class Client extends RSApplet {

	private final TitleScreen titleScreen;
	private final Minimap minimap;

	private static String formatAmount(final int amount) {
		String formattedAmount = String.valueOf(amount);
		for (int i = formattedAmount.length() - 3; i > 0; i -= 3) {
            formattedAmount = formattedAmount.substring(0, i) + "," + formattedAmount.substring(i);
        }
		if (formattedAmount.length() > 8) {
            formattedAmount = "@gre@" + formattedAmount.substring(0, formattedAmount.length() - 8) + " million @whi@("
                    + formattedAmount + ")";
        } else if (formattedAmount.length() > 4) {
            formattedAmount = "@cya@" + formattedAmount.substring(0, formattedAmount.length() - 4) + "K @whi@("
                    + formattedAmount + ")";
        }
		return " " + formattedAmount;
	}

	private static String getAmountString(final int amount) {
		if (amount < 100000) {
            return String.valueOf(amount);
        }

		if (amount < 10000000) {
            return amount / 1000 + "K";
        }

		return amount / 1000000 + "M";
	}

	private static String getCombatLevelDifferenceColour(final int localLevel, final int otherLevel) {
		final int difference = localLevel - otherLevel;

		if (difference < -9) {
            return "@red@";
        }

		if (difference < -6) {
            return "@or3@";
        }

		if (difference < -3) {
            return "@or2@";
        }

		if (difference < 0) {
            return "@or1@";
        }

		if (difference > 9) {
            return "@gre@";
        }

		if (difference > 6) {
            return "@gr3@";
        }

		if (difference > 3) {
            return "@gr2@";
        }

		if (difference > 0) {
            return "@gr1@";
        }

		return "@yel@";
	}

	public static void main(final String[] args) {
		try {
			System.out.println("RS2 user client - release #" + 317);

			if (args.length != 5) {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			localWorldId = Integer.parseInt(args[0]);
			portOffset = Integer.parseInt(args[1]);
			if (args[2].equals("lowmem")) {
                setLowMemory();
            } else if (args[2].equals("highmem")) {
				setHighMem();
			} else {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			if (args[3].equals("free")) {
                membersWorld = false;
            } else if (args[3].equals("members")) {
				membersWorld = true;
			} else {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			signlink.storeid = Integer.parseInt(args[4]);
			signlink.startpriv(InetAddress.getLocalHost());
			final Client client1 = new Client();
			client1.createClientFrame(765, 503);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

	private static void setHighMem() {
		WorldController.lowMemory = false;
		Rasterizer.lowMemory = false;
		lowMemory = false;
		Region.lowMemory = false;
		GameObjectDefinition.lowMemory = false;
	}

	private static void setLowMemory() {
		WorldController.lowMemory = true;
		Rasterizer.lowMemory = true;
		lowMemory = true;
		Region.lowMemory = true;
		GameObjectDefinition.lowMemory = true;
	}

	private int ignoreCount;
	private long loadRegionTime;
	private int[][] distanceValues;
	private int[] friendsWorldIds;
	private DoubleEndedQueue[][][] groundArray;
	private Socket jaggrabSocket;
	private int loginScreenState;
	private Buffer textStream;
	private NPC[] npcs;
	private int npcCount;
	private int[] npcIds;
	private int actorsToUpdateCount;
	private int[] actorsToUpdateIds;
	private int mostRecentOpcode;
	private int secondMostRecentOpcode;
	private int thirdMostRecentOpcode;
	private String clickToContinueString;
	private int privateChatMode;
	private Buffer loginStream;
	private boolean effectsEnabled;
	private int hintIconType;
	private int openInterfaceId;
	private int cameraPositionX;
	private int cameraPositionZ;
	private int cameraPositionY;
	private int cameraVerticalRotation;
	private int cameraHorizontalRotation;
	private int playerRights;
	private final int[] skillExperience;
	private IndexedImage redStone1_3;
	private IndexedImage redStone2_3;
	private IndexedImage redStone3_2;
	private IndexedImage redStone1_4;
	private IndexedImage redStone2_4;
	private boolean aBoolean872;
	private final int[] cameraJitter;
	private int currentTrackId;
	private final boolean[] customCameraActive;
	private int playerWeight;
	private MouseDetection mouseDetection;
	private volatile boolean drawFlames;
	private String reportAbuseInput;
	private int playerListId;
	private boolean menuOpen;
	private int anInt886;
	private String inputString;
	private final int MAX_ENTITY_COUNT;
	private final int LOCAL_PLAYER_ID;
	private Player[] players;
	private int localPlayerCount;
	private int[] localPlayers;
	private int playersObservedCount;
	private int[] playersObserved;
	private Buffer[] playerAppearanceData;
	private int cameraRandomisationA;
	private int nextCameraRandomisationA;
	private int friendsCount;
	private int friendListStatus;
	private int[][] wayPoints;
	private final int SCROLLBAR_GRIP_HIGHLIGHT;
	private RSImageProducer backLeftIP1;
	private RSImageProducer backLeftIP2;
	private RSImageProducer backRightIP1;
	private RSImageProducer backRightIP2;
	private RSImageProducer backTopIP1;
	private RSImageProducer backVmidIP1;
	private RSImageProducer backVmidIP2;
	private RSImageProducer backVmidIP3;
	private RSImageProducer backVmidIP2_2;
	private byte[] animatedPixels;
	private int bankInsertMode;
	private int crossX;
	private int crossY;
	private int crossIndex;
	private int crossType;
	private int plane;
	private final int[] skillLevel;
	private final long[] ignoreListAsLongs;
	private boolean loadingError;
	private final int SCROLLBAR_GRIP_LOWLIGHT;
	private final int[] cameraFrequency;
	private int[][] tileRenderCount;
	private Sprite characterEditButtonDefualt;
	private Sprite characterEditButtonActive;
	private int hintIconPlayerId;
	private int hintIconX;
	private int hintIconY;
	private int hintIconDrawHeight;
	private int hintIconDrawTileX;
	private int hintIconDrawTileY;
	private final int[] chatTypes;
	private final String[] chatNames;
	private final String[] chatMessages;
	private int animationTimePassed;
	private WorldController worldController;
	private IndexedImage[] sideIconImage;
	private int menuScreenArea;
	private int menuOffsetX;
	private int menuOffsetY;
	private int menuWidth;
	private int menuHeight;
	private long privateMessageTarget;
	private boolean windowFocused;
	private long[] friendsListAsLongs;
	private int currentSong;
	private static int localWorldId = 10;
	static int portOffset;
	private static boolean membersWorld = true;
	private static boolean lowMemory;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] SPOKEN_TEXT_COLOURS = { 0xFFFF00, 0xFF0000, 0x00FF00, 0x00FFFF, 0xFF00FF, 0xFFFFFF };

	final FileCache[] caches;
	public int[] interfaceSettings;
	private boolean aBoolean972;
	private final int overheadMessageCount;
	private final int[] overheadTextDrawX;
	private final int[] overheadTextDrawY;
	private final int[] overheadTextHeight;
	private final int[] overheadTextWidth;
	private final int[] overheadTextColour;
	private final int[] overheadTextEffect;
	private final int[] overheadTextCycle;
	private final String[] overheadTextMessage;
	private int secondaryCameraVertical;
	private int lastRegionId;
	private Sprite[] hitMarkImage;
	private int cameraRandomisationCounter;
	private int lastItemDragTime;
	private final int[] characterEditColours;
	private static boolean clientRunning;
	private int anInt995;
	private int anInt996;
	private int cameraOffsetZ;
	private int anInt998;
	private int anInt999;
	private ISAACRandomGenerator encryption;
	private final int SCROLLBAR_TRACK_COLOUR;
	static final int[][] APPEARANCE_COLOURS = {
			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193 },
			{ 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239 },
			{ 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003 },
			{ 4626, 11146, 6439, 12, 4758, 10270 }, { 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 } };
	private String amountOrNameInput;
	private int daysSinceLogin;
	private int packetSize;
	private int packetOpcode;
	private int packetReadAnticheat;
	private int idleCounter;
	private int idleLogout;
	private DoubleEndedQueue projectileQueue;
	private int currentCameraPositionH;
	private int currentCameraPositionV;
	private int cameraMovedWriteDelay;
	private boolean cameraMovedWrite;
	private int walkableInterfaceId;
	private static final int[] EXPERIENCE_TABLE;
	private int sameClickPositionCounter;
	private int loadingStage;
	private IndexedImage scrollBarUp;
	private IndexedImage scrollBarDown;
	private int anInt1026;
	private IndexedImage backBase1Image;
	private IndexedImage backBase2Image;
	private IndexedImage backHmid1Image;
	private final int[] unknownCameraVariable;
	private boolean characterModelChanged;
	private int baseX;
	private int baseY;
	private int anInt1036;
	private int anInt1037;
	private int loginFailures;
	private int anInt1039;
	private int dialogID;
	private final int[] skillMaxLevel;
	private final int[] defaultSettings;
	private int membershipStatus;
	private boolean characterEditChangeGender;
	private int anInt1048;
	private String loadingBarText;
	private static int loadedRegions;
	private Archive archiveTitle;
	private int flashingSidebar;
	private boolean multiCombatZone;
	private DoubleEndedQueue stationaryGraphicQueue;
	private final RSInterface chatboxInterface;
	private static int drawCycle;
	private int trackCount;
	private final int SCROLLBAR_GRIP_FOREGROUND;
	private int friendsListAction;
	private final int[] characterEditIdentityKits;
	private int moveItemSlotEnd;
	private int lastActiveInventoryInterface;
	private OnDemandFetcher onDemandFetcher;
	private int regionX;
	private int regionY;
	private int loadingBarPercentage;
	private boolean loadingMap;
	private String[] friendsList;
	private Buffer inStream;
	private int moveItemInterfaceId;
	private int moveItemSlotStart;
	private int activeInterfaceType;
	private int lastMouseX;
	private int lastMouseY;
	private int anInt1089;
	private final int[] expectedCRCs;
	private int[] menuActionData2;
	private int[] menuActionData3;
	private int[] menuActionId;
	private int[] menuActionData1;
	private Sprite[] headIcons;
	private int anInt1098;
	private int anInt1099;
	private int anInt1100;
	private int anInt1101;
	private int anInt1102;
	private boolean drawTabIcons;
	private int systemUpdateTime;
	private static int mouseClickCounter;
	private int membership;
	private String chatboxInputNeededString;
	private RSImageProducer chatSettingImageProducer;
	private RSImageProducer bottomSideIconImageProducer;
	private RSImageProducer topSideIconImageProducer;
	public static Player localPlayer;
	private final String[] playerActionText;
	private final boolean[] playerActionUnpinned;
	private final int[][][] constructMapTiles;
	private final int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	private int cameraRandomisationV;
	private int nextCameraRandomisationV;
	private int menuActionRow;
	private boolean spellSelected;
	private int selectedSpellId;
	private int spellUsableOn;
	private String spellTooltip;
	private boolean inTutorialIsland;
	private IndexedImage redStone1;
	private IndexedImage redStone2;
	private IndexedImage redStone3;
	private IndexedImage redStone1_2;
	private IndexedImage redStone2_2;
	private int playerEnergy;
	private boolean continuedDialogue;
	private Sprite[] crosses;
	private boolean musicEnabled;

	private boolean redrawTab;
	private int unreadMessages;
	private static boolean displayFpsAndMemory;
	public boolean loggedIn;
	private boolean reportAbuseMute;
	private boolean loadGeneratedMap;
	private boolean cutsceneActive;
	static int tick;
	private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	private RSImageProducer tabImageProducer;
	private RSImageProducer gameScreenImageProducer;
	private RSImageProducer chatboxImageProducer;
	private int daysSinceRecoveryChange;
	private RSSocket socket;
	private int privateMessagePointer;
	private int randomisationMinimapZoom;
	private long songStartTime;
	private String enteredUsername;
	private String enteredPassword;
	private boolean genericLoadingError;
	private final int[] objectTypes = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int reportAbuseInterfaceID;
	private DoubleEndedQueue spawnObjectList;
	private int[] chatboxLineOffsets;
	private int[] sidebarOffsets;
	private int[] viewportOffsets;
	private byte[][] terrainData;
	private static int cameraVertical;
	public static int cameraHorizontal;
	private int cameraModificationH;
	private int cameraModificationV;
	private int inventoryOverlayInterfaceID;
	private Buffer stream;
	private int lastAddress;
	private int splitPrivateChat;
	private IndexedImage inventoryBackgroundImage;
	private IndexedImage chatBackgroundImage;
	private String[] menuActionName;
	private final int[] cameraAmplitude;
	static final int[] BEARD_COLOURS = { 9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027,
			1457, 16565, 34991, 25486 };
	private static boolean flagged;
	private final int[] trackIds;
	private int randomisationMinimapRotation;
	private int chatboxScrollMax;
	private String promptInput;
	private int anInt1213;
	private int[][][] intGroundArray;
	private long serverSessionKey;
	private int loginScreenFocus;
	private final IndexedImage[] modIcons;
	private long lastClickTime;
	private int currentTabId;
	private int hintIconNpcId;
	private boolean redrawChatbox;
	private int inputDialogState;
	private int nextSong;
	private boolean songChanging;
	private CollisionMap[] currentCollisionMap;
	public static final int[] BITFIELD_MAX_VALUE;
	private boolean updateChatSettings;
	private int[] mapCoordinates;
	private int[] terrainDataIds;
	private int[] objectDataIds;
	private int lastClickX;
	private int lastClickY;
	private final int[] privateMessages;
	private final int[] trackLoop;
	private boolean lastItemDragged;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] objectData;
	private int tradeMode;
	private int chatEffectsDisabled;
	private final int[] trackDelay;
	private int inTutorial;
	private final boolean rsAlreadyLoaded;
	private int oneMouseButton;
	private int minimapRandomisationCounter;
	private boolean messagePromptRaised;
	private int songStartOffset;
	private byte[][][] tileFlags;
	private int prevSong;
	private int destinationX;
	private int destinationY;
	private int arbitraryDestination;
	private int renderCount;
	private String loginMessage1;
	private String loginMessage2;
	private int playerPositionX;
	private int playerPositionY;
	private GameFont fontSmall;
	private GameFont fontPlain;
	private GameFont fontBold;
	private int chatboxInterfaceId;
	private int cameraRandomisationH;
	private int nextCameraRandomisationH;
	private int[] walkingQueueX;
	private int[] walkingQueueY;
	private boolean itemSelected;
	private int lastItemSelectedSlot;
	private int lastItemSelectedInterface;
	private int useItemId;
	private String selectedItemName;
	private int publicChatMode;
	private static int currentWalkingQueueSize;
	private int currentTrackLoop;

	static {
		EXPERIENCE_TABLE = new int[99];
		int totalExp = 0;
		for (int n = 0; n < 99; n++) {
			final int level = n + 1;
			final int exp = (int) (level + 300D * Math.pow(2D, level / 7D));
			totalExp += exp;
			EXPERIENCE_TABLE[n] = totalExp / 4;
		}

		BITFIELD_MAX_VALUE = new int[32];
		totalExp = 2;
		for (int k = 0; k < 32; k++) {
			BITFIELD_MAX_VALUE[k] = totalExp - 1;
			totalExp += totalExp;
		}

	}

	private Client() {
		this.distanceValues = new int[104][104];
		this.friendsWorldIds = new int[200];
		this.groundArray = new DoubleEndedQueue[4][104][104];
		this.textStream = new Buffer(new byte[5000]);
		this.npcs = new NPC[16384];
		this.npcIds = new int[16384];
		this.actorsToUpdateIds = new int[1000];
		this.loginStream = Buffer.create();
		this.effectsEnabled = true;
		this.openInterfaceId = -1;
		this.skillExperience = new int[Skills.skillsCount];
		this.aBoolean872 = false;
		this.cameraJitter = new int[5];
		this.currentTrackId = -1;
		this.customCameraActive = new boolean[5];
		this.drawFlames = false;
		this.reportAbuseInput = "";
		this.playerListId = -1;
		this.menuOpen = false;
		this.inputString = "";
		this.MAX_ENTITY_COUNT = 2048;
		this.LOCAL_PLAYER_ID = 2047;
		this.players = new Player[this.MAX_ENTITY_COUNT];
		this.localPlayers = new int[this.MAX_ENTITY_COUNT];
		this.playersObserved = new int[this.MAX_ENTITY_COUNT];
		this.playerAppearanceData = new Buffer[this.MAX_ENTITY_COUNT];
		this.nextCameraRandomisationA = 1;
		this.wayPoints = new int[104][104];
		this.SCROLLBAR_GRIP_HIGHLIGHT = 0x766654;
		this.animatedPixels = new byte[16384];
		this.skillLevel = new int[Skills.skillsCount];
		this.ignoreListAsLongs = new long[100];
		this.loadingError = false;
		this.SCROLLBAR_GRIP_LOWLIGHT = 0x332d25;
		this.cameraFrequency = new int[5];
		this.tileRenderCount = new int[104][104];
		this.chatTypes = new int[100];
		this.chatNames = new String[100];
		this.chatMessages = new String[100];
		this.sideIconImage = new IndexedImage[13];
		this.windowFocused = true;
		this.friendsListAsLongs = new long[200];
		this.currentSong = -1;
		this.spriteDrawX = -1;
		this.spriteDrawY = -1;
		this.caches = new FileCache[5];
		this.interfaceSettings = new int[2000];
		this.aBoolean972 = false;
		this.overheadMessageCount = 50;
		this.overheadTextDrawX = new int[this.overheadMessageCount];
		this.overheadTextDrawY = new int[this.overheadMessageCount];
		this.overheadTextHeight = new int[this.overheadMessageCount];
		this.overheadTextWidth = new int[this.overheadMessageCount];
		this.overheadTextColour = new int[this.overheadMessageCount];
		this.overheadTextEffect = new int[this.overheadMessageCount];
		this.overheadTextCycle = new int[this.overheadMessageCount];
		this.overheadTextMessage = new String[this.overheadMessageCount];
		this.lastRegionId = -1;
		this.hitMarkImage = new Sprite[20];
		this.characterEditColours = new int[5];
		this.SCROLLBAR_TRACK_COLOUR = 0x23201b;
		this.amountOrNameInput = "";
		this.projectileQueue = new DoubleEndedQueue();
		this.cameraMovedWrite = false;
		this.walkableInterfaceId = -1;
		this.unknownCameraVariable = new int[5];
		this.characterModelChanged = false;
		this.dialogID = -1;
		this.skillMaxLevel = new int[Skills.skillsCount];
		this.defaultSettings = new int[2000];
		this.characterEditChangeGender = true;
		this.flashingSidebar = -1;
		this.stationaryGraphicQueue = new DoubleEndedQueue();
		this.chatboxInterface = new RSInterface();
		this.SCROLLBAR_GRIP_FOREGROUND = 0x4d4233;
		this.characterEditIdentityKits = new int[7];
		this.loadingMap = false;
		this.friendsList = new String[200];
		this.inStream = Buffer.create();
		this.expectedCRCs = new int[9];
		this.menuActionData2 = new int[500];
		this.menuActionData3 = new int[500];
		this.menuActionId = new int[500];
		this.menuActionData1 = new int[500];
		this.headIcons = new Sprite[20];
		this.drawTabIcons = false;
		this.chatboxInputNeededString = "";
		this.playerActionText = new String[5];
		this.playerActionUnpinned = new boolean[5];
		this.constructMapTiles = new int[4][13][13];
		this.nextCameraRandomisationV = 2;
		this.inTutorialIsland = false;
		this.continuedDialogue = false;
		this.crosses = new Sprite[8];
		this.musicEnabled = true;
		this.redrawTab = false;
		this.loggedIn = false;
		this.reportAbuseMute = false;
		this.loadGeneratedMap = false;
		this.cutsceneActive = false;
		this.randomisationMinimapZoom = 1;
		this.enteredUsername = "";
		this.enteredPassword = "";
		this.genericLoadingError = false;
		this.reportAbuseInterfaceID = -1;
		this.spawnObjectList = new DoubleEndedQueue();
		cameraVertical = 128;
		this.inventoryOverlayInterfaceID = -1;
		this.stream = Buffer.create();
		this.menuActionName = new String[500];
		this.cameraAmplitude = new int[5];
		this.trackIds = new int[50];
		this.randomisationMinimapRotation = 2;
		this.chatboxScrollMax = 78;
		this.promptInput = "";
		this.modIcons = new IndexedImage[2];
		this.currentTabId = 3;
		this.redrawChatbox = false;
		this.songChanging = true;
		this.currentCollisionMap = new CollisionMap[4];
		this.updateChatSettings = false;
		this.privateMessages = new int[100];
		this.trackLoop = new int[50];
		this.lastItemDragged = false;
		this.trackDelay = new int[50];
		this.rsAlreadyLoaded = false;
		this.messagePromptRaised = false;
		this.loginMessage1 = "";
		this.loginMessage2 = "";
		this.chatboxInterfaceId = -1;
		this.nextCameraRandomisationH = 2;
		this.walkingQueueX = new int[4000];
		this.walkingQueueY = new int[4000];
		this.currentTrackLoop = -1;

		this.titleScreen = new TitleScreen();
		this.minimap = new Minimap();
	}

	private void addFriend(final long targetHash) {
		try {
			if (targetHash == 0L) {
                return;
            }
			if (this.friendsCount >= 100 && this.membershipStatus != 1) {
				this.pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			if (this.friendsCount >= 200) {
				this.pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			final String targetName = TextClass.formatName(TextClass.longToName(targetHash));
			for (int f = 0; f < this.friendsCount; f++) {
                if (this.friendsListAsLongs[f] == targetHash) {
					this.pushMessage(targetName + " is already on your friend list", 0, "");
                    return;
                }
            }
			for (int i = 0; i < this.ignoreCount; i++) {
                if (this.ignoreListAsLongs[i] == targetHash) {
					this.pushMessage("Please remove " + targetName + " from your ignore list first", 0, "");
                    return;
                }
            }

			if (targetName.equals(localPlayer.name)) {
				return;
			} else {
				this.friendsList[this.friendsCount] = targetName;
				this.friendsListAsLongs[this.friendsCount] = targetHash;
				this.friendsWorldIds[this.friendsCount] = 0;
				this.friendsCount++;
				this.redrawTab = true;
				this.stream.putOpcode(188);
				this.stream.putLong(targetHash);
				return;
			}
		} catch (final RuntimeException runtimeexception) {
			signlink.reporterror("15283, " + (byte) 68 + ", " + targetHash + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void addIgnore(final long target) {
		try {
			if (target == 0L) {
                return;
            }
			if (this.ignoreCount >= 100) {
				this.pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			final String targetName = TextClass.formatName(TextClass.longToName(target));
			for (int p = 0; p < this.ignoreCount; p++) {
                if (this.ignoreListAsLongs[p] == target) {
					this.pushMessage(targetName + " is already on your ignore list", 0, "");
                    return;
                }
            }
			for (int p = 0; p < this.friendsCount; p++) {
                if (this.friendsListAsLongs[p] == target) {
					this.pushMessage("Please remove " + targetName + " from your friend list first", 0, "");
                    return;
                }
            }

			this.ignoreListAsLongs[this.ignoreCount++] = target;
			this.redrawTab = true;
			this.stream.putOpcode(133);
			this.stream.putLong(target);
			return;
		} catch (final RuntimeException runtimeexception) {
			signlink.reporterror("45688, " + target + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void adjustVolume(final boolean flag, final int volume) {
		signlink.midiVolume = volume;
		if (flag) {
            signlink.midi = "voladjust";
        }
	}

	private boolean animateInterface(final int timePassed, final int interfaceId) {
		boolean animated = false;
		final RSInterface targetInterface = RSInterface.cache[interfaceId];
		for (int c = 0; c < targetInterface.children.length; c++) {
			if (targetInterface.children[c] == -1) {
                break;
            }
			final RSInterface childInterface = RSInterface.cache[targetInterface.children[c]];
			if (childInterface.type == 1) {
                animated |= this.animateInterface(timePassed, childInterface.id);
            }
			if (childInterface.type == 6
					&& (childInterface.animationIdDefault != -1 || childInterface.animationIdActive != -1)) {
				final int animationId = this.interfaceIsActive(childInterface) ? childInterface.animationIdActive
						: childInterface.animationIdDefault;

				if (animationId != -1) {
					final AnimationSequence animation = AnimationSequence.animations[animationId];
					for (childInterface.animationDuration += timePassed; childInterface.animationDuration > animation
							.getFrameLength(childInterface.animationFrame);) {
						childInterface.animationDuration -= animation.getFrameLength(childInterface.animationFrame) + 1;
						childInterface.animationFrame++;
						if (childInterface.animationFrame >= animation.frameCount) {
							childInterface.animationFrame -= animation.frameStep;
							if (childInterface.animationFrame < 0
									|| childInterface.animationFrame >= animation.frameCount) {
                                childInterface.animationFrame = 0;
                            }
						}
						animated = true;
					}

				}
			}
		}

		return animated;
	}

	private void animateTexture(final int textureId) {
		if (!lowMemory) {
			if (Rasterizer.textureLastUsed[17] >= textureId) {
				final IndexedImage background = Rasterizer.textureImages[17];
				final int area = background.width * background.height - 1;
				final int difference = background.width * this.animationTimePassed * 2;
				final byte[] originalPixels = background.pixels;
				final byte[] shiftedPixels = this.animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++) {
                    shiftedPixels[pixel] = originalPixels[pixel - difference & area];
                }

				background.pixels = shiftedPixels;
				this.animatedPixels = originalPixels;
				Rasterizer.resetTexture(17);
			}
			if (Rasterizer.textureLastUsed[24] >= textureId) {
				final IndexedImage background = Rasterizer.textureImages[24];
				final int area = background.width * background.height - 1;
				final int difference = background.width * this.animationTimePassed * 2;
				final byte[] originalPixels = background.pixels;
				final byte[] shiftedPixels = this.animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++) {
                    shiftedPixels[pixel] = originalPixels[pixel - difference & area];
                }

				background.pixels = shiftedPixels;
				this.animatedPixels = originalPixels;
				Rasterizer.resetTexture(24);
			}
			if (Rasterizer.textureLastUsed[34] >= textureId) {
				final IndexedImage background = Rasterizer.textureImages[34];
				final int area = background.width * background.height - 1;
				final int difference = background.width * this.animationTimePassed * 2;
				final byte[] originalPixels = background.pixels;
				final byte[] shiftedPixels = this.animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++) {
                    shiftedPixels[pixel] = originalPixels[pixel - difference & area];
                }

				background.pixels = shiftedPixels;
				this.animatedPixels = originalPixels;
				Rasterizer.resetTexture(34);
			}
		}
	}

	private void appendAnimation(final Entity entity) {
		entity.dynamic = false;
		if (entity.queuedAnimationId != -1) {
			final AnimationSequence animation = AnimationSequence.animations[entity.queuedAnimationId];
			entity.queuedAnimationDuration++;
			if (entity.queuedAnimationFrame < animation.frameCount
					&& entity.queuedAnimationDuration > animation.getFrameLength(entity.queuedAnimationFrame)) {
				entity.queuedAnimationDuration = 0;
				entity.queuedAnimationFrame++;
			}
			if (entity.queuedAnimationFrame >= animation.frameCount) {
				entity.queuedAnimationDuration = 0;
				entity.queuedAnimationFrame = 0;
			}
		}
		if (entity.graphicId != -1 && tick >= entity.graphicEndCycle) {
			if (entity.currentAnimationId < 0) {
                entity.currentAnimationId = 0;
            }
			final AnimationSequence animation = SpotAnimation.cache[entity.graphicId].sequences;
			for (entity.currentAnimationTimeRemaining++; entity.currentAnimationId < animation.frameCount
					&& entity.currentAnimationTimeRemaining > animation
							.getFrameLength(entity.currentAnimationId); entity.currentAnimationId++) {
                entity.currentAnimationTimeRemaining -= animation.getFrameLength(entity.currentAnimationId);
            }

			if (entity.currentAnimationId >= animation.frameCount
					&& (entity.currentAnimationId < 0 || entity.currentAnimationId >= animation.frameCount)) {
                entity.graphicId = -1;
            }
		}
		if (entity.animation != -1 && entity.animationDelay <= 1) {
			final AnimationSequence animation = AnimationSequence.animations[entity.animation];
			if (animation.precedenceAnimating == 1 && entity.stepsRemaining > 0 && entity.tickStart <= tick
					&& entity.tickEnd < tick) {
				entity.animationDelay = 1;
				return;
			}
		}
		if (entity.animation != -1 && entity.animationDelay == 0) {
			final AnimationSequence animation = AnimationSequence.animations[entity.animation];
			for (entity.currentAnimationDuration++; entity.currentAnimationFrame < animation.frameCount
					&& entity.currentAnimationDuration > animation
							.getFrameLength(entity.currentAnimationFrame); entity.currentAnimationFrame++) {
                entity.currentAnimationDuration -= animation.getFrameLength(entity.currentAnimationFrame);
            }

			if (entity.currentAnimationFrame >= animation.frameCount) {
				entity.currentAnimationFrame -= animation.frameStep;
				entity.currentAnimationLoopCount++;
				if (entity.currentAnimationLoopCount >= animation.maximumLoops) {
                    entity.animation = -1;
                }
				if (entity.currentAnimationFrame < 0 || entity.currentAnimationFrame >= animation.frameCount) {
                    entity.animation = -1;
                }
			}
			entity.dynamic = animation.dynamic;
		}
		if (entity.animationDelay > 0) {
            entity.animationDelay--;
        }
	}

	private void appendFocusDestination(final Entity entity) {
		if (entity.degreesToTurn == 0) {
            return;
        }
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			final NPC npc = this.npcs[entity.interactingEntity];
			if (npc != null) {
				final int distanceX = entity.x - npc.x;
				final int distanceY = entity.y - npc.y;
				if (distanceX != 0 || distanceY != 0) {
                    entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7FF;
                }
			}
		}
		if (entity.interactingEntity >= 32768) {
			int targetPlayerId = entity.interactingEntity - 32768;
			if (targetPlayerId == this.playerListId) {
                targetPlayerId = this.LOCAL_PLAYER_ID;
            }
			final Player player = this.players[targetPlayerId];
			if (player != null) {
				final int distanceX = entity.x - player.x;
				final int distanceY = entity.y - player.y;
				if (distanceX != 0 || distanceY != 0) {
                    entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7FF;
                }
			}
		}
		if ((entity.faceTowardX != 0 || entity.faceTowardY != 0)
				&& (entity.waypointCount == 0 || entity.stepsDelayed > 0)) {
			final int distanceX = entity.x - (entity.faceTowardX - this.baseX - this.baseX) * 64;
			final int distanceY = entity.y - (entity.faceTowardY - this.baseY - this.baseY) * 64;
			if (distanceX != 0 || distanceY != 0) {
                entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7FF;
            }
			entity.faceTowardX = 0;
			entity.faceTowardY = 0;
		}
		final int angle = entity.turnDirection - entity.currentRotation & 0x7FF;
		if (angle != 0) {
			if (angle < entity.degreesToTurn || angle > 2048 - entity.degreesToTurn) {
                entity.currentRotation = entity.turnDirection;
            } else if (angle > 1024) {
                entity.currentRotation -= entity.degreesToTurn;
            } else {
                entity.currentRotation += entity.degreesToTurn;
            }
			entity.currentRotation &= 0x7FF;
			if (entity.queuedAnimationId == entity.standAnimationId && entity.currentRotation != entity.turnDirection) {
				if (entity.standTurnAnimationId != -1) {
					entity.queuedAnimationId = entity.standTurnAnimationId;
					return;
				}
				entity.queuedAnimationId = entity.walkAnimationId;
			}
		}
	}

	private void build3dScreenMenu() {
		if (this.itemSelected == false && this.spellSelected == false) {
			this.menuActionName[this.menuActionRow] = "Walk here";
			this.menuActionId[this.menuActionRow] = 516;
			this.menuActionData2[this.menuActionRow] = super.mouseX;
			this.menuActionData3[this.menuActionRow] = super.mouseY;
			this.menuActionRow++;
		}
		int originalHash = -1;
		for (int k = 0; k < Model.resourceCount; k++) {
			final int hash = Model.resourceId[k];
			final int x = hash & 0x7F;
			final int y = hash >> 7 & 0x7F;
			final int type = hash >> 29 & 3;
			final int objectId = hash >> 14 & 0x7FFF;
			if (hash == originalHash) {
                continue;
            }
			originalHash = hash;
			if (type == 2 && this.worldController.getConfig(hash, x, y, this.plane) >= 0) {
				GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
				if (object.childIds != null) {
                    object = object.getChildDefinition();
                }
				if (object == null) {
                    continue;
                }
				if (this.itemSelected) {
					this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @cya@" + object.name;
					this.menuActionId[this.menuActionRow] = 62;
					this.menuActionData1[this.menuActionRow] = hash;
					this.menuActionData2[this.menuActionRow] = x;
					this.menuActionData3[this.menuActionRow] = y;
					this.menuActionRow++;
				} else if (this.spellSelected) {
					if ((this.spellUsableOn & 4) == 4) {
						this.menuActionName[this.menuActionRow] = this.spellTooltip + " @cya@" + object.name;
						this.menuActionId[this.menuActionRow] = 956;
						this.menuActionData1[this.menuActionRow] = hash;
						this.menuActionData2[this.menuActionRow] = x;
						this.menuActionData3[this.menuActionRow] = y;
						this.menuActionRow++;
					}
				} else {
					if (object.actions != null) {
						for (int action = 4; action >= 0; action--) {
                            if (object.actions[action] != null) {
								this.menuActionName[this.menuActionRow] = object.actions[action] + " @cya@" + object.name;
                                if (action == 0) {
									this.menuActionId[this.menuActionRow] = 502;
                                }
                                if (action == 1) {
									this.menuActionId[this.menuActionRow] = 900;
                                }
                                if (action == 2) {
									this.menuActionId[this.menuActionRow] = 113;
                                }
                                if (action == 3) {
									this.menuActionId[this.menuActionRow] = 872;
                                }
                                if (action == 4) {
									this.menuActionId[this.menuActionRow] = 1062;
                                }
								this.menuActionData1[this.menuActionRow] = hash;
								this.menuActionData2[this.menuActionRow] = x;
								this.menuActionData3[this.menuActionRow] = y;
								this.menuActionRow++;
                            }
                        }

					}
					this.menuActionName[this.menuActionRow] = "Examine @cya@" + object.name + " @gre@(@whi@" + objectId
							+ "@gre@) (@whi@" + (x + this.baseX) + "," + (y + this.baseY) + "@gre@)";
					this.menuActionId[this.menuActionRow] = 1226;
					this.menuActionData1[this.menuActionRow] = object.id << 14;
					this.menuActionData2[this.menuActionRow] = x;
					this.menuActionData3[this.menuActionRow] = y;
					this.menuActionRow++;
				}
			}
			if (type == 1) {
				final NPC npc = this.npcs[objectId];
				if (npc.npcDefinition.boundaryDimension == 1 && (npc.x & 0x7F) == 64 && (npc.y & 0x7F) == 64) {
					for (int n = 0; n < this.npcCount; n++) {
						final NPC npc2 = this.npcs[this.npcIds[n]];
						if (npc2 != null && npc2 != npc && npc2.npcDefinition.boundaryDimension == 1 && npc2.x == npc.x
								&& npc2.y == npc.y) {
							this.buildMenuForNPC(npc2.npcDefinition, this.npcIds[n], y, x);
                        }
					}

					for (int p = 0; p < this.localPlayerCount; p++) {
						final Player player = this.players[this.localPlayers[p]];
						if (player != null && player.x == npc.x && player.y == npc.y) {
							this.buildMenuForPlayer(x, this.localPlayers[p], player, y);
                        }
					}

				}
				this.buildMenuForNPC(npc.npcDefinition, objectId, y, x);
			}
			if (type == 0) {
				final Player player = this.players[objectId];
				if ((player.x & 0x7F) == 64 && (player.y & 0x7F) == 64) {
					for (int n = 0; n < this.npcCount; n++) {
						final NPC npc = this.npcs[this.npcIds[n]];
						if (npc != null && npc.npcDefinition.boundaryDimension == 1 && npc.x == player.x
								&& npc.y == player.y) {
							this.buildMenuForNPC(npc.npcDefinition, this.npcIds[n], y, x);
                        }
					}

					for (int p = 0; p < this.localPlayerCount; p++) {
						final Player player2 = this.players[this.localPlayers[p]];
						if (player2 != null && player2 != player && player2.x == player.x && player2.y == player.y) {
							this.buildMenuForPlayer(x, this.localPlayers[p], player2, y);
                        }
					}

				}
				this.buildMenuForPlayer(x, objectId, player, y);
			}
			if (type == 3) {
				final DoubleEndedQueue itemStack = this.groundArray[this.plane][x][y];
				if (itemStack != null) {
					for (Item item = (Item) itemStack.peekBack(); item != null; item = (Item) itemStack.getPrevious()) {
						final ItemDefinition definition = ItemDefinition.getDefinition(item.itemId);
						if (this.itemSelected) {
							this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @lre@" + definition.name;
							this.menuActionId[this.menuActionRow] = 511;
							this.menuActionData1[this.menuActionRow] = item.itemId;
							this.menuActionData2[this.menuActionRow] = x;
							this.menuActionData3[this.menuActionRow] = y;
							this.menuActionRow++;
						} else if (this.spellSelected) {
							if ((this.spellUsableOn & 1) == 1) {
								this.menuActionName[this.menuActionRow] = this.spellTooltip + " @lre@" + definition.name;
								this.menuActionId[this.menuActionRow] = 94;
								this.menuActionData1[this.menuActionRow] = item.itemId;
								this.menuActionData2[this.menuActionRow] = x;
								this.menuActionData3[this.menuActionRow] = y;
								this.menuActionRow++;
							}
						} else {
							for (int a = 4; a >= 0; a--) {
                                if (definition.groundActions != null && definition.groundActions[a] != null) {
									this.menuActionName[this.menuActionRow] = definition.groundActions[a] + " @lre@"
                                            + definition.name;
                                    if (a == 0) {
										this.menuActionId[this.menuActionRow] = 652;
                                    }
                                    if (a == 1) {
										this.menuActionId[this.menuActionRow] = 567;
                                    }
                                    if (a == 2) {
										this.menuActionId[this.menuActionRow] = 234;
                                    }
                                    if (a == 3) {
										this.menuActionId[this.menuActionRow] = 244;
                                    }
                                    if (a == 4) {
										this.menuActionId[this.menuActionRow] = 213;
                                    }
									this.menuActionData1[this.menuActionRow] = item.itemId;
									this.menuActionData2[this.menuActionRow] = x;
									this.menuActionData3[this.menuActionRow] = y;
									this.menuActionRow++;
                                } else if (a == 2) {
									this.menuActionName[this.menuActionRow] = "Take @lre@" + definition.name;
									this.menuActionId[this.menuActionRow] = 234;
									this.menuActionData1[this.menuActionRow] = item.itemId;
									this.menuActionData2[this.menuActionRow] = x;
									this.menuActionData3[this.menuActionRow] = y;
									this.menuActionRow++;
                                }
                            }

							this.menuActionName[this.menuActionRow] = "Examine @lre@" + definition.name + " @gre@(@whi@"
									+ item.itemId + "@gre@)";
							this.menuActionId[this.menuActionRow] = 1448;
							this.menuActionData1[this.menuActionRow] = item.itemId;
							this.menuActionData2[this.menuActionRow] = x;
							this.menuActionData3[this.menuActionRow] = y;
							this.menuActionRow++;
						}
					}

				}
			}
		}
	}

	private void buildChatboxMenu(final int y) {
		int rowCount = 0;
		for (int m = 0; m < 100; m++) {
			if (this.chatMessages[m] == null) {
                continue;
            }
			final int chatType = this.chatTypes[m];
			final int _y = (70 - rowCount * 14) + this.anInt1089 + 4;
			if (_y < -20) {
                break;
            }
			String chatName = this.chatNames[m];
			if (chatName != null && chatName.startsWith("@cr1@")) {
				chatName = chatName.substring(5);
			}
			if (chatName != null && chatName.startsWith("@cr2@")) {
				chatName = chatName.substring(5);
			}
			if (chatType == 0) {
                rowCount++;
            }
			if ((chatType == 1 || chatType == 2)
					&& (chatType == 1 || this.publicChatMode == 0 || this.publicChatMode == 1 && this.isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y && !chatName.equals(localPlayer.name)) {
					if (this.playerRights >= 1) {
						this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + chatName;
						this.menuActionId[this.menuActionRow] = 606;
						this.menuActionRow++;
					}
					this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 42;
					this.menuActionRow++;
					this.menuActionName[this.menuActionRow] = "Add friend @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 337;
					this.menuActionRow++;
				}
				rowCount++;
			}
			if ((chatType == 3 || chatType == 7) && this.splitPrivateChat == 0
					&& (chatType == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					if (this.playerRights >= 1) {
						this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + chatName;
						this.menuActionId[this.menuActionRow] = 606;
						this.menuActionRow++;
					}
					this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 42;
					this.menuActionRow++;
					this.menuActionName[this.menuActionRow] = "Add friend @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 337;
					this.menuActionRow++;
				}
				rowCount++;
			}
			if (chatType == 4 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					this.menuActionName[this.menuActionRow] = "Accept trade @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 484;
					this.menuActionRow++;
				}
				rowCount++;
			}
			if ((chatType == 5 || chatType == 6) && this.splitPrivateChat == 0 && this.privateChatMode < 2) {
                rowCount++;
            }
			if (chatType == 8 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					this.menuActionName[this.menuActionRow] = "Accept challenge @whi@" + chatName;
					this.menuActionId[this.menuActionRow] = 6;
					this.menuActionRow++;
				}
				rowCount++;
			}
		}

	}

	private boolean buildFriendsListMenu(final RSInterface rsInterface) {
		int type = rsInterface.contentType;
		if (type >= 1 && type <= 200 || type >= 701 && type <= 900) {
			if (type >= 801) {
                type -= 701;
            } else if (type >= 701) {
                type -= 601;
            } else if (type >= 101) {
                type -= 101;
            } else {
                type--;
            }
			this.menuActionName[this.menuActionRow] = "Remove @whi@" + this.friendsList[type];
			this.menuActionId[this.menuActionRow] = 792;
			this.menuActionRow++;
			this.menuActionName[this.menuActionRow] = "Message @whi@" + this.friendsList[type];
			this.menuActionId[this.menuActionRow] = 639;
			this.menuActionRow++;
			return true;
		}
		if (type >= 401 && type <= 500) {
			this.menuActionName[this.menuActionRow] = "Remove @whi@" + rsInterface.textDefault;
			this.menuActionId[this.menuActionRow] = 322;
			this.menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	private void buildInterfaceMenu(final int i, final RSInterface rsInterface, final int k, final int l, final int i1, final int j1) {
		if (rsInterface.type != 0 || rsInterface.children == null || rsInterface.hoverOnly) {
            return;
        }
		if (k < i || i1 < l || k > i + rsInterface.width || i1 > l + rsInterface.height) {
            return;
        }
		final int childCount = rsInterface.children.length;
		for (int child = 0; child < childCount; child++) {
			int i2 = rsInterface.childX[child] + i;
			int j2 = (rsInterface.childY[child] + l) - j1;
			final RSInterface childInterface = RSInterface.cache[rsInterface.children[child]];
			i2 += childInterface.x;
			j2 += childInterface.y;
			if ((childInterface.hoveredPopup >= 0 || childInterface.colourDefaultHover != 0) && k >= i2 && i1 >= j2
					&& k < i2 + childInterface.width && i1 < j2 + childInterface.height) {
                if (childInterface.hoveredPopup >= 0) {
					this.anInt886 = childInterface.hoveredPopup;
                } else {
					this.anInt886 = childInterface.id;
                }
            }
			if (childInterface.type == 0) {
				this.buildInterfaceMenu(i2, childInterface, k, j2, i1, childInterface.scrollPosition);
				if (childInterface.scrollMax > childInterface.height) {
					this.scrollInterface(i2 + childInterface.width, childInterface.height, k, i1, childInterface, j2, true,
                            childInterface.scrollMax);
                }
			} else {
				if (childInterface.actionType == 1 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					boolean flag = false;
					if (childInterface.contentType != 0) {
                        flag = this.buildFriendsListMenu(childInterface);
                    }
					if (!flag) {
						// System.out.println("1"+class9_1.tooltip + ", " +
						// class9_1.interfaceID);
						this.menuActionName[this.menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
						this.menuActionId[this.menuActionRow] = 315;
						this.menuActionData3[this.menuActionRow] = childInterface.id;
						this.menuActionRow++;
					}
				}
				if (childInterface.actionType == 2 && this.spellSelected == false && k >= i2 && i1 >= j2
						&& k < i2 + childInterface.width && i1 < j2 + childInterface.height) {
					String actionName = childInterface.selectedActionName;
					if (actionName.contains(" ")) {
                        actionName = actionName.substring(0, actionName.indexOf(" "));
                    }
					this.menuActionName[this.menuActionRow] = actionName + " @gre@" + childInterface.spellName;
					this.menuActionId[this.menuActionRow] = 626;
					this.menuActionData3[this.menuActionRow] = childInterface.id;
					this.menuActionRow++;
				}
				if (childInterface.actionType == 3 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					this.menuActionName[this.menuActionRow] = "Close";
					this.menuActionId[this.menuActionRow] = 200;
					this.menuActionData3[this.menuActionRow] = childInterface.id;
					this.menuActionRow++;
				}
				if (childInterface.actionType == 4 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					// System.out.println("2"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					this.menuActionName[this.menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					this.menuActionId[this.menuActionRow] = 169;
					this.menuActionData3[this.menuActionRow] = childInterface.id;
					this.menuActionRow++;
				}
				if (childInterface.actionType == 5 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					// System.out.println("3"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					this.menuActionName[this.menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					this.menuActionId[this.menuActionRow] = 646;
					this.menuActionData3[this.menuActionRow] = childInterface.id;
					this.menuActionRow++;
				}
				if (childInterface.actionType == 6 && !this.continuedDialogue && k >= i2 && i1 >= j2
						&& k < i2 + childInterface.width && i1 < j2 + childInterface.height) {
					// System.out.println("4"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					this.menuActionName[this.menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					this.menuActionId[this.menuActionRow] = 679;
					this.menuActionData3[this.menuActionRow] = childInterface.id;
					this.menuActionRow++;
				}
				if (childInterface.type == 2) {
					int slot = 0;
					for (int row = 0; row < childInterface.height; row++) {
						for (int column = 0; column < childInterface.width; column++) {
							int j3 = i2 + column * (32 + childInterface.inventorySpritePaddingColumn);
							int k3 = j2 + row * (32 + childInterface.inventorySpritePaddingRow);
							if (slot < 20) {
								j3 += childInterface.spritesX[slot];
								k3 += childInterface.spritesY[slot];
							}
							if (k >= j3 && i1 >= k3 && k < j3 + 32 && i1 < k3 + 32) {
								this.moveItemSlotEnd = slot;
								this.lastActiveInventoryInterface = childInterface.id;
								if (childInterface.inventoryItemId[slot] > 0) {
									final ItemDefinition itemDef = ItemDefinition
											.getDefinition(childInterface.inventoryItemId[slot] - 1);
									if (this.itemSelected && childInterface.inventory) {
										if (childInterface.id != this.lastItemSelectedInterface
												|| slot != this.lastItemSelectedSlot) {
											this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @lre@"
													+ itemDef.name;
											this.menuActionId[this.menuActionRow] = 870;
											this.menuActionData1[this.menuActionRow] = itemDef.id;
											this.menuActionData2[this.menuActionRow] = slot;
											this.menuActionData3[this.menuActionRow] = childInterface.id;
											this.menuActionRow++;
										}
									} else if (this.spellSelected && childInterface.inventory) {
										if ((this.spellUsableOn & 0x10) == 16) {
											this.menuActionName[this.menuActionRow] = this.spellTooltip + " @lre@" + itemDef.name;
											this.menuActionId[this.menuActionRow] = 543;
											this.menuActionData1[this.menuActionRow] = itemDef.id;
											this.menuActionData2[this.menuActionRow] = slot;
											this.menuActionData3[this.menuActionRow] = childInterface.id;
											this.menuActionRow++;
										}
									} else {
										if (childInterface.inventory) {
											for (int l3 = 4; l3 >= 3; l3--) {
                                                if (itemDef.actions != null && itemDef.actions[l3] != null) {
													this.menuActionName[this.menuActionRow] = itemDef.actions[l3] + " @lre@"
                                                            + itemDef.name;
                                                    if (l3 == 3) {
														this.menuActionId[this.menuActionRow] = 493;
                                                    }
                                                    if (l3 == 4) {
														this.menuActionId[this.menuActionRow] = 847;
                                                    }
													this.menuActionData1[this.menuActionRow] = itemDef.id;
													this.menuActionData2[this.menuActionRow] = slot;
													this.menuActionData3[this.menuActionRow] = childInterface.id;
													this.menuActionRow++;
                                                } else if (l3 == 4) {
													this.menuActionName[this.menuActionRow] = "Drop @lre@" + itemDef.name;
													this.menuActionId[this.menuActionRow] = 847;
													this.menuActionData1[this.menuActionRow] = itemDef.id;
													this.menuActionData2[this.menuActionRow] = slot;
													this.menuActionData3[this.menuActionRow] = childInterface.id;
													this.menuActionRow++;
                                                }
                                            }

										}
										if (childInterface.usableItemInterface) {
											this.menuActionName[this.menuActionRow] = "Use @lre@" + itemDef.name;
											this.menuActionId[this.menuActionRow] = 447;
											this.menuActionData1[this.menuActionRow] = itemDef.id;
											this.menuActionData2[this.menuActionRow] = slot;
											this.menuActionData3[this.menuActionRow] = childInterface.id;
											this.menuActionRow++;
										}
										if (childInterface.inventory && itemDef.actions != null) {
											for (int i4 = 2; i4 >= 0; i4--) {
                                                if (itemDef.actions[i4] != null) {
													this.menuActionName[this.menuActionRow] = itemDef.actions[i4] + " @lre@"
                                                            + itemDef.name;
                                                    if (i4 == 0) {
														this.menuActionId[this.menuActionRow] = 74;
                                                    }
                                                    if (i4 == 1) {
														this.menuActionId[this.menuActionRow] = 454;
                                                    }
                                                    if (i4 == 2) {
														this.menuActionId[this.menuActionRow] = 539;
                                                    }
													this.menuActionData1[this.menuActionRow] = itemDef.id;
													this.menuActionData2[this.menuActionRow] = slot;
													this.menuActionData3[this.menuActionRow] = childInterface.id;
													this.menuActionRow++;
                                                }
                                            }

										}
										if (childInterface.actions != null) {
											for (int j4 = 4; j4 >= 0; j4--) {
                                                if (childInterface.actions[j4] != null) {
													this.menuActionName[this.menuActionRow] = childInterface.actions[j4]
                                                            + " @lre@" + itemDef.name;
                                                    if (j4 == 0) {
														this.menuActionId[this.menuActionRow] = 632;
                                                    }
                                                    if (j4 == 1) {
														this.menuActionId[this.menuActionRow] = 78;
                                                    }
                                                    if (j4 == 2) {
														this.menuActionId[this.menuActionRow] = 867;
                                                    }
                                                    if (j4 == 3) {
														this.menuActionId[this.menuActionRow] = 431;
                                                    }
                                                    if (j4 == 4) {
														this.menuActionId[this.menuActionRow] = 53;
                                                    }
													this.menuActionData1[this.menuActionRow] = itemDef.id;
													this.menuActionData2[this.menuActionRow] = slot;
													this.menuActionData3[this.menuActionRow] = childInterface.id;
													this.menuActionRow++;
                                                }
                                            }

										}
										this.menuActionName[this.menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@"
												+ (childInterface.inventoryItemId[slot] - 1) + "@gre@)";
										this.menuActionId[this.menuActionRow] = 1125;
										this.menuActionData1[this.menuActionRow] = itemDef.id;
										this.menuActionData2[this.menuActionRow] = slot;
										this.menuActionData3[this.menuActionRow] = childInterface.id;
										this.menuActionRow++;
									}
								}
							}
							slot++;
						}

					}

				}
			}
		}

	}

	private void buildMenuForNPC(EntityDefinition definition, final int data1, final int data3, final int data2) {
		if (this.menuActionRow >= 400) {
            return;
        }
		if (definition.childrenIDs != null) {
            definition = definition.getChildDefinition();
        }
		if (definition == null) {
            return;
        }
		if (!definition.clickable) {
            return;
        }
		String displayName = definition.name;
		if (definition.combatLevel != 0) {
            displayName = displayName + getCombatLevelDifferenceColour(localPlayer.combatLevel, definition.combatLevel)
                    + " (level-" + definition.combatLevel + ")";
        }
		if (this.itemSelected) {
			this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @yel@" + displayName;
			this.menuActionId[this.menuActionRow] = 582;
			this.menuActionData1[this.menuActionRow] = data1;
			this.menuActionData2[this.menuActionRow] = data2;
			this.menuActionData3[this.menuActionRow] = data3;
			this.menuActionRow++;
			return;
		}
		if (this.spellSelected) {
			if ((this.spellUsableOn & 2) == 2) {
				this.menuActionName[this.menuActionRow] = this.spellTooltip + " @yel@" + displayName;
				this.menuActionId[this.menuActionRow] = 413;
				this.menuActionData1[this.menuActionRow] = data1;
				this.menuActionData2[this.menuActionRow] = data2;
				this.menuActionData3[this.menuActionRow] = data3;
				this.menuActionRow++;
			}
		} else {
			if (definition.actions != null) {
				for (int a = 4; a >= 0; a--) {
                    if (definition.actions[a] != null && !definition.actions[a].equalsIgnoreCase("attack")) {
						this.menuActionName[this.menuActionRow] = definition.actions[a] + " @yel@" + displayName;
                        if (a == 0) {
							this.menuActionId[this.menuActionRow] = 20;
                        }
                        if (a == 1) {
							this.menuActionId[this.menuActionRow] = 412;
                        }
                        if (a == 2) {
							this.menuActionId[this.menuActionRow] = 225;
                        }
                        if (a == 3) {
							this.menuActionId[this.menuActionRow] = 965;
                        }
                        if (a == 4) {
							this.menuActionId[this.menuActionRow] = 478;
                        }
						this.menuActionData1[this.menuActionRow] = data1;
						this.menuActionData2[this.menuActionRow] = data2;
						this.menuActionData3[this.menuActionRow] = data3;
						this.menuActionRow++;
                    }
                }

			}
			if (definition.actions != null) {
				for (int a = 4; a >= 0; a--) {
                    if (definition.actions[a] != null && definition.actions[a].equalsIgnoreCase("attack")) {
                        int modifier = 0;
                        if (definition.combatLevel > localPlayer.combatLevel) {
                            modifier = 2000;
                        }
						this.menuActionName[this.menuActionRow] = definition.actions[a] + " @yel@" + displayName;
                        if (a == 0) {
							this.menuActionId[this.menuActionRow] = 20 + modifier;
                        }
                        if (a == 1) {
							this.menuActionId[this.menuActionRow] = 412 + modifier;
                        }
                        if (a == 2) {
							this.menuActionId[this.menuActionRow] = 225 + modifier;
                        }
                        if (a == 3) {
							this.menuActionId[this.menuActionRow] = 965 + modifier;
                        }
                        if (a == 4) {
							this.menuActionId[this.menuActionRow] = 478 + modifier;
                        }
						this.menuActionData1[this.menuActionRow] = data1;
						this.menuActionData2[this.menuActionRow] = data2;
						this.menuActionData3[this.menuActionRow] = data3;
						this.menuActionRow++;
                    }
                }

			}
			this.menuActionName[this.menuActionRow] = "Examine @yel@" + displayName + " @gre@(@whi@" + definition.id + "@gre@)";
			this.menuActionId[this.menuActionRow] = 1025;
			this.menuActionData1[this.menuActionRow] = data1;
			this.menuActionData2[this.menuActionRow] = data2;
			this.menuActionData3[this.menuActionRow] = data3;
			this.menuActionRow++;
		}
	}

	private void buildMenuForPlayer(final int data2, final int data1, final Player player, final int data3) {
		if (player == localPlayer) {
            return;
        }
		if (this.menuActionRow >= 400) {
            return;
        }
		final String displayName;
		if (player.skill == 0) {
            displayName = player.name + getCombatLevelDifferenceColour(localPlayer.combatLevel, player.combatLevel)
                    + " (level-" + player.combatLevel + ")";
        } else {
            displayName = player.name + " (skill-" + player.skill + ")";
        }
		if (this.itemSelected) {
			this.menuActionName[this.menuActionRow] = "Use " + this.selectedItemName + " with @whi@" + displayName;
			this.menuActionId[this.menuActionRow] = 491;
			this.menuActionData1[this.menuActionRow] = data1;
			this.menuActionData2[this.menuActionRow] = data2;
			this.menuActionData3[this.menuActionRow] = data3;
			this.menuActionRow++;
		} else if (this.spellSelected) {
			if ((this.spellUsableOn & 8) == 8) {
				this.menuActionName[this.menuActionRow] = this.spellTooltip + " @whi@" + displayName;
				this.menuActionId[this.menuActionRow] = 365;
				this.menuActionData1[this.menuActionRow] = data1;
				this.menuActionData2[this.menuActionRow] = data2;
				this.menuActionData3[this.menuActionRow] = data3;
				this.menuActionRow++;
			}
		} else {
			for (int a = 4; a >= 0; a--) {
                if (this.playerActionText[a] != null) {
					this.menuActionName[this.menuActionRow] = this.playerActionText[a] + " @whi@" + displayName;
                    int modifier = 0;
                    if (this.playerActionText[a].equalsIgnoreCase("attack")) {
                        if (player.combatLevel > localPlayer.combatLevel) {
                            modifier = 2000;
                        }
                        if (localPlayer.team != 0 && player.team != 0) {
                            if (localPlayer.team == player.team) {
                                modifier = 2000;
                            } else {
                                modifier = 0;
                            }
                        }
                    } else if (this.playerActionUnpinned[a]) {
                        modifier = 2000;
                    }
                    if (a == 0) {
						this.menuActionId[this.menuActionRow] = 561 + modifier;
                    }
                    if (a == 1) {
						this.menuActionId[this.menuActionRow] = 779 + modifier;
                    }
                    if (a == 2) {
						this.menuActionId[this.menuActionRow] = 27 + modifier;
                    }
                    if (a == 3) {
						this.menuActionId[this.menuActionRow] = 577 + modifier;
                    }
                    if (a == 4) {
						this.menuActionId[this.menuActionRow] = 729 + modifier;
                    }
					this.menuActionData1[this.menuActionRow] = data1;
					this.menuActionData2[this.menuActionRow] = data2;
					this.menuActionData3[this.menuActionRow] = data3;
					this.menuActionRow++;
                }
            }

		}
		for (int a = 0; a < this.menuActionRow; a++) {
            if (this.menuActionId[a] == 516) {
				this.menuActionName[a] = "Walk here @whi@" + displayName;
                return;
            }
        }

	}

	private void buildSplitPrivateChatMenu() {
		if (this.splitPrivateChat == 0) {
            return;
        }
		int line = 0;
		if (this.systemUpdateTime != 0) {
            line = 1;
        }
		for (int c = 0; c < 100; c++) {
            if (this.chatMessages[c] != null) {
                final int chatType = this.chatTypes[c];
                String chatName = this.chatNames[c];
                if (chatName != null && chatName.startsWith("@cr1@")) {
                    chatName = chatName.substring(5);
                }
                if (chatName != null && chatName.startsWith("@cr2@")) {
                    chatName = chatName.substring(5);
                }
                if ((chatType == 3 || chatType == 7) && (chatType == 7 || this.privateChatMode == 0
                        || this.privateChatMode == 1 && this.isFriendOrSelf(chatName))) {
                    final int height = 329 - line * 13;
                    if (super.mouseX > 4 && super.mouseY - 4 > height - 10 && super.mouseY - 4 <= height + 3) {
                        int width = this.fontPlain.getTextDisplayedWidth("From:  " + chatName + this.chatMessages[c]) + 25;
                        if (width > 450) {
                            width = 450;
                        }
                        if (super.mouseX < 4 + width) {
                            if (this.playerRights >= 1) {
								this.menuActionName[this.menuActionRow] = "Report abuse @whi@" + chatName;
								this.menuActionId[this.menuActionRow] = 2606;
								this.menuActionRow++;
                            }
							this.menuActionName[this.menuActionRow] = "Add ignore @whi@" + chatName;
							this.menuActionId[this.menuActionRow] = 2042;
							this.menuActionRow++;
							this.menuActionName[this.menuActionRow] = "Add friend @whi@" + chatName;
							this.menuActionId[this.menuActionRow] = 2337;
							this.menuActionRow++;
                        }
                    }
                    if (++line >= 5) {
                        return;
                    }
                }
                if ((chatType == 5 || chatType == 6) && this.privateChatMode < 2 && ++line >= 5) {
                    return;
                }
            }
        }

	}



	private void calculateEntityScreenPosition(final Entity entity, final int height) {
		this.calculateScreenPosition(entity.x, height, entity.y);

		// aryan entity.entScreenX = spriteDrawX; entity.entScreenY =
		// spriteDrawY;
	}

	private void calculateScreenPosition(int x, final int height, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			this.spriteDrawX = -1;
			this.spriteDrawY = -1;
			return;
		}
		int z = this.getFloorDrawHeight(this.plane, y, x) - height;
		x -= this.cameraPositionX;
		z -= this.cameraPositionZ;
		y -= this.cameraPositionY;
		final int sineHorizontal = Model.SINE[this.cameraVerticalRotation];
		final int cosineHorizontal = Model.COSINE[this.cameraVerticalRotation];
		final int sineVertical = Model.SINE[this.cameraHorizontalRotation];
		final int cosineVertical = Model.COSINE[this.cameraHorizontalRotation];
		int temp = y * sineVertical + x * cosineVertical >> 16;
		y = y * cosineVertical - x * sineVertical >> 16;
		x = temp;
		temp = z * cosineHorizontal - y * sineHorizontal >> 16;
		y = z * sineHorizontal + y * cosineHorizontal >> 16;
		z = temp;
		if (y >= 50) {
			this.spriteDrawX = Rasterizer.centreX + (x << 9) / y;
			this.spriteDrawY = Rasterizer.centreY + (z << 9) / y;
		} else {
			this.spriteDrawX = -1;
			this.spriteDrawY = -1;
		}
	}

	private void changeGender() {
		this.characterModelChanged = true;
		for (int type = 0; type < 7; type++) {
			this.characterEditIdentityKits[type] = -1;
			for (int kit = 0; kit < IdentityKit.count; kit++) {
				if (IdentityKit.cache[kit].widgetDisplayed
						|| IdentityKit.cache[kit].partId != type + (this.characterEditChangeGender ? 0 : 7)) {
                    continue;
                }
				this.characterEditIdentityKits[type] = kit;
				break;
			}

		}

	}

	private void checkTutorialIsland() {
		this.inTutorial = 0;
		final int x = (localPlayer.x >> 7) + this.baseX;
		final int y = (localPlayer.y >> 7) + this.baseY;
		if (x >= 3053 && x <= 3156 && y >= 3056 && y <= 3136) {
			this.inTutorial = 1;
        }
		if (x >= 3072 && x <= 3118 && y >= 9492 && y <= 9535) {
			this.inTutorial = 1;
        }
		if (this.inTutorial == 1 && x >= 3139 && x <= 3199 && y >= 3008 && y <= 3062) {
			this.inTutorial = 0;
        }
	}

	@Override
	public void cleanUpForQuit() {
		signlink.reporterror = false;
		try {
			if (this.socket != null) {
				this.socket.close();
            }
		} catch (final Exception _ex) {
		}
		this.socket = null;
		this.stopMidi();
		if (this.mouseDetection != null) {
			this.mouseDetection.running = false;
        }
		this.mouseDetection = null;
		this.onDemandFetcher.disable();
		this.onDemandFetcher = null;
		this.textStream = null;
		this.stream = null;
		this.loginStream = null;
		this.inStream = null;
		this.mapCoordinates = null;
		this.terrainData = null;
		this.objectData = null;
		this.terrainDataIds = null;
		this.objectDataIds = null;
		this.intGroundArray = null;
		this.tileFlags = null;
		this.worldController = null;
		this.currentCollisionMap = null;
		this.wayPoints = null;
		this.distanceValues = null;
		this.walkingQueueX = null;
		this.walkingQueueY = null;
		this.animatedPixels = null;
		this.tabImageProducer = null;
		this.gameScreenImageProducer = null;
		this.chatboxImageProducer = null;
		this.chatSettingImageProducer = null;
		this.bottomSideIconImageProducer = null;
		this.topSideIconImageProducer = null;
		this.backLeftIP1 = null;
		this.backLeftIP2 = null;
		this.backRightIP1 = null;
		this.backRightIP2 = null;
		this.backTopIP1 = null;
		this.backVmidIP1 = null;
		this.backVmidIP2 = null;
		this.backVmidIP3 = null;
		this.backVmidIP2_2 = null;
		this.inventoryBackgroundImage = null;
		this.chatBackgroundImage = null;
		this.backBase1Image = null;
		this.backBase2Image = null;
		this.backHmid1Image = null;
		this.sideIconImage = null;
		this.redStone1 = null;
		this.redStone2 = null;
		this.redStone3 = null;
		this.redStone1_2 = null;
		this.redStone2_2 = null;
		this.redStone1_3 = null;
		this.redStone2_3 = null;
		this.redStone3_2 = null;
		this.redStone1_4 = null;
		this.redStone2_4 = null;
		this.hitMarkImage = null;
		this.headIcons = null;
		this.crosses = null;
		this.tileRenderCount = null;
		this.players = null;
		this.localPlayers = null;
		this.playersObserved = null;
		this.playerAppearanceData = null;
		this.actorsToUpdateIds = null;
		this.npcs = null;
		this.npcIds = null;
		this.groundArray = null;
		this.spawnObjectList = null;
		this.projectileQueue = null;
		this.stationaryGraphicQueue = null;
		this.menuActionData2 = null;
		this.menuActionData3 = null;
		this.menuActionId = null;
		this.menuActionData1 = null;
		this.menuActionName = null;
		this.interfaceSettings = null;
		this.friendsList = null;
		this.friendsListAsLongs = null;
		this.friendsWorldIds = null;

		this.titleScreen.nullLoader();

		this.nullLoader();
		GameObjectDefinition.nullLoader();
		EntityDefinition.nullLoader();
		ItemDefinition.nullLoader();
		FloorDefinition.cache = null;
		IdentityKit.cache = null;
		RSInterface.cache = null;
		AnimationSequence.animations = null;
		SpotAnimation.cache = null;
		SpotAnimation.modelCache = null;
		Varp.values = null;
		super.fullGameScreen = null;
		Player.mruNodes = null;
		Rasterizer.nullLoader();
		WorldController.nullLoader();
		Model.nullLoader();
		Animation.nullLoader();
		System.gc();
	}

	private void clearObjectSpawnRequests() {
		GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList.peekFront();
		for (; spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList.getPrevious()) {
            if (spawnRequest.delayUntilRespawn == -1) {
                spawnRequest.delayUntilSpawn = 0;
				this.configureSpawnRequest(spawnRequest);
            } else {
                spawnRequest.unlink();
            }
        }

	}

	private void clearTopInterfaces() {
		this.stream.putOpcode(130);
		if (this.inventoryOverlayInterfaceID != -1) {
			this.inventoryOverlayInterfaceID = -1;
			this.redrawTab = true;
			this.continuedDialogue = false;
			this.drawTabIcons = true;
		}
		if (this.chatboxInterfaceId != -1) {
			this.chatboxInterfaceId = -1;
			this.redrawChatbox = true;
			this.continuedDialogue = false;
		}
		this.openInterfaceId = -1;
	}

	private boolean clickInteractiveObject(final int hash, final int y, final int x) {
		final int objectId = hash >> 14 & 0x7FFF;
		final int config = this.worldController.getConfig(hash, x, y, this.plane);
		if (config == -1) {
            return false;
        }
		final int type = config & 0x1F;
		final int rotation = config >> 6 & 3;
		if (type == 10 || type == 11 || type == 22) {
			final GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
			final int sizeX;
			final int sizeY;
			if (rotation == 0 || rotation == 2) {
				sizeX = object.sizeX;
				sizeY = object.sizeY;
			} else {
				sizeX = object.sizeY;
				sizeY = object.sizeX;
			}
			int surroundings = object.face;
			if (rotation != 0) {
                surroundings = (surroundings << rotation & 0xf) + (surroundings >> 4 - rotation);
            }
			this.doWalkTo(2, 0, sizeY, 0, localPlayer.waypointY[0], sizeX, surroundings, y, localPlayer.waypointX[0], false,
					x);
		} else {
			this.doWalkTo(2, rotation, 0, type + 1, localPlayer.waypointY[0], 0, 0, y, localPlayer.waypointX[0], false, x);
		}
		this.crossX = super.clickX;
		this.crossY = super.clickY;
		this.crossType = 2;
		this.crossIndex = 0;
		return true;
	}

	private void configureSpawnRequest(final GameObjectSpawnRequest spawnRequest) {
		int uid = 0;
		int id = -1;
		int type = 0;
		int face = 0;
		if (spawnRequest.objectType == 0) {
            uid = this.worldController.getWallObjectHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
        }
		if (spawnRequest.objectType == 1) {
            uid = this.worldController.getWallDecorationHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
        }
		if (spawnRequest.objectType == 2) {
            uid = this.worldController.getInteractibleObjectHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
        }
		if (spawnRequest.objectType == 3) {
            uid = this.worldController.getGroundDecorationHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
        }
		if (uid != 0) {
			final int config = this.worldController.getConfig(uid, spawnRequest.x, spawnRequest.y, spawnRequest.z);
			id = uid >> 14 & 0x7FFF;
			type = config & 0x1F;
			face = config >> 6;
		}
		spawnRequest.id = id;
		spawnRequest.type = type;
		spawnRequest.face = face;
	}

	private void connectServer() {
		/*
		 * int j = 5; expectedCRCs[8] = 0; int k = 0; while(expectedCRCs[8] == 0) {
		 * String s = "Unknown problem"; drawLoadingText(20, (byte)4,
		 * "Connecting to web server"); try { DataInputStream datainputstream =
		 * openJagGrabInputStream("crc" + (int)(Math.random() * 99999999D) + "-" + 317);
		 * Stream class30_sub2_sub2 = new Stream(new byte[40], 891);
		 * datainputstream.readFully(class30_sub2_sub2.buffer, 0, 40);
		 * datainputstream.close(); for(int i1 = 0; i1 < 9; i1++) expectedCRCs[i1] =
		 * class30_sub2_sub2.readDWord();
		 *
		 * int j1 = class30_sub2_sub2.readDWord(); int k1 = 1234; for(int l1 = 0; l1 <
		 * 9; l1++) k1 = (k1 << 1) + expectedCRCs[l1];
		 *
		 * if(j1 != k1) { s = "checksum problem"; expectedCRCs[8] = 0; } }
		 * catch(EOFException _ex) { s = "EOF problem"; expectedCRCs[8] = 0; }
		 * catch(IOException _ex) { s = "connection problem"; expectedCRCs[8] = 0; }
		 * catch(Exception _ex) { s = "logic problem"; expectedCRCs[8] = 0;
		 * if(!signlink.reporterror) return; } if(expectedCRCs[8] == 0) { k++; for(int l
		 * = j; l > 0; l--) { if(k >= 10) { drawLoadingText(10, (byte)4,
		 * "Game updated - please reload page"); l = 10; } else { drawLoadingText(10,
		 * (byte)4, s + " - Will retry in " + l + " secs."); } try {
		 * Thread.sleep(1000L); } catch(Exception _ex) { } }
		 *
		 * j *= 2; if(j > 60) j = 60; aBoolean872 = !aBoolean872; } }
		 */
	}

	private void createObjectSpawnRequest(final int delayUntilRespawn, final int id2, final int face2, final int type, final int y, final int type2, final int z,
										  final int x, final int delayUntilSpawn) {
		GameObjectSpawnRequest request = null;
		for (GameObjectSpawnRequest request2 = (GameObjectSpawnRequest) this.spawnObjectList
				.peekFront(); request2 != null; request2 = (GameObjectSpawnRequest) this.spawnObjectList.getPrevious()) {
			if (request2.z != z || request2.x != x || request2.y != y || request2.objectType != type) {
                continue;
            }

			request = request2;
			break;
		}

		if (request == null) {
			request = new GameObjectSpawnRequest();
			request.z = z;
			request.objectType = type;
			request.x = x;
			request.y = y;
			this.configureSpawnRequest(request);
			this.spawnObjectList.pushBack(request);
		}
		request.id2 = id2;
		request.type2 = type2;
		request.face2 = face2;
		request.delayUntilSpawn = delayUntilSpawn;
		request.delayUntilRespawn = delayUntilRespawn;
	}

	private void cycleEntitySpokenText() {
		for (int p = -1; p < this.localPlayerCount; p++) {
			final int pId;
			if (p == -1) {
                pId = this.LOCAL_PLAYER_ID;
            } else {
                pId = this.localPlayers[p];
            }
			final Player player = this.players[pId];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0) {
                    player.overheadTextMessage = null;
                }
			}
		}

		for (int n = 0; n < this.npcCount; n++) {
			final int nId = this.npcIds[n];
			final NPC npc = this.npcs[nId];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0) {
                    npc.overheadTextMessage = null;
                }
			}
		}
	}

	private void deleteFriend(final long friend) {
		try {
			if (friend == 0L) {
                return;
            }
			for (int f = 0; f < this.friendsCount; f++) {
				if (this.friendsListAsLongs[f] != friend) {
                    continue;
                }
				this.friendsCount--;
				this.redrawTab = true;
				for (int _f = f; _f < this.friendsCount; _f++) {
					this.friendsList[_f] = this.friendsList[_f + 1];
					this.friendsWorldIds[_f] = this.friendsWorldIds[_f + 1];
					this.friendsListAsLongs[_f] = this.friendsListAsLongs[_f + 1];
				}

				this.stream.putOpcode(215);
				this.stream.putLong(friend);
				break;
			}
		} catch (final RuntimeException runtimeexception) {
			signlink.reporterror("18622, " + false + ", " + friend + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private void deleteIgnore(final long target) {
		try {
			if (target == 0L) {
                return;
            }
			for (int i = 0; i < this.ignoreCount; i++) {
                if (this.ignoreListAsLongs[i] == target) {
					this.ignoreCount--;
					this.redrawTab = true;
                    System.arraycopy(this.ignoreListAsLongs, i + 1, this.ignoreListAsLongs, i, this.ignoreCount - i);

					this.stream.putOpcode(74);
					this.stream.putLong(target);
                    return;
                }
            }

			return;
		} catch (final RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + target + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void despawnGameObject(final int y, final int z, final int face, final int l, final int x, final int objectType, final int objectId) {
		if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
			if (lowMemory && z != this.plane) {
                return;
            }
			int hash = 0;
			if (objectType == 0) {
                hash = this.worldController.getWallObjectHash(x, y, z);
            }
			if (objectType == 1) {
                hash = this.worldController.getWallDecorationHash(x, y, z);
            }
			if (objectType == 2) {
                hash = this.worldController.getInteractibleObjectHash(x, y, z);
            }
			if (objectType == 3) {
                hash = this.worldController.getGroundDecorationHash(x, y, z);
            }
			if (hash != 0) {
				final int config = this.worldController.getConfig(hash, x, y, z);
				final int _objectId = hash >> 14 & 0x7FFF;
				final int position = config & 0x1F;
				final int orientation = config >> 6;
				if (objectType == 0) {
					this.worldController.removeWallObject(x, z, y);
					final GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (object.solid) {
						this.currentCollisionMap[z].unmarkWall(x, y, position, orientation, object.walkable);
                    }
				}
				if (objectType == 1) {
					this.worldController.removeWallDecoration(x, y, z);
                }
				if (objectType == 2) {
					this.worldController.removeInteractiveObject(x, y, z);
					final GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (x + object.sizeX > 103 || y + object.sizeX > 103 || x + object.sizeY > 103
							|| y + object.sizeY > 103) {
                        return;
                    }
					if (object.solid) {
						this.currentCollisionMap[z].unmarkSolidOccupant(x, y, object.sizeX, object.sizeY, orientation,
                                object.walkable);
                    }
				}
				if (objectType == 3) {
					this.worldController.removeGroundDecoration(x, y, z);
					final GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (object.solid && object.hasActions) {
						this.currentCollisionMap[z].unmarkConcealed(x, y);
                    }
				}
			}
			if (objectId >= 0) {
				int height = z;
				if (height < 3 && (this.tileFlags[1][x][y] & 2) == 2) {
                    height++;
                }
				Region.forceRenderObject(this.worldController, face, y, l, height, this.currentCollisionMap[z], this.intGroundArray, x,
						objectId, z);
			}
		}
	}

	private void doAction(final int row) {
		if (row < 0) {
            return;
        }
		if (this.inputDialogState != 0) {
			this.inputDialogState = 0;
			this.redrawChatbox = true;
		}
		final int actionInformation2 = this.menuActionData2[row];
		final int actionInformation1 = this.menuActionData3[row];
		int menuAction = this.menuActionId[row];
		final int actionTarget = this.menuActionData1[row];
		if (menuAction >= 2000) {
            menuAction -= 2000;
        }
		if (menuAction == 582) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(57);
				this.stream.putShortA(this.useItemId);
				this.stream.putShortA(actionTarget);
				this.stream.putLEShort(this.lastItemSelectedSlot);
				this.stream.putShortA(this.lastItemSelectedInterface);
			}
		}
		if (menuAction == 234) {
			boolean flag1 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag1) {
                flag1 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(236);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putShort(actionTarget);
			this.stream.putLEShort(actionInformation2 + this.baseX);
		}
		if (menuAction == 62 && this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2)) {
			this.stream.putOpcode(192);
			this.stream.putShort(this.lastItemSelectedInterface);
			this.stream.putLEShort(actionTarget >> 14 & 0x7FFF);
			this.stream.putLEShortA(actionInformation1 + this.baseY);
			this.stream.putLEShort(this.lastItemSelectedSlot);
			this.stream.putLEShortA(actionInformation2 + this.baseX);
			this.stream.putShort(this.useItemId);
		}
		if (menuAction == 511) {
			boolean flag2 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag2) {
                flag2 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(25);
			this.stream.putLEShort(this.lastItemSelectedInterface);
			this.stream.putShortA(this.useItemId);
			this.stream.putShort(actionTarget);
			this.stream.putShortA(actionInformation1 + this.baseY);
			this.stream.putLEShortA(this.lastItemSelectedSlot);
			this.stream.putShort(actionInformation2 + this.baseX);
		}
		if (menuAction == 74) {
			this.stream.putOpcode(122);
			this.stream.putLEShortA(actionInformation1);
			this.stream.putShortA(actionInformation2);
			this.stream.putLEShort(actionTarget);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 315) {
			final RSInterface rsInterface = RSInterface.cache[actionInformation1];
			boolean flag8 = true;
			if (rsInterface.contentType > 0) {
                flag8 = this.promptUserForInput(rsInterface);
            }
			if (flag8) {
				this.stream.putOpcode(185);
				this.stream.putShort(actionInformation1);
			}
		}
		if (menuAction == 561) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(128);
				this.stream.putShort(actionTarget);
			}
		}
		if (menuAction == 20) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(155);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 779) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(153);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 516) {
            if (!this.menuOpen) {
				this.worldController.request2DTrace(super.clickX - 4, super.clickY - 4);
            } else {
				this.worldController.request2DTrace(actionInformation2 - 4, actionInformation1 - 4);
            }
        }
		if (menuAction == 1062) {
			this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			this.stream.putOpcode(228);
			this.stream.putShortA(actionTarget >> 14 & 0x7FFF);
			this.stream.putShortA(actionInformation1 + this.baseY);
			this.stream.putShort(actionInformation2 + this.baseX);
		}
		if (menuAction == 679 && !this.continuedDialogue) {
			this.stream.putOpcode(40);
			this.stream.putShort(actionInformation1);
			this.continuedDialogue = true;
		}
		if (menuAction == 431) {
			this.stream.putOpcode(129);
			this.stream.putShortA(actionInformation2);
			this.stream.putShort(actionInformation1);
			this.stream.putShortA(actionTarget);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 337 || menuAction == 42 || menuAction == 792 || menuAction == 322) {
			final String name = this.menuActionName[row];
			final int position = name.indexOf("@whi@");
			if (position != -1) {
				final long targetAsLong = TextClass.nameToLong(name.substring(position + 5).trim());
				if (menuAction == 337) {
					this.addFriend(targetAsLong);
                }
				if (menuAction == 42) {
					this.addIgnore(targetAsLong);
                }
				if (menuAction == 792) {
					this.deleteFriend(targetAsLong);
                }
				if (menuAction == 322) {
					this.deleteIgnore(targetAsLong);
                }
			}
		}
		if (menuAction == 53) {
			this.stream.putOpcode(135);
			this.stream.putLEShort(actionInformation2);
			this.stream.putShortA(actionInformation1);
			this.stream.putLEShort(actionTarget);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 539) {
			this.stream.putOpcode(16);
			this.stream.putShortA(actionTarget);
			this.stream.putLEShortA(actionInformation2);
			this.stream.putLEShortA(actionInformation1);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 484 || menuAction == 6) {
			String name = this.menuActionName[row];
			final int position = name.indexOf("@whi@");
			if (position != -1) {
				name = name.substring(position + 5).trim();
				final String nameAsLong = TextClass.formatName(TextClass.longToName(TextClass.nameToLong(name)));
				boolean foundPlayer = false;
				for (int p = 0; p < this.localPlayerCount; p++) {
					final Player player = this.players[this.localPlayers[p]];
					if (player == null || player.name == null || !player.name.equalsIgnoreCase(nameAsLong)) {
                        continue;
                    }
					this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
							false, player.waypointX[0]);
					if (menuAction == 484) {
						// Follow another player

						this.stream.putOpcode(139);
						this.stream.putLEShort(this.localPlayers[p]);
					}
					if (menuAction == 6) {
						this.stream.putOpcode(128);
						this.stream.putShort(this.localPlayers[p]);
					}
					foundPlayer = true;
					break;
				}

				if (!foundPlayer) {
					this.pushMessage("Unable to find " + nameAsLong, 0, "");
                }
			}
		}
		if (menuAction == 870) {
			this.stream.putOpcode(53);
			this.stream.putShort(actionInformation2);
			this.stream.putShortA(this.lastItemSelectedSlot);
			this.stream.putLEShortA(actionTarget);
			this.stream.putShort(this.lastItemSelectedInterface);
			this.stream.putLEShort(this.useItemId);
			this.stream.putShort(actionInformation1);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 847) {
			this.stream.putOpcode(87);
			this.stream.putShortA(actionTarget);
			this.stream.putShort(actionInformation1);
			this.stream.putShortA(actionInformation2);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 626) {
			final RSInterface rsInterface = RSInterface.cache[actionInformation1];
			this.spellSelected = true;
			this.selectedSpellId = actionInformation1;
			this.spellUsableOn = rsInterface.spellUsableOn;
			this.itemSelected = false;
			this.redrawTab = true;
			String namePartOne = rsInterface.selectedActionName;
			if (namePartOne.contains(" ")) {
                namePartOne = namePartOne.substring(0, namePartOne.indexOf(" "));
            }
			String namePartTwo = rsInterface.selectedActionName;
			if (namePartTwo.contains(" ")) {
                namePartTwo = namePartTwo.substring(namePartTwo.indexOf(" ") + 1);
            }
			this.spellTooltip = namePartOne + " " + rsInterface.spellName + " " + namePartTwo;
			if (this.spellUsableOn == 16) {
				this.redrawTab = true;
				this.currentTabId = 3;
				this.drawTabIcons = true;
			}
			return;
		}
		if (menuAction == 78) {
			this.stream.putOpcode(117);
			this.stream.putLEShortA(actionInformation1);
			this.stream.putLEShortA(actionTarget);
			this.stream.putLEShort(actionInformation2);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 27) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(73);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 213) {
			boolean flag3 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag3) {
                flag3 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(79);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putShort(actionTarget);
			this.stream.putShortA(actionInformation2 + this.baseX);
		}
		if (menuAction == 632) {
			this.stream.putOpcode(145);
			this.stream.putShortA(actionInformation1);
			this.stream.putShortA(actionInformation2);
			this.stream.putShortA(actionTarget);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 493) {
			this.stream.putOpcode(75);
			this.stream.putLEShortA(actionInformation1);
			this.stream.putLEShort(actionInformation2);
			this.stream.putShortA(actionTarget);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 652) {
			boolean flag4 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag4) {
                flag4 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(156);
			this.stream.putShortA(actionInformation2 + this.baseX);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putLEShortA(actionTarget);
		}
		if (menuAction == 94) {
			boolean flag5 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag5) {
                flag5 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(181);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putShort(actionTarget);
			this.stream.putLEShort(actionInformation2 + this.baseX);
			this.stream.putShortA(this.selectedSpellId);
		}
		if (menuAction == 646) {
			this.stream.putOpcode(185);
			this.stream.putShort(actionInformation1);
			final RSInterface rsInterface = RSInterface.cache[actionInformation1];
			if (rsInterface.opcodes != null && rsInterface.opcodes[0][0] == 5) {
				final int setting = rsInterface.opcodes[0][1];
				if (this.interfaceSettings[setting] != rsInterface.conditionValue[0]) {
					this.interfaceSettings[setting] = rsInterface.conditionValue[0];
					this.handleInterfaceSetting(setting);
					this.redrawTab = true;
				}
			}
		}
		if (menuAction == 225) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(17);
				this.stream.putLEShortA(actionTarget);
			}
		}
		if (menuAction == 965) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(21);
				this.stream.putShort(actionTarget);
			}
		}
		if (menuAction == 413) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(131);
				this.stream.putLEShortA(actionTarget);
				this.stream.putShortA(this.selectedSpellId);
			}
		}
		if (menuAction == 200) {
			this.clearTopInterfaces();
        }
		if (menuAction == 1025) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				EntityDefinition entityDef = npc.npcDefinition;
				if (entityDef.childrenIDs != null) {
                    entityDef = entityDef.getChildDefinition();
                }
				if (entityDef != null) {
					final String description;
					if (entityDef.description != null) {
                        description = new String(entityDef.description);
                    } else {
                        description = "It's a " + entityDef.name + ".";
                    }
					this.pushMessage(description, 0, "");
				}
			}
		}
		if (menuAction == 900) {
			this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			this.stream.putOpcode(252);
			this.stream.putLEShortA(actionTarget >> 14 & 0x7FFF);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putShortA(actionInformation2 + this.baseX);
		}
		if (menuAction == 412) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(72);
				this.stream.putShortA(actionTarget);
			}
		}
		if (menuAction == 365) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(249);
				this.stream.putShortA(actionTarget);
				this.stream.putLEShort(this.selectedSpellId);
			}
		}
		if (menuAction == 729) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(39);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 577) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(139);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 956 && this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2)) {
			this.stream.putOpcode(35);
			this.stream.putLEShort(actionInformation2 + this.baseX);
			this.stream.putShortA(this.selectedSpellId);
			this.stream.putShortA(actionInformation1 + this.baseY);
			this.stream.putLEShort(actionTarget >> 14 & 0x7FFF);
		}
		if (menuAction == 567) {
			boolean flag6 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag6) {
                flag6 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(23);
			this.stream.putLEShort(actionInformation1 + this.baseY);
			this.stream.putLEShort(actionTarget);
			this.stream.putLEShort(actionInformation2 + this.baseX);
		}
		if (menuAction == 867) {
			this.stream.putOpcode(43);
			this.stream.putLEShort(actionInformation1);
			this.stream.putShortA(actionTarget);
			this.stream.putShortA(actionInformation2);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 543) {
			this.stream.putOpcode(237);
			this.stream.putShort(actionInformation2);
			this.stream.putShortA(actionTarget);
			this.stream.putShort(actionInformation1);
			this.stream.putShortA(this.selectedSpellId);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 606) {
			final String name = this.menuActionName[row];
			final int position = name.indexOf("@whi@");
			if (position != -1) {
                if (this.openInterfaceId == -1) {
					this.clearTopInterfaces();
					this.reportAbuseInput = name.substring(position + 5).trim();
					this.reportAbuseMute = false;
                    for (int rsInterface = 0; rsInterface < RSInterface.cache.length; rsInterface++) {
                        if (RSInterface.cache[rsInterface] == null || RSInterface.cache[rsInterface].contentType != 600) {
                            continue;
                        }
						this.reportAbuseInterfaceID = this.openInterfaceId = RSInterface.cache[rsInterface].parentID;
                        break;
                    }

                } else {
					this.pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
                }
            }
		}
		if (menuAction == 491) {
			final Player player = this.players[actionTarget];
			if (player != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(14);
				this.stream.putShortA(this.lastItemSelectedInterface);
				this.stream.putShort(actionTarget);
				this.stream.putShort(this.useItemId);
				this.stream.putLEShort(this.lastItemSelectedSlot);
			}
		}
		if (menuAction == 639) {
			final String name = this.menuActionName[row];
			final int position = name.indexOf("@whi@");
			if (position != -1) {
				final long nameAsLong = TextClass.nameToLong(name.substring(position + 5).trim());
				int target = -1;
				for (int friend = 0; friend < this.friendsCount; friend++) {
					if (this.friendsListAsLongs[friend] != nameAsLong) {
                        continue;
                    }
					target = friend;
					break;
				}

				if (target != -1 && this.friendsWorldIds[target] > 0) {
					this.redrawChatbox = true;
					this.inputDialogState = 0;
					this.messagePromptRaised = true;
					this.promptInput = "";
					this.friendsListAction = 3;
					this.privateMessageTarget = this.friendsListAsLongs[target];
					this.chatboxInputNeededString = "Enter message to send to " + this.friendsList[target];
				}
			}
		}
		if (menuAction == 454) {
			this.stream.putOpcode(41);
			this.stream.putShort(actionTarget);
			this.stream.putShortA(actionInformation2);
			this.stream.putShortA(actionInformation1);
			this.atInventoryLoopCycle = 0;
			this.atInventoryInterface = actionInformation1;
			this.atInventoryIndex = actionInformation2;
			this.atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == this.openInterfaceId) {
				this.atInventoryInterfaceType = 1;
            }
			if (RSInterface.cache[actionInformation1].parentID == this.chatboxInterfaceId) {
				this.atInventoryInterfaceType = 3;
            }
		}
		if (menuAction == 478) {
			final NPC npc = this.npcs[actionTarget];
			if (npc != null) {
				this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 2;
				this.crossIndex = 0;
				this.stream.putOpcode(18);
				this.stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 113) {
			this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			this.stream.putOpcode(70);
			this.stream.putLEShort(actionInformation2 + this.baseX);
			this.stream.putShort(actionInformation1 + this.baseY);
			this.stream.putLEShortA(actionTarget >> 14 & 0x7FFF);
		}
		if (menuAction == 872) {
			this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			this.stream.putOpcode(234);
			this.stream.putLEShortA(actionInformation2 + this.baseX);
			this.stream.putShortA(actionTarget >> 14 & 0x7FFF);
			this.stream.putLEShortA(actionInformation1 + this.baseY);
		}
		if (menuAction == 502) {
			this.clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			this.stream.putOpcode(132);
			this.stream.putLEShortA(actionInformation2 + this.baseX);
			this.stream.putShort(actionTarget >> 14 & 0x7FFF);
			this.stream.putShortA(actionInformation1 + this.baseY);
		}
		if (menuAction == 1125) {
			final ItemDefinition item = ItemDefinition.getDefinition(actionTarget);
			final RSInterface rsInterface = RSInterface.cache[actionInformation1];
			final String description;
			if (rsInterface != null && rsInterface.inventoryStackSize[actionInformation2] >= 0x186a0) {
                description = rsInterface.inventoryStackSize[actionInformation2] + " x " + item.name;
            } else if (item.description != null) {
                description = new String(item.description);
            } else {
                description = "It's a " + item.name + ".";
            }
			this.pushMessage(description, 0, "");
		}
		if (menuAction == 169) {
			this.stream.putOpcode(185);
			this.stream.putShort(actionInformation1);
			final RSInterface rsInterface = RSInterface.cache[actionInformation1];
			if (rsInterface.opcodes != null && rsInterface.opcodes[0][0] == 5) {
				final int setting = rsInterface.opcodes[0][1];
				this.interfaceSettings[setting] = 1 - this.interfaceSettings[setting];
				this.handleInterfaceSetting(setting);
				this.redrawTab = true;
			}
		}
		if (menuAction == 447) {
			this.itemSelected = true;
			this.lastItemSelectedSlot = actionInformation2;
			this.lastItemSelectedInterface = actionInformation1;
			this.useItemId = actionTarget;
			this.selectedItemName = ItemDefinition.getDefinition(actionTarget).name;
			this.spellSelected = false;
			this.redrawTab = true;
			return;
		}
		if (menuAction == 1226) {
			final int objectId = actionTarget >> 14 & 0x7FFF;
			final GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
			final String description;
			if (object.description != null) {
                description = new String(object.description);
            } else {
                description = "It's a " + object.name + ".";
            }
			this.pushMessage(description, 0, "");
		}
		if (menuAction == 244) {
			boolean flag7 = this.doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag7) {
                flag7 = this.doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
                        localPlayer.waypointX[0], false, actionInformation2);
            }
			this.crossX = super.clickX;
			this.crossY = super.clickY;
			this.crossType = 2;
			this.crossIndex = 0;
			this.stream.putOpcode(253);
			this.stream.putLEShort(actionInformation2 + this.baseX);
			this.stream.putLEShortA(actionInformation1 + this.baseY);
			this.stream.putShortA(actionTarget);
		}
		if (menuAction == 1448) {
			final ItemDefinition item = ItemDefinition.getDefinition(actionTarget);
			final String description;
			if (item.description != null) {
                description = new String(item.description);
            } else {
                description = "It's a " + item.name + ".";
            }
			this.pushMessage(description, 0, "");
		}
		this.itemSelected = false;
		this.spellSelected = false;
		this.redrawTab = true;
	}

	private boolean doWalkTo(final int clickType, final int objectRotation, final int objectSizeY, final int objectType, final int startY,
							 final int objectSizeX, final int targetSurroundings, final int endY, final int startX, final boolean flag, final int endX) {
		final byte mapSizeX = 104;
		final byte mapSizeY = 104;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				this.wayPoints[x][y] = 0;
				this.distanceValues[x][y] = 0x5F5E0FF;
			}
		}

		int currentX = startX;
		int currentY = startY;
		this.wayPoints[startX][startY] = 99;
		this.distanceValues[startX][startY] = 0;
		int nextIndex = 0;
		int currentIndex = 0;
		this.walkingQueueX[nextIndex] = startX;
		this.walkingQueueY[nextIndex++] = startY;
		boolean foundDestination = false;
		int maxPathSize = this.walkingQueueX.length;
		final int[][] clippingPaths = this.currentCollisionMap[this.plane].clippingData;
		while (currentIndex != nextIndex) {
			currentX = this.walkingQueueX[currentIndex];
			currentY = this.walkingQueueY[currentIndex];
			currentIndex = (currentIndex + 1) % maxPathSize;
			if (currentX == endX && currentY == endY) {
				foundDestination = true;
				break;
			}
			if (objectType != 0) {
				if ((objectType < 5 || objectType == 10) && this.currentCollisionMap[this.plane].reachedWall(currentX, currentY,
						endX, endY, objectType - 1, objectRotation)) {
					foundDestination = true;
					break;
				}
				if (objectType < 10 && this.currentCollisionMap[this.plane].reachedWallDecoration(currentX, currentY, endX, endY,
						objectType - 1, objectRotation)) {
					foundDestination = true;
					break;
				}
			}
			if (objectSizeX != 0 && objectSizeY != 0 && this.currentCollisionMap[this.plane].reachedFacingObject(currentX,
					currentY, endX, endY, objectSizeX, objectSizeY, targetSurroundings)) {
				foundDestination = true;
				break;
			}
			final int newDistanceValue = this.distanceValues[currentX][currentY] + 1;
			if (currentX > 0 && this.wayPoints[currentX - 1][currentY] == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0) {
				this.walkingQueueX[nextIndex] = currentX - 1;
				this.walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX - 1][currentY] = 2;
				this.distanceValues[currentX - 1][currentY] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && this.wayPoints[currentX + 1][currentY] == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0) {
				this.walkingQueueX[nextIndex] = currentX + 1;
				this.walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX + 1][currentY] = 8;
				this.distanceValues[currentX + 1][currentY] = newDistanceValue;
			}
			if (currentY > 0 && this.wayPoints[currentX][currentY - 1] == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				this.walkingQueueX[nextIndex] = currentX;
				this.walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX][currentY - 1] = 1;
				this.distanceValues[currentX][currentY - 1] = newDistanceValue;
			}
			if (currentY < mapSizeY - 1 && this.wayPoints[currentX][currentY + 1] == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				this.walkingQueueX[nextIndex] = currentX;
				this.walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX][currentY + 1] = 4;
				this.distanceValues[currentX][currentY + 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY > 0 && this.wayPoints[currentX - 1][currentY - 1] == 0
					&& (clippingPaths[currentX - 1][currentY - 1] & 0x128010e) == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				this.walkingQueueX[nextIndex] = currentX - 1;
				this.walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX - 1][currentY - 1] = 3;
				this.distanceValues[currentX - 1][currentY - 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY > 0 && this.wayPoints[currentX + 1][currentY - 1] == 0
					&& (clippingPaths[currentX + 1][currentY - 1] & 0x1280183) == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				this.walkingQueueX[nextIndex] = currentX + 1;
				this.walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX + 1][currentY - 1] = 9;
				this.distanceValues[currentX + 1][currentY - 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY < mapSizeY - 1 && this.wayPoints[currentX - 1][currentY + 1] == 0
					&& (clippingPaths[currentX - 1][currentY + 1] & 0x1280138) == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				this.walkingQueueX[nextIndex] = currentX - 1;
				this.walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX - 1][currentY + 1] = 6;
				this.distanceValues[currentX - 1][currentY + 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY < mapSizeY - 1 && this.wayPoints[currentX + 1][currentY + 1] == 0
					&& (clippingPaths[currentX + 1][currentY + 1] & 0x12801e0) == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				this.walkingQueueX[nextIndex] = currentX + 1;
				this.walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				this.wayPoints[currentX + 1][currentY + 1] = 12;
				this.distanceValues[currentX + 1][currentY + 1] = newDistanceValue;
			}
		}
		this.arbitraryDestination = 0;
		if (!foundDestination) {
			if (flag) {
				int maxStepsNonInclusive = 100;
				for (int deviation = 1; deviation < 2; deviation++) {
					for (int deviationX = endX - deviation; deviationX <= endX + deviation; deviationX++) {
						for (int deviationY = endY - deviation; deviationY <= endY + deviation; deviationY++) {
                            if (deviationX >= 0 && deviationY >= 0 && deviationX < 104 && deviationY < 104
                                    && this.distanceValues[deviationX][deviationY] < maxStepsNonInclusive) {
                                maxStepsNonInclusive = this.distanceValues[deviationX][deviationY];
                                currentX = deviationX;
                                currentY = deviationY;
								this.arbitraryDestination = 1;
                                foundDestination = true;
                            }
                        }

					}

					if (foundDestination) {
                        break;
                    }
				}

			}
			if (!foundDestination) {
                return false;
            }
		}
		currentIndex = 0;
		this.walkingQueueX[currentIndex] = currentX;
		this.walkingQueueY[currentIndex++] = currentY;
		int initialSkipCheck;
		for (int waypoint = initialSkipCheck = this.wayPoints[currentX][currentY]; currentX != startX
				|| currentY != startY; waypoint = this.wayPoints[currentX][currentY]) {
			if (waypoint != initialSkipCheck) {
				initialSkipCheck = waypoint;
				this.walkingQueueX[currentIndex] = currentX;
				this.walkingQueueY[currentIndex++] = currentY;
			}
			if ((waypoint & 2) != 0) {
                currentX++;
            } else if ((waypoint & 8) != 0) {
                currentX--;
            }
			if ((waypoint & 1) != 0) {
                currentY++;
            } else if ((waypoint & 4) != 0) {
                currentY--;
            }
		}

		if (currentIndex > 0) {
			maxPathSize = currentIndex;
			if (maxPathSize > 25) {
                maxPathSize = 25;
            }
			currentIndex--;
			final int x = this.walkingQueueX[currentIndex];
			final int y = this.walkingQueueY[currentIndex];
			currentWalkingQueueSize += maxPathSize;
			if (currentWalkingQueueSize >= 92) {
				this.stream.putOpcode(36);
				this.stream.putInt(0);
				currentWalkingQueueSize = 0;
			}
			if (clickType == 0) {
				this.stream.putOpcode(164);
				this.stream.put(maxPathSize + maxPathSize + 3);
			}
			if (clickType == 1) {
				this.stream.putOpcode(248);
				this.stream.put(maxPathSize + maxPathSize + 3 + 14);
			}
			if (clickType == 2) {
				this.stream.putOpcode(98);
				this.stream.put(maxPathSize + maxPathSize + 3);
			}
			this.stream.putLEShortA(x + this.baseX);
			this.destinationX = this.walkingQueueX[0];
			this.destinationY = this.walkingQueueY[0];
			for (int counter = 1; counter < maxPathSize; counter++) {
				currentIndex--;
				this.stream.put(this.walkingQueueX[currentIndex] - x);
				this.stream.put(this.walkingQueueY[currentIndex] - y);
			}

			this.stream.putLEShort(y + this.baseY);
			this.stream.putByteC(super.keyStatus[5] != 1 ? 0 : 1);
			return true;
		}
		return clickType != 1;
	}

	private void draw3dScreen() {
		this.drawSplitPrivateChat();
		if (this.crossType == 1) {
			this.crosses[this.crossIndex / 100].drawImage(this.crossX - 8 - 4, this.crossY - 8 - 4);
		}
		if (this.crossType == 2) {
			this.crosses[4 + this.crossIndex / 100].drawImage(this.crossX - 8 - 4, this.crossY - 8 - 4);
        }
		if (this.walkableInterfaceId != -1) {
			this.animateInterface(this.animationTimePassed, this.walkableInterfaceId);
			this.drawInterface(0, 0, RSInterface.cache[this.walkableInterfaceId], 0);
		}
		if (this.openInterfaceId != -1) {
			this.animateInterface(this.animationTimePassed, this.openInterfaceId);
			this.drawInterface(0, 0, RSInterface.cache[this.openInterfaceId], 0);
		}
		this.checkTutorialIsland();
		if (!this.menuOpen) {
			this.processRightClick();
			this.drawTooltip();
		} else if (this.menuScreenArea == 0) {
			this.drawMenu();
        }
		if (this.multiCombatZone) {
			this.headIcons[1].drawImage(472, 296);
        }
		if (displayFpsAndMemory) {
			final int x = 507;
			int y = 20;
			int colour = 0xFFFF00;
			if (super.fps < 15) {
                colour = 0xFF0000;
            }
			this.fontPlain.drawTextLeft("Fps:" + super.fps, x, y, colour);
			y += 15;
			final Runtime runtime = Runtime.getRuntime();
			final int memory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			colour = 0xFFFF00;
			if (memory > 0x2000000 && lowMemory) {
                colour = 0xFF0000;
            }
			this.fontPlain.drawTextLeft("Mem:" + memory + "k", x, y, 0xFFFF00);
			y += 15;
		}
		if (this.systemUpdateTime != 0) {
			int seconds = this.systemUpdateTime / 50;
			final int minutes = seconds / 60;
			seconds %= 60;
			if (seconds < 10) {
				this.fontPlain.drawText("System update in: " + minutes + ":0" + seconds, 4, 329, 0xFFFF00);
            } else {
				this.fontPlain.drawText("System update in: " + minutes + ":" + seconds, 4, 329, 0xFFFF00);
            }
		}
	}

	private void drawChatArea() {
		this.chatboxImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = this.chatboxLineOffsets;
		this.chatBackgroundImage.draw(0, 0);
		if (this.messagePromptRaised) {
			this.fontBold.drawCentredText(this.chatboxInputNeededString, 239, 40, 0);
			this.fontBold.drawCentredText(this.promptInput + "*", 239, 60, 128);
		} else if (this.inputDialogState == 1) {
			this.fontBold.drawCentredText("Enter amount:", 239, 40, 0);
			this.fontBold.drawCentredText(this.amountOrNameInput + "*", 239, 60, 128);
		} else if (this.inputDialogState == 2) {
			this.fontBold.drawCentredText("Enter name:", 239, 40, 0);
			this.fontBold.drawCentredText(this.amountOrNameInput + "*", 239, 60, 128);
		} else if (this.clickToContinueString != null) {
			this.fontBold.drawCentredText(this.clickToContinueString, 239, 40, 0);
			this.fontBold.drawCentredText("Click to continue", 239, 60, 128);
		} else if (this.chatboxInterfaceId != -1) {
			this.drawInterface(0, 0, RSInterface.cache[this.chatboxInterfaceId], 0);
        } else if (this.dialogID != -1) {
			this.drawInterface(0, 0, RSInterface.cache[this.dialogID], 0);
		} else {
			final GameFont textDrawingArea = this.fontPlain;
			int rowCount = 0;
			DrawingArea.setDrawingArea(77, 0, 463, 0);
			for (int m = 0; m < 100; m++) {
                if (this.chatMessages[m] != null) {
                    final int type = this.chatTypes[m];
                    final int y = (70 - rowCount * 14) + this.anInt1089;
                    String name = this.chatNames[m];
                    byte playerRights = 0;
                    if (name != null && name.startsWith("@cr1@")) {
                        name = name.substring(5);
                        playerRights = 1;
                    }
                    if (name != null && name.startsWith("@cr2@")) {
                        name = name.substring(5);
                        playerRights = 2;
                    }
                    if (type == 0) {
                        if (y > 0 && y < 110) {
                            textDrawingArea.drawText(this.chatMessages[m], 4, y, 0);
                        }
                        rowCount++;
                    }
                    if ((type == 1 || type == 2)
                            && (type == 1 || this.publicChatMode == 0 || this.publicChatMode == 1 && this.isFriendOrSelf(name))) {
                        if (y > 0 && y < 110) {
                            int x = 4;
                            if (playerRights == 1) {
								this.modIcons[0].draw(x, y - 12);
                                x += 14;
                            }
                            if (playerRights == 2) {
								this.modIcons[1].draw(x, y - 12);
                                x += 14;
                            }
                            textDrawingArea.drawText(name + ":", x, y, 0);
                            x += textDrawingArea.getTextDisplayedWidth(name) + 8;
                            textDrawingArea.drawText(this.chatMessages[m], x, y, 255);
                        }
                        rowCount++;
                    }
                    if ((type == 3 || type == 7) && this.splitPrivateChat == 0
                            && (type == 7 || this.privateChatMode == 0 || this.privateChatMode == 1 && this.isFriendOrSelf(name))) {
                        if (y > 0 && y < 110) {
                            int x = 4;
                            textDrawingArea.drawText("From", x, y, 0);
                            x += textDrawingArea.getTextDisplayedWidth("From ");
                            if (playerRights == 1) {
								this.modIcons[0].draw(x, y - 12);
                                x += 14;
                            }
                            if (playerRights == 2) {
								this.modIcons[1].draw(x, y - 12);
                                x += 14;
                            }
                            textDrawingArea.drawText(name + ":", x, y, 0);
                            x += textDrawingArea.getTextDisplayedWidth(name) + 8;
                            textDrawingArea.drawText(this.chatMessages[m], x, y, 0x800000);
                        }
                        rowCount++;
                    }
                    if (type == 4 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(name))) {
                        if (y > 0 && y < 110) {
                            textDrawingArea.drawText(name + " " + this.chatMessages[m], 4, y, 0x800080);
                        }
                        rowCount++;
                    }
                    if (type == 5 && this.splitPrivateChat == 0 && this.privateChatMode < 2) {
                        if (y > 0 && y < 110) {
                            textDrawingArea.drawText(this.chatMessages[m], 4, y, 0x800000);
                        }
                        rowCount++;
                    }
                    if (type == 6 && this.splitPrivateChat == 0 && this.privateChatMode < 2) {
                        if (y > 0 && y < 110) {
                            textDrawingArea.drawText("To " + name + ":", 4, y, 0);
                            textDrawingArea.drawText(this.chatMessages[m],
                                    12 + textDrawingArea.getTextDisplayedWidth("To " + name), y, 0x800000);
                        }
                        rowCount++;
                    }
                    if (type == 8 && (this.tradeMode == 0 || this.tradeMode == 1 && this.isFriendOrSelf(name))) {
                        if (y > 0 && y < 110) {
                            textDrawingArea.drawText(name + " " + this.chatMessages[m], 4, y, 0x7e3200);
                        }
                        rowCount++;
                    }
                }
            }

			DrawingArea.defaultDrawingAreaSize();
			this.chatboxScrollMax = rowCount * 14 + 7;
			if (this.chatboxScrollMax < 78) {
				this.chatboxScrollMax = 78;
            }
			this.renderChatInterface(463, 0, 77, this.chatboxScrollMax - this.anInt1089 - 77, this.chatboxScrollMax);
			final String name;
			if (localPlayer != null && localPlayer.name != null) {
                name = localPlayer.name;
            } else {
                name = TextClass.formatName(this.enteredUsername);
            }
			textDrawingArea.drawText(name + ":", 4, 90, 0);
			textDrawingArea.drawText(this.inputString + "*", 6 + textDrawingArea.getTextDisplayedWidth(name + ": "), 90,
					255);
			DrawingArea.drawHorizontalLine(77, 0, 479, 0);
		}
		if (this.menuOpen && this.menuScreenArea == 2) {
			this.drawMenu();
        }
		this.chatboxImageProducer.drawGraphics(357, super.gameGraphics, 17);
		this.gameScreenImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = this.viewportOffsets;
	}

	private void drawFriendsListOrWelcomeScreen(final RSInterface rsInterface) {
		int type = rsInterface.contentType;
		if (type >= 1 && type <= 100 || type >= 701 && type <= 800) {
			if (type == 1 && this.friendListStatus == 0) {
				rsInterface.textDefault = "Loading friend list";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 1 && this.friendListStatus == 1) {
				rsInterface.textDefault = "Connecting to friendserver";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 2 && this.friendListStatus != 2) {
				rsInterface.textDefault = "Please wait...";
				rsInterface.actionType = 0;
				return;
			}
			int f = this.friendsCount;
			if (this.friendListStatus != 2) {
                f = 0;
            }
			if (type > 700) {
                type -= 601;
            } else {
                type--;
            }
			if (type >= f) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			} else {
				rsInterface.textDefault = this.friendsList[type];
				rsInterface.actionType = 1;
				return;
			}
		}
		if (type >= 101 && type <= 200 || type >= 801 && type <= 900) {
			int f = this.friendsCount;
			if (this.friendListStatus != 2) {
                f = 0;
            }
			if (type > 800) {
                type -= 701;
            } else {
                type -= 101;
            }
			if (type >= f) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			}
			if (this.friendsWorldIds[type] == 0) {
                rsInterface.textDefault = "@red@Offline";
            } else if (this.friendsWorldIds[type] == localWorldId) {
                rsInterface.textDefault = "@gre@World-" + (this.friendsWorldIds[type] - 9);
            } else {
                rsInterface.textDefault = "@yel@World-" + (this.friendsWorldIds[type] - 9);
            }
			rsInterface.actionType = 1;
			return;
		}
		if (type == 203) {
			int f = this.friendsCount;
			if (this.friendListStatus != 2) {
                f = 0;
            }
			rsInterface.scrollMax = f * 15 + 20;
			if (rsInterface.scrollMax <= rsInterface.height) {
                rsInterface.scrollMax = rsInterface.height + 1;
            }
			return;
		}
		if (type >= 401 && type <= 500) {
			if ((type -= 401) == 0 && this.friendListStatus == 0) {
				rsInterface.textDefault = "Loading ignore list";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 1 && this.friendListStatus == 0) {
				rsInterface.textDefault = "Please wait...";
				rsInterface.actionType = 0;
				return;
			}
			int i = this.ignoreCount;
			if (this.friendListStatus == 0) {
                i = 0;
            }
			if (type >= i) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			} else {
				rsInterface.textDefault = TextClass.formatName(TextClass.longToName(this.ignoreListAsLongs[type]));
				rsInterface.actionType = 1;
				return;
			}
		}
		if (type == 503) {
			rsInterface.scrollMax = this.ignoreCount * 15 + 20;
			if (rsInterface.scrollMax <= rsInterface.height) {
                rsInterface.scrollMax = rsInterface.height + 1;
            }
			return;
		}
		if (type == 327) {
			rsInterface.modelRotationX = 150;
			rsInterface.modelRotationY = (int) (Math.sin(tick / 40D) * 256D) & 0x7FF;
			if (this.characterModelChanged) {
				for (int k = 0; k < 7; k++) {
					final int kit = this.characterEditIdentityKits[k];
					if (kit >= 0 && !IdentityKit.cache[kit].bodyModelCached()) {
                        return;
                    }
				}

				this.characterModelChanged = false;
				final Model[] bodyModels = new Model[7];
				int bodyModelCount = 0;
				for (int k = 0; k < 7; k++) {
					final int kit = this.characterEditIdentityKits[k];
					if (kit >= 0) {
                        bodyModels[bodyModelCount++] = IdentityKit.cache[kit].getBodyModel();
                    }
				}

				final Model model = new Model(bodyModelCount, bodyModels);
				for (int colour = 0; colour < 5; colour++) {
                    if (this.characterEditColours[colour] != 0) {
                        model.recolour(APPEARANCE_COLOURS[colour][0],
                                APPEARANCE_COLOURS[colour][this.characterEditColours[colour]]);
                        if (colour == 1) {
                            model.recolour(BEARD_COLOURS[0], BEARD_COLOURS[this.characterEditColours[colour]]);
                        }
                    }
                }

				model.createBones();
				model.applyTransformation(AnimationSequence.animations[localPlayer.standAnimationId].primaryFrames[0]);
				model.applyLighting(64, 850, -30, -50, -30, true);
				rsInterface.modelTypeDefault = 5;
				rsInterface.modelIdDefault = 0;
				RSInterface.setModel(model);
			}
			return;
		}
		if (type == 324) {
			if (this.characterEditButtonDefualt == null) {
				this.characterEditButtonDefualt = rsInterface.spriteDefault;
				this.characterEditButtonActive = rsInterface.spriteActive;
			}
			if (this.characterEditChangeGender) {
				rsInterface.spriteDefault = this.characterEditButtonActive;
				return;
			} else {
				rsInterface.spriteDefault = this.characterEditButtonDefualt;
				return;
			}
		}
		if (type == 325) {
			if (this.characterEditButtonDefualt == null) {
				this.characterEditButtonDefualt = rsInterface.spriteDefault;
				this.characterEditButtonActive = rsInterface.spriteActive;
			}
			if (this.characterEditChangeGender) {
				rsInterface.spriteDefault = this.characterEditButtonDefualt;
				return;
			} else {
				rsInterface.spriteDefault = this.characterEditButtonActive;
				return;
			}
		}
		if (type == 600) {
			rsInterface.textDefault = this.reportAbuseInput;
			if (tick % 20 < 10) {
				rsInterface.textDefault += "|";
				return;
			} else {
				rsInterface.textDefault += " ";
				return;
			}
		}
		if (type == 613) {
            if (this.playerRights >= 1) {
                if (this.reportAbuseMute) {
                    rsInterface.colourDefault = 0xFF0000;
                    rsInterface.textDefault = "Moderator option: Mute player for 48 hours: <ON>";
                } else {
                    rsInterface.colourDefault = 0xFFFFFF;
                    rsInterface.textDefault = "Moderator option: Mute player for 48 hours: <OFF>";
                }
            } else {
                rsInterface.textDefault = "";
            }
        }
		if (type == 650 || type == 655) {
            if (this.lastAddress != 0) {
                final String s;
                if (this.daysSinceLogin == 0) {
                    s = "earlier today";
                } else if (this.daysSinceLogin == 1) {
                    s = "yesterday";
                } else {
                    s = this.daysSinceLogin + " days ago";
                }
                rsInterface.textDefault = "You last logged in " + s + " from: " + signlink.dns;
            } else {
                rsInterface.textDefault = "";
            }
        }
		if (type == 651) {
			if (this.unreadMessages == 0) {
				rsInterface.textDefault = "0 unread messages";
				rsInterface.colourDefault = 0xFFFF00;
			}
			if (this.unreadMessages == 1) {
				rsInterface.textDefault = "1 unread message";
				rsInterface.colourDefault = 0x00FF00;
			}
			if (this.unreadMessages > 1) {
				rsInterface.textDefault = this.unreadMessages + " unread messages";
				rsInterface.colourDefault = 0x00FF00;
			}
		}
		if (type == 652) {
            if (this.daysSinceRecoveryChange == 201) {
                if (this.membership == 1) {
                    rsInterface.textDefault = "@yel@This is a non-members world: @whi@Since you are a member we";
                } else {
                    rsInterface.textDefault = "";
                }
            } else if (this.daysSinceRecoveryChange == 200) {
                rsInterface.textDefault = "You have not yet set any password recovery questions.";
            } else {
                final String time;
                if (this.daysSinceRecoveryChange == 0) {
                    time = "Earlier today";
                } else if (this.daysSinceRecoveryChange == 1) {
                    time = "Yesterday";
                } else {
                    time = this.daysSinceRecoveryChange + " days ago";
                }
                rsInterface.textDefault = time + " you changed your recovery questions";
            }
        }
		if (type == 653) {
            if (this.daysSinceRecoveryChange == 201) {
                if (this.membership == 1) {
                    rsInterface.textDefault = "@whi@recommend you use a members world instead. You may use";
                } else {
                    rsInterface.textDefault = "";
                }
            } else if (this.daysSinceRecoveryChange == 200) {
                rsInterface.textDefault = "We strongly recommend you do so now to secure your account.";
            } else {
                rsInterface.textDefault = "If you do not remember making this change then cancel it immediately";
            }
        }
		if (type == 654) {
			if (this.daysSinceRecoveryChange == 201) {
                if (this.membership == 1) {
                    rsInterface.textDefault = "@whi@this world but member benefits are unavailable whilst here.";
                    return;
                } else {
                    rsInterface.textDefault = "";
                    return;
                }
            }
			if (this.daysSinceRecoveryChange == 200) {
				rsInterface.textDefault = "Do this from the 'account management' area on our front webpage";
				return;
			}
			rsInterface.textDefault = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void drawGameScreen() {
		if (this.titleScreen.welcomeScreenRaised) {
			this.titleScreen.welcomeScreenRaised = false;
			this.backLeftIP1.drawGraphics(4, super.gameGraphics, 0);
			this.backLeftIP2.drawGraphics(357, super.gameGraphics, 0);
			this.backRightIP1.drawGraphics(4, super.gameGraphics, 722);
			this.backRightIP2.drawGraphics(205, super.gameGraphics, 743);
			this.backTopIP1.drawGraphics(0, super.gameGraphics, 0);
			this.backVmidIP1.drawGraphics(4, super.gameGraphics, 516);
			this.backVmidIP2.drawGraphics(205, super.gameGraphics, 516);
			this.backVmidIP3.drawGraphics(357, super.gameGraphics, 496);
			this.backVmidIP2_2.drawGraphics(338, super.gameGraphics, 0);
			this.redrawTab = true;
			this.redrawChatbox = true;
			this.drawTabIcons = true;
			this.updateChatSettings = true;
			if (this.loadingStage != 2) {
				this.gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
				this.minimap.draw(super.gameGraphics);
			}
		}
		if (this.loadingStage == 2) {
			this.renderGameView();
        }
		if (this.menuOpen && this.menuScreenArea == 1) {
			this.redrawTab = true;
        }
		if (this.inventoryOverlayInterfaceID != -1) {
			final boolean flag1 = this.animateInterface(this.animationTimePassed, this.inventoryOverlayInterfaceID);
			if (flag1) {
				this.redrawTab = true;
            }
		}
		if (this.atInventoryInterfaceType == 2) {
			this.redrawTab = true;
        }
		if (this.activeInterfaceType == 2) {
			this.redrawTab = true;
        }
		if (this.redrawTab) {
			this.drawTabArea();
			this.redrawTab = false;
		}
		if (this.chatboxInterfaceId == -1) {
			this.chatboxInterface.scrollPosition = this.chatboxScrollMax - this.anInt1089 - 77;
			if (super.mouseX > 448 && super.mouseX < 560 && super.mouseY > 332) {
				this.scrollInterface(463, 77, super.mouseX - 17, super.mouseY - 357, this.chatboxInterface, 0, false,
						this.chatboxScrollMax);
            }
			int i = this.chatboxScrollMax - 77 - this.chatboxInterface.scrollPosition;
			if (i < 0) {
                i = 0;
            }
			if (i > this.chatboxScrollMax - 77) {
                i = this.chatboxScrollMax - 77;
            }
			if (this.anInt1089 != i) {
				this.anInt1089 = i;
				this.redrawChatbox = true;
			}
		}
		if (this.chatboxInterfaceId != -1) {
			final boolean flag2 = this.animateInterface(this.animationTimePassed, this.chatboxInterfaceId);
			if (flag2) {
				this.redrawChatbox = true;
            }
		}
		if (this.atInventoryInterfaceType == 3) {
			this.redrawChatbox = true;
        }
		if (this.activeInterfaceType == 3) {
			this.redrawChatbox = true;
        }
		if (this.clickToContinueString != null) {
			this.redrawChatbox = true;
        }
		if (this.menuOpen && this.menuScreenArea == 2) {
			this.redrawChatbox = true;
        }
		if (this.redrawChatbox) {
			this.drawChatArea();
			this.redrawChatbox = false;
		}
		if (this.loadingStage == 2) {
			this.minimap.updateImageProducer(
					this.baseX, this.baseY,
					this.localPlayerCount, this.players, this.localPlayers,
					this.friendsCount, this.friendsListAsLongs, this.friendsWorldIds,
					this.groundArray[this.plane],
					this.npcCount, this.npcs, this.npcIds,
					this.hintIconType, this.hintIconNpcId, this.hintIconPlayerId, this.hintIconX, this.hintIconY,
					this.destinationX, this.destinationY, tick
			);
			this.minimap.draw(super.gameGraphics);
			this.gameScreenImageProducer.initDrawingArea();
		}
		if (this.flashingSidebar != -1) {
			this.drawTabIcons = true;
        }
		if (this.drawTabIcons) {
			if (this.flashingSidebar != -1 && this.flashingSidebar == this.currentTabId) {
				this.flashingSidebar = -1;
				this.stream.putOpcode(120);
				this.stream.put(this.currentTabId);
			}
			this.drawTabIcons = false;
			this.topSideIconImageProducer.initDrawingArea();
			this.backHmid1Image.draw(0, 0);
			if (this.inventoryOverlayInterfaceID == -1) {
				if (this.tabInterfaceIDs[this.currentTabId] != -1) {
					if (this.currentTabId == 0) {
						this.redStone1.draw(22, 10);
                    }
					if (this.currentTabId == 1) {
						this.redStone2.draw(54, 8);
                    }
					if (this.currentTabId == 2) {
						this.redStone2.draw(82, 8);
                    }
					if (this.currentTabId == 3) {
						this.redStone3.draw(110, 8);
                    }
					if (this.currentTabId == 4) {
						this.redStone2_2.draw(153, 8);
                    }
					if (this.currentTabId == 5) {
						this.redStone2_2.draw(181, 8);
                    }
					if (this.currentTabId == 6) {
						this.redStone1_2.draw(209, 9);
                    }
				}
				if (this.tabInterfaceIDs[0] != -1 && (this.flashingSidebar != 0 || tick % 20 < 10)) {
					this.sideIconImage[0].draw(29, 13);
                }
				if (this.tabInterfaceIDs[1] != -1 && (this.flashingSidebar != 1 || tick % 20 < 10)) {
					this.sideIconImage[1].draw(53, 11);
                }
				if (this.tabInterfaceIDs[2] != -1 && (this.flashingSidebar != 2 || tick % 20 < 10)) {
					this.sideIconImage[2].draw(82, 11);
                }
				if (this.tabInterfaceIDs[3] != -1 && (this.flashingSidebar != 3 || tick % 20 < 10)) {
					this.sideIconImage[3].draw(115, 12);
                }
				if (this.tabInterfaceIDs[4] != -1 && (this.flashingSidebar != 4 || tick % 20 < 10)) {
					this.sideIconImage[4].draw(153, 13);
                }
				if (this.tabInterfaceIDs[5] != -1 && (this.flashingSidebar != 5 || tick % 20 < 10)) {
					this.sideIconImage[5].draw(180, 11);
                }
				if (this.tabInterfaceIDs[6] != -1 && (this.flashingSidebar != 6 || tick % 20 < 10)) {
					this.sideIconImage[6].draw(208, 13);
                }
			}
			this.topSideIconImageProducer.drawGraphics(160, super.gameGraphics, 516);
			this.bottomSideIconImageProducer.initDrawingArea();
			this.backBase2Image.draw(0, 0);
			if (this.inventoryOverlayInterfaceID == -1) {
				if (this.tabInterfaceIDs[this.currentTabId] != -1) {
					if (this.currentTabId == 7) {
						this.redStone1_3.draw(42, 0);
                    }
					if (this.currentTabId == 8) {
						this.redStone2_3.draw(74, 0);
                    }
					if (this.currentTabId == 9) {
						this.redStone2_3.draw(102, 0);
                    }
					if (this.currentTabId == 10) {
						this.redStone3_2.draw(130, 1);
                    }
					if (this.currentTabId == 11) {
						this.redStone2_4.draw(173, 0);
                    }
					if (this.currentTabId == 12) {
						this.redStone2_4.draw(201, 0);
                    }
					if (this.currentTabId == 13) {
						this.redStone1_4.draw(229, 0);
                    }
				}
				if (this.tabInterfaceIDs[8] != -1 && (this.flashingSidebar != 8 || tick % 20 < 10)) {
					this.sideIconImage[7].draw(74, 2);
                }
				if (this.tabInterfaceIDs[9] != -1 && (this.flashingSidebar != 9 || tick % 20 < 10)) {
					this.sideIconImage[8].draw(102, 3);
                }
				if (this.tabInterfaceIDs[10] != -1 && (this.flashingSidebar != 10 || tick % 20 < 10)) {
					this.sideIconImage[9].draw(137, 4);
                }
				if (this.tabInterfaceIDs[11] != -1 && (this.flashingSidebar != 11 || tick % 20 < 10)) {
					this.sideIconImage[10].draw(174, 2);
                }
				if (this.tabInterfaceIDs[12] != -1 && (this.flashingSidebar != 12 || tick % 20 < 10)) {
					this.sideIconImage[11].draw(201, 2);
                }
				if (this.tabInterfaceIDs[13] != -1 && (this.flashingSidebar != 13 || tick % 20 < 10)) {
					this.sideIconImage[12].draw(226, 2);
                }
			}
			this.bottomSideIconImageProducer.drawGraphics(466, super.gameGraphics, 496);
			this.gameScreenImageProducer.initDrawingArea();
		}
		if (this.updateChatSettings) {
			this.updateChatSettings = false;
			this.chatSettingImageProducer.initDrawingArea();
			this.backBase1Image.draw(0, 0);
			this.fontPlain.drawCentredTextWithPotentialShadow("Public chat", 55, 28, 0xFFFFFF, true);
			if (this.publicChatMode == 0) {
				this.fontPlain.drawCentredTextWithPotentialShadow("On", 55, 41, 0x00FF00, true);
            }
			if (this.publicChatMode == 1) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Friends", 55, 41, 0xFFFF00, true);
            }
			if (this.publicChatMode == 2) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Off", 55, 41, 0xFF0000, true);
            }
			if (this.publicChatMode == 3) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Hide", 55, 41, 0x00FFFF, true);
            }
			this.fontPlain.drawCentredTextWithPotentialShadow("Private chat", 184, 28, 0xFFFFFF, true);
			if (this.privateChatMode == 0) {
				this.fontPlain.drawCentredTextWithPotentialShadow("On", 184, 41, 0x00FF00, true);
            }
			if (this.privateChatMode == 1) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Friends", 184, 41, 0xFFFF00, true);
            }
			if (this.privateChatMode == 2) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Off", 184, 41, 0xFF0000, true);
            }
			this.fontPlain.drawCentredTextWithPotentialShadow("Trade/compete", 324, 28, 0xFFFFFF, true);
			if (this.tradeMode == 0) {
				this.fontPlain.drawCentredTextWithPotentialShadow("On", 324, 41, 0x00FF00, true);
            }
			if (this.tradeMode == 1) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Friends", 324, 41, 0xFFFF00, true);
            }
			if (this.tradeMode == 2) {
				this.fontPlain.drawCentredTextWithPotentialShadow("Off", 324, 41, 0xFF0000, true);
            }
			this.fontPlain.drawCentredTextWithPotentialShadow("Report abuse", 458, 33, 0xFFFFFF, true);
			this.chatSettingImageProducer.drawGraphics(453, super.gameGraphics, 0);
			this.gameScreenImageProducer.initDrawingArea();
		}
		this.animationTimePassed = 0;
	}

	private void drawHeadIcon() {
		if (this.hintIconType != 2) {
            return;
        }
		this.calculateScreenPosition((this.hintIconX - this.baseX << 7) + this.hintIconDrawTileX, this.hintIconDrawHeight * 2,
				(this.hintIconY - this.baseY << 7) + this.hintIconDrawTileY);
		if (this.spriteDrawX > -1 && tick % 20 < 10) {
			this.headIcons[2].drawImage(this.spriteDrawX - 12, this.spriteDrawY - 28);
        }
	}

	private void drawInterface(final int j, final int x, final RSInterface rsInterface, final int y) {
		if (rsInterface.type != 0 || rsInterface.children == null) {
            return;
        }
		if (rsInterface.hoverOnly && this.anInt1026 != rsInterface.id && this.anInt1048 != rsInterface.id
				&& this.anInt1039 != rsInterface.id) {
            return;
        }
		final int clipLeft = DrawingArea.topX;
		final int clipTop = DrawingArea.topY;
		final int clipRight = DrawingArea.bottomX;
		final int clipBottom = DrawingArea.bottomY;
		DrawingArea.setDrawingArea(y + rsInterface.height, x, x + rsInterface.width, y);
		final int childCount = rsInterface.children.length;
		for (int childId = 0; childId < childCount; childId++) {
			int _x = rsInterface.childX[childId] + x;
			int _y = (rsInterface.childY[childId] + y) - j;
			final RSInterface childInterface = RSInterface.cache[rsInterface.children[childId]];
			_x += childInterface.x;
			_y += childInterface.y;
			if (childInterface.contentType > 0) {
				this.drawFriendsListOrWelcomeScreen(childInterface);
            }
			if (childInterface.type == 0) {
				if (childInterface.scrollPosition > childInterface.scrollMax - childInterface.height) {
                    childInterface.scrollPosition = childInterface.scrollMax - childInterface.height;
                }
				if (childInterface.scrollPosition < 0) {
                    childInterface.scrollPosition = 0;
                }
				this.drawInterface(childInterface.scrollPosition, _x, childInterface, _y);
				if (childInterface.scrollMax > childInterface.height) {
					this.renderChatInterface(_x + childInterface.width, _y, childInterface.height,
                            childInterface.scrollPosition, childInterface.scrollMax);
                }
			} else if (childInterface.type != 1) {
                if (childInterface.type == 2) {
                    int item = 0;
                    for (int row = 0; row < childInterface.height; row++) {
                        for (int column = 0; column < childInterface.width; column++) {
                            int tileX = _x + column * (32 + childInterface.inventorySpritePaddingColumn);
                            int tileY = _y + row * (32 + childInterface.inventorySpritePaddingRow);
                            if (item < 20) {
                                tileX += childInterface.spritesX[item];
                                tileY += childInterface.spritesY[item];
                            }
                            if (childInterface.inventoryItemId[item] > 0) {
                                int differenceX = 0;
                                int differenceY = 0;
                                final int itemId = childInterface.inventoryItemId[item] - 1;
                                if (tileX > DrawingArea.topX - 32 && tileX < DrawingArea.bottomX
                                        && tileY > DrawingArea.topY - 32 && tileY < DrawingArea.bottomY
                                        || this.activeInterfaceType != 0 && this.moveItemSlotStart == item) {
                                    int outlineColour = 0;
                                    if (this.itemSelected && this.lastItemSelectedSlot == item
                                            && this.lastItemSelectedInterface == childInterface.id) {
                                        outlineColour = 0xFFFFFF;
                                    }
                                    final Sprite sprite = ItemDefinition.getSprite(itemId,
                                            childInterface.inventoryStackSize[item], outlineColour);
                                    if (sprite != null) {
                                        if (this.activeInterfaceType != 0 && this.moveItemSlotStart == item
                                                && this.moveItemInterfaceId == childInterface.id) {
                                            differenceX = super.mouseX - this.lastMouseX;
                                            differenceY = super.mouseY - this.lastMouseY;
                                            if (differenceX < 5 && differenceX > -5) {
                                                differenceX = 0;
                                            }
                                            if (differenceY < 5 && differenceY > -5) {
                                                differenceY = 0;
                                            }
                                            if (this.lastItemDragTime < 5) {
                                                differenceX = 0;
                                                differenceY = 0;
                                            }
                                            sprite.drawImageAlpha(tileX + differenceX, tileY + differenceY);
                                            if (tileY + differenceY < DrawingArea.topY
                                                    && rsInterface.scrollPosition > 0) {
                                                int difference = (this.animationTimePassed
                                                        * (DrawingArea.topY - tileY - differenceY)) / 3;
                                                if (difference > this.animationTimePassed * 10) {
                                                    difference = this.animationTimePassed * 10;
                                                }
                                                if (difference > rsInterface.scrollPosition) {
                                                    difference = rsInterface.scrollPosition;
                                                }
                                                rsInterface.scrollPosition -= difference;
												this.lastMouseY += difference;
                                            }
                                            if (tileY + differenceY + 32 > DrawingArea.bottomY
                                                    && rsInterface.scrollPosition < rsInterface.scrollMax
                                                            - rsInterface.height) {
                                                int difference = (this.animationTimePassed
                                                        * ((tileY + differenceY + 32) - DrawingArea.bottomY)) / 3;
                                                if (difference > this.animationTimePassed * 10) {
                                                    difference = this.animationTimePassed * 10;
                                                }
                                                if (difference > rsInterface.scrollMax - rsInterface.height
                                                        - rsInterface.scrollPosition) {
                                                    difference = rsInterface.scrollMax - rsInterface.height
                                                            - rsInterface.scrollPosition;
                                                }
                                                rsInterface.scrollPosition += difference;
												this.lastMouseY -= difference;
                                            }
                                        } else if (this.atInventoryInterfaceType != 0 && this.atInventoryIndex == item
                                                && this.atInventoryInterface == childInterface.id) {
                                            sprite.drawImageAlpha(tileX, tileY);
                                        } else {
                                            sprite.drawImage(tileX, tileY);
                                        }
                                        if (sprite.maxWidth == 33 || childInterface.inventoryStackSize[item] != 1) {
                                            final int stackSize = childInterface.inventoryStackSize[item];
											this.fontSmall.drawText(getAmountString(stackSize), tileX + 1 + differenceX,
                                                    tileY + 10 + differenceY, 0);
											this.fontSmall.drawText(getAmountString(stackSize), tileX + differenceX,
                                                    tileY + 9 + differenceY, 0xFFFF00);
                                        }
                                    }
                                }
                            } else if (childInterface.sprites != null && item < 20) {
                                final Sprite sprite = childInterface.sprites[item];
                                if (sprite != null) {
                                    sprite.drawImage(tileX, tileY);
                                }
                            }
                            item++;
                        }

                    }

                } else if (childInterface.type == 3) {
                    boolean hover = false;
                    if (this.anInt1039 == childInterface.id || this.anInt1048 == childInterface.id
                            || this.anInt1026 == childInterface.id) {
                        hover = true;
                    }
                    int colour;
                    if (this.interfaceIsActive(childInterface)) {
                        colour = childInterface.colourActive;
                        if (hover && childInterface.colourActiveHover != 0) {
                            colour = childInterface.colourActiveHover;
                        }
                    } else {
                        colour = childInterface.colourDefault;
                        if (hover && childInterface.colourDefaultHover != 0) {
                            colour = childInterface.colourDefaultHover;
                        }
                    }
                    if (childInterface.alpha == 0) {
                        if (childInterface.filled) {
                            DrawingArea.drawFilledRectangle(_x, _y, childInterface.width, childInterface.height,
                                    colour);
                        } else {
                            DrawingArea.drawUnfilledRectangle(_x, childInterface.width, childInterface.height, colour,
                                    _y);
                        }
                    } else if (childInterface.filled) {
                        DrawingArea.drawFilledRectangleAlpha(colour, _y, childInterface.width, childInterface.height,
                                256 - (childInterface.alpha & 0xFF), _x);
                    } else {
                        DrawingArea.drawUnfilledRectangleAlpha(_y, childInterface.height,
                                256 - (childInterface.alpha & 0xFF), colour, childInterface.width, _x);
                    }
                } else if (childInterface.type == 4) {
                    final GameFont textDrawingArea = childInterface.textDrawingAreas;
                    String text = childInterface.textDefault;
                    boolean hover = false;
                    if (this.anInt1039 == childInterface.id || this.anInt1048 == childInterface.id
                            || this.anInt1026 == childInterface.id) {
                        hover = true;
                    }
                    int colour;
                    if (this.interfaceIsActive(childInterface)) {
                        colour = childInterface.colourActive;
                        if (hover && childInterface.colourActiveHover != 0) {
                            colour = childInterface.colourActiveHover;
                        }
                        if (childInterface.textActive.length() > 0) {
                            text = childInterface.textActive;
                        }
                    } else {
                        colour = childInterface.colourDefault;
                        if (hover && childInterface.colourDefaultHover != 0) {
                            colour = childInterface.colourDefaultHover;
                        }
                    }
                    if (childInterface.actionType == 6 && this.continuedDialogue) {
                        text = "Please wait...";
                        colour = childInterface.colourDefault;
                    }
                    if (DrawingArea.width == 479) {
                        if (colour == 0xFFFF00) {
                            colour = 255;
                        }
                        if (colour == 49152) {
                            colour = 0xFFFFFF;
                        }
                    }
                    for (int __y = _y + textDrawingArea.fontHeight; text
                            .length() > 0; __y += textDrawingArea.fontHeight) {
                        if (text.contains("%")) {
                            do {
                                final int placeholder = text.indexOf("%1");
                                if (placeholder == -1) {
                                    break;
                                }
                                text = text.substring(0, placeholder)
                                        + this.interfaceIntToString(this.parseInterfaceOpcode(childInterface, 0))
                                        + text.substring(placeholder + 2);
                            } while (true);
                            do {
                                final int placeholder = text.indexOf("%2");
                                if (placeholder == -1) {
                                    break;
                                }
                                text = text.substring(0, placeholder)
                                        + this.interfaceIntToString(this.parseInterfaceOpcode(childInterface, 1))
                                        + text.substring(placeholder + 2);
                            } while (true);
                            do {
                                final int placeholder = text.indexOf("%3");
                                if (placeholder == -1) {
                                    break;
                                }
                                text = text.substring(0, placeholder)
                                        + this.interfaceIntToString(this.parseInterfaceOpcode(childInterface, 2))
                                        + text.substring(placeholder + 2);
                            } while (true);
                            do {
                                final int placeholder = text.indexOf("%4");
                                if (placeholder == -1) {
                                    break;
                                }
                                text = text.substring(0, placeholder)
                                        + this.interfaceIntToString(this.parseInterfaceOpcode(childInterface, 3))
                                        + text.substring(placeholder + 2);
                            } while (true);
                            do {
                                final int placeholder = text.indexOf("%5");
                                if (placeholder == -1) {
                                    break;
                                }
                                text = text.substring(0, placeholder)
                                        + this.interfaceIntToString(this.parseInterfaceOpcode(childInterface, 4))
                                        + text.substring(placeholder + 2);
                            } while (true);
                        }
                        final int newLine = text.indexOf("\\n");
                        final String text2;
                        if (newLine != -1) {
                            text2 = text.substring(0, newLine);
                            text = text.substring(newLine + 2);
                        } else {
                            text2 = text;
                            text = "";
                        }
                        if (childInterface.textCentred) {
                            textDrawingArea.drawCentredTextWithPotentialShadow(text2, _x + childInterface.width / 2,
                                    __y, colour, childInterface.textShadowed);
                        } else {
                            textDrawingArea.drawTextWithPotentialShadow(text2, _x, __y, colour,
                                    childInterface.textShadowed);
                        }
                    }

                } else if (childInterface.type == 5) {
                    final Sprite sprite = this.interfaceIsActive(childInterface) ? childInterface.spriteActive
                            : childInterface.spriteDefault;

                    if (sprite != null) {
                        sprite.drawImage(_x, _y);
                    }
                } else if (childInterface.type == 6) {
                    final int centreX = Rasterizer.centreX;
                    final int centreY = Rasterizer.centreY;
                    Rasterizer.centreX = _x + childInterface.width / 2;
                    Rasterizer.centreY = _y + childInterface.height / 2;
                    final int sine = Rasterizer.SINE[childInterface.modelRotationX] * childInterface.modelZoom >> 16;
                    final int cosine = Rasterizer.COSINE[childInterface.modelRotationX] * childInterface.modelZoom >> 16;
                    final boolean active = this.interfaceIsActive(childInterface);
                    final int anim = active ? childInterface.animationIdActive : childInterface.animationIdDefault;

                    final Model model;
                    if (anim == -1) {
                        model = childInterface.getAnimatedModel(-1, -1, active);
                    } else {
                        final AnimationSequence animation = AnimationSequence.animations[anim];
                        model = childInterface.getAnimatedModel(animation.secondaryFrames[childInterface.animationFrame],
                                animation.primaryFrames[childInterface.animationFrame], active);
                    }
                    if (model != null) {
                        model.renderSingle(childInterface.modelRotationY, 0, childInterface.modelRotationX, 0, sine,
                                cosine);
                    }
                    Rasterizer.centreX = centreX;
                    Rasterizer.centreY = centreY;
                } else if (childInterface.type == 7) {
                    final GameFont font = childInterface.textDrawingAreas;
                    int slot = 0;
                    for (int row = 0; row < childInterface.height; row++) {
                        for (int column = 0; column < childInterface.width; column++) {
                            if (childInterface.inventoryItemId[slot] > 0) {
                                final ItemDefinition item = ItemDefinition
                                        .getDefinition(childInterface.inventoryItemId[slot] - 1);
                                String name = item.name;
                                if (item.stackable || childInterface.inventoryStackSize[slot] != 1) {
                                    name = name + " x" + formatAmount(childInterface.inventoryStackSize[slot]);
                                }
                                final int __x = _x + column * (115 + childInterface.inventorySpritePaddingColumn);
                                final int __y = _y + row * (12 + childInterface.inventorySpritePaddingRow);
                                if (childInterface.textCentred) {
                                    font.drawCentredTextWithPotentialShadow(name, __x + childInterface.width / 2, __y,
                                            childInterface.colourDefault, childInterface.textShadowed);
                                } else {
                                    font.drawTextWithPotentialShadow(name, __x, __y, childInterface.colourDefault,
                                            childInterface.textShadowed);
                                }
                            }
                            slot++;
                        }

                    }
                }
            }
		}

		DrawingArea.setDrawingArea(clipBottom, clipLeft, clipRight, clipTop);
	}

	@Override
	void drawLoadingText(final int percentage, final String text) {
		this.loadingBarPercentage = percentage;
		this.loadingBarText = text;
		this.setupLoginScreen();
		if (this.archiveTitle == null) {
			super.drawLoadingText(percentage, text);
			return;
		}

		this.titleScreen.drawLoadingText(super.gameGraphics, percentage, text);
	}

	private void drawMenu() {
		final int offsetX = this.menuOffsetX;
		final int offsetY = this.menuOffsetY;
		final int width = this.menuWidth;
		final int height = this.menuHeight;
		final int colour = 0x5D5447;
		DrawingArea.drawFilledRectangle(offsetX, offsetY, width, height, colour);
		DrawingArea.drawFilledRectangle(offsetX + 1, offsetY + 1, width - 2, 16, 0);
		DrawingArea.drawUnfilledRectangle(offsetX + 1, width - 2, height - 19, 0, offsetY + 18);
		this.fontBold.drawText("Choose Option", offsetX + 3, offsetY + 14, colour);
		int x = super.mouseX;
		int y = super.mouseY;
		if (this.menuScreenArea == 0) {
			x -= 4;
			y -= 4;
		}
		if (this.menuScreenArea == 1) {
			x -= 553;
			y -= 205;
		}
		if (this.menuScreenArea == 2) {
			x -= 17;
			y -= 357;
		}
		for (int action = 0; action < this.menuActionRow; action++) {
			final int actionY = offsetY + 31 + (this.menuActionRow - 1 - action) * 15;
			int actionColour = 0xFFFFFF;
			if (x > offsetX && x < offsetX + width && y > actionY - 13 && y < actionY + 3) {
                actionColour = 0xFFFF00;
            }
			this.fontBold.drawTextWithPotentialShadow(this.menuActionName[action], offsetX + 3, actionY, actionColour, true);
		}

	}

	private void drawSplitPrivateChat() {
		if (this.splitPrivateChat == 0) {
            return;
        }
		final GameFont textDrawingArea = this.fontPlain;
		int updating = 0;
		if (this.systemUpdateTime != 0) {
            updating = 1;
        }
		for (int m = 0; m < 100; m++) {
            if (this.chatMessages[m] != null) {
                final int chatType = this.chatTypes[m];
                String chatName = this.chatNames[m];
                byte playerRights = 0;
                if (chatName != null && chatName.startsWith("@cr1@")) {
                    chatName = chatName.substring(5);
                    playerRights = 1;
                }
                if (chatName != null && chatName.startsWith("@cr2@")) {
                    chatName = chatName.substring(5);
                    playerRights = 2;
                }
                if ((chatType == 3 || chatType == 7) && (chatType == 7 || this.privateChatMode == 0
                        || this.privateChatMode == 1 && this.isFriendOrSelf(chatName))) {
                    final int y = 329 - updating * 13;
                    int x = 4;
                    textDrawingArea.drawText("From", x, y, 0);
                    textDrawingArea.drawText("From", x, y - 1, 0x00FFFF);
                    x += textDrawingArea.getTextDisplayedWidth("From ");
                    if (playerRights == 1) {
						this.modIcons[0].draw(x, y - 12);
                        x += 14;
                    }
                    if (playerRights == 2) {
						this.modIcons[1].draw(x, y - 12);
                        x += 14;
                    }
                    textDrawingArea.drawText(chatName + ": " + this.chatMessages[m], x, y, 0);
                    textDrawingArea.drawText(chatName + ": " + this.chatMessages[m], x, y - 1, 0x00FFFF);
                    if (++updating >= 5) {
                        return;
                    }
                }
                if (chatType == 5 && this.privateChatMode < 2) {
                    final int y = 329 - updating * 13;
                    textDrawingArea.drawText(this.chatMessages[m], 4, y, 0);
                    textDrawingArea.drawText(this.chatMessages[m], 4, y - 1, 0x00FFFF);
                    if (++updating >= 5) {
                        return;
                    }
                }
                if (chatType == 6 && this.privateChatMode < 2) {
                    final int y = 329 - updating * 13;
                    textDrawingArea.drawText("To " + chatName + ": " + this.chatMessages[m], 4, y, 0);
                    textDrawingArea.drawText("To " + chatName + ": " + this.chatMessages[m], 4, y - 1, 0x00FFFF);
                    if (++updating >= 5) {
                        return;
                    }
                }
            }
        }
	}

	private void drawTabArea() {
		this.tabImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = this.sidebarOffsets;
		this.inventoryBackgroundImage.draw(0, 0);
		if (this.inventoryOverlayInterfaceID != -1) {
			this.drawInterface(0, 0, RSInterface.cache[this.inventoryOverlayInterfaceID], 0);
        } else if (this.tabInterfaceIDs[this.currentTabId] != -1) {
			this.drawInterface(0, 0, RSInterface.cache[this.tabInterfaceIDs[this.currentTabId]], 0);
        }
		if (this.menuOpen && this.menuScreenArea == 1) {
			this.drawMenu();
        }
		this.tabImageProducer.drawGraphics(205, super.gameGraphics, 553);
		this.gameScreenImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = this.viewportOffsets;
	}

	private void drawTooltip() {
		if (this.menuActionRow < 2 && this.itemSelected == false && this.spellSelected == false) {
            return;
        }
		String s;
		if (this.itemSelected && this.menuActionRow < 2) {
            s = "Use " + this.selectedItemName + " with...";
        } else if (this.spellSelected && this.menuActionRow < 2) {
            s = this.spellTooltip + "...";
        } else {
            s = this.menuActionName[this.menuActionRow - 1];
        }
		if (this.menuActionRow > 2) {
            s = s + "@whi@ / " + (this.menuActionRow - 2) + " more options";
        }
		this.fontBold.drawAlphaTextWithShadow(s, 4, 15, 0xFFFFFF, tick / 1000);
	}

	private void dropClient() {
		if (this.idleLogout > 0) {
			this.logout();
			return;
		}
		this.gameScreenImageProducer.initDrawingArea();
		this.fontPlain.drawCentredText("Connection lost", 257, 144, 0);
		this.fontPlain.drawCentredText("Connection lost", 256, 143, 0xFFFFFF);
		this.fontPlain.drawCentredText("Please wait - attempting to reestablish", 257, 159, 0);
		this.fontPlain.drawCentredText("Please wait - attempting to reestablish", 256, 158, 0xFFFFFF);
		this.gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
		this.minimap.state = 0;
		this.destinationX = 0;
		final RSSocket rsSocket = this.socket;
		this.loggedIn = false;
		this.loginFailures = 0;
		this.login(this.enteredUsername, this.enteredPassword, true);
		if (!this.loggedIn) {
			this.logout();
        }
		try {
			rsSocket.close();
		} catch (final Exception _ex) {
		}
	}

	@Override
	public AppletContext getAppletContext() {
		if (signlink.applet != null) {
            return signlink.applet.getAppletContext();
        } else {
            return super.getAppletContext();
        }
	}

	private int getCameraPlaneCutscene() {
		final int terrainDrawHeight = this.getFloorDrawHeight(this.plane, this.cameraPositionY, this.cameraPositionX);
		if (terrainDrawHeight - this.cameraPositionZ < 800
				&& (this.tileFlags[this.plane][this.cameraPositionX >> 7][this.cameraPositionY >> 7] & 4) != 0) {
            return this.plane;
        } else {
            return 3;
        }
	}

	@Override
	public URL getCodeBase() {
		if (signlink.applet != null) {
            return signlink.applet.getCodeBase();
        }
		try {
			if (super.gameFrame != null) {
                return new URL("http://127.0.0.1:" + (80 + portOffset));
            }
		} catch (final Exception _ex) {
		}
		return super.getCodeBase();
	}

	private String getDocumentBaseHost() {
		if (signlink.applet != null) {
            return signlink.applet.getDocumentBase().getHost().toLowerCase();
        }
		if (super.gameFrame != null) {
            return "runescape.com";
        } else {
            return super.getDocumentBase().getHost().toLowerCase();
        }
	}

	private int getFloorDrawHeight(final int z, final int y, final int x) {
		final int groundX = x >> 7;
		final int groundY = y >> 7;
		if (groundX < 0 || groundY < 0 || groundX > 103 || groundY > 103) {
            return 0;
        }
		int groundZ = z;
		if (groundZ < 3 && (this.tileFlags[1][groundX][groundY] & 2) == 2) {
            groundZ++;
        }
		final int _x = x & 0x7F;
		final int _y = y & 0x7F;
		final int i2 = this.intGroundArray[groundZ][groundX][groundY] * (128 - _x)
				+ this.intGroundArray[groundZ][groundX + 1][groundY] * _x >> 7;
		final int j2 = this.intGroundArray[groundZ][groundX][groundY + 1] * (128 - _x)
				+ this.intGroundArray[groundZ][groundX + 1][groundY + 1] * _x >> 7;

		return i2 * (128 - _y) + j2 * _y >> 7;
	}

	@Override
	Component getGameComponent() {
		if (signlink.applet != null) {
            return signlink.applet;
        }
		if (super.gameFrame != null) {
            return super.gameFrame;
        } else {
            return this;
        }
	}

	@Override
	public String getParameter(final String s) {
		if (signlink.applet != null) {
            return signlink.applet.getParameter(s);
        } else {
            return super.getParameter(s);
        }
	}

	private int getWorldDrawPlane() {
		int worldDrawPlane = 3;
		if (this.cameraVerticalRotation < 310) {
			int cameraX = this.cameraPositionX >> 7;
			int cameraY = this.cameraPositionY >> 7;
			final int playerX = localPlayer.x >> 7;
			final int playerY = localPlayer.y >> 7;
			if ((this.tileFlags[this.plane][cameraX][cameraY] & 4) != 0) {
                worldDrawPlane = this.plane;
            }
			final int x;
			if (playerX > cameraX) {
                x = playerX - cameraX;
            } else {
                x = cameraX - playerX;
            }
			final int y;
			if (playerY > cameraY) {
                y = playerY - cameraY;
            } else {
                y = cameraY - playerY;
            }
			if (x > y) {
				final int unknown1 = (y * 65536) / x;
				int unknown2 = 32768;
				while (cameraX != playerX) {
					if (cameraX < playerX) {
                        cameraX++;
                    } else if (cameraX > playerX) {
                        cameraX--;
                    }
					if ((this.tileFlags[this.plane][cameraX][cameraY] & 4) != 0) {
                        worldDrawPlane = this.plane;
                    }
					unknown2 += unknown1;
					if (unknown2 >= 65536) {
						unknown2 -= 65536;
						if (cameraY < playerY) {
                            cameraY++;
                        } else if (cameraY > playerY) {
                            cameraY--;
                        }
						if ((this.tileFlags[this.plane][cameraX][cameraY] & 4) != 0) {
                            worldDrawPlane = this.plane;
                        }
					}
				}
			} else {
				final int unknown1 = (x * 65536) / y;
				int unknown2 = 32768;
				while (cameraY != playerY) {
					if (cameraY < playerY) {
                        cameraY++;
                    } else if (cameraY > playerY) {
                        cameraY--;
                    }
					if ((this.tileFlags[this.plane][cameraX][cameraY] & 4) != 0) {
                        worldDrawPlane = this.plane;
                    }
					unknown2 += unknown1;
					if (unknown2 >= 65536) {
						unknown2 -= 65536;
						if (cameraX < playerX) {
                            cameraX++;
                        } else if (cameraX > playerX) {
                            cameraX--;
                        }
						if ((this.tileFlags[this.plane][cameraX][cameraY] & 4) != 0) {
                            worldDrawPlane = this.plane;
                        }
					}
				}
			}
		}
		if ((this.tileFlags[this.plane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0) {
            worldDrawPlane = this.plane;
        }

		return worldDrawPlane;
	}

	private boolean handleIncomingData() {
		if (this.socket == null) {
            return false;
        }
		try {
			int availableBytes = this.socket.available();
			if (availableBytes == 0) {
                return false;
            }
			if (this.packetOpcode == -1) {
				this.socket.read(this.inStream.buffer, 1);
				this.packetOpcode = this.inStream.buffer[0] & 0xFF;
				if (this.encryption != null) {
					this.packetOpcode = this.packetOpcode - this.encryption.value() & 0xFF;
                }
				this.packetSize = PacketInformation.PACKET_SIZES[this.packetOpcode];
				availableBytes--;
			}
			if (this.packetSize == -1) {
                if (availableBytes > 0) {
					this.socket.read(this.inStream.buffer, 1);
					this.packetSize = this.inStream.buffer[0] & 0xFF;
                    availableBytes--;
                } else {
                    return false;
                }
            }
			if (this.packetSize == -2) {
                if (availableBytes > 1) {
					this.socket.read(this.inStream.buffer, 2);
					this.inStream.position = 0;
					this.packetSize = this.inStream.getUnsignedLEShort();
                    availableBytes -= 2;
                } else {
                    return false;
                }
            }
			if (availableBytes < this.packetSize) {
                return false;
            }
			this.inStream.position = 0;
			this.socket.read(this.inStream.buffer, this.packetSize);
			this.packetReadAnticheat = 0;
			this.thirdMostRecentOpcode = this.secondMostRecentOpcode;
			this.secondMostRecentOpcode = this.mostRecentOpcode;
			this.mostRecentOpcode = this.packetOpcode;
			if (this.packetOpcode == 81) {
				this.updatePlayers(this.packetSize, this.inStream);
				this.loadingMap = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 176) {
				this.daysSinceRecoveryChange = this.inStream.getUnsignedByteC();
				this.unreadMessages = this.inStream.getUnsignedLEShortA();
				this.membership = this.inStream.getUnsignedByte();
				this.lastAddress = this.inStream.getMEBInt();
				this.daysSinceLogin = this.inStream.getUnsignedLEShort();
				if (this.lastAddress != 0 && this.openInterfaceId == -1) {
					signlink.dnslookup(TextClass.decodeDNS(this.lastAddress));
					this.clearTopInterfaces();
					int contentType = 650;
					if (this.daysSinceRecoveryChange != 201 || this.membership == 1) {
                        contentType = 655;
                    }
					this.reportAbuseInput = "";
					this.reportAbuseMute = false;
					for (int interfaceId = 0; interfaceId < RSInterface.cache.length; interfaceId++) {
						if (RSInterface.cache[interfaceId] == null
								|| RSInterface.cache[interfaceId].contentType != contentType) {
                            continue;
                        }
						this.openInterfaceId = RSInterface.cache[interfaceId].parentID;
						break;
					}

				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 64) {
				this.playerPositionX = this.inStream.getUnsignedByteC();
				this.playerPositionY = this.inStream.getUnsignedByteS();
				for (int x = this.playerPositionX; x < this.playerPositionX + 8; x++) {
					for (int y = this.playerPositionY; y < this.playerPositionY + 8; y++) {
                        if (this.groundArray[this.plane][x][y] != null) {
							this.groundArray[this.plane][x][y] = null;
							this.spawnGroundItem(x, y);
                        }
                    }

				}

				for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
						.peekFront(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
								.getPrevious()) {
                    if (spawnRequest.x >= this.playerPositionX && spawnRequest.x < this.playerPositionX + 8
                            && spawnRequest.y >= this.playerPositionY && spawnRequest.y < this.playerPositionY + 8
                            && spawnRequest.z == this.plane) {
                        spawnRequest.delayUntilRespawn = 0;
                    }
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 185) {
				final int interfaceId = this.inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelTypeDefault = 3;
				if (localPlayer.npcAppearance == null) {
                    RSInterface.cache[interfaceId].modelIdDefault = (localPlayer.bodyPartColour[0] << 25)
                            + (localPlayer.bodyPartColour[4] << 20) + (localPlayer.appearance[0] << 15)
                            + (localPlayer.appearance[8] << 10) + (localPlayer.appearance[11] << 5)
                            + localPlayer.appearance[1];
                } else {
                    RSInterface.cache[interfaceId].modelIdDefault = (int) (0x12345678L + localPlayer.npcAppearance.id);
                }
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 107) {
				this.cutsceneActive = false;
				for (int c = 0; c < 5; c++) {
					this.customCameraActive[c] = false;
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 72) {
				final int interfaceId = this.inStream.getUnsignedShort();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				for (int slot = 0; slot < rsInterface.inventoryItemId.length; slot++) {
					rsInterface.inventoryItemId[slot] = -1;
					rsInterface.inventoryItemId[slot] = 0;
				}

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 214) {
				this.ignoreCount = this.packetSize / 8;
				for (int p = 0; p < this.ignoreCount; p++) {
					this.ignoreListAsLongs[p] = this.inStream.getLong();
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 166) { // Spin camera
				this.cutsceneActive = true;
				this.anInt1098 = this.inStream.getUnsignedByte();
				this.anInt1099 = this.inStream.getUnsignedByte();
				this.anInt1100 = this.inStream.getUnsignedLEShort();
				this.anInt1101 = this.inStream.getUnsignedByte();
				this.anInt1102 = this.inStream.getUnsignedByte();
				if (this.anInt1102 >= 100) {
					this.cameraPositionX = this.anInt1098 * 128 + 64;
					this.cameraPositionY = this.anInt1099 * 128 + 64;
					this.cameraPositionZ = this.getFloorDrawHeight(this.plane, this.cameraPositionY, this.cameraPositionX) - this.anInt1100;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 134) {
				this.redrawTab = true;
				final int _skillId = this.inStream.getUnsignedByte();
				final int _skillExp = this.inStream.getMESInt();
				final int _skillLevel = this.inStream.getUnsignedByte();
				this.skillExperience[_skillId] = _skillExp;
				this.skillLevel[_skillId] = _skillLevel;
				this.skillMaxLevel[_skillId] = 1;
				for (int level = 0; level < 98; level++) {
                    if (_skillExp >= EXPERIENCE_TABLE[level]) {
						this.skillMaxLevel[_skillId] = level + 2;
                    }
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 71) {
				int sidebarId = this.inStream.getUnsignedLEShort();
				final int interfaceId = this.inStream.getUnsignedByteA();
				if (sidebarId == 0x00FFFF) {
                    sidebarId = -1;
                }
				this.tabInterfaceIDs[interfaceId] = sidebarId;
				this.redrawTab = true;
				this.drawTabIcons = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 74) {
				int songId = this.inStream.getUnsignedShort();
				if (songId == 0x00FFFF) {
                    songId = -1;
                }
				if (songId != this.currentSong && this.musicEnabled && !lowMemory && this.prevSong == 0) {
					this.nextSong = songId;
					this.songChanging = true;
					this.onDemandFetcher.request(2, this.nextSong);
				}
				this.currentSong = songId;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 121) {
				final int nextSong = this.inStream.getUnsignedShortA();
				final int previousSong = this.inStream.getUnsignedLEShortA();
				if (this.musicEnabled && !lowMemory) {
					this.nextSong = nextSong;
					this.songChanging = false;
					this.onDemandFetcher.request(2, this.nextSong);
					this.prevSong = previousSong;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 109) {
				this.logout();
				this.packetOpcode = -1;
				return false;
			}
			if (this.packetOpcode == 70) {
				final int x = this.inStream.getShort();
				final int y = this.inStream.getSignedLEShort();
				final int interfaceId = this.inStream.getUnsignedShort();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				rsInterface.x = x;
				rsInterface.y = y;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 73 || this.packetOpcode == 241) {

				// mapReset();
				int playerRegionX = this.regionX;
				int playerRegionY = this.regionY;
				if (this.packetOpcode == 73) {
					playerRegionX = this.inStream.getUnsignedLEShortA();
					playerRegionY = this.inStream.getUnsignedLEShort();
					this.loadGeneratedMap = false;
				}
				if (this.packetOpcode == 241) {
					playerRegionY = this.inStream.getUnsignedLEShortA();
					this.inStream.initBitAccess();
					for (int z = 0; z < 4; z++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								final int tileExists = this.inStream.readBits(1);
								if (tileExists == 1) {
									this.constructMapTiles[z][x][y] = this.inStream.readBits(26);
                                } else {
									this.constructMapTiles[z][x][y] = -1;
                                }
							}
						}
					}

					this.inStream.finishBitAccess();
					playerRegionX = this.inStream.getUnsignedLEShort();
					this.loadGeneratedMap = true;
				}
				if (this.regionX == playerRegionX && this.regionY == playerRegionY && this.loadingStage == 2) {
					this.packetOpcode = -1;
					return true;
				}
				this.regionX = playerRegionX;
				this.regionY = playerRegionY;
				this.baseX = (this.regionX - 6) * 8;
				this.baseY = (this.regionY - 6) * 8;
				this.inTutorialIsland = (this.regionX / 8 == 48 || this.regionX / 8 == 49) && this.regionY / 8 == 48;
				if (this.regionX / 8 == 48 && this.regionY / 8 == 148) {
					this.inTutorialIsland = true;
                }
				this.loadingStage = 1;
				this.loadRegionTime = System.currentTimeMillis();
				this.gameScreenImageProducer.initDrawingArea();
				this.fontPlain.drawCentredText("Loading - please wait.", 257, 151, 0);
				this.fontPlain.drawCentredText("Loading - please wait.", 256, 150, 0xFFFFFF);
				this.gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
				if (this.packetOpcode == 73) {
					int r = 0;
					for (int x = (this.regionX - 6) / 8; x <= (this.regionX + 6) / 8; x++) {
						for (int y = (this.regionY - 6) / 8; y <= (this.regionY + 6) / 8; y++) {
                            r++;
                        }
					}

					this.terrainData = new byte[r][];
					this.objectData = new byte[r][];
					this.mapCoordinates = new int[r];
					this.terrainDataIds = new int[r];
					this.objectDataIds = new int[r];
					r = 0;
					for (int x = (this.regionX - 6) / 8; x <= (this.regionX + 6) / 8; x++) {
						for (int y = (this.regionY - 6) / 8; y <= (this.regionY + 6) / 8; y++) {
							this.mapCoordinates[r] = (x << 8) + y;
							if (this.inTutorialIsland
									&& (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
								this.terrainDataIds[r] = -1;
								this.objectDataIds[r] = -1;
								r++;
							} else {
								final int terrainId = this.terrainDataIds[r] = this.onDemandFetcher.getMapId(0, x, y);
								if (terrainId != -1) {
									this.onDemandFetcher.request(3, terrainId);
                                }
								final int objectId = this.objectDataIds[r] = this.onDemandFetcher.getMapId(1, x, y);
								if (objectId != -1) {
									this.onDemandFetcher.request(3, objectId);
                                }
								r++;
							}
						}
					}
				}
				if (this.packetOpcode == 241) {
					int l16 = 0;
					final int[] ai = new int[676];
					for (int plane = 0; plane < 4; plane++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								final int k30 = this.constructMapTiles[plane][x][y];
								if (k30 != -1) {
									final int k31 = k30 >> 14 & 0x3FF;
									final int i32 = k30 >> 3 & 0x7FF;
									int k32 = (k31 / 8 << 8) + i32 / 8;
									for (int j33 = 0; j33 < l16; j33++) {
										if (ai[j33] != k32) {
                                            continue;
                                        }
										k32 = -1;
										break;
									}

									if (k32 != -1) {
                                        ai[l16++] = k32;
                                    }
								}
							}
						}
					}

					this.terrainData = new byte[l16][];
					this.objectData = new byte[l16][];
					this.mapCoordinates = new int[l16];
					this.terrainDataIds = new int[l16];
					this.objectDataIds = new int[l16];
					for (int r = 0; r < l16; r++) {
						final int coords = this.mapCoordinates[r] = ai[r];
						final int x = coords >> 8 & 0xFF;
						final int y = coords & 0xFF;
						final int terrainId = this.terrainDataIds[r] = this.onDemandFetcher.getMapId(0, x, y);
						if (terrainId != -1) {
							this.onDemandFetcher.request(3, terrainId);
                        }
						final int objectId = this.objectDataIds[r] = this.onDemandFetcher.getMapId(1, x, y);
						if (objectId != -1) {
							this.onDemandFetcher.request(3, objectId);
                        }
					}
				}
				final int _x = this.baseX - this.anInt1036;
				final int _y = this.baseY - this.anInt1037;
				this.anInt1036 = this.baseX;
				this.anInt1037 = this.baseY;
				for (int n = 0; n < 16384; n++) {
					final NPC npc = this.npcs[n];
					if (npc != null) {
						for (int waypoint = 0; waypoint < 10; waypoint++) {
							npc.waypointX[waypoint] -= _x;
							npc.waypointY[waypoint] -= _y;
						}

						npc.x -= _x * 128;
						npc.y -= _y * 128;
					}
				}
				for (int p = 0; p < this.MAX_ENTITY_COUNT; p++) {
					final Player player = this.players[p];
					if (player != null) {
						for (int waypoint = 0; waypoint < 10; waypoint++) {
							player.waypointX[waypoint] -= _x;
							player.waypointY[waypoint] -= _y;
						}

						player.x -= _x * 128;
						player.y -= _y * 128;
					}
				}
				this.loadingMap = true;
				byte currentPositionX = 0;
				byte boundaryPositionX = 104;
				byte incrementX = 1;
				if (_x < 0) {
					currentPositionX = 103;
					boundaryPositionX = -1;
					incrementX = -1;
				}
				byte currentPositionY = 0;
				byte boundaryPositionY = 104;
				byte incrementY = 1;
				if (_y < 0) {
					currentPositionY = 103;
					boundaryPositionY = -1;
					incrementY = -1;
				}
				for (int x = currentPositionX; x != boundaryPositionX; x += incrementX) {
					for (int y = currentPositionY; y != boundaryPositionY; y += incrementY) {
						final int x2 = x + _x;
						final int y2 = y + _y;
						for (int z = 0; z < 4; z++) {
                            if (x2 >= 0 && y2 >= 0 && x2 < 104 && y2 < 104) {
								this.groundArray[z][x][y] = this.groundArray[z][x2][y2];
                            } else {
								this.groundArray[z][x][y] = null;
                            }
                        }
					}
				}
				for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
						.peekFront(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
								.getPrevious()) {
					spawnRequest.x -= _x;
					spawnRequest.y -= _y;
					if (spawnRequest.x < 0 || spawnRequest.y < 0 || spawnRequest.x >= 104 || spawnRequest.y >= 104) {
                        spawnRequest.unlink();
                    }
				}

				if (this.destinationX != 0) {
					this.destinationX -= _x;
					this.destinationY -= _y;
				}
				this.cutsceneActive = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 208) {
				final int interfaceId = this.inStream.getSignedLEShort();
				if (interfaceId >= 0) {
					this.loadInterface(interfaceId);
                }
				this.walkableInterfaceId = interfaceId;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 99) {
				this.minimap.state = this.inStream.getUnsignedByte();
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 75) {
				final int modelId = this.inStream.getUnsignedShortA();
				final int interfaceId = this.inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelTypeDefault = 2;
				RSInterface.cache[interfaceId].modelIdDefault = modelId;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 114) {
				this.systemUpdateTime = this.inStream.getUnsignedShort() * 30;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 60) {
				this.playerPositionY = this.inStream.getUnsignedByte();
				this.playerPositionX = this.inStream.getUnsignedByteC();
				while (this.inStream.position < this.packetSize) {
					final int opcode = this.inStream.getUnsignedByte();
					this.parseGroupPacket(this.inStream, opcode);
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 35) {
				final int cameraId = this.inStream.getUnsignedByte();
				final int jitter = this.inStream.getUnsignedByte();
				final int amplitude = this.inStream.getUnsignedByte();
				final int frequency = this.inStream.getUnsignedByte();
				this.customCameraActive[cameraId] = true;
				this.cameraJitter[cameraId] = jitter;
				this.cameraAmplitude[cameraId] = amplitude;
				this.cameraFrequency[cameraId] = frequency;
				this.unknownCameraVariable[cameraId] = 0;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 174) {
				final int trackId = this.inStream.getUnsignedLEShort();
				final int loop = this.inStream.getUnsignedByte();
				final int delay = this.inStream.getUnsignedLEShort();
				if (this.effectsEnabled && !lowMemory && this.trackCount < 50) {
					this.trackIds[this.trackCount] = trackId;
					this.trackLoop[this.trackCount] = loop;
					this.trackDelay[this.trackCount] = delay + Effect.effectDelays[trackId];
					this.trackCount++;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 104) {
				final int actionId = this.inStream.getUnsignedByteC();
				final int actionAtTop = this.inStream.getUnsignedByteA();
				String actionText = this.inStream.getString();
				if (actionId >= 1 && actionId <= 5) {
					if (actionText.equalsIgnoreCase("null")) {
                        actionText = null;
                    }
					this.playerActionText[actionId - 1] = actionText;
					this.playerActionUnpinned[actionId - 1] = actionAtTop == 0;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 78) {
				this.destinationX = 0;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 253) {
				final String message = this.inStream.getString();
				if (message.endsWith(":tradereq:")) {
					final String name = message.substring(0, message.indexOf(":"));
					final long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < this.ignoreCount; p++) {
						if (this.ignoreListAsLongs[p] != nameAsLong) {
                            continue;
                        }
						ignored = true;
						break;
					}

					if (!ignored && this.inTutorial == 0) {
						this.pushMessage("wishes to trade with you.", 4, name);
                    }
				} else if (message.endsWith(":duelreq:")) {
					final String name = message.substring(0, message.indexOf(":"));
					final long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < this.ignoreCount; p++) {
						if (this.ignoreListAsLongs[p] != nameAsLong) {
                            continue;
                        }
						ignored = true;
						break;
					}

					if (!ignored && this.inTutorial == 0) {
						this.pushMessage("wishes to duel with you.", 8, name);
                    }
				} else if (message.endsWith(":chalreq:")) {
					final String name = message.substring(0, message.indexOf(":"));
					final long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < this.ignoreCount; p++) {
						if (this.ignoreListAsLongs[p] != nameAsLong) {
                            continue;
                        }
						ignored = true;
						break;
					}

					if (!ignored && this.inTutorial == 0) {
						final String text = message.substring(message.indexOf(":") + 1, message.length() - 9);
						this.pushMessage(text, 8, name);
					}
				} else {
					this.pushMessage(message, 0, "");
				}
				this.packetOpcode = -1;

				return true;
			}
			if (this.packetOpcode == 1) {
				for (int p = 0; p < this.players.length; p++) {
                    if (this.players[p] != null) {
						this.players[p].animation = -1;
                    }
                }

				for (int n = 0; n < this.npcs.length; n++) {
                    if (this.npcs[n] != null) {
						this.npcs[n].animation = -1;
                    }
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 50) {
				final long nameAsLong = this.inStream.getLong();
				final int worldId = this.inStream.getUnsignedByte();
				String name = TextClass.formatName(TextClass.longToName(nameAsLong));
				for (int friend = 0; friend < this.friendsCount; friend++) {
					if (nameAsLong != this.friendsListAsLongs[friend]) {
                        continue;
                    }
					if (this.friendsWorldIds[friend] != worldId) {
						this.friendsWorldIds[friend] = worldId;
						this.redrawTab = true;
						if (worldId > 0) {
							this.pushMessage(name + " has logged in.", 5, "");
                        }
						if (worldId == 0) {
							this.pushMessage(name + " has logged out.", 5, "");
                        }
					}
					name = null;
					break;
				}
				if (name != null && this.friendsCount < 200) {
					this.friendsListAsLongs[this.friendsCount] = nameAsLong;
					this.friendsList[this.friendsCount] = name;
					this.friendsWorldIds[this.friendsCount] = worldId;
					this.friendsCount++;
					this.redrawTab = true;
				}
				for (boolean orderComplete = false; !orderComplete;) {
					orderComplete = true;
					for (int friend = 0; friend < this.friendsCount - 1; friend++) {
                        if (this.friendsWorldIds[friend] != localWorldId && this.friendsWorldIds[friend + 1] == localWorldId
                                || this.friendsWorldIds[friend] == 0 && this.friendsWorldIds[friend + 1] != 0) {
                            final int tempWorld = this.friendsWorldIds[friend];
							this.friendsWorldIds[friend] = this.friendsWorldIds[friend + 1];
							this.friendsWorldIds[friend + 1] = tempWorld;
                            final String tempName = this.friendsList[friend];
							this.friendsList[friend] = this.friendsList[friend + 1];
							this.friendsList[friend + 1] = tempName;
                            final long tempLong = this.friendsListAsLongs[friend];
							this.friendsListAsLongs[friend] = this.friendsListAsLongs[friend + 1];
							this.friendsListAsLongs[friend + 1] = tempLong;
							this.redrawTab = true;
                            orderComplete = false;
                        }
                    }
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 110) {
				if (this.currentTabId == 12) {
					this.redrawTab = true;
                }
				this.playerEnergy = this.inStream.getUnsignedByte();
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 254) {
				this.hintIconType = this.inStream.getUnsignedByte();
				if (this.hintIconType == 1) {
					this.hintIconNpcId = this.inStream.getUnsignedLEShort();
                }
				if (this.hintIconType >= 2 && this.hintIconType <= 6) {
					if (this.hintIconType == 2) {
						this.hintIconDrawTileX = 64;
						this.hintIconDrawTileY = 64;
					}
					if (this.hintIconType == 3) {
						this.hintIconDrawTileX = 0;
						this.hintIconDrawTileY = 64;
					}
					if (this.hintIconType == 4) {
						this.hintIconDrawTileX = 128;
						this.hintIconDrawTileY = 64;
					}
					if (this.hintIconType == 5) {
						this.hintIconDrawTileX = 64;
						this.hintIconDrawTileY = 0;
					}
					if (this.hintIconType == 6) {
						this.hintIconDrawTileX = 64;
						this.hintIconDrawTileY = 128;
					}
					this.hintIconType = 2;
					this.hintIconX = this.inStream.getUnsignedLEShort();
					this.hintIconY = this.inStream.getUnsignedLEShort();
					this.hintIconDrawHeight = this.inStream.getUnsignedByte();
				}
				if (this.hintIconType == 10) {
					this.hintIconPlayerId = this.inStream.getUnsignedLEShort();
                }
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 248) {
				final int interfaceId = this.inStream.getUnsignedLEShortA();
				final int inventoryInterfaceId = this.inStream.getUnsignedLEShort();
				if (this.chatboxInterfaceId != -1) {
					this.chatboxInterfaceId = -1;
					this.redrawChatbox = true;
				}
				if (this.inputDialogState != 0) {
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
				this.openInterfaceId = interfaceId;
				this.inventoryOverlayInterfaceID = inventoryInterfaceId;
				this.redrawTab = true;
				this.drawTabIcons = true;
				this.continuedDialogue = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 79) {
				final int interfaceId = this.inStream.getUnsignedShort();
				int scrollPosition = this.inStream.getUnsignedLEShortA();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				if (rsInterface != null && rsInterface.type == 0) {
					if (scrollPosition < 0) {
                        scrollPosition = 0;
                    }
					if (scrollPosition > rsInterface.scrollMax - rsInterface.height) {
                        scrollPosition = rsInterface.scrollMax - rsInterface.height;
                    }
					rsInterface.scrollPosition = scrollPosition;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 68) {
				for (int setting = 0; setting < this.interfaceSettings.length; setting++) {
                    if (this.interfaceSettings[setting] != this.defaultSettings[setting]) {
						this.interfaceSettings[setting] = this.defaultSettings[setting];
						this.handleInterfaceSetting(setting);
						this.redrawTab = true;
                    }
                }

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 196) {
				final long nameAsLong = this.inStream.getLong();
				final int messageId = this.inStream.getInt();
				final int playerRights = this.inStream.getUnsignedByte();
				boolean ignored = false;
				for (int message = 0; message < 100; message++) {
					if (this.privateMessages[message] != messageId) {
                        continue;
                    }
					ignored = true;
					break;
				}
				if (playerRights <= 1) {
					for (int p = 0; p < this.ignoreCount; p++) {
						if (this.ignoreListAsLongs[p] != nameAsLong) {
                            continue;
                        }
						ignored = true;
						break;
					}
				}
				if (!ignored && this.inTutorial == 0) {
                    try {
						this.privateMessages[this.privateMessagePointer] = messageId;
						this.privateMessagePointer = (this.privateMessagePointer + 1) % 100;
                        String message = TextInput.readFromStream(this.packetSize - 13, this.inStream);
                        if (playerRights != 3) {
                            message = Censor.censor(message);
                        }
                        if (playerRights == 2 || playerRights == 3) {
							this.pushMessage(message, 7, "@cr2@" + TextClass.formatName(TextClass.longToName(nameAsLong)));
                        } else if (playerRights == 1) {
							this.pushMessage(message, 7, "@cr1@" + TextClass.formatName(TextClass.longToName(nameAsLong)));
                        } else {
							this.pushMessage(message, 3, TextClass.formatName(TextClass.longToName(nameAsLong)));
                        }
                    } catch (final Exception exception1) {
                        signlink.reporterror("cde1");
                    }
                }
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 85) {
				this.playerPositionY = this.inStream.getUnsignedByteC();
				this.playerPositionX = this.inStream.getUnsignedByteC();
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 24) {
				this.flashingSidebar = this.inStream.getUnsignedByteS();
				if (this.flashingSidebar == this.currentTabId) {
					if (this.flashingSidebar == 3) {
						this.currentTabId = 1;
                    } else {
						this.currentTabId = 3;
                    }
					this.redrawTab = true;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 246) {
				final int interfaceId = this.inStream.getUnsignedShort();
				final int itemModelZoom = this.inStream.getUnsignedLEShort();
				final int itemId = this.inStream.getUnsignedLEShort();
				if (itemId == 0x00FFFF) {
					RSInterface.cache[interfaceId].modelTypeDefault = 0;
					this.packetOpcode = -1;
					return true;
				} else {
					final ItemDefinition itemDef = ItemDefinition.getDefinition(itemId);
					RSInterface.cache[interfaceId].modelTypeDefault = 4;
					RSInterface.cache[interfaceId].modelIdDefault = itemId;
					RSInterface.cache[interfaceId].modelRotationX = itemDef.modelRotationX;
					RSInterface.cache[interfaceId].modelRotationY = itemDef.modelRotationY;
					RSInterface.cache[interfaceId].modelZoom = (itemDef.modelZoom * 100) / itemModelZoom;
					this.packetOpcode = -1;
					return true;
				}
			}
			if (this.packetOpcode == 171) {
				final boolean hiddenUntilHovered = this.inStream.getUnsignedByte() == 1;
				final int interfaceId = this.inStream.getUnsignedLEShort();
				RSInterface.cache[interfaceId].hoverOnly = hiddenUntilHovered;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 142) {
				final int interfaceId = this.inStream.getUnsignedShort();
				this.loadInterface(interfaceId);
				if (this.chatboxInterfaceId != -1) {
					this.chatboxInterfaceId = -1;
					this.redrawChatbox = true;
				}
				if (this.inputDialogState != 0) {
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
				this.inventoryOverlayInterfaceID = interfaceId;
				this.redrawTab = true;
				this.drawTabIcons = true;
				this.openInterfaceId = -1;
				this.continuedDialogue = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 126) {
				final String text = this.inStream.getString();
				final int interfaceId = this.inStream.getUnsignedLEShortA();
				RSInterface.cache[interfaceId].textDefault = text;
				if (RSInterface.cache[interfaceId].parentID == this.tabInterfaceIDs[this.currentTabId]) {
					this.redrawTab = true;
                }
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 206) {
				this.publicChatMode = this.inStream.getUnsignedByte();
				this.privateChatMode = this.inStream.getUnsignedByte();
				this.tradeMode = this.inStream.getUnsignedByte();
				this.updateChatSettings = true;
				this.redrawChatbox = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 240) {
				if (this.currentTabId == 12) {
					this.redrawTab = true;
                }
				this.playerWeight = this.inStream.getShort();
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 8) {
				final int interfaceId = this.inStream.getUnsignedShortA();
				final int interfaceModelId = this.inStream.getUnsignedLEShort();
				RSInterface.cache[interfaceId].modelTypeDefault = 1;
				RSInterface.cache[interfaceId].modelIdDefault = interfaceModelId;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 122) {
				final int interfaceId = this.inStream.getUnsignedShortA();
				final int rgb = this.inStream.getUnsignedShortA();
				final int r = rgb >> 10 & 0x1F;
				final int g = rgb >> 5 & 0x1F;
				final int b = rgb & 0x1F;
				RSInterface.cache[interfaceId].colourDefault = (r << 19) + (g << 11) + (b << 3);
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 53) {
				this.redrawTab = true;
				final int interfaceId = this.inStream.getUnsignedLEShort();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				final int itemCount = this.inStream.getUnsignedLEShort();

				for (int item = 0; item < itemCount; item++) {
					int stackSize = this.inStream.getUnsignedByte();

					if (stackSize == 255) {
						stackSize = this.inStream.getMEBInt();
					}

					rsInterface.inventoryItemId[item] = this.inStream.getUnsignedShortA();
					rsInterface.inventoryStackSize[item] = stackSize;
				}

				for (int i = itemCount; i < rsInterface.inventoryItemId.length; i++) {
					rsInterface.inventoryItemId[i] = 0;
					rsInterface.inventoryStackSize[i] = 0;
				}

				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 230) {
				final int modelZoom = this.inStream.getUnsignedLEShortA();
				final int interfaceId = this.inStream.getUnsignedLEShort();
				final int modelRotationX = this.inStream.getUnsignedLEShort();
				final int modelRotationY = this.inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelRotationX = modelRotationX;
				RSInterface.cache[interfaceId].modelRotationY = modelRotationY;
				RSInterface.cache[interfaceId].modelZoom = modelZoom;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 221) {
				this.friendListStatus = this.inStream.getUnsignedByte();
				this.redrawTab = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 177) {
				this.cutsceneActive = true;
				this.anInt995 = this.inStream.getUnsignedByte();
				this.anInt996 = this.inStream.getUnsignedByte();
				this.cameraOffsetZ = this.inStream.getUnsignedLEShort();
				this.anInt998 = this.inStream.getUnsignedByte();
				this.anInt999 = this.inStream.getUnsignedByte();
				if (this.anInt999 >= 100) {
					final int x = this.anInt995 * 128 + 64;
					final int y = this.anInt996 * 128 + 64;
					final int z = this.getFloorDrawHeight(this.plane, y, x) - this.cameraOffsetZ;
					final int distanceX = x - this.cameraPositionX;
					final int distanceZ = z - this.cameraPositionZ;
					final int distanceY = y - this.cameraPositionY;
					final int distanceScalar = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
					this.cameraVerticalRotation = (int) (Math.atan2(distanceZ, distanceScalar) * 325.94900000000001D)
							& 0x7FF;
					this.cameraHorizontalRotation = (int) (Math.atan2(distanceX, distanceY) * -325.94900000000001D) & 0x7FF;
					if (this.cameraVerticalRotation < 128) {
						this.cameraVerticalRotation = 128;
                    }
					if (this.cameraVerticalRotation > 383) {
						this.cameraVerticalRotation = 383;
                    }
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 249) {
				this.membershipStatus = this.inStream.getUnsignedByteA();
				this.playerListId = this.inStream.getUnsignedShortA();
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 65) {
				this.updateNPCs(this.inStream, this.packetSize);
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 27) {
				this.messagePromptRaised = false;
				this.inputDialogState = 1;
				this.amountOrNameInput = "";
				this.redrawChatbox = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 187) {
				this.messagePromptRaised = false;
				this.inputDialogState = 2;
				this.amountOrNameInput = "";
				this.redrawChatbox = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 97) {
				final int interfaceId = this.inStream.getUnsignedLEShort();
				this.loadInterface(interfaceId);
				if (this.inventoryOverlayInterfaceID != -1) {
					this.inventoryOverlayInterfaceID = -1;
					this.redrawTab = true;
					this.drawTabIcons = true;
				}
				if (this.chatboxInterfaceId != -1) {
					this.chatboxInterfaceId = -1;
					this.redrawChatbox = true;
				}
				if (this.inputDialogState != 0) {
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
				this.openInterfaceId = interfaceId;
				this.continuedDialogue = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 218) {
				this.dialogID = this.inStream.getSignedLEShortA();
				this.redrawChatbox = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 87) {
				final int settingId = this.inStream.getUnsignedShort();
				final int settingValue = this.inStream.getMESInt();
				this.defaultSettings[settingId] = settingValue;
				if (this.interfaceSettings[settingId] != settingValue) {
					this.interfaceSettings[settingId] = settingValue;
					this.handleInterfaceSetting(settingId);
					this.redrawTab = true;
					if (this.dialogID != -1) {
						this.redrawChatbox = true;
                    }
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 36) {
				final int settingId = this.inStream.getUnsignedShort();
				final byte settingValue = this.inStream.get();
				this.defaultSettings[settingId] = settingValue;
				if (this.interfaceSettings[settingId] != settingValue) {
					this.interfaceSettings[settingId] = settingValue;
					this.handleInterfaceSetting(settingId);
					this.redrawTab = true;
					if (this.dialogID != -1) {
						this.redrawChatbox = true;
                    }
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 61) {
				this.multiCombatZone = this.inStream.getUnsignedByte() == 1;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 200) {
				final int interfaceId = this.inStream.getUnsignedLEShort();
				final int animationId = this.inStream.getShort();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				rsInterface.animationIdDefault = animationId;
				if (animationId == -1) {
					rsInterface.animationFrame = 0;
					rsInterface.animationDuration = 0;
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 219) {
				if (this.inventoryOverlayInterfaceID != -1) {
					this.inventoryOverlayInterfaceID = -1;
					this.redrawTab = true;
					this.drawTabIcons = true;
				}
				if (this.chatboxInterfaceId != -1) {
					this.chatboxInterfaceId = -1;
					this.redrawChatbox = true;
				}
				if (this.inputDialogState != 0) {
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
				this.openInterfaceId = -1;
				this.continuedDialogue = false;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 34) {
				this.redrawTab = true;
				final int interfaceId = this.inStream.getUnsignedLEShort();
				final RSInterface rsInterface = RSInterface.cache[interfaceId];
				while (this.inStream.position < this.packetSize) {
					final int itemSlot = this.inStream.getSmartB();
					final int itemId = this.inStream.getUnsignedLEShort();
					int itemAmount = this.inStream.getUnsignedByte();
					if (itemAmount == 255) {
                        itemAmount = this.inStream.getInt();
                    }
					if (itemSlot >= 0 && itemSlot < rsInterface.inventoryItemId.length) {
						rsInterface.inventoryItemId[itemSlot] = itemId;
						rsInterface.inventoryStackSize[itemSlot] = itemAmount;
					}
				}
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 105 || this.packetOpcode == 84 || this.packetOpcode == 147 || this.packetOpcode == 215
					|| this.packetOpcode == 4 || this.packetOpcode == 117 || this.packetOpcode == 156 || this.packetOpcode == 44
					|| this.packetOpcode == 160 || this.packetOpcode == 101 || this.packetOpcode == 151) {
				this.parseGroupPacket(this.inStream, this.packetOpcode);
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 106) {
				this.currentTabId = this.inStream.getUnsignedByteC();
				this.redrawTab = true;
				this.drawTabIcons = true;
				this.packetOpcode = -1;
				return true;
			}
			if (this.packetOpcode == 164) {
				final int interfaceId = this.inStream.getUnsignedShort();
				this.loadInterface(interfaceId);
				if (this.inventoryOverlayInterfaceID != -1) {
					this.inventoryOverlayInterfaceID = -1;
					this.redrawTab = true;
					this.drawTabIcons = true;
				}
				this.chatboxInterfaceId = interfaceId;
				this.redrawChatbox = true;
				this.openInterfaceId = -1;
				this.continuedDialogue = false;
				this.packetOpcode = -1;
				return true;
			}
			signlink.reporterror("T1 - " + this.packetOpcode + "," + this.packetSize + " - " + this.secondMostRecentOpcode + ","
					+ this.thirdMostRecentOpcode);
			this.logout();
		} catch (final IOException _ex) {
			this.dropClient();
		} catch (final Exception exception) {
			StringBuilder s2 = new StringBuilder("T2 - " + this.packetOpcode + "," + this.secondMostRecentOpcode + "," + this.thirdMostRecentOpcode + " - "
					+ this.packetSize + "," + (this.baseX + localPlayer.waypointX[0]) + "," + (this.baseY + localPlayer.waypointY[0])
					+ " - ");
			for (int j15 = 0; j15 < this.packetSize && j15 < 50; j15++) {
                s2.append(this.inStream.buffer[j15]).append(",");
            }

			signlink.reporterror(s2.toString());
			this.logout();
		}
		return true;
	}

	private void handleInterfaceSetting(final int s) {
		final int opcode = Varp.values[s].type;
		if (opcode == 0) {
            return;
        }
		final int setting = this.interfaceSettings[s];
		if (opcode == 1) {
			// Brightness
			if (setting == 1) {
                Rasterizer.calculatePalette(0.90000000000000002D);
            }
			if (setting == 2) {
                Rasterizer.calculatePalette(0.80000000000000004D);
            }
			if (setting == 3) {
                Rasterizer.calculatePalette(0.69999999999999996D);
            }
			if (setting == 4) {
                Rasterizer.calculatePalette(0.59999999999999998D);
            }
			ItemDefinition.spriteCache.clear();
			this.titleScreen.welcomeScreenRaised = true;
		}
		if (opcode == 3) {
			// Music volume
			final boolean originalMusicEnabled = this.musicEnabled;
			if (setting == 0) {
				this.adjustVolume(this.musicEnabled, 0);
				this.musicEnabled = true;
			}
			if (setting == 1) {
				this.adjustVolume(this.musicEnabled, -400);
				this.musicEnabled = true;
			}
			if (setting == 2) {
				this.adjustVolume(this.musicEnabled, -800);
				this.musicEnabled = true;
			}
			if (setting == 3) {
				this.adjustVolume(this.musicEnabled, -1200);
				this.musicEnabled = true;
			}
			if (setting == 4) {
				this.musicEnabled = false;
            }
			if (this.musicEnabled != originalMusicEnabled && !lowMemory) {
				if (this.musicEnabled) {
					this.nextSong = this.currentSong;
					this.songChanging = true;
					this.onDemandFetcher.request(2, this.nextSong);
				} else {
					this.stopMidi();
				}
				this.prevSong = 0;
			}
		}
		if (opcode == 4) {

			if (setting == 0) {
				this.effectsEnabled = true;
				this.setWaveVolume(0);
			}
			if (setting == 1) {
				this.effectsEnabled = true;
				this.setWaveVolume(-400);
			}
			if (setting == 2) {
				this.effectsEnabled = true;
				this.setWaveVolume(-800);
			}
			if (setting == 3) {
				this.effectsEnabled = true;
				this.setWaveVolume(-1200);
			}
			if (setting == 4) {
				this.effectsEnabled = false;
            }
		}
		if (opcode == 5) {
			this.oneMouseButton = setting;
        }
		if (opcode == 6) {
			this.chatEffectsDisabled = setting;
        }
		if (opcode == 8) {
			this.splitPrivateChat = setting;
			this.redrawChatbox = true;
		}
		if (opcode == 9) {
			this.bankInsertMode = setting;
        }
	}

	private void handleMusic() {
		for (int track = 0; track < this.trackCount; track++) {
            if (this.trackDelay[track] <= 0) {
                boolean moveToNextSong = false;
                try {
                    if (this.trackIds[track] == this.currentTrackId && this.trackLoop[track] == this.currentTrackLoop) {
                        if (!this.replayWave()) {
                            moveToNextSong = true;
                        }
                    } else {
                        final Buffer stream = Effect.data(this.trackLoop[track], this.trackIds[track]);
                        if (System.currentTimeMillis() + stream.position / 22 > this.songStartTime + this.songStartOffset / 22) {
							this.songStartOffset = stream.position;
							this.songStartTime = System.currentTimeMillis();
                            if (this.saveWave(stream.buffer, stream.position)) {
								this.currentTrackId = this.trackIds[track];
								this.currentTrackLoop = this.trackLoop[track];
                            } else {
                                moveToNextSong = true;
                            }
                        }
                    }
                } catch (final Exception exception) {
                }
                if (!moveToNextSong || this.trackDelay[track] == -5) {
					this.trackCount--;
                    for (int _track = track; _track < this.trackCount; _track++) {
						this.trackIds[_track] = this.trackIds[_track + 1];
						this.trackLoop[_track] = this.trackLoop[_track + 1];
						this.trackDelay[_track] = this.trackDelay[_track + 1];
                    }

                    track--;
                } else {
					this.trackDelay[track] = -5;
                }
            } else {
				this.trackDelay[track]--;
            }
        }

		if (this.prevSong > 0) {
			this.prevSong -= 20;
			if (this.prevSong < 0) {
				this.prevSong = 0;
            }
			if (this.prevSong == 0 && this.musicEnabled && !lowMemory) {
				this.nextSong = this.currentSong;
				this.songChanging = true;
				this.onDemandFetcher.request(2, this.nextSong);
			}
		}
	}

	@Override
	public void init() {
		localWorldId = Integer.parseInt(this.getParameter("nodeid"));
		portOffset = Integer.parseInt(this.getParameter("portoff"));
		final String lowMemory = this.getParameter("lowmem");
		if (lowMemory != null && lowMemory.equals("1")) {
            setLowMemory();
        } else {
            setHighMem();
        }
		final String freeWorld = this.getParameter("free");
		membersWorld = !(freeWorld != null && freeWorld.equals("1"));
		this.initClientFrame(765, 503);
	}

	private int initialiseRegionLoading() {
		for (int t = 0; t < this.terrainData.length; t++) {
			if (this.terrainData[t] == null && this.terrainDataIds[t] != -1) {
                return -1;
            }
			if (this.objectData[t] == null && this.objectDataIds[t] != -1) {
                return -2;
            }
		}

		boolean regionsCached = true;
		for (int region = 0; region < this.terrainData.length; region++) {
			final byte[] objects = this.objectData[region];
			if (objects != null) {
				int blockX = (this.mapCoordinates[region] >> 8) * 64 - this.baseX;
				int blockY = (this.mapCoordinates[region] & 0xFF) * 64 - this.baseY;
				if (this.loadGeneratedMap) {
					blockX = 10;
					blockY = 10;
				}
				regionsCached &= Region.regionCached(blockX, blockY, objects);
			}
		}

		if (!regionsCached) {
            return -3;
        }
		if (this.loadingMap) {
			return -4;
		} else {
			this.loadingStage = 2;
			Region.plane = this.plane;
			this.loadRegion();
			this.stream.putOpcode(121);
			return 0;
		}
	}

	private String interfaceIntToString(final int value) {
		if (value < 999999999) {
            return String.valueOf(value);
        } else {
            return "*";
        }
	}

	private boolean interfaceIsActive(final RSInterface rsInterface) {
		if (rsInterface.conditionType == null) {
            return false;
        }
		for (int c = 0; c < rsInterface.conditionType.length; c++) {
			final int opcode = this.parseInterfaceOpcode(rsInterface, c);
			final int value = rsInterface.conditionValue[c];
			if (rsInterface.conditionType[c] == 2) {
				if (opcode >= value) {
                    return false;
                }
			} else if (rsInterface.conditionType[c] == 3) {
				if (opcode <= value) {
                    return false;
                }
			} else if (rsInterface.conditionType[c] == 4) {
				if (opcode == value) {
                    return false;
                }
			} else if (opcode != value) {
                return false;
            }
		}

		return true;
	}

	private boolean isFriendOrSelf(final String name) {
		if (name == null) {
            return false;
        }
		for (int i = 0; i < this.friendsCount; i++) {
            if (name.equalsIgnoreCase(this.friendsList[i])) {
                return true;
            }
        }
		return name.equalsIgnoreCase(localPlayer.name);
	}

	private void loadError() {
		final String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			this.getAppletContext().showDocument(new URL(this.getCodeBase(), "loaderror_" + s + ".html"));
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
		do {
            try {
                Thread.sleep(1000L);
            } catch (final Exception _ex) {
            }
        }
		while (true);
	}

	private void loadingStages() {
		if (lowMemory && this.loadingStage == 2 && Region.plane != this.plane) {
			this.gameScreenImageProducer.initDrawingArea();
			this.fontPlain.drawCentredText("Loading - please wait.", 257, 151, 0);
			this.fontPlain.drawCentredText("Loading - please wait.", 256, 150, 0xFFFFFF);
			this.gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
			this.loadingStage = 1;
			this.loadRegionTime = System.currentTimeMillis();
		}
		if (this.loadingStage == 1) {
			final int successful = this.initialiseRegionLoading();
			if (successful != 0 && System.currentTimeMillis() - this.loadRegionTime > 360000L) {
				signlink.reporterror(this.enteredUsername + " glcfb " + this.serverSessionKey + "," + successful + "," + lowMemory
						+ "," + this.caches[0] + "," + this.onDemandFetcher.immediateRequestCount() + "," + this.plane + "," + this.regionX
						+ "," + this.regionY);
				this.loadRegionTime = System.currentTimeMillis();
			}
		}
		if (this.loadingStage == 2 && this.plane != this.lastRegionId) {
			this.lastRegionId = this.plane;
			this.minimap.render(this.worldController, this.plane, this.tileFlags, this.currentCollisionMap);

			this.gameScreenImageProducer.initDrawingArea();
		}
	}

	private void loadInterface(final int i) {
		final RSInterface rsInterface = RSInterface.cache[i];
		for (int j = 0; j < rsInterface.children.length; j++) {
			if (rsInterface.children[j] == -1) {
                break;
            }
			final RSInterface child = RSInterface.cache[rsInterface.children[j]];
			if (child.type == 1) {
				this.loadInterface(child.id);
            }
			child.animationFrame = 0;
			child.animationDuration = 0;
		}
	}

	private void loadRegion() {
		try {
			this.lastRegionId = -1;
			this.stationaryGraphicQueue.clear();
			this.projectileQueue.clear();
			Rasterizer.clearTextureCache();
			this.resetModelCaches();
			this.worldController.initToNull();
			System.gc();
			for (int z = 0; z < 4; z++) {
				this.currentCollisionMap[z].reset();
            }

			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 104; x++) {
					for (int y = 0; y < 104; y++) {
						this.tileFlags[z][x][y] = 0;
                    }

				}

			}

			final Region objectManager = new Region(this.tileFlags, this.intGroundArray);
			final int dataLength = this.terrainData.length;
			this.stream.putOpcode(0);
			if (!this.loadGeneratedMap) {
				for (int pointer = 0; pointer < dataLength; pointer++) {
					final int offsetX = (this.mapCoordinates[pointer] >> 8) * 64 - this.baseX;
					final int offsetY = (this.mapCoordinates[pointer] & 0xFF) * 64 - this.baseY;
					final byte[] data = this.terrainData[pointer];
					if (data != null) {
                        objectManager.loadTerrainBlock(data, offsetY, offsetX, (this.regionX - 6) * 8, (this.regionY - 6) * 8,
								this.currentCollisionMap);
                    }
				}

				for (int pointer = 0; pointer < dataLength; pointer++) {
					final int offsetX = (this.mapCoordinates[pointer] >> 8) * 64 - this.baseX;
					final int offsetY = (this.mapCoordinates[pointer] & 0xFF) * 64 - this.baseY;
					final byte[] data = this.terrainData[pointer];
					if (data == null && this.regionY < 800) {
                        objectManager.initiateVertexHeights(offsetY, 64, 64, offsetX);
                    }
				}
				this.stream.putOpcode(0);
				for (int region = 0; region < dataLength; region++) {
					final byte[] data = this.objectData[region];
					if (data != null) {
						final int offsetX = (this.mapCoordinates[region] >> 8) * 64 - this.baseX;
						final int offsetY = (this.mapCoordinates[region] & 0xFF) * 64 - this.baseY;
						objectManager.loadObjectBlock(offsetX, this.currentCollisionMap, offsetY, this.worldController, data);
					}
				}

			}
			if (this.loadGeneratedMap) {
				for (int z = 0; z < 4; z++) {
					for (int x = 0; x < 13; x++) {
						for (int y = 0; y < 13; y++) {
							final int data = this.constructMapTiles[z][x][y];
							if (data != -1) {
								final int tileZ = data >> 24 & 3;
								final int tileRotation = data >> 1 & 3;
								final int tileX = data >> 14 & 0x3FF;
								final int tileY = data >> 3 & 0x7FF;
								final int tileCoordinates = (tileX / 8 << 8) + tileY / 8;
								for (int pointer = 0; pointer < this.mapCoordinates.length; pointer++) {
									if (this.mapCoordinates[pointer] != tileCoordinates || this.terrainData[pointer] == null) {
                                        continue;
                                    }
									objectManager.loadTerrainSubblock(tileZ, tileRotation, this.currentCollisionMap, x * 8,
											(tileX & 7) * 8, this.terrainData[pointer], (tileY & 7) * 8, z, y * 8);
									break;
								}

							}
						}

					}

				}

				for (int x = 0; x < 13; x++) {
					for (int y = 0; y < 13; y++) {
						final int displayMap = this.constructMapTiles[0][x][y];
						if (displayMap == -1) {
                            objectManager.initiateVertexHeights(y * 8, 8, 8, x * 8);
                        }
					}

				}

				this.stream.putOpcode(0);
				for (int z = 0; z < 4; z++) {
					for (int x = 0; x < 13; x++) {
						for (int y = 0; y < 13; y++) {
							final int bits = this.constructMapTiles[z][x][y];
							if (bits != -1) {
								final int tileZ = bits >> 24 & 3;
								final int tileRotation = bits >> 1 & 3;
								final int tileX = bits >> 14 & 0x3FF;
								final int tileY = bits >> 3 & 0x7FF;
								final int tileCoorindates = (tileX / 8 << 8) + tileY / 8;
								for (int pointer = 0; pointer < this.mapCoordinates.length; pointer++) {
									if (this.mapCoordinates[pointer] != tileCoorindates || this.objectData[pointer] == null) {
                                        continue;
                                    }
									objectManager.loadObjectSubblock(this.currentCollisionMap, this.worldController, tileZ, x * 8,
											(tileY & 7) * 8, z, this.objectData[pointer], (tileX & 7) * 8, tileRotation,
											y * 8);
									break;
								}

							}
						}

					}

				}

			}
			this.stream.putOpcode(0);
			objectManager.createRegion(this.currentCollisionMap, this.worldController);
			this.gameScreenImageProducer.initDrawingArea();
			this.stream.putOpcode(0);
			int z = Region.lowestPlane;
			if (z > this.plane) {
                z = this.plane;
            }
			if (z < this.plane - 1) {
                z = this.plane - 1;
            }
			if (lowMemory) {
				this.worldController.setHeightLevel(Region.lowestPlane);
            } else {
				this.worldController.setHeightLevel(0);
            }
			for (int x = 0; x < 104; x++) {
				for (int y = 0; y < 104; y++) {
					this.spawnGroundItem(x, y);
                }

			}

			loadedRegions++;
			if (loadedRegions > 98) {
				loadedRegions = 0;
				this.stream.putOpcode(150);
			}
			this.clearObjectSpawnRequests();
		} catch (final Exception exception) {
		}
		GameObjectDefinition.modelCache.clear();
		if (super.gameFrame != null) {
			this.stream.putOpcode(210);
			this.stream.putInt(0x3F008EDD);
		}
		if (lowMemory && signlink.cache_dat != null) {
			final int modelCount = this.onDemandFetcher.fileCount(0);
			for (int model = 0; model < modelCount; model++) {
				final int modelIndex = this.onDemandFetcher.getModelId(model);
				if ((modelIndex & 0x79) == 0) {
                    Model.resetModel(model);
                }
			}

		}
		System.gc();
		Rasterizer.resetTextures();
		this.onDemandFetcher.clearPassiveRequests();
		int x1 = (this.regionX - 6) / 8 - 1;
		int x2 = (this.regionX + 6) / 8 + 1;
		int y1 = (this.regionY - 6) / 8 - 1;
		int y2 = (this.regionY + 6) / 8 + 1;
		if (this.inTutorialIsland) {
			x1 = 49;
			x2 = 50;
			y1 = 49;
			y2 = 50;
		}
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
                if (x == x1 || x == x2 || y == y1 || y == y2) {
                    final int mapIndex1 = this.onDemandFetcher.getMapId(0, x, y);
                    if (mapIndex1 != -1) {
						this.onDemandFetcher.passiveRequest(mapIndex1, 3);
                    }
                    final int mapIndex2 = this.onDemandFetcher.getMapId(1, x, y);
                    if (mapIndex2 != -1) {
						this.onDemandFetcher.passiveRequest(mapIndex2, 3);
                    }
                }
            }

		}

	}

	private void loadTitleScreen() {
		this.drawLoadingText(10, "Connecting to fileserver");
		if (!this.titleScreen.currentlyDrawingFlames) {
			this.drawFlames = true;
			this.titleScreen.currentlyDrawingFlames = true;
			this.startRunnable(this, 2);
		}
	}

	private void login(final String playerUsername, final String playerPassword, final boolean recoveredConnection) {
		signlink.errorname = playerUsername;
		try {
			if (!recoveredConnection) {
				this.loginMessage1 = "";
				this.loginMessage2 = "Connecting to server...";
				this.setupLoginScreen();
				this.titleScreen.drawLoginScreen(super.gameGraphics, true, this.loginScreenState, this.onDemandFetcher.statusString, this.loginMessage1, this.loginMessage2, this.enteredUsername, this.enteredPassword, tick, this.loginScreenFocus);
			}
			this.socket = new RSSocket(this, this.openSocket(43594 + portOffset));
			final long nameLong = TextClass.nameToLong(playerUsername);
			final int nameHash = (int) (nameLong >> 16 & 31L);
			this.stream.position = 0;
			this.stream.put(14);
			this.stream.put(nameHash);
			this.socket.write(2, this.stream.buffer);
			for (int ignoredByte = 0; ignoredByte < 8; ignoredByte++) {
				this.socket.read();
            }

			int responseCode = this.socket.read();
			final int initialResponseCode = responseCode;
			if (responseCode == 0) {
				this.socket.read(this.inStream.buffer, 8);
				this.inStream.position = 0;
				this.serverSessionKey = this.inStream.getLong();
				final int[] seed = new int[4];
				seed[0] = (int) (Math.random() * 99999999D);
				seed[1] = (int) (Math.random() * 99999999D);
				seed[2] = (int) (this.serverSessionKey >> 32);
				seed[3] = (int) this.serverSessionKey;
				this.stream.position = 0;
				this.stream.put(10);
				this.stream.putInt(seed[0]);
				this.stream.putInt(seed[1]);
				this.stream.putInt(seed[2]);
				this.stream.putInt(seed[3]);
				this.stream.putInt(signlink.uid);
				this.stream.putString(playerUsername);
				this.stream.putString(playerPassword);
				this.stream.generateKeys();
				this.loginStream.position = 0;
				if (recoveredConnection) {
					this.loginStream.put(18);
                } else {
					this.loginStream.put(16);
                }
				this.loginStream.put(this.stream.position + 40);
				this.loginStream.put(255);
				this.loginStream.putShort(317);
				this.loginStream.put(lowMemory ? 1 : 0);
				for (int crc = 0; crc < 9; crc++) {
					this.loginStream.putInt(this.expectedCRCs[crc]);
                }

				this.loginStream.putBytes(this.stream.buffer, this.stream.position, 0);
				this.stream.encryptor = new ISAACRandomGenerator(seed);
				for (int index = 0; index < 4; index++) {
                    seed[index] += 50;
                }

				this.encryption = new ISAACRandomGenerator(seed);
				this.socket.write(this.loginStream.position, this.loginStream.buffer);
				responseCode = this.socket.read();
			}
			if (responseCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (final Exception _ex) {
				}
				this.login(playerUsername, playerPassword, recoveredConnection);
				return;
			}
			if (responseCode == 2) {
				this.playerRights = this.socket.read();
				flagged = this.socket.read() == 1;
				this.lastClickTime = 0L;
				this.sameClickPositionCounter = 0;
				this.mouseDetection.coordsIndex = 0;
				super.awtFocus = true;
				this.windowFocused = true;
				this.loggedIn = true;
				this.stream.position = 0;
				this.inStream.position = 0;
				this.packetOpcode = -1;
				this.mostRecentOpcode = -1;
				this.secondMostRecentOpcode = -1;
				this.thirdMostRecentOpcode = -1;
				this.packetSize = 0;
				this.packetReadAnticheat = 0;
				this.systemUpdateTime = 0;
				this.idleLogout = 0;
				this.hintIconType = 0;
				this.menuActionRow = 0;
				this.menuOpen = false;
				super.idleTime = 0;
				for (int m = 0; m < 100; m++) {
					this.chatMessages[m] = null;
                }

				this.itemSelected = false;
				this.spellSelected = false;
				this.loadingStage = 0;
				this.trackCount = 0;
				this.cameraRandomisationH = (int) (Math.random() * 100D) - 50;
				this.cameraRandomisationV = (int) (Math.random() * 110D) - 55;
				this.cameraRandomisationA = (int) (Math.random() * 80D) - 40;
				this.minimap.rotation = (int) (Math.random() * 120D) - 60;
				this.minimap.zoom = (int) (Math.random() * 30D) - 20;
				cameraHorizontal = (int) (Math.random() * 20D) - 10 & 0x7FF;
				this.minimap.state = 0;
				this.lastRegionId = -1;
				this.destinationX = 0;
				this.destinationY = 0;
				this.localPlayerCount = 0;
				this.npcCount = 0;
				for (int p = 0; p < this.MAX_ENTITY_COUNT; p++) {
					this.players[p] = null;
					this.playerAppearanceData[p] = null;
				}

				for (int n = 0; n < 16384; n++) {
					this.npcs[n] = null;
                }

				localPlayer = this.players[this.LOCAL_PLAYER_ID] = new Player();
				this.projectileQueue.clear();
				this.stationaryGraphicQueue.clear();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++) {
							this.groundArray[l2][i3][k3] = null;
                        }

					}

				}

				this.spawnObjectList = new DoubleEndedQueue();
				this.friendListStatus = 0;
				this.friendsCount = 0;
				this.dialogID = -1;
				this.chatboxInterfaceId = -1;
				this.openInterfaceId = -1;
				this.inventoryOverlayInterfaceID = -1;
				this.walkableInterfaceId = -1;
				this.continuedDialogue = false;
				this.currentTabId = 3;
				this.inputDialogState = 0;
				this.menuOpen = false;
				this.messagePromptRaised = false;
				this.clickToContinueString = null;
				this.multiCombatZone = false;
				this.flashingSidebar = -1;
				this.characterEditChangeGender = true;
				this.changeGender();
				for (int c = 0; c < 5; c++) {
					this.characterEditColours[c] = 0;
                }

				for (int a = 0; a < 5; a++) {
					this.playerActionText[a] = null;
					this.playerActionUnpinned[a] = false;
				}
				currentWalkingQueueSize = 0;
				this.setupGameplayScreen();
				return;
			}
			if (responseCode == 3) {
				this.loginMessage1 = "";
				this.loginMessage2 = "Invalid username or password.";
				return;
			}
			if (responseCode == 4) {
				this.loginMessage1 = "Your account has been disabled.";
				this.loginMessage2 = "Please check your message-center for details.";
				return;
			}
			if (responseCode == 5) {
				this.loginMessage1 = "Your account is already logged in.";
				this.loginMessage2 = "Try again in 60 secs...";
				return;
			}
			if (responseCode == 6) {
				this.loginMessage1 = "RuneScape has been updated!";
				this.loginMessage2 = "Please reload this page.";
				return;
			}
			if (responseCode == 7) {
				this.loginMessage1 = "This world is full.";
				this.loginMessage2 = "Please use a different world.";
				return;
			}
			if (responseCode == 8) {
				this.loginMessage1 = "Unable to connect.";
				this.loginMessage2 = "Login server offline.";
				return;
			}
			if (responseCode == 9) {
				this.loginMessage1 = "Login limit exceeded.";
				this.loginMessage2 = "Too many connections from your address.";
				return;
			}
			if (responseCode == 10) {
				this.loginMessage1 = "Unable to connect.";
				this.loginMessage2 = "Bad session id.";
				return;
			}
			if (responseCode == 11) {
				this.loginMessage2 = "Login server rejected session.";
				this.loginMessage2 = "Please try again.";
				return;
			}
			if (responseCode == 12) {
				this.loginMessage1 = "You need a members account to login to this world.";
				this.loginMessage2 = "Please subscribe, or use a different world.";
				return;
			}
			if (responseCode == 13) {
				this.loginMessage1 = "Could not complete login.";
				this.loginMessage2 = "Please try using a different world.";
				return;
			}
			if (responseCode == 14) {
				this.loginMessage1 = "The server is being updated.";
				this.loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (responseCode == 15) {
				this.loggedIn = true;
				this.stream.position = 0;
				this.inStream.position = 0;
				this.packetOpcode = -1;
				this.mostRecentOpcode = -1;
				this.secondMostRecentOpcode = -1;
				this.thirdMostRecentOpcode = -1;
				this.packetSize = 0;
				this.packetReadAnticheat = 0;
				this.systemUpdateTime = 0;
				this.menuActionRow = 0;
				this.menuOpen = false;
				this.loadRegionTime = System.currentTimeMillis();
				return;
			}
			if (responseCode == 16) {
				this.loginMessage1 = "Login attempts exceeded.";
				this.loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (responseCode == 17) {
				this.loginMessage1 = "You are standing in a members-only area.";
				this.loginMessage2 = "To play on this world move to a free area first";
				return;
			}
			if (responseCode == 20) {
				this.loginMessage1 = "Invalid loginserver requested";
				this.loginMessage2 = "Please try using a different world.";
				return;
			}
			if (responseCode == 21) {
				for (int s = this.socket.read(); s >= 0; s--) {
					this.loginMessage1 = "You have only just left another world";
					this.loginMessage2 = "Your profile will be transferred in: " + s + " seconds";
					this.setupLoginScreen();
					this.titleScreen.drawLoginScreen(super.gameGraphics, true, this.loginScreenState, this.onDemandFetcher.statusString, this.loginMessage1, this.loginMessage2, this.enteredUsername, this.enteredPassword, tick, this.loginScreenFocus);
					try {
						Thread.sleep(1000L);
					} catch (final Exception _ex) {
					}
				}

				this.login(playerUsername, playerPassword, recoveredConnection);
				return;
			}
			if (responseCode == -1) {
				if (initialResponseCode == 0) {
					if (this.loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch (final Exception _ex) {
						}
						this.loginFailures++;
						this.login(playerUsername, playerPassword, recoveredConnection);
						return;
					} else {
						this.loginMessage1 = "No response from loginserver";
						this.loginMessage2 = "Please wait 1 minute and try again.";
						return;
					}
				} else {
					this.loginMessage1 = "No response from server";
					this.loginMessage2 = "Please try using a different world.";
					return;
				}
			} else {
				System.out.println("response:" + responseCode);
				this.loginMessage1 = "Unexpected server response";
				this.loginMessage2 = "Please try using a different world.";
				return;
			}
		} catch (final IOException _ex) {
			this.loginMessage1 = "";
		}
		this.loginMessage2 = "Error connecting to server.";
	}

	private void logout() {
		try {
			if (this.socket != null) {
				this.socket.close();
            }
		} catch (final Exception _ex) {
		}
		this.socket = null;
		this.loggedIn = false;
		this.loginScreenState = 0;
		// myUsername = "";
		// myPassword = "";
		this.resetModelCaches();
		this.worldController.initToNull();
		for (int i = 0; i < 4; i++) {
			this.currentCollisionMap[i].reset();
        }

		System.gc();
		this.stopMidi();
		this.currentSong = -1;
		this.nextSong = -1;
		this.prevSong = 0;
	}

	private void updateGame() {
		if (this.systemUpdateTime > 1) {
			this.systemUpdateTime--;
        }

		if (this.idleLogout > 0) {
			this.idleLogout--;
        }

		for (int j = 0; j < 5; j++) {
            if (!this.handleIncomingData()) {
                break;
            }
        }

		if (!this.loggedIn) {
            return;
        }

		synchronized (this.mouseDetection.syncObject) {
			if (flagged) {
				if (super.clickType != 0 || this.mouseDetection.coordsIndex >= 40) {
					this.stream.putOpcode(45);
					this.stream.put(0);
					final int originalOffset = this.stream.position;
					int coordinateCount = 0;
					for (int c = 0; c < this.mouseDetection.coordsIndex; c++) {
						if (originalOffset - this.stream.position >= 240) {
                            break;
                        }
						coordinateCount++;
						int y = this.mouseDetection.coordsY[c];
						if (y < 0) {
                            y = 0;
                        } else if (y > 502) {
                            y = 502;
                        }
						int x = this.mouseDetection.coordsX[c];
						if (x < 0) {
                            x = 0;
                        } else if (x > 764) {
                            x = 764;
                        }
						int pixelOffset = y * 765 + x;
						if (this.mouseDetection.coordsY[c] == -1 && this.mouseDetection.coordsX[c] == -1) {
							x = -1;
							y = -1;
							pixelOffset = 0x7FFFf;
						}
						if (x == this.lastClickX && y == this.lastClickY) {
							if (this.sameClickPositionCounter < 2047) {
								this.sameClickPositionCounter++;
                            }
						} else {
							int differenceX = x - this.lastClickX;
							this.lastClickX = x;
							int differenceY = y - this.lastClickY;
							this.lastClickY = y;
							if (this.sameClickPositionCounter < 8 && differenceX >= -32 && differenceX <= 31
									&& differenceY >= -32 && differenceY <= 31) {
								differenceX += 32;
								differenceY += 32;
								this.stream.putShort((this.sameClickPositionCounter << 12) + (differenceX << 6) + differenceY);
								this.sameClickPositionCounter = 0;
							} else if (this.sameClickPositionCounter < 8) {
								this.stream.put24BitInt(0x800000 + (this.sameClickPositionCounter << 19) + pixelOffset);
								this.sameClickPositionCounter = 0;
							} else {
								this.stream.putInt(0xc0000000 + (this.sameClickPositionCounter << 19) + pixelOffset);
								this.sameClickPositionCounter = 0;
							}
						}
					}

					this.stream.putSizeByte(this.stream.position - originalOffset);
					if (coordinateCount >= this.mouseDetection.coordsIndex) {
						this.mouseDetection.coordsIndex = 0;
					} else {
						this.mouseDetection.coordsIndex -= coordinateCount;
						for (int c = 0; c < this.mouseDetection.coordsIndex; c++) {
							this.mouseDetection.coordsX[c] = this.mouseDetection.coordsX[c + coordinateCount];
							this.mouseDetection.coordsY[c] = this.mouseDetection.coordsY[c + coordinateCount];
						}

					}
				}
			} else {
				this.mouseDetection.coordsIndex = 0;
			}
		}
		if (super.clickType != 0) {
			long timeBetweenClicks = (super.clickTime - this.lastClickTime) / 50L;
			if (timeBetweenClicks > 4095L) {
                timeBetweenClicks = 4095L;
            }
			this.lastClickTime = super.clickTime;
			int y = super.clickY;
			if (y < 0) {
                y = 0;
            } else if (y > 502) {
                y = 502;
            }
			int x = super.clickX;
			if (x < 0) {
                x = 0;
            } else if (x > 764) {
                x = 764;
            }
			final int pixelOffset = y * 765 + x;
			int rightClick = 0;
			if (super.clickType == 2) {
                rightClick = 1;
            }
			final int timeDifference = (int) timeBetweenClicks;
			this.stream.putOpcode(241);
			this.stream.putInt((timeDifference << 20) + (rightClick << 19) + pixelOffset);
		}
		if (this.cameraMovedWriteDelay > 0) {
			this.cameraMovedWriteDelay--;
        }
		if (super.keyStatus[1] == 1 || super.keyStatus[2] == 1 || super.keyStatus[3] == 1 || super.keyStatus[4] == 1) {
			this.cameraMovedWrite = true;
        }
		if (this.cameraMovedWrite && this.cameraMovedWriteDelay <= 0) {
			this.cameraMovedWriteDelay = 20;
			this.cameraMovedWrite = false;
			this.stream.putOpcode(86);
			this.stream.putShort(cameraVertical);
			this.stream.putShortA(cameraHorizontal);
		}
		if (super.awtFocus && !this.windowFocused) {
			this.windowFocused = true;
			this.stream.putOpcode(3);
			this.stream.put(1);
		}
		if (!super.awtFocus && this.windowFocused) {
			this.windowFocused = false;
			this.stream.putOpcode(3);
			this.stream.put(0);
		}
		this.loadingStages();
		this.spawnGameObjects();
		this.handleMusic();
		this.packetReadAnticheat++;
		if (this.packetReadAnticheat > 750) {
			this.dropClient();
        }
		this.updatePlayerInstances();
		this.updateNPCInstances();
		this.cycleEntitySpokenText();
		this.animationTimePassed++;
		if (this.crossType != 0) {
			this.crossIndex += 20;
			if (this.crossIndex >= 400) {
				this.crossType = 0;
            }
		}
		if (this.atInventoryInterfaceType != 0) {
			this.atInventoryLoopCycle++;
			if (this.atInventoryLoopCycle >= 15) {
				if (this.atInventoryInterfaceType == 2) {
					this.redrawTab = true;
                }
				if (this.atInventoryInterfaceType == 3) {
					this.redrawChatbox = true;
                }
				this.atInventoryInterfaceType = 0;
			}
		}
		if (this.activeInterfaceType != 0) {
			this.lastItemDragTime++;
			if (super.mouseX > this.lastMouseX + 5 || super.mouseX < this.lastMouseX - 5 || super.mouseY > this.lastMouseY + 5
					|| super.mouseY < this.lastMouseY - 5) {
				this.lastItemDragged = true;
            }
			if (super.mouseButton == 0) {
				if (this.activeInterfaceType == 2) {
					this.redrawTab = true;
                }
				if (this.activeInterfaceType == 3) {
					this.redrawChatbox = true;
                }
				this.activeInterfaceType = 0;
				if (this.lastItemDragged && this.lastItemDragTime >= 5) {
					this.lastActiveInventoryInterface = -1;
					this.processRightClick();
					if (this.lastActiveInventoryInterface == this.moveItemInterfaceId && this.moveItemSlotEnd != this.moveItemSlotStart) {
						final RSInterface rsInterface = RSInterface.cache[this.moveItemInterfaceId];
						int moveItemInsetionMode = 0;
						if (this.bankInsertMode == 1 && rsInterface.contentType == 206) {
                            moveItemInsetionMode = 1;
                        }
						if (rsInterface.inventoryItemId[this.moveItemSlotEnd] <= 0) {
                            moveItemInsetionMode = 0;
                        }
						if (rsInterface.itemDeletesDragged) {
							final int slotStart = this.moveItemSlotStart;
							final int slotEnd = this.moveItemSlotEnd;
							rsInterface.inventoryItemId[slotEnd] = rsInterface.inventoryItemId[slotStart];
							rsInterface.inventoryStackSize[slotEnd] = rsInterface.inventoryStackSize[slotStart];
							rsInterface.inventoryItemId[slotStart] = -1;
							rsInterface.inventoryStackSize[slotStart] = 0;
						} else if (moveItemInsetionMode == 1) {
							int slotStart = this.moveItemSlotStart;
							for (final int slotPointer = this.moveItemSlotEnd; slotStart != slotPointer;) {
                                if (slotStart > slotPointer) {
                                    rsInterface.swapInventoryItems(slotStart, slotStart - 1);
                                    slotStart--;
                                } else if (slotStart < slotPointer) {
                                    rsInterface.swapInventoryItems(slotStart, slotStart + 1);
                                    slotStart++;
                                }
                            }

						} else {
							rsInterface.swapInventoryItems(this.moveItemSlotStart, this.moveItemSlotEnd);
						}
						this.stream.putOpcode(214);
						this.stream.putLEShortA(this.moveItemInterfaceId);
						this.stream.putByteC(moveItemInsetionMode);
						this.stream.putLEShortA(this.moveItemSlotStart);
						this.stream.putLEShort(this.moveItemSlotEnd);
					}
				} else if ((this.oneMouseButton == 1 || this.menuRowIsAddFriend(this.menuActionRow - 1)) && this.menuActionRow > 2) {
					this.processMenuHovering();
                } else if (this.menuActionRow > 0) {
					this.doAction(this.menuActionRow - 1);
                }
				this.atInventoryLoopCycle = 10;
				super.clickType = 0;
			}
		}
		if (WorldController.clickedTileX != -1) {
			final int x = WorldController.clickedTileX;
			final int y = WorldController.clickedTileY;
			final boolean walkable = this.doWalkTo(0, 0, 0, 0, localPlayer.waypointY[0], 0, 0, y, localPlayer.waypointX[0], true,
					x);
			WorldController.clickedTileX = -1;
			if (walkable) {
				this.crossX = super.clickX;
				this.crossY = super.clickY;
				this.crossType = 1;
				this.crossIndex = 0;
			}
		}
		if (super.clickType == 1 && this.clickToContinueString != null) {
			this.clickToContinueString = null;
			this.redrawChatbox = true;
			super.clickType = 0;
		}
		this.processMenuClick();
		this.processMinimapClick();
		this.processTabClick();
		this.processChatModeClick();
		if (super.mouseButton == 1 || super.clickType == 1) {
			this.anInt1213++;
        }
		if (this.loadingStage == 2) {
			this.setStandardCameraPosition();
        }
		if (this.loadingStage == 2 && this.cutsceneActive) {
			this.setCutsceneCamera();
        }
		for (int camera = 0; camera < 5; camera++) {
			this.unknownCameraVariable[camera]++;
        }

		this.manageTextInput();
		super.idleTime++;
		if (super.idleTime > 4500) {
			this.idleLogout = 250;
			super.idleTime -= 500;
			this.stream.putOpcode(202);
		}
		this.cameraRandomisationCounter++;
		if (this.cameraRandomisationCounter > 500) {
			this.cameraRandomisationCounter = 0;
			final int type = (int) (Math.random() * 8D);
			if ((type & 1) == 1) {
				this.cameraRandomisationH += this.nextCameraRandomisationH;
            }
			if ((type & 2) == 2) {
				this.cameraRandomisationV += this.nextCameraRandomisationV;
            }
			if ((type & 4) == 4) {
				this.cameraRandomisationA += this.nextCameraRandomisationA;
            }
		}
		if (this.cameraRandomisationH < -50) {
			this.nextCameraRandomisationH = 2;
        }
		if (this.cameraRandomisationH > 50) {
			this.nextCameraRandomisationH = -2;
        }
		if (this.cameraRandomisationV < -55) {
			this.nextCameraRandomisationV = 2;
        }
		if (this.cameraRandomisationV > 55) {
			this.nextCameraRandomisationV = -2;
        }
		if (this.cameraRandomisationA < -40) {
			this.nextCameraRandomisationA = 1;
        }
		if (this.cameraRandomisationA > 40) {
			this.nextCameraRandomisationA = -1;
        }
		this.minimapRandomisationCounter++;
		if (this.minimapRandomisationCounter > 500) {
			this.minimapRandomisationCounter = 0;
			final int type = (int) (Math.random() * 8D);
			if ((type & 1) == 1) {
				this.minimap.rotation += this.randomisationMinimapRotation;
            }
			if ((type & 2) == 2) {
				this.minimap.zoom += this.randomisationMinimapZoom;
            }
		}
		if (this.minimap.rotation < -60) {
			this.randomisationMinimapRotation = 2;
        }
		if (this.minimap.rotation > 60) {
			this.randomisationMinimapRotation = -2;
        }
		if (this.minimap.zoom < -20) {
			this.randomisationMinimapZoom = 1;
        }
		if (this.minimap.zoom > 10) {
			this.randomisationMinimapZoom = -1;
        }
		this.idleCounter++;
		if (this.idleCounter > 50) {
			this.stream.putOpcode(0);
        }
		try {
			if (this.socket != null && this.stream.position > 0) {
				this.socket.write(this.stream.position, this.stream.buffer);
				this.stream.position = 0;
				this.idleCounter = 0;
			}
		} catch (final IOException _ex) {
			this.dropClient();
		} catch (final Exception exception) {
			this.logout();
		}
	}

	private void manageTextInput() {
		do {
			final int c = this.readCharacter();
			if (c == -1) {
                break;
            }
			if (this.openInterfaceId != -1 && this.openInterfaceId == this.reportAbuseInterfaceID) {
				if (c == 8 && this.reportAbuseInput.length() > 0) {
					this.reportAbuseInput = this.reportAbuseInput.substring(0, this.reportAbuseInput.length() - 1);
                }
				if ((c >= 97 && c <= 122 || c >= 65 && c <= 90 || c >= 48 && c <= 57 || c == 32)
						&& this.reportAbuseInput.length() < 12) {
					this.reportAbuseInput += (char) c;
                }
			} else if (this.messagePromptRaised) {
				if (c >= 32 && c <= 122 && this.promptInput.length() < 80) {
					// Player pressed an enterable character
					this.promptInput += (char) c;
					this.redrawChatbox = true;
				}
				if (c == 8 && this.promptInput.length() > 0) {
					// Player pressed backspace
					this.promptInput = this.promptInput.substring(0, this.promptInput.length() - 1);
					this.redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					// Player pressed enter
					this.messagePromptRaised = false;
					this.redrawChatbox = true;
					if (this.friendsListAction == 1) {
						final long nameLong = TextClass.nameToLong(this.promptInput);
						this.addFriend(nameLong);
					}
					if (this.friendsListAction == 2 && this.friendsCount > 0) {
						final long nameLong = TextClass.nameToLong(this.promptInput);
						this.deleteFriend(nameLong);
					}
					if (this.friendsListAction == 3 && this.promptInput.length() > 0) {
						this.stream.putOpcode(126);
						this.stream.put(0);
						final int originalOffset = this.stream.position;
						this.stream.putLong(this.privateMessageTarget);
						TextInput.writeToStream(this.promptInput, this.stream);
						this.stream.putSizeByte(this.stream.position - originalOffset);
						this.promptInput = TextInput.processText(this.promptInput);
						this.promptInput = Censor.censor(this.promptInput);
						this.pushMessage(this.promptInput, 6, TextClass.formatName(TextClass.longToName(this.privateMessageTarget)));
						if (this.privateChatMode == 2) {
							this.privateChatMode = 1;
							this.updateChatSettings = true;
							this.stream.putOpcode(95);
							this.stream.put(this.publicChatMode);
							this.stream.put(this.privateChatMode);
							this.stream.put(this.tradeMode);
						}
					}
					if (this.friendsListAction == 4 && this.ignoreCount < 100) {
						final long nameLong = TextClass.nameToLong(this.promptInput);
						this.addIgnore(nameLong);
					}
					if (this.friendsListAction == 5 && this.ignoreCount > 0) {
						final long nameLong = TextClass.nameToLong(this.promptInput);
						this.deleteIgnore(nameLong);
					}
				}
			} else if (this.inputDialogState == 1) {
				if (c >= 48 && c <= 57 && this.amountOrNameInput.length() < 10) {
					this.amountOrNameInput += (char) c;
					this.redrawChatbox = true;
				}
				if (c == 8 && this.amountOrNameInput.length() > 0) {
					this.amountOrNameInput = this.amountOrNameInput.substring(0, this.amountOrNameInput.length() - 1);
					this.redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					if (this.amountOrNameInput.length() > 0) {
						int bankAmount = 0;
						try {
							bankAmount = Integer.parseInt(this.amountOrNameInput);
						} catch (final Exception _ex) {
						}
						this.stream.putOpcode(208);
						this.stream.putInt(bankAmount);
					}
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
			} else if (this.inputDialogState == 2) {
				if (c >= 32 && c <= 122 && this.amountOrNameInput.length() < 12) {
					this.amountOrNameInput += (char) c;
					this.redrawChatbox = true;
				}
				if (c == 8 && this.amountOrNameInput.length() > 0) {
					this.amountOrNameInput = this.amountOrNameInput.substring(0, this.amountOrNameInput.length() - 1);
					this.redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					if (this.amountOrNameInput.length() > 0) {
						this.stream.putOpcode(60);
						this.stream.putLong(TextClass.nameToLong(this.amountOrNameInput));
					}
					this.inputDialogState = 0;
					this.redrawChatbox = true;
				}
			} else if (this.chatboxInterfaceId == -1) {
				if (c >= 32 && c <= 122 && this.inputString.length() < 80) {
					this.inputString += (char) c;
					this.redrawChatbox = true;
				}
				if (c == 8 && this.inputString.length() > 0) {
					this.inputString = this.inputString.substring(0, this.inputString.length() - 1);
					this.redrawChatbox = true;
				}
				if ((c == 13 || c == 10) && this.inputString.length() > 0) {
					if (this.playerRights == 2) {
						if (this.inputString.equals("::clientdrop")) {
							this.dropClient();
                        }
						if (this.inputString.equals("::lag")) {
							this.printDebug();
                        }
						if (this.inputString.equals("::prefetchmusic")) {
							for (int j1 = 0; j1 < this.onDemandFetcher.fileCount(2); j1++) {
								this.onDemandFetcher.setPriority((byte) 1, 2, j1);
                            }

						}
						if (this.inputString.equals("::fpson")) {
                            displayFpsAndMemory = true;
                        }
						if (this.inputString.equals("::fpsoff")) {
                            displayFpsAndMemory = false;
                        }
						if (this.inputString.equals("::noclip")) {
							for (int z = 0; z < 4; z++) {
								for (int x = 1; x < 103; x++) {
									for (int y = 1; y < 103; y++) {
										this.currentCollisionMap[z].clippingData[x][y] = 0;
                                    }

								}

							}
						}
					}
					if (this.inputString.startsWith("::")) {
						this.stream.putOpcode(103);
						this.stream.put(this.inputString.length() - 1);
						this.stream.putString(this.inputString.substring(2));
					} else {
						String text = this.inputString.toLowerCase();
						int colour = 0;
						if (text.startsWith("yellow:")) {
							colour = 0;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("red:")) {
							colour = 1;
							this.inputString = this.inputString.substring(4);
						} else if (text.startsWith("green:")) {
							colour = 2;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("cyan:")) {
							colour = 3;
							this.inputString = this.inputString.substring(5);
						} else if (text.startsWith("purple:")) {
							colour = 4;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("white:")) {
							colour = 5;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("flash1:")) {
							colour = 6;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("flash2:")) {
							colour = 7;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("flash3:")) {
							colour = 8;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("glow1:")) {
							colour = 9;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("glow2:")) {
							colour = 10;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("glow3:")) {
							colour = 11;
							this.inputString = this.inputString.substring(6);
						}
						text = this.inputString.toLowerCase();
						int effect = 0;
						if (text.startsWith("wave:")) {
							effect = 1;
							this.inputString = this.inputString.substring(5);
						} else if (text.startsWith("wave2:")) {
							effect = 2;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("shake:")) {
							effect = 3;
							this.inputString = this.inputString.substring(6);
						} else if (text.startsWith("scroll:")) {
							effect = 4;
							this.inputString = this.inputString.substring(7);
						} else if (text.startsWith("slide:")) {
							effect = 5;
							this.inputString = this.inputString.substring(6);
						}
						this.stream.putOpcode(4);
						this.stream.put(0);
						final int originalOffset = this.stream.position;
						this.stream.putByteS(effect);
						this.stream.putByteS(colour);
						this.textStream.position = 0;
						TextInput.writeToStream(this.inputString, this.textStream);
						this.stream.putBytesA(0, this.textStream.buffer, this.textStream.position);
						this.stream.putSizeByte(this.stream.position - originalOffset);
						this.inputString = TextInput.processText(this.inputString);
						this.inputString = Censor.censor(this.inputString);
						localPlayer.overheadTextMessage = this.inputString;
						localPlayer.chatColour = colour;
						localPlayer.chatEffect = effect;
						localPlayer.textCycle = 150;
						if (this.playerRights == 2) {
							this.pushMessage(localPlayer.overheadTextMessage, 2, "@cr2@" + localPlayer.name);
                        } else if (this.playerRights == 1) {
							this.pushMessage(localPlayer.overheadTextMessage, 2, "@cr1@" + localPlayer.name);
                        } else {
							this.pushMessage(localPlayer.overheadTextMessage, 2, localPlayer.name);
                        }
						if (this.publicChatMode == 2) {
							this.publicChatMode = 3;
							this.updateChatSettings = true;
							this.stream.putOpcode(95);
							this.stream.put(this.publicChatMode);
							this.stream.put(this.privateChatMode);
							this.stream.put(this.tradeMode);
						}
					}
					this.inputString = "";
					this.redrawChatbox = true;
				}
			}
		} while (true);
	}

	private boolean menuRowIsAddFriend(final int row) {
		if (row < 0) {
            return false;
        }
		int actionId = this.menuActionId[row];
		if (actionId >= 2000) {
            actionId -= 2000;
        }
		return actionId == 337;
	}

	private void nullLoader() {
		this.titleScreen.currentlyDrawingFlames = false;
		while (this.titleScreen.drawingFlames) {
			this.titleScreen.currentlyDrawingFlames = false;
			try {
				Thread.sleep(50L);
			} catch (final Exception _ex) {
			}
		}
	}

	private DataInputStream openJagGrabInputStream(final String s) throws IOException {
		// if(!aBoolean872)
		// if(signlink.mainapp != null)
		// return signlink.openurl(s);
		// else
		// return new DataInputStream((new URL(getCodeBase(), s)).openStream());
		if (this.jaggrabSocket != null) {
			try {
				this.jaggrabSocket.close();
			} catch (final Exception _ex) {
			}
			this.jaggrabSocket = null;
		}
		this.jaggrabSocket = this.openSocket(43595);
		this.jaggrabSocket.setSoTimeout(10000);
		final java.io.InputStream inputstream = this.jaggrabSocket.getInputStream();
		final OutputStream outputstream = this.jaggrabSocket.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	public Socket openSocket(final int port) throws IOException {
		if (signlink.applet != null) {
            return signlink.openSocket(port);
        } else {
            return new Socket(InetAddress.getByName(this.getCodeBase().getHost()), port);
        }
	}

	private void parseGroupPacket(final Buffer stream, final int opcode) {
		if (opcode == 84) {
			final int positionOffset = stream.getUnsignedByte();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int targetItemId = stream.getUnsignedLEShort();
			final int targetItemAmount = stream.getUnsignedLEShort();
			final int itemCount = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				final DoubleEndedQueue groundItemArray = this.groundArray[this.plane][x][y];
				if (groundItemArray != null) {
					for (Item item = (Item) groundItemArray.peekFront(); item != null; item = (Item) groundItemArray
							.getPrevious()) {
						if (item.itemId != (targetItemId & 0x7FFF) || item.itemCount != targetItemAmount) {
                            continue;
                        }
						item.itemCount = itemCount;
						break;
					}

					this.spawnGroundItem(x, y);
				}
			}
			return;
		}
		if (opcode == 105) {
			final int positionOffset = stream.getUnsignedByte();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int trackId = stream.getUnsignedLEShort();
			final int data = stream.getUnsignedByte();
			final int boundarySize = data >> 4 & 0xf;
			final int loop = data & 7;
			if (localPlayer.waypointX[0] >= x - boundarySize && localPlayer.waypointX[0] <= x + boundarySize
					&& localPlayer.waypointY[0] >= y - boundarySize && localPlayer.waypointY[0] <= y + boundarySize
					&& this.effectsEnabled && !lowMemory && this.trackCount < 50) {
				this.trackIds[this.trackCount] = trackId;
				this.trackLoop[this.trackCount] = loop;
				this.trackDelay[this.trackCount] = Effect.effectDelays[trackId];
				this.trackCount++;
			}
		}
		if (opcode == 215) {
			final int id = stream.getUnsignedLEShortA();
			final int positionOffset = stream.getUnsignedByteS();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int playerId = stream.getUnsignedLEShortA();
			final int count = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104 && playerId != this.playerListId) {
				final Item item = new Item();
				item.itemId = id;
				item.itemCount = count;
				if (this.groundArray[this.plane][x][y] == null) {
					this.groundArray[this.plane][x][y] = new DoubleEndedQueue();
                }
				this.groundArray[this.plane][x][y].pushBack(item);
				this.spawnGroundItem(x, y);
			}
			return;
		}
		if (opcode == 156) {
			final int positionOffset = stream.getUnsignedByteA();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int itemId = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				final DoubleEndedQueue groundItems = this.groundArray[this.plane][x][y];
				if (groundItems != null) {
					for (Item item = (Item) groundItems.peekFront(); item != null; item = (Item) groundItems
							.getPrevious()) {
						if (item.itemId != (itemId & 0x7FFF)) {
                            continue;
                        }
						item.unlink();
						break;
					}

					if (groundItems.peekFront() == null) {
						this.groundArray[this.plane][x][y] = null;
                    }
					this.spawnGroundItem(x, y);
				}
			}
			return;
		}
		if (opcode == 160) // Spawn a 4-square object?
		{
			final int positionOffset = stream.getUnsignedByteS();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int objectData = stream.getUnsignedByteS();
			int objectType = objectData >> 2;
			final int orientation = objectData & 3;
			final int type = this.objectTypes[objectType];
			final int animationId = stream.getUnsignedLEShortA();
			if (x >= 0 && y >= 0 && x < 103 && y < 103) {
				final int tileHeightX0Y0 = this.intGroundArray[this.plane][x][y];
				final int tileHeightX1Y0 = this.intGroundArray[this.plane][x + 1][y];
				final int tileHeightX1Y1 = this.intGroundArray[this.plane][x + 1][y + 1];
				final int tileHeightX0Y1 = this.intGroundArray[this.plane][x][y + 1];
				if (type == 0) {
					final Wall wallObject = this.worldController.getWallObject(x, y, this.plane);
					if (wallObject != null) {
						final int uid = wallObject.uid >> 14 & 0x7FFF;
						if (objectType == 2) {
							wallObject.primary = new GameObject(uid, 4 + orientation, 2, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
							wallObject.secondary = new GameObject(uid, orientation + 1 & 3, 2, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
						} else {
							wallObject.primary = new GameObject(uid, orientation, objectType, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
						}
					}
				}
				if (type == 1) {
					final WallDecoration wallDecoration = this.worldController.getWallDecoration(x, y, this.plane);
					if (wallDecoration != null) {
                        wallDecoration.renderable = new GameObject(wallDecoration.uid >> 14 & 0x7FFF, 0, 4,
                                tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
                    }
				}
				if (type == 2) {
					final InteractiveObject interactiveObject = this.worldController.getInteractiveObject(x, y, this.plane);
					if (objectType == 11) {
                        objectType = 10;
                    }
					if (interactiveObject != null) {
                        interactiveObject.renderable = new GameObject(interactiveObject.uid >> 14 & 0x7FFF, orientation,
                                objectType, tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId,
                                false);
                    }
				}
				if (type == 3) {
					final GroundDecoration groundDecoration = this.worldController.getGroundDecoration(x, y, this.plane);
					if (groundDecoration != null) {
                        groundDecoration.renderable = new GameObject(groundDecoration.uid >> 14 & 0x7FFF, orientation,
                                22, tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
                    }
				}
			}
			return;
		}
		if (opcode == 147) {
			final int positionOffset = stream.getUnsignedByteS();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int targetPlayer = stream.getUnsignedLEShort();
			byte tileHeight = stream.getByteS();
			final int startDelay = stream.getUnsignedShort();
			byte tileWidth = stream.getByteC();
			final int duration = stream.getUnsignedLEShort();
			final int objectData = stream.getUnsignedByteS();
			final int objectType = objectData >> 2;
			final int objectOrientation = objectData & 3;
			final int type = this.objectTypes[objectType];
			byte offsetX = stream.get();
			final int objectId = stream.getUnsignedLEShort();
			byte offsetY = stream.getByteC();
			final Player player;
			if (targetPlayer == this.playerListId) {
                player = localPlayer;
            } else {
                player = this.players[targetPlayer];
            }
			if (player != null) {
				final GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
				final int tileHeightX0Y0 = this.intGroundArray[this.plane][x][y];
				final int tileHeightX1Y0 = this.intGroundArray[this.plane][x + 1][y];
				final int tileHeightX1Y1 = this.intGroundArray[this.plane][x + 1][y + 1];
				final int tileHeightX0Y1 = this.intGroundArray[this.plane][x][y + 1];
				final Model model = object.getModelAt(objectType, objectOrientation, tileHeightX0Y0, tileHeightX1Y0,
						tileHeightX1Y1, tileHeightX0Y1, -1);
				if (model != null) {
					this.createObjectSpawnRequest(duration + 1, -1, 0, type, y, 0, this.plane, x, startDelay + 1);
					player.modifiedAppearanceStartTime = startDelay + tick;
					player.modifiedAppearanceEndTime = duration + tick;
					player.playerModel = model;
					int sizeX = object.sizeX;
					int sizeY = object.sizeY;
					if (objectOrientation == 1 || objectOrientation == 3) {
						sizeX = object.sizeY;
						sizeY = object.sizeX;
					}
					player.anInt1711 = x * 128 + sizeX * 64;
					player.anInt1713 = y * 128 + sizeY * 64;
					player.drawHeight = this.getFloorDrawHeight(this.plane, player.anInt1713, player.anInt1711);
					if (offsetX > tileHeight) {
						final byte temp = offsetX;
						offsetX = tileHeight;
						tileHeight = temp;
					}
					if (offsetY > tileWidth) {
						final byte temp = offsetY;
						offsetY = tileWidth;
						tileWidth = temp;
					}
					player.localX = x + offsetX;
					player.playerTileHeight = x + tileHeight;
					player.localY = y + offsetY;
					player.playerTileWidth = y + tileWidth;
				}
			}
		}
		if (opcode == 151) {
			final int positionOffset = stream.getUnsignedByteA();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			final int objectId = stream.getUnsignedShort();
			final int data = stream.getUnsignedByteS();
			final int objectType = data >> 2;
			final int orientation = data & 3;
			final int type = this.objectTypes[objectType];
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				this.createObjectSpawnRequest(-1, objectId, orientation, type, y, objectType, this.plane, x, 0);
            }
			return;
		}
		if (opcode == 4) {
			final int positionOffset = stream.getUnsignedByte();
			int x = this.playerPositionX + (positionOffset >> 4 & 7);
			int y = this.playerPositionY + (positionOffset & 7);
			final int graphicId = stream.getUnsignedLEShort();
			final int drawHeight = stream.getUnsignedByte();
			final int delay = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				x = x * 128 + 64;
				y = y * 128 + 64;
				final StationaryGraphic stationaryGraphic = new StationaryGraphic(x, y, this.plane,
						this.getFloorDrawHeight(this.plane, y, x) - drawHeight, graphicId, tick, delay);
				this.stationaryGraphicQueue.pushBack(stationaryGraphic);
			}
			return;
		}
		if (opcode == 44) {
			final int itemId = stream.getUnsignedShortA();
			final int itemAmount = stream.getUnsignedLEShort();
			final int positionOffset = stream.getUnsignedByte();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				final Item item = new Item();
				item.itemId = itemId;
				item.itemCount = itemAmount;
				if (this.groundArray[this.plane][x][y] == null) {
					this.groundArray[this.plane][x][y] = new DoubleEndedQueue();
                }
				this.groundArray[this.plane][x][y].pushBack(item);
				this.spawnGroundItem(x, y);
			}
			return;
		}
		if (opcode == 101) {
			final int objectData = stream.getUnsignedByteC();
			final int objectType = objectData >> 2;
			final int face = objectData & 3;
			final int type = this.objectTypes[objectType];
			final int positionOffset = stream.getUnsignedByte();
			final int x = this.playerPositionX + (positionOffset >> 4 & 7);
			final int y = this.playerPositionY + (positionOffset & 7);
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				this.createObjectSpawnRequest(-1, -1, face, type, y, objectType, this.plane, x, 0);
            }
			return;
		}
		if (opcode == 117) {
			final int projectileAngle = stream.getUnsignedByte();
			int projectileX = this.playerPositionX + (projectileAngle >> 4 & 7);
			int projectileY = this.playerPositionY + (projectileAngle & 7);
			int projectileOffsetX = projectileX + stream.get();
			int projectileOffsetY = projectileY + stream.get();
			final int projectileTarget = stream.getShort();
			final int projectileGraphicId = stream.getUnsignedLEShort();
			final int projectileHeightStart = stream.getUnsignedByte() * 4;
			final int projectileHeightEnd = stream.getUnsignedByte() * 4;
			final int projectileCreatedTime = stream.getUnsignedLEShort();
			final int projectileSpeed = stream.getUnsignedLEShort();
			final int projectileInitialSlope = stream.getUnsignedByte();
			final int projectileDistanceFromSource = stream.getUnsignedByte();
			if (projectileX >= 0 && projectileY >= 0 && projectileX < 104 && projectileY < 104 && projectileOffsetX >= 0
					&& projectileOffsetY >= 0 && projectileOffsetX < 104 && projectileOffsetY < 104
					&& projectileGraphicId != 0x00FFFF) {
				projectileX = projectileX * 128 + 64;
				projectileY = projectileY * 128 + 64;
				projectileOffsetX = projectileOffsetX * 128 + 64;
				projectileOffsetY = projectileOffsetY * 128 + 64;
				final Projectile projectile = new Projectile(projectileInitialSlope, projectileHeightEnd,
						projectileCreatedTime + tick, projectileSpeed + tick, projectileDistanceFromSource, this.plane,
						this.getFloorDrawHeight(this.plane, projectileY, projectileX) - projectileHeightStart, projectileY,
						projectileX, projectileTarget, projectileGraphicId);
				projectile.trackTarget(projectileCreatedTime + tick, projectileOffsetY,
						this.getFloorDrawHeight(this.plane, projectileOffsetY, projectileOffsetX) - projectileHeightEnd,
						projectileOffsetX);
				this.projectileQueue.pushBack(projectile);
			}
		}
	}

	private int parseInterfaceOpcode(final RSInterface rsInterface, final int interfaceId) {
		if (rsInterface.opcodes == null || interfaceId >= rsInterface.opcodes.length) {
            return -2;
        }
		try {
			final int[] opcodes = rsInterface.opcodes[interfaceId];
			int result = 0;
			int counter = 0;
			int type = 0;
			do {
				final int opcode = opcodes[counter++];
				int value = 0;
				byte tempType = 0;
				if (opcode == 0) {
                    return result;
                }
				if (opcode == 1) {
                    value = this.skillLevel[opcodes[counter++]];
                }
				if (opcode == 2) {
                    value = this.skillMaxLevel[opcodes[counter++]];
                }
				if (opcode == 3) {
                    value = this.skillExperience[opcodes[counter++]];
                }
				if (opcode == 4) {
					final RSInterface itemInterface = RSInterface.cache[opcodes[counter++]];
					final int itemId = opcodes[counter++];
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersObject || membersWorld)) {
						for (int item = 0; item < itemInterface.inventoryItemId.length; item++) {
                            if (itemInterface.inventoryItemId[item] == itemId + 1) {
                                value += itemInterface.inventoryStackSize[item];
                            }
                        }

					}
				}
				if (opcode == 5) {
                    value = this.interfaceSettings[opcodes[counter++]];
                }
				if (opcode == 6) {
                    value = EXPERIENCE_TABLE[this.skillMaxLevel[opcodes[counter++]] - 1];
                }
				if (opcode == 7) {
                    value = (this.interfaceSettings[opcodes[counter++]] * 100) / 46875;
                }
				if (opcode == 8) {
                    value = localPlayer.combatLevel;
                }
				if (opcode == 9) {
					for (int skill = 0; skill < Skills.skillsCount; skill++) {
                        if (Skills.skillEnabled[skill]) {
                            value += this.skillMaxLevel[skill];
                        }
                    }

				}
				if (opcode == 10) {
					final RSInterface itemInterface = RSInterface.cache[opcodes[counter++]];
					final int itemId = opcodes[counter++] + 1;
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersObject || membersWorld)) {
						for (int item = 0; item < itemInterface.inventoryItemId.length; item++) {
							if (itemInterface.inventoryItemId[item] != itemId) {
                                continue;
                            }
							value = 999999999;
							break;
						}

					}
				}
				if (opcode == 11) {
                    value = this.playerEnergy;
                }
				if (opcode == 12) {
                    value = this.playerWeight;
                }
				if (opcode == 13) {
					final int setting = this.interfaceSettings[opcodes[counter++]];
					final int info = opcodes[counter++];
					value = (setting & 1 << info) == 0 ? 0 : 1;
				}
				if (opcode == 14) {
					final int varBitId = opcodes[counter++];
					final VarBit varBit = VarBit.values[varBitId];
					final int configId = varBit.configId;
					final int lsb = varBit.leastSignificantBit;
					final int msb = varBit.mostSignificantBit;
					final int bit = BITFIELD_MAX_VALUE[msb - lsb];
					value = this.interfaceSettings[configId] >> lsb & bit;
				}
				if (opcode == 15) {
                    tempType = 1;
                }
				if (opcode == 16) {
                    tempType = 2;
                }
				if (opcode == 17) {
                    tempType = 3;
                }
				if (opcode == 18) {
                    value = (localPlayer.x >> 7) + this.baseX;
                }
				if (opcode == 19) {
                    value = (localPlayer.y >> 7) + this.baseY;
                }
				if (opcode == 20) {
                    value = opcodes[counter++];
                }
				if (tempType == 0) {
					if (type == 0) {
                        result += value;
                    }
					if (type == 1) {
                        result -= value;
                    }
					if (type == 2 && value != 0) {
                        result /= value;
                    }
					if (type == 3) {
                        result *= value;
                    }
					type = 0;
				} else {
					type = tempType;
				}
			} while (true);
		} catch (final Exception _ex) {
			return -1;
		}
	}

	private void printDebug() {
		System.out.println("============");
		if (this.onDemandFetcher != null) {
            System.out.println("Od-cycle:" + this.onDemandFetcher.onDemandCycle);
        }
		System.out.println("loop-cycle:" + tick);
		System.out.println("draw-cycle:" + drawCycle);
		System.out.println("ptype:" + this.packetOpcode);
		System.out.println("psize:" + this.packetSize);
		if (this.socket != null) {
			this.socket.printDebug();
        }
		super.debugRequested = true;
	}

	private void processChatModeClick() {
		if (super.clickType == 1) {
			if (super.clickX >= 6 && super.clickX <= 106 && super.clickY >= 467 && super.clickY <= 499) {
				this.publicChatMode = (this.publicChatMode + 1) % 4;
				this.updateChatSettings = true;
				this.redrawChatbox = true;
				this.stream.putOpcode(95);
				this.stream.put(this.publicChatMode);
				this.stream.put(this.privateChatMode);
				this.stream.put(this.tradeMode);
			}
			if (super.clickX >= 135 && super.clickX <= 235 && super.clickY >= 467 && super.clickY <= 499) {
				this.privateChatMode = (this.privateChatMode + 1) % 3;
				this.updateChatSettings = true;
				this.redrawChatbox = true;
				this.stream.putOpcode(95);
				this.stream.put(this.publicChatMode);
				this.stream.put(this.privateChatMode);
				this.stream.put(this.tradeMode);
			}
			if (super.clickX >= 273 && super.clickX <= 373 && super.clickY >= 467 && super.clickY <= 499) {
				this.tradeMode = (this.tradeMode + 1) % 3;
				this.updateChatSettings = true;
				this.redrawChatbox = true;
				this.stream.putOpcode(95);
				this.stream.put(this.publicChatMode);
				this.stream.put(this.privateChatMode);
				this.stream.put(this.tradeMode);
			}
			if (super.clickX >= 412 && super.clickX <= 512 && super.clickY >= 467 && super.clickY <= 499) {
				if (this.openInterfaceId == -1) {
					this.clearTopInterfaces();
					this.reportAbuseInput = "";
					this.reportAbuseMute = false;
					for (int i = 0; i < RSInterface.cache.length; i++) {
						if (RSInterface.cache[i] == null || RSInterface.cache[i].contentType != 600) {
                            continue;
                        }
						this.reportAbuseInterfaceID = this.openInterfaceId = RSInterface.cache[i].parentID;
						break;
					}

				} else {
					this.pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			}
		}
	}

	@Override
	public void processDrawing() {
		if (this.rsAlreadyLoaded || this.loadingError || this.genericLoadingError) {
			this.showErrorScreen();
			return;
		}
		drawCycle++;
		if (!this.loggedIn) {
			this.setupLoginScreen();
			this.titleScreen.drawLoginScreen(super.gameGraphics, false, this.loginScreenState, this.onDemandFetcher.statusString, this.loginMessage1, this.loginMessage2, this.enteredUsername, this.enteredPassword, tick, this.loginScreenFocus);
		} else {
			this.drawGameScreen();
        }
		this.anInt1213 = 0;
	}

	@Override
	public void processGameLoop() {
		if (this.rsAlreadyLoaded || this.loadingError || this.genericLoadingError) {
            return;
        }
		tick++;
		if (!this.loggedIn) {
			this.updateLogin();
        } else {
			this.updateGame();
        }
		this.processOnDemandQueue();
	}

	private void updateLogin() {
		if (this.loginScreenState == 0) {
			int x = super.width / 2 - 80;
			int y = super.height / 2 + 20;
			y += 20;
			if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
					&& super.clickY <= y + 20) {
				this.loginScreenState = 3;
				this.loginScreenFocus = 0;
			}
			x = super.width / 2 + 80;
			if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
					&& super.clickY <= y + 20) {
				this.loginMessage1 = "";
				this.loginMessage2 = "Enter your username & password.";
				this.loginScreenState = 2;
				this.loginScreenFocus = 0;
			}
		} else {
			if (this.loginScreenState == 2) {
				int y = super.height / 2 - 40;
				y += 30;
				y += 25;
				if (super.clickType == 1 && super.clickY >= y - 15 && super.clickY < y) {
					this.loginScreenFocus = 0;
                }
				y += 15;
				if (super.clickType == 1 && super.clickY >= y - 15 && super.clickY < y) {
					this.loginScreenFocus = 1;
                }
				y += 15;
				int x = super.width / 2 - 80;
				int _y = super.height / 2 + 50;
				_y += 20;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= _y - 20
						&& super.clickY <= _y + 20) {
					this.loginFailures = 0;
					this.login(this.enteredUsername, this.enteredPassword, false);
					if (this.loggedIn) {
                        return;
                    }
				}
				x = super.width / 2 + 80;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= _y - 20
						&& super.clickY <= _y + 20) {
					this.loginScreenState = 0;
					// myUsername = "";
					// myPassword = "";
				}
				do {
					final int character = this.readCharacter();
					if (character == -1) {
                        break;
                    }
					boolean validCharacter = false;
					for (int c = 0; c < validUserPassChars.length(); c++) {
						if (character != validUserPassChars.charAt(c)) {
                            continue;
                        }
						validCharacter = true;
						break;
					}

					if (this.loginScreenFocus == 0) {
						if (character == 8 && this.enteredUsername.length() > 0) {
							this.enteredUsername = this.enteredUsername.substring(0, this.enteredUsername.length() - 1);
                        }
						if (character == 9 || character == 10 || character == 13) {
							this.loginScreenFocus = 1;
                        }
						if (validCharacter) {
							this.enteredUsername += (char) character;
                        }
						if (this.enteredUsername.length() > 12) {
							this.enteredUsername = this.enteredUsername.substring(0, 12);
                        }
					} else if (this.loginScreenFocus == 1) {
						if (character == 8 && this.enteredPassword.length() > 0) {
							this.enteredPassword = this.enteredPassword.substring(0, this.enteredPassword.length() - 1);
                        }
						if (character == 9 || character == 10 || character == 13) {
							this.loginScreenFocus = 0;
                        }
						if (validCharacter) {
							this.enteredPassword += (char) character;
                        }
						if (this.enteredPassword.length() > 20) {
							this.enteredPassword = this.enteredPassword.substring(0, 20);
                        }
					}
				} while (true);
				return;
			}
			if (this.loginScreenState == 3) {
				final int x = super.width / 2;
				int y = super.height / 2 + 50;
				y += 20;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
						&& super.clickY <= y + 20) {
					this.loginScreenState = 0;
                }
			}
		}
	}

	private void processMenuClick() {
		if (this.activeInterfaceType != 0) {
            return;
        }
		int clickType = super.clickType;
		if (this.spellSelected && super.clickX >= 516 && super.clickY >= 160 && super.clickX <= 765 && super.clickY <= 205) {
            clickType = 0;
        }
		if (this.menuOpen) {
			if (clickType != 1) {
				int x = super.mouseX;
				int y = super.mouseY;
				if (this.menuScreenArea == 0) {
					x -= 4;
					y -= 4;
				}
				if (this.menuScreenArea == 1) {
					x -= 553;
					y -= 205;
				}
				if (this.menuScreenArea == 2) {
					x -= 17;
					y -= 357;
				}
				if (x < this.menuOffsetX - 10 || x > this.menuOffsetX + this.menuWidth + 10 || y < this.menuOffsetY - 10
						|| y > this.menuOffsetY + this.menuHeight + 10) {
					this.menuOpen = false;
					if (this.menuScreenArea == 1) {
						this.redrawTab = true;
                    }
					if (this.menuScreenArea == 2) {
						this.redrawChatbox = true;
                    }
				}
			}
			if (clickType == 1) {
				final int menuX = this.menuOffsetX;
				final int height = this.menuOffsetY;
				final int width = this.menuWidth;
				int x = super.clickX;
				int y = super.clickY;
				if (this.menuScreenArea == 0) {
					x -= 4;
					y -= 4;
				}
				if (this.menuScreenArea == 1) {
					x -= 553;
					y -= 205;
				}
				if (this.menuScreenArea == 2) {
					x -= 17;
					y -= 357;
				}
				int hoveredRow = -1;
				for (int row = 0; row < this.menuActionRow; row++) {
					final int rowY = height + 31 + (this.menuActionRow - 1 - row) * 15;
					if (x > menuX && x < menuX + width && y > rowY - 13 && y < rowY + 3) {
                        hoveredRow = row;
                    }
				}

				if (hoveredRow != -1) {
					this.doAction(hoveredRow);
                }
				this.menuOpen = false;
				if (this.menuScreenArea == 1) {
					this.redrawTab = true;
                }
				if (this.menuScreenArea == 2) {
					this.redrawChatbox = true;
				}
			}
		} else {
			if (clickType == 1 && this.menuActionRow > 0) {
				final int action = this.menuActionId[this.menuActionRow - 1];
				if (action == 632 || action == 78 || action == 867 || action == 431 || action == 53 || action == 74
						|| action == 454 || action == 539 || action == 493 || action == 847 || action == 447
						|| action == 1125) {
					final int slot = this.menuActionData2[this.menuActionRow - 1];
					final int interfaceId = this.menuActionData3[this.menuActionRow - 1];
					final RSInterface rsInterface = RSInterface.cache[interfaceId];
					if (rsInterface.itemSwappable || rsInterface.itemDeletesDragged) {
						this.lastItemDragged = false;
						this.lastItemDragTime = 0;
						this.moveItemInterfaceId = interfaceId;
						this.moveItemSlotStart = slot;
						this.activeInterfaceType = 2;
						this.lastMouseX = super.clickX;
						this.lastMouseY = super.clickY;
						if (RSInterface.cache[interfaceId].parentID == this.openInterfaceId) {
							this.activeInterfaceType = 1;
                        }
						if (RSInterface.cache[interfaceId].parentID == this.chatboxInterfaceId) {
							this.activeInterfaceType = 3;
                        }
						return;
					}
				}
			}
			if (clickType == 1 && (this.oneMouseButton == 1 || this.menuRowIsAddFriend(this.menuActionRow - 1)) && this.menuActionRow > 2) {
                clickType = 2;
            }
			if (clickType == 1 && this.menuActionRow > 0) {
				this.doAction(this.menuActionRow - 1);
            }
			if (clickType == 2 && this.menuActionRow > 0) {
				this.processMenuHovering();
            }
		}
	}

	private void processMenuHovering() {
		int width = this.fontBold.getTextDisplayedWidth("Choose Option");
		for (int row = 0; row < this.menuActionRow; row++) {
			final int rowWidth = this.fontBold.getTextDisplayedWidth(this.menuActionName[row]);
			if (rowWidth > width) {
                width = rowWidth;
            }
		}

		width += 8;
		final int height = 15 * this.menuActionRow + 21;
		if (super.clickX > 4 && super.clickY > 4 && super.clickX < 516 && super.clickY < 338) {
			int x = super.clickX - 4 - width / 2;
			if (x + width > 512) {
                x = 512 - width;
            }
			if (x < 0) {
                x = 0;
            }
			int y = super.clickY - 4;
			if (y + height > 334) {
                y = 334 - height;
            }
			if (y < 0) {
                y = 0;
            }
			this.menuOpen = true;
			this.menuScreenArea = 0;
			this.menuOffsetX = x;
			this.menuOffsetY = y;
			this.menuWidth = width;
			this.menuHeight = 15 * this.menuActionRow + 22;
		}
		if (super.clickX > 553 && super.clickY > 205 && super.clickX < 743 && super.clickY < 466) {
			int x = super.clickX - 553 - width / 2;
			if (x < 0) {
                x = 0;
            } else if (x + width > 190) {
                x = 190 - width;
            }
			int y = super.clickY - 205;
			if (y < 0) {
                y = 0;
            } else if (y + height > 261) {
                y = 261 - height;
            }
			this.menuOpen = true;
			this.menuScreenArea = 1;
			this.menuOffsetX = x;
			this.menuOffsetY = y;
			this.menuWidth = width;
			this.menuHeight = 15 * this.menuActionRow + 22;
		}
		if (super.clickX > 17 && super.clickY > 357 && super.clickX < 496 && super.clickY < 453) {
			int x = super.clickX - 17 - width / 2;
			if (x < 0) {
                x = 0;
            } else if (x + width > 479) {
                x = 479 - width;
            }
			int y = super.clickY - 357;
			if (y < 0) {
                y = 0;
            } else if (y + height > 96) {
                y = 96 - height;
            }
			this.menuOpen = true;
			this.menuScreenArea = 2;
			this.menuOffsetX = x;
			this.menuOffsetY = y;
			this.menuWidth = width;
			this.menuHeight = 15 * this.menuActionRow + 22;
		}
	}

	private void processMinimapClick() {
		if (this.minimap.state != 0) {
            return;
        }
		if (super.clickType == 1) {
			int i = super.clickX - 25 - 550;
			int j = super.clickY - 5 - 4;
			if (i >= 0 && j >= 0 && i < 146 && j < 151) {
				i -= 73;
				j -= 75;
				final int k = cameraHorizontal + this.minimap.rotation & 0x7FF;
				int sine = Rasterizer.SINE[k];
				int cosine = Rasterizer.COSINE[k];
				sine = sine * (this.minimap.zoom + 256) >> 8;
				cosine = cosine * (this.minimap.zoom + 256) >> 8;
				final int k1 = j * sine + i * cosine >> 11;
				final int l1 = j * cosine - i * sine >> 11;
				final int i2 = localPlayer.x + k1 >> 7;
				final int j2 = localPlayer.y - l1 >> 7;
				final boolean canWalk = this.doWalkTo(1, 0, 0, 0, localPlayer.waypointY[0], 0, 0, j2, localPlayer.waypointX[0],
						true, i2);
				if (canWalk) {
					this.stream.put(i);
					this.stream.put(j);
					this.stream.putShort(cameraHorizontal);
					this.stream.put(57);
					this.stream.put(this.minimap.rotation);
					this.stream.put(this.minimap.zoom);
					this.stream.put(89);
					this.stream.putShort(localPlayer.x);
					this.stream.putShort(localPlayer.y);
					this.stream.put(this.arbitraryDestination);
					this.stream.put(63);
				}
			}
			mouseClickCounter++;
			if (mouseClickCounter > 1151) {
				mouseClickCounter = 0;
				this.stream.putOpcode(246);
				this.stream.put(0);
				final int l = this.stream.position;
				if ((int) (Math.random() * 2D) == 0) {
					this.stream.put(101);
                }
				this.stream.put(197);
				this.stream.putShort((int) (Math.random() * 65536D));
				this.stream.put((int) (Math.random() * 256D));
				this.stream.put(67);
				this.stream.putShort(14214);
				if ((int) (Math.random() * 2D) == 0) {
					this.stream.putShort(29487);
                }
				this.stream.putShort((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0) {
					this.stream.put(220);
                }
				this.stream.put(180);
				this.stream.putSizeByte(this.stream.position - l);
			}
		}
	}

	private void processOnDemandQueue() {
		do {
			OnDemandData onDemandData;
			do {
				onDemandData = this.onDemandFetcher.getNextNode();
				if (onDemandData == null) {
                    return;
                }
				if (onDemandData.dataType == 0) {
					Model.loadModelHeader(onDemandData.buffer, onDemandData.id);
					if ((this.onDemandFetcher.getModelId(onDemandData.id) & 0x62) != 0) {
						this.redrawTab = true;
						if (this.chatboxInterfaceId != -1) {
							this.redrawChatbox = true;
                        }
					}
				}
				if (onDemandData.dataType == 1 && onDemandData.buffer != null) {
                    Animation.method529(onDemandData.buffer);
                }
				if (onDemandData.dataType == 2 && onDemandData.id == this.nextSong && onDemandData.buffer != null) {
					this.saveMidi(this.songChanging, onDemandData.buffer);
                }
				if (onDemandData.dataType == 3 && this.loadingStage == 1) {
					for (int r = 0; r < this.terrainData.length; r++) {
						if (this.terrainDataIds[r] == onDemandData.id) {
							this.terrainData[r] = onDemandData.buffer;
							if (onDemandData.buffer == null) {
								this.terrainDataIds[r] = -1;
                            }
							break;
						}

						if (this.objectDataIds[r] != onDemandData.id) {
                            continue;
                        }
						this.objectData[r] = onDemandData.buffer;
						if (onDemandData.buffer == null) {
							this.objectDataIds[r] = -1;
                        }
						break;
					}

				}
			} while (onDemandData.dataType != 93 || !this.onDemandFetcher.method564(onDemandData.id));
			Region.passivelyRequestGameObjectModels(new Buffer(onDemandData.buffer), this.onDemandFetcher);
		} while (true);
	}

	private void processRightClick() {
		if (this.activeInterfaceType != 0) {
            return;
        }
		this.menuActionName[0] = "Cancel";
		this.menuActionId[0] = 1107;
		this.menuActionRow = 1;
		this.buildSplitPrivateChatMenu();
		this.anInt886 = 0;
		if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338) {
            if (this.openInterfaceId != -1) {
				this.buildInterfaceMenu(4, RSInterface.cache[this.openInterfaceId], super.mouseX, 4, super.mouseY, 0);
            } else {
				this.build3dScreenMenu();
            }
        }
		if (this.anInt886 != this.anInt1026) {
			this.anInt1026 = this.anInt886;
        }
		this.anInt886 = 0;
		if (super.mouseX > 553 && super.mouseY > 205 && super.mouseX < 743 && super.mouseY < 466) {
            if (this.inventoryOverlayInterfaceID != -1) {
				this.buildInterfaceMenu(553, RSInterface.cache[this.inventoryOverlayInterfaceID], super.mouseX, 205, super.mouseY,
                        0);
            } else if (this.tabInterfaceIDs[this.currentTabId] != -1) {
				this.buildInterfaceMenu(553, RSInterface.cache[this.tabInterfaceIDs[this.currentTabId]], super.mouseX, 205,
                        super.mouseY, 0);
            }
        }
		if (this.anInt886 != this.anInt1048) {
			this.redrawTab = true;
			this.anInt1048 = this.anInt886;
		}
		this.anInt886 = 0;
		if (super.mouseX > 17 && super.mouseY > 357 && super.mouseX < 496 && super.mouseY < 453) {
            if (this.chatboxInterfaceId != -1) {
				this.buildInterfaceMenu(17, RSInterface.cache[this.chatboxInterfaceId], super.mouseX, 357, super.mouseY, 0);
            } else if (super.mouseY < 434 && super.mouseX < 426) {
				this.buildChatboxMenu(super.mouseY - 357);
            }
        }
		if (this.chatboxInterfaceId != -1 && this.anInt886 != this.anInt1039) {
			this.redrawChatbox = true;
			this.anInt1039 = this.anInt886;
		}
		boolean ordered = false;
		while (!ordered) {
			ordered = true;
			for (int a = 0; a < this.menuActionRow - 1; a++) {
                if (this.menuActionId[a] < 1000 && this.menuActionId[a + 1] > 1000) {
                    final String s = this.menuActionName[a];
					this.menuActionName[a] = this.menuActionName[a + 1];
					this.menuActionName[a + 1] = s;
                    int temp = this.menuActionId[a];
					this.menuActionId[a] = this.menuActionId[a + 1];
					this.menuActionId[a + 1] = temp;
                    temp = this.menuActionData2[a];
					this.menuActionData2[a] = this.menuActionData2[a + 1];
					this.menuActionData2[a + 1] = temp;
                    temp = this.menuActionData3[a];
					this.menuActionData3[a] = this.menuActionData3[a + 1];
					this.menuActionData3[a + 1] = temp;
                    temp = this.menuActionData1[a];
					this.menuActionData1[a] = this.menuActionData1[a + 1];
					this.menuActionData1[a + 1] = temp;
                    ordered = false;
                }
            }

		}
	}

	private void processTabClick() {
		if (super.clickType == 1) {
			if (super.clickX >= 539 && super.clickX <= 573 && super.clickY >= 169 && super.clickY < 205
					&& this.tabInterfaceIDs[0] != -1) {
				this.redrawTab = true;
				this.currentTabId = 0;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 569 && super.clickX <= 599 && super.clickY >= 168 && super.clickY < 205
					&& this.tabInterfaceIDs[1] != -1) {
				this.redrawTab = true;
				this.currentTabId = 1;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 597 && super.clickX <= 627 && super.clickY >= 168 && super.clickY < 205
					&& this.tabInterfaceIDs[2] != -1) {
				this.redrawTab = true;
				this.currentTabId = 2;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 625 && super.clickX <= 669 && super.clickY >= 168 && super.clickY < 203
					&& this.tabInterfaceIDs[3] != -1) {
				this.redrawTab = true;
				this.currentTabId = 3;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 666 && super.clickX <= 696 && super.clickY >= 168 && super.clickY < 205
					&& this.tabInterfaceIDs[4] != -1) {
				this.redrawTab = true;
				this.currentTabId = 4;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 694 && super.clickX <= 724 && super.clickY >= 168 && super.clickY < 205
					&& this.tabInterfaceIDs[5] != -1) {
				this.redrawTab = true;
				this.currentTabId = 5;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 722 && super.clickX <= 756 && super.clickY >= 169 && super.clickY < 205
					&& this.tabInterfaceIDs[6] != -1) {
				this.redrawTab = true;
				this.currentTabId = 6;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 540 && super.clickX <= 574 && super.clickY >= 466 && super.clickY < 502
					&& this.tabInterfaceIDs[7] != -1) {
				this.redrawTab = true;
				this.currentTabId = 7;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 572 && super.clickX <= 602 && super.clickY >= 466 && super.clickY < 503
					&& this.tabInterfaceIDs[8] != -1) {
				this.redrawTab = true;
				this.currentTabId = 8;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 599 && super.clickX <= 629 && super.clickY >= 466 && super.clickY < 503
					&& this.tabInterfaceIDs[9] != -1) {
				this.redrawTab = true;
				this.currentTabId = 9;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 627 && super.clickX <= 671 && super.clickY >= 467 && super.clickY < 502
					&& this.tabInterfaceIDs[10] != -1) {
				this.redrawTab = true;
				this.currentTabId = 10;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 669 && super.clickX <= 699 && super.clickY >= 466 && super.clickY < 503
					&& this.tabInterfaceIDs[11] != -1) {
				this.redrawTab = true;
				this.currentTabId = 11;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 696 && super.clickX <= 726 && super.clickY >= 466 && super.clickY < 503
					&& this.tabInterfaceIDs[12] != -1) {
				this.redrawTab = true;
				this.currentTabId = 12;
				this.drawTabIcons = true;
			}
			if (super.clickX >= 724 && super.clickX <= 758 && super.clickY >= 466 && super.clickY < 502
					&& this.tabInterfaceIDs[13] != -1) {
				this.redrawTab = true;
				this.currentTabId = 13;
				this.drawTabIcons = true;
			}
		}
	}

	private void processWalkingStep(final Entity entity) {
		entity.queuedAnimationId = entity.standAnimationId;
		if (entity.waypointCount == 0) {
			entity.stepsDelayed = 0;
			return;
		}
		if (entity.animation != -1 && entity.animationDelay == 0) {
			final AnimationSequence animation = AnimationSequence.animations[entity.animation];
			if (entity.stepsRemaining > 0 && animation.precedenceAnimating == 0) {
				entity.stepsDelayed++;
				return;
			}
			if (entity.stepsRemaining <= 0 && animation.precedenceWalking == 0) {
				entity.stepsDelayed++;
				return;
			}
		}
		final int x1 = entity.x;
		final int y1 = entity.y;
		final int x2 = entity.waypointX[entity.waypointCount - 1] * 128 + entity.boundaryDimension * 64;
		final int y2 = entity.waypointY[entity.waypointCount - 1] * 128 + entity.boundaryDimension * 64;
		if (x2 - x1 > 256 || x2 - x1 < -256 || y2 - y1 > 256 || y2 - y1 < -256) {
			entity.x = x2;
			entity.y = y2;
			return;
		}
		if (x1 < x2) {
			if (y1 < y2) {
                entity.turnDirection = 1280;
            } else if (y1 > y2) {
                entity.turnDirection = 1792;
            } else {
                entity.turnDirection = 1536;
            }
		} else if (x1 > x2) {
			if (y1 < y2) {
                entity.turnDirection = 768;
            } else if (y1 > y2) {
                entity.turnDirection = 256;
            } else {
                entity.turnDirection = 512;
            }
		} else if (y1 < y2) {
            entity.turnDirection = 1024;
        } else {
            entity.turnDirection = 0;
        }
		int rotationDifference = entity.turnDirection - entity.currentRotation & 0x7FF;
		if (rotationDifference > 1024) {
            rotationDifference -= 2048;
        }
		int anim = entity.turnAboutAnimationId;
		if (rotationDifference >= -256 && rotationDifference <= 256) {
            anim = entity.walkAnimationId;
        } else if (rotationDifference >= 256 && rotationDifference < 768) {
            anim = entity.turnLeftAnimationId;
        } else if (rotationDifference >= -768 && rotationDifference <= -256) {
            anim = entity.turnRightAnimationId;
        }
		if (anim == -1) {
            anim = entity.walkAnimationId;
        }
		entity.queuedAnimationId = anim;
		int k1 = 4;
		if (entity.currentRotation != entity.turnDirection && entity.interactingEntity == -1
				&& entity.degreesToTurn != 0) {
            k1 = 2;
        }
		if (entity.waypointCount > 2) {
            k1 = 6;
        }
		if (entity.waypointCount > 3) {
            k1 = 8;
        }
		if (entity.stepsDelayed > 0 && entity.waypointCount > 1) {
			k1 = 8;
			entity.stepsDelayed--;
		}
		if (entity.waypointRan[entity.waypointCount - 1]) {
            k1 <<= 1;
        }
		if (k1 >= 8 && entity.queuedAnimationId == entity.walkAnimationId && entity.runAnimationId != -1) {
            entity.queuedAnimationId = entity.runAnimationId;
        }
		if (x1 < x2) {
			entity.x += k1;
			if (entity.x > x2) {
                entity.x = x2;
            }
		} else if (x1 > x2) {
			entity.x -= k1;
			if (entity.x < x2) {
                entity.x = x2;
            }
		}
		if (y1 < y2) {
			entity.y += k1;
			if (entity.y > y2) {
                entity.y = y2;
            }
		} else if (y1 > y2) {
			entity.y -= k1;
			if (entity.y < y2) {
                entity.y = y2;
            }
		}
		if (entity.x == x2 && entity.y == y2) {
			entity.waypointCount--;
			if (entity.stepsRemaining > 0) {
                entity.stepsRemaining--;
            }
		}
	}

	private boolean promptUserForInput(final RSInterface rsInterface) {
		final int contentType = rsInterface.contentType;
		if (this.friendListStatus == 2) {
			if (contentType == 201) {
				this.redrawChatbox = true;
				this.inputDialogState = 0;
				this.messagePromptRaised = true;
				this.promptInput = "";
				this.friendsListAction = 1;
				this.chatboxInputNeededString = "Enter name of friend to add to list";
			}
			if (contentType == 202) {
				this.redrawChatbox = true;
				this.inputDialogState = 0;
				this.messagePromptRaised = true;
				this.promptInput = "";
				this.friendsListAction = 2;
				this.chatboxInputNeededString = "Enter name of friend to delete from list";
			}
		}
		if (contentType == 205) {
			this.idleLogout = 250;
			return true;
		}
		if (contentType == 501) {
			this.redrawChatbox = true;
			this.inputDialogState = 0;
			this.messagePromptRaised = true;
			this.promptInput = "";
			this.friendsListAction = 4;
			this.chatboxInputNeededString = "Enter name of player to add to list";
		}
		if (contentType == 502) {
			this.redrawChatbox = true;
			this.inputDialogState = 0;
			this.messagePromptRaised = true;
			this.promptInput = "";
			this.friendsListAction = 5;
			this.chatboxInputNeededString = "Enter name of player to delete from list";
		}
		if (contentType >= 300 && contentType <= 313) {
			final int type = (contentType - 300) / 2;
			final int direction = contentType & 1;
			int currentId = this.characterEditIdentityKits[type];
			if (currentId != -1) {
				do {
					if (direction == 0 && --currentId < 0) {
                        currentId = IdentityKit.count - 1;
                    }
					if (direction == 1 && ++currentId >= IdentityKit.count) {
                        currentId = 0;
                    }
				} while (IdentityKit.cache[currentId].widgetDisplayed
						|| IdentityKit.cache[currentId].partId != type + (this.characterEditChangeGender ? 0 : 7));
				this.characterEditIdentityKits[type] = currentId;
				this.characterModelChanged = true;
			}
		}
		if (contentType >= 314 && contentType <= 323) {
			final int type = (contentType - 314) / 2;
			final int direction = contentType & 1;
			int currentId = this.characterEditColours[type];
			if (direction == 0 && --currentId < 0) {
                currentId = APPEARANCE_COLOURS[type].length - 1;
            }
			if (direction == 1 && ++currentId >= APPEARANCE_COLOURS[type].length) {
                currentId = 0;
            }
			this.characterEditColours[type] = currentId;
			this.characterModelChanged = true;
		}
		if (contentType == 324 && !this.characterEditChangeGender) {
			this.characterEditChangeGender = true;
			this.changeGender();
		}
		if (contentType == 325 && this.characterEditChangeGender) {
			this.characterEditChangeGender = false;
			this.changeGender();
		}
		if (contentType == 326) {
			this.stream.putOpcode(101);
			this.stream.put(this.characterEditChangeGender ? 0 : 1);
			for (int part = 0; part < 7; part++) {
				this.stream.put(this.characterEditIdentityKits[part]);
            }

			for (int part = 0; part < 5; part++) {
				this.stream.put(this.characterEditColours[part]);
            }

			return true;
		}
		if (contentType == 613) {
			this.reportAbuseMute = !this.reportAbuseMute;
        }
		if (contentType >= 601 && contentType <= 612) {
			this.clearTopInterfaces();
			if (this.reportAbuseInput.length() > 0) {
				this.stream.putOpcode(218);
				this.stream.putLong(TextClass.nameToLong(this.reportAbuseInput));
				this.stream.put(contentType - 601);
				this.stream.put(this.reportAbuseMute ? 1 : 0);
			}
		}
		return false;
	}

	private void pushMessage(final String message, final int type, final String name) {
		if (type == 0 && this.dialogID != -1) {
			this.clickToContinueString = message;
			super.clickType = 0;
		}
		if (this.chatboxInterfaceId == -1) {
			this.redrawChatbox = true;
        }
		for (int m = 99; m > 0; m--) {
			this.chatTypes[m] = this.chatTypes[m - 1];
			this.chatNames[m] = this.chatNames[m - 1];
			this.chatMessages[m] = this.chatMessages[m - 1];
		}

		this.chatTypes[0] = type;
		this.chatNames[0] = name;
		this.chatMessages[0] = message;
	}

	@Override
	public void redraw() {
		this.titleScreen.welcomeScreenRaised = true;
	}

	private void renderChatInterface(final int x, final int y, final int height, final int scrollPosition, final int scrollMaximum) {
		this.scrollBarUp.draw(x, y);
		this.scrollBarDown.draw(x, (y + height) - 16);
		DrawingArea.drawFilledRectangle(x, y + 16, 16, height - 32, this.SCROLLBAR_TRACK_COLOUR);
		int length = ((height - 32) * height) / scrollMaximum;
		if (length < 8) {
            length = 8;
        }
		final int scrollCurrent = ((height - 32 - length) * scrollPosition) / (scrollMaximum - height);
		DrawingArea.drawFilledRectangle(x, y + 16 + scrollCurrent, 16, length, this.SCROLLBAR_GRIP_FOREGROUND);
		DrawingArea.drawVerticalLine(x, y + 16 + scrollCurrent, length, this.SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawVerticalLine(x + 1, y + 16 + scrollCurrent, length, this.SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawHorizontalLine(y + 16 + scrollCurrent, x, 16, this.SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawHorizontalLine(y + 17 + scrollCurrent, x, 16, this.SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawVerticalLine(x + 15, y + 16 + scrollCurrent, length, this.SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawVerticalLine(x + 14, y + 17 + scrollCurrent, length - 1, this.SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawHorizontalLine(y + 15 + scrollCurrent + length, x, 16, this.SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawHorizontalLine(y + 14 + scrollCurrent + length, x + 1, 15, this.SCROLLBAR_GRIP_LOWLIGHT);
	}

	private void renderGameView() {
		this.renderCount++;
		this.renderPlayers(true);
		this.renderNPCs(true);
		this.renderPlayers(false);
		this.renderNPCs(false);
		this.renderProjectiles();
		this.renderStationaryGraphics();
		if (!this.cutsceneActive) {
			int vertical = cameraVertical;
			if (this.secondaryCameraVertical / 256 > vertical) {
                vertical = this.secondaryCameraVertical / 256;
            }
			if (this.customCameraActive[4] && this.cameraAmplitude[4] + 128 > vertical) {
                vertical = this.cameraAmplitude[4] + 128;
            }
			final int horizontal = cameraHorizontal + this.cameraRandomisationA & 0x7FF;
			this.setCameraPosition(this.currentCameraPositionH, this.currentCameraPositionV,
					this.getFloorDrawHeight(this.plane, localPlayer.y, localPlayer.x) - 50, horizontal, vertical);
		}
		final int cameraPlane;
		if (!this.cutsceneActive) {
            cameraPlane = this.getWorldDrawPlane();
        } else {
            cameraPlane = this.getCameraPlaneCutscene();
        }
		final int x = this.cameraPositionX;
		final int y = this.cameraPositionZ;
		final int z = this.cameraPositionY;
		final int curveY = this.cameraVerticalRotation;
		final int curveZ = this.cameraHorizontalRotation;
		for (int i = 0; i < 5; i++) {
            if (this.customCameraActive[i]) {
                final int randomisation = (int) ((Math.random() * (this.cameraJitter[i] * 2 + 1) - this.cameraJitter[i])
                        + Math.sin(this.unknownCameraVariable[i] * (this.cameraFrequency[i] / 100D)) * this.cameraAmplitude[i]);
                if (i == 0) {
					this.cameraPositionX += randomisation;
                }
                if (i == 1) {
					this.cameraPositionZ += randomisation;
                }
                if (i == 2) {
					this.cameraPositionY += randomisation;
                }
                if (i == 3) {
					this.cameraHorizontalRotation = this.cameraHorizontalRotation + randomisation & 0x7FF;
                }
                if (i == 4) {
					this.cameraVerticalRotation += randomisation;
                    if (this.cameraVerticalRotation < 128) {
						this.cameraVerticalRotation = 128;
                    }
                    if (this.cameraVerticalRotation > 383) {
						this.cameraVerticalRotation = 383;
                    }
                }
            }
        }

		final int textureId = Rasterizer.textureGetCount;
		Model.aBoolean1684 = true;
		Model.resourceCount = 0;
		Model.cursorX = super.mouseX - 4;
		Model.cursorY = super.mouseY - 4;
		DrawingArea.clear();
		this.worldController.render(this.cameraPositionX, this.cameraPositionY, this.cameraHorizontalRotation, this.cameraPositionZ, cameraPlane,
				this.cameraVerticalRotation);
		this.worldController.clearInteractiveObjectCache();
		this.updateEntities();
		this.drawHeadIcon();
		this.animateTexture(textureId);
		this.draw3dScreen();
		this.gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
		this.cameraPositionX = x;
		this.cameraPositionZ = y;
		this.cameraPositionY = z;
		this.cameraVerticalRotation = curveY;
		this.cameraHorizontalRotation = curveZ;
	}

	private void renderNPCs(final boolean flag) {
		for (int n = 0; n < this.npcCount; n++) {
			final NPC npc = this.npcs[this.npcIds[n]];
			int hash = 0x20000000 + (this.npcIds[n] << 14);
			if (npc == null || !npc.isVisible() || npc.npcDefinition.visible != flag) {
                continue;
            }
			final int npcWidth = npc.x >> 7;
			final int npcHeight = npc.y >> 7;
			if (npcWidth < 0 || npcWidth >= 104 || npcHeight < 0 || npcHeight >= 104) {
                continue;
            }
			if (npc.boundaryDimension == 1 && (npc.x & 0x7F) == 64 && (npc.y & 0x7F) == 64) {
				if (this.tileRenderCount[npcWidth][npcHeight] == this.renderCount) {
                    continue;
                }
				this.tileRenderCount[npcWidth][npcHeight] = this.renderCount;
			}
			if (!npc.npcDefinition.clickable) {
                hash += 0x80000000;
            }
			this.worldController.addEntity(this.plane, npc.x, npc.y, this.getFloorDrawHeight(this.plane, npc.y, npc.x),
					npc.currentRotation, npc, hash, (npc.boundaryDimension - 1) * 64 + 60, npc.dynamic);
		}
	}

	private void renderPlayers(final boolean localPlayerOnly) {
		if (localPlayer.x >> 7 == this.destinationX && localPlayer.y >> 7 == this.destinationY) {
			this.destinationX = 0;
        }
		int playersToRender = this.localPlayerCount;
		if (localPlayerOnly) {
            playersToRender = 1;
        }
		for (int p = 0; p < playersToRender; p++) {
			final Player player;
			final int hash;
			if (localPlayerOnly) {
				player = localPlayer;
				hash = this.LOCAL_PLAYER_ID << 14;
			} else {
				player = this.players[this.localPlayers[p]];
				hash = this.localPlayers[p] << 14;
			}
			if (player == null || !player.isVisible()) {
                continue;
            }
			player.preventRotation = (lowMemory && this.localPlayerCount > 50 || this.localPlayerCount > 200) && !localPlayerOnly
					&& player.queuedAnimationId == player.standAnimationId;
			final int x = player.x >> 7;
			final int y = player.y >> 7;
			if (x < 0 || x >= 104 || y < 0 || y >= 104) {
                continue;
            }
			if (player.playerModel != null && tick >= player.modifiedAppearanceStartTime
					&& tick < player.modifiedAppearanceEndTime) {
				player.preventRotation = false;
				player.drawHeight2 = this.getFloorDrawHeight(this.plane, player.y, player.x);
				this.worldController.addEntity(player.localX, player.localY, this.plane, player.x, player.y, player.drawHeight2,
						player.currentRotation, player.playerTileWidth, player.playerTileHeight, player, hash);
				continue;
			}
			if ((player.x & 0x7F) == 64 && (player.y & 0x7F) == 64) {
				if (this.tileRenderCount[x][y] == this.renderCount) {
                    continue;
                }
				this.tileRenderCount[x][y] = this.renderCount;
			}
			player.drawHeight2 = this.getFloorDrawHeight(this.plane, player.y, player.x);
			this.worldController.addEntity(this.plane, player.x, player.y, player.drawHeight2, player.currentRotation, player,
					hash, 60, player.dynamic);
		}

	}

	private void renderProjectiles() {
		for (Projectile projectile = (Projectile) this.projectileQueue
				.peekFront(); projectile != null; projectile = (Projectile) this.projectileQueue.getPrevious()) {
            if (projectile.plane != this.plane || tick > projectile.endCycle) {
                projectile.unlink();
            } else if (tick >= projectile.delay) {
                if (projectile.targetId > 0) {
                    final NPC npc = this.npcs[projectile.targetId - 1];
                    if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312) {
                        projectile.trackTarget(tick, npc.y,
								this.getFloorDrawHeight(projectile.plane, npc.y, npc.x) - projectile.endZ, npc.x);
                    }
                }
                if (projectile.targetId < 0) {
                    final int playerId = -projectile.targetId - 1;
                    final Player player;
                    if (playerId == this.playerListId) {
                        player = localPlayer;
                    } else {
                        player = this.players[playerId];
                    }
                    if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312) {
                        projectile.trackTarget(tick, player.y,
								this.getFloorDrawHeight(projectile.plane, player.y, player.x) - projectile.endZ, player.x);
                    }
                }
                projectile.move(this.animationTimePassed);
				this.worldController.addEntity(this.plane, (int) projectile.currentX, (int) projectile.currentY,
                        (int) projectile.currentZ, projectile.rotationY, projectile, -1, 60, false);
            }
        }

	}

	private void renderStationaryGraphics() {
		StationaryGraphic stationaryGraphic = (StationaryGraphic) this.stationaryGraphicQueue.peekFront();
		for (; stationaryGraphic != null; stationaryGraphic = (StationaryGraphic) this.stationaryGraphicQueue
				.getPrevious()) {
            if (stationaryGraphic.z != this.plane || stationaryGraphic.transformationCompleted) {
                stationaryGraphic.unlink();
            } else if (tick >= stationaryGraphic.stationaryGraphicLoopCycle) {
                stationaryGraphic.animationStep(this.animationTimePassed);
                if (stationaryGraphic.transformationCompleted) {
                    stationaryGraphic.unlink();
                } else {
					this.worldController.addEntity(stationaryGraphic.z, stationaryGraphic.x, stationaryGraphic.y,
                            stationaryGraphic.drawHeight, 0, stationaryGraphic, -1, 60, false);
                }
            }
        }

	}

	private boolean replayWave() {
		return signlink.wavereplay();
	}

	private Archive requestArchive(final int i, final String s, final String s1, final int j, final int k) {
		byte[] abyte0 = null;
		int l = 5;
		try {
			if (this.caches[0] != null) {
                abyte0 = this.caches[0].decompress(i);
            }
		} catch (final Exception _ex) {
		}
		if (abyte0 != null) {
			// aCRC32_930.reset();
			// aCRC32_930.update(abyte0);
			// int i1 = (int)aCRC32_930.getValue();
			// if(i1 != j)
		}
		if (abyte0 != null) {
			return new Archive(abyte0);
		}
		final int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			this.drawLoadingText(k, "Requesting " + s);
			try {
				int k1 = 0;
				final DataInputStream datainputstream = this.openJagGrabInputStream(s1 + j);
				final byte[] abyte1 = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				final Buffer stream = new Buffer(abyte1);
				stream.position = 3;
				final int i2 = stream.get3Bytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000) {
                        l2 = 1000;
                    }
					final int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					final int k3 = (j2 * 100) / i2;
					if (k3 != k1) {
						this.drawLoadingText(k, "Loading " + s + " - " + k3 + "%");
                    }
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (this.caches[0] != null) {
						this.caches[0].put(abyte0.length, abyte0, i);
                    }
				} catch (final Exception _ex) {
					this.caches[0] = null;
				}
				/*
				 * if(abyte0 != null) { aCRC32_930.reset(); aCRC32_930.update(abyte0); int i3 =
				 * (int)aCRC32_930.getValue(); if(i3 != j) { abyte0 = null; j1++; s2 =
				 * "Checksum error: " + i3; } }
				 */
			} catch (final IOException ioexception) {
				if (s2.equals("Unknown error")) {
                    s2 = "Connection error";
                }
				abyte0 = null;
			} catch (final NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!signlink.reporterror) {
                    return null;
                }
			} catch (final ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!signlink.reporterror) {
                    return null;
                }
			} catch (final Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!signlink.reporterror) {
                    return null;
                }
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						this.drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else {
						this.drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (final Exception _ex) {
					}
				}

				l *= 2;
				if (l > 60) {
                    l = 60;
                }
				this.aBoolean872 = !this.aBoolean872;
			}

		}

		return new Archive(abyte0);
	}

	private void setupLoginScreen() {
		if (this.titleScreen.imageProducersInitialised()) {
            return;
        }
		super.fullGameScreen = null;
		this.chatboxImageProducer = null;
		this.tabImageProducer = null;
		this.gameScreenImageProducer = null;
		this.chatSettingImageProducer = null;
		this.bottomSideIconImageProducer = null;
		this.topSideIconImageProducer = null;

		this.titleScreen.setupImageProducers(this.getGameComponent());

		if (this.archiveTitle != null) {
			this.loadTitleScreen();
		}
		this.titleScreen.welcomeScreenRaised = true;
	}

	private void setupGameplayScreen() {
		if (this.chatboxImageProducer != null) {
            return;
        }
		this.nullLoader();
		super.fullGameScreen = null;
		this.titleScreen.clearImageProducers();
		this.chatboxImageProducer = new RSImageProducer(479, 96, this.getGameComponent());
		this.minimap.setupImageProducer(this.getGameComponent());
		this.tabImageProducer = new RSImageProducer(190, 261, this.getGameComponent());
		this.gameScreenImageProducer = new RSImageProducer(512, 334, this.getGameComponent());
		DrawingArea.clear();
		this.chatSettingImageProducer = new RSImageProducer(496, 50, this.getGameComponent());
		this.bottomSideIconImageProducer = new RSImageProducer(269, 37, this.getGameComponent());
		this.topSideIconImageProducer = new RSImageProducer(249, 45, this.getGameComponent());
		this.titleScreen.welcomeScreenRaised = true;
	}

	private void resetModelCaches() {
		GameObjectDefinition.modelCache.clear();
		GameObjectDefinition.animatedModelCache.clear();
		EntityDefinition.modelCache.clear();
		ItemDefinition.modelCache.clear();
		ItemDefinition.spriteCache.clear();
		Player.mruNodes.clear();
		SpotAnimation.modelCache.clear();
	}

	@Override
	public void run() {
		if (this.drawFlames) {
			this.titleScreen.drawFlames2(super.gameGraphics, tick);
		} else {
			super.run();
		}
	}

	private void saveMidi(final boolean flag, final byte[] abyte0) {
		signlink.midiFade = flag ? 1 : 0;
		signlink.midisave(abyte0, abyte0.length);
	}

	private boolean saveWave(final byte[] abyte0, final int i) {
		return abyte0 == null || signlink.wavesave(abyte0, i);
	}

	private void scrollInterface(final int i, final int j, final int k, final int l, final RSInterface rsInterface, final int i1, final boolean redrawTabArea,
								 final int j1) {
		final int anInt992;
		if (this.aBoolean972) {
            anInt992 = 32;
        } else {
            anInt992 = 0;
        }
		this.aBoolean972 = false;
		if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
			rsInterface.scrollPosition -= this.anInt1213 * 4;
			if (redrawTabArea) {
				this.redrawTab = true;
			}
		} else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
			rsInterface.scrollPosition += this.anInt1213 * 4;
			if (redrawTabArea) {
				this.redrawTab = true;
			}
		} else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && this.anInt1213 > 0) {
			int l1 = ((j - 32) * j) / j1;
			if (l1 < 8) {
                l1 = 8;
            }
			final int i2 = l - i1 - 16 - l1 / 2;
			final int j2 = j - 32 - l1;
			rsInterface.scrollPosition = ((j1 - j) * i2) / j2;
			if (redrawTabArea) {
				this.redrawTab = true;
            }
			this.aBoolean972 = true;
		}
	}

	private void setCameraPosition(final int x, final int y, final int z, final int horizontal, final int vertical) {
		final int verticalDifference = 2048 - vertical & 0x7FF;
		final int horizontalDifference = 2048 - horizontal & 0x7FF;
		int offsetX = 0;
		int offsetZ = 0;
		int offsetY = 600 + vertical * 3;
		if (verticalDifference != 0) {
			final int sine = Model.SINE[verticalDifference];
			final int cos = Model.COSINE[verticalDifference];
			final int tmp = offsetZ * cos - offsetY * sine >> 16;
			offsetY = offsetZ * sine + offsetY * cos >> 16;
			offsetZ = tmp;
		}
		if (horizontalDifference != 0) {
			final int sin = Model.SINE[horizontalDifference];
			final int cos = Model.COSINE[horizontalDifference];
			final int tmp = offsetY * sin + offsetX * cos >> 16;
			offsetY = offsetY * cos - offsetX * sin >> 16;
			offsetX = tmp;
		}
		this.cameraPositionX = x - offsetX;
		this.cameraPositionZ = z - offsetZ;
		this.cameraPositionY = y - offsetY;
		this.cameraVerticalRotation = vertical;
		this.cameraHorizontalRotation = horizontal;
	}

	private void setCutsceneCamera() {
		int x = this.anInt1098 * 128 + 64;
		int y = this.anInt1099 * 128 + 64;
		int z = this.getFloorDrawHeight(this.plane, y, x) - this.anInt1100;
		if (this.cameraPositionX < x) {
			this.cameraPositionX += this.anInt1101 + ((x - this.cameraPositionX) * this.anInt1102) / 1000;
			if (this.cameraPositionX > x) {
				this.cameraPositionX = x;
            }
		}
		if (this.cameraPositionX > x) {
			this.cameraPositionX -= this.anInt1101 + ((this.cameraPositionX - x) * this.anInt1102) / 1000;
			if (this.cameraPositionX < x) {
				this.cameraPositionX = x;
            }
		}
		if (this.cameraPositionZ < z) {
			this.cameraPositionZ += this.anInt1101 + ((z - this.cameraPositionZ) * this.anInt1102) / 1000;
			if (this.cameraPositionZ > z) {
				this.cameraPositionZ = z;
            }
		}
		if (this.cameraPositionZ > z) {
			this.cameraPositionZ -= this.anInt1101 + ((this.cameraPositionZ - z) * this.anInt1102) / 1000;
			if (this.cameraPositionZ < z) {
				this.cameraPositionZ = z;
            }
		}
		if (this.cameraPositionY < y) {
			this.cameraPositionY += this.anInt1101 + ((y - this.cameraPositionY) * this.anInt1102) / 1000;
			if (this.cameraPositionY > y) {
				this.cameraPositionY = y;
            }
		}
		if (this.cameraPositionY > y) {
			this.cameraPositionY -= this.anInt1101 + ((this.cameraPositionY - y) * this.anInt1102) / 1000;
			if (this.cameraPositionY < y) {
				this.cameraPositionY = y;
            }
		}
		x = this.anInt995 * 128 + 64;
		y = this.anInt996 * 128 + 64;
		z = this.getFloorDrawHeight(this.plane, y, x) - this.cameraOffsetZ;
		final int distanceX = x - this.cameraPositionX;
		final int distanceZ = z - this.cameraPositionZ;
		final int distanceY = y - this.cameraPositionY;
		final int distanceScalar = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		int curveHorizontal = (int) (Math.atan2(distanceZ, distanceScalar) * 325.94900000000001D) & 0x7FF;
		final int curveVertical = (int) (Math.atan2(distanceX, distanceY) * -325.94900000000001D) & 0x7FF;
		if (curveHorizontal < 128) {
            curveHorizontal = 128;
        }
		if (curveHorizontal > 383) {
            curveHorizontal = 383;
        }
		if (this.cameraVerticalRotation < curveHorizontal) {
			this.cameraVerticalRotation += this.anInt998 + ((curveHorizontal - this.cameraVerticalRotation) * this.anInt999) / 1000;
			if (this.cameraVerticalRotation > curveHorizontal) {
				this.cameraVerticalRotation = curveHorizontal;
            }
		}
		if (this.cameraVerticalRotation > curveHorizontal) {
			this.cameraVerticalRotation -= this.anInt998 + ((this.cameraVerticalRotation - curveHorizontal) * this.anInt999) / 1000;
			if (this.cameraVerticalRotation < curveHorizontal) {
				this.cameraVerticalRotation = curveHorizontal;
            }
		}
		int _vertical1 = curveVertical - this.cameraHorizontalRotation;
		if (_vertical1 > 1024) {
            _vertical1 -= 2048;
        }
		if (_vertical1 < -1024) {
            _vertical1 += 2048;
        }
		if (_vertical1 > 0) {
			this.cameraHorizontalRotation += this.anInt998 + (_vertical1 * this.anInt999) / 1000;
			this.cameraHorizontalRotation &= 0x7FF;
		}
		if (_vertical1 < 0) {
			this.cameraHorizontalRotation -= this.anInt998 + (-_vertical1 * this.anInt999) / 1000;
			this.cameraHorizontalRotation &= 0x7FF;
		}
		int _vertical2 = curveVertical - this.cameraHorizontalRotation;
		if (_vertical2 > 1024) {
            _vertical2 -= 2048;
        }
		if (_vertical2 < -1024) {
            _vertical2 += 2048;
        }
		if (_vertical2 < 0 && _vertical1 > 0 || _vertical2 > 0 && _vertical1 < 0) {
			this.cameraHorizontalRotation = curveVertical;
        }
	}

	private void setStandardCameraPosition() {
		try {
			final int x = localPlayer.x + this.cameraRandomisationH;
			final int y = localPlayer.y + this.cameraRandomisationV;
			if (this.currentCameraPositionH - x < -500 || this.currentCameraPositionH - x > 500
					|| this.currentCameraPositionV - y < -500 || this.currentCameraPositionV - y > 500) {
				this.currentCameraPositionH = x;
				this.currentCameraPositionV = y;
			}

			if (this.currentCameraPositionH != x) {
				this.currentCameraPositionH += (x - this.currentCameraPositionH) / 16;
            }
			if (this.currentCameraPositionV != y) {
				this.currentCameraPositionV += (y - this.currentCameraPositionV) / 16;
            }
			if (super.keyStatus[1] == 1) {
				this.cameraModificationH += (-24 - this.cameraModificationH) / 2;
            } else if (super.keyStatus[2] == 1) {
				this.cameraModificationH += (24 - this.cameraModificationH) / 2;
            } else {
				this.cameraModificationH /= 2;
            }
			if (super.keyStatus[3] == 1) {
				this.cameraModificationV += (12 - this.cameraModificationV) / 2;
            } else if (super.keyStatus[4] == 1) {
				this.cameraModificationV += (-12 - this.cameraModificationV) / 2;
            } else {
				this.cameraModificationV /= 2;
            }
			cameraHorizontal = cameraHorizontal + this.cameraModificationH / 2 & 0x7FF;
			cameraVertical += this.cameraModificationV / 2;
			if (cameraVertical < 128) {
                cameraVertical = 128;
            }
			if (cameraVertical > 383) {
                cameraVertical = 383;
            }

			final int maximumX = this.currentCameraPositionH >> 7;
			final int maximumY = this.currentCameraPositionV >> 7;
			final int drawHeight = this.getFloorDrawHeight(this.plane, this.currentCameraPositionV, this.currentCameraPositionH);
			int maximumDrawHeight = 0;
			if (maximumX > 3 && maximumY > 3 && maximumX < 100 && maximumY < 100) {
				for (int _x = maximumX - 4; _x <= maximumX + 4; _x++) {
					for (int _y = maximumY - 4; _y <= maximumY + 4; _y++) {
						int _z = this.plane;
						if (_z < 3 && (this.tileFlags[1][_x][_y] & 2) == 2) {
                            _z++;
                        }
						final int h = drawHeight - this.intGroundArray[_z][_x][_y];
						if (h > maximumDrawHeight) {
                            maximumDrawHeight = h;
                        }
					}
				}
			}
			int h = maximumDrawHeight * 192;
			if (h > 0x17f00) {
                h = 0x17f00;
            }
			if (h < 32768) {
                h = 32768;
            }
			if (h > this.secondaryCameraVertical) {
				this.secondaryCameraVertical += (h - this.secondaryCameraVertical) / 24;
				return;
			}
			if (h < this.secondaryCameraVertical) {
				this.secondaryCameraVertical += (h - this.secondaryCameraVertical) / 80;
			}
		} catch (final Exception _ex) {
			signlink.reporterror("glfc_ex " + localPlayer.x + "," + localPlayer.y + "," + this.currentCameraPositionH + ","
					+ this.currentCameraPositionV + "," + this.regionX + "," + this.regionY + "," + this.baseX + "," + this.baseY);
			throw new RuntimeException("eek");
		}
	}

	private void setWaveVolume(final int i) {
		signlink.wavevol = i;
	}

	private void showErrorScreen() {
		final Graphics g = this.getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		this.setFrameRate(1);
		if (this.loadingError) {
			this.titleScreen.currentlyDrawingFlames = false;
			g.setFont(new Font("Helvetica", Font.BOLD, 16));
			g.setColor(Color.yellow);
			int currentPositionY = 35;
			g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", Font.BOLD, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("3: Try using a different game-world", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("4: Try rebooting your computer", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, currentPositionY);
		}
		if (this.genericLoadingError) {
			this.titleScreen.currentlyDrawingFlames = false;
			g.setFont(new Font("Helvetica", Font.BOLD, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play RuneScape make sure you play from", 50, 100);
			g.drawString("http://www.runescape.com", 50, 150);
		}
		if (this.rsAlreadyLoaded) {
			this.titleScreen.currentlyDrawingFlames = false;
			g.setColor(Color.yellow);
			int currentPositionY = 35;
			g.drawString("Error a copy of RuneScape already appears to be loaded", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", Font.BOLD, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, currentPositionY);
			currentPositionY += 30;
		}
	}

	private void spawnGameObjects() {
		if (this.loadingStage == 2) {
			for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
					.peekFront(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) this.spawnObjectList
							.getPrevious()) {
				if (spawnRequest.delayUntilRespawn > 0) {
                    spawnRequest.delayUntilRespawn--;
                }
				if (spawnRequest.delayUntilRespawn == 0) {
					if (spawnRequest.id < 0 || Region.modelTypeCached(spawnRequest.id, spawnRequest.type)) {
						this.despawnGameObject(spawnRequest.y, spawnRequest.z, spawnRequest.face, spawnRequest.type,
								spawnRequest.x, spawnRequest.objectType, spawnRequest.id);
						spawnRequest.unlink();
					}
				} else {
					if (spawnRequest.delayUntilSpawn > 0) {
                        spawnRequest.delayUntilSpawn--;
                    }
					if (spawnRequest.delayUntilSpawn == 0 && spawnRequest.x >= 1 && spawnRequest.y >= 1
							&& spawnRequest.x <= 102 && spawnRequest.y <= 102
							&& (spawnRequest.id2 < 0 || Region.modelTypeCached(spawnRequest.id2, spawnRequest.type2))) {
						this.despawnGameObject(spawnRequest.y, spawnRequest.z, spawnRequest.face2, spawnRequest.type2,
								spawnRequest.x, spawnRequest.objectType, spawnRequest.id2);
						spawnRequest.delayUntilSpawn = -1;
						if (spawnRequest.id2 == spawnRequest.id && spawnRequest.id == -1) {
                            spawnRequest.unlink();
                        } else if (spawnRequest.id2 == spawnRequest.id && spawnRequest.face2 == spawnRequest.face
								&& spawnRequest.type2 == spawnRequest.type) {
                            spawnRequest.unlink();
                        }
					}
				}
			}
		}
	}

	private void spawnGroundItem(final int x, final int y) {
		final DoubleEndedQueue groundItemList = this.groundArray[this.plane][x][y];
		if (groundItemList == null) {
			this.worldController.removeGroundItemTile(x, y, this.plane);
			return;
		}
		int highestValue = -99999999;
		Object item = null;
		for (Item currentItem = (Item) groundItemList
				.peekFront(); currentItem != null; currentItem = (Item) groundItemList.getPrevious()) {
			final ItemDefinition itemDef = ItemDefinition.getDefinition(currentItem.itemId);
			int value = itemDef.value;
			if (itemDef.stackable) {
                value *= currentItem.itemCount + 1;
            }
			if (value > highestValue) {
				highestValue = value;
				item = currentItem;
			}
		}

		groundItemList.pushFront(((Linkable) (item)));
		Object secondItem = null;
		Object thirdItem = null;
		for (Item currentItem = (Item) groundItemList
				.peekFront(); currentItem != null; currentItem = (Item) groundItemList.getPrevious()) {
			if (currentItem.itemId != ((Item) (item)).itemId && secondItem == null) {
                secondItem = currentItem;
            }
			if (currentItem.itemId != ((Item) (item)).itemId && currentItem.itemId != ((Item) (secondItem)).itemId
					&& thirdItem == null) {
                thirdItem = currentItem;
            }
		}

		final int hash = x + (y << 7) + 0x60000000;
		this.worldController.addGroundItemTile(x, y, this.plane, this.getFloorDrawHeight(this.plane, y * 128 + 64, x * 128 + 64), hash,
				((Animable) (item)), ((Animable) (secondItem)), ((Animable) (thirdItem)));
	}

	@Override
	public void startRunnable(final Runnable runnable, int priority) {
		if (priority > 10) {
            priority = 10;
        }
		if (signlink.applet != null) {
			signlink.startThread(runnable, priority);
		} else {
			super.startRunnable(runnable, priority);
		}
	}

	@Override
	void startUp() {
		this.drawLoadingText(20, "Starting up");
		if (signlink.sunjava) {
            super.minDelay = 5;
        }
		if (clientRunning) {
			// rsAlreadyLoaded = true;
			// return;
		}
		clientRunning = true;
		boolean validHost = true;
		final String s = this.getDocumentBaseHost();
		if (s.endsWith("jagex.com")) {
            validHost = true;
        }
		if (s.endsWith("runescape.com")) {
            validHost = true;
        }
		if (s.endsWith("192.168.1.2")) {
            validHost = true;
        }
		if (s.endsWith("192.168.1.229")) {
            validHost = true;
        }
		if (s.endsWith("192.168.1.228")) {
            validHost = true;
        }
		if (s.endsWith("192.168.1.227")) {
            validHost = true;
        }
		if (s.endsWith("192.168.1.226")) {
            validHost = true;
        }
		if (s.endsWith("127.0.0.1")) {
            validHost = true;
        }
		if (!validHost) {
			this.genericLoadingError = true;
			return;
		}
		if (signlink.cache_dat != null) {
			for (int i = 0; i < 5; i++) {
				this.caches[i] = new FileCache(signlink.cache_dat, signlink.cache_idx[i], i + 1);
            }

		}
		try {
			this.connectServer();
			this.archiveTitle = this.requestArchive(1, "title screen", "title", this.expectedCRCs[1], 25);
			this.fontSmall = new GameFont("p11_full", this.archiveTitle, false);
			this.fontPlain = new GameFont("p12_full", this.archiveTitle, false);
			this.fontBold = new GameFont("b12_full", this.archiveTitle, false);
			final GameFont fontFancy = new GameFont("q8_full", this.archiveTitle, true);

			this.titleScreen.load(this, this.archiveTitle, this.fontSmall, this.fontPlain, this.fontBold);

			this.loadTitleScreen();
			final Archive archiveConfig = this.requestArchive(2, "config", "config", this.expectedCRCs[2], 30);
			final Archive archiveInterface = this.requestArchive(3, "interface", "interface", this.expectedCRCs[3], 35);
			final Archive archiveMedia = this.requestArchive(4, "2d graphics", "media", this.expectedCRCs[4], 40);
			final Archive archiveTextures = this.requestArchive(6, "textures", "textures", this.expectedCRCs[6], 45);
			final Archive archiveWord = this.requestArchive(7, "chat system", "wordenc", this.expectedCRCs[7], 50);
			final Archive archiveSounds = this.requestArchive(8, "sound effects", "sounds", this.expectedCRCs[8], 55);
			this.tileFlags = new byte[4][104][104];
			this.intGroundArray = new int[4][105][105];
			this.worldController = new WorldController(this.intGroundArray);
			for (int z = 0; z < 4; z++) {
				this.currentCollisionMap[z] = new CollisionMap();
            }

			final Archive archiveVersions = this.requestArchive(5, "update list", "versionlist", this.expectedCRCs[5], 60);
			this.drawLoadingText(60, "Connecting to update server");
			this.onDemandFetcher = new OnDemandFetcher();
			this.onDemandFetcher.start(archiveVersions, this);
			Animation.init(this.onDemandFetcher.getAnimCount());
			Model.init(this.onDemandFetcher.fileCount(0), this.onDemandFetcher);
			if (!lowMemory) {
				this.nextSong = 0;
				try {
					this.nextSong = Integer.parseInt(this.getParameter("music"));
				} catch (final Exception _ex) {
				}
				this.songChanging = true;
				this.onDemandFetcher.request(2, this.nextSong);
				while (this.onDemandFetcher.immediateRequestCount() > 0) {
					this.processOnDemandQueue();
					try {
						Thread.sleep(100L);
					} catch (final Exception _ex) {
					}
					if (this.onDemandFetcher.failedRequests > 3) {
						this.loadError();
						return;
					}
				}
			}
			this.drawLoadingText(65, "Requesting animations");
			int fileRequestCount = this.onDemandFetcher.fileCount(1);
			for (int id = 0; id < fileRequestCount; id++) {
				this.onDemandFetcher.request(1, id);
            }

			while (this.onDemandFetcher.immediateRequestCount() > 0) {
				final int remaining = fileRequestCount - this.onDemandFetcher.immediateRequestCount();
				if (remaining > 0) {
					this.drawLoadingText(65, "Loading animations - " + (remaining * 100) / fileRequestCount + "%");
                }
				this.processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch (final Exception _ex) {
				}
				if (this.onDemandFetcher.failedRequests > 3) {
					this.loadError();
					return;
				}
			}
			this.drawLoadingText(70, "Requesting models");
			fileRequestCount = this.onDemandFetcher.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				final int modelId = this.onDemandFetcher.getModelId(id);
				if ((modelId & 1) != 0) {
					this.onDemandFetcher.request(0, id);
                }
			}

			fileRequestCount = this.onDemandFetcher.immediateRequestCount();
			while (this.onDemandFetcher.immediateRequestCount() > 0) {
				final int remaining = fileRequestCount - this.onDemandFetcher.immediateRequestCount();
				if (remaining > 0) {
					this.drawLoadingText(70, "Loading models - " + (remaining * 100) / fileRequestCount + "%");
                }
				this.processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch (final Exception _ex) {
				}
			}
			if (this.caches[0] != null) {
				this.drawLoadingText(75, "Requesting maps");
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 47, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 47, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 48, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 48, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 49, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 49, 48));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 47, 47));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 47, 47));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 48, 47));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 48, 47));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(0, 48, 148));
				this.onDemandFetcher.request(3, this.onDemandFetcher.getMapId(1, 48, 148));
				fileRequestCount = this.onDemandFetcher.immediateRequestCount();
				while (this.onDemandFetcher.immediateRequestCount() > 0) {
					final int remaining = fileRequestCount - this.onDemandFetcher.immediateRequestCount();
					if (remaining > 0) {
						this.drawLoadingText(75, "Loading maps - " + (remaining * 100) / fileRequestCount + "%");
                    }
					this.processOnDemandQueue();
					try {
						Thread.sleep(100L);
					} catch (final Exception _ex) {
					}
				}
			}
			fileRequestCount = this.onDemandFetcher.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				final int modelId = this.onDemandFetcher.getModelId(id);
				byte priority = 0;
				if ((modelId & 8) != 0) {
                    priority = 10;
                } else if ((modelId & 0x20) != 0) {
                    priority = 9;
                } else if ((modelId & 0x10) != 0) {
                    priority = 8;
                } else if ((modelId & 0x40) != 0) {
                    priority = 7;
                } else if ((modelId & 0x80) != 0) {
                    priority = 6;
                } else if ((modelId & 2) != 0) {
                    priority = 5;
                } else if ((modelId & 4) != 0) {
                    priority = 4;
                }
				if ((modelId & 1) != 0) {
                    priority = 3;
                }
				if (priority != 0) {
					this.onDemandFetcher.setPriority(priority, 0, id);
                }
			}

			this.onDemandFetcher.preloadRegions(membersWorld);
			if (!lowMemory) {
				final int count = this.onDemandFetcher.fileCount(2);
				for (int id = 1; id < count; id++) {
                    if (this.onDemandFetcher.midiIdEqualsOne(id)) {
						this.onDemandFetcher.setPriority((byte) 1, 2, id);
                    }
                }

			}
			this.drawLoadingText(80, "Unpacking media");
			this.inventoryBackgroundImage = new IndexedImage(archiveMedia, "invback", 0);
			this.chatBackgroundImage = new IndexedImage(archiveMedia, "chatback", 0);

			this.minimap.load(archiveMedia);

			this.backBase1Image = new IndexedImage(archiveMedia, "backbase1", 0);
			this.backBase2Image = new IndexedImage(archiveMedia, "backbase2", 0);
			this.backHmid1Image = new IndexedImage(archiveMedia, "backhmid1", 0);
			for (int icon = 0; icon < 13; icon++) {
				this.sideIconImage[icon] = new IndexedImage(archiveMedia, "sideicons", icon);
            }

			try {
				for (int i = 0; i < 20; i++) {
					this.hitMarkImage[i] = new Sprite(archiveMedia, "hitmarks", i);
				}

			} catch (final Exception _ex) {
			}
			try {
				for (int i = 0; i < 20; i++) {
					this.headIcons[i] = new Sprite(archiveMedia, "headicons", i);
				}
			} catch (final Exception _ex) {
				_ex.printStackTrace();
			}
			for (int i = 0; i < 8; i++) {
				this.crosses[i] = new Sprite(archiveMedia, "cross", i);
            }

			this.scrollBarUp = new IndexedImage(archiveMedia, "scrollbar", 0);
			this.scrollBarDown = new IndexedImage(archiveMedia, "scrollbar", 1);
			this.redStone1 = new IndexedImage(archiveMedia, "redstone1", 0);
			this.redStone2 = new IndexedImage(archiveMedia, "redstone2", 0);
			this.redStone3 = new IndexedImage(archiveMedia, "redstone3", 0);
			this.redStone1_2 = new IndexedImage(archiveMedia, "redstone1", 0);
			this.redStone1_2.flipHorizontally();
			this.redStone2_2 = new IndexedImage(archiveMedia, "redstone2", 0);
			this.redStone2_2.flipHorizontally();
			this.redStone1_3 = new IndexedImage(archiveMedia, "redstone1", 0);
			this.redStone1_3.flipVertically();
			this.redStone2_3 = new IndexedImage(archiveMedia, "redstone2", 0);
			this.redStone2_3.flipVertically();
			this.redStone3_2 = new IndexedImage(archiveMedia, "redstone3", 0);
			this.redStone3_2.flipVertically();
			this.redStone1_4 = new IndexedImage(archiveMedia, "redstone1", 0);
			this.redStone1_4.flipHorizontally();
			this.redStone1_4.flipVertically();
			this.redStone2_4 = new IndexedImage(archiveMedia, "redstone2", 0);
			this.redStone2_4.flipHorizontally();
			this.redStone2_4.flipVertically();
			for (int i = 0; i < 2; i++) {
				this.modIcons[i] = new IndexedImage(archiveMedia, "mod_icons", i);
            }

			Sprite sprite = new Sprite(archiveMedia, "backleft1", 0);
			this.backLeftIP1 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backleft2", 0);
			this.backLeftIP2 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backright1", 0);
			this.backRightIP1 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backright2", 0);
			this.backRightIP2 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backtop1", 0);
			this.backTopIP1 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid1", 0);
			this.backVmidIP1 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid2", 0);
			this.backVmidIP2 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid3", 0);
			this.backVmidIP3 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backhmid2", 0);
			this.backVmidIP2_2 = new RSImageProducer(sprite.width, sprite.height, this.getGameComponent());
			sprite.drawInverse(0, 0);

			this.drawLoadingText(83, "Unpacking textures");
			Rasterizer.unpackTextures(archiveTextures);
			Rasterizer.calculatePalette(0.80000000000000004D);
			Rasterizer.resetTextures();
			this.drawLoadingText(86, "Unpacking config");
			AnimationSequence.unpackConfig(archiveConfig);
			GameObjectDefinition.load(archiveConfig);
			FloorDefinition.load(archiveConfig);
			ItemDefinition.load(archiveConfig);
			EntityDefinition.load(archiveConfig);
			IdentityKit.load(archiveConfig);
			SpotAnimation.load(archiveConfig);
			Varp.load(archiveConfig);
			VarBit.load(archiveConfig);
			ItemDefinition.membersWorld = membersWorld;
			if (!lowMemory) {
				this.drawLoadingText(90, "Unpacking sounds");
				final byte[] soundData = archiveSounds.decompressFile("sounds.dat");
				final Buffer stream = new Buffer(soundData);
				Effect.load(stream);
			}
			this.drawLoadingText(95, "Unpacking interfaces");
			final GameFont[] fonts = {this.fontSmall, this.fontPlain, this.fontBold, fontFancy};
			RSInterface.unpack(archiveInterface, fonts, archiveMedia);
			this.drawLoadingText(100, "Preparing game engine");

			Rasterizer.setBounds(479, 96);
			this.chatboxLineOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(190, 261);
			this.sidebarOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(512, 334);
			this.viewportOffsets = Rasterizer.lineOffsets;

			final int[] ai = new int[9];
			for (int i8 = 0; i8 < 9; i8++) {
				final int k8 = 128 + i8 * 32 + 15;
				final int l8 = 600 + k8 * 3;
				final int i9 = Rasterizer.SINE[k8];
				ai[i8] = l8 * i9 >> 16;
			}

			WorldController.setupViewport(500, 800, 512, 334, ai);
			Censor.load(archiveWord);
			this.mouseDetection = new MouseDetection(this);
			this.startRunnable(this.mouseDetection, 10);
			GameObject.clientInstance = this;
			GameObjectDefinition.clientInstance = this;
			EntityDefinition.clientInstance = this;
			return;
		} catch (final Exception exception) {
			signlink.reporterror("loaderror " + this.loadingBarText + " " + this.loadingBarPercentage);
		}
		this.loadingError = true;
	}

	private void stopMidi() {
		signlink.midiFade = 0;
		signlink.midi = "stop";
	}

	private void updateEntities() {
		try {
			int overheadMessage = 0;
			for (int entity = -1; entity < this.localPlayerCount + this.npcCount; entity++) {
				final Entity target;
				if (entity == -1) {
                    target = localPlayer;
                } else if (entity < this.localPlayerCount) {
                    target = this.players[this.localPlayers[entity]];
                } else {
                    target = this.npcs[this.npcIds[entity - this.localPlayerCount]];
                }
				if (target == null || !target.isVisible()) {
                    continue;
                }
				if (target instanceof NPC) {
					EntityDefinition definition = ((NPC) target).npcDefinition;
					if (definition.childrenIDs != null) {
                        definition = definition.getChildDefinition();
                    }
					if (definition == null) {
                        continue;
                    }
				}
				if (entity < this.localPlayerCount) {
					int height = 30;
					final Player player = (Player) target;
					if (player.headIcon != 0) {
						this.calculateEntityScreenPosition(((target)), target.height + 15);
						if (this.spriteDrawX > -1) {
							for (int icon = 0; icon < 8; icon++) {
                                if ((player.headIcon & 1 << icon) != 0) {
									this.headIcons[icon].drawImage(this.spriteDrawX - 12, this.spriteDrawY - height);
                                    height -= 25;
                                }
                            }

						}
					}
					if (entity >= 0 && this.hintIconType == 10 && this.hintIconPlayerId == this.localPlayers[entity]) {
						this.calculateEntityScreenPosition(((target)), target.height + 15);
						if (this.spriteDrawX > -1) {
							this.headIcons[7].drawImage(this.spriteDrawX - 12, this.spriteDrawY - height);
                        }
					}
				} else {
					final EntityDefinition definition = ((NPC) target).npcDefinition;
					if (definition.headIcon >= 0 && definition.headIcon < this.headIcons.length) {
						this.calculateEntityScreenPosition(((target)), target.height + 15);
						if (this.spriteDrawX > -1) {
							this.headIcons[definition.headIcon].drawImage(this.spriteDrawX - 12, this.spriteDrawY - 30);
                        }
					}
					if (this.hintIconType == 1 && this.hintIconNpcId == this.npcIds[entity - this.localPlayerCount] && tick % 20 < 10) {
						this.calculateEntityScreenPosition(((target)), target.height + 15);
						if (this.spriteDrawX > -1) {
							this.headIcons[2].drawImage(this.spriteDrawX - 12, this.spriteDrawY - 28);
                        }
					}
				}
				if (target.overheadTextMessage != null && (entity >= this.localPlayerCount || this.publicChatMode == 0
						|| this.publicChatMode == 3 || this.publicChatMode == 1 && this.isFriendOrSelf(((Player) target).name))) {
					this.calculateEntityScreenPosition(((target)), target.height);
					if (this.spriteDrawX > -1 && overheadMessage < this.overheadMessageCount) {
						this.overheadTextWidth[overheadMessage] = this.fontBold.getTextWidth(target.overheadTextMessage) / 2;
						this.overheadTextHeight[overheadMessage] = this.fontBold.fontHeight;
						this.overheadTextDrawX[overheadMessage] = this.spriteDrawX;
						this.overheadTextDrawY[overheadMessage] = this.spriteDrawY;
						this.overheadTextColour[overheadMessage] = target.chatColour;
						this.overheadTextEffect[overheadMessage] = target.chatEffect;
						this.overheadTextCycle[overheadMessage] = target.textCycle;
						this.overheadTextMessage[overheadMessage++] = target.overheadTextMessage;
						if (this.chatEffectsDisabled == 0 && target.chatEffect >= 1 && target.chatEffect <= 3) {
							this.overheadTextHeight[overheadMessage] += 10;
							this.overheadTextDrawY[overheadMessage] += 5;
						}
						if (this.chatEffectsDisabled == 0 && target.chatEffect == 4) {
							this.overheadTextWidth[overheadMessage] = 60;
                        }
						if (this.chatEffectsDisabled == 0 && target.chatEffect == 5) {
							this.overheadTextHeight[overheadMessage] += 5;
                        }
					}
				}
				if (target.loopCycleStatus > tick) {
					try {
						this.calculateEntityScreenPosition(((target)), target.height + 15);
						if (this.spriteDrawX > -1) {
							int percentage = (target.currentHealth * 30) / target.maxHealth;
							if (percentage > 30) {
                                percentage = 30;
                            }
							DrawingArea.drawFilledRectangle(this.spriteDrawX - 15, this.spriteDrawY - 3, percentage, 5, 0x00FF00);
							DrawingArea.drawFilledRectangle((this.spriteDrawX - 15) + percentage, this.spriteDrawY - 3,
									30 - percentage, 5, 0xFF0000);
						}
					} catch (final Exception e) {
					}
				}
				for (int hit = 0; hit < 4; hit++) {
                    if (target.hitsLoopCycle[hit] > tick) {
						this.calculateEntityScreenPosition(((target)), target.height / 2);
                        if (this.spriteDrawX > -1) {
                            if (hit == 1) {
								this.spriteDrawY -= 20;
                            }
                            if (hit == 2) {
								this.spriteDrawX -= 15;
								this.spriteDrawY -= 10;
                            }
                            if (hit == 3) {
								this.spriteDrawX += 15;
								this.spriteDrawY -= 10;
                            }
							this.hitMarkImage[target.hitMarkTypes[hit]].drawImage(this.spriteDrawX - 12, this.spriteDrawY - 12);
							this.fontSmall.drawCentredText(String.valueOf(target.hitArray[hit]), this.spriteDrawX,
									this.spriteDrawY + 4, 0);
							this.fontSmall.drawCentredText(String.valueOf(target.hitArray[hit]), this.spriteDrawX - 1,
									this.spriteDrawY + 3, 0xFFFFFF);
                        }
                    }
                }

			}
			for (int m = 0; m < overheadMessage; m++) {
				final int x = this.overheadTextDrawX[m];
				int y = this.overheadTextDrawY[m];
				final int w = this.overheadTextWidth[m];
				final int h = this.overheadTextHeight[m];
				boolean messagesRemaining = true;
				while (messagesRemaining) {
					messagesRemaining = false;
					for (int _m = 0; _m < m; _m++) {
                        if (y + 2 > this.overheadTextDrawY[_m] - this.overheadTextHeight[_m] && y - h < this.overheadTextDrawY[_m] + 2
                                && x - w < this.overheadTextDrawX[_m] + this.overheadTextWidth[_m]
                                && x + w > this.overheadTextDrawX[_m] - this.overheadTextWidth[_m]
                                && this.overheadTextDrawY[_m] - this.overheadTextHeight[_m] < y) {
                            y = this.overheadTextDrawY[_m] - this.overheadTextHeight[_m];
                            messagesRemaining = true;
                        }
                    }

				}
				this.spriteDrawX = this.overheadTextDrawX[m];
				this.spriteDrawY = this.overheadTextDrawY[m] = y;
				final String message = this.overheadTextMessage[m];
				if (this.chatEffectsDisabled == 0) {
					int colour = 0xFFFF00;
					if (this.overheadTextColour[m] < 6) {
                        colour = this.SPOKEN_TEXT_COLOURS[this.overheadTextColour[m]];
                    }
					if (this.overheadTextColour[m] == 6) {
                        colour = this.renderCount % 20 >= 10 ? 0xFFFF00 : 0xFF0000;
                    }
					if (this.overheadTextColour[m] == 7) {
                        colour = this.renderCount % 20 >= 10 ? 0x00FFFF : 0x0000FF;
                    }
					if (this.overheadTextColour[m] == 8) {
                        colour = this.renderCount % 20 >= 10 ? 0x80FF80 : 0x00B000;
                    }
					if (this.overheadTextColour[m] == 9) {
						final int cycle = 150 - this.overheadTextCycle[m];
						if (cycle < 50) {
                            colour = 0xFF0000 + 0x000500 * cycle;
                        } else if (cycle < 100) {
                            colour = 0xFFFF00 - 0x050000 * (cycle - 50);
                        } else if (cycle < 150) {
                            colour = 0x00FF00 + 0x000005 * (cycle - 100);
                        }
					}
					if (this.overheadTextColour[m] == 10) {
						final int cycle = 150 - this.overheadTextCycle[m];
						if (cycle < 50) {
                            colour = 0xFF0000 + 5 * cycle;
                        } else if (cycle < 100) {
                            colour = 0xFF00FF - 0x050000 * (cycle - 50);
                        } else if (cycle < 150) {
                            colour = (0x0000FF + 0x050000 * (cycle - 100)) - 5 * (cycle - 100);
                        }
					}
					if (this.overheadTextColour[m] == 11) {
						final int cycle = 150 - this.overheadTextCycle[m];
						if (cycle < 50) {
                            colour = 0xFFFFFF - 0x050005 * cycle;
                        } else if (cycle < 100) {
                            colour = 0x00FF00 + 0x050005 * (cycle - 50);
                        } else if (cycle < 150) {
                            colour = 0xFFFFFF - 0x050000 * (cycle - 100);
                        }
					}
					if (this.overheadTextEffect[m] == 0) {
						this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY + 1, 0);
						this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY, colour);
					}
					if (this.overheadTextEffect[m] == 1) {
						this.fontBold.drawVerticalSineWaveText(message, this.spriteDrawX, this.spriteDrawY + 1, 0, this.renderCount);
						this.fontBold.drawVerticalSineWaveText(message, this.spriteDrawX, this.spriteDrawY, colour, this.renderCount);
					}
					if (this.overheadTextEffect[m] == 2) {
						this.fontBold.drawVerticalHorizontalSineWaveText(message, this.spriteDrawX, this.spriteDrawY + 1, 0,
								this.renderCount);
						this.fontBold.drawVerticalHorizontalSineWaveText(message, this.spriteDrawX, this.spriteDrawY, colour,
								this.renderCount);
					}
					if (this.overheadTextEffect[m] == 3) {
						this.fontBold.drawShakingText(message, this.spriteDrawX, this.spriteDrawY + 1, 0, 150 - this.overheadTextCycle[m],
								this.renderCount);
						this.fontBold.drawShakingText(message, this.spriteDrawX, this.spriteDrawY, colour, 150 - this.overheadTextCycle[m],
								this.renderCount);
					}
					if (this.overheadTextEffect[m] == 4) {
						final int width = this.fontBold.getTextWidth(message);
						final int offsetX = ((150 - this.overheadTextCycle[m]) * (width + 100)) / 150;
						DrawingArea.setDrawingArea(334, this.spriteDrawX - 50, this.spriteDrawX + 50, 0);
						this.fontBold.drawText(message, (this.spriteDrawX + 50) - offsetX, this.spriteDrawY + 1, 0);
						this.fontBold.drawText(message, (this.spriteDrawX + 50) - offsetX, this.spriteDrawY, colour);
						DrawingArea.defaultDrawingAreaSize();
					}
					if (this.overheadTextEffect[m] == 5) {
						final int cycle = 150 - this.overheadTextCycle[m];
						int offsetY = 0;
						if (cycle < 25) {
                            offsetY = cycle - 25;
                        } else if (cycle > 125) {
                            offsetY = cycle - 125;
                        }
						DrawingArea.setDrawingArea(this.spriteDrawY + 5, 0, 512, this.spriteDrawY - this.fontBold.fontHeight - 1);
						this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY + 1 + offsetY, 0);
						this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY + offsetY, colour);
						DrawingArea.defaultDrawingAreaSize();
					}
				} else {
					this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY + 1, 0);
					this.fontBold.drawCentredText(message, this.spriteDrawX, this.spriteDrawY, 0xFFFF00);
				}
			}
		} catch (final Exception e) {
		}

	}

	private void updateEntity(final Entity entity) {
		if (entity.x < 128 || entity.y < 128 || entity.x >= 13184 || entity.y >= 13184) {
			entity.animation = -1;
			entity.graphicId = -1;
			entity.tickStart = 0;
			entity.tickEnd = 0;
			entity.x = entity.waypointX[0] * 128 + entity.boundaryDimension * 64;
			entity.y = entity.waypointY[0] * 128 + entity.boundaryDimension * 64;
			entity.resetPath();
		}
		if (entity == localPlayer && (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.animation = -1;
			entity.graphicId = -1;
			entity.tickStart = 0;
			entity.tickEnd = 0;
			entity.x = entity.waypointX[0] * 128 + entity.boundaryDimension * 64;
			entity.y = entity.waypointY[0] * 128 + entity.boundaryDimension * 64;
			entity.resetPath();
		}
		if (entity.tickStart > tick) {
			this.updatePosition(entity);
        } else if (entity.tickEnd >= tick) {
			this.updateFacingDirection(entity);
        } else {
			this.processWalkingStep(entity);
        }
		this.appendFocusDestination(entity);
		this.appendAnimation(entity);
	}

	private void updateFacingDirection(final Entity entity) {
		if (entity.tickEnd == tick || entity.animation == -1 || entity.animationDelay != 0
				|| entity.currentAnimationDuration + 1 > AnimationSequence.animations[entity.animation]
						.getFrameLength(entity.currentAnimationFrame)) {
			final int duration = entity.tickEnd - entity.tickStart;
			final int timePassed = tick - entity.tickStart;
			final int differenceStartX = entity.startX * 128 + entity.boundaryDimension * 64;
			final int differenceStartY = entity.startY * 128 + entity.boundaryDimension * 64;
			final int differenceEndX = entity.endX * 128 + entity.boundaryDimension * 64;
			final int differenceEndY = entity.endY * 128 + entity.boundaryDimension * 64;
			entity.x = (differenceStartX * (duration - timePassed) + differenceEndX * timePassed) / duration;
			entity.y = (differenceStartY * (duration - timePassed) + differenceEndY * timePassed) / duration;
		}
		entity.stepsDelayed = 0;
		if (entity.direction == 0) {
            entity.turnDirection = 1024;
        }
		if (entity.direction == 1) {
            entity.turnDirection = 1536;
        }
		if (entity.direction == 2) {
            entity.turnDirection = 0;
        }
		if (entity.direction == 3) {
            entity.turnDirection = 512;
        }
		entity.currentRotation = entity.turnDirection;
	}

	private void updateLocalPlayerMovement(final Buffer stream) {
		stream.initBitAccess();
		final int currentlyUpdating = stream.readBits(1);
		if (currentlyUpdating == 0) {
            return;
        }
		final int movementUpdateType = stream.readBits(2);
		if (movementUpdateType == 0) {
			this.playersObserved[this.playersObservedCount++] = this.LOCAL_PLAYER_ID;
			return;
		}
		if (movementUpdateType == 1) {
			final int direction = stream.readBits(3);
			localPlayer.move(false, direction);
			final int furtherUpdateRequired = stream.readBits(1);
			if (furtherUpdateRequired == 1) {
				this.playersObserved[this.playersObservedCount++] = this.LOCAL_PLAYER_ID;
            }
			return;
		}
		if (movementUpdateType == 2) {
			final int lastDirection = stream.readBits(3);
			localPlayer.move(true, lastDirection);
			final int currentDirection = stream.readBits(3);
			localPlayer.move(true, currentDirection);
			final int updateRequired = stream.readBits(1);
			if (updateRequired == 1) {
				this.playersObserved[this.playersObservedCount++] = this.LOCAL_PLAYER_ID;
            }
			return;
		}
		if (movementUpdateType == 3) {
			this.plane = stream.readBits(2);
			final int clearWaypointQueue = stream.readBits(1);
			final int updateRequired = stream.readBits(1);
			if (updateRequired == 1) {
				this.playersObserved[this.playersObservedCount++] = this.LOCAL_PLAYER_ID;
            }
			final int x = stream.readBits(7);
			final int y = stream.readBits(7);
			localPlayer.setPos(y, x, clearWaypointQueue == 1);
		}
	}

	private void updateNPCBlock(final Buffer stream) {
		for (int n = 0; n < this.playersObservedCount; n++) {
			final int npcId = this.playersObserved[n];
			final NPC npc = this.npcs[npcId];
			final int updateType = stream.getUnsignedByte();
			if ((updateType & 0x10) != 0) {
				int animationId = stream.getUnsignedShort();
				if (animationId == 0x00FFFF) {
                    animationId = -1;
                }
				final int animationDelay = stream.getUnsignedByte();
				if (animationId == npc.animation && animationId != -1) {
					final int replayMode = AnimationSequence.animations[animationId].replayMode;
					if (replayMode == 1) {
						npc.currentAnimationFrame = 0;
						npc.currentAnimationDuration = 0;
						npc.animationDelay = animationDelay;
						npc.currentAnimationLoopCount = 0;
					}
					if (replayMode == 2) {
                        npc.currentAnimationLoopCount = 0;
                    }
				} else if (animationId == -1 || npc.animation == -1
						|| AnimationSequence.animations[animationId].priority >= AnimationSequence.animations[npc.animation].priority) {
					npc.animation = animationId;
					npc.currentAnimationFrame = 0;
					npc.currentAnimationDuration = 0;
					npc.animationDelay = animationDelay;
					npc.currentAnimationLoopCount = 0;
					npc.stepsRemaining = npc.waypointCount;
				}
			}
			if ((updateType & 8) != 0) {
				final int hitDamage = stream.getUnsignedByteA();
				final int hitType = stream.getUnsignedByteC();
				npc.updateHitData(hitType, hitDamage, tick);
				npc.loopCycleStatus = tick + 300;
				npc.currentHealth = stream.getUnsignedByteA();
				npc.maxHealth = stream.getUnsignedByte();
			}
			if ((updateType & 0x80) != 0) {
				npc.graphicId = stream.getUnsignedLEShort();
				final int delay = stream.getInt();
				npc.graphicHeight = delay >> 16;
				npc.graphicEndCycle = tick + (delay & 0xFFff);
				npc.currentAnimationId = 0;
				npc.currentAnimationTimeRemaining = 0;
				if (npc.graphicEndCycle > tick) {
                    npc.currentAnimationId = -1;
                }
				if (npc.graphicId == 0x00FFFF) {
                    npc.graphicId = -1;
                }
			}
			if ((updateType & 0x20) != 0) {
				npc.interactingEntity = stream.getUnsignedLEShort();
				if (npc.interactingEntity == 0x00FFFF) {
                    npc.interactingEntity = -1;
                }
			}
			if ((updateType & 1) != 0) {
				npc.overheadTextMessage = stream.getString();
				npc.textCycle = 100;
			}
			if ((updateType & 0x40) != 0) {
				final int hitDamage = stream.getUnsignedByteC();
				final int hitType = stream.getUnsignedByteS();
				npc.updateHitData(hitType, hitDamage, tick);
				npc.loopCycleStatus = tick + 300;
				npc.currentHealth = stream.getUnsignedByteS();
				npc.maxHealth = stream.getUnsignedByteC();
			}
			if ((updateType & 2) != 0) {
				npc.npcDefinition = EntityDefinition.getDefinition(stream.getUnsignedShortA());
				npc.boundaryDimension = npc.npcDefinition.boundaryDimension;
				npc.degreesToTurn = npc.npcDefinition.degreesToTurn;
				npc.walkAnimationId = npc.npcDefinition.walkAnimationId;
				npc.turnAboutAnimationId = npc.npcDefinition.turnAboutAnimationId;
				npc.turnRightAnimationId = npc.npcDefinition.turnRightAnimationId;
				npc.turnLeftAnimationId = npc.npcDefinition.turnLeftAnimationId;
				npc.standAnimationId = npc.npcDefinition.standAnimationId;
			}
			if ((updateType & 4) != 0) {
				npc.faceTowardX = stream.getUnsignedShort();
				npc.faceTowardY = stream.getUnsignedShort();
			}
		}
	}

	private void updateNPCInstances() {
		for (int n = 0; n < this.npcCount; n++) {
			final int npcId = this.npcIds[n];
			final NPC npc = this.npcs[npcId];
			if (npc != null) {
				this.updateEntity(npc);
            }
		}
	}

	private void updateNPCList(final int amount, final Buffer stream) {
		while (stream.bitPosition + 21 < amount * 8) {
			final int npcId = stream.readBits(14);
			if (npcId == 16383) {
                break;
            }
			if (this.npcs[npcId] == null) {
				this.npcs[npcId] = new NPC();
            }
			final NPC npc = this.npcs[npcId];
			this.npcIds[this.npcCount++] = npcId;
			npc.lastUpdateTick = tick;
			int y = stream.readBits(5);
			if (y > 15) {
                y -= 32;
            }
			int x = stream.readBits(5);
			if (x > 15) {
                x -= 32;
            }
			final int clearWaypointQueue = stream.readBits(1);
			npc.npcDefinition = EntityDefinition.getDefinition(stream.readBits(12));
			final int furtherUpdateRequired = stream.readBits(1);
			if (furtherUpdateRequired == 1) {
				this.playersObserved[this.playersObservedCount++] = npcId;
            }
			npc.boundaryDimension = npc.npcDefinition.boundaryDimension;
			npc.degreesToTurn = npc.npcDefinition.degreesToTurn;
			npc.walkAnimationId = npc.npcDefinition.walkAnimationId;
			npc.turnAboutAnimationId = npc.npcDefinition.turnAboutAnimationId;
			npc.turnRightAnimationId = npc.npcDefinition.turnRightAnimationId;
			npc.turnLeftAnimationId = npc.npcDefinition.turnLeftAnimationId;
			npc.standAnimationId = npc.npcDefinition.standAnimationId;
			npc.setPos(localPlayer.waypointX[0] + x, localPlayer.waypointY[0] + y, clearWaypointQueue == 1);
		}
		stream.finishBitAccess();
	}

	private void updateNPCMovement(final Buffer stream) {
		stream.initBitAccess();
		final int npcsToUpdate = stream.readBits(8);
		if (npcsToUpdate < this.npcCount) {
			for (int n = npcsToUpdate; n < this.npcCount; n++) {
				this.actorsToUpdateIds[this.actorsToUpdateCount++] = this.npcIds[n];
            }

		}
		if (npcsToUpdate > this.npcCount) {
			signlink.reporterror(this.enteredUsername + " Too many npcs");
			throw new RuntimeException("eek");
		}
		this.npcCount = 0;
		for (int n = 0; n < npcsToUpdate; n++) {
			final int npcId = this.npcIds[n];
			final NPC npc = this.npcs[npcId];
			final int updateRequired = stream.readBits(1);
			if (updateRequired == 0) {
				this.npcIds[this.npcCount++] = npcId;
				npc.lastUpdateTick = tick;
			} else {
				final int movementUpdateType = stream.readBits(2);
				if (movementUpdateType == 0) {
					this.npcIds[this.npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					this.playersObserved[this.playersObservedCount++] = npcId;
				} else if (movementUpdateType == 1) {
					this.npcIds[this.npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					final int direction = stream.readBits(3);
					npc.move(false, direction);
					final int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1) {
						this.playersObserved[this.playersObservedCount++] = npcId;
                    }
				} else if (movementUpdateType == 2) {
					this.npcIds[this.npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					final int lastDirection = stream.readBits(3);
					npc.move(true, lastDirection);
					final int currentDirection = stream.readBits(3);
					npc.move(true, currentDirection);
					final int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1) {
						this.playersObserved[this.playersObservedCount++] = npcId;
                    }
				} else if (movementUpdateType == 3) {
					this.actorsToUpdateIds[this.actorsToUpdateCount++] = npcId;
                }
			}
		}
	}

	private void updateNPCs(final Buffer stream, final int amount) {
		this.actorsToUpdateCount = 0;
		this.playersObservedCount = 0;
		this.updateNPCMovement(stream);
		this.updateNPCList(amount, stream);
		this.updateNPCBlock(stream);
		for (int k = 0; k < this.actorsToUpdateCount; k++) {
			final int npcId = this.actorsToUpdateIds[k];
			if (this.npcs[npcId].lastUpdateTick != tick) {
				this.npcs[npcId].npcDefinition = null;
				this.npcs[npcId] = null;
			}
		}

		if (stream.position != amount) {
			signlink.reporterror(
					this.enteredUsername + " size mismatch in getnpcpos - pos:" + stream.position + " psize:" + amount);
			throw new RuntimeException("eek");
		}
		for (int n = 0; n < this.npcCount; n++) {
            if (this.npcs[this.npcIds[n]] == null) {
                signlink.reporterror(this.enteredUsername + " null entry in npc list - pos:" + n + " size:" + this.npcCount);
                throw new RuntimeException("eek");
            }
        }
	}

	private void updateOtherPlayerMovement(final Buffer stream) {
		final int playersToUpdate = stream.readBits(8);
		if (playersToUpdate < this.localPlayerCount) {
			for (int p = playersToUpdate; p < this.localPlayerCount; p++) {
				this.actorsToUpdateIds[this.actorsToUpdateCount++] = this.localPlayers[p];
            }
		}
		if (playersToUpdate > this.localPlayerCount) {
			signlink.reporterror(this.enteredUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		this.localPlayerCount = 0;
		for (int p = 0; p < playersToUpdate; p++) {
			final int pId = this.localPlayers[p];
			final Player player = this.players[pId];
			final int updateRequired = stream.readBits(1);
			if (updateRequired == 0) {
				this.localPlayers[this.localPlayerCount++] = pId;
				player.lastUpdateTick = tick;
			} else {
				final int movementUpdateType = stream.readBits(2);
				if (movementUpdateType == 0) {
					this.localPlayers[this.localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					this.playersObserved[this.playersObservedCount++] = pId;
				} else if (movementUpdateType == 1) {
					this.localPlayers[this.localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					final int direction = stream.readBits(3);
					player.move(false, direction);
					final int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1) {
						this.playersObserved[this.playersObservedCount++] = pId;
                    }
				} else if (movementUpdateType == 2) {
					this.localPlayers[this.localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					final int lastDirection = stream.readBits(3);
					player.move(true, lastDirection);
					final int currentDirection = stream.readBits(3);
					player.move(true, currentDirection);
					final int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1) {
						this.playersObserved[this.playersObservedCount++] = pId;
                    }
				} else if (movementUpdateType == 3) {
					this.actorsToUpdateIds[this.actorsToUpdateCount++] = pId;
                }
			}
		}
	}

	private void updatePlayer(final Buffer stream, final int updateType, final Player player, final int playerId) {
		if ((updateType & 0x400) != 0) {
			player.startX = stream.getUnsignedByteS();
			player.startY = stream.getUnsignedByteS();
			player.endX = stream.getUnsignedByteS();
			player.endY = stream.getUnsignedByteS();
			player.tickStart = stream.getUnsignedShortA() + tick;
			player.tickEnd = stream.getUnsignedLEShortA() + tick;
			player.direction = stream.getUnsignedByteS();
			player.resetPath();
		}
		if ((updateType & 0x100) != 0) {
			player.graphicId = stream.getUnsignedShort();
			final int delay = stream.getInt();
			player.graphicHeight = delay >> 16;
			player.graphicEndCycle = tick + (delay & 0xFFff);
			player.currentAnimationId = 0;
			player.currentAnimationTimeRemaining = 0;
			if (player.graphicEndCycle > tick) {
                player.currentAnimationId = -1;
            }
			if (player.graphicId == 0x00FFFF) {
                player.graphicId = -1;
            }
		}
		if ((updateType & 0x008) != 0) {
			int animationId = stream.getUnsignedShort();
			if (animationId == 0x00FFFF) {
                animationId = -1;
            }
			final int animationDelay = stream.getUnsignedByteC();
			if (animationId == player.animation && animationId != -1) {
				final int replayMode = AnimationSequence.animations[animationId].replayMode;
				if (replayMode == 1) {
					player.currentAnimationFrame = 0;
					player.currentAnimationDuration = 0;
					player.animationDelay = animationDelay;
					player.currentAnimationLoopCount = 0;
				}
				if (replayMode == 2) {
                    player.currentAnimationLoopCount = 0;
                }
			} else if (animationId == -1 || player.animation == -1
					|| AnimationSequence.animations[animationId].priority >= AnimationSequence.animations[player.animation].priority) {
				player.animation = animationId;
				player.currentAnimationFrame = 0;
				player.currentAnimationDuration = 0;
				player.animationDelay = animationDelay;
				player.currentAnimationLoopCount = 0;
				player.stepsRemaining = player.waypointCount;
			}
		}
		if ((updateType & 0x004) != 0) {
			player.overheadTextMessage = stream.getString();
			if (player.overheadTextMessage.charAt(0) == '~') {
				player.overheadTextMessage = player.overheadTextMessage.substring(1);
				this.pushMessage(player.overheadTextMessage, 2, player.name);
			} else if (player == localPlayer) {
				this.pushMessage(player.overheadTextMessage, 2, player.name);
            }
			player.chatColour = 0;
			player.chatEffect = 0;
			player.textCycle = 150;
		}
		if ((updateType & 0x080) != 0) {
			final int colourAndEffect = stream.getUnsignedShort();
			final int rights = stream.getUnsignedByte();
			final int messageLength = stream.getUnsignedByteC();
			final int originalOffset = stream.position;
			if (player.name != null && player.visible) {
				final long nameAsLong = TextClass.nameToLong(player.name);
				boolean ignored = false;
				if (rights <= 1) {
					for (int p = 0; p < this.ignoreCount; p++) {
						if (this.ignoreListAsLongs[p] != nameAsLong) {
                            continue;
                        }
						ignored = true;
						break;
					}

				}
				if (!ignored && this.inTutorial == 0) {
                    try {
						this.textStream.position = 0;
                        stream.getBytes(messageLength, 0, this.textStream.buffer);
						this.textStream.position = 0;
                        String text = TextInput.readFromStream(messageLength, this.textStream);
                        text = Censor.censor(text);
                        player.overheadTextMessage = text;
                        player.chatColour = colourAndEffect >> 8;
                        player.rights = rights;

                        // entityMessage(player);

                        player.chatEffect = colourAndEffect & 0xFF;
                        player.textCycle = 150;
                        if (rights == 2 || rights == 3) {
							this.pushMessage(text, 1, "@cr2@" + player.name);
                        } else if (rights == 1) {
							this.pushMessage(text, 1, "@cr1@" + player.name);
                        } else {
							this.pushMessage(text, 2, player.name);
                        }
                    } catch (final Exception exception) {
                        signlink.reporterror("cde2");
                    }
                }
			}
			stream.position = originalOffset + messageLength;
		}
		if ((updateType & 0x001) != 0) {
			player.interactingEntity = stream.getUnsignedShort();
			if (player.interactingEntity == 0x00FFFF) {
                player.interactingEntity = -1;
            }
		}
		if ((updateType & 0x010) != 0) {
			final int appearanceBufferSize = stream.getUnsignedByteC();
			final byte[] _appearanceBuffer = new byte[appearanceBufferSize];
			final Buffer appearanceBuffer = new Buffer(_appearanceBuffer);
			stream.readBytes(appearanceBufferSize, 0, _appearanceBuffer);
			this.playerAppearanceData[playerId] = appearanceBuffer;
			player.updatePlayerAppearance(appearanceBuffer);
		}
		if ((updateType & 0x002) != 0) {
			player.faceTowardX = stream.getUnsignedShortA();
			player.faceTowardY = stream.getUnsignedShort();
		}
		if ((updateType & 0x020) != 0) {
			final int hitDamage = stream.getUnsignedByte();
			final int hitType = stream.getUnsignedByteA();
			player.updateHitData(hitType, hitDamage, tick);
			player.loopCycleStatus = tick + 300;
			player.currentHealth = stream.getUnsignedByteC();
			player.maxHealth = stream.getUnsignedByte();
		}
		if ((updateType & 0x200) != 0) {
			final int hitDamage = stream.getUnsignedByte();
			final int hitType = stream.getUnsignedByteS();
			player.updateHitData(hitType, hitDamage, tick);
			player.loopCycleStatus = tick + 300;
			player.currentHealth = stream.getUnsignedByte();
			player.maxHealth = stream.getUnsignedByteC();
		}
	}

	private void updatePlayerInstances() {
		for (int p = -1; p < this.localPlayerCount; p++) {
			final int id;
			if (p == -1) {
                id = this.LOCAL_PLAYER_ID;
            } else {
                id = this.localPlayers[p];
            }
			final Player player = this.players[id];
			if (player != null) {
				this.updateEntity(player);
            }
		}

	}

	private void updatePlayerList(final Buffer stream, final int count) {
		while (stream.bitPosition + 10 < count * 8) {
			final int pId = stream.readBits(11);
			if (pId == 2047) {
                break;
            }
			if (this.players[pId] == null) {
				this.players[pId] = new Player();
				if (this.playerAppearanceData[pId] != null) {
					this.players[pId].updatePlayerAppearance(this.playerAppearanceData[pId]);
                }
			}
			this.localPlayers[this.localPlayerCount++] = pId;
			final Player player = this.players[pId];
			player.lastUpdateTick = tick;
			final int observed = stream.readBits(1);
			if (observed == 1) {
				this.playersObserved[this.playersObservedCount++] = pId;
            }
			final int teleported = stream.readBits(1);
			int x = stream.readBits(5);
			if (x > 15) {
                x -= 32;
            }
			int y = stream.readBits(5);
			if (y > 15) {
                y -= 32;
            }
			player.setPos(localPlayer.waypointX[0] + y, localPlayer.waypointY[0] + x, teleported == 1);
		}
		stream.finishBitAccess();
	}

	private void updatePlayers(final int packetSize, final Buffer stream) {
		this.actorsToUpdateCount = 0;
		this.playersObservedCount = 0;
		this.updateLocalPlayerMovement(stream);
		this.updateOtherPlayerMovement(stream);
		this.updatePlayerList(stream, packetSize);
		this.updatePlayersBlock(stream);
		for (int p = 0; p < this.actorsToUpdateCount; p++) {
			final int playerId = this.actorsToUpdateIds[p];
			if (this.players[playerId].lastUpdateTick != tick) {
				this.players[playerId] = null;
            }
		}

		if (stream.position != packetSize) {
			signlink.reporterror(
					"Error packet size mismatch in getplayer pos:" + stream.position + " psize:" + packetSize);
			throw new RuntimeException("eek");
		}
		for (int p = 0; p < this.localPlayerCount; p++) {
            if (this.players[this.localPlayers[p]] == null) {
                signlink.reporterror(
						this.enteredUsername + " null entry in pl list - pos:" + p + " size:" + this.localPlayerCount);
                throw new RuntimeException("eek");
            }
        }
	}

	private void updatePlayersBlock(final Buffer stream) {
		for (int p = 0; p < this.playersObservedCount; p++) {
			final int pId = this.playersObserved[p];
			final Player player = this.players[pId];
			int updateType = stream.getUnsignedByte();
			if ((updateType & 0x40) != 0) {
                updateType += stream.getUnsignedByte() << 8;
            }
			this.updatePlayer(stream, updateType, player, pId);
		}

	}

	private void updatePosition(final Entity entity) {
		final int timePassed = entity.tickStart - tick;
		final int differenceX = entity.startX * 128 + entity.boundaryDimension * 64;
		final int differenceY = entity.startY * 128 + entity.boundaryDimension * 64;
		entity.x += (differenceX - entity.x) / timePassed;
		entity.y += (differenceY - entity.y) / timePassed;
		entity.stepsDelayed = 0;
		if (entity.direction == 0) {
            entity.turnDirection = 1024;
        }
		if (entity.direction == 1) {
            entity.turnDirection = 1536;
        }
		if (entity.direction == 2) {
            entity.turnDirection = 0;
        }
		if (entity.direction == 3) {
            entity.turnDirection = 512;
        }
	}
}
