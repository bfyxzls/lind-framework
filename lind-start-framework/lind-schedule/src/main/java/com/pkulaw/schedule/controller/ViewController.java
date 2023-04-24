package com.pkulaw.schedule.controller;

import com.pkulaw.schedule.dto.JobDto;
import com.pkulaw.schedule.service.QuartzService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("quartz-view")
public class ViewController {

	@Autowired
	QuartzService quartzService;

	@GetMapping("add")
	public String add() {
		return "view/add";
	}

	@SneakyThrows
	@PostMapping("add")
	public void add(JobDto jobDto, HttpServletResponse response) {
		quartzService.addJob(jobDto);
		response.sendRedirect("/quartz-view/list");
	}

	@GetMapping("list")
	public String list(Model model) {
		List<JobDto> list = quartzService.getAllJob();
		model.addAttribute("result", list);
		return "view/list";
	}

	@SneakyThrows
	@GetMapping("start")
	public void start(String jobName, String groupName, HttpServletResponse response) {
		quartzService.resumeJob(jobName, groupName);
		response.sendRedirect("/quartz-view/list");
	}

	@SneakyThrows
	@GetMapping("stop")
	public void stop(String jobName, String groupName, HttpServletResponse response) {
		quartzService.pauseJob(jobName, groupName);
		response.sendRedirect("/quartz-view/list");
	}

}
