package com.tcc.common.wechat.material.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename MaterialCountResult.java
 *
 * @Description 素材统计
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class MaterialCountResult extends BaseResult {
	
	private int	voiceCount;	//语音总数量
				
	private int	videoCount;	//视频总数量
				
	private int	imageCount;	//图片总数量
				
	private int	newsCount;	//图文总数量
				
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
