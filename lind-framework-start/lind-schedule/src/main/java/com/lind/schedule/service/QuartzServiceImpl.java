package com.lind.schedule.service;

import com.lind.schedule.dto.JobDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class QuartzServiceImpl implements QuartzService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private final Scheduler scheduler;

	/**
	 * 创建job
	 */
	@SneakyThrows
	public void addJob(JobDto jobDto) {
		addJob(jobDto, null);
	}

	/**
	 * 创建job，可传参
	 * @param jobDto 任务类
	 * @param argMap map形式参数
	 */
	@SneakyThrows
	public void addJob(JobDto jobDto, Map<String, Object> argMap) {
		String jobName = jobDto.getJobName();
		String groupName = jobDto.getGroupName();
		String cronExpression = jobDto.getCronExpression();
		// 启动调度器
		scheduler.start();
		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(((Class<? extends Job>) Class.forName(jobDto.getJobClassName())))
				.withIdentity(jobName, groupName).withDescription(jobDto.getDesc()).build();

		// 表达式调度构建器(即任务执行的时间)
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder)
				.build();
		// 获得JobDataMap，写入数据
		if (argMap != null) {
			trigger.getJobDataMap().putAll(argMap);
		}
		if (!StringUtils.isBlank(jobDto.getTime())) {
			trigger.getJobDataMap().put("time", jobDto.getTime());
		}
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * 暂停job
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	@SneakyThrows
	public void pauseJob(String jobName, String jobGroupName) {
		scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
	}

	/**
	 * 恢复job
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	@SneakyThrows
	public void resumeJob(String jobName, String jobGroupName) {
		scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
	}

	/**
	 * job 更新,只更新频率
	 */
	@SneakyThrows
	public void updateJob(JobDto jobDto) {
		updateJob(jobDto, null);
	}

	/**
	 * job 更新,更新频率和参数
	 * @param argMap 参数
	 */
	@SneakyThrows
	public void updateJob(JobDto jobDto, Map<String, Object> argMap) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobDto.getJobName(), jobDto.getGroupName());
		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobDto.getCronExpression());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 按新的cronExpression表达式重新构建trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
		// 修改map
		if (argMap != null) {
			trigger.getJobDataMap().putAll(argMap);
		}
		trigger.getJobDataMap().put("time", jobDto.getTime());
		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
	}

	/**
	 * job 更新,只更新更新参数
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 * @param argMap 参数
	 */
	@SneakyThrows
	public void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 修改map
		trigger.getJobDataMap().putAll(argMap);
		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);

	}

	/**
	 * job 删除
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	@SneakyThrows
	public void deleteJob(String jobName, String jobGroupName) {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
	}

	/**
	 * 启动所有定时任务
	 */
	@SneakyThrows
	public void startAllJobs() {
		scheduler.start();
	}

	/**
	 * 关闭所有定时任务
	 */
	@SneakyThrows
	public void shutdownAllJobs() {
		if (!scheduler.isShutdown()) {
			scheduler.shutdown();
		}
	}

	/**
	 * 获取所有任务列表
	 * @return
	 */
	@SneakyThrows
	public List<JobDto> getAllJob() {
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		List<JobDto> jobList = new ArrayList<>();
		Set<JobKey> jobKeys = null;
		jobKeys = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				JobDto jobDto = new JobDto();

				jobDto.setJobName(jobKey.getName());
				jobDto.setGroupName(jobKey.getGroup());
				JobDetail jobDetail = scheduler.getJobDetail(jobKey);
				jobDto.setJobClassName(jobDetail.getJobClass().getName());
				jobDto.setTriggerName(trigger.getKey().getName());
				jobDto.setTriggerGroupName(trigger.getKey().getGroup());
				jobDto.setDesc(jobDetail.getDescription());
				jobDto.setTime(trigger.getJobDataMap().getString("startTime"));

				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				jobDto.setStatus(triggerState.name());

				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					jobDto.setCronExpression(cronExpression);
				}
				jobList.add(jobDto);
			}
		}

		return jobList;
	}

	/**
	 * 通过jobname查询job
	 * @param jobName
	 * @return job的状态
	 */
	@SneakyThrows
	public Trigger.TriggerState getJobByName(String jobName) {
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = null;
		jobKeys = scheduler.getJobKeys(matcher);
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				if (jobKey.getName().equals(jobName)) {
					return triggerState;
				}
			}
		}
		return null;
	}

}
