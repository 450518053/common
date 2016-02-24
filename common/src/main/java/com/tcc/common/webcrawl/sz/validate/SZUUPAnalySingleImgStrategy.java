package com.tcc.common.webcrawl.sz.validate;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import com.tcc.common.webcrawl.base.model.Template;
import com.tcc.common.webcrawl.base.util.ImageUtils;
import com.tcc.common.webcrawl.base.validate.AnalySingleImgStrategy;

/**                    
 * @Filename SZUUPAnalySingleImgStrategy.java
 *
 * @Description 深圳社会统一用户基础平台解析单张图片策略
 *
 * @author frank.tan 2015-12-28
 *
 */
public class SZUUPAnalySingleImgStrategy extends AnalySingleImgStrategy {
	
	/**
	 * 构建一个<code>SZUUPAnalySingleImgStrategy.java</code>
	 * @param templateList
	 * @param criticalValue
	 */
	public SZUUPAnalySingleImgStrategy(List<Template> templateList, int criticalValue) {
		super(templateList, criticalValue);
	}
	
	/**
	 * 构建一个<code>SZUUPAnalySingleImgStrategy.java</code>
	 * @param templateMap
	 * @param criticalValue
	 */
	public SZUUPAnalySingleImgStrategy(Map<String, List<Template>> templateMap, int criticalValue) {
		super(templateMap, criticalValue);
	}
	
	/**
	 * @param image
	 * @return
	 * @see com.uaf.grabweb.base.validate.AnalySingleImgStrategy#getResult(java.awt.image.BufferedImage)
	 */
	@Override
	protected String getResult(BufferedImage image) {
		int count = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		if (width < 2 || height < 2) {
			return "&";//误差
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (ImageUtils.getAvgRGB(image.getRGB(x, y)) < 100) {
					count++;
				}
			}
		}
		System.out.println(count);
		return null;
	}
	
}
