package com.tcc.common.wechat.mass.enums;

import java.util.ArrayList;
import java.util.List;

/**                    
 * @Filename MassTypeEnums.java
 *
 * @Description 群发消息类型
 *
 * @author tan 2015年10月30日
 *
 * @email 450518053@qq.com
 * 
 */
public enum MassTypeEnums {

	NEWS("mpnews","图文消息"),
	TEXT("text","文本"),
	VOICE("voice","语音"),
	IMAGE("image","图片"),
	CARD("wxcard","卡券");
	
	private final String	code;//实际值
	
	private final String	message;//描述
	
	MassTypeEnums(String code, String message) {
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
	 *		若无则返回 MassTypeEnums.UNKNOWN
	 * @param code
	 * @return MassTypeEnums
	 */
	public static MassTypeEnums getByCode(String code) {
		for (MassTypeEnums _enum : values()) {
			if (_enum.code.equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<MassTypeEnums>
	 */
	public static List<MassTypeEnums> getAllEnum() {
		List<MassTypeEnums> list = new ArrayList<MassTypeEnums>();
		for (MassTypeEnums _enum : values()) {
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
		for (MassTypeEnums _enum : values()) {
			list.add(_enum.code);
		}
		return list;
	}
}
