package com.tcc.common.webcrawl.base.validate;

import java.awt.image.BufferedImage;
import java.util.List;

/**                    
 * @Filename CutImgStrategy.java
 *
 * @Description 切割图片策略
 *
 * @author tcc 2015-12-28
 *
 */
public abstract class CutImgStrategy {
	
	/**
	 * 切割
	 * @param codeImg
	 * @return
	 */
	protected abstract List<BufferedImage> getSubImgs(BufferedImage codeImg);
	
}
