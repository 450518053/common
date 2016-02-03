package com.tcc.common.quartz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcc.common.util.SpringContextUtils;

/**                    
 * @Filename QuartzContextListener.java
 *
 * @Description quartz关闭listener
 * 					保证tomcat关闭时，所有已开始的任务能执行完成（防止被tomcat强制关闭）
 * 	
		需在web.xml作如下配置
		<listener>
			<listener-class>com.tcc.common.quartz.QuartzContextListener</listener-class>
		</listener>
 *
 * @author tan 2016年1月27日
 *
 * @email 450518053@qq.com
 * 
 */
public class QuartzContextListener implements ServletContextListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @param arg0
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("关闭quartz调度中心开始");
		Scheduler scheduler = SpringContextUtils.getBean(Scheduler.class);
		if (scheduler != null) {
			try {
				long start = System.currentTimeMillis();
				scheduler.shutdown(true);
				logger.info("关闭quartz调度中心，等待所有已开始的任务执行完成");
				while (!scheduler.isShutdown()) {
					Thread.sleep(1000);
				}
				long end = System.currentTimeMillis();
				logger.info("quartz调度中心成功关闭，耗时'" + (end - start) + "'毫秒");
			} catch (Exception e) {
				logger.error("关闭quartz调度中心异常", e);
			}
		} else {
			logger.error("无法从容器中获取quartz调度中心");
		}
		
	}
	
	/**
	 * @param arg0
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
