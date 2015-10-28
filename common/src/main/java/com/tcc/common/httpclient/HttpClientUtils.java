package com.tcc.common.httpclient;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcc.common.util.StringUtils;

/**
 * 
 * @Filename HttpClientUtils.java
 *
 * @Description httpClient工具类
 * 					适用httpClient 4.5版本
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class HttpClientUtils {
	
	/**
	 * @see com.tcc.util.CloseableHttpClientInit#初始化
	 */
	private static CloseableHttpClient	client;
										
	private static final String			defaultCharset	= "utf-8";
														
	private static Logger				logger			= LoggerFactory
		.getLogger(HttpClientUtils.class);
		
	public static void setClient(CloseableHttpClient client) {
		HttpClientUtils.client = client;
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param outputStr 内容
	 * @param charset
	 * @return
	 * @throws Exception 
	 */
	public static String post(	String requestUrl, String outputStr,
								String charset) throws Exception {
		charset = checkCharset(charset);
		HttpPost httpPost = new HttpPost(requestUrl);
		HttpEntity entity = null;
		if (outputStr != null) {
			StringEntity params = new StringEntity(outputStr, charset);
			httpPost.setEntity(params);
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (Exception e) {
			logger.error("发起post请求异常,requestUrl:"+ requestUrl + ",outputStr:" + outputStr + ","
							+ getResponseMess(httpResponse),
				e);
			throw e;
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpPost.releaseConnection();
			} catch (IOException e) {
				logger.error("发起post请求释放资源异常,requestUrl:"+ requestUrl + ",outputStr:" + outputStr
								+ "," + getResponseMess(httpResponse),
					e);
				throw e;
			}
		}
	}
	
	/**
	 * get请求
	 * @param requestUrl 请求地址
	 * @param charset
	 * @return
	 * @throws Exception 
	 */
	public static String get(String requestUrl, String charset) throws Exception {
		charset = checkCharset(charset);
		HttpGet httpGet = new HttpGet(requestUrl);
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (Exception e) {
			logger.error("发起get请求异常,requestUrl:" + requestUrl + "," + getResponseMess(httpResponse),
				e);
			throw e;
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpGet.releaseConnection();
			} catch (IOException e) {
				logger.error(
					"发起get请求释放资源异常,requestUrl:" + requestUrl + "," + getResponseMess(httpResponse),
					e);
				throw e;
			}
		}
	}
	
	/**
	 * 获取httpResponse状态码及消息头
	 * @return
	 */
	private static String getResponseMess(HttpResponse httpResponse) {
		if (httpResponse != null) {
			return "状态码:"+ httpResponse.getStatusLine() + ",消息头:"
					+ Arrays.toString(httpResponse.getAllHeaders());
		}
		return "httpResponse=null";
	}
	
	private static String checkCharset(String charset) {
		if (StringUtils.isBlank(charset)) {
			charset = defaultCharset;
		}
		return charset;
	}
}
