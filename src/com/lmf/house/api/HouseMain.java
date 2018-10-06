package com.lmf.house.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lmf.common.Log;
import com.lmf.house.HouseCrawler;
import com.lmf.house.model.SearchModel;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

public class HouseMain {

	public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String startTime = df.format(new Date());
		Log.e("start: " + startTime);// new Date()为获取当前系统时间

		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 10;
		int maxDepthOfCrawling = 50;
		int maxPagesToFetch = -1;
		int politenessDelay = 100;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		// 设置搜索深度
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		// 设置搜索文件个数
		config.setMaxPagesToFetch(maxPagesToFetch);
		// 延迟策略
		config.setPolitenessDelay(politenessDelay);

		// config.setProxyHost("127.0.0.1");
		// config.setProxyPort(7777);

		APIFetcher pageFetcher = new APIFetcher(config);

//		HouseAPIDBManager.init();

		int max = getMaxCount(pageFetcher);
		int count = max / numberOfCrawlers + 1;
		Log.e("max is " + max);
//		if (max < 1) {
//			Log.e("error max:" + max);
//			System.exit(0);
//			return;
//		}
//		List<APIRunner> runnerList = new ArrayList<APIRunner>();
//		for (int i = 0; i < numberOfCrawlers; i++) {
//			APIRunner runner = new APIRunner(i * count, (i + 1) * count, HouseConfig.REQUEST_COUNT, pageFetcher);
//			runner.start();
//			runnerList.add(runner);
//		}
//
//		boolean isRunning = true;
//		while (isRunning) {
//			boolean isAlive = false;
//			for (APIRunner runner : runnerList) {
//				isAlive = runner.isAlive() || isAlive;
//			}
//			if (isAlive) {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			} else {
//				isRunning = false;
//			}
//		}

		String endTime = df.format(new Date());
		Log.e("end crawler:" + endTime + ",start crawler:" + startTime );
//		HouseDBManager.release();
		Log.e(">>>>>>>>>>");
		Log.flush();

	}

	private static int getMaxCount(APIFetcher pageFetcher) {
		HouseUrl url = new HouseUrl();
		HouseConfig.buildUrl(url);
		Gson gson = new Gson();
		try {
			String data = getPageString(pageFetcher, url);
			SearchModel model = parseString(gson, data);
			return model.data.total_count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static String getPageString(APIFetcher pageFetcher, HouseUrl url) throws Exception {
		String data = null;
		Log.i("url:"+url.url);
		APIFetchResult result = pageFetcher.fetchPage(url.url);
		data = EntityUtils.toString(result.getEntity());
		return data;
	}

	private static SearchModel parseString(Gson gson, String data) throws Exception {
		SearchModel model = null;
		Log.i(data);
		model = gson.fromJson(data, SearchModel.class);
		return model;

	}
}
