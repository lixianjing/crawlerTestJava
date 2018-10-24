package com.lmf.house;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lmf.house.db.HouseJsonDBManager;

public class SaltUtils {
	public static List<String> readSalt() {
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
		list.addAll(HouseJsonDBManager.selectSalt());
		return list;

	}

	public static void writeSalt(String str) {

		if (str == null || str.length() == 0) {
			return;
		}
		if (!isAddsalt()) {
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

	public static boolean isAddsalt() {
		if (System.currentTimeMillis() % 100 == 11) {
			return true;
		} else {
			return false;
		}
	}
}
