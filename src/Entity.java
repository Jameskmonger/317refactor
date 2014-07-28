public class Entity extends Animable {

    public final void setPos(int x, int y, boolean flag)
    {
        if(animation != -1 && Animation.anims[animation].priority == 1)
            animation = -1;
        if(!flag)
        {
            int distanceX = x - smallX[0];
            int distanceY = y - smallY[0];
            if(distanceX >= -8 && distanceX <= 8 && distanceY >= -8 && distanceY <= 8)
            {
                if(pathLength < 9)
                    pathLength++;
                for(int i = pathLength; i > 0; i--)
                {
                    smallX[i] = smallX[i - 1];
                    smallY[i] = smallY[i - 1];
                    pathRun[i] = pathRun[i - 1];
                }

                smallX[0] = x;
                smallY[0] = y;
                pathRun[0] = false;
                return;
            }
        }
        pathLength = 0;
        anInt1542 = 0;
        anInt1503 = 0;
        smallX[0] = x;
        smallY[0] = y;
        this.x = smallX[0] * 128 + boundaryDimension * 64;
        this.y = smallY[0] * 128 + boundaryDimension * 64;
    }

    public final void resetPath()
    {
        pathLength = 0;
        anInt1542 = 0;
    }

    public final void updateHitData(int type, int damage, int currentTime)
    {
        for(int hit = 0; hit < 4; hit++)
            if(hitsLoopCycle[hit] <= currentTime)
            {
                hitArray[hit] = damage;
                hitMarkTypes[hit] = type;
                hitsLoopCycle[hit] = currentTime + 70;
                return;
            }
    }

    public final void move(boolean flag, int direction)
    {
        int x = smallX[0];
        int y = smallY[0];
        if(direction == 0)
        {
            x--;
            y++;
        }
        if(direction == 1)
            y++;
        if(direction == 2)
        {
            x++;
            y++;
        }
        if(direction == 3)
            x--;
        if(direction == 4)
            x++;
        if(direction == 5)
        {
            x--;
            y--;
        }
        if(direction == 6)
            y--;
        if(direction == 7)
        {
            x++;
            y--;
        }
        if(animation != -1 && Animation.anims[animation].priority == 1)
            animation = -1;
        if(pathLength < 9)
            pathLength++;
        for(int l = pathLength; l > 0; l--)
        {
            smallX[l] = smallX[l - 1];
            smallY[l] = smallY[l - 1];
            pathRun[l] = pathRun[l - 1];
        }
            smallX[0] = x;
            smallY[0] = y;
            pathRun[0] = flag;
    }

    public int entScreenX;
	public int entScreenY;
	public final int index = -1;
	public boolean isVisible()
    {
        return false;
    }

    Entity()
    {
        smallX = new int[10];
        smallY = new int[10];
        interactingEntity = -1;
        degreesToTurn = 32;
        runAnimationId = -1;
        height = 200;
        standAnimationId = -1;
        standTurnAnimationId = -1;
        hitArray = new int[4];
        hitMarkTypes = new int[4];
        hitsLoopCycle = new int[4];
        anInt1517 = -1;
        graphicId = -1;
        animation = -1;
        loopCycleStatus = -1000;
        textCycle = 100;
        boundaryDimension = 1;
        aBoolean1541 = false;
        pathRun = new boolean[10];
        walkAnimationId = -1;
        turnAboutAnimationId = -1;
        turnRightAnimationId = -1;
        turnLeftAnimationId = -1;
    }

    public final int[] smallX;
    public final int[] smallY;
    public int interactingEntity;
    int anInt1503;
    int degreesToTurn;
    int runAnimationId;
    public String textSpoken;
    public int height;
    public int turnDirection;
    int standAnimationId;
    int standTurnAnimationId;
    int chatColour;
    final int[] hitArray;
    final int[] hitMarkTypes;
    final int[] hitsLoopCycle;
    int anInt1517;
    int anInt1518;
    int anInt1519;
    int graphicId;
    int currentAnimation;
    int anInt1522;
    int graphicEndCycle;
    int graphicHeight;
    int pathLength;
    public int animation;
    int anInt1527;
    int anInt1528;
    int animationDelay;
    int anInt1530;
    int chatEffect;
    public int loopCycleStatus;
    public int currentHealth;
    public int maxHealth;
    int textCycle;
    int lastUpdateTime;
    int faceTowardX;
    int faceTowardY;
    int boundaryDimension;
    boolean aBoolean1541;
    int anInt1542;
    int startX;
    int endX;
    int startY;
    int endY;
    int speedToStart;
    int speedToEnd;
    int direction;
    public int x;
    public int y;
    int currentRotation;
    final boolean[] pathRun;
    int walkAnimationId;
    int turnAboutAnimationId;
    int turnRightAnimationId;
    int turnLeftAnimationId;
}
