package com.tcc.common.httpclient.support;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**                    
 * @Filename HttpClientBuilder.java
 *
 * @Description HttpClient初始化类
 * 					适用httpClient 4.5版本
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 * 
 */
public class HttpClientBuilder {
	
	private static HttpClientBuilder			httpClientBuilder;
												
	private final int							defaultSocketTimeout;
												
	private final int							defaultConnectTimeout;
												
	private final int							defaultConnectionRequestTimeout;
												
	private final int							defaultMaxTotal;
												
	private final int							defaultMaxPerRoute;
												
	private final int							defaultKeepAliveTime;
												
	private PoolingHttpClientConnectionManager	connectionManage;
												
	private CloseableHttpClient					client;
												
	/**
	 * 重试处理程序
	 */
	private HttpRequestRetryHandler				retryHandler;
												
	/**
	 * keep-alive策略
	 */
	private ConnectionKeepAliveStrategy			keepAliveStrategy;
												
	/**
	* 获得一个实例
	* 	确保HttpClientBuilder中PoolingHttpClientConnectionManager，CloseableHttpClient只被初始化一次
	* @return
	*/
	public static HttpClientBuilder getInstance(BuilderParam builderParam) {
		if (httpClientBuilder == null) {
			httpClientBuilder = new HttpClientBuilder(builderParam.getDefaultSocketTimeout(),
				builderParam.getDefaultConnectTimeout(),
				builderParam.getDefaultConnectionRequestTimeout(),
				builderParam.getDefaultMaxTotal(), builderParam.getDefaultMaxPerRoute(),
				builderParam.getDefaultKeepAliveTime(), builderParam.getRetryHandler(),
				builderParam.getKeepAliveStrategy());
		} else {
			throw new IllegalArgumentException("不能重复初始化HttpClientBuilder");
		}
		return httpClientBuilder;
	}
	
	/**
	 * 构建一个<code>HttpClientBuilder.java</code>
	 * @param defaultSocketTimeout 请求超时，默认15000
	 * @param defaultConnectTimeout 连接超时，默认15000
	 * @param defaultConnectionRequestTimeout 从连接池中取连接的超时时间，默认15000
	 * @param defaultMaxTotal 总最大连接，默认300
	 * @param defaultMaxPerRoute 最大路由，默认200
	 * @param defaultKeepAliveTime keep-alive time，小于等于0关闭keep-alive策略
	 * @param retryHandler
	 * @param keepAliveStrategy
	 */
	private HttpClientBuilder(	int defaultSocketTimeout, int defaultConnectTimeout,
								int defaultConnectionRequestTimeout, int defaultMaxTotal,
								int defaultMaxPerRoute, int defaultKeepAliveTime,
								HttpRequestRetryHandler retryHandler,
								ConnectionKeepAliveStrategy keepAliveStrategy) {
		this.defaultSocketTimeout = defaultSocketTimeout > 0 ? defaultSocketTimeout : 15000;
		this.defaultConnectTimeout = defaultConnectTimeout > 0 ? defaultConnectTimeout : 15000;
		this.defaultConnectionRequestTimeout = defaultConnectionRequestTimeout > 0
			? defaultConnectionRequestTimeout : 15000;
		this.defaultMaxTotal = defaultMaxTotal > 0 ? defaultMaxTotal : 300;
		this.defaultMaxPerRoute = defaultMaxPerRoute > 0 ? defaultMaxPerRoute : 200;
		this.defaultKeepAliveTime = defaultKeepAliveTime;
		this.retryHandler = retryHandler;
		this.keepAliveStrategy = keepAliveStrategy;
		initialize();
	}
	
