package com.tcc.common.wechat.token;

import com.tcc.common.wechat.base.BaseTest;
import com.tcc.common.wechat.token.client.AccessTokenClient;
import com.tcc.common.wechat.token.result.JsApiTicketResult;

/**                    
 * @Filename TokenTest.java
 *
 * @Description 
 *
 * @author tan 2016年2月17日
 *
 * @email 450518053@qq.com
 * 
 */
public class TokenTest {
	
	public static void main(String[] args) throws Exception {
		BaseTest.init();
		AccessTokenClient client = new AccessTokenClient();
		JsApiTicketResult result = client.jsApiTicket();
		System.out.println(result);
	}
}
