package com.jagex.runescape;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Represents a file cache containing multiple archives.
 */
final class FileCache {

    private static final byte[] buffer = new byte[520];
    private final RandomAccessFile dataFile;
    private final RandomAccessFile indexFile;
    private final int storeId;

    public FileCache(final RandomAccessFile data, final RandomAccessFile index, final int storeId) {
        this.storeId = storeId;
        this.dataFile = data;
        this.indexFile = index;
    }

    public synchronized byte[] decompress(final int index) {
        try {
            this.seek(this.indexFile, index * 6);
            int in;
            for (int r = 0; r < 6; r += in) {
                in = this.indexFile.read(buffer, r, 6 - r);
                if (in == -1) {
                    return null;
                }
            }

            final int size = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
            int sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);

            if (size < 0 || size > 0x7a120) {
                return null;
            }

            if (sector <= 0 || sector > this.dataFile.length() / 520L) {
                return null;
            }

            final byte[] decompressed = new byte[size];
            int read = 0;
            for (int part = 0; read < size; part++) {
                if (sector == 0) {
                    return null;
                }
                this.seek(this.dataFile, sector * 520);
                int r = 0;
                int unread = size - read;
                if (unread > 512) {
                    unread = 512;
                }
                int in_;
                for (; r < unread + 8; r += in_) {
                    in_ = this.dataFile.read(buffer, r, (unread + 8) - r);
                    if (in_ == -1) {
                        return null;
                    }
                }

                final int decompressedIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
                final int decompressedPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
                final int decompressedSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
                final int decompressedStoreId = buffer[7] & 0xff;

                if (decompressedIndex != index || decompressedPart != part || decompressedStoreId != this.storeId) {
                    return null;
                }

                if (decompressedSector < 0 || decompressedSector > this.dataFile.length() / 520L) {
                    return null;
                }

                for (int i = 0; i < unread; i++) {
                    decompressed[read++] = buffer[i + 8];
                }

                sector = decompressedSector;
            }

            return decompressed;
        } catch (final IOException _ex) {
            return null;
        }
    }

    public synchronized boolean put(final int size, final byte[] data, final int index) {
        boolean exists = this.put(true, index, size, data);
        if (!exists) {
            exists = this.put(false, index, size, data);
        }
        return exists;
    }

    private synchronized boolean put(boolean exists, final int index, final int size, final byte[] data) {
        try {
            int sector;
            if (exists) {
                this.seek(this.indexFile, index * 6);
                int in;
                for (int r = 0; r < 6; r += in) {
                    in = this.indexFile.read(buffer, r, 6 - r);
                    if (in == -1) {
                        return false;
                    }
                }

                sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
                if (sector <= 0 || sector > this.dataFile.length() / 520L) {
                    return false;
                }
            } else {
                sector = (int) ((this.dataFile.length() + 519L) / 520L);
                if (sector == 0) {
                    sector = 1;
                }
            }
            buffer[0] = (byte) (size >> 16);
            buffer[1] = (byte) (size >> 8);
            buffer[2] = (byte) size;
            buffer[3] = (byte) (sector >> 16);
            buffer[4] = (byte) (sector >> 8);
            buffer[5] = (byte) sector;
            this.seek(this.indexFile, index * 6);
            this.indexFile.write(buffer, 0, 6);
            int written = 0;
            for (int part = 0; written < size; part++) {
                int decompressedSector = 0;
                if (exists) {
                    this.seek(this.dataFile, sector * 520);
                    int read;
                    int in;
                    for (read = 0; read < 8; read += in) {
                        in = this.dataFile.read(buffer, read, 8 - read);
                        if (in == -1) {
                            break;
                        }
                    }

                    if (read == 8) {
                        final int decompressedIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
                        final int decompressedPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
                        decompressedSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8)
                            + (buffer[6] & 0xff);
                        final int decompressedStoreId = buffer[7] & 0xff;
                        if (decompressedIndex != index || decompressedPart != part || decompressedStoreId != this.storeId) {
                            return false;
                        }
                        if (decompressedSector < 0 || decompressedSector > this.dataFile.length() / 520L) {
                            return false;
                        }
                    }
                }

                if (decompressedSector == 0) {
                    exists = false;
                    decompressedSector = (int) ((this.dataFile.length() + 519L) / 520L);
                    if (decompressedSector == 0) {
                        decompressedSector++;
                    }
                    if (decompressedSector == sector) {
                        decompressedSector++;
                    }
                }

                if (size - written <= 512) {
                    decompressedSector = 0;
                }

                buffer[0] = (byte) (index >> 8);
                buffer[1] = (byte) index;
                buffer[2] = (byte) (part >> 8);
                buffer[3] = (byte) part;
                buffer[4] = (byte) (decompressedSector >> 16);
                buffer[5] = (byte) (decompressedSector >> 8);
                buffer[6] = (byte) decompressedSector;
                buffer[7] = (byte) this.storeId;
                this.seek(this.dataFile, sector * 520);
                this.dataFile.write(buffer, 0, 8);

                int unwritten = size - written;
                if (unwritten > 512) {
                    unwritten = 512;
                }
                this.dataFile.write(data, written, unwritten);
                written += unwritten;
                sector = decompressedSector;
            }
            return true;
        } catch (final IOException _ex) {
            return false;
        }
    }

    private synchronized void seek(final RandomAccessFile file, int position) throws IOException {
        if (position < 0 || position > 0x3c00000) {
            System.out.println("Badseek - pos:" + position + " len:" + file.length());
            position = 0x3c00000;
            try {
                Thread.sleep(1000L);
            } catch (final Exception _ex) {
            }
        }
        file.seek(position);
    }
}