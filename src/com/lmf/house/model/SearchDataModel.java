package com.lmf.house.model;

import java.util.List;

public class SearchDataModel {
	public int total_count;
	public int return_count;
	public int has_more_data;
	public List<HouseInfoModel> list;
	@Override
	public String toString() {
		return "SearchDataModel [total_count=" + total_count + ", return_count=" + return_count + ", has_more_data="
				+ has_more_data + ", list=" + list + "]";
	}
	
}
