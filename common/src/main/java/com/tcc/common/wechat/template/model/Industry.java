package com.tcc.common.wechat.template.model;

/**                    
 * @Filename Industry.java
 *
 * @Description 行业
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public class Industry {
	
	private String	firstClass;//主行业
					
	private String	secondClass;//副行业
					
	public String getFirstClass() {
		return firstClass;
	}
	
	public void setFirstClass(String firstClass) {
		this.firstClass = firstClass;
	}
	
	public String getSecondClass() {
		return secondClass;
	}
	
	public void setSecondClass(String secondClass) {
		this.secondClass = secondClass;
	}
	
}
