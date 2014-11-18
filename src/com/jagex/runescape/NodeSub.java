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

public class NodeSub extends Node {

    public final void unlinkSub()
    {
        if(nextNodeSub == null)
        {
        } else
        {
            nextNodeSub.prevNodeSub = prevNodeSub;
            prevNodeSub.nextNodeSub = nextNodeSub;
            prevNodeSub = null;
            nextNodeSub = null;
        }
    }

    public NodeSub()
    {
    }

    public NodeSub prevNodeSub;
    NodeSub nextNodeSub;
    public static int anInt1305;
}
