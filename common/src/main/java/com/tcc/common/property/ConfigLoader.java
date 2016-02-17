package com.tcc.common.property;

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
 * @Filename ConfigLoader.java
 *
 * @Description 通用的配置文件读取类 
 *
 * @author tan 2016年1月7日
 *
 * @email 450518053@qq.com
 * 
 */
public class ConfigLoader {
	
	/**
	 * 获取一个Config实例
	 * @param configFileDir 在项目中的目录
	 * @param configFileName 文件名
	 * @return
	 * @throws IOException 
	 */
	public static Config getInstatnce(	final String configFileDir,
										final String configFileName) throws IOException {
		String result = "";
		Pattern p = Pattern.compile("[/|\\\\]*([A-Za-z0-9-_\\.]+)[/|\\\\]*");//清理掉目录前后的分割符
		Matcher m = p.matcher(configFileDir);
		while (m.find()) {
			result += m.group(1);
		}
		result = "/" + result + "/";
		String configFilePath = System.getProperties().getProperty("user.dir")+ result
								+ configFileName;
		return getInstatnce(configFilePath);
	}
	
	/**
	 * 获取一个Config实例
	 * @param configFilePath 路径
	 * @return
	 * @throws IOException 
	 */
	public static Config getInstatnce(final String configFilePath) throws IOException {
		InputStream in = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			in = Config.class.getClassLoader().getResourceAsStream(configFilePath);
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
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return new Config(map);
	}
	
}
