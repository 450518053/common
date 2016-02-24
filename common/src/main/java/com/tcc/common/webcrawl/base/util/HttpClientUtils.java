package com.tcc.common.webcrawl.base.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
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
 * @author tcc 2015-12-11
 *
 */
@SuppressWarnings("unchecked")
public class HttpClientUtils {
	
	private static CloseableHttpClient					client;
	
	private static PoolingHttpClientConnectionManager	connectionManage;
	
	private static BasicCookieStore						cookieStore;
	
	static {
		cookieStore = new BasicCookieStore();
		RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
			.setSocketTimeout(15000)//请求超时
			.setConnectTimeout(15000)//连接超时
			.setConnectionRequestTimeout(15000)//从连接池中取连接的超时时间
			.setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllHosts(), new SecureRandom());//信任所有证书
		} catch (Exception e) {
			e.printStackTrace();
		}
		connectionManage = new PoolingHttpClientConnectionManager(RegistryBuilder
			.<ConnectionSocketFactory> create()
			.register("http", PlainConnectionSocketFactory.getSocketFactory())
			.register(
				"https",
				new SSLConnectionSocketFactory(sc, SSLConnectionSocketFactory
					.getDefaultHostnameVerifier())).build(), null, null, null, -1,
			TimeUnit.MILLISECONDS);
		connectionManage.setMaxTotal(10);//总最大连接
		connectionManage.setDefaultMaxPerRoute(10);//目前只有一个路由，因此让他等于最大值
		//		connectionManage.setDefaultConnectionConfig(defaultConnectionConfig)
		//		List<Header> defaultHeaders = new ArrayList<Header>();
		//		defaultHeaders.add(new BasicHeader("User-Agent", USER_AGENT));
		//		defaultHeaders.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"));
		//		defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		//		defaultHeaders.add(new BasicHeader("Connection", "keep-alive"));
		client = HttpClients
			.custom()
			//			.setDefaultHeaders(defaultHeaders)
			.setDefaultRequestConfig(requestConfig)
			//请求管理
			.setConnectionManager(connectionManage)
			//连接管理
			//			.disableAutomaticRetries()//禁用自动重试
			.setRetryHandler(
				new CustomHttpRequestRetryHandler(3, false, Arrays.asList(ConnectException.class,
					SocketTimeoutException.class, SocketException.class)))
			//自定义自动重试
			.setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
			.setDefaultCookieStore(cookieStore).build();
	}
	
	/**
	 * 下载图片
	 * @param requestUrl 请求地址
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage downloadImg(String requestUrl, HttpContext context)
																					throws Exception {
		return downloadImg(new HttpGet(requestUrl), context);
	}
	
	/**
	 * 下载图片
	 * @param requestUrl 请求地址
	 * @param headers 消息头
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage downloadImg(String requestUrl, List<Header> headers,
											HttpContext context) throws Exception {
		HttpGet httpGet = new HttpGet(requestUrl);
		for (Header header : headers) {
			httpGet.addHeader(header);
		}
		return downloadImg(httpGet, context);
	}
	
	/**
	 * 下载图片
	 * @param httpGet
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static BufferedImage downloadImg(HttpGet httpGet, HttpContext context) throws Exception {
		CloseableHttpResponse httpResponse = null;
		HttpEntity entity = null;
		InputStream in = null;
		BufferedImage image = null;
		try {
			httpResponse = client.execute(httpGet, context);
			if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
				//http返回码等于200
				entity = httpResponse.getEntity();
				in = entity.getContent();
				image = ImageIO.read(in);
				testPrint(httpGet, httpResponse, context);
			} else {
				throw new Exception("下载图片失败," + getResponseMess(httpResponse));
			}
		} catch (Exception e) {
			throw new Exception("下载图片异常," + getResponseMess(httpResponse), e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpGet.releaseConnection();
			} catch (IOException e) {
				throw new Exception("下载图片释放资源异常," + getResponseMess(httpResponse), e);
			}
		}
		return image;
	}
	
	/**
	 * post请求
	 * @param requestUrl
	 * @param params
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String post(String requestUrl, HttpEntity params, HttpContext context)
																						throws Exception {
		HttpPost httpPost = new HttpPost(requestUrl);
		httpPost.setEntity(params);
		return post(httpPost, context);
	}
	
	/**
	 * post请求
	 * @param requestUrl
	 * @param params
	 * @param headers
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String post(String requestUrl, HttpEntity params, List<Header> headers,
								HttpContext context) throws Exception {
		HttpPost httpPost = new HttpPost(requestUrl);
		for (Header header : headers) {
			httpPost.addHeader(header);
		}
		httpPost.setEntity(params);
		return post(httpPost, context);
	}
	
	/**
	 * post请求
	 * @param httpPost
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String post(HttpPost httpPost, HttpContext context) throws Exception {
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost, context);
			entity = httpResponse.getEntity();
			testPrint(httpPost, httpResponse, context);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			throw new Exception("发起post请求异常,requestUrl:" + httpPost.getURI() + ","
								+ getResponseMess(httpResponse), e);
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpPost.releaseConnection();
			} catch (IOException e) {
				throw new Exception("发起post请求释放资源异常,requestUrl:" + httpPost.getURI() + ","
									+ getResponseMess(httpResponse), e);
			}
		}
	}
	
	/**
	 * get请求
	 * @param requestUrl
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String get(String requestUrl, HttpContext context) throws Exception {
		HttpGet httpGet = new HttpGet(requestUrl);
		return get(httpGet, context);
	}
	
	/**
	 * get请求
	 * @param requestUrl
	 * @param headers
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String get(String requestUrl, List<Header> headers, HttpContext context)
																							throws Exception {
		HttpGet httpGet = new HttpGet(requestUrl);
		for (Header header : headers) {
			httpGet.addHeader(header);
		}
		return get(httpGet, context);
	}
	
	/**
	 * get请求
	 * @param httpGet
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public static String get(HttpGet httpGet, HttpContext context) throws Exception {
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet, context);
			entity = httpResponse.getEntity();
			testPrint(httpGet, httpResponse, context);
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			throw new Exception("发起get请求异常,requestUrl:" + httpGet.getURI() + ","
								+ getResponseMess(httpResponse), e);
		} finally {
			try {
				EntityUtils.consume(entity);
				if (httpResponse != null) {
					httpResponse.close();
				}
				httpGet.releaseConnection();
			} catch (IOException e) {
				throw new Exception("发起get请求释放资源异常,requestUrl:" + httpGet.getURI() + ","
									+ getResponseMess(httpResponse), e);
			}
		}
	}
	
	static class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler {
		
		/**重试次数*/
		private final int								retryTime;
		
		private final boolean							requestSentRetryEnabled;
		
		/**可重试的异常类*/
		private final Set<Class<? extends IOException>>	retriableClasses;
		
		private CustomHttpRequestRetryHandler(int retryTime, boolean requestSentRetryEnabled,
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
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
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
	
	static class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
		
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
			HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
			if ("file.api.weixin.qq.com".equalsIgnoreCase(target.getHostName())) {//下载多媒体
				return 120 * 1000;
			} else {//其他
				return 60 * 1000;
			}
		}
	}
	
	/**
	 * 打印
	 * @param request
	 * @param httpResponse
	 * @param context
	 */
	private static void testPrint(HttpRequestBase request, HttpResponse httpResponse,
									HttpContext context) {
		StringBuilder builder = new StringBuilder();
		builder.append("请求:" + request.getMethod() + " " + request.getURI().toString() + "\r\n");
		builder.append(getHeadersMess(request.getAllHeaders()));
		builder.append("响应:\r\n");
		builder.append(getResponseMess(httpResponse) + "\r\n");
		builder.append("Cookie:\r\n");
		builder.append(getCookieMess(context));
		System.out.println(builder.toString());
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
	
	/**
	 * 打印消息头信息
	 * @param headers
	 * @return
	 */
	private static String getHeadersMess(Header[] headers) {
		StringBuilder builder = new StringBuilder();
		for (Header header : headers) {
			builder.append(header.getName() + ":" + header.getValue() + "\r\n");
		}
		return builder.toString();
	}
	
	/**
	 * 打印cookie信息
	 * @param context
	 * @return
	 */
	private static String getCookieMess(HttpContext context) {
		StringBuilder builder = new StringBuilder();
		CookieStore cookieStore = (CookieStore) context
			.getAttribute(HttpClientContext.COOKIE_STORE);
		for (Cookie cookie : cookieStore.getCookies()) {
			builder.append(cookie.getName() + ":" + cookie.getValue() + "\r\n");
		}
		return builder.toString();
	}
	
	/**
	 * 创建不验证证书链的信任管理器
	 * @return
	 */
	private static TrustManager[] trustAllHosts() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
																				throws CertificateException {
			}
			
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
																				throws CertificateException {
			}
			
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		} };
		return trustAllCerts;
	}
}
