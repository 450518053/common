package com.tcc.common.wechat.qrcode.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename AcquireTicketResult.java
 *
 * @Description 获取ticket结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class AcquireTicketResult extends BaseResult {
	
	private String	ticket;
					
	private int		expireSeconds;
					
	private String	url;
					
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public int getExpireSeconds() {
		return expireSeconds;
	}
	
	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}
