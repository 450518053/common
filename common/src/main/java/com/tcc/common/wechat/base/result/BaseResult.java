package com.tcc.common.wechat.base.result;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.tcc.common.wechat.base.enums.ResultCodeEnum;

/**                    
 * @Filename BaseResult.java
 *
 * @Description 基本返回结果类
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
	 * 错误码
	 */
	private int				errorCode;
							
	/**
	 * 腾讯端直接返回的错误信息
	 */
	private String			errorMessage;
							
	/**
	 * 请求参数
	 */
	private String			args;
							
	/**
	 * 腾讯端返回值
	 */
	private String			retVal;
							
	public String getRetVal() {
		return retVal;
	}
	
	public void setRetVal(String retVal) {
		this.retVal = retVal;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
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
	
	public String getArgs() {
		return args;
	}
	
	public void setArgs(String args) {
		this.args = args;
	}
	
	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
