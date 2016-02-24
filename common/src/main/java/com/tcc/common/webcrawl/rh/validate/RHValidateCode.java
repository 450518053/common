package com.tcc.common.webcrawl.rh.validate;

import java.awt.image.BufferedImage;

import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.validate.AnalySingleImgStrategy;
import com.tcc.common.webcrawl.base.validate.CutImgStrategy;
import com.tcc.common.webcrawl.base.validate.ValidateCode;

/**                    
 * @Filename RHValidateCode.java
 *
 * @Description 人行验证码处理
 *
 * @author tcc 2015-12-28
 *
 */
public class RHValidateCode extends ValidateCode {
	
	/**
	 * 构建一个<code>RHValidateCode.java</code>
	 * @param cutImgStrategy
	 * @param analySingleImgStrategy
	 */
	public RHValidateCode(CutImgStrategy cutImgStrategy,
							AnalySingleImgStrategy analySingleImgStrategy) {
		super(cutImgStrategy, analySingleImgStrategy);
	}
	
	/**
	 * 对j、i特殊处理
	 * @param codeImg
	 * @return
	 * @throws Exception
	 * @see com.uaf.grabweb.base.validate.ValidateCode#analy(java.awt.image.BufferedImage)
	 */
	@Override
	public AnalyResult analy(BufferedImage codeImg) throws Exception {
		AnalyResult analyResult = super.analy(codeImg);
		String result = analyResult.getResult().replace("0", "");
		return analyResult.setResult(result);
	}
}
