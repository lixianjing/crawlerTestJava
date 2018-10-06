/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmf.house2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import edu.uci.ics.crawler4j.crawler.authentication.NtAuthInfo;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Configurable;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.BasicAuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.crawler.exceptions.PageBiggerThanMaxSizeException;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar
 */
public class APIFetcher extends Configurable {
	protected static final Logger logger = LoggerFactory.getLogger(APIFetcher.class);

	protected PoolingHttpClientConnectionManager connectionManager;
	protected CloseableHttpClient httpClient;
	protected final Object mutex = new Object();
	protected long lastFetchTime = 0;
	protected APIIdleConnectionMonitorThread connectionMonitorThread = null;

	public APIFetcher(CrawlConfig config) {
		super(config);

		RequestConfig requestConfig = RequestConfig.custom().setExpectContinueEnabled(false)
				.setCookieSpec(CookieSpecs.DEFAULT).setRedirectsEnabled(false)
				.setSocketTimeout(config.getSocketTimeout()).setConnectTimeout(config.getConnectionTimeout()).build();

		RegistryBuilder<ConnectionSocketFactory> connRegistryBuilder = RegistryBuilder.create();
		connRegistryBuilder.register("http", PlainConnectionSocketFactory.INSTANCE);

		Registry<ConnectionSocketFactory> connRegistry = connRegistryBuilder.build();
		connectionManager = new PoolingHttpClientConnectionManager(connRegistry);
		connectionManager.setMaxTotal(config.getMaxTotalConnections());
		connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerHost());

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);
		clientBuilder.setUserAgent(config.getUserAgentString());
		clientBuilder.setDefaultHeaders(config.getDefaultHeaders());
		  if (config.getProxyHost() != null) {
		      if (config.getProxyUsername() != null) {
		        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		        credentialsProvider.setCredentials(new AuthScope(config.getProxyHost(), config.getProxyPort()),
		                                           new UsernamePasswordCredentials(config.getProxyUsername(),
		                                                                           config.getProxyPassword()));
		        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		      }

		      HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort());
		      clientBuilder.setProxy(proxy);
		      logger.debug("Working through Proxy: {}", proxy.getHostName());
		    }
		httpClient = clientBuilder.build();

		if (connectionMonitorThread == null) {
			connectionMonitorThread = new APIIdleConnectionMonitorThread(connectionManager);
		}
		connectionMonitorThread.start();
	}

	public APIFetchResult fetchPage(String webUrl)
			throws InterruptedException, IOException, PageBiggerThanMaxSizeException {
		// Getting URL, setting headers & content
		APIFetchResult fetchResult = new APIFetchResult();
		HttpUriRequest request = null;
		try {
			request = newHttpUriRequest(webUrl);

			// Applying Politeness delay
			synchronized (mutex) {
				long now = (new Date()).getTime();
				if ((now - lastFetchTime) < config.getPolitenessDelay()) {
					Thread.sleep(config.getPolitenessDelay() - (now - lastFetchTime));
				}
				lastFetchTime = (new Date()).getTime();
			}

			CloseableHttpResponse response = httpClient.execute(request);
			fetchResult.setEntity(response.getEntity());
			fetchResult.setResponseHeaders(response.getAllHeaders());

			// Setting HttpStatus
			int statusCode = response.getStatusLine().getStatusCode();

			// If Redirect ( 3xx )
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY
					|| statusCode == HttpStatus.SC_MULTIPLE_CHOICES || statusCode == HttpStatus.SC_SEE_OTHER
					|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT || statusCode == 308) { // todo
																								// follow
																								// https://issues.apache.org/jira/browse/HTTPCORE-389

				Header header = response.getFirstHeader("Location");
				if (header != null) {
					String movedToUrl = URLCanonicalizer.getCanonicalURL(header.getValue(), webUrl);
					fetchResult.setMovedToUrl(movedToUrl);
				}
			} else if (statusCode >= 200 && statusCode <= 299) { // is 2XX,
																	// everything
																	// looks ok
				fetchResult.setFetchedUrl(webUrl);
				String uri = request.getURI().toString();
				if (!uri.equals(webUrl)) {
					if (!URLCanonicalizer.getCanonicalURL(uri).equals(webUrl)) {
						fetchResult.setFetchedUrl(uri);
					}
				}

				// Checking maximum size
				if (fetchResult.getEntity() != null) {
					long size = fetchResult.getEntity().getContentLength();
					if (size == -1) {
						Header length = response.getLastHeader("Content-Length");
						if (length == null) {
							length = response.getLastHeader("Content-length");
						}
						if (length != null) {
							size = Integer.parseInt(length.getValue());
						}
					}
					if (size > config.getMaxDownloadSize()) {
						// fix issue #52 - consume entity
						response.close();
						throw new PageBiggerThanMaxSizeException(size);
					}
				}
			}

			fetchResult.setStatusCode(statusCode);
			return fetchResult;

		} finally { // occurs also with thrown exceptions
			if ((fetchResult.getEntity() == null) && (request != null)) {
				request.abort();
			}
		}
	}

	public synchronized void shutDown() {
		if (connectionMonitorThread != null) {
			connectionManager.shutdown();
			connectionMonitorThread.shutdown();
		}
	}

	/**
	 * Creates a new HttpUriRequest for the given url. The default is to create
	 * a HttpGet without any further configuration. Subclasses may override this
	 * method and provide their own logic.
	 *
	 * @param url
	 *            the url to be fetched
	 * @return the HttpUriRequest for the given url
	 */
	protected HttpUriRequest newHttpUriRequest(String url) {
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		request.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		request.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
		request.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
		
		request.addHeader("Host", "m.api.lianjia.com");
		request.addHeader("Connection", "keep-alive");
		request.addHeader("Upgrade-Insecure-Requests", "1");
		
		return request;
	}

}
