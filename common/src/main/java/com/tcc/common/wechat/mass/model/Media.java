package com.tcc.common.wechat.mass.model;

/**                    
 * @Filename Media.java
 *
 * @Description 多媒体消息
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class Media {
	
	private String media_id;//media_id需通过基础支持中的上传下载多媒体文件来得到
	
	/**
	 * 构建一个<code>Media.java</code>
	 */
	public Media() {
		super();
	}
	
	/**
	 * 构建一个<code>Media.java</code>
	 * @param media_id
	 */
	public Media(String media_id) {
		super();
		this.media_id = media_id;
	}
	
	public String getMedia_id() {
		return media_id;
	}
	
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
}
