package com.lmf.house;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lmf.common.Log;
import com.lmf.house.db.HouseJsonDBManager;

public class SaltUtils {
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	private static final int MAX_DB_COUNT = 100;

	public static List<String> readSalt() {
		// from db
		List<String> dblist = HouseJsonDBManager.selectSalt();
		if (dblist.size() > MAX_DB_COUNT) {
			HouseJsonDBManager.deleteSalt(System.currentTimeMillis() - ONE_DAY);
		}
		return dblist;
	}

	public static void insertSalt(String str) {

		if (str == null || str.length() == 0) {
			return;
		}
		if (!isAddsalt()) {
			return;
		}

		HouseJsonDBManager.insertSalt(str);

	}

	// public static void writeSalt(String str) {
	//
	// if (str == null || str.length() == 0) {
	// return;
	// }
	// if (!isAddsalt()) {
	// return;
	// }
	//
	// FileWriter writer = null;
	// BufferedWriter bw = null;
	//
	// try {
	// // read file content from file
	// StringBuffer sb = new StringBuffer("");
	//
	// writer = new FileWriter("resources/salt", true);
	// bw = new BufferedWriter(writer);
	//
	// bw.write(str + "\n");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (bw != null) {
	// try {
	// bw.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// if (writer != null) {
	// try {
	// writer.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// }

	public static boolean isAddsalt() {
		if (System.currentTimeMillis() % 100 == 11) {
			return true;
		} else {
			return false;
		}
	}
}
