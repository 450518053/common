package com.tcc.common.webcrawl.sz.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcc.common.webcrawl.base.enums.HttpHandlerTypeEnum;
import com.tcc.common.webcrawl.base.factory.HttpHandlerFactory;
import com.tcc.common.webcrawl.base.http.HttpHandler;
import com.tcc.common.webcrawl.base.main.BaseMain;
import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.model.Template;
import com.tcc.common.webcrawl.base.util.ImageUtils;
import com.tcc.common.webcrawl.base.validate.CutImgStrategy;
import com.tcc.common.webcrawl.base.validate.ValidateCode;
import com.tcc.common.webcrawl.sz.validate.SZUUPAnalySingleImgStrategy;
import com.tcc.common.webcrawl.sz.validate.SZValidateCode;

/**                    
 * @Filename UnifiedUserPlatformLogin.java
 *
 * @Description 深圳社会统一用户基础平台
 * 					https://suum.szsi.gov.cn/suum/
 *
 * @author tcc 2015-12-18
 *
 */
public class UnifiedUserPlatformLogin extends BaseMain {
	
	private static final String			imgStyle;
										
	private static final String			errorCodeImgSaveDir;
										
	private static final ValidateCode	validateCode;
										
	//	private static final int			codeSize;				//验证码个数
	
	static {
		int criticalValue = 0;
		List<Template> templateList = Collections.emptyList();
		imgStyle = "jpg";
		errorCodeImgSaveDir = "C:\\Users\\cq02559\\Desktop\\sz\\error_code\\unifiedUserPlatform\\";
		validateCode = new SZValidateCode(new CutImgStrategy() {
			@Override
			protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
				List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
				int width = codeImg.getWidth();
				int height = codeImg.getHeight();
				BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						Color color = new Color(codeImg.getRGB(x, y));
						if (color.getBlue() > 125 && (color.getRed() + color.getGreen()) < 100) {
							img.setRGB(x, y, Color.BLACK.getRGB());
						} else {
							img.setRGB(x, y, Color.WHITE.getRGB());
						}
					}
				}
				subImgs.addAll(ImageUtils.getSubImgsByBucketAlgorithm(img, 120));
				return subImgs;
			}
		}, new SZUUPAnalySingleImgStrategy(templateList, criticalValue));
		//		codeSize = 4;
	}
	
	//图像处理之基于NCC模板匹配识别  http://blog.csdn.net/jia20003/article/details/48852549
	public static void main(String[] args) throws Exception {
		new UnifiedUserPlatformLogin().login();
	}
	
	public void login() throws Exception {
		HttpHandler httpHandler = HttpHandlerFactory
			.create(HttpHandlerTypeEnum.SZ_UNIFIED_USER_PLATFORM);
		//		BufferedImage image = httpHandler
		//			.downloadImg("https://suum.szsi.gov.cn/suum/getKaptchaImage.do?method=getKaptchaImage");
		for (int i = 0; i < 10; i++) {
			BufferedImage image = httpHandler.downloadImg(
				"https://suum.szsi.gov.cn/suum/getKaptchaImage.do?method=getKaptchaImage");
			System.out.println(validateCode.analy(image));
			AnalyResult analyResult = validateCode.analy(image);
			saveErrorResult(analyResult, imgStyle, errorCodeImgSaveDir);
		}
	}
}
