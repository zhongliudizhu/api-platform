package com.winstar.cashier.construction.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);

	public static String httpReader(String url, String code) {
		logger.info("GetPage:" + url);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		String result = null;
		try {
			client.executeMethod(method);
			int status = method.getStatusCode();
			if (status == HttpStatus.SC_OK) {
				result = method.getResponseBodyAsString();
			} else {
				logger.info("Method failed: " + method.getStatusLine());
			}
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.info("Please check your provided http address!");
			
		} catch (IOException e) {
			// 发生网络异常
			logger.info("发生网络异常！");
			
		} finally {
			// 释放连接
			if (method != null)
				method.releaseConnection();
			method = null;
			client = null;
		}
		return result;
	}

	public static String httpPost(String url, Map<String, String> paramMap, String code) {
		logger.info("GetPage:" + url);
		String content = null;
		if (url == null || url.trim().length() == 0 || paramMap == null || paramMap.isEmpty())
			return null;
		HttpClient httpClient = new HttpClient();
		// 设置header
		httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");//
		
		//20120627 by ljyan add 设置HttpPost编码
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, code);
		
		//添加信任链接 by ljyan20131008
		if(url.indexOf("https")>=0){
			Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
			Protocol.registerProtocol("https", myhttps);
		}
		
		PostMethod method = new PostMethod(url);
		Iterator<String> it = paramMap.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next() + StringUtils.EMPTY;
			Object o = paramMap.get(key);
			if (o != null && o instanceof String) {
				method.addParameter(new NameValuePair(key, o.toString()));
			}
			if (o != null && o instanceof String[]) {
				String[] s = (String[]) o;
				if (s != null)
					for (int i = 0; i < s.length; i++) {
						method.addParameter(new NameValuePair(key, s[i]));
					}
			}
		}
		try {
			int statusCode = httpClient.executeMethod(method);
			logger.info("httpClientUtils::statusCode=" + statusCode);
			logger.info(method.getStatusLine().toString());
			//content = new String(method.getResponseBody(), code);
			content = method.getResponseBodyAsString();
		} catch (Exception e) {
			logger.info("time out");
			
		} finally {
			if (method != null)
				method.releaseConnection();
			method = null;
			httpClient = null;
		}
		return content;

	}

	public static String httpPost(String url, Map<String, String> paramMap) {
		// 编码：GB2312
		return HttpClientUtil.httpPost(url, paramMap, "GB2312");
	}

}
