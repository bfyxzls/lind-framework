package com.lind.admin.interceptor;

import com.lind.admin.exception.PermissionLimitException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleException(IllegalArgumentException e) {
		ModelAndView modelAndView = new ModelAndView("common/common.exception");
		modelAndView.addObject("errorMessage", "服务器处理出错!");
		return modelAndView;
	}

	@ExceptionHandler(PermissionLimitException.class)
	public ModelAndView myexcept(PermissionLimitException e, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("common/common.exception");
		modelAndView.addObject("errorMessage", e.getMessage());
		return modelAndView;
	}

}
