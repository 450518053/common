package com.tcc.common.wechat.base.exception;

import com.tcc.common.wechat.base.enums.ResultCodeEnum;

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
	
	/**
	 * 返回码
	 * 默认未知
	 */
	private ResultCodeEnum		resultCode			= ResultCodeEnum.UNKNOWN;
													
	/**
	 * 腾讯端直接返回的错误信息
	 */
	private String				errorMessage;
								
	/**
	 * 错误码
	 */
	private int					errorCode;
								
	/**
	 * 自定义错误描述
	 */
	private String				description;
								
	/** Comment for <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 8561092224847631765L;
													
	public WechatException(int errorCode, String errmsg, String description) {
		this.errorCode = errorCode;
		this.errorMessage = errmsg;
		this.description = description;
		this.resultCode = ResultCodeEnum.getByCode(errorCode);
	}
	
	public ResultCodeEnum getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(ResultCodeEnum resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
