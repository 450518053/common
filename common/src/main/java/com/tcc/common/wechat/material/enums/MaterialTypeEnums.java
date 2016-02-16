package com.tcc.common.wechat.material.enums;

import java.util.ArrayList;
import java.util.List;

/**                    
 * @Filename MaterialTypeEnums.java
 *
 * @Description 素材类型
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public enum MaterialTypeEnums {

	IMAGE("image","图片"),
	VOICE("voice","语音"),
	VIDEO("video","视频"),
	THUMB("thumb","缩略图"),//主要用于视频与音乐格式的缩略图
	NEWS("news","图文");
	
	private final String	code;//实际值
	
	private final String	message;//描述
	
	MaterialTypeEnums(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return Returns the code.
	 */
	public String code() {
		return code;
	}
	
	/**
	 * @return Returns the message.
	 */
	public String message() {
		return message;
	}
	
	/**
	 * 通过枚举<code>code</code>获得枚举
	 *		若无则返回 MaterialTypeEnums.UNKNOWN
	 * @param code
	 * @return MaterialTypeEnums
	 */
	public static MaterialTypeEnums getByCode(String code) {
		for (MaterialTypeEnums _enum : values()) {
			if (_enum.code.equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<MaterialTypeEnums>
	 */
	public static List<MaterialTypeEnums> getAllEnum() {
		List<MaterialTypeEnums> list = new ArrayList<MaterialTypeEnums>();
		for (MaterialTypeEnums _enum : values()) {
			list.add(_enum);
		}
		return list;
	}
	
	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public static List<String> getAllEnumCode() {
		List<String> list = new ArrayList<String>();
		for (MaterialTypeEnums _enum : values()) {
			list.add(_enum.code);
		}
		return list;
	}
}
