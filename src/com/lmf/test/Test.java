package com.lmf.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.lmf.common.Log;
import com.lmf.common.Utils;
import com.lmf.house.model.HouseJsonModel;

public class Test {

	public static void main(String[] args) throws Exception {

		System.out.println("hello world" + System.getProperty("user.dir"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(new Date());
		Gson gson = new Gson();
		System.out.println("hello world:" + gson);
		// Log.init();
		// Log.e("eset");
		// Log.e("xxx");
		// Log.flush();
		writeSalt("sdf");
		writeSalt("saa");
		addSalt();
		// String str="https://bj.lianjia.com/ershoufang/101103243713.html?is_sem=1";
		// System.out.println(checkUrl(str));
		//

		// writeFile("test.txt","time:" + startTime+"\n");

		// String str = readToString("resources/one");
		//
		// System.out.println("111:" + str);
		// System.out.println("222:" + catchData(str));
		//
		// String json = catchJson(catchData(str));
		//
		// System.out.println("333:" + json);
		// System.out.println("444:" + catchHouseJsonModel(gson, json));

		Log.init();
		Log.e("test" + Utils.getLocalPath());
		Log.e("test");
		Log.flush();

	}

	private static void writeSalt(String str) {
		if (str == null || str.length() == 0) {
			return;
		}
		FileWriter writer = null;
		BufferedWriter bw = null;

		try {
			// read file content from file
			StringBuffer sb = new StringBuffer("");

			writer = new FileWriter("resources/salt", true);
			bw = new BufferedWriter(writer);

			bw.write(str + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private static String checkUrl(String str) {
		if (str == null) {
			return null;
		}
		int index = str.indexOf('?');
		if (index > 0) {
			str = str.substring(0, index);
		}
		return str;
	}

	public static void writeFile(String file, String content) {
		System.out.println("content:" + content);
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");
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

	public static String readToString(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	private static final String RESOURCE_TAG_INDEX = "require(['ershoufang/sellDetail/detailV3']";
	private static final String RESOURCE_TAG_END = "</script>";
	private static final String RESOURCE_TAG_JSON_BEGIN = "init({";
	private static final String RESOURCE_TAG_JSON_END = "})";

	private static String catchData(String rource) {
		if (rource == null) {
			return null;
		}
		try {
			int begin = rource.lastIndexOf(RESOURCE_TAG_INDEX);
			System.out.println("begin:" + begin);
			if (begin > 0) {
				int end = rource.indexOf(RESOURCE_TAG_END, begin);
				System.out.println("end:" + end);
				if (end > 0) {
					return rource.substring(begin, end);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String catchJson(String rource) {
		if (rource == null) {
			return null;
		}
		try {
			int begin = rource.lastIndexOf(RESOURCE_TAG_JSON_BEGIN);
			System.out.println("begin:" + begin);
			if (begin > 0) {
				int end = rource.indexOf(RESOURCE_TAG_JSON_END, begin);
				System.out.println("end:" + end);
				if (end > 0) {
					return rource.substring(begin + RESOURCE_TAG_JSON_BEGIN.length() - 1, end + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static HouseJsonModel catchHouseJsonModel(Gson gson, String json) {
		if (json != null) {
			return gson.fromJson(json, HouseJsonModel.class);
		}
		return null;
	}

	private static List<String> addSalt() {
		List<String> list = new ArrayList<String>();
		// from file;
		FileReader reader = null;
		BufferedReader br = null;
		String str = null;
		try {
			// read file content from file
			StringBuffer sb = new StringBuffer("");

			reader = new FileReader("resources/salt");
			br = new BufferedReader(reader);

			while ((str = br.readLine()) != null) {
				list.add(str);
				System.out.println(str);
			}
			br.close();
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// from db

		return list;

	}

}
