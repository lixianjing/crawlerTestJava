package com.lmf.house.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerJsonModel {

	private static final int MAX_CONTENT_LENGTH = 1500;
	private static Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	public static final int STATUS_INIT = 0;
	public static final int STATUS_DONE = 1;
	public static final int STATUS_ERROR = 2;

	public String title;
	public String url;
	public String content;
	public int status = STATUS_INIT;

	public CrawlerJsonModel(String title, String url,  String content) {
		super();
		this.title = title;
		this.url = url;
		setContent(content);

	}

	public void setContent(String content) {
		if (content != null) {
			
			Matcher m = p.matcher(content);
			content = m.replaceAll("");

			if (content.length() > MAX_CONTENT_LENGTH) {
				this.content = content.substring(0, MAX_CONTENT_LENGTH);
			} else {
				this.content = content;
			}
		} else {
			this.content = content;
		}
	}

	@Override
	public String toString() {
		return "CrawlerJsonModel [title=" + title + ", url=" + url + ", content=" + content
				+ ", status=" + status + "]";
	}

	

}
