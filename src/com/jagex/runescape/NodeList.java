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

final class NodeList {

    public NodeList()
    {
        head = new Node();
        head.prev = head;
        head.next = head;
    }

    public void insertHead(Node node)
    {
        if(node.next != null)
            node.remove();
        node.next = head.next;
        node.prev = head;
        node.next.prev = node;
        node.prev.next = node;
    }

    public void insertTail(Node node)
    {
        if(node.next != null)
            node.remove();
        node.next = head;
        node.prev = head.prev;
        node.next.prev = node;
        node.prev.next = node;
    }

    public Node popHead()
    {
        Node node = head.prev;
        if(node == head)
        {
            return null;
        } else
        {
            node.remove();
            return node;
        }
    }

    public Node getBack()
    {
        Node node = head.prev;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.prev;
            return node;
        }
    }

    public Node getFirst()
    {
        Node node = head.next;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.next;
            return node;
        }
    }

    public Node reverseGetNext()
    {
        Node node = current;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.prev;
            return node;
        }
    }

    public Node getNext()
    {
        Node node = current;
        if(node == head)
        {
            current = null;
            return null;
        }
        current = node.next;
            return node;
    }

    public void removeAll()
    {
        if(head.prev == head)
            return;
        do
        {
            Node node = head.prev;
            if(node == head)
                return;
            node.remove();
        } while(true);
    }

    private final Node head;
    private Node current;
}
