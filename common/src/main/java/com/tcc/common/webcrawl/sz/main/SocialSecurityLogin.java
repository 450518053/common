package com.tcc.common.webcrawl.sz.main;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.tcc.common.util.EncodeUtils;
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
import com.tcc.common.webcrawl.sz.validate.SZSSLTemplateParam;
import com.tcc.common.webcrawl.sz.validate.SZValidateCode;

/**                    
 * @Filename SocialSecurityLogin.java
 *
 * @Description 深圳社保登录
 *					https://e.szsi.gov.cn/siservice/login.jsp
 *
 * @author tcc 2015-12-11
 *
 */
public class SocialSecurityLogin extends BaseMain {
	
	public static void main(String[] args) throws Exception {
		new SocialSecurityLogin().login("2", "432503198412280569", "19841228Al");
		//		new SocialSecurityLogin().login("2", "432503198412280569", "19841228Al");
	}
	
	private static final String			imgStyle;
	
	private static final String			errorCodeImgSaveDir;
	
	private static final ValidateCode	validateCode;
	
	private static final int			criticalValue;													//阀值
																										
	private static final int			codeSize;														//验证码个数
																										
	/**
	 * 默认消息头
	 * 仿照谷歌浏览器
	 * 动态 Accept Content-Type Referer
	 */
	private static final Header[]		DEFAULT_HEADERS	= new Header[] {
			new BasicHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36"),
			new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"),
			new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"),
			new BasicHeader("Connection", "keep-alive"), new BasicHeader("Host", "e.szsi.gov.cn") };
	
