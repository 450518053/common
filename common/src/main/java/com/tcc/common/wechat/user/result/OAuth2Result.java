package com.tcc.common.wechat.user.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename OAuth2Result.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class OAuth2Result extends BaseResult {
	
	private String	accessToken;	//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
					
	private int		expiresIn;		//access_token接口调用凭证超时时间，单位（秒）
					
	private String	refreshToken;	//用户刷新access_token
					
	private String	openId;			//用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
					
	private String	scope;			//用户授权的作用域，使用逗号（,）分隔
					
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getOpenId() {
		return openId;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
