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

import com.jagex.runescape.sign.signlink;

public final class MRUNodes {

    public MRUNodes(int i)
    {
        emptyNodeSub = new NodeSub();
        nodeSubList = new NodeSubList();
        initialCount = i;
        spaceLeft = i;
        nodeCache = new NodeCache();
    }

    public NodeSub get(long l)
    {
        NodeSub nodeSub = (NodeSub) nodeCache.findNodeByID(l);
        if(nodeSub != null)
        {
            nodeSubList.insertHead(nodeSub);
        }
        return nodeSub;
    }

    public void put(NodeSub nodeSub, long l)
    {
        try
        {
            if(spaceLeft == 0)
            {
                NodeSub nodeSub_1 = nodeSubList.popTail();
                nodeSub_1.remove();
                nodeSub_1.unlinkSub();
                if(nodeSub_1 == emptyNodeSub)
                {
                    NodeSub nodeSub_2 = nodeSubList.popTail();
                    nodeSub_2.remove();
                    nodeSub_2.unlinkSub();
                }
            } else
            {
                spaceLeft--;
            }
            nodeCache.removeFromCache(nodeSub, l);
            nodeSubList.insertHead(nodeSub);
            return;
        }
        catch(RuntimeException runtimeexception)
        {
            signlink.reporterror("47547, " + nodeSub + ", " + l + ", " + (byte)2 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    public void unlinkAll()
    {
        do
        {
            NodeSub nodeSub = nodeSubList.popTail();
            if(nodeSub != null)
            {
                nodeSub.remove();
                nodeSub.unlinkSub();
            } else
            {
                spaceLeft = initialCount;
                return;
            }
        } while(true);
    }

    private final NodeSub emptyNodeSub;
    private final int initialCount;
    private int spaceLeft;
    private final NodeCache nodeCache;
    private final NodeSubList nodeSubList;
}
