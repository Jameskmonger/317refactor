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

import java.io.*;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import com.jagex.runescape.sign.signlink;

public final class OnDemandFetcher extends OnDemandFetcherParent implements Runnable {

	private int totalFiles;

	private final NodeList requested;

	private int highestPriority;

	public String statusString;

	private int writeLoopCycle;

	private long openSocketTime;

	private int[] mapIndices3;

	private final CRC32 crc32;

	private final byte[] ioBuffer;

	public int onDemandCycle;

	private final byte[][] filePriorities;

	private Client clientInstance;

	private final NodeList passiveRequests;

	private int completedSize;

	private int expectedSize;

	private int[] anIntArray1348;

	public int failedRequests;

	private int[] mapIndices2;

	private int filesLoaded;

	private boolean running;

	private OutputStream outputStream;

	private int[] mapIndices4;

	private boolean waiting;

	private final NodeList aClass19_1358;

	private final byte[] gzipInputBuffer;
	private int[] anIntArray1360;
	private final Deque nodeSubList;
	private InputStream inputStream;
	private Socket socket;
	private final int[][] versions;
	private final int[][] crcs;
	private int uncompletedCount;
	private int completedCount;
	private final NodeList aClass19_1368;
	private OnDemandData current;
	private final NodeList aClass19_1370;
	private int[] mapIndices1;
	private byte[] modelIndices;
	private int loopCycle;

	public OnDemandFetcher() {
		requested = new NodeList();
		statusString = "";
		crc32 = new CRC32();
		ioBuffer = new byte[500];
		filePriorities = new byte[4][];
		passiveRequests = new NodeList();
		running = true;
		waiting = false;
		aClass19_1358 = new NodeList();
		gzipInputBuffer = new byte[65000];
		nodeSubList = new Deque();
		versions = new int[4][];
		crcs = new int[4][];
		aClass19_1368 = new NodeList();
		aClass19_1370 = new NodeList();
	}

	private void checkReceived() {
		OnDemandData onDemandData;
		synchronized (aClass19_1370) {
			onDemandData = (OnDemandData) aClass19_1370.popHead();
		}
		while (onDemandData != null) {
			waiting = true;
			byte abyte0[] = null;
			if (clientInstance.caches[0] != null)
				abyte0 = clientInstance.caches[onDemandData.dataType + 1].decompress(onDemandData.id);
			if (!crcMatches(versions[onDemandData.dataType][onDemandData.id],
					crcs[onDemandData.dataType][onDemandData.id], abyte0))
				abyte0 = null;
			synchronized (aClass19_1370) {
				if (abyte0 == null) {
					aClass19_1368.insertHead(onDemandData);
				} else {
					onDemandData.buffer = abyte0;
					synchronized (aClass19_1358) {
						aClass19_1358.insertHead(onDemandData);
					}
				}
				onDemandData = (OnDemandData) aClass19_1370.popHead();
			}
		}
	}

	public void clearPassiveRequests() {
		synchronized (passiveRequests) {
			passiveRequests.removeAll();
		}
	}

	private void closeRequest(OnDemandData onDemandData) {
		try {
			if (socket == null) {
				long l = System.currentTimeMillis();
				if (l - openSocketTime < 4000L)
					return;
				openSocketTime = l;
				socket = clientInstance.openSocket(43594 + Client.portOffset);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				outputStream.write(15);
				for (int j = 0; j < 8; j++)
					inputStream.read();

				loopCycle = 0;
			}
			ioBuffer[0] = (byte) onDemandData.dataType;
			ioBuffer[1] = (byte) (onDemandData.id >> 8);
			ioBuffer[2] = (byte) onDemandData.id;
			if (onDemandData.incomplete)
				ioBuffer[3] = 2;
			else if (!clientInstance.loggedIn)
				ioBuffer[3] = 1;
			else
				ioBuffer[3] = 0;
			outputStream.write(ioBuffer, 0, 4);
			writeLoopCycle = 0;
			failedRequests = -10000;
			return;
		} catch (IOException ioexception) {
		}
		try {
			socket.close();
		} catch (Exception _ex) {
		}
		socket = null;
		inputStream = null;
		outputStream = null;
		expectedSize = 0;
		failedRequests++;
	}

	private boolean crcMatches(int i, int j, byte abyte0[]) {
		if (abyte0 == null || abyte0.length < 2)
			return false;
		int k = abyte0.length - 2;
		int l = ((abyte0[k] & 0xff) << 8) + (abyte0[k + 1] & 0xff);
		crc32.reset();
		crc32.update(abyte0, 0, k);
		int i1 = (int) crc32.getValue();
		return l == i && i1 == j;
	}

	public void disable() {
		running = false;
	}

	public int fileCount(int j) {
		return versions[j].length;
	}

