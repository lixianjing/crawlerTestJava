package com.lmf.house;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lmf.common.Log;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class HouseController {
	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 20;
		int maxDepthOfCrawling = 500;
		int maxPagesToFetch = 10000;
		int politenessDelay = 100;
		long begin = System.currentTimeMillis();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String startTime = df.format(new Date());
		Log.e("<<<<<<<<<<");
		Log.e("start crawler:" + startTime);// new Date()为获取当前系统时间
//		HouseDBManager.init();
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		// 设置搜索深度
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		// 设置搜索文件个数
		config.setMaxPagesToFetch(maxPagesToFetch);
		// 延迟策略
		config.setPolitenessDelay(politenessDelay);
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

		controller.addSeed(HouseConstant.SEED_URL_MOBILE);
		controller.addSeed(HouseConstant.SEED_URL_WEB);
		for (int i = 2; i < 100; i++) {
			controller.addSeed(HouseConstant.SEED_URL_WEB_PAGE + i + "/");
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */
		controller.start(HouseCrawler.class, numberOfCrawlers);

		String endTime = df.format(new Date());
		Log.e("end crawler:" + endTime + ",start crawler:" + startTime + ",total visit:" + HouseCrawler.count);
//		HouseDBManager.release();
		Log.e(">>>>>>>>>>");
		Log.flush();
	}
}