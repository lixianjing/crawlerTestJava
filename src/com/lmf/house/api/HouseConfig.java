package com.lmf.house.api;

public class HouseConfig {
	public static final String HOST = "http://app.api.ke.com";
	public static final String PATH = "/house/ershoufang/searchv4";
	public static final String CHANNEL = "channel";
	public static final String CITY_ID = "city_id";
	public static final String LIMIT_COUNT = "limit_count";
	public static final String LIMIT_OFFSET = "limit_offset";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String UTM_SOURCE = "utm_source";
	public static final String DEVICE_ID = "device_id";

	public static final String CHANNEL_TEXT = "ershoufang";
	public static final String CITY_ID_INT = "110000";
	public static final int REQUEST_COUNT = 40;
	public static final String ACCESS_TOKEN_TEXT = "";
	public static final String UTM_SOURCE_TEXT = "";

	public static String buildBaseURL() {
		return HOST + PATH + "?" + CHANNEL + "=" + CHANNEL_TEXT + "&" + CITY_ID + "=" + CITY_ID_INT + "&" + LIMIT_COUNT
				+ "=" + REQUEST_COUNT;
	}

	/**
	 * 
	 * @return like GET
	 *         /house/ershoufang/searchv2?channel=ershoufang&city_id=110000&
	 *         limit_count=20&limit_offset=20&access_token=&utm_source=&
	 *         device_id=b8d898e379b7001291e7606d82d7b35a
	 */
	public static void buildUrl(HouseUrl url) {
		url.url = HouseConfig.buildBaseURL() + "&" + HouseConfig.LIMIT_OFFSET + "=" + url.limitOffset + "&"
				+ HouseConfig.ACCESS_TOKEN + "=" + ACCESS_TOKEN_TEXT + "&" + HouseConfig.UTM_SOURCE + "="
				+ UTM_SOURCE_TEXT + "&" + DEVICE_ID + "=" + url.deviceId+ "&isSuggestion=0&condition=&isHotSearch=0&hasRecommend=0&isHistory=0";
	}

}
