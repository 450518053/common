package com.tcc.common.wechat.menu.model;

/**                    
 * @Filename ClickButton.java
 *
 * @Description 
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class ClickButton extends Button {
	
	private String type;
	
	private String key;
	
	public ClickButton() {
		super();
		type = "click";
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
}
