package com.jagex.runescape.sign;

import java.applet.Applet;
import java.io.*;
import java.net.*;

public final class signlink implements Runnable {

	public static synchronized void dnslookup(final String s) {
		dns = s;
		dnsreq = s;
	}

	private static String findcachedir() {
		return "./cache/";
	}

	public static String findcachedirORIG() {
		final String[] as = {"c:/windows/", "c:/winnt/", "d:/windows/", "d:/winnt/", "e:/windows/", "e:/winnt/",
				"f:/windows/", "f:/winnt/", "c:/", "~/", "/tmp/", "", "c:/rscache", "/rscache"};
		if (storeid < 32 || storeid > 34) {
            storeid = 32;
        }
		final String s = ".file_store_" + storeid;
		for (int i = 0; i < as.length; i++) {
            try {
                final String s1 = as[i];
                if (s1.length() > 0) {
                    final File file = new File(s1);
                    if (!file.exists()) {
                        continue;
                    }
                }
                final File file1 = new File(s1 + s);
                if (file1.exists() || file1.mkdir()) {
                    return s1 + s + "/";
                }
            } catch (final Exception _ex) {
            }
        }

		return null;

	}

	private static int getuid(final String s) {
		try {
			final File file = new File(s + "uid.dat");
			if (!file.exists() || file.length() < 4L) {
				final DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(s + "uid.dat"));
				dataoutputstream.writeInt((int) (Math.random() * 99999999D));
				dataoutputstream.close();
			}
		} catch (final Exception _ex) {
		}
		try {
			final DataInputStream datainputstream = new DataInputStream(new FileInputStream(s + "uid.dat"));
			final int i = datainputstream.readInt();
			datainputstream.close();
			return i + 1;
		} catch (final Exception _ex) {
			return 0;
		}
	}

	public static synchronized void midisave(final byte[] abyte0, final int i) {
		if (i > 0x1e8480) {
            return;
        }
		if (savereq != null) {
		} else {
			midipos = (midipos + 1) % 5;
			savelen = i;
			savebuf = abyte0;
			midiplay = true;
			savereq = "jingle" + midipos + ".mid";
		}
	}

	public static synchronized Socket openSocket(final int port) throws IOException {
		for (socketreq = port; socketreq != 0;) {
            try {
                Thread.sleep(50L);
            } catch (final Exception _ex) {
            }
        }

		if (socket == null) {
            throw new IOException("could not open socket");
        } else {
            return socket;
        }
	}

	public static synchronized DataInputStream openurl(final String s) throws IOException {
		for (urlreq = s; urlreq != null;) {
            try {
                Thread.sleep(50L);
            } catch (final Exception _ex) {
            }
        }

		if (urlstream == null) {
            throw new IOException("could not open: " + s);
        } else {
            return urlstream;
        }
	}

	public static void reporterror(final String s) {
		System.out.println("Error: " + s);
	}

	public static void startpriv(final InetAddress inetaddress) {
		threadliveid = (int) (Math.random() * 99999999D);
		if (active) {
			try {
				Thread.sleep(500L);
			} catch (final Exception _ex) {
			}
			active = false;
		}
		socketreq = 0;
		threadreq = null;
		dnsreq = null;
		savereq = null;
		urlreq = null;
		socketip = inetaddress;
		final Thread thread = new Thread(new signlink());
		thread.setDaemon(true);
		thread.start();
		while (!active) {
            try {
                Thread.sleep(50L);
            } catch (final Exception _ex) {
            }
        }
	}

	public static synchronized void startThread(final Runnable runnable, final int priority) {
		threadreqpri = priority;
		threadreq = runnable;
	}

	public static synchronized boolean wavereplay() {
		if (savereq != null) {
			return false;
		} else {
			savebuf = null;
			waveplay = true;
			savereq = "sound" + wavepos + ".wav";
			return true;
		}
	}

	public static synchronized boolean wavesave(final byte[] abyte0, final int i) {
		if (i > 0x1e8480) {
            return false;
        }
		if (savereq != null) {
			return false;
		} else {
			wavepos = (wavepos + 1) % 5;
			savelen = i;
			savebuf = abyte0;
			waveplay = true;
			savereq = "sound" + wavepos + ".wav";
			return true;
		}
	}

	public static final int clientversion = 317;

	public static int uid;

	public static int storeid = 32;
	public static RandomAccessFile cache_dat = null;
	public static final RandomAccessFile[] cache_idx = new RandomAccessFile[5];
	public static boolean sunjava;
	public static final Applet applet = null;
	private static boolean active;
	private static int threadliveid;
	private static InetAddress socketip;
	private static int socketreq;
	private static Socket socket = null;
	private static int threadreqpri = 1;
	private static Runnable threadreq = null;
	private static String dnsreq = null;
	public static String dns = null;
	private static String urlreq = null;
	private static DataInputStream urlstream = null;
	private static int savelen;
	private static String savereq = null;
	private static byte[] savebuf = null;
	private static boolean midiplay;
	private static int midipos;
	public static String midi = null;
	public static int midiVolume;
	public static int midiFade;
	private static boolean waveplay;
	private static int wavepos;
	public static int wavevol;
	public static boolean reporterror = true;
	public static String errorname = "";

	private signlink() {
	}

	@Override
	public void run() {
		active = true;
		final String s = findcachedir();
		uid = getuid(s);
		try {
			final File file = new File(s + "main_file_cache.dat");
			if (file.exists() && file.length() > 0x3200000L) {
                file.delete();
            }
			cache_dat = new RandomAccessFile(s + "main_file_cache.dat", "rw");
			for (int j = 0; j < 5; j++) {
                cache_idx[j] = new RandomAccessFile(s + "main_file_cache.idx" + j, "rw");
            }

		} catch (final Exception exception) {
			exception.printStackTrace();
		}
		for (final int i = threadliveid; threadliveid == i;) {
			if (socketreq != 0) {
				try {
					socket = new Socket(socketip, socketreq);
				} catch (final Exception _ex) {
					socket = null;
				}
				socketreq = 0;
			} else if (threadreq != null) {
				final Thread thread = new Thread(threadreq);
				thread.setDaemon(true);
				thread.start();
				thread.setPriority(threadreqpri);
				threadreq = null;
			} else if (dnsreq != null) {
				try {
					dns = InetAddress.getByName(dnsreq).getHostName();
				} catch (final Exception _ex) {
					dns = "unknown";
				}
				dnsreq = null;
			} else if (savereq != null) {
				if (savebuf != null) {
                    try {
                        final FileOutputStream fileoutputstream = new FileOutputStream(s + savereq);
                        fileoutputstream.write(savebuf, 0, savelen);
                        fileoutputstream.close();
                    } catch (final Exception _ex) {
                    }
                }
				if (waveplay) {
					waveplay = false;
				}

				if (midiplay) {
					midi = s + savereq;
					midiplay = false;
				}

				savereq = null;
			} else if (urlreq != null) {
				try {
					System.out.println("urlstream");
					urlstream = new DataInputStream((new URL(applet.getCodeBase(), urlreq)).openStream());
				} catch (final Exception _ex) {
					urlstream = null;
				}
				urlreq = null;
			}
			try {
				Thread.sleep(50L);
			} catch (final Exception _ex) {
			}
		}

	}

}
