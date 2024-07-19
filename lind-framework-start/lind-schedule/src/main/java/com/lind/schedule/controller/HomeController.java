package com.lind.schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author lind
 * @date 2023/7/12 9:35
 * @since 1.0.0
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public RedirectView redirectToDemoIndex() {
		return new RedirectView("/quartz-view/list");
	}

}
