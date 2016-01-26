package com.tcc.common.property;

import java.util.Map;

/**                    
 * @Filename Config.java
 *
 * @Description 通用的配置文件类 
 *
 * @author tan 2016年1月7日
 *
 * @email 450518053@qq.com
 * 
 */
public class Config {
	
	/**读取的配置*/
	private final Map<String, String> propertiesMap;
	
	/**
	 * 构建一个<code>Config.java</code>
	 * @param propertiesMap
	 */
	public Config(final Map<String, String> propertiesMap) {
		super();
		this.propertiesMap = propertiesMap;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public String getString(final String key) {
		return propertiesMap.get(key);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(final String key, final String defaultValue) {
		String value = getString(key);
		return value == null ? defaultValue : value;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public int getInt(final String key) {
		return Integer.parseInt(getString(key));
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(final String key, int defaultValue) {
		return Integer.parseInt(getString(key, Integer.toString(defaultValue)));
	}
	
	/**
	 * @param name
	 * @return
	 */
	public boolean getBoolean(final String name) {
		return getBoolean(name, false);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(final String key, boolean defaultValue) {
		return Boolean.parseBoolean(getString(key, Boolean.toString(defaultValue)));
	}
	
	/**
	 * @return
	 */
	public Map<String, String> getPropertiesMap() {
		return propertiesMap;
	}
	
}
