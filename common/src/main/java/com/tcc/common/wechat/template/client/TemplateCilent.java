package com.tcc.common.wechat.template.client;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.template.model.Industry;
import com.tcc.common.wechat.template.model.TemplateMessage;
import com.tcc.common.wechat.template.result.AcquireTempIdResult;
import com.tcc.common.wechat.template.result.SendTempMesResult;
import com.tcc.common.wechat.template.result.SetIndustryResult;

/**                    
 * @Filename TemplateService.java
 *
 * @Description 模板消息
 *
 * @author tan 2015年10月29日
 *
 * @email 450518053@qq.com
 * 
 */
public class TemplateCilent extends WechatClient {
	
	/**
	 * 推送模板消息，需替换ACCESS_TOKEN
	 */
	private static final String	SEND_TEM_MESS_URL	= "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
													
	/**
	 * 获取模板消息id，需替换ACCESS_TOKEN
	 */
	private static final String	GET_TEMID_URL		= "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
													
	/**
	 * 设置行业，需替换ACCESS_TOKEN
	 */
	private static final String	SET_INDUSTRY_URL	= "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
													
	/**
	 * 获取模板消息id拼接json
	 */
	private static final String	GET_TEMID_JSON		= "{\"template_id_short\":\"{NUM}\"}";
													
	/**
	 * 设置行业拼接json
	 */
	private static final String	SET_INDUSTRY_JSON	= "{\"industry_id1\":\"{ID1}\",\"industry_id2\":\"{ID2}\"}";
													
	/**
	 * 发送模板消息
	 * @param templateMessage
	 * @return msgId
	 * @throws IOException
	 */
	public SendTempMesResult send(TemplateMessage templateMessage) throws IOException {
		Args.notNull(templateMessage, "templateMessage");
		String jsonStr = JSON.toJSONString(templateMessage);
		JSONObject jsonObject = WechatHttpClientUtils
			.post(SEND_TEM_MESS_URL.replace("ACCESS_TOKEN", token), jsonStr);
		SendTempMesResult result = new SendTempMesResult();
		result.setArgs(jsonStr);
		if (analyErrcode(result, jsonObject)) {
			result.setMsgid(jsonObject.getIntValue("msgid"));
		}
		return result;
	}
	
	/**
	 * 通过模板消息的编号获取模板消息的id
	 * @param num
	 * @return
	 * @throws IOException
	 */
	public AcquireTempIdResult acquireIdByNum(String num) throws IOException {
		Args.notBlank(num, "num");
		JSONObject jsonObject = WechatHttpClientUtils.post(
			GET_TEMID_URL.replace("ACCESS_TOKEN", token), GET_TEMID_JSON.replace("{NUM}", num));
		AcquireTempIdResult result = new AcquireTempIdResult();
		result.setArgs(num);
		if (analyErrcode(result, jsonObject)) {
			result.setTemplateId(jsonObject.getString("template_id"));
		}
		return result;
	}
	
	/**
	 * 设置所属行业，最多两个行业，不抛异常视为设置成功
	 * @param id1
	 * @param id2
	 * @throws IOException
	 */
	public SetIndustryResult setIndustry(String id1, String id2) throws IOException {
		Args.notBlank(id1, "id1");
		Args.notBlank(id2, "id2");
		JSONObject jsonObject = WechatHttpClientUtils.post(
			SET_INDUSTRY_URL.replace("ACCESS_TOKEN", token),
			SET_INDUSTRY_JSON.replace("{ID1}", id1).replace("{ID2}", id2));
		SetIndustryResult result = new SetIndustryResult();
		result.setArgs(id1 + ":" + id2);
		if (analyErrcode(result, jsonObject)) {
			result.setPrimaryIndustry(jsonObject.getObject("primary_industry", Industry.class));
			result.setSecondaryIndustry(jsonObject.getObject("secondary_industry", Industry.class));
		}
		return result;
	}
}
