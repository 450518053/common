package com.tcc.common.wechat.menu.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.result.BaseResult;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.menu.model.Menu;

/**                    
 * @Filename MenuClient.java
 *
 * @Description 菜单
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class MenuClient extends WechatClient {
	
	/**
	 * 创建菜单url，需替换ACCESS_TOKEN
	 */
	private static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	/**
	 * 创建菜单
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public BaseResult createMenu(Menu menu) throws Exception {
		Args.notNull(menu, "menu");
		String jsonStr = JSON.toJSONString(menu);
		JSONObject jsonObject = WechatHttpClientUtils
			.post(MENU_CREATE_URL.replace("ACCESS_TOKEN", token), jsonStr);
		BaseResult result = new BaseResult();
		result.setArgs(jsonStr);
		analyErrcode(result, jsonObject);
		return result;
	}
	
}
