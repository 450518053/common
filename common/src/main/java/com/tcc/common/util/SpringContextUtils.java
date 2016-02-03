package com.tcc.common.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @Filename SpringContextUtils.java
 *
 * @Description ApplicationContext上下文工具类
 * 					
 		1.此类应注入spring容器
		2.需在web.xml作如下配置
		<listener>
			<listener-class>com.tcc.common.util.SpringContextUtils</listener-class>
		</listener>
		
 * @author tan 2016年1月27日
 *
 * @email 450518053@qq.com
 *
 */
public class SpringContextUtils implements ApplicationContextAware, ServletContextListener {
	
	private static ApplicationContext applicationContext;//Spring应用上下文环境
	
	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * @param applicationContext
	 * @throws BeansException
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	* 获取对象   
	* @param name bean注册名
	* @return Object 一个以所给名字注册的bean的实例
	* @throws BeansException
	*/
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}
	
	/**
	* 获取类型为requiredType的对象
	* 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	* @param name bean注册名
	* @param requiredType 返回对象类型
	* @return Object 返回requiredType类型对象
	* @throws BeansException
	*/
	public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}
	
	/**
	 * 获取类型为requiredType的对象
	 * @param requiredType 返回requiredType类型对象
	 * @return Object
	 * @throws BeansException
	 */
	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		return applicationContext.getBean(requiredType);
	}
	
	/**
	* 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true 
	* @param name
	* @return boolean
	*/
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}
	
	/**
	* 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	* 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）   
	* @param name
	* @return boolean
	* @throws NoSuchBeanDefinitionException
	*/
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(name);
	}
	
	/**
	* @param name
	* @return Class 注册对象的类型
	* @throws NoSuchBeanDefinitionException
	*/
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getType(name);
	}
	
	/**
	* 如果给定的bean名字在bean定义中有别名，则返回这些别名   
	* @param name
	* @return
	* @throws NoSuchBeanDefinitionException
	*/
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}
	
	/**
	 * @param arg0
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	
	}
	
	/**
	 * @param arg0
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
	
	}
	
}
