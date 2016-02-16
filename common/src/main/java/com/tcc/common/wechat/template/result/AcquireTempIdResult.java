package com.tcc.common.wechat.template.result;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename AcquireTempIdResult.java
 *
 * @Description 获取模板id结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class AcquireTempIdResult extends BaseResult {
	
	private String templateId;//模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式 
	
	public String getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}
