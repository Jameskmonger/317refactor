package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 10th of April 2006.
 * 
 * This file has been refactored in order to
 * restore readability to the codebase for
 * educational purposes, primarility to those
 * with an interest in game development.
 * 
 * It may be a criminal offence to run this
 * file. This file is the intellectual property
 * of Jagex Ltd.
 */

final class NodeSubList {

    public NodeSubList()
    {
        head = new NodeSub();
        head.prevNodeSub = head;
        head.nextNodeSub = head;
    }

    public void insertHead(NodeSub nodeSub)
    {
        if(nodeSub.nextNodeSub != null)
            nodeSub.unlinkSub();
        nodeSub.nextNodeSub = head.nextNodeSub;
        nodeSub.prevNodeSub = head;
        nodeSub.nextNodeSub.prevNodeSub = nodeSub;
        nodeSub.prevNodeSub.nextNodeSub = nodeSub;
    }

    public NodeSub popTail()
    {
        NodeSub nodeSub = head.prevNodeSub;
        if(nodeSub == head)
        {
            return null;
        } else
        {
            nodeSub.unlinkSub();
            return nodeSub;
        }
    }

    public NodeSub reverseGetFirst()
    {
        NodeSub nodeSub = head.prevNodeSub;
        if(nodeSub == head)
        {
            current = null;
            return null;
        } else
        {
            current = nodeSub.prevNodeSub;
            return nodeSub;
        }
    }

    public NodeSub reverseGetNext()
    {
        NodeSub nodeSub = current;
        if(nodeSub == head)
        {
            current = null;
            return null;
        } else
        {
            current = nodeSub.prevNodeSub;
            return nodeSub;
        }
    }

    public int getNodeCount()
    {
        int i = 0;
        for(NodeSub nodeSub = head.prevNodeSub; nodeSub != head; nodeSub = nodeSub.prevNodeSub)
            i++;

        return i;
    }

    private final NodeSub head;
    private NodeSub current;
}
