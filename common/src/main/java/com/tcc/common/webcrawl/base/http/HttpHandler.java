package com.tcc.common.webcrawl.base.http;

import java.awt.image.BufferedImage;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

/**                    
 * @Filename HttpHandler.java
 *
 * @Description http处理，调用HttpClientUtils前的封装
 *
 * @author tcc 2015-12-17
 *
 */
public interface HttpHandler {
	
	/**
	 * 下载图片
	 * @param requestUrl
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	BufferedImage downloadImg(String requestUrl, Header... headers) throws Exception;
	
	/**
	 * get
	 * @param requestUrl
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	String get(String requestUrl, Header... headers) throws Exception;
	
	/**
	 * post
	 * @param requestUrl
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	String post(String requestUrl, HttpEntity params, Header... headers) throws Exception;
}
