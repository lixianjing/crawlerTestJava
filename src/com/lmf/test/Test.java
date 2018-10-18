package com.lmf.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class Test {

	public static void main(String[] args) throws Exception {

		
		System.out.println("hello world"+System.getProperty("user.dir"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(new Date());
		Gson gson=new Gson();
		System.out.println("hello world:"+gson);
		

		
//		writeFile("test.txt","time:" + startTime+"\n");
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

}
