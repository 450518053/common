package com.tcc.common.webcrawl.sz.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcc.common.webcrawl.base.enums.HttpHandlerTypeEnum;
import com.tcc.common.webcrawl.base.enums.LoginFailedTypeEnum;
import com.tcc.common.webcrawl.base.exception.AnalyValidateImgErrorException;
import com.tcc.common.webcrawl.base.exception.LoginFailedException;
import com.tcc.common.webcrawl.base.factory.HttpHandlerFactory;
import com.tcc.common.webcrawl.base.http.HttpHandler;
import com.tcc.common.webcrawl.base.main.BaseMain;
import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.validate.CutImgStrategy;
import com.tcc.common.webcrawl.base.validate.DefaultAnalySingleImgStrategy;
import com.tcc.common.webcrawl.base.validate.ValidateCode;
import com.tcc.common.webcrawl.sz.validate.SZAFTemplateParam;
import com.tcc.common.webcrawl.sz.validate.SZValidateCode;

/**                    
 * @Filename AccumulationFundQuery.java
 *
 * @Description 深圳公积金查询
 * 					http://www.szzfgjj.com/fzgn/zfcq/
 *
 * @author tcc 2015-12-14
 *
 */
public class AccumulationFundQuery extends BaseMain {
	//621911958 432503198412280569
	public static void main(String[] args) throws Exception {
		new AccumulationFundQuery().query("0", "621911958", "432503198412280569");
	}
	
	private static final String			imgStyle;
	
	private static final String			errorCodeImgSaveDir;
	
	private static final ValidateCode	validateCode;
	
	private static final int			criticalValue;			//阀值
																
	private static final int			codeSize;				//验证码个数
																
	static {
		imgStyle = "jpg";
		errorCodeImgSaveDir = "C:\\Users\\cq02559\\Desktop\\sz\\error_code\\accumulationFund\\";
		criticalValue = 111;
		validateCode = new SZValidateCode(new CutImgStrategy() {
			@Override
			protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
				List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
				subImgs.add(codeImg.getSubimage(7, 3, 9, 13));//截取
				subImgs.add(codeImg.getSubimage(20, 3, 9, 13));
				subImgs.add(codeImg.getSubimage(33, 3, 9, 13));
				subImgs.add(codeImg.getSubimage(46, 3, 9, 13));
				return subImgs;
			}
		}, new DefaultAnalySingleImgStrategy(SZAFTemplateParam.getInstance().getTemplateList(),
			criticalValue));
		codeSize = 4;
	}
	
	/**
	 * 查询
	 * @param qryflag=1，个人公积金账号、身份证号/qryflag=0，个人社保电脑号、身份证号
	 * @param accnum
	 * @param certinum
	 * @throws Exception
	 */
	public void query(String qryflag, String accnum, String certinum) throws Exception {
		HttpHandler httpHandler = HttpHandlerFactory
			.create(HttpHandlerTypeEnum.SZ_ACCUMULATION_FUNDA);
		httpHandler.get("http://www.szzfgjj.com/fzgn/zfcq/");
		httpHandler.get("http://app.szzfgjj.com:7001/pages/alone_ywcx.jsp");
		BufferedImage codeImg = httpHandler
			.downloadImg("http://app.szzfgjj.com:7001/pages/code.jsp");//获取验证码图片
		//		BufferedImage codeImg = HttpClientUtils
		//			.downloadImg("http://app.szzfgjj.com:7001/pages/code.jsp?yzm=" + num)
		String code = null;
		AnalyResult analyResult = null;
		try {
			analyResult = validateCode.analy(codeImg);//解析验证码
			if (analyResult.isSuccess() && analyResult.getSize() == codeSize) {
				code = analyResult.getResult();
			} else {
				throw new AnalyValidateImgErrorException();
			}
		} catch (Exception e) {
			saveErrorResult(analyResult, imgStyle, errorCodeImgSaveDir);//保存解析失败的图片
			throw new AnalyValidateImgErrorException(e);//重试
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(getQueryParams(qryflag, accnum,
			certinum, code), "gbk");
		String result = httpHandler.post("http://app.szzfgjj.com:7001/accountQuery", entity);//返回json格式
		JSONObject jsonObject = JSON
			.parseObject(new String(result.getBytes("iso-8859-1"), "utf-8"));
		if (!jsonObject.getBooleanValue("success")) {
			String errorMessage = jsonObject.getString("msg");
			if (errorMessage.contains("验证码不正确") || errorMessage.contains("验证码异常,请重新刷新验证码")) {
				saveErrorResult(analyResult, imgStyle, errorCodeImgSaveDir);//保存解析错误的图片
				throw new LoginFailedException(LoginFailedTypeEnum.ERROR_CODE);//重试
			} else if (errorMessage.contains("无此记录")) {
				System.out.println("无此记录");
				throw new LoginFailedException(LoginFailedTypeEnum.ACC_NOT_EXIST);
			} else {
				throw new LoginFailedException(errorMessage);
			}
		} else {
			//查询成功
			String newaccnum = jsonObject.getString("newaccnum");//个人公积金账号
			String balance = jsonObject.getString("msg");//账户余额
			String sbbalance = jsonObject.getString("sbbalance");//社保移交金额
			String peraccstate = getState(jsonObject);//状态
			System.out.println(newaccnum);
			System.out.println(balance);
			System.out.println(sbbalance);
			System.out.println(peraccstate);
		}
	}
	
	/**
	 * 解析状态
	 * @param jsonObject
	 * @return
	 * @throws LoginFailedException 
	 */
	private String getState(JSONObject jsonObject) throws LoginFailedException {
		String peraccstate = null;
		switch (jsonObject.getInteger("peraccstate")) {
			case 0:
				peraccstate = "正常";
				break;
			case 1:
				peraccstate = "封存";
				break;
			case 8:
				peraccstate = "销户未结算";
				break;
			case 9:
				switch (jsonObject.getInteger("cardstat")) {
					case 0:
						peraccstate = "账户未生效-新账户";
						break;
					case 1:
						peraccstate = "账户未生效-核查中";
						break;
					case 2:
						peraccstate = "销户";
						break;
					case 3:
						peraccstate = "账户未生效-核查失败";
						break;
					default:
						throw new LoginFailedException("解析'" + jsonObject.toString() + "'获取账户状态失败");
				}
				break;
			default:
				peraccstate = "非正常";
				break;
		}
		return peraccstate;
	}
	
	/**
	 * 组装查询post请求参数
	 * @param qryflag
	 * @param accnum 个人公积金账号/个人社保电脑号
	 * @param certinum 身份证号
	 * @param code 验证码
	 * @return
	 * @throws Exception
	 */
	public List<NameValuePair> getQueryParams(String qryflag, String accnum, String certinum,
												String code) throws Exception {
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		parms.add(new BasicNameValuePair("accnum", accnum));
		parms.add(new BasicNameValuePair("certinum", certinum));
		parms.add(new BasicNameValuePair("qryflag", qryflag));
		parms.add(new BasicNameValuePair("verify", code));
		return parms;
	}
}
