package com.lmf.house2.model;

public class SearchModel {
	public String request_id;
	public int errno;
	public String error;
	public SearchDataModel data;
	@Override
	public String toString() {
		return "SearchModel [request_id=" + request_id + ", errno=" + errno + ", error=" + error + ", data=" + data
				+ "]";
	}
	
}
