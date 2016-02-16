package com.tcc.common.wechat.user.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.util.StringUtils;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.exception.WechatException;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.user.model.UserInfo;
import com.tcc.common.wechat.user.result.OpenIdListResult;
import com.tcc.common.wechat.user.result.UserInfoResult;
import com.tcc.common.wechat.user.result.UserInfosResult;

/**                    
 * @Filename UserInfoQueryClient.java
 *
 * @Description 用户信息
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class UserInfoClient extends WechatClient {
	
	/**
	 * 获取用户信息url，需替换ACCESS_TOKEN，OPENID
	 * 国家地区语言版本，zh_CN 简体
	 */
	private static final String	GET_USERINFO_URL			= "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
															
	/**
	 * 获取openId列表，需替换ACCESS_TOKEN
	 */
	private static final String	GET_OPENIDLIST_URL			= "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
															
	/**
	 * 关注者数量超过10000时，获取openId列表，需替换ACCESS_TOKEN，NEXT_OPENID
	 */
	private static final String	GET_OPENIDLIST_OTHER_URL	= "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
															
	/**
	 * 获取用户信息列表，需替换ACCESS_TOKEN
	 */
	private static final String	GET_USERINFOLIST_URL		= "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token=ACCESS_TOKEN";
															
	/**
	 * 从腾讯端获取客户信息
	 * @param openId
	 * @return
	 * @throws Exception
	 */
	public UserInfoResult getByOpenId(String openId) throws Exception {
		Args.notEmpty(openId, "openId不能为null");
		JSONObject jsonObject = WechatHttpClientUtils
			.get(GET_USERINFO_URL.replace("ACCESS_TOKEN", token).replace("OPENID", openId));
		UserInfoResult result = new UserInfoResult();
		result.setArgs(openId);
		if (analyErrcode(result, jsonObject)) {
			//			UserInfo userInfo = new UserInfo();
			//			userInfo.setOpenId(jsonObject.getString("openid"));
			//			userInfo.setUnionId(jsonObject.getString("unionid"));
			//			int subscribe = jsonObject.getIntValue("subscribe");
			//			userInfo.setSubscribe(jsonObject.getIntValue("subscribe"));
			//			if (subscribe == 1) {//关注用户
			//				userInfo.setNickname(jsonObject.getString("nickname"));
			//				userInfo.setSex(jsonObject.getIntValue("sex"));
			//				userInfo.setLanguage(jsonObject.getString("language"));
			//				userInfo.setCity(jsonObject.getString("city"));
			//				userInfo.setProvince(jsonObject.getString("province"));
			//				userInfo.setCountry(jsonObject.getString("country"));
			//				userInfo.setHeadimgUrl(jsonObject.getString("headimgurl"));
			//				userInfo.setSubscribeTime(jsonObject.getIntValue("subscribe_time"));
			//				userInfo.setRemark(jsonObject.getString("remark"));
			//			}
			//			result.setUserInfo(userInfo);
			result.setUserInfo(convert(jsonObject));
		}
		return result;
	}
	
	/**
	 * 获取关注用户openId列表
	 * 		一次拉取调用最多拉取10000个关注者的openId，可以通过多次拉取的方式来满足需求
	 * @param nextOpenid 拉取列表的最后一个用户的openId，若为null则为首次获取
	 * @return
	 * @throws Exception
	 */
	public OpenIdListResult getOpenIdList(String nextOpenid) throws Exception {
		JSONObject jsonObject = null;
		if (StringUtils.isBlank(nextOpenid)) {
			//（首次）获取
			jsonObject = WechatHttpClientUtils
				.get(GET_OPENIDLIST_URL.replace("ACCESS_TOKEN", token));
		} else {
			//非首次获取
			jsonObject = WechatHttpClientUtils.get(GET_OPENIDLIST_OTHER_URL
				.replace("ACCESS_TOKEN", token).replace("NEXT_OPENID", nextOpenid));
		}
		OpenIdListResult result = new OpenIdListResult();
		result.setArgs(nextOpenid);
		if (analyErrcode(result, jsonObject)) {
			List<String> data = convertJSONArray(
				jsonObject.getJSONObject("data").getJSONArray("openid"));
			result.setOpenIdList(data);
			result.setTotal(jsonObject.getIntValue("total"));
			result.setCount(jsonObject.getIntValue("count"));
			result.setNextOpenId(jsonObject.getString("next_openid"));
		}
		return result;
		//		int count = jsonObject.getIntValue("total") - jsonObject.getIntValue("count");//关注者数量超过10000时
		//		int index = 1;
		//		while (count > 0) {
		//			index++;
		//			jsonObject = getOpenIdList(jsonObject.getString("next_openid"), index);
		//			data.addAll(JSONArray.toList(jsonObject.getJSONObject("data").getJSONArray("openid")));
		//			count -= jsonObject.getInt("count");
		//		}
		//		return new HashSet<String>(data);
	}
	
	/**
	 * 获取用户信息列表
	 * @param openIdList
	 * @return
	 * @throws Exception
	 */
	public UserInfosResult getUserInfoList(List<String> openIdList) throws Exception {
		Args.notEmpty(openIdList, "openIdList");
		UserInfosResult result = new UserInfosResult();
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		int j = 0;
		try {
			for (; i < openIdList.size(); i++) {
				j++;
				stringBuilder.append("{\"openid\":\"" + openIdList.get(i) + "\"},");
				if (j == 100) {//一次最多拉取100条
					stringBuilder.deleteCharAt(stringBuilder.length() - 1);
					analy(getUserInfoList(stringBuilder.toString(), i, i + j), userInfoList);
					j = 0;
					stringBuilder.setLength(0);
				}
			}
			if (j > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				analy(getUserInfoList(stringBuilder.toString(), i - j, i), userInfoList);
			}
			result.setUserInfos(userInfoList);
			result.setArgs(openIdList.toString());
		} catch (WechatException e) {
			result.setErrorCode(e.getErrorCode());
			result.setErrorMessage(e.getErrorMessage());
			result.setResultCode(e.getResultCode());
			result.setArgs(e.getDescription());
		}
		return result;
	}
	
	/**
	 * 拼装数据
	 * @param openIdListStr
	 * @param i
	 * @param j
	 * @return
	 * @throws Exception
	 */
	private JSONObject getUserInfoList(String openIdListStr, int i, int j) throws Exception {
		String outputStr = "{\"user_list\":[{OPENIDLIST}]}".replace("{OPENIDLIST}", openIdListStr);//一次最多拉取100条
		JSONObject jsonObject = WechatHttpClientUtils
			.post(GET_USERINFOLIST_URL.replace("ACCESS_TOKEN", token), outputStr);
		if (jsonObject.containsKey("errcode")) {
			throw new WechatException(jsonObject.getIntValue("errcode"),
				jsonObject.getString("errmsg"),
				"获取" + i + "到" + j + "用户信息列表'" + openIdListStr + "'失败");
		}
		return jsonObject;
	}
	
	/**
	 * 解析返回的json字符串
	 * @param jsonObject
	 * @param userInfoList
	 */
	private void analy(JSONObject jsonObject, List<UserInfo> userInfoList) {
		JSONArray jsonArray = jsonObject.getJSONArray("user_info_list");
		for (int i = 0; i < jsonArray.size(); i++) {
			userInfoList.add(convert(jsonArray.getJSONObject(i)));
		}
	}
	
	/**
	 * JSONArray转换为list
	 * @param jsonArray
	 * @return
	 */
	private List<String> convertJSONArray(JSONArray jsonArray) {
		List<String> dataList = new ArrayList<String>();
		Iterator<Object> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			Object data = iterator.next();
			dataList.add(data.toString());
		}
		return dataList;
	}
	
	/**
	 * JSONObject转换为UserInfo
	 * @param jsonObject
	 * @return
	 */
	private UserInfo convert(JSONObject jsonObject) {
		UserInfo userInfo = new UserInfo();
		userInfo.setOpenId(jsonObject.getString("openid"));
		userInfo.setUnionId(jsonObject.getString("unionid"));
		int subscribe = jsonObject.getIntValue("subscribe");
		userInfo.setSubscribe(jsonObject.getIntValue("subscribe"));
		if (subscribe == 1) {//关注用户
			userInfo.setNickname(jsonObject.getString("nickname"));
			userInfo.setSex(jsonObject.getIntValue("sex"));
			userInfo.setLanguage(jsonObject.getString("language"));
			userInfo.setCity(jsonObject.getString("city"));
			userInfo.setProvince(jsonObject.getString("province"));
			userInfo.setCountry(jsonObject.getString("country"));
			userInfo.setHeadimgUrl(jsonObject.getString("headimgurl"));
			userInfo.setSubscribeTime(jsonObject.getIntValue("subscribe_time"));
			userInfo.setRemark(jsonObject.getString("remark"));
		}
		return userInfo;
	}
	
}
