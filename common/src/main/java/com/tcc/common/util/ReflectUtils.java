package com.tcc.common.util;

import java.lang.reflect.Method;

/**                    
 * @Filename ReflectUtils.java
 *
 * @Description 反射工具类
 *
 * @author tan 2016年1月5日
 *
 * @email 450518053@qq.com
 * 
 */
public class ReflectUtils {
	
	/**
	 * 使用反射set属性
	 * 		只支持set字符串
	 * @param attrName 类的属性名
	 * @param value 属性值
	 * @param thisObj 对象
	 */
	public static void setValue(String attrName, String value, Object thisObj) {
		setValue(attrName, value, thisObj, String.class);
	}
	
	/**
	 * 使用反射set属性
	 * @param attrName 类的属性名
	 * @param value 属性值
	 * @param thisObj 对象
	 * @param attrClass 属性类型
	 */
	public static void setValue(String attrName, Object value, Object thisObj, Class<?> attrClass) {
		Class<?> c = null;
		try {
			c = Class.forName(thisObj.getClass().getName());//获取对象的class
			Method m = c.getMethod(acquireSetMethodName(attrName), attrClass);//获取属性的set方法
			m.invoke(thisObj, value);
		} catch (Exception e) {
			throw new IllegalArgumentException(
				String.format("使用反射set属性异常,类的属性名'%s',属性值'%s',属性类型'%s',类名'%s'", attrName, value,
					attrClass.getName(), c.getName()),
				e);
		}
	}
	
	/**
	 * 获取set方法名
	 * @param fildeName
	 * @return
	 * @throws Exception
	 */
	public static String acquireSetMethodName(String fildeName) throws Exception {
		byte[] items = fildeName.getBytes();
		//若属性首字母小写，则改为大写
		if (items[0] >= 97) {
			items[0] = (byte) ((char) items[0] - 'a' + 'A');
		}
		return "set" + new String(items);
	}
	
}
