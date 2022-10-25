package com.jagex.runescape;

import java.io.*;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

import com.jagex.runescape.collection.CacheableQueue;
import com.jagex.runescape.collection.DoubleEndedQueue;
import com.jagex.runescape.sign.signlink;

public final class OnDemandFetcher implements Runnable {

    private int totalFiles;

    private final DoubleEndedQueue requested;

    private int highestPriority;

    public String statusString;

    private int writeLoopCycle;

    private long lastRequestTime;

    private final CRC32 crc32;

    private final byte[] payload;

    public int onDemandCycle;

    private final byte[][] filePriorities;

    private Client clientInstance;

    private final DoubleEndedQueue passiveRequests;

    private int completedSize;

    private int expectedSize;

    private int[] musicPriorities;

    public int failedRequests;


    private int filesLoaded;

    private boolean running;

    private OutputStream outputStream;

    private boolean waiting;

    private final DoubleEndedQueue complete;

    private final byte[] gzipInputBuffer;
    private int[] frames;
    private final CacheableQueue nodeSubList;
    private InputStream inputStream;
    private Socket socket;
    private final int[][] versions;
    private final int[][] crcs;
    private int uncompletedCount;
    private int completedCount;
    private final DoubleEndedQueue unrequested;
    private OnDemandData current;
    private final DoubleEndedQueue mandatoryRequests;
    private byte[] modelIndices;
    private int loopCycle;

    /**
     * The ID (coordinates) of the mapsquares.
     */
    private int[] mapSquareIds;

    /**
     * If this value is not 0, the mapsquare will be preloaded.
     */
    private int[] shouldPreloadMap;

    /**
     * The file containing the objects for a mapsquare.
     */
    private int[] objectMapIndices;

    /**
     * The file containing the terrain for a mapsquare.
     */
    private int[] terrainMapIndices;

    public OnDemandFetcher() {
        this.requested = new DoubleEndedQueue();
        this.statusString = "";
        this.crc32 = new CRC32();
        this.payload = new byte[500];
        this.filePriorities = new byte[4][];
        this.passiveRequests = new DoubleEndedQueue();
        this.running = true;
        this.waiting = false;
        this.complete = new DoubleEndedQueue();
        this.gzipInputBuffer = new byte[65000];
        this.nodeSubList = new CacheableQueue();
        this.versions = new int[4][];
        this.crcs = new int[4][];
        this.unrequested = new DoubleEndedQueue();
        this.mandatoryRequests = new DoubleEndedQueue();
    }

    private void checkReceived() {
        OnDemandData request;
        synchronized (this.mandatoryRequests) {
            request = (OnDemandData) this.mandatoryRequests.popFront();
        }
        while (request != null) {
            this.waiting = true;
            byte[] data = null;
            if (this.clientInstance.caches[0] != null) {
                data = this.clientInstance.caches[request.dataType + 1].decompress(request.id);
            }
            if (!this.crcMatches(this.versions[request.dataType][request.id],
                this.crcs[request.dataType][request.id], data)) {
                data = null;
            }
            synchronized (this.mandatoryRequests) {
                if (data == null) {
                    this.unrequested.pushBack(request);
                } else {
                    request.buffer = data;
                    synchronized (this.complete) {
                        this.complete.pushBack(request);
                    }
                }
                request = (OnDemandData) this.mandatoryRequests.popFront();
            }
        }
    }

    public void clearPassiveRequests() {
        synchronized (this.passiveRequests) {
            this.passiveRequests.clear();
        }
    }

    private void closeRequest(final OnDemandData request) {
        try {
            if (this.socket == null) {
                final long currentTime = System.currentTimeMillis();
                if (currentTime - this.lastRequestTime < 4000L) {
                    return;
                }
                this.lastRequestTime = currentTime;
                this.socket = this.clientInstance.openSocket(43594 + Client.portOffset);
                this.inputStream = this.socket.getInputStream();
                this.outputStream = this.socket.getOutputStream();
                this.outputStream.write(15);
                for (int j = 0; j < 8; j++) {
                    this.inputStream.read();
                }

                this.loopCycle = 0;
            }
            this.payload[0] = (byte) request.dataType;
            this.payload[1] = (byte) (request.id >> 8);
            this.payload[2] = (byte) request.id;
            if (request.incomplete) {
                this.payload[3] = 2;
            } else if (!this.clientInstance.loggedIn) {
                this.payload[3] = 1;
            } else {
                this.payload[3] = 0;
            }
            this.outputStream.write(this.payload, 0, 4);
            this.writeLoopCycle = 0;
            this.failedRequests = -10000;
            return;
        } catch (final IOException ioexception) {
        }
        try {
            this.socket.close();
        } catch (final Exception _ex) {
        }
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.expectedSize = 0;
        this.failedRequests++;
    }

