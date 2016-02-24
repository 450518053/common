package com.tcc.common.webcrawl.sz.validate;

import com.tcc.common.webcrawl.base.validate.AnalySingleImgStrategy;
import com.tcc.common.webcrawl.base.validate.CutImgStrategy;
import com.tcc.common.webcrawl.base.validate.ValidateCode;

/**                    
 * @Filename SZValidateCode.java
 *
 * @Description 深圳验证码处理
 *
 * @author frank.tan 2015-12-28
 *
 */
public class SZValidateCode extends ValidateCode {
	
	/**
	 * 构建一个<code>SZValidateCode.java</code>
	 * @param cutImgStrategy
	 * @param analySingleImgStrategy
	 */
	public SZValidateCode(CutImgStrategy cutImgStrategy,
							AnalySingleImgStrategy analySingleImgStrategy) {
		super(cutImgStrategy, analySingleImgStrategy);
	}
	
}
