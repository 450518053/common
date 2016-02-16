package com.tcc.common.wechat.menu.model;

/**                    
 * @Filename LinkButton.java
 *
 * @Description 跳转URL按钮
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class LinkButton extends Button {
	
	private String type;
	
	private String url;
	
	public LinkButton() {
		super();
		type = "view";
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}
