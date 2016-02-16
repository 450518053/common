package com.tcc.common.wechat.user.enums;

import java.util.ArrayList;
import java.util.List;

/**                    
 * @Filename ScopeTypeEnums.java
 *
 * @Description 应用授权作用域类型
 *
 * @author tan 2016年2月16日
 *
 * @email 450518053@qq.com
 * 
 */
public enum ScopeTypeEnums {

	SNSAPI_BASE("snsapi_base"),//不弹出授权页面，直接跳转，只能获取用户openid
	SNSAPI_USERINFO("snsapi_userinfo");//弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息
	
	private final String	code;//实际值
	
	ScopeTypeEnums(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the code.
	 */
	public String code() {
		return code;
	}
	
	/**
	 * 通过枚举<code>code</code>获得枚举
	 *		若无则返回 ScopeTypeEnums.UNKNOWN
	 * @param code
	 * @return ScopeTypeEnums
	 */
	public static ScopeTypeEnums getByCode(String code) {
		for (ScopeTypeEnums _enum : values()) {
			if (_enum.code.equals(code)) {
				return _enum;
			}
		}
		return null;
	}
	
	/**
	 * 获取全部枚举
	 * 
	 * @return List<ScopeTypeEnums>
	 */
	public static List<ScopeTypeEnums> getAllEnum() {
		List<ScopeTypeEnums> list = new ArrayList<ScopeTypeEnums>();
		for (ScopeTypeEnums _enum : values()) {
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
		for (ScopeTypeEnums _enum : values()) {
			list.add(_enum.code);
		}
		return list;
	}
	
}
