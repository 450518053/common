package com.tcc.common.webcrawl.base.validate;

import java.awt.image.BufferedImage;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.util.ImageUtils;

/**
 * @Filename ValidateCode.java
 *
 * @Description 验证码处理
 *
 * @author frank.tan 2015-12-11
 *
 */
public abstract class ValidateCode {
	
	protected final CutImgStrategy			cutImgStrategy;
	
	protected final AnalySingleImgStrategy	analySingleImgStrategy;
	
	/**
	 * 构建一个<code>ValidateCode.java</code>
	 * @param cutImgStrategy
	 * @param analySingleImgStrategy
	 */
	public ValidateCode(CutImgStrategy cutImgStrategy, AnalySingleImgStrategy analySingleImgStrategy) {
		this.cutImgStrategy = cutImgStrategy;
		this.analySingleImgStrategy = analySingleImgStrategy;
	}
	
	/**
	 * 解析验证码图片
	 * @param codeImg
	 * @return
	 * @throws Exception
	 */
	public AnalyResult analy(BufferedImage codeImg) throws Exception {
		List<BufferedImage> subImgs = getSubImgs(codeImg);
		boolean success = true;
		String analyResult = "";
		for (int i = 0; i < subImgs.size(); i++) {//遍历每张图片
			BufferedImage grayImage = ImageUtils.grayImage(subImgs.get(i));
			String result = getResult(grayImage);
			if (StringUtils.isBlank(result)) {//解析失败
				result = "&";
				success = false;
			}
			analyResult += result;
		}
		return new AnalyResult(success, analyResult, codeImg, subImgs);
	}
	
	/**
	 * 切割
	 * @param codeImg
	 * @return
	 */
	protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
		return cutImgStrategy.getSubImgs(codeImg);
	}
	
	/**
	 * 根据截取的单个图片获取解析结果
	 * @param image
	 * @return 返回空视为解析失败
	 */
	protected String getResult(BufferedImage image) {
		return analySingleImgStrategy.getResult(image);
	}
}
