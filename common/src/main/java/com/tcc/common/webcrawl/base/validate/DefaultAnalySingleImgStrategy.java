package com.tcc.common.webcrawl.base.validate;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import com.tcc.common.webcrawl.base.model.Template;
import com.tcc.common.webcrawl.base.util.ImageUtils;

/**                    
 * @Filename DefaultAnalySingleImgStrategy.java
 *
 * @Description 默认的解析单张图片策略
 *
 * @author tcc 2015-12-28
 *
 */
public class DefaultAnalySingleImgStrategy extends AnalySingleImgStrategy {
	
	/**
	 * 构建一个<code>DefaultAnalySingleImgStrategy.java</code>
	 * @param templateList
	 * @param criticalValue
	 */
	public DefaultAnalySingleImgStrategy(List<Template> templateList, int criticalValue) {
		super(templateList, criticalValue);
	}
	
	/**
	 * 构建一个<code>DefaultAnalySingleImgStrategy.java</code>
	 * @param templateMap
	 * @param criticalValue
	 */
	public DefaultAnalySingleImgStrategy(Map<String, List<Template>> templateMap, int criticalValue) {
		super(templateMap, criticalValue);
	}
	
	/**
	 * @param image
	 * @return 若没有找到匹配的模板，返回null
	 * @see com.uaf.grabweb.base.validate.AnalySingleImgStrategy#getResult(java.awt.image.BufferedImage)
	 */
	@Override
	protected String getResult(BufferedImage image) {
		String result = "";
		double max = 0d;
		int width = image.getWidth();
		int height = image.getHeight();
		//通过验证码图片长宽去获取模板
		List<Template> list = templateMap.get(getKey(width, height));
		if (list == null || list.size() == 0) {
			//没有对应模板，则无法解析或解析失败
			//返回null
			return null;
		} else {
			for (Template temp : list) {//模板长度需一致
				int count = 0;
				int[][] array = temp.getTempArray();
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						if (array[j][i] == 0
							|| (ImageUtils.isWhite(image.getRGB(i, j), criticalValue))) {
							continue;
						}
						count++;
					}
				}
				double percent = (double) count / temp.getCorrectCount();
				if (max < percent) {
					max = percent;
					result = temp.getResult();
				}
			}
		}
		return result;
	}
	
}
