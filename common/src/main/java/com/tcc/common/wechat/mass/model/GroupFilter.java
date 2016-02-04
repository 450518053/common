package com.tcc.common.wechat.mass.model;

/**                    
 * @Filename GroupFilter.java
 *
 * @Description 
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class GroupFilter {
	
	private boolean is_to_all;//用于设定是否向全部用户发送，值为true或false
	
	private String group_id;//群发到的分组的group_id
	
	/**
	 * 构建一个<code>GroupFilter.java</code>
	 */
	public GroupFilter() {
		super();
		this.is_to_all = true;
	}
	
	/**
	 * 构建一个<code>GroupFilter.java</code>
	 * @param group_id
	 */
	public GroupFilter(String group_id) {
		super();
		this.is_to_all = false;
		this.group_id = group_id;
	}
	
	public boolean isIs_to_all() {
		return is_to_all;
	}
	
	public void setIs_to_all(boolean is_to_all) {
		this.is_to_all = is_to_all;
	}
	
	public String getGroup_id() {
		return group_id;
	}
	
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	
}
