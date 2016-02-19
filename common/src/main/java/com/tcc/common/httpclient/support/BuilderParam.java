package com.tcc.common.httpclient.support;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectionKeepAliveStrategy;

/**                    
 * @Filename BuilderParam.java
 *
 * @Description 
 *
 * @author tan 2016年2月19日
 *
 * @email 450518053@qq.com
 * 
 */
public class BuilderParam {
	
	/**请求超时*/
	private int							defaultSocketTimeout;
										
	/**连接超时*/
	private int							defaultConnectTimeout;
										
	/**从连接池中取连接的超时时间*/
	private int							defaultConnectionRequestTimeout;
										
	/**总最大连接*/
	private int							defaultMaxTotal;
										
	/**最大路由*/
	private int							defaultMaxPerRoute;
										
	/**keep-alive策略中关闭连接时间*/
	private int							defaultKeepAliveTime;
										
	/**重试处理程序*/
	private HttpRequestRetryHandler		retryHandler;
										
	/**keep-alive策略*/
	private ConnectionKeepAliveStrategy	keepAliveStrategy;
										
	/**
	 * 构建一个<code>BuilderParam.java</code>
	 * @param defaultSocketTimeout
	 * @param defaultConnectTimeout
	 * @param defaultConnectionRequestTimeout
	 * @param defaultMaxTotal
	 * @param defaultMaxPerRoute
	 * @param defaultKeepAliveTime
	 */
	public BuilderParam(int defaultSocketTimeout, int defaultConnectTimeout,
						int defaultConnectionRequestTimeout, int defaultMaxTotal,
						int defaultMaxPerRoute, int defaultKeepAliveTime) {
		super();
		this.defaultSocketTimeout = defaultSocketTimeout;
		this.defaultConnectTimeout = defaultConnectTimeout;
		this.defaultConnectionRequestTimeout = defaultConnectionRequestTimeout;
		this.defaultMaxTotal = defaultMaxTotal;
		this.defaultMaxPerRoute = defaultMaxPerRoute;
		this.defaultKeepAliveTime = defaultKeepAliveTime;
	}
	
	public BuilderParam setDefaultSocketTimeout(int defaultSocketTimeout) {
		this.defaultSocketTimeout = defaultSocketTimeout;
		return this;
	}
	
	public BuilderParam setDefaultConnectTimeout(int defaultConnectTimeout) {
		this.defaultConnectTimeout = defaultConnectTimeout;
		return this;
	}
	
	public BuilderParam setDefaultConnectionRequestTimeout(int defaultConnectionRequestTimeout) {
		this.defaultConnectionRequestTimeout = defaultConnectionRequestTimeout;
		return this;
	}
	
	public BuilderParam setDefaultMaxTotal(int defaultMaxTotal) {
		this.defaultMaxTotal = defaultMaxTotal;
		return this;
	}
	
	public BuilderParam setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
		return this;
	}
	
	public BuilderParam setDefaultKeepAliveTime(int defaultKeepAliveTime) {
		this.defaultKeepAliveTime = defaultKeepAliveTime;
		return this;
	}
	
	public BuilderParam setRetryHandler(HttpRequestRetryHandler retryHandler) {
		this.retryHandler = retryHandler;
		return this;
	}
	
	public BuilderParam setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
		this.keepAliveStrategy = keepAliveStrategy;
		return this;
	}
	
	public int getDefaultSocketTimeout() {
		return defaultSocketTimeout;
	}
	
	public int getDefaultConnectTimeout() {
		return defaultConnectTimeout;
	}
	
	public int getDefaultConnectionRequestTimeout() {
		return defaultConnectionRequestTimeout;
	}
	
	public int getDefaultMaxTotal() {
		return defaultMaxTotal;
	}
	
	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}
	
	public int getDefaultKeepAliveTime() {
		return defaultKeepAliveTime;
	}
	
	public HttpRequestRetryHandler getRetryHandler() {
		return retryHandler;
	}
	
	public ConnectionKeepAliveStrategy getKeepAliveStrategy() {
		return keepAliveStrategy;
	}
	
}
