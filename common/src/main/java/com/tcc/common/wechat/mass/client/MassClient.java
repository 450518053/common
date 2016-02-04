package com.tcc.common.wechat.mass.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.mass.enums.MassTypeEnums;
import com.tcc.common.wechat.mass.model.Card;
import com.tcc.common.wechat.mass.model.Content;
import com.tcc.common.wechat.mass.model.GroupFilter;
import com.tcc.common.wechat.mass.model.Media;
import com.tcc.common.wechat.mass.result.MassResult;

/**                    
 * @Filename MassClient.java
 *
 * @Description 群发消息
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class MassClient extends WechatClient {
	
	/**
	 * 分组群发，需替换ACCESS_TOKEN
	 */
	private static final String	MASS_GROUP_URL			= "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
														
	/**
	 * 按用户列表分组群发，需替换ACCESS_TOKEN
	 */
	private static final String	MASS_OPENID_LIST_URL	= "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
														
	/**
	 * 删除群发，需替换ACCESS_TOKEN
	 */
	private static final String	MASS_DELETE_URL			= "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";
														
	/**
	 * 群发消息预览，需替换ACCESS_TOKEN
	 */
	private static final String	MESS_PREVIEW_URL		= "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";
														
	/**
	 * 按组群发
	 * @param type
	 * @param groupId 组id，若为空则向全部用户发送
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public MassResult sendByGroup(	MassTypeEnums type, String groupId,
									String content) throws Exception {
		Args.notEmpty(content, "content");
		GroupFilter filter = null;
		if (StringUtils.isBlank(groupId)) {
			filter = new GroupFilter();//向全部用户发送
		} else {
			filter = new GroupFilter(groupId);//向指定组发送
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("filter", filter);
		fillData(jsonMap, type, content);
		String json = JSON.toJSONString(jsonMap).toString();
		JSONObject jsonObject = WechatHttpClientUtils
			.post(MASS_GROUP_URL.replace("ACCESS_TOKEN", token), json);
		MassResult result = new MassResult();
		fillResult(result, json, jsonObject);
		return result;
	}
	
	/**
	 * 按用户列表分组群发
	 * @param type
	 * @param openIdList
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public MassResult sendByOpenId(	MassTypeEnums type, List<String> openIdList,
									String content) throws Exception {
		Args.notEmpty(content, "content");
		Args.notEmpty(openIdList, "openIdList");
		if (openIdList.size() < 2) {
			throw new IllegalArgumentException("用户列表长度不能小于2");
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("touser", openIdList);
		fillData(jsonMap, type, content);
		String json = JSON.toJSONString(jsonMap).toString();
		JSONObject jsonObject = WechatHttpClientUtils
			.post(MASS_OPENID_LIST_URL.replace("ACCESS_TOKEN", token), json);
		MassResult result = new MassResult();
		fillResult(result, json, jsonObject);
		return result;
	}
	
	/**
	 * 预览群发，可通过该方法发送消息给指定用户，在手机端查看消息的样式和排版，不抛异常视为发送成功
	 * @param type
	 * @param openId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public MassResult preview(MassTypeEnums type, String openId, String content) throws Exception {
		Args.notEmpty(content, "content");
		Args.notEmpty(openId, "openId");
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("touser", openId);
		fillData(jsonMap, type, content);
		String json = JSON.toJSONString(jsonMap).toString();
		JSONObject jsonObject = WechatHttpClientUtils
			.post(MESS_PREVIEW_URL.replace("ACCESS_TOKEN", token), json);
		MassResult result = new MassResult();
		result.setArgs(json);
		if (analyErrcode(result, jsonObject)) {
			result.setMsgId(jsonObject.getIntValue("msg_id"));
		}
		return result;
	}
	
	/**
	 * 删除群发，群发只有在刚发出的半小时内可以删除，不抛异常视为操作成功
	 * 		删除群发消息只能删除图文消息和视频消息
	 * @param msgId
	 * @throws Exception  
	 */
	public MassResult deleteMass(int msgId) throws Exception {
		String json = "{\"msg_id\":" + msgId + "}";
		JSONObject jsonObject = WechatHttpClientUtils
			.post(MASS_DELETE_URL.replace("ACCESS_TOKEN", token), json);
		MassResult result = new MassResult();
		result.setArgs(json);
		analyErrcode(result, jsonObject);
		return result;
	}
	
	/**
	 * 填充请求数据
	 * @param jsonMap
	 * @param type
	 * @param content
	 */
	private void fillData(Map<String, Object> jsonMap, MassTypeEnums type, String content) {
		switch (type) {//无video
			case NEWS://图文消息
				jsonMap.put(type.code(), new Media(content));
				break;
			case TEXT://文本
				jsonMap.put(type.code(), new Content(content));
				break;
			case VOICE://语音
				jsonMap.put(type.code(), new Media(content));
				break;
			case IMAGE://图片
				jsonMap.put(type.code(), new Media(content));
				break;
			case CARD://卡券
				jsonMap.put(type.code(), new Card(content));
				break;
			default:
				throw new IllegalArgumentException("无法识别的类型'" + type + "'");
		}
		jsonMap.put("msgtype", type);
	}
	
	/**
	 * 填充返回结果
	 * @param result
	 * @param json
	 * @param jsonObject
	 */
	private void fillResult(MassResult result, String json, JSONObject jsonObject) {
		result.setArgs(json);
		if (analyErrcode(result, jsonObject)) {
			result.setType(jsonObject.getString("type"));
			result.setMsgId(jsonObject.getIntValue("msg_id"));
			result.setMsgDataId(jsonObject.getIntValue("msg_data_id"));
		}
	}
	
}
