package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lind
 * @date 2023/12/25 15:20
 * @since 1.0.0
 */
@Controller
public class HomeController {

	@GetMapping("/")
	public String index(@RequestParam(required = false) String code, HttpServletResponse response) {
		if (code != null) {
			String[] arr = code.split("\\.");
			if (arr.length == 3) {
				Cookie cookie = new Cookie("session_state", arr[1]);
				cookie.setMaxAge(24 * 60 * 60); // 设置过期时间为一天
				cookie.setPath("/"); // 设置cookie路径
				cookie.setSecure(false);
				response.addCookie(cookie);
			}
		}
		return "index";
	}

}
