package com.tcc.common.wechat.material.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename NewsImgUploadResult.java
 *
 * @Description 上传图文消息内的图片结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class NewsImgUploadResult extends BaseResult {
	
	private String url;
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
}
