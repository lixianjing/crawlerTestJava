package com.lmf.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	private static final long MAX_BUFFER = 1024 * 1024;
	private static final String PATH = "logs";

	private static final SimpleDateFormat df = new SimpleDateFormat("yy_MM_dd_HH");
	private static StringBuffer builder = new StringBuffer();
	private static boolean isInit = false;

	public static void i(String content) {
		System.out.println(content);
		append(content);

	}

	public static void e(String content) {
		System.err.println(content);
		append(content);

	}

	public static void init() {
		try {
			File file = new File(PATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			isInit = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void flush() {
		if (!isInit) {
			return;
		}
		if (builder == null || builder.length() == 0) {
			return;
		}
		writeFile("log" + df.format(new Date()), builder.toString());
		builder.delete(0, builder.length() - 1);
	}

	private static void append(String str) {
		builder.append(str + "\n");
		if (builder.length() > MAX_BUFFER) {
			flush();
		}
	}

	private static void writeFile(String file, String content) {
		if (!isInit) {
			return;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(PATH + "/" + file, "rw");
			long fileLength = raf.length();
			raf.seek(fileLength);
			raf.writeBytes(content);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
