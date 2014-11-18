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

public class Node {

    public final void remove()
    {
        if(next == null)
        {
        } else
        {
            next.prev = prev;
            prev.next = next;
            prev = null;
            next = null;
        }
    }

    public Node()
    {
    }

    public long id;
    public Node prev;
    public Node next;
}
