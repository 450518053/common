package com.tcc.common.webcrawl.base.validate;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcc.common.webcrawl.base.model.Template;

/**                    
 * @Filename AnalySingleImgStrategy.java
 *
 * @Description 解析单张图片策略
 *
 * @author tcc 2015-12-28
 *
 */
public abstract class AnalySingleImgStrategy {
	
	/**模板*/
	protected final Map<String, List<Template>>	templateMap;
	
	/**阀值*/
	protected final int							criticalValue;
																
	/**
	 * 构建一个<code>AnalySingleImgStrategy.java</code>
	 * @param templateList
	 * @param criticalValue
	 */
	public AnalySingleImgStrategy(List<Template> templateList, int criticalValue) {
		this.criticalValue = criticalValue;
		templateMap = new HashMap<String, List<Template>>();
		for (Template template : templateList) {
			int height = template.getHeight();
			int width = template.getWidth();
			List<Template> list = templateMap.get(getKey(width, height));//按模板长宽分类
			if (list != null) {
				list.add(template);
			} else {
				list = new ArrayList<Template>();
				list.add(template);
				templateMap.put(getKey(width, height), list);
			}
		}
	}
	
	/**
	 * 构建一个<code>AnalySingleImgStrategy.java</code>
	 * @param templateMap
	 * @param criticalValue
	 */
	public AnalySingleImgStrategy(Map<String, List<Template>> templateMap, int criticalValue) {
		super();
		this.templateMap = templateMap;
		this.criticalValue = criticalValue;
	}
	
	/**
	 * 根据截取的单个图片获取解析结果
	 * @param image
	 * @return 返回空视为解析失败
	 */
	protected abstract String getResult(BufferedImage image);
	
	/**
	 * 组装key
	 * @param width
	 * @param height
	 * @return
	 */
	protected String getKey(int width, int height) {
		return width + ":" + height;
	}
}
