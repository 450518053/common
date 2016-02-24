package com.tcc.common.webcrawl.base.exception;

import com.tcc.common.webcrawl.base.enums.LoginFailedTypeEnum;

/**                    
 * @Filename LoginFailedException.java
 *
 * @Description 登录失败
 *
 * @author tcc 2015-12-11
 *
 */
public class LoginFailedException extends Exception {
	
	/**
	 * 登录失败原因
	 */
	private LoginFailedTypeEnum	failureCause		= LoginFailedTypeEnum.UNKNOWN;
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -190898015733514665L;
	
	public LoginFailedTypeEnum getFailureCause() {
		return failureCause;
	}
	
	public void setFailureCause(LoginFailedTypeEnum failureCause) {
		this.failureCause = failureCause;
	}
	
	/**
	 * 构建一个<code>LoginFailedException.java</code>
	 */
	public LoginFailedException() {
		super();
	}
	
	/**
	 * 构建一个<code>LoginFailedException.java</code>
	 * @param failureCause
	 */
	public LoginFailedException(LoginFailedTypeEnum failureCause) {
		super();
		this.failureCause = failureCause;
	}

	/**
	 * 构建一个<code>LoginFailedException.java</code>
	 * @param message
	 * @param cause
	 */
	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 构建一个<code>LoginFailedException.java</code>
	 * @param message
	 */
	public LoginFailedException(String message) {
		super(message);
	}
	
	/**
	 * 构建一个<code>LoginFailedException.java</code>
	 * @param cause
	 */
	public LoginFailedException(Throwable cause) {
		super(cause);
	}
	
}
