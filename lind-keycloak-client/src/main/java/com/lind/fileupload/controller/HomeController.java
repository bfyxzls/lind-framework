package com.lind.fileupload.controller;

import com.lind.common.core.upload.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lind
 * @date 2023/12/25 15:20
 * @since 1.0.0
 */
@Controller
public class HomeController {

	@Autowired
	FileUploadUtils fileUploadUtils;

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

	@PostMapping("/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile bufferedImageMultipartFile) throws IOException {
		fileUploadUtils.upload("d:\\", bufferedImageMultipartFile);
		return "ok";
	}

	@GetMapping("wait")
	@ResponseBody
	public String waits() {
		try {
			Thread.sleep(5000);

		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "ok";
	}

}
