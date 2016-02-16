package com.tcc.common.wechat.menu.model;

import java.util.List;

/**
 *  
 * @Filename ComplexButton.java
 *
 * @Description 复杂按钮（父按钮）
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class ComplexButton extends Button {
	
	private List<Button> sub_button;
	
	public List<Button> getSub_button() {
		return sub_button;
	}
	
	public void setSub_button(List<Button> sub_button) {
		this.sub_button = sub_button;
	}
}