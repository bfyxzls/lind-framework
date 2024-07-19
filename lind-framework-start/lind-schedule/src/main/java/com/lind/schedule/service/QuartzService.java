package com.lind.schedule.service;

import com.lind.schedule.dto.JobDto;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface QuartzService {

	/**
	 * 创建job
	 * @param jobDto 任务类
	 */
	void addJob(JobDto jobDto);

	/**
	 * 创建job，可传参
	 * @param jobDto 任务类
	 * @param argMap map形式参数
	 */
	void addJob(JobDto jobDto, Map<String, Object> argMap);

	/**
	 * 暂停job
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	void pauseJob(String jobName, String jobGroupName);

	/**
	 * 恢复job
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	void resumeJob(String jobName, String jobGroupName);

	/**
	 * job 更新,只更新频率
	 */
	void updateJob(JobDto jobDto);

	/**
	 * job 更新,更新频率和参数
	 * @param argMap 参数
	 */
	void updateJob(JobDto jobDto, Map<String, Object> argMap);

	/**
	 * job 更新,只更新更新参数
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 * @param argMap 参数
	 */
	void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap);

	/**
	 * job 删除
	 * @param jobName 任务名称
	 * @param jobGroupName 任务所在组名称
	 */
	void deleteJob(String jobName, String jobGroupName);

	/**
	 * 启动所有定时任务
	 */
	void startAllJobs();

	/**
	 * 关闭所有定时任务
	 */
	void shutdownAllJobs();

	/**
	 * 获取所有任务列表
	 * @return
	 */
	List<JobDto> getAllJob();

	/**
	 * 通过jobname查询job
	 * @param jobName
	 * @return job的状态
	 */
	Trigger.TriggerState getJobByName(String jobName);

}
