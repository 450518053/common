package com.tcc.common.wechat.qrcode.client;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.qrcode.result.AcquireTicketResult;

/**                    
 * @Filename QrCodeClient.java
 *
 * @Description 二维码
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class QrCodeClient extends WechatClient {
	
	/**
	 * 创建永久二维码，需替换TOKEN
	 */
	private static final String	QRCODE_CREATE_URL	= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
													
	/**
	 * 创建永久二维码，需替换TOKEN
	 */
	private static final String	QRCODE_CREATE_JSON	= "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"{SCENE}\"}}}";
													
	/**
	 * 通过接口获取永久二维码ticket，该ticket可在有限时间内换取二维码
	 * @param scene
	 * @return
	 * @throws IOException
	 */
	public AcquireTicketResult acquireTicket(String scene) throws IOException {
		Args.notBlank(scene, "scene");
		JSONObject jsonObject = WechatHttpClientUtils.post(QRCODE_CREATE_URL,
			QRCODE_CREATE_JSON.replace("{SCENE}", scene));
		AcquireTicketResult result = new AcquireTicketResult();
		result.setArgs(scene);
		if (analyErrcode(result, jsonObject)) {
			result.setTicket(jsonObject.getString("ticket"));
			result.setExpireSeconds(jsonObject.getIntValue("expire_seconds"));
			result.setUrl(jsonObject.getString("url"));
		}
		return result;
	}
	
	/**
	 * 通过ticket下载二维码，存放至指定路径，不抛异常视为下载成功
	 * @param saveFile
	 * @param ticket
	 * @return
	 * @throws IOException 
	 */
	public void downQrCode(File saveFile, String ticket) throws IOException {
		WechatHttpClientUtils.download(saveFile, ticket);
	}
}