    private boolean crcMatches(final int cacheVersion, final int cacheChecksum, final byte[] data) {
        if (data == null || data.length < 2) {
            return false;
        }

        final int length = data.length - 2;
        final int version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
        this.crc32.reset();
        this.crc32.update(data, 0, length);
        final int calculatedChecksum = (int) this.crc32.getValue();
        return version == cacheVersion && calculatedChecksum == cacheChecksum;
    }

    public void disable() {
        this.running = false;
    }

    public int fileCount(final int j) {
        return this.versions[j].length;
    }

    public int getAnimCount() {
        return this.frames.length;
    }

    public int getMapId(final int type, final int mapX, final int mapY) {
        final int coordinates = (mapX << 8) + mapY;
        for (int pointer = 0; pointer < this.mapSquareIds.length; pointer++) {
            if (this.mapSquareIds[pointer] == coordinates) {
                if (type == 0) {
                    return this.terrainMapIndices[pointer];
                } else {
                    return this.objectMapIndices[pointer];
                }
            }
        }
        return -1;
    }

    public int getModelId(final int i) {
        return this.modelIndices[i] & 0xff;
    }

    public OnDemandData getNextNode() {
        final OnDemandData onDemandData;
        synchronized (this.complete) {
            onDemandData = (OnDemandData) this.complete.popFront();
        }
        if (onDemandData == null) {
            return null;
        }
        synchronized (this.nodeSubList) {
            onDemandData.unlinkCacheable();
        }
        if (onDemandData.buffer == null) {
            return onDemandData;
        }
        int i = 0;
        try {
            final GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(onDemandData.buffer));
            do {
                if (i == this.gzipInputBuffer.length) {
                    throw new RuntimeException("buffer overflow!");
                }
                final int k = gzipinputstream.read(this.gzipInputBuffer, i, this.gzipInputBuffer.length - i);
                if (k == -1) {
                    break;
                }
                i += k;
            } while (true);
        } catch (final IOException _ex) {
            throw new RuntimeException("error unzipping");
        }
        onDemandData.buffer = new byte[i];
        System.arraycopy(this.gzipInputBuffer, 0, onDemandData.buffer, 0, i);

