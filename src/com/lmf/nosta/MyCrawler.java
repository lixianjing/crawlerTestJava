package com.lmf.nosta;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|htm|html" + "|png|mp3|mp3|zip|gz))$");

	/**
	 * This method receives two parameters. The first parameter is the page in which
	 * we have discovered this new url and the second parameter is the new url. You
	 * should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic). In this example, we are
	 * instructing the crawler to ignore urls that have css, js, git, ... extensions
	 * and to only accept urls that start with "http://www.ics.uci.edu/". In this
	 * case, we didn't need the referringPage parameter to make the decision.
	 */

//	http://www.nosta.gov.cn/web/detail1.aspx?menuID=163&contentID=1572

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean bool = !FILTERS.matcher(href).matches() && checkHost(url.getDomain());
		return bool;
	}

	private boolean checkHost(String href) {
		boolean bool = Controller.hostsSet.contains(href);
		return bool;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		WebURL webUrl = page.getWebURL();
		System.out.println("url: " + webUrl.getURL());
//		if (webUrl.getPath() != null && webUrl.getURL().contains("contentID")) {
//			if (page.getParseData() instanceof HtmlParseData) {
//				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//				String text = htmlParseData.getText();
//				String html = htmlParseData.getHtml();
//
//				System.out.println("Text length: " + text.length());
//				System.out.println("Text: " + text);
//
//			}
//		}
	}
}