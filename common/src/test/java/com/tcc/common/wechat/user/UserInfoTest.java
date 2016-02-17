package com.tcc.common.wechat.user;

import com.tcc.common.wechat.base.BaseTest;
import com.tcc.common.wechat.user.client.UserInfoClient;
import com.tcc.common.wechat.user.result.OpenIdListResult;
import com.tcc.common.wechat.user.result.UserInfoResult;
import com.tcc.common.wechat.user.result.UserInfosResult;

/**                    
 * @Filename UserInfoTest.java
 *
 * @Description 
 *
 * @author tan 2016年2月17日
 *
 * @email 450518053@qq.com
 * 
 */
public class UserInfoTest {
	
	public static void main(String[] args) throws Exception {
		BaseTest.init();
		UserInfoClient client = new UserInfoClient();
		UserInfoResult result1 = client.getByOpenId("oaHCws3QWY8ITkBZlcHCyBVtyVWk");
		System.out.println(result1);
		
		OpenIdListResult result2 = client.getOpenIdList();
		System.out.println(result2);
		
		UserInfosResult result3 = client.getUserInfoList(result2.getOpenIdList());
		System.out.println(result3);
		
	}
}
