package com.tcc.common.webcrawl.base.exception;

/**                    
 * @Filename AnalyValidateImgErrorException.java
 *
 * @Description 解析验证码图片失败异常
 *
 * @author tan 2015-12-11
 *
 */
public class AnalyValidateImgErrorException extends Exception {
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -6907984509366153642L;
	
	/**
	 * 构建一个<code>AnalyValidateImgErrorException.java</code>
	 */
	public AnalyValidateImgErrorException() {
		super();
	}
	
	/**
	 * 构建一个<code>AnalyValidateImgErrorException.java</code>
	 * @param message
	 * @param cause
	 */
	public AnalyValidateImgErrorException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 构建一个<code>AnalyValidateImgErrorException.java</code>
	 * @param message
	 */
	public AnalyValidateImgErrorException(String message) {
		super(message);
	}
	
	/**
	 * 构建一个<code>AnalyValidateImgErrorException.java</code>
	 * @param cause
	 */
	public AnalyValidateImgErrorException(Throwable cause) {
		super(cause);
	}
	
}
