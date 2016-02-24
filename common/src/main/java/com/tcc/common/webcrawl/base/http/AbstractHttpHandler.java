package com.tcc.common.webcrawl.base.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.HttpContext;

/**                    
 * @Filename AbstractHttpHandler.java
 *
 * @Description 重点封装header消息头
 *
 * @author tcc 2015-12-17
 *
 */
public abstract class AbstractHttpHandler implements HttpHandler {
	
	/**
	 * http上下文
	 */
	protected HttpContext	context;
	
	/**
	 * 默认消息头
	 */
	protected List<Header>	defaultHeaders;
	
	/**
	 * 构建一个<code>AbstractHttpHandler.java</code>
	 * @param context
	 * @param headers
	 */
	public AbstractHttpHandler(HttpContext context, Header... headers) {
		super();
		this.context = context;
		this.defaultHeaders = new ArrayList<Header>(Arrays.asList(headers));
		this.context.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());//TODO cookieStroe应遵循某种策略从缓存获取
	}
	
	/**
	 * 构建一个<code>AbstractHttpHandler.java</code>
	 * @param context
	 * @param defaultHeaders
	 */
	public AbstractHttpHandler(HttpContext context, List<Header> defaultHeaders) {
		super();
		this.context = context;
		this.defaultHeaders = defaultHeaders;
		this.context.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());//TODO cookieStroe应遵循某种策略从缓存获取
	}
	
	/**
	 * 拼装消息头
	 * 		默认消息头+入参消息头
	 * @param headers
	 * @return
	 */
	protected List<Header> getHeaders(Header... headers) {
		List<Header> headerList = new ArrayList<Header>(defaultHeaders);
		for (Header header : headers) {
			headerList.add(header);
		}
		return headerList;
	}
	
	public HttpHandler setContext(HttpContext context) {
		this.context = context;
		return this;
	}
	
	public HttpHandler setDefaultHeaders(List<Header> defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
		return this;
	}
	
}
