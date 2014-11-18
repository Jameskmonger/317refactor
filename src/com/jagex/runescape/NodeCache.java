package com.jagex.runescape;

/*
 * This file is part of the RuneScape client
 * revision 317, which was publicly released
 * on the 13th of June 2005.
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

/* 
 * This file was renamed as part of the 317refactor project.
 */

import com.jagex.runescape.sign.signlink;

final class NodeCache {

	private final int size;

	private final Node[] cache;

	public NodeCache() {
		int i = 1024;// was parameter
		size = i;
		cache = new Node[i];
		for (int k = 0; k < i; k++) {
			Node node = cache[k] = new Node();
			node.prev = node;
			node.next = node;
		}

	}

	public Node findNodeByID(long l) {
		Node node = cache[(int) (l & size - 1)];
		for (Node node_1 = node.prev; node_1 != node; node_1 = node_1.prev)
			if (node_1.id == l)
				return node_1;

		return null;
	}
	public void removeFromCache(Node node, long l) {
		try {
			if (node.next != null)
				node.remove();
			Node node_1 = cache[(int) (l & size - 1)];
			node.next = node_1.next;
			node.prev = node_1;
			node.next.prev = node;
			node.prev.next = node;
			node.id = l;
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("91499, " + node + ", " + l + ", " + (byte) 7
					+ ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
}
