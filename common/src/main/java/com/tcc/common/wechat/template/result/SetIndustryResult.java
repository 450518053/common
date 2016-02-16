package com.tcc.common.wechat.template.result;

import com.tcc.common.wechat.base.result.BaseResult;
import com.tcc.common.wechat.template.model.Industry;

/**                    
 * @Filename SetIndustryResult.java
 *
 * @Description 设置行业结果类
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class SetIndustryResult extends BaseResult {
	
	private Industry	primaryIndustry;	//帐号设置的主营行业
						
	private Industry	secondaryIndustry;	//帐号设置的副营行业
						
	public Industry getPrimaryIndustry() {
		return primaryIndustry;
	}
	
	public void setPrimaryIndustry(Industry primaryIndustry) {
		this.primaryIndustry = primaryIndustry;
	}
	
	public Industry getSecondaryIndustry() {
		return secondaryIndustry;
	}
	
	public void setSecondaryIndustry(Industry secondaryIndustry) {
		this.secondaryIndustry = secondaryIndustry;
	}
	
}
