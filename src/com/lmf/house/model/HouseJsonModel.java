package com.lmf.house.model;

import java.util.List;

public class HouseJsonModel {
	
	public String ucid;
	public String houseType;
	public String isUnique;
	public String registerTime;
	public String totalPrice;
	public String price;
	public String houseId;
	public String resblockId;
	public String resblockName;
	public String isRemove;
	public String defaultImg;
	public String defaultBrokerIcon;
	public String resblockPosition;
	public boolean hasDaikan;
	public boolean uniqueAgent;
	public String showCart;
	public boolean hasFangjia;
	public String test_400_hide;
	public boolean newTax;
	public String uuid;
	public String loadingImg;
	public String qrImg;
	public String title;
//	public List<ImageJsonModel> images;
//	public String videoId;
	
	@Override
	public String toString() {
		return "HouseJsonModel [ucid=" + ucid + ", houseType=" + houseType + ", isUnique=" + isUnique
				+ ", registerTime=" + registerTime + ", totalPrice=" + totalPrice + ", price=" + price + ", houseId="
				+ houseId + ", resblockId=" + resblockId + ", resblockName=" + resblockName + ", isRemove=" + isRemove
				+ ", defaultImg=" + defaultImg + ", defaultBrokerIcon=" + defaultBrokerIcon + ", resblockPosition="
				+ resblockPosition + ", hasDaikan=" + hasDaikan + ", uniqueAgent=" + uniqueAgent + ", showCart="
				+ showCart + ", hasFangjia=" + hasFangjia + ", test_400_hide=" + test_400_hide + ", newTax=" + newTax
				+ ", uuid=" + uuid + ", loadingImg=" + loadingImg + ", qrImg=" + qrImg + ", title=" + title
				+ "]";
	}

	
}
