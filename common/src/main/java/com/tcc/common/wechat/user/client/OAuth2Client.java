package com.tcc.common.wechat.user.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.util.StringUtils;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.user.enums.ScopeTypeEnums;
import com.tcc.common.wechat.user.result.OAuth2Result;

/**                    
 * @Filename OAuth2Client.java
 *
 * @Description OAuth2.0 网页授权获取用户基本信息
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class OAuth2Client extends WechatClient {
	
	/**
	 * 通过网页授权后获取的code获取openId，需替换APPID、SECRET、CODE
	 */
	private static final String	GET_OPENID_BY_CODE_URL	= "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
		.replace("APPID", appId).replace("APPSECRET", appSecret);
		
	/**
	 * 网页授权OAuth2，菜单连接需要包装，需替换APPID、REDIRECT_URI、SCOPE、STATE
	 */
	private static final String	OAUTH2_URL				= "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"
		.replace("APPID", appId);
		
	/**
	 * 通过code获取客户信息(openId)
	 * @param code
	 * @return
	 * @throws IOException
	 */
	public OAuth2Result getByCode(String code) throws IOException {
		Args.notBlank(code, "code");
		JSONObject jsonObject = WechatHttpClientUtils
			.get(GET_OPENID_BY_CODE_URL.replace("CODE", code));
		OAuth2Result result = new OAuth2Result();
		result.setArgs(code);
		if (analyErrcode(result, jsonObject)) {
			result.setAccessToken(jsonObject.getString("access_token"));
			result.setExpiresIn(jsonObject.getIntValue("expires_in"));
			result.setRefreshToken(jsonObject.getString("refresh_token"));
			result.setOpenId(jsonObject.getString("openid"));
			result.setScope(jsonObject.getString("scope"));
		}
		return result;
	}
	
	/**
	 * 网页授权url
	 * @param scopeTypeEnums
	 * @param redirectURI 授权后重定向的回调链接地址，包括http://或者https://
	 * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节 
	 * @return
	 */
	public String transURL(ScopeTypeEnums scopeTypeEnums, String redirectURI, String state) {
		Args.notBlank(redirectURI, "redirectURI");
		String newURI = null;
		try {
			newURI = URLEncoder.encode(redirectURI, "utf-8");//urlencode对链接进行处理
		} catch (UnsupportedEncodingException e) {
		}
		newURI = OAUTH2_URL.replace("REDIRECT_URI", newURI).replace("SCOPE", scopeTypeEnums.code());
		if (StringUtils.isNotBlank(state)) {
			newURI = newURI.replace("STATE", state);
		}
		return newURI;
	}
	
	/**
	 * 网页授权url
	 * @param scopeTypeEnums
	 * @param redirectURI 授权后重定向的回调链接地址，包括http://或者https://
	 * @return
	 */
	public String transURL(ScopeTypeEnums scopeTypeEnums, String redirectURI) {
		return transURL(scopeTypeEnums, redirectURI, null);
	}
}
