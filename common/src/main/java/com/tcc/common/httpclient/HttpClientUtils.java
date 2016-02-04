package com.tcc.common.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.tcc.common.log.Logger;
import com.tcc.common.log.LoggerFactory;
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
														
	private static final Logger			logger			= LoggerFactory
		.getLogger(HttpClientUtils.class);
		
	public static void setClient(CloseableHttpClient client) {
		HttpClientUtils.client = client;
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param bytes 字节数组
	 * @return utf-8编码字符串
	 * @throws Exception 
	 */
	public static String post(String requestUrl, byte[] bytes) throws Exception {
		ByteArrayEntity byteEntity = new ByteArrayEntity(bytes);
		return post(new HttpPost(requestUrl), byteEntity, defaultCharset);
	}
	
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param outputStr 字符串
	 * @return utf-8编码字符串
	 * @throws Exception 
	 */
	public static String post(String requestUrl, String outputStr) throws Exception {
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
	 * @throws Exception 
	 */
	public static String post(HttpPost httpPost, HttpEntity params) throws Exception {
		return post(httpPost, params, defaultCharset);
	}
	
	/**
	 * post请求
	 * @param httpPost 自定义消息头
	 * @param params 内容
	 * @return 指定charset编码字符串
	 * @throws Exception 
	 */
	public static String post(	HttpPost httpPost, HttpEntity params,
								String charset) throws Exception {
		HttpEntity entity = null;
		httpPost.setEntity(params);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (Exception e) {
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
	 * @throws Exception
	 */
	public static String get(String requestUrl) throws Exception {
		return get(new HttpGet(requestUrl), defaultCharset);
	}
	
	/**
	 * get请求get请求
	 * @param httpGet
	 * @return
	 * @throws Exception
	 */
	public static String get(HttpGet httpGet) throws Exception {
		return get(httpGet, defaultCharset);
	}
	
	/**
	 * get请求
	 * @param httpGet
	 * @param charset
	 * @return
	 * @throws Exception 
	 */
	public static String get(HttpGet httpGet, String charset) throws Exception {
		charset = checkCharset(charset);
		HttpEntity entity = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet);
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (Exception e) {
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
	 * @throws Exception 
	 */
	public static void download(File saveFile, String requestUrl) throws Exception {
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
				throw new Exception(String.valueOf(code));
			}
		} catch (Exception e) {
			logger.error("下载多媒体异常,保存路径'{}','{}'", saveFile.getAbsolutePath(),
				getResponseMess(httpResponse), e);
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				closeQuietly(httpResponse);
			} catch (IOException e) {
				logger.error("下载多媒体释放资源异常,保存路径'{}','{}'", saveFile.getAbsolutePath(),
					getResponseMess(httpResponse), e);
				throw e;
			}
		}
		
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
}
