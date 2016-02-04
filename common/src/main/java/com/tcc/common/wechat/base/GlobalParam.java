//package com.tcc.common.wechat.base;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
///**                    
// * @Filename GlobalParam.java
// *
// * @Description 全局参数
// *
// * @author tan 2015年10月29日
// *
// * @email 450518053@qq.com
// * 
// */
//public class GlobalParam {
//	
//	static {
//		InputStream input = null;
//		Properties p = new Properties();
//		try {
//			input = GlobalParam.class.getClassLoader().getResourceAsStream("wechat.properties");
//			p.load(input);
//			appId = p.getProperty("base_appId").trim();//初始化appID、appSecret
//			appSecret = p.getProperty("base_appSecret").trim();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	public static String	appId;
//							
//	public static String	appSecret;
//							
//	/**
//	 * 接口调用凭据，需在项目启动时初始化
//	 */
//	public static String	token;
//							
//	/**
//	 * js接口调用凭据，需在项目启动时初始化
//	 */
//	public static String	ticket;
//							
//}
