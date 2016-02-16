package com.tcc.common.wechat.template.model;

import java.util.Map;

/**                    
 * @Filename TemplateMessage.java
 *
 * @Description 模板消息
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class TemplateMessage {
	
	private String touser; //openid
	
	private String template_id; //模板消息id
	
	private String url; //URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
	
	private String topcolor; //顶部颜色，默认为#173177
	
	private Map<String, TemplateData> data; //模板消息内容
	
	/**
	 * 构建一个<code>TemplateMessage.java</code>
	 */
	public TemplateMessage() {
		super();
	}
	
	public String getTouser() {
		return touser;
	}
	
	public void setTouser(String touser) {
		this.touser = touser;
	}
	
	public String getTemplate_id() {
		return template_id;
	}
	
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTopcolor() {
		return topcolor;
	}
	
	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}
	
	public Map<String, TemplateData> getData() {
		return data;
	}
	
	public void setData(Map<String, TemplateData> data) {
		this.data = data;
	}
}
