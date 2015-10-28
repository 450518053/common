package com.tcc.util;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**                    
 * @Filename HttpClientUtils.java
 *
 * @Description httpClient工具类
 *
 * @author tan 2015年10月26日
 *
 */
public class HttpClientUtils {
	
	private static CloseableHttpClient					client;
	
	private static PoolingHttpClientConnectionManager	connectionManage;
	
	private static int									defaultSocketTimeout			= 30000;	//请求超时
																									
	private static int									defaultConnectTimeout			= 30000;	//连接超时
																									
	private static int									defaultConnectionRequestTimeout	= 60000;	//从连接池中取连接的超时时间
																									
	private static int									defaultMaxTotal					= 200;		//总最大连接
																									
	private static int									defaultMaxPerRoute				= 200;		//最大路由
																									
	private static int									defaultKeepAliveTime			= 60000;	//keep-alive time
																									
	/**
	 * 初始化
	 */
	public static void init() {
		RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
			.setSocketTimeout(defaultSocketTimeout)//请求超时
			.setConnectTimeout(defaultConnectTimeout)//连接超时
			.setConnectionRequestTimeout(defaultConnectionRequestTimeout)//从连接池中取连接的超时时间
			.build();
		connectionManage = new PoolingHttpClientConnectionManager();
		connectionManage.setMaxTotal(defaultMaxTotal);//总最大连接
		connectionManage.setDefaultMaxPerRoute(defaultMaxPerRoute);//最大路由
		client = HttpClients.custom().setDefaultRequestConfig(requestConfig)//请求管理
			.setConnectionManager(connectionManage)//连接管理
			.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
				/**
				 * keep-alive策略
				 * 为了使connectionManage.closeExpiredConnections();起到作用
				 * 我们需要指定连接keep-alive策略，来告诉httpClient，哪些连接大概什么时候会过期，可以关闭他们
				 * @param response
				 * @param context
				 * @return
				 * @see org.apache.http.conn.ConnectionKeepAliveStrategy#getKeepAliveDuration(org.apache.http.HttpResponse, org.apache.http.protocol.HttpContext)
				 */
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					HeaderElementIterator it = new BasicHeaderElementIterator(response
						.headerIterator(HTTP.CONN_KEEP_ALIVE));
					while (it.hasNext()) {
						HeaderElement he = it.nextElement();
						String param = he.getName();
						String value = he.getValue();
						if (value != null && param.equalsIgnoreCase("timeout")) {
							try {
								return Long.parseLong(value) * 1000;
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
						}
					}
					return defaultKeepAliveTime;
				}
			}).build();
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param outputStr 内容
	 * @return utf-8编码字符串
	 * @throws Exception 
	 */
	public static String post(String requestUrl, String outputStr) throws Exception {
		HttpPost httpPost = new HttpPost(requestUrl);
		HttpEntity entity = null;
		if (outputStr != null) {
			StringEntity params = new StringEntity(outputStr, "UTF-8");
			httpPost.setEntity(params);
		}
		CloseableHttpResponse httpResponse = null;
		try {
			if (client == null) {
				init();
			}
			httpResponse = client.execute(httpPost);
			entity = httpResponse.getEntity();
			String jsonString = new String(EntityUtils.toString(entity).getBytes("ISO-8859-1"),
				"utf-8");
			return jsonString;
		} catch (Exception e) {
			throw new Exception("发起post请求异常,requestUrl:" + requestUrl + ",outputStr:" + outputStr
								+ "," + getResponseMess(httpResponse), e);
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpPost.releaseConnection();
			} catch (IOException e) {
				throw new Exception("发起post请求释放资源异常,requestUrl:" + requestUrl + ",outputStr:"
									+ outputStr + "," + getResponseMess(httpResponse), e);
			}
		}
	}
	
	/**
	 * get请求
	 * @param requestUrl 请求地址
	 * @return utf-8编码字符串
	 * @throws Exception 
	 */
	public static String get(String requestUrl) throws Exception {
		HttpGet httpGet = new HttpGet(requestUrl);
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			if (client == null) {
				init();
			}
			httpResponse = client.execute(httpGet);
			entity = httpResponse.getEntity();
			String jsonString = new String(EntityUtils.toString(entity).getBytes("ISO-8859-1"),
				"utf-8");
			return jsonString;
		} catch (Exception e) {
			throw new Exception("发起get请求异常,requestUrl:" + requestUrl + ","
								+ getResponseMess(httpResponse), e);
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpGet.releaseConnection();
			} catch (IOException e) {
				throw new Exception("发起get请求释放资源异常,requestUrl:" + requestUrl + ","
									+ getResponseMess(httpResponse), e);
			}
		}
	}
	
	/**
	 * 获取httpResponse状态码及消息头
	 * @return
	 */
	private static String getResponseMess(HttpResponse httpResponse) {
		if (httpResponse != null) {
			return "状态码:" + httpResponse.getStatusLine() + ",消息头:"
					+ Arrays.toString(httpResponse.getAllHeaders());
		}
		return "httpResponse=null";
	}
	
	public static void setDefaultSocketTimeout(int defaultSocketTimeout) {
		HttpClientUtils.defaultSocketTimeout = defaultSocketTimeout;
	}
	
	public static void setDefaultConnectTimeout(int defaultConnectTimeout) {
		HttpClientUtils.defaultConnectTimeout = defaultConnectTimeout;
	}
	
	public static void setDefaultConnectionRequestTimeout(int defaultConnectionRequestTimeout) {
		HttpClientUtils.defaultConnectionRequestTimeout = defaultConnectionRequestTimeout;
	}
	
	public static void setDefaultMaxTotal(int defaultMaxTotal) {
		HttpClientUtils.defaultMaxTotal = defaultMaxTotal;
	}
	
	public static void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		HttpClientUtils.defaultMaxPerRoute = defaultMaxPerRoute;
	}
	
	public static void setDefaultKeepAliveTime(int defaultKeepAliveTime) {
		HttpClientUtils.defaultKeepAliveTime = defaultKeepAliveTime;
	}
	
}
