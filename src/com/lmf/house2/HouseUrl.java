package com.lmf.house2;

import java.util.UUID;

public class HouseUrl {

	public int limitOffset = 0;
	public String url;
	// like b8d1a8e379b7001291e7606d82d7b35a
	public String deviceId = getRandomUUID();

	public static String getRandomUUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
}