        return onDemandData;
    }

    private void handleFailed() {
        this.uncompletedCount = 0;
        this.completedCount = 0;
        for (OnDemandData onDemandData = (OnDemandData) this.requested
            .peekFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getPrevious()) {
            if (onDemandData.incomplete) {
                this.uncompletedCount++;
            } else {
                this.completedCount++;
            }
        }

        while (this.uncompletedCount < 10) {
            final OnDemandData onDemandData_1 = (OnDemandData) this.unrequested.popFront();
            if (onDemandData_1 == null) {
                break;
            }
            if (this.filePriorities[onDemandData_1.dataType][onDemandData_1.id] != 0) {
                this.filesLoaded++;
            }
            this.filePriorities[onDemandData_1.dataType][onDemandData_1.id] = 0;
            this.requested.pushBack(onDemandData_1);
            this.uncompletedCount++;
            this.closeRequest(onDemandData_1);
            this.waiting = true;
        }
    }

    public int immediateRequestCount() {
        synchronized (this.nodeSubList) {
            return this.nodeSubList.getSize();
        }
    }

    public boolean method564(final int i) {
        for (int k = 0; k < this.mapSquareIds.length; k++) {
            if (this.objectMapIndices[k] == i) {
                return true;
            }
        }
        return false;
    }

    private void method568() {
        while (this.uncompletedCount == 0 && this.completedCount < 10) {
            if (this.highestPriority == 0) {
                break;
            }
            OnDemandData onDemandData;
            synchronized (this.passiveRequests) {
                onDemandData = (OnDemandData) this.passiveRequests.popFront();
            }
            while (onDemandData != null) {
                if (this.filePriorities[onDemandData.dataType][onDemandData.id] != 0) {
                    this.filePriorities[onDemandData.dataType][onDemandData.id] = 0;
                    this.requested.pushBack(onDemandData);
                    this.closeRequest(onDemandData);
                    this.waiting = true;
                    if (this.filesLoaded < this.totalFiles) {
                        this.filesLoaded++;
                    }
                    this.statusString = "Loading extra files - " + (this.filesLoaded * 100) / this.totalFiles + "%";
                    this.completedCount++;
                    if (this.completedCount == 10) {
                        return;
                    }
                }
                synchronized (this.passiveRequests) {
                    onDemandData = (OnDemandData) this.passiveRequests.popFront();
                }
            }
            for (int j = 0; j < 4; j++) {
                final byte[] abyte0 = this.filePriorities[j];
                final int k = abyte0.length;
                for (int l = 0; l < k; l++) {
                    if (abyte0[l] == this.highestPriority) {
                        abyte0[l] = 0;
                        final OnDemandData onDemandData_1 = new OnDemandData();
                        onDemandData_1.dataType = j;
                        onDemandData_1.id = l;
                        onDemandData_1.incomplete = false;
                        this.requested.pushBack(onDemandData_1);
                        this.closeRequest(onDemandData_1);
                        this.waiting = true;
                        if (this.filesLoaded < this.totalFiles) {
                            this.filesLoaded++;
                        }
                        this.statusString = "Loading extra files - " + (this.filesLoaded * 100) / this.totalFiles + "%";
                        this.completedCount++;
                        if (this.completedCount == 10) {
                            return;
                        }
                    }
                }

            }

            this.highestPriority--;
        }
    }

    public boolean midiIdEqualsOne(final int i) {
        return this.musicPriorities[i] == 1;
    }

    public void passiveRequest(final int id, final int type) {
        if (this.clientInstance.caches[0] == null) {
            return;
        }
        if (this.versions[type][id] == 0) {
            return;
        }
        if (this.filePriorities[type][id] == 0) {
            return;
        }
        if (this.highestPriority == 0) {
            return;
        }
        final OnDemandData onDemandData = new OnDemandData();
        onDemandData.dataType = type;
        onDemandData.id = id;
        onDemandData.incomplete = false;
        synchronized (this.passiveRequests) {
            this.passiveRequests.pushBack(onDemandData);
        }
    }

    public void preloadMapSquares(final boolean flag) {
        final int mapSquareCount = this.mapSquareIds.length;
        for (int mapSquareIndex = 0; mapSquareIndex < mapSquareCount; mapSquareIndex++) {
            if (flag || this.shouldPreloadMap[mapSquareIndex] != 0) {
                this.setPriority((byte) 2, 3, this.objectMapIndices[mapSquareIndex]);
                this.setPriority((byte) 2, 3, this.terrainMapIndices[mapSquareIndex]);
            }
        }
    }

    private void readData() {
        try {
            final int j = this.inputStream.available();
            if (this.expectedSize == 0 && j >= 6) {
                this.waiting = true;
                for (int k = 0; k < 6; k += this.inputStream.read(this.payload, k, 6 - k)) {
                }
                final int l = this.payload[0] & 0xff;
                final int j1 = ((this.payload[1] & 0xff) << 8) + (this.payload[2] & 0xff);
                final int l1 = ((this.payload[3] & 0xff) << 8) + (this.payload[4] & 0xff);
                final int i2 = this.payload[5] & 0xff;
                this.current = null;
                for (OnDemandData onDemandData = (OnDemandData) this.requested
                    .peekFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getPrevious()) {
                    if (onDemandData.dataType == l && onDemandData.id == j1) {
                        this.current = onDemandData;
                    }
                    if (this.current != null) {
                        onDemandData.loopCycle = 0;
                    }
                }

                if (this.current != null) {
                    this.loopCycle = 0;
                    if (l1 == 0) {
                        signlink.reporterror("Rej: " + l + "," + j1);
                        this.current.buffer = null;
                        if (this.current.incomplete) {
                            synchronized (this.complete) {
                                this.complete.pushBack(this.current);
                            }
                        } else {
                            this.current.unlink();
                        }
                        this.current = null;
                    } else {
                        if (this.current.buffer == null && i2 == 0) {
                            this.current.buffer = new byte[l1];
                        }
                        if (this.current.buffer == null && i2 != 0) {
                            throw new IOException("missing start of file");
                        }
                    }
                }
                this.completedSize = i2 * 500;
                this.expectedSize = 500;
                if (this.expectedSize > l1 - i2 * 500) {
                    this.expectedSize = l1 - i2 * 500;
                }
            }
            if (this.expectedSize > 0 && j >= this.expectedSize) {
                this.waiting = true;
                byte[] abyte0 = this.payload;
                int i1 = 0;
                if (this.current != null) {
                    abyte0 = this.current.buffer;
                    i1 = this.completedSize;
                }
                for (int k1 = 0; k1 < this.expectedSize; k1 += this.inputStream.read(abyte0, k1 + i1, this.expectedSize - k1)) {
                }
                if (this.expectedSize + this.completedSize >= abyte0.length && this.current != null) {
                    if (this.clientInstance.caches[0] != null) {
                        this.clientInstance.caches[this.current.dataType + 1].put(abyte0.length, abyte0, this.current.id);
                    }
                    if (!this.current.incomplete && this.current.dataType == 3) {
                        this.current.incomplete = true;
                        this.current.dataType = 93;
                    }
                    if (this.current.incomplete) {
                        synchronized (this.complete) {
                            this.complete.pushBack(this.current);
                        }
                    } else {
                        this.current.unlink();
                    }
                }
                this.expectedSize = 0;
            }
        } catch (final IOException ioexception) {
            try {
                this.socket.close();
            } catch (final Exception _ex) {
            }
            this.socket = null;
            this.inputStream = null;
            this.outputStream = null;
            this.expectedSize = 0;
        }
    }

    public void request(final int i) {
        this.request(0, i);
    }

    public void request(final int i, final int j) {
        if (i < 0 || i > this.versions.length || j < 0 || j > this.versions[i].length) {
            return;
        }
        if (this.versions[i][j] == 0) {
            return;
        }
        synchronized (this.nodeSubList) {
            for (OnDemandData onDemandData = (OnDemandData) this.nodeSubList
                .peek(); onDemandData != null; onDemandData = (OnDemandData) this.nodeSubList
                .getNext()) {
                if (onDemandData.dataType == i && onDemandData.id == j) {
                    return;
                }
            }

            final OnDemandData onDemandData_1 = new OnDemandData();
            onDemandData_1.dataType = i;
            onDemandData_1.id = j;
            onDemandData_1.incomplete = true;
            synchronized (this.mandatoryRequests) {
                this.mandatoryRequests.pushBack(onDemandData_1);
            }
            this.nodeSubList.push(onDemandData_1);
        }
    }

    @Override
    public void run() {
        try {
            while (this.running) {
                this.onDemandCycle++;
                int i = 20;
                if (this.highestPriority == 0 && this.clientInstance.caches[0] != null) {
                    i = 50;
                }
                try {
                    Thread.sleep(i);
                } catch (final Exception _ex) {
                }
                this.waiting = true;
                for (int j = 0; j < 100; j++) {
                    if (!this.waiting) {
                        break;
                    }
                    this.waiting = false;
                    this.checkReceived();
                    this.handleFailed();
                    if (this.uncompletedCount == 0 && j >= 5) {
                        break;
                    }
                    this.method568();
                    if (this.inputStream != null) {
                        this.readData();
                    }
                }

                boolean flag = false;
                for (OnDemandData onDemandData = (OnDemandData) this.requested
                    .peekFront(); onDemandData != null; onDemandData = (OnDemandData) this.requested.getPrevious()) {
                    if (onDemandData.incomplete) {
                        flag = true;
                        onDemandData.loopCycle++;
                        if (onDemandData.loopCycle > 50) {
                            onDemandData.loopCycle = 0;
                            this.closeRequest(onDemandData);
                        }
                    }
                }

                if (!flag) {
                    for (OnDemandData onDemandData_1 = (OnDemandData) this.requested
                        .peekFront(); onDemandData_1 != null; onDemandData_1 = (OnDemandData) this.requested
                        .getPrevious()) {
                        flag = true;
                        onDemandData_1.loopCycle++;
                        if (onDemandData_1.loopCycle > 50) {
                            onDemandData_1.loopCycle = 0;
                            this.closeRequest(onDemandData_1);
                        }
                    }

                }
                if (flag) {
                    this.loopCycle++;
                    if (this.loopCycle > 750) {
                        try {
                            this.socket.close();
                        } catch (final Exception _ex) {
                        }
                        this.socket = null;
                        this.inputStream = null;
                        this.outputStream = null;
                        this.expectedSize = 0;
                    }
                } else {
                    this.loopCycle = 0;
                    this.statusString = "";
                }
                if (this.clientInstance.loggedIn && this.socket != null && this.outputStream != null
                    && (this.highestPriority > 0 || this.clientInstance.caches[0] == null)) {
                    this.writeLoopCycle++;
                    if (this.writeLoopCycle > 500) {
                        this.writeLoopCycle = 0;
                        this.payload[0] = 0;
                        this.payload[1] = 0;
                        this.payload[2] = 0;
                        this.payload[3] = 10;
                        try {
                            this.outputStream.write(this.payload, 0, 4);
                        } catch (final IOException _ex) {
                            this.loopCycle = 5000;
                        }
                    }
                }
            }
        } catch (final Exception exception) {
            signlink.reporterror("od_ex " + exception.getMessage());
        }
    }

    public void setPriority(final byte priority, final int type, final int index) {
        if (this.clientInstance.caches[0] == null) {
            return;
        }
        if (this.versions[type][index] == 0) {
            return;
        }
        final byte[] abyte0 = this.clientInstance.caches[type + 1].decompress(index);
        if (this.crcMatches(this.versions[type][index], this.crcs[type][index], abyte0)) {
            return;
        }
        this.filePriorities[type][index] = priority;
        if (priority > this.highestPriority) {
            this.highestPriority = priority;
        }
        this.totalFiles++;
    }

    public void start(final Archive streamLoader, final Client client1) {
        final String[] as = {"model_version", "anim_version", "midi_version", "map_version"};
        for (int i = 0; i < 4; i++) {
            final byte[] abyte0 = streamLoader.decompressFile(as[i]);
            final int j = abyte0.length / 2;
            final Buffer stream = new Buffer(abyte0);
            this.versions[i] = new int[j];
            this.filePriorities[i] = new byte[j];
            for (int l = 0; l < j; l++) {
                this.versions[i][l] = stream.getUnsignedLEShort();
            }

        }

        final String[] as1 = {"model_crc", "anim_crc", "midi_crc", "map_crc"};
        for (int k = 0; k < 4; k++) {
            final byte[] abyte1 = streamLoader.decompressFile(as1[k]);
            final int i1 = abyte1.length / 4;
            final Buffer stream_1 = new Buffer(abyte1);
            this.crcs[k] = new int[i1];
            for (int l1 = 0; l1 < i1; l1++) {
                this.crcs[k][l1] = stream_1.getInt();
            }

        }

        byte[] abyte2 = streamLoader.decompressFile("model_index");
        int j1 = this.versions[0].length;
        this.modelIndices = new byte[j1];
        for (int k1 = 0; k1 < j1; k1++) {
            if (k1 < abyte2.length) {
                this.modelIndices[k1] = abyte2[k1];
            } else {
                this.modelIndices[k1] = 0;
            }
        }

        abyte2 = streamLoader.decompressFile("map_index");
        Buffer stream2 = new Buffer(abyte2);

        // a mapsquare's index file is made up of 7 bytes
        j1 = abyte2.length / 7;
        this.mapSquareIds = new int[j1];
        this.terrainMapIndices = new int[j1];
        this.objectMapIndices = new int[j1];
        this.shouldPreloadMap = new int[j1];
        for (int i = 0; i < j1; i++) {
            this.mapSquareIds[i] = stream2.getUnsignedLEShort();
            this.terrainMapIndices[i] = stream2.getUnsignedLEShort();
            this.objectMapIndices[i] = stream2.getUnsignedLEShort();
            this.shouldPreloadMap[i] = stream2.getUnsignedByte();
        }

        abyte2 = streamLoader.decompressFile("anim_index");
        stream2 = new Buffer(abyte2);
        j1 = abyte2.length / 2;
        this.frames = new int[j1];
        for (int j2 = 0; j2 < j1; j2++) {
            this.frames[j2] = stream2.getUnsignedLEShort();
        }

        abyte2 = streamLoader.decompressFile("midi_index");
        stream2 = new Buffer(abyte2);
        j1 = abyte2.length;
        this.musicPriorities = new int[j1];
        for (int k2 = 0; k2 < j1; k2++) {
            this.musicPriorities[k2] = stream2.getUnsignedByte();
        }

        this.clientInstance = client1;
        this.running = true;
        this.clientInstance.startRunnable(this, 2);
    }
}
