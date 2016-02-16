package com.tcc.common.wechat.user.result;

import java.util.List;

import com.tcc.common.wechat.base.result.BaseResult;
import com.tcc.common.wechat.user.model.UserInfo;

/**                    
 * @Filename UserInfosResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class UserInfosResult extends BaseResult {
	
	private List<UserInfo> userInfos;
	
	public List<UserInfo> getUserInfos() {
		return userInfos;
	}
	
	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}
	
}
