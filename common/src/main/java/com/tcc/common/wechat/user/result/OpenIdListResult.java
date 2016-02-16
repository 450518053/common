package com.tcc.common.wechat.user.result;

import java.util.List;

import com.tcc.common.wechat.base.result.BaseResult;

/**                    
 * @Filename OpenIdListResult.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class OpenIdListResult extends BaseResult {
	
	private List<String>	openIdList;	//列表数据，OPENID的列表 
							
	private int				total;		//关注该公众账号的总用户数 
							
	private int				count;		//拉取的OPENID个数，最大值为10000 
							
	private String			nextOpenId;	//拉取列表的最后一个用户的OPENID
							
	public List<String> getOpenIdList() {
		return openIdList;
	}
	
	public void setOpenIdList(List<String> openIdList) {
		this.openIdList = openIdList;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getNextOpenId() {
		return nextOpenId;
	}
	
	public void setNextOpenId(String nextOpenId) {
		this.nextOpenId = nextOpenId;
	}
	
}
