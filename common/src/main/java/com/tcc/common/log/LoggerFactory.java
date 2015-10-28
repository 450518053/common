package com.tcc.common.log;

/**                    
 * @Filename LoggerFactory.java
 *
 * @Description 提供生成变长参数的logger工厂
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public final class LoggerFactory {
	
	public static com.tcc.common.log.Logger getLogger(String name) {
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(name);
		return new LoggerImpl(logger);
	}
	
	public static com.tcc.common.log.Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
		
	}
}
