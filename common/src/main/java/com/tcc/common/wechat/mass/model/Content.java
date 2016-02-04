package com.tcc.common.wechat.mass.model;

/**                    
 * @Filename Content.java
 *
 * @Description 文本消息
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class Content {
	
	private String content;
	
	/**
	 * 构建一个<code>Content.java</code>
	 */
	public Content() {
		super();
	}
	
	/**
	 * 构建一个<code>Content.java</code>
	 * @param content
	 */
	public Content(String content) {
		super();
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
