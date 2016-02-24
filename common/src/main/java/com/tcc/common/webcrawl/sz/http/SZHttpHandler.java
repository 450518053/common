package com.tcc.common.webcrawl.sz.http;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import com.tcc.common.webcrawl.base.http.AbstractHttpHandler;
import com.tcc.common.webcrawl.base.util.HttpClientUtils;

/**                    
 * @Filename SZHttpHandler.java
 *
 * @Description http处理
 *
 * @author tcc 2015-12-17
 *
 */
public class SZHttpHandler extends AbstractHttpHandler {
	
	/**
	 * 构建一个<code>SZHttpHandler.java</code>
	 * @param context
	 * @param defaultHeaders
	 */
	public SZHttpHandler(HttpContext context, Header... headers) {
		super(context, headers);
	}
	
	/**
	 * 构建一个<code>SZHttpHandler.java</code>
	 * @param context
	 * @param defaultHeaders
	 */
	public SZHttpHandler(HttpContext context, List<Header> defaultHeaders) {
		super(context, defaultHeaders);
	}
	
	/**
	 * 下载图片
	 * @param requestUrl
	 * @param headers
	 * @return
	 * @throws Exception
	 * @see com.uaf.grabweb.base.http.HttpHandler#downloadImg(java.lang.String, org.apache.http.Header[])
	 */
	public BufferedImage downloadImg(String requestUrl, Header... headers) throws Exception {
		List<Header> headerList = getHeaders(headers);
		headerList.add(new BasicHeader("Accept", "image/webp,*/*;q=0.8"));
		return HttpClientUtils.downloadImg(requestUrl, headerList, context);
	}
	
	/**
	 * get
	 * @param requestUrl
	 * @param headers
	 * @return
	 * @throws Exception
	 * @see com.uaf.grabweb.base.http.HttpHandler#get(java.lang.String, org.apache.http.Header[])
	 */
	public String get(String requestUrl, Header... headers) throws Exception {
		List<Header> headerList = getHeaders(headers);
		headerList.add(new BasicHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		return HttpClientUtils.get(requestUrl, headerList, context);
	}
	
	/**
	 * post
	 * @param requestUrl
	 * @param params
	 * @param headers
	 * @return
	 * @throws Exception
	 * @see com.uaf.grabweb.base.http.HttpHandler#post(java.lang.String, org.apache.http.HttpEntity, org.apache.http.Header[])
	 */
	public String post(String requestUrl, HttpEntity params, Header... headers) throws Exception {
		List<Header> headerList = getHeaders(headers);
		headerList.add(new BasicHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
		return HttpClientUtils.post(requestUrl, params, headerList, context);
	}
	
}
