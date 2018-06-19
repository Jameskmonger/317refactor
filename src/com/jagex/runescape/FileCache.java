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

/**
 * 
 * Represents a file cache containing multiple archives.
 * 
 */
final class FileCache {

	private static final byte[] buffer = new byte[520];

	private final RandomAccessFile dataFile;

	private final RandomAccessFile indexFile;

	private final int storeId;

	public FileCache(RandomAccessFile data, RandomAccessFile index, int storeId) {
		this.storeId = storeId;
		this.dataFile = data;
		this.indexFile = index;
	}

	public synchronized byte[] decompress(int index) {
		try {
			seek(indexFile, index * 6);
			int in;
			for (int r = 0; r < 6; r += in) {
				in = indexFile.read(buffer, r, 6 - r);
				if (in == -1)
					return null;
			}

			int size = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);

			if (size < 0 || size > 0x7a120)
				return null;

			if (sector <= 0 || sector > dataFile.length() / 520L)
				return null;

			byte decompressed[] = new byte[size];
			int read = 0;
			for (int part = 0; read < size; part++) {
				if (sector == 0)
					return null;
				seek(dataFile, sector * 520);
				int r = 0;
				int unread = size - read;
				if (unread > 512)
					unread = 512;
				int in_;
				for (; r < unread + 8; r += in_) {
					in_ = dataFile.read(buffer, r, (unread + 8) - r);
					if (in_ == -1)
						return null;
				}

				int decompressedIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int decompressedPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int decompressedSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int decompressedStoreId = buffer[7] & 0xff;

				if (decompressedIndex != index || decompressedPart != part || decompressedStoreId != storeId)
					return null;

				if (decompressedSector < 0 || decompressedSector > dataFile.length() / 520L)
					return null;

				for (int i = 0; i < unread; i++)
					decompressed[read++] = buffer[i + 8];

				sector = decompressedSector;
			}

			return decompressed;
		} catch (IOException _ex) {
			return null;
		}
	}

	public synchronized boolean put(int size, byte data[], int index) {
		boolean exists = put(true, index, size, data);
		if (!exists)
			exists = put(false, index, size, data);
		return exists;
	}

	private synchronized boolean put(boolean exists, int index, int size, byte data[]) {
		try {
			int sector;
			if (exists) {
				seek(indexFile, index * 6);
				int in;
				for (int r = 0; r < 6; r += in) {
					in = indexFile.read(buffer, r, 6 - r);
					if (in == -1)
						return false;
				}

				sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
				if (sector <= 0 || sector > dataFile.length() / 520L)
					return false;
			} else {
				sector = (int) ((dataFile.length() + 519L) / 520L);
				if (sector == 0)
					sector = 1;
			}
			buffer[0] = (byte) (size >> 16);
			buffer[1] = (byte) (size >> 8);
			buffer[2] = (byte) size;
			buffer[3] = (byte) (sector >> 16);
			buffer[4] = (byte) (sector >> 8);
			buffer[5] = (byte) sector;
			seek(indexFile, index * 6);
			indexFile.write(buffer, 0, 6);
			int written = 0;
			for (int part = 0; written < size; part++) {
				int decompressedSector = 0;
				if (exists) {
					seek(dataFile, sector * 520);
					int read;
					int in;
					for (read = 0; read < 8; read += in) {
						in = dataFile.read(buffer, read, 8 - read);
						if (in == -1)
							break;
					}

					if (read == 8) {
						int decompressedIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
						int decompressedPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
						decompressedSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8)
								+ (buffer[6] & 0xff);
						int decompressedStoreId = buffer[7] & 0xff;
						if (decompressedIndex != index || decompressedPart != part || decompressedStoreId != storeId)
							return false;
						if (decompressedSector < 0 || decompressedSector > dataFile.length() / 520L)
							return false;
					}
				}

				if (decompressedSector == 0) {
					exists = false;
					decompressedSector = (int) ((dataFile.length() + 519L) / 520L);
					if (decompressedSector == 0)
						decompressedSector++;
					if (decompressedSector == sector)
						decompressedSector++;
				}

				if (size - written <= 512)
					decompressedSector = 0;

				buffer[0] = (byte) (index >> 8);
				buffer[1] = (byte) index;
				buffer[2] = (byte) (part >> 8);
				buffer[3] = (byte) part;
				buffer[4] = (byte) (decompressedSector >> 16);
				buffer[5] = (byte) (decompressedSector >> 8);
				buffer[6] = (byte) decompressedSector;
				buffer[7] = (byte) storeId;
				seek(dataFile, sector * 520);
				dataFile.write(buffer, 0, 8);

				int unwritten = size - written;
				if (unwritten > 512)
					unwritten = 512;
				dataFile.write(data, written, unwritten);
				written += unwritten;
				sector = decompressedSector;
			}
			return true;
		} catch (IOException _ex) {
			return false;
		}
	}

	private synchronized void seek(RandomAccessFile file, int position) throws IOException {
		if (position < 0 || position > 0x3c00000) {
			System.out.println("Badseek - pos:" + position + " len:" + file.length());
			position = 0x3c00000;
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		}
		file.seek(position);
	}
}