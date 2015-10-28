package com.tcc.common.log;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.tcc.common.util.StringUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**                    
 * @Filename LogbackConfigListener.java
 *
 * @Description logback web容器listener
 * 
 * <h3>功能说明</h3>
 * <p/>
 * 1.加载logback配置文件
 * <p/>
 * 2.切换jul日志输出到logback
 * <p/>
 * <h3>Usage Examples</h3>
 * <li>1.使用默认logback日志路径</li>
 * 
 * <pre class="code">
 * {@code
 * 
 *	<listener>
 *		<listener-class>com.tcc.log.LogbackConfigListener</listener-class>
 *	</listener>
 *
 * }
 * 上面的配置使用默认路径WEB-INF/logback.xml
 * 
 * <li>2.使用非默认路径</li>
 * 如果使用其他路径请在在web.xml中加入配置：
 * 
 * <pre class="code">
 * {@code
 *	<context-param>
 *		<param-name>logbackConfigLocation</param-name>
 *		<param-value>WEB-INF/logback-xxx.xml</param-value>
 *	</context-param>
 * }
 * </pre>
 *
 * <li>3.使用classpath路径</li>
 * <pre class="code">
 * {@code
 *	<context-param>
 *		<param-name>logbackConfigLocation</param-name>
 *		<param-value>classpath:logback.xml</param-value>
 *	</context-param>
 * }
 * </pre>
 *
 * @author tan 2015年10月28日
 *
 * @email 450518053@qq.com
 *
 */
public class LogbackConfigListener implements ServletContextListener {
	
	/**
	 * logback.xml
	 */
	private static final String	CONFIG_LOCATION			= "logbackConfigLocation";
														
	/**
	 * 环境
	 */
	private static final String	ENV						= "spring.profiles.active";
														
	public static final String	PLACEHOLDER_PREFIX		= "${";
														
	public static final String	PLACEHOLDER_SUFFIX		= "}";
														
	public static final String	CLASSPATH_URL_PREFIX	= "classpath:";
														
	public static final String	DEFAULT_LOCATION		= "WEB-INF/logback.xml";
														
	/**
	 * @see http://bohr.me/
	 * @param event
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		//从web.xml中加载指定文件名的日志配置文件 ，如果为空，使用默认配置文件
		String logbackConfigLocation = event.getServletContext().getInitParameter(CONFIG_LOCATION);
		if (StringUtils.isBlank(logbackConfigLocation)) {
			logbackConfigLocation = DEFAULT_LOCATION;
		}
		//加载环境变量 ，如果为空，抛出异常
		String env = event.getServletContext().getInitParameter(ENV);
		if (StringUtils.isBlank(env)) {
			env = System.getProperty(ENV);
			if (StringUtils.isBlank(env)) {
				throw new RuntimeException("请配置环境变量'spring.profiles.active'");
			}
		}
		try {
			if (isClassPath(logbackConfigLocation)) {
				logbackConfigLocation = resolveClasspath(logbackConfigLocation);
			} else if (!isUrl(logbackConfigLocation)) {
				logbackConfigLocation = getRealPath(event.getServletContext(),
					logbackConfigLocation);
			}
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			loggerContext.putProperty(ENV, env);//放入，以便logback.xml能读取
			//logback日志组件替换log4j
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			//加载日志文件
			joranConfigurator.doConfigure(logbackConfigLocation);
			event.getServletContext().log("log4j日志输出到logback");
			event.getServletContext().log("加载logback日志文件：" + logbackConfigLocation);
			SLF4JBridgeHandler.install();
			event.getServletContext().log("jul日志输出到logback");
		} catch (Exception e) {
			event.getServletContext().log("加载logback配置文件[" + logbackConfigLocation + "]失败", e);
			throw new RuntimeException("加载logback配置文件[" + logbackConfigLocation + "]失败", e);
		}
		
		Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread
			.getDefaultUncaughtExceptionHandler();
		UncaughtExceptionHandlerWrapper uncaughtExceptionHandlerWrapper = new UncaughtExceptionHandlerWrapper(
			uncaughtExceptionHandler);
		Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandlerWrapper);
		event.getServletContext().log("设置默认线程未捕获异常处理器:" + uncaughtExceptionHandlerWrapper);
	}
	
	/**
	 * @param event
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
	
	}
	
	private String resolveClasspath(String resourceLocation) throws FileNotFoundException {
		String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
		URL url = getDefaultClassLoader().getResource(path);
		if (url == null) {
			throw new FileNotFoundException("没有找到logback配置文件:" + resourceLocation);
		}
		return url.getFile();
		
	}
	
	private ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			//ignore
		}
		if (cl == null) {
			cl = LogbackConfigListener.class.getClassLoader();
		}
		return cl;
	}
	
	private boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}
	
	private boolean isClassPath(String logbackConfigLocation) {
		return logbackConfigLocation.startsWith(CLASSPATH_URL_PREFIX);
	}
	
	private String getRealPath(	ServletContext servletContext,
								String path) throws FileNotFoundException {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		String realPath = servletContext.getRealPath(path);
		if (realPath == null) {
			throw new FileNotFoundException("ServletContext不能解析绝对路径的文件[" + path + "]");
		}
		return realPath;
	}
	
	/**
	 * 默认线程未捕获异常处理器
	 */
	private static class UncaughtExceptionHandlerWrapper	implements
															Thread.UncaughtExceptionHandler {
															
		private static Logger					logger	= LoggerFactory
			.getLogger(UncaughtExceptionHandlerWrapper.class);
			
		private Thread.UncaughtExceptionHandler	defaultUncaughtExceptionHandler;
												
		public UncaughtExceptionHandlerWrapper(Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler) {
			this.defaultUncaughtExceptionHandler = defaultUncaughtExceptionHandler;
		}
		
		/**
		 * @param t
		 * @param e
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		public void uncaughtException(Thread t, Throwable e) {
			logger.error("线程[{}]遇到没有捕获的异常", t.getName(), e);
			if (defaultUncaughtExceptionHandler != null) {
				defaultUncaughtExceptionHandler.uncaughtException(t, e);
			}
		}
	}
}
