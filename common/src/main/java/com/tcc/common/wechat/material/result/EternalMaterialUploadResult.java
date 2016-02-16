package com.tcc.common.wechat.material.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename EternalMaterialUploadResult.java
 *
 * @Description 永久素材上传结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class EternalMaterialUploadResult extends BaseResult {
	
	private String mediaId;
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
