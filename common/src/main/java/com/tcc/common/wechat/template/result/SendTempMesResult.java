package com.tcc.common.wechat.template.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename SendTempMesResult.java
 *
 * @Description 发送模板消息结果
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class SendTempMesResult extends BaseResult {
	
	private int msgid;
	
	public int getMsgid() {
		return msgid;
	}
	
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	
}