	public int getAnimCount() {
		return anIntArray1360.length;
	}

	public int getMapId(int type, int mapX, int mapY) {
		int coordinates = (mapX << 8) + mapY;
		for (int pointer = 0; pointer < mapIndices1.length; pointer++)
			if (mapIndices1[pointer] == coordinates)
				if (type == 0)
					return mapIndices2[pointer];
				else
					return mapIndices3[pointer];
		return -1;
	}

	public int getModelId(int i) {
		return modelIndices[i] & 0xff;
	}

	public OnDemandData getNextNode() {
		OnDemandData onDemandData;
		synchronized (aClass19_1358) {
			onDemandData = (OnDemandData) aClass19_1358.popHead();
		}
		if (onDemandData == null)
			return null;
		synchronized (nodeSubList) {
			onDemandData.unlist();
		}
		if (onDemandData.buffer == null)
			return onDemandData;
		int i = 0;
		try {
			GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(onDemandData.buffer));
			do {
				if (i == gzipInputBuffer.length)
					throw new RuntimeException("buffer overflow!");
				int k = gzipinputstream.read(gzipInputBuffer, i, gzipInputBuffer.length - i);
				if (k == -1)
					break;
				i += k;
			} while (true);
		} catch (IOException _ex) {
			throw new RuntimeException("error unzipping");
		}
		onDemandData.buffer = new byte[i];
		System.arraycopy(gzipInputBuffer, 0, onDemandData.buffer, 0, i);

