package com.tcc.common.wechat.material.model;

/**                    
 * @Filename MaterialCount.java
 *
 * @Description 素材统计
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class MaterialCount {
	
	private int	voiceCount;	//语音总数量
	
	private int	videoCount;	//视频总数量
	
	private int	imageCount;	//图片总数量
	
	private int	newsCount;	//图文总数量
	
	/**
	 * 构建一个<code>MaterialCount.java</code>
	 */
	public MaterialCount() {
		super();
	}
	
	/**
	 * 构建一个<code>MaterialCount.java</code>
	 * @param voiceCount
	 * @param videoCount
	 * @param imageCount
	 * @param newsCount
	 */
	public MaterialCount(int voiceCount, int videoCount, int imageCount, int newsCount) {
		super();
		this.voiceCount = voiceCount;
		this.videoCount = videoCount;
		this.imageCount = imageCount;
		this.newsCount = newsCount;
	}
	
	public int getVoiceCount() {
		return voiceCount;
	}
	
	public void setVoiceCount(int voiceCount) {
		this.voiceCount = voiceCount;
	}
	
	public int getVideoCount() {
		return videoCount;
	}
	
	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}
	
	public int getImageCount() {
		return imageCount;
	}
	
	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
	
	public int getNewsCount() {
		return newsCount;
	}
	
	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}
	
}
