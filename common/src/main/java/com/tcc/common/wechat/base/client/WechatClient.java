package com.tcc.common.wechat.base.client;

import com.alibaba.fastjson.JSONObject;
import com.tcc.common.wechat.base.enums.ResultCodeEnum;
import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename BaseClient.java
 *
 * @Description 基本类
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public abstract class WechatClient {
	
	public static String			appId;
									
	public static String			appSecret;
									
	/**
	 * 接口调用凭据，需在项目启动时初始化
	 */
	public volatile static String	token;
									
	/**
	 * js接口调用凭据，需在项目启动时初始化
	 */
	public volatile static String	ticket;
									
	/**
	 * 解析errcode
	 * @param result
	 * @param jsonObject
	 * @return 
	 */
	protected boolean analyErrcode(BaseResult result, JSONObject jsonObject) {
		result.setRetVal(jsonObject.toJSONString());
		int errcode = jsonObject.getIntValue("errcode");
		if (errcode == 0) {
			return true;
		}
		ResultCodeEnum resultCode = ResultCodeEnum.getByCode(errcode);
		result.setErrorCode(errcode);
		result.setResultCode(resultCode);
		result.setErrorMessage(jsonObject.getString("errmsg"));
		return false;
		//		String errorMessage = message+ ",errcode:" + errcode + ",errmsg:"
		//								+ jsonObject.getString("errmsg") + ","
		//								+ ResultCodeEnum.getByCode(errcode).message();
		//		if (errcode == 40001 || errcode == 40014 || errcode == 42001) {
		//			throw new TokenInvalidException(errorMessage);
		//		} else {
		//			throw new UnTokenInvalidException(errorMessage);
		//		}
	}
	
}
