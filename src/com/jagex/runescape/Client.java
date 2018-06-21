package com.jagex.runescape;

import java.applet.AppletContext;
import java.awt.*;
import java.io.*;
import java.net.*;

import com.jagex.runescape.audio.Effect;
import com.jagex.runescape.sign.signlink;
import com.jagex.runescape.isaac.ISAACRandomGenerator;

@SuppressWarnings("serial")
public final class Client extends RSApplet {

	private static String formatAmount(int amount) {
		String formattedAmount = String.valueOf(amount);
		for (int i = formattedAmount.length() - 3; i > 0; i -= 3)
			formattedAmount = formattedAmount.substring(0, i) + "," + formattedAmount.substring(i);
		if (formattedAmount.length() > 8)
			formattedAmount = "@gre@" + formattedAmount.substring(0, formattedAmount.length() - 8) + " million @whi@("
					+ formattedAmount + ")";
		else if (formattedAmount.length() > 4)
			formattedAmount = "@cya@" + formattedAmount.substring(0, formattedAmount.length() - 4) + "K @whi@("
					+ formattedAmount + ")";
		return " " + formattedAmount;
	}

	private static String getAmountString(int amount) {
		if (amount < 100000)
			return String.valueOf(amount);
		if (amount < 10000000)
			return amount / 1000 + "K";
		else
			return amount / 1000000 + "M";
	}

	private static String getCombatLevelDifferenceColour(int localLevel, int otherLevel) {
		int difference = localLevel - otherLevel;
		if (difference < -9)
			return "@red@";
		if (difference < -6)
			return "@or3@";
		if (difference < -3)
			return "@or2@";
		if (difference < 0)
			return "@or1@";
		if (difference > 9)
			return "@gre@";
		if (difference > 6)
			return "@gr3@";
		if (difference > 3)
			return "@gr2@";
		if (difference > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	public static void main(String args[]) {
		try {
			System.out.println("RS2 user client - release #" + 317);

			if (args.length != 5) {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			localWorldId = Integer.parseInt(args[0]);
			portOffset = Integer.parseInt(args[1]);
			if (args[2].equals("lowmem"))
				setLowMemory();
			else if (args[2].equals("highmem")) {
				setHighMem();
			} else {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			if (args[3].equals("free"))
				membersWorld = false;
			else if (args[3].equals("members")) {
				membersWorld = true;
			} else {
				System.out.println("Usage: node-id, port-offset, [lowmem/highmem], [free/members], storeid");
				return;
			}
			signlink.storeid = Integer.parseInt(args[4]);
			signlink.startpriv(InetAddress.getLocalHost());
			Client client1 = new Client();
			client1.createClientFrame(765, 503);
		} catch (Exception exception) {
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
	private NodeList[][][] groundArray;
	private int[] anIntArray828;
	private int[] anIntArray829;
	private volatile boolean currentlyDrawingFlames;
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
	private int[] currentFlameColours;
	private int[] flameColour1;
	private int[] flameColour2;
	private int[] flameColour3;
	private int hintIconType;
	private int openInterfaceId;
	private int cameraPositionX;
	private int cameraPositionZ;
	private int cameraPositionY;
	private int cameraVerticalRotation;
	private int cameraHorizontalRotation;
	private int playerRights;
	private final int[] skillExperience;
	private Background redStone1_3;
	private Background redStone2_3;
	private Background redStone3_2;
	private Background redStone1_4;
	private Background redStone2_4;
	private Sprite mapFlag;
	private Sprite mapMarker;
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
	private static int anInt940;
	private final int[] chatTypes;
	private final String[] chatNames;
	private final String[] chatMessages;
	private int animationTimePassed;
	private WorldController worldController;
	private Background[] sideIconImage;
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
	private volatile boolean drawingFlames;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] SPOKEN_TEXT_COLOURS = { 0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff };
	private Background titleBoxImage;
	private Background titleButtonImage;
	private final int[] compassHingeSize;
	private final int[] anIntArray969;
	final FileCache[] caches;
	public int interfaceSettings[];
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
	private Sprite minimapEdgeImage;
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
	private int anInt1009;
	private int idleCounter;
	private int idleLogout;
	private NodeList projectileQueue;
	private int currentCameraPositionH;
	private int currentCameraPositionV;
	private int cameraMovedWriteDelay;
	private boolean cameraMovedWrite;
	private int walkableInterfaceId;
	private static final int[] EXPERIENCE_TABLE;
	private int minimapState;
	private int sameClickPositionCounter;
	private int loadingStage;
	private Background scrollBarUp;
	private Background scrollBarDown;
	private int anInt1026;
	private Background backBase1Image;
	private Background backBase2Image;
	private Background backHmid1Image;
	private final int[] unknownCameraVariable;
	private boolean characterModelChanged;
	private Sprite[] mapFunctionImage;
	private int baseX;
	private int baseY;
	private int anInt1036;
	private int anInt1037;
	private int loginFailures;
	private int anInt1039;
	private int anInt1040;
	private int anInt1041;
	private int dialogID;
	private final int[] skillMaxLevel;
	private final int[] defaultSettings;
	private int membershipStatus;
	private boolean characterEditChangeGender;
	private int anInt1048;
	private String loadingBarText;
	private static int loadedRegions;
	private final int[] minimapLeft;
	private Archive archiveTitle;
	private int flashingSidebar;
	private boolean multiCombatZone;
	private NodeList stationaryGraphicQueue;
	private final int[] compassWidthMap;
	private final RSInterface chatboxInterface;
	private Background[] mapSceneImage;
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
	private int minimapHintCount;
	private int[] minimapHintX;
	private int[] minimapHintY;
	private Sprite mapDotItem;
	private Sprite mapDotNPC;
	private Sprite mapDotPlayer;
	private Sprite mapDotFriend;
	private Sprite mapDotTeam;
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
	private RSImageProducer topLeft1BackgroundTile;
	private RSImageProducer bottomLeft1BackgroundTile;
	private RSImageProducer loginBoxLeftBackgroundTile;
	private RSImageProducer flameLeftBackground;
	private RSImageProducer flameRightBackground;
	private RSImageProducer bottomLeft0BackgroundTile;
	private RSImageProducer aRSImageProducer_1113;
	private RSImageProducer middleLeft1BackgroundTile;
	private RSImageProducer aRSImageProducer_1115;
	private static int mouseClickCounter;
	private int membership;
	private String chatboxInputNeededString;
	private Sprite minimapCompassImage;
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
	private Sprite[] minimapHint;
	private boolean inTutorialIsland;
	private Background redStone1;
	private Background redStone2;
	private Background redStone3;
	private Background redStone1_2;
	private Background redStone2_2;
	private int playerEnergy;
	private boolean continuedDialogue;
	private Sprite[] crosses;
	private boolean musicEnabled;
	private Background[] flameRuneImage;
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
	private RSImageProducer minimapImageProducer;
	private RSImageProducer gameScreenImageProducer;
	private RSImageProducer chatboxImageProducer;
	private int daysSinceRecoveryChange;
	private RSSocket socket;
	private int privateMessagePointer;
	private int minimapZoom;
	private int randomisationMinimapZoom;
	private long songStartTime;
	private String enteredUsername;
	private String enteredPassword;
	private boolean genericLoadingError;
	private final int[] objectTypes = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int reportAbuseInterfaceID;
	private NodeList spawnObjectList;
	private int[] chatboxLineOffsets;
	private int[] sidebarOffsets;
	private int[] viewportOffsets;
	private byte[][] terrainData;
	private int cameraVertical;
	private int cameraHorizontal;
	private int cameraModificationH;
	private int cameraModificationV;
	private int inventoryOverlayInterfaceID;
	private int[] anIntArray1190;
	private int[] anIntArray1191;
	private Buffer stream;
	private int lastAddress;
	private int splitPrivateChat;
	private Background inventoryBackgroundImage;
	private Background minimapBackgroundImage;
	private Background chatBackgroundImage;
	private String[] menuActionName;
	private Sprite flameLeftBackground2;
	private Sprite flameRightBackground2;
	private final int[] cameraAmplitude;
	static final int[] BEARD_COLOURS = { 9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027,
			1457, 16565, 34991, 25486 };
	private static boolean flagged;
	private final int[] trackIds;
	private int flameCycle;
	private int minimapRotation;
	private int randomisationMinimapRotation;
	private int chatboxScrollMax;
	private String promptInput;
	private int anInt1213;
	private int[][][] intGroundArray;
	private long serverSessionKey;
	private int loginScreenFocus;
	private final Background[] modIcons;
	private long lastClickTime;
	private int currentTabId;
	private int hintIconNpcId;
	private boolean redrawChatbox;
	private int inputDialogState;
	private int nextSong;
	private boolean songChanging;
	private final int[] minimapLineWidth;
	private CollisionMap[] currentCollisionMap;
	public static int BITFIELD_MAX_VALUE[];
	private boolean updateChatSettings;
	private int[] mapCoordinates;
	private int[] terrainDataIds;
	private int[] objectDataIds;
	private int lastClickX;
	private int lastClickY;
	public final int anInt1239 = 100;
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
	private boolean welcomeScreenRaised;
	private boolean messagePromptRaised;
	private int songStartOffset;
	private byte[][][] tileFlags;
	private int prevSong;
	private int destinationX;
	private int destinationY;
	private Sprite minimapImage;
	private int arbitraryDestination;
	private int renderCount;
	private String loginMessage1;
	private String loginMessage2;
	private int playerPositionX;
	private int playerPositionY;
	private GameFont fontSmall;
	private GameFont fontPlain;
	private GameFont fontBold;
	private int anInt1275;
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
			int level = n + 1;
			int exp = (int) (level + 300D * Math.pow(2D, level / 7D));
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
		distanceValues = new int[104][104];
		friendsWorldIds = new int[200];
		groundArray = new NodeList[4][104][104];
		currentlyDrawingFlames = false;
		textStream = new Buffer(new byte[5000]);
		npcs = new NPC[16384];
		npcIds = new int[16384];
		actorsToUpdateIds = new int[1000];
		loginStream = Buffer.create();
		effectsEnabled = true;
		openInterfaceId = -1;
		skillExperience = new int[Skills.skillsCount];
		aBoolean872 = false;
		cameraJitter = new int[5];
		currentTrackId = -1;
		customCameraActive = new boolean[5];
		drawFlames = false;
		reportAbuseInput = "";
		playerListId = -1;
		menuOpen = false;
		inputString = "";
		MAX_ENTITY_COUNT = 2048;
		LOCAL_PLAYER_ID = 2047;
		players = new Player[MAX_ENTITY_COUNT];
		localPlayers = new int[MAX_ENTITY_COUNT];
		playersObserved = new int[MAX_ENTITY_COUNT];
		playerAppearanceData = new Buffer[MAX_ENTITY_COUNT];
		nextCameraRandomisationA = 1;
		wayPoints = new int[104][104];
		SCROLLBAR_GRIP_HIGHLIGHT = 0x766654;
		animatedPixels = new byte[16384];
		skillLevel = new int[Skills.skillsCount];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		SCROLLBAR_GRIP_LOWLIGHT = 0x332d25;
		cameraFrequency = new int[5];
		tileRenderCount = new int[104][104];
		chatTypes = new int[100];
		chatNames = new String[100];
		chatMessages = new String[100];
		sideIconImage = new Background[13];
		windowFocused = true;
		friendsListAsLongs = new long[200];
		currentSong = -1;
		drawingFlames = false;
		spriteDrawX = -1;
		spriteDrawY = -1;
		compassHingeSize = new int[33];
		anIntArray969 = new int[256];
		caches = new FileCache[5];
		interfaceSettings = new int[2000];
		aBoolean972 = false;
		overheadMessageCount = 50;
		overheadTextDrawX = new int[overheadMessageCount];
		overheadTextDrawY = new int[overheadMessageCount];
		overheadTextHeight = new int[overheadMessageCount];
		overheadTextWidth = new int[overheadMessageCount];
		overheadTextColour = new int[overheadMessageCount];
		overheadTextEffect = new int[overheadMessageCount];
		overheadTextCycle = new int[overheadMessageCount];
		overheadTextMessage = new String[overheadMessageCount];
		lastRegionId = -1;
		hitMarkImage = new Sprite[20];
		characterEditColours = new int[5];
		SCROLLBAR_TRACK_COLOUR = 0x23201b;
		amountOrNameInput = "";
		projectileQueue = new NodeList();
		cameraMovedWrite = false;
		walkableInterfaceId = -1;
		unknownCameraVariable = new int[5];
		characterModelChanged = false;
		mapFunctionImage = new Sprite[100];
		dialogID = -1;
		skillMaxLevel = new int[Skills.skillsCount];
		defaultSettings = new int[2000];
		characterEditChangeGender = true;
		minimapLeft = new int[151];
		flashingSidebar = -1;
		stationaryGraphicQueue = new NodeList();
		compassWidthMap = new int[33];
		chatboxInterface = new RSInterface();
		mapSceneImage = new Background[100];
		SCROLLBAR_GRIP_FOREGROUND = 0x4d4233;
		characterEditIdentityKits = new int[7];
		minimapHintX = new int[1000];
		minimapHintY = new int[1000];
		loadingMap = false;
		friendsList = new String[200];
		inStream = Buffer.create();
		expectedCRCs = new int[9];
		menuActionData2 = new int[500];
		menuActionData3 = new int[500];
		menuActionId = new int[500];
		menuActionData1 = new int[500];
		headIcons = new Sprite[20];
		drawTabIcons = false;
		chatboxInputNeededString = "";
		playerActionText = new String[5];
		playerActionUnpinned = new boolean[5];
		constructMapTiles = new int[4][13][13];
		nextCameraRandomisationV = 2;
		minimapHint = new Sprite[1000];
		inTutorialIsland = false;
		continuedDialogue = false;
		crosses = new Sprite[8];
		musicEnabled = true;
		redrawTab = false;
		loggedIn = false;
		reportAbuseMute = false;
		loadGeneratedMap = false;
		cutsceneActive = false;
		randomisationMinimapZoom = 1;
		enteredUsername = "";
		enteredPassword = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		spawnObjectList = new NodeList();
		cameraVertical = 128;
		inventoryOverlayInterfaceID = -1;
		stream = Buffer.create();
		menuActionName = new String[500];
		cameraAmplitude = new int[5];
		trackIds = new int[50];
		randomisationMinimapRotation = 2;
		chatboxScrollMax = 78;
		promptInput = "";
		modIcons = new Background[2];
		currentTabId = 3;
		redrawChatbox = false;
		songChanging = true;
		minimapLineWidth = new int[151];
		currentCollisionMap = new CollisionMap[4];
		updateChatSettings = false;
		privateMessages = new int[100];
		trackLoop = new int[50];
		lastItemDragged = false;
		trackDelay = new int[50];
		rsAlreadyLoaded = false;
		welcomeScreenRaised = false;
		messagePromptRaised = false;
		loginMessage1 = "";
		loginMessage2 = "";
		chatboxInterfaceId = -1;
		nextCameraRandomisationH = 2;
		walkingQueueX = new int[4000];
		walkingQueueY = new int[4000];
		currentTrackLoop = -1;
	}

	private void addFriend(long targetHash) {
		try {
			if (targetHash == 0L)
				return;
			if (friendsCount >= 100 && membershipStatus != 1) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			if (friendsCount >= 200) {
				pushMessage("Your friendlist is full. Max of 100 for free users, and 200 for members", 0, "");
				return;
			}
			String targetName = TextClass.formatName(TextClass.longToName(targetHash));
			for (int f = 0; f < friendsCount; f++)
				if (friendsListAsLongs[f] == targetHash) {
					pushMessage(targetName + " is already on your friend list", 0, "");
					return;
				}
			for (int i = 0; i < ignoreCount; i++)
				if (ignoreListAsLongs[i] == targetHash) {
					pushMessage("Please remove " + targetName + " from your ignore list first", 0, "");
					return;
				}

			if (targetName.equals(localPlayer.name)) {
				return;
			} else {
				friendsList[friendsCount] = targetName;
				friendsListAsLongs[friendsCount] = targetHash;
				friendsWorldIds[friendsCount] = 0;
				friendsCount++;
				redrawTab = true;
				stream.putOpcode(188);
				stream.putLong(targetHash);
				return;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("15283, " + (byte) 68 + ", " + targetHash + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void addIgnore(long target) {
		try {
			if (target == 0L)
				return;
			if (ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			String targetName = TextClass.formatName(TextClass.longToName(target));
			for (int p = 0; p < ignoreCount; p++)
				if (ignoreListAsLongs[p] == target) {
					pushMessage(targetName + " is already on your ignore list", 0, "");
					return;
				}
			for (int p = 0; p < friendsCount; p++)
				if (friendsListAsLongs[p] == target) {
					pushMessage("Please remove " + targetName + " from your friend list first", 0, "");
					return;
				}

			ignoreListAsLongs[ignoreCount++] = target;
			redrawTab = true;
			stream.putOpcode(133);
			stream.putLong(target);
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("45688, " + target + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void adjustVolume(boolean flag, int volume) {
		signlink.midiVolume = volume;
		if (flag)
			signlink.midi = "voladjust";
	}

	private boolean animateInterface(int timePassed, int interfaceId) {
		boolean animated = false;
		RSInterface targetInterface = RSInterface.cache[interfaceId];
		for (int c = 0; c < targetInterface.children.length; c++) {
			if (targetInterface.children[c] == -1)
				break;
			RSInterface childInterface = RSInterface.cache[targetInterface.children[c]];
			if (childInterface.type == 1)
				animated |= animateInterface(timePassed, childInterface.id);
			if (childInterface.type == 6
					&& (childInterface.animationIdDefault != -1 || childInterface.animationIdActive != -1)) {
				int animationId = interfaceIsActive(childInterface) ? childInterface.animationIdActive
						: childInterface.animationIdDefault;

				if (animationId != -1) {
					AnimationSequence animation = AnimationSequence.animations[animationId];
					for (childInterface.animationDuration += timePassed; childInterface.animationDuration > animation
							.getFrameLength(childInterface.animationFrame);) {
						childInterface.animationDuration -= animation.getFrameLength(childInterface.animationFrame) + 1;
						childInterface.animationFrame++;
						if (childInterface.animationFrame >= animation.frameCount) {
							childInterface.animationFrame -= animation.frameStep;
							if (childInterface.animationFrame < 0
									|| childInterface.animationFrame >= animation.frameCount)
								childInterface.animationFrame = 0;
						}
						animated = true;
					}

				}
			}
		}

		return animated;
	}

	private void animateTexture(int textureId) {
		if (!lowMemory) {
			if (Rasterizer.textureLastUsed[17] >= textureId) {
				Background background = Rasterizer.textureImages[17];
				int area = background.imageWidth * background.imageHeight - 1;
				int difference = background.imageWidth * animationTimePassed * 2;
				byte originalPixels[] = background.imagePixels;
				byte shiftedPixels[] = animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++)
					shiftedPixels[pixel] = originalPixels[pixel - difference & area];

				background.imagePixels = shiftedPixels;
				animatedPixels = originalPixels;
				Rasterizer.resetTexture(17);
			}
			if (Rasterizer.textureLastUsed[24] >= textureId) {
				Background background = Rasterizer.textureImages[24];
				int area = background.imageWidth * background.imageHeight - 1;
				int difference = background.imageWidth * animationTimePassed * 2;
				byte originalPixels[] = background.imagePixels;
				byte shiftedPixels[] = animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++)
					shiftedPixels[pixel] = originalPixels[pixel - difference & area];

				background.imagePixels = shiftedPixels;
				animatedPixels = originalPixels;
				Rasterizer.resetTexture(24);
			}
			if (Rasterizer.textureLastUsed[34] >= textureId) {
				Background background = Rasterizer.textureImages[34];
				int area = background.imageWidth * background.imageHeight - 1;
				int difference = background.imageWidth * animationTimePassed * 2;
				byte originalPixels[] = background.imagePixels;
				byte shiftedPixels[] = animatedPixels;
				for (int pixel = 0; pixel <= area; pixel++)
					shiftedPixels[pixel] = originalPixels[pixel - difference & area];

				background.imagePixels = shiftedPixels;
				animatedPixels = originalPixels;
				Rasterizer.resetTexture(34);
			}
		}
	}

	private void appendAnimation(Entity entity) {
		entity.dynamic = false;
		if (entity.queuedAnimationId != -1) {
			AnimationSequence animation = AnimationSequence.animations[entity.queuedAnimationId];
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
			if (entity.currentAnimationId < 0)
				entity.currentAnimationId = 0;
			AnimationSequence animation = SpotAnimation.cache[entity.graphicId].sequences;
			for (entity.currentAnimationTimeRemaining++; entity.currentAnimationId < animation.frameCount
					&& entity.currentAnimationTimeRemaining > animation
							.getFrameLength(entity.currentAnimationId); entity.currentAnimationId++)
				entity.currentAnimationTimeRemaining -= animation.getFrameLength(entity.currentAnimationId);

			if (entity.currentAnimationId >= animation.frameCount
					&& (entity.currentAnimationId < 0 || entity.currentAnimationId >= animation.frameCount))
				entity.graphicId = -1;
		}
		if (entity.animation != -1 && entity.animationDelay <= 1) {
			AnimationSequence animation = AnimationSequence.animations[entity.animation];
			if (animation.precedenceAnimating == 1 && entity.stepsRemaining > 0 && entity.tickStart <= tick
					&& entity.tickEnd < tick) {
				entity.animationDelay = 1;
				return;
			}
		}
		if (entity.animation != -1 && entity.animationDelay == 0) {
			AnimationSequence animation = AnimationSequence.animations[entity.animation];
			for (entity.currentAnimationDuration++; entity.currentAnimationFrame < animation.frameCount
					&& entity.currentAnimationDuration > animation
							.getFrameLength(entity.currentAnimationFrame); entity.currentAnimationFrame++)
				entity.currentAnimationDuration -= animation.getFrameLength(entity.currentAnimationFrame);

			if (entity.currentAnimationFrame >= animation.frameCount) {
				entity.currentAnimationFrame -= animation.frameStep;
				entity.currentAnimationLoopCount++;
				if (entity.currentAnimationLoopCount >= animation.maximumLoops)
					entity.animation = -1;
				if (entity.currentAnimationFrame < 0 || entity.currentAnimationFrame >= animation.frameCount)
					entity.animation = -1;
			}
			entity.dynamic = animation.dynamic;
		}
		if (entity.animationDelay > 0)
			entity.animationDelay--;
	}

	private void appendFocusDestination(Entity entity) {
		if (entity.degreesToTurn == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			NPC npc = npcs[entity.interactingEntity];
			if (npc != null) {
				int distanceX = entity.x - npc.x;
				int distanceY = entity.y - npc.y;
				if (distanceX != 0 || distanceY != 0)
					entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7ff;
			}
		}
		if (entity.interactingEntity >= 32768) {
			int targetPlayerId = entity.interactingEntity - 32768;
			if (targetPlayerId == playerListId)
				targetPlayerId = LOCAL_PLAYER_ID;
			Player player = players[targetPlayerId];
			if (player != null) {
				int distanceX = entity.x - player.x;
				int distanceY = entity.y - player.y;
				if (distanceX != 0 || distanceY != 0)
					entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7ff;
			}
		}
		if ((entity.faceTowardX != 0 || entity.faceTowardY != 0)
				&& (entity.waypointCount == 0 || entity.stepsDelayed > 0)) {
			int distanceX = entity.x - (entity.faceTowardX - baseX - baseX) * 64;
			int distanceY = entity.y - (entity.faceTowardY - baseY - baseY) * 64;
			if (distanceX != 0 || distanceY != 0)
				entity.turnDirection = (int) (Math.atan2(distanceX, distanceY) * 325.949) & 0x7ff;
			entity.faceTowardX = 0;
			entity.faceTowardY = 0;
		}
		int angle = entity.turnDirection - entity.currentRotation & 0x7ff;
		if (angle != 0) {
			if (angle < entity.degreesToTurn || angle > 2048 - entity.degreesToTurn)
				entity.currentRotation = entity.turnDirection;
			else if (angle > 1024)
				entity.currentRotation -= entity.degreesToTurn;
			else
				entity.currentRotation += entity.degreesToTurn;
			entity.currentRotation &= 0x7ff;
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
		if (itemSelected == false && spellSelected == false) {
			menuActionName[menuActionRow] = "Walk here";
			menuActionId[menuActionRow] = 516;
			menuActionData2[menuActionRow] = super.mouseX;
			menuActionData3[menuActionRow] = super.mouseY;
			menuActionRow++;
		}
		int originalHash = -1;
		for (int k = 0; k < Model.resourceCount; k++) {
			int hash = Model.resourceId[k];
			int x = hash & 0x7f;
			int y = hash >> 7 & 0x7f;
			int type = hash >> 29 & 3;
			int objectId = hash >> 14 & 0x7fff;
			if (hash == originalHash)
				continue;
			originalHash = hash;
			if (type == 2 && worldController.getConfig(hash, x, y, plane) >= 0) {
				GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
				if (object.childIds != null)
					object = object.getChildDefinition();
				if (object == null)
					continue;
				if (itemSelected) {
					menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + object.name;
					menuActionId[menuActionRow] = 62;
					menuActionData1[menuActionRow] = hash;
					menuActionData2[menuActionRow] = x;
					menuActionData3[menuActionRow] = y;
					menuActionRow++;
				} else if (spellSelected) {
					if ((spellUsableOn & 4) == 4) {
						menuActionName[menuActionRow] = spellTooltip + " @cya@" + object.name;
						menuActionId[menuActionRow] = 956;
						menuActionData1[menuActionRow] = hash;
						menuActionData2[menuActionRow] = x;
						menuActionData3[menuActionRow] = y;
						menuActionRow++;
					}
				} else {
					if (object.actions != null) {
						for (int action = 4; action >= 0; action--)
							if (object.actions[action] != null) {
								menuActionName[menuActionRow] = object.actions[action] + " @cya@" + object.name;
								if (action == 0)
									menuActionId[menuActionRow] = 502;
								if (action == 1)
									menuActionId[menuActionRow] = 900;
								if (action == 2)
									menuActionId[menuActionRow] = 113;
								if (action == 3)
									menuActionId[menuActionRow] = 872;
								if (action == 4)
									menuActionId[menuActionRow] = 1062;
								menuActionData1[menuActionRow] = hash;
								menuActionData2[menuActionRow] = x;
								menuActionData3[menuActionRow] = y;
								menuActionRow++;
							}

					}
					menuActionName[menuActionRow] = "Examine @cya@" + object.name + " @gre@(@whi@" + objectId
							+ "@gre@) (@whi@" + (x + baseX) + "," + (y + baseY) + "@gre@)";
					menuActionId[menuActionRow] = 1226;
					menuActionData1[menuActionRow] = object.id << 14;
					menuActionData2[menuActionRow] = x;
					menuActionData3[menuActionRow] = y;
					menuActionRow++;
				}
			}
			if (type == 1) {
				NPC npc = npcs[objectId];
				if (npc.npcDefinition.boundaryDimension == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
					for (int n = 0; n < npcCount; n++) {
						NPC npc2 = npcs[npcIds[n]];
						if (npc2 != null && npc2 != npc && npc2.npcDefinition.boundaryDimension == 1 && npc2.x == npc.x
								&& npc2.y == npc.y)
							buildMenuForNPC(npc2.npcDefinition, npcIds[n], y, x);
					}

					for (int p = 0; p < localPlayerCount; p++) {
						Player player = players[localPlayers[p]];
						if (player != null && player.x == npc.x && player.y == npc.y)
							buildMenuForPlayer(x, localPlayers[p], player, y);
					}

				}
				buildMenuForNPC(npc.npcDefinition, objectId, y, x);
			}
			if (type == 0) {
				Player player = players[objectId];
				if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for (int n = 0; n < npcCount; n++) {
						NPC npc = npcs[npcIds[n]];
						if (npc != null && npc.npcDefinition.boundaryDimension == 1 && npc.x == player.x
								&& npc.y == player.y)
							buildMenuForNPC(npc.npcDefinition, npcIds[n], y, x);
					}

					for (int p = 0; p < localPlayerCount; p++) {
						Player player2 = players[localPlayers[p]];
						if (player2 != null && player2 != player && player2.x == player.x && player2.y == player.y)
							buildMenuForPlayer(x, localPlayers[p], player2, y);
					}

				}
				buildMenuForPlayer(x, objectId, player, y);
			}
			if (type == 3) {
				NodeList itemStack = groundArray[plane][x][y];
				if (itemStack != null) {
					for (Item item = (Item) itemStack.getFirst(); item != null; item = (Item) itemStack.getNext()) {
						ItemDefinition definition = ItemDefinition.getDefinition(item.itemId);
						if (itemSelected) {
							menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + definition.name;
							menuActionId[menuActionRow] = 511;
							menuActionData1[menuActionRow] = item.itemId;
							menuActionData2[menuActionRow] = x;
							menuActionData3[menuActionRow] = y;
							menuActionRow++;
						} else if (spellSelected) {
							if ((spellUsableOn & 1) == 1) {
								menuActionName[menuActionRow] = spellTooltip + " @lre@" + definition.name;
								menuActionId[menuActionRow] = 94;
								menuActionData1[menuActionRow] = item.itemId;
								menuActionData2[menuActionRow] = x;
								menuActionData3[menuActionRow] = y;
								menuActionRow++;
							}
						} else {
							for (int a = 4; a >= 0; a--)
								if (definition.groundActions != null && definition.groundActions[a] != null) {
									menuActionName[menuActionRow] = definition.groundActions[a] + " @lre@"
											+ definition.name;
									if (a == 0)
										menuActionId[menuActionRow] = 652;
									if (a == 1)
										menuActionId[menuActionRow] = 567;
									if (a == 2)
										menuActionId[menuActionRow] = 234;
									if (a == 3)
										menuActionId[menuActionRow] = 244;
									if (a == 4)
										menuActionId[menuActionRow] = 213;
									menuActionData1[menuActionRow] = item.itemId;
									menuActionData2[menuActionRow] = x;
									menuActionData3[menuActionRow] = y;
									menuActionRow++;
								} else if (a == 2) {
									menuActionName[menuActionRow] = "Take @lre@" + definition.name;
									menuActionId[menuActionRow] = 234;
									menuActionData1[menuActionRow] = item.itemId;
									menuActionData2[menuActionRow] = x;
									menuActionData3[menuActionRow] = y;
									menuActionRow++;
								}

							menuActionName[menuActionRow] = "Examine @lre@" + definition.name + " @gre@(@whi@"
									+ item.itemId + "@gre@)";
							menuActionId[menuActionRow] = 1448;
							menuActionData1[menuActionRow] = item.itemId;
							menuActionData2[menuActionRow] = x;
							menuActionData3[menuActionRow] = y;
							menuActionRow++;
						}
					}

				}
			}
		}
	}

	private void buildChatboxMenu(int y) {
		int rowCount = 0;
		for (int m = 0; m < 100; m++) {
			if (chatMessages[m] == null)
				continue;
			int chatType = chatTypes[m];
			int _y = (70 - rowCount * 14) + anInt1089 + 4;
			if (_y < -20)
				break;
			String chatName = chatNames[m];
			if (chatName != null && chatName.startsWith("@cr1@")) {
				chatName = chatName.substring(5);
			}
			if (chatName != null && chatName.startsWith("@cr2@")) {
				chatName = chatName.substring(5);
			}
			if (chatType == 0)
				rowCount++;
			if ((chatType == 1 || chatType == 2)
					&& (chatType == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y && !chatName.equals(localPlayer.name)) {
					if (playerRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + chatName;
						menuActionId[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + chatName;
					menuActionId[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + chatName;
					menuActionId[menuActionRow] = 337;
					menuActionRow++;
				}
				rowCount++;
			}
			if ((chatType == 3 || chatType == 7) && splitPrivateChat == 0
					&& (chatType == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					if (playerRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + chatName;
						menuActionId[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + chatName;
					menuActionId[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + chatName;
					menuActionId[menuActionRow] = 337;
					menuActionRow++;
				}
				rowCount++;
			}
			if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + chatName;
					menuActionId[menuActionRow] = 484;
					menuActionRow++;
				}
				rowCount++;
			}
			if ((chatType == 5 || chatType == 6) && splitPrivateChat == 0 && privateChatMode < 2)
				rowCount++;
			if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(chatName))) {
				if (y > _y - 14 && y <= _y) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + chatName;
					menuActionId[menuActionRow] = 6;
					menuActionRow++;
				}
				rowCount++;
			}
		}

	}

	private boolean buildFriendsListMenu(RSInterface rsInterface) {
		int type = rsInterface.contentType;
		if (type >= 1 && type <= 200 || type >= 701 && type <= 900) {
			if (type >= 801)
				type -= 701;
			else if (type >= 701)
				type -= 601;
			else if (type >= 101)
				type -= 101;
			else
				type--;
			menuActionName[menuActionRow] = "Remove @whi@" + friendsList[type];
			menuActionId[menuActionRow] = 792;
			menuActionRow++;
			menuActionName[menuActionRow] = "Message @whi@" + friendsList[type];
			menuActionId[menuActionRow] = 639;
			menuActionRow++;
			return true;
		}
		if (type >= 401 && type <= 500) {
			menuActionName[menuActionRow] = "Remove @whi@" + rsInterface.textDefault;
			menuActionId[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	private void buildInterfaceMenu(int i, RSInterface rsInterface, int k, int l, int i1, int j1) {
		if (rsInterface.type != 0 || rsInterface.children == null || rsInterface.hoverOnly)
			return;
		if (k < i || i1 < l || k > i + rsInterface.width || i1 > l + rsInterface.height)
			return;
		int childCount = rsInterface.children.length;
		for (int child = 0; child < childCount; child++) {
			int i2 = rsInterface.childX[child] + i;
			int j2 = (rsInterface.childY[child] + l) - j1;
			RSInterface childInterface = RSInterface.cache[rsInterface.children[child]];
			i2 += childInterface.x;
			j2 += childInterface.y;
			if ((childInterface.hoveredPopup >= 0 || childInterface.colourDefaultHover != 0) && k >= i2 && i1 >= j2
					&& k < i2 + childInterface.width && i1 < j2 + childInterface.height)
				if (childInterface.hoveredPopup >= 0)
					anInt886 = childInterface.hoveredPopup;
				else
					anInt886 = childInterface.id;
			if (childInterface.type == 0) {
				buildInterfaceMenu(i2, childInterface, k, j2, i1, childInterface.scrollPosition);
				if (childInterface.scrollMax > childInterface.height)
					scrollInterface(i2 + childInterface.width, childInterface.height, k, i1, childInterface, j2, true,
							childInterface.scrollMax);
			} else {
				if (childInterface.actionType == 1 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					boolean flag = false;
					if (childInterface.contentType != 0)
						flag = buildFriendsListMenu(childInterface);
					if (!flag) {
						// System.out.println("1"+class9_1.tooltip + ", " +
						// class9_1.interfaceID);
						menuActionName[menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
						menuActionId[menuActionRow] = 315;
						menuActionData3[menuActionRow] = childInterface.id;
						menuActionRow++;
					}
				}
				if (childInterface.actionType == 2 && spellSelected == false && k >= i2 && i1 >= j2
						&& k < i2 + childInterface.width && i1 < j2 + childInterface.height) {
					String actionName = childInterface.selectedActionName;
					if (actionName.indexOf(" ") != -1)
						actionName = actionName.substring(0, actionName.indexOf(" "));
					menuActionName[menuActionRow] = actionName + " @gre@" + childInterface.spellName;
					menuActionId[menuActionRow] = 626;
					menuActionData3[menuActionRow] = childInterface.id;
					menuActionRow++;
				}
				if (childInterface.actionType == 3 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					menuActionName[menuActionRow] = "Close";
					menuActionId[menuActionRow] = 200;
					menuActionData3[menuActionRow] = childInterface.id;
					menuActionRow++;
				}
				if (childInterface.actionType == 4 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					// System.out.println("2"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					menuActionName[menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					menuActionId[menuActionRow] = 169;
					menuActionData3[menuActionRow] = childInterface.id;
					menuActionRow++;
				}
				if (childInterface.actionType == 5 && k >= i2 && i1 >= j2 && k < i2 + childInterface.width
						&& i1 < j2 + childInterface.height) {
					// System.out.println("3"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					menuActionName[menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					menuActionId[menuActionRow] = 646;
					menuActionData3[menuActionRow] = childInterface.id;
					menuActionRow++;
				}
				if (childInterface.actionType == 6 && !continuedDialogue && k >= i2 && i1 >= j2
						&& k < i2 + childInterface.width && i1 < j2 + childInterface.height) {
					// System.out.println("4"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					menuActionName[menuActionRow] = childInterface.tooltip + ", " + childInterface.id;
					menuActionId[menuActionRow] = 679;
					menuActionData3[menuActionRow] = childInterface.id;
					menuActionRow++;
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
								moveItemSlotEnd = slot;
								lastActiveInventoryInterface = childInterface.id;
								if (childInterface.inventoryItemId[slot] > 0) {
									ItemDefinition itemDef = ItemDefinition
											.getDefinition(childInterface.inventoryItemId[slot] - 1);
									if (itemSelected && childInterface.inventory) {
										if (childInterface.id != lastItemSelectedInterface
												|| slot != lastItemSelectedSlot) {
											menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@"
													+ itemDef.name;
											menuActionId[menuActionRow] = 870;
											menuActionData1[menuActionRow] = itemDef.id;
											menuActionData2[menuActionRow] = slot;
											menuActionData3[menuActionRow] = childInterface.id;
											menuActionRow++;
										}
									} else if (spellSelected && childInterface.inventory) {
										if ((spellUsableOn & 0x10) == 16) {
											menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
											menuActionId[menuActionRow] = 543;
											menuActionData1[menuActionRow] = itemDef.id;
											menuActionData2[menuActionRow] = slot;
											menuActionData3[menuActionRow] = childInterface.id;
											menuActionRow++;
										}
									} else {
										if (childInterface.inventory) {
											for (int l3 = 4; l3 >= 3; l3--)
												if (itemDef.actions != null && itemDef.actions[l3] != null) {
													menuActionName[menuActionRow] = itemDef.actions[l3] + " @lre@"
															+ itemDef.name;
													if (l3 == 3)
														menuActionId[menuActionRow] = 493;
													if (l3 == 4)
														menuActionId[menuActionRow] = 847;
													menuActionData1[menuActionRow] = itemDef.id;
													menuActionData2[menuActionRow] = slot;
													menuActionData3[menuActionRow] = childInterface.id;
													menuActionRow++;
												} else if (l3 == 4) {
													menuActionName[menuActionRow] = "Drop @lre@" + itemDef.name;
													menuActionId[menuActionRow] = 847;
													menuActionData1[menuActionRow] = itemDef.id;
													menuActionData2[menuActionRow] = slot;
													menuActionData3[menuActionRow] = childInterface.id;
													menuActionRow++;
												}

										}
										if (childInterface.usableItemInterface) {
											menuActionName[menuActionRow] = "Use @lre@" + itemDef.name;
											menuActionId[menuActionRow] = 447;
											menuActionData1[menuActionRow] = itemDef.id;
											menuActionData2[menuActionRow] = slot;
											menuActionData3[menuActionRow] = childInterface.id;
											menuActionRow++;
										}
										if (childInterface.inventory && itemDef.actions != null) {
											for (int i4 = 2; i4 >= 0; i4--)
												if (itemDef.actions[i4] != null) {
													menuActionName[menuActionRow] = itemDef.actions[i4] + " @lre@"
															+ itemDef.name;
													if (i4 == 0)
														menuActionId[menuActionRow] = 74;
													if (i4 == 1)
														menuActionId[menuActionRow] = 454;
													if (i4 == 2)
														menuActionId[menuActionRow] = 539;
													menuActionData1[menuActionRow] = itemDef.id;
													menuActionData2[menuActionRow] = slot;
													menuActionData3[menuActionRow] = childInterface.id;
													menuActionRow++;
												}

										}
										if (childInterface.actions != null) {
											for (int j4 = 4; j4 >= 0; j4--)
												if (childInterface.actions[j4] != null) {
													menuActionName[menuActionRow] = childInterface.actions[j4]
															+ " @lre@" + itemDef.name;
													if (j4 == 0)
														menuActionId[menuActionRow] = 632;
													if (j4 == 1)
														menuActionId[menuActionRow] = 78;
													if (j4 == 2)
														menuActionId[menuActionRow] = 867;
													if (j4 == 3)
														menuActionId[menuActionRow] = 431;
													if (j4 == 4)
														menuActionId[menuActionRow] = 53;
													menuActionData1[menuActionRow] = itemDef.id;
													menuActionData2[menuActionRow] = slot;
													menuActionData3[menuActionRow] = childInterface.id;
													menuActionRow++;
												}

										}
										menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@"
												+ (childInterface.inventoryItemId[slot] - 1) + "@gre@)";
										menuActionId[menuActionRow] = 1125;
										menuActionData1[menuActionRow] = itemDef.id;
										menuActionData2[menuActionRow] = slot;
										menuActionData3[menuActionRow] = childInterface.id;
										menuActionRow++;
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

	private void buildMenuForNPC(EntityDefinition definition, int data1, int data3, int data2) {
		if (menuActionRow >= 400)
			return;
		if (definition.childrenIDs != null)
			definition = definition.getChildDefinition();
		if (definition == null)
			return;
		if (!definition.clickable)
			return;
		String displayName = definition.name;
		if (definition.combatLevel != 0)
			displayName = displayName + getCombatLevelDifferenceColour(localPlayer.combatLevel, definition.combatLevel)
					+ " (level-" + definition.combatLevel + ")";
		if (itemSelected) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + displayName;
			menuActionId[menuActionRow] = 582;
			menuActionData1[menuActionRow] = data1;
			menuActionData2[menuActionRow] = data2;
			menuActionData3[menuActionRow] = data3;
			menuActionRow++;
			return;
		}
		if (spellSelected) {
			if ((spellUsableOn & 2) == 2) {
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + displayName;
				menuActionId[menuActionRow] = 413;
				menuActionData1[menuActionRow] = data1;
				menuActionData2[menuActionRow] = data2;
				menuActionData3[menuActionRow] = data3;
				menuActionRow++;
			}
		} else {
			if (definition.actions != null) {
				for (int a = 4; a >= 0; a--)
					if (definition.actions[a] != null && !definition.actions[a].equalsIgnoreCase("attack")) {
						menuActionName[menuActionRow] = definition.actions[a] + " @yel@" + displayName;
						if (a == 0)
							menuActionId[menuActionRow] = 20;
						if (a == 1)
							menuActionId[menuActionRow] = 412;
						if (a == 2)
							menuActionId[menuActionRow] = 225;
						if (a == 3)
							menuActionId[menuActionRow] = 965;
						if (a == 4)
							menuActionId[menuActionRow] = 478;
						menuActionData1[menuActionRow] = data1;
						menuActionData2[menuActionRow] = data2;
						menuActionData3[menuActionRow] = data3;
						menuActionRow++;
					}

			}
			if (definition.actions != null) {
				for (int a = 4; a >= 0; a--)
					if (definition.actions[a] != null && definition.actions[a].equalsIgnoreCase("attack")) {
						int modifier = 0;
						if (definition.combatLevel > localPlayer.combatLevel)
							modifier = 2000;
						menuActionName[menuActionRow] = definition.actions[a] + " @yel@" + displayName;
						if (a == 0)
							menuActionId[menuActionRow] = 20 + modifier;
						if (a == 1)
							menuActionId[menuActionRow] = 412 + modifier;
						if (a == 2)
							menuActionId[menuActionRow] = 225 + modifier;
						if (a == 3)
							menuActionId[menuActionRow] = 965 + modifier;
						if (a == 4)
							menuActionId[menuActionRow] = 478 + modifier;
						menuActionData1[menuActionRow] = data1;
						menuActionData2[menuActionRow] = data2;
						menuActionData3[menuActionRow] = data3;
						menuActionRow++;
					}

			}
			menuActionName[menuActionRow] = "Examine @yel@" + displayName + " @gre@(@whi@" + definition.id + "@gre@)";
			menuActionId[menuActionRow] = 1025;
			menuActionData1[menuActionRow] = data1;
			menuActionData2[menuActionRow] = data2;
			menuActionData3[menuActionRow] = data3;
			menuActionRow++;
		}
	}

	private void buildMenuForPlayer(int data2, int data1, Player player, int data3) {
		if (player == localPlayer)
			return;
		if (menuActionRow >= 400)
			return;
		String displayName;
		if (player.skill == 0)
			displayName = player.name + getCombatLevelDifferenceColour(localPlayer.combatLevel, player.combatLevel)
					+ " (level-" + player.combatLevel + ")";
		else
			displayName = player.name + " (skill-" + player.skill + ")";
		if (itemSelected) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @whi@" + displayName;
			menuActionId[menuActionRow] = 491;
			menuActionData1[menuActionRow] = data1;
			menuActionData2[menuActionRow] = data2;
			menuActionData3[menuActionRow] = data3;
			menuActionRow++;
		} else if (spellSelected) {
			if ((spellUsableOn & 8) == 8) {
				menuActionName[menuActionRow] = spellTooltip + " @whi@" + displayName;
				menuActionId[menuActionRow] = 365;
				menuActionData1[menuActionRow] = data1;
				menuActionData2[menuActionRow] = data2;
				menuActionData3[menuActionRow] = data3;
				menuActionRow++;
			}
		} else {
			for (int a = 4; a >= 0; a--)
				if (playerActionText[a] != null) {
					menuActionName[menuActionRow] = playerActionText[a] + " @whi@" + displayName;
					int modifier = 0;
					if (playerActionText[a].equalsIgnoreCase("attack")) {
						if (player.combatLevel > localPlayer.combatLevel)
							modifier = 2000;
						if (localPlayer.team != 0 && player.team != 0)
							if (localPlayer.team == player.team)
								modifier = 2000;
							else
								modifier = 0;
					} else if (playerActionUnpinned[a])
						modifier = 2000;
					if (a == 0)
						menuActionId[menuActionRow] = 561 + modifier;
					if (a == 1)
						menuActionId[menuActionRow] = 779 + modifier;
					if (a == 2)
						menuActionId[menuActionRow] = 27 + modifier;
					if (a == 3)
						menuActionId[menuActionRow] = 577 + modifier;
					if (a == 4)
						menuActionId[menuActionRow] = 729 + modifier;
					menuActionData1[menuActionRow] = data1;
					menuActionData2[menuActionRow] = data2;
					menuActionData3[menuActionRow] = data3;
					menuActionRow++;
				}

		}
		for (int a = 0; a < menuActionRow; a++)
			if (menuActionId[a] == 516) {
				menuActionName[a] = "Walk here @whi@" + displayName;
				return;
			}

	}

	private void buildSplitPrivateChatMenu() {
		if (splitPrivateChat == 0)
			return;
		int line = 0;
		if (systemUpdateTime != 0)
			line = 1;
		for (int c = 0; c < 100; c++)
			if (chatMessages[c] != null) {
				int chatType = chatTypes[c];
				String chatName = chatNames[c];
				if (chatName != null && chatName.startsWith("@cr1@")) {
					chatName = chatName.substring(5);
				}
				if (chatName != null && chatName.startsWith("@cr2@")) {
					chatName = chatName.substring(5);
				}
				if ((chatType == 3 || chatType == 7) && (chatType == 7 || privateChatMode == 0
						|| privateChatMode == 1 && isFriendOrSelf(chatName))) {
					int height = 329 - line * 13;
					if (super.mouseX > 4 && super.mouseY - 4 > height - 10 && super.mouseY - 4 <= height + 3) {
						int width = fontPlain.getTextDisplayedWidth("From:  " + chatName + chatMessages[c]) + 25;
						if (width > 450)
							width = 450;
						if (super.mouseX < 4 + width) {
							if (playerRights >= 1) {
								menuActionName[menuActionRow] = "Report abuse @whi@" + chatName;
								menuActionId[menuActionRow] = 2606;
								menuActionRow++;
							}
							menuActionName[menuActionRow] = "Add ignore @whi@" + chatName;
							menuActionId[menuActionRow] = 2042;
							menuActionRow++;
							menuActionName[menuActionRow] = "Add friend @whi@" + chatName;
							menuActionId[menuActionRow] = 2337;
							menuActionRow++;
						}
					}
					if (++line >= 5)
						return;
				}
				if ((chatType == 5 || chatType == 6) && privateChatMode < 2 && ++line >= 5)
					return;
			}

	}

	private void calcFlamesPosition() {
		char c = '\u0100';
		for (int j = 10; j < 117; j++) {
			int k = (int) (Math.random() * 100D);
			if (k < 50)
				anIntArray828[j + (c - 2 << 7)] = 255;
		}
		for (int l = 0; l < 100; l++) {
			int i1 = (int) (Math.random() * 124D) + 2;
			int k1 = (int) (Math.random() * 128D) + 128;
			int k2 = i1 + (k1 << 7);
			anIntArray828[k2] = 192;
		}

		for (int j1 = 1; j1 < c - 1; j1++) {
			for (int l1 = 1; l1 < 127; l1++) {
				int l2 = l1 + (j1 << 7);
				anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128]
						+ anIntArray828[l2 + 128]) / 4;
			}

		}

		anInt1275 += 128;
		if (anInt1275 > anIntArray1190.length) {
			anInt1275 -= anIntArray1190.length;
			int i2 = (int) (Math.random() * 12D);
			randomizeBackground(flameRuneImage[i2]);
		}
		for (int j2 = 1; j2 < c - 1; j2++) {
			for (int i3 = 1; i3 < 127; i3++) {
				int k3 = i3 + (j2 << 7);
				int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
				if (i4 < 0)
					i4 = 0;
				anIntArray828[k3] = i4;
			}

		}

		System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

		anIntArray969[c
				- 1] = (int) (Math.sin(tick / 14D) * 16D + Math.sin(tick / 15D) * 14D + Math.sin(tick / 16D) * 12D);
		if (anInt1040 > 0)
			anInt1040 -= 4;
		if (anInt1041 > 0)
			anInt1041 -= 4;
		if (anInt1040 == 0 && anInt1041 == 0) {
			int l3 = (int) (Math.random() * 2000D);
			if (l3 == 0)
				anInt1040 = 1024;
			if (l3 == 1)
				anInt1041 = 1024;
		}
	}

	private void calculateEntityScreenPosition(Entity entity, int height) {
		calculateScreenPosition(entity.x, height, entity.y);

		// aryan entity.entScreenX = spriteDrawX; entity.entScreenY =
		// spriteDrawY;
	}

	private void calculateScreenPosition(int x, int height, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			spriteDrawX = -1;
			spriteDrawY = -1;
			return;
		}
		int z = getFloorDrawHeight(plane, y, x) - height;
		x -= cameraPositionX;
		z -= cameraPositionZ;
		y -= cameraPositionY;
		int sineHorizontal = Model.SINE[cameraVerticalRotation];
		int cosineHorizontal = Model.COSINE[cameraVerticalRotation];
		int sineVertical = Model.SINE[cameraHorizontalRotation];
		int cosineVertical = Model.COSINE[cameraHorizontalRotation];
		int temp = y * sineVertical + x * cosineVertical >> 16;
		y = y * cosineVertical - x * sineVertical >> 16;
		x = temp;
		temp = z * cosineHorizontal - y * sineHorizontal >> 16;
		y = z * sineHorizontal + y * cosineHorizontal >> 16;
		z = temp;
		if (y >= 50) {
			spriteDrawX = Rasterizer.centreX + (x << 9) / y;
			spriteDrawY = Rasterizer.centreY + (z << 9) / y;
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}

	private void changeGender() {
		characterModelChanged = true;
		for (int type = 0; type < 7; type++) {
			characterEditIdentityKits[type] = -1;
			for (int kit = 0; kit < IdentityKit.count; kit++) {
				if (IdentityKit.cache[kit].widgetDisplayed
						|| IdentityKit.cache[kit].partId != type + (characterEditChangeGender ? 0 : 7))
					continue;
				characterEditIdentityKits[type] = kit;
				break;
			}

		}

	}

	private void checkTutorialIsland() {
		inTutorial = 0;
		int x = (localPlayer.x >> 7) + baseX;
		int y = (localPlayer.y >> 7) + baseY;
		if (x >= 3053 && x <= 3156 && y >= 3056 && y <= 3136)
			inTutorial = 1;
		if (x >= 3072 && x <= 3118 && y >= 9492 && y <= 9535)
			inTutorial = 1;
		if (inTutorial == 1 && x >= 3139 && x <= 3199 && y >= 3008 && y <= 3062)
			inTutorial = 0;
	}

	@Override
	public void cleanUpForQuit() {
		signlink.reporterror = false;
		try {
			if (socket != null)
				socket.close();
		} catch (Exception _ex) {
		}
		socket = null;
		stopMidi();
		if (mouseDetection != null)
			mouseDetection.running = false;
		mouseDetection = null;
		onDemandFetcher.disable();
		onDemandFetcher = null;
		textStream = null;
		stream = null;
		loginStream = null;
		inStream = null;
		mapCoordinates = null;
		terrainData = null;
		objectData = null;
		terrainDataIds = null;
		objectDataIds = null;
		intGroundArray = null;
		tileFlags = null;
		worldController = null;
		currentCollisionMap = null;
		wayPoints = null;
		distanceValues = null;
		walkingQueueX = null;
		walkingQueueY = null;
		animatedPixels = null;
		tabImageProducer = null;
		minimapImageProducer = null;
		gameScreenImageProducer = null;
		chatboxImageProducer = null;
		chatSettingImageProducer = null;
		bottomSideIconImageProducer = null;
		topSideIconImageProducer = null;
		backLeftIP1 = null;
		backLeftIP2 = null;
		backRightIP1 = null;
		backRightIP2 = null;
		backTopIP1 = null;
		backVmidIP1 = null;
		backVmidIP2 = null;
		backVmidIP3 = null;
		backVmidIP2_2 = null;
		inventoryBackgroundImage = null;
		minimapBackgroundImage = null;
		chatBackgroundImage = null;
		backBase1Image = null;
		backBase2Image = null;
		backHmid1Image = null;
		sideIconImage = null;
		redStone1 = null;
		redStone2 = null;
		redStone3 = null;
		redStone1_2 = null;
		redStone2_2 = null;
		redStone1_3 = null;
		redStone2_3 = null;
		redStone3_2 = null;
		redStone1_4 = null;
		redStone2_4 = null;
		minimapCompassImage = null;
		hitMarkImage = null;
		headIcons = null;
		crosses = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapDotTeam = null;
		mapSceneImage = null;
		mapFunctionImage = null;
		tileRenderCount = null;
		players = null;
		localPlayers = null;
		playersObserved = null;
		playerAppearanceData = null;
		actorsToUpdateIds = null;
		npcs = null;
		npcIds = null;
		groundArray = null;
		spawnObjectList = null;
		projectileQueue = null;
		stationaryGraphicQueue = null;
		menuActionData2 = null;
		menuActionData3 = null;
		menuActionId = null;
		menuActionData1 = null;
		menuActionName = null;
		interfaceSettings = null;
		minimapHintX = null;
		minimapHintY = null;
		minimapHint = null;
		minimapImage = null;
		friendsList = null;
		friendsListAsLongs = null;
		friendsWorldIds = null;
		flameLeftBackground = null;
		flameRightBackground = null;
		topLeft1BackgroundTile = null;
		bottomLeft1BackgroundTile = null;
		loginBoxLeftBackgroundTile = null;
		bottomLeft0BackgroundTile = null;
		aRSImageProducer_1113 = null;
		middleLeft1BackgroundTile = null;
		aRSImageProducer_1115 = null;
		nullLoader();
		GameObjectDefinition.nullLoader();
		EntityDefinition.nullLoader();
		ItemDefinition.nullLoader();
		FloorDefinition.cache = null;
		IdentityKit.cache = null;
		RSInterface.cache = null;
		AnimationSequence.animations = null;
		SpotAnimation.cache = null;
		SpotAnimation.modelCache = null;
		Varp.cache = null;
		super.fullGameScreen = null;
		Player.mruNodes = null;
		Rasterizer.nullLoader();
		WorldController.nullLoader();
		Model.nullLoader();
		Animation.nullLoader();
		System.gc();
	}

	private void clearObjectSpawnRequests() {
		GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) spawnObjectList.peekLast();
		for (; spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) spawnObjectList.reverseGetNext())
			if (spawnRequest.delayUntilRespawn == -1) {
				spawnRequest.delayUntilSpawn = 0;
				configureSpawnRequest(spawnRequest);
			} else {
				spawnRequest.unlink();
			}

	}

	private void clearTopInterfaces() {
		stream.putOpcode(130);
		if (inventoryOverlayInterfaceID != -1) {
			inventoryOverlayInterfaceID = -1;
			redrawTab = true;
			continuedDialogue = false;
			drawTabIcons = true;
		}
		if (chatboxInterfaceId != -1) {
			chatboxInterfaceId = -1;
			redrawChatbox = true;
			continuedDialogue = false;
		}
		openInterfaceId = -1;
	}

	private boolean clickInteractiveObject(int hash, int y, int x) {
		int objectId = hash >> 14 & 0x7fff;
		int config = worldController.getConfig(hash, x, y, plane);
		if (config == -1)
			return false;
		int type = config & 0x1f;
		int rotation = config >> 6 & 3;
		if (type == 10 || type == 11 || type == 22) {
			GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
			int sizeX;
			int sizeY;
			if (rotation == 0 || rotation == 2) {
				sizeX = object.sizeX;
				sizeY = object.sizeY;
			} else {
				sizeX = object.sizeY;
				sizeY = object.sizeX;
			}
			int surroundings = object.face;
			if (rotation != 0)
				surroundings = (surroundings << rotation & 0xf) + (surroundings >> 4 - rotation);
			doWalkTo(2, 0, sizeY, 0, localPlayer.waypointY[0], sizeX, surroundings, y, localPlayer.waypointX[0], false,
					x);
		} else {
			doWalkTo(2, rotation, 0, type + 1, localPlayer.waypointY[0], 0, 0, y, localPlayer.waypointX[0], false, x);
		}
		crossX = super.clickX;
		crossY = super.clickY;
		crossType = 2;
		crossIndex = 0;
		return true;
	}

	private void configureSpawnRequest(GameObjectSpawnRequest spawnRequest) {
		int uid = 0;
		int id = -1;
		int type = 0;
		int face = 0;
		if (spawnRequest.objectType == 0)
			uid = worldController.getWallObjectHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
		if (spawnRequest.objectType == 1)
			uid = worldController.getWallDecorationHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
		if (spawnRequest.objectType == 2)
			uid = worldController.getInteractibleObjectHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
		if (spawnRequest.objectType == 3)
			uid = worldController.getGroundDecorationHash(spawnRequest.x, spawnRequest.y, spawnRequest.z);
		if (uid != 0) {
			int config = worldController.getConfig(uid, spawnRequest.x, spawnRequest.y, spawnRequest.z);
			id = uid >> 14 & 0x7fff;
			type = config & 0x1f;
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

	private void createObjectSpawnRequest(int delayUntilRespawn, int id2, int face2, int type, int y, int type2, int z,
			int x, int delayUntilSpawn) {
		GameObjectSpawnRequest request = null;
		for (GameObjectSpawnRequest request2 = (GameObjectSpawnRequest) spawnObjectList
				.peekLast(); request2 != null; request2 = (GameObjectSpawnRequest) spawnObjectList.reverseGetNext()) {
			if (request2.z != z || request2.x != x || request2.y != y || request2.objectType != type)
				continue;

			request = request2;
			break;
		}

		if (request == null) {
			request = new GameObjectSpawnRequest();
			request.z = z;
			request.objectType = type;
			request.x = x;
			request.y = y;
			configureSpawnRequest(request);
			spawnObjectList.insertHead(request);
		}
		request.id2 = id2;
		request.type2 = type2;
		request.face2 = face2;
		request.delayUntilSpawn = delayUntilSpawn;
		request.delayUntilRespawn = delayUntilRespawn;
	}

	private void cycleEntitySpokenText() {
		for (int p = -1; p < localPlayerCount; p++) {
			int pId;
			if (p == -1)
				pId = LOCAL_PLAYER_ID;
			else
				pId = localPlayers[p];
			Player player = players[pId];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0)
					player.overheadTextMessage = null;
			}
		}

		for (int n = 0; n < npcCount; n++) {
			int nId = npcIds[n];
			NPC npc = npcs[nId];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0)
					npc.overheadTextMessage = null;
			}
		}
	}

	private void deleteFriend(long friend) {
		try {
			if (friend == 0L)
				return;
			for (int f = 0; f < friendsCount; f++) {
				if (friendsListAsLongs[f] != friend)
					continue;
				friendsCount--;
				redrawTab = true;
				for (int _f = f; _f < friendsCount; _f++) {
					friendsList[_f] = friendsList[_f + 1];
					friendsWorldIds[_f] = friendsWorldIds[_f + 1];
					friendsListAsLongs[_f] = friendsListAsLongs[_f + 1];
				}

				stream.putOpcode(215);
				stream.putLong(friend);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("18622, " + false + ", " + friend + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private void deleteIgnore(long target) {
		try {
			if (target == 0L)
				return;
			for (int i = 0; i < ignoreCount; i++)
				if (ignoreListAsLongs[i] == target) {
					ignoreCount--;
					redrawTab = true;
					System.arraycopy(ignoreListAsLongs, i + 1, ignoreListAsLongs, i, ignoreCount - i);

					stream.putOpcode(74);
					stream.putLong(target);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + target + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void despawnGameObject(int y, int z, int face, int l, int x, int objectType, int objectId) {
		if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
			if (lowMemory && z != plane)
				return;
			int hash = 0;
			if (objectType == 0)
				hash = worldController.getWallObjectHash(x, y, z);
			if (objectType == 1)
				hash = worldController.getWallDecorationHash(x, y, z);
			if (objectType == 2)
				hash = worldController.getInteractibleObjectHash(x, y, z);
			if (objectType == 3)
				hash = worldController.getGroundDecorationHash(x, y, z);
			if (hash != 0) {
				int config = worldController.getConfig(hash, x, y, z);
				int _objectId = hash >> 14 & 0x7fff;
				int position = config & 0x1f;
				int orientation = config >> 6;
				if (objectType == 0) {
					worldController.removeWallObject(x, z, y);
					GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (object.solid)
						currentCollisionMap[z].unmarkWall(x, y, position, orientation, object.walkable);
				}
				if (objectType == 1)
					worldController.removeWallDecoration(x, y, z);
				if (objectType == 2) {
					worldController.removeInteractiveObject(x, y, z);
					GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (x + object.sizeX > 103 || y + object.sizeX > 103 || x + object.sizeY > 103
							|| y + object.sizeY > 103)
						return;
					if (object.solid)
						currentCollisionMap[z].unmarkSolidOccupant(x, y, object.sizeX, object.sizeY, orientation,
								object.walkable);
				}
				if (objectType == 3) {
					worldController.removeGroundDecoration(x, y, z);
					GameObjectDefinition object = GameObjectDefinition.getDefinition(_objectId);
					if (object.solid && object.hasActions)
						currentCollisionMap[z].unmarkConcealed(x, y);
				}
			}
			if (objectId >= 0) {
				int height = z;
				if (height < 3 && (tileFlags[1][x][y] & 2) == 2)
					height++;
				Region.forceRenderObject(worldController, face, y, l, height, currentCollisionMap[z], intGroundArray, x,
						objectId, z);
			}
		}
	}

	private void doAction(int row) {
		if (row < 0)
			return;
		if (inputDialogState != 0) {
			inputDialogState = 0;
			redrawChatbox = true;
		}
		int actionInformation2 = menuActionData2[row];
		int actionInformation1 = menuActionData3[row];
		int menuAction = menuActionId[row];
		int actionTarget = menuActionData1[row];
		if (menuAction >= 2000)
			menuAction -= 2000;
		if (menuAction == 582) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(57);
				stream.putShortA(useItemId);
				stream.putShortA(actionTarget);
				stream.putLEShort(lastItemSelectedSlot);
				stream.putShortA(lastItemSelectedInterface);
			}
		}
		if (menuAction == 234) {
			boolean flag1 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag1)
				flag1 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(236);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putShort(actionTarget);
			stream.putLEShort(actionInformation2 + baseX);
		}
		if (menuAction == 62 && clickInteractiveObject(actionTarget, actionInformation1, actionInformation2)) {
			stream.putOpcode(192);
			stream.putShort(lastItemSelectedInterface);
			stream.putLEShort(actionTarget >> 14 & 0x7fff);
			stream.putLEShortA(actionInformation1 + baseY);
			stream.putLEShort(lastItemSelectedSlot);
			stream.putLEShortA(actionInformation2 + baseX);
			stream.putShort(useItemId);
		}
		if (menuAction == 511) {
			boolean flag2 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag2)
				flag2 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(25);
			stream.putLEShort(lastItemSelectedInterface);
			stream.putShortA(useItemId);
			stream.putShort(actionTarget);
			stream.putShortA(actionInformation1 + baseY);
			stream.putLEShortA(lastItemSelectedSlot);
			stream.putShort(actionInformation2 + baseX);
		}
		if (menuAction == 74) {
			stream.putOpcode(122);
			stream.putLEShortA(actionInformation1);
			stream.putShortA(actionInformation2);
			stream.putLEShort(actionTarget);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 315) {
			RSInterface rsInterface = RSInterface.cache[actionInformation1];
			boolean flag8 = true;
			if (rsInterface.contentType > 0)
				flag8 = promptUserForInput(rsInterface);
			if (flag8) {
				stream.putOpcode(185);
				stream.putShort(actionInformation1);
			}
		}
		if (menuAction == 561) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(128);
				stream.putShort(actionTarget);
			}
		}
		if (menuAction == 20) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(155);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 779) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(153);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 516)
			if (!menuOpen)
				worldController.request2DTrace(super.clickX - 4, super.clickY - 4);
			else
				worldController.request2DTrace(actionInformation2 - 4, actionInformation1 - 4);
		if (menuAction == 1062) {
			clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			stream.putOpcode(228);
			stream.putShortA(actionTarget >> 14 & 0x7fff);
			stream.putShortA(actionInformation1 + baseY);
			stream.putShort(actionInformation2 + baseX);
		}
		if (menuAction == 679 && !continuedDialogue) {
			stream.putOpcode(40);
			stream.putShort(actionInformation1);
			continuedDialogue = true;
		}
		if (menuAction == 431) {
			stream.putOpcode(129);
			stream.putShortA(actionInformation2);
			stream.putShort(actionInformation1);
			stream.putShortA(actionTarget);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 337 || menuAction == 42 || menuAction == 792 || menuAction == 322) {
			String name = menuActionName[row];
			int position = name.indexOf("@whi@");
			if (position != -1) {
				long targetAsLong = TextClass.nameToLong(name.substring(position + 5).trim());
				if (menuAction == 337)
					addFriend(targetAsLong);
				if (menuAction == 42)
					addIgnore(targetAsLong);
				if (menuAction == 792)
					deleteFriend(targetAsLong);
				if (menuAction == 322)
					deleteIgnore(targetAsLong);
			}
		}
		if (menuAction == 53) {
			stream.putOpcode(135);
			stream.putLEShort(actionInformation2);
			stream.putShortA(actionInformation1);
			stream.putLEShort(actionTarget);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 539) {
			stream.putOpcode(16);
			stream.putShortA(actionTarget);
			stream.putLEShortA(actionInformation2);
			stream.putLEShortA(actionInformation1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 484 || menuAction == 6) {
			String name = menuActionName[row];
			int position = name.indexOf("@whi@");
			if (position != -1) {
				name = name.substring(position + 5).trim();
				String nameAsLong = TextClass.formatName(TextClass.longToName(TextClass.nameToLong(name)));
				boolean foundPlayer = false;
				for (int p = 0; p < localPlayerCount; p++) {
					Player player = players[localPlayers[p]];
					if (player == null || player.name == null || !player.name.equalsIgnoreCase(nameAsLong))
						continue;
					doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
							false, player.waypointX[0]);
					if (menuAction == 484) {
						// Follow another player

						stream.putOpcode(139);
						stream.putLEShort(localPlayers[p]);
					}
					if (menuAction == 6) {
						stream.putOpcode(128);
						stream.putShort(localPlayers[p]);
					}
					foundPlayer = true;
					break;
				}

				if (!foundPlayer)
					pushMessage("Unable to find " + nameAsLong, 0, "");
			}
		}
		if (menuAction == 870) {
			stream.putOpcode(53);
			stream.putShort(actionInformation2);
			stream.putShortA(lastItemSelectedSlot);
			stream.putLEShortA(actionTarget);
			stream.putShort(lastItemSelectedInterface);
			stream.putLEShort(useItemId);
			stream.putShort(actionInformation1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 847) {
			stream.putOpcode(87);
			stream.putShortA(actionTarget);
			stream.putShort(actionInformation1);
			stream.putShortA(actionInformation2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 626) {
			RSInterface rsInterface = RSInterface.cache[actionInformation1];
			spellSelected = true;
			selectedSpellId = actionInformation1;
			spellUsableOn = rsInterface.spellUsableOn;
			itemSelected = false;
			redrawTab = true;
			String namePartOne = rsInterface.selectedActionName;
			if (namePartOne.indexOf(" ") != -1)
				namePartOne = namePartOne.substring(0, namePartOne.indexOf(" "));
			String namePartTwo = rsInterface.selectedActionName;
			if (namePartTwo.indexOf(" ") != -1)
				namePartTwo = namePartTwo.substring(namePartTwo.indexOf(" ") + 1);
			spellTooltip = namePartOne + " " + rsInterface.spellName + " " + namePartTwo;
			if (spellUsableOn == 16) {
				redrawTab = true;
				currentTabId = 3;
				drawTabIcons = true;
			}
			return;
		}
		if (menuAction == 78) {
			stream.putOpcode(117);
			stream.putLEShortA(actionInformation1);
			stream.putLEShortA(actionTarget);
			stream.putLEShort(actionInformation2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 27) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(73);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 213) {
			boolean flag3 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag3)
				flag3 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(79);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putShort(actionTarget);
			stream.putShortA(actionInformation2 + baseX);
		}
		if (menuAction == 632) {
			stream.putOpcode(145);
			stream.putShortA(actionInformation1);
			stream.putShortA(actionInformation2);
			stream.putShortA(actionTarget);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 493) {
			stream.putOpcode(75);
			stream.putLEShortA(actionInformation1);
			stream.putLEShort(actionInformation2);
			stream.putShortA(actionTarget);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 652) {
			boolean flag4 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag4)
				flag4 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(156);
			stream.putShortA(actionInformation2 + baseX);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putLEShortA(actionTarget);
		}
		if (menuAction == 94) {
			boolean flag5 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag5)
				flag5 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(181);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putShort(actionTarget);
			stream.putLEShort(actionInformation2 + baseX);
			stream.putShortA(selectedSpellId);
		}
		if (menuAction == 646) {
			stream.putOpcode(185);
			stream.putShort(actionInformation1);
			RSInterface rsInterface = RSInterface.cache[actionInformation1];
			if (rsInterface.opcodes != null && rsInterface.opcodes[0][0] == 5) {
				int setting = rsInterface.opcodes[0][1];
				if (interfaceSettings[setting] != rsInterface.conditionValue[0]) {
					interfaceSettings[setting] = rsInterface.conditionValue[0];
					handleInterfaceSetting(setting);
					redrawTab = true;
				}
			}
		}
		if (menuAction == 225) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(17);
				stream.putLEShortA(actionTarget);
			}
		}
		if (menuAction == 965) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(21);
				stream.putShort(actionTarget);
			}
		}
		if (menuAction == 413) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(131);
				stream.putLEShortA(actionTarget);
				stream.putShortA(selectedSpellId);
			}
		}
		if (menuAction == 200)
			clearTopInterfaces();
		if (menuAction == 1025) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				EntityDefinition entityDef = npc.npcDefinition;
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.getChildDefinition();
				if (entityDef != null) {
					String description;
					if (entityDef.description != null)
						description = new String(entityDef.description);
					else
						description = "It's a " + entityDef.name + ".";
					pushMessage(description, 0, "");
				}
			}
		}
		if (menuAction == 900) {
			clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			stream.putOpcode(252);
			stream.putLEShortA(actionTarget >> 14 & 0x7fff);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putShortA(actionInformation2 + baseX);
		}
		if (menuAction == 412) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(72);
				stream.putShortA(actionTarget);
			}
		}
		if (menuAction == 365) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(249);
				stream.putShortA(actionTarget);
				stream.putLEShort(selectedSpellId);
			}
		}
		if (menuAction == 729) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(39);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 577) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(139);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 956 && clickInteractiveObject(actionTarget, actionInformation1, actionInformation2)) {
			stream.putOpcode(35);
			stream.putLEShort(actionInformation2 + baseX);
			stream.putShortA(selectedSpellId);
			stream.putShortA(actionInformation1 + baseY);
			stream.putLEShort(actionTarget >> 14 & 0x7fff);
		}
		if (menuAction == 567) {
			boolean flag6 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag6)
				flag6 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(23);
			stream.putLEShort(actionInformation1 + baseY);
			stream.putLEShort(actionTarget);
			stream.putLEShort(actionInformation2 + baseX);
		}
		if (menuAction == 867) {
			stream.putOpcode(43);
			stream.putLEShort(actionInformation1);
			stream.putShortA(actionTarget);
			stream.putShortA(actionInformation2);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 543) {
			stream.putOpcode(237);
			stream.putShort(actionInformation2);
			stream.putShortA(actionTarget);
			stream.putShort(actionInformation1);
			stream.putShortA(selectedSpellId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 606) {
			String name = menuActionName[row];
			int position = name.indexOf("@whi@");
			if (position != -1)
				if (openInterfaceId == -1) {
					clearTopInterfaces();
					reportAbuseInput = name.substring(position + 5).trim();
					reportAbuseMute = false;
					for (int rsInterface = 0; rsInterface < RSInterface.cache.length; rsInterface++) {
						if (RSInterface.cache[rsInterface] == null || RSInterface.cache[rsInterface].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceId = RSInterface.cache[rsInterface].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
		}
		if (menuAction == 491) {
			Player player = players[actionTarget];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, player.waypointY[0], localPlayer.waypointX[0],
						false, player.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(14);
				stream.putShortA(lastItemSelectedInterface);
				stream.putShort(actionTarget);
				stream.putShort(useItemId);
				stream.putLEShort(lastItemSelectedSlot);
			}
		}
		if (menuAction == 639) {
			String name = menuActionName[row];
			int position = name.indexOf("@whi@");
			if (position != -1) {
				long nameAsLong = TextClass.nameToLong(name.substring(position + 5).trim());
				int target = -1;
				for (int friend = 0; friend < friendsCount; friend++) {
					if (friendsListAsLongs[friend] != nameAsLong)
						continue;
					target = friend;
					break;
				}

				if (target != -1 && friendsWorldIds[target] > 0) {
					redrawChatbox = true;
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					privateMessageTarget = friendsListAsLongs[target];
					chatboxInputNeededString = "Enter message to send to " + friendsList[target];
				}
			}
		}
		if (menuAction == 454) {
			stream.putOpcode(41);
			stream.putShort(actionTarget);
			stream.putShortA(actionInformation2);
			stream.putShortA(actionInformation1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = actionInformation1;
			atInventoryIndex = actionInformation2;
			atInventoryInterfaceType = 2;
			if (RSInterface.cache[actionInformation1].parentID == openInterfaceId)
				atInventoryInterfaceType = 1;
			if (RSInterface.cache[actionInformation1].parentID == chatboxInterfaceId)
				atInventoryInterfaceType = 3;
		}
		if (menuAction == 478) {
			NPC npc = npcs[actionTarget];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, npc.waypointY[0], localPlayer.waypointX[0], false,
						npc.waypointX[0]);
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 2;
				crossIndex = 0;
				stream.putOpcode(18);
				stream.putLEShort(actionTarget);
			}
		}
		if (menuAction == 113) {
			clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			stream.putOpcode(70);
			stream.putLEShort(actionInformation2 + baseX);
			stream.putShort(actionInformation1 + baseY);
			stream.putLEShortA(actionTarget >> 14 & 0x7fff);
		}
		if (menuAction == 872) {
			clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			stream.putOpcode(234);
			stream.putLEShortA(actionInformation2 + baseX);
			stream.putShortA(actionTarget >> 14 & 0x7fff);
			stream.putLEShortA(actionInformation1 + baseY);
		}
		if (menuAction == 502) {
			clickInteractiveObject(actionTarget, actionInformation1, actionInformation2);
			stream.putOpcode(132);
			stream.putLEShortA(actionInformation2 + baseX);
			stream.putShort(actionTarget >> 14 & 0x7fff);
			stream.putShortA(actionInformation1 + baseY);
		}
		if (menuAction == 1125) {
			ItemDefinition item = ItemDefinition.getDefinition(actionTarget);
			RSInterface rsInterface = RSInterface.cache[actionInformation1];
			String description;
			if (rsInterface != null && rsInterface.inventoryStackSize[actionInformation2] >= 0x186a0)
				description = rsInterface.inventoryStackSize[actionInformation2] + " x " + item.name;
			else if (item.description != null)
				description = new String(item.description);
			else
				description = "It's a " + item.name + ".";
			pushMessage(description, 0, "");
		}
		if (menuAction == 169) {
			stream.putOpcode(185);
			stream.putShort(actionInformation1);
			RSInterface rsInterface = RSInterface.cache[actionInformation1];
			if (rsInterface.opcodes != null && rsInterface.opcodes[0][0] == 5) {
				int setting = rsInterface.opcodes[0][1];
				interfaceSettings[setting] = 1 - interfaceSettings[setting];
				handleInterfaceSetting(setting);
				redrawTab = true;
			}
		}
		if (menuAction == 447) {
			itemSelected = true;
			lastItemSelectedSlot = actionInformation2;
			lastItemSelectedInterface = actionInformation1;
			useItemId = actionTarget;
			selectedItemName = ItemDefinition.getDefinition(actionTarget).name;
			spellSelected = false;
			redrawTab = true;
			return;
		}
		if (menuAction == 1226) {
			int objectId = actionTarget >> 14 & 0x7fff;
			GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
			String description;
			if (object.description != null)
				description = new String(object.description);
			else
				description = "It's a " + object.name + ".";
			pushMessage(description, 0, "");
		}
		if (menuAction == 244) {
			boolean flag7 = doWalkTo(2, 0, 0, 0, localPlayer.waypointY[0], 0, 0, actionInformation1,
					localPlayer.waypointX[0], false, actionInformation2);
			if (!flag7)
				flag7 = doWalkTo(2, 0, 1, 0, localPlayer.waypointY[0], 1, 0, actionInformation1,
						localPlayer.waypointX[0], false, actionInformation2);
			crossX = super.clickX;
			crossY = super.clickY;
			crossType = 2;
			crossIndex = 0;
			stream.putOpcode(253);
			stream.putLEShort(actionInformation2 + baseX);
			stream.putLEShortA(actionInformation1 + baseY);
			stream.putShortA(actionTarget);
		}
		if (menuAction == 1448) {
			ItemDefinition item = ItemDefinition.getDefinition(actionTarget);
			String description;
			if (item.description != null)
				description = new String(item.description);
			else
				description = "It's a " + item.name + ".";
			pushMessage(description, 0, "");
		}
		itemSelected = false;
		spellSelected = false;
		redrawTab = true;
	}

	private void doFlamesDrawing() {
		char c = '\u0100';
		if (anInt1040 > 0) {
			for (int i = 0; i < 256; i++)
				if (anInt1040 > 768)
					currentFlameColours[i] = rotateFlameColour(flameColour1[i], flameColour2[i], 1024 - anInt1040);
				else if (anInt1040 > 256)
					currentFlameColours[i] = flameColour2[i];
				else
					currentFlameColours[i] = rotateFlameColour(flameColour2[i], flameColour1[i], 256 - anInt1040);

		} else if (anInt1041 > 0) {
			for (int j = 0; j < 256; j++)
				if (anInt1041 > 768)
					currentFlameColours[j] = rotateFlameColour(flameColour1[j], flameColour3[j], 1024 - anInt1041);
				else if (anInt1041 > 256)
					currentFlameColours[j] = flameColour3[j];
				else
					currentFlameColours[j] = rotateFlameColour(flameColour3[j], flameColour1[j], 256 - anInt1041);

		} else {
			System.arraycopy(flameColour1, 0, currentFlameColours, 0, 256);

		}
		System.arraycopy(flameLeftBackground2.pixels, 0, flameLeftBackground.pixels, 0, 33920);

		int i1 = 0;
		int j1 = 1152;
		for (int k1 = 1; k1 < c - 1; k1++) {
			int l1 = (anIntArray969[k1] * (c - k1)) / c;
			int j2 = 22 + l1;
			if (j2 < 0)
				j2 = 0;
			i1 += j2;
			for (int l2 = j2; l2 < 128; l2++) {
				int j3 = anIntArray828[i1++];
				if (j3 != 0) {
					int l3 = j3;
					int j4 = 256 - j3;
					j3 = currentFlameColours[j3];
					int l4 = flameLeftBackground.pixels[j1];
					flameLeftBackground.pixels[j1++] = ((j3 & 0xff00ff) * l3 + (l4 & 0xff00ff) * j4 & 0xff00ff00)
							+ ((j3 & 0xff00) * l3 + (l4 & 0xff00) * j4 & 0xff0000) >> 8;
				} else {
					j1++;
				}
			}

			j1 += j2;
		}

		flameLeftBackground.drawGraphics(0, super.gameGraphics, 0);
		System.arraycopy(flameRightBackground2.pixels, 0, flameRightBackground.pixels, 0, 33920);

		i1 = 0;
		j1 = 1176;
		for (int k2 = 1; k2 < c - 1; k2++) {
			int i3 = (anIntArray969[k2] * (c - k2)) / c;
			int k3 = 103 - i3;
			j1 += i3;
			for (int i4 = 0; i4 < k3; i4++) {
				int k4 = anIntArray828[i1++];
				if (k4 != 0) {
					int i5 = k4;
					int j5 = 256 - k4;
					k4 = currentFlameColours[k4];
					int k5 = flameRightBackground.pixels[j1];
					flameRightBackground.pixels[j1++] = ((k4 & 0xff00ff) * i5 + (k5 & 0xff00ff) * j5 & 0xff00ff00)
							+ ((k4 & 0xff00) * i5 + (k5 & 0xff00) * j5 & 0xff0000) >> 8;
				} else {
					j1++;
				}
			}

			i1 += 128 - k3;
			j1 += 128 - k3 - i3;
		}

		flameRightBackground.drawGraphics(0, super.gameGraphics, 637);
	}

	private boolean doWalkTo(int clickType, int objectRotation, int objectSizeY, int objectType, int startY,
			int objectSizeX, int targetSurroundings, int endY, int startX, boolean flag, int endX) {
		byte mapSizeX = 104;
		byte mapSizeY = 104;
		for (int x = 0; x < mapSizeX; x++) {
			for (int y = 0; y < mapSizeY; y++) {
				wayPoints[x][y] = 0;
				distanceValues[x][y] = 0x5f5e0ff;
			}
		}

		int currentX = startX;
		int currentY = startY;
		wayPoints[startX][startY] = 99;
		distanceValues[startX][startY] = 0;
		int nextIndex = 0;
		int currentIndex = 0;
		walkingQueueX[nextIndex] = startX;
		walkingQueueY[nextIndex++] = startY;
		boolean foundDestination = false;
		int maxPathSize = walkingQueueX.length;
		int clippingPaths[][] = currentCollisionMap[plane].clippingData;
		while (currentIndex != nextIndex) {
			currentX = walkingQueueX[currentIndex];
			currentY = walkingQueueY[currentIndex];
			currentIndex = (currentIndex + 1) % maxPathSize;
			if (currentX == endX && currentY == endY) {
				foundDestination = true;
				break;
			}
			if (objectType != 0) {
				if ((objectType < 5 || objectType == 10) && currentCollisionMap[plane].reachedWall(currentX, currentY,
						endX, endY, objectType - 1, objectRotation)) {
					foundDestination = true;
					break;
				}
				if (objectType < 10 && currentCollisionMap[plane].reachedWallDecoration(currentX, currentY, endX, endY,
						objectType - 1, objectRotation)) {
					foundDestination = true;
					break;
				}
			}
			if (objectSizeX != 0 && objectSizeY != 0 && currentCollisionMap[plane].reachedFacingObject(currentX,
					currentY, endX, endY, objectSizeX, objectSizeY, targetSurroundings)) {
				foundDestination = true;
				break;
			}
			int newDistanceValue = distanceValues[currentX][currentY] + 1;
			if (currentX > 0 && wayPoints[currentX - 1][currentY] == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY] = 2;
				distanceValues[currentX - 1][currentY] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && wayPoints[currentX + 1][currentY] == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY] = 8;
				distanceValues[currentX + 1][currentY] = newDistanceValue;
			}
			if (currentY > 0 && wayPoints[currentX][currentY - 1] == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX][currentY - 1] = 1;
				distanceValues[currentX][currentY - 1] = newDistanceValue;
			}
			if (currentY < mapSizeY - 1 && wayPoints[currentX][currentY + 1] == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX][currentY + 1] = 4;
				distanceValues[currentX][currentY + 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY > 0 && wayPoints[currentX - 1][currentY - 1] == 0
					&& (clippingPaths[currentX - 1][currentY - 1] & 0x128010e) == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY - 1] = 3;
				distanceValues[currentX - 1][currentY - 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY > 0 && wayPoints[currentX + 1][currentY - 1] == 0
					&& (clippingPaths[currentX + 1][currentY - 1] & 0x1280183) == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingPaths[currentX][currentY - 1] & 0x1280102) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY - 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY - 1] = 9;
				distanceValues[currentX + 1][currentY - 1] = newDistanceValue;
			}
			if (currentX > 0 && currentY < mapSizeY - 1 && wayPoints[currentX - 1][currentY + 1] == 0
					&& (clippingPaths[currentX - 1][currentY + 1] & 0x1280138) == 0
					&& (clippingPaths[currentX - 1][currentY] & 0x1280108) == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX - 1;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX - 1][currentY + 1] = 6;
				distanceValues[currentX - 1][currentY + 1] = newDistanceValue;
			}
			if (currentX < mapSizeX - 1 && currentY < mapSizeY - 1 && wayPoints[currentX + 1][currentY + 1] == 0
					&& (clippingPaths[currentX + 1][currentY + 1] & 0x12801e0) == 0
					&& (clippingPaths[currentX + 1][currentY] & 0x1280180) == 0
					&& (clippingPaths[currentX][currentY + 1] & 0x1280120) == 0) {
				walkingQueueX[nextIndex] = currentX + 1;
				walkingQueueY[nextIndex] = currentY + 1;
				nextIndex = (nextIndex + 1) % maxPathSize;
				wayPoints[currentX + 1][currentY + 1] = 12;
				distanceValues[currentX + 1][currentY + 1] = newDistanceValue;
			}
		}
		arbitraryDestination = 0;
		if (!foundDestination) {
			if (flag) {
				int maxStepsNonInclusive = 100;
				for (int deviation = 1; deviation < 2; deviation++) {
					for (int deviationX = endX - deviation; deviationX <= endX + deviation; deviationX++) {
						for (int deviationY = endY - deviation; deviationY <= endY + deviation; deviationY++)
							if (deviationX >= 0 && deviationY >= 0 && deviationX < 104 && deviationY < 104
									&& distanceValues[deviationX][deviationY] < maxStepsNonInclusive) {
								maxStepsNonInclusive = distanceValues[deviationX][deviationY];
								currentX = deviationX;
								currentY = deviationY;
								arbitraryDestination = 1;
								foundDestination = true;
							}

					}

					if (foundDestination)
						break;
				}

			}
			if (!foundDestination)
				return false;
		}
		currentIndex = 0;
		walkingQueueX[currentIndex] = currentX;
		walkingQueueY[currentIndex++] = currentY;
		int initialSkipCheck;
		for (int waypoint = initialSkipCheck = wayPoints[currentX][currentY]; currentX != startX
				|| currentY != startY; waypoint = wayPoints[currentX][currentY]) {
			if (waypoint != initialSkipCheck) {
				initialSkipCheck = waypoint;
				walkingQueueX[currentIndex] = currentX;
				walkingQueueY[currentIndex++] = currentY;
			}
			if ((waypoint & 2) != 0)
				currentX++;
			else if ((waypoint & 8) != 0)
				currentX--;
			if ((waypoint & 1) != 0)
				currentY++;
			else if ((waypoint & 4) != 0)
				currentY--;
		}

		if (currentIndex > 0) {
			maxPathSize = currentIndex;
			if (maxPathSize > 25)
				maxPathSize = 25;
			currentIndex--;
			int x = walkingQueueX[currentIndex];
			int y = walkingQueueY[currentIndex];
			currentWalkingQueueSize += maxPathSize;
			if (currentWalkingQueueSize >= 92) {
				stream.putOpcode(36);
				stream.putInt(0);
				currentWalkingQueueSize = 0;
			}
			if (clickType == 0) {
				stream.putOpcode(164);
				stream.put(maxPathSize + maxPathSize + 3);
			}
			if (clickType == 1) {
				stream.putOpcode(248);
				stream.put(maxPathSize + maxPathSize + 3 + 14);
			}
			if (clickType == 2) {
				stream.putOpcode(98);
				stream.put(maxPathSize + maxPathSize + 3);
			}
			stream.putLEShortA(x + baseX);
			destinationX = walkingQueueX[0];
			destinationY = walkingQueueY[0];
			for (int counter = 1; counter < maxPathSize; counter++) {
				currentIndex--;
				stream.put(walkingQueueX[currentIndex] - x);
				stream.put(walkingQueueY[currentIndex] - y);
			}

			stream.putLEShort(y + baseY);
			stream.putByteC(super.keyStatus[5] != 1 ? 0 : 1);
			return true;
		}
		return clickType != 1;
	}

	private void draw3dScreen() {
		drawSplitPrivateChat();
		if (crossType == 1) {
			crosses[crossIndex / 100].drawImage(crossX - 8 - 4, crossY - 8 - 4);
		}
		if (crossType == 2)
			crosses[4 + crossIndex / 100].drawImage(crossX - 8 - 4, crossY - 8 - 4);
		if (walkableInterfaceId != -1) {
			animateInterface(animationTimePassed, walkableInterfaceId);
			drawInterface(0, 0, RSInterface.cache[walkableInterfaceId], 0);
		}
		if (openInterfaceId != -1) {
			animateInterface(animationTimePassed, openInterfaceId);
			drawInterface(0, 0, RSInterface.cache[openInterfaceId], 0);
		}
		checkTutorialIsland();
		if (!menuOpen) {
			processRightClick();
			drawTooltip();
		} else if (menuScreenArea == 0)
			drawMenu();
		if (multiCombatZone)
			headIcons[1].drawImage(472, 296);
		if (displayFpsAndMemory) {
			int x = 507;
			int y = 20;
			int colour = 0xffff00;
			if (super.fps < 15)
				colour = 0xff0000;
			fontPlain.drawTextLeft("Fps:" + super.fps, x, y, colour);
			y += 15;
			Runtime runtime = Runtime.getRuntime();
			int memory = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			colour = 0xffff00;
			if (memory > 0x2000000 && lowMemory)
				colour = 0xff0000;
			fontPlain.drawTextLeft("Mem:" + memory + "k", x, y, 0xffff00);
			y += 15;
		}
		if (systemUpdateTime != 0) {
			int seconds = systemUpdateTime / 50;
			int minutes = seconds / 60;
			seconds %= 60;
			if (seconds < 10)
				fontPlain.drawText("System update in: " + minutes + ":0" + seconds, 4, 329, 0xffff00);
			else
				fontPlain.drawText("System update in: " + minutes + ":" + seconds, 4, 329, 0xffff00);
		}
	}

	private void drawChatArea() {
		chatboxImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = chatboxLineOffsets;
		chatBackgroundImage.drawImage(0, 0);
		if (messagePromptRaised) {
			fontBold.drawCentredText(chatboxInputNeededString, 239, 40, 0);
			fontBold.drawCentredText(promptInput + "*", 239, 60, 128);
		} else if (inputDialogState == 1) {
			fontBold.drawCentredText("Enter amount:", 239, 40, 0);
			fontBold.drawCentredText(amountOrNameInput + "*", 239, 60, 128);
		} else if (inputDialogState == 2) {
			fontBold.drawCentredText("Enter name:", 239, 40, 0);
			fontBold.drawCentredText(amountOrNameInput + "*", 239, 60, 128);
		} else if (clickToContinueString != null) {
			fontBold.drawCentredText(clickToContinueString, 239, 40, 0);
			fontBold.drawCentredText("Click to continue", 239, 60, 128);
		} else if (chatboxInterfaceId != -1)
			drawInterface(0, 0, RSInterface.cache[chatboxInterfaceId], 0);
		else if (dialogID != -1) {
			drawInterface(0, 0, RSInterface.cache[dialogID], 0);
		} else {
			GameFont textDrawingArea = fontPlain;
			int rowCount = 0;
			DrawingArea.setDrawingArea(77, 0, 463, 0);
			for (int m = 0; m < 100; m++)
				if (chatMessages[m] != null) {
					int type = chatTypes[m];
					int y = (70 - rowCount * 14) + anInt1089;
					String name = chatNames[m];
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
						if (y > 0 && y < 110)
							textDrawingArea.drawText(chatMessages[m], 4, y, 0);
						rowCount++;
					}
					if ((type == 1 || type == 2)
							&& (type == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(name))) {
						if (y > 0 && y < 110) {
							int x = 4;
							if (playerRights == 1) {
								modIcons[0].drawImage(x, y - 12);
								x += 14;
							}
							if (playerRights == 2) {
								modIcons[1].drawImage(x, y - 12);
								x += 14;
							}
							textDrawingArea.drawText(name + ":", x, y, 0);
							x += textDrawingArea.getTextDisplayedWidth(name) + 8;
							textDrawingArea.drawText(chatMessages[m], x, y, 255);
						}
						rowCount++;
					}
					if ((type == 3 || type == 7) && splitPrivateChat == 0
							&& (type == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
						if (y > 0 && y < 110) {
							int x = 4;
							textDrawingArea.drawText("From", x, y, 0);
							x += textDrawingArea.getTextDisplayedWidth("From ");
							if (playerRights == 1) {
								modIcons[0].drawImage(x, y - 12);
								x += 14;
							}
							if (playerRights == 2) {
								modIcons[1].drawImage(x, y - 12);
								x += 14;
							}
							textDrawingArea.drawText(name + ":", x, y, 0);
							x += textDrawingArea.getTextDisplayedWidth(name) + 8;
							textDrawingArea.drawText(chatMessages[m], x, y, 0x800000);
						}
						rowCount++;
					}
					if (type == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
						if (y > 0 && y < 110)
							textDrawingArea.drawText(name + " " + chatMessages[m], 4, y, 0x800080);
						rowCount++;
					}
					if (type == 5 && splitPrivateChat == 0 && privateChatMode < 2) {
						if (y > 0 && y < 110)
							textDrawingArea.drawText(chatMessages[m], 4, y, 0x800000);
						rowCount++;
					}
					if (type == 6 && splitPrivateChat == 0 && privateChatMode < 2) {
						if (y > 0 && y < 110) {
							textDrawingArea.drawText("To " + name + ":", 4, y, 0);
							textDrawingArea.drawText(chatMessages[m],
									12 + textDrawingArea.getTextDisplayedWidth("To " + name), y, 0x800000);
						}
						rowCount++;
					}
					if (type == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
						if (y > 0 && y < 110)
							textDrawingArea.drawText(name + " " + chatMessages[m], 4, y, 0x7e3200);
						rowCount++;
					}
				}

			DrawingArea.defaultDrawingAreaSize();
			chatboxScrollMax = rowCount * 14 + 7;
			if (chatboxScrollMax < 78)
				chatboxScrollMax = 78;
			renderChatInterface(463, 0, 77, chatboxScrollMax - anInt1089 - 77, chatboxScrollMax);
			String name;
			if (localPlayer != null && localPlayer.name != null)
				name = localPlayer.name;
			else
				name = TextClass.formatName(enteredUsername);
			textDrawingArea.drawText(name + ":", 4, 90, 0);
			textDrawingArea.drawText(inputString + "*", 6 + textDrawingArea.getTextDisplayedWidth(name + ": "), 90,
					255);
			DrawingArea.drawHorizontalLine(77, 0, 479, 0);
		}
		if (menuOpen && menuScreenArea == 2)
			drawMenu();
		chatboxImageProducer.drawGraphics(357, super.gameGraphics, 17);
		gameScreenImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = viewportOffsets;
	}

	private void drawFlames() {
		drawingFlames = true;
		try {
			long startTime = System.currentTimeMillis();
			int currentLoop = 0;
			int interval = 20;
			while (currentlyDrawingFlames) {
				flameCycle++;
				calcFlamesPosition();
				calcFlamesPosition();
				doFlamesDrawing();
				if (++currentLoop > 10) {
					long currentTime = System.currentTimeMillis();
					int difference = (int) (currentTime - startTime) / 10 - interval;
					interval = 40 - difference;
					if (interval < 5)
						interval = 5;
					currentLoop = 0;
					startTime = currentTime;
				}
				try {
					Thread.sleep(interval);
				} catch (Exception _ex) {
				}
			}
		} catch (Exception _ex) {
		}
		drawingFlames = false;
	}

	private void drawFriendsListOrWelcomeScreen(RSInterface rsInterface) {
		int type = rsInterface.contentType;
		if (type >= 1 && type <= 100 || type >= 701 && type <= 800) {
			if (type == 1 && friendListStatus == 0) {
				rsInterface.textDefault = "Loading friend list";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 1 && friendListStatus == 1) {
				rsInterface.textDefault = "Connecting to friendserver";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 2 && friendListStatus != 2) {
				rsInterface.textDefault = "Please wait...";
				rsInterface.actionType = 0;
				return;
			}
			int f = friendsCount;
			if (friendListStatus != 2)
				f = 0;
			if (type > 700)
				type -= 601;
			else
				type--;
			if (type >= f) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			} else {
				rsInterface.textDefault = friendsList[type];
				rsInterface.actionType = 1;
				return;
			}
		}
		if (type >= 101 && type <= 200 || type >= 801 && type <= 900) {
			int f = friendsCount;
			if (friendListStatus != 2)
				f = 0;
			if (type > 800)
				type -= 701;
			else
				type -= 101;
			if (type >= f) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			}
			if (friendsWorldIds[type] == 0)
				rsInterface.textDefault = "@red@Offline";
			else if (friendsWorldIds[type] == localWorldId)
				rsInterface.textDefault = "@gre@World-" + (friendsWorldIds[type] - 9);
			else
				rsInterface.textDefault = "@yel@World-" + (friendsWorldIds[type] - 9);
			rsInterface.actionType = 1;
			return;
		}
		if (type == 203) {
			int f = friendsCount;
			if (friendListStatus != 2)
				f = 0;
			rsInterface.scrollMax = f * 15 + 20;
			if (rsInterface.scrollMax <= rsInterface.height)
				rsInterface.scrollMax = rsInterface.height + 1;
			return;
		}
		if (type >= 401 && type <= 500) {
			if ((type -= 401) == 0 && friendListStatus == 0) {
				rsInterface.textDefault = "Loading ignore list";
				rsInterface.actionType = 0;
				return;
			}
			if (type == 1 && friendListStatus == 0) {
				rsInterface.textDefault = "Please wait...";
				rsInterface.actionType = 0;
				return;
			}
			int i = ignoreCount;
			if (friendListStatus == 0)
				i = 0;
			if (type >= i) {
				rsInterface.textDefault = "";
				rsInterface.actionType = 0;
				return;
			} else {
				rsInterface.textDefault = TextClass.formatName(TextClass.longToName(ignoreListAsLongs[type]));
				rsInterface.actionType = 1;
				return;
			}
		}
		if (type == 503) {
			rsInterface.scrollMax = ignoreCount * 15 + 20;
			if (rsInterface.scrollMax <= rsInterface.height)
				rsInterface.scrollMax = rsInterface.height + 1;
			return;
		}
		if (type == 327) {
			rsInterface.modelRotationX = 150;
			rsInterface.modelRotationY = (int) (Math.sin(tick / 40D) * 256D) & 0x7ff;
			if (characterModelChanged) {
				for (int k = 0; k < 7; k++) {
					int kit = characterEditIdentityKits[k];
					if (kit >= 0 && !IdentityKit.cache[kit].bodyModelCached())
						return;
				}

				characterModelChanged = false;
				Model bodyModels[] = new Model[7];
				int bodyModelCount = 0;
				for (int k = 0; k < 7; k++) {
					int kit = characterEditIdentityKits[k];
					if (kit >= 0)
						bodyModels[bodyModelCount++] = IdentityKit.cache[kit].getBodyModel();
				}

				Model model = new Model(bodyModelCount, bodyModels);
				for (int colour = 0; colour < 5; colour++)
					if (characterEditColours[colour] != 0) {
						model.recolour(APPEARANCE_COLOURS[colour][0],
								APPEARANCE_COLOURS[colour][characterEditColours[colour]]);
						if (colour == 1)
							model.recolour(BEARD_COLOURS[0], BEARD_COLOURS[characterEditColours[colour]]);
					}

				model.createBones();
				model.applyTransformation(AnimationSequence.animations[localPlayer.standAnimationId].frame2Ids[0]);
				model.applyLighting(64, 850, -30, -50, -30, true);
				rsInterface.modelTypeDefault = 5;
				rsInterface.modelIdDefault = 0;
				RSInterface.setModel(model);
			}
			return;
		}
		if (type == 324) {
			if (characterEditButtonDefualt == null) {
				characterEditButtonDefualt = rsInterface.spriteDefault;
				characterEditButtonActive = rsInterface.spriteActive;
			}
			if (characterEditChangeGender) {
				rsInterface.spriteDefault = characterEditButtonActive;
				return;
			} else {
				rsInterface.spriteDefault = characterEditButtonDefualt;
				return;
			}
		}
		if (type == 325) {
			if (characterEditButtonDefualt == null) {
				characterEditButtonDefualt = rsInterface.spriteDefault;
				characterEditButtonActive = rsInterface.spriteActive;
			}
			if (characterEditChangeGender) {
				rsInterface.spriteDefault = characterEditButtonDefualt;
				return;
			} else {
				rsInterface.spriteDefault = characterEditButtonActive;
				return;
			}
		}
		if (type == 600) {
			rsInterface.textDefault = reportAbuseInput;
			if (tick % 20 < 10) {
				rsInterface.textDefault += "|";
				return;
			} else {
				rsInterface.textDefault += " ";
				return;
			}
		}
		if (type == 613)
			if (playerRights >= 1) {
				if (reportAbuseMute) {
					rsInterface.colourDefault = 0xff0000;
					rsInterface.textDefault = "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					rsInterface.colourDefault = 0xffffff;
					rsInterface.textDefault = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				rsInterface.textDefault = "";
			}
		if (type == 650 || type == 655)
			if (lastAddress != 0) {
				String s;
				if (daysSinceLogin == 0)
					s = "earlier today";
				else if (daysSinceLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLogin + " days ago";
				rsInterface.textDefault = "You last logged in " + s + " from: " + signlink.dns;
			} else {
				rsInterface.textDefault = "";
			}
		if (type == 651) {
			if (unreadMessages == 0) {
				rsInterface.textDefault = "0 unread messages";
				rsInterface.colourDefault = 0xffff00;
			}
			if (unreadMessages == 1) {
				rsInterface.textDefault = "1 unread message";
				rsInterface.colourDefault = 65280;
			}
			if (unreadMessages > 1) {
				rsInterface.textDefault = unreadMessages + " unread messages";
				rsInterface.colourDefault = 65280;
			}
		}
		if (type == 652)
			if (daysSinceRecoveryChange == 201) {
				if (membership == 1)
					rsInterface.textDefault = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					rsInterface.textDefault = "";
			} else if (daysSinceRecoveryChange == 200) {
				rsInterface.textDefault = "You have not yet set any password recovery questions.";
			} else {
				String time;
				if (daysSinceRecoveryChange == 0)
					time = "Earlier today";
				else if (daysSinceRecoveryChange == 1)
					time = "Yesterday";
				else
					time = daysSinceRecoveryChange + " days ago";
				rsInterface.textDefault = time + " you changed your recovery questions";
			}
		if (type == 653)
			if (daysSinceRecoveryChange == 201) {
				if (membership == 1)
					rsInterface.textDefault = "@whi@recommend you use a members world instead. You may use";
				else
					rsInterface.textDefault = "";
			} else if (daysSinceRecoveryChange == 200)
				rsInterface.textDefault = "We strongly recommend you do so now to secure your account.";
			else
				rsInterface.textDefault = "If you do not remember making this change then cancel it immediately";
		if (type == 654) {
			if (daysSinceRecoveryChange == 201)
				if (membership == 1) {
					rsInterface.textDefault = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					rsInterface.textDefault = "";
					return;
				}
			if (daysSinceRecoveryChange == 200) {
				rsInterface.textDefault = "Do this from the 'account management' area on our front webpage";
				return;
			}
			rsInterface.textDefault = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void drawGameScreen() {
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			backLeftIP1.drawGraphics(4, super.gameGraphics, 0);
			backLeftIP2.drawGraphics(357, super.gameGraphics, 0);
			backRightIP1.drawGraphics(4, super.gameGraphics, 722);
			backRightIP2.drawGraphics(205, super.gameGraphics, 743);
			backTopIP1.drawGraphics(0, super.gameGraphics, 0);
			backVmidIP1.drawGraphics(4, super.gameGraphics, 516);
			backVmidIP2.drawGraphics(205, super.gameGraphics, 516);
			backVmidIP3.drawGraphics(357, super.gameGraphics, 496);
			backVmidIP2_2.drawGraphics(338, super.gameGraphics, 0);
			redrawTab = true;
			redrawChatbox = true;
			drawTabIcons = true;
			updateChatSettings = true;
			if (loadingStage != 2) {
				gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
				minimapImageProducer.drawGraphics(4, super.gameGraphics, 550);
			}
		}
		if (loadingStage == 2)
			renderGameView();
		if (menuOpen && menuScreenArea == 1)
			redrawTab = true;
		if (inventoryOverlayInterfaceID != -1) {
			boolean flag1 = animateInterface(animationTimePassed, inventoryOverlayInterfaceID);
			if (flag1)
				redrawTab = true;
		}
		if (atInventoryInterfaceType == 2)
			redrawTab = true;
		if (activeInterfaceType == 2)
			redrawTab = true;
		if (redrawTab) {
			drawTabArea();
			redrawTab = false;
		}
		if (chatboxInterfaceId == -1) {
			chatboxInterface.scrollPosition = chatboxScrollMax - anInt1089 - 77;
			if (super.mouseX > 448 && super.mouseX < 560 && super.mouseY > 332)
				scrollInterface(463, 77, super.mouseX - 17, super.mouseY - 357, chatboxInterface, 0, false,
						chatboxScrollMax);
			int i = chatboxScrollMax - 77 - chatboxInterface.scrollPosition;
			if (i < 0)
				i = 0;
			if (i > chatboxScrollMax - 77)
				i = chatboxScrollMax - 77;
			if (anInt1089 != i) {
				anInt1089 = i;
				redrawChatbox = true;
			}
		}
		if (chatboxInterfaceId != -1) {
			boolean flag2 = animateInterface(animationTimePassed, chatboxInterfaceId);
			if (flag2)
				redrawChatbox = true;
		}
		if (atInventoryInterfaceType == 3)
			redrawChatbox = true;
		if (activeInterfaceType == 3)
			redrawChatbox = true;
		if (clickToContinueString != null)
			redrawChatbox = true;
		if (menuOpen && menuScreenArea == 2)
			redrawChatbox = true;
		if (redrawChatbox) {
			drawChatArea();
			redrawChatbox = false;
		}
		if (loadingStage == 2) {
			drawMinimap();
			minimapImageProducer.drawGraphics(4, super.gameGraphics, 550);
		}
		if (flashingSidebar != -1)
			drawTabIcons = true;
		if (drawTabIcons) {
			if (flashingSidebar != -1 && flashingSidebar == currentTabId) {
				flashingSidebar = -1;
				stream.putOpcode(120);
				stream.put(currentTabId);
			}
			drawTabIcons = false;
			topSideIconImageProducer.initDrawingArea();
			backHmid1Image.drawImage(0, 0);
			if (inventoryOverlayInterfaceID == -1) {
				if (tabInterfaceIDs[currentTabId] != -1) {
					if (currentTabId == 0)
						redStone1.drawImage(22, 10);
					if (currentTabId == 1)
						redStone2.drawImage(54, 8);
					if (currentTabId == 2)
						redStone2.drawImage(82, 8);
					if (currentTabId == 3)
						redStone3.drawImage(110, 8);
					if (currentTabId == 4)
						redStone2_2.drawImage(153, 8);
					if (currentTabId == 5)
						redStone2_2.drawImage(181, 8);
					if (currentTabId == 6)
						redStone1_2.drawImage(209, 9);
				}
				if (tabInterfaceIDs[0] != -1 && (flashingSidebar != 0 || tick % 20 < 10))
					sideIconImage[0].drawImage(29, 13);
				if (tabInterfaceIDs[1] != -1 && (flashingSidebar != 1 || tick % 20 < 10))
					sideIconImage[1].drawImage(53, 11);
				if (tabInterfaceIDs[2] != -1 && (flashingSidebar != 2 || tick % 20 < 10))
					sideIconImage[2].drawImage(82, 11);
				if (tabInterfaceIDs[3] != -1 && (flashingSidebar != 3 || tick % 20 < 10))
					sideIconImage[3].drawImage(115, 12);
				if (tabInterfaceIDs[4] != -1 && (flashingSidebar != 4 || tick % 20 < 10))
					sideIconImage[4].drawImage(153, 13);
				if (tabInterfaceIDs[5] != -1 && (flashingSidebar != 5 || tick % 20 < 10))
					sideIconImage[5].drawImage(180, 11);
				if (tabInterfaceIDs[6] != -1 && (flashingSidebar != 6 || tick % 20 < 10))
					sideIconImage[6].drawImage(208, 13);
			}
			topSideIconImageProducer.drawGraphics(160, super.gameGraphics, 516);
			bottomSideIconImageProducer.initDrawingArea();
			backBase2Image.drawImage(0, 0);
			if (inventoryOverlayInterfaceID == -1) {
				if (tabInterfaceIDs[currentTabId] != -1) {
					if (currentTabId == 7)
						redStone1_3.drawImage(42, 0);
					if (currentTabId == 8)
						redStone2_3.drawImage(74, 0);
					if (currentTabId == 9)
						redStone2_3.drawImage(102, 0);
					if (currentTabId == 10)
						redStone3_2.drawImage(130, 1);
					if (currentTabId == 11)
						redStone2_4.drawImage(173, 0);
					if (currentTabId == 12)
						redStone2_4.drawImage(201, 0);
					if (currentTabId == 13)
						redStone1_4.drawImage(229, 0);
				}
				if (tabInterfaceIDs[8] != -1 && (flashingSidebar != 8 || tick % 20 < 10))
					sideIconImage[7].drawImage(74, 2);
				if (tabInterfaceIDs[9] != -1 && (flashingSidebar != 9 || tick % 20 < 10))
					sideIconImage[8].drawImage(102, 3);
				if (tabInterfaceIDs[10] != -1 && (flashingSidebar != 10 || tick % 20 < 10))
					sideIconImage[9].drawImage(137, 4);
				if (tabInterfaceIDs[11] != -1 && (flashingSidebar != 11 || tick % 20 < 10))
					sideIconImage[10].drawImage(174, 2);
				if (tabInterfaceIDs[12] != -1 && (flashingSidebar != 12 || tick % 20 < 10))
					sideIconImage[11].drawImage(201, 2);
				if (tabInterfaceIDs[13] != -1 && (flashingSidebar != 13 || tick % 20 < 10))
					sideIconImage[12].drawImage(226, 2);
			}
			bottomSideIconImageProducer.drawGraphics(466, super.gameGraphics, 496);
			gameScreenImageProducer.initDrawingArea();
		}
		if (updateChatSettings) {
			updateChatSettings = false;
			chatSettingImageProducer.initDrawingArea();
			backBase1Image.drawImage(0, 0);
			fontPlain.drawCentredTextWithPotentialShadow("Public chat", 55, 28, 0xffffff, true);
			if (publicChatMode == 0)
				fontPlain.drawCentredTextWithPotentialShadow("On", 55, 41, 65280, true);
			if (publicChatMode == 1)
				fontPlain.drawCentredTextWithPotentialShadow("Friends", 55, 41, 0xffff00, true);
			if (publicChatMode == 2)
				fontPlain.drawCentredTextWithPotentialShadow("Off", 55, 41, 0xff0000, true);
			if (publicChatMode == 3)
				fontPlain.drawCentredTextWithPotentialShadow("Hide", 55, 41, 65535, true);
			fontPlain.drawCentredTextWithPotentialShadow("Private chat", 184, 28, 0xffffff, true);
			if (privateChatMode == 0)
				fontPlain.drawCentredTextWithPotentialShadow("On", 184, 41, 65280, true);
			if (privateChatMode == 1)
				fontPlain.drawCentredTextWithPotentialShadow("Friends", 184, 41, 0xffff00, true);
			if (privateChatMode == 2)
				fontPlain.drawCentredTextWithPotentialShadow("Off", 184, 41, 0xff0000, true);
			fontPlain.drawCentredTextWithPotentialShadow("Trade/compete", 324, 28, 0xffffff, true);
			if (tradeMode == 0)
				fontPlain.drawCentredTextWithPotentialShadow("On", 324, 41, 65280, true);
			if (tradeMode == 1)
				fontPlain.drawCentredTextWithPotentialShadow("Friends", 324, 41, 0xffff00, true);
			if (tradeMode == 2)
				fontPlain.drawCentredTextWithPotentialShadow("Off", 324, 41, 0xff0000, true);
			fontPlain.drawCentredTextWithPotentialShadow("Report abuse", 458, 33, 0xffffff, true);
			chatSettingImageProducer.drawGraphics(453, super.gameGraphics, 0);
			gameScreenImageProducer.initDrawingArea();
		}
		animationTimePassed = 0;
	}

	private void drawHeadIcon() {
		if (hintIconType != 2)
			return;
		calculateScreenPosition((hintIconX - baseX << 7) + hintIconDrawTileX, hintIconDrawHeight * 2,
				(hintIconY - baseY << 7) + hintIconDrawTileY);
		if (spriteDrawX > -1 && tick % 20 < 10)
			headIcons[2].drawImage(spriteDrawX - 12, spriteDrawY - 28);
	}

	private void drawInterface(int j, int x, RSInterface rsInterface, int y) {
		if (rsInterface.type != 0 || rsInterface.children == null)
			return;
		if (rsInterface.hoverOnly && anInt1026 != rsInterface.id && anInt1048 != rsInterface.id
				&& anInt1039 != rsInterface.id)
			return;
		int clipLeft = DrawingArea.topX;
		int clipTop = DrawingArea.topY;
		int clipRight = DrawingArea.bottomX;
		int clipBottom = DrawingArea.bottomY;
		DrawingArea.setDrawingArea(y + rsInterface.height, x, x + rsInterface.width, y);
		int childCount = rsInterface.children.length;
		for (int childId = 0; childId < childCount; childId++) {
			int _x = rsInterface.childX[childId] + x;
			int _y = (rsInterface.childY[childId] + y) - j;
			RSInterface childInterface = RSInterface.cache[rsInterface.children[childId]];
			_x += childInterface.x;
			_y += childInterface.y;
			if (childInterface.contentType > 0)
				drawFriendsListOrWelcomeScreen(childInterface);
			if (childInterface.type == 0) {
				if (childInterface.scrollPosition > childInterface.scrollMax - childInterface.height)
					childInterface.scrollPosition = childInterface.scrollMax - childInterface.height;
				if (childInterface.scrollPosition < 0)
					childInterface.scrollPosition = 0;
				drawInterface(childInterface.scrollPosition, _x, childInterface, _y);
				if (childInterface.scrollMax > childInterface.height)
					renderChatInterface(_x + childInterface.width, _y, childInterface.height,
							childInterface.scrollPosition, childInterface.scrollMax);
			} else if (childInterface.type != 1)
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
								int itemId = childInterface.inventoryItemId[item] - 1;
								if (tileX > DrawingArea.topX - 32 && tileX < DrawingArea.bottomX
										&& tileY > DrawingArea.topY - 32 && tileY < DrawingArea.bottomY
										|| activeInterfaceType != 0 && moveItemSlotStart == item) {
									int outlineColour = 0;
									if (itemSelected && lastItemSelectedSlot == item
											&& lastItemSelectedInterface == childInterface.id)
										outlineColour = 0xffffff;
									Sprite sprite = ItemDefinition.getSprite(itemId,
											childInterface.inventoryStackSize[item], outlineColour);
									if (sprite != null) {
										if (activeInterfaceType != 0 && moveItemSlotStart == item
												&& moveItemInterfaceId == childInterface.id) {
											differenceX = super.mouseX - lastMouseX;
											differenceY = super.mouseY - lastMouseY;
											if (differenceX < 5 && differenceX > -5)
												differenceX = 0;
											if (differenceY < 5 && differenceY > -5)
												differenceY = 0;
											if (lastItemDragTime < 5) {
												differenceX = 0;
												differenceY = 0;
											}
											sprite.drawImageAlpha(tileX + differenceX, tileY + differenceY);
											if (tileY + differenceY < DrawingArea.topY
													&& rsInterface.scrollPosition > 0) {
												int difference = (animationTimePassed
														* (DrawingArea.topY - tileY - differenceY)) / 3;
												if (difference > animationTimePassed * 10)
													difference = animationTimePassed * 10;
												if (difference > rsInterface.scrollPosition)
													difference = rsInterface.scrollPosition;
												rsInterface.scrollPosition -= difference;
												lastMouseY += difference;
											}
											if (tileY + differenceY + 32 > DrawingArea.bottomY
													&& rsInterface.scrollPosition < rsInterface.scrollMax
															- rsInterface.height) {
												int difference = (animationTimePassed
														* ((tileY + differenceY + 32) - DrawingArea.bottomY)) / 3;
												if (difference > animationTimePassed * 10)
													difference = animationTimePassed * 10;
												if (difference > rsInterface.scrollMax - rsInterface.height
														- rsInterface.scrollPosition)
													difference = rsInterface.scrollMax - rsInterface.height
															- rsInterface.scrollPosition;
												rsInterface.scrollPosition += difference;
												lastMouseY -= difference;
											}
										} else if (atInventoryInterfaceType != 0 && atInventoryIndex == item
												&& atInventoryInterface == childInterface.id)
											sprite.drawImageAlpha(tileX, tileY);
										else
											sprite.drawImage(tileX, tileY);
										if (sprite.maxWidth == 33 || childInterface.inventoryStackSize[item] != 1) {
											int stackSize = childInterface.inventoryStackSize[item];
											fontSmall.drawText(getAmountString(stackSize), tileX + 1 + differenceX,
													tileY + 10 + differenceY, 0);
											fontSmall.drawText(getAmountString(stackSize), tileX + differenceX,
													tileY + 9 + differenceY, 0xffff00);
										}
									}
								}
							} else if (childInterface.sprites != null && item < 20) {
								Sprite sprite = childInterface.sprites[item];
								if (sprite != null)
									sprite.drawImage(tileX, tileY);
							}
							item++;
						}

					}

				} else if (childInterface.type == 3) {
					boolean hover = false;
					if (anInt1039 == childInterface.id || anInt1048 == childInterface.id
							|| anInt1026 == childInterface.id)
						hover = true;
					int colour;
					if (interfaceIsActive(childInterface)) {
						colour = childInterface.colourActive;
						if (hover && childInterface.colourActiveHover != 0)
							colour = childInterface.colourActiveHover;
					} else {
						colour = childInterface.colourDefault;
						if (hover && childInterface.colourDefaultHover != 0)
							colour = childInterface.colourDefaultHover;
					}
					if (childInterface.alpha == 0) {
						if (childInterface.filled)
							DrawingArea.drawFilledRectangle(_x, _y, childInterface.width, childInterface.height,
									colour);
						else
							DrawingArea.drawUnfilledRectangle(_x, childInterface.width, childInterface.height, colour,
									_y);
					} else if (childInterface.filled)
						DrawingArea.drawFilledRectangleAlpha(colour, _y, childInterface.width, childInterface.height,
								256 - (childInterface.alpha & 0xff), _x);
					else
						DrawingArea.drawUnfilledRectangleAlpha(_y, childInterface.height,
								256 - (childInterface.alpha & 0xff), colour, childInterface.width, _x);
				} else if (childInterface.type == 4) {
					GameFont textDrawingArea = childInterface.textDrawingAreas;
					String text = childInterface.textDefault;
					boolean hover = false;
					if (anInt1039 == childInterface.id || anInt1048 == childInterface.id
							|| anInt1026 == childInterface.id)
						hover = true;
					int colour;
					if (interfaceIsActive(childInterface)) {
						colour = childInterface.colourActive;
						if (hover && childInterface.colourActiveHover != 0)
							colour = childInterface.colourActiveHover;
						if (childInterface.textActive.length() > 0)
							text = childInterface.textActive;
					} else {
						colour = childInterface.colourDefault;
						if (hover && childInterface.colourDefaultHover != 0)
							colour = childInterface.colourDefaultHover;
					}
					if (childInterface.actionType == 6 && continuedDialogue) {
						text = "Please wait...";
						colour = childInterface.colourDefault;
					}
					if (DrawingArea.width == 479) {
						if (colour == 0xffff00)
							colour = 255;
						if (colour == 49152)
							colour = 0xffffff;
					}
					for (int __y = _y + textDrawingArea.fontHeight; text
							.length() > 0; __y += textDrawingArea.fontHeight) {
						if (text.indexOf("%") != -1) {
							do {
								int placeholder = text.indexOf("%1");
								if (placeholder == -1)
									break;
								text = text.substring(0, placeholder)
										+ interfaceIntToString(parseInterfaceOpcode(childInterface, 0))
										+ text.substring(placeholder + 2);
							} while (true);
							do {
								int placeholder = text.indexOf("%2");
								if (placeholder == -1)
									break;
								text = text.substring(0, placeholder)
										+ interfaceIntToString(parseInterfaceOpcode(childInterface, 1))
										+ text.substring(placeholder + 2);
							} while (true);
							do {
								int placeholder = text.indexOf("%3");
								if (placeholder == -1)
									break;
								text = text.substring(0, placeholder)
										+ interfaceIntToString(parseInterfaceOpcode(childInterface, 2))
										+ text.substring(placeholder + 2);
							} while (true);
							do {
								int placeholder = text.indexOf("%4");
								if (placeholder == -1)
									break;
								text = text.substring(0, placeholder)
										+ interfaceIntToString(parseInterfaceOpcode(childInterface, 3))
										+ text.substring(placeholder + 2);
							} while (true);
							do {
								int placeholder = text.indexOf("%5");
								if (placeholder == -1)
									break;
								text = text.substring(0, placeholder)
										+ interfaceIntToString(parseInterfaceOpcode(childInterface, 4))
										+ text.substring(placeholder + 2);
							} while (true);
						}
						int newLine = text.indexOf("\\n");
						String text2;
						if (newLine != -1) {
							text2 = text.substring(0, newLine);
							text = text.substring(newLine + 2);
						} else {
							text2 = text;
							text = "";
						}
						if (childInterface.textCentred)
							textDrawingArea.drawCentredTextWithPotentialShadow(text2, _x + childInterface.width / 2,
									__y, colour, childInterface.textShadowed);
						else
							textDrawingArea.drawTextWithPotentialShadow(text2, _x, __y, colour,
									childInterface.textShadowed);
					}

				} else if (childInterface.type == 5) {
					Sprite sprite = interfaceIsActive(childInterface) ? childInterface.spriteActive
							: childInterface.spriteDefault;

					if (sprite != null)
						sprite.drawImage(_x, _y);
				} else if (childInterface.type == 6) {
					int centreX = Rasterizer.centreX;
					int centreY = Rasterizer.centreY;
					Rasterizer.centreX = _x + childInterface.width / 2;
					Rasterizer.centreY = _y + childInterface.height / 2;
					int sine = Rasterizer.SINE[childInterface.modelRotationX] * childInterface.modelZoom >> 16;
					int cosine = Rasterizer.COSINE[childInterface.modelRotationX] * childInterface.modelZoom >> 16;
					boolean active = interfaceIsActive(childInterface);
					int anim = active ? childInterface.animationIdActive : childInterface.animationIdDefault;

					Model model;
					if (anim == -1) {
						model = childInterface.getAnimatedModel(-1, -1, active);
					} else {
						AnimationSequence animation = AnimationSequence.animations[anim];
						model = childInterface.getAnimatedModel(animation.frame1Ids[childInterface.animationFrame],
								animation.frame2Ids[childInterface.animationFrame], active);
					}
					if (model != null)
						model.renderSingle(childInterface.modelRotationY, 0, childInterface.modelRotationX, 0, sine,
								cosine);
					Rasterizer.centreX = centreX;
					Rasterizer.centreY = centreY;
				} else if (childInterface.type == 7) {
					GameFont font = childInterface.textDrawingAreas;
					int slot = 0;
					for (int row = 0; row < childInterface.height; row++) {
						for (int column = 0; column < childInterface.width; column++) {
							if (childInterface.inventoryItemId[slot] > 0) {
								ItemDefinition item = ItemDefinition
										.getDefinition(childInterface.inventoryItemId[slot] - 1);
								String name = item.name;
								if (item.stackable || childInterface.inventoryStackSize[slot] != 1)
									name = name + " x" + formatAmount(childInterface.inventoryStackSize[slot]);
								int __x = _x + column * (115 + childInterface.inventorySpritePaddingColumn);
								int __y = _y + row * (12 + childInterface.inventorySpritePaddingRow);
								if (childInterface.textCentred)
									font.drawCentredTextWithPotentialShadow(name, __x + childInterface.width / 2, __y,
											childInterface.colourDefault, childInterface.textShadowed);
								else
									font.drawTextWithPotentialShadow(name, __x, __y, childInterface.colourDefault,
											childInterface.textShadowed);
							}
							slot++;
						}

					}
				}
		}

		DrawingArea.setDrawingArea(clipBottom, clipLeft, clipRight, clipTop);
	}

	@Override
	void drawLoadingText(int percentage, String text) {
		loadingBarPercentage = percentage;
		loadingBarText = text;
		setupLoginScreen();
		if (archiveTitle == null) {
			super.drawLoadingText(percentage, text);
			return;
		}
		loginBoxLeftBackgroundTile.initDrawingArea();

		int horizontalOffset = 360;
		int verticalOffset1 = 200;
		int verticalOffset2 = 20;
		fontBold.drawCentredText("RuneScape is loading - please wait...", horizontalOffset / 2,
				verticalOffset1 / 2 - 26 - verticalOffset2, 0xffffff);
		int loadingBarHeight = verticalOffset1 / 2 - 18 - verticalOffset2;

		DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 152, 304, 34, 0x8c1111, loadingBarHeight);
		DrawingArea.drawUnfilledRectangle(horizontalOffset / 2 - 151, 302, 32, 0, loadingBarHeight + 1);
		DrawingArea.drawFilledRectangle(horizontalOffset / 2 - 150, loadingBarHeight + 2, percentage * 3, 30, 0x8c1111);
		DrawingArea.drawFilledRectangle((horizontalOffset / 2 - 150) + percentage * 3, loadingBarHeight + 2,
				300 - percentage * 3, 30, 0);
		fontBold.drawCentredText(text, horizontalOffset / 2, (verticalOffset1 / 2 + 5) - verticalOffset2, 0xffffff);
		loginBoxLeftBackgroundTile.drawGraphics(171, super.gameGraphics, 202);
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			if (!currentlyDrawingFlames) {
				flameLeftBackground.drawGraphics(0, super.gameGraphics, 0);
				flameRightBackground.drawGraphics(0, super.gameGraphics, 637);
			}
			topLeft1BackgroundTile.drawGraphics(0, super.gameGraphics, 128);
			bottomLeft1BackgroundTile.drawGraphics(371, super.gameGraphics, 202);
			bottomLeft0BackgroundTile.drawGraphics(265, super.gameGraphics, 0);
			aRSImageProducer_1113.drawGraphics(265, super.gameGraphics, 562);
			middleLeft1BackgroundTile.drawGraphics(171, super.gameGraphics, 128);
			aRSImageProducer_1115.drawGraphics(171, super.gameGraphics, 562);
		}
	}

	private void drawLoginScreen(boolean originalLoginScreen) {
		setupLoginScreen();
		loginBoxLeftBackgroundTile.initDrawingArea();
		titleBoxImage.drawImage(0, 0);
		int x = 360;
		int y = 200;
		if (loginScreenState == 0) {
			int _y = y / 2 + 80;
			fontSmall.drawCentredTextWithPotentialShadow(onDemandFetcher.statusString, x / 2, _y, 0x75a9a9, true);
			_y = y / 2 - 20;
			fontBold.drawCentredTextWithPotentialShadow("Welcome to RuneScape", x / 2, _y, 0xffff00, true);
			_y += 30;
			int _x = x / 2 - 80;
			int __y = y / 2 + 20;
			titleButtonImage.drawImage(_x - 73, __y - 20);
			fontBold.drawCentredTextWithPotentialShadow("New User", _x, __y + 5, 0xffffff, true);
			_x = x / 2 + 80;
			titleButtonImage.drawImage(_x - 73, __y - 20);
			fontBold.drawCentredTextWithPotentialShadow("Existing User", _x, __y + 5, 0xffffff, true);
		}
		if (loginScreenState == 2) {
			int _y = y / 2 - 40;
			if (loginMessage1.length() > 0) {
				fontBold.drawCentredTextWithPotentialShadow(loginMessage1, x / 2, _y - 15, 0xffff00, true);
				fontBold.drawCentredTextWithPotentialShadow(loginMessage2, x / 2, _y, 0xffff00, true);
				_y += 30;
			} else {
				fontBold.drawCentredTextWithPotentialShadow(loginMessage2, x / 2, _y - 7, 0xffff00, true);
				_y += 30;
			}
			fontBold.drawTextWithPotentialShadow(
					"Username: " + enteredUsername + ((loginScreenFocus == 0) & (tick % 40 < 20) ? "@yel@|" : ""),
					x / 2 - 90, _y, 0xffffff, true);
			_y += 15;
			fontBold.drawTextWithPotentialShadow(
					"Password: " + TextClass.asterisksForString(enteredPassword)
							+ ((loginScreenFocus == 1) & (tick % 40 < 20) ? "@yel@|" : ""),
					x / 2 - 88, _y, 0xffffff, true);
			_y += 15;
			if (!originalLoginScreen) {
				int _x = x / 2 - 80;
				int __y = y / 2 + 50;
				titleButtonImage.drawImage(_x - 73, __y - 20);
				fontBold.drawCentredTextWithPotentialShadow("Login", _x, __y + 5, 0xffffff, true);
				_x = x / 2 + 80;
				titleButtonImage.drawImage(_x - 73, __y - 20);
				fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xffffff, true);
			}
		}
		if (loginScreenState == 3) {
			fontBold.drawCentredTextWithPotentialShadow("Create a free account", x / 2, y / 2 - 60, 0xffff00, true);
			int _y = y / 2 - 35;
			fontBold.drawCentredTextWithPotentialShadow("To create a new account you need to", x / 2, _y, 0xffffff,
					true);
			_y += 15;
			fontBold.drawCentredTextWithPotentialShadow("go back to the main RuneScape webpage", x / 2, _y, 0xffffff,
					true);
			_y += 15;
			fontBold.drawCentredTextWithPotentialShadow("and choose the red 'create account'", x / 2, _y, 0xffffff,
					true);
			_y += 15;
			fontBold.drawCentredTextWithPotentialShadow("button at the top right of that page.", x / 2, _y, 0xffffff,
					true);
			_y += 15;
			int _x = x / 2;
			int __y = y / 2 + 50;
			titleButtonImage.drawImage(_x - 73, __y - 20);
			fontBold.drawCentredTextWithPotentialShadow("Cancel", _x, __y + 5, 0xffffff, true);
		}
		loginBoxLeftBackgroundTile.drawGraphics(171, super.gameGraphics, 202);
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			topLeft1BackgroundTile.drawGraphics(0, super.gameGraphics, 128);
			bottomLeft1BackgroundTile.drawGraphics(371, super.gameGraphics, 202);
			bottomLeft0BackgroundTile.drawGraphics(265, super.gameGraphics, 0);
			aRSImageProducer_1113.drawGraphics(265, super.gameGraphics, 562);
			middleLeft1BackgroundTile.drawGraphics(171, super.gameGraphics, 128);
			aRSImageProducer_1115.drawGraphics(171, super.gameGraphics, 562);
		}
	}

	private void drawLogo() {
		byte titleData[] = archiveTitle.decompressFile("title.dat");
		Sprite sprite = new Sprite(titleData, this);
		flameLeftBackground.initDrawingArea();
		sprite.drawInverse(0, 0);
		flameRightBackground.initDrawingArea();
		sprite.drawInverse(-637, 0);
		topLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(-128, 0);
		bottomLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(-202, -371);
		loginBoxLeftBackgroundTile.initDrawingArea();
		sprite.drawInverse(-202, -171);
		bottomLeft0BackgroundTile.initDrawingArea();
		sprite.drawInverse(0, -265);
		aRSImageProducer_1113.initDrawingArea();
		sprite.drawInverse(-562, -265);
		middleLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(-128, -171);
		aRSImageProducer_1115.initDrawingArea();
		sprite.drawInverse(-562, -171);
		int modifiedPixels[] = new int[sprite.width];
		for (int row = 0; row < sprite.height; row++) {
			for (int column = 0; column < sprite.width; column++)
				modifiedPixels[column] = sprite.pixels[(sprite.width - column - 1) + sprite.width * row];

			System.arraycopy(modifiedPixels, 0, sprite.pixels, sprite.width * row, sprite.width);

		}

		flameLeftBackground.initDrawingArea();
		sprite.drawInverse(382, 0);
		flameRightBackground.initDrawingArea();
		sprite.drawInverse(-255, 0);
		topLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(254, 0);
		bottomLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(180, -371);
		loginBoxLeftBackgroundTile.initDrawingArea();
		sprite.drawInverse(180, -171);
		bottomLeft0BackgroundTile.initDrawingArea();
		sprite.drawInverse(382, -265);
		aRSImageProducer_1113.initDrawingArea();
		sprite.drawInverse(-180, -265);
		middleLeft1BackgroundTile.initDrawingArea();
		sprite.drawInverse(254, -171);
		aRSImageProducer_1115.initDrawingArea();
		sprite.drawInverse(-180, -171);
		sprite = new Sprite(archiveTitle, "logo", 0);
		topLeft1BackgroundTile.initDrawingArea();
		sprite.drawImage(382 - sprite.width / 2 - 128, 18);
		sprite = null;
		System.gc();

	}

	private void drawMenu() {
		int offsetX = menuOffsetX;
		int offsetY = menuOffsetY;
		int width = menuWidth;
		int height = menuHeight;
		int colour = 0x5d5447;
		DrawingArea.drawFilledRectangle(offsetX, offsetY, width, height, colour);
		DrawingArea.drawFilledRectangle(offsetX + 1, offsetY + 1, width - 2, 16, 0);
		DrawingArea.drawUnfilledRectangle(offsetX + 1, width - 2, height - 19, 0, offsetY + 18);
		fontBold.drawText("Choose Option", offsetX + 3, offsetY + 14, colour);
		int x = super.mouseX;
		int y = super.mouseY;
		if (menuScreenArea == 0) {
			x -= 4;
			y -= 4;
		}
		if (menuScreenArea == 1) {
			x -= 553;
			y -= 205;
		}
		if (menuScreenArea == 2) {
			x -= 17;
			y -= 357;
		}
		for (int action = 0; action < menuActionRow; action++) {
			int actionY = offsetY + 31 + (menuActionRow - 1 - action) * 15;
			int actionColour = 0xffffff;
			if (x > offsetX && x < offsetX + width && y > actionY - 13 && y < actionY + 3)
				actionColour = 0xffff00;
			fontBold.drawTextWithPotentialShadow(menuActionName[action], offsetX + 3, actionY, actionColour, true);
		}

	}

	private void drawMinimap() {
		minimapImageProducer.initDrawingArea();
		if (minimapState == 2) {
			byte backgroundPixels[] = minimapBackgroundImage.imagePixels;
			int rasterPixels[] = DrawingArea.pixels;
			int pixelCount = backgroundPixels.length;
			for (int p = 0; p < pixelCount; p++)
				if (backgroundPixels[p] == 0)
					rasterPixels[p] = 0;

			minimapCompassImage.rotate(33, cameraHorizontal, compassWidthMap, 256, compassHingeSize, 25, 0, 0, 33, 25);
			gameScreenImageProducer.initDrawingArea();
			return;
		}
		int angle = cameraHorizontal + minimapRotation & 0x7ff;
		int centreX = 48 + localPlayer.x / 32;
		int centreY = 464 - localPlayer.y / 32;
		minimapImage.rotate(151, angle, minimapLineWidth, 256 + minimapZoom, minimapLeft, centreY, 5, 25, 146, centreX);
		minimapCompassImage.rotate(33, cameraHorizontal, compassWidthMap, 256, compassHingeSize, 25, 0, 0, 33, 25);
		for (int icon = 0; icon < minimapHintCount; icon++) {
			int mapX = (minimapHintX[icon] * 4 + 2) - localPlayer.x / 32;
			int mapY = (minimapHintY[icon] * 4 + 2) - localPlayer.y / 32;
			markMinimap(minimapHint[icon], mapX, mapY);
		}

		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				NodeList itemStack = groundArray[plane][x][y];
				if (itemStack != null) {
					int mapX = (x * 4 + 2) - localPlayer.x / 32;
					int mapY = (y * 4 + 2) - localPlayer.y / 32;
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
					int mapX = npc.x / 32 - localPlayer.x / 32;
					int mapY = npc.y / 32 - localPlayer.y / 32;
					markMinimap(mapDotNPC, mapX, mapY);
				}
			}
		}

		for (int p = 0; p < localPlayerCount; p++) {
			Player player = players[localPlayers[p]];
			if (player != null && player.isVisible()) {
				int mapX = player.x / 32 - localPlayer.x / 32;
				int mapY = player.y / 32 - localPlayer.y / 32;
				boolean friend = false;
				long nameHash = TextClass.nameToLong(player.name);
				for (int f = 0; f < friendsCount; f++) {
					if (nameHash != friendsListAsLongs[f] || friendsWorldIds[f] == 0)
						continue;
					friend = true;
					break;
				}

				boolean team = false;
				if (localPlayer.team != 0 && player.team != 0 && localPlayer.team == player.team)
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
					int mapX = npc.x / 32 - localPlayer.x / 32;
					int mapY = npc.y / 32 - localPlayer.y / 32;
					drawMinimapTarget(mapMarker, mapY, mapX);
				}
			}
			if (hintIconType == 2) {
				int mapX = ((hintIconX - baseX) * 4 + 2) - localPlayer.x / 32;
				int mapY = ((hintIconY - baseY) * 4 + 2) - localPlayer.y / 32;
				drawMinimapTarget(mapMarker, mapY, mapX);
			}
			if (hintIconType == 10 && hintIconPlayerId >= 0 && hintIconPlayerId < players.length) {
				Player player = players[hintIconPlayerId];
				if (player != null) {
					int mapX = player.x / 32 - localPlayer.x / 32;
					int mapY = player.y / 32 - localPlayer.y / 32;
					drawMinimapTarget(mapMarker, mapY, mapX);
				}
			}
		}
		if (destinationX != 0) {
			int mapX = (destinationX * 4 + 2) - localPlayer.x / 32;
			int mapY = (destinationY * 4 + 2) - localPlayer.y / 32;
			markMinimap(mapFlag, mapX, mapY);
		}
		DrawingArea.drawFilledRectangle(97, 78, 3, 3, 0xffffff);
		gameScreenImageProducer.initDrawingArea();
	}

	private void drawMinimapScene(int y, int lineColour, int x, int interfactiveColour, int z) {
		int uid = worldController.getWallObjectHash(x, y, z);
		if (uid != 0) {
			// Walls

			int config = worldController.getConfig(uid, x, y, z);
			int direction = config >> 6 & 3;
			int type = config & 0x1f;
			int colour = lineColour;
			if (uid > 0)
				colour = interfactiveColour;
			int pixels[] = minimapImage.pixels;
			int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
			int objectId = uid >> 14 & 0x7fff;
			GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
			if (definition.mapScene != -1) {
				Background background = mapSceneImage[definition.mapScene];
				if (background != null) {
					int _x = (definition.sizeX * 4 - background.imageWidth) / 2;
					int _y = (definition.sizeY * 4 - background.imageHeight) / 2;
					background.drawImage(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
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
			int type = config & 0x1f;
			int objectId = uid >> 14 & 0x7fff;
			GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
			if (definition.mapScene != -1) {
				Background background = mapSceneImage[definition.mapScene];
				if (background != null) {
					int _x = (definition.sizeX * 4 - background.imageWidth) / 2;
					int _y = (definition.sizeY * 4 - background.imageHeight) / 2;
					background.drawImage(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
				}
			} else if (type == 9) {
				// Diagonal walls and doors

				int colour = 0xeeeeee;
				if (uid > 0)
					colour = 0xee0000;
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
			int objectId = uid >> 14 & 0x7fff;
			GameObjectDefinition definition = GameObjectDefinition.getDefinition(objectId);
			if (definition.mapScene != -1) {
				Background background = mapSceneImage[definition.mapScene];
				if (background != null) {
					int _x = (definition.sizeX * 4 - background.imageWidth) / 2;
					int _y = (definition.sizeY * 4 - background.imageHeight) / 2;
					background.drawImage(48 + x * 4 + _x, 48 + (104 - y - definition.sizeY) * 4 + _y);
				}
			}
		}
	}

	private void drawMinimapTarget(Sprite sprite, int y, int x) {
		int l = x * x + y * y;
		if (l > 4225 && l < 0x15f90) {
			int angle = cameraHorizontal + minimapRotation & 0x7ff;
			int sine = Model.SINE[angle];
			int cosine = Model.COSINE[angle];
			sine = (sine * 256) / (minimapZoom + 256);
			cosine = (cosine * 256) / (minimapZoom + 256);
			int l1 = y * sine + x * cosine >> 16;
			int i2 = y * cosine - x * sine >> 16;
			double d = Math.atan2(l1, i2);
			int j2 = (int) (Math.sin(d) * 63D);
			int k2 = (int) (Math.cos(d) * 57D);
			minimapEdgeImage.rotate((94 + j2 + 4) - 10, 83 - k2 - 20, d);
		} else {
			markMinimap(sprite, x, y);
		}
	}

	private void drawSplitPrivateChat() {
		if (splitPrivateChat == 0)
			return;
		GameFont textDrawingArea = fontPlain;
		int updating = 0;
		if (systemUpdateTime != 0)
			updating = 1;
		for (int m = 0; m < 100; m++)
			if (chatMessages[m] != null) {
				int chatType = chatTypes[m];
				String chatName = chatNames[m];
				byte playerRights = 0;
				if (chatName != null && chatName.startsWith("@cr1@")) {
					chatName = chatName.substring(5);
					playerRights = 1;
				}
				if (chatName != null && chatName.startsWith("@cr2@")) {
					chatName = chatName.substring(5);
					playerRights = 2;
				}
				if ((chatType == 3 || chatType == 7) && (chatType == 7 || privateChatMode == 0
						|| privateChatMode == 1 && isFriendOrSelf(chatName))) {
					int y = 329 - updating * 13;
					int x = 4;
					textDrawingArea.drawText("From", x, y, 0);
					textDrawingArea.drawText("From", x, y - 1, 65535);
					x += textDrawingArea.getTextDisplayedWidth("From ");
					if (playerRights == 1) {
						modIcons[0].drawImage(x, y - 12);
						x += 14;
					}
					if (playerRights == 2) {
						modIcons[1].drawImage(x, y - 12);
						x += 14;
					}
					textDrawingArea.drawText(chatName + ": " + chatMessages[m], x, y, 0);
					textDrawingArea.drawText(chatName + ": " + chatMessages[m], x, y - 1, 65535);
					if (++updating >= 5)
						return;
				}
				if (chatType == 5 && privateChatMode < 2) {
					int y = 329 - updating * 13;
					textDrawingArea.drawText(chatMessages[m], 4, y, 0);
					textDrawingArea.drawText(chatMessages[m], 4, y - 1, 65535);
					if (++updating >= 5)
						return;
				}
				if (chatType == 6 && privateChatMode < 2) {
					int y = 329 - updating * 13;
					textDrawingArea.drawText("To " + chatName + ": " + chatMessages[m], 4, y, 0);
					textDrawingArea.drawText("To " + chatName + ": " + chatMessages[m], 4, y - 1, 65535);
					if (++updating >= 5)
						return;
				}
			}
	}

	private void drawTabArea() {
		tabImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = sidebarOffsets;
		inventoryBackgroundImage.drawImage(0, 0);
		if (inventoryOverlayInterfaceID != -1)
			drawInterface(0, 0, RSInterface.cache[inventoryOverlayInterfaceID], 0);
		else if (tabInterfaceIDs[currentTabId] != -1)
			drawInterface(0, 0, RSInterface.cache[tabInterfaceIDs[currentTabId]], 0);
		if (menuOpen && menuScreenArea == 1)
			drawMenu();
		tabImageProducer.drawGraphics(205, super.gameGraphics, 553);
		gameScreenImageProducer.initDrawingArea();
		Rasterizer.lineOffsets = viewportOffsets;
	}

	private void drawTooltip() {
		if (menuActionRow < 2 && itemSelected == false && spellSelected == false)
			return;
		String s;
		if (itemSelected && menuActionRow < 2)
			s = "Use " + selectedItemName + " with...";
		else if (spellSelected && menuActionRow < 2)
			s = spellTooltip + "...";
		else
			s = menuActionName[menuActionRow - 1];
		if (menuActionRow > 2)
			s = s + "@whi@ / " + (menuActionRow - 2) + " more options";
		fontBold.drawAlphaTextWithShadow(s, 4, 15, 0xffffff, tick / 1000);
	}

	private void dropClient() {
		if (idleLogout > 0) {
			logout();
			return;
		}
		gameScreenImageProducer.initDrawingArea();
		fontPlain.drawCentredText("Connection lost", 257, 144, 0);
		fontPlain.drawCentredText("Connection lost", 256, 143, 0xffffff);
		fontPlain.drawCentredText("Please wait - attempting to reestablish", 257, 159, 0);
		fontPlain.drawCentredText("Please wait - attempting to reestablish", 256, 158, 0xffffff);
		gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
		minimapState = 0;
		destinationX = 0;
		RSSocket rsSocket = socket;
		loggedIn = false;
		loginFailures = 0;
		login(enteredUsername, enteredPassword, true);
		if (!loggedIn)
			logout();
		try {
			rsSocket.close();
		} catch (Exception _ex) {
		}
	}

	@Override
	public AppletContext getAppletContext() {
		if (signlink.applet != null)
			return signlink.applet.getAppletContext();
		else
			return super.getAppletContext();
	}

	private int getCameraPlaneCutscene() {
		int terrainDrawHeight = getFloorDrawHeight(plane, cameraPositionY, cameraPositionX);
		if (terrainDrawHeight - cameraPositionZ < 800
				&& (tileFlags[plane][cameraPositionX >> 7][cameraPositionY >> 7] & 4) != 0)
			return plane;
		else
			return 3;
	}

	@Override
	public URL getCodeBase() {
		if (signlink.applet != null)
			return signlink.applet.getCodeBase();
		try {
			if (super.gameFrame != null)
				return new URL("http://127.0.0.1:" + (80 + portOffset));
		} catch (Exception _ex) {
		}
		return super.getCodeBase();
	}

	private String getDocumentBaseHost() {
		if (signlink.applet != null)
			return signlink.applet.getDocumentBase().getHost().toLowerCase();
		if (super.gameFrame != null)
			return "runescape.com";
		else
			return super.getDocumentBase().getHost().toLowerCase();
	}

	private int getFloorDrawHeight(int z, int y, int x) {
		int groundX = x >> 7;
		int groundY = y >> 7;
		if (groundX < 0 || groundY < 0 || groundX > 103 || groundY > 103)
			return 0;
		int groundZ = z;
		if (groundZ < 3 && (tileFlags[1][groundX][groundY] & 2) == 2)
			groundZ++;
		int _x = x & 0x7f;
		int _y = y & 0x7f;
		int i2 = intGroundArray[groundZ][groundX][groundY] * (128 - _x)
				+ intGroundArray[groundZ][groundX + 1][groundY] * _x >> 7;
		int j2 = intGroundArray[groundZ][groundX][groundY + 1] * (128 - _x)
				+ intGroundArray[groundZ][groundX + 1][groundY + 1] * _x >> 7;

		return i2 * (128 - _y) + j2 * _y >> 7;
	}

	@Override
	Component getGameComponent() {
		if (signlink.applet != null)
			return signlink.applet;
		if (super.gameFrame != null)
			return super.gameFrame;
		else
			return this;
	}

	@Override
	public String getParameter(String s) {
		if (signlink.applet != null)
			return signlink.applet.getParameter(s);
		else
			return super.getParameter(s);
	}

	private int getWorldDrawPlane() {
		int worldDrawPlane = 3;
		if (cameraVerticalRotation < 310) {
			int cameraX = cameraPositionX >> 7;
			int cameraY = cameraPositionY >> 7;
			int playerX = localPlayer.x >> 7;
			int playerY = localPlayer.y >> 7;
			if ((tileFlags[plane][cameraX][cameraY] & 4) != 0)
				worldDrawPlane = plane;
			int x;
			if (playerX > cameraX)
				x = playerX - cameraX;
			else
				x = cameraX - playerX;
			int y;
			if (playerY > cameraY)
				y = playerY - cameraY;
			else
				y = cameraY - playerY;
			if (x > y) {
				int unknown1 = (y * 65536) / x;
				int unknown2 = 32768;
				while (cameraX != playerX) {
					if (cameraX < playerX)
						cameraX++;
					else if (cameraX > playerX)
						cameraX--;
					if ((tileFlags[plane][cameraX][cameraY] & 4) != 0)
						worldDrawPlane = plane;
					unknown2 += unknown1;
					if (unknown2 >= 65536) {
						unknown2 -= 65536;
						if (cameraY < playerY)
							cameraY++;
						else if (cameraY > playerY)
							cameraY--;
						if ((tileFlags[plane][cameraX][cameraY] & 4) != 0)
							worldDrawPlane = plane;
					}
				}
			} else {
				int unknown1 = (x * 65536) / y;
				int unknown2 = 32768;
				while (cameraY != playerY) {
					if (cameraY < playerY)
						cameraY++;
					else if (cameraY > playerY)
						cameraY--;
					if ((tileFlags[plane][cameraX][cameraY] & 4) != 0)
						worldDrawPlane = plane;
					unknown2 += unknown1;
					if (unknown2 >= 65536) {
						unknown2 -= 65536;
						if (cameraX < playerX)
							cameraX++;
						else if (cameraX > playerX)
							cameraX--;
						if ((tileFlags[plane][cameraX][cameraY] & 4) != 0)
							worldDrawPlane = plane;
					}
				}
			}
		}
		if ((tileFlags[plane][localPlayer.x >> 7][localPlayer.y >> 7] & 4) != 0)
			worldDrawPlane = plane;

		return worldDrawPlane;
	}

	private boolean handleIncomingData() {
		if (socket == null)
			return false;
		try {
			int availableBytes = socket.available();
			if (availableBytes == 0)
				return false;
			if (packetOpcode == -1) {
				socket.read(inStream.buffer, 1);
				packetOpcode = inStream.buffer[0] & 0xff;
				if (encryption != null)
					packetOpcode = packetOpcode - encryption.value() & 0xff;
				packetSize = SizeConstants.PACKET_SIZES[packetOpcode];
				availableBytes--;
			}
			if (packetSize == -1)
				if (availableBytes > 0) {
					socket.read(inStream.buffer, 1);
					packetSize = inStream.buffer[0] & 0xff;
					availableBytes--;
				} else {
					return false;
				}
			if (packetSize == -2)
				if (availableBytes > 1) {
					socket.read(inStream.buffer, 2);
					inStream.position = 0;
					packetSize = inStream.getUnsignedLEShort();
					availableBytes -= 2;
				} else {
					return false;
				}
			if (availableBytes < packetSize)
				return false;
			inStream.position = 0;
			socket.read(inStream.buffer, packetSize);
			anInt1009 = 0;
			thirdMostRecentOpcode = secondMostRecentOpcode;
			secondMostRecentOpcode = mostRecentOpcode;
			mostRecentOpcode = packetOpcode;
			if (packetOpcode == 81) {
				updatePlayers(packetSize, inStream);
				loadingMap = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 176) {
				daysSinceRecoveryChange = inStream.getUnsignedByteC();
				unreadMessages = inStream.getUnsignedLEShortA();
				membership = inStream.getUnsignedByte();
				lastAddress = inStream.getMEBInt();
				daysSinceLogin = inStream.getUnsignedLEShort();
				if (lastAddress != 0 && openInterfaceId == -1) {
					signlink.dnslookup(TextClass.decodeDNS(lastAddress));
					clearTopInterfaces();
					int contentType = 650;
					if (daysSinceRecoveryChange != 201 || membership == 1)
						contentType = 655;
					reportAbuseInput = "";
					reportAbuseMute = false;
					for (int interfaceId = 0; interfaceId < RSInterface.cache.length; interfaceId++) {
						if (RSInterface.cache[interfaceId] == null
								|| RSInterface.cache[interfaceId].contentType != contentType)
							continue;
						openInterfaceId = RSInterface.cache[interfaceId].parentID;
						break;
					}

				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 64) {
				playerPositionX = inStream.getUnsignedByteC();
				playerPositionY = inStream.getUnsignedByteS();
				for (int x = playerPositionX; x < playerPositionX + 8; x++) {
					for (int y = playerPositionY; y < playerPositionY + 8; y++)
						if (groundArray[plane][x][y] != null) {
							groundArray[plane][x][y] = null;
							spawnGroundItem(x, y);
						}

				}

				for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) spawnObjectList
						.peekLast(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) spawnObjectList
								.reverseGetNext())
					if (spawnRequest.x >= playerPositionX && spawnRequest.x < playerPositionX + 8
							&& spawnRequest.y >= playerPositionY && spawnRequest.y < playerPositionY + 8
							&& spawnRequest.z == plane)
						spawnRequest.delayUntilRespawn = 0;

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 185) {
				int interfaceId = inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelTypeDefault = 3;
				if (localPlayer.npcAppearance == null)
					RSInterface.cache[interfaceId].modelIdDefault = (localPlayer.bodyPartColour[0] << 25)
							+ (localPlayer.bodyPartColour[4] << 20) + (localPlayer.appearance[0] << 15)
							+ (localPlayer.appearance[8] << 10) + (localPlayer.appearance[11] << 5)
							+ localPlayer.appearance[1];
				else
					RSInterface.cache[interfaceId].modelIdDefault = (int) (0x12345678L + localPlayer.npcAppearance.id);
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 107) {
				cutsceneActive = false;
				for (int c = 0; c < 5; c++)
					customCameraActive[c] = false;

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 72) {
				int interfaceId = inStream.getUnsignedShort();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				for (int slot = 0; slot < rsInterface.inventoryItemId.length; slot++) {
					rsInterface.inventoryItemId[slot] = -1;
					rsInterface.inventoryItemId[slot] = 0;
				}

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 214) {
				ignoreCount = packetSize / 8;
				for (int p = 0; p < ignoreCount; p++)
					ignoreListAsLongs[p] = inStream.getLong();

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 166) { // Spin camera
				cutsceneActive = true;
				anInt1098 = inStream.getUnsignedByte();
				anInt1099 = inStream.getUnsignedByte();
				anInt1100 = inStream.getUnsignedLEShort();
				anInt1101 = inStream.getUnsignedByte();
				anInt1102 = inStream.getUnsignedByte();
				if (anInt1102 >= 100) {
					cameraPositionX = anInt1098 * 128 + 64;
					cameraPositionY = anInt1099 * 128 + 64;
					cameraPositionZ = getFloorDrawHeight(plane, cameraPositionY, cameraPositionX) - anInt1100;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 134) {
				redrawTab = true;
				int _skillId = inStream.getUnsignedByte();
				int _skillExp = inStream.getMESInt();
				int _skillLevel = inStream.getUnsignedByte();
				skillExperience[_skillId] = _skillExp;
				skillLevel[_skillId] = _skillLevel;
				skillMaxLevel[_skillId] = 1;
				for (int level = 0; level < 98; level++)
					if (_skillExp >= EXPERIENCE_TABLE[level])
						skillMaxLevel[_skillId] = level + 2;

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 71) {
				int sidebarId = inStream.getUnsignedLEShort();
				int interfaceId = inStream.getUnsignedByteA();
				if (sidebarId == 65535)
					sidebarId = -1;
				tabInterfaceIDs[interfaceId] = sidebarId;
				redrawTab = true;
				drawTabIcons = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 74) {
				int songId = inStream.getUnsignedShort();
				if (songId == 65535)
					songId = -1;
				if (songId != currentSong && musicEnabled && !lowMemory && prevSong == 0) {
					nextSong = songId;
					songChanging = true;
					onDemandFetcher.request(2, nextSong);
				}
				currentSong = songId;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 121) {
				int nextSong = inStream.getUnsignedShortA();
				int previousSong = inStream.getUnsignedLEShortA();
				if (musicEnabled && !lowMemory) {
					this.nextSong = nextSong;
					songChanging = false;
					onDemandFetcher.request(2, this.nextSong);
					this.prevSong = previousSong;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 109) {
				logout();
				packetOpcode = -1;
				return false;
			}
			if (packetOpcode == 70) {
				int x = inStream.getShort();
				int y = inStream.getSignedLEShort();
				int interfaceId = inStream.getUnsignedShort();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				rsInterface.x = x;
				rsInterface.y = y;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 73 || packetOpcode == 241) {

				// mapReset();
				int playerRegionX = regionX;
				int playerRegionY = regionY;
				if (packetOpcode == 73) {
					playerRegionX = inStream.getUnsignedLEShortA();
					playerRegionY = inStream.getUnsignedLEShort();
					loadGeneratedMap = false;
				}
				if (packetOpcode == 241) {
					playerRegionY = inStream.getUnsignedLEShortA();
					inStream.initBitAccess();
					for (int z = 0; z < 4; z++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								int tileExists = inStream.readBits(1);
								if (tileExists == 1)
									constructMapTiles[z][x][y] = inStream.readBits(26);
								else
									constructMapTiles[z][x][y] = -1;
							}
						}
					}

					inStream.finishBitAccess();
					playerRegionX = inStream.getUnsignedLEShort();
					loadGeneratedMap = true;
				}
				if (regionX == playerRegionX && regionY == playerRegionY && loadingStage == 2) {
					packetOpcode = -1;
					return true;
				}
				regionX = playerRegionX;
				regionY = playerRegionY;
				baseX = (regionX - 6) * 8;
				baseY = (regionY - 6) * 8;
				inTutorialIsland = (regionX / 8 == 48 || regionX / 8 == 49) && regionY / 8 == 48;
				if (regionX / 8 == 48 && regionY / 8 == 148)
					inTutorialIsland = true;
				loadingStage = 1;
				loadRegionTime = System.currentTimeMillis();
				gameScreenImageProducer.initDrawingArea();
				fontPlain.drawCentredText("Loading - please wait.", 257, 151, 0);
				fontPlain.drawCentredText("Loading - please wait.", 256, 150, 0xffffff);
				gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
				if (packetOpcode == 73) {
					int r = 0;
					for (int x = (regionX - 6) / 8; x <= (regionX + 6) / 8; x++) {
						for (int y = (regionY - 6) / 8; y <= (regionY + 6) / 8; y++)
							r++;
					}

					terrainData = new byte[r][];
					objectData = new byte[r][];
					mapCoordinates = new int[r];
					terrainDataIds = new int[r];
					objectDataIds = new int[r];
					r = 0;
					for (int x = (regionX - 6) / 8; x <= (regionX + 6) / 8; x++) {
						for (int y = (regionY - 6) / 8; y <= (regionY + 6) / 8; y++) {
							mapCoordinates[r] = (x << 8) + y;
							if (inTutorialIsland
									&& (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
								terrainDataIds[r] = -1;
								objectDataIds[r] = -1;
								r++;
							} else {
								int terrainId = terrainDataIds[r] = onDemandFetcher.getMapId(0, x, y);
								if (terrainId != -1)
									onDemandFetcher.request(3, terrainId);
								int objectId = objectDataIds[r] = onDemandFetcher.getMapId(1, x, y);
								if (objectId != -1)
									onDemandFetcher.request(3, objectId);
								r++;
							}
						}
					}
				}
				if (packetOpcode == 241) {
					int l16 = 0;
					int ai[] = new int[676];
					for (int plane = 0; plane < 4; plane++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								int k30 = constructMapTiles[plane][x][y];
								if (k30 != -1) {
									int k31 = k30 >> 14 & 0x3ff;
									int i32 = k30 >> 3 & 0x7ff;
									int k32 = (k31 / 8 << 8) + i32 / 8;
									for (int j33 = 0; j33 < l16; j33++) {
										if (ai[j33] != k32)
											continue;
										k32 = -1;
										break;
									}

									if (k32 != -1)
										ai[l16++] = k32;
								}
							}
						}
					}

					terrainData = new byte[l16][];
					objectData = new byte[l16][];
					mapCoordinates = new int[l16];
					terrainDataIds = new int[l16];
					objectDataIds = new int[l16];
					for (int r = 0; r < l16; r++) {
						int coords = mapCoordinates[r] = ai[r];
						int x = coords >> 8 & 0xff;
						int y = coords & 0xff;
						int terrainId = terrainDataIds[r] = onDemandFetcher.getMapId(0, x, y);
						if (terrainId != -1)
							onDemandFetcher.request(3, terrainId);
						int objectId = objectDataIds[r] = onDemandFetcher.getMapId(1, x, y);
						if (objectId != -1)
							onDemandFetcher.request(3, objectId);
					}
				}
				int _x = baseX - anInt1036;
				int _y = baseY - anInt1037;
				anInt1036 = baseX;
				anInt1037 = baseY;
				for (int n = 0; n < 16384; n++) {
					NPC npc = npcs[n];
					if (npc != null) {
						for (int waypoint = 0; waypoint < 10; waypoint++) {
							npc.waypointX[waypoint] -= _x;
							npc.waypointY[waypoint] -= _y;
						}

						npc.x -= _x * 128;
						npc.y -= _y * 128;
					}
				}
				for (int p = 0; p < MAX_ENTITY_COUNT; p++) {
					Player player = players[p];
					if (player != null) {
						for (int waypoint = 0; waypoint < 10; waypoint++) {
							player.waypointX[waypoint] -= _x;
							player.waypointY[waypoint] -= _y;
						}

						player.x -= _x * 128;
						player.y -= _y * 128;
					}
				}
				loadingMap = true;
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
						int x2 = x + _x;
						int y2 = y + _y;
						for (int z = 0; z < 4; z++)
							if (x2 >= 0 && y2 >= 0 && x2 < 104 && y2 < 104)
								groundArray[z][x][y] = groundArray[z][x2][y2];
							else
								groundArray[z][x][y] = null;
					}
				}
				for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) spawnObjectList
						.peekLast(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) spawnObjectList
								.reverseGetNext()) {
					spawnRequest.x -= _x;
					spawnRequest.y -= _y;
					if (spawnRequest.x < 0 || spawnRequest.y < 0 || spawnRequest.x >= 104 || spawnRequest.y >= 104)
						spawnRequest.unlink();
				}

				if (destinationX != 0) {
					destinationX -= _x;
					destinationY -= _y;
				}
				cutsceneActive = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 208) {
				int interfaceId = inStream.getSignedLEShort();
				if (interfaceId >= 0)
					loadInterface(interfaceId);
				walkableInterfaceId = interfaceId;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 99) {
				minimapState = inStream.getUnsignedByte();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 75) {
				int modelId = inStream.getUnsignedShortA();
				int interfaceId = inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelTypeDefault = 2;
				RSInterface.cache[interfaceId].modelIdDefault = modelId;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 114) {
				systemUpdateTime = inStream.getUnsignedShort() * 30;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 60) {
				playerPositionY = inStream.getUnsignedByte();
				playerPositionX = inStream.getUnsignedByteC();
				while (inStream.position < packetSize) {
					int opcode = inStream.getUnsignedByte();
					parseGroupPacket(inStream, opcode);
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 35) {
				int cameraId = inStream.getUnsignedByte();
				int jitter = inStream.getUnsignedByte();
				int amplitude = inStream.getUnsignedByte();
				int frequency = inStream.getUnsignedByte();
				customCameraActive[cameraId] = true;
				cameraJitter[cameraId] = jitter;
				cameraAmplitude[cameraId] = amplitude;
				cameraFrequency[cameraId] = frequency;
				unknownCameraVariable[cameraId] = 0;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 174) {
				int trackId = inStream.getUnsignedLEShort();
				int loop = inStream.getUnsignedByte();
				int delay = inStream.getUnsignedLEShort();
				if (effectsEnabled && !lowMemory && trackCount < 50) {
					trackIds[trackCount] = trackId;
					trackLoop[trackCount] = loop;
					trackDelay[trackCount] = delay + Effect.effectDelays[trackId];
					trackCount++;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 104) {
				int actionId = inStream.getUnsignedByteC();
				int actionAtTop = inStream.getUnsignedByteA();
				String actionText = inStream.getString();
				if (actionId >= 1 && actionId <= 5) {
					if (actionText.equalsIgnoreCase("null"))
						actionText = null;
					playerActionText[actionId - 1] = actionText;
					playerActionUnpinned[actionId - 1] = actionAtTop == 0;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 78) {
				destinationX = 0;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 253) {
				String message = inStream.getString();
				if (message.endsWith(":tradereq:")) {
					String name = message.substring(0, message.indexOf(":"));
					long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < ignoreCount; p++) {
						if (ignoreListAsLongs[p] != nameAsLong)
							continue;
						ignored = true;
						break;
					}

					if (!ignored && inTutorial == 0)
						pushMessage("wishes to trade with you.", 4, name);
				} else if (message.endsWith(":duelreq:")) {
					String name = message.substring(0, message.indexOf(":"));
					long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < ignoreCount; p++) {
						if (ignoreListAsLongs[p] != nameAsLong)
							continue;
						ignored = true;
						break;
					}

					if (!ignored && inTutorial == 0)
						pushMessage("wishes to duel with you.", 8, name);
				} else if (message.endsWith(":chalreq:")) {
					String name = message.substring(0, message.indexOf(":"));
					long nameAsLong = TextClass.nameToLong(name);
					boolean ignored = false;
					for (int p = 0; p < ignoreCount; p++) {
						if (ignoreListAsLongs[p] != nameAsLong)
							continue;
						ignored = true;
						break;
					}

					if (!ignored && inTutorial == 0) {
						String text = message.substring(message.indexOf(":") + 1, message.length() - 9);
						pushMessage(text, 8, name);
					}
				} else {
					pushMessage(message, 0, "");
				}
				packetOpcode = -1;

				return true;
			}
			if (packetOpcode == 1) {
				for (int p = 0; p < players.length; p++)
					if (players[p] != null)
						players[p].animation = -1;

				for (int n = 0; n < npcs.length; n++)
					if (npcs[n] != null)
						npcs[n].animation = -1;

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 50) {
				long nameAsLong = inStream.getLong();
				int worldId = inStream.getUnsignedByte();
				String name = TextClass.formatName(TextClass.longToName(nameAsLong));
				for (int friend = 0; friend < friendsCount; friend++) {
					if (nameAsLong != friendsListAsLongs[friend])
						continue;
					if (friendsWorldIds[friend] != worldId) {
						friendsWorldIds[friend] = worldId;
						redrawTab = true;
						if (worldId > 0)
							pushMessage(name + " has logged in.", 5, "");
						if (worldId == 0)
							pushMessage(name + " has logged out.", 5, "");
					}
					name = null;
					break;
				}
				if (name != null && friendsCount < 200) {
					friendsListAsLongs[friendsCount] = nameAsLong;
					friendsList[friendsCount] = name;
					friendsWorldIds[friendsCount] = worldId;
					friendsCount++;
					redrawTab = true;
				}
				for (boolean orderComplete = false; !orderComplete;) {
					orderComplete = true;
					for (int friend = 0; friend < friendsCount - 1; friend++)
						if (friendsWorldIds[friend] != localWorldId && friendsWorldIds[friend + 1] == localWorldId
								|| friendsWorldIds[friend] == 0 && friendsWorldIds[friend + 1] != 0) {
							int tempWorld = friendsWorldIds[friend];
							friendsWorldIds[friend] = friendsWorldIds[friend + 1];
							friendsWorldIds[friend + 1] = tempWorld;
							String tempName = friendsList[friend];
							friendsList[friend] = friendsList[friend + 1];
							friendsList[friend + 1] = tempName;
							long tempLong = friendsListAsLongs[friend];
							friendsListAsLongs[friend] = friendsListAsLongs[friend + 1];
							friendsListAsLongs[friend + 1] = tempLong;
							redrawTab = true;
							orderComplete = false;
						}
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 110) {
				if (currentTabId == 12)
					redrawTab = true;
				playerEnergy = inStream.getUnsignedByte();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 254) {
				hintIconType = inStream.getUnsignedByte();
				if (hintIconType == 1)
					hintIconNpcId = inStream.getUnsignedLEShort();
				if (hintIconType >= 2 && hintIconType <= 6) {
					if (hintIconType == 2) {
						hintIconDrawTileX = 64;
						hintIconDrawTileY = 64;
					}
					if (hintIconType == 3) {
						hintIconDrawTileX = 0;
						hintIconDrawTileY = 64;
					}
					if (hintIconType == 4) {
						hintIconDrawTileX = 128;
						hintIconDrawTileY = 64;
					}
					if (hintIconType == 5) {
						hintIconDrawTileX = 64;
						hintIconDrawTileY = 0;
					}
					if (hintIconType == 6) {
						hintIconDrawTileX = 64;
						hintIconDrawTileY = 128;
					}
					hintIconType = 2;
					hintIconX = inStream.getUnsignedLEShort();
					hintIconY = inStream.getUnsignedLEShort();
					hintIconDrawHeight = inStream.getUnsignedByte();
				}
				if (hintIconType == 10)
					hintIconPlayerId = inStream.getUnsignedLEShort();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 248) {
				int interfaceId = inStream.getUnsignedLEShortA();
				int inventoryInterfaceId = inStream.getUnsignedLEShort();
				if (chatboxInterfaceId != -1) {
					chatboxInterfaceId = -1;
					redrawChatbox = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					redrawChatbox = true;
				}
				openInterfaceId = interfaceId;
				inventoryOverlayInterfaceID = inventoryInterfaceId;
				redrawTab = true;
				drawTabIcons = true;
				continuedDialogue = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 79) {
				int interfaceId = inStream.getUnsignedShort();
				int scrollPosition = inStream.getUnsignedLEShortA();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				if (rsInterface != null && rsInterface.type == 0) {
					if (scrollPosition < 0)
						scrollPosition = 0;
					if (scrollPosition > rsInterface.scrollMax - rsInterface.height)
						scrollPosition = rsInterface.scrollMax - rsInterface.height;
					rsInterface.scrollPosition = scrollPosition;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 68) {
				for (int setting = 0; setting < interfaceSettings.length; setting++)
					if (interfaceSettings[setting] != defaultSettings[setting]) {
						interfaceSettings[setting] = defaultSettings[setting];
						handleInterfaceSetting(setting);
						redrawTab = true;
					}

				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 196) {
				long nameAsLong = inStream.getLong();
				int messageId = inStream.getInt();
				int playerRights = inStream.getUnsignedByte();
				boolean ignored = false;
				for (int message = 0; message < 100; message++) {
					if (privateMessages[message] != messageId)
						continue;
					ignored = true;
					break;
				}
				if (playerRights <= 1) {
					for (int p = 0; p < ignoreCount; p++) {
						if (ignoreListAsLongs[p] != nameAsLong)
							continue;
						ignored = true;
						break;
					}
				}
				if (!ignored && inTutorial == 0)
					try {
						privateMessages[privateMessagePointer] = messageId;
						privateMessagePointer = (privateMessagePointer + 1) % 100;
						String message = TextInput.readFromStream(packetSize - 13, inStream);
						if (playerRights != 3)
							message = Censor.doCensor(message);
						if (playerRights == 2 || playerRights == 3)
							pushMessage(message, 7, "@cr2@" + TextClass.formatName(TextClass.longToName(nameAsLong)));
						else if (playerRights == 1)
							pushMessage(message, 7, "@cr1@" + TextClass.formatName(TextClass.longToName(nameAsLong)));
						else
							pushMessage(message, 3, TextClass.formatName(TextClass.longToName(nameAsLong)));
					} catch (Exception exception1) {
						signlink.reporterror("cde1");
					}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 85) {
				playerPositionY = inStream.getUnsignedByteC();
				playerPositionX = inStream.getUnsignedByteC();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 24) {
				flashingSidebar = inStream.getUnsignedByteS();
				if (flashingSidebar == currentTabId) {
					if (flashingSidebar == 3)
						currentTabId = 1;
					else
						currentTabId = 3;
					redrawTab = true;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 246) {
				int interfaceId = inStream.getUnsignedShort();
				int itemModelZoom = inStream.getUnsignedLEShort();
				int itemId = inStream.getUnsignedLEShort();
				if (itemId == 65535) {
					RSInterface.cache[interfaceId].modelTypeDefault = 0;
					packetOpcode = -1;
					return true;
				} else {
					ItemDefinition itemDef = ItemDefinition.getDefinition(itemId);
					RSInterface.cache[interfaceId].modelTypeDefault = 4;
					RSInterface.cache[interfaceId].modelIdDefault = itemId;
					RSInterface.cache[interfaceId].modelRotationX = itemDef.modelRotationX;
					RSInterface.cache[interfaceId].modelRotationY = itemDef.modelRotationY;
					RSInterface.cache[interfaceId].modelZoom = (itemDef.modelZoom * 100) / itemModelZoom;
					packetOpcode = -1;
					return true;
				}
			}
			if (packetOpcode == 171) {
				boolean hiddenUntilHovered = inStream.getUnsignedByte() == 1;
				int interfaceId = inStream.getUnsignedLEShort();
				RSInterface.cache[interfaceId].hoverOnly = hiddenUntilHovered;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 142) {
				int interfaceId = inStream.getUnsignedShort();
				loadInterface(interfaceId);
				if (chatboxInterfaceId != -1) {
					chatboxInterfaceId = -1;
					redrawChatbox = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					redrawChatbox = true;
				}
				inventoryOverlayInterfaceID = interfaceId;
				redrawTab = true;
				drawTabIcons = true;
				openInterfaceId = -1;
				continuedDialogue = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 126) {
				String text = inStream.getString();
				int interfaceId = inStream.getUnsignedLEShortA();
				RSInterface.cache[interfaceId].textDefault = text;
				if (RSInterface.cache[interfaceId].parentID == tabInterfaceIDs[currentTabId])
					redrawTab = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 206) {
				publicChatMode = inStream.getUnsignedByte();
				privateChatMode = inStream.getUnsignedByte();
				tradeMode = inStream.getUnsignedByte();
				updateChatSettings = true;
				redrawChatbox = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 240) {
				if (currentTabId == 12)
					redrawTab = true;
				playerWeight = inStream.getShort();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 8) {
				int interfaceId = inStream.getUnsignedShortA();
				int interfaceModelId = inStream.getUnsignedLEShort();
				RSInterface.cache[interfaceId].modelTypeDefault = 1;
				RSInterface.cache[interfaceId].modelIdDefault = interfaceModelId;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 122) {
				int interfaceId = inStream.getUnsignedShortA();
				int rgb = inStream.getUnsignedShortA();
				int r = rgb >> 10 & 0x1f;
				int g = rgb >> 5 & 0x1f;
				int b = rgb & 0x1f;
				RSInterface.cache[interfaceId].colourDefault = (r << 19) + (g << 11) + (b << 3);
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 53) {
				redrawTab = true;
				int interfaceId = inStream.getUnsignedLEShort();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				int itemCount = inStream.getUnsignedLEShort();
				for (int item = 0; item < itemCount; item++) {
					int stackSize = inStream.getUnsignedByte();
					if (stackSize == 255)
						stackSize = inStream.getMEBInt();
					rsInterface.inventoryItemId[item] = inStream.getUnsignedShortA();
					rsInterface.inventoryStackSize[item] = stackSize;
				}
				for (int j25 = itemCount; j25 < rsInterface.inventoryItemId.length; j25++) {
					rsInterface.inventoryItemId[j25] = 0;
					rsInterface.inventoryStackSize[j25] = 0;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 230) {
				int modelZoom = inStream.getUnsignedLEShortA();
				int interfaceId = inStream.getUnsignedLEShort();
				int modelRotationX = inStream.getUnsignedLEShort();
				int modelRotationY = inStream.getUnsignedShortA();
				RSInterface.cache[interfaceId].modelRotationX = modelRotationX;
				RSInterface.cache[interfaceId].modelRotationY = modelRotationY;
				RSInterface.cache[interfaceId].modelZoom = modelZoom;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 221) {
				friendListStatus = inStream.getUnsignedByte();
				redrawTab = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 177) {
				cutsceneActive = true;
				anInt995 = inStream.getUnsignedByte();
				anInt996 = inStream.getUnsignedByte();
				cameraOffsetZ = inStream.getUnsignedLEShort();
				anInt998 = inStream.getUnsignedByte();
				anInt999 = inStream.getUnsignedByte();
				if (anInt999 >= 100) {
					int x = anInt995 * 128 + 64;
					int y = anInt996 * 128 + 64;
					int z = getFloorDrawHeight(plane, y, x) - cameraOffsetZ;
					int distanceX = x - cameraPositionX;
					int distanceZ = z - cameraPositionZ;
					int distanceY = y - cameraPositionY;
					int distanceScalar = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
					cameraVerticalRotation = (int) (Math.atan2(distanceZ, distanceScalar) * 325.94900000000001D)
							& 0x7ff;
					cameraHorizontalRotation = (int) (Math.atan2(distanceX, distanceY) * -325.94900000000001D) & 0x7ff;
					if (cameraVerticalRotation < 128)
						cameraVerticalRotation = 128;
					if (cameraVerticalRotation > 383)
						cameraVerticalRotation = 383;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 249) {
				membershipStatus = inStream.getUnsignedByteA();
				playerListId = inStream.getUnsignedShortA();
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 65) {
				updateNPCs(inStream, packetSize);
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 27) {
				messagePromptRaised = false;
				inputDialogState = 1;
				amountOrNameInput = "";
				redrawChatbox = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 187) {
				messagePromptRaised = false;
				inputDialogState = 2;
				amountOrNameInput = "";
				redrawChatbox = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 97) {
				int interfaceId = inStream.getUnsignedLEShort();
				loadInterface(interfaceId);
				if (inventoryOverlayInterfaceID != -1) {
					inventoryOverlayInterfaceID = -1;
					redrawTab = true;
					drawTabIcons = true;
				}
				if (chatboxInterfaceId != -1) {
					chatboxInterfaceId = -1;
					redrawChatbox = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					redrawChatbox = true;
				}
				openInterfaceId = interfaceId;
				continuedDialogue = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 218) {
				int interfaceId = inStream.getSignedLEShortA();
				dialogID = interfaceId;
				redrawChatbox = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 87) {
				int settingId = inStream.getUnsignedShort();
				int settingValue = inStream.getMESInt();
				defaultSettings[settingId] = settingValue;
				if (interfaceSettings[settingId] != settingValue) {
					interfaceSettings[settingId] = settingValue;
					handleInterfaceSetting(settingId);
					redrawTab = true;
					if (dialogID != -1)
						redrawChatbox = true;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 36) {
				int settingId = inStream.getUnsignedShort();
				byte settingValue = inStream.get();
				defaultSettings[settingId] = settingValue;
				if (interfaceSettings[settingId] != settingValue) {
					interfaceSettings[settingId] = settingValue;
					handleInterfaceSetting(settingId);
					redrawTab = true;
					if (dialogID != -1)
						redrawChatbox = true;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 61) {
				multiCombatZone = inStream.getUnsignedByte() == 1;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 200) {
				int interfaceId = inStream.getUnsignedLEShort();
				int animationId = inStream.getShort();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				rsInterface.animationIdDefault = animationId;
				if (animationId == -1) {
					rsInterface.animationFrame = 0;
					rsInterface.animationDuration = 0;
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 219) {
				if (inventoryOverlayInterfaceID != -1) {
					inventoryOverlayInterfaceID = -1;
					redrawTab = true;
					drawTabIcons = true;
				}
				if (chatboxInterfaceId != -1) {
					chatboxInterfaceId = -1;
					redrawChatbox = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					redrawChatbox = true;
				}
				openInterfaceId = -1;
				continuedDialogue = false;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 34) {
				redrawTab = true;
				int interfaceId = inStream.getUnsignedLEShort();
				RSInterface rsInterface = RSInterface.cache[interfaceId];
				while (inStream.position < packetSize) {
					int itemSlot = inStream.getSmartB();
					int itemId = inStream.getUnsignedLEShort();
					int itemAmount = inStream.getUnsignedByte();
					if (itemAmount == 255)
						itemAmount = inStream.getInt();
					if (itemSlot >= 0 && itemSlot < rsInterface.inventoryItemId.length) {
						rsInterface.inventoryItemId[itemSlot] = itemId;
						rsInterface.inventoryStackSize[itemSlot] = itemAmount;
					}
				}
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 105 || packetOpcode == 84 || packetOpcode == 147 || packetOpcode == 215
					|| packetOpcode == 4 || packetOpcode == 117 || packetOpcode == 156 || packetOpcode == 44
					|| packetOpcode == 160 || packetOpcode == 101 || packetOpcode == 151) {
				parseGroupPacket(inStream, packetOpcode);
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 106) {
				currentTabId = inStream.getUnsignedByteC();
				redrawTab = true;
				drawTabIcons = true;
				packetOpcode = -1;
				return true;
			}
			if (packetOpcode == 164) {
				int interfaceId = inStream.getUnsignedShort();
				loadInterface(interfaceId);
				if (inventoryOverlayInterfaceID != -1) {
					inventoryOverlayInterfaceID = -1;
					redrawTab = true;
					drawTabIcons = true;
				}
				chatboxInterfaceId = interfaceId;
				redrawChatbox = true;
				openInterfaceId = -1;
				continuedDialogue = false;
				packetOpcode = -1;
				return true;
			}
			signlink.reporterror("T1 - " + packetOpcode + "," + packetSize + " - " + secondMostRecentOpcode + ","
					+ thirdMostRecentOpcode);
			logout();
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			String s2 = "T2 - " + packetOpcode + "," + secondMostRecentOpcode + "," + thirdMostRecentOpcode + " - "
					+ packetSize + "," + (baseX + localPlayer.waypointX[0]) + "," + (baseY + localPlayer.waypointY[0])
					+ " - ";
			for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
				s2 = s2 + inStream.buffer[j15] + ",";

			signlink.reporterror(s2);
			logout();
		}
		return true;
	}

	private void handleInterfaceSetting(int s) {
		int attribute = Varp.cache[s].type;
		if (attribute == 0)
			return;
		int setting = interfaceSettings[s];
		if (attribute == 1) {
			// Brightness
			if (setting == 1)
				Rasterizer.calculatePalette(0.90000000000000002D);
			if (setting == 2)
				Rasterizer.calculatePalette(0.80000000000000004D);
			if (setting == 3)
				Rasterizer.calculatePalette(0.69999999999999996D);
			if (setting == 4)
				Rasterizer.calculatePalette(0.59999999999999998D);
			ItemDefinition.spriteCache.unlinkAll();
			welcomeScreenRaised = true;
		}
		if (attribute == 3) {
			// Music volume
			boolean originalMusicEnabled = musicEnabled;
			if (setting == 0) {
				adjustVolume(musicEnabled, 0);
				musicEnabled = true;
			}
			if (setting == 1) {
				adjustVolume(musicEnabled, -400);
				musicEnabled = true;
			}
			if (setting == 2) {
				adjustVolume(musicEnabled, -800);
				musicEnabled = true;
			}
			if (setting == 3) {
				adjustVolume(musicEnabled, -1200);
				musicEnabled = true;
			}
			if (setting == 4)
				musicEnabled = false;
			if (musicEnabled != originalMusicEnabled && !lowMemory) {
				if (musicEnabled) {
					nextSong = currentSong;
					songChanging = true;
					onDemandFetcher.request(2, nextSong);
				} else {
					stopMidi();
				}
				prevSong = 0;
			}
		}
		if (attribute == 4) {

			if (setting == 0) {
				effectsEnabled = true;
				setWaveVolume(0);
			}
			if (setting == 1) {
				effectsEnabled = true;
				setWaveVolume(-400);
			}
			if (setting == 2) {
				effectsEnabled = true;
				setWaveVolume(-800);
			}
			if (setting == 3) {
				effectsEnabled = true;
				setWaveVolume(-1200);
			}
			if (setting == 4)
				effectsEnabled = false;
		}
		if (attribute == 5)
			oneMouseButton = setting;
		if (attribute == 6)
			chatEffectsDisabled = setting;
		if (attribute == 8) {
			splitPrivateChat = setting;
			redrawChatbox = true;
		}
		if (attribute == 9)
			bankInsertMode = setting;
	}

	private void handleMusic() {
		for (int track = 0; track < trackCount; track++)
			if (trackDelay[track] <= 0) {
				boolean moveToNextSong = false;
				try {
					if (trackIds[track] == currentTrackId && trackLoop[track] == currentTrackLoop) {
						if (!replayWave())
							moveToNextSong = true;
					} else {
						Buffer stream = Effect.data(trackLoop[track], trackIds[track]);
						if (System.currentTimeMillis() + stream.position / 22 > songStartTime + songStartOffset / 22) {
							songStartOffset = stream.position;
							songStartTime = System.currentTimeMillis();
							if (saveWave(stream.buffer, stream.position)) {
								currentTrackId = trackIds[track];
								currentTrackLoop = trackLoop[track];
							} else {
								moveToNextSong = true;
							}
						}
					}
				} catch (Exception exception) {
				}
				if (!moveToNextSong || trackDelay[track] == -5) {
					trackCount--;
					for (int _track = track; _track < trackCount; _track++) {
						trackIds[_track] = trackIds[_track + 1];
						trackLoop[_track] = trackLoop[_track + 1];
						trackDelay[_track] = trackDelay[_track + 1];
					}

					track--;
				} else {
					trackDelay[track] = -5;
				}
			} else {
				trackDelay[track]--;
			}

		if (prevSong > 0) {
			prevSong -= 20;
			if (prevSong < 0)
				prevSong = 0;
			if (prevSong == 0 && musicEnabled && !lowMemory) {
				nextSong = currentSong;
				songChanging = true;
				onDemandFetcher.request(2, nextSong);
			}
		}
	}

	@Override
	public void init() {
		localWorldId = Integer.parseInt(getParameter("nodeid"));
		portOffset = Integer.parseInt(getParameter("portoff"));
		String lowMemory = getParameter("lowmem");
		if (lowMemory != null && lowMemory.equals("1"))
			setLowMemory();
		else
			setHighMem();
		String freeWorld = getParameter("free");
		membersWorld = !(freeWorld != null && freeWorld.equals("1"));
		initClientFrame(765, 503);
	}

	private int initialiseRegionLoading() {
		for (int t = 0; t < terrainData.length; t++) {
			if (terrainData[t] == null && terrainDataIds[t] != -1)
				return -1;
			if (objectData[t] == null && objectDataIds[t] != -1)
				return -2;
		}

		boolean regionsCached = true;
		for (int region = 0; region < terrainData.length; region++) {
			byte objects[] = objectData[region];
			if (objects != null) {
				int blockX = (mapCoordinates[region] >> 8) * 64 - baseX;
				int blockY = (mapCoordinates[region] & 0xff) * 64 - baseY;
				if (loadGeneratedMap) {
					blockX = 10;
					blockY = 10;
				}
				regionsCached &= Region.regionCached(blockX, blockY, objects);
			}
		}

		if (!regionsCached)
			return -3;
		if (loadingMap) {
			return -4;
		} else {
			loadingStage = 2;
			Region.plane = plane;
			loadRegion();
			stream.putOpcode(121);
			return 0;
		}
	}

	private String interfaceIntToString(int value) {
		if (value < 999999999)
			return String.valueOf(value);
		else
			return "*";
	}

	private boolean interfaceIsActive(RSInterface rsInterface) {
		if (rsInterface.conditionType == null)
			return false;
		for (int c = 0; c < rsInterface.conditionType.length; c++) {
			int opcode = parseInterfaceOpcode(rsInterface, c);
			int value = rsInterface.conditionValue[c];
			if (rsInterface.conditionType[c] == 2) {
				if (opcode >= value)
					return false;
			} else if (rsInterface.conditionType[c] == 3) {
				if (opcode <= value)
					return false;
			} else if (rsInterface.conditionType[c] == 4) {
				if (opcode == value)
					return false;
			} else if (opcode != value)
				return false;
		}

		return true;
	}

	private boolean isFriendOrSelf(String name) {
		if (name == null)
			return false;
		for (int i = 0; i < friendsCount; i++)
			if (name.equalsIgnoreCase(friendsList[i]))
				return true;
		return name.equalsIgnoreCase(localPlayer.name);
	}

	private void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	private void loadingStages() {
		if (lowMemory && loadingStage == 2 && Region.plane != plane) {
			gameScreenImageProducer.initDrawingArea();
			fontPlain.drawCentredText("Loading - please wait.", 257, 151, 0);
			fontPlain.drawCentredText("Loading - please wait.", 256, 150, 0xffffff);
			gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
			loadingStage = 1;
			loadRegionTime = System.currentTimeMillis();
		}
		if (loadingStage == 1) {
			int successful = initialiseRegionLoading();
			if (successful != 0 && System.currentTimeMillis() - loadRegionTime > 360000L) {
				signlink.reporterror(enteredUsername + " glcfb " + serverSessionKey + "," + successful + "," + lowMemory
						+ "," + caches[0] + "," + onDemandFetcher.immediateRequestCount() + "," + plane + "," + regionX
						+ "," + regionY);
				loadRegionTime = System.currentTimeMillis();
			}
		}
		if (loadingStage == 2 && plane != lastRegionId) {
			lastRegionId = plane;
			renderMinimap(plane);
		}
	}

	private void loadInterface(int i) {
		RSInterface rsInterface = RSInterface.cache[i];
		for (int j = 0; j < rsInterface.children.length; j++) {
			if (rsInterface.children[j] == -1)
				break;
			RSInterface child = RSInterface.cache[rsInterface.children[j]];
			if (child.type == 1)
				loadInterface(child.id);
			child.animationFrame = 0;
			child.animationDuration = 0;
		}
	}

	private void loadRegion() {
		try {
			lastRegionId = -1;
			stationaryGraphicQueue.removeAll();
			projectileQueue.removeAll();
			Rasterizer.clearTextureCache();
			resetModelCaches();
			worldController.initToNull();
			System.gc();
			for (int z = 0; z < 4; z++)
				currentCollisionMap[z].reset();

			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 104; x++) {
					for (int y = 0; y < 104; y++)
						tileFlags[z][x][y] = 0;

				}

			}

			Region objectManager = new Region(tileFlags, intGroundArray);
			int dataLength = terrainData.length;
			stream.putOpcode(0);
			if (!loadGeneratedMap) {
				for (int pointer = 0; pointer < dataLength; pointer++) {
					int offsetX = (mapCoordinates[pointer] >> 8) * 64 - baseX;
					int offsetY = (mapCoordinates[pointer] & 0xff) * 64 - baseY;
					byte data[] = terrainData[pointer];
					if (data != null)
						objectManager.loadTerrainBlock(data, offsetY, offsetX, (regionX - 6) * 8, (regionY - 6) * 8,
								currentCollisionMap);
				}

				for (int pointer = 0; pointer < dataLength; pointer++) {
					int offsetX = (mapCoordinates[pointer] >> 8) * 64 - baseX;
					int offsetY = (mapCoordinates[pointer] & 0xff) * 64 - baseY;
					byte data[] = terrainData[pointer];
					if (data == null && regionY < 800)
						objectManager.initiateVertexHeights(offsetY, 64, 64, offsetX);
				}
				stream.putOpcode(0);
				for (int region = 0; region < dataLength; region++) {
					byte data[] = objectData[region];
					if (data != null) {
						int offsetX = (mapCoordinates[region] >> 8) * 64 - baseX;
						int offsetY = (mapCoordinates[region] & 0xff) * 64 - baseY;
						objectManager.loadObjectBlock(offsetX, currentCollisionMap, offsetY, worldController, data);
					}
				}

			}
			if (loadGeneratedMap) {
				for (int z = 0; z < 4; z++) {
					for (int x = 0; x < 13; x++) {
						for (int y = 0; y < 13; y++) {
							int data = constructMapTiles[z][x][y];
							if (data != -1) {
								int tileZ = data >> 24 & 3;
								int tileRotation = data >> 1 & 3;
								int tileX = data >> 14 & 0x3ff;
								int tileY = data >> 3 & 0x7ff;
								int tileCoordinates = (tileX / 8 << 8) + tileY / 8;
								for (int pointer = 0; pointer < mapCoordinates.length; pointer++) {
									if (mapCoordinates[pointer] != tileCoordinates || terrainData[pointer] == null)
										continue;
									objectManager.loadTerrainSubblock(tileZ, tileRotation, currentCollisionMap, x * 8,
											(tileX & 7) * 8, terrainData[pointer], (tileY & 7) * 8, z, y * 8);
									break;
								}

							}
						}

					}

				}

				for (int x = 0; x < 13; x++) {
					for (int y = 0; y < 13; y++) {
						int displayMap = constructMapTiles[0][x][y];
						if (displayMap == -1)
							objectManager.initiateVertexHeights(y * 8, 8, 8, x * 8);
					}

				}

				stream.putOpcode(0);
				for (int z = 0; z < 4; z++) {
					for (int x = 0; x < 13; x++) {
						for (int y = 0; y < 13; y++) {
							int bits = constructMapTiles[z][x][y];
							if (bits != -1) {
								int tileZ = bits >> 24 & 3;
								int tileRotation = bits >> 1 & 3;
								int tileX = bits >> 14 & 0x3ff;
								int tileY = bits >> 3 & 0x7ff;
								int tileCoorindates = (tileX / 8 << 8) + tileY / 8;
								for (int pointer = 0; pointer < mapCoordinates.length; pointer++) {
									if (mapCoordinates[pointer] != tileCoorindates || objectData[pointer] == null)
										continue;
									objectManager.loadObjectSubblock(currentCollisionMap, worldController, tileZ, x * 8,
											(tileY & 7) * 8, z, objectData[pointer], (tileX & 7) * 8, tileRotation,
											y * 8);
									break;
								}

							}
						}

					}

				}

			}
			stream.putOpcode(0);
			objectManager.createRegion(currentCollisionMap, worldController);
			gameScreenImageProducer.initDrawingArea();
			stream.putOpcode(0);
			int z = Region.lowestPlane;
			if (z > plane)
				z = plane;
			if (z < plane - 1)
				z = plane - 1;
			if (lowMemory)
				worldController.setHeightLevel(Region.lowestPlane);
			else
				worldController.setHeightLevel(0);
			for (int x = 0; x < 104; x++) {
				for (int y = 0; y < 104; y++)
					spawnGroundItem(x, y);

			}

			loadedRegions++;
			if (loadedRegions > 98) {
				loadedRegions = 0;
				stream.putOpcode(150);
			}
			clearObjectSpawnRequests();
		} catch (Exception exception) {
		}
		GameObjectDefinition.modelCache.unlinkAll();
		if (super.gameFrame != null) {
			stream.putOpcode(210);
			stream.putInt(0x3f008edd);
		}
		if (lowMemory && signlink.cache_dat != null) {
			int modelCount = onDemandFetcher.fileCount(0);
			for (int model = 0; model < modelCount; model++) {
				int modelIndex = onDemandFetcher.getModelId(model);
				if ((modelIndex & 0x79) == 0)
					Model.resetModel(model);
			}

		}
		System.gc();
		Rasterizer.resetTextures();
		onDemandFetcher.clearPassiveRequests();
		int x1 = (regionX - 6) / 8 - 1;
		int x2 = (regionX + 6) / 8 + 1;
		int y1 = (regionY - 6) / 8 - 1;
		int y2 = (regionY + 6) / 8 + 1;
		if (inTutorialIsland) {
			x1 = 49;
			x2 = 50;
			y1 = 49;
			y2 = 50;
		}
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++)
				if (x == x1 || x == x2 || y == y1 || y == y2) {
					int mapIndex1 = onDemandFetcher.getMapId(0, x, y);
					if (mapIndex1 != -1)
						onDemandFetcher.passiveRequest(mapIndex1, 3);
					int mapIndex2 = onDemandFetcher.getMapId(1, x, y);
					if (mapIndex2 != -1)
						onDemandFetcher.passiveRequest(mapIndex2, 3);
				}

		}

	}

	private void loadTitleScreen() {
		titleBoxImage = new Background(archiveTitle, "titlebox", 0);
		titleButtonImage = new Background(archiveTitle, "titlebutton", 0);
		flameRuneImage = new Background[12];
		int icon = 0;
		try {
			icon = Integer.parseInt(getParameter("fl_icon"));
		} catch (Exception _ex) {
		}
		if (icon == 0) {
			for (int r = 0; r < 12; r++)
				flameRuneImage[r] = new Background(archiveTitle, "runes", r);

		} else {
			for (int r = 0; r < 12; r++)
				flameRuneImage[r] = new Background(archiveTitle, "runes", 12 + (r & 3));

		}
		flameLeftBackground2 = new Sprite(128, 265);
		flameRightBackground2 = new Sprite(128, 265);
		System.arraycopy(flameLeftBackground.pixels, 0, flameLeftBackground2.pixels, 0, 33920);

		System.arraycopy(flameRightBackground.pixels, 0, flameRightBackground2.pixels, 0, 33920);

		flameColour1 = new int[256];
		for (int c = 0; c < 64; c++)
			flameColour1[c] = c * 0x40000;

		for (int c = 0; c < 64; c++)
			flameColour1[c + 64] = 0xff0000 + 1024 * c;

		for (int c = 0; c < 64; c++)
			flameColour1[c + 128] = 0xffff00 + 4 * c;

		for (int c = 0; c < 64; c++)
			flameColour1[c + 192] = 0xffffff;

		flameColour2 = new int[256];
		for (int c = 0; c < 64; c++)
			flameColour2[c] = c * 1024;

		for (int c = 0; c < 64; c++)
			flameColour2[c + 64] = 65280 + 4 * c;

		for (int c = 0; c < 64; c++)
			flameColour2[c + 128] = 65535 + 0x40000 * c;

		for (int c = 0; c < 64; c++)
			flameColour2[c + 192] = 0xffffff;

		flameColour3 = new int[256];
		for (int c = 0; c < 64; c++)
			flameColour3[c] = c * 4;

		for (int c = 0; c < 64; c++)
			flameColour3[c + 64] = 255 + 0x40000 * c;

		for (int c = 0; c < 64; c++)
			flameColour3[c + 128] = 0xff00ff + 1024 * c;

		for (int c = 0; c < 64; c++)
			flameColour3[c + 192] = 0xffffff;

		currentFlameColours = new int[256];
		anIntArray1190 = new int[32768];
		anIntArray1191 = new int[32768];
		randomizeBackground(null);
		anIntArray828 = new int[32768];
		anIntArray829 = new int[32768];
		drawLoadingText(10, "Connecting to fileserver");
		if (!currentlyDrawingFlames) {
			drawFlames = true;
			currentlyDrawingFlames = true;
			startRunnable(this, 2);
		}
	}

	private void login(String playerUsername, String playerPassword, boolean recoveredConnection) {
		signlink.errorname = playerUsername;
		try {
			if (!recoveredConnection) {
				loginMessage1 = "";
				loginMessage2 = "Connecting to server...";
				drawLoginScreen(true);
			}
			socket = new RSSocket(this, openSocket(43594 + portOffset));
			long nameLong = TextClass.nameToLong(playerUsername);
			int nameHash = (int) (nameLong >> 16 & 31L);
			stream.position = 0;
			stream.put(14);
			stream.put(nameHash);
			socket.write(2, stream.buffer);
			for (int ignoredByte = 0; ignoredByte < 8; ignoredByte++)
				socket.read();

			int responseCode = socket.read();
			int initialResponseCode = responseCode;
			if (responseCode == 0) {
				socket.read(inStream.buffer, 8);
				inStream.position = 0;
				serverSessionKey = inStream.getLong();
				int seed[] = new int[4];
				seed[0] = (int) (Math.random() * 99999999D);
				seed[1] = (int) (Math.random() * 99999999D);
				seed[2] = (int) (serverSessionKey >> 32);
				seed[3] = (int) serverSessionKey;
				stream.position = 0;
				stream.put(10);
				stream.putInt(seed[0]);
				stream.putInt(seed[1]);
				stream.putInt(seed[2]);
				stream.putInt(seed[3]);
				stream.putInt(signlink.uid);
				stream.putString(playerUsername);
				stream.putString(playerPassword);
				stream.generateKeys();
				loginStream.position = 0;
				if (recoveredConnection)
					loginStream.put(18);
				else
					loginStream.put(16);
				loginStream.put(stream.position + 40);
				loginStream.put(255);
				loginStream.putShort(317);
				loginStream.put(lowMemory ? 1 : 0);
				for (int crc = 0; crc < 9; crc++)
					loginStream.putInt(expectedCRCs[crc]);

				loginStream.putBytes(stream.buffer, stream.position, 0);
				stream.encryptor = new ISAACRandomGenerator(seed);
				for (int index = 0; index < 4; index++)
					seed[index] += 50;

				encryption = new ISAACRandomGenerator(seed);
				socket.write(loginStream.position, loginStream.buffer);
				responseCode = socket.read();
			}
			if (responseCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception _ex) {
				}
				login(playerUsername, playerPassword, recoveredConnection);
				return;
			}
			if (responseCode == 2) {
				playerRights = socket.read();
				flagged = socket.read() == 1;
				lastClickTime = 0L;
				sameClickPositionCounter = 0;
				mouseDetection.coordsIndex = 0;
				super.awtFocus = true;
				windowFocused = true;
				loggedIn = true;
				stream.position = 0;
				inStream.position = 0;
				packetOpcode = -1;
				mostRecentOpcode = -1;
				secondMostRecentOpcode = -1;
				thirdMostRecentOpcode = -1;
				packetSize = 0;
				anInt1009 = 0;
				systemUpdateTime = 0;
				idleLogout = 0;
				hintIconType = 0;
				menuActionRow = 0;
				menuOpen = false;
				super.idleTime = 0;
				for (int m = 0; m < 100; m++)
					chatMessages[m] = null;

				itemSelected = false;
				spellSelected = false;
				loadingStage = 0;
				trackCount = 0;
				cameraRandomisationH = (int) (Math.random() * 100D) - 50;
				cameraRandomisationV = (int) (Math.random() * 110D) - 55;
				cameraRandomisationA = (int) (Math.random() * 80D) - 40;
				minimapRotation = (int) (Math.random() * 120D) - 60;
				minimapZoom = (int) (Math.random() * 30D) - 20;
				cameraHorizontal = (int) (Math.random() * 20D) - 10 & 0x7ff;
				minimapState = 0;
				lastRegionId = -1;
				destinationX = 0;
				destinationY = 0;
				localPlayerCount = 0;
				npcCount = 0;
				for (int p = 0; p < MAX_ENTITY_COUNT; p++) {
					players[p] = null;
					playerAppearanceData[p] = null;
				}

				for (int n = 0; n < 16384; n++)
					npcs[n] = null;

				localPlayer = players[LOCAL_PLAYER_ID] = new Player();
				projectileQueue.removeAll();
				stationaryGraphicQueue.removeAll();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundArray[l2][i3][k3] = null;

					}

				}

				spawnObjectList = new NodeList();
				friendListStatus = 0;
				friendsCount = 0;
				dialogID = -1;
				chatboxInterfaceId = -1;
				openInterfaceId = -1;
				inventoryOverlayInterfaceID = -1;
				walkableInterfaceId = -1;
				continuedDialogue = false;
				currentTabId = 3;
				inputDialogState = 0;
				menuOpen = false;
				messagePromptRaised = false;
				clickToContinueString = null;
				multiCombatZone = false;
				flashingSidebar = -1;
				characterEditChangeGender = true;
				changeGender();
				for (int c = 0; c < 5; c++)
					characterEditColours[c] = 0;

				for (int a = 0; a < 5; a++) {
					playerActionText[a] = null;
					playerActionUnpinned[a] = false;
				}
				currentWalkingQueueSize = 0;
				setupGameplayScreen();
				return;
			}
			if (responseCode == 3) {
				loginMessage1 = "";
				loginMessage2 = "Invalid username or password.";
				return;
			}
			if (responseCode == 4) {
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Please check your message-center for details.";
				return;
			}
			if (responseCode == 5) {
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 secs...";
				return;
			}
			if (responseCode == 6) {
				loginMessage1 = "RuneScape has been updated!";
				loginMessage2 = "Please reload this page.";
				return;
			}
			if (responseCode == 7) {
				loginMessage1 = "This world is full.";
				loginMessage2 = "Please use a different world.";
				return;
			}
			if (responseCode == 8) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Login server offline.";
				return;
			}
			if (responseCode == 9) {
				loginMessage1 = "Login limit exceeded.";
				loginMessage2 = "Too many connections from your address.";
				return;
			}
			if (responseCode == 10) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Bad session id.";
				return;
			}
			if (responseCode == 11) {
				loginMessage2 = "Login server rejected session.";
				loginMessage2 = "Please try again.";
				return;
			}
			if (responseCode == 12) {
				loginMessage1 = "You need a members account to login to this world.";
				loginMessage2 = "Please subscribe, or use a different world.";
				return;
			}
			if (responseCode == 13) {
				loginMessage1 = "Could not complete login.";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if (responseCode == 14) {
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (responseCode == 15) {
				loggedIn = true;
				stream.position = 0;
				inStream.position = 0;
				packetOpcode = -1;
				mostRecentOpcode = -1;
				secondMostRecentOpcode = -1;
				thirdMostRecentOpcode = -1;
				packetSize = 0;
				anInt1009 = 0;
				systemUpdateTime = 0;
				menuActionRow = 0;
				menuOpen = false;
				loadRegionTime = System.currentTimeMillis();
				return;
			}
			if (responseCode == 16) {
				loginMessage1 = "Login attempts exceeded.";
				loginMessage2 = "Please wait 1 minute and try again.";
				return;
			}
			if (responseCode == 17) {
				loginMessage1 = "You are standing in a members-only area.";
				loginMessage2 = "To play on this world move to a free area first";
				return;
			}
			if (responseCode == 20) {
				loginMessage1 = "Invalid loginserver requested";
				loginMessage2 = "Please try using a different world.";
				return;
			}
			if (responseCode == 21) {
				for (int s = socket.read(); s >= 0; s--) {
					loginMessage1 = "You have only just left another world";
					loginMessage2 = "Your profile will be transferred in: " + s + " seconds";
					drawLoginScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(playerUsername, playerPassword, recoveredConnection);
				return;
			}
			if (responseCode == -1) {
				if (initialResponseCode == 0) {
					if (loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch (Exception _ex) {
						}
						loginFailures++;
						login(playerUsername, playerPassword, recoveredConnection);
						return;
					} else {
						loginMessage1 = "No response from loginserver";
						loginMessage2 = "Please wait 1 minute and try again.";
						return;
					}
				} else {
					loginMessage1 = "No response from server";
					loginMessage2 = "Please try using a different world.";
					return;
				}
			} else {
				System.out.println("response:" + responseCode);
				loginMessage1 = "Unexpected server response";
				loginMessage2 = "Please try using a different world.";
				return;
			}
		} catch (IOException _ex) {
			loginMessage1 = "";
		}
		loginMessage2 = "Error connecting to server.";
	}

	private void logout() {
		try {
			if (socket != null)
				socket.close();
		} catch (Exception _ex) {
		}
		socket = null;
		loggedIn = false;
		loginScreenState = 0;
		// myUsername = "";
		// myPassword = "";
		resetModelCaches();
		worldController.initToNull();
		for (int i = 0; i < 4; i++)
			currentCollisionMap[i].reset();

		System.gc();
		stopMidi();
		currentSong = -1;
		nextSong = -1;
		prevSong = 0;
	}

	private void updateGame() {
		if (systemUpdateTime > 1)
			systemUpdateTime--;

		if (idleLogout > 0)
			idleLogout--;

		for (int j = 0; j < 5; j++)
			if (!handleIncomingData())
				break;

		if (!loggedIn)
			return;

		synchronized (mouseDetection.syncObject) {
			if (flagged) {
				if (super.clickType != 0 || mouseDetection.coordsIndex >= 40) {
					stream.putOpcode(45);
					stream.put(0);
					int originalOffset = stream.position;
					int coordinateCount = 0;
					for (int c = 0; c < mouseDetection.coordsIndex; c++) {
						if (originalOffset - stream.position >= 240)
							break;
						coordinateCount++;
						int y = mouseDetection.coordsY[c];
						if (y < 0)
							y = 0;
						else if (y > 502)
							y = 502;
						int x = mouseDetection.coordsX[c];
						if (x < 0)
							x = 0;
						else if (x > 764)
							x = 764;
						int pixelOffset = y * 765 + x;
						if (mouseDetection.coordsY[c] == -1 && mouseDetection.coordsX[c] == -1) {
							x = -1;
							y = -1;
							pixelOffset = 0x7ffff;
						}
						if (x == lastClickX && y == lastClickY) {
							if (sameClickPositionCounter < 2047)
								sameClickPositionCounter++;
						} else {
							int differenceX = x - lastClickX;
							lastClickX = x;
							int differenceY = y - lastClickY;
							lastClickY = y;
							if (sameClickPositionCounter < 8 && differenceX >= -32 && differenceX <= 31
									&& differenceY >= -32 && differenceY <= 31) {
								differenceX += 32;
								differenceY += 32;
								stream.putShort((sameClickPositionCounter << 12) + (differenceX << 6) + differenceY);
								sameClickPositionCounter = 0;
							} else if (sameClickPositionCounter < 8) {
								stream.put24BitInt(0x800000 + (sameClickPositionCounter << 19) + pixelOffset);
								sameClickPositionCounter = 0;
							} else {
								stream.putInt(0xc0000000 + (sameClickPositionCounter << 19) + pixelOffset);
								sameClickPositionCounter = 0;
							}
						}
					}

					stream.putSizeByte(stream.position - originalOffset);
					if (coordinateCount >= mouseDetection.coordsIndex) {
						mouseDetection.coordsIndex = 0;
					} else {
						mouseDetection.coordsIndex -= coordinateCount;
						for (int c = 0; c < mouseDetection.coordsIndex; c++) {
							mouseDetection.coordsX[c] = mouseDetection.coordsX[c + coordinateCount];
							mouseDetection.coordsY[c] = mouseDetection.coordsY[c + coordinateCount];
						}

					}
				}
			} else {
				mouseDetection.coordsIndex = 0;
			}
		}
		if (super.clickType != 0) {
			long timeBetweenClicks = (super.clickTime - lastClickTime) / 50L;
			if (timeBetweenClicks > 4095L)
				timeBetweenClicks = 4095L;
			lastClickTime = super.clickTime;
			int y = super.clickY;
			if (y < 0)
				y = 0;
			else if (y > 502)
				y = 502;
			int x = super.clickX;
			if (x < 0)
				x = 0;
			else if (x > 764)
				x = 764;
			int pixelOffset = y * 765 + x;
			int rightClick = 0;
			if (super.clickType == 2)
				rightClick = 1;
			int timeDifference = (int) timeBetweenClicks;
			stream.putOpcode(241);
			stream.putInt((timeDifference << 20) + (rightClick << 19) + pixelOffset);
		}
		if (cameraMovedWriteDelay > 0)
			cameraMovedWriteDelay--;
		if (super.keyStatus[1] == 1 || super.keyStatus[2] == 1 || super.keyStatus[3] == 1 || super.keyStatus[4] == 1)
			cameraMovedWrite = true;
		if (cameraMovedWrite && cameraMovedWriteDelay <= 0) {
			cameraMovedWriteDelay = 20;
			cameraMovedWrite = false;
			stream.putOpcode(86);
			stream.putShort(cameraVertical);
			stream.putShortA(cameraHorizontal);
		}
		if (super.awtFocus && !windowFocused) {
			windowFocused = true;
			stream.putOpcode(3);
			stream.put(1);
		}
		if (!super.awtFocus && windowFocused) {
			windowFocused = false;
			stream.putOpcode(3);
			stream.put(0);
		}
		loadingStages();
		spawnGameObjects();
		handleMusic();
		anInt1009++;
		if (anInt1009 > 750)
			dropClient();
		updatePlayerInstances();
		updateNPCInstances();
		cycleEntitySpokenText();
		animationTimePassed++;
		if (crossType != 0) {
			crossIndex += 20;
			if (crossIndex >= 400)
				crossType = 0;
		}
		if (atInventoryInterfaceType != 0) {
			atInventoryLoopCycle++;
			if (atInventoryLoopCycle >= 15) {
				if (atInventoryInterfaceType == 2)
					redrawTab = true;
				if (atInventoryInterfaceType == 3)
					redrawChatbox = true;
				atInventoryInterfaceType = 0;
			}
		}
		if (activeInterfaceType != 0) {
			lastItemDragTime++;
			if (super.mouseX > lastMouseX + 5 || super.mouseX < lastMouseX - 5 || super.mouseY > lastMouseY + 5
					|| super.mouseY < lastMouseY - 5)
				lastItemDragged = true;
			if (super.mouseButton == 0) {
				if (activeInterfaceType == 2)
					redrawTab = true;
				if (activeInterfaceType == 3)
					redrawChatbox = true;
				activeInterfaceType = 0;
				if (lastItemDragged && lastItemDragTime >= 5) {
					lastActiveInventoryInterface = -1;
					processRightClick();
					if (lastActiveInventoryInterface == moveItemInterfaceId && moveItemSlotEnd != moveItemSlotStart) {
						RSInterface rsInterface = RSInterface.cache[moveItemInterfaceId];
						int moveItemInsetionMode = 0;
						if (bankInsertMode == 1 && rsInterface.contentType == 206)
							moveItemInsetionMode = 1;
						if (rsInterface.inventoryItemId[moveItemSlotEnd] <= 0)
							moveItemInsetionMode = 0;
						if (rsInterface.itemDeletesDragged) {
							int slotStart = moveItemSlotStart;
							int slotEnd = moveItemSlotEnd;
							rsInterface.inventoryItemId[slotEnd] = rsInterface.inventoryItemId[slotStart];
							rsInterface.inventoryStackSize[slotEnd] = rsInterface.inventoryStackSize[slotStart];
							rsInterface.inventoryItemId[slotStart] = -1;
							rsInterface.inventoryStackSize[slotStart] = 0;
						} else if (moveItemInsetionMode == 1) {
							int slotStart = moveItemSlotStart;
							for (int slotPointer = moveItemSlotEnd; slotStart != slotPointer;)
								if (slotStart > slotPointer) {
									rsInterface.swapInventoryItems(slotStart, slotStart - 1);
									slotStart--;
								} else if (slotStart < slotPointer) {
									rsInterface.swapInventoryItems(slotStart, slotStart + 1);
									slotStart++;
								}

						} else {
							rsInterface.swapInventoryItems(moveItemSlotStart, moveItemSlotEnd);
						}
						stream.putOpcode(214);
						stream.putLEShortA(moveItemInterfaceId);
						stream.putByteC(moveItemInsetionMode);
						stream.putLEShortA(moveItemSlotStart);
						stream.putLEShort(moveItemSlotEnd);
					}
				} else if ((oneMouseButton == 1 || menuRowIsAddFriend(menuActionRow - 1)) && menuActionRow > 2)
					processMenuHovering();
				else if (menuActionRow > 0)
					doAction(menuActionRow - 1);
				atInventoryLoopCycle = 10;
				super.clickType = 0;
			}
		}
		if (WorldController.clickedTileX != -1) {
			int x = WorldController.clickedTileX;
			int y = WorldController.clickedTileY;
			boolean walkable = doWalkTo(0, 0, 0, 0, localPlayer.waypointY[0], 0, 0, y, localPlayer.waypointX[0], true,
					x);
			WorldController.clickedTileX = -1;
			if (walkable) {
				crossX = super.clickX;
				crossY = super.clickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if (super.clickType == 1 && clickToContinueString != null) {
			clickToContinueString = null;
			redrawChatbox = true;
			super.clickType = 0;
		}
		processMenuClick();
		processMinimapClick();
		processTabClick();
		processChatModeClick();
		if (super.mouseButton == 1 || super.clickType == 1)
			anInt1213++;
		if (loadingStage == 2)
			setStandardCameraPosition();
		if (loadingStage == 2 && cutsceneActive)
			setCutsceneCamera();
		for (int camera = 0; camera < 5; camera++)
			unknownCameraVariable[camera]++;

		manageTextInput();
		super.idleTime++;
		if (super.idleTime > 4500) {
			idleLogout = 250;
			super.idleTime -= 500;
			stream.putOpcode(202);
		}
		cameraRandomisationCounter++;
		if (cameraRandomisationCounter > 500) {
			cameraRandomisationCounter = 0;
			int type = (int) (Math.random() * 8D);
			if ((type & 1) == 1)
				cameraRandomisationH += nextCameraRandomisationH;
			if ((type & 2) == 2)
				cameraRandomisationV += nextCameraRandomisationV;
			if ((type & 4) == 4)
				cameraRandomisationA += nextCameraRandomisationA;
		}
		if (cameraRandomisationH < -50)
			nextCameraRandomisationH = 2;
		if (cameraRandomisationH > 50)
			nextCameraRandomisationH = -2;
		if (cameraRandomisationV < -55)
			nextCameraRandomisationV = 2;
		if (cameraRandomisationV > 55)
			nextCameraRandomisationV = -2;
		if (cameraRandomisationA < -40)
			nextCameraRandomisationA = 1;
		if (cameraRandomisationA > 40)
			nextCameraRandomisationA = -1;
		minimapRandomisationCounter++;
		if (minimapRandomisationCounter > 500) {
			minimapRandomisationCounter = 0;
			int type = (int) (Math.random() * 8D);
			if ((type & 1) == 1)
				minimapRotation += randomisationMinimapRotation;
			if ((type & 2) == 2)
				minimapZoom += randomisationMinimapZoom;
		}
		if (minimapRotation < -60)
			randomisationMinimapRotation = 2;
		if (minimapRotation > 60)
			randomisationMinimapRotation = -2;
		if (minimapZoom < -20)
			randomisationMinimapZoom = 1;
		if (minimapZoom > 10)
			randomisationMinimapZoom = -1;
		idleCounter++;
		if (idleCounter > 50)
			stream.putOpcode(0);
		try {
			if (socket != null && stream.position > 0) {
				socket.write(stream.position, stream.buffer);
				stream.position = 0;
				idleCounter = 0;
			}
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			logout();
		}
	}

	private void manageTextInput() {
		do {
			int c = readCharacter();
			if (c == -1)
				break;
			if (openInterfaceId != -1 && openInterfaceId == reportAbuseInterfaceID) {
				if (c == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				if ((c >= 97 && c <= 122 || c >= 65 && c <= 90 || c >= 48 && c <= 57 || c == 32)
						&& reportAbuseInput.length() < 12)
					reportAbuseInput += (char) c;
			} else if (messagePromptRaised) {
				if (c >= 32 && c <= 122 && promptInput.length() < 80) {
					// Player pressed an enterable character
					promptInput += (char) c;
					redrawChatbox = true;
				}
				if (c == 8 && promptInput.length() > 0) {
					// Player pressed backspace
					promptInput = promptInput.substring(0, promptInput.length() - 1);
					redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					// Player pressed enter
					messagePromptRaised = false;
					redrawChatbox = true;
					if (friendsListAction == 1) {
						long nameLong = TextClass.nameToLong(promptInput);
						addFriend(nameLong);
					}
					if (friendsListAction == 2 && friendsCount > 0) {
						long nameLong = TextClass.nameToLong(promptInput);
						deleteFriend(nameLong);
					}
					if (friendsListAction == 3 && promptInput.length() > 0) {
						stream.putOpcode(126);
						stream.put(0);
						int originalOffset = stream.position;
						stream.putLong(privateMessageTarget);
						TextInput.writeToStream(promptInput, stream);
						stream.putSizeByte(stream.position - originalOffset);
						promptInput = TextInput.processText(promptInput);
						promptInput = Censor.doCensor(promptInput);
						pushMessage(promptInput, 6, TextClass.formatName(TextClass.longToName(privateMessageTarget)));
						if (privateChatMode == 2) {
							privateChatMode = 1;
							updateChatSettings = true;
							stream.putOpcode(95);
							stream.put(publicChatMode);
							stream.put(privateChatMode);
							stream.put(tradeMode);
						}
					}
					if (friendsListAction == 4 && ignoreCount < 100) {
						long nameLong = TextClass.nameToLong(promptInput);
						addIgnore(nameLong);
					}
					if (friendsListAction == 5 && ignoreCount > 0) {
						long nameLong = TextClass.nameToLong(promptInput);
						deleteIgnore(nameLong);
					}
				}
			} else if (inputDialogState == 1) {
				if (c >= 48 && c <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) c;
					redrawChatbox = true;
				}
				if (c == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					if (amountOrNameInput.length() > 0) {
						int bankAmount = 0;
						try {
							bankAmount = Integer.parseInt(amountOrNameInput);
						} catch (Exception _ex) {
						}
						stream.putOpcode(208);
						stream.putInt(bankAmount);
					}
					inputDialogState = 0;
					redrawChatbox = true;
				}
			} else if (inputDialogState == 2) {
				if (c >= 32 && c <= 122 && amountOrNameInput.length() < 12) {
					amountOrNameInput += (char) c;
					redrawChatbox = true;
				}
				if (c == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					redrawChatbox = true;
				}
				if (c == 13 || c == 10) {
					if (amountOrNameInput.length() > 0) {
						stream.putOpcode(60);
						stream.putLong(TextClass.nameToLong(amountOrNameInput));
					}
					inputDialogState = 0;
					redrawChatbox = true;
				}
			} else if (chatboxInterfaceId == -1) {
				if (c >= 32 && c <= 122 && inputString.length() < 80) {
					inputString += (char) c;
					redrawChatbox = true;
				}
				if (c == 8 && inputString.length() > 0) {
					inputString = inputString.substring(0, inputString.length() - 1);
					redrawChatbox = true;
				}
				if ((c == 13 || c == 10) && inputString.length() > 0) {
					if (playerRights == 2) {
						if (inputString.equals("::clientdrop"))
							dropClient();
						if (inputString.equals("::lag"))
							printDebug();
						if (inputString.equals("::prefetchmusic")) {
							for (int j1 = 0; j1 < onDemandFetcher.fileCount(2); j1++)
								onDemandFetcher.setPriority((byte) 1, 2, j1);

						}
						if (inputString.equals("::fpson"))
							displayFpsAndMemory = true;
						if (inputString.equals("::fpsoff"))
							displayFpsAndMemory = false;
						if (inputString.equals("::noclip")) {
							for (int z = 0; z < 4; z++) {
								for (int x = 1; x < 103; x++) {
									for (int y = 1; y < 103; y++)
										currentCollisionMap[z].clippingData[x][y] = 0;

								}

							}
						}
					}
					if (inputString.startsWith("::")) {
						stream.putOpcode(103);
						stream.put(inputString.length() - 1);
						stream.putString(inputString.substring(2));
					} else {
						String text = inputString.toLowerCase();
						int colour = 0;
						if (text.startsWith("yellow:")) {
							colour = 0;
							inputString = inputString.substring(7);
						} else if (text.startsWith("red:")) {
							colour = 1;
							inputString = inputString.substring(4);
						} else if (text.startsWith("green:")) {
							colour = 2;
							inputString = inputString.substring(6);
						} else if (text.startsWith("cyan:")) {
							colour = 3;
							inputString = inputString.substring(5);
						} else if (text.startsWith("purple:")) {
							colour = 4;
							inputString = inputString.substring(7);
						} else if (text.startsWith("white:")) {
							colour = 5;
							inputString = inputString.substring(6);
						} else if (text.startsWith("flash1:")) {
							colour = 6;
							inputString = inputString.substring(7);
						} else if (text.startsWith("flash2:")) {
							colour = 7;
							inputString = inputString.substring(7);
						} else if (text.startsWith("flash3:")) {
							colour = 8;
							inputString = inputString.substring(7);
						} else if (text.startsWith("glow1:")) {
							colour = 9;
							inputString = inputString.substring(6);
						} else if (text.startsWith("glow2:")) {
							colour = 10;
							inputString = inputString.substring(6);
						} else if (text.startsWith("glow3:")) {
							colour = 11;
							inputString = inputString.substring(6);
						}
						text = inputString.toLowerCase();
						int effect = 0;
						if (text.startsWith("wave:")) {
							effect = 1;
							inputString = inputString.substring(5);
						} else if (text.startsWith("wave2:")) {
							effect = 2;
							inputString = inputString.substring(6);
						} else if (text.startsWith("shake:")) {
							effect = 3;
							inputString = inputString.substring(6);
						} else if (text.startsWith("scroll:")) {
							effect = 4;
							inputString = inputString.substring(7);
						} else if (text.startsWith("slide:")) {
							effect = 5;
							inputString = inputString.substring(6);
						}
						stream.putOpcode(4);
						stream.put(0);
						int originalOffset = stream.position;
						stream.putByteS(effect);
						stream.putByteS(colour);
						textStream.position = 0;
						TextInput.writeToStream(inputString, textStream);
						stream.putBytesA(0, textStream.buffer, textStream.position);
						stream.putSizeByte(stream.position - originalOffset);
						inputString = TextInput.processText(inputString);
						inputString = Censor.doCensor(inputString);
						localPlayer.overheadTextMessage = inputString;
						localPlayer.chatColour = colour;
						localPlayer.chatEffect = effect;
						localPlayer.textCycle = 150;
						if (playerRights == 2)
							pushMessage(localPlayer.overheadTextMessage, 2, "@cr2@" + localPlayer.name);
						else if (playerRights == 1)
							pushMessage(localPlayer.overheadTextMessage, 2, "@cr1@" + localPlayer.name);
						else
							pushMessage(localPlayer.overheadTextMessage, 2, localPlayer.name);
						if (publicChatMode == 2) {
							publicChatMode = 3;
							updateChatSettings = true;
							stream.putOpcode(95);
							stream.put(publicChatMode);
							stream.put(privateChatMode);
							stream.put(tradeMode);
						}
					}
					inputString = "";
					redrawChatbox = true;
				}
			}
		} while (true);
	}

	private void markMinimap(Sprite sprite, int x, int y) {
		int angle = cameraHorizontal + minimapRotation & 0x7ff;
		int l = x * x + y * y;
		if (l > 6400)
			return;
		int sineAngle = Model.SINE[angle];
		int cosineAngle = Model.COSINE[angle];
		sineAngle = (sineAngle * 256) / (minimapZoom + 256);
		cosineAngle = (cosineAngle * 256) / (minimapZoom + 256);
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

	private boolean menuRowIsAddFriend(int row) {
		if (row < 0)
			return false;
		int actionId = menuActionId[row];
		if (actionId >= 2000)
			actionId -= 2000;
		return actionId == 337;
	}

	private void nullLoader() {
		currentlyDrawingFlames = false;
		while (drawingFlames) {
			currentlyDrawingFlames = false;
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
		titleBoxImage = null;
		titleButtonImage = null;
		flameRuneImage = null;
		currentFlameColours = null;
		flameColour1 = null;
		flameColour2 = null;
		flameColour3 = null;
		anIntArray1190 = null;
		anIntArray1191 = null;
		anIntArray828 = null;
		anIntArray829 = null;
		flameLeftBackground2 = null;
		flameRightBackground2 = null;
	}

	private DataInputStream openJagGrabInputStream(String s) throws IOException {
		// if(!aBoolean872)
		// if(signlink.mainapp != null)
		// return signlink.openurl(s);
		// else
		// return new DataInputStream((new URL(getCodeBase(), s)).openStream());
		if (jaggrabSocket != null) {
			try {
				jaggrabSocket.close();
			} catch (Exception _ex) {
			}
			jaggrabSocket = null;
		}
		jaggrabSocket = openSocket(43595);
		jaggrabSocket.setSoTimeout(10000);
		java.io.InputStream inputstream = jaggrabSocket.getInputStream();
		OutputStream outputstream = jaggrabSocket.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	public Socket openSocket(int port) throws IOException {
		if (signlink.applet != null)
			return signlink.openSocket(port);
		else
			return new Socket(InetAddress.getByName(getCodeBase().getHost()), port);
	}

	private void parseGroupPacket(Buffer stream, int opcode) {
		if (opcode == 84) {
			int positionOffset = stream.getUnsignedByte();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int targetItemId = stream.getUnsignedLEShort();
			int targetItemAmount = stream.getUnsignedLEShort();
			int itemCount = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				NodeList groundItemArray = groundArray[plane][x][y];
				if (groundItemArray != null) {
					for (Item item = (Item) groundItemArray.peekLast(); item != null; item = (Item) groundItemArray
							.reverseGetNext()) {
						if (item.itemId != (targetItemId & 0x7fff) || item.itemCount != targetItemAmount)
							continue;
						item.itemCount = itemCount;
						break;
					}

					spawnGroundItem(x, y);
				}
			}
			return;
		}
		if (opcode == 105) {
			int positionOffset = stream.getUnsignedByte();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int trackId = stream.getUnsignedLEShort();
			int data = stream.getUnsignedByte();
			int boundarySize = data >> 4 & 0xf;
			int loop = data & 7;
			if (localPlayer.waypointX[0] >= x - boundarySize && localPlayer.waypointX[0] <= x + boundarySize
					&& localPlayer.waypointY[0] >= y - boundarySize && localPlayer.waypointY[0] <= y + boundarySize
					&& effectsEnabled && !lowMemory && trackCount < 50) {
				trackIds[trackCount] = trackId;
				trackLoop[trackCount] = loop;
				trackDelay[trackCount] = Effect.effectDelays[trackId];
				trackCount++;
			}
		}
		if (opcode == 215) {
			int id = stream.getUnsignedLEShortA();
			int positionOffset = stream.getUnsignedByteS();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int playerId = stream.getUnsignedLEShortA();
			int count = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104 && playerId != playerListId) {
				Item item = new Item();
				item.itemId = id;
				item.itemCount = count;
				if (groundArray[plane][x][y] == null)
					groundArray[plane][x][y] = new NodeList();
				groundArray[plane][x][y].insertHead(item);
				spawnGroundItem(x, y);
			}
			return;
		}
		if (opcode == 156) {
			int positionOffset = stream.getUnsignedByteA();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int itemId = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				NodeList groundItems = groundArray[plane][x][y];
				if (groundItems != null) {
					for (Item item = (Item) groundItems.peekLast(); item != null; item = (Item) groundItems
							.reverseGetNext()) {
						if (item.itemId != (itemId & 0x7fff))
							continue;
						item.unlink();
						break;
					}

					if (groundItems.peekLast() == null)
						groundArray[plane][x][y] = null;
					spawnGroundItem(x, y);
				}
			}
			return;
		}
		if (opcode == 160) // Spawn a 4-square object?
		{
			int positionOffset = stream.getUnsignedByteS();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int objectData = stream.getUnsignedByteS();
			int objectType = objectData >> 2;
			int orientation = objectData & 3;
			int type = objectTypes[objectType];
			int animationId = stream.getUnsignedLEShortA();
			if (x >= 0 && y >= 0 && x < 103 && y < 103) {
				int tileHeightX0Y0 = intGroundArray[plane][x][y];
				int tileHeightX1Y0 = intGroundArray[plane][x + 1][y];
				int tileHeightX1Y1 = intGroundArray[plane][x + 1][y + 1];
				int tileHeightX0Y1 = intGroundArray[plane][x][y + 1];
				if (type == 0) {
					WallObject wallObject = worldController.getWallObject(x, y, plane);
					if (wallObject != null) {
						int uid = wallObject.uid >> 14 & 0x7fff;
						if (objectType == 2) {
							wallObject.renderable = new GameObject(uid, 4 + orientation, 2, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
							wallObject.renderable2 = new GameObject(uid, orientation + 1 & 3, 2, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
						} else {
							wallObject.renderable = new GameObject(uid, orientation, objectType, tileHeightX1Y0,
									tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
						}
					}
				}
				if (type == 1) {
					WallDecoration wallDecoration = worldController.getWallDecoration(x, y, plane);
					if (wallDecoration != null)
						wallDecoration.renderable = new GameObject(wallDecoration.uid >> 14 & 0x7fff, 0, 4,
								tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
				}
				if (type == 2) {
					InteractiveObject interactiveObject = worldController.getInteractiveObject(x, y, plane);
					if (objectType == 11)
						objectType = 10;
					if (interactiveObject != null)
						interactiveObject.renderable = new GameObject(interactiveObject.uid >> 14 & 0x7fff, orientation,
								objectType, tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId,
								false);
				}
				if (type == 3) {
					GroundDecoration groundDecoration = worldController.getGroundDecoration(x, y, plane);
					if (groundDecoration != null)
						groundDecoration.renderable = new GameObject(groundDecoration.uid >> 14 & 0x7fff, orientation,
								22, tileHeightX1Y0, tileHeightX1Y1, tileHeightX0Y0, tileHeightX0Y1, animationId, false);
				}
			}
			return;
		}
		if (opcode == 147) {
			int positionOffset = stream.getUnsignedByteS();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int targetPlayer = stream.getUnsignedLEShort();
			byte tileHeight = stream.getByteS();
			int startDelay = stream.getUnsignedShort();
			byte tileWidth = stream.getByteC();
			int duration = stream.getUnsignedLEShort();
			int objectData = stream.getUnsignedByteS();
			int objectType = objectData >> 2;
			int objectOrientation = objectData & 3;
			int type = objectTypes[objectType];
			byte offsetX = stream.get();
			int objectId = stream.getUnsignedLEShort();
			byte offsetY = stream.getByteC();
			Player player;
			if (targetPlayer == playerListId)
				player = localPlayer;
			else
				player = players[targetPlayer];
			if (player != null) {
				GameObjectDefinition object = GameObjectDefinition.getDefinition(objectId);
				int tileHeightX0Y0 = intGroundArray[plane][x][y];
				int tileHeightX1Y0 = intGroundArray[plane][x + 1][y];
				int tileHeightX1Y1 = intGroundArray[plane][x + 1][y + 1];
				int tileHeightX0Y1 = intGroundArray[plane][x][y + 1];
				Model model = object.getModelAt(objectType, objectOrientation, tileHeightX0Y0, tileHeightX1Y0,
						tileHeightX1Y1, tileHeightX0Y1, -1);
				if (model != null) {
					createObjectSpawnRequest(duration + 1, -1, 0, type, y, 0, plane, x, startDelay + 1);
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
					player.drawHeight = getFloorDrawHeight(plane, player.anInt1713, player.anInt1711);
					if (offsetX > tileHeight) {
						byte temp = offsetX;
						offsetX = tileHeight;
						tileHeight = temp;
					}
					if (offsetY > tileWidth) {
						byte temp = offsetY;
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
			int positionOffset = stream.getUnsignedByteA();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int objectId = stream.getUnsignedShort();
			int data = stream.getUnsignedByteS();
			int objectType = data >> 2;
			int orientation = data & 3;
			int type = objectTypes[objectType];
			if (x >= 0 && y >= 0 && x < 104 && y < 104)
				createObjectSpawnRequest(-1, objectId, orientation, type, y, objectType, plane, x, 0);
			return;
		}
		if (opcode == 4) {
			int positionOffset = stream.getUnsignedByte();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			int graphicId = stream.getUnsignedLEShort();
			int drawHeight = stream.getUnsignedByte();
			int delay = stream.getUnsignedLEShort();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				x = x * 128 + 64;
				y = y * 128 + 64;
				StationaryGraphic stationaryGraphic = new StationaryGraphic(x, y, plane,
						getFloorDrawHeight(plane, y, x) - drawHeight, graphicId, tick, delay);
				stationaryGraphicQueue.insertHead(stationaryGraphic);
			}
			return;
		}
		if (opcode == 44) {
			int itemId = stream.getUnsignedShortA();
			int itemAmount = stream.getUnsignedLEShort();
			int positionOffset = stream.getUnsignedByte();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				Item item = new Item();
				item.itemId = itemId;
				item.itemCount = itemAmount;
				if (groundArray[plane][x][y] == null)
					groundArray[plane][x][y] = new NodeList();
				groundArray[plane][x][y].insertHead(item);
				spawnGroundItem(x, y);
			}
			return;
		}
		if (opcode == 101) {
			int objectData = stream.getUnsignedByteC();
			int objectType = objectData >> 2;
			int face = objectData & 3;
			int type = objectTypes[objectType];
			int positionOffset = stream.getUnsignedByte();
			int x = playerPositionX + (positionOffset >> 4 & 7);
			int y = playerPositionY + (positionOffset & 7);
			if (x >= 0 && y >= 0 && x < 104 && y < 104)
				createObjectSpawnRequest(-1, -1, face, type, y, objectType, plane, x, 0);
			return;
		}
		if (opcode == 117) {
			int projectileAngle = stream.getUnsignedByte();
			int projectileX = playerPositionX + (projectileAngle >> 4 & 7);
			int projectileY = playerPositionY + (projectileAngle & 7);
			int projectileOffsetX = projectileX + stream.get();
			int projectileOffsetY = projectileY + stream.get();
			int projectileTarget = stream.getShort();
			int projectileGraphicId = stream.getUnsignedLEShort();
			int projectileHeightStart = stream.getUnsignedByte() * 4;
			int projectileHeightEnd = stream.getUnsignedByte() * 4;
			int projectileCreatedTime = stream.getUnsignedLEShort();
			int projectileSpeed = stream.getUnsignedLEShort();
			int projectileInitialSlope = stream.getUnsignedByte();
			int projectileDistanceFromSource = stream.getUnsignedByte();
			if (projectileX >= 0 && projectileY >= 0 && projectileX < 104 && projectileY < 104 && projectileOffsetX >= 0
					&& projectileOffsetY >= 0 && projectileOffsetX < 104 && projectileOffsetY < 104
					&& projectileGraphicId != 65535) {
				projectileX = projectileX * 128 + 64;
				projectileY = projectileY * 128 + 64;
				projectileOffsetX = projectileOffsetX * 128 + 64;
				projectileOffsetY = projectileOffsetY * 128 + 64;
				Projectile projectile = new Projectile(projectileInitialSlope, projectileHeightEnd,
						projectileCreatedTime + tick, projectileSpeed + tick, projectileDistanceFromSource, plane,
						getFloorDrawHeight(plane, projectileY, projectileX) - projectileHeightStart, projectileY,
						projectileX, projectileTarget, projectileGraphicId);
				projectile.trackTarget(projectileCreatedTime + tick, projectileOffsetY,
						getFloorDrawHeight(plane, projectileOffsetY, projectileOffsetX) - projectileHeightEnd,
						projectileOffsetX);
				projectileQueue.insertHead(projectile);
			}
		}
	}

	private int parseInterfaceOpcode(RSInterface rsInterface, int interfaceId) {
		if (rsInterface.opcodes == null || interfaceId >= rsInterface.opcodes.length)
			return -2;
		try {
			int opcodes[] = rsInterface.opcodes[interfaceId];
			int result = 0;
			int counter = 0;
			int type = 0;
			do {
				int opcode = opcodes[counter++];
				int value = 0;
				byte tempType = 0;
				if (opcode == 0)
					return result;
				if (opcode == 1)
					value = skillLevel[opcodes[counter++]];
				if (opcode == 2)
					value = skillMaxLevel[opcodes[counter++]];
				if (opcode == 3)
					value = skillExperience[opcodes[counter++]];
				if (opcode == 4) {
					RSInterface itemInterface = RSInterface.cache[opcodes[counter++]];
					int itemId = opcodes[counter++];
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersObject || membersWorld)) {
						for (int item = 0; item < itemInterface.inventoryItemId.length; item++)
							if (itemInterface.inventoryItemId[item] == itemId + 1)
								value += itemInterface.inventoryStackSize[item];

					}
				}
				if (opcode == 5)
					value = interfaceSettings[opcodes[counter++]];
				if (opcode == 6)
					value = EXPERIENCE_TABLE[skillMaxLevel[opcodes[counter++]] - 1];
				if (opcode == 7)
					value = (interfaceSettings[opcodes[counter++]] * 100) / 46875;
				if (opcode == 8)
					value = localPlayer.combatLevel;
				if (opcode == 9) {
					for (int skill = 0; skill < Skills.skillsCount; skill++)
						if (Skills.skillEnabled[skill])
							value += skillMaxLevel[skill];

				}
				if (opcode == 10) {
					RSInterface itemInterface = RSInterface.cache[opcodes[counter++]];
					int itemId = opcodes[counter++] + 1;
					if (itemId >= 0 && itemId < ItemDefinition.itemCount
							&& (!ItemDefinition.getDefinition(itemId).membersObject || membersWorld)) {
						for (int item = 0; item < itemInterface.inventoryItemId.length; item++) {
							if (itemInterface.inventoryItemId[item] != itemId)
								continue;
							value = 999999999;
							break;
						}

					}
				}
				if (opcode == 11)
					value = playerEnergy;
				if (opcode == 12)
					value = playerWeight;
				if (opcode == 13) {
					int setting = interfaceSettings[opcodes[counter++]];
					int info = opcodes[counter++];
					value = (setting & 1 << info) == 0 ? 0 : 1;
				}
				if (opcode == 14) {
					int varBitId = opcodes[counter++];
					VarBit varBit = VarBit.cache[varBitId];
					int configId = varBit.configId;
					int lsb = varBit.leastSignificantBit;
					int msb = varBit.mostSignificantBit;
					int bit = BITFIELD_MAX_VALUE[msb - lsb];
					value = interfaceSettings[configId] >> lsb & bit;
				}
				if (opcode == 15)
					tempType = 1;
				if (opcode == 16)
					tempType = 2;
				if (opcode == 17)
					tempType = 3;
				if (opcode == 18)
					value = (localPlayer.x >> 7) + baseX;
				if (opcode == 19)
					value = (localPlayer.y >> 7) + baseY;
				if (opcode == 20)
					value = opcodes[counter++];
				if (tempType == 0) {
					if (type == 0)
						result += value;
					if (type == 1)
						result -= value;
					if (type == 2 && value != 0)
						result /= value;
					if (type == 3)
						result *= value;
					type = 0;
				} else {
					type = tempType;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	private void printDebug() {
		System.out.println("============");
		System.out.println("flame-cycle:" + flameCycle);
		if (onDemandFetcher != null)
			System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle);
		System.out.println("loop-cycle:" + tick);
		System.out.println("draw-cycle:" + drawCycle);
		System.out.println("ptype:" + packetOpcode);
		System.out.println("psize:" + packetSize);
		if (socket != null)
			socket.printDebug();
		super.debugRequested = true;
	}

	private void processChatModeClick() {
		if (super.clickType == 1) {
			if (super.clickX >= 6 && super.clickX <= 106 && super.clickY >= 467 && super.clickY <= 499) {
				publicChatMode = (publicChatMode + 1) % 4;
				updateChatSettings = true;
				redrawChatbox = true;
				stream.putOpcode(95);
				stream.put(publicChatMode);
				stream.put(privateChatMode);
				stream.put(tradeMode);
			}
			if (super.clickX >= 135 && super.clickX <= 235 && super.clickY >= 467 && super.clickY <= 499) {
				privateChatMode = (privateChatMode + 1) % 3;
				updateChatSettings = true;
				redrawChatbox = true;
				stream.putOpcode(95);
				stream.put(publicChatMode);
				stream.put(privateChatMode);
				stream.put(tradeMode);
			}
			if (super.clickX >= 273 && super.clickX <= 373 && super.clickY >= 467 && super.clickY <= 499) {
				tradeMode = (tradeMode + 1) % 3;
				updateChatSettings = true;
				redrawChatbox = true;
				stream.putOpcode(95);
				stream.put(publicChatMode);
				stream.put(privateChatMode);
				stream.put(tradeMode);
			}
			if (super.clickX >= 412 && super.clickX <= 512 && super.clickY >= 467 && super.clickY <= 499)
				if (openInterfaceId == -1) {
					clearTopInterfaces();
					reportAbuseInput = "";
					reportAbuseMute = false;
					for (int i = 0; i < RSInterface.cache.length; i++) {
						if (RSInterface.cache[i] == null || RSInterface.cache[i].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceId = RSInterface.cache[i].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
			anInt940++;
			if (anInt940 > 1386) {
				anInt940 = 0;
				stream.putOpcode(165);
				stream.put(0);
				int j = stream.position;
				stream.put(139);
				stream.put(150);
				stream.putShort(32131);
				stream.put((int) (Math.random() * 256D));
				stream.putShort(3250);
				stream.put(177);
				stream.putShort(24859);
				stream.put(119);
				if ((int) (Math.random() * 2D) == 0)
					stream.putShort(47234);
				if ((int) (Math.random() * 2D) == 0)
					stream.put(21);
				stream.putSizeByte(stream.position - j);
			}
		}
	}

	@Override
	public void processDrawing() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError) {
			showErrorScreen();
			return;
		}
		drawCycle++;
		if (!loggedIn)
			drawLoginScreen(false);
		else
			drawGameScreen();
		anInt1213 = 0;
	}

	@Override
	public void processGameLoop() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		tick++;
		if (!loggedIn)
			updateLogin();
		else
			updateGame();
		processOnDemandQueue();
	}

	private void updateLogin() {
		if (loginScreenState == 0) {
			int x = super.width / 2 - 80;
			int y = super.height / 2 + 20;
			y += 20;
			if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
					&& super.clickY <= y + 20) {
				loginScreenState = 3;
				loginScreenFocus = 0;
			}
			x = super.width / 2 + 80;
			if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
					&& super.clickY <= y + 20) {
				loginMessage1 = "";
				loginMessage2 = "Enter your username & password.";
				loginScreenState = 2;
				loginScreenFocus = 0;
			}
		} else {
			if (loginScreenState == 2) {
				int y = super.height / 2 - 40;
				y += 30;
				y += 25;
				if (super.clickType == 1 && super.clickY >= y - 15 && super.clickY < y)
					loginScreenFocus = 0;
				y += 15;
				if (super.clickType == 1 && super.clickY >= y - 15 && super.clickY < y)
					loginScreenFocus = 1;
				y += 15;
				int x = super.width / 2 - 80;
				int _y = super.height / 2 + 50;
				_y += 20;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= _y - 20
						&& super.clickY <= _y + 20) {
					loginFailures = 0;
					login(enteredUsername, enteredPassword, false);
					if (loggedIn)
						return;
				}
				x = super.width / 2 + 80;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= _y - 20
						&& super.clickY <= _y + 20) {
					loginScreenState = 0;
					// myUsername = "";
					// myPassword = "";
				}
				do {
					int character = readCharacter();
					if (character == -1)
						break;
					boolean validCharacter = false;
					for (int c = 0; c < validUserPassChars.length(); c++) {
						if (character != validUserPassChars.charAt(c))
							continue;
						validCharacter = true;
						break;
					}

					if (loginScreenFocus == 0) {
						if (character == 8 && enteredUsername.length() > 0)
							enteredUsername = enteredUsername.substring(0, enteredUsername.length() - 1);
						if (character == 9 || character == 10 || character == 13)
							loginScreenFocus = 1;
						if (validCharacter)
							enteredUsername += (char) character;
						if (enteredUsername.length() > 12)
							enteredUsername = enteredUsername.substring(0, 12);
					} else if (loginScreenFocus == 1) {
						if (character == 8 && enteredPassword.length() > 0)
							enteredPassword = enteredPassword.substring(0, enteredPassword.length() - 1);
						if (character == 9 || character == 10 || character == 13)
							loginScreenFocus = 0;
						if (validCharacter)
							enteredPassword += (char) character;
						if (enteredPassword.length() > 20)
							enteredPassword = enteredPassword.substring(0, 20);
					}
				} while (true);
				return;
			}
			if (loginScreenState == 3) {
				int x = super.width / 2;
				int y = super.height / 2 + 50;
				y += 20;
				if (super.clickType == 1 && super.clickX >= x - 75 && super.clickX <= x + 75 && super.clickY >= y - 20
						&& super.clickY <= y + 20)
					loginScreenState = 0;
			}
		}
	}

	private void processMenuClick() {
		if (activeInterfaceType != 0)
			return;
		int clickType = super.clickType;
		if (spellSelected && super.clickX >= 516 && super.clickY >= 160 && super.clickX <= 765 && super.clickY <= 205)
			clickType = 0;
		if (menuOpen) {
			if (clickType != 1) {
				int x = super.mouseX;
				int y = super.mouseY;
				if (menuScreenArea == 0) {
					x -= 4;
					y -= 4;
				}
				if (menuScreenArea == 1) {
					x -= 553;
					y -= 205;
				}
				if (menuScreenArea == 2) {
					x -= 17;
					y -= 357;
				}
				if (x < menuOffsetX - 10 || x > menuOffsetX + menuWidth + 10 || y < menuOffsetY - 10
						|| y > menuOffsetY + menuHeight + 10) {
					menuOpen = false;
					if (menuScreenArea == 1)
						redrawTab = true;
					if (menuScreenArea == 2)
						redrawChatbox = true;
				}
			}
			if (clickType == 1) {
				int menuX = menuOffsetX;
				int height = menuOffsetY;
				int width = menuWidth;
				int x = super.clickX;
				int y = super.clickY;
				if (menuScreenArea == 0) {
					x -= 4;
					y -= 4;
				}
				if (menuScreenArea == 1) {
					x -= 553;
					y -= 205;
				}
				if (menuScreenArea == 2) {
					x -= 17;
					y -= 357;
				}
				int hoveredRow = -1;
				for (int row = 0; row < menuActionRow; row++) {
					int rowY = height + 31 + (menuActionRow - 1 - row) * 15;
					if (x > menuX && x < menuX + width && y > rowY - 13 && y < rowY + 3)
						hoveredRow = row;
				}

				if (hoveredRow != -1)
					doAction(hoveredRow);
				menuOpen = false;
				if (menuScreenArea == 1)
					redrawTab = true;
				if (menuScreenArea == 2) {
					redrawChatbox = true;
				}
			}
		} else {
			if (clickType == 1 && menuActionRow > 0) {
				int action = menuActionId[menuActionRow - 1];
				if (action == 632 || action == 78 || action == 867 || action == 431 || action == 53 || action == 74
						|| action == 454 || action == 539 || action == 493 || action == 847 || action == 447
						|| action == 1125) {
					int slot = menuActionData2[menuActionRow - 1];
					int interfaceId = menuActionData3[menuActionRow - 1];
					RSInterface rsInterface = RSInterface.cache[interfaceId];
					if (rsInterface.itemSwappable || rsInterface.itemDeletesDragged) {
						lastItemDragged = false;
						lastItemDragTime = 0;
						moveItemInterfaceId = interfaceId;
						moveItemSlotStart = slot;
						activeInterfaceType = 2;
						lastMouseX = super.clickX;
						lastMouseY = super.clickY;
						if (RSInterface.cache[interfaceId].parentID == openInterfaceId)
							activeInterfaceType = 1;
						if (RSInterface.cache[interfaceId].parentID == chatboxInterfaceId)
							activeInterfaceType = 3;
						return;
					}
				}
			}
			if (clickType == 1 && (oneMouseButton == 1 || menuRowIsAddFriend(menuActionRow - 1)) && menuActionRow > 2)
				clickType = 2;
			if (clickType == 1 && menuActionRow > 0)
				doAction(menuActionRow - 1);
			if (clickType == 2 && menuActionRow > 0)
				processMenuHovering();
		}
	}

	private void processMenuHovering() {
		int width = fontBold.getTextDisplayedWidth("Choose Option");
		for (int row = 0; row < menuActionRow; row++) {
			int rowWidth = fontBold.getTextDisplayedWidth(menuActionName[row]);
			if (rowWidth > width)
				width = rowWidth;
		}

		width += 8;
		int height = 15 * menuActionRow + 21;
		if (super.clickX > 4 && super.clickY > 4 && super.clickX < 516 && super.clickY < 338) {
			int x = super.clickX - 4 - width / 2;
			if (x + width > 512)
				x = 512 - width;
			if (x < 0)
				x = 0;
			int y = super.clickY - 4;
			if (y + height > 334)
				y = 334 - height;
			if (y < 0)
				y = 0;
			menuOpen = true;
			menuScreenArea = 0;
			menuOffsetX = x;
			menuOffsetY = y;
			menuWidth = width;
			menuHeight = 15 * menuActionRow + 22;
		}
		if (super.clickX > 553 && super.clickY > 205 && super.clickX < 743 && super.clickY < 466) {
			int x = super.clickX - 553 - width / 2;
			if (x < 0)
				x = 0;
			else if (x + width > 190)
				x = 190 - width;
			int y = super.clickY - 205;
			if (y < 0)
				y = 0;
			else if (y + height > 261)
				y = 261 - height;
			menuOpen = true;
			menuScreenArea = 1;
			menuOffsetX = x;
			menuOffsetY = y;
			menuWidth = width;
			menuHeight = 15 * menuActionRow + 22;
		}
		if (super.clickX > 17 && super.clickY > 357 && super.clickX < 496 && super.clickY < 453) {
			int x = super.clickX - 17 - width / 2;
			if (x < 0)
				x = 0;
			else if (x + width > 479)
				x = 479 - width;
			int y = super.clickY - 357;
			if (y < 0)
				y = 0;
			else if (y + height > 96)
				y = 96 - height;
			menuOpen = true;
			menuScreenArea = 2;
			menuOffsetX = x;
			menuOffsetY = y;
			menuWidth = width;
			menuHeight = 15 * menuActionRow + 22;
		}
	}

	private void processMinimapClick() {
		if (minimapState != 0)
			return;
		if (super.clickType == 1) {
			int i = super.clickX - 25 - 550;
			int j = super.clickY - 5 - 4;
			if (i >= 0 && j >= 0 && i < 146 && j < 151) {
				i -= 73;
				j -= 75;
				int k = cameraHorizontal + minimapRotation & 0x7ff;
				int sine = Rasterizer.SINE[k];
				int cosine = Rasterizer.COSINE[k];
				sine = sine * (minimapZoom + 256) >> 8;
				cosine = cosine * (minimapZoom + 256) >> 8;
				int k1 = j * sine + i * cosine >> 11;
				int l1 = j * cosine - i * sine >> 11;
				int i2 = localPlayer.x + k1 >> 7;
				int j2 = localPlayer.y - l1 >> 7;
				boolean canWalk = doWalkTo(1, 0, 0, 0, localPlayer.waypointY[0], 0, 0, j2, localPlayer.waypointX[0],
						true, i2);
				if (canWalk) {
					stream.put(i);
					stream.put(j);
					stream.putShort(cameraHorizontal);
					stream.put(57);
					stream.put(minimapRotation);
					stream.put(minimapZoom);
					stream.put(89);
					stream.putShort(localPlayer.x);
					stream.putShort(localPlayer.y);
					stream.put(arbitraryDestination);
					stream.put(63);
				}
			}
			mouseClickCounter++;
			if (mouseClickCounter > 1151) {
				mouseClickCounter = 0;
				stream.putOpcode(246);
				stream.put(0);
				int l = stream.position;
				if ((int) (Math.random() * 2D) == 0)
					stream.put(101);
				stream.put(197);
				stream.putShort((int) (Math.random() * 65536D));
				stream.put((int) (Math.random() * 256D));
				stream.put(67);
				stream.putShort(14214);
				if ((int) (Math.random() * 2D) == 0)
					stream.putShort(29487);
				stream.putShort((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0)
					stream.put(220);
				stream.put(180);
				stream.putSizeByte(stream.position - l);
			}
		}
	}

	private void processOnDemandQueue() {
		do {
			OnDemandData onDemandData;
			do {
				onDemandData = onDemandFetcher.getNextNode();
				if (onDemandData == null)
					return;
				if (onDemandData.dataType == 0) {
					Model.loadModelHeader(onDemandData.buffer, onDemandData.id);
					if ((onDemandFetcher.getModelId(onDemandData.id) & 0x62) != 0) {
						redrawTab = true;
						if (chatboxInterfaceId != -1)
							redrawChatbox = true;
					}
				}
				if (onDemandData.dataType == 1 && onDemandData.buffer != null)
					Animation.method529(onDemandData.buffer);
				if (onDemandData.dataType == 2 && onDemandData.id == nextSong && onDemandData.buffer != null)
					saveMidi(songChanging, onDemandData.buffer);
				if (onDemandData.dataType == 3 && loadingStage == 1) {
					for (int r = 0; r < terrainData.length; r++) {
						if (terrainDataIds[r] == onDemandData.id) {
							terrainData[r] = onDemandData.buffer;
							if (onDemandData.buffer == null)
								terrainDataIds[r] = -1;
							break;
						}

						if (objectDataIds[r] != onDemandData.id)
							continue;
						objectData[r] = onDemandData.buffer;
						if (onDemandData.buffer == null)
							objectDataIds[r] = -1;
						break;
					}

				}
			} while (onDemandData.dataType != 93 || !onDemandFetcher.method564(onDemandData.id));
			Region.passivelyRequestGameObjectModels(new Buffer(onDemandData.buffer), onDemandFetcher);
		} while (true);
	}

	private void processRightClick() {
		if (activeInterfaceType != 0)
			return;
		menuActionName[0] = "Cancel";
		menuActionId[0] = 1107;
		menuActionRow = 1;
		buildSplitPrivateChatMenu();
		anInt886 = 0;
		if (super.mouseX > 4 && super.mouseY > 4 && super.mouseX < 516 && super.mouseY < 338)
			if (openInterfaceId != -1)
				buildInterfaceMenu(4, RSInterface.cache[openInterfaceId], super.mouseX, 4, super.mouseY, 0);
			else
				build3dScreenMenu();
		if (anInt886 != anInt1026)
			anInt1026 = anInt886;
		anInt886 = 0;
		if (super.mouseX > 553 && super.mouseY > 205 && super.mouseX < 743 && super.mouseY < 466)
			if (inventoryOverlayInterfaceID != -1)
				buildInterfaceMenu(553, RSInterface.cache[inventoryOverlayInterfaceID], super.mouseX, 205, super.mouseY,
						0);
			else if (tabInterfaceIDs[currentTabId] != -1)
				buildInterfaceMenu(553, RSInterface.cache[tabInterfaceIDs[currentTabId]], super.mouseX, 205,
						super.mouseY, 0);
		if (anInt886 != anInt1048) {
			redrawTab = true;
			anInt1048 = anInt886;
		}
		anInt886 = 0;
		if (super.mouseX > 17 && super.mouseY > 357 && super.mouseX < 496 && super.mouseY < 453)
			if (chatboxInterfaceId != -1)
				buildInterfaceMenu(17, RSInterface.cache[chatboxInterfaceId], super.mouseX, 357, super.mouseY, 0);
			else if (super.mouseY < 434 && super.mouseX < 426)
				buildChatboxMenu(super.mouseY - 357);
		if (chatboxInterfaceId != -1 && anInt886 != anInt1039) {
			redrawChatbox = true;
			anInt1039 = anInt886;
		}
		boolean ordered = false;
		while (!ordered) {
			ordered = true;
			for (int a = 0; a < menuActionRow - 1; a++)
				if (menuActionId[a] < 1000 && menuActionId[a + 1] > 1000) {
					String s = menuActionName[a];
					menuActionName[a] = menuActionName[a + 1];
					menuActionName[a + 1] = s;
					int temp = menuActionId[a];
					menuActionId[a] = menuActionId[a + 1];
					menuActionId[a + 1] = temp;
					temp = menuActionData2[a];
					menuActionData2[a] = menuActionData2[a + 1];
					menuActionData2[a + 1] = temp;
					temp = menuActionData3[a];
					menuActionData3[a] = menuActionData3[a + 1];
					menuActionData3[a + 1] = temp;
					temp = menuActionData1[a];
					menuActionData1[a] = menuActionData1[a + 1];
					menuActionData1[a + 1] = temp;
					ordered = false;
				}

		}
	}

	private void processTabClick() {
		if (super.clickType == 1) {
			if (super.clickX >= 539 && super.clickX <= 573 && super.clickY >= 169 && super.clickY < 205
					&& tabInterfaceIDs[0] != -1) {
				redrawTab = true;
				currentTabId = 0;
				drawTabIcons = true;
			}
			if (super.clickX >= 569 && super.clickX <= 599 && super.clickY >= 168 && super.clickY < 205
					&& tabInterfaceIDs[1] != -1) {
				redrawTab = true;
				currentTabId = 1;
				drawTabIcons = true;
			}
			if (super.clickX >= 597 && super.clickX <= 627 && super.clickY >= 168 && super.clickY < 205
					&& tabInterfaceIDs[2] != -1) {
				redrawTab = true;
				currentTabId = 2;
				drawTabIcons = true;
			}
			if (super.clickX >= 625 && super.clickX <= 669 && super.clickY >= 168 && super.clickY < 203
					&& tabInterfaceIDs[3] != -1) {
				redrawTab = true;
				currentTabId = 3;
				drawTabIcons = true;
			}
			if (super.clickX >= 666 && super.clickX <= 696 && super.clickY >= 168 && super.clickY < 205
					&& tabInterfaceIDs[4] != -1) {
				redrawTab = true;
				currentTabId = 4;
				drawTabIcons = true;
			}
			if (super.clickX >= 694 && super.clickX <= 724 && super.clickY >= 168 && super.clickY < 205
					&& tabInterfaceIDs[5] != -1) {
				redrawTab = true;
				currentTabId = 5;
				drawTabIcons = true;
			}
			if (super.clickX >= 722 && super.clickX <= 756 && super.clickY >= 169 && super.clickY < 205
					&& tabInterfaceIDs[6] != -1) {
				redrawTab = true;
				currentTabId = 6;
				drawTabIcons = true;
			}
			if (super.clickX >= 540 && super.clickX <= 574 && super.clickY >= 466 && super.clickY < 502
					&& tabInterfaceIDs[7] != -1) {
				redrawTab = true;
				currentTabId = 7;
				drawTabIcons = true;
			}
			if (super.clickX >= 572 && super.clickX <= 602 && super.clickY >= 466 && super.clickY < 503
					&& tabInterfaceIDs[8] != -1) {
				redrawTab = true;
				currentTabId = 8;
				drawTabIcons = true;
			}
			if (super.clickX >= 599 && super.clickX <= 629 && super.clickY >= 466 && super.clickY < 503
					&& tabInterfaceIDs[9] != -1) {
				redrawTab = true;
				currentTabId = 9;
				drawTabIcons = true;
			}
			if (super.clickX >= 627 && super.clickX <= 671 && super.clickY >= 467 && super.clickY < 502
					&& tabInterfaceIDs[10] != -1) {
				redrawTab = true;
				currentTabId = 10;
				drawTabIcons = true;
			}
			if (super.clickX >= 669 && super.clickX <= 699 && super.clickY >= 466 && super.clickY < 503
					&& tabInterfaceIDs[11] != -1) {
				redrawTab = true;
				currentTabId = 11;
				drawTabIcons = true;
			}
			if (super.clickX >= 696 && super.clickX <= 726 && super.clickY >= 466 && super.clickY < 503
					&& tabInterfaceIDs[12] != -1) {
				redrawTab = true;
				currentTabId = 12;
				drawTabIcons = true;
			}
			if (super.clickX >= 724 && super.clickX <= 758 && super.clickY >= 466 && super.clickY < 502
					&& tabInterfaceIDs[13] != -1) {
				redrawTab = true;
				currentTabId = 13;
				drawTabIcons = true;
			}
		}
	}

	private void processWalkingStep(Entity entity) {
		entity.queuedAnimationId = entity.standAnimationId;
		if (entity.waypointCount == 0) {
			entity.stepsDelayed = 0;
			return;
		}
		if (entity.animation != -1 && entity.animationDelay == 0) {
			AnimationSequence animation = AnimationSequence.animations[entity.animation];
			if (entity.stepsRemaining > 0 && animation.precedenceAnimating == 0) {
				entity.stepsDelayed++;
				return;
			}
			if (entity.stepsRemaining <= 0 && animation.precedenceWalking == 0) {
				entity.stepsDelayed++;
				return;
			}
		}
		int x1 = entity.x;
		int y1 = entity.y;
		int x2 = entity.waypointX[entity.waypointCount - 1] * 128 + entity.boundaryDimension * 64;
		int y2 = entity.waypointY[entity.waypointCount - 1] * 128 + entity.boundaryDimension * 64;
		if (x2 - x1 > 256 || x2 - x1 < -256 || y2 - y1 > 256 || y2 - y1 < -256) {
			entity.x = x2;
			entity.y = y2;
			return;
		}
		if (x1 < x2) {
			if (y1 < y2)
				entity.turnDirection = 1280;
			else if (y1 > y2)
				entity.turnDirection = 1792;
			else
				entity.turnDirection = 1536;
		} else if (x1 > x2) {
			if (y1 < y2)
				entity.turnDirection = 768;
			else if (y1 > y2)
				entity.turnDirection = 256;
			else
				entity.turnDirection = 512;
		} else if (y1 < y2)
			entity.turnDirection = 1024;
		else
			entity.turnDirection = 0;
		int rotationDifference = entity.turnDirection - entity.currentRotation & 0x7ff;
		if (rotationDifference > 1024)
			rotationDifference -= 2048;
		int anim = entity.turnAboutAnimationId;
		if (rotationDifference >= -256 && rotationDifference <= 256)
			anim = entity.walkAnimationId;
		else if (rotationDifference >= 256 && rotationDifference < 768)
			anim = entity.turnLeftAnimationId;
		else if (rotationDifference >= -768 && rotationDifference <= -256)
			anim = entity.turnRightAnimationId;
		if (anim == -1)
			anim = entity.walkAnimationId;
		entity.queuedAnimationId = anim;
		int k1 = 4;
		if (entity.currentRotation != entity.turnDirection && entity.interactingEntity == -1
				&& entity.degreesToTurn != 0)
			k1 = 2;
		if (entity.waypointCount > 2)
			k1 = 6;
		if (entity.waypointCount > 3)
			k1 = 8;
		if (entity.stepsDelayed > 0 && entity.waypointCount > 1) {
			k1 = 8;
			entity.stepsDelayed--;
		}
		if (entity.waypointRan[entity.waypointCount - 1])
			k1 <<= 1;
		if (k1 >= 8 && entity.queuedAnimationId == entity.walkAnimationId && entity.runAnimationId != -1)
			entity.queuedAnimationId = entity.runAnimationId;
		if (x1 < x2) {
			entity.x += k1;
			if (entity.x > x2)
				entity.x = x2;
		} else if (x1 > x2) {
			entity.x -= k1;
			if (entity.x < x2)
				entity.x = x2;
		}
		if (y1 < y2) {
			entity.y += k1;
			if (entity.y > y2)
				entity.y = y2;
		} else if (y1 > y2) {
			entity.y -= k1;
			if (entity.y < y2)
				entity.y = y2;
		}
		if (entity.x == x2 && entity.y == y2) {
			entity.waypointCount--;
			if (entity.stepsRemaining > 0)
				entity.stepsRemaining--;
		}
	}

	private boolean promptUserForInput(RSInterface rsInterface) {
		int contentType = rsInterface.contentType;
		if (friendListStatus == 2) {
			if (contentType == 201) {
				redrawChatbox = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				chatboxInputNeededString = "Enter name of friend to add to list";
			}
			if (contentType == 202) {
				redrawChatbox = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				chatboxInputNeededString = "Enter name of friend to delete from list";
			}
		}
		if (contentType == 205) {
			idleLogout = 250;
			return true;
		}
		if (contentType == 501) {
			redrawChatbox = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			chatboxInputNeededString = "Enter name of player to add to list";
		}
		if (contentType == 502) {
			redrawChatbox = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			chatboxInputNeededString = "Enter name of player to delete from list";
		}
		if (contentType >= 300 && contentType <= 313) {
			int type = (contentType - 300) / 2;
			int direction = contentType & 1;
			int currentId = characterEditIdentityKits[type];
			if (currentId != -1) {
				do {
					if (direction == 0 && --currentId < 0)
						currentId = IdentityKit.count - 1;
					if (direction == 1 && ++currentId >= IdentityKit.count)
						currentId = 0;
				} while (IdentityKit.cache[currentId].widgetDisplayed
						|| IdentityKit.cache[currentId].partId != type + (characterEditChangeGender ? 0 : 7));
				characterEditIdentityKits[type] = currentId;
				characterModelChanged = true;
			}
		}
		if (contentType >= 314 && contentType <= 323) {
			int type = (contentType - 314) / 2;
			int direction = contentType & 1;
			int currentId = characterEditColours[type];
			if (direction == 0 && --currentId < 0)
				currentId = APPEARANCE_COLOURS[type].length - 1;
			if (direction == 1 && ++currentId >= APPEARANCE_COLOURS[type].length)
				currentId = 0;
			characterEditColours[type] = currentId;
			characterModelChanged = true;
		}
		if (contentType == 324 && !characterEditChangeGender) {
			characterEditChangeGender = true;
			changeGender();
		}
		if (contentType == 325 && characterEditChangeGender) {
			characterEditChangeGender = false;
			changeGender();
		}
		if (contentType == 326) {
			stream.putOpcode(101);
			stream.put(characterEditChangeGender ? 0 : 1);
			for (int part = 0; part < 7; part++)
				stream.put(characterEditIdentityKits[part]);

			for (int part = 0; part < 5; part++)
				stream.put(characterEditColours[part]);

			return true;
		}
		if (contentType == 613)
			reportAbuseMute = !reportAbuseMute;
		if (contentType >= 601 && contentType <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.putOpcode(218);
				stream.putLong(TextClass.nameToLong(reportAbuseInput));
				stream.put(contentType - 601);
				stream.put(reportAbuseMute ? 1 : 0);
			}
		}
		return false;
	}

	private void pushMessage(String message, int type, String name) {
		if (type == 0 && dialogID != -1) {
			clickToContinueString = message;
			super.clickType = 0;
		}
		if (chatboxInterfaceId == -1)
			redrawChatbox = true;
		for (int m = 99; m > 0; m--) {
			chatTypes[m] = chatTypes[m - 1];
			chatNames[m] = chatNames[m - 1];
			chatMessages[m] = chatMessages[m - 1];
		}

		chatTypes[0] = type;
		chatNames[0] = name;
		chatMessages[0] = message;
	}

	private void randomizeBackground(Background background) {
		int j = 256;
		for (int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for (int l = 0; l < 5000; l++) {
			int i1 = (int) (Math.random() * 128D * j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}

		for (int j1 = 0; j1 < 20; j1++) {
			for (int k1 = 1; k1 < j - 1; k1++) {
				for (int i2 = 1; i2 < 127; i2++) {
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128]
							+ anIntArray1190[k2 + 128]) / 4;
				}

			}

			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}

		if (background != null) {
			int l1 = 0;
			for (int row = 0; row < background.imageHeight; row++) {
				for (int column = 0; column < background.imageWidth; column++)
					if (background.imagePixels[l1++] != 0) {
						int i3 = column + 16 + background.drawOffsetX;
						int j3 = row + 16 + background.drawOffsetY;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}

			}

		}
	}

	@Override
	public void redraw() {
		welcomeScreenRaised = true;
	}

	private void renderChatInterface(int x, int y, int height, int scrollPosition, int scrollMaximum) {
		scrollBarUp.drawImage(x, y);
		scrollBarDown.drawImage(x, (y + height) - 16);
		DrawingArea.drawFilledRectangle(x, y + 16, 16, height - 32, SCROLLBAR_TRACK_COLOUR);
		int length = ((height - 32) * height) / scrollMaximum;
		if (length < 8)
			length = 8;
		int scrollCurrent = ((height - 32 - length) * scrollPosition) / (scrollMaximum - height);
		DrawingArea.drawFilledRectangle(x, y + 16 + scrollCurrent, 16, length, SCROLLBAR_GRIP_FOREGROUND);
		DrawingArea.drawVerticalLine(x, y + 16 + scrollCurrent, length, SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawVerticalLine(x + 1, y + 16 + scrollCurrent, length, SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawHorizontalLine(y + 16 + scrollCurrent, x, 16, SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawHorizontalLine(y + 17 + scrollCurrent, x, 16, SCROLLBAR_GRIP_HIGHLIGHT);
		DrawingArea.drawVerticalLine(x + 15, y + 16 + scrollCurrent, length, SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawVerticalLine(x + 14, y + 17 + scrollCurrent, length - 1, SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawHorizontalLine(y + 15 + scrollCurrent + length, x, 16, SCROLLBAR_GRIP_LOWLIGHT);
		DrawingArea.drawHorizontalLine(y + 14 + scrollCurrent + length, x + 1, 15, SCROLLBAR_GRIP_LOWLIGHT);
	}

	private void renderGameView() {
		renderCount++;
		renderPlayers(true);
		renderNPCs(true);
		renderPlayers(false);
		renderNPCs(false);
		renderProjectiles();
		renderStationaryGraphics();
		if (!cutsceneActive) {
			int vertical = cameraVertical;
			if (secondaryCameraVertical / 256 > vertical)
				vertical = secondaryCameraVertical / 256;
			if (customCameraActive[4] && cameraAmplitude[4] + 128 > vertical)
				vertical = cameraAmplitude[4] + 128;
			int horizontal = cameraHorizontal + cameraRandomisationA & 0x7ff;
			setCameraPosition(currentCameraPositionH, currentCameraPositionV,
					getFloorDrawHeight(plane, localPlayer.y, localPlayer.x) - 50, horizontal, vertical);
		}
		int cameraPlane;
		if (!cutsceneActive)
			cameraPlane = getWorldDrawPlane();
		else
			cameraPlane = getCameraPlaneCutscene();
		int x = cameraPositionX;
		int y = cameraPositionZ;
		int z = cameraPositionY;
		int curveY = cameraVerticalRotation;
		int curveZ = cameraHorizontalRotation;
		for (int i = 0; i < 5; i++)
			if (customCameraActive[i]) {
				int randomisation = (int) ((Math.random() * (cameraJitter[i] * 2 + 1) - cameraJitter[i])
						+ Math.sin(unknownCameraVariable[i] * (cameraFrequency[i] / 100D)) * cameraAmplitude[i]);
				if (i == 0)
					cameraPositionX += randomisation;
				if (i == 1)
					cameraPositionZ += randomisation;
				if (i == 2)
					cameraPositionY += randomisation;
				if (i == 3)
					cameraHorizontalRotation = cameraHorizontalRotation + randomisation & 0x7ff;
				if (i == 4) {
					cameraVerticalRotation += randomisation;
					if (cameraVerticalRotation < 128)
						cameraVerticalRotation = 128;
					if (cameraVerticalRotation > 383)
						cameraVerticalRotation = 383;
				}
			}

		int textureId = Rasterizer.textureGetCount;
		Model.aBoolean1684 = true;
		Model.resourceCount = 0;
		Model.cursorX = super.mouseX - 4;
		Model.cursorY = super.mouseY - 4;
		DrawingArea.clear();
		worldController.render(cameraPositionX, cameraPositionY, cameraHorizontalRotation, cameraPositionZ, cameraPlane,
				cameraVerticalRotation);
		worldController.clearInteractiveObjectCache();
		updateEntities();
		drawHeadIcon();
		animateTexture(textureId);
		draw3dScreen();
		gameScreenImageProducer.drawGraphics(4, super.gameGraphics, 4);
		cameraPositionX = x;
		cameraPositionZ = y;
		cameraPositionY = z;
		cameraVerticalRotation = curveY;
		cameraHorizontalRotation = curveZ;
	}

	private void renderMinimap(int z) {
		int pixels[] = minimapImage.pixels;
		int pixelCount = pixels.length;
		for (int pixel = 0; pixel < pixelCount; pixel++)
			pixels[pixel] = 0;

		for (int y = 1; y < 103; y++) {
			int pixel = 24628 + (103 - y) * 512 * 4;
			for (int x = 1; x < 103; x++) {
				if ((tileFlags[z][x][y] & 0x18) == 0)
					worldController.drawMinimapTile(x, y, z, pixels, pixel);
				if (z < 3 && (tileFlags[z + 1][x][y] & 8) != 0)
					worldController.drawMinimapTile(x, y, z + 1, pixels, pixel);
				pixel += 4;
			}

		}

		int primaryColour = ((238 + (int) (Math.random() * 20D)) - 10 << 16)
				+ ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
		int secondaryColour = (238 + (int) (Math.random() * 20D)) - 10 << 16;
		minimapImage.initDrawingArea();
		for (int y = 1; y < 103; y++) {
			for (int x = 1; x < 103; x++) {
				if ((tileFlags[z][x][y] & 0x18) == 0)
					drawMinimapScene(y, primaryColour, x, secondaryColour, z);
				if (z < 3 && (tileFlags[z + 1][x][y] & 8) != 0)
					drawMinimapScene(y, primaryColour, x, secondaryColour, z + 1);
			}

		}

		gameScreenImageProducer.initDrawingArea();
		minimapHintCount = 0;
		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				int hash = worldController.getGroundDecorationHash(x, y, plane);
				if (hash != 0) {
					hash = hash >> 14 & 0x7fff;
					int icon = GameObjectDefinition.getDefinition(hash).icon;

					if (icon >= 0) {
						int drawPointX = x;
						int drawPointY = y;

						// All the shop icons, it seems
						if (icon != 22 && icon != 29 && icon != 34 && icon != 36 && icon != 46 && icon != 47
								&& icon != 48) {
							byte regionWidth = 104;
							byte regionHeight = 104;
							int clippingFlags[][] = currentCollisionMap[plane].clippingData;
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

	private void renderNPCs(boolean flag) {
		for (int n = 0; n < npcCount; n++) {
			NPC npc = npcs[npcIds[n]];
			int hash = 0x20000000 + (npcIds[n] << 14);
			if (npc == null || !npc.isVisible() || npc.npcDefinition.visible != flag)
				continue;
			int npcWidth = npc.x >> 7;
			int npcHeight = npc.y >> 7;
			if (npcWidth < 0 || npcWidth >= 104 || npcHeight < 0 || npcHeight >= 104)
				continue;
			if (npc.boundaryDimension == 1 && (npc.x & 0x7f) == 64 && (npc.y & 0x7f) == 64) {
				if (tileRenderCount[npcWidth][npcHeight] == renderCount)
					continue;
				tileRenderCount[npcWidth][npcHeight] = renderCount;
			}
			if (!npc.npcDefinition.clickable)
				hash += 0x80000000;
			worldController.addEntityA(plane, npc.x, npc.y, getFloorDrawHeight(plane, npc.y, npc.x),
					npc.currentRotation, npc, hash, (npc.boundaryDimension - 1) * 64 + 60, npc.dynamic);
		}
	}

	private void renderPlayers(boolean localPlayerOnly) {
		if (localPlayer.x >> 7 == destinationX && localPlayer.y >> 7 == destinationY)
			destinationX = 0;
		int playersToRender = localPlayerCount;
		if (localPlayerOnly)
			playersToRender = 1;
		for (int p = 0; p < playersToRender; p++) {
			Player player;
			int hash;
			if (localPlayerOnly) {
				player = localPlayer;
				hash = LOCAL_PLAYER_ID << 14;
			} else {
				player = players[localPlayers[p]];
				hash = localPlayers[p] << 14;
			}
			if (player == null || !player.isVisible())
				continue;
			player.preventRotation = (lowMemory && localPlayerCount > 50 || localPlayerCount > 200) && !localPlayerOnly
					&& player.queuedAnimationId == player.standAnimationId;
			int x = player.x >> 7;
			int y = player.y >> 7;
			if (x < 0 || x >= 104 || y < 0 || y >= 104)
				continue;
			if (player.playerModel != null && tick >= player.modifiedAppearanceStartTime
					&& tick < player.modifiedAppearanceEndTime) {
				player.preventRotation = false;
				player.drawHeight2 = getFloorDrawHeight(plane, player.y, player.x);
				worldController.addEntity(player.localX, player.localY, plane, player.x, player.y, player.drawHeight2,
						player.currentRotation, player.playerTileWidth, player.playerTileHeight, player, hash);
				continue;
			}
			if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
				if (tileRenderCount[x][y] == renderCount)
					continue;
				tileRenderCount[x][y] = renderCount;
			}
			player.drawHeight2 = getFloorDrawHeight(plane, player.y, player.x);
			worldController.addEntityA(plane, player.x, player.y, player.drawHeight2, player.currentRotation, player,
					hash, 60, player.dynamic);
		}

	}

	private void renderProjectiles() {
		for (Projectile projectile = (Projectile) projectileQueue
				.peekLast(); projectile != null; projectile = (Projectile) projectileQueue.reverseGetNext())
			if (projectile.plane != plane || tick > projectile.endCycle)
				projectile.unlink();
			else if (tick >= projectile.delay) {
				if (projectile.targetId > 0) {
					NPC npc = npcs[projectile.targetId - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312)
						projectile.trackTarget(tick, npc.y,
								getFloorDrawHeight(projectile.plane, npc.y, npc.x) - projectile.endZ, npc.x);
				}
				if (projectile.targetId < 0) {
					int playerId = -projectile.targetId - 1;
					Player player;
					if (playerId == playerListId)
						player = localPlayer;
					else
						player = players[playerId];
					if (player != null && player.x >= 0 && player.x < 13312 && player.y >= 0 && player.y < 13312)
						projectile.trackTarget(tick, player.y,
								getFloorDrawHeight(projectile.plane, player.y, player.x) - projectile.endZ, player.x);
				}
				projectile.move(animationTimePassed);
				worldController.addEntityA(plane, (int) projectile.currentX, (int) projectile.currentY,
						(int) projectile.currentZ, projectile.rotationY, projectile, -1, 60, false);
			}

	}

	private void renderStationaryGraphics() {
		StationaryGraphic stationaryGraphic = (StationaryGraphic) stationaryGraphicQueue.peekLast();
		for (; stationaryGraphic != null; stationaryGraphic = (StationaryGraphic) stationaryGraphicQueue
				.reverseGetNext())
			if (stationaryGraphic.z != plane || stationaryGraphic.transformationCompleted)
				stationaryGraphic.unlink();
			else if (tick >= stationaryGraphic.stationaryGraphicLoopCycle) {
				stationaryGraphic.animationStep(animationTimePassed);
				if (stationaryGraphic.transformationCompleted)
					stationaryGraphic.unlink();
				else
					worldController.addEntityA(stationaryGraphic.z, stationaryGraphic.x, stationaryGraphic.y,
							stationaryGraphic.drawHeight, 0, stationaryGraphic, -1, 60, false);
			}

	}

	private boolean replayWave() {
		return signlink.wavereplay();
	}

	private Archive requestArchive(int i, String s, String s1, int j, int k) {
		byte abyte0[] = null;
		int l = 5;
		try {
			if (caches[0] != null)
				abyte0 = caches[0].decompress(i);
		} catch (Exception _ex) {
		}
		if (abyte0 != null) {
			// aCRC32_930.reset();
			// aCRC32_930.update(abyte0);
			// int i1 = (int)aCRC32_930.getValue();
			// if(i1 != j)
		}
		if (abyte0 != null) {
			Archive streamLoader = new Archive(abyte0);
			return streamLoader;
		}
		int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			drawLoadingText(k, "Requesting " + s);
			try {
				int k1 = 0;
				DataInputStream datainputstream = openJagGrabInputStream(s1 + j);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Buffer stream = new Buffer(abyte1);
				stream.position = 3;
				int i2 = stream.get3Bytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1)
						drawLoadingText(k, "Loading " + s + " - " + k3 + "%");
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (caches[0] != null)
						caches[0].put(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					caches[0] = null;
				}
				/*
				 * if(abyte0 != null) { aCRC32_930.reset(); aCRC32_930.update(abyte0); int i3 =
				 * (int)aCRC32_930.getValue(); if(i3 != j) { abyte0 = null; j1++; s2 =
				 * "Checksum error: " + i3; } }
				 */
			} catch (IOException ioexception) {
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else {
						drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				l *= 2;
				if (l > 60)
					l = 60;
				aBoolean872 = !aBoolean872;
			}

		}

		Archive streamLoader_1 = new Archive(abyte0);
		return streamLoader_1;
	}

	private void setupLoginScreen() {
		if (topLeft1BackgroundTile != null)
			return;
		super.fullGameScreen = null;
		chatboxImageProducer = null;
		minimapImageProducer = null;
		tabImageProducer = null;
		gameScreenImageProducer = null;
		chatSettingImageProducer = null;
		bottomSideIconImageProducer = null;
		topSideIconImageProducer = null;
		flameLeftBackground = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.clear();
		flameRightBackground = new RSImageProducer(128, 265, getGameComponent());
		DrawingArea.clear();
		topLeft1BackgroundTile = new RSImageProducer(509, 171, getGameComponent());
		DrawingArea.clear();
		bottomLeft1BackgroundTile = new RSImageProducer(360, 132, getGameComponent());
		DrawingArea.clear();
		loginBoxLeftBackgroundTile = new RSImageProducer(360, 200, getGameComponent());
		DrawingArea.clear();
		bottomLeft0BackgroundTile = new RSImageProducer(202, 238, getGameComponent());
		DrawingArea.clear();
		aRSImageProducer_1113 = new RSImageProducer(203, 238, getGameComponent());
		DrawingArea.clear();
		middleLeft1BackgroundTile = new RSImageProducer(74, 94, getGameComponent());
		DrawingArea.clear();
		aRSImageProducer_1115 = new RSImageProducer(75, 94, getGameComponent());
		DrawingArea.clear();
		if (archiveTitle != null) {
			drawLogo();
			loadTitleScreen();
		}
		welcomeScreenRaised = true;
	}

	private void setupGameplayScreen() {
		if (chatboxImageProducer != null)
			return;
		nullLoader();
		super.fullGameScreen = null;
		topLeft1BackgroundTile = null;
		bottomLeft1BackgroundTile = null;
		loginBoxLeftBackgroundTile = null;
		flameLeftBackground = null;
		flameRightBackground = null;
		bottomLeft0BackgroundTile = null;
		aRSImageProducer_1113 = null;
		middleLeft1BackgroundTile = null;
		aRSImageProducer_1115 = null;
		chatboxImageProducer = new RSImageProducer(479, 96, getGameComponent());
		minimapImageProducer = new RSImageProducer(172, 156, getGameComponent());
		DrawingArea.clear();
		minimapBackgroundImage.drawImage(0, 0);
		tabImageProducer = new RSImageProducer(190, 261, getGameComponent());
		gameScreenImageProducer = new RSImageProducer(512, 334, getGameComponent());
		DrawingArea.clear();
		chatSettingImageProducer = new RSImageProducer(496, 50, getGameComponent());
		bottomSideIconImageProducer = new RSImageProducer(269, 37, getGameComponent());
		topSideIconImageProducer = new RSImageProducer(249, 45, getGameComponent());
		welcomeScreenRaised = true;
	}

	private void resetModelCaches() {
		GameObjectDefinition.modelCache.unlinkAll();
		GameObjectDefinition.animatedModelCache.unlinkAll();
		EntityDefinition.modelCache.unlinkAll();
		ItemDefinition.modelCache.unlinkAll();
		ItemDefinition.spriteCache.unlinkAll();
		Player.mruNodes.unlinkAll();
		SpotAnimation.modelCache.unlinkAll();
	}

	private int rotateFlameColour(int r, int g, int b) {
		int alpha = 256 - b;
		return ((r & 0xff00ff) * alpha + (g & 0xff00ff) * b & 0xff00ff00)
				+ ((r & 0xff00) * alpha + (g & 0xff00) * b & 0xff0000) >> 8;
	}

	@Override
	public void run() {
		if (drawFlames) {
			drawFlames();
		} else {
			super.run();
		}
	}

	private void saveMidi(boolean flag, byte abyte0[]) {
		signlink.midiFade = flag ? 1 : 0;
		signlink.midisave(abyte0, abyte0.length);
	}

	private boolean saveWave(byte abyte0[], int i) {
		return abyte0 == null || signlink.wavesave(abyte0, i);
	}

	private void scrollInterface(int i, int j, int k, int l, RSInterface rsInterface, int i1, boolean redrawTabArea,
			int j1) {
		int anInt992;
		if (aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if (k >= i && k < i + 16 && l >= i1 && l < i1 + 16) {
			rsInterface.scrollPosition -= anInt1213 * 4;
			if (redrawTabArea) {
				redrawTab = true;
			}
		} else if (k >= i && k < i + 16 && l >= (i1 + j) - 16 && l < i1 + j) {
			rsInterface.scrollPosition += anInt1213 * 4;
			if (redrawTabArea) {
				redrawTab = true;
			}
		} else if (k >= i - anInt992 && k < i + 16 + anInt992 && l >= i1 + 16 && l < (i1 + j) - 16 && anInt1213 > 0) {
			int l1 = ((j - 32) * j) / j1;
			if (l1 < 8)
				l1 = 8;
			int i2 = l - i1 - 16 - l1 / 2;
			int j2 = j - 32 - l1;
			rsInterface.scrollPosition = ((j1 - j) * i2) / j2;
			if (redrawTabArea)
				redrawTab = true;
			aBoolean972 = true;
		}
	}

	private void setCameraPosition(int x, int y, int z, int horizontal, int vertical) {
		int verticalDifference = 2048 - vertical & 0x7ff;
		int horizontalDifference = 2048 - horizontal & 0x7ff;
		int offsetX = 0;
		int offsetZ = 0;
		int offsetY = 600 + vertical * 3;
		if (verticalDifference != 0) {
			int sine = Model.SINE[verticalDifference];
			int cos = Model.COSINE[verticalDifference];
			int tmp = offsetZ * cos - offsetY * sine >> 16;
			offsetY = offsetZ * sine + offsetY * cos >> 16;
			offsetZ = tmp;
		}
		if (horizontalDifference != 0) {
			int sin = Model.SINE[horizontalDifference];
			int cos = Model.COSINE[horizontalDifference];
			int tmp = offsetY * sin + offsetX * cos >> 16;
			offsetY = offsetY * cos - offsetX * sin >> 16;
			offsetX = tmp;
		}
		cameraPositionX = x - offsetX;
		cameraPositionZ = z - offsetZ;
		cameraPositionY = y - offsetY;
		cameraVerticalRotation = vertical;
		cameraHorizontalRotation = horizontal;
	}

	private void setCutsceneCamera() {
		int x = anInt1098 * 128 + 64;
		int y = anInt1099 * 128 + 64;
		int z = getFloorDrawHeight(plane, y, x) - anInt1100;
		if (cameraPositionX < x) {
			cameraPositionX += anInt1101 + ((x - cameraPositionX) * anInt1102) / 1000;
			if (cameraPositionX > x)
				cameraPositionX = x;
		}
		if (cameraPositionX > x) {
			cameraPositionX -= anInt1101 + ((cameraPositionX - x) * anInt1102) / 1000;
			if (cameraPositionX < x)
				cameraPositionX = x;
		}
		if (cameraPositionZ < z) {
			cameraPositionZ += anInt1101 + ((z - cameraPositionZ) * anInt1102) / 1000;
			if (cameraPositionZ > z)
				cameraPositionZ = z;
		}
		if (cameraPositionZ > z) {
			cameraPositionZ -= anInt1101 + ((cameraPositionZ - z) * anInt1102) / 1000;
			if (cameraPositionZ < z)
				cameraPositionZ = z;
		}
		if (cameraPositionY < y) {
			cameraPositionY += anInt1101 + ((y - cameraPositionY) * anInt1102) / 1000;
			if (cameraPositionY > y)
				cameraPositionY = y;
		}
		if (cameraPositionY > y) {
			cameraPositionY -= anInt1101 + ((cameraPositionY - y) * anInt1102) / 1000;
			if (cameraPositionY < y)
				cameraPositionY = y;
		}
		x = anInt995 * 128 + 64;
		y = anInt996 * 128 + 64;
		z = getFloorDrawHeight(plane, y, x) - cameraOffsetZ;
		int distanceX = x - cameraPositionX;
		int distanceZ = z - cameraPositionZ;
		int distanceY = y - cameraPositionY;
		int distanceScalar = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		int curveHorizontal = (int) (Math.atan2(distanceZ, distanceScalar) * 325.94900000000001D) & 0x7ff;
		int curveVertical = (int) (Math.atan2(distanceX, distanceY) * -325.94900000000001D) & 0x7ff;
		if (curveHorizontal < 128)
			curveHorizontal = 128;
		if (curveHorizontal > 383)
			curveHorizontal = 383;
		if (cameraVerticalRotation < curveHorizontal) {
			cameraVerticalRotation += anInt998 + ((curveHorizontal - cameraVerticalRotation) * anInt999) / 1000;
			if (cameraVerticalRotation > curveHorizontal)
				cameraVerticalRotation = curveHorizontal;
		}
		if (cameraVerticalRotation > curveHorizontal) {
			cameraVerticalRotation -= anInt998 + ((cameraVerticalRotation - curveHorizontal) * anInt999) / 1000;
			if (cameraVerticalRotation < curveHorizontal)
				cameraVerticalRotation = curveHorizontal;
		}
		int _vertical1 = curveVertical - cameraHorizontalRotation;
		if (_vertical1 > 1024)
			_vertical1 -= 2048;
		if (_vertical1 < -1024)
			_vertical1 += 2048;
		if (_vertical1 > 0) {
			cameraHorizontalRotation += anInt998 + (_vertical1 * anInt999) / 1000;
			cameraHorizontalRotation &= 0x7ff;
		}
		if (_vertical1 < 0) {
			cameraHorizontalRotation -= anInt998 + (-_vertical1 * anInt999) / 1000;
			cameraHorizontalRotation &= 0x7ff;
		}
		int _vertical2 = curveVertical - cameraHorizontalRotation;
		if (_vertical2 > 1024)
			_vertical2 -= 2048;
		if (_vertical2 < -1024)
			_vertical2 += 2048;
		if (_vertical2 < 0 && _vertical1 > 0 || _vertical2 > 0 && _vertical1 < 0)
			cameraHorizontalRotation = curveVertical;
	}

	private void setStandardCameraPosition() {
		try {
			int x = localPlayer.x + cameraRandomisationH;
			int y = localPlayer.y + cameraRandomisationV;
			if (currentCameraPositionH - x < -500 || currentCameraPositionH - x > 500
					|| currentCameraPositionV - y < -500 || currentCameraPositionV - y > 500) {
				currentCameraPositionH = x;
				currentCameraPositionV = y;
			}

			if (currentCameraPositionH != x)
				currentCameraPositionH += (x - currentCameraPositionH) / 16;
			if (currentCameraPositionV != y)
				currentCameraPositionV += (y - currentCameraPositionV) / 16;
			if (super.keyStatus[1] == 1)
				cameraModificationH += (-24 - cameraModificationH) / 2;
			else if (super.keyStatus[2] == 1)
				cameraModificationH += (24 - cameraModificationH) / 2;
			else
				cameraModificationH /= 2;
			if (super.keyStatus[3] == 1)
				cameraModificationV += (12 - cameraModificationV) / 2;
			else if (super.keyStatus[4] == 1)
				cameraModificationV += (-12 - cameraModificationV) / 2;
			else
				cameraModificationV /= 2;
			cameraHorizontal = cameraHorizontal + cameraModificationH / 2 & 0x7ff;
			cameraVertical += cameraModificationV / 2;
			if (cameraVertical < 128)
				cameraVertical = 128;
			if (cameraVertical > 383)
				cameraVertical = 383;

			int maximumX = currentCameraPositionH >> 7;
			int maximumY = currentCameraPositionV >> 7;
			int drawHeight = getFloorDrawHeight(plane, currentCameraPositionV, currentCameraPositionH);
			int maximumDrawHeight = 0;
			if (maximumX > 3 && maximumY > 3 && maximumX < 100 && maximumY < 100) {
				for (int _x = maximumX - 4; _x <= maximumX + 4; _x++) {
					for (int _y = maximumY - 4; _y <= maximumY + 4; _y++) {
						int _z = plane;
						if (_z < 3 && (tileFlags[1][_x][_y] & 2) == 2)
							_z++;
						int h = drawHeight - intGroundArray[_z][_x][_y];
						if (h > maximumDrawHeight)
							maximumDrawHeight = h;
					}
				}
			}
			int h = maximumDrawHeight * 192;
			if (h > 0x17f00)
				h = 0x17f00;
			if (h < 32768)
				h = 32768;
			if (h > secondaryCameraVertical) {
				secondaryCameraVertical += (h - secondaryCameraVertical) / 24;
				return;
			}
			if (h < secondaryCameraVertical) {
				secondaryCameraVertical += (h - secondaryCameraVertical) / 80;
			}
		} catch (Exception _ex) {
			signlink.reporterror("glfc_ex " + localPlayer.x + "," + localPlayer.y + "," + currentCameraPositionH + ","
					+ currentCameraPositionV + "," + regionX + "," + regionY + "," + baseX + "," + baseY);
			throw new RuntimeException("eek");
		}
	}

	private void setWaveVolume(int i) {
		signlink.wavevol = i;
	}

	private void showErrorScreen() {
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		setFrameRate(1);
		if (loadingError) {
			currentlyDrawingFlames = false;
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int currentPositionY = 35;
			g.drawString("Sorry, an error has occured whilst loading RuneScape", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
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
		if (genericLoadingError) {
			currentlyDrawingFlames = false;
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play RuneScape make sure you play from", 50, 100);
			g.drawString("http://www.runescape.com", 50, 150);
		}
		if (rsAlreadyLoaded) {
			currentlyDrawingFlames = false;
			g.setColor(Color.yellow);
			int currentPositionY = 35;
			g.drawString("Error a copy of RuneScape already appears to be loaded", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, currentPositionY);
			currentPositionY += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, currentPositionY);
			currentPositionY += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, currentPositionY);
			currentPositionY += 30;
		}
	}

	private void spawnGameObjects() {
		if (loadingStage == 2) {
			for (GameObjectSpawnRequest spawnRequest = (GameObjectSpawnRequest) spawnObjectList
					.peekLast(); spawnRequest != null; spawnRequest = (GameObjectSpawnRequest) spawnObjectList
							.reverseGetNext()) {
				if (spawnRequest.delayUntilRespawn > 0)
					spawnRequest.delayUntilRespawn--;
				if (spawnRequest.delayUntilRespawn == 0) {
					if (spawnRequest.id < 0 || Region.modelTypeCached(spawnRequest.id, spawnRequest.type)) {
						despawnGameObject(spawnRequest.y, spawnRequest.z, spawnRequest.face, spawnRequest.type,
								spawnRequest.x, spawnRequest.objectType, spawnRequest.id);
						spawnRequest.unlink();
					}
				} else {
					if (spawnRequest.delayUntilSpawn > 0)
						spawnRequest.delayUntilSpawn--;
					if (spawnRequest.delayUntilSpawn == 0 && spawnRequest.x >= 1 && spawnRequest.y >= 1
							&& spawnRequest.x <= 102 && spawnRequest.y <= 102
							&& (spawnRequest.id2 < 0 || Region.modelTypeCached(spawnRequest.id2, spawnRequest.type2))) {
						despawnGameObject(spawnRequest.y, spawnRequest.z, spawnRequest.face2, spawnRequest.type2,
								spawnRequest.x, spawnRequest.objectType, spawnRequest.id2);
						spawnRequest.delayUntilSpawn = -1;
						if (spawnRequest.id2 == spawnRequest.id && spawnRequest.id == -1)
							spawnRequest.unlink();
						else if (spawnRequest.id2 == spawnRequest.id && spawnRequest.face2 == spawnRequest.face
								&& spawnRequest.type2 == spawnRequest.type)
							spawnRequest.unlink();
					}
				}
			}
		}
	}

	private void spawnGroundItem(int x, int y) {
		NodeList groundItemList = groundArray[plane][x][y];
		if (groundItemList == null) {
			worldController.removeGroundItemTile(x, y, plane);
			return;
		}
		int highestValue = -99999999;
		Object item = null;
		for (Item currentItem = (Item) groundItemList
				.peekLast(); currentItem != null; currentItem = (Item) groundItemList.reverseGetNext()) {
			ItemDefinition itemDef = ItemDefinition.getDefinition(currentItem.itemId);
			int value = itemDef.value;
			if (itemDef.stackable)
				value *= currentItem.itemCount + 1;
			if (value > highestValue) {
				highestValue = value;
				item = currentItem;
			}
		}

		groundItemList.insertTail(((Link) (item)));
		Object secondItem = null;
		Object thirdItem = null;
		for (Item currentItem = (Item) groundItemList
				.peekLast(); currentItem != null; currentItem = (Item) groundItemList.reverseGetNext()) {
			if (currentItem.itemId != ((Item) (item)).itemId && secondItem == null)
				secondItem = currentItem;
			if (currentItem.itemId != ((Item) (item)).itemId && currentItem.itemId != ((Item) (secondItem)).itemId
					&& thirdItem == null)
				thirdItem = currentItem;
		}

		int hash = x + (y << 7) + 0x60000000;
		worldController.addGroundItemTile(x, y, plane, getFloorDrawHeight(plane, y * 128 + 64, x * 128 + 64), hash,
				((Animable) (item)), ((Animable) (secondItem)), ((Animable) (thirdItem)));
	}

	@Override
	public void startRunnable(Runnable runnable, int priority) {
		if (priority > 10)
			priority = 10;
		if (signlink.applet != null) {
			signlink.startThread(runnable, priority);
		} else {
			super.startRunnable(runnable, priority);
		}
	}

	@Override
	void startUp() {
		drawLoadingText(20, "Starting up");
		if (signlink.sunjava)
			super.minDelay = 5;
		if (clientRunning) {
			// rsAlreadyLoaded = true;
			// return;
		}
		clientRunning = true;
		boolean validHost = true;
		String s = getDocumentBaseHost();
		if (s.endsWith("jagex.com"))
			validHost = true;
		if (s.endsWith("runescape.com"))
			validHost = true;
		if (s.endsWith("192.168.1.2"))
			validHost = true;
		if (s.endsWith("192.168.1.229"))
			validHost = true;
		if (s.endsWith("192.168.1.228"))
			validHost = true;
		if (s.endsWith("192.168.1.227"))
			validHost = true;
		if (s.endsWith("192.168.1.226"))
			validHost = true;
		if (s.endsWith("127.0.0.1"))
			validHost = true;
		if (!validHost) {
			genericLoadingError = true;
			return;
		}
		if (signlink.cache_dat != null) {
			for (int i = 0; i < 5; i++)
				caches[i] = new FileCache(signlink.cache_dat, signlink.cache_idx[i], i + 1);

		}
		try {
			connectServer();
			archiveTitle = requestArchive(1, "title screen", "title", expectedCRCs[1], 25);
			fontSmall = new GameFont("p11_full", archiveTitle, false);
			fontPlain = new GameFont("p12_full", archiveTitle, false);
			fontBold = new GameFont("b12_full", archiveTitle, false);
			GameFont fontFancy = new GameFont("q8_full", archiveTitle, true);
			drawLogo();
			loadTitleScreen();
			Archive archiveConfig = requestArchive(2, "config", "config", expectedCRCs[2], 30);
			Archive archiveInterface = requestArchive(3, "interface", "interface", expectedCRCs[3], 35);
			Archive archiveMedia = requestArchive(4, "2d graphics", "media", expectedCRCs[4], 40);
			Archive archiveTextures = requestArchive(6, "textures", "textures", expectedCRCs[6], 45);
			Archive archiveWord = requestArchive(7, "chat system", "wordenc", expectedCRCs[7], 50);
			Archive archiveSounds = requestArchive(8, "sound effects", "sounds", expectedCRCs[8], 55);
			tileFlags = new byte[4][104][104];
			intGroundArray = new int[4][105][105];
			worldController = new WorldController(intGroundArray);
			for (int z = 0; z < 4; z++)
				currentCollisionMap[z] = new CollisionMap();

			minimapImage = new Sprite(512, 512);
			Archive archiveVersions = requestArchive(5, "update list", "versionlist", expectedCRCs[5], 60);
			drawLoadingText(60, "Connecting to update server");
			onDemandFetcher = new OnDemandFetcher();
			onDemandFetcher.start(archiveVersions, this);
			Animation.init(onDemandFetcher.getAnimCount());
			Model.init(onDemandFetcher.fileCount(0), onDemandFetcher);
			if (!lowMemory) {
				nextSong = 0;
				try {
					nextSong = Integer.parseInt(getParameter("music"));
				} catch (Exception _ex) {
				}
				songChanging = true;
				onDemandFetcher.request(2, nextSong);
				while (onDemandFetcher.immediateRequestCount() > 0) {
					processOnDemandQueue();
					try {
						Thread.sleep(100L);
					} catch (Exception _ex) {
					}
					if (onDemandFetcher.failedRequests > 3) {
						loadError();
						return;
					}
				}
			}
			drawLoadingText(65, "Requesting animations");
			int fileRequestCount = onDemandFetcher.fileCount(1);
			for (int id = 0; id < fileRequestCount; id++)
				onDemandFetcher.request(1, id);

			while (onDemandFetcher.immediateRequestCount() > 0) {
				int remaining = fileRequestCount - onDemandFetcher.immediateRequestCount();
				if (remaining > 0)
					drawLoadingText(65, "Loading animations - " + (remaining * 100) / fileRequestCount + "%");
				processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch (Exception _ex) {
				}
				if (onDemandFetcher.failedRequests > 3) {
					loadError();
					return;
				}
			}
			drawLoadingText(70, "Requesting models");
			fileRequestCount = onDemandFetcher.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				int modelId = onDemandFetcher.getModelId(id);
				if ((modelId & 1) != 0)
					onDemandFetcher.request(0, id);
			}

			fileRequestCount = onDemandFetcher.immediateRequestCount();
			while (onDemandFetcher.immediateRequestCount() > 0) {
				int remaining = fileRequestCount - onDemandFetcher.immediateRequestCount();
				if (remaining > 0)
					drawLoadingText(70, "Loading models - " + (remaining * 100) / fileRequestCount + "%");
				processOnDemandQueue();
				try {
					Thread.sleep(100L);
				} catch (Exception _ex) {
				}
			}
			if (caches[0] != null) {
				drawLoadingText(75, "Requesting maps");
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 47, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 47, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 48, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 48, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 49, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 49, 48));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 47, 47));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 47, 47));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 48, 47));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 48, 47));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(0, 48, 148));
				onDemandFetcher.request(3, onDemandFetcher.getMapId(1, 48, 148));
				fileRequestCount = onDemandFetcher.immediateRequestCount();
				while (onDemandFetcher.immediateRequestCount() > 0) {
					int remaining = fileRequestCount - onDemandFetcher.immediateRequestCount();
					if (remaining > 0)
						drawLoadingText(75, "Loading maps - " + (remaining * 100) / fileRequestCount + "%");
					processOnDemandQueue();
					try {
						Thread.sleep(100L);
					} catch (Exception _ex) {
					}
				}
			}
			fileRequestCount = onDemandFetcher.fileCount(0);
			for (int id = 0; id < fileRequestCount; id++) {
				int modelId = onDemandFetcher.getModelId(id);
				byte priority = 0;
				if ((modelId & 8) != 0)
					priority = 10;
				else if ((modelId & 0x20) != 0)
					priority = 9;
				else if ((modelId & 0x10) != 0)
					priority = 8;
				else if ((modelId & 0x40) != 0)
					priority = 7;
				else if ((modelId & 0x80) != 0)
					priority = 6;
				else if ((modelId & 2) != 0)
					priority = 5;
				else if ((modelId & 4) != 0)
					priority = 4;
				if ((modelId & 1) != 0)
					priority = 3;
				if (priority != 0)
					onDemandFetcher.setPriority(priority, 0, id);
			}

			onDemandFetcher.preloadRegions(membersWorld);
			if (!lowMemory) {
				int count = onDemandFetcher.fileCount(2);
				for (int id = 1; id < count; id++)
					if (onDemandFetcher.midiIdEqualsOne(id))
						onDemandFetcher.setPriority((byte) 1, 2, id);

			}
			drawLoadingText(80, "Unpacking media");
			inventoryBackgroundImage = new Background(archiveMedia, "invback", 0);
			chatBackgroundImage = new Background(archiveMedia, "chatback", 0);
			minimapBackgroundImage = new Background(archiveMedia, "mapback", 0);
			backBase1Image = new Background(archiveMedia, "backbase1", 0);
			backBase2Image = new Background(archiveMedia, "backbase2", 0);
			backHmid1Image = new Background(archiveMedia, "backhmid1", 0);
			for (int icon = 0; icon < 13; icon++)
				sideIconImage[icon] = new Background(archiveMedia, "sideicons", icon);

			minimapCompassImage = new Sprite(archiveMedia, "compass", 0);
			minimapEdgeImage = new Sprite(archiveMedia, "mapedge", 0);
			minimapEdgeImage.trim();
			try {
				for (int i = 0; i < 100; i++)
					mapSceneImage[i] = new Background(archiveMedia, "mapscene", i);

			} catch (Exception _ex) {
			}
			try {
				for (int i = 0; i < 100; i++)
					mapFunctionImage[i] = new Sprite(archiveMedia, "mapfunction", i);

			} catch (Exception _ex) {
			}
			try {
				for (int i = 0; i < 20; i++) {
					hitMarkImage[i] = new Sprite(archiveMedia, "hitmarks", i);
				}

			} catch (Exception _ex) {
			}
			try {
				for (int i = 0; i < 20; i++) {
					headIcons[i] = new Sprite(archiveMedia, "headicons", i);
				}
			} catch (Exception _ex) {
				_ex.printStackTrace();
			}
			mapFlag = new Sprite(archiveMedia, "mapmarker", 0);
			mapMarker = new Sprite(archiveMedia, "mapmarker", 1);
			for (int i = 0; i < 8; i++)
				crosses[i] = new Sprite(archiveMedia, "cross", i);

			mapDotItem = new Sprite(archiveMedia, "mapdots", 0);
			mapDotNPC = new Sprite(archiveMedia, "mapdots", 1);
			mapDotPlayer = new Sprite(archiveMedia, "mapdots", 2);
			mapDotFriend = new Sprite(archiveMedia, "mapdots", 3);
			mapDotTeam = new Sprite(archiveMedia, "mapdots", 4);
			scrollBarUp = new Background(archiveMedia, "scrollbar", 0);
			scrollBarDown = new Background(archiveMedia, "scrollbar", 1);
			redStone1 = new Background(archiveMedia, "redstone1", 0);
			redStone2 = new Background(archiveMedia, "redstone2", 0);
			redStone3 = new Background(archiveMedia, "redstone3", 0);
			redStone1_2 = new Background(archiveMedia, "redstone1", 0);
			redStone1_2.method358();
			redStone2_2 = new Background(archiveMedia, "redstone2", 0);
			redStone2_2.method358();
			redStone1_3 = new Background(archiveMedia, "redstone1", 0);
			redStone1_3.method359();
			redStone2_3 = new Background(archiveMedia, "redstone2", 0);
			redStone2_3.method359();
			redStone3_2 = new Background(archiveMedia, "redstone3", 0);
			redStone3_2.method359();
			redStone1_4 = new Background(archiveMedia, "redstone1", 0);
			redStone1_4.method358();
			redStone1_4.method359();
			redStone2_4 = new Background(archiveMedia, "redstone2", 0);
			redStone2_4.method358();
			redStone2_4.method359();
			for (int i = 0; i < 2; i++)
				modIcons[i] = new Background(archiveMedia, "mod_icons", i);

			Sprite sprite = new Sprite(archiveMedia, "backleft1", 0);
			backLeftIP1 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backleft2", 0);
			backLeftIP2 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backright1", 0);
			backRightIP1 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backright2", 0);
			backRightIP2 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backtop1", 0);
			backTopIP1 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid1", 0);
			backVmidIP1 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid2", 0);
			backVmidIP2 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backvmid3", 0);
			backVmidIP3 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
			sprite = new Sprite(archiveMedia, "backhmid2", 0);
			backVmidIP2_2 = new RSImageProducer(sprite.width, sprite.height, getGameComponent());
			sprite.drawInverse(0, 0);
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

			drawLoadingText(83, "Unpacking textures");
			Rasterizer.unpackTextures(archiveTextures);
			Rasterizer.calculatePalette(0.80000000000000004D);
			Rasterizer.resetTextures();
			drawLoadingText(86, "Unpacking config");
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
				drawLoadingText(90, "Unpacking sounds");
				byte soundData[] = archiveSounds.decompressFile("sounds.dat");
				Buffer stream = new Buffer(soundData);
				Effect.load(stream);
			}
			drawLoadingText(95, "Unpacking interfaces");
			GameFont fonts[] = { fontSmall, fontPlain, fontBold, fontFancy };
			RSInterface.unpack(archiveInterface, fonts, archiveMedia);
			drawLoadingText(100, "Preparing game engine");
			for (int _y = 0; _y < 33; _y++) {
				int firstXOfLine = 999;
				int lastXOfLine = 0;
				for (int _x = 0; _x < 34; _x++) {
					if (minimapBackgroundImage.imagePixels[_x + _y * minimapBackgroundImage.imageWidth] == 0) {
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
					if (minimapBackgroundImage.imagePixels[_x + _y * minimapBackgroundImage.imageWidth] == 0
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

			Rasterizer.setBounds(479, 96);
			chatboxLineOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(190, 261);
			sidebarOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(512, 334);
			viewportOffsets = Rasterizer.lineOffsets;

			int ai[] = new int[9];
			for (int i8 = 0; i8 < 9; i8++) {
				int k8 = 128 + i8 * 32 + 15;
				int l8 = 600 + k8 * 3;
				int i9 = Rasterizer.SINE[k8];
				ai[i8] = l8 * i9 >> 16;
			}

			WorldController.setupViewport(500, 800, 512, 334, ai);
			Censor.load(archiveWord);
			mouseDetection = new MouseDetection(this);
			startRunnable(mouseDetection, 10);
			GameObject.clientInstance = this;
			GameObjectDefinition.clientInstance = this;
			EntityDefinition.clientInstance = this;
			return;
		} catch (Exception exception) {
			signlink.reporterror("loaderror " + loadingBarText + " " + loadingBarPercentage);
		}
		loadingError = true;
	}

	private void stopMidi() {
		signlink.midiFade = 0;
		signlink.midi = "stop";
	}

	private void updateEntities() {
		try {
			int overheadMessage = 0;
			for (int entity = -1; entity < localPlayerCount + npcCount; entity++) {
				Entity target;
				if (entity == -1)
					target = localPlayer;
				else if (entity < localPlayerCount)
					target = players[localPlayers[entity]];
				else
					target = npcs[npcIds[entity - localPlayerCount]];
				if (target == null || !(target).isVisible())
					continue;
				if (target instanceof NPC) {
					EntityDefinition definition = ((NPC) target).npcDefinition;
					if (definition.childrenIDs != null)
						definition = definition.getChildDefinition();
					if (definition == null)
						continue;
				}
				if (entity < localPlayerCount) {
					int height = 30;
					Player player = (Player) target;
					if (player.headIcon != 0) {
						calculateEntityScreenPosition(((target)), (target).height + 15);
						if (spriteDrawX > -1) {
							for (int icon = 0; icon < 8; icon++)
								if ((player.headIcon & 1 << icon) != 0) {
									headIcons[icon].drawImage(spriteDrawX - 12, spriteDrawY - height);
									height -= 25;
								}

						}
					}
					if (entity >= 0 && hintIconType == 10 && hintIconPlayerId == localPlayers[entity]) {
						calculateEntityScreenPosition(((target)), (target).height + 15);
						if (spriteDrawX > -1)
							headIcons[7].drawImage(spriteDrawX - 12, spriteDrawY - height);
					}
				} else {
					EntityDefinition definition = ((NPC) target).npcDefinition;
					if (definition.headIcon >= 0 && definition.headIcon < headIcons.length) {
						calculateEntityScreenPosition(((target)), (target).height + 15);
						if (spriteDrawX > -1)
							headIcons[definition.headIcon].drawImage(spriteDrawX - 12, spriteDrawY - 30);
					}
					if (hintIconType == 1 && hintIconNpcId == npcIds[entity - localPlayerCount] && tick % 20 < 10) {
						calculateEntityScreenPosition(((target)), (target).height + 15);
						if (spriteDrawX > -1)
							headIcons[2].drawImage(spriteDrawX - 12, spriteDrawY - 28);
					}
				}
				if ((target).overheadTextMessage != null && (entity >= localPlayerCount || publicChatMode == 0
						|| publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) target).name))) {
					calculateEntityScreenPosition(((target)), (target).height);
					if (spriteDrawX > -1 && overheadMessage < overheadMessageCount) {
						overheadTextWidth[overheadMessage] = fontBold.getTextWidth((target).overheadTextMessage) / 2;
						overheadTextHeight[overheadMessage] = fontBold.fontHeight;
						overheadTextDrawX[overheadMessage] = spriteDrawX;
						overheadTextDrawY[overheadMessage] = spriteDrawY;
						overheadTextColour[overheadMessage] = (target).chatColour;
						overheadTextEffect[overheadMessage] = (target).chatEffect;
						overheadTextCycle[overheadMessage] = (target).textCycle;
						overheadTextMessage[overheadMessage++] = (target).overheadTextMessage;
						if (chatEffectsDisabled == 0 && (target).chatEffect >= 1 && (target).chatEffect <= 3) {
							overheadTextHeight[overheadMessage] += 10;
							overheadTextDrawY[overheadMessage] += 5;
						}
						if (chatEffectsDisabled == 0 && (target).chatEffect == 4)
							overheadTextWidth[overheadMessage] = 60;
						if (chatEffectsDisabled == 0 && (target).chatEffect == 5)
							overheadTextHeight[overheadMessage] += 5;
					}
				}
				if ((target).loopCycleStatus > tick) {
					try {
						calculateEntityScreenPosition(((target)), (target).height + 15);
						if (spriteDrawX > -1) {
							int percentage = ((target).currentHealth * 30) / (target).maxHealth;
							if (percentage > 30)
								percentage = 30;
							DrawingArea.drawFilledRectangle(spriteDrawX - 15, spriteDrawY - 3, percentage, 5, 65280);
							DrawingArea.drawFilledRectangle((spriteDrawX - 15) + percentage, spriteDrawY - 3,
									30 - percentage, 5, 0xff0000);
						}
					} catch (Exception e) {
					}
				}
				for (int hit = 0; hit < 4; hit++)
					if ((target).hitsLoopCycle[hit] > tick) {
						calculateEntityScreenPosition(((target)), (target).height / 2);
						if (spriteDrawX > -1) {
							if (hit == 1)
								spriteDrawY -= 20;
							if (hit == 2) {
								spriteDrawX -= 15;
								spriteDrawY -= 10;
							}
							if (hit == 3) {
								spriteDrawX += 15;
								spriteDrawY -= 10;
							}
							hitMarkImage[(target).hitMarkTypes[hit]].drawImage(spriteDrawX - 12, spriteDrawY - 12);
							fontSmall.drawCentredText(String.valueOf((target).hitArray[hit]), spriteDrawX,
									spriteDrawY + 4, 0);
							fontSmall.drawCentredText(String.valueOf((target).hitArray[hit]), spriteDrawX - 1,
									spriteDrawY + 3, 0xffffff);
						}
					}

			}
			for (int m = 0; m < overheadMessage; m++) {
				int x = overheadTextDrawX[m];
				int y = overheadTextDrawY[m];
				int w = overheadTextWidth[m];
				int h = overheadTextHeight[m];
				boolean messagesRemaining = true;
				while (messagesRemaining) {
					messagesRemaining = false;
					for (int _m = 0; _m < m; _m++)
						if (y + 2 > overheadTextDrawY[_m] - overheadTextHeight[_m] && y - h < overheadTextDrawY[_m] + 2
								&& x - w < overheadTextDrawX[_m] + overheadTextWidth[_m]
								&& x + w > overheadTextDrawX[_m] - overheadTextWidth[_m]
								&& overheadTextDrawY[_m] - overheadTextHeight[_m] < y) {
							y = overheadTextDrawY[_m] - overheadTextHeight[_m];
							messagesRemaining = true;
						}

				}
				spriteDrawX = overheadTextDrawX[m];
				spriteDrawY = overheadTextDrawY[m] = y;
				String message = overheadTextMessage[m];
				if (chatEffectsDisabled == 0) {
					int colour = 0xffff00;
					if (overheadTextColour[m] < 6)
						colour = SPOKEN_TEXT_COLOURS[overheadTextColour[m]];
					if (overheadTextColour[m] == 6)
						colour = renderCount % 20 >= 10 ? 0xffff00 : 0xff0000;
					if (overheadTextColour[m] == 7)
						colour = renderCount % 20 >= 10 ? 65535 : 255;
					if (overheadTextColour[m] == 8)
						colour = renderCount % 20 >= 10 ? 0x80ff80 : 45056;
					if (overheadTextColour[m] == 9) {
						int cycle = 150 - overheadTextCycle[m];
						if (cycle < 50)
							colour = 0xff0000 + 1280 * cycle;
						else if (cycle < 100)
							colour = 0xffff00 - 0x50000 * (cycle - 50);
						else if (cycle < 150)
							colour = 65280 + 5 * (cycle - 100);
					}
					if (overheadTextColour[m] == 10) {
						int cycle = 150 - overheadTextCycle[m];
						if (cycle < 50)
							colour = 0xff0000 + 5 * cycle;
						else if (cycle < 100)
							colour = 0xff00ff - 0x50000 * (cycle - 50);
						else if (cycle < 150)
							colour = (255 + 0x50000 * (cycle - 100)) - 5 * (cycle - 100);
					}
					if (overheadTextColour[m] == 11) {
						int cycle = 150 - overheadTextCycle[m];
						if (cycle < 50)
							colour = 0xffffff - 0x50005 * cycle;
						else if (cycle < 100)
							colour = 65280 + 0x50005 * (cycle - 50);
						else if (cycle < 150)
							colour = 0xffffff - 0x50000 * (cycle - 100);
					}
					if (overheadTextEffect[m] == 0) {
						fontBold.drawCentredText(message, spriteDrawX, spriteDrawY + 1, 0);
						fontBold.drawCentredText(message, spriteDrawX, spriteDrawY, colour);
					}
					if (overheadTextEffect[m] == 1) {
						fontBold.drawVerticalSineWaveText(message, spriteDrawX, spriteDrawY + 1, 0, renderCount);
						fontBold.drawVerticalSineWaveText(message, spriteDrawX, spriteDrawY, colour, renderCount);
					}
					if (overheadTextEffect[m] == 2) {
						fontBold.drawVerticalHorizontalSineWaveText(message, spriteDrawX, spriteDrawY + 1, 0,
								renderCount);
						fontBold.drawVerticalHorizontalSineWaveText(message, spriteDrawX, spriteDrawY, colour,
								renderCount);
					}
					if (overheadTextEffect[m] == 3) {
						fontBold.drawShakingText(message, spriteDrawX, spriteDrawY + 1, 0, 150 - overheadTextCycle[m],
								renderCount);
						fontBold.drawShakingText(message, spriteDrawX, spriteDrawY, colour, 150 - overheadTextCycle[m],
								renderCount);
					}
					if (overheadTextEffect[m] == 4) {
						int width = fontBold.getTextWidth(message);
						int offsetX = ((150 - overheadTextCycle[m]) * (width + 100)) / 150;
						DrawingArea.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
						fontBold.drawText(message, (spriteDrawX + 50) - offsetX, spriteDrawY + 1, 0);
						fontBold.drawText(message, (spriteDrawX + 50) - offsetX, spriteDrawY, colour);
						DrawingArea.defaultDrawingAreaSize();
					}
					if (overheadTextEffect[m] == 5) {
						int cycle = 150 - overheadTextCycle[m];
						int offsetY = 0;
						if (cycle < 25)
							offsetY = cycle - 25;
						else if (cycle > 125)
							offsetY = cycle - 125;
						DrawingArea.setDrawingArea(spriteDrawY + 5, 0, 512, spriteDrawY - fontBold.fontHeight - 1);
						fontBold.drawCentredText(message, spriteDrawX, spriteDrawY + 1 + offsetY, 0);
						fontBold.drawCentredText(message, spriteDrawX, spriteDrawY + offsetY, colour);
						DrawingArea.defaultDrawingAreaSize();
					}
				} else {
					fontBold.drawCentredText(message, spriteDrawX, spriteDrawY + 1, 0);
					fontBold.drawCentredText(message, spriteDrawX, spriteDrawY, 0xffff00);
				}
			}
		} catch (Exception e) {
		}

	}

	private void updateEntity(Entity entity) {
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
		if (entity.tickStart > tick)
			updatePosition(entity);
		else if (entity.tickEnd >= tick)
			updateFacingDirection(entity);
		else
			processWalkingStep(entity);
		appendFocusDestination(entity);
		appendAnimation(entity);
	}

	private void updateFacingDirection(Entity entity) {
		if (entity.tickEnd == tick || entity.animation == -1 || entity.animationDelay != 0
				|| entity.currentAnimationDuration + 1 > AnimationSequence.animations[entity.animation]
						.getFrameLength(entity.currentAnimationFrame)) {
			int duration = entity.tickEnd - entity.tickStart;
			int timePassed = tick - entity.tickStart;
			int differenceStartX = entity.startX * 128 + entity.boundaryDimension * 64;
			int differenceStartY = entity.startY * 128 + entity.boundaryDimension * 64;
			int differenceEndX = entity.endX * 128 + entity.boundaryDimension * 64;
			int differenceEndY = entity.endY * 128 + entity.boundaryDimension * 64;
			entity.x = (differenceStartX * (duration - timePassed) + differenceEndX * timePassed) / duration;
			entity.y = (differenceStartY * (duration - timePassed) + differenceEndY * timePassed) / duration;
		}
		entity.stepsDelayed = 0;
		if (entity.direction == 0)
			entity.turnDirection = 1024;
		if (entity.direction == 1)
			entity.turnDirection = 1536;
		if (entity.direction == 2)
			entity.turnDirection = 0;
		if (entity.direction == 3)
			entity.turnDirection = 512;
		entity.currentRotation = entity.turnDirection;
	}

	private void updateLocalPlayerMovement(Buffer stream) {
		stream.initBitAccess();
		int currentlyUpdating = stream.readBits(1);
		if (currentlyUpdating == 0)
			return;
		int movementUpdateType = stream.readBits(2);
		if (movementUpdateType == 0) {
			playersObserved[playersObservedCount++] = LOCAL_PLAYER_ID;
			return;
		}
		if (movementUpdateType == 1) {
			int direction = stream.readBits(3);
			localPlayer.move(false, direction);
			int furtherUpdateRequired = stream.readBits(1);
			if (furtherUpdateRequired == 1)
				playersObserved[playersObservedCount++] = LOCAL_PLAYER_ID;
			return;
		}
		if (movementUpdateType == 2) {
			int lastDirection = stream.readBits(3);
			localPlayer.move(true, lastDirection);
			int currentDirection = stream.readBits(3);
			localPlayer.move(true, currentDirection);
			int updateRequired = stream.readBits(1);
			if (updateRequired == 1)
				playersObserved[playersObservedCount++] = LOCAL_PLAYER_ID;
			return;
		}
		if (movementUpdateType == 3) {
			plane = stream.readBits(2);
			int clearWaypointQueue = stream.readBits(1);
			int updateRequired = stream.readBits(1);
			if (updateRequired == 1)
				playersObserved[playersObservedCount++] = LOCAL_PLAYER_ID;
			int x = stream.readBits(7);
			int y = stream.readBits(7);
			localPlayer.setPos(y, x, clearWaypointQueue == 1);
		}
	}

	private void updateNPCBlock(Buffer stream) {
		for (int n = 0; n < playersObservedCount; n++) {
			int npcId = playersObserved[n];
			NPC npc = npcs[npcId];
			int updateType = stream.getUnsignedByte();
			if ((updateType & 0x10) != 0) {
				int animationId = stream.getUnsignedShort();
				if (animationId == 65535)
					animationId = -1;
				int animationDelay = stream.getUnsignedByte();
				if (animationId == npc.animation && animationId != -1) {
					int replayMode = AnimationSequence.animations[animationId].replayMode;
					if (replayMode == 1) {
						npc.currentAnimationFrame = 0;
						npc.currentAnimationDuration = 0;
						npc.animationDelay = animationDelay;
						npc.currentAnimationLoopCount = 0;
					}
					if (replayMode == 2)
						npc.currentAnimationLoopCount = 0;
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
				int hitDamage = stream.getUnsignedByteA();
				int hitType = stream.getUnsignedByteC();
				npc.updateHitData(hitType, hitDamage, tick);
				npc.loopCycleStatus = tick + 300;
				npc.currentHealth = stream.getUnsignedByteA();
				npc.maxHealth = stream.getUnsignedByte();
			}
			if ((updateType & 0x80) != 0) {
				npc.graphicId = stream.getUnsignedLEShort();
				int delay = stream.getInt();
				npc.graphicHeight = delay >> 16;
				npc.graphicEndCycle = tick + (delay & 0xffff);
				npc.currentAnimationId = 0;
				npc.currentAnimationTimeRemaining = 0;
				if (npc.graphicEndCycle > tick)
					npc.currentAnimationId = -1;
				if (npc.graphicId == 65535)
					npc.graphicId = -1;
			}
			if ((updateType & 0x20) != 0) {
				npc.interactingEntity = stream.getUnsignedLEShort();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((updateType & 1) != 0) {
				npc.overheadTextMessage = stream.getString();
				npc.textCycle = 100;
			}
			if ((updateType & 0x40) != 0) {
				int hitDamage = stream.getUnsignedByteC();
				int hitType = stream.getUnsignedByteS();
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
		for (int n = 0; n < npcCount; n++) {
			int npcId = npcIds[n];
			NPC npc = npcs[npcId];
			if (npc != null)
				updateEntity(npc);
		}
	}

	private void updateNPCList(int amount, Buffer stream) {
		while (stream.bitPosition + 21 < amount * 8) {
			int npcId = stream.readBits(14);
			if (npcId == 16383)
				break;
			if (npcs[npcId] == null)
				npcs[npcId] = new NPC();
			NPC npc = npcs[npcId];
			npcIds[npcCount++] = npcId;
			npc.lastUpdateTick = tick;
			int y = stream.readBits(5);
			if (y > 15)
				y -= 32;
			int x = stream.readBits(5);
			if (x > 15)
				x -= 32;
			int clearWaypointQueue = stream.readBits(1);
			npc.npcDefinition = EntityDefinition.getDefinition(stream.readBits(12));
			int furtherUpdateRequired = stream.readBits(1);
			if (furtherUpdateRequired == 1)
				playersObserved[playersObservedCount++] = npcId;
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

	private void updateNPCMovement(Buffer stream) {
		stream.initBitAccess();
		int npcsToUpdate = stream.readBits(8);
		if (npcsToUpdate < npcCount) {
			for (int n = npcsToUpdate; n < npcCount; n++)
				actorsToUpdateIds[actorsToUpdateCount++] = npcIds[n];

		}
		if (npcsToUpdate > npcCount) {
			signlink.reporterror(enteredUsername + " Too many npcs");
			throw new RuntimeException("eek");
		}
		npcCount = 0;
		for (int n = 0; n < npcsToUpdate; n++) {
			int npcId = npcIds[n];
			NPC npc = npcs[npcId];
			int updateRequired = stream.readBits(1);
			if (updateRequired == 0) {
				npcIds[npcCount++] = npcId;
				npc.lastUpdateTick = tick;
			} else {
				int movementUpdateType = stream.readBits(2);
				if (movementUpdateType == 0) {
					npcIds[npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					playersObserved[playersObservedCount++] = npcId;
				} else if (movementUpdateType == 1) {
					npcIds[npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					int direction = stream.readBits(3);
					npc.move(false, direction);
					int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1)
						playersObserved[playersObservedCount++] = npcId;
				} else if (movementUpdateType == 2) {
					npcIds[npcCount++] = npcId;
					npc.lastUpdateTick = tick;
					int lastDirection = stream.readBits(3);
					npc.move(true, lastDirection);
					int currentDirection = stream.readBits(3);
					npc.move(true, currentDirection);
					int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1)
						playersObserved[playersObservedCount++] = npcId;
				} else if (movementUpdateType == 3)
					actorsToUpdateIds[actorsToUpdateCount++] = npcId;
			}
		}
	}

	private void updateNPCs(Buffer stream, int amount) {
		actorsToUpdateCount = 0;
		playersObservedCount = 0;
		updateNPCMovement(stream);
		updateNPCList(amount, stream);
		updateNPCBlock(stream);
		for (int k = 0; k < actorsToUpdateCount; k++) {
			int npcId = actorsToUpdateIds[k];
			if (npcs[npcId].lastUpdateTick != tick) {
				npcs[npcId].npcDefinition = null;
				npcs[npcId] = null;
			}
		}

		if (stream.position != amount) {
			signlink.reporterror(
					enteredUsername + " size mismatch in getnpcpos - pos:" + stream.position + " psize:" + amount);
			throw new RuntimeException("eek");
		}
		for (int n = 0; n < npcCount; n++)
			if (npcs[npcIds[n]] == null) {
				signlink.reporterror(enteredUsername + " null entry in npc list - pos:" + n + " size:" + npcCount);
				throw new RuntimeException("eek");
			}
	}

	private void updateOtherPlayerMovement(Buffer stream) {
		int playersToUpdate = stream.readBits(8);
		if (playersToUpdate < localPlayerCount) {
			for (int p = playersToUpdate; p < localPlayerCount; p++)
				actorsToUpdateIds[actorsToUpdateCount++] = localPlayers[p];
		}
		if (playersToUpdate > localPlayerCount) {
			signlink.reporterror(enteredUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		localPlayerCount = 0;
		for (int p = 0; p < playersToUpdate; p++) {
			int pId = localPlayers[p];
			Player player = players[pId];
			int updateRequired = stream.readBits(1);
			if (updateRequired == 0) {
				localPlayers[localPlayerCount++] = pId;
				player.lastUpdateTick = tick;
			} else {
				int movementUpdateType = stream.readBits(2);
				if (movementUpdateType == 0) {
					localPlayers[localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					playersObserved[playersObservedCount++] = pId;
				} else if (movementUpdateType == 1) {
					localPlayers[localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					int direction = stream.readBits(3);
					player.move(false, direction);
					int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1)
						playersObserved[playersObservedCount++] = pId;
				} else if (movementUpdateType == 2) {
					localPlayers[localPlayerCount++] = pId;
					player.lastUpdateTick = tick;
					int lastDirection = stream.readBits(3);
					player.move(true, lastDirection);
					int currentDirection = stream.readBits(3);
					player.move(true, currentDirection);
					int furtherUpdateRequired = stream.readBits(1);
					if (furtherUpdateRequired == 1)
						playersObserved[playersObservedCount++] = pId;
				} else if (movementUpdateType == 3)
					actorsToUpdateIds[actorsToUpdateCount++] = pId;
			}
		}
	}

	private void updatePlayer(Buffer stream, int updateType, Player player, int playerId) {
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
			int delay = stream.getInt();
			player.graphicHeight = delay >> 16;
			player.graphicEndCycle = tick + (delay & 0xffff);
			player.currentAnimationId = 0;
			player.currentAnimationTimeRemaining = 0;
			if (player.graphicEndCycle > tick)
				player.currentAnimationId = -1;
			if (player.graphicId == 65535)
				player.graphicId = -1;
		}
		if ((updateType & 8) != 0) {
			int animationId = stream.getUnsignedShort();
			if (animationId == 65535)
				animationId = -1;
			int animationDelay = stream.getUnsignedByteC();
			if (animationId == player.animation && animationId != -1) {
				int replayMode = AnimationSequence.animations[animationId].replayMode;
				if (replayMode == 1) {
					player.currentAnimationFrame = 0;
					player.currentAnimationDuration = 0;
					player.animationDelay = animationDelay;
					player.currentAnimationLoopCount = 0;
				}
				if (replayMode == 2)
					player.currentAnimationLoopCount = 0;
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
		if ((updateType & 4) != 0) {
			player.overheadTextMessage = stream.getString();
			if (player.overheadTextMessage.charAt(0) == '~') {
				player.overheadTextMessage = player.overheadTextMessage.substring(1);
				pushMessage(player.overheadTextMessage, 2, player.name);
			} else if (player == localPlayer)
				pushMessage(player.overheadTextMessage, 2, player.name);
			player.chatColour = 0;
			player.chatEffect = 0;
			player.textCycle = 150;
		}
		if ((updateType & 0x80) != 0) {
			int colourAndEffect = stream.getUnsignedShort();
			int rights = stream.getUnsignedByte();
			int messageLength = stream.getUnsignedByteC();
			int originalOffset = stream.position;
			if (player.name != null && player.visible) {
				long nameAsLong = TextClass.nameToLong(player.name);
				boolean ignored = false;
				if (rights <= 1) {
					for (int p = 0; p < ignoreCount; p++) {
						if (ignoreListAsLongs[p] != nameAsLong)
							continue;
						ignored = true;
						break;
					}

				}
				if (!ignored && inTutorial == 0)
					try {
						textStream.position = 0;
						stream.getBytes(messageLength, 0, textStream.buffer);
						textStream.position = 0;
						String text = TextInput.readFromStream(messageLength, textStream);
						text = Censor.doCensor(text);
						player.overheadTextMessage = text;
						player.chatColour = colourAndEffect >> 8;
						player.rights = rights;

						// entityMessage(player);

						player.chatEffect = colourAndEffect & 0xff;
						player.textCycle = 150;
						if (rights == 2 || rights == 3)
							pushMessage(text, 1, "@cr2@" + player.name);
						else if (rights == 1)
							pushMessage(text, 1, "@cr1@" + player.name);
						else
							pushMessage(text, 2, player.name);
					} catch (Exception exception) {
						signlink.reporterror("cde2");
					}
			}
			stream.position = originalOffset + messageLength;
		}
		if ((updateType & 1) != 0) {
			player.interactingEntity = stream.getUnsignedShort();
			if (player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if ((updateType & 0x10) != 0) {
			int appearanceBufferSize = stream.getUnsignedByteC();
			byte _appearanceBuffer[] = new byte[appearanceBufferSize];
			Buffer appearanceBuffer = new Buffer(_appearanceBuffer);
			stream.readBytes(appearanceBufferSize, 0, _appearanceBuffer);
			playerAppearanceData[playerId] = appearanceBuffer;
			player.updatePlayerAppearance(appearanceBuffer);
		}
		if ((updateType & 2) != 0) {
			player.faceTowardX = stream.getUnsignedShortA();
			player.faceTowardY = stream.getUnsignedShort();
		}
		if ((updateType & 0x20) != 0) {
			int hitDamage = stream.getUnsignedByte();
			int hitType = stream.getUnsignedByteA();
			player.updateHitData(hitType, hitDamage, tick);
			player.loopCycleStatus = tick + 300;
			player.currentHealth = stream.getUnsignedByteC();
			player.maxHealth = stream.getUnsignedByte();
		}
		if ((updateType & 0x200) != 0) {
			int hitDamage = stream.getUnsignedByte();
			int hitType = stream.getUnsignedByteS();
			player.updateHitData(hitType, hitDamage, tick);
			player.loopCycleStatus = tick + 300;
			player.currentHealth = stream.getUnsignedByte();
			player.maxHealth = stream.getUnsignedByteC();
		}
	}

	private void updatePlayerInstances() {
		for (int p = -1; p < localPlayerCount; p++) {
			int id;
			if (p == -1)
				id = LOCAL_PLAYER_ID;
			else
				id = localPlayers[p];
			Player player = players[id];
			if (player != null)
				updateEntity(player);
		}

	}

	private void updatePlayerList(Buffer stream, int count) {
		while (stream.bitPosition + 10 < count * 8) {
			int pId = stream.readBits(11);
			if (pId == 2047)
				break;
			if (players[pId] == null) {
				players[pId] = new Player();
				if (playerAppearanceData[pId] != null)
					players[pId].updatePlayerAppearance(playerAppearanceData[pId]);
			}
			localPlayers[localPlayerCount++] = pId;
			Player player = players[pId];
			player.lastUpdateTick = tick;
			int observed = stream.readBits(1);
			if (observed == 1)
				playersObserved[playersObservedCount++] = pId;
			int teleported = stream.readBits(1);
			int x = stream.readBits(5);
			if (x > 15)
				x -= 32;
			int y = stream.readBits(5);
			if (y > 15)
				y -= 32;
			player.setPos(localPlayer.waypointX[0] + y, localPlayer.waypointY[0] + x, teleported == 1);
		}
		stream.finishBitAccess();
	}

	private void updatePlayers(int packetSize, Buffer stream) {
		actorsToUpdateCount = 0;
		playersObservedCount = 0;
		updateLocalPlayerMovement(stream);
		updateOtherPlayerMovement(stream);
		updatePlayerList(stream, packetSize);
		updatePlayersBlock(stream);
		for (int p = 0; p < actorsToUpdateCount; p++) {
			int playerId = actorsToUpdateIds[p];
			if (players[playerId].lastUpdateTick != tick)
				players[playerId] = null;
		}

		if (stream.position != packetSize) {
			signlink.reporterror(
					"Error packet size mismatch in getplayer pos:" + stream.position + " psize:" + packetSize);
			throw new RuntimeException("eek");
		}
		for (int p = 0; p < localPlayerCount; p++)
			if (players[localPlayers[p]] == null) {
				signlink.reporterror(
						enteredUsername + " null entry in pl list - pos:" + p + " size:" + localPlayerCount);
				throw new RuntimeException("eek");
			}
	}

	private void updatePlayersBlock(Buffer stream) {
		for (int p = 0; p < playersObservedCount; p++) {
			int pId = playersObserved[p];
			Player player = players[pId];
			int updateType = stream.getUnsignedByte();
			if ((updateType & 0x40) != 0)
				updateType += stream.getUnsignedByte() << 8;
			updatePlayer(stream, updateType, player, pId);
		}

	}

	private void updatePosition(Entity entity) {
		int timePassed = entity.tickStart - tick;
		int differenceX = entity.startX * 128 + entity.boundaryDimension * 64;
		int differenceY = entity.startY * 128 + entity.boundaryDimension * 64;
		entity.x += (differenceX - entity.x) / timePassed;
		entity.y += (differenceY - entity.y) / timePassed;
		entity.stepsDelayed = 0;
		if (entity.direction == 0)
			entity.turnDirection = 1024;
		if (entity.direction == 1)
			entity.turnDirection = 1536;
		if (entity.direction == 2)
			entity.turnDirection = 0;
		if (entity.direction == 3)
			entity.turnDirection = 512;
	}
}
