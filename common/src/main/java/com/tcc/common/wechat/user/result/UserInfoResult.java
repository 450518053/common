package com.tcc.common.wechat.user.result;

import com.tcc.common.wechat.base.result.BaseResult;
import com.tcc.common.wechat.user.model.UserInfo;

/**                    
 * @Filename UserInfoResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class UserInfoResult extends BaseResult {
	
	private UserInfo userInfo;
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
}
