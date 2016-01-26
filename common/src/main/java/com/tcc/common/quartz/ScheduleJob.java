package com.tcc.common.quartz;

import java.io.Serializable;

/**
 * 
 * @Filename ScheduleJob.java
 *
 * @Description 计划任务信息 jobName+jobGroup唯一标识
 *
 * @author tan 2016年1月26日
 *
 * @email 450518053@qq.com
 * 
 */
public class ScheduleJob implements Serializable {
	
	private static final long	serialVersionUID	= 4888005949821878223L;
													
	/** 任务名称 */
	private String				jobName;
								
	/** 任务分组 */
	private String				jobGroup;
								
	/** 任务运行时间表达式 */
	private String				cronExpression;
								
	/** 处理的类名 */
	private String				className;
								
	private String				status;
								
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getJobGroup() {
		return jobGroup;
	}
	
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	public String getCronExpression() {
		return cronExpression;
	}
	
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((cronExpression == null) ? 0 : cronExpression.hashCode());
		result = prime * result + ((jobGroup == null) ? 0 : jobGroup.hashCode());
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleJob other = (ScheduleJob) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (cronExpression == null) {
			if (other.cronExpression != null)
				return false;
		} else if (!cronExpression.equals(other.cronExpression))
			return false;
		if (jobGroup == null) {
			if (other.jobGroup != null)
				return false;
		} else if (!jobGroup.equals(other.jobGroup))
			return false;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
}
