package com.pkulaw.schedule.controller;

import com.pkulaw.schedule.dto.JobDto;
import com.pkulaw.schedule.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quartz")
public class QuartzController {

	@Autowired
	private QuartzService quartzService;

	/**
	 * 新增任务
	 */
	@GetMapping("/insert")
	public String insertTask(String job, String jName, String jGroup, String tName, String tGroup, String cron) {
		quartzService.addJob(JobDto.builder().jobClassName(job).jobName(jName).groupName(jGroup)
				.triggerGroupName(tGroup).triggerName(tName).cronExpression(cron).build());
		return "添加成功！";
	}

	/**
	 * 暂停任务
	 */
	@GetMapping("/pause")
	public String pauseTask(String jName, String jGroup) {
		quartzService.pauseJob(jName, jGroup);
		return "暂停成功！";
	}

	/**
	 * 继续任务
	 */
	@GetMapping("/resume")
	public String resumeTask(String jName, String jGroup) {
		quartzService.resumeJob(jName, jGroup);
		return "继续成功！";
	}

	/**
	 * 删除任务
	 */
	@GetMapping("/delete")
	public String deleteTask(String jName, String jGroup) {
		quartzService.deleteJob(jName, jGroup);
		return "删除成功！";
	}

	/**
	 * 任务列表
	 * @return
	 */
	@GetMapping("/list")
	public ResponseEntity listTask() {
		return ResponseEntity.ok(quartzService.getAllJob());
	}

}