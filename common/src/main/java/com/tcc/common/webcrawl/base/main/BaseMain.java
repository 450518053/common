package com.tcc.common.webcrawl.base.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.tcc.common.util.DateUtils;
import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.util.ImageUtils;

/**                    
 * @Filename BaseMain.java
 *
 * @Description 基本的主类
 *
 * @author tcc 2015-12-21
 *
 */
public class BaseMain {
	
	/**
	 * 验证码解析失败时，存储错误结果
	 * @param analyResult
	 * @param imgStyle
	 * @param saveDir
	 * @throws IOException 
	 */
	protected void saveErrorResult(	AnalyResult analyResult, String imgStyle,
									String saveDir) throws IOException {
		if (analyResult == null) {
			return;
		}
		BufferedImage codeImg = analyResult.getCodeImg();
		List<BufferedImage> subImgs = analyResult.getSubImgs();
		String date = DateUtils.getFormat(DateUtils.dtLong).format(new Date());
		ImageUtils.saveImg(codeImg,
			saveDir + date + "-codeImg-" + analyResult.getResult() + "." + imgStyle, imgStyle);
		for (int i = 0; i < subImgs.size(); i++) {//遍历每张图片
			ImageUtils.saveImg(subImgs.get(i), saveDir + date + "-piece-" + i + "." + imgStyle,
				imgStyle);
		}
	}
	
}
