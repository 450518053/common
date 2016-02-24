package com.tcc.common.webcrawl.base.factory;

import org.apache.http.Header;
import org.apache.http.protocol.BasicHttpContext;

import com.tcc.common.webcrawl.base.enums.HttpHandlerTypeEnum;
import com.tcc.common.webcrawl.base.http.HttpHandler;
import com.tcc.common.webcrawl.rh.http.RHHttpHandler;
import com.tcc.common.webcrawl.sz.http.SZHttpHandler;

/**                    
 * @Filename HttpHandlerFactory.java
 *
 * @Description HttpHandler工厂类
 *
 * @author tcc 2015-12-21
 *
 */
public class HttpHandlerFactory {
	
	private HttpHandlerFactory() {
	
	}
	
	/**
	 * 获得一个HttpHandler实体
	 * @param type
	 * @param headers
	 * @return
	 */
	public static HttpHandler create(HttpHandlerTypeEnum type, Header... headers) {
		HttpHandler handler = null;
		switch (type) {
			case SZ_ACCUMULATION_FUNDA:
			case SZ_SOCIAL_SECURITY:
			case SZ_UNIFIED_USER_PLATFORM:
				//TODO httpContext应遵循某种策略从缓存获取
				handler = new SZHttpHandler(new BasicHttpContext(), headers);
				break;
			case RH_PERRSONAL_CREDIT:
				handler = new RHHttpHandler(new BasicHttpContext(), headers);
				break;
			default:
				throw new IllegalArgumentException("无法识别的'" + type + "',具体请看HttpHandlerTypeEnum类");
		}
		return handler;
	}
	
}
