package com.tcc.common.wechat.material.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename TempMaterialUploadResult.java
 *
 * @Description 临时素材上传结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class TempMaterialUploadResult extends BaseResult {
	
	private String	type;
					
	private String	mediaId;
					
	private int		createdAt;
					
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMediaId() {
		return mediaId;
	}
	
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
	public int getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(int createdAt) {
		this.createdAt = createdAt;
	}
	
}
