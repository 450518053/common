package com.tcc.common.quartz;

import org.quartz.DisallowConcurrentExecution;

/**
 * 
 * @Filename StatefulJobFactory.java
 *
 * @Description 有状态job工厂类
 * 					保证多个任务间不会同时执行
 *
 * @author tan 2015-8-20
 *
 */
@DisallowConcurrentExecution
public class StatefulJobFactory extends JobFactory{
	
}
