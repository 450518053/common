package com.tcc.common.wechat.token.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename JsApiTicketResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class JsApiTicketResult extends BaseResult {
	
	private String	ticket;
					
	private int		expiresIn;
					
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
}
