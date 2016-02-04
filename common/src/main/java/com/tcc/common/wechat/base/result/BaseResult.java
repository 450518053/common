package com.tcc.common.wechat.base.result;

import com.tcc.common.wechat.base.enums.ResultCodeEnum;

/**                    
 * @Filename BaseResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class BaseResult {
	
	/**
	 * 返回码
	 * 默认成功
	 */
	private ResultCodeEnum	resultCode	= ResultCodeEnum._0;
										
	/**
	 * 返回结果信息
	 */
	private String			resultMessage;
							
	/**
	 * 腾讯端直接返回的错误信息
	 */
	private String			errorMessage;
							
	/**
	 * 请求参数
	 */
	private String			args;
							
	public ResultCodeEnum getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(ResultCodeEnum resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getResultMessage() {
		return resultMessage;
	}
	
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getArgs() {
		return args;
	}
	
	public void setArgs(String args) {
		this.args = args;
	}
	
}