package com.tcc.common.wechat.base.exception;

/**                    
 * @Filename WechatException.java
 *
 * @Description 腾讯端异常
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class WechatException extends Exception {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 8561092224847631765L;
	
	/**
	 * 构建一个<code>WechatException.java</code>
	 */
	public WechatException() {
		super();
	}
	
	/**
	 * 构建一个<code>WechatException.java</code>
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WechatException(	String message, Throwable cause, boolean enableSuppression,
							boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	/**
	 * 构建一个<code>WechatException.java</code>
	 * @param message
	 * @param cause
	 */
	public WechatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 构建一个<code>WechatException.java</code>
	 * @param message
	 */
	public WechatException(String message) {
		super(message);
	}
	
	/**
	 * 构建一个<code>WechatException.java</code>
	 * @param cause
	 */
	public WechatException(Throwable cause) {
		super(cause);
	}
	
}
