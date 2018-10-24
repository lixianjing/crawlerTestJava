package com.lmf.house;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.lmf.common.Log;
import com.lmf.house.db.HouseJsonDBManager;
import com.lmf.house.model.CrawlerJsonModel;
import com.lmf.house.model.HouseJsonModel;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class HouseCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpg|jpeg|ico" + "|png|tiff|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "xml" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static final Gson gson = new Gson();
	private static final String RESOURCE_TAG_INDEX = "require(['ershoufang/sellDetail/detailV3']";
	private static final String RESOURCE_TAG_END = "</script>";
	private static final String RESOURCE_TAG_JSON_BEGIN = "init({";
	private static final String RESOURCE_TAG_JSON_END = "})";

	public static long count = 0;

	private Object lock = new Object();

	/**
	 * This method receives two parameters. The first parameter is the page in which
	 * we have discovered this new url and the second parameter is the new url. You
	 * should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic). In this example, we are
	 * instructing the crawler to ignore urls that have css, js, git, ... extensions
	 * and to only accept urls that start with "http://www.ics.uci.edu/". In this
	 * case, we didn't need the referringPage parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		if ((href.endsWith("html")) && (href.startsWith(HouseConstant.SEED_URL_WEB))) {
			return true;
		}

		if ((href.startsWith(HouseConstant.SEED_URL_WEB_PAGE))) {
			return true;
		}

		return false;

	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if ((url.endsWith("html")) && (url.startsWith(HouseConstant.SEED_URL_WEB))) {
			Log.i("URL: " + url);
			if (page.getParseData() instanceof HtmlParseData) {
				count++;
				HtmlParseData parseData = (HtmlParseData) page.getParseData();

				CrawlerJsonModel model = new CrawlerJsonModel(parseData.getTitle(), url, parseData.getText());

				HouseJsonModel houseModel = catchHouseJsonModel(gson, catchJson(catchData(parseData.getHtml())));
				if (houseModel == null) {
					model.status = CrawlerJsonModel.STATUS_ERROR;
					Log.e("error:" + model.url);
				} else {
					Log.i("done:" + model.url);
					model.status = CrawlerJsonModel.STATUS_DONE;
				}
				HouseJsonDBManager.insertCrawler(model);
				HouseJsonDBManager.insertHouse(houseModel);
				System.out.println("model:>>>>>>>>" + model.toString());
				System.out.println("houseModel:>>>>>>>>" + houseModel.toString());

			}

		}

	}

	private String catchData(String rource) {
		if (rource == null) {
			return null;
		}
		try {
			int begin = rource.lastIndexOf(RESOURCE_TAG_INDEX);
			if (begin > 0) {
				int end = rource.indexOf(RESOURCE_TAG_END, begin);
				if (end > 0) {
					return rource.substring(begin, end);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String catchJson(String rource) {
		if (rource == null) {
			return null;
		}
		try {
			int begin = rource.lastIndexOf(RESOURCE_TAG_JSON_BEGIN);

			if (begin > 0) {
				int end = rource.indexOf(RESOURCE_TAG_JSON_END, begin);

				if (end > 0) {
					return rource.substring(begin + RESOURCE_TAG_JSON_BEGIN.length() - 1, end + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private HouseJsonModel catchHouseJsonModel(Gson gson, String json) {
		if (json != null) {
			return gson.fromJson(json, HouseJsonModel.class);
		}
		return null;
	}

}