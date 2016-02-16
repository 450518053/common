package com.tcc.common.util;

import java.io.File;

/**                    
 * @Filename IOUtils.java
 *
 * @Description 
 *
 * @author tan 2016年1月22日
 *
 * @email 450518053@qq.com
 * 
 */
public class IOUtils {
	
	/**
	 * 通过 路径 得到文件/文件夹扩展名。
	 * @param file 要得到扩展名文件/文件夹的路径。
	 * @return file 对应的文件/文件夹的扩展名。
	 * @throws IllegalArgumentException 当 file 为 null 时。
	 */
	public static String getExtension(File file) {
		Args.notNull(file, "notNull");
		return getExtension(file.getPath());
	}
	
	/**
	 * 通过 路径名 得到文件/文件夹扩展名。
	 * @param fileName 要得到扩展名文件/文件夹的路径名。
	 * @return fileName 对应的文件/文件夹的扩展名。
	 * @throws IllegalArgumentException 当 fileName 为 null 时。
	 */
	public static String getExtension(String fileName) {
		Args.notBlank(fileName, "fileName");
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return "";
		}
		return fileName.substring(index + 1);
	}
	
}
