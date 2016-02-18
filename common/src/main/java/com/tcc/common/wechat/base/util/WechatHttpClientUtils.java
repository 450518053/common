package com.tcc.common.wechat.base.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.ByteArrayBuffer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.httpclient.HttpClientUtils;
import com.tcc.common.log.Logger;
import com.tcc.common.log.LoggerFactory;

/**                    
 * @Filename WechatHttpClientUtils.java
 *
 * @Description 
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class WechatHttpClientUtils {
	
	/**
	 * 是否开启执行记录日志
	 */
	private static final boolean	IS_LOG	= true;
											
	private static final Logger		logger	= LoggerFactory.getLogger(WechatHttpClientUtils.class);
											
	/**
	 * post请求
	 * @param requestUrl 请求地址
	 * @param outputStr 内容
	 * @return JSONObject
	 * @throws IOException 
	 */
	public static JSONObject post(String requestUrl, String outputStr) throws IOException {
		long start = System.currentTimeMillis();
		try {
			return JSON.parseObject(HttpClientUtils.post(requestUrl, outputStr));
		} finally {
			if (IS_LOG) {
				logger.info("post请求,执行时间'{}'毫秒,请求地址'{}',请求参数'{}'",
					System.currentTimeMillis() - start, requestUrl, outputStr);
			}
		}
	}
	
	/**
	 * get请求
	 * @param requestUrl 请求地址
	 * @return JSONObject
	 * @throws IOException 
	 */
	public static JSONObject get(String requestUrl) throws IOException {
		long start = System.currentTimeMillis();
		try {
			return JSON.parseObject(HttpClientUtils.get(requestUrl));
		} finally {
			if (IS_LOG) {
				logger.info("get请求,执行时间'{}'毫秒,请求地址'{}'", System.currentTimeMillis() - start,
					requestUrl);
			}
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
		long start = System.currentTimeMillis();
		try {
			HttpClientUtils.download(saveFile, requestUrl);
		} finally {
			if (IS_LOG) {
				logger.info("下载文件,执行时间'{}'毫秒,请求地址'{}',文件路径'{}'", System.currentTimeMillis() - start,
					requestUrl, saveFile.getAbsolutePath());
			}
		}
	}
	
	private static BasicHeader[] basicHeaders = {	new BasicHeader("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
													new BasicHeader("Accept-Encoding",
														"gzip, deflate"),
													new BasicHeader("Accept-Language",
														"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"),
													new BasicHeader("Host", "api.weixin.qq.com"),
													new BasicHeader("Connection", "keep-alive") };
													
	/**
	 * 上传文件
	 * @param uploadFile
	 * @param requestUrl 完整的请求地址
	 * @return 
	 * @throws IOException 
	 *///TODO 其他格式文件
	public static JSONObject uploadMedia(File uploadFile, String requestUrl) throws IOException {
		HttpPost httpPost = new HttpPost(requestUrl);
		httpPost.setHeaders(basicHeaders);
		String BOUNDARY = "---------------------------" + System.currentTimeMillis();
		httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		
		long start = System.currentTimeMillis();
		try {
			return JSON.parseObject(
				HttpClientUtils.post(httpPost, new ByteArrayEntity(packing(uploadFile, BOUNDARY))));
		} finally {
			if (IS_LOG) {
				logger.info("上传文件,执行时间'{}'毫秒,请求地址'{}',文件路径'{}'", System.currentTimeMillis() - start,
					requestUrl, uploadFile.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 上传，组装数据
	 * @param file
	 * @param boundary
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	private static byte[] packing(File file, String boundary) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		ByteArrayBuffer byteArrayBuilder = null;
		DataInputStream in = null;
		stringBuilder.append("--");// 必须多两道线
		stringBuilder.append(boundary);
		stringBuilder.append("\r\n");
		stringBuilder.append("Content-Disposition: form-data; name=\"media\"; filename=\""
								+ file.getName() + "\"\r\n");
		stringBuilder.append("Content-Type: image/jpeg\r\n\r\n");
		try {
			byteArrayBuilder = new ByteArrayBuffer((int) file.length());
			byteArrayBuilder.append(stringBuilder.toString().getBytes("utf-8"), 0,
				stringBuilder.toString().getBytes("utf-8").length);
			in = new DataInputStream(new FileInputStream(file));
			int length = 0;
			byte[] bufferOut = new byte[1024];
			while ((length = in.read(bufferOut)) != -1) {
				byteArrayBuilder.append(bufferOut, 0, length);
			}
			byteArrayBuilder.append(("\r\n--" + boundary + "--\r\n").getBytes("utf-8"), 0,
				("\r\n--" + boundary + "--\r\n").getBytes("utf-8").length);//定义最后数据分隔线 
			return byteArrayBuilder.toByteArray();
		} finally {
			if (byteArrayBuilder != null)
				byteArrayBuilder.clear();
			if (in != null)
				in.close();
		}
	}
	
}
