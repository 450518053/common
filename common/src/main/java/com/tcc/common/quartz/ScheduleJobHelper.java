package com.tcc.common.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * 
 * @Filename ScheduleJobHelper.java
 *
 * @Description 定时任务工具类
 *
 * @author tan 2016年1月26日
 *
 * @email 450518053@qq.com
 * 
 */
public class ScheduleJobHelper {
	
	/**
	 * 获取触发器key
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
		
		return TriggerKey.triggerKey(jobName, jobGroup);
	}
	
	/**
	 * 获取表达式触发器
	 * @param scheduler the scheduler
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @return cron trigger
	 * @throws SchedulerException 
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup)
																									throws Exception {
		
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
			return (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			throw new Exception("获取定时任务CronTrigger出现异常", e);
		}
	}
	
	/**
	 * 创建任务
	 * @param scheduler
	 * @param scheduleJob
	 * @throws Exception 
	 */
	public static void createScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob)
																						throws Exception {
		createScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup(),
			scheduleJob.getCronExpression(), scheduleJob.getClassName(), scheduleJob);
	}
	
	/**
	 * 创建定时任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @param cronExpression
	 * @param className
	 * @param param
	 * @throws Exception 
	 */
	public static void createScheduleJob(Scheduler scheduler, String jobName, String jobGroup,
											String cronExpression, String className, Object param)
																									throws Exception {
		try {
			Class<? extends Job> jobClass = Class.forName(className).asSubclass(Job.class);//若不是Job的子类会抛出异常
			//构建job信息
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup)
				.build();
			
			//表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			
			//按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
				.withSchedule(scheduleBuilder).build();
			
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			throw new Exception("创建定时任务失败", e);
		}
	}
	
	/**
	 * 运行一次任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @throws Exception 
	 */
	public static void runOnce(Scheduler scheduler, String jobName, String jobGroup)
																					throws Exception {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			throw new Exception("运行一次定时任务失败", e);
		}
	}
	
	/**
	 * 暂停任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @throws Exception 
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup)
																						throws Exception {
		
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			throw new Exception("暂停定时任务失败", e);
		}
	}
	
	/**
	 * 恢复任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @throws Exception 
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup)
																						throws Exception {
		
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			throw new Exception("暂停定时任务失败", e);
		}
	}
	
	/**
	 * 获取jobKey
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @return the job key
	 */
	public static JobKey getJobKey(String jobName, String jobGroup) {
		
		return JobKey.jobKey(jobName, jobGroup);
	}
	
	/**
	 * 更新定时任务
	 * @param scheduler the scheduler
	 * @param scheduleJob the schedule job
	 * @throws Exception 
	 */
	public static void updateScheduleJob(Scheduler scheduler, ScheduleJob scheduleJob)
																						throws Exception {
		updateScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup(),
			scheduleJob.getCronExpression(), scheduleJob.getClassName(), scheduleJob);
	}
	
	/**
	 * 更新定时任务
	 * @param scheduler the scheduler
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @param cronExpression the cron expression
	 * @param isSync the is sync
	 * @param param the param
	 * @throws Exception 
	 */
	public static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup,
											String cronExpression, String className, Object param)
																									throws Exception {
		
		//同步或异步
		//        Class<? extends Job> jobClass = isSync ? JobSyncFactory.class : JobFactory.class;
		
		try {
			//            JobDetail jobDetail = scheduler.getJobDetail(getJobKey(jobName, jobGroup));
			
			//            jobDetail = jobDetail.getJobBuilder().ofType(jobClass).build();
			
			//更新参数 实际测试中发现无法更新
			//            JobDataMap jobDataMap = jobDetail.getJobDataMap();
			//            jobDataMap.put(ScheduleJobVo.JOB_PARAM_KEY, param);
			//            jobDetail.getJobBuilder().usingJobData(jobDataMap);
			
			TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);
			
			//表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			
			//按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
				.withSchedule(scheduleBuilder).build();
			
			//按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			throw new Exception("更新定时任务失败", e);
		}
	}
	
	/**
	 * 删除定时任务
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 * @throws Exception 
	 */
	public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup)
																								throws Exception {
		try {
			scheduler.deleteJob(getJobKey(jobName, jobGroup));
		} catch (SchedulerException e) {
			throw new Exception("删除定时任务失败", e);
		}
	}
}
