package com.tcc.common.wechat.token.client;

import com.alibaba.fastjson.JSONObject;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.token.result.AccessTokenResult;
import com.tcc.common.wechat.token.result.JsApiTicketResult;

/**                    
 * @Filename AccessTokenClient.java
 *
 * @Description 获取凭证
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class AccessTokenClient extends WechatClient {
	
	/**
	 * 请求获取token地址，需替换APPID、APPSECRET
	 */
	private static final String	ACESS_TOKEN_URL	= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET"
		.replace("APPID", appId).replace("APPSECRET", appSecret);
		
	/**
	 * 获取js接口调用凭据，需替换ACCESS_TOKEN
	 */
	private static final String	JS_API_URL		= "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
												
	/**
	 * 获取accessToken凭证
	 * @return
	 * @throws Exception
	 */
	public AccessTokenResult getAccessToken() throws Exception {
		JSONObject jsonObject = WechatHttpClientUtils.get(ACESS_TOKEN_URL);
		AccessTokenResult result = new AccessTokenResult();
		if (analyErrcode(result, jsonObject)) {
			result.setAccessToken(jsonObject.getString("access_token"));
			result.setExpiresIn(jsonObject.getIntValue("expires_in"));
		}
		return result;
	}
	
	/**
	 * 获取jsApiTicket凭证
	 * @return
	 * @throws Exception
	 */
	public JsApiTicketResult jsApiTicket() throws Exception {
		JSONObject jsonObject = WechatHttpClientUtils
			.get(JS_API_URL.replace("ACCESS_TOKEN", token));
		JsApiTicketResult result = new JsApiTicketResult();
		if (analyErrcode(result, jsonObject)) {
			result.setTicket(jsonObject.getString("ticket"));
			result.setExpiresIn(jsonObject.getIntValue("expires_in"));
		}
		return result;
	}
}
