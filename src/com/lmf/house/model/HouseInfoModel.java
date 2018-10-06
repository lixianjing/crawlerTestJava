package com.lmf.house.model;

import java.util.List;

public class HouseInfoModel {
	public String house_code;
	public String kv_house_type;
	public String title;
	public String cover_pic;
	public String comm_avg_price;
	public int district_id;
	public int bizcircle_id;
	public String bizcircle_name;
	public String community_id;
	public String community_name;
	public int blueprint_hall_num;
	public int blueprint_bedroom_num;
	public double area;
	public String price;
	public String unit_price;
	public String sign_price;
	public String sign_unit_price;
	public String sign_time;
	public String sign_source;
	public String floor_state;
	public String orientation;
	public float baidu_la;
	public float baidu_lo;
	
	public List<String> tags;
	public List<SchoolInfoModel> school_info;
	public List<SubwayInfoModel> subway_info;
	@Override
	public String toString() {
		return "HouseInfoModel [house_code=" + house_code + ", kv_house_type=" + kv_house_type + ", title=" + title
				+ ", cover_pic=" + cover_pic + ", comm_avg_price=" + comm_avg_price + ", district_id=" + district_id
				+ ", bizcircle_id=" + bizcircle_id + ", bizcircle_name=" + bizcircle_name + ", community_id="
				+ community_id + ", community_name=" + community_name + ", blueprint_hall_num=" + blueprint_hall_num
				+ ", blueprint_bedroom_num=" + blueprint_bedroom_num + ", area=" + area + ", price=" + price
				+ ", unit_price=" + unit_price + ", sign_price=" + sign_price + ", sign_unit_price=" + sign_unit_price
				+ ", sign_time=" + sign_time + ", sign_source=" + sign_source + ", floor_state=" + floor_state
				+ ", orientation=" + orientation + ", baidu_la=" + baidu_la + ", baidu_lo=" + baidu_lo + ", tags="
				+ tags + ", school_info=" + school_info + ", subway_info=" + subway_info + "]";
	}
	

 
}