	/**
	 * 初始化一个CloseableHttpClient
	 * @param isKeepAliveConfig 
	 * @return CloseableHttpClient
	 */
	@SuppressWarnings("unchecked")
	public void initialize() {
		RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
			.setSocketTimeout(defaultSocketTimeout)//请求超时
			.setConnectTimeout(defaultConnectTimeout)//连接超时
			.setConnectionRequestTimeout(defaultConnectionRequestTimeout)//从连接池中取连接的超时时间
			.build();
		connectionManage = new PoolingHttpClientConnectionManager();
		connectionManage.setMaxTotal(defaultMaxTotal);//总最大连接
		connectionManage.setDefaultMaxPerRoute(defaultMaxPerRoute);//最大路由
		//默认重试处理程序
		if (retryHandler == null) {
			retryHandler = new CustomHttpRequestRetryHandler(3, false, Arrays.asList(
				ConnectException.class, SocketTimeoutException.class, SocketException.class));
		}
		org.apache.http.impl.client.HttpClientBuilder builder = HttpClients.custom();
		builder.setDefaultRequestConfig(requestConfig)//请求管理
			.setConnectionManager(connectionManage)//连接管理
			.setRetryHandler(retryHandler);//重试处理程序
		if (defaultKeepAliveTime > 0) {
			//默认keep-alive策略
			if (keepAliveStrategy == null) {
				keepAliveStrategy = new CustomConnectionKeepAliveStrategy();
			}
			builder.setKeepAliveStrategy(keepAliveStrategy);
		}
		this.client = builder.build();
	}
	
	public PoolingHttpClientConnectionManager getConnectionManage() {
		return connectionManage;
	}
	
	public CloseableHttpClient getClient() {
		return client;
	}
	
	private class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler {
		
		/**重试次数*/
		private final int								retryTime;
														
		private final boolean							requestSentRetryEnabled;
														
		/**可重试的异常类*/
		private final Set<Class<? extends IOException>>	retriableClasses;
														
		private CustomHttpRequestRetryHandler(	int retryTime, boolean requestSentRetryEnabled,
												Collection<Class<? extends IOException>> clazzes) {
			this.retryTime = retryTime;
			this.requestSentRetryEnabled = requestSentRetryEnabled;
			this.retriableClasses = new HashSet<Class<? extends IOException>>();
			for (Class<? extends IOException> clazz : clazzes) {
				this.retriableClasses.add(clazz);
			}
		}
		
		/**
		 * 负责处理请求重试
		 * 当httpclient发送请求之后出现异常时，就会调用这个方法。
		 * 在该方法中根据已执行请求的次数、请求内容、异常信息判断是否继续重试，若继续重试返回true，否则返回false。
		 * @param exception
		 * @param executionCount 该方法已执行失败的次数
		 * @param context
		 * @return
		 * @see http://blog.csdn.net/bhq2010/article/details/9210007
		 * @see org.apache.http.client.HttpRequestRetryHandler#retryRequest(java.io.IOException, int, org.apache.http.protocol.HttpContext)
		 */
		public boolean retryRequest(IOException exception, int executionCount,
									HttpContext context) {
			if (executionCount >= retryTime) {
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			if (handleAsIdempotent(request)) {//请求是幂等的
				return true;
			}
			//如果该请求没有被完全发送，或者如果它没有被发送到已发送的重试的方法，重试
			if (!clientContext.isRequestSent() || requestSentRetryEnabled) {
				return true;
			}
			//是否是可重试的异常类
			if (retriableClasses.contains(exception.getClass())) {
				return true;
			} else {
				for (final Class<? extends IOException> allowException : retriableClasses) {
					if (allowException.isInstance(exception)) {
						return true;
					}
				}
			}
			return false;
		}
		
		protected boolean handleAsIdempotent(HttpRequest request) {
			return !(request instanceof HttpEntityEnclosingRequest);
		}
	}
	
	private class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
		
		private CustomConnectionKeepAliveStrategy() {
		
		}
		
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
			HeaderElementIterator it = new BasicHeaderElementIterator(
				response.headerIterator(HTTP.CONN_KEEP_ALIVE));
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
			//			HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
			//			if ("file.api.weixin.qq.com".equalsIgnoreCase(target.getHostName())) {//下载多媒体
			//				return defaultKeepAliveTime;
			//			} else {//其他
			//				return defaultKeepAliveTime
			//			}
			return defaultKeepAliveTime;
		}
	}
}
