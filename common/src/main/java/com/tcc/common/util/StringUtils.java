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
	 * 检查字符是否是空白： null、长度为0
	 * @param s 要检查的字符串
	 * @return
	 */
	public static boolean isEmpty(final CharSequence c) {
		if (c == null) {
			return true;
		}
		return c.length() == 0;
	}
	
	/**
	 * 检查字符是否是空白： null、长度为0
	 * @param s 要检查的字符串
	 * @return
	 */
	public static boolean isNotEmpty(final CharSequence c) {
		return !isEmpty(c);
	}
	
	/**
	 * 检查字符是否不是空白： null 、空字符串"" 或只有空白字符。
	 * @param c 要检查的字符
	 * @return 如果不为空白, 则返回true
	 */
	public static boolean isNotBlank(final CharSequence c) {
		return !StringUtils.isBlank(c);
	}
	
	/**
	 * 检查字符是否是空白： null 、空字符串"" 或只有空白字符。
	 * @param c 要检查的字符
	 * @return 如果为空白, 则返回true
	 */
	public static boolean isBlank(final CharSequence c) {
		int length;
		if ((c == null) || ((length = c.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(c.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
