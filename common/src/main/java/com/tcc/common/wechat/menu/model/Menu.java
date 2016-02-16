package com.tcc.common.wechat.menu.model;

import java.util.List;

/**
 *  
 * @Filename Menu.java
 *
 * @Description 菜单
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class Menu {
	
	private List<Button> button;
	
	public List<Button> getButton() {
		return button;
	}
	
	public void setButton(List<Button> button) {
		this.button = button;
	}
}