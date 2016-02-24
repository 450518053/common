package com.tcc.common.webcrawl.base.model;

import java.awt.image.BufferedImage;
import java.util.List;

/**                    
 * @Filename AnalyResult.java
 *
 * @Description 解析验证码结果类
 *
 * @author tcc 2015-12-21
 *
 */
public class AnalyResult {
	
	private boolean				success;	//是否解析成功
											
	private String				result;	//解析结果
											
	private int					size;		//结果个数
											
	private BufferedImage		codeImg;
	
	private List<BufferedImage>	subImgs;
	
	/**
	 * 构建一个<code>AnalyResult.java</code>
	 * @param success
	 * @param result
	 * @param codeImg
	 * @param subImgs
	 */
	public AnalyResult(boolean success, String result, BufferedImage codeImg,
						List<BufferedImage> subImgs) {
		super();
		this.success = success;
		this.result = result;
		this.codeImg = codeImg;
		this.subImgs = subImgs;
		this.size = subImgs.size();
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public AnalyResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	
	public String getResult() {
		return result;
	}
	
	public AnalyResult setResult(String result) {
		this.result = result;
		return this;
	}
	
	public int getSize() {
		return size;
	}
	
	public AnalyResult setSize(int size) {
		this.size = size;
		return this;
	}
	
	public BufferedImage getCodeImg() {
		return codeImg;
	}
	
	public AnalyResult setCodeImg(BufferedImage codeImg) {
		this.codeImg = codeImg;
		return this;
	}
	
	public List<BufferedImage> getSubImgs() {
		return subImgs;
	}
	
	public AnalyResult setSubImgs(List<BufferedImage> subImgs) {
		this.subImgs = subImgs;
		return this;
	}
	
	@Override
	public String toString() {
		return "AnalyResult [success=" + success + ", result=" + result + ", size=" + size
				+ ", codeImg=" + codeImg + ", subImgs=" + subImgs + "]";
	}
	
}
