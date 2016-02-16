package com.tcc.common.wechat.material.client;

import java.io.File;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.util.Args;
import com.tcc.common.wechat.base.client.WechatClient;
import com.tcc.common.wechat.base.result.BaseResult;
import com.tcc.common.wechat.base.util.WechatHttpClientUtils;
import com.tcc.common.wechat.material.enums.MaterialTypeEnums;
import com.tcc.common.wechat.material.model.News;
import com.tcc.common.wechat.material.result.EternalMaterialUploadResult;
import com.tcc.common.wechat.material.result.MaterialCountResult;
import com.tcc.common.wechat.material.result.NewsImgUploadResult;
import com.tcc.common.wechat.material.result.TempMaterialUploadResult;

/**                    
 * @Filename MaterialClient.java
 *
 * @Description 素材管理
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public class MaterialClient extends WechatClient {
	
	/**
	 * 上传群发的临时图文消息，需替换ACCESS_TOKEN
	 */
	private static final String	UPLOAD_TEMP_NEWS_URL		= "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
															
	/**
	 * 上传永久图文消息，需替换ACCESS_TOKEN
	 */
	private static final String	UPLOAD_ETERNAL_NEWS_URL		= "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
															
	/**
	 * 群发图文消息内的图片获取URL，需替换ACCESS_TOKEN
	 */
	private static final String	UPLOAD_IMG_URL				= "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
															
	/**
	* 上传临时素材，需替换ACCESS_TOKEN、TYPE
	*/
	private static final String	UPLOAD_TEMP_MEDIA_URL		= "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
															
	/**
	* 上传永久素材，需替换ACCESS_TOKEN、TYPE
	*/
	private static final String	UPLOAD_ETERNAL_MEDIA_URL	= "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN";
															
	/**
	 * 获取永久素材总数，需替换ACCESS_TOKEN
	 */
	private static final String	All_MATERIAL_URL			= "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";
															
	/**
	 * 删除永久素材，需替换ACCESS_TOKEN
	 */
	private static final String	DELETE_MEDIA_URL			= "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";
															
	/**
	 * 上传图文消息内的图片获取URL，此为永久素材
	 * 		不占用公众号的素材库中图片数量的5000个的限制
	 * 			图片仅支持jpg/png格式，大小必须在1MB以下
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public NewsImgUploadResult uploadNewsImg(File file) throws Exception {
		//TODO 图片校验
		JSONObject jsonObject = WechatHttpClientUtils.uploadMedia(file,
			UPLOAD_IMG_URL.replace("ACCESS_TOKEN", token));
		NewsImgUploadResult result = new NewsImgUploadResult();
		result.setArgs(file.getName());
		if (analyErrcode(result, jsonObject)) {
			result.setUrl(jsonObject.getString("url"));
		}
		return result;
	}
	
	/**
	 * 上传临时多媒体素材获取media_id
	 * @param file
	 * @param type 除图文外
	 * @return
	 * @throws Exception
	 */
	public TempMaterialUploadResult uploadTempMaterial(	File file,
														MaterialTypeEnums type) throws Exception {
		//TODO 校验
		JSONObject jsonObject = WechatHttpClientUtils.uploadMedia(file,
			UPLOAD_TEMP_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("TYPE", type.code()));
		TempMaterialUploadResult result = new TempMaterialUploadResult();
		result.setArgs(file.getAbsolutePath() + ":" + type);
		if (analyErrcode(result, jsonObject)) {
			result.setType(jsonObject.getString("type"));
			result.setMediaId(jsonObject.getString("media_id"));
			result.setCreatedAt(jsonObject.getIntValue("created_at"));
		}
		return result;
	}
	
	/**
	* 上传群发图文消息，临时的
	* @param news
	* @return mediaId
	* @throws Exception 
	*/
	public TempMaterialUploadResult uploadTempMaterial(News news) throws Exception {
		Args.notNull(news, "news");
		if (CollectionUtils.isEmpty(news.getArticles()) || news.getArticles().size() > 8) {
			throw new IllegalArgumentException("图文消息支持1到8条图文 ");
		}
		String jsonStr = JSON.toJSONString(news);
		JSONObject jsonObject = WechatHttpClientUtils
			.post(UPLOAD_TEMP_NEWS_URL.replace("ACCESS_TOKEN", token), jsonStr);
		TempMaterialUploadResult result = new TempMaterialUploadResult();
		result.setArgs(jsonStr);
		if (analyErrcode(result, jsonObject)) {
			result.setType(jsonObject.getString("type"));
			result.setMediaId(jsonObject.getString("media_id"));
			result.setCreatedAt(jsonObject.getIntValue("created_at"));
		}
		return result;
	}
	
	/**
	 * 上传永久多媒体素材获取media_id
	 * @param file
	 * @param type 除图文外
	 * @return
	 * @throws Exception
	 */
	public EternalMaterialUploadResult uploadEternalMaterial(	File file,
																MaterialTypeEnums type) throws Exception {
		//TODO 校验
		JSONObject jsonObject = WechatHttpClientUtils.uploadMedia(file,
			UPLOAD_ETERNAL_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("TYPE", type.code()));
		EternalMaterialUploadResult result = new EternalMaterialUploadResult();
		result.setArgs(file.getAbsolutePath() + ":" + type);
		if (analyErrcode(result, jsonObject)) {
			result.setMediaId(jsonObject.getString("media_id"));
		}
		return result;
	}
	
	/**
	 * 上传永久图文消息
	 * @param news
	 * @return
	 * @throws Exception
	 */
	public EternalMaterialUploadResult uploadEternalMaterial(News news) throws Exception {
		Args.notNull(news, "news");
		if (CollectionUtils.isEmpty(news.getArticles()) || news.getArticles().size() > 8) {
			throw new IllegalArgumentException("图文消息支持1到8条图文 ");
		}
		String jsonStr = JSON.toJSONString(news);
		JSONObject jsonObject = WechatHttpClientUtils
			.post(UPLOAD_ETERNAL_NEWS_URL.replace("ACCESS_TOKEN", token), jsonStr);
		EternalMaterialUploadResult result = new EternalMaterialUploadResult();
		result.setArgs(jsonStr);
		if (analyErrcode(result, jsonObject)) {
			result.setMediaId(jsonObject.getString("media_id"));
		}
		return result;
	}
	
	/**
	 * 获取永久素材总数
	 * 		图片和图文消息素材（包括单图文和多图文）的总数上限为5000，其他素材的总数上限为1000
	 * @return
	 * @throws Exception 
	 */
	public MaterialCountResult getAllMaterial() throws Exception {
		JSONObject jsonObject = WechatHttpClientUtils
			.get(All_MATERIAL_URL.replace("ACCESS_TOKEN", token));
		MaterialCountResult result = new MaterialCountResult();
		if (analyErrcode(result, jsonObject)) {
			result.setVoiceCount(jsonObject.getIntValue("voice_count"));
			result.setVideoCount(jsonObject.getIntValue("video_count"));
			result.setImageCount(jsonObject.getIntValue("image_count"));
			result.setNewsCount(jsonObject.getIntValue("news_count"));
		}
		return result;
	};
	
	/**
	 * 删除指定的永久素材，不抛异常视为操作成功
	 * @param mediaId
	 * @return
	 * @throws Exception 
	 */
	public BaseResult deleteMedia(String mediaId) throws Exception {
		Args.notBlank(mediaId, "mediaId");
		JSONObject jsonObject = WechatHttpClientUtils.post(
			DELETE_MEDIA_URL.replace("ACCESS_TOKEN", token), "{\"media_id\":\"" + mediaId + "\"}");
		BaseResult result = new BaseResult();
		result.setArgs(mediaId);
		analyErrcode(result, jsonObject);
		return result;
	};
	
}
