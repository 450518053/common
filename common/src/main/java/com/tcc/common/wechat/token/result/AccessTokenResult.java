package com.tcc.common.wechat.token.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename AccessTokenResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class AccessTokenResult extends BaseResult {
	
	private String	accessToken;
					
	private int		expiresIn;
					
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
	
}
