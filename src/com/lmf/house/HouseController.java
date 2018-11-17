package com.lmf.house;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lmf.common.Log;
import com.lmf.common.Utils;
import com.lmf.house.db.HouseJsonDBManager;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class HouseController {
	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 20;
		int maxDepthOfCrawling = 1000;
		int maxPagesToFetch = 10000000;
		int politenessDelay = 50;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(Utils.getLocalPath() + crawlStorageFolder);
		// 页面深度
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		// 页面个数
		config.setMaxPagesToFetch(maxPagesToFetch);
		// 延迟策略
		config.setPolitenessDelay(politenessDelay);
		onCreate();

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first URLs that
		 * are fetched and then the crawler starts following links which are found in
		 * these pages
		 */
		try {
			addSeed(controller);
			/*
			 * Start the crawl. This is a blocking operation, meaning that your code will
			 * reach the line after this only when crawling is finished.
			 */
			controller.start(HouseCrawler.class, numberOfCrawlers);
		} catch (Exception e) {
			Log.e(e.getMessage());
		}

		onDestory();
		System.exit(0);
	}

	private static long startStamp = 0;
	private static SimpleDateFormat df = null;

	private static void onCreate() {
		startStamp = System.currentTimeMillis();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = df.format(new Date());
		Log.init();
		Log.e("<<<<<<<<<<");
		cleanCrawler("crawlerTestJava");
		Log.e("start crawler:" + startTime);
		HouseJsonDBManager.init();
	}

	private static void onDestory() {
		long spendTime = System.currentTimeMillis() - startStamp;
		String endTime = df.format(new Date());
		Log.e("end crawler:" + endTime + ",spend:" + spendTime / 1000 / 60 + "min,total visit:" + HouseCrawler.count);
		HouseJsonDBManager.release();
		Log.e(">>>>>>>>>>");
		Log.flush();
	
	}
	private static void cleanCrawler(String key) {
		try {
			Process pid = Runtime.getRuntime().exec("ps -ef");
			BufferedReader br = new BufferedReader(new InputStreamReader(pid.getInputStream()), 2048);

			String line = null;
			List<String> pidList=new ArrayList<String>();

			while ((line = br.readLine()) != null) {
				if(line!=null&&line.contains(key)) {
					String strs[]=line.trim().split("  ");
					if(strs.length>2) {
						Log.e("current pid:"+strs[1]);
						pidList.add(strs[1]);
					}
				}
			}
			if(pidList.size()>0) {
				pidList.remove(pidList.size()-1);
			}
			
			for(String str:pidList) {
				Runtime.getRuntime().exec("kill -9 "+str);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * For each crawl, you need to add some seed urls. These are the first URLs that
	 * are fetched and then the crawler starts following links which are found in
	 * these pages
	 */
	private static void addSeed(CrawlController controller) {
		List<String> list = SaltUtils.readSalt();
		for (String str : list) {
			controller.addSeed(str);
		}
	}

}