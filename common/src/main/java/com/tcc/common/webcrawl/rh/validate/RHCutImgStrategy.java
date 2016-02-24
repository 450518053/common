package com.tcc.common.webcrawl.rh.validate;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.tcc.common.webcrawl.base.util.ImageUtils;
import com.tcc.common.webcrawl.base.validate.CutImgStrategy;

/**                    
 * @Filename RHCutImgStrategy.java
 *
 * @Description 人行切割图片策略
 *
 * @author tcc 2015-12-28
 *
 */
public class RHCutImgStrategy extends CutImgStrategy {
	
	/**
	 * @param codeImg
	 * @return
	 * @see com.uaf.grabweb.base.validate.CutImgStrategy#getSubImgs(java.awt.image.BufferedImage)
	 */
	@Override
	protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		//			boolean flag = false;
		//			int startX = 0;
		//			int endX = 0;
		//			int startY = 0;
		//			int endY = 0;
		//			for (int i = 0; i < codeImg.getWidth(); i++) {
		//				boolean hasBlack = false;
		//				for (int j = 0; j < codeImg.getHeight(); j++) {
		//					if (ImageUtils.isBlack(codeImg.getRGB(i, j), criticalValue)) {
		//						hasBlack = true;// 此列有黑色块
		//						if (flag) {
		//							if (startY > j) {
		//								startY = j;
		//							}
		//							if (j > endY) {
		//								endY = j;
		//							}
		//						} else {
		//							startX = i;
		//							startY = j;
		//							endX = i;
		//							endY = j;
		//							flag = true;// 开始查找边界
		//						}
		//					} else {
		//						if (flag) {
		//							if (!hasBlack && (j == codeImg.getHeight() - 1)) {// 最后一格是白色且该列无黑色块
		//								flag = false;// 结束查找边界
		//								endX = i;
		//								BufferedImage tempImg = codeImg.getSubimage(startX, startY,
		//									endX - startX, endY - startY + 1);
		//								//通过验证码图片长宽去获取模板
		//								if (templateMap
		//									.get(getKey(tempImg.getWidth(), tempImg.getHeight())) == null) {
		//									//没有，则图片由多个字母或数字组成，进一步切割
		//			ImagCut imagCut = new ImagCut(tempImg, 120);
		subImgs.addAll(ImageUtils.getSubImgsByBucketAlgorithm(codeImg, 120));
		//								} else {
		//									subImgs.add(tempImg);
		//								}
		//								startY = 0;
		//								endY = 0;
		//							}
		//						}
		//					}
		//				}
		//			}
		return subImgs;
	}
	
}
