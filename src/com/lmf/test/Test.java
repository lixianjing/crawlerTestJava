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
		cleanCrawler("com.lmf.test.Test");
		System.out.println("hello world" + System.getProperty("user.dir"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(new Date());
		Gson gson = new Gson();
		System.out.println("hello world:" + gson);

		Thread.sleep(30 * 1000);

		String str = readToString("resources/all_ke");

//		String data = catchData(str);
//		String json = catchJson(data);
//		System.out.println("hello json:" + json);
//		HouseJsonModel houseModel = catchHouseJsonModel(gson, json);
//
//		System.out.println("hello world:" + houseModel);

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
			if (begin > 0) {
				int end = rource.indexOf(RESOURCE_TAG_END, begin);
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
					String resource = rource.substring(begin + RESOURCE_TAG_JSON_BEGIN.length() - 1, end + 1);
					for (int i = 2; i < 20; i++) {
						char c = resource.charAt(resource.length() - i);
						if (c == ' ' || c == '\n' || c == '\t') {
							continue;
						} else {
							if (c == ',') {
								return resource.substring(0, resource.length() - i) + "}";
							}
							break;
						}
					}
					return resource;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void cleanCrawler(String key) {
		try {
			Process pid = Runtime.getRuntime().exec("ps -ef");
			BufferedReader br = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);

			String line = null;
			List<String> pidList=new ArrayList<String>();

			while ((line = br.readLine()) != null) {
				if(line!=null&&line.contains(key)) {
					String strs[]=line.trim().split("  ");
//					for(String str:strs) {
//						System.out.println("str>>"+str);
//					}
//					System.out.println(line);	
					
					if(strs.length>2) {
						System.out.println(strs[1]);
						pidList.add(strs[1]);
					}
				}
			}
			if(pidList.size()>0) {
				pidList.remove(pidList.size()-1);
			}
			
			for(String str:pidList) {
				Runtime.getRuntime().exec("kill -9 "+str);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static HouseJsonModel catchHouseJsonModel(Gson gson, String json) {
		if (json != null) {
			return gson.fromJson(json, HouseJsonModel.class);
		}
		return null;
	}

}
