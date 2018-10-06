package com.lmf.house;

import java.util.Set;
import java.util.regex.Pattern;

import com.lmf.common.Log;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class HouseCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpg|jpeg|ico" + "|png|tiff|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "xml" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

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

//				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//				String html = htmlParseData.getHtml();
//				String text = htmlParseData.getText();
//				System.out.println("html:" + html);
//				System.out.println("text:" + text);
//				try {
//					int i = html.indexOf("<!-- 房屋相关信息 -->");
//					if (i > 0) {
//						html = html.substring(i, i + 1500);
//						html = html.replaceAll("&nbsp;", "");
//						i = html.indexOf("</dl></div>");
//						if (i > 0) {
//							html = html.substring(0, i + 11);
//							html = html + "</div>";
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				//
//				HouseInfo info = manager.getHouseInfo(url, htmlParseData.getTitle(), html);
//				if (info != null) {
				//
//					HousePrice housePrice = HouseDBManager.getHousePrice(info.hid);
				//
//					if (housePrice == null) {
//						// insert
//						int id = HouseDBManager.insert(info);
//						if (id > 0) {
//							housePrice = new HousePrice();
//							housePrice.hid = info.hid;
//							housePrice.priceNew = info.price;
//							housePrice.priceOld = "";
//							housePrice.price_status = HousePrice.PRICE_STATUS_DEFAULT;
//							housePrice.house_price_info = info.price;
//							housePrice.update_id = HouseDBManager.getPriceInfoId(info.url);
//							housePrice.url = info.url;
//							housePrice.size = info.size;
//							housePrice.title = info.title;
//							housePrice.address = info.address;
//							housePrice.house_type = info.houseType;
//							housePrice.house_direct = info.houseDirect;
//							housePrice.house_height = info.houseHeight;
//							housePrice.stamp = System.currentTimeMillis() / 1000;
//							HouseDBManager.insertHousePrice(housePrice);
//						}
//					} else {
//						if (housePrice.priceNew == null) {
//							int id = HouseDBManager.insert(info);
//							if (id > 0) {
//								housePrice.hid = info.hid;
//								housePrice.priceNew = info.price;
//								housePrice.priceOld = "";
//								housePrice.price_status = HousePrice.PRICE_STATUS_DEFAULT;
//								housePrice.house_price_info = info.price;
//								housePrice.update_id = HouseDBManager.getPriceInfoId(info.url);
//								housePrice.url = info.url;
//								housePrice.size = info.size;
//								housePrice.title = info.title;
//								housePrice.address = info.address;
//								housePrice.house_type = info.houseType;
//								housePrice.house_direct = info.houseDirect;
//								housePrice.house_height = info.houseHeight;
//								housePrice.stamp = System.currentTimeMillis() / 1000;
//								HouseDBManager.updatePriceInfo(housePrice);
//							}
//						} else if (!housePrice.priceNew.equals(info.price)) {
//							int id = HouseDBManager.insert(info);
//							if (id > 0) {
//								housePrice.update_id = HouseDBManager.getPriceInfoId(info.url);
//								housePrice.priceOld = new String(housePrice.priceNew);
//								housePrice.priceNew = info.price;
//								housePrice.stamp = System.currentTimeMillis() / 1000;
//								housePrice.house_price_info = housePrice.house_price_info == null ? ""
//										: housePrice.house_price_info + "," + info.price;
//								HouseDBManager.updatePriceInfo(housePrice);
//							}
//						}
				//
//					}
				//
//				}
				//
			}

		}

	}
}