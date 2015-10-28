package com.tcc.common.util;

/**                    
 * @Filename StringUtils.java
 *
 * @Description String 工具类
 *
 * @author tan 2016年1月26日
 *
 * @email 450518053@qq.com
 * 
 */
public class StringUtils {
	
	/**
	 * 检查字符串是否不是空白： null 、空字符串"" 或只有空白字符。
	 * @param str
	 *            要检查的字符串
	 * @return 如果不为空白, 则返回true
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}
	
	/**
	 * 检查字符串是否是空白： null 、空字符串"" 或只有空白字符。
	 * @param str
	 *            要检查的字符串
	 * @return 如果为空白, 则返回true
	 */
	public static boolean isBlank(String str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
