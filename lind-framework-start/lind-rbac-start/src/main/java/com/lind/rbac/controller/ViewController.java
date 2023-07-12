package com.lind.rbac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/")
	public String userList(Model model) {
		return "view/index";
	}

	/**
	 * 登录
	 */
	@GetMapping("login")
	public String login(Model model) {
		return "common/login";
	}

	@GetMapping("/xtsz/roleList")
	public String roleList(Model model) {
		return "view/index";
	}

}
