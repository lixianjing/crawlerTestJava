package com.lmf.house.api;

import java.util.UUID;

import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lmf.common.Log;
import com.lmf.house.db.HouseAPIDBManager;
import com.lmf.house.model.HouseInfoModel;
import com.lmf.house.model.HousePriceModel;
import com.lmf.house.model.SearchModel;

public class APIRunner extends Thread {

	public static final int TRY_COUNT = 5;
	private int index;
	private int endIndex;
	private int pageSize;
	private APIFetcher pageFetcher;
	private boolean isRunning;

	private int tryCount = 0;
	private int processCount = 0;

	public APIRunner(int index, int endIndex, int pageSize, APIFetcher pageFetcher) {
		this.index = index;
		this.endIndex = endIndex;
		this.pageSize = pageSize;
		this.pageFetcher = pageFetcher;
		isRunning = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.e(Thread.currentThread().getId() + " begin work index:" + index + " endIndex:" + endIndex);
		Gson gson = new Gson();
		String data = null;
		SearchModel model = null;
		HouseUrl houseUrl = new HouseUrl();
		houseUrl.limitOffset = index;
		while (isRunning) {
			if (tryCount > TRY_COUNT) {
				Log.e("error>>tryCount");
				isRunning = false;
				return;
			}

			HouseConfig.buildUrl(houseUrl);
//			Log.i("url:" + houseUrl.url);
			try {
				data = getPageString(houseUrl);
				model = parseString(gson, data);
				if (checkModel(houseUrl, model)) {
					Log.e(Thread.currentThread().getId() + " work done 111 index:" + index + " endIndex:" + endIndex + " processCount:"
							+ processCount);
					isRunning = false;
					return;
				}
				updateDB(model);
			} catch (Exception e) {
				e.printStackTrace();
				tryCount++;
				continue;
			}

			if (houseUrl.limitOffset > endIndex) {
				Log.e(Thread.currentThread().getId() + " work done 222 index:" + index + " endIndex:" + endIndex + " processCount:"
						+ processCount);
				isRunning = false;
				return;
			} else {
				houseUrl.limitOffset = houseUrl.limitOffset + pageSize;
			}
		}
		Log.e(Thread.currentThread().getId() + " work done 333 index:" + index + " endIndex:" + endIndex + " processCount:"
				+ processCount);
	}

	private boolean checkModel(HouseUrl url, SearchModel model) {
		if (model != null && model.data != null) {
			if (model.data.has_more_data != 1 || url.limitOffset > model.data.total_count) {
				return true;
			}
		}
		return false;
	}

	private String getPageString(HouseUrl url) throws Exception {
		String data = null;
		APIFetchResult result = pageFetcher.fetchPage(url.url);
		data = EntityUtils.toString(result.getEntity());
		return data;
	}

	private SearchModel parseString(Gson gson, String data) throws Exception {
		SearchModel model = null;
		model = gson.fromJson(data, SearchModel.class);
		return model;

	}

	private void updateDB(SearchModel model) {
		if (model != null && model.data != null && model.data.list != null && model.data.list.size() > 0) {
			for (HouseInfoModel info : model.data.list) {
				processCount++;
				HousePriceModel temp = HouseAPIDBManager.getHousePriceModelFromDB(info.house_code);
				if(temp==null){
					UUID uuid = UUID.randomUUID();
					int result=HouseAPIDBManager.insertInfo(uuid.toString(),info);
					if(result>0){
						HouseAPIDBManager.insertPrice(uuid.toString(),info);
					}
				}else{
					if(temp.price!=null&&temp.price.equals(info.price)){
						// do nothing;
					}else{
						UUID uuid = UUID.randomUUID();
						int result=HouseAPIDBManager.insertInfo(uuid.toString(),info);
						if(result>0){
							HouseAPIDBManager.updatePrice(uuid.toString(),info,temp);
						}
					}
				}
			}
		}
	}

}
