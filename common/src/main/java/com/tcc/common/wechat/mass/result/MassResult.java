package com.tcc.common.wechat.mass.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename MassResult.java
 *
 * @Description 群发消息返回类
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class MassResult extends BaseResult {
	
	private String	type;		//媒体文件类型
					
	private int		msgId;
					
	private int		msgDataId;	//消息发送任务的ID，消息的数据ID，，该字段只有在群发图文消息时，才会出现
					
	/**
	 * 构建一个<code>MassResult.java</code>
	 */
	public MassResult() {
		super();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getMsgId() {
		return msgId;
	}
	
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	
	public int getMsgDataId() {
		return msgDataId;
	}
	
	public void setMsgDataId(int msgDataId) {
		this.msgDataId = msgDataId;
	}
	
}
