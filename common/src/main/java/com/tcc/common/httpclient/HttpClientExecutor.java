package com.tcc.common.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.tcc.common.httpclient.support.HttpClientBuilder;
import com.tcc.common.log.Logger;
import com.tcc.common.log.LoggerFactory;
import com.tcc.common.util.ShutdownHooks;
import com.tcc.common.util.StringUtils;

/**
 * 
 * @Filename HttpClientExecutor.java
 *
 * @Description HttpClient执行类
 * 					适用httpClient 4.5版本
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class HttpClientExecutor {
	
	private static boolean								shutdown		= false;;
																		
	private static CloseableHttpClient					client;
														
	private static PoolingHttpClientConnectionManager	connectionManage;
														
	/**
	 * 默认编码
	 */
	private static final String							defaultCharset	= "utf-8";
																		
	private static final Logger							logger			= LoggerFactory
		.getLogger(HttpClientExecutor.class);
		
	/**
	 * 初始化，使用HttpClientUtils调用此方法
	 * @param httpClientBuilder
	 */
	public static void initialize(HttpClientBuilder httpClientBuilder) {
		if (client == null && connectionManage == null) {
			client = httpClientBuilder.getClient();
			connectionManage = httpClientBuilder.getConnectionManage();
			new IdleConnectionClearTask().start();
			ShutdownHooks.addShutdownHook(new Runnable() {
				
				public void run() {
					try {
						destory();
					} catch (IOException e) {
						logger.error("", e);
					}
					
				}
			}, "HttpClient销毁");
		} else {
			throw new IllegalArgumentException("不能重复初始化HttpClient");
		}
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param bytes 字节数组
	 * @return utf-8编码字符串
	 * @throws IOException 
	 */
	public static String post(String requestUrl, byte[] bytes) throws IOException {
		ByteArrayEntity byteEntity = new ByteArrayEntity(bytes);
		return post(new HttpPost(requestUrl), byteEntity, defaultCharset);
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param outputStr 字符串
	 * @return utf-8编码字符串
	 * @throws IOException 
	 */
	public static String post(String requestUrl, String outputStr) throws IOException {
		StringEntity stringEntity = null;
		if (outputStr != null) {
			stringEntity = new StringEntity(outputStr, defaultCharset);
		}
		return post(new HttpPost(requestUrl), stringEntity, defaultCharset);
	}
	
	/**
	 * post请求
	 * @param httpPost
	 * @param params 内容
	 * @throws IOException 
	 */
	public static String post(HttpPost httpPost, HttpEntity params) throws IOException {
		return post(httpPost, params, defaultCharset);
	}
	
	/**
	 * post请求
	 * @param httpPost 自定义消息头
	 * @param params 内容
	 * @return 指定charset编码字符串
	 * @throws IOException 
	 */
	public static String post(	HttpPost httpPost, HttpEntity params,
								String charset) throws IOException {
		HttpEntity entity = null;
		httpPost.setEntity(params);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (IOException e) {
			logger.error("发起post请求异常,请求地址'{}',请求参数'{}'" + ",'{}'", httpPost.getURI().toString(),
				params.toString(), getResponseMess(httpResponse), e);
			throw e;
		} finally {
			closeQuietly(httpResponse);
		}
	}
	
	/**
	 * get请求
	 * @param requestUrl 请求地址
	 * @return
	 * @throws IOException
	 */
	public static String get(String requestUrl) throws IOException {
		return get(new HttpGet(requestUrl), defaultCharset);
	}
	
	/**
	 * get请求get请求
	 * @param httpGet
	 * @return
	 * @throws IOException
	 */
	public static String get(HttpGet httpGet) throws IOException {
		return get(httpGet, defaultCharset);
	}
	
	/**
	 * get请求
	 * @param httpGet
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static String get(HttpGet httpGet, String charset) throws IOException {
		charset = checkCharset(charset);
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (IOException e) {
			logger.error("发起get请求异常,请求地址'{}','{}'", httpGet.getURI().toString(),
				getResponseMess(httpResponse), e);
			throw e;
		} finally {
			closeQuietly(httpResponse);
		}
	}
	
	/**
	 * 下载文件，不抛异常视为下载成功
	 * @param saveFile
	 * @param requestUrl 完整的请求地址
	 * @return 
	 * @throws IOException 
	 */
	public static void download(File saveFile, String requestUrl) throws IOException {
		CloseableHttpResponse httpResponse = null;
		HttpEntity entity = null;
		InputStream in = null;
		FileOutputStream out = null;
		HttpGet httpGet = new HttpGet(requestUrl);
		try {
			httpResponse = client.execute(httpGet);
			int code = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == code) {
				//http返回码等于200
				entity = httpResponse.getEntity();
				in = entity.getContent();
				int length = 0;
				byte[] bufferOut = new byte[1024];
				out = new FileOutputStream(saveFile);
				while ((length = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, length);
				}
				out.flush();
			} else {
				throw new IOException(String.valueOf(code));
			}
		} catch (IOException e) {
			logger.error("下载多媒体异常,保存路径'{}','{}'", saveFile.getAbsolutePath(),
				getResponseMess(httpResponse), e);
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
			closeQuietly(httpResponse);
		}
		
	}
	
	/**
	 * 销毁
	 * @throws IOException
	 */
	public static void destory() throws IOException {
		connectionManage.close();
		client.close();
		shutdown = true;
	}
	
	/**
	 * @see org.apache.http.client.utils.HttpClientUtils#closeQuietly(HttpResponse)
	 * @param response
	 */
	public static void closeQuietly(final HttpResponse response) {
		org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
	}
	
	/**
	 * @see org.apache.http.client.utils.HttpClientUtils#closeQuietly(CloseableHttpResponse)
	 * @param response
	 */
	public static void closeQuietly(final CloseableHttpResponse response) {
		org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
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
	
	private static class IdleConnectionClearTask extends Thread {
		
		private long	housekeepingTimeout	= 30000;
											
		private long	closeIdleTimeout	= 120000;
											
		/**是否关闭过期连接*/
		private boolean	closeExpired		= true;
											
		@Override
		public void run() {
			logger.info("开启-->关闭过期连接定时任务");
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(housekeepingTimeout);
						logger.info("关闭过期连接");
						if (closeExpired) {
							connectionManage.closeExpiredConnections();
						}
						if (closeIdleTimeout > 0) {
							connectionManage.closeIdleConnections(closeIdleTimeout,
								TimeUnit.MILLISECONDS);
						}
					}
				}
				logger.info("Httpclient关闭，终止关闭过期连接定时任务");
			} catch (InterruptedException e) {
				logger.error("关闭过期连接定时任务异常，终止该定时任务", e);
			}
		}
		
	}
	
}
