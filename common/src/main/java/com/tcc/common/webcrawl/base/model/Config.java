package com.tcc.common.webcrawl.base.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**                    
 * @Filename Config.java
 *
 * @Description 通用的配置文件读取类
 *
 * @author tcc 2015-12-29
 *
 */
public class Config {
	
	/**
	 * 获取一个Config实例
	 * @param configFileDir 在项目中的目录
	 * @param configFileName 文件名
	 * @return
	 */
	public static Config getInstatnce(final String configFileDir, final String configFileName) {
		String result = "";
		Pattern p = Pattern.compile("[/|\\\\]*([A-Za-z0-9-_\\.]+)[/|\\\\]*");//清理掉目录前后的分割符
		Matcher m = p.matcher(configFileDir);
		while (m.find()) {
			result += m.group(1);
		}
		result = "/" + result + "/";
		String configFilePath = System.getProperties().getProperty("user.dir") + result
								+ configFileName;
		return getInstatnce(configFilePath);
	}
	
	/**
	 * 获取一个Config实例
	 * @param configFilePath 绝对路径
	 * @return
	 */
	public static Config getInstatnce(final String configFilePath) {
		InputStream in = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			in = Config.class.getResourceAsStream(configFilePath);
			if (in == null) {
				in = new FileInputStream(configFilePath);
			}
			Properties p = new Properties();
			p.load(in);
			Enumeration<?> e = p.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				map.put(key, p.getProperty(key));
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		return new Config(map);
	}
	
	/**读取的配置*/
	private final Map<String, String>	propertiesMap;
	
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
	
}
