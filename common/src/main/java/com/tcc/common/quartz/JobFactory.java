package com.tcc.common.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**                    
 * @Filename JobFactory.java
 *
 * @Description 自定义job工厂类
 * 					为了解决spring接入quartz框架，
 * 						quartz的job无法注入spring容器托管的对象
 * 
	1.与spring整合
		{@code
			<!-- spring、quartz整合 -->
			<bean id="schedulerFactoryBean" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
				<property name="jobFactory">
		            <bean class="com.uaf.biz.schedule.JobFactory" />
		        </property>
			</bean>
		}
	2.需在xml配置{@link JobFactory}，属性注入AutowireCapableBeanFactory
	3.管理quartz的服务应注入quartz调度工厂
		{@code
		  	@Resource
			private Scheduler			scheduler;
		}
	4.处理类需继承{@link StatefulJobFactory}或{@link NoStatefulJobFactory}
		并重写 {@link #execute(JobExecutionContext)}
	
 * @see 参考：http://blog.arganzheng.me/posts/junit-and-spring-integration-ioc-autowire.html
 * 
 * @author tan 2015-8-20
 *
 */
public class JobFactory extends SpringBeanJobFactory implements Job {
	
	private AutowireCapableBeanFactory beanFactory;
	
	public AutowireCapableBeanFactory getBeanFactory() {
		return beanFactory;
	}
	
	public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	/**
	 * 覆盖super的createJobInstance方法
	 * 对其创建出来的类再进行autowire
	 * @param bundle
	 * @return
	 * @throws Exception
	 * @see org.springframework.scheduling.quartz.SpringBeanJobFactory#createJobInstance(org.quartz.spi.TriggerFiredBundle)
	 */
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		beanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
	
	/**
	 * @param arg0
	 * @throws JobExecutionException
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
	}
}