	static {
		imgStyle = "jpg";
		errorCodeImgSaveDir = "C:\\Users\\cq02559\\Desktop\\sz\\error_code\\socialSecurity\\";
		criticalValue = 111;
		validateCode = new SZValidateCode(new CutImgStrategy() {
			@Override
			protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
				List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
				subImgs.add(codeImg.getSubimage(6, 4, 8, 12));//截取
				subImgs.add(codeImg.getSubimage(19, 4, 8, 12));
				subImgs.add(codeImg.getSubimage(32, 4, 8, 12));
				subImgs.add(codeImg.getSubimage(45, 4, 8, 12));
				return subImgs;
			}
		}, new DefaultAnalySingleImgStrategy(SZSSLTemplateParam.getInstance().getTemplateList(),
			criticalValue));
		codeSize = 4;
		
	}
	
	/**
	 * 登录
	 * @param type=1，用户名、密码/type=2，身份证号、密码
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void login(String type, String username, String password) throws Exception {
		HttpHandler httpHandler = HttpHandlerFactory.create(HttpHandlerTypeEnum.SZ_SOCIAL_SECURITY,
			DEFAULT_HEADERS);
		//进入登录页面
		String loginPage = httpHandler.get("https://e.szsi.gov.cn/siservice/login.jsp?dlfs=2");
		//获取pid
		String loginPid = getPid(loginPage);
		//下载验证码
		BufferedImage codeImg = httpHandler.downloadImg(
			"https://e.szsi.gov.cn/siservice/PImages?pid=" + loginPid, new BasicHeader("Referer",
				"https://e.szsi.gov.cn/siservice/login.jsp?dlfs=2"));
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
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(getLoginParams(type, username,
			password, loginPid, code), "gbk");
		//登录
		String loginResult = httpHandler.post("https://e.szsi.gov.cn/siservice/LoginAction.do",
			entity, new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
			new BasicHeader("Referer", "https://e.szsi.gov.cn/siservice/LoginAction.do"));
		//解析登录后返回的页面
		if (!loginResult.contains("action=\"/siservice/pIndex.jsp\"")) {
			//登录不成功
			String errorMessage = getErrorMessage(loginResult);
			if (errorMessage.contains("验证码不正确")) {
				saveErrorResult(analyResult, imgStyle, errorCodeImgSaveDir);//保存解析错误的图片
				throw new LoginFailedException(LoginFailedTypeEnum.ERROR_CODE);//重试
			} else {
				throw new LoginFailedException(errorMessage);
			}
		} else {
			//登录成功
			//点击进入个人信息首页
			String mainResult = httpHandler.post("https://e.szsi.gov.cn/siservice/pIndex.jsp",
				new StringEntity("pid=" + getPid(loginResult), "gbk"), new BasicHeader(
					"Content-Type", "application/x-www-form-urlencoded"), new BasicHeader(
					"Referer", "https://e.szsi.gov.cn/siservice/LoginAction.do"));
			System.out.println(mainResult.length());
		}
	}
	
	/**
	 * 密码处理
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String encodePassword(String password) throws UnsupportedEncodingException {
		password = EncodeUtils.base64Encode(password.getBytes());
		return password;
	}
	
	/**
	 * 获取登录失败错误信息
	 * @param source
	 * @return
	 */
	private String getErrorMessage(String source) {
		return getByRegex(source, "<script language\\='JavaScript'>alert\\('(.*)'\\)</script>")
			.replace("<script language='JavaScript'>alert('", "").replace("')</script>", "");
	}
	
	/**
	 * 获取pid
	 * @param source
	 * @return
	 */
	private String getPid(String source) {
		String result = getByRegex(source,
			"<input\\stype=\"hidden\"\\sname=\"pid\"\\svalue=\"([0-9]*)\">");
		if (StringUtils.isBlank(result)) {
			throw new IllegalArgumentException("getPid无需要的数据->>" + source);
		}
		return result;
	}
	
	/**
	 * 通过正则截取字符串
	 * @param source
	 * @param pattern
	 * @return
	 */
	private String getByRegex(String source, String pattern) {
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
	
	/**
	 * 组装登录post请求参数
	 * @param type
	 * @param username 用户名/身份证
	 * @param password 密码
	 * @param pid
	 * @param code 验证码
	 * @return
	 * @throws Exception
	 */
	private List<NameValuePair> getLoginParams(String type, String username, String password,
												String pid, String code) throws Exception {
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		parms.add(new BasicNameValuePair("Method", "P"));
		parms.add(new BasicNameValuePair("pid", pid));
		parms.add(new BasicNameValuePair("type", ""));
		parms.add(new BasicNameValuePair("dlfs", type));
		parms.add(new BasicNameValuePair("AAC002", username));
		parms.add(new BasicNameValuePair("CAC222", encodePassword(password)));
		parms.add(new BasicNameValuePair("PSINPUT", code));
		return parms;
	}
	
	//	private static class LoginValidateCode extends ValidateCode {
	//		
	//		/**
	//		 * 构建一个<code>SocialSecurityLogin.java</code>
	//		 * @param templateList
	//		 */
	//		public LoginValidateCode(List<Template> templateList) {
	//			super(templateList);
	//		}
	//		
	//		/**
	//		 * 切割
	//		 * @param codeImg
	//		 * @return
	//		 * @see com.uaf.grabweb.base.validate.ValidateCode#getSubImgs(java.awt.image.BufferedImage)
	//		 */
	//		protected List<BufferedImage> getSubImgs(BufferedImage codeImg) {
	//			List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
	//			subImgs.add(codeImg.getSubimage(6, 4, 8, 12));//截取
	//			subImgs.add(codeImg.getSubimage(19, 4, 8, 12));
	//			subImgs.add(codeImg.getSubimage(32, 4, 8, 12));
	//			subImgs.add(codeImg.getSubimage(45, 4, 8, 12));
	//			return subImgs;
	//		}
	//		
	//		/**
	//		 * 根据截取的单个图片获取解析结果，规则的字符
	//		 * @param image
	//		 * @return
	//		 * @see com.uaf.grabweb.base.validate.ValidateCode#getResult(java.awt.image.BufferedImage)
	//		 */
	//		protected String getResult(BufferedImage image) {
	//			String result = null;
	//			double max = 0d;
	//			int width = image.getWidth();
	//			int height = image.getHeight();
	//			for (Template temp : super.templateList) {//模板长度需一致
	//				int count = 0;
	//				int[][] array = temp.getTempArray();
	//				for (int i = 0; i < width; i++) {
	//					for (int j = 0; j < height; j++) {
	//						if (array[j][i] == 0
	//							|| (ImageUtils.isWhite(image.getRGB(i, j), criticalValue))) {
	//							continue;
	//						}
	//						count++;
	//					}
	//				}
	//				double percent = (double) count / temp.getCorrectCount();
	//				if (max < percent) {
	//					max = percent;
	//					result = temp.getResult();
	//				}
	//			}
	//			return result;
	//		}
	//	}
	
}