		return onDemandData;
	}

	private void handleFailed() {
		uncompletedCount = 0;
		completedCount = 0;
		for (OnDemandData onDemandData = (OnDemandData) requested
				.peekLast(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext())
			if (onDemandData.incomplete)
				uncompletedCount++;
			else
				completedCount++;

		while (uncompletedCount < 10) {
			OnDemandData onDemandData_1 = (OnDemandData) aClass19_1368.popHead();
			if (onDemandData_1 == null)
				break;
			if (filePriorities[onDemandData_1.dataType][onDemandData_1.id] != 0)
				filesLoaded++;
			filePriorities[onDemandData_1.dataType][onDemandData_1.id] = 0;
			requested.insertHead(onDemandData_1);
			uncompletedCount++;
			closeRequest(onDemandData_1);
			waiting = true;
		}
	}

	public int immediateRequestCount() {
		synchronized (nodeSubList) {
			return nodeSubList.getNodeCount();
		}
	}

	public boolean method564(int i) {
		for (int k = 0; k < mapIndices1.length; k++)
			if (mapIndices3[k] == i)
				return true;
		return false;
	}

	private void method568() {
		while (uncompletedCount == 0 && completedCount < 10) {
			if (highestPriority == 0)
				break;
			OnDemandData onDemandData;
			synchronized (passiveRequests) {
				onDemandData = (OnDemandData) passiveRequests.popHead();
			}
			while (onDemandData != null) {
				if (filePriorities[onDemandData.dataType][onDemandData.id] != 0) {
					filePriorities[onDemandData.dataType][onDemandData.id] = 0;
					requested.insertHead(onDemandData);
					closeRequest(onDemandData);
					waiting = true;
					if (filesLoaded < totalFiles)
						filesLoaded++;
					statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
					completedCount++;
					if (completedCount == 10)
						return;
				}
				synchronized (passiveRequests) {
					onDemandData = (OnDemandData) passiveRequests.popHead();
				}
			}
			for (int j = 0; j < 4; j++) {
				byte abyte0[] = filePriorities[j];
				int k = abyte0.length;
				for (int l = 0; l < k; l++)
					if (abyte0[l] == highestPriority) {
						abyte0[l] = 0;
						OnDemandData onDemandData_1 = new OnDemandData();
						onDemandData_1.dataType = j;
						onDemandData_1.id = l;
						onDemandData_1.incomplete = false;
						requested.insertHead(onDemandData_1);
						closeRequest(onDemandData_1);
						waiting = true;
						if (filesLoaded < totalFiles)
							filesLoaded++;
						statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
						completedCount++;
						if (completedCount == 10)
							return;
					}

			}

			highestPriority--;
		}
	}

	public boolean midiIdEqualsOne(int i) {
		return anIntArray1348[i] == 1;
	}

	public void passiveRequest(int id, int type) {
		if (clientInstance.caches[0] == null)
			return;
		if (versions[type][id] == 0)
			return;
		if (filePriorities[type][id] == 0)
			return;
		if (highestPriority == 0)
			return;
		OnDemandData onDemandData = new OnDemandData();
		onDemandData.dataType = type;
		onDemandData.id = id;
		onDemandData.incomplete = false;
		synchronized (passiveRequests) {
			passiveRequests.insertHead(onDemandData);
		}
	}

	public void preloadRegions(boolean flag) {
		int j = mapIndices1.length;
		for (int k = 0; k < j; k++)
			if (flag || mapIndices4[k] != 0) {
				setPriority((byte) 2, 3, mapIndices3[k]);
				setPriority((byte) 2, 3, mapIndices2[k]);
			}

	}

	private void readData() {
		try {
			int j = inputStream.available();
			if (expectedSize == 0 && j >= 6) {
				waiting = true;
				for (int k = 0; k < 6; k += inputStream.read(ioBuffer, k, 6 - k))
					;
				int l = ioBuffer[0] & 0xff;
				int j1 = ((ioBuffer[1] & 0xff) << 8) + (ioBuffer[2] & 0xff);
				int l1 = ((ioBuffer[3] & 0xff) << 8) + (ioBuffer[4] & 0xff);
				int i2 = ioBuffer[5] & 0xff;
				current = null;
				for (OnDemandData onDemandData = (OnDemandData) requested
						.peekLast(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext()) {
					if (onDemandData.dataType == l && onDemandData.id == j1)
						current = onDemandData;
					if (current != null)
						onDemandData.loopCycle = 0;
				}

				if (current != null) {
					loopCycle = 0;
					if (l1 == 0) {
						signlink.reporterror("Rej: " + l + "," + j1);
						current.buffer = null;
						if (current.incomplete)
							synchronized (aClass19_1358) {
								aClass19_1358.insertHead(current);
							}
						else
							current.unlink();
						current = null;
					} else {
						if (current.buffer == null && i2 == 0)
							current.buffer = new byte[l1];
						if (current.buffer == null && i2 != 0)
							throw new IOException("missing start of file");
					}
				}
				completedSize = i2 * 500;
				expectedSize = 500;
				if (expectedSize > l1 - i2 * 500)
					expectedSize = l1 - i2 * 500;
			}
			if (expectedSize > 0 && j >= expectedSize) {
				waiting = true;
				byte abyte0[] = ioBuffer;
				int i1 = 0;
				if (current != null) {
					abyte0 = current.buffer;
					i1 = completedSize;
				}
				for (int k1 = 0; k1 < expectedSize; k1 += inputStream.read(abyte0, k1 + i1, expectedSize - k1))
					;
				if (expectedSize + completedSize >= abyte0.length && current != null) {
					if (clientInstance.caches[0] != null)
						clientInstance.caches[current.dataType + 1].put(abyte0.length, abyte0, current.id);
					if (!current.incomplete && current.dataType == 3) {
						current.incomplete = true;
						current.dataType = 93;
					}
					if (current.incomplete)
						synchronized (aClass19_1358) {
							aClass19_1358.insertHead(current);
						}
					else
						current.unlink();
				}
				expectedSize = 0;
			}
		} catch (IOException ioexception) {
			try {
				socket.close();
			} catch (Exception _ex) {
			}
			socket = null;
			inputStream = null;
			outputStream = null;
			expectedSize = 0;
		}
	}

	@Override
	public void request(int i) {
		request(0, i);
	}

	public void request(int i, int j) {
		if (i < 0 || i > versions.length || j < 0 || j > versions[i].length)
			return;
		if (versions[i][j] == 0)
			return;
		synchronized (nodeSubList) {
			for (OnDemandData onDemandData = (OnDemandData) nodeSubList
					.reverseGetFirst(); onDemandData != null; onDemandData = (OnDemandData) nodeSubList
							.reverseGetNext())
				if (onDemandData.dataType == i && onDemandData.id == j)
					return;

			OnDemandData onDemandData_1 = new OnDemandData();
			onDemandData_1.dataType = i;
			onDemandData_1.id = j;
			onDemandData_1.incomplete = true;
			synchronized (aClass19_1370) {
				aClass19_1370.insertHead(onDemandData_1);
			}
			nodeSubList.push(onDemandData_1);
		}
	}

	@Override
	public void run() {
		try {
			while (running) {
				onDemandCycle++;
				int i = 20;
				if (highestPriority == 0 && clientInstance.caches[0] != null)
					i = 50;
				try {
					Thread.sleep(i);
				} catch (Exception _ex) {
				}
				waiting = true;
				for (int j = 0; j < 100; j++) {
					if (!waiting)
						break;
					waiting = false;
					checkReceived();
					handleFailed();
					if (uncompletedCount == 0 && j >= 5)
						break;
					method568();
					if (inputStream != null)
						readData();
				}

				boolean flag = false;
				for (OnDemandData onDemandData = (OnDemandData) requested
						.peekLast(); onDemandData != null; onDemandData = (OnDemandData) requested.reverseGetNext())
					if (onDemandData.incomplete) {
						flag = true;
						onDemandData.loopCycle++;
						if (onDemandData.loopCycle > 50) {
							onDemandData.loopCycle = 0;
							closeRequest(onDemandData);
						}
					}

				if (!flag) {
					for (OnDemandData onDemandData_1 = (OnDemandData) requested
							.peekLast(); onDemandData_1 != null; onDemandData_1 = (OnDemandData) requested
									.reverseGetNext()) {
						flag = true;
						onDemandData_1.loopCycle++;
						if (onDemandData_1.loopCycle > 50) {
							onDemandData_1.loopCycle = 0;
							closeRequest(onDemandData_1);
						}
					}

				}
				if (flag) {
					loopCycle++;
					if (loopCycle > 750) {
						try {
							socket.close();
						} catch (Exception _ex) {
						}
						socket = null;
						inputStream = null;
						outputStream = null;
						expectedSize = 0;
					}
				} else {
					loopCycle = 0;
					statusString = "";
				}
				if (clientInstance.loggedIn && socket != null && outputStream != null
						&& (highestPriority > 0 || clientInstance.caches[0] == null)) {
					writeLoopCycle++;
					if (writeLoopCycle > 500) {
						writeLoopCycle = 0;
						ioBuffer[0] = 0;
						ioBuffer[1] = 0;
						ioBuffer[2] = 0;
						ioBuffer[3] = 10;
						try {
							outputStream.write(ioBuffer, 0, 4);
						} catch (IOException _ex) {
							loopCycle = 5000;
						}
					}
				}
			}
		} catch (Exception exception) {
			signlink.reporterror("od_ex " + exception.getMessage());
		}
	}

	public void setPriority(byte byte0, int i, int j) {
		if (clientInstance.caches[0] == null)
			return;
		if (versions[i][j] == 0)
			return;
		byte abyte0[] = clientInstance.caches[i + 1].decompress(j);
		if (crcMatches(versions[i][j], crcs[i][j], abyte0))
			return;
		filePriorities[i][j] = byte0;
		if (byte0 > highestPriority)
			highestPriority = byte0;
		totalFiles++;
	}

	public void start(Archive streamLoader, Client client1) {
		String as[] = { "model_version", "anim_version", "midi_version", "map_version" };
		for (int i = 0; i < 4; i++) {
			byte abyte0[] = streamLoader.decompressFile(as[i]);
			int j = abyte0.length / 2;
			Buffer stream = new Buffer(abyte0);
			versions[i] = new int[j];
			filePriorities[i] = new byte[j];
			for (int l = 0; l < j; l++)
				versions[i][l] = stream.getUnsignedLEShort();

		}

		String as1[] = { "model_crc", "anim_crc", "midi_crc", "map_crc" };
		for (int k = 0; k < 4; k++) {
			byte abyte1[] = streamLoader.decompressFile(as1[k]);
			int i1 = abyte1.length / 4;
			Buffer stream_1 = new Buffer(abyte1);
			crcs[k] = new int[i1];
			for (int l1 = 0; l1 < i1; l1++)
				crcs[k][l1] = stream_1.getInt();

		}

		byte abyte2[] = streamLoader.decompressFile("model_index");
		int j1 = versions[0].length;
		modelIndices = new byte[j1];
		for (int k1 = 0; k1 < j1; k1++)
			if (k1 < abyte2.length)
				modelIndices[k1] = abyte2[k1];
			else
				modelIndices[k1] = 0;

		abyte2 = streamLoader.decompressFile("map_index");
		Buffer stream2 = new Buffer(abyte2);
		j1 = abyte2.length / 7;
		mapIndices1 = new int[j1];
		mapIndices2 = new int[j1];
		mapIndices3 = new int[j1];
		mapIndices4 = new int[j1];
		for (int i2 = 0; i2 < j1; i2++) {
			mapIndices1[i2] = stream2.getUnsignedLEShort();
			mapIndices2[i2] = stream2.getUnsignedLEShort();
			mapIndices3[i2] = stream2.getUnsignedLEShort();
			mapIndices4[i2] = stream2.getUnsignedByte();
		}

		abyte2 = streamLoader.decompressFile("anim_index");
		stream2 = new Buffer(abyte2);
		j1 = abyte2.length / 2;
		anIntArray1360 = new int[j1];
		for (int j2 = 0; j2 < j1; j2++)
			anIntArray1360[j2] = stream2.getUnsignedLEShort();

		abyte2 = streamLoader.decompressFile("midi_index");
		stream2 = new Buffer(abyte2);
		j1 = abyte2.length;
		anIntArray1348 = new int[j1];
		for (int k2 = 0; k2 < j1; k2++)
			anIntArray1348[k2] = stream2.getUnsignedByte();

		clientInstance = client1;
		running = true;
		clientInstance.startRunnable(this, 2);
	}
}
