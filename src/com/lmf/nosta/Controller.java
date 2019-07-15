package com.lmf.nosta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static Set<String> hostsSet = new HashSet();

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "/data/crawl/root";
		int numberOfCrawlers = 10;
		int maxDepthOfCrawling = 500;
		int maxPagesToFetch = 20000;
		int politenessDelay = 1000;
		Date date = new Date();
		long begin = System.currentTimeMillis();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		System.out.println("start: " + df.format(new Date()));// new Date()为获取当前系统时间
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

//		hostsSet.add("most.gov.cn");
		hostsSet.add("nosta.gov.cn");

		controller.addSeed("http://www.nosta.gov.cn/web/index.aspx");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);
		long takeTime = (System.currentTimeMillis() - begin) / 1000;
		System.out.println("end:" + df.format(new Date()) + ",take:" + takeTime / 60 + " m " + takeTime % 60 + " s");// new
																														// Date()为获取当前系统时间
	}
}