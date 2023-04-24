package com.pkulaw.schedule.dynamic;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.util.Map;

/**
 * @author lind
 * @date 2023/3/2 14:00
 * @since 1.0.0
 */
public class DynamicJob {

	// 要执行类, 实现Job接口
	private Class<? extends Job> target;

	// cron 表达式
	private String cronExpression;

	private String jobGroup = Scheduler.DEFAULT_GROUP;

	// 必须唯一
	private String jobName;

	private transient TriggerKey triggerKey;

	private transient JobDetail jobDetail;

	// default
	public DynamicJob() {
	}

	public DynamicJob(String jobName) {
		this.jobName = jobName;
	}

	public Class<? extends Job> target() {
		return target;
	}

	public DynamicJob target(Class<? extends Job> target) {
		this.target = target;
		return this;
	}

	public DynamicJob cronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
		return this;
	}

	public String jobGroup() {
		return jobGroup;
	}

	public DynamicJob jobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
		return this;
	}

	public String jobName() {
		return jobName;
	}

	public DynamicJob jobName(String jobName) {
		this.jobName = jobName;
		return this;
	}

	public TriggerKey triggerKey() {
		if (triggerKey == null) {
			triggerKey = TriggerKey.triggerKey(this.jobName, this.jobGroup);
		}
		return triggerKey;
	}

	public JobDetail jobDetail() {
		if (jobDetail == null) {
			jobDetail = JobBuilder.newJob(target).withIdentity(this.jobName, this.jobGroup).build();
		}
		return jobDetail;
	}

	/*
	 * 传参数给 执行的 job 在job中 通过 context.getMergedJobDataMap().get(key) 获取值
	 */
	public DynamicJob addJobData(String key, Object value) {
		final JobDetail detail = jobDetail();
		final JobDataMap jobDataMap = detail.getJobDataMap();
		jobDataMap.put(key, value);
		return this;
	}

	/*
	 * 传参数给 执行的 job 在job中 通过 context.getMergedJobDataMap().get(key) 获取值
	 */
	public DynamicJob addJobDataMap(Map<String, Object> map) {
		final JobDetail detail = jobDetail();
		final JobDataMap jobDataMap = detail.getJobDataMap();
		jobDataMap.putAll(map);
		return this;
	}

	public String cronExpression() {
		return this.cronExpression;
	}

	public CronTrigger cronTrigger() {
		final CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(this.cronExpression);
		return TriggerBuilder.newTrigger().withIdentity(triggerKey()).withSchedule(cronScheduleBuilder).build();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{target=").append(target);
		sb.append(", cronExpression='").append(cronExpression).append('\'');
		sb.append(", jobGroup='").append(jobGroup).append('\'');
		sb.append(", jobName='").append(jobName).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
