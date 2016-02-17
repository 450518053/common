package com.tcc.common.wechat.base;

import com.tcc.common.httpclient.HttpClientSupport;
import com.tcc.common.httpclient.HttpClientUtils;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.token.client.AccessTokenClient;
import com.tcc.common.wechat.token.result.AccessTokenResult;

/**                    
 * @Filename BaseTest.java
 *
 * @Description 
 *
 * @author tan 2016年2月17日
 *
 * @email 450518053@qq.com
 * 
 */
public class BaseTest {
	
	public static void init() throws Exception {
		HttpClientUtils.setClient(new HttpClientSupport(0, 0, 0, 0, 0, 3000).init());
		WechatClient.appId = "wx0ee15917eed9b5b6";
		WechatClient.appSecret = "730b2dd2bbb523aa8642045557cafe4e";
		AccessTokenClient client = new AccessTokenClient();
		AccessTokenResult result1 = client.getAccessToken();
		System.out.println(result1);
		WechatClient.token = result1.getAccessToken();
	}
	
}
