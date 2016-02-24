package com.tcc.common.webcrawl.rh.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.tcc.common.webcrawl.base.enums.HttpHandlerTypeEnum;
import com.tcc.common.webcrawl.base.enums.LoginFailedTypeEnum;
import com.tcc.common.webcrawl.base.exception.AnalyValidateImgErrorException;
import com.tcc.common.webcrawl.base.exception.LoginFailedException;
import com.tcc.common.webcrawl.base.factory.HttpHandlerFactory;
import com.tcc.common.webcrawl.base.http.HttpHandler;
import com.tcc.common.webcrawl.base.main.BaseMain;
import com.tcc.common.webcrawl.base.model.AnalyResult;
import com.tcc.common.webcrawl.base.model.Template;
import com.tcc.common.webcrawl.base.util.ImageUtils;
import com.tcc.common.webcrawl.base.validate.DefaultAnalySingleImgStrategy;
import com.tcc.common.webcrawl.base.validate.ValidateCode;
import com.tcc.common.webcrawl.rh.validate.RHCutImgStrategy;
import com.tcc.common.webcrawl.rh.validate.RHTemplateParam;
import com.tcc.common.webcrawl.rh.validate.RHValidateCode;

/**
 * @Filename PersonalCreditLogin.java
 *
 * @Description 人行个人征信登录
 * 					https://ipcrs.pbccrc.org.cn/
 *
 * @author tcc 2015-12-14
 *
 */
public class PersonalCreditLogin extends BaseMain {
	
	private static final String			imgStyle;
	
	private static final String			errorCodeImgSaveDir;
	
	private static final ValidateCode	validateCode;
	
	private static final int			criticalValue;			//阀值
																
	private static final int			codeSize;				//验证码个数
																
	/**
	 * 默认消息头
	 */
	private static final Header[]		DEFAULT_HEADERS	= new Header[] {
			new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0"),//火狐
			new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"),
			new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"),
			new BasicHeader("Connection", "keep-alive"),
			new BasicHeader("Host", "ipcrs.pbccrc.org.cn") };
	
	static {
		imgStyle = "png";
		errorCodeImgSaveDir = "C:\\Users\\cq02559\\Desktop\\rh\\";
		criticalValue = 120;
		List<Template> templateList = new ArrayList<Template>();
		try {
			templateList.addAll(ImageUtils.getTemplateList(new File("template/rh"), criticalValue));//一对多模板
		} catch (Exception e) {
			e.printStackTrace();
		}
		templateList.addAll(RHTemplateParam.getInstance().getTemplateList());//一对一模板
		validateCode = new RHValidateCode(new RHCutImgStrategy(),
			new DefaultAnalySingleImgStrategy(templateList, criticalValue));
		codeSize = 6;
	}
	
	public static void main(String[] args) throws Exception {
		new PersonalCreditLogin().login("tcc0807", "qq729827");
	}
	
	public void login(String loginName, String password) throws Exception {
		HttpHandler httpHandler = HttpHandlerFactory.create(
			HttpHandlerTypeEnum.RH_PERRSONAL_CREDIT, DEFAULT_HEADERS);
		//进入首页
		httpHandler.get("https://ipcrs.pbccrc.org.cn/");
		//进入登录页面
		String initResult = httpHandler.get(
			"https://ipcrs.pbccrc.org.cn/login.do?method=initLogin", new BasicHeader("Referer",
				"https://ipcrs.pbccrc.org.cn/top1.do"));
		System.out.println(initResult);
		BufferedImage codeImg = httpHandler.downloadImg("https://ipcrs.pbccrc.org.cn/imgrc.do?"
														+ getImg(initResult), new BasicHeader(
			"Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp"));
		//		BufferedImage codeImg = httpHandler.downloadImg("https://ipcrs.pbccrc.org.cn/imgrc.do?"
		//														+ Math.random(), new BasicHeader("Referer",
		//			"https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp"));
		//		BufferedImage image = ImageIO.read(new File(
		//			"C:\\Users\\cq02559\\Desktop\\rh\\20151221164255-codeImg-.png"));
		String code = null;
		//解析验证码
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
		System.out.println(code);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(getLoginParams(loginName, password,
			initResult, code), "gbk");
		String loginResult = httpHandler.post("https://ipcrs.pbccrc.org.cn/login.do", entity,
			new BasicHeader("Referer", "https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp"));
		System.out.println(loginResult);
		if (loginResult.contains("验证码输入错误")) {
			saveErrorResult(analyResult, imgStyle, errorCodeImgSaveDir);//保存解析错误的图片
			throw new LoginFailedException(LoginFailedTypeEnum.ERROR_CODE);//重试
		}
	}
	
	/**
	 * 组装登录数据
	 * @param loginName
	 * @param password
	 * @param source
	 * @param code
	 * @return
	 */
	private static List<NameValuePair> getLoginParams(String loginName, String password,
														String source, String code) {
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		parms.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", getToken(source)));
		parms.add(new BasicNameValuePair("method", "login"));
		parms.add(new BasicNameValuePair("date", getDate(source)));
		parms.add(new BasicNameValuePair("loginname", loginName));
		parms.add(new BasicNameValuePair("password", password));
		parms.add(new BasicNameValuePair("_@IMGRC@_", code));//验证码
		return parms;
	}
	
	/**
	 * 获取验证码图片请求后缀
	 * @param source
	 * @return
	 */
	private static String getImg(String source) {
		String result = getByRegex(source, "img\\ssrc\\=\"/imgrc\\.do\\?0\\.([0-9]*)\"");
		if (StringUtils.isBlank(result)) {
			throw new IllegalArgumentException("getImg无需要的数据->>" + source);
		}
		return "0." + result;
	}
	
	/**
	 * 获取date
	 * @param source
	 * @return
	 */
	private static String getDate(String source) {
		int index = source.indexOf("<input type=\"hidden\" name=\"date\" value=\"");
		if (index < 0) {
			throw new IllegalArgumentException("getDate无需要的数据->>" + source);
		}
		index += "<input type=\"hidden\" name=\"date\" value=\"".length();
		String result = source.substring(index, 13 + index);
		return result;
	}
	
	/**
	 * 获取token
	 * @param source
	 * @return
	 */
	private static String getToken(String source) {
		String result = getByRegex(
			source,
			"<input\\stype=\"hidden\"\\sname=\"org\\.apache\\.struts\\.taglib\\.html\\.TOKEN\"\\svalue=\"([0-9a-z]*)\">");
		if (StringUtils.isBlank(result)) {
			throw new IllegalArgumentException("getToken无需要的数据->>" + source);
		}
		return result;
	}
	
	/**
	 * 通过正则截取字符串
	 * @param source
	 * @param pattern
	 * @return
	 */
	private static String getByRegex(String source, String pattern) {
		//^ $ . * +  - ? = ! : | \ / ( ) [ ] { }
		String result = "";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		while (m.find()) {
			result += m.group(1);
		}
		System.out.println(result);
		return result;
	}
}
