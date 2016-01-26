package com.tcc.common.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @Filename MD5Util.java
 *
 * @Description md5算法工具类
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class MD5Util {
	
	static MessageDigest md = null;
	
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对一个文件求他的md5值
	 * @param f 要求md5值的文件
	 * @return md5串
	 * @throws Exception 
	 */
	public static String md5(File f) throws Exception {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			//每次100KB
			byte[] buffer = new byte[102400];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(md.digest()));
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("计算文件MD5值异常,未找到文件'" + f.getAbsolutePath() + "'");
		} catch (Exception e) {
			throw new Exception("计算文件MD5值异常", e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				throw new IOException("计算文件MD5值关闭流异常", e);
			}
		}
	}
	
	/**
	 * 求一个字符串的md5值
	 * @param target 字符串
	 * @return md5 value
	 */
	public static String md5(String target) {
		return DigestUtils.md5Hex(target);
	}
	
}