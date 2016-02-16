package com.tcc.common.wechat.template.model;

/**                    
 * @Filename TemplateData.java
 *
 * @Description 模板消息内容
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class TemplateData {
	
	private String value;
	
	private String color;
	
	/**
	 * 构建一个<code>TemplateData.java</code>
	 */
	public TemplateData() {
		super();
		this.value = "";
		this.color = "#173177";
	}
	
	/**
	 * 构建一个<code>TemplateData.java</code>
	 * @param value
	 * @param color
	 */
	public TemplateData(String value) {
		super();
		if (value == null) {
			value = "";
		}
		this.value = value;
		this.color = "#173177";
	}
	
	/**
	 * 构建一个<code>TemplateData.java</code>
	 * @param value
	 * @param color
	 */
	public TemplateData(String value, String color) {
		super();
		this.value = value;
		this.color = color;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
}
