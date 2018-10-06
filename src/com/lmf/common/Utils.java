package com.lmf.common;

public class Utils {

	public static int parseInteger(String str) {
		if (str != null) {
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}